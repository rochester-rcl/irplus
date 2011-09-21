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

import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceDAO;
import edu.ur.ir.groupspace.GroupWorkspaceInviteException;
import edu.ur.ir.groupspace.GroupWorkspaceUserDAO;
import edu.ur.ir.groupspace.GroupWorkspaceUserInvite;
import edu.ur.ir.groupspace.GroupWorkspaceUserInviteDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserManager;

/**
 * Test the persistence of the invited group workspace group user data
 * access object
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class GroupWorkspaceUserInviteDAOTest {

	
	//get the application context 
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	// group workspace group data access
	GroupWorkspaceUserDAO groupWorkspaceUserDAO = (GroupWorkspaceUserDAO) ctx
	.getBean("groupWorkspaceUserDAO");

	// group workspace data access
	GroupWorkspaceDAO groupWorkspaceDAO = (GroupWorkspaceDAO) ctx
	.getBean("groupWorkspaceDAO");
	
	// invite data access object
	GroupWorkspaceUserInviteDAO groupWorkspaceUserInviteDAO = (GroupWorkspaceUserInviteDAO) ctx
	.getBean("groupWorkspaceUserInviteDAO");
	
	// Transaction manager
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
    		TransactionDefinition.PROPAGATION_REQUIRED);
	
    // user data access
     IrUserDAO userDAO= (IrUserDAO) ctx
     	.getBean("irUserDAO");

   	/** Properties file with testing specific information. */
 	PropertiesLoader propertiesLoader = new PropertiesLoader();
 	
 	/** Get the properties file  */
 	Properties properties = propertiesLoader.getProperties();
	
     

	
	/**
	 * Test persistence of inviting a user into a group workspace.
	 * @throws DuplicateNameException 
	 * @throws GroupWorkspaceInviteException 
	 */
	@Test
	public void basegroupWorkspaceEmailInviteDAOTest() throws DuplicateNameException, GroupWorkspaceInviteException{

		TransactionStatus ts = tm.getTransaction(td);

			
		// create a user
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		userDAO.makePersistent(user);
		
		IrUser user2 = userManager.createUser("passowrd2", "userName2");
		user2.setAccountExpired(true);
		user2.setAccountLocked(true);
		user2.setCredentialsExpired(true);
		userDAO.makePersistent(user2);
		
		// create group workspace and group
		GroupWorkspace groupSpace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupSpace);
        
 		
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);
		
	    GroupWorkspaceUserInvite invite = groupSpace.addInviteUser(user2, user, false, "test@mail.com");
	    invite.setInviteMessage("hello! come work with me");
	    groupWorkspaceUserInviteDAO.makePersistent(invite);
		//complete the transaction
		tm.commit(ts);
        
		ts = tm.getTransaction(td);
		GroupWorkspaceUserInvite other = groupWorkspaceUserInviteDAO.getById(invite.getId(), false);
		assert other.equals(invite) : "The user invite information should be equal invite = " + invite + " other = " + other;
		assert other.getEmail().equals("test@mail.com") : "Email should be equal but email = " + other.getEmail();
		assert other.getInviteMessage().equals("hello! come work with me") : "Message should be equal";
		assert other.getInvitingUser().equals(user) : "User should be equal user = " + user + " invitingUser = " + other.getInvitingUser();
		assert other.getInvitedUser().equals(user2) : "User should be equal user = " + user2 + " invitedUser = " + other.getInvitedUser();
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		groupWorkspaceUserInviteDAO.makeTransient(groupWorkspaceUserInviteDAO.getById(other.getId(), false));
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupSpace.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert groupWorkspaceUserInviteDAO.getById(other.getId(), false) == null : "Should not be able to find other";
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		userDAO.makeTransient(userDAO.getById(user2.getId(), false));
		tm.commit(ts);
	}
}
