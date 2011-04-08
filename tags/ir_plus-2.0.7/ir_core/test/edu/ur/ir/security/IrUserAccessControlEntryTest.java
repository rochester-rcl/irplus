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

package edu.ur.ir.security;

import org.testng.annotations.Test;

import edu.ur.ir.item.LanguageType;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassType;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserAccessControlEntry;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserEmail;

/**
 * Test the User access control entry classes.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrUserAccessControlEntryTest {

	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 * @throws ClassNotFoundException 
	 */
	public void testUserAccessControlEntrySets() throws ClassNotFoundException 
	{
		// protect the language type
		LanguageType languageType = new LanguageType();
		languageType.setId(44l);
		languageType.setName("languageName");

		// create the language type class
		IrClassType classType = new IrClassType(languageType.getClass());
		
		IrAcl irAcl = new IrAcl(languageType, classType);
		irAcl.setEntriesInheriting(true);
		
		UserEmail userEmail = new UserEmail("test@hotmail.com");
		
		IrUser user = new IrUser();
		user.setLastName("familyName");
		user.setFirstName("forename");
		user.setId(4L);
		user.setPassword("password");
		user.addUserEmail(userEmail, true);
		user.setVersion(5);
		
		// create a user access control entry
		IrUserAccessControlEntry uace = irAcl.createUserAccessControlEntry(user);
		
		// create a class type permission for languages
		IrClassTypePermission classTypePermission = new IrClassTypePermission(classType);
		classTypePermission.setName("read");
		classTypePermission.setDescription("Gives users read permission");
		classTypePermission.setId(55l);
		classTypePermission.setVersion(33);

		// add the read permission 
		uace.addPermission(classTypePermission);
		
		// make sure the information is correctly set.
		assert uace.getAcl().equals(irAcl) : "UACE should contain parent irACl";
		assert uace.getIrClassTypePermissions().contains(classTypePermission):"Should contain permission";
		assert uace.getSid().equals(user) : "Sid should be a user";
	}
	
	/**
	 * Test adding and removing permissions.
	 * 
	 * @param description
	 * @throws ClassNotFoundException 
	 */
	public void testAddPermissionUserEntry() throws ClassNotFoundException 
	{
		// protect the language type
		LanguageType languageType = new LanguageType();
		languageType.setId(44l);
		languageType.setName("languageName");
		
		IrClassType classType = new IrClassType(languageType.getClass());
		
		IrAcl irAcl = new IrAcl(languageType, classType);
		irAcl.setEntriesInheriting(true);
		
			
		IrUser user = new IrUser();
		user.setLastName("familyName");
		user.setFirstName("forename");
		user.setId(4L);
		user.setPassword("password");
		user.setUsername("username1");
		user.setVersion(5);
		
		IrUserAccessControlEntry uace = irAcl.createUserAccessControlEntry(user);
		
		
		// create the class type permission
		IrClassTypePermission classTypePermission1 = new IrClassTypePermission(classType);
		classTypePermission1.setName("read");
		classTypePermission1.setDescription("Gives users read permission");
		classTypePermission1.setId(55l);
		classTypePermission1.setVersion(33);
		

		// give the user the read permission
		uace.addPermission(classTypePermission1);
		
		IrClassTypePermission classTypePermission2 = new IrClassTypePermission(classType);
		classTypePermission2.setName("write");
		classTypePermission2.setDescription("Gives users write permission");
		classTypePermission2.setId(55l);
		classTypePermission2.setVersion(33);
		
		// give the user write permission
		uace.addPermission(classTypePermission2);
				
		
		// check the permissions.
		assert uace.getIrClassTypePermissions().contains(classTypePermission1) : "Should contain permission";
		
		assert uace.isGranting("read", user) : "Permission should be granted";
		assert uace.isGranting("write", user) : "Permission should be granted";
		
		uace.removePermission(classTypePermission1);
		
		assert !uace.isGranting("read", user)  : "Permission should NOT be granted";
		assert uace.isGranting("write", user)  : "Permission should be granted";
		
		assert uace.getPermissions().size() == 1 : "should equal 1";
		assert uace.getIrClassTypePermissions().contains(classTypePermission2) : "Should contain permission";
		assert !uace.getIrClassTypePermissions().contains(classTypePermission1) : "Should not contain permission";
		
	}
}
