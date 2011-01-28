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


package edu.ur.ir.institution.service;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.handle.HandleNameAuthority;
import edu.ur.ir.handle.HandleService;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.CollectionDoesNotAcceptItemsException;
import edu.ur.ir.institution.DeletedInstitutionalItemService;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSecurityService;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecord;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.repository.LicenseService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryLicenseNotAcceptedException;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.VersionedLicense;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserGroupService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserRepositoryLicense;
import edu.ur.ir.user.UserService;
import edu.ur.util.FileUtil;

/**
 * Tests for the Institutional collection service.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultInstitutionalCollectionServiceTest {
	
	/** Spring application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
    /** Repository service  */
    RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
    
    /** Collection security service  */
    InstitutionalCollectionSecurityService institutionalCollectionSecurityService = 
    	(InstitutionalCollectionSecurityService) ctx.getBean("institutionalCollectionSecurityService");
   
    /** Collection service  */
    InstitutionalCollectionService institutionalCollectionService = 
    	(InstitutionalCollectionService) ctx.getBean("institutionalCollectionService");

    /** Institutional Item service  */
    InstitutionalItemService institutionalItemService = 
    	(InstitutionalItemService) ctx.getBean("institutionalItemService");
    
    /** Deleted Institutional Item service  */
    DeletedInstitutionalItemService deletedInstitutionalItemService = 
    	(DeletedInstitutionalItemService) ctx.getBean("deletedInstitutionalItemService");
    
    /** user service */
    UserService userService = (UserService) ctx.getBean("userService");
    
	/** Base Security data access */
	SecurityService securityService = (SecurityService) ctx
	.getBean("securityService");
	
	/** transaction manager for dealing with transactions  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	/** the transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

	
	/** Item Service */
	ItemService itemService = (ItemService) ctx
	.getBean("itemService");
	
	/** User data access */
	UserGroupService userGroupService = (UserGroupService) ctx
	.getBean("userGroupService");
	
	   /** index processing type record service  */
	IndexProcessingTypeService indexProcessingTypeService = 
    	(IndexProcessingTypeService) ctx.getBean("indexProcessingTypeService");
	
	   /** Institutional Item index processing record service  */
	InstitutionalItemIndexProcessingRecordService recordProcessingService = 
    	(InstitutionalItemIndexProcessingRecordService) ctx.getBean("institutionalItemIndexProcessingRecordService");
	
	/** Default license service */
	LicenseService licenseService = (LicenseService) ctx.getBean("licenseService");
	
    /** Service for dealing with handles  */
    HandleService handleService = (HandleService) ctx.getBean("handleService");
	
	
	/**
	 * Test moving collections to an existing collection
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void moveCollectionToCollectionTest() throws DuplicateNameException, 
	LocationAlreadyExistsException, CollectionDoesNotAcceptItemsException
	{
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
	 
		// save the repository
		tm.commit(ts);
		
        // Start the transaction - create collections
		ts = tm.getTransaction(td);
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		assert collection != null : "collection should be created";
		InstitutionalCollection subCollection = collection.createChild("subChild");
		institutionalCollectionService.saveCollection(collection);	
		InstitutionalCollection destination = repo.createInstitutionalCollection("destination"); 
	    assert destination != null : "destination collection should be created";
		tm.commit(ts);
        
		
		// start new transaction
        ts = tm.getTransaction(td);
        assert collection.getId() != null : "Institutional colletion should not have a null id " + collection;
        List<InstitutionalCollection> collectionsToMove = new LinkedList<InstitutionalCollection>();
        collectionsToMove.add(collection);
        
        institutionalCollectionService.moveCollectionInformation(destination, collectionsToMove, null);
        tm.commit(ts);
     
        //new transaction
        // make sure the collection was moved.
		ts = tm.getTransaction(td);
		InstitutionalCollection theDestination = repo.getInstitutionalCollection(destination.getName());
		InstitutionalCollection newChild = theDestination.getChild(collection.getName());
		
		assert  newChild != null : "Was not able to find " + collection
		+ " in children of " +
		theDestination ;
		
		assert newChild.getLeftValue().equals(2l) : "new child left value should = 2 but equals " + 
		    newChild.getLeftValue();
		
		assert newChild.getRightValue().equals(5l) : "new child right value should = 5l but equals " + 
	    newChild.getRightValue();

		String path = "/" + destination.getName() + "/" + newChild.getName() + "/";
		assert newChild.getFullPath().equals(path) : "new child path should = " + path 
		+ " but equals " + newChild.getFullPath();
		
		
		InstitutionalCollection aSubChild = newChild.getChild(subCollection.getName());
		assert aSubChild != null : "Sub child should not be null";
		
		assert aSubChild.getLeftValue().equals(3l) : "a sub child left value should = 3 but equals " + 
	    aSubChild.getLeftValue();
	
	    assert aSubChild.getRightValue().equals(4l) : "new child right value should = 4l but equals " + 
        aSubChild.getRightValue();
	    
	    path = path + aSubChild.getName() + "/";
	    
	    assert aSubChild.getFullPath().equals(path) : " a sub child path should = " + path +
	    " but equals " + aSubChild.getFullPath();
		
		
		tm.commit(ts);	
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		helper.cleanUpRepository();
		tm.commit(ts);	
		
	}
	
	/**
	 * Test deleting a collection with at least one item
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 * @throws UserDeletedPublicationException 
	 * @throws UserHasPublishedDeleteException 
	 */
	public void testDeleteInstitutinalCollectionWithItems() throws LocationAlreadyExistsException, 
	DuplicateNameException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException,
	CollectionDoesNotAcceptItemsException
	{
		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
			
		String userEmail1 = properties.getProperty("user_1_email");
		UserEmail email = new UserEmail(userEmail1);
		IrUser user = userService.createUser("password", "username", email);
		
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		InstitutionalCollection subCollection = collection.createChild("sub-collection");
		institutionalCollectionService.saveCollection(collection);
		
		// create the generic items
		GenericItem genericItem1 = new GenericItem("aItem");
		GenericItem genericItem2 = new GenericItem("subItem");
	        
        // add the item to the collection
        InstitutionalItem institutionalItem1 = collection.createInstitutionalItem(genericItem1);
        InstitutionalItem institutionalItem2 = collection.createInstitutionalItem(genericItem2);
        institutionalItemService.saveInstitutionalItem(institutionalItem1);
        institutionalItemService.saveInstitutionalItem(institutionalItem2);
		tm.commit(ts);
			
		// delete data
		ts = tm.getTransaction(td);
		InstitutionalCollection topLevelCollection = institutionalCollectionService.getCollection(collection.getId(), false);
		// delete the to level collection
		institutionalCollectionService.deleteCollection(topLevelCollection, user);
		tm.commit(ts);
			
	    // Start new transaction - make sure things deleted clean up remaining data
		ts = tm.getTransaction(td);
		topLevelCollection = institutionalCollectionService.getCollection(collection.getId(), false);
		assert topLevelCollection == null : "Should not be able to find top level collection " + topLevelCollection;
		subCollection = institutionalCollectionService.getCollection(subCollection.getId(), false);
		assert subCollection == null : "Should not be able to find sub collection " + subCollection;
		
		institutionalItem1 = institutionalItemService.getInstitutionalItem(institutionalItem1.getId(), false);
		assert institutionalItem1 == null : "Should not be able to find institutional item " + institutionalItem1;

		institutionalItem2 = institutionalItemService.getInstitutionalItem(institutionalItem2.getId(), false);
		assert institutionalItem2 == null : "Should not be able to find institutional item " + institutionalItem2;

		
		helper.cleanUpRepository();
		deletedInstitutionalItemService.deleteAllInstitutionalItemHistory();
		IrUser userToDelete = userService.getUser(user.getUsername());
		userService.deleteUser(userToDelete, userToDelete);

		tm.commit(ts);	
	}
	
	
	
	
	/**
	 * Test moving collections to an existing collection
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void moveCollectionToRootTest() throws DuplicateNameException, LocationAlreadyExistsException
	{
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
	 
		// save the repository
		tm.commit(ts);
		
        // Start the transaction - create collections
		ts = tm.getTransaction(td);
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		InstitutionalCollection subCollection = collection.createChild("subChild");
		institutionalCollectionService.saveCollection(collection);
		tm.commit(ts);
        
		
		// start new transaction
        ts = tm.getTransaction(td);
        //reload the objects for new transaction
        repo = repositoryService.getRepository(repo.getId(), false);
        collection = repo.getInstitutionalCollection(collection.getName());
        subCollection = collection.getChild(subCollection.getName());
        assert collection.getId() != null : "Institutional colletion should not have a null id " + collection;
        List<InstitutionalCollection> collectionsToMove = new LinkedList<InstitutionalCollection>();
        collectionsToMove.add(subCollection);
        institutionalCollectionService.moveCollectionInformation(repo, collectionsToMove);
        tm.commit(ts);
     
        //new transaction
        // make sure the collection was moved.
		ts = tm.getTransaction(td);
		InstitutionalCollection newRoot = repo.getInstitutionalCollection(subCollection.getName());
		InstitutionalCollection oldParent = repo.getInstitutionalCollection(collection.getName());
		
		assert oldParent.getLeftValue().equals(1l) : "old parent left value should = 1l but equals " + 
		    oldParent.getLeftValue();
		
		assert oldParent.getRightValue().equals(2l) : "old parent right value should = 2l but equals " + 
	    oldParent.getRightValue();
		
		InstitutionalCollection oldChild = oldParent.getChild(subCollection.getName());
		assert oldChild == null : "old child should be null";
		
		assert newRoot.getLeftValue().equals(1l) : "new root left value should = 4 but equals " + 
	    newRoot.getLeftValue();
	
	    assert newRoot.getRightValue().equals(2l) : "new root right value should = 5l but equals " + 
        newRoot.getRightValue();
	    
	    String path =  "/" + newRoot.getName() + "/";
	    
	    assert newRoot.getFullPath().equals(path) : " new root path should = " + path +
	    " but equals " + newRoot.getFullPath();
		
		
		tm.commit(ts);	
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		helper.cleanUpRepository();
		tm.commit(ts);	
		
	}

	/**
	 * Test adding subscribers to collection
	 * 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	public void collectionSubscriptionTest() throws DuplicateNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
	{
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		
		String userEmail1 = properties.getProperty("user_1_email");
		UserEmail email = new UserEmail(userEmail1);
		IrUser user = userService.createUser("password", "username", email);
		
		// save the repository
		tm.commit(ts);
		
        // Start the transaction - create collections
		ts = tm.getTransaction(td);
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		collection.addSuscriber(user);
		institutionalCollectionService.saveCollection(collection);
		tm.commit(ts);
        
		
		// start new transaction
        ts = tm.getTransaction(td);
        //reload the objects for new transaction
        assert collection.getSubscriptions().size() == 1 : "Institutional colletion should have subscriptions";
        assert collection.hasSubscriber(user) : "Collection should have subscriber - " + user.getUsername();
        collection.removeSubscriber(user);
        institutionalCollectionService.saveCollection(collection);
        tm.commit(ts);
     
	    // Start new transaction
		ts = tm.getTransaction(td);
		InstitutionalCollection otherCollection = institutionalCollectionService.getCollection(collection.getId(), false);
		assert otherCollection.getSubscriptions().size() == 0: "Collection should not have subscribers";
		institutionalCollectionService.deleteCollection(otherCollection, user);
		IrUser userToDelete = userService.getUser(user.getUsername());
		userService.deleteUser(userToDelete, userToDelete);
		helper.cleanUpRepository();
		tm.commit(ts);	
		
	}
	
	public void testSetAllPublicationsWithinCollectionPublic() throws IllegalFileSystemNameException, 
	DuplicateNameException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException, 
	UserDeletedPublicationException,
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException{

		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);

		// save the repository
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		
		UserEmail email = new UserEmail("email");
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
		
		IrFile irFile = repositoryService.createIrFile(repo, f, "fileName", "description");

		// create a personal item to publish into the repository
		GenericItem genericItem = new GenericItem("item name");
		genericItem.addFile(irFile).setPublic(false);
		genericItem.setPubliclyViewable(false);
		collection.createInstitutionalItem(genericItem);
		institutionalCollectionService.saveCollection(collection);

		tm.commit(ts);
		
		
		// test searching for the data
		ts = tm.getTransaction(td);
        institutionalCollectionService.setAllItemsWithinCollectionPublic(collection);
        
		GenericItem otherItem = itemService.getGenericItem(genericItem.getId(), false);
		assert otherItem.isPubliclyViewable() : "The item should be public";

		assert otherItem.getItemFile("fileName").isPublic(): "The item file should be public";
		
		tm.commit(ts);
		
	    // Start new transaction - clean up the data
		ts = tm.getTransaction(td);
        	
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(),false), user);
        deletedInstitutionalItemService.deleteAllInstitutionalItemHistory();
        IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);
        helper.cleanUpRepository();
		tm.commit(ts);	

	}

	public void testSetAllPublicationsWithinCollectionPrivate() throws IllegalFileSystemNameException, 
	DuplicateNameException, 
	UserDeletedPublicationException, 
	UserHasPublishedDeleteException, 
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException{

		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);

		// save the repository
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		
		UserEmail email = new UserEmail("email");
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
		
		IrFile irFile = repositoryService.createIrFile(repo, f, "fileName", "description");

		// create a personal item to publish into the repository
		GenericItem genericItem = new GenericItem("item name");
		genericItem.addFile(irFile);
		collection.createInstitutionalItem(genericItem);
		institutionalCollectionService.saveCollection(collection);

		tm.commit(ts);
		
		
		// test searching for the data
		ts = tm.getTransaction(td);
        institutionalCollectionService.setAllItemsWithinCollectionPrivate(collection);
        
		GenericItem otherItem = itemService.getGenericItem(genericItem.getId(), false);
		assert !otherItem.isPubliclyViewable() : "The item should be private";

		assert !otherItem.getItemFile("fileName").isPublic(): "The item file should be private";
		
		tm.commit(ts);
		
	    // Start new transaction - clean up the data
		ts = tm.getTransaction(td);
		
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(),false), user);
        deletedInstitutionalItemService.deleteAllInstitutionalItemHistory();
        IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
        helper.cleanUpRepository();
		tm.commit(ts);	

	}
	
	/**
	 * 
	 * Use the service to add an item to a collection
	 * 
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 * @throws CollectionDoesNotAcceptItemsException 
	 * @throws RepositoryLicenseNotAcceptedException 
	 * @throws PermissionNotGrantedException 
	 * @throws UserDeletedPublicationException 
	 * @throws UserHasPublishedDeleteException 
	 */
	public void testAddItemToCollectionWithoutHandleTest() throws LocationAlreadyExistsException, 
	DuplicateNameException, 
	PermissionNotGrantedException, 
	RepositoryLicenseNotAcceptedException, 
	CollectionDoesNotAcceptItemsException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException
	{

		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);

		// save the repository
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		
		institutionalCollectionService.saveCollection(collection);
		
		VersionedLicense license = licenseService.createLicense(user, "text", " a license ", "this is a license description");
	    repo.updateDefaultLicense(user, license.getCurrentVersion());
	    repositoryService.saveRepository(repo);
	    
	    user.addAcceptedLicense(license.getCurrentVersion());
	    userService.makeUserPersistent(user);
		
		IrUserGroup userGroup = new IrUserGroup("directSubmitGroup");
		userGroup.addUser(user);
	    userGroupService.save(userGroup);
	    
		
	
		// set up direct submit permissions
		IrClassTypePermission directSubmitPermission = 
			securityService.getPermissionForClass(collection, InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION.getPermission());		
		List<IrClassTypePermission> permissions = new ArrayList<IrClassTypePermission>();
		permissions.add(directSubmitPermission);
		securityService.createPermissions(collection, userGroup, permissions);
		
		// create the generic item
		GenericItem item = new GenericItem("item name");
		
		
		// index processing types
		IndexProcessingType updateProcessingType = new IndexProcessingType(IndexProcessingTypeService.UPDATE);
		indexProcessingTypeService.save(updateProcessingType);
		
		IndexProcessingType deleteProcessingType = new IndexProcessingType(IndexProcessingTypeService.DELETE);
		indexProcessingTypeService.save(deleteProcessingType);
		
		IndexProcessingType insertProcessingType =  new IndexProcessingType(IndexProcessingTypeService.INSERT);
		indexProcessingTypeService.save(insertProcessingType);
		tm.commit(ts);
		
		
		
		ts = tm.getTransaction(td);
		// create an item user service to add it to collection
		assert repo.getDefaultLicense() != null;
		InstitutionalItem institutionalItem = institutionalCollectionService.addItemToCollection(user, item, collection);
		assert institutionalItem != null : "institutional item should not be null";
		assert institutionalItem.getInstitutionalCollection().equals(collection) : "institutional item collection should equal " 
			+ collection + " but equals " + institutionalItem.getInstitutionalCollection();
		assert institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getRepositoryLicense().getLicenseVersion().equals(repo.getDefaultLicense().getVersionedLicense().getCurrentVersion()) : 
			"License should equal " + repo.getDefaultLicense().getVersionedLicense().getCurrentVersion() + " but equals " + institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getRepositoryLicense().getLicenseVersion();
		
		tm.commit(ts);
		
		ts = tm.getTransaction(td);

		   /** Institutional Item index processing record service  */
		List<InstitutionalItemIndexProcessingRecord> processingRecords = recordProcessingService.getAll();
		for(InstitutionalItemIndexProcessingRecord pr : processingRecords )
		{
			recordProcessingService.delete(pr);
		}
		
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.DELETE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
		
		
		securityService.deletePermissions(collection.getId(), collection.getClass().getName(), userGroup);
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(),false), user);
        deletedInstitutionalItemService.deleteAllInstitutionalItemHistory();
        userGroupService.delete(userGroupService.get(userGroup.getId(), false));
        helper.cleanUpRepository();
        IrUser deleteUser = userService.getUser(user.getId(), false);
        
        Set<UserRepositoryLicense> licenses = deleteUser.getAcceptedLicenses();
        for(UserRepositoryLicense url : licenses)
        {
        	deleteUser.removeAcceptedLicense(url);
        }
        userService.makeUserPersistent(deleteUser);
 		tm.commit(ts);

 		ts = tm.getTransaction(td);
        licenseService.delete(licenseService.get(license.getId(), false));
 		deleteUser = userService.getUser(user.getId(), false);
 		userService.deleteUser(deleteUser, deleteUser);
 		tm.commit(ts);
 		
	}
	
	/**
	 * 
	 * Use the service to add an item to a collection
	 * 
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 * @throws CollectionDoesNotAcceptItemsException 
	 * @throws RepositoryLicenseNotAcceptedException 
	 * @throws PermissionNotGrantedException 
	 * @throws UserDeletedPublicationException 
	 * @throws UserHasPublishedDeleteException 
	 */
	public void testAddItemToCollectionWithHandleTest() throws LocationAlreadyExistsException, 
	DuplicateNameException, 
	PermissionNotGrantedException, 
	RepositoryLicenseNotAcceptedException, 
	CollectionDoesNotAcceptItemsException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException
	{

		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		

		HandleNameAuthority authority = new HandleNameAuthority("1802");
		

		// save the repository
		repo = repositoryService.getRepository(repo.getId(), false);
		repo.setDefaultHandleNameAuthority(authority);
		
		repositoryService.saveRepository(repo);
		
		
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		
		institutionalCollectionService.saveCollection(collection);
	    
		IrUserGroup userGroup = new IrUserGroup("directSubmitGroup");
		userGroup.addUser(user);
	    userGroupService.save(userGroup);
	    
		
	
		// set up direct submit permissions
		IrClassTypePermission directSubmitPermission = 
			securityService.getPermissionForClass(collection, InstitutionalCollectionSecurityService.DIRECT_SUBMIT_PERMISSION.getPermission());		
		List<IrClassTypePermission> permissions = new ArrayList<IrClassTypePermission>();
		permissions.add(directSubmitPermission);
		securityService.createPermissions(collection, userGroup, permissions);
		
		// create the generic item
		GenericItem item = new GenericItem("item name");
		
		
		// index processing types
		IndexProcessingType updateProcessingType = new IndexProcessingType(IndexProcessingTypeService.UPDATE);
		indexProcessingTypeService.save(updateProcessingType);
		
		IndexProcessingType deleteProcessingType = new IndexProcessingType(IndexProcessingTypeService.DELETE);
		indexProcessingTypeService.save(deleteProcessingType);
		
		IndexProcessingType insertProcessingType =  new IndexProcessingType(IndexProcessingTypeService.INSERT);
		indexProcessingTypeService.save(insertProcessingType);
		InstitutionalItem institutionalItem = institutionalCollectionService.addItemToCollection(user, item, collection);

		tm.commit(ts);
		
		
		
		ts = tm.getTransaction(td);
		// create an item user service to add it to collection
		institutionalItem = institutionalItemService.getInstitutionalItem(institutionalItem.getId(), false);
		assert institutionalItem != null : "institutional item should not be null";
		assert institutionalItem.getInstitutionalCollection().equals(collection) : "institutional item collection should equal " 
			+ collection + " but equals " + institutionalItem.getInstitutionalCollection();

		HandleInfo handleInfo = institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getHandleInfo();
		assert handleInfo != null : "Should have a handle info object";
		assert handleInfo.getNameAuthority().equals(authority) : "Info authority should equal " + authority + " but equals " + handleInfo.getNameAuthority();
		assert handleInfo.getData() != null : "Data should not be null";
		assert !handleInfo.getData().equals("") : "Data should not be an empty string";

		
		   /** Institutional Item index processing record service  */
		List<InstitutionalItemIndexProcessingRecord> processingRecords = recordProcessingService.getAll();
		for(InstitutionalItemIndexProcessingRecord pr : processingRecords )
		{
			recordProcessingService.delete(pr);
		}
		
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.DELETE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
		
		
		securityService.deletePermissions(collection.getId(), collection.getClass().getName(), userGroup);
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(),false), user);
        deletedInstitutionalItemService.deleteAllInstitutionalItemHistory();
        IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
        userGroupService.delete(userGroupService.get(userGroup.getId(), false));
        helper.cleanUpRepository();
        tm.commit(ts);
        
        ts = tm.getTransaction(td);
		handleService.delete(handleService.getHandleInfo(handleInfo.getId(), false));
		handleService.delete(handleService.getNameAuthority(authority.getId(), false));
		tm.commit(ts);

	}
	
	/**
	 * 
	 * Use the service to add an item to a collection
	 * 
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 * @throws CollectionDoesNotAcceptItemsException 
	 * @throws RepositoryLicenseNotAcceptedException 
	 * @throws PermissionNotGrantedException 
	 * @throws UserDeletedPublicationException 
	 * @throws UserHasPublishedDeleteException 
	 */
	public void testAddItemToCollectionWithNoPermissions() throws LocationAlreadyExistsException, 
	DuplicateNameException, 
	RepositoryLicenseNotAcceptedException, 
	CollectionDoesNotAcceptItemsException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException
	{

		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		
		institutionalCollectionService.saveCollection(collection);
	    tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		// create an item user service to add it to collection
		
		InstitutionalItem institutionalItem = null;
		try {
			// create the generic item
			GenericItem item = new GenericItem("item name");
			institutionalItem = institutionalCollectionService.addItemToCollection(user, item, collection);
			assert false : "this should fail with permission granted exception ";
		} catch (PermissionNotGrantedException e) {
		   // swallow error
		}
		assert institutionalItem == null : "institutional item should be null";
		
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(),false), user);
        deletedInstitutionalItemService.deleteAllInstitutionalItemHistory();
        IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
        helper.cleanUpRepository();
        tm.commit(ts);
	}
	
	/**
	 * 
	 * Use the service to add an item to a collection when the repository has a license the
	 * user must accept
	 * 
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 * @throws CollectionDoesNotAcceptItemsException 
	 * @throws RepositoryLicenseNotAcceptedException 
	 * @throws PermissionNotGrantedException 
	 * @throws UserDeletedPublicationException 
	 * @throws UserHasPublishedDeleteException 
	 */
	public void testAddItemToCollectionWithLicense() throws LocationAlreadyExistsException, 
	DuplicateNameException, 
	CollectionDoesNotAcceptItemsException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException, 
	PermissionNotGrantedException
	{

		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		
		institutionalCollectionService.saveCollection(collection);
	    
	
		VersionedLicense license = licenseService.createLicense(user, "text", " a license ", "this is a license description");
	    repo.updateDefaultLicense(user, license.getCurrentVersion());
	    repositoryService.saveRepository(repo);

		// create the generic item
		GenericItem item = new GenericItem("item name");
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		// create an item user service to add it to collection

	    InstitutionalItem institutionalItem = null;
		try {
			institutionalItem = institutionalCollectionService.addItemToCollection(user, item, collection);
			assert false : "Should not make it here";
		} catch (RepositoryLicenseNotAcceptedException e) {
			//swallow error
		}
		assert institutionalItem == null : "institutional item should be null";
		
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);

        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(),false), user);
        IrUser deleteUser = userService.getUser(user.getId(), false);
        helper.cleanUpRepository();
        licenseService.delete(licenseService.get(license.getId(), false));
        userService.deleteUser(deleteUser, deleteUser);	
        tm.commit(ts);
	}
	
	
	
}
