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


package edu.ur.ir.person.service;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.person.PersonNameAuthority;

/**
 * Test getting person information.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultPersonServiceTest {
	
	ApplicationContext ctx =  ContextHolder.getApplicationContext();
	
    RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
	
	// Check the repositories
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** person data access */
	PersonService personService = (PersonService) ctx
	.getBean("personService");
	
	/**
	 * Test creating a person.
	 */
	public void testCreatePerson()
	{
		TransactionStatus ts = tm.getTransaction(td);
 		PersonName name = new PersonName();
		name.setFamilyName("familyName");
		name.setForename("forename");
		name.setInitials("n.d.s.");
		name.setMiddleName("MiddleName");
		name.setNumeration("III");
		name.setSurname("surname");
		
		PersonNameAuthority p = new PersonNameAuthority(name);
		
		personService.save(p);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		PersonNameAuthority other = personService.getAuthority(p.getId(), false);
		assert other != null : " should be able to find person " + p ;
		assert other.equals(p) : "Person p " + p +
		   " should equal other " + other;
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		personService.delete(personService.getAuthority(p.getId(), false));
		assert personService.getAuthority(p.getId(), false) == null : 
			"Should not be able to find person " + p;
		tm.commit(ts);
	}
	

}
