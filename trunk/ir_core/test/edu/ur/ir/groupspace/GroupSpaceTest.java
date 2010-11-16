/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.groupspace;

import org.testng.annotations.Test;

import edu.ur.ir.user.IrUser;

/**
 * Testing for group spaces.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class GroupSpaceTest {
	
	/**
	 * Base tests for group work spaces.
	 */
	public void baseGroupSpaceTest()
	{
		IrUser user = new IrUser("username", "password");
		GroupSpace groupSpace = new GroupSpace("test group", user, "group description");
		
		assert groupSpace.getRootFolder() != null : "Group should have root folder";
		assert groupSpace.getRootFolder().getName().equals("test group") : "root folder should have the same name as group space but is " + groupSpace.getRootFolder().getName();
	    assert groupSpace.getName().equals("test group") : " group name should equal test group but equals " + groupSpace.getName();
	    assert groupSpace.getOwner().equals(user) : "Group owner should equal " + user + " but equals " + groupSpace.getOwner();
	}

}
