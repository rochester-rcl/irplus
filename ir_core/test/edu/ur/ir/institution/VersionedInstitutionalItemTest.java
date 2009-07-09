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

package edu.ur.ir.institution;

import org.testng.annotations.Test;

import edu.ur.ir.institution.VersionedInstitutionalItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemVersion;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.user.IrUser;

/**
 * Test the VersionedInstitutionalItem Class
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class VersionedInstitutionalItemTest {
    
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testVersionedInstitutionalItem() 
	{
		GenericItem item = new GenericItem("myItem");
	    
	    VersionedInstitutionalItem vii = new VersionedInstitutionalItem(item);

	    assert vii.getCurrentVersion().getItem().equals(item);
	    assert vii.getLargestVersion() == 1 : "The largest version number should equal 1";
	    assert vii.getInstitutionalItemVersions().size() == 1 :"Number of versions should be 1";
	} 
	
	/**
	 * Test adding a new published item version. 
	 */
	public void testAddingNewVersion()
	{
		GenericItem item = new GenericItem("myItem");
		GenericItem item2 = new GenericItem("myNewerItem");
	    
		IrUser user = new IrUser("username", "password");
		VersionedItem vi = new VersionedItem(user, item);
	   
	    VersionedInstitutionalItem vii = new VersionedInstitutionalItem(item);
    
	    // Add new version
	    ItemVersion v2 = vi.addNewVersion(item2);
	    
	    vii.addNewVersion(v2.getItem());
	    
	    assert vii.getLargestVersion() == 2 : "The largest version number should equal 2";
	    assert vii.getCurrentVersion().getItem().equals(item2);
	    
	}	
	

}
