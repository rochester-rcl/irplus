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

package edu.ur.hibernate.ir.institution.db;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeDAO;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecord;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO;


/**
 * Test the persistence methods for an institutional item Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class InstitutionalItemIndexProcessingRecordDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** index porcessing type data access object */
	IndexProcessingTypeDAO indexProcessingTypeDAO = (IndexProcessingTypeDAO) ctx
	.getBean("indexProcessingTypeDAO");
	
	/** institutional item index processing record data access object */
	InstitutionalItemIndexProcessingRecordDAO institutionalItemIndexProcessingRecordDAO = (InstitutionalItemIndexProcessingRecordDAO) ctx
	.getBean("institutionalItemIndexProcessingRecordDAO");;
	
	/** platform transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test Institutional item processing record persistence
	 */
	@Test
	public void baseInstitutionalItemIndexProcessingRecordTest()
	{
		IndexProcessingType indexProcessingType = new IndexProcessingType("ipName");
 		indexProcessingType.setDescription("description");
         
        TransactionStatus ts = tm.getTransaction(td);
 		indexProcessingTypeDAO.makePersistent(indexProcessingType);
 		
 		InstitutionalItemIndexProcessingRecord itemIndexProcessingRecord = 
 			new InstitutionalItemIndexProcessingRecord(33l, indexProcessingType);
 		
 		Timestamp t = new Timestamp(new Date().getTime());
        itemIndexProcessingRecord.setUpdatedDate(t);
 		institutionalItemIndexProcessingRecordDAO.makePersistent(itemIndexProcessingRecord);
 	    tm.commit(ts);
 	    
 
 	    
 	    ts = tm.getTransaction(td);
 	    InstitutionalItemIndexProcessingRecord other = institutionalItemIndexProcessingRecordDAO.getById(itemIndexProcessingRecord.getId(), false);
        assert other.equals(itemIndexProcessingRecord) : "index processing record " + itemIndexProcessingRecord + "should be equal" + other;
         
        List<InstitutionalItemIndexProcessingRecord> records = institutionalItemIndexProcessingRecordDAO.getAllOrderByItemIdUpdatedDate();
        
        assert records.size() == 1 : "Should have at 1 record but has " + records.size();
        assert records.contains(other) : "records should contain other";
        
        institutionalItemIndexProcessingRecordDAO.makeTransient(other);
        indexProcessingTypeDAO.makeTransient(indexProcessingTypeDAO.getById(other.getId(), false));
        assert institutionalItemIndexProcessingRecordDAO.getById(other.getId(), false) == null : "Should no longer be able to find index processing record";
	    tm.commit(ts);

	}

}
