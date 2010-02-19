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


package edu.ur.ir.security.service;

import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.security.Authentication;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;

/**
 * Test class for connecting to ldap
 * 
 * @author Nathan Sarr
 *
 */
public class UrLdapAuthenticationProviderTest {
	
	/** Application context  for loading information using spring*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");
	
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

	/** User data access */
	UrLdapAuthenticationProvider ldapAuthProvider = (UrLdapAuthenticationProvider) ctx.getBean("ldapAuthProvider");
	
	/**
	 * Test the service methods for authenticating/looking up user information using ldap
	 * 
	 * @author Nathan Sarr
	 * 
	 */
	@Test(groups = { "baseTests" }, enabled = true)
	public void testAuthentication() throws UserHasPublishedDeleteException, UserDeletedPublicationException
	{
 	    // Start the transaction this is for lazy loading
		TransactionStatus ts = tm.getTransaction(td);
		UserEmail email = new UserEmail("email");

	    String ldapUsername = (String)properties.get("ldap.user.name");
	    String ldapPassword = (String)properties.get("ldap.user.password");

		IrUser user = userService.createUser("password","username",email);
		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);
		LdapAuthenticationToken authRequest = new LdapAuthenticationToken(ldapUsername, ldapPassword);
		Authentication authentication = ldapAuthProvider.authenticate(authRequest);
		assert authentication.isAuthenticated() : "user should be authenticated" + authentication.getDetails() ;
		
       
		
		IrUser deleteUser = userService.getUser(user.getId(), false);
		userService.deleteUser(deleteUser,deleteUser);
		
		tm.commit(ts);
		
	}

}
