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

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonNameAuthorityDAO;

/**
 * Test the persistance methods for Person Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class PersonNameAuthorityDAOTest {

	/** Simple data format  */
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** Person data access  */
	PersonNameAuthorityDAO personNameAuthorityDAO = (PersonNameAuthorityDAO) ctx.getBean("personNameAuthorityDAO");

 	/** Transaction manager */
 	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");

	/** Transaction definition  */
	TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test Person persistance
	 */
	@Test
	public void basePersonDAOTest() throws Exception{
		PersonName name = new PersonName();
		name.setFamilyName("familyName");
		name.setForename("forename");
		name.setInitials("n.d.s.");
		name.setMiddleName("MiddleName");
		name.setNumeration("III");
		name.setSurname("surname");
		
		PersonNameAuthority p = new PersonNameAuthority(name);
		p.addBirthDate(2005);
		p.addDeathDate(2105);
	
		TransactionStatus ts = tm.getTransaction(td);

		personNameAuthorityDAO.makePersistent(p);
		PersonNameAuthority other = personNameAuthorityDAO.getById(p.getId(), false);
		assert other.equals(p) : "other should be equal to p";
		
		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert personNameAuthorityDAO.getCount() == 1 : "Should have one person in the database but there is " + personNameAuthorityDAO.getCount();
		
		personNameAuthorityDAO.makeTransient(p);
		assert personNameAuthorityDAO.getById(p.getId(), false) == null : "Should no longer be able to find person";
		tm.commit(ts);

	}
	
	/**
	 * Test Person persistance
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void personDAONameTest() throws Exception{
 		PersonName name = new PersonName();
		name.setFamilyName("familyName");
		name.setForename("forename");
		name.setInitials("n.d.s.");
		name.setMiddleName("MiddleName");
		name.setNumeration("III");
		name.setSurname("surname");
		
		PersonNameAuthority p = new PersonNameAuthority(name);
		p.addBirthDate(2005);
		p.addDeathDate(2105);
		
		p.addName(name, true);

        TransactionStatus ts = tm.getTransaction(td);
		personNameAuthorityDAO.makePersistent(p);
		
        // Start the transaction this is for lazy loading
		PersonNameAuthority other = personNameAuthorityDAO.getById(p.getId(), false);
		assert other.equals(p) : "other should be equal to p";
		assert other.getAuthoritativeName().equals(name);
		
		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert personNameAuthorityDAO.getCount() == 1 : "Should have one person in the database but there is " + personNameAuthorityDAO.getCount();

		List people = personNameAuthorityDAO.getAllAuthoritativeNameAsc(0, 2);
		assert people.size() == 1 : "Size should be 1 but is " + people.size() ;

		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p.getId(), false));
		assert personNameAuthorityDAO.getById(p.getId(), false) == null : "Should no longer be able to find person";
		tm.commit(ts);

		
	}
}
