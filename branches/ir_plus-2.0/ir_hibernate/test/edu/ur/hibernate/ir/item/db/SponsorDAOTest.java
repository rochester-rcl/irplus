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

package edu.ur.hibernate.ir.item.db;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.SponsorDAO;

/**
 * Test the persistence methods for a sponsor
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class SponsorDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	SponsorDAO sponsorDAO = (SponsorDAO) ctx
	.getBean("sponsorDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test Sponsor persistence
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseSponsorDAOTest() throws Exception{

		Sponsor sponsor = new Sponsor("sponsorName");
 		sponsor.setDescription("sponsorDescription");
         
        TransactionStatus ts = tm.getTransaction(td);

 		 sponsorDAO.makePersistent(sponsor);
 		 Sponsor other = sponsorDAO.getById(sponsor.getId(), false);
         assert other.equals(sponsor) : "Sponsors should be equal";
         
         List<Sponsor> sponsors =  sponsorDAO.getAllOrderByName(0, 1);
         assert sponsors.size() == 1 : "One sponsor should be found";
         
         assert sponsorDAO.getAllNameOrder().size() == 1 : "One sponsor should be found";
         
         Sponsor sponsorByName =  sponsorDAO.findByUniqueName(sponsor.getName());
        
         assert sponsorByName.equals(sponsor) : "Sponsor should be found";
         
         sponsorDAO.makeTransient(other);
         assert  sponsorDAO.getById(other.getId(), false) == null : 
        	 "Should no longer be able to find sponsor";
        
         tm.commit(ts);
	}
}
