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
 * Test the Person Name Class
 * 
 * @author Nathan Sarr
 * 
 */

@Test(groups = { "baseTests" }, enabled = true)
public class PersonNameTest {

	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		PersonName name = new PersonName();
		name.setFamilyName("familyName");
		name.setForename("forename");
		name.setId(22l);
		name.setInitials("n.d.s.");
		name.setMiddleName("MiddleName");
		name.setNumeration("III");
		name.setSurname("surname");
		name.setVersion(3);
		
		assert name.getFamilyName().equals("familyName") : "Family name should equal familyName";
		assert name.getForename().equals("forename") : "forename should equal forname";
		assert name.getId().equals(22l) : "id should equal 22l";
		assert name.getInitials().equals("n.d.s.") : "Initials should equal n.d.s.";
		assert name.getMiddleName().equals("MiddleName") : "middle name should equal MiddleName";
		assert name.getNumeration().equals("III") : "Numeration should equal III";
		assert name.getSurname().equals("surname") : "surname equals surname";
		assert name.getVersion() == 3 : "Version should equal 3";
	}
	
	
	/**
	 * Test adding and removing a person name title.
	 */
	public void testAddPersonNameTitle()
	{
		PersonName name = new PersonName();
		
		PersonNameTitle personNameTitle1 = name.addPersonNameTitle("value1");
		
		assert name.getPersonNameTitles().contains(personNameTitle1) : 
			"Should contain " + personNameTitle1;
		assert name.getPersonNameTitles().size() == 1 : "Size should be 1";
		PersonNameTitle personNameTitle2 = name.addPersonNameTitle("value2");
		assert name.getPersonNameTitles().contains(personNameTitle2) : 
			"Should contain " + personNameTitle2;
		
		assert name.removePersonNameTitle(personNameTitle1) : "Should be removed";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
		
		PersonName name1 = new PersonName();
		name1.setFamilyName("familyName");
		name1.setForename("forename");
		name1.setInitials("n.d.s");
		name1.setMiddleName("MiddleName");
		name1.setNumeration("III");
		name1.setSurname("surname");
		
		PersonName name2 = new PersonName();
		name2.setFamilyName("familyName2");
		name2.setForename("forename2");
		name2.setInitials("n.d.s2");
		name2.setMiddleName("MiddleName2");
		name2.setNumeration("III");
		name2.setSurname("surname2");
		
		PersonName name3 = new PersonName();
		name3.setFamilyName("familyName");
		name3.setForename("forename");
		name3.setInitials("n.d.s");
		name3.setMiddleName("MiddleName");
		name3.setNumeration("III");
		name3.setSurname("surname");

	    assert name1.equals(name3) : "Names should be equal";
	    assert !name1.equals(name2) : "names should not be equal";
	    
	    assert name1.hashCode() == name3.hashCode() : "hash codes should be equal";
	    assert name1.hashCode() != name2.hashCode() : "hash codes should not be equal";
	}
	

}
