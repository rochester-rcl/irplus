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


package edu.ur.ir.item.service;

import java.io.File;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

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
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.item.OriginalItemCreationDate;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.util.FileUtil;

@Test(groups = { "baseTests" }, enabled = true)

public class DefaultItemServiceTest {

	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");
	
	/** Repository data access */
	RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
	
	/** User File System data access */
	UserFileSystemService userFileSystemService = (UserFileSystemService) ctx.getBean("userFileSystemService");
	
	/** Item services */
	ItemService itemService = (ItemService) ctx.getBean("itemService");

	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** User data access */
	UserPublishingFileSystemService userPublishingFileSystemService = 
		(UserPublishingFileSystemService) ctx.getBean("userPublishingFileSystemService");

	
	/**
	 * Test creating a user
	 * @throws DuplicateNameException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void deleteItemTest() throws DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
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
		File f1 = testUtil.creatFile(directory, "testFile1", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test1");
		File f2 = testUtil.creatFile(directory, "testFile2", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test1");
		
		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		IrFile irFile1 = repositoryService.createIrFile(repo, f1, "fileName", "description");
		IrFile irFile2 = repositoryService.createIrFile(repo, f2, "fileName", "description");
		tm.commit(ts);
		
		// new transaction
		ts = tm.getTransaction(td);
		PersonalFile pf = null;

		pf = userFileSystemService.addFileToUser(repo, f, user, 
		    		"test_file", "description");
		tm.commit(ts);
		IrFile irFile = pf.getVersionedFile().getCurrentVersion().getIrFile();
		
		// new transaction
		ts = tm.getTransaction(td);
		GenericItem item1 = new GenericItem("item1");
		item1.addFile(irFile2);
		itemService.makePersistent(item1);
		tm.commit(ts);
		
		// new transaction
		ts = tm.getTransaction(td);
		GenericItem item2 = new GenericItem("item2");
		item2.addFile(irFile);
		item2.addFile(irFile1);
		item2.addFile(irFile2);
		itemService.makePersistent(item2);
		tm.commit(ts);
     
        //new transaction
		ts = tm.getTransaction(td);
		GenericItem otherItem = itemService.getGenericItem(item2.getId(), false);
		itemService.deleteItem(otherItem);
		tm.commit(ts);
		
		 //new transaction
		ts = tm.getTransaction(td);
		// IrFile used by personal file
		assert repositoryService.getIrFile(irFile.getId(), false) != null : "IrFile should exist";
		// IrFile not used by others
		assert repositoryService.getIrFile(irFile1.getId(), false) == null : "IrFile1 should not exist";
		// IrFile used by other item
		assert repositoryService.getIrFile(irFile2.getId(), false) != null : "IrFile2 should exist";

		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		GenericItem otherItem1 = itemService.getGenericItem(item1.getId(), false);
		itemService.deleteItem(otherItem1);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert repositoryService.getIrFile(irFile2.getId(), false) == null : "IrFile2 should not exist";
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		PersonalFile pF = userFileSystemService.getPersonalFile(pf.getId(), false);
		userFileSystemService.delete(pF, pf.getOwner(), "user delete");
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert repositoryService.getIrFile(irFile.getId(), false) == null : "IrFile2 should not exist";
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		IrUser otherUser = userService.getUser(user.getUsername());
        userService.deleteUser(otherUser, otherUser);	
		
		helper.cleanUpRepository();
		tm.commit(ts);	
	}

	/**
	 * Test creating a user
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void deleteUnUsedIrFilesTest() throws IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
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
		File f1 = testUtil.creatFile(directory, "testFile1", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test1");
		File f2 = testUtil.creatFile(directory, "testFile2", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test1");
		
		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		IrFile irFile1 = repositoryService.createIrFile(repo, f1, "fileName", "description");
		IrFile irFile2 = repositoryService.createIrFile(repo, f2, "fileName", "description");
		tm.commit(ts);
		
		// new transaction
		ts = tm.getTransaction(td);
		PersonalFile pf = null;
		try
		{
		    pf = userFileSystemService.addFileToUser(repo, f, user, "test_file", "description");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		tm.commit(ts);
		IrFile irFile = pf.getVersionedFile().getCurrentVersion().getIrFile();
		
		// new transaction
		ts = tm.getTransaction(td);
		GenericItem item1 = new GenericItem("item1");
		item1.addFile(irFile2);
		itemService.makePersistent(item1);
		tm.commit(ts);
		
		//new transaction
		ts = tm.getTransaction(td);
		Set<Long> irFileIds = new HashSet<Long>();
		irFileIds.add(irFile.getId());
		irFileIds.add(irFile1.getId());
		irFileIds.add(irFile2.getId());
		
		itemService.deleteUnUsedIrFiles(irFileIds);
		
		tm.commit(ts);
		
		 //new transaction
		ts = tm.getTransaction(td);
		// IrFile used by personal file
		assert repositoryService.getIrFile(irFile.getId(), false) != null : "IrFile should exist";
		// IrFile not used by others
		assert repositoryService.getIrFile(irFile1.getId(), false) == null : "IrFile1 should not exist";
		// IrFile used by other item
		assert repositoryService.getIrFile(irFile2.getId(), false) != null : "IrFile2 should exist";

		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		GenericItem otherItem1 = itemService.getGenericItem(item1.getId(), false);
		itemService.deleteItem(otherItem1);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert repositoryService.getIrFile(irFile2.getId(), false) == null : "IrFile2 should not exist";
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		PersonalFile pF = userFileSystemService.getPersonalFile(pf.getId(), false);
		userFileSystemService.delete(pF, pf.getOwner(), "user delete");
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert repositoryService.getIrFile(irFile.getId(), false) == null : "IrFile2 should not exist";
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		IrUser otherUser = userService.getUser(user.getUsername());
        userService.deleteUser(otherUser, otherUser);	
		helper.cleanUpRepository();
		tm.commit(ts);	
	}

	/**
	 * Test creating the default groups
	 */
	public void createNewItemVersionTest() throws Exception
	{
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		
		PersonalCollection collection = userPublishingFileSystemService.createRootPersonalCollection(user, "collectionName", "description");
		userPublishingFileSystemService.makePersonalCollectionPersistent(collection);
		tm.commit(ts);
    
		// Start the transaction 
		ts = tm.getTransaction(td);
		
		PersonalItem personalItem = userPublishingFileSystemService.createPersonalItem(collection, user, "articles", "itemName");
		userPublishingFileSystemService.makePersonalItemPersistent(personalItem);
		
		tm.commit(ts);
		
		// Start the transaction 
		ts = tm.getTransaction(td);
		personalItem = userPublishingFileSystemService.getPersonalItem(personalItem.getId(), false);
		GenericItem clonedItem = personalItem.getVersionedItem().getCurrentVersion().getItem().clone();
		personalItem.getVersionedItem()
			.addNewVersion(clonedItem);
		
		userPublishingFileSystemService.makePersonalItemPersistent(personalItem);
		tm.commit(ts);

		assert personalItem.getVersionedItem().getLargestVersion() == 2 :"No. of versions should be 2";
		assert personalItem.getVersionedItem().getCurrentVersion().getItem().equals(clonedItem) : "The items should be equal";
		

		// Start the transaction 
		ts = tm.getTransaction(td);
		PersonalCollection pc = userPublishingFileSystemService.getPersonalCollection(collection.getId(), false);
		userPublishingFileSystemService.deletePersonalCollection(pc, userService.getUser(user.getUsername()), "TESTING");
		tm.commit(ts);
		
		// Start new transaction
		ts = tm.getTransaction(td);
		IrUser otherUser = userService.getUser(user.getUsername());
        userService.deleteUser(otherUser, otherUser);	
		tm.commit(ts);			

		
		
	}
	
	/**
	 *  Test creating item with date
	 */
	public void createItemWithDateTest() throws Exception
	{
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		GenericItem item = new GenericItem("name");
		OriginalItemCreationDate originalItemCreationDate = item.updateOriginalItemCreationDate(1, 12, 1990);
		itemService.makePersistent(item);
		
		tm.commit(ts);


		assert item.getOriginalItemCreationDate().equals(originalItemCreationDate) :"Release date should be equal";
		assert item.getOriginalItemCreationDate().getMonth() == 1 : "Moth equals " + item.getOriginalItemCreationDate().getMonth();
		assert item.getOriginalItemCreationDate().getDay() == 12 : "Day equals " + item.getOriginalItemCreationDate().getDay();
		assert item.getOriginalItemCreationDate().getYear() == 1990 : "Year equals " + item.getOriginalItemCreationDate().getYear();
		// Start the transaction 
		ts = tm.getTransaction(td);
		
		itemService.deleteItem(item);
		
		tm.commit(ts);
		
	}	
}
