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

package edu.ur.hibernate.ir.user.db;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.IrUserGroupDAO;
import edu.ur.ir.user.UserEmail;

/**
 * Test the persistance methods for group Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrUserGroupDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	IrUserGroupDAO irGroupDAO = (IrUserGroupDAO) ctx
	.getBean("irUserGroupDAO");

	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
	TransactionDefinition td = new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRED);
	
    /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");

	/**
	 * Test group persistence
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseGroupDAOTest() throws Exception{
		
	
		IrUserGroup group = new IrUserGroup("groupName");
		
 		group.setDescription("groupDescription");
         
 		irGroupDAO.makePersistent(group);
 		IrUserGroup other = irGroupDAO.getById(group.getId(), false);
         assert other.equals(group) : "Role types should be equal";
         
         List<IrUserGroup> itemIrGroups =  irGroupDAO.getAllOrderByName(0, 1);
         assert itemIrGroups.size() == 1 : "One group should be found";
         
         
         assert irGroupDAO.getAllNameOrder().size() == 1 : "One group should be found but found " + irGroupDAO.getAllNameOrder().size();
         
         IrUserGroup itemIrGroupByName =  irGroupDAO.findByUniqueName(group.getName());
         assert itemIrGroupByName.equals(group) : "Role should be found";
         
         irGroupDAO.makeTransient(other);
         assert  irGroupDAO.getById(other.getId(), false) == null : "Should no longer be able to find group";
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
 		user = userDAO.getById(user.getId(), false);
 		assert user != null : "Should be able to find user " + user;
 		
 		IrUserGroup group = new IrUserGroup("groupName");
 		group.setDescription("groupDescription");
 		group.addUser(user);
 		irGroupDAO.makePersistent(group);
 		tm.commit(ts);
        
 		ts = tm.getTransaction(td);
 		group = irGroupDAO.getById(group.getId(), false);
 		assert group.getUsers().contains(user) : "Group should contain user " + user;
 		
 		//make sure select works.
 		user = irGroupDAO.getUserForGroup(group.getId(), user.getId());	
 		assert user != null : "Should find user";
 		
 		IrUser otherUser = irGroupDAO.getUserForGroup(group.getId(), user.getId() + 55);
 		assert otherUser == null : "Should not find other user";
 		
 		List<IrUserGroup> groups = irGroupDAO.getUserGroupsForUser(user.getId());
 		assert groups.size() == 1 : "Should contain 1 group but contains " + groups.size();
 		assert groups.contains(group) : " select should contain group " + group;
        irGroupDAO.makeTransient(group);
        userDAO.makeTransient(userDAO.getById(user.getId(), false));
        tm.commit(ts);
        
	}
}
