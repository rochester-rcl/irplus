
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

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.ir.repository.VersionedLicense;
import edu.ur.ir.repository.VersionedLicenseDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserManager;
import edu.ur.ir.user.UserRepositoryLicense;
import edu.ur.ir.user.UserRepositoryLicenseDAO;

/**
 * Test the data access of user repository license information.
 * 
 * @author Nathan Sarr
 *
 */
public class UserRepositoryLicenseDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	// Transaction manager
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
    
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

	/** user data access object */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");

    /** versioned license data access  */
    VersionedLicenseDAO versionedLicenseDAO = (VersionedLicenseDAO) ctx.getBean("versionedLicenseDAO");

    /** user repository license DAO */
    UserRepositoryLicenseDAO userRepositoryLicenseDAO = (UserRepositoryLicenseDAO)ctx.getBean("userRepositoryLicenseDAO");
	/**
	 * Test PersonName persistance
	 */
	@Test
	public void baseUserRepositoryLicenseDAOTest()throws Exception{
		

		// create a user
  		UserManager userManager = new UserManager();
  		UserEmail userEmail = new UserEmail("user@email");
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		user.setPersonalIndexFolder("personalIndexFolder");
		
		TransactionStatus ts = tm.getTransaction(td);
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		// create a versioned license
		VersionedLicense versionedLicense = new VersionedLicense(user, "this is license text", "Main License");
		versionedLicenseDAO.makePersistent(versionedLicense);
		
		UserRepositoryLicense userAcceptedLicense = user.addAcceptedLicense(versionedLicense.getCurrentVersion());
		userDAO.makePersistent(user);
        
		IrUser other = userDAO.getById(user.getId(), false);
		
		UserRepositoryLicense otherUserLicense = user.getAcceptedLicense(versionedLicense.getCurrentVersion());
		assert otherUserLicense.equals(userAcceptedLicense) : "other license = " + otherUserLicense + 
				" should equal user accepted license = " + userAcceptedLicense;
		
		//complete the transaction
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		otherUserLicense = userRepositoryLicenseDAO.getById(userAcceptedLicense.getId(), false);
		
		assert otherUserLicense != null : "Should be able to find userAcceptedLicense for id " 
		+ userAcceptedLicense.getId();
		
		// delete the user accepted license
		userRepositoryLicenseDAO.makeTransient(otherUserLicense);
		
		// make the versioned license transient
		versionedLicenseDAO.makeTransient(versionedLicenseDAO.getById(versionedLicense.getId(), false));
		tm.commit(ts);
		
		// delete the user
		ts = tm.getTransaction(td);
		userDAO.makeTransient(userDAO.getById(other.getId(), false));
		tm.commit(ts);
		
		assert userDAO.getById(other.getId(), false) == null : "Should not be able to find other";
	}
	
	

}
