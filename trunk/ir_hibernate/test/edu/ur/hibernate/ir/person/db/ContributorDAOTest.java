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

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorDAO;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeDAO;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonNameAuthorityDAO;

/**
 * Test the persistance methods for Contributor Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ContributorDAOTest {
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
     
	/** person name authority data access */
	PersonNameAuthorityDAO personNameAuthorityDAO = (PersonNameAuthorityDAO) ctx
	.getBean("personNameAuthorityDAO");
	
	/** contributor type data access */
     ContributorTypeDAO contributorTypeDAO = (ContributorTypeDAO) ctx
	.getBean("contributorTypeDAO");

    /** contributor data access object */
    ContributorDAO contributorDAO = (ContributorDAO) ctx
		.getBean("contributorDAO");
     
	static final String birthDate = "1/1/2005";
	static final String passDate = "1/1/2105";
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);

	/**
	 * Test PersonName persistance
	 */
	@Test
	public void baseContributorDAOTest()throws Exception{
	
  		ContributorType ct = new ContributorType("ctName");
 		ct.setDescription("ctDescription");
 		
		ContributorType ct2 = new ContributorType("ctName2");
 		ct2.setDescription("ctDescription2");

        TransactionStatus ts = tm.getTransaction(td);

        contributorTypeDAO.makePersistent(ct);
        contributorTypeDAO.makePersistent(ct2);
         
 		PersonName name = new PersonName();
		name.setFamilyName("familyName");
		name.setForename("forename");
		name.setInitials("n.d.s.");
		name.setMiddleName("MiddleName");
		name.setNumeration("III");
		name.setSurname("surname");
		
		PersonNameAuthority p = new PersonNameAuthority(name);
		p.addBirthDate(1,1,2005);
		p.addDeathDate(1, 1, 2105);
		
		personNameAuthorityDAO.makePersistent(p);
		
		Contributor contrib = new Contributor(name, ct);
		contributorDAO.makePersistent(contrib);
		
		Contributor contrib2 = new Contributor(name, ct2);
		contributorDAO.makePersistent(contrib2);
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);
		Contributor other = contributorDAO.getById(contrib.getId(), false);
		assert other.equals(contrib) : "The contributors should be equal";

		assert contributorDAO.findByNameType(name.getId(), ct.getId()) != null : "Should find the contributor " + ct;
		assert contributorDAO.getAllForName(name.getId()).size() == 2 : "Should find the two contributor types";

		
		
		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		contributorDAO.makeTransient(contributorDAO.getById(contrib.getId(), false));
		contributorDAO.makeTransient(contributorDAO.getById(contrib2.getId(), false));
		assert contributorDAO.getById(other.getId(), false) == null : "Should not be able to find other";
		
		contributorTypeDAO.makeTransient(contributorTypeDAO.getById(ct.getId(), false));
		contributorTypeDAO.makeTransient(contributorTypeDAO.getById(ct2.getId(), false));
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p.getId(), false));
		tm.commit(ts);
	}
	

}
