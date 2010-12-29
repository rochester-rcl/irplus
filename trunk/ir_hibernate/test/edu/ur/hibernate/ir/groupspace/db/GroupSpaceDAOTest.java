/**  
   Copyright 2008 - 2010 University of Rochester

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


package edu.ur.hibernate.ir.groupspace.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.groupspace.GroupSpace;
import edu.ur.ir.groupspace.GroupSpaceDAO;

/**
 * Group space data access object test.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class GroupSpaceDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	GroupSpaceDAO groupSpaceDAO = (GroupSpaceDAO) ctx
	.getBean("groupSpaceDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test content type persistance
	 */
	@Test
	public void simpleGroupSpaceDAOTest() throws Exception{

		GroupSpace groupSpace = new GroupSpace("grouName", "groupDescription");
         
        TransactionStatus ts = tm.getTransaction(td);
 		groupSpaceDAO.makePersistent(groupSpace);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		GroupSpace other = groupSpaceDAO.getById(groupSpace.getId(), false);
        assert other.equals(groupSpace) : "Group space " + other + " should equal " + groupSpace;
        tm.commit(ts);        

 	    ts = tm.getTransaction(td);
 	    other = groupSpaceDAO.getById(groupSpace.getId(), false);
        groupSpaceDAO.makeTransient(other);
        assert  groupSpaceDAO.getById(other.getId(), false) == null : "Should no longer be able to find groupSpace";
	    tm.commit(ts);
	}
}
