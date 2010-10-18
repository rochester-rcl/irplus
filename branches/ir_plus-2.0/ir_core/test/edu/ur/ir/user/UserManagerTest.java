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
import edu.ur.ir.user.UserManager;

/**
 * Test the user manager test.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class UserManagerTest {
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testUserBasicSets() throws Exception
	{
	    UserManager manager = new UserManager();
	    IrUser user1 = manager.createUser("test", "nsarr");
	    IrUser user2 = manager.createUser("test", "nsarr");
	    
	    assert user1.equals(user2);
	}
	

}
