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
import edu.ur.ir.invite.InviteToken;
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
	 * @throws ClassNotFoundException 
	 */
	public void testBasics() throws ClassNotFoundException 
	{
		IrUser user = new IrUser();
		user.setId(4L);
		user.setUsername("username");
		user.setVersion(5);
		
		IrClassType classType = null;
		classType = new IrClassType(VersionedFile.class);
		
		IrClassTypePermission permission = new IrClassTypePermission(classType);
		permission.setName("read");
		
		
		InviteToken inviteToken = new InviteToken("test@ufr.com", "qwerty12345", user );
		inviteToken.setInviteMessage("invite message!");
		InviteInfo info = new InviteInfo ();
		info.setInviteToken(inviteToken);
		info.addPermission(permission);
		
		
		assert info.getInviteToken().getEmail().equals("test@ufr.com") : "Should equal test@ufr.com";
		assert info.getInviteToken().getToken().equals("qwerty12345") : "Shoud equal qwerty12345";
		assert info.getPermissions().contains(permission) : "Shoud contain the permission";
		assert info.getInviteToken().getInvitingUser().equals(user) : "User should be equal";
		assert info.getInviteToken().getInviteMessage().equals("invite message!") : "Message should be equal";

	}
	
	
	/**
	 * Test equals and hash code methods.
	 * @throws ClassNotFoundException 
	 */
	public void testEquals() throws ClassNotFoundException
	{
		IrUser user = new IrUser();
		user.setId(4L);
		user.setUsername("username");
		user.setVersion(5);
		
		IrClassType classType = null;
		classType = new IrClassType(VersionedFile.class);
		
		IrClassTypePermission permission = new IrClassTypePermission(classType);
		permission.setName("read");
		
		IrClassTypePermission permission1 = new IrClassTypePermission(classType);
		permission1.setName("write");

		InviteToken inviteToken = new InviteToken("test@ufr.com", "qwerty12345", user );
		InviteInfo inviteInfo1 = new InviteInfo();
		inviteInfo1.setInviteToken(inviteToken);

		InviteToken inviteToken2 = new InviteToken("test2@ufr.com", "2qwerty12345", user );
		InviteInfo inviteInfo2 = new InviteInfo();
		inviteInfo2.setInviteToken(inviteToken2);

		InviteToken inviteToken3 = new InviteToken("test@ufr.com", "qwerty12345", user );
		InviteInfo inviteInfo3 = new InviteInfo();
		inviteInfo3.setInviteToken(inviteToken3);
	
		
		assert inviteInfo1.equals(inviteInfo3) : "InviteInfo should be equal";
		assert !inviteInfo1.equals(inviteInfo2) : "InviteInfo should not be equal";
		
		assert inviteInfo1.hashCode() == inviteInfo3.hashCode() : "Hash codes should be the same";
		assert inviteInfo2.hashCode() != inviteInfo3.hashCode() : "Hash codes should not be the same";
	}
	
	
}
