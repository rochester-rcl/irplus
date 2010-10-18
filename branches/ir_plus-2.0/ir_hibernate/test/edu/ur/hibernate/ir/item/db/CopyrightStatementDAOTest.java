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

package edu.ur.hibernate.ir.item.db;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.item.CopyrightStatement;
import edu.ur.ir.item.CopyrightStatementDAO;
import edu.ur.order.OrderType;

/**
 * Test the persistance methods for copyright statement Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class CopyrightStatementDAOTest {

	/** get the application context */
	ApplicationContext csx = ContextHolder.getApplicationContext();

	CopyrightStatementDAO copyrightStatementDAO = (CopyrightStatementDAO) csx
	.getBean("copyrightStatementDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) csx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
    		TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test Contributor type persistence
	 */
	@Test
	public void baseCopyrightStatementDAOTest() throws Exception{

		CopyrightStatement cs = new CopyrightStatement("csName");
 		cs.setDescription("csDescription");
         
        TransactionStatus ts = tm.getTransaction(td);
 		copyrightStatementDAO.makePersistent(cs);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		CopyrightStatement other = copyrightStatementDAO.getById(cs.getId(), false);
        assert other.equals(cs) : "Copyright statements should be equal";
         
        List<CopyrightStatement> itemCopyrightStatements =  copyrightStatementDAO.getCopyrightStatementsOrderByName(0, 1, OrderType.ASCENDING_ORDER);
        assert itemCopyrightStatements.size() == 1 : "One copyright statement should be found";
        assert copyrightStatementDAO.getAll().size() == 1 : "One copyrightStatement should be found";
         
        CopyrightStatement itemCopyrightStatementByName =  copyrightStatementDAO.findByUniqueName(cs.getName());
        assert itemCopyrightStatementByName.equals(cs) : "Copyright statement should be found";
         
        copyrightStatementDAO.makeTransient(other);
        assert  copyrightStatementDAO.getById(other.getId(), false) == null : "Should no longer be able to find copyright statement";
	    tm.commit(ts);
	}
}
