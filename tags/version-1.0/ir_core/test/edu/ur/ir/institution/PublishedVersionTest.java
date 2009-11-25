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

import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.VersionedInstitutionalItem;
import edu.ur.ir.item.GenericItem;

/**
 * Test the Published version class
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class PublishedVersionTest {
	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		GenericItem genericItem = new GenericItem("genericItem");
		GenericItem genericItem2 = new GenericItem("genericItem2");
		
		VersionedInstitutionalItem vii = new VersionedInstitutionalItem(genericItem);		
		
		InstitutionalItemVersion publishedVersion1 =  new InstitutionalItemVersion(genericItem, vii, 1);
		
		InstitutionalItemVersion publishedVersion2 =  new InstitutionalItemVersion(genericItem, vii, 1);
		
		InstitutionalItemVersion publishedVersion3 =  new InstitutionalItemVersion(genericItem2, vii, 2);
	
	    assert publishedVersion2.equals(publishedVersion1) : "Published version should be equal";
	    assert !publishedVersion1.equals(publishedVersion3) : "Published version should not be equal";
	}
	
	

}
