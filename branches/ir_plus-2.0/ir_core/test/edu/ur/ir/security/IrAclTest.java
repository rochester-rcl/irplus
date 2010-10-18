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
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserEmail;


/**
 * Test the Access control List class.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrAclTest 
{
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 * @throws ClassNotFoundException 
	 */
	public void testAccessControlListSets() throws ClassNotFoundException 
	{
		
		// protect the language type
		LanguageType languageType = new LanguageType();
		languageType.setId(44l);
		languageType.setName("languageName");

		// create the language type class
		IrClassType classType = new IrClassType(languageType.getClass());

		IrAcl irAcl = new IrAcl(languageType, classType);
		irAcl.setEntriesInheriting(true);
		
		assert irAcl.getEntriesInheriting() : "Entries should be inheriting";
		assert irAcl.getClassType().equals(classType) : "Class type should be equal";
		assert irAcl.getParentAcl() == null : "Should not have a parent acl";
	}
	
	
	/**
	 * Test adding user access control entries 
	 * @throws ClassNotFoundException 
	 */
	public void testAddUserControlEntry() throws ClassNotFoundException
	{
		
		// protect the language type
		LanguageType languageType = new LanguageType();
		languageType.setId(44l);
		languageType.setName("languageName");
		
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
		
		IrUserAccessControlEntry uace = irAcl.createUserAccessControlEntry(user);
		
		uace.setId(27L);
		
		
		// create the class type permission
		IrClassTypePermission classTypePermission1 = new IrClassTypePermission(classType);

		classTypePermission1.setName("read");
		classTypePermission1.setDescription("Gives users read permission");
		classTypePermission1.setId(55l);
		classTypePermission1.setVersion(33);
		

		
		// give the user the read permission
		uace.addPermission(classTypePermission1);
		
		IrClassTypePermission classTypePermission2 = new IrClassTypePermission(classType);
		classTypePermission2.setName("read");
		classTypePermission2.setDescription("Gives users read permission");
		classTypePermission2.setId(55l);
		classTypePermission2.setVersion(33);

		
		// give the user write permission
		uace.addPermission(classTypePermission2);
				
		assert irAcl.getUserEntries().contains(uace) : "Should contain the user access control entry";
		assert irAcl.getUserAccessControlEntry(27L).equals(uace) : "Should find the access control entry";
	}
	
	/**
	 * Test adding and removing permissions.
	 * 
	 * @param description
	 * @throws ClassNotFoundException 
	 */
	public void testAddRoleEntry() throws ClassNotFoundException 
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
		
		IrUserGroupAccessControlEntry gace = irAcl.createGroupAccessControlEntry(group1);
		gace.setId(39l);
		
		// create the class type permission
		IrClassTypePermission classTypePermission1 = new IrClassTypePermission(classType);
		classTypePermission1.setName("read");
		classTypePermission1.setDescription("Gives users read permission");
		classTypePermission1.setId(55l);
		classTypePermission1.setVersion(33);

		// give the role the read permission
		gace.addPermission(classTypePermission1);
		
		IrClassTypePermission classTypePermission2 = new IrClassTypePermission(classType);
		classTypePermission2.setName("read");
		classTypePermission2.setDescription("Gives users read permission");
		classTypePermission2.setId(55l);
		classTypePermission2.setVersion(33);

		
		// give the role write permission
		gace.addPermission(classTypePermission2);
		
		assert irAcl.getGroupAccessControlEntry(39L).equals(gace) : 
			"IrAcl should contain the group access control entry";
		
		assert irAcl.getGroupEntries().contains(gace) : "IrAcl should contain the group access control entry";
	}
	
	/**
	 * Test adding and removing permissions.
	 * 
	 * @param description
	 * @throws ClassNotFoundException 
	 */
	public void testAddGroupEntry() throws ClassNotFoundException 
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
		race.setId(39l);
		
		
		// create the class type permission
		IrClassTypePermission classTypePermission1 = new IrClassTypePermission(classType);
		classTypePermission1.setName("read");
		classTypePermission1.setDescription("Gives users read permission");
		classTypePermission1.setId(55l);
		classTypePermission1.setVersion(33);

		// give the role the read permission
		race.addPermission(classTypePermission1);
		
		IrClassTypePermission classTypePermission2 = new IrClassTypePermission(classType);
		classTypePermission2.setName("read");
		classTypePermission2.setDescription("Gives users read permission");
		classTypePermission2.setId(55l);
		classTypePermission2.setVersion(33);
		
		
		// give the role write permission
		race.addPermission(classTypePermission2);
		
		assert irAcl.getRoleAccessControlEntry(39L).equals(race) : 
			"IrAcl should contain the role access control entry";
		
		assert irAcl.getRoleEntries().contains(race) : "IrAcl should contain the role access control entry";
	}
	
	/**
	 * Test use an access control entry with inheritance
	 * @throws ClassNotFoundException 
	 */
	public void testInheritance() throws ClassNotFoundException
	{
		// protect the language type
		LanguageType languageType = new LanguageType();
		languageType.setId(44l);
		languageType.setName("languageName");
		
		IrClassType classType = new IrClassType(languageType.getClass());
		
		IrAcl irAcl1 = new IrAcl();
		irAcl1.setEntriesInheriting(true);
		
		IrAcl parent = new IrAcl(languageType, classType);
		parent.setEntriesInheriting(false);
		
		irAcl1.setIrParentAcl(parent);
        
		assert irAcl1.getParentAcl().equals(parent) : "Should have a parent";
		
		// create the admin role
		IrRole role1 = new IrRole();
		role1.setName("admin");
		role1.setId(89l);
		
		IrRoleAccessControlEntry race = parent.createRoleAccessControlEntry(role1);
		race.setId(39l);

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
		
		
		assert irAcl1.isGranted("read", role1, false) : "Permission should be granted by parent";
		
		irAcl1.setEntriesInheriting(false);
		
		assert !irAcl1.isGranted("read", role1, false) : "Permission should not be granted by parent";
	}


}
