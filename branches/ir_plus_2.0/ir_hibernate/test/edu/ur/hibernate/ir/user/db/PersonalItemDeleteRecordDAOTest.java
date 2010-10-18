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

package edu.ur.hibernate.ir.user.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.user.PersonalItemDeleteRecord;
import edu.ur.ir.user.PersonalItemDeleteRecordDAO;

/**
 * Test the persistence methods for personal item delete records
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)

public class PersonalItemDeleteRecordDAOTest {
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	PersonalItemDeleteRecordDAO personalItemDeleteRecordDAO = 
		(PersonalItemDeleteRecordDAO)ctx.getBean("personalItemDeleteRecordDAO");
	
	// Transaction manager
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
    		TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test personal item delete record
	 */
	@Test
	public void basePersonalItemDeleteRecordDAOTest() throws Exception{

        TransactionStatus ts = tm.getTransaction(td);
        PersonalItemDeleteRecord personalItemDeleteRecord = new PersonalItemDeleteRecord(22l, 44l, "/my/path/toitem/item", "the item description");
	
        personalItemDeleteRecordDAO.makePersistent(personalItemDeleteRecord);
 		tm.commit(ts);
 		PersonalItemDeleteRecord other = personalItemDeleteRecordDAO.getById(personalItemDeleteRecord.getId(), false);
        assert other.equals(personalItemDeleteRecord) : "personal files should be equal";
         
        assert personalItemDeleteRecordDAO.getCount() == 1 : "Should find one record but found " +  personalItemDeleteRecordDAO.getCount();
        personalItemDeleteRecordDAO.makeTransient(other);
        assert  personalItemDeleteRecordDAO.getById(other.getId(), false) == null : "Should no longer be able to find personal file";
	}


}
