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
public class ContributorTest {


	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() throws Exception 
	{
		ContributorType ct = new ContributorType();
		ct.setName("ctName");
		ct.setDescription("ctDescription");
		ct.setId(55l);
		ct.setVersion(33);
		
		PersonName name1 = new PersonName();
		name1.setFamilyName("familyName");
		name1.setForename("forename");
		name1.setInitials("n.d.s");
		name1.setMiddleName("MiddleName");
		name1.setNumeration("III");
		name1.setSurname("surname");
		
		Contributor contrib = new Contributor();
		contrib.setContributorType(ct);
		contrib.setPersonName(name1);
		
		assert contrib.getContributorType().equals(ct) : "Contributor type should equal " + ct;
		assert contrib.getPersonName().equals(name1) : "Person name should equal " + name1;
	}
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
		ContributorType ct = new ContributorType();
		ct.setName("ctName");
		ct.setDescription("ctDescription");
		
		PersonName name1 = new PersonName();
		name1.setFamilyName("familyName");
		name1.setForename("forename");
		name1.setInitials("n.d.s");
		name1.setMiddleName("MiddleName");
		name1.setNumeration("III");
		name1.setSurname("surname");
		
		
		Contributor contrib1 = new Contributor();
		contrib1.setContributorType(ct);
		contrib1.setPersonName(name1);
		
		ContributorType ct2 = new ContributorType();
		ct2.setName("ctName2");
		ct2.setDescription("ctDescription2");
		
		PersonName name2 = new PersonName();
		name2.setFamilyName("familyName2");
		name2.setForename("forename2");
		name2.setInitials("n.d.s2");
		name2.setMiddleName("MiddleName2");
		name2.setNumeration("III2");
		name2.setSurname("surname2");
		
		Contributor contrib2 = new Contributor();
		contrib2.setContributorType(ct2);
		contrib2.setPersonName(name2);
		
		ContributorType ct3 = new ContributorType();
		ct3.setName("ctName");
		ct3.setDescription("ctDescription");
		
		PersonName name3 = new PersonName();
		name3.setFamilyName("familyName");
		name3.setForename("forename");
		name3.setInitials("n.d.s");
		name3.setMiddleName("MiddleName");
		name3.setNumeration("III");
		name3.setSurname("surname");
		
		Contributor contrib3 = new Contributor();
		contrib3.setContributorType(ct3);
		contrib3.setPersonName(name3);
		
		assert contrib1.equals(contrib3) : "Contrib1 should equal contrib3";
		assert !contrib1.equals(contrib2) : "Contrib2 should not equal contrib1";
		assert contrib1.hashCode() == contrib3.hashCode() : "Hash codes should be equal";
		assert contrib1.hashCode() != contrib2.hashCode() : "Hash codes should not be equal";
	}
}
