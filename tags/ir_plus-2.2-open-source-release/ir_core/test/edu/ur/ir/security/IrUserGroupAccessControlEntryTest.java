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
import edu.ur.ir.security.IrUserGroupAccessControlEntry;
import edu.ur.ir.user.IrUserGroup;

/**
 * Test the group access control entry class.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrUserGroupAccessControlEntryTest {

	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 * @throws ClassNotFoundException 
	 */
	public void testGroupAccessControlEntrySets() throws ClassNotFoundException 
	{
		// protect the language type
		LanguageType languageType = new LanguageType();
		languageType.setId(44l);
		languageType.setName("languageName");

		// create the language type class
		IrClassType classType = new IrClassType(languageType.getClass());
		
		IrAcl irAcl = new IrAcl(languageType, classType);
		irAcl.setEntriesInheriting(true);
		
		// create the admin group
		IrUserGroup group = new IrUserGroup("admin");
		group.setId(89l);

		// create a user access control entry
		IrUserGroupAccessControlEntry gace = irAcl.createGroupAccessControlEntry(group);

		// create a class type permission for languages
		IrClassTypePermission classTypePermission = new IrClassTypePermission(classType);
		classTypePermission.setName("read");
		classTypePermission.setDescription("Gives users read permission");
		classTypePermission.setId(55l);
		classTypePermission.setVersion(33);

		// add the read permission 
		gace.addPermission(classTypePermission);
		
		// make sure the information is correctly set.
		assert gace.getAcl().equals(irAcl) : "UACE should contain parent irACl";
		assert gace.getIrClassTypePermissions().contains(classTypePermission) : "Should contain permissions";
		assert gace.getSid().equals(group) : "Sid should be a group";
	}
	
	/**
	 * Test adding and removing permissions.
	 * 
	 * @param description
	 * @throws ClassNotFoundException 
	 */
	public void testAddPermissionGroupEntry() throws ClassNotFoundException 
	{
		// protect the language type
		LanguageType languageType = new LanguageType();
		languageType.setId(44l);
		languageType.setName("languageName");
		
		IrClassType classType = new IrClassType(languageType.getClass());
		
		IrAcl irAcl = new IrAcl(languageType, classType);
		irAcl.setEntriesInheriting(true);
		
		// create the admin group
		IrUserGroup group1 = new IrUserGroup("admin");
		group1.setId(89l);
		

        //create the anonymous group
		IrUserGroup group2 = new IrUserGroup("anonymous");
		group2.setId(29l);
		
		IrUserGroupAccessControlEntry gace = irAcl.createGroupAccessControlEntry(group1);
		
		// create the class type permission
		IrClassTypePermission classTypePermission1 = new IrClassTypePermission(classType);
		classTypePermission1.setName("read");
		classTypePermission1.setDescription("Gives users read permission");
		classTypePermission1.setId(55l);
		classTypePermission1.setVersion(33);
		

		// give the group the read permission
		gace.addPermission(classTypePermission1);
		
		IrClassTypePermission classTypePermission2 = new IrClassTypePermission(classType);
		classTypePermission2.setName("write");
		classTypePermission2.setDescription("Gives users read permission");
		classTypePermission2.setId(55l);
		classTypePermission2.setVersion(33);
		
		// give the group write permission
		gace.addPermission(classTypePermission2);
		
		// check the permissions.
		assert gace.getIrClassTypePermissions().contains(classTypePermission1) : "Should contain permission";
		
		assert !gace.isGranting("read", group2) : "Permission should not be granted";
		
		assert gace.isGranting("read", group1) : "Permission should be granted";
		assert gace.isGranting("write", group1) : "Permission should be granted";
		
		gace.removePermission(classTypePermission1);
		
		assert !gace.isGranting("read", group1)  : "Permission should NOT be granted";
		assert gace.isGranting("write", group1)  : "Permission should be granted";
		
		assert gace.getPermissions().size() == 1 : "should equal 1";
		assert gace.getIrClassTypePermissions().contains(classTypePermission2) : "Should contain permission";
		assert !gace.getIrClassTypePermissions().contains(classTypePermission1) : "Should not contain permission";
	}
}
