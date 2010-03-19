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

import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorService;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeService;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;


/**
 * Test the service methods for contributor service
 *   
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultContributorServiceTest {
	
	ApplicationContext ctx =  ContextHolder.getApplicationContext();
 	
	// Check the repositories
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** person data access */
	PersonService personService = (PersonService) ctx
	.getBean("personService");
	
	/** contributor type data access */
	ContributorTypeService contributorTypeService = (ContributorTypeService) ctx
	.getBean("contributorTypeService");
	
	/** contributor data access */
	ContributorService contributorService = (ContributorService) ctx
	.getBean("contributorService");

	/**
	 * Test getting contributor information
	 */
	public void testGetContributor()
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
		
		
	       // create a contributor type
		ContributorType contributorType1 = new ContributorType("contributorType1");
		contributorTypeService.save(contributorType1);
		
		// create the contributor
		Contributor c = new Contributor();
		c.setPersonName(name);
		c.setContributorType(contributorType1);
		contributorService.save(c);
		
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		Contributor other = contributorService.get(c.getPersonName(), c.getContributorType());
		assert other != null : "other should be found for persona name = " + c.getPersonName() 
		+ " and type = " + c.getContributorType();
		
		assert other.equals(c) : "Types should be equal";
		contributorService.delete(other);
		personService.delete(personService.getAuthority(p.getId(), false));
		contributorTypeService.delete(contributorTypeService.get(contributorType1.getId(), false));
		
		tm.commit(ts);
	}
	
}
