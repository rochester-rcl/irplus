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
import java.util.List;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.file.db.FolderInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.SearchResults;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserSearchService;
import edu.ur.ir.user.UserService;


/**
 * Test the service methods for the searching for users
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultUserSearchServiceTest {
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");

	/** User search service */
	UserSearchService userSearchService = (UserSearchService) ctx.getBean("userSearchService");

	/** User indexing service */
	DefaultUserIndexService userIndexService = (DefaultUserIndexService) ctx.getBean("userIndexService");


	/**
	 * Test indexing a personal file - which may have multiple versions 
	 * in it.
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws NoUserIndexFolderException 
	 */
	public void testSearchPersonalFile() throws NoIndexFoundException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);

        // Start the transaction 
		ts = tm.getTransaction(td);
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		user.setFirstName("firstName");
		user.setLastName("lastName");
		userService.makeUserPersistent(user);
		FolderInfo userIndexFolder = repo.getFileDatabase().createFolder("userIndexFolderIndex");
		repo.setUserIndexFolder(userIndexFolder);
		assert user.getId() != null : "User id should not be null";
		tm.commit(ts);
		
        //new transaction - add a file to a user
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		
		File userIndex = new File(userIndexFolder.getFullPath());
		userIndexService.addToIndex(user, userIndex);
		tm.commit(ts);
		
	    // Start new transaction - clean up
		ts = tm.getTransaction(td);
		SearchResults<IrUser> searchResults = userSearchService.search(new File(userIndexFolder.getFullPath()), "firstName", 0, 10);
		List<IrUser> users = searchResults.getObjects();
		assert users.size() == 1;
		assert users.contains(user);
		
		user = userService.getUser(user.getId(), false);
		userService.deleteUser(user);
		helper.cleanUpRepository();
		tm.commit(ts);
	}


}
