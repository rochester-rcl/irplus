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

import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemVersion;
import edu.ur.ir.item.VersionedItem;

/**
 * Test the Personal Item class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class PersonalItemTest {
	
	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		GenericItem genericItem = new GenericItem("genericItem");
		GenericItem genericItem2 = new GenericItem("genericItem2");
		
		// create the owner of the personal item
		IrUser user = new IrUser("nate", "password");
		VersionedItem versionedItem = new VersionedItem(user, genericItem);
		assert versionedItem != null : "Versioned item should not be null";

		PersonalItem personalItem = new PersonalItem(user, versionedItem);
		assert personalItem.getVersionedItem() != null : "Should be able to find personal item";
		ItemVersion versionedItem2 = versionedItem.addNewVersion(genericItem2);
	
		VersionedItem personalVersionedItem = personalItem.getVersionedItem();
		assert personalVersionedItem.equals(versionedItem) : "Items should be equal";
	    assert personalVersionedItem.getItemVersions().contains(versionedItem2) : "Versioned item should have " + versionedItem2;
	}

}
