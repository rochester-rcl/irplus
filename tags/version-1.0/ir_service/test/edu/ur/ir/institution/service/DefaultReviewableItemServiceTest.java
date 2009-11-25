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

import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.ReviewableItem;
import edu.ur.ir.institution.ReviewableItemService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;

/**
 * Test for reviewable item accept / reject
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
	
	/**
	 * Test accepting the item review 
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void acceptReviewableItemTest() throws DuplicateNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
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
		institutionalItemService.deleteAllInstitutionalItemHistory();
		
		IrUser deleteUser = userService.getUser(reviewer.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
		
		tm.commit(ts);	
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		helper.cleanUpRepository();
		tm.commit(ts);	
		
	}
	

}
