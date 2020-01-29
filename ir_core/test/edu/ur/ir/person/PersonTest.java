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
public class PersonTest {
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() throws Exception
	{
		PersonName name1 = new PersonName();
		name1.setFamilyName("familyName");
		name1.setForename("forename");
		name1.setInitials("n.d.s");
		name1.setMiddleName("MiddleName");
		name1.setNumeration("III");
		name1.setSurname("surname");
		
		PersonNameAuthority p = new PersonNameAuthority();
		BirthDate bDate = p.addBirthDate(2005);
		DeathDate pDate = p.addDeathDate(2105);
		p.setId(55l);
		p.setVersion(11);
		
		p.addName(name1, true);
		
		assert p.getAuthoritativeName().equals(name1) : "Person should have authoritative name " + name1; 
		
		assert p.getBirthDate().equals(bDate) : "Dates should be equal";
		assert p.getDeathDate().equals(pDate) : "Dates should be equal";
		assert p.getId().equals(55l) : "Id's should be equal.";
		assert p.getVersion() == 11 : "Version should be equal";
	}
	
	/**
	 * Test adding and removing names.
	 */
	public void testAddName()
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
		name2.setNumeration("III2");
		name2.setSurname("surname2");
		
		PersonNameAuthority p = new PersonNameAuthority();
		p.addName(name1, false);
		
		assert p.getAuthoritativeName() == null : "Authoritative name should be null";
		assert p.getNames().contains(name1) : "Name 1 should be in the set";
		
		p.addName(name2, true);
		
		assert p.getAuthoritativeName().equals(name2) : " Authoritative name should be equal to name2";
		assert p.getNames().size() == 2 : "Size should be 2";
	
		assert p.getNames().contains(name2) : "Name 2 should be found";
		
		p.removeName(name2);
		
		assert !p.getNames().contains(name2) : "Should no longer contain name 2";
		assert p.getAuthoritativeName() == null;
		
		assert p.getNames().contains(name1) : "Should still contain name1";
	}
	
	/**
	 * Test adding and removing names.
	 */
	public void testChangeAuthoriativeName()
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
		name2.setNumeration("III2");
		name2.setSurname("surname2");
		
		PersonNameAuthority p = new PersonNameAuthority();
		p.addName(name1, false);
		
		assert p.getAuthoritativeName() == null : "Authoritative name should be null";
		assert p.getNames().contains(name1) : "Name 1 should be in the set";
		
		p.addName(name2, true);
		
		assert p.getAuthoritativeName().equals(name2) : " Authoritative name should be equal to name2";
		assert p.getNames().size() == 2 : "Size should be 2";
	
		assert p.getNames().contains(name2) : "Name 2 should be found";
		
		p.changeAuthoritativeName(name1);
		
		assert p.getNames().contains(name2) : "Should still contain name 2";
		assert !p.getAuthoritativeName().equals(name2) : "Name 2 should not be authoriative";
		
		assert p.getNames().contains(name1) : "Should still contain name1";
		assert p.getAuthoritativeName().equals(name1) : "Authoritave name should be name 1";
		
		assert p.getNames().size() == 2 : "Size should still be 2";
	}
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()throws Exception
	{
		
		PersonName name1 = new PersonName();
		name1.setFamilyName("familyName");
		name1.setForename("forename");
		name1.setInitials("n.d.s");
		name1.setMiddleName("MiddleName");
		name1.setNumeration("III");
		name1.setSurname("surname");

		PersonNameAuthority p = new PersonNameAuthority();
		p.addBirthDate(2005);
		p.addDeathDate(2105);
		p.setVersion(11);
		
		p.addName(name1, true);
			
		PersonName name2 = new PersonName();
		name2.setFamilyName("familyName2");
		name2.setForename("forename2");
		name2.setInitials("n.d.s2");
		name2.setMiddleName("MiddleName2");
		name2.setNumeration("III");
		name2.setSurname("surname2");

		PersonNameAuthority p2 = new PersonNameAuthority();
		
		p2.addBirthDate(2005);
		p2.addDeathDate(2105);
		p2.setId(77l);
		p2.setVersion(12);
		
		p2.addName(name2, true);
		
		PersonName name3 = new PersonName();
		name3.setFamilyName("familyName");
		name3.setForename("forename");
		name3.setInitials("n.d.s");
		name3.setMiddleName("MiddleName");
		name3.setNumeration("III");
		name3.setSurname("surname");
		
		PersonNameAuthority p3 = new PersonNameAuthority();
		
		p3.addBirthDate(2005);
		p3.addDeathDate(2105);
		p3.setVersion(44);
		
		p3.addName(name3, true);
		
	    assert p.equals(p3) : "People should be equal";
	    assert !p.equals(p2) : "People should not be equal";
	    
	    assert p.hashCode() == p3.hashCode() : "hash codes should be equal";
	    assert p.hashCode() != p2.hashCode() : "hash codes should not be equal";
	}
	
	/**
	 * Test adding and removing  identifiers
	 */
	public void testPersonNameAuthorityIdentifiers()
	{
		PersonName name1 = new PersonName();
		name1.setFamilyName("familyName");
		name1.setForename("forename");
		name1.setInitials("n.d.s");
		name1.setMiddleName("MiddleName");
		name1.setNumeration("III");
		name1.setSurname("surname");
		
		PersonNameAuthority p = new PersonNameAuthority();
		p.addName(name1, false);
		
		assert p.getAuthoritativeName() == null : "Authoritative name should be null";
		assert p.getNames().contains(name1) : "Name 1 should be in the set";
		
		
		PersonNameAuthorityIdentifierType pt = new PersonNameAuthorityIdentifierType();
		pt.setName("orcid");
		pt.setDescription("orcid identifier");
		pt.setId(55l);
		pt.setVersion(33);
		
		PersonNameAuthorityIdentifier ident1 = p.addIdentifier("123834347", pt);
		
		assert p.getIdentifiers().size() == 1 : "Size should be 1 but is " + p.getIdentifiers().size();
		assert(p.getIdentifiers().contains(ident1));
		
		PersonNameAuthorityIdentifierType pt2 = new PersonNameAuthorityIdentifierType();
		pt2.setName("blah");
		pt2.setDescription("blah identifier");
		pt2.setId(50l);
		pt2.setVersion(2);
		
		PersonNameAuthorityIdentifier ident2 = p.addIdentifier("1238aaa", pt2);
		
		assert p.getIdentifiers().size() == 2 : "Size should be 2 but is " + p.getIdentifiers().size();
		
		assert(p.getIdentifiers().contains(ident2));
		assert(p.getIdentifiers().contains(ident1));
		
		p.removeIdentifier(ident1);
		
		assert p.getIdentifiers().size() == 1 : "Size should be 1 but is " + p.getIdentifiers().size();
		assert(p.getIdentifiers().contains(ident2));
		assert(!p.getIdentifiers().contains(ident1));
		
		p.removeAllIdentifiers();
		
		assert p.getIdentifiers().isEmpty() : "Size should be 1 but is " + p.getIdentifiers().size();
		
		 
	}

}
