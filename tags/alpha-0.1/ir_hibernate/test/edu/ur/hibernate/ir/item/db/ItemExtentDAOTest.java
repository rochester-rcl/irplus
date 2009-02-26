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
import edu.ur.ir.item.ExtentType;
import edu.ur.ir.item.ExtentTypeDAO;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.item.ItemExtent;
import edu.ur.ir.item.ItemExtentDAO;

/**
 * Test the persistance methods for item extent Information
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ItemExtentDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
			
	/** Extent relational data access */
	ExtentTypeDAO extentTypeDAO = (ExtentTypeDAO) ctx.getBean("extentTypeDAO");
			
	/** Item extent relatioanal data access. */
	ItemExtentDAO itemExtentDAO = (ItemExtentDAO) ctx.getBean("itemExtentDAO");		
	
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
	public void baseItemExtentDAOTest() throws Exception{

		ExtentType identType = new ExtentType();
		identType.setName("identTypeName");
 		identType.setDescription("identTypeDescription");
 		extentTypeDAO.makePersistent(identType);

		TransactionStatus ts = tm.getTransaction(td);
		GenericItem item = new GenericItem("item2");
		itemDAO.makePersistent(item);
		
        //commit the transaction 
		tm.commit(ts);
		

		ItemExtent itemExtent = item.addItemExtent(identType, "333-44-7856");
		itemExtentDAO.makePersistent(itemExtent);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		assert itemExtentDAO.getById(itemExtent.getId(), false).equals(itemExtent) : "item extent should be found";
		item.removeItemExtent(itemExtent);
        
        tm.commit(ts);
        
        itemExtentDAO.makeTransient(itemExtent);
        assert itemExtentDAO.getById(itemExtent.getId(), false) == null : "Should not find the item extent"; 
        
        extentTypeDAO.makeTransient(extentTypeDAO.getById(identType.getId(), false));
        
		// delete the item
		itemDAO.makeTransient(itemDAO.getById(item.getId(), false));
	}
}
