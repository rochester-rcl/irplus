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
import edu.ur.ir.item.ItemLink;
import edu.ur.ir.item.ItemLinkDAO;


/**
 * Test the persistance methods for item link information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ItemLinkDAOTest {
	

    ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	GenericItemDAO itemDAO = (GenericItemDAO) ctx.getBean("itemDAO");

	ItemLinkDAO itemLinkDAO = (ItemLinkDAO) ctx
	.getBean("itemLinkDAO");
	
	// Check the repositories
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
			.getBean("transactionManager");

	// Start the transaction this is for lazy loading
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);

	/**
	 * Test item link persistance
	 */
	@Test
	public void baseItemLinkDAOTest() throws Exception{
		
		TransactionStatus ts = tm.getTransaction(td);
		GenericItem item = new GenericItem("item");
		
		ItemLink itemLink = item.createLink("msnbc", "http://www.msnbc.com");
		itemDAO.makePersistent(item);
		
		tm.commit(ts);
		
		ts = tm.getTransaction(td);

		assert itemLinkDAO.getById(itemLink.getId(), false).equals(itemLink) : "item  link should be found";
		item.removeLink(itemLink);
		
		tm.commit(ts);

        itemLinkDAO.makeTransient(itemLink);
        assert itemLinkDAO.getById(itemLink.getId(), false) == null : "Should not find the item link"; 
		// delete the item
		itemDAO.makeTransient(itemDAO.getById(item.getId(), false));
		
	}

}
