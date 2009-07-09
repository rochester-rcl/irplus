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

package edu.ur.ir.item;

import org.testng.annotations.Test;

import edu.ur.ir.user.IrUser;

/**
 * Simple test for the item version class
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ItemVersionTest {
	
	/**
	 * Test adding a new item version. 
	 */
	public void testAddingNewVersion()
	{
		GenericItem item = new GenericItem("myItem");
		GenericItem item2 = new GenericItem("myNewerItem");
		GenericItem item3 = new GenericItem("myNewestItem");
	    
		IrUser user = new IrUser("username", "password");
		VersionedItem vi = new VersionedItem(user, item);
		ItemVersion v1 = vi.getCurrentVersion();
	    ItemVersion v2 = vi.addNewVersion(item2);
	    ItemVersion v3 = vi.addNewVersion(item3);
	    
	    ItemVersion sameAsV2 = new ItemVersion(item2, vi, 2);
	    
	    assert sameAsV2.equals(v2) : "These should be the same";
	    assert !v1.equals(v2) : "These should not be the same";
	     
	    assert vi.getLargestVersion() == 3 : "The largest version number should equal 3";
	    assert vi.getCurrentVersion().equals(v3);
	}	
	
}
