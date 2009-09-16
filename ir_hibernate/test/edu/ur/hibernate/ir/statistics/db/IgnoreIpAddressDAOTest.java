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
import edu.ur.ir.statistics.IgnoreIpAddress;
import edu.ur.ir.statistics.IgnoreIpAddressDAO;

/**
 * Test ignore ipaddress persistance
 * 
 * @author Sharmila Ranganathan
 *
 */
public class IgnoreIpAddressDAOTest {

	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** Data access for ignore ip address class */
	IgnoreIpAddressDAO ignoreIpAddressDAO = (IgnoreIpAddressDAO) ctx
	.getBean("ignoreIpAddressDAO");
	
	/** platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    /** transaction definition */
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);

    
	/**
	 * Test Ignore Ip address range persistance
	 */
	@Test
	public void baseIgnoreIpInfoDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);

	    IgnoreIpAddress ip1 = new IgnoreIpAddress(123,0,0,1, 10);
        
        ignoreIpAddressDAO.makePersistent(ip1);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 	    IgnoreIpAddress other = ignoreIpAddressDAO.getById(ip1.getId(), false);
        assert other.equals(ip1) : "Ignore Ip address range should be equal";
         
        ignoreIpAddressDAO.makeTransient(other);
        assert  ignoreIpAddressDAO.getById(other.getId(), false) == null : "Should no longer be able to find Ignore Ip address range";
	    tm.commit(ts);
	}
	
	/**
	 * Make sure the count is correct for a given ip address.
	 */
	public void checkIngoreAddressCountTest()
	{
		 TransactionStatus ts = tm.getTransaction(td);

		 IgnoreIpAddress ip1 = new IgnoreIpAddress(123,0,0,1, 10);
	     ignoreIpAddressDAO.makePersistent(ip1);

	     IgnoreIpAddress ip2 = new IgnoreIpAddress(123,0,0,5, 5);
	     ignoreIpAddressDAO.makePersistent(ip1);
	     tm.commit(ts);

	 	 // check to make sure the ip address counts are correct
	 	 ts = tm.getTransaction(td);
	 	 
	 	 int count = ignoreIpAddressDAO.getIgnoreCountForIp(123, 0, 0, 5);  
	 	 assert count == 2 : "count equals " + count;
	 	 
	 	 count = ignoreIpAddressDAO.getIgnoreCountForIp(123, 0, 0, 7);  
	 	 assert count == 1 : "count equals " + count;
	 	 
	 	 count = ignoreIpAddressDAO.getIgnoreCountForIp(123, 0, 0, 11);  
	 	 assert count == 0 : "count equals " + count;
	 	 
	 	 
	 	 tm.commit(ts);
	     
	 	 
	 	 ts = tm.getTransaction(td);
	     ignoreIpAddressDAO.makeTransient(ignoreIpAddressDAO.getById(ip1.getId(), false));
	     ignoreIpAddressDAO.makeTransient(ignoreIpAddressDAO.getById(ip2.getId(), false));
		 tm.commit(ts);
	}
	


}
