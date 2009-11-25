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

package edu.ur.ir.researcher;

import org.testng.annotations.Test;

import edu.ur.ir.user.IrUser;

/**
 * Test the Researcher link class
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ResearcherLinkTest {
	
	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		
		// create the owner of the personal item
		IrUser user = new IrUser("nate", "password");
		Researcher researcher = new Researcher(user); 

		ResearcherLink researcherLink = new ResearcherLink(researcher, "www.google.com");
		assert researcherLink.getLink().equals("www.google.com") : "Should be equal to - www.google.com";
		
	    assert researcherLink.getResearcher().equals(researcher): "Researchers should be equal";
	}

}
