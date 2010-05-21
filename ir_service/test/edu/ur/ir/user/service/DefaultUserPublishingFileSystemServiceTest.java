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
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeService;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeService;
import edu.ur.ir.item.ItemIdentifier;
import edu.ur.ir.item.ItemReport;
import edu.ur.ir.item.ItemSponsor;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeService;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.PublisherService;
import edu.ur.ir.item.Series;
import edu.ur.ir.item.SeriesService;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.SponsorService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.PersonalItemDeleteRecordDAO;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.util.FileUtil;


/**
 * Test the service methods for the default user publishing system services.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultUserPublishingFileSystemServiceTest {
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");

	/** User data access */
	UserPublishingFileSystemService userPublishingFileSystemService = 
		(UserPublishingFileSystemService) ctx.getBean("userPublishingFileSystemService");

	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	
	/** Repository data access */
	RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

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
	
	/** item data access object  */
	PersonalItemDeleteRecordDAO personalItemDeleteRecordDAO = (PersonalItemDeleteRecordDAO)ctx.getBean("personalItemDeleteRecordDAO");


	
	/**
	 * Test creating a personal item
	 * @throws UserHasPublishedDeleteException 
	 */
	public void testCreateRootPersonalItem() throws UserHasPublishedDeleteException, UserDeletedPublicationException
	{
		
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		UserEmail email = new UserEmail("email");

		IrUser user = userService.createUser("password", "username", email);
		tm.commit(ts);
		
		// Start the transaction
		ts = tm.getTransaction(td);
		PersonalItem personalItem = userPublishingFileSystemService.createRootPersonalItem(user, "articles", "rootCollection");
		tm.commit(ts);
		// save the root personal item
		
		// Start the transaction
		ts = tm.getTransaction(td);
		PersonalItem other = userPublishingFileSystemService.getPersonalItem(personalItem.getId(), false);
		assert other != null : "Should be able to find the persona file in the database";
		assert other.equals(personalItem) : " other " + other + " should equal the personal item " + personalItem; 
		assert other.getVersionedItem().getCurrentVersion().getItem().getLeadingNameArticles().equals("articles") : "Should equal articles but equals " + other.getVersionedItem().getCurrentVersion().getItem().getLeadingNameArticles();
		
		user.removeRootPersonalItem(other);
		tm.commit(ts);
		
		// Start new transaction
		ts = tm.getTransaction(td);
		IrUser deleteUser = userService.getUser(user.getId(), false);
		userService.deleteUser(deleteUser, deleteUser);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert userService.getUser(user.getId(), false) == null : "User should be null";
		personalItemDeleteRecordDAO.deleteAll();
		tm.commit(ts);
		
	}
	
	/**
	 * Test creating a personal item with item files
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void createItemWithFiles() throws IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
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
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		IrFile irFile1 = repositoryService.createIrFile(repo, f, "fileName", "description");
		tm.commit(ts);
		
		// new transaction
		ts = tm.getTransaction(td);
		PersonalItem personalItem = userPublishingFileSystemService.createRootPersonalItem(user, "articles", "rootCollection");
		GenericItem item1 = personalItem.getVersionedItem().getCurrentVersion().getItem();
		item1.addFile(irFile1);
		userPublishingFileSystemService.makePersonalItemPersistent(personalItem);
		tm.commit(ts);
		
        //new transaction
		ts = tm.getTransaction(td);
		PersonalItem otherPersonalItem = userPublishingFileSystemService.getPersonalItem(personalItem.getId(), false);
		assert otherPersonalItem.getVersionedItem().getCurrentVersion().getItem().equals(item1) : "Should be equal to item1";
		userPublishingFileSystemService.deletePersonalItem(otherPersonalItem, otherPersonalItem.getOwner(), "TESTING");
		tm.commit(ts);

	    // Start new transaction
		ts = tm.getTransaction(td);
		
		IrUser deleteUser = userService.getUser(user.getId(), false);
		userService.deleteUser(deleteUser, deleteUser);
		helper.cleanUpRepository();
		tm.commit(ts);	
	}


	/**
	 * Test creating a personal item with metadata
	 * @throws UserHasPublishedDeleteException 
	 */
	public void createItemWithMetadata() throws UserHasPublishedDeleteException, UserDeletedPublicationException
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);

		UserEmail email = new UserEmail("email");

		IrUser user = userService.createUser("password", "username", email);
		
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
		
		tm.commit(ts);
		
		// new transaction
		ts = tm.getTransaction(td);
		PersonalItem personalItem = userPublishingFileSystemService.createRootPersonalItem(user, "articles", "rootCollection");
		GenericItem item1 = personalItem.getVersionedItem().getCurrentVersion().getItem();
		item1.setName("item1");
		item1.setPrimaryContentType(contentType);
		ItemReport report = item1.addReport(series, "reportNumber");
		
		ItemIdentifier itemIdentifier = item1.addItemIdentifier("111", identifierType);
	
		item1.setItemAbstract("itemAbstract");
		item1.setItemKeywords("itemKeywords");
		
		ItemSponsor itemSponsor = item1.addItemSponsor(sponsor);
		
		item1.setLanguageType(languageType);
		
		ExternalPublishedItem externalPublishedItem = new ExternalPublishedItem();
		externalPublishedItem.setPublisher(publisher);
		externalPublishedItem.addPublishedDate(05,13,2008);
		externalPublishedItem.setCitation("citation");
		
		item1.setExternalPublishedItem(externalPublishedItem);
		
		userPublishingFileSystemService.makePersonalItemPersistent(personalItem);
		tm.commit(ts);
		
        //new transaction
		ts = tm.getTransaction(td);
		PersonalItem otherPersonalItem = userPublishingFileSystemService.getPersonalItem(personalItem.getId(), false);
		GenericItem otherItem = otherPersonalItem.getVersionedItem().getCurrentVersion().getItem();
		assert otherItem.equals(item1) : "Should be equal to item1";
		assert otherItem.getPrimaryContentType().equals(contentType) : "Content type should be equal";
		assert otherItem.getLanguageType().equals(languageType) : "Language type should be equal";
		
		assert otherItem.getItemIdentifiers().contains(itemIdentifier): "Identifier type should be equal";
		assert otherItem.getItemAbstract().equals("itemAbstract") : "Abstract should be equal";
		assert otherItem.getItemKeywords().equals("itemKeywords") : "Keywords should be equal";
		
		assert otherItem.getItemReports().contains(report) : "Report should be equal";
		assert otherItem.getItemSponsors().contains(itemSponsor): "Sponsor should be equal";
		assert otherItem.getExternalPublishedItem().equals(externalPublishedItem) : "External Published Item should be equal";

		userPublishingFileSystemService.deletePersonalItem(otherPersonalItem, otherPersonalItem.getOwner(), "TESTING");
		tm.commit(ts);

	    // Start new transaction
		ts = tm.getTransaction(td);
		
		IrUser deleteUser = userService.getUser(user.getId(), false);
		userService.deleteUser(deleteUser, deleteUser);

		contentTypeService.deleteContentType("contentType");
		seriesService.deleteSeries("seriesName");
		identifierTypeService.delete(identifierTypeService.get("identifierType"));
		languageTypeService.delete(languageTypeService.get("languageType"));
		sponsor = sponsorService.get("sponsor");
		sponsorService.delete(sponsor);
		publisherService.deletePublisher("publisher");

		personalItemDeleteRecordDAO.deleteAll();
		tm.commit(ts);	
	}

	 
	/**
	 * Test moving publications and collections to an existing collection
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void moveItemCollectionTest() throws UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
	{
		
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		UserEmail email = new UserEmail("email");

		IrUser user = userService.createUser("password", "username", email);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		// create the first file to store in the temporary collection
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		tm.commit(ts);
		
		// new transaction - create two new collections
		ts = tm.getTransaction(td);
		
		PersonalCollection myCollection = null;
		PersonalCollection destination = null;
		
		try
		{
		    myCollection = userPublishingFileSystemService.createRootPersonalCollection(user, "myCollection1" , "desc1");
		    assert myCollection != null : "collection should be created";
		    
		    destination = userPublishingFileSystemService.createRootPersonalCollection(user, "myCollection2", "desc2");
	
	        assert destination != null : "collection should be created";
		}
		catch(Exception e)
		{
		    
		}
		
		
		assert destination != null : "collection should be created";
		assert user.getRootPersonalCollection(destination.getName()) != null : 
			"Should be able to find collection " + destination;
		
		PersonalItem it = null;
		
		try
		{
		    it = userPublishingFileSystemService.createPersonalItem(myCollection, user, "articles", "item1");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		tm.commit(ts);
        
		// start new transaction
        ts = tm.getTransaction(td);
        // make sure you can get the list of personal items for a user
        List<Long> itemIds = new LinkedList<Long>();
        itemIds.add(it.getId());
        userPublishingFileSystemService.getPersonalItems(user.getId(), itemIds);
        tm.commit(ts);
		
		
		// start new transaction
        ts = tm.getTransaction(td);
        
        assert it.getId() != null : "Personal publication should not have a null id " + it;
        List<PersonalCollection> collectionsToMove = new LinkedList<PersonalCollection>();
        collectionsToMove.add(myCollection);
        
        try
        {
        	userPublishingFileSystemService.moveCollectionSystemInformation(destination, 
        		collectionsToMove, null);
        }
        catch(Exception e)
        {
        	throw new IllegalStateException(e);
        }
        
        
        
        tm.commit(ts);
     
        //new transaction
        // make sure the collection was moved.
		ts = tm.getTransaction(td);
		IrUser otherUser2 = userService.getUser(user.getUsername());
		PersonalCollection theDestination = user.getRootPersonalCollection(destination.getName());
		PersonalCollection newChild = theDestination.getChild(myCollection.getName());
		
		assert  newChild != null : "Was not able to find " + myCollection
		+ " in children of " +
		theDestination ;
		
		assert newChild.getPersonalItem(it.getName()) != null : "Item: " + it.getName() 
		+ "was not found ";
		
		// move the item now to the collection above
		List<PersonalItem> itemsToMove = new LinkedList<PersonalItem>();
        itemsToMove.add(it);
        
        try
        {
        	userPublishingFileSystemService.moveCollectionSystemInformation(destination, 
				null, itemsToMove);
        }
        catch(Exception e)
        {
        	throw new IllegalStateException(e);
        }
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		IrUser deleteUser = userService.getUser(user.getId(), false);
		userService.deleteUser(deleteUser, deleteUser);
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		assert userService.getUser(otherUser2.getId(), false) == null : "User should be null"; 
		assert userService.getUser(user.getId(), false) == null : "User should be null";
		helper.cleanUpRepository();
		tm.commit(ts);	
		
	}
	
	/**
	 * Test moving files and collections to the root (the User)
	 * @throws DuplicateNameException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void moveItemCollectionToRootTest() throws DuplicateNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
	{
		
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		UserEmail email = new UserEmail("email");

		IrUser user = userService.createUser("password", "username", email);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		// create the first file to store in the temporary collection
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		tm.commit(ts);
		
		// new transaction - create two new collections
		ts = tm.getTransaction(td);
		
		PersonalCollection myCollection = null;
		PersonalCollection subCollection = null;
		try
		{
		    myCollection = userPublishingFileSystemService.createRootPersonalCollection(user, "myCollection1", "desc1");
		    subCollection = myCollection.createChild("subCollection");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		userPublishingFileSystemService.makePersonalCollectionPersistent(myCollection);
		
		assert myCollection != null : "collection should be created";
		
		// create a sub collection
		assert subCollection != null : "collection should be created";
		
		PersonalItem it = null;
		try
		{
		    it = userPublishingFileSystemService.createPersonalItem(myCollection, user, "articles", "item1");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		tm.commit(ts);
         
		// start new transaction
        ts = tm.getTransaction(td);
        assert it.getId() != null : "Personal item should not have a null id " + it;
        List<PersonalCollection> collectionsToMove = new LinkedList<PersonalCollection>();
        collectionsToMove.add(subCollection);
        userPublishingFileSystemService.moveCollectionSystemInformation(userService.getUser(user.getId(), false), 
        		collectionsToMove, null);
        tm.commit(ts);

        // start new transaction
        ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		PersonalCollection newRoot = user.getRootPersonalCollection(subCollection.getName());
		assert newRoot != null : "Should be able to find the collection " + subCollection;
		assert newRoot.getId().equals(subCollection.getId()) : "collection id's should be the same newRoot = " + newRoot
		+ " subCollection = " + subCollection;
        tm.commit(ts);
 
        ts = tm.getTransaction(td);
		PersonalCollection oldParent = userPublishingFileSystemService.getPersonalCollection(myCollection.getId(), false);
		assert oldParent.getChildren().size() == 0 : "Should not have any children but has " +
		oldParent.getChildren().size();
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		// move the item now to the collection above
		List<PersonalItem> itemsToMove = new LinkedList<PersonalItem>();
		itemsToMove.add(userPublishingFileSystemService.getPersonalItem(it.getId(),false));
		userPublishingFileSystemService.moveCollectionSystemInformation(userService.getUser(user.getId(), false), 
				null, itemsToMove);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		// make sure the item has been moved
		it = userPublishingFileSystemService.getPersonalItem(it.getId(), false);
		assert it.getPersonalCollection() == null : "PersonalItem should no longer have a parent collection " + 
		it.getPersonalCollection();
		user = userService.getUser(user.getId(), false);
		assert user.getRootPersonalItem(it.getName()) != null : "Should be able to find the item";
		tm.commit(ts);
		
		 // Start new transaction
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		userService.deleteUser(user, user);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert userService.getUser(user.getId(), false) == null : "User should be null"; 
		helper.cleanUpRepository();
		tm.commit(ts);
	}
	

}
