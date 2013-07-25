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

package edu.ur.hibernate.ir.user.db;


import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserEmailDAO;
import edu.ur.ir.user.UserManager;

/**
 * Test for persisting user email
 * 
 * @author Sharmila Ranganathan
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class UserEmailDAOTest {

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	// Transaction manager
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
    		TransactionDefinition.PROPAGATION_REQUIRED);
	
     IrUserDAO userDAO= (IrUserDAO) ctx
	.getBean("irUserDAO");

	UserEmailDAO emailDAO = (UserEmailDAO) ctx
		.getBean("userEmailDAO");

     
	/**
	 * Test PersonName persistance
	 */
	@Test
	public void baseUserEmailDAOTest()throws Exception{
		TransactionStatus ts = tm.getTransaction(td);

		UserEmail email = new UserEmail("a@b.com");
		
		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		userDAO.makePersistent(user);
		
		user.addUserEmail(email, true);
		emailDAO.makePersistent(email);
		
		
		UserEmail other = emailDAO.getById(email.getId(), false);
		assert other.equals(email) : "other should be equal to email";
		
		//complete the transaction
		tm.commit(ts);

        ts = tm.getTransaction(td);
		assert ((emailDAO.getById(email.getId(), false)).getEmail()).equals("a@b.com"): "Email should be equal to a@b.com";
		
		userDAO.makeTransient( userDAO.getById(user.getId(), false));
		tm.commit(ts);
			

		
	}
	
	public void userEmailNoDefaultEmailDAOTest()throws Exception{
		

		UserEmail email = new UserEmail("a@b.com");
		
		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		

        // Start the transaction this is for lazy loading
        TransactionStatus ts = tm.getTransaction(td);

		userDAO.makePersistent(user);
		user.addUserEmail(email, false);
		emailDAO.makePersistent(email);
        
		IrUser other = userDAO.getById(user.getId(), false);
		UserEmail tempEmail = other.getUserEmail(email.getId());
	
		assert tempEmail.equals(email) : "other should be equal to email";
		assert other.getDefaultEmail() == null : "Default name should be null";
		
		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
				
		// make sure we can delete the name
		emailDAO.makeTransient(emailDAO.getById(email.getId(), false));
//		assert emailDAO.getById(tempEmail.getId(), false) == null : "Should no longer be able to find user email";	
		
		user = userDAO.getById(user.getId(), false);
		userDAO.makeTransient(user);
		//complete the transaction
		tm.commit(ts);

	}

	
}
