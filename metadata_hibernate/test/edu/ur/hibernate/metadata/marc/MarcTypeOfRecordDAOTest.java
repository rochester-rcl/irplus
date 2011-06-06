/**  
   Copyright 2008-2011 University of Rochester

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


package edu.ur.hibernate.metadata.marc;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.metadata.helper.test.ContextHolder;
import edu.ur.metadata.marc.MarcTypeOfRecord;
import edu.ur.metadata.marc.MarcTypeOfRecordDAO;

/**
 * Test marc type of record persistence.
 * 
 * @author Nathan Sarr
 *
 */
public class MarcTypeOfRecordDAOTest {

    /** spring application context manager  */
    ApplicationContext ctx = ContextHolder.getApplicationContext();
    
    /** dublin core element data access object */
    MarcTypeOfRecordDAO marcTypeOfRecordDAO = (MarcTypeOfRecordDAO)ctx.getBean("marcTypeOfRecordDAO");
    
    /** platform transaction manager  */
    PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
    
    /** transaction definition */
    TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
    
	/**
	 * Test dublin core term persistence
	 */
	@Test
	public void baseMarcTypeOfRecordDAOTest() throws Exception{

		MarcTypeOfRecord element = new MarcTypeOfRecord("name", "a" );
 		element.setDescription("dsescription");
         
        TransactionStatus ts = tm.getTransaction(td);
 		marcTypeOfRecordDAO.makePersistent(element);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		MarcTypeOfRecord other = marcTypeOfRecordDAO.getById(element.getId(), false);
        assert other.equals(element) : "Marc record type should be equal mt = " + element + " other = " + other;
         
        MarcTypeOfRecord typeOfRecordByName =  marcTypeOfRecordDAO.findByUniqueName(element.getName());
        assert typeOfRecordByName.equals(element) : "Marc record type should be found " + element; 
        
        MarcTypeOfRecord typeOfRecordByRecordType =  marcTypeOfRecordDAO.getByRecordType(element.getRecordType());
        assert typeOfRecordByRecordType.equals(element) : "Marc record type should be found " + element; 
         
        marcTypeOfRecordDAO.makeTransient(other);
        assert  marcTypeOfRecordDAO.getById(other.getId(), false) == null : "Should no longer be able to find marc record type";
	    tm.commit(ts);
	}
}
