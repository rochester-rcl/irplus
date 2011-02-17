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
import edu.ur.ir.groupspace.GroupWorkspaceGroup;
import edu.ur.ir.groupspace.GroupWorkspaceGroupDAO;
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
public class GroupWorkspaceGroupDAOTest 
{
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	// group workspace group data access
	GroupWorkspaceGroupDAO groupWorkspaceGroupDAO = (GroupWorkspaceGroupDAO) ctx
	.getBean("groupWorkspaceGroupDAO");

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
	 * Test group persistence
	 */
	@Test
	public void baseGroupDAOTest() throws Exception{
		
        TransactionStatus ts = tm.getTransaction(td);
		GroupWorkspace groupSpace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupSpace);
        GroupWorkspaceGroup group = groupSpace.createGroup("groupName");
        group.setDescription("groupDescription");
 		groupWorkspaceGroupDAO.makePersistent(group);
 	    tm.commit(ts);

		
 	    ts = tm.getTransaction(td);
 	    GroupWorkspaceGroup other = groupWorkspaceGroupDAO.getById(group.getId(), false);
        assert other.equals(group) : "Role types should be equal";
        groupWorkspaceGroupDAO.makeTransient(other);
        assert  groupWorkspaceGroupDAO.getById(other.getId(), false) == null : "Should no longer be able to find group";
        groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupSpace.getId(), false));
        tm.commit(ts);


	}
	
	/**
	 * Test group persistence
	 */
	@Test
	public void addUserToGroupDAOTest() throws Exception{
		
	    // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
    	UserEmail userEmail = new UserEmail("email");
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
        userDAO.makePersistent(user);
 		tm.commit(ts);
 		
 		//start a new transaction
 		ts = tm.getTransaction(td);
 		
 		GroupWorkspace groupSpace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupSpace);
        GroupWorkspaceGroup group = groupSpace.createGroup("groupName");
        group.setDescription("groupDescription");
 		group.addUser(user);
 		groupWorkspaceGroupDAO.makePersistent(group);
 		tm.commit(ts);
        
 		ts = tm.getTransaction(td);
 		group = groupWorkspaceGroupDAO.getById(group.getId(), false);
 		assert group.getUsers().contains(user) : "Group should contain user " + user;
 		
 		//make sure select works.
 		user = groupWorkspaceGroupDAO.getUserForGroup(group.getId(), user.getId());	
 		assert user != null : "Should find user";
 		
 		IrUser otherUser = groupWorkspaceGroupDAO.getUserForGroup(group.getId(), user.getId() + 55);
 		assert otherUser == null : "Should not find other user";
 		
        groupWorkspaceGroupDAO.makeTransient(group);
        userDAO.makeTransient(userDAO.getById(user.getId(), false));
        groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupSpace.getId(), false));
        tm.commit(ts);
        
	}

}
