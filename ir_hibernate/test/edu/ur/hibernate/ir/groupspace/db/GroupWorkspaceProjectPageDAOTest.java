/**  
   Copyright 2008 - 2011 University of Rochester

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
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageDAO;
import edu.ur.ir.user.IrUserDAO;

/**
 * Group workspace project page tests.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceProjectPageDAOTest {
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	GroupWorkspaceDAO groupWorkspaceDAO = (GroupWorkspaceDAO) ctx
	.getBean("groupWorkspaceDAO");
	
	GroupWorkspaceProjectPageDAO groupWorkspaceProjectPageDAO = (GroupWorkspaceProjectPageDAO) ctx
	.getBean("groupWorkspaceProjectPageDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
    /** User data access */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");

    
	/**
	 * Test group space persistence
	 */
	@Test
	public void simpleGroupWorkspaceProjectPageDAOTest() throws Exception{

         
        TransactionStatus ts = tm.getTransaction(td);
		GroupWorkspace groupSpace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupSpace);
        GroupWorkspaceProjectPage groupProjectPage = groupSpace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupProjectPage);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		GroupWorkspace other = groupWorkspaceDAO.getById(groupSpace.getId(), false);
        assert other.equals(groupSpace) : "Group space " + other + " should equal " + groupSpace;
        tm.commit(ts);        

 	    ts = tm.getTransaction(td);
 	    other = groupWorkspaceDAO.getById(groupSpace.getId(), false);
        groupWorkspaceDAO.makeTransient(other);
        assert  groupWorkspaceDAO.getById(other.getId(), false) == null : "Should no longer be able to find groupSpace";
	    tm.commit(ts);
	}

	

}
