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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeDAO;

import java.util.List;

/**
 * Test the persistance methods for Contributor type Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ContributorTypeDAOTest {

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

     ContributorTypeDAO contributorTypeDAO = (ContributorTypeDAO) ctx
	.getBean("contributorTypeDAO");
     
  	/** Transaction manager */
  	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
 	.getBean("transactionManager");

 	/** Transaction definition  */
 	TransactionDefinition td = new DefaultTransactionDefinition(
 	TransactionDefinition.PROPAGATION_REQUIRED);
 	

	
	/**
	 * Test Contributor type persistance
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseContributorTypeDAOTest() throws Exception{

 		ContributorType ct = new ContributorType("ctName");
 		ct.setDescription("ctDescription");
 		ct.setUniqueSystemCode("systemCode");
         
        TransactionStatus ts = tm.getTransaction(td);

         contributorTypeDAO.makePersistent(ct);
         ContributorType other = contributorTypeDAO.getById(ct.getId(), false);
         assert other.equals(ct) : "Contributor types should be equal";
         
         List<ContributorType> contribTypes = (List<ContributorType>)contributorTypeDAO.getAllOrderByName(0, 1);
         assert contribTypes.size() == 1 : "One contributor should be found";
         
         ContributorType contribTypeByName = contributorTypeDAO.findByUniqueName(ct.getName());
         assert contribTypeByName.equals(ct) : "Contributor should be found";
         
         assert contributorTypeDAO.getByUniqueSystemCode("systemCode").equals(ct);
         
         contributorTypeDAO.makeTransient(other);
         assert contributorTypeDAO.getById(other.getId(), false) == null : "Should no longer be able to find contributor type";
         tm.commit(ts);
	}

}
