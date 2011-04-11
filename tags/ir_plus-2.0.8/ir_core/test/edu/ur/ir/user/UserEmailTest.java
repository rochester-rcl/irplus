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

/**
 * Test for user email
 * 
 * @author Sharmila Ranganathan
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class UserEmailTest {

	public void testEmailBasic() throws Exception {
		IrUser user = new IrUser();
		user.setUsername("username1");
		user.setPassword("pass1");
		
		
		UserEmail userEmail = new UserEmail();
		userEmail.setEmail("testEmail@y.com");
		userEmail.setIrUser(user);
		userEmail.setId(55l);
		userEmail.setVersion(33);
		
		
		assert userEmail.getEmail().equals("testEmail@y.com") : "Should equal testEmail@y.com";
		assert userEmail.getId().equals(55l) : "Should equal 55l";
		assert userEmail.getVersion() == 33 : "Should equal 33";
		assert userEmail.getIrUser().equals(user) : "Should be equal to user";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		UserEmail userEmail1 = new UserEmail();
		userEmail1.setEmail("userEmailName");
		userEmail1.setId(55l);
		userEmail1.setVersion(33);
		
		UserEmail userEmail2 = new UserEmail();
		userEmail2.setEmail("userEmailName2");
		userEmail2.setId(55l);
		userEmail2.setVersion(33);

		
		UserEmail userEmail3 = new UserEmail();
		userEmail3.setEmail("userEmailName");
		userEmail3.setId(55l);
		userEmail3.setVersion(33);
		
		assert userEmail1.equals(userEmail3) : "Email should be equal";
		assert !userEmail1.equals(userEmail2) : "Email should not be equal";
		
		assert userEmail1.hashCode() == userEmail3.hashCode() : "Hash codes should be the same";
		assert userEmail2.hashCode() != userEmail3.hashCode() : "Hash codes should not be the same";
	}
		
		
		
	
}
