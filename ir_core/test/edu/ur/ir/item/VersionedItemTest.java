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
 * Test the VersionedItem Class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class VersionedItemTest {
    
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testVersionedItem() 
	{
		GenericItem item = new GenericItem("myItem");
		IrUser user = new IrUser("username", "password");
	    VersionedItem vi = new VersionedItem(user, item);
	    
	    ItemVersion itemVersion = new ItemVersion(item, vi, 1);
	    assert vi.getCurrentVersion().equals(itemVersion);
	    assert vi.getLargestVersion() == 1 : "The largest version number should equal 1";
	}
	
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
	    vi.addNewVersion(item2);
	    ItemVersion v3 = vi.addNewVersion(item3);
	     
	  
	    assert vi.getLargestVersion() == 3 : "The largest version number should equal 1";
	    assert vi.getCurrentVersion().equals(v3);
	    
	}	
	
	/**
	 * Make sure we can change the current version
	 */
	public void testChangingCurrentVersion()
	{
		GenericItem item = new GenericItem("myItem");
		GenericItem item2 = new GenericItem("myNewerItem");
		GenericItem item3 = new GenericItem("myNewestItem");
	    
		IrUser user = new IrUser("username", "password");
		VersionedItem vi = new VersionedItem(user, item);
	    ItemVersion version2 = vi.addNewVersion(item2);
	    vi.addNewVersion(item3);
	     
	    // make version 2 to the most current version
	    ItemVersion version4 = vi.changeCurrentIrVersion(version2.getVersionNumber());
	    assert vi.getCurrentVersion().equals(version4);
	}
}
