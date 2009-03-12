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

package edu.ur.hibernate.ir.handle.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.handle.HandleInfoDAO;
import edu.ur.ir.handle.HandleNameAuthority;
import edu.ur.ir.handle.HandleNameAuthorityDAO;

/**
 * Class for testing handle name authority persistence.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class HandleInfoDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** used to store handle name authority data */
	HandleNameAuthorityDAO handleNameAuthorityDAO = (HandleNameAuthorityDAO) ctx
	.getBean("handleNameAuthorityDAO");
	
	/** used to store handle name authority data */
	HandleInfoDAO handleInfoDAO = (HandleInfoDAO) ctx
	.getBean("handleInfoDAO");
	
	/** transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test basic persistance.
	 * 
	 * @throws Exception
	 */
	public void baseHandleNameAuthorityDAOTest() throws Exception
	{
		HandleNameAuthority handleNameAuthority = new HandleNameAuthority("12345678");
		
		HandleInfo info = new HandleInfo("1234", "http://www.google.com", handleNameAuthority);
 		
        TransactionStatus ts = tm.getTransaction(td);
        handleNameAuthorityDAO.makePersistent(handleNameAuthority);
        handleInfoDAO.makePersistent(info);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 	    HandleInfo other = handleInfoDAO.getById(info.getId(), false);
        assert other.equals(info) : "handle info's should be equal other = " + 
        other + " handle info = " + info;
        
        HandleInfo byAuthorityLocalName = handleInfoDAO.get(other.getNameAuthority().getNamingAuthority(), other.getLocalName());
        assert byAuthorityLocalName != null : "Should be able to find handle info by with " + other.getNameAuthority().getNamingAuthority() 
        + " and " + other.getLocalName();
        assert byAuthorityLocalName.equals(other) : "handle info's should be equal other = " + 
        other + " byAuthorityLocalName = " +  byAuthorityLocalName;
        
        long count = handleInfoDAO.getHandleCountForNameAuthority(handleNameAuthority.getId());
        assert  count == 1l : " Count should equal one but equals " + count;
        tm.commit(ts);
        
        ts = tm.getTransaction(td);
        handleInfoDAO.makeTransient(handleInfoDAO.getById(info.getId(), false));
        handleNameAuthorityDAO.makeTransient(handleNameAuthorityDAO.getById(handleNameAuthority.getId(), false));
        assert  handleInfoDAO.getById(other.getId(), false) == null : "Should no longer be able to find handle info";
	    tm.commit(ts);
	}

}