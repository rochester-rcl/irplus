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
import edu.ur.ir.statistics.IpIgnoreFileDownloadInfo;
import edu.ur.ir.statistics.IpIgnoreFileDownloadInfoDAO;


/**
 * 
 * Interface to save file download info 
 * 
 * @author Nathan Sarr
 *
 */
public class IpIgnoreFileDownloadInfoDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	IpIgnoreFileDownloadInfoDAO ipIgnorefileDownloadInfoDAO = (IpIgnoreFileDownloadInfoDAO) ctx
	.getBean("ipIgnoreFileDownloadInfoDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);

	/**
	 * Test download info persistance
	 */
	@Test
	public void baseIpIgnoreFileDownloadInfoDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);

	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy");
	    Date d = simpleDateFormat.parse("1/1/2008");
        IpIgnoreFileDownloadInfo ipIgnoredownloadInfo1 = new IpIgnoreFileDownloadInfo("123.0.0.1", 1l,d);
        ipIgnoredownloadInfo1.setDownloadCount(1);
        
        ipIgnorefileDownloadInfoDAO.makePersistent(ipIgnoredownloadInfo1);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		IpIgnoreFileDownloadInfo other = ipIgnorefileDownloadInfoDAO.getById(ipIgnoredownloadInfo1.getId(), false);
        assert other.equals(ipIgnoredownloadInfo1) : "Ip Ignore File download info should be equal other = \n" + other + "\n downloadInfo1 = " + ipIgnoredownloadInfo1;
         
        ipIgnorefileDownloadInfoDAO.makeTransient(other);
        assert  ipIgnorefileDownloadInfoDAO.getById(other.getId(), false) == null : "Should no longer be able to find file download info";
	    tm.commit(ts);
	}
}
