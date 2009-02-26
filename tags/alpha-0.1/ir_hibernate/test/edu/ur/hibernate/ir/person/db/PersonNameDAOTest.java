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
import edu.ur.ir.person.PersonNameDAO;
import edu.ur.ir.person.PersonNameTitle;

/**
 * Test the persistance methods for Institution Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class PersonNameDAOTest {

	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

     /** Person name data access */
    PersonNameDAO personNameDAO = (PersonNameDAO) ctx
	.getBean("personNameDAO");
     
	/** Person data access  */
	PersonNameAuthorityDAO personNameAuthorityDAO = (PersonNameAuthorityDAO) ctx
	.getBean("personNameAuthorityDAO");

	/** Transaction management  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
		.getBean("transactionManager");
    
	/** Transaction definition  */
	TransactionDefinition td = new DefaultTransactionDefinition(
		TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test PersonName persistance
	 */
	@Test
	public void basePersonNameDAOTest()throws Exception{
 		PersonName name = new PersonName();
		name.setFamilyName("familyName");
		name.setForename("forename");
		name.setInitials("n.d.s.");
		name.setMiddleName("MiddleName");
		name.setNumeration("III");
		name.setSurname("surname");
		
		// give the person some titles
		PersonNameTitle title = new PersonNameTitle("Dr.", name);
		PersonNameTitle title2 = new PersonNameTitle("Chef", name);
		
		name.addPersonNameTitle("Dr.");
		name.addPersonNameTitle("Chef");
		
		PersonNameAuthority p = new PersonNameAuthority(name);
		p.addBirthDate(1,1,2005);
		p.addDeathDate(1, 1, 2105);

        TransactionStatus ts = tm.getTransaction(td);
		personNameAuthorityDAO.makePersistent(p);
		
		
		PersonName other = personNameDAO.getById(name.getId(), false);
		assert other.equals(name) : "other should be equal to name";
		
		for( PersonNameTitle pnt : other.getPersonNameTitles() )
		{
			System.out.println("Title = " + pnt.getTitle());
		}
		
		assert other.getPersonNameTitles().contains(title) : "other name should have " + title;
		assert other.getPersonNameTitles().contains(title2) : "other name should have " + title2;
		
		//complete the transaction
		tm.commit(ts);

        ts = tm.getTransaction(td);
		assert personNameDAO.getCount() == 1 : "Should have one person name in the database";

		personNameDAO.getAllNameOrder();
		personNameDAO.getAllOrderByName(1, 2);
		
		// make sure we can delete the authoritative name
		personNameDAO.makeTransient(personNameDAO.getById(name.getId(), false));
		assert personNameDAO.getById(name.getId(), false) == null : "Should no longer be able to find person name";	
		personNameAuthorityDAO.makeTransient( personNameAuthorityDAO.getById(p.getId(), false));
		tm.commit(ts);

	}
	
	/**
	 * Test searching for a person name 
	 */
	@Test
	public void personNameSearchingDAOTest()throws Exception{
		
 		PersonName name = new PersonName();
		name.setForename("forename1");
		name.setSurname("surname1");
		
		PersonNameAuthority p = new PersonNameAuthority(name);
		p.addBirthDate(1,1,2005);
		p.addDeathDate(1, 1, 2105);

        // Start the transaction this is for lazy loading
        TransactionStatus ts = tm.getTransaction(td);

		personNameAuthorityDAO.makePersistent(p);
		
		
		PersonName name2 = new PersonName();
		name2.setForename("fore2name");
		name2.setSurname("sur2name");
		
		PersonNameAuthority p2 = new PersonNameAuthority(name2);
		p2.addBirthDate(1,1,2005);
		p2.addDeathDate(1, 1, 2105);
		
		personNameAuthorityDAO.makePersistent(p2);
		p2.addName(name2, true);
		personNameDAO.makePersistent(name2);
		tm.commit(ts);

		ts = tm.getTransaction(td);
		List<PersonName> names = personNameDAO.findPersonLikeFirstName("fore", 0, 10);
		assert names.size() == 2 : "Should have 2 people found in the database but found " + names.size();
		
		names = personNameDAO.findPersonLikeFirstName("foreName", 0, 10);
		assert names.size() == 1 : "Should have one person found in the database but found " + names.size();

		names = personNameDAO.findPersonLikeLastName("sur", 0, 10);
		assert names.size() == 2 : "Should have 2 people found in the database but found " + names.size();
		
		names = personNameDAO.findPersonLikeLastName("surname", 0, 10);
		assert names.size() == 1 : "Should have one person found in the database but found " + names.size();

		names = personNameDAO.findPersonLikeFirstLastName( "fore", "sur",0, 10);
		assert names.size() == 2 : "should find both recrods but found " + names.size();
		
		names = personNameDAO.findPersonLikeFirstLastName( "fore", "surname", 0, 10);
		assert names.size() == 1 : "should only find one name but found " + names.size();
		
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p.getId(), false));
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p2.getId(), false));
		tm.commit(ts);

	}
}
