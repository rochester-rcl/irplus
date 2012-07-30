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

package edu.ur.hibernate.ir.index.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeDAO;


/**
 * Test for accessing index processing types.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IndexProcessingTypeDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	IndexProcessingTypeDAO indexProcessingTypeDAO = (IndexProcessingTypeDAO) ctx
	.getBean("indexProcessingTypeDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test Index processing type persistence
	 */
	@Test
	public void baseIndexProcessingTypeDAOTest() throws Exception{

		IndexProcessingType indexProcessintType = new IndexProcessingType("ipName");
 		indexProcessintType.setDescription("description");
         
        TransactionStatus ts = tm.getTransaction(td);
 		indexProcessingTypeDAO.makePersistent(indexProcessintType);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		IndexProcessingType other = indexProcessingTypeDAO.getById(indexProcessintType.getId(), false);
        assert other.equals(indexProcessintType) : "index processing types should be equal";
         
        IndexProcessingType itemIndexProcessingTypeByName =  indexProcessingTypeDAO.findByUniqueName(indexProcessintType.getName());
        assert itemIndexProcessingTypeByName.equals(indexProcessintType) : "Index processing type should be found";
         
        indexProcessingTypeDAO.makeTransient(other);
        assert  indexProcessingTypeDAO.getById(other.getId(), false) == null : "Should no longer be able to find index processing type";
	    tm.commit(ts);
	}

}
