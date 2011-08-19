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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.institution.CollectionDoesNotAcceptItemsException;
import edu.ur.ir.institution.DeletedInstitutionalItemService;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionSecurityService;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.ReviewableItem;
import edu.ur.ir.institution.ReviewableItemService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryLicenseNotAcceptedException;
import edu.ur.ir.repository.RepositoryService;
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
import edu.ur.ir.user.UserService;

/**
 * Test for reviewable items 
 * 
 * @author Sharmila Ranganathan
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultReviewableItemServiceTest {

	
	/** Spring application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
    /** Repository service  */
    RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
    
    /** Reviewable item service  */
    ReviewableItemService reviewableItemService = 
    	(ReviewableItemService) ctx.getBean("reviewableItemService");

    /** Item service  */
    ItemService itemService = 
    	(ItemService) ctx.getBean("itemService");

    /** Collection service  */
    InstitutionalCollectionService institutionalCollectionService = 
    	(InstitutionalCollectionService) ctx.getBean("institutionalCollectionService");
    
    /** Item service  */
    InstitutionalItemService institutionalItemService = 
    	(InstitutionalItemService) ctx.getBean("institutionalItemService");

    /** user service */
    UserService userService = (UserService) ctx.getBean("userService");
    
	/** transaction manager for dealing with transactions  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	/** the transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
    /** Deleted Institutional Item service  */
    DeletedInstitutionalItemService deletedInstitutionalItemService = 
    	(DeletedInstitutionalItemService) ctx.getBean("deletedInstitutionalItemService");
    
	/** User data access */
	UserGroupService userGroupService = (UserGroupService) ctx
	.getBean("userGroupService");
	
	/** Base Security data access */
	SecurityService securityService = (SecurityService) ctx
	.getBean("securityService");
	
	/**
	 * Test accepting the item review 
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 * @throws CollectionDoesNotAcceptItemsException 
	 */
	public void acceptReviewableItemTest() throws DuplicateNameException, 
	UserHasPublishedDeleteException,
	UserDeletedPublicationException, 
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
		institutionalCollectionService.saveCollection(collection);
		assert collection != null : "collection should be created";
		tm.commit(ts);
        
		
		// start new transaction
        ts = tm.getTransaction(td);
        GenericItem item = new GenericItem("itemName");
        itemService.makePersistent(item);
		
        String strEmail = properties.getProperty("user_1_email");
        UserEmail email = new UserEmail(strEmail);

        IrUser reviewer = userService.createUser("password", "username", email);
        userService.makeUserPersistent(reviewer);
        tm.commit(ts);

		// start new transaction
        ts = tm.getTransaction(td);
        item = itemService.getGenericItem(item.getId(), false);
        ReviewableItem ri = collection.addReviewableItem(item);
        institutionalCollectionService.saveCollection(collection);
        tm.commit(ts);
     
        // new transaction
        // make sure the collection was moved.
		ts = tm.getTransaction(td);
		ReviewableItem otherRI = reviewableItemService.getReviewableItem(ri.getId(), false);
		InstitutionalCollection otherCollection1 = institutionalCollectionService.getCollection(collection.getId(), false);
		assert otherRI.getItem().equals(item) : "Item should be same";
		assert otherCollection1.getReviewableItem(item).equals(otherRI) : "Reviewable item should exist in collection";
		InstitutionalItem ii = reviewableItemService.acceptItem(otherRI, reviewer);
		
		tm.commit(ts);	
		
		ts = tm.getTransaction(td);
		InstitutionalCollection otherCollection = institutionalCollectionService.getCollection(collection.getId(), false);
		
		assert otherCollection.getItems("itemName").contains(ii) : "Item should be published to collection";
		institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(), false), reviewer);
		deletedInstitutionalItemService.deleteAllInstitutionalItemHistory();
		
		IrUser deleteUser = userService.getUser(reviewer.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
		
		tm.commit(ts);	
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		helper.cleanUpRepository();
		tm.commit(ts);	
		
	}
	
	/**
	 * Make sure the reviewable item is added to the collection correctly.
	 * 
	 * @throws LocationAlreadyExistsException
	 * @throws DuplicateNameException
	 * @throws PermissionNotGrantedException
	 * @throws RepositoryLicenseNotAcceptedException
	 * @throws CollectionDoesNotAcceptItemsException
	 * @throws UserHasPublishedDeleteException
	 * @throws UserDeletedPublicationException
	 */
	public void addReviewableItemToCollectionTest() throws LocationAlreadyExistsException, 
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
		repositoryService.saveRepository(repo);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		institutionalCollectionService.saveCollection(collection);
	    
		IrUserGroup userGroup = new IrUserGroup("reviewSubmitGroup");
		userGroup.addUser(user);
	    userGroupService.save(userGroup);
	
		// set up direct submit permissions
		IrClassTypePermission directSubmitPermission = 
			securityService.getPermissionForClass(collection, InstitutionalCollectionSecurityService.REVIEW_SUBMIT_PERMISSION.getPermission());		
		List<IrClassTypePermission> permissions = new ArrayList<IrClassTypePermission>();
		permissions.add(directSubmitPermission);
		securityService.createPermissions(collection, userGroup, permissions);
		
		// create the generic item
		GenericItem item = new GenericItem("item name");
		
		ReviewableItem reviewableItem = reviewableItemService.addReviewableItemToCollection(user, item, collection);

		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		// create an item user service to add it to collection
		reviewableItem = reviewableItemService.getReviewableItem(reviewableItem.getId(), false);
		assert reviewableItem != null : "reviewableItem item should not be null";
		assert reviewableItem.getInstitutionalCollection().equals(collection) : "institutional collection should equal " 
			+ collection + " but equals " + reviewableItem.getInstitutionalCollection();
		
		securityService.deletePermissions(collection.getId(), collection.getClass().getName(), userGroup);
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(),false), user);
        deletedInstitutionalItemService.deleteAllInstitutionalItemHistory();
        IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
        userGroupService.delete(userGroupService.get(userGroup.getId(), false));
        helper.cleanUpRepository();
        tm.commit(ts);

	}
	

}
