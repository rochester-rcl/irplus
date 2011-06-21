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
	 * Test item extent type persistence
	 */
	@Test
	public void baseItemExtentDAOTest() throws Exception{
		TransactionStatus ts = tm.getTransaction(td);

		ExtentType extentType = new ExtentType("extentTypeName","extentTypeDescription" );
 		extentTypeDAO.makePersistent(extentType);

		GenericItem item = new GenericItem("item2");
		itemDAO.makePersistent(item);
		
		ItemExtent itemExtent = item.addItemExtent(extentType, "333-44-7856");
		itemExtentDAO.makePersistent(itemExtent);
        //commit the transaction 
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);
		assert itemExtentDAO.getById(itemExtent.getId(), false).equals(itemExtent) : "item extent should be found";
		item.removeItemExtent(itemExtent);
		itemExtentDAO.makeTransient(itemExtentDAO.getById(itemExtent.getId(), false));
        extentTypeDAO.makeTransient(extentTypeDAO.getById(extentType.getId(), false));
        
		// delete the item
		itemDAO.makeTransient(itemDAO.getById(item.getId(), false));
        tm.commit(ts);

	}
	
	/**
	 * Test Contributor type persistence
	 */
	@Test
	public void countItemExtentDAOTest() throws Exception{

		TransactionStatus ts = tm.getTransaction(td);
		ExtentType extentType = new ExtentType("extentTypeName", "extentTypeDescription");
 		extentTypeDAO.makePersistent(extentType);
 		
 		ExtentType extentType2 = new ExtentType("extentTypeName2", "extentTypeName2");
 		extentTypeDAO.makePersistent(extentType2);

		
		GenericItem item = new GenericItem("item");
		GenericItem item2 = new GenericItem("item2");
		itemDAO.makePersistent(item);
		itemDAO.makePersistent(item2);
       
		

		ItemExtent itemExtent = item.addItemExtent(extentType, "333-44-7856");
		ItemExtent itemExtent2 = item2.addItemExtent(extentType, "333-44-7856");
		ItemExtent itemExtent3 = item.addItemExtent(extentType2, "333-44-7856");

		itemExtentDAO.makePersistent(itemExtent);
		itemExtentDAO.makePersistent(itemExtent2);
		itemExtentDAO.makePersistent(itemExtent3);
		 //commit the transaction 
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		long count = itemExtentDAO.getItemCount(extentType);
		assert count == 2l : "Should find two but found " + count;
		
		long count2 = itemExtentDAO.getItemCount(extentType2);
		assert count2 == 1l : "Cound should be one but found " + count;
        
        tm.commit(ts);

		ts = tm.getTransaction(td);

		// delete the item
		itemDAO.makeTransient(itemDAO.getById(item.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(item2.getId(), false));
        
        extentTypeDAO.makeTransient(extentTypeDAO.getById(extentType.getId(), false));
        extentTypeDAO.makeTransient(extentTypeDAO.getById(extentType2.getId(), false));
        

        tm.commit(ts);

	}
}
