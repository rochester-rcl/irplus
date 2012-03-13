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

package edu.ur.ir.groupspace.service;

import java.io.File;
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
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.SearchResults;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceSearchService;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;


/**
 * Test the service methods for the searching for group workspaces
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultGroupWorkspaceSearchServiceTest {
	
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
	GroupWorkspaceService groupWorkspaceService = (GroupWorkspaceService) ctx.getBean("groupWorkspaceService");

	/** User search service */
	GroupWorkspaceSearchService groupWorkspaceSearchService = (GroupWorkspaceSearchService) ctx.getBean("groupWorkspaceSearchService");

	/** User indexing service */
	DefaultGroupWorkspaceIndexService groupWorkspaceIndexService = (DefaultGroupWorkspaceIndexService) ctx.getBean("groupWorkspaceIndexService");
	

	/* User data access */
	UserService userService = (UserService) ctx.getBean("userService");

	/**
	 * Test indexing a personal file - which may have multiple versions 
	 * in it.
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws PermissionNotGrantedException 
	 * @throws DuplicateNameException 
	 * @throws NoUserIndexFolderException 
	 */
	public void testSearchGroupWorkspaces() throws NoIndexFoundException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, PermissionNotGrantedException, DuplicateNameException
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
		
		// create the group workspace
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupSpace");
		groupWorkspace.add(user, true);
		groupWorkspaceService.save(groupWorkspace);
		
		tm.commit(ts);
		
        //new transaction - add a file to a user
		ts = tm.getTransaction(td);
		groupWorkspace = groupWorkspaceService.get(groupWorkspace.getId(), false);
		File userIndex = new File(repo.getGroupWorkspaceIndexFolder());
		groupWorkspaceIndexService.add(groupWorkspace, userIndex);
		tm.commit(ts);
		
	    // Start new transaction - clean up
		ts = tm.getTransaction(td);
		SearchResults<GroupWorkspace> searchResults = groupWorkspaceSearchService.search(new File(repo.getGroupWorkspaceIndexFolder()), "groupSpace", 0, 10);
		List<GroupWorkspace> groupWorkspaces = searchResults.getObjects();
		assert groupWorkspaces.size() == 1 : "Size should be one but is " + groupWorkspaces.size();
		assert groupWorkspaces.contains(groupWorkspace) : "Should contain groupWorkspace " + groupWorkspace + " but does not";
		
		groupWorkspace = groupWorkspaceService.get(groupWorkspace.getId(), false);
		user = userService.getUser(user.getId(), false);
		groupWorkspaceService.delete(groupWorkspace, user);
		userService.deleteUser( user, userService.getUser(user.getId(), false));
	    
	    File f = new File(repo.getGroupWorkspaceIndexFolder());
	    assert f.exists() : " File " + f.getAbsolutePath() + " should exist ";
		helper.cleanUpRepository();
		assert !f.exists() : " File " + f.getAbsolutePath() + " should NOT exist ";
		tm.commit(ts);
		
	}

}
