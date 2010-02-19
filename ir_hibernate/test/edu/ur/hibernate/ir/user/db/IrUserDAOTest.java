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

import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.ir.user.ExternalAccountType;
import edu.ur.ir.user.ExternalAccountTypeAlreadyExistsException;
import edu.ur.ir.user.ExternalAccountTypeDAO;
import edu.ur.ir.user.ExternalUserAccount;
import edu.ur.ir.user.ExternalUserAccountDAO;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrRoleDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserManager;

/**
 * Test the persistence methods for User Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrUserDAOTest {

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	// Transaction manager
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
    
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

	/** user data access */
     IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");

     /** role data access */
     IrRoleDAO irRoleDAO= (IrRoleDAO) ctx.getBean("irRoleDAO");
     
     /** external account data access */
 	 ExternalAccountTypeDAO externalAccountTypeDAO = (ExternalAccountTypeDAO) ctx.getBean("externalAccountTypeDAO");
 	 
     /** external account data access */
 	 ExternalUserAccountDAO externalUserAccountDAO = (ExternalUserAccountDAO) ctx.getBean("externalUserAccountDAO");

	/**
	 * Test PersonName persistance
	 */
	@Test
	public void baseUserDAOTest()throws Exception{
		
		UserEmail userEmail = new UserEmail("user@email");
  		
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		user.setPersonalIndexFolder("personalIndexFolder");

		userDAO.makePersistent(user);
        TransactionStatus ts = tm.getTransaction(td);
        
		IrUser other = userDAO.getById(user.getId(), false);
		assert other.equals(user) : "The userers should be equal";
		assert user.getAccountExpired() : "Account should be set to expired";
		assert user.getAccountLocked() : "Account should be set to locked";
		assert user.getCredentialsExpired() : "Credentials should be set to expired";
		assert user.getPersonalIndexFolder().equals("personalIndexFolder") : 
			"Personal index folder should equal personalIndexFolder but is " + user.getPersonalIndexFolder();

		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		userDAO.makeTransient(userDAO.getById(other.getId(), false));
		tm.commit(ts);
		
		assert userDAO.getById(other.getId(), false) == null : "Should not be able to find other";
	}
	
	
	/**
	 * Test adding roles to the user
	 */
	public void testAddRoles()throws Exception
	{
		// create the roles for this user.
		IrRole role1 = new IrRole();
		role1.setName("roleName1");
 		role1.setDescription("roleDescription");
         
 		irRoleDAO.makePersistent(role1);
		
		IrRole role2 = new IrRole();
		role2.setName("roleName2");
 		role2.setDescription("roleDescription2");
 		irRoleDAO.makePersistent(role2);
		
		UserEmail userEmail = new UserEmail("user@email");
  		
 		// create the user and test.
		IrUser user = new IrUser("userName", "passowrd");
		user.addUserEmail(userEmail, true);
		user.setPasswordEncoding("none");
		
		user.addRole(role1);
		user.addRole(role2);
		
		userDAO.makePersistent(user);
	
        TransactionStatus ts = tm.getTransaction(td);
        
		IrUser other = userDAO.getById(user.getId(), false);
		Set<IrRole> roles = other.getRoles();

		assert roles.contains(role1) : "Role 1 should be found for this user";
		assert roles.contains(role2) : "Role 2 should be found for this user";
		
		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		
		assert userDAO.getById(other.getId(), false) == null : "Should not be able to find other";
		
		irRoleDAO.makeTransient(role1);
		irRoleDAO.makeTransient(role2);
	}
	
	/**
	 * Test PersonName persistance
	 */
	@Test
	public void userWithNameDAOTest()throws Exception{
		
		
		UserEmail userEmail = new UserEmail("user@email");
 
		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.setLastName("familyName");
		user.setFirstName("forename");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		
		userDAO.makePersistent(user);
		
        // Start the transaction this is for lazy loading
        TransactionStatus ts = tm.getTransaction(td);
        
		IrUser other = userDAO.getById(user.getId(), false);
		assert other.equals(user) : "The userers should be equal";
		assert user.getFirstName().equals("forename") : "First name should be equal";
		assert user.getLastName().equals("familyName") : "Last name should be equal";
		assert user.getAccountExpired() : "Account should be set to expired";
		assert user.getAccountLocked() : "Account should be set to locked";
		assert user.getCredentialsExpired() : "Credentials should be set to expired";

		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		userDAO.makeTransient(userDAO.getById(other.getId(), false));
		tm.commit(ts);
		
		assert userDAO.getById(other.getId(), false) == null : "Should not be able to find other";
		
	}
	
	/**
	 * Test PersonName persistance
	 */
	@Test
	public void userByTokenTest()throws Exception{
		
		UserEmail userEmail = new UserEmail("user@email");
  		
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		user.setPasswordToken("passwordToken");

		userDAO.makePersistent(user);
        TransactionStatus ts = tm.getTransaction(td);
        
		IrUser other = userDAO.getUserByToken("passwordToken");
		assert other.equals(user) : "The user should be equal";
		assert user.getPasswordToken().equals("passwordToken") : "Password token should be equal to - passwordToken";

		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		userDAO.makeTransient(userDAO.getById(other.getId(), false));
		tm.commit(ts);
		
		assert userDAO.getById(other.getId(), false) == null : "Should not be able to find other";
	}
	
	/**
	 * Test adding external account information to a user.
	 * 
	 * @throws ExternalAccountTypeAlreadyExistsException 
	 */
	@Test
	public void testAddExternalAccount() throws ExternalAccountTypeAlreadyExistsException
	{
		TransactionStatus ts = tm.getTransaction(td);
		ExternalAccountType externalAccountType1 = new ExternalAccountType("externalAccountTypeName1");
		externalAccountType1.setDescription("description1");
        externalAccountTypeDAO.makePersistent(externalAccountType1);
        
        ExternalAccountType externalAccountType2 = new ExternalAccountType("externalAccountTypeName2");
		externalAccountType2.setDescription("description2");
        externalAccountTypeDAO.makePersistent(externalAccountType2);
 	    tm.commit(ts);
		
	
		ts = tm.getTransaction(td);
		UserEmail userEmail = new UserEmail("user@email");
 		// create the user and test.
		IrUser user = new IrUser("userName", "passowrd");
		user.addUserEmail(userEmail, true);
		user.setPasswordEncoding("none");
		user.createExternalUserAccount( "user_account_name1", externalAccountType1);
		ExternalUserAccount userAccount1 = user.getExternalAccount();
		userDAO.makePersistent(user);
	    tm.commit(ts);
	    
	    
        ts = tm.getTransaction(td);
        
		IrUser other = userDAO.getById(user.getId(), false);
		ExternalUserAccount otherAccount = other.getExternalAccount();

		assert otherAccount.equals(userAccount1) : "Account "+ userAccount1 + " should be found for this user";
		
		List<ExternalUserAccount> otherAccounts = externalUserAccountDAO.getByExternalUserName("user_account_name1");
		assert otherAccounts.size() == 1 : "accounts size should be 1 but is " + otherAccounts.size();
		assert otherAccounts.contains(userAccount1) : "Should contain user account " + userAccount1;
	
		ExternalUserAccount otherAccount2 = otherAccounts.get(0);
		assert otherAccount2.getUser().equals(other) : "other account should point to user " + other + " but is " + otherAccount.getUser(); 
		
		other.deleteExternalUserAccount();
		externalUserAccountDAO.makeTransient(otherAccount);
		//complete the transaction
		tm.commit(ts);
		
		// switch to a new external account
		ts = tm.getTransaction(td);
		other = userDAO.getById(user.getId(), false);
		
		// switch the account for the user
		other.createExternalUserAccount( "user_account_name2", externalAccountType2);
		ExternalUserAccount userAccount2 = other.getExternalAccount();
		
		userDAO.makePersistent(other);
		
		//complete the transaction
		tm.commit(ts);
		
		// make sue the new account exists and the old one is deleted
		ts = tm.getTransaction(td);
	        
		other = userDAO.getById(user.getId(), false);
		otherAccount = other.getExternalAccount();

		assert otherAccount.equals(userAccount2) : "Account "+ userAccount2 + " should be found for this user";
		assert !otherAccount.equals(userAccount1) : "Account "+ userAccount1 + " should NOT be found for this user";
			
		otherAccounts = externalUserAccountDAO.getByExternalUserName("user_account_name2");
		assert otherAccounts.size() == 1 : "accounts size should be 1 but is " + otherAccounts.size();
		assert otherAccounts.contains(userAccount2) : "Should contain user account " + userAccount2;
		
		otherAccount2 = otherAccounts.get(0);
		assert otherAccount2.getUser().equals(other) : "other account should point to user " + other + " but is " + otherAccount2.getUser(); 

		// old account should no longer exist
		otherAccounts = externalUserAccountDAO.getByExternalUserName("user_account_name1");
		assert otherAccounts.size() == 0 : "accounts size should be 0 but is " + otherAccounts.size();
		
		ExternalUserAccount externalAccountByType1 = externalUserAccountDAO.getByExternalUserNameAccountType("user_account_name2", otherAccount.getExternalAccountType());
		assert externalAccountByType1.equals(otherAccount) : "Account by type name should = " + otherAccount + " but equals " + externalAccountByType1;
			
			
		//complete the transaction
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		
		
		assert userDAO.getById(other.getId(), false) == null : "Should not be able to find other";
		
		externalAccountTypeDAO.makeTransient(externalAccountTypeDAO.getById(externalAccountType1.getId(), false));
		externalAccountTypeDAO.makeTransient(externalAccountTypeDAO.getById(externalAccountType2.getId(), false));
		tm.commit(ts);
	}

}
