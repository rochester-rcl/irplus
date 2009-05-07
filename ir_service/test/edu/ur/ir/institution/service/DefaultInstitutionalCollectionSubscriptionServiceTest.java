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


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.mail.MessagingException;


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
import edu.ur.ir.institution.InstitutionalCollectionSubscriptionService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.GenericItem;
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
 * Tests for the Institutional collection service.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultInstitutionalCollectionSubscriptionServiceTest {
	
	/** Spring application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	InstitutionalCollectionSubscriptionService subscriptionService = (InstitutionalCollectionSubscriptionService)ctx.getBean("institutionalCollectionSubscriptionService");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** transaction manager for dealing with transactions  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	/** the transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");
	
	/** Repository data access */
	RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
	
    /** Collection service  */
    InstitutionalCollectionService institutionalCollectionService = 
    	(InstitutionalCollectionService) ctx.getBean("institutionalCollectionService");
    
	/** service for dealing with institutional items */
	InstitutionalItemService institutionalItemService = (InstitutionalItemService)ctx.getBean("institutionalItemService");

	/**
	 * Test sending emails for subscriptions.
	 * 
	 * @throws MessagingException 
	 * @throws UserDeletedPublicationException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 */
	public void testSendSubscriptionEmails() throws MessagingException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, DuplicateNameException 
	{
		
		boolean sendEmails = new Boolean(properties.getProperty("send_emails"));
		
		if( sendEmails )
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
		    // create a personal item to publish into the repository
		    GenericItem genericItem = new GenericItem("item name");
		    collection.createInstitutionalItem(genericItem);
		    institutionalCollectionService.saveCollection(collection);
		    tm.commit(ts);
        
		
		    // start new transaction
            ts = tm.getTransaction(td);
		    GregorianCalendar calendar = new GregorianCalendar();
		    calendar.add(Calendar.DAY_OF_YEAR, -1);
		    Date yesterday = calendar.getTime();
		
		    calendar.add(Calendar.DAY_OF_YEAR, +2);
		    Date tomorrow = calendar.getTime();
		
		
            subscriptionService.sendSubscriberEmail(user, yesterday, tomorrow);
            tm.commit(ts);
     
	        // Start new transaction
		    ts = tm.getTransaction(td);
 		    userService.deleteUser(userService.getUser(user.getUsername()));
		    helper.cleanUpRepository();
		    tm.commit(ts);
		}
	}

}
