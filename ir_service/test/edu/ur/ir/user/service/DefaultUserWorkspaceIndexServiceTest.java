/**  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  


package edu.ur.ir.user.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.file.FileCollaborator;
import edu.ur.ir.file.FileVersion;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeService;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeService;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeService;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.PublisherService;
import edu.ur.ir.item.Series;
import edu.ur.ir.item.SeriesService;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.SponsorService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.FileSharingException;
import edu.ur.ir.user.InviteInfo;
import edu.ur.ir.user.InviteInfoDAO;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.util.FileUtil;

/**
 * Test the service methods for the indexing user workspace information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultUserWorkspaceIndexServiceTest {
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultUserWorkspaceIndexServiceTest.class);
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");

	/** User indexing service */
	DefaultUserWorkspaceIndexService userWorkspaceIndexService = (DefaultUserWorkspaceIndexService) ctx.getBean("userWorkspaceIndexService");

	/** User file system data access */
	UserFileSystemService userFileSystemService = 
		(UserFileSystemService) ctx.getBean("userFileSystemService");
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** unique name generator */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) ctx.getBean("uniqueNameGenerator");
	
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/**Service to invite users */
	InviteUserService inviteUserService = (InviteUserService) ctx
	.getBean("inviteUserService");
	
	/** versioned file data access object */
    VersionedFileDAO versionedFileDAO= (VersionedFileDAO) ctx
	.getBean("versionedFileDAO");
    
    /** invite information data access object */
    InviteInfoDAO inviteInfoDAO= (InviteInfoDAO) ctx
 	.getBean("inviteInfoDAO");
    
	/** Item services */
	ContentTypeService contentTypeService = (ContentTypeService) ctx.getBean("contentTypeService");
	
	/** Series services */
	SeriesService seriesService = (SeriesService) ctx.getBean("seriesService");
	
	/** Language type services */
	LanguageTypeService languageTypeService = (LanguageTypeService) ctx.getBean("languageTypeService");
	
	/** Identifier type services */
	IdentifierTypeService identifierTypeService = (IdentifierTypeService) ctx.getBean("identifierTypeService");
	
	/** Sponsor services */
	SponsorService sponsorService = (SponsorService) ctx.getBean("sponsorService");
	
	/** Publisher services */
	PublisherService publisherService = (PublisherService) ctx.getBean("publisherService");
	
	/** User data access */
	UserPublishingFileSystemService userPublishingFileSystemService = 
		(UserPublishingFileSystemService) ctx.getBean("userPublishingFileSystemService");

	
	/**
	 * Executes the query returning the number of hits.
	 * 
	 * @param field - field to search on
	 * @param queryString - query string
	 * @param dir - lucene index to search
	 * 
	 * @return - number of hits
	 * 
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 */
	private int executeQuery(String field, String queryString, Directory dir)
			throws CorruptIndexException, IOException, ParseException {
		IndexSearcher searcher = new IndexSearcher(dir);
		QueryParser parser = new QueryParser(field, new StandardAnalyzer());
		Query q1 = parser.parse(queryString);
		TopDocs hits = searcher.search(q1, 1000);
		int hitCount = hits.totalHits;

		searcher.close();

		return hitCount;
	}
	
	/**
	 * Test indexing a personal file - which may have multiple versions 
	 * in it.
	 * @throws NoIndexFoundException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws IOException 
	 */
	public void testIndexPersonalFile() throws NoIndexFoundException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, IOException 
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);
		
		UserEmail email = new UserEmail("email");

        // Start the transaction 
		ts = tm.getTransaction(td);
		IrUser user = userService.createUser("password", "username", email);
		String name = uniqueNameGenerator.getNextName();
		userFileSystemService.createIndexFolder(user, repo, name);
		assert user.getId() != null : "User id should not be null";
		tm.commit(ts);
		
        //new transaction - add a file to a user
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		// add the first file
		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		PersonalFile personalFile = null;
		try
		{
		    personalFile = userFileSystemService.addFileToUser(repo, 
				f, 
				user, 
				"test_file.txt", 
				"description");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
	
		
		userWorkspaceIndexService.addToIndex(repo, personalFile);
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		
		VersionedFile versionedFile = personalFile.getVersionedFile();
		FileVersion currentVersion = versionedFile.getCurrentVersion();
		
		Directory lucenDirectory;
		try {
			lucenDirectory = FSDirectory.getDirectory(user.getPersonalIndexFolder());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultUserWorkspaceIndexService.FULL_VERSIONED_FILE_NAME, 
					versionedFile.getNameWithExtension(), 
					lucenDirectory);

			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultUserWorkspaceIndexService.FULL_VERSIONED_FILE_NAME + " " 
			+ versionedFile.getNameWithExtension();
			
			hits = executeQuery(DefaultUserWorkspaceIndexService.FILE_VERSION_CREATOR, 
					currentVersion.getVersionCreator().getUsername(), 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultUserWorkspaceIndexService.FILE_VERSION_CREATOR + " " + currentVersion.getVersionCreator().getUsername();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		userWorkspaceIndexService.deleteFileFromIndex(personalFile.getOwner(), personalFile.getId());
		
		try {
			lucenDirectory = FSDirectory.getDirectory(user.getPersonalIndexFolder());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultUserWorkspaceIndexService.FULL_VERSIONED_FILE_NAME, 
					versionedFile.getNameWithExtension(), 
					lucenDirectory);

			assert hits == 0 : "Hit count should equal 0 but equals " + hits 
			+ " for finding " + DefaultUserWorkspaceIndexService.FULL_VERSIONED_FILE_NAME + " " 
			+ versionedFile.getNameWithExtension();
			
			hits = executeQuery(DefaultUserWorkspaceIndexService.FILE_VERSION_CREATOR, 
					currentVersion.getVersionCreator().getUsername(), 
					lucenDirectory);
			
			assert hits == 0 : "Hit count should equal 0 but equals " + hits 
			+ " for finding " + DefaultUserWorkspaceIndexService.FILE_VERSION_CREATOR + 
			" " + currentVersion.getVersionCreator().getUsername();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		userService.deleteUser(user);
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test indexing a personal folder
	 *
	 * @throws NoIndexFoundException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws IOException 
	 */
	public void testIndexPersonalFolder() throws NoIndexFoundException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, IOException 
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);
		
		UserEmail email = new UserEmail("email");

        // Start the transaction 
		ts = tm.getTransaction(td);
		IrUser user = userService.createUser("password", "username", email);
		String name = uniqueNameGenerator.getNextName();
		userFileSystemService.createIndexFolder(user, repo, name);
		assert user.getId() != null : "User id should not be null";
		tm.commit(ts);
		
        //new transaction - add a file to a user
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		
		PersonalFolder personalFolder = null;
		try
		{
		    personalFolder =  userFileSystemService.createNewFolder(user, "testFolder");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		userWorkspaceIndexService.addToIndex(repo, personalFolder);
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		
		
		Directory lucenDirectory;
		try {
			lucenDirectory = FSDirectory.getDirectory(user.getPersonalIndexFolder());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultUserWorkspaceIndexService.PERSONAL_FOLDER_NAME, 
					personalFolder.getName(), 
					lucenDirectory);

			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultUserWorkspaceIndexService.PERSONAL_FOLDER_NAME + " "
			+ personalFolder.getName();
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		userWorkspaceIndexService.deleteFolderFromIndex(personalFolder.getOwner(), personalFolder.getId());
		
		try {
			lucenDirectory = FSDirectory.getDirectory(user.getPersonalIndexFolder());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultUserWorkspaceIndexService.PERSONAL_FOLDER_NAME, 
					personalFolder.getName(), 
					lucenDirectory);

			assert hits == 0 : "Hit count should equal 0 but equals " + hits 
			+ " for finding " + DefaultUserWorkspaceIndexService.PERSONAL_FOLDER_NAME + " " + personalFolder.getName();
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		userService.deleteUser(user);
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test sharing and indexing a personal file - which has been shared with another user
	 * but not yet moved into the users files and folders.
	 * 
	 * @throws NoIndexFoundException 
	 * @throws DuplicateNameException 
	 * @throws FileSharingException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws IOException 
	 */
	public void testIndexSharedFileInUserInbox() throws NoIndexFoundException,
		DuplicateNameException, FileSharingException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, IOException 
	{
		// determine if we should be sending emails 
		boolean sendEmail = new Boolean(properties.getProperty("send_emails")).booleanValue();
		
		log.debug("test testIndexSharedFileInUserInbox send email = " + sendEmail);

		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		String userEmail1 = properties.getProperty("user_1_email").trim();
		UserEmail email = new UserEmail(userEmail1);
		IrUser user = userService.createUser("password", "username", email);
		
		
		String userEmail2 = properties.getProperty("user_2_email").trim();
		UserEmail email1 = new UserEmail(userEmail2);
		IrUser user1 = userService.createUser("password1", "username1", email1);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile.txt", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		PersonalFile pf = null;
		
		pf = userFileSystemService.addFileToUser(repo, f, user, 
				    "test_file.txt", "description");
		
        tm.commit(ts);

		//Start a transaction 
		ts = tm.getTransaction(td);
        VersionedFile vf = pf.getVersionedFile();
		// Share the file with other user
		SharedInboxFile inboxFile = inviteUserService.shareFile(user, user1, vf);
		tm.commit(ts);
		
		
		//Start a transaction - make sure the file exists
		ts = tm.getTransaction(td);
		VersionedFile otherVf = versionedFileDAO.getById(vf.getId(), false);
		IrUser u = userService.getUser(user1.getUsername());
		FileCollaborator fCollaborator = otherVf.getCollaborator(u);
		assert u.getSharedInboxFile(otherVf) != null : "Versioned file should exist should be in shared inbox";
		assert u.getSharedInboxFile(inboxFile.getId()).getVersionedFile().equals(otherVf) : "Versioned file should exist in the other user root";
		assert fCollaborator.getCollaborator().equals(u) : "Should be equal to user : u";
		
		tm.commit(ts);

		/********* Test Indexing the file ****************/
		//Start a transaction 
		ts = tm.getTransaction(td);

		//reload the repository and personal file
	    repo = helper.getRepository();
	    pf = userFileSystemService.getPersonalFile(pf.getId(), false);
		userWorkspaceIndexService.updateAllIndexes(repo, pf);
		
		// reload the users
		user = pf.getOwner();
		user1 = userService.getUser(user1.getUsername());
		
		Directory lucenDirectory;
		try {
			lucenDirectory = FSDirectory.getDirectory(user.getPersonalIndexFolder());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultUserWorkspaceIndexService.FILE_BODY_TEXT, 
					"Hello", 
					lucenDirectory);

			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultUserWorkspaceIndexService.FILE_BODY_TEXT;
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		// test finding in shared inbox file
		try {
			lucenDirectory = FSDirectory.getDirectory(user1.getPersonalIndexFolder());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultUserWorkspaceIndexService.FILE_BODY_TEXT, 
					"Hello", 
					lucenDirectory);

			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultUserWorkspaceIndexService.FILE_BODY_TEXT;
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// remove the indexes
		
		userWorkspaceIndexService.deleteFromAllIndexes(pf);
		
		try {
			lucenDirectory = FSDirectory.getDirectory(user.getPersonalIndexFolder());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultUserWorkspaceIndexService.FILE_BODY_TEXT, 
					"Hello", 
					lucenDirectory);

			assert hits == 0 : "Hit count should equal 0 but equals " + hits 
			+ " for finding " + DefaultUserWorkspaceIndexService.FILE_BODY_TEXT;
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		// test finding in shared inbox file
		try {
			lucenDirectory = FSDirectory.getDirectory(user1.getPersonalIndexFolder());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultUserWorkspaceIndexService.FILE_BODY_TEXT, 
					"Hello", 
					lucenDirectory);

			assert hits == 0 : "Hit count should equal 0 but equals " + hits 
			+ " for finding " + DefaultUserWorkspaceIndexService.FILE_BODY_TEXT;
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		tm.commit(ts);
		/********* Done Test Indexing the file ****************/
		
		//Start a transaction 
		ts = tm.getTransaction(td);
		List<IrUser> userList = new ArrayList<IrUser>();
		userList.add(userService.getUser(user1.getUsername()));
		otherVf = versionedFileDAO.getById(vf.getId(), false);
		fCollaborator = otherVf.getCollaborator(u);
		
		// get email information
		String ownerEmail = fCollaborator.getVersionedFile().getOwner().getDefaultEmail().getEmail();
		String collaboratorEmail = fCollaborator.getCollaborator().getDefaultEmail().getEmail();
		String fileName = fCollaborator.getVersionedFile().getName();
		
		// UnShare the file 		
		assert fCollaborator.getVersionedFile().getOwner().getUsername() != null : "Owner of the versioned file is null " + fCollaborator.getVersionedFile();
		inviteUserService.unshareFile(fCollaborator);
		
		if( sendEmail )
		{
			log.debug("Sending email to ownerEmail " + ownerEmail);
			inviteUserService.sendUnshareEmail(ownerEmail, collaboratorEmail, fileName);
		}
		
		IrUser u1 = userService.getUser(user1.getUsername());
		VersionedFile otherVf1 = versionedFileDAO.getById(vf.getId(), false);		
		assert u1.getRootFile("myname") == null : " The file should be unshared from the user";
		assert otherVf1.getCollaborators().size() == 0 :"Collaborator size should be 0";
	
		tm.commit(ts);

		// Test InviteInfo persistence
		ts = tm.getTransaction(td);
		String strEmail = properties.getProperty("user_1_email").trim();
		user = userService.getUser(user.getUsername());
		vf = versionedFileDAO.getById(vf.getId(), false);
		InviteInfo t = new InviteInfo(user, vf);
		t.setEmail(strEmail);
		t.setToken("token");
		t.setInviteMessage("inviteMessage");
		inviteUserService.makeInviteInfoPersistent(t);
		
		tm.commit(ts);

		// Start a transaction 
		ts = tm.getTransaction(td);

		InviteInfo otherToken = inviteInfoDAO.getById(t.getId(), false);
		
		assert otherToken.getEmail().equals(strEmail) : "Email should be equal strEmail = " + strEmail + " other email = " + otherToken.getEmail();
		assert otherToken.getToken().equals("token"): "Token should be equal";
		assert otherToken.getUser().equals(user) : "User should be equal";
		assert otherToken.getFiles().contains(vf) :"Versioned file should be equal";

		tm.commit(ts);

		// Start a transaction 
		ts = tm.getTransaction(td);

		if(sendEmail)
		{
		    inviteUserService.sendEmailToExistingUser(t);
		}

		tm.commit(ts);
		
		// Start a transaction - clean up data
		ts = tm.getTransaction(td);
		inviteInfoDAO.makeTransient(otherToken);

		tm.commit(ts);
		
		// Start a transaction 
		ts = tm.getTransaction(td);
		userService.deleteUser(userService.getUser(user.getId(), false));
		userService.deleteUser(userService.getUser(user1.getId(), false));
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		assert userService.getUser(user1.getId(), false) == null : "User should be null"; 
		assert userService.getUser(user.getId(), false) == null : "User should be null";
		helper.cleanUpRepository();
		tm.commit(ts);	 
	}
	
	/**
	 * Test indexing a personal item
	 *
	 * @throws NoIndexFoundException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws IOException 
	 */
	public void testIndexPersonalItem() throws NoIndexFoundException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, IOException 
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);
		
		UserEmail email = new UserEmail("email");

        // Start the transaction 
		ts = tm.getTransaction(td);
		IrUser user = userService.createUser("password", "username", email);
		String name = uniqueNameGenerator.getNextName();
		userFileSystemService.createIndexFolder(user, repo, name);
		assert user.getId() != null : "User id should not be null";
		tm.commit(ts);
		
        //new transaction - add a file to a user
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		
		ContentType contentType = new ContentType("contentType");
		contentTypeService.saveContentType(contentType);
		
		IdentifierType identifierType = new IdentifierType();
		identifierType.setName("identifierType");
		identifierTypeService.save(identifierType);
		
		LanguageType languageType = new LanguageType("languageType");
		languageTypeService.save(languageType);
		
		Series series = new Series("seriesName", "s1001");
		seriesService.saveSeries(series);
		
		Sponsor sponsor = new Sponsor("sponsor");
		sponsorService.saveSponsor(sponsor);
		
		Publisher publisher = new Publisher("publisher");
		publisherService.savePublisher(publisher);
		
		PersonalItem personalItem = userPublishingFileSystemService.createRootPersonalItem(user, "articles", "rootCollection");
		GenericItem item1 = personalItem.getVersionedItem().getCurrentVersion().getItem();
		item1.setName("item1");
		item1.setPrimaryContentType(contentType);
		item1.addReport(series, "reportNumber");
		
		item1.addItemIdentifier("111", identifierType);
	
		item1.setItemAbstract("itemAbstract");
		item1.setItemKeywords("itemKeywords");
		
		item1.addItemSponsor(sponsor);
		
		item1.setLanguageType(languageType);
		
		ExternalPublishedItem externalPublishedItem = new ExternalPublishedItem();
		externalPublishedItem.setPublisher(publisher);
		externalPublishedItem.addPublishedDate(05,13,2008);
		externalPublishedItem.setCitation("citation");
		
		item1.setExternalPublishedItem(externalPublishedItem);
		
		userPublishingFileSystemService.makePersonalItemPersistent(personalItem);
	
		
		userWorkspaceIndexService.addToIndex(repo, personalItem);
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		
		
		Directory lucenDirectory;
		try {
			lucenDirectory = FSDirectory.getDirectory(user.getPersonalIndexFolder());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultUserWorkspaceIndexService.PERSONAL_ITEM_NAME, 
					personalItem.getName(), 
					lucenDirectory);

			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultUserWorkspaceIndexService.PERSONAL_ITEM_NAME + " "
			+ personalItem.getName();
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		userWorkspaceIndexService.deleteItemFromIndex(personalItem.getOwner(), personalItem.getId());
		
		try {
			lucenDirectory = FSDirectory.getDirectory(user.getPersonalIndexFolder());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultUserWorkspaceIndexService.PERSONAL_ITEM_NAME, 
					personalItem.getName(), 
					lucenDirectory);

			assert hits == 0 : "Hit count should equal 0 but equals " + hits 
			+ " for finding " + DefaultUserWorkspaceIndexService.PERSONAL_ITEM_NAME + " " + personalItem.getName();
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		userPublishingFileSystemService.deletePersonalItem(userPublishingFileSystemService.getPersonalItem(personalItem.getId(), false));

		userService.deleteUser(user);
		
		contentTypeService.deleteContentType("contentType");
		seriesService.deleteSeries("seriesName");
		identifierTypeService.delete(identifierTypeService.get("identifierType"));
		languageTypeService.delete(languageTypeService.get("languageType"));
		sponsorService.deleteSponsor("sponsor");
		publisherService.deletePublisher("publisher");
		
		helper.cleanUpRepository();
		tm.commit(ts);	
	}

}
