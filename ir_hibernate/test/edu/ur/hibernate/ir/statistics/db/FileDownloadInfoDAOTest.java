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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.statistics.FileDownloadInfo;
import edu.ur.ir.statistics.FileDownloadInfoDAO;


/**
 * Test file download info persistance
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class FileDownloadInfoDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	FileDownloadInfoDAO fileDownloadInfoDAO = (FileDownloadInfoDAO) ctx
	.getBean("fileDownloadInfoDAO");
	
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);

	/**
	 * Test download info persistance
	 */
	@Test
	public void baseDownloadInfoDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);

	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy");
	    Date d = simpleDateFormat.parse("1/1/2008");
        FileDownloadInfo downloadInfo1 = new FileDownloadInfo("123.0.0.1", 1l,d);
        downloadInfo1.setDownloadCount(1);
        
        fileDownloadInfoDAO.makePersistent(downloadInfo1);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		FileDownloadInfo other = fileDownloadInfoDAO.getById(downloadInfo1.getId(), false);
        assert other.equals(downloadInfo1) : "File download info should be equal other = \n" + other + "\n downloadInfo1 = " + downloadInfo1;
         
        fileDownloadInfoDAO.makeTransient(other);
        assert  fileDownloadInfoDAO.getById(other.getId(), false) == null : "Should no longer be able to find file download info";
	    tm.commit(ts);
	}

}
