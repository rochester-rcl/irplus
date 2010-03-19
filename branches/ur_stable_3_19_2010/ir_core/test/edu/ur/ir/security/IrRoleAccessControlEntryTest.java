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
import edu.ur.ir.user.IrRole;

/**
 * Test the role access control entry class.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrRoleAccessControlEntryTest {
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 * @throws ClassNotFoundException 
	 */
	public void testRoleAccessControlEntrySets() throws ClassNotFoundException 
	{
		// protect the language type
		LanguageType languageType = new LanguageType();
		languageType.setId(44l);
		languageType.setName("languageName");

		// create the language type class
		IrClassType classType = new IrClassType(languageType.getClass());

		IrAcl irAcl = new IrAcl(languageType, classType);
		irAcl.setEntriesInheriting(true);
		
		// create the admin role
		IrRole role = new IrRole();
		role.setName("admin");
		role.setId(89l);

		
		// create a user access control entry
		IrRoleAccessControlEntry race = irAcl.createRoleAccessControlEntry(role);
		
		// create a class type permission for languages
		IrClassTypePermission classTypePermission = new IrClassTypePermission(classType);
		classTypePermission.setName("read");
		classTypePermission.setDescription("Gives users read permission");
		classTypePermission.setId(55l);
		classTypePermission.setVersion(33);

		// add the read permission 
		race.addPermission(classTypePermission);
		
		// make sure the information is correctly set.
		assert race.getAcl().equals(irAcl) : "UACE should contain parent irACl";
		assert race.getIrAcl().equals(irAcl) : "UACE should contain parent irACL";
		assert race.getIrClassTypePermissions().contains(classTypePermission) : "UACE should contain class type permission";
		assert race.getSid().equals(role) : "Sid should be a role";
	}
	
	/**
	 * Test adding and removing permissions.
	 * 
	 * @param description
	 * @throws ClassNotFoundException 
	 */
	public void testAddPermissionRoleEntry() throws ClassNotFoundException 
	{
		// protect the language type
		LanguageType languageType = new LanguageType();
		languageType.setId(44l);
		languageType.setName("languageName");
		
		IrClassType classType = new IrClassType(languageType.getClass());
		
		// create the admin role
		IrRole role1 = new IrRole();
		role1.setName("admin");
		role1.setId(89l);

		IrAcl irAcl = new IrAcl(languageType, classType);
		irAcl.setEntriesInheriting(true);
		
		IrRoleAccessControlEntry race = irAcl.createRoleAccessControlEntry(role1);
		
		// create the class type permission
		IrClassTypePermission classTypePermission1 = new IrClassTypePermission(classType);
		classTypePermission1.setName("read");
		classTypePermission1.setDescription("Gives users read permission");
		classTypePermission1.setId(55l);
		classTypePermission1.setVersion(33);

		// give the role the read permission
		race.addPermission(classTypePermission1);
		
		IrClassTypePermission classTypePermission2 = new IrClassTypePermission(classType);
		classTypePermission2.setName("write");
		classTypePermission2.setDescription("Gives users write permission");
		classTypePermission2.setId(55l);
		classTypePermission2.setVersion(33);
		
		// give the role write permission
		race.addPermission(classTypePermission2);
		
		// check the permissions.
		assert race.getIrClassTypePermissions().contains(classTypePermission1) : "Should contain permission";
		
		assert race.isGranting("read", role1) : "Read permission should be granted";
		assert race.isGranting("write", role1) : "Write permission should be granted";
		
		race.removePermission(classTypePermission1);
		
		assert !race.isGranting("read", role1)  : "Read permission should NOT be granted";
		assert race.isGranting("write", role1)  : "Write permission should be granted";
		
		assert race.getPermissions().size() == 1 : "should equal 1";
		assert race.getIrClassTypePermissions().contains(classTypePermission2) : "Should contain permission";
		assert !race.getIrClassTypePermissions().contains(classTypePermission1) : "Should not contain permission";
	}

}
