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
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeDAO;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.item.ItemIdentifier;
import edu.ur.ir.item.ItemIdentifierDAO;

/**
 * Test the persistance methods for item identifier Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ItemIdentifierDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
			
	/** Identifier relational data access */
	IdentifierTypeDAO identifierTypeDAO = (IdentifierTypeDAO) ctx.getBean("identifierTypeDAO");
			
	/** Item identifier relatioanal data access. */
	ItemIdentifierDAO itemIdentifierDAO = (ItemIdentifierDAO) ctx.getBean("itemIdentifierDAO");		
	
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
	public void baseItemIdentifierDAOTest() throws Exception{

		IdentifierType identType = new IdentifierType();
		identType.setName("identTypeName");
 		identType.setDescription("identTypeDescription");
 		identifierTypeDAO.makePersistent(identType);

		TransactionStatus ts = tm.getTransaction(td);
		GenericItem item = new GenericItem("item2");
		itemDAO.makePersistent(item);
		
        //commit the transaction 
		tm.commit(ts);
		
		ItemIdentifier itemIdentifier = item.addItemIdentifier("333-44-7856", identType);
		itemIdentifierDAO.makePersistent(itemIdentifier);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		assert itemIdentifierDAO.getById(itemIdentifier.getId(), false).equals(itemIdentifier) : "item identifier should be found";
		item.removeItemIdentifier(itemIdentifier);
        
        tm.commit(ts);
        
        itemIdentifierDAO.makeTransient(itemIdentifier);
        assert itemIdentifierDAO.getById(itemIdentifier.getId(), false) == null : "Should not find the item identifier"; 
        
        identifierTypeDAO.makeTransient(identifierTypeDAO.getById(identType.getId(), false));
        
		// delete the item
		itemDAO.makeTransient(itemDAO.getById(item.getId(), false));
	}
}
