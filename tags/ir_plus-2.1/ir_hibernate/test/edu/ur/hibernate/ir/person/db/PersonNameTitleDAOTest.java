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

package edu.ur.hibernate.ir.person.db;

import org.springframework.context.ApplicationContext;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonNameAuthorityDAO;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameDAO;
import edu.ur.ir.person.PersonNameTitle;
import edu.ur.ir.person.PersonNameTitleDAO;

/**
 * Test the persistance methods for Contributor type Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class PersonNameTitleDAOTest {

	
	/**
	 * Test Person name title persistance
	 */
	@Test
	public void basePersonNameTitleDAOTest() throws Exception {

		/** get the application context */
		ApplicationContext ctx = ContextHolder.getApplicationContext();

		PersonNameDAO personNameDAO = (PersonNameDAO) ctx
				.getBean("personNameDAO");

		PersonNameAuthorityDAO personNameAuthorityDAO = (PersonNameAuthorityDAO) ctx.getBean("personNameAuthorityDAO");

		PersonName name = new PersonName();
		name.setFamilyName("familyName");
		name.setForename("forename");
		name.setInitials("n.d.s.");
		name.setMiddleName("MiddleName");
		name.setNumeration("III");
		name.setSurname("surname");

		
		name.addPersonNameTitle("Dr.");
		name.addPersonNameTitle("Chef");

		PersonNameAuthority p = new PersonNameAuthority(name);
		personNameAuthorityDAO.makePersistent(p);

		p.addName(name, true);
		
		PersonNameTitle personNameTitle = name.addPersonNameTitle("personNameTitleName");

		personNameDAO.makePersistent(name);

		PersonNameTitleDAO personNameTitleDAO = (PersonNameTitleDAO) ctx
				.getBean("personNameTitleDAO");

		PersonNameTitle other = personNameTitleDAO.getById(personNameTitle
				.getId(), false);
		assert other.equals(personNameTitle) : "Person Name titles should be equal";

		personNameTitleDAO.makeTransient(other);

		PersonNameTitle other2 = personNameTitleDAO.getById(personNameTitle
				.getId(), false);
		assert other2 == null : "should not find other 2";
		
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p.getId(),false));
	}
}
