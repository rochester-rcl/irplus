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

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;

/**
 * Test the Group Class
 * 
 * @author Nathan Sarr
 * 
 */

@Test(groups = { "baseTests" }, enabled = true)
public class IrUserGroupTest {
	
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicGroupSets() 
	{
		IrUserGroup group = new IrUserGroup();
		group.setName("groupName");
		group.setDescription("groupDescription");
		group.setId(55l);
		group.setVersion(33);
		
		assert group.getName().equals("groupName") : "Should equal groupName";
		assert group.getDescription().equals("groupDescription") : "Shoud equal groupDescription";
		assert group.getId().equals(55l) : "Should equal 55l";
		assert group.getVersion() == 33 : "Should equal 33";
	}
	
	/**
	 * Test adding an removing a user
	 */
	public void testGroupUser()
	{
		IrUserGroup group = new IrUserGroup("groupName");
        
		UserEmail userEmail = new UserEmail();
		userEmail.setEmail("test@hotmail.com");
		
		IrUser user = new IrUser();
		user.setLastName("familyName");
		user.setFirstName("forename");
		user.setId(4L);
		user.setPassword("password");
		user.addUserEmail(userEmail, true);
		user.setVersion(5);
		
		group.addUser(user);
		
		assert group.getUsers().contains(user) : "Should contain user";
		assert group.removeUser(user): "Should be able to remove user";
		assert !group.getUsers().contains(user) : "Should contain user";
		assert group.getUsers().size() == 0 : "Should have zero users";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		IrUserGroup group1 = new IrUserGroup();
		group1.setName("groupName");
		group1.setDescription("groupDescription");
		group1.setId(55l);
		group1.setVersion(33);
		
		IrUserGroup group2 = new IrUserGroup();
		group2.setName("groupName2");
		group2.setDescription("groupDescription2");
		group2.setId(55l);
		group2.setVersion(33);

		IrUserGroup group3 = new IrUserGroup();
		group3.setName("groupName");
		group3.setDescription("groupDescription");
		group3.setId(55l);
		group3.setVersion(33);
		
		assert group1.equals(group3) : "Groups should be equal";
		assert !group1.equals(group2) : "Groups should not be equal";
		
		assert group1.hashCode() == group3.hashCode() : "Hash codes should be the same";
		assert group2.hashCode() != group3.hashCode() : "Hash codes should not be the same";
	}

}
