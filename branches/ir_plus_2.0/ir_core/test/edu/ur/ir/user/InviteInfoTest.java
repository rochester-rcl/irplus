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

import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.security.IrClassType;
import edu.ur.ir.security.IrClassTypePermission;

/**
 * Test for Invite information
 * 
 * @author Sharmila Ranganathan
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class InviteInfoTest {

	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasics() 
	{
		IrUser user = new IrUser();
		user.setId(4L);
		user.setUsername("username");
		user.setVersion(5);
		
		IrClassType classType = null;
		try {
			classType = new IrClassType(VersionedFile.class);
		} catch (ClassNotFoundException e) {
			
		}
		
		IrClassTypePermission permission = new IrClassTypePermission(classType);
		permission.setName("read");
		
		InviteInfo token = new  InviteInfo();
		token.setEmail("test@ufr.com");
		token.setToken("qwerty12345");
		token.addPermission(permission);
		token.setUser(user);
		token.setInviteMessage("invite message!");
		
		
		assert token.getEmail().equals("test@ufr.com") : "Should equal test@ufr.com";
		assert token.getToken().equals("qwerty12345") : "Shoud equal qwerty12345";
		assert token.getPermissions().contains(permission) : "Shoud contain the permission";
		assert token.getUser().equals(user) : "User should be equal";
		assert token.getInviteMessage().equals("invite message!") : "Message should be equal";

	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{

		IrClassType classType = null;
		try {
			classType = new IrClassType(VersionedFile.class);
		} catch (ClassNotFoundException e) {
			
		}
		
		IrClassTypePermission permission = new IrClassTypePermission(classType);
		permission.setName("read");
		
		IrClassTypePermission permission1 = new IrClassTypePermission(classType);
		permission1.setName("write");

		
		InviteInfo token1 = new InviteInfo();
		token1.setEmail("test@ufr.com");
		token1.setToken("qwerty12345");
		token1.addPermission(permission);
		token1.setInviteMessage("invite message1");

		
		InviteInfo token2 = new InviteInfo();
		token2.setEmail("test2@ufr.com");
		token2.setToken("2qwerty12345");
		token2.addPermission(permission1);
		token2.setInviteMessage("invite message2");

		InviteInfo token3 = new InviteInfo();
		token3.setEmail("test@ufr.com");
		token3.setToken("qwerty12345");
		token3.addPermission(permission);
		token3.setInviteMessage("invite message1");
		
		assert token1.equals(token3) : "InviteInfo should be equal";
		assert !token1.equals(token2) : "InviteInfo should not be equal";
		
		assert token1.hashCode() == token3.hashCode() : "Hash codes should be the same";
		assert token2.hashCode() != token3.hashCode() : "Hash codes should not be the same";
	}
	
	
}
