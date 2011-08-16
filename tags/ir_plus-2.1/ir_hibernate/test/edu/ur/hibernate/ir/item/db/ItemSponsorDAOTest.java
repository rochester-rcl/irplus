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

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.item.ItemSponsor;
import edu.ur.ir.item.ItemSponsorDAO;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.SponsorDAO;

/**
 * Test the persistance methods for item sponsor Information
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ItemSponsorDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
			
	/** Sponsor relational data access */
	SponsorDAO sponsorDAO = (SponsorDAO) ctx.getBean("sponsorDAO");
			
	/** Item sponsor relatioanal data access. */
	ItemSponsorDAO itemSponsorDAO = (ItemSponsorDAO) ctx.getBean("itemSponsorDAO");		
	
	/** Item relational data access. */
	GenericItemDAO itemDAO = (GenericItemDAO) ctx.getBean("itemDAO");

	/** Platform transaction manager.  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
			.getBean("transactionManager");

	/** transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test Contributor type persistence
	 */
	@Test
	public void baseItemSponsorDAOTest() throws Exception{

		Sponsor sponsor = new Sponsor("sponsorName");
 		sponsor.setDescription("sponsorDescription");
 		sponsorDAO.makePersistent(sponsor);

		TransactionStatus ts = tm.getTransaction(td);
		GenericItem item = new GenericItem("item2");
		itemDAO.makePersistent(item);
		
        //commit the transaction 
		tm.commit(ts);
		
	    ItemSponsor itemSponsor = item.addItemSponsor(sponsor, "description");
		itemSponsorDAO.makePersistent(itemSponsor);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		assert itemSponsorDAO.getById(itemSponsor.getId(), false).equals(itemSponsor) : "item sponsor should be found";
		item.removeItemSponsor(itemSponsor);
        
        tm.commit(ts);
        
        itemSponsorDAO.makeTransient(itemSponsor);
        assert itemSponsorDAO.getById(itemSponsor.getId(), false) == null : "Should not find the item sponsor"; 
        
        sponsorDAO.makeTransient(sponsorDAO.getById(sponsor.getId(), false));
        
		// delete the item
		itemDAO.makeTransient(itemDAO.getById(item.getId(), false));
	}
}
