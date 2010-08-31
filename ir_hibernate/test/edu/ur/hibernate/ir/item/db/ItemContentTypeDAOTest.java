/**  
   Copyright 2008-2010 University of Rochester

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
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeDAO;
import edu.ur.ir.item.ItemContentType;
import edu.ur.ir.item.ItemContentTypeDAO;

/**
 * Test the persistence of the item content type.
 * 
 * @author Nathan Sarr
 *
 */
public class ItemContentTypeDAOTest {

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
			
	/** Identifier relational data access */
	ContentTypeDAO contentTypeDAO = (ContentTypeDAO) ctx.getBean("contentTypeDAO");
			
	/** Item content type relatioanal data access. */
	ItemContentTypeDAO itemContentTypeDAO = (ItemContentTypeDAO) ctx.getBean("itemContentTypeDAO");		
	
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
	public void baseItemContentTypeDAOTest() throws Exception{
		TransactionStatus ts = tm.getTransaction(td);
		//commit the transaction 
		
		ContentType contentType = new ContentType("contentTypeName","contentTypeDescription" );
 		contentTypeDAO.makePersistent(contentType);

		
		GenericItem item = new GenericItem("item2");
		itemDAO.makePersistent(item);
		
     	ItemContentType itemContentType = item.setPrimaryContentType(contentType);
		itemContentTypeDAO.makePersistent(itemContentType);
		
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		assert itemContentTypeDAO.getById(itemContentType.getId(), false).equals(itemContentType) : "item content type " + itemContentType +  " should be found";
		item.removeItemContentType(itemContentType);
        
        tm.commit(ts);
        
        ts = tm.getTransaction(td);
        itemContentTypeDAO.makeTransient(itemContentType);
        assert itemContentTypeDAO.getById(itemContentType.getId(), false) == null : "Should not find the item content type " + itemContentType; 
        contentTypeDAO.makeTransient(contentTypeDAO.getById(contentType.getId(), false));
        
        assert contentTypeDAO.getById(contentType.getId(), false) == null : " Should not be able to find content type " + contentType;
		// delete the item
		itemDAO.makeTransient(itemDAO.getById(item.getId(), false));
		tm.commit(ts);
	        
	}
	
}
