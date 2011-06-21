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


package edu.ur.ir.user.service;

import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.ldap.core.DirContextOperations;
import org.testng.annotations.Test;

import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.user.LdapUserService;

/**
 * Test the service methods for the indexing user information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultUserLdapServiceTest {
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User data access */
	LdapUserService ldapUserService = (LdapUserService) ctx.getBean("ldapUserService");

	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	public void testUserSearch()
	{
	    String ldapUsername = (String)properties.get("ldap.user.name");
		DirContextOperations dirContextOperations = ldapUserService.searchForUser(ldapUsername);
		assert dirContextOperations != null : "operations object should not be null for " + ldapUsername;
	}
	
	
}
