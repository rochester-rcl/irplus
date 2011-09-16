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
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.groupspace.GroupWorkspaceUserDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;

/**
 * 
 * Test persistence of the group workspace group data access object.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class GroupWorkspaceUserDAOTest 
{
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	// group workspace group data access
	GroupWorkspaceUserDAO groupWorkspaceUserDAO = (GroupWorkspaceUserDAO) ctx
	.getBean("groupWorkspaceUserDAO");

	// group workspace data access
	GroupWorkspaceDAO groupWorkspaceDAO = (GroupWorkspaceDAO) ctx
	.getBean("groupWorkspaceDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
	TransactionDefinition td = new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRED);
	
    /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");


	
	/**
	 * Test group workspace user persistence
	 */
	@Test
	public void addUserToWorkspaceDAOTest() throws Exception{
		
	    // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
    	UserEmail userEmail = new UserEmail("email");
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
        userDAO.makePersistent(user);
        GroupWorkspace groupSpace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupSpace);
        GroupWorkspaceUser workspaceUser = groupSpace.add(user, false);
       
 		groupWorkspaceUserDAO.makePersistent(workspaceUser);
        
 		tm.commit(ts);
 		
 		
        
 		ts = tm.getTransaction(td);
 		workspaceUser = groupWorkspaceUserDAO.getById(workspaceUser.getId(), false);
 		assert workspaceUser.getUser().equals(user) : "Workspace user should contain user " + user;
 		
        groupWorkspaceUserDAO.makeTransient(workspaceUser);
        userDAO.makeTransient(userDAO.getById(user.getId(), false));
        groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupSpace.getId(), false));
        tm.commit(ts);
        
	}

}
