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
import java.util.Properties;

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

import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.ir.NoIndexFoundException;
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
import edu.ur.ir.user.InviteInfoDAO;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.PersonalItem;
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
		
		File workspaceIndexFolder = new File(user.getPersonalIndexFolder());
		
		assert workspaceIndexFolder.exists() : "Workspace folder " + workspaceIndexFolder.getAbsolutePath() + " should exist";
		userService.deleteUser(user, user);
		assert !workspaceIndexFolder.exists() : "Workspace folder " + workspaceIndexFolder.getAbsolutePath() + " should NOT exist";
		
		
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
		userService.deleteUser(user, user);
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
		
		IdentifierType identifierType = new IdentifierType("identifierType");
		identifierTypeService.save(identifierType);
		
		LanguageType languageType = new LanguageType("languageType");
		languageTypeService.save(languageType);
		
		Series series = new Series("seriesName", "s1001");
		seriesService.saveSeries(series);
		
		Sponsor sponsor = new Sponsor("sponsor");
		sponsorService.save(sponsor);
		
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


		userService.deleteUser(user, user);
		
		contentTypeService.deleteContentType("contentType");
		seriesService.deleteSeries("seriesName");
		identifierTypeService.delete(identifierTypeService.get("identifierType"));
		languageTypeService.delete(languageTypeService.get("languageType"));
		sponsor = sponsorService.get("sponsor");
		sponsorService.delete(sponsor);
		publisherService.deletePublisher("publisher");
		
		helper.cleanUpRepository();
		tm.commit(ts);	
	}

}
