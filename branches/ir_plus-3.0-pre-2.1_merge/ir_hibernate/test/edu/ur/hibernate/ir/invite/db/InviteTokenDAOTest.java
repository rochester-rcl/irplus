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


package edu.ur.hibernate.ir.invite.db;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.invite.InviteToken;
import edu.ur.ir.invite.InviteTokenDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserManager;

/**
 *  Test class for persistent methods of InviteInfo
 *  
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class InviteTokenDAOTest {

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	// Transaction manager
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
    		TransactionDefinition.PROPAGATION_REQUIRED);
	
     IrUserDAO userDAO= (IrUserDAO) ctx
     	.getBean("irUserDAO");

     InviteTokenDAO inviteTokenDAO= (InviteTokenDAO) ctx
 		.getBean("inviteTokenDAO");
     
     
	/**
	 * Test Invite user persistence
	 */
	@Test
	public void baseInviteInfoDAOTest()throws Exception{

		TransactionStatus ts = tm.getTransaction(td);

	
		
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		userDAO.makePersistent(user);
		
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);

		InviteToken inviteToken = new InviteToken("test@mail.com", "123", user);
		inviteToken.setInviteMessage("invite message");

		inviteTokenDAO.makePersistent(inviteToken);
		
		//complete the transaction
		tm.commit(ts);
        
		ts = tm.getTransaction(td);
		InviteToken other = inviteTokenDAO.getById(inviteToken.getId(), false);
		assert other.equals(inviteToken) : "The token information should be equal";
		assert inviteToken.getEmail() == "test@mail.com" : "Email should be equal but is : " + inviteToken.getEmail();
		assert inviteToken.getInviteMessage() == "invite message" : "Message should be equal but is : " + inviteToken.getInviteMessage();
		assert inviteToken.getToken() == "123" : "inviteInfo should be equal : " + inviteToken.getToken();
		assert inviteToken.getInvitingUser().equals(user) : "User should be equal ";
		assert (inviteTokenDAO.getByToken("123")).equals(inviteToken) : "The user inviteTokens should be equal";
		List<InviteToken> invites = inviteTokenDAO.getByEmail("TeSt@Mail.com");
		assert invites.size() == 1 : "Should find one invite but found " + invites.size();
		assert invites.get(0).equals(inviteToken) : "invite " + invites.get(0) + " should equal " + inviteToken;
		
		
		invites = inviteTokenDAO.getByEmail("test@mail.com");
		assert invites.size() == 1 : "Should find one invite but found " + invites.size();
		assert invites.get(0).equals(inviteToken) : "invite " + invites.get(0) + " should equal " + inviteToken;
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		inviteTokenDAO.makeTransient(inviteTokenDAO.getById(other.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert inviteTokenDAO.getById(other.getId(), false) == null : "Should not be able to find other";
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		
		
		
	}
}
