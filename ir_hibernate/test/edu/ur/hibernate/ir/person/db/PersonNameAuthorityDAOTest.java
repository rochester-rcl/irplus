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
import edu.ur.ir.person.PersonNameAuthorityIdentifier;
import edu.ur.ir.person.PersonNameAuthorityIdentifierDAO;
import edu.ur.ir.person.PersonNameAuthorityIdentifierType;
import edu.ur.ir.person.PersonNameAuthorityIdentifierTypeDAO;

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
	
	/** Identifier relational data access */
	PersonNameAuthorityIdentifierTypeDAO identifierTypeDAO = (PersonNameAuthorityIdentifierTypeDAO) ctx.getBean("personNameAuthorityIdentifierTypeDAO");
			
	/** Item identifier relatioanal data access. */
	PersonNameAuthorityIdentifierDAO identifierDAO = (PersonNameAuthorityIdentifierDAO) ctx.getBean("personNameAuthorityIdentifierDAO");		

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
	
	/**
	 * Test add a item identifier.
	 */
	@Test
	public void addIdentifierDAOTest() throws Exception{

		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);

		PersonNameAuthorityIdentifierType identType = new PersonNameAuthorityIdentifierType("identTypeNameA","identTypeDescription" );
 		identifierTypeDAO.makePersistent(identType);
 
 		PersonName name = new PersonName();
		name.setFamilyName("familyName");
		name.setForename("forename");
		name.setInitials("n.d.s.");
		name.setMiddleName("MiddleName");
		name.setNumeration("III");
		name.setSurname("surname");
		
		PersonNameAuthority p = new PersonNameAuthority(name);

		
        PersonNameAuthorityIdentifier ident1 = p.addIdentifier("123834347", identType);
		
		assert p.getIdentifiers().size() == 1 : "Size should be 1 but is " + p.getIdentifiers().size();
		assert(p.getIdentifiers().contains(ident1));
		 
		personNameAuthorityDAO.makePersistent(p);
		tm.commit(ts);
		
        // Start the transaction 
        ts = tm.getTransaction(td);
        
        PersonNameAuthority other = personNameAuthorityDAO.getById(p.getId(), false);
		assert other.getIdentifiers().contains(ident1) : "The item should contain " + ident1;
		System.out.println("ident 1 = " + ident1);
    	List<PersonNameAuthorityIdentifierType> identifierTypes = personNameAuthorityDAO.getPossibleIdentifierTypes(other.getId());
    	
    	for(PersonNameAuthorityIdentifierType i : identifierTypes) {
    		System.out.println("identifier = " + i);
    	}
		
		// there should only be one identifier type for the person 
		assert identifierTypes.size() == 0 : "There should only be 0 identifier types that can be used " 
			+ identifierTypes.size();

		// you should get all identifier types when you don't find the person
		identifierTypes = personNameAuthorityDAO.getPossibleIdentifierTypes(0L);
		
		assert identifierTypes.size() == 1 : "One Identifier type should be available but ther are " 
			+ identifierTypes.size();
		
		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		other = personNameAuthorityDAO.getById(p.getId(), false);
		ident1 = identifierDAO.getById(ident1.getId(), false);
		other.removeIdentifier(ident1);
		identifierDAO.makeTransient(ident1);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		// reload the person
		other = personNameAuthorityDAO.getById(other.getId(), false);
		assert other.getIdentifiers().size() == 0 : "Should not have any item identifiers but found "  + other.getIdentifiers().size();
		
		//complete the transaction
		identifierTypeDAO.makeTransient(identType);
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(other.getId(), false));
		tm.commit(ts);
	}
	
}
