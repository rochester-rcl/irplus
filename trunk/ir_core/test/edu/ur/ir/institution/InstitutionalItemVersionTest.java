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

import edu.ur.ir.item.GenericItem;
import edu.ur.ir.user.IrUser;


/**
 * Test the Institutional Item Version class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class InstitutionalItemVersionTest {
	
	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testWithdraw() 
	{
		IrUser user = new IrUser("username", "password");
		GenericItem item = new GenericItem("myItem");
	    
	    VersionedInstitutionalItem vii = new VersionedInstitutionalItem(item);

	    InstitutionalItemVersion institutionalItemVersion = vii.getCurrentVersion();
	    assert !institutionalItemVersion.getWithdrawn() : "item should NOT be withdrawn";
	    
	    institutionalItemVersion.withdraw(user, "bad object", true);
	    
	    assert institutionalItemVersion.getWithdrawn() : "Object " + institutionalItemVersion + " should be withdrawn ";
	    WithdrawnToken withdrawnToken = institutionalItemVersion.getWithdrawnToken();
	    assert withdrawnToken != null : " Withdrawn token should not be null";
	    
	    assert withdrawnToken.getUser().equals(user) : " Withdrawn user " + withdrawnToken.getUser() + " should equal " + user;
	} 
	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testReInstate() 
	{
		IrUser user = new IrUser("username", "password");
		GenericItem item = new GenericItem("myItem");
	    
	    VersionedInstitutionalItem vii = new VersionedInstitutionalItem(item);

	    InstitutionalItemVersion institutionalItemVersion = vii.getCurrentVersion();
	    assert !institutionalItemVersion.getWithdrawn() : "item should NOT be withdrawn";
	    
	    institutionalItemVersion.withdraw(user, "bad object", true);
	    
	    assert institutionalItemVersion.getWithdrawn() : "Object " + institutionalItemVersion + " should be withdrawn ";
	    WithdrawnToken withdrawnToken = institutionalItemVersion.getWithdrawnToken();
	    assert withdrawnToken != null : " Withdrawn token should not be null";
	    
	    // now re-instate the object
	    ReinstateToken reInstateToken = institutionalItemVersion.reInstate(user, "I like it");
	    assert institutionalItemVersion.getWithdrawnToken() == null : "Withdrawn token should be null but isnt't " + institutionalItemVersion.getWithdrawnToken();
	    assert reInstateToken != null : "ReInstate token should not be null";
	    assert reInstateToken.getUser().equals(user);
	    
	    
	    assert institutionalItemVersion.getReinstateHistory().size() == 1 : "Should have a reinstate history";
	    assert institutionalItemVersion.getWithdrawHistory().size() == 1: "Should have a withdraw history ";
	    
	    
	    
	   
	} 

}
