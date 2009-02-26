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

import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.file.db.FolderInfo;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.repository.Repository;
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
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
    		TransactionDefinition.PROPAGATION_REQUIRED);
    
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

	
     IrUserDAO userDAO= (IrUserDAO) ctx
	.getBean("irUserDAO");

     IrRoleDAO irRoleDAO= (IrRoleDAO) ctx
		.getBean("irRoleDAO");

	/**
	 * Test PersonName persistance
	 */
	@Test
	public void baseUserDAOTest()throws Exception{
		
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");
  		
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);

		userDAO.makePersistent(user);
        TransactionStatus ts = tm.getTransaction(td);
        
		IrUser other = userDAO.getById(user.getId(), false);
		assert other.equals(user) : "The userers should be equal";
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
		
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");
  		
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
		
		
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");
 
		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.setLastName("familyName");
		user.setFirstName("forename");
		user.setLdapUserName("ldapUserName");
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
		assert user.getLdapUserName().equals("ldapUserName") : "LDAP user name should be equal";

		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		IrUser ldapUser = userDAO.findByLdapUserName(user.getLdapUserName());
		assert ldapUser != null : "could not find user by " + user.getLdapUserName();
		userDAO.makeTransient(userDAO.getById(other.getId(), false));
		tm.commit(ts);
		
		assert userDAO.getById(other.getId(), false) == null : "Should not be able to find other";
		
	}
	
	/**
	 * Test PersonName persistance
	 */
	@Test
	public void userByTokenTest()throws Exception{
		
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");
  		
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
	 * Test PersonName persistance
	 */
	@Test
	public void createPersonalIndexFolderTest()throws Exception{
		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer",
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		// save the repository
		tm.commit(ts);
		
        // create a user and add an indexed folder to the user 
		ts = tm.getTransaction(td);

		FolderInfo indexFolder = repo.getFileDatabase().createFolder("personalFolder");
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("none");
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");
		user.addUserEmail(userEmail, true);
		user.setPersonalIndexFolder(indexFolder);
		
		// create the user
		userDAO.makePersistent(user);

		tm.commit(ts);
		
		
        // clean up
		ts = tm.getTransaction(td);
		FolderInfo info = user.getPersonalIndexFolder();
		assert  info != null : 
			"personal index folder should not be null";
		
		assert info.equals(indexFolder) : "info " + info + 
		" should be equal to index folder " + indexFolder;
		
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	


	}
	

}
