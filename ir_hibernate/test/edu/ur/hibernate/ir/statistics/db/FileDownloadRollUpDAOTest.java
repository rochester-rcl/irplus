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


package edu.ur.hibernate.ir.statistics.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;

import edu.ur.ir.statistics.FileDownloadRollUp;
import edu.ur.ir.statistics.FileDownloadRollUpDAO;


/**
 * File Download roll up DAO test.
 * 
 * @author Nathan Sarr
 *
 */
public class FileDownloadRollUpDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	
	/** transaction management  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
	
    /** transaction definition  */
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
    
	/** file roll up statistics */
	FileDownloadRollUpDAO fileDownloadRollUpDAO = (FileDownloadRollUpDAO) ctx
	.getBean("fileDownloadRollUpDAO");
	
	
	/**
	 * Test download file roll up persistance
	 */
	@Test
	public void baseDownloadInfoDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);
        FileDownloadRollUp fileDownloadRollUp = new FileDownloadRollUp(22l, 44l);
	   
      
        
        fileDownloadRollUpDAO.makePersistent(fileDownloadRollUp);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		FileDownloadRollUp other = fileDownloadRollUpDAO.getById(fileDownloadRollUp.getId(), false);
        assert other.equals(fileDownloadRollUp) : "File download info should be equal other = \n" +
        other + "\n downloadInfo1 = " + fileDownloadRollUp;
         
        fileDownloadRollUpDAO.makeTransient(other);
        assert  fileDownloadRollUpDAO.getById(other.getId(), false) == null : "Should no longer be able to find file download info";
	    tm.commit(ts);
	}
	




}
