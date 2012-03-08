/**  
   Copyright 2008 - 2011 University of Rochester

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


package edu.ur.hibernate.ir.groupspace.db;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceDAO;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserManager;
import edu.ur.order.OrderType;

/**
 * Group space data access object test.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class GroupWorkspaceDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	GroupWorkspaceDAO groupWorkspaceDAO = (GroupWorkspaceDAO) ctx
	.getBean("groupWorkspaceDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
    /** User data access */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");

    
	/**
	 * Test group space persistence
	 */
	@Test
	public void simpleGroupWorkspaceDAOTest() throws Exception{

         
        TransactionStatus ts = tm.getTransaction(td);
		GroupWorkspace groupSpace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupSpace);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		GroupWorkspace other = groupWorkspaceDAO.getById(groupSpace.getId(), false);
        assert other.equals(groupSpace) : "Group space " + other + " should equal " + groupSpace;
        tm.commit(ts);        

 	    ts = tm.getTransaction(td);
 	    other = groupWorkspaceDAO.getById(groupSpace.getId(), false);
        groupWorkspaceDAO.makeTransient(other);
        assert  groupWorkspaceDAO.getById(other.getId(), false) == null : "Should no longer be able to find groupSpace";
	    tm.commit(ts);
	}
	
	/**
	 * Test adding onwer to group spaces
	 */
	@Test
	public void addGroupWorkspaceOwnerDAOTest() throws Exception{

         
        TransactionStatus ts = tm.getTransaction(td);
		GroupWorkspace groupSpace = new GroupWorkspace("grouName", "groupDescription");
		
        // create a user who has their own folder
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		UserEmail userEmail = new UserEmail("user@email");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		// create the user and their folder.
		userDAO.makePersistent(user);
		
		groupSpace.add(user, true);
		
        groupWorkspaceDAO.makePersistent(groupSpace);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		GroupWorkspace other = groupWorkspaceDAO.getById(groupSpace.getId(), false);
 		GroupWorkspaceUser workspaceUser = other.getUser(user);
 		assert workspaceUser != null : "Workspace user for " + user + " should not be null";
 		assert workspaceUser.isOwner() : "Workspace user  " + workspaceUser + " should be owner of project but is not";
 		List<GroupWorkspace> workspaces = groupWorkspaceDAO.getGroupWorkspacesForUser(user.getId(), 0, 20, OrderType.ASCENDING_ORDER);
 		assert workspaces.size() == 1 : "Should find one workspace but found " + workspaces.size();
 		assert workspaces.contains(other) : "List should contain " + other + " but does not";
        tm.commit(ts);        

 	    ts = tm.getTransaction(td);
 	    other = groupWorkspaceDAO.getById(groupSpace.getId(), false);
        groupWorkspaceDAO.makeTransient(other);
        assert  groupWorkspaceDAO.getById(other.getId(), false) == null : "Should no longer be able to find groupSpace";
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
        tm.commit(ts);
	}
}
