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

package edu.ur.ir.person;

import org.testng.annotations.Test;

/**
 * Test the Contributor type Class
 * 
 * @author Nathan Sarr
 * 
 */

@Test(groups = { "baseTests" }, enabled = true)
public class PersonNameTitleTest {
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		PersonNameTitle personNameTitle = new PersonNameTitle();
		personNameTitle.setTitle("personNameTitleName");
		personNameTitle.setId(55l);
		personNameTitle.setVersion(33);
		
		assert personNameTitle.getTitle().equals("personNameTitleName") : "Should equal personNameTitleName";
		assert personNameTitle.getId().equals(55l) : "Should equal 55l";
		assert personNameTitle.getVersion() == 33 : "Should equal 33";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		PersonNameTitle personNameTitle1 = new PersonNameTitle();
		personNameTitle1.setTitle("personNameTitleName");
		personNameTitle1.setId(55l);
		personNameTitle1.setVersion(33);
		
		PersonNameTitle personNameTitle2 = new PersonNameTitle();
		personNameTitle2.setTitle("personNameTitleName2");
		personNameTitle2.setId(55l);
		personNameTitle2.setVersion(33);

		
		PersonNameTitle personNameTitle3 = new PersonNameTitle();
		personNameTitle3.setTitle("personNameTitleName");
		personNameTitle3.setId(55l);
		personNameTitle3.setVersion(33);
		
		assert personNameTitle1.equals(personNameTitle3) : "Contributor types should be equal";
		assert !personNameTitle1.equals(personNameTitle2) : "Contributor types should not be equal";
		
		assert personNameTitle1.hashCode() == personNameTitle3.hashCode() : "Hash codes should be the same";
		assert personNameTitle2.hashCode() != personNameTitle3.hashCode() : "Hash codes should not be the same";
	}
}
