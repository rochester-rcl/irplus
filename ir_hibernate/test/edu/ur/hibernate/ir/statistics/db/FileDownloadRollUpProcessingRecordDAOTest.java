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
import edu.ur.ir.statistics.FileDownloadRollUpProcessingRecord;
import edu.ur.ir.statistics.FileDownloadRollUpProcessingRecordDAO;
import edu.ur.ir.statistics.IgnoreIpAddress;
import edu.ur.ir.statistics.IgnoreIpAddressDAO;

/**
 * File download Processing record DAO test.
 * 
 * @author Nathan Sarr
 *
 */
public class FileDownloadRollUpProcessingRecordDAOTest 
{

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	
	/** transaction management  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
	
    /** transaction definition  */
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
    
	/** file roll up statistics */
	FileDownloadRollUpProcessingRecordDAO fileDownloadRollUpProcessingRecordDAO = (FileDownloadRollUpProcessingRecordDAO) ctx
	.getBean("fileDownloadRollUpProcessingRecordDAO");
	
	/**  file download info DAO */
	FileDownloadInfoDAO fileDownloadInfoDAO = (FileDownloadInfoDAO) ctx
	.getBean("fileDownloadInfoDAO");
	
	/** persistance for dealing with ignoring ip addesses */
	IgnoreIpAddressDAO ignoreIpAddressDAO = (IgnoreIpAddressDAO) ctx
	.getBean("ignoreIpAddressDAO");
	
	/**
	 * Test download file roll up persistance
	 */
	@Test
	public void fileDownloadRollUpProcessingRecordDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);
	    FileDownloadRollUpProcessingRecord rollUpProcessingRecord = new FileDownloadRollUpProcessingRecord(22l);
        
        fileDownloadRollUpProcessingRecordDAO.makePersistent(rollUpProcessingRecord);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 	    FileDownloadRollUpProcessingRecord other = fileDownloadRollUpProcessingRecordDAO.getById(rollUpProcessingRecord.getId(), false);
        assert other.equals(rollUpProcessingRecord) : "File download info should be equal other = \n" +
        other + "\n downloadInfo1 = " + rollUpProcessingRecord;
        tm.commit(ts);
         
        ts = tm.getTransaction(td);
        other = fileDownloadRollUpProcessingRecordDAO.getByIrFileId(22l);
        assert other != null : "Should be able to find record by irFileId";
        fileDownloadRollUpProcessingRecordDAO.makeTransient(other);
        assert  fileDownloadRollUpProcessingRecordDAO.getById(other.getId(), false) == null : "Should no longer be able to find file processing record";
	    tm.commit(ts);
	}
	
	/**
	 * Test insert from file download info DAO
	 */
	@Test
	public void insertCountsWithIngoreDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy");
	    Date d = simpleDateFormat.parse("1/1/2008");
        FileDownloadInfo downloadInfo1 = new FileDownloadInfo("123.0.0.1", 1l,d);
        downloadInfo1.setDownloadCount(1);
        fileDownloadInfoDAO.makePersistent(downloadInfo1);
        
        // create an ignore address
	    IgnoreIpAddress ip1 = new IgnoreIpAddress(123,0,0,7, 7);
        ignoreIpAddressDAO.makePersistent(ip1);
        
        // create a download for the same ir file but different ip address
        FileDownloadInfo downloadInfo2 = new FileDownloadInfo("123.0.0.7", 2l,d);
        downloadInfo2.setDownloadCount(1);
        fileDownloadInfoDAO.makePersistent(downloadInfo2);
 	    tm.commit(ts);
 	    
 	    // create a new insert
 	    ts = tm.getTransaction(td);
 	    long count = fileDownloadRollUpProcessingRecordDAO.updateAllRepositoryDownloadCounts();
 	    assert count == 2l : "Count should equal two but equals " + count;
 
 	    tm.commit(ts);
 	    
 	    
 	    ts = tm.getTransaction(td);
 	    // clean up
 	    fileDownloadRollUpProcessingRecordDAO.makeTransient(fileDownloadRollUpProcessingRecordDAO.getByIrFileId(downloadInfo1.getIrFileId()));
 	    fileDownloadRollUpProcessingRecordDAO.makeTransient(fileDownloadRollUpProcessingRecordDAO.getByIrFileId(downloadInfo2.getIrFileId()));

 	    fileDownloadInfoDAO.makeTransient(fileDownloadInfoDAO.getById(downloadInfo1.getId(), false));
        fileDownloadInfoDAO.makeTransient(fileDownloadInfoDAO.getById(downloadInfo2.getId(), false));
    	ignoreIpAddressDAO.makeTransient(ignoreIpAddressDAO.getById(ip1.getId(), false));
	    tm.commit(ts);
	}
	
}
