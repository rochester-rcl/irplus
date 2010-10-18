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

/**
 * Test the Sponsor Class
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class SponsorTest {
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		Sponsor sponsor = new Sponsor();
		sponsor.setName("sponsorName");
		sponsor.setDescription("sponsorDescription");
		sponsor.setId(55l);
		sponsor.setVersion(33);
		
		assert sponsor.getName().equals("sponsorName") : "Should equal sponsorName";
		assert sponsor.getDescription().equals("sponsorDescription") : "Shoud equal sponsorDescription";
		assert sponsor.getId().equals(55l) : "Should equal 55l";
		assert sponsor.getVersion() == 33 : "Should equal 33";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		Sponsor sponsor1 = new Sponsor();
		sponsor1.setName("sponsorName");
		sponsor1.setDescription("sponsorDescription");
		sponsor1.setId(55l);
		sponsor1.setVersion(33);
		
		Sponsor sponsor2 = new Sponsor();
		sponsor2.setName("sponsorName2");
		sponsor2.setDescription("sponsorDescription2");
		sponsor2.setId(55l);
		sponsor2.setVersion(33);

		
		Sponsor sponsor3 = new Sponsor();
		sponsor3.setName("sponsorName");
		sponsor3.setDescription("sponsorDescription");
		sponsor3.setId(55l);
		sponsor3.setVersion(33);
		
		assert sponsor1.equals(sponsor3) : "Sponsor should be equal";
		assert !sponsor1.equals(sponsor2) : "Sponsor should not be equal";
		
		assert sponsor1.hashCode() == sponsor3.hashCode() : "Hash codes should be the same";
		assert sponsor2.hashCode() != sponsor3.hashCode() : "Hash codes should not be the same";
	}
}
