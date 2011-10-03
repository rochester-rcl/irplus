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
import edu.ur.ir.groupspace.GroupWorkspaceEmailInvite;
import edu.ur.ir.groupspace.GroupWorkspaceUserDAO;
import edu.ur.ir.groupspace.GroupWorkspaceEmailInviteDAO;
import edu.ur.ir.groupspace.GroupWorkspaceInviteException;
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
public class GroupWorkspaceEmailInviteDAOTest 
{
	//get the application context 
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	// group workspace group data access
	GroupWorkspaceUserDAO groupWorkspaceUserDAO = (GroupWorkspaceUserDAO) ctx
	.getBean("groupWorkspaceUserDAO");

	// group workspace data access
	GroupWorkspaceDAO groupWorkspaceDAO = (GroupWorkspaceDAO) ctx
	.getBean("groupWorkspaceDAO");
	
	// invite data access object
	GroupWorkspaceEmailInviteDAO groupWorkspaceEmailInviteDAO = (GroupWorkspaceEmailInviteDAO) ctx
	.getBean("groupWorkspaceEmailInviteDAO");
	
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
	public void baseGroupWorkspaceEmailInviteDAOTest() throws DuplicateNameException, GroupWorkspaceInviteException{

		TransactionStatus ts = tm.getTransaction(td);

			
		// create a user
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		userDAO.makePersistent(user);
		
		// create group workspace and group
		GroupWorkspace groupSpace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupSpace);
        
 		
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);
		
	   
        GroupWorkspaceEmailInvite invite = groupSpace.addInviteUser("test@mail.com", user, "123test@mail.com");
        invite.getInviteToken().setInviteMessage("invite message");
		groupWorkspaceEmailInviteDAO.makePersistent(invite);
		
		//complete the transaction
		tm.commit(ts);
        
		ts = tm.getTransaction(td);
		GroupWorkspaceEmailInvite other = groupWorkspaceEmailInviteDAO.getById(invite.getId(), false);
		assert other.equals(invite) : "The user invite information should be equal invite = " + invite + " other = " + other;
		assert other.getInviteToken().getEmail().equals("test@mail.com") : "Email should be equal but email = " + other.getInviteToken().getEmail();
		assert other.getInviteToken().getInviteMessage().equals("invite message") : "Message should be equal";
		assert other.getInviteToken().getToken().equals("123test@mail.com") : "inviteInfo should be equal but is " + other.getInviteToken().getToken();
		assert other.getInviteToken().getInvitingUser().equals(user) : "User should be equal user = " + user + " invitingUser = " + other.getInviteToken().getInvitingUser();
		assert (groupWorkspaceEmailInviteDAO.getInviteInfoForToken("123test@mail.com")).equals(other) : "The user inviteInfo should be equal";
		List<GroupWorkspaceEmailInvite> invites = groupWorkspaceEmailInviteDAO.getInviteInfoByEmail("TeSt@Mail.com");
		assert invites.size() == 1 : "Should find one invite but found " + invites.size();
		assert invites.get(0).equals(invite) : "invite " + invites.get(0) + " should equal " + invite;
		
		
		invites = groupWorkspaceEmailInviteDAO.getInviteInfoByEmail("test@mail.com");
		assert invites.size() == 1 : "Should find one invite but found " + invites.size();
		assert invites.get(0).equals(invite) : "invite " + invites.get(0) + " should equal " + invite;
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		groupWorkspaceEmailInviteDAO.makeTransient(groupWorkspaceEmailInviteDAO.getById(other.getId(), false));
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupSpace.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert groupWorkspaceEmailInviteDAO.getById(other.getId(), false) == null : "Should not be able to find other";
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
	}
	
}
