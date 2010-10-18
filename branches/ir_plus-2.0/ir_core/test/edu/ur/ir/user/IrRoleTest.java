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

package edu.ur.ir.user;

import org.testng.annotations.Test;

import edu.ur.ir.user.IrRole;

/**
 * Test the Role Class
 * 
 * @author Nathan Sarr
 * 
 */

@Test(groups = { "baseTests" }, enabled = true)
public class IrRoleTest {

	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicRoleSets() 
	{
		IrRole role = new IrRole();
		role.setName("roleName");
		role.setDescription("roleDescription");
		role.setId(55l);
		role.setVersion(33);
		
		assert role.getName().equals("roleName") : "Should equal roleName";
		assert role.getDescription().equals("roleDescription") : "Shoud equal roleDescription";
		assert role.getId().equals(55l) : "Should equal 55l";
		assert role.getVersion() == 33 : "Should equal 33";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		IrRole role1 = new IrRole();
		role1.setName("roleName");
		role1.setDescription("roleDescription");
		role1.setId(55l);
		role1.setVersion(33);
		
		IrRole role2 = new IrRole();
		role2.setName("roleName2");
		role2.setDescription("roleDescription2");
		role2.setId(55l);
		role2.setVersion(33);

		IrRole role3 = new IrRole();
		role3.setName("roleName");
		role3.setDescription("roleDescription");
		role3.setId(55l);
		role3.setVersion(33);
		
		assert role1.equals(role3) : "Roles should be equal";
		assert !role1.equals(role2) : "Roles should not be equal";
		
		assert role1.hashCode() == role3.hashCode() : "Hash codes should be the same";
		assert role2.hashCode() != role3.hashCode() : "Hash codes should not be the same";
	}
}
