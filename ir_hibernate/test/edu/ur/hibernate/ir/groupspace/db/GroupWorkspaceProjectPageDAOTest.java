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
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageMember;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.groupspace.GroupWorkspaceUserDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;

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
	
	// group workspace user data access
	GroupWorkspaceUserDAO groupWorkspaceUserDAO = (GroupWorkspaceUserDAO) ctx
	.getBean("groupWorkspaceUserDAO");

	
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
	
	/**
	 * Test adding user to the group workspace project page 
	 */
	@Test
	public void addGroupWorkspaceProjectPageMemberDAOTest() throws Exception{

         
        TransactionStatus ts = tm.getTransaction(td);
		GroupWorkspace groupSpace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupSpace);
        GroupWorkspaceProjectPage groupProjectPage = groupSpace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupProjectPage);
        
        
        // add a user to the system
        UserEmail userEmail = new UserEmail("email");
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
        userDAO.makePersistent(user);
        
        GroupWorkspaceUser workspaceUser = groupSpace.add(user, false);
        
 		groupWorkspaceUserDAO.makePersistent(workspaceUser);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		GroupWorkspace other = groupWorkspaceDAO.getById(groupSpace.getId(), false);
        assert other.equals(groupSpace) : "Group space " + other + " should equal " + groupSpace;
        GroupWorkspaceProjectPage projectPage = other.getGroupWorkspaceProjectPage();
        assert projectPage != null : "Project page should not be null";
        
        workspaceUser = groupSpace.getUser(user);
        projectPage.addMember(workspaceUser);
        
        groupWorkspaceProjectPageDAO.makePersistent(projectPage);
        
        tm.commit(ts);        

 	    ts = tm.getTransaction(td);
 	    other = groupWorkspaceDAO.getById(groupSpace.getId(), false);
 	    
 	    projectPage = other.getGroupWorkspaceProjectPage();
 	    GroupWorkspaceProjectPageMember member = projectPage.getMember(workspaceUser);
 	    assert member != null : "Should be able to find member";
 	    assert member.getOrder() == 1 : "Order should be 1 but is " + member.getOrder();
 	    
        groupWorkspaceDAO.makeTransient(other);
        assert  groupWorkspaceDAO.getById(other.getId(), false) == null : "Should no longer be able to find groupSpace";
        userDAO.makeTransient(userDAO.getById(user.getId(), false));
        tm.commit(ts);
	}


	

}
