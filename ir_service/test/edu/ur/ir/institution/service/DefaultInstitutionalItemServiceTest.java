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
import java.util.HashSet;
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
import edu.ur.ir.institution.CollectionDoesNotAcceptItemsException;
import edu.ur.ir.institution.DeletedInstitutionalItemService;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSecurityService;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemFileSecurityService;
import edu.ur.ir.item.ItemSecurityService;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserGroupService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;
import edu.ur.util.FileUtil;


/**
 * Test the service methods for the institutional item services
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultInstitutionalItemServiceTest {
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");
	
	/** Repository data access */
	RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
    /** Collection service  */
    InstitutionalCollectionService institutionalCollectionService = 
    	(InstitutionalCollectionService) ctx.getBean("institutionalCollectionService");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** service for dealing with institutional items */
	InstitutionalItemService institutionalItemService = (InstitutionalItemService)ctx.getBean("institutionalItemService");
	
    /** user group service  */
    UserGroupService userGroupService = 
    	(UserGroupService) ctx.getBean("userGroupService");

    
	/** Base Security data access */
	SecurityService securityService = (SecurityService) ctx
	.getBean("securityService");
	
	/** Item Service */
	ItemService itemService = (ItemService) ctx
	.getBean("itemService");
	

    /** Deleted Institutional Item service  */
    DeletedInstitutionalItemService deletedInstitutionalItemService = 
    	(DeletedInstitutionalItemService) ctx.getBean("deletedInstitutionalItemService");
	
	/**
	 * Test deleted institutional item
	 * 
	 * @throws DuplicateNameException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testInstitutionalItemHistory() throws DuplicateNameException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException, 
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException
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

		// create a personal item to publish into the repository
		GenericItem genericItem = new GenericItem("item name");
		InstitutionalItem institutionalItem = collection.createInstitutionalItem(genericItem);
		institutionalCollectionService.saveCollection(collection);

		tm.commit(ts);
		
		
		// test searching for the data
		ts = tm.getTransaction(td);
        
		long instituionalItemId = institutionalItem.getId();
		assert institutionalItemService.getInstitutionalItem(institutionalItem.getId(), false).equals(institutionalItem) : "Institutional item should exist";
        institutionalItemService.deleteInstitutionalItem(institutionalItemService.getInstitutionalItem(institutionalItem.getId(), false), user);
        assert institutionalItemService.getInstitutionalItem(instituionalItemId, false) == null : "Institutional item should not exist";
        assert deletedInstitutionalItemService.getDeleteInfoForInstitutionalItem(instituionalItemId) != null : "Delete history for institutional item should exist";
		tm.commit(ts);
		
	    // Start new transaction - clean up the data
		ts = tm.getTransaction(td);
		deletedInstitutionalItemService.deleteAllInstitutionalItemHistory();
		IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(),false), user);
        helper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Tests the assignment of user group permissions for an item which is submitted
	 * to private collection
	 * 
	 * @throws DuplicateNameException
	 * @throws UserHasPublishedDeleteException
	 * @throws IllegalFileSystemNameException
	 * @throws UserDeletedPublicationException
	 * @throws LocationAlreadyExistsException 
	 */
	public void testAssignGroupPermissionToItem()  throws DuplicateNameException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException, 
	IllegalFileSystemNameException, 
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
		InstitutionalItem institutionalItem = collection.createInstitutionalItem(genericItem);
		institutionalCollectionService.saveCollection(collection);

		IrUserGroup userGroup = new IrUserGroup("DEFAULT_ADMIN_GROUP");
		userGroupService.save(userGroup);
		
		IrClassTypePermission viewPermission = 
			securityService.getPermissionForClass(collection, 
				InstitutionalCollectionSecurityService.VIEW_PERMISSION.getPermission());
			
		IrClassTypePermission adminPermission = 
			securityService.getPermissionForClass(collection, 
					InstitutionalCollectionSecurityService.ADMINISTRATION_PERMISSION.getPermission());

		
		HashSet<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.add(viewPermission);
		permissions.add(adminPermission);
		
		securityService.createPermissions(collection, userGroup, permissions);
		
		tm.commit(ts);
		List<InstitutionalCollection> collections = new LinkedList<InstitutionalCollection>();
		collections.add(collection);
		
		// test searching for the data
		ts = tm.getTransaction(td);
        institutionalItemService.setItemPrivatePermissions(genericItem, collections);
        
		GenericItem otherItem = itemService.getGenericItem(genericItem.getId(), false);
		ItemFile file = otherItem.getItemFile("fileName");
		
		assert !otherItem.isPubliclyViewable() : "Item should be private";
		
		assert !otherItem.getItemFile("fileName").isPublic() : "Item File should be private";
		
		assert securityService.getAcl(otherItem).getGroupAccessControlEntry(userGroup).getPermissionNames().contains(ItemSecurityService.ITEM_METADATA_READ_PERMISSION.getPermission())
						: "The user group should have metadata read permission";

		assert securityService.getAcl(otherItem).getGroupAccessControlEntry(userGroup).getPermissionNames().contains(ItemSecurityService.ITEM_FILE_EDIT_PERMISSION.getPermission())
						: "The user group should have file edit permission";
		
		assert securityService.getAcl(otherItem).getGroupAccessControlEntry(userGroup).getPermissionNames().contains(ItemSecurityService.ITEM_METADATA_EDIT_PERMISSION.getPermission())
						: "The user group should have metadata edit permission";
		
		assert !securityService.getAcl(otherItem).getGroupAccessControlEntry(userGroup).getPermissionNames().contains(ItemFileSecurityService.ITEM_FILE_READ_PERMISSION.getPermission())
						: "The user group should not have file read permission";

		assert securityService.getAcl(file).getGroupAccessControlEntry(userGroup).getPermissionNames().contains(ItemFileSecurityService.ITEM_FILE_READ_PERMISSION.getPermission())
						: "The user group should have file read permission";
		
		tm.commit(ts);
		
	    // Start new transaction - clean up the data
		ts = tm.getTransaction(td);
		securityService.deleteAcl(securityService.getAcl(otherItem));
		securityService.deleteAcl(securityService.getAcl(file));
		securityService.deleteAcl(securityService.getAcl(collection));
		institutionalItemService.deleteInstitutionalItem(institutionalItemService.getInstitutionalItem(institutionalItem.getId(), false), user);
		deletedInstitutionalItemService.deleteAllInstitutionalItemHistory();
		IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);		
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(),false), user);
        userGroupService.delete(userGroupService.get(userGroup.getId(), false));
        helper.cleanUpRepository();
		tm.commit(ts);	

	}
}
