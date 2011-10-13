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

package edu.ur.ir.groupspace.service;

import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceEmailInvite;
import edu.ur.ir.groupspace.GroupWorkspaceInviteException;
import edu.ur.ir.groupspace.GroupWorkspaceInviteService;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.user.FileSharingException;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;

/**
 * Basic testing for inviting users into a group.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultGroupWorkspaceInviteServiceTest {

	/* Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/* User data access */
	UserService userService = (UserService) ctx.getBean("userService");

	/* service to deal with group workspace information */
	GroupWorkspaceService groupWorkspaceService = (GroupWorkspaceService) ctx.getBean("groupWorkspaceService");
	
	GroupWorkspaceInviteService groupWorkspaceInviteService = (GroupWorkspaceInviteService) ctx.getBean("groupWorkspaceInviteService");
	
	/* Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/* Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	/* Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/* Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/**
	 * Test inviting user into the workspace.
	 * 
	 * @throws FileSharingException
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws UserHasPublishedDeleteException
	 * @throws UserDeletedPublicationException
	 * @throws LocationAlreadyExistsException
	 * @throws GroupWorkspaceInviteException 
	 */
	public void inviteExistingUserToGroupTest() throws FileSharingException, DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, GroupWorkspaceInviteException {
		// determine if we should be sending emails 
		boolean sendEmail = Boolean.valueOf(properties.getProperty("send_emails")).booleanValue();

		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);

		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		String userEmail1 = properties.getProperty("user_1_email").trim();
		UserEmail email = new UserEmail(userEmail1);
		IrUser user = userService.createUser("password", "username", email);
		
		
		String userEmail2 = properties.getProperty("user_2_email").trim();
		UserEmail email1 = new UserEmail(userEmail2);
		IrUser user1 = userService.createUser("password1", "username1", email1);

		// create the group workspace
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupSpace");
		groupWorkspaceService.save(groupWorkspace);

		
		
		if( sendEmail )
		{
		    groupWorkspaceInviteService.sendEmailInvite(user, groupWorkspace, email1.getEmail(), "test email inviteExistingUserToGroupTest");
		}
		
        tm.commit(ts);

		
		// Start a transaction 
		ts = tm.getTransaction(td);
		groupWorkspaceService.delete(groupWorkspaceService.get(groupWorkspace.getId(), false), user);
		IrUser deleteUser = userService.getUser(user.getId(), false); 
		userService.deleteUser(deleteUser,deleteUser);
		
		IrUser deleteUser2 = userService.getUser(user1.getId(), false);
		userService.deleteUser(deleteUser2, deleteUser2);
		
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		assert userService.getUser(user1.getId(), false) == null : "User should be null"; 
		assert userService.getUser(user.getId(), false) == null : "User should be null";
		tm.commit(ts);	 
	}
	
	
	/**
	 * Test inviting someone who does not exist in the system.
	 * 
	 * @throws FileSharingException
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws UserHasPublishedDeleteException
	 * @throws UserDeletedPublicationException
	 * @throws LocationAlreadyExistsException
	 * @throws GroupWorkspaceInviteException 
	 */
	public void inviteNewUserToGroupTest() throws FileSharingException, DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, GroupWorkspaceInviteException {
		// determine if we should be sending emails 
		boolean sendEmail = Boolean.valueOf(properties.getProperty("send_emails")).booleanValue();

		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);

		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		String userEmail1 = properties.getProperty("user_1_email").trim();
		UserEmail email = new UserEmail(userEmail1);
		IrUser user = userService.createUser("password", "username", email);
		String userEmail2 = properties.getProperty("user_2_email").trim();

		// create the group workspace
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupSpace");
		groupWorkspaceService.save(groupWorkspace);
		
		GroupWorkspaceEmailInvite emailInvite = groupWorkspace.addInviteUser(userEmail2, null, user, "token");
		groupWorkspaceInviteService.save(emailInvite);
		
		if( sendEmail )
		{
		    groupWorkspaceInviteService.sendEmailInvite(emailInvite);
		}
		
        tm.commit(ts);
		
		// Start a transaction 
		ts = tm.getTransaction(td);
		groupWorkspaceInviteService.delete(groupWorkspaceInviteService.getEmailInviteById(emailInvite.getId(), false));

		tm.commit(ts);
		
		// Start a transaction 
		ts = tm.getTransaction(td);
		groupWorkspaceService.delete(groupWorkspaceService.get(groupWorkspace.getId(), false), user);
		IrUser deleteUser = userService.getUser(user.getId(), false); 
		userService.deleteUser(deleteUser,deleteUser);
		
		
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		assert userService.getUser(user.getId(), false) == null : "User should be null";
		tm.commit(ts);	 
	}
}
