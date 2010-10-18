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


package edu.ur.ir.user.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.file.FileCollaborator;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.FileSharingException;
import edu.ur.ir.user.InviteInfo;
import edu.ur.ir.user.InviteInfoDAO;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFileDAO;
import edu.ur.ir.user.RoleService;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;
import edu.ur.util.FileUtil;
import edu.ur.util.TokenGenerator;

/**
 * Test class for service - invite user
 *  
 * @author Sharmila Ranganathan
 *
 */ 
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultInviteUserServiceTest {

	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultInviteUserServiceTest.class);

	/** User data access */
	UserService userService = (UserService) ctx
	.getBean("userService");

	/** User data access */
	UserFileSystemService userFileSystemService = (UserFileSystemService) ctx
	.getBean("userFileSystemService");

	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

	/**Service to invite users */
	InviteUserService inviteUserService = (InviteUserService) ctx
	.getBean("inviteUserService");
	
	SecurityService securityService = (SecurityService) ctx.getBean("securityService");

	/** user data access */
    IrUserDAO userDAO= (IrUserDAO) ctx
 	.getBean("irUserDAO");

    PersonalFileDAO personalFileDAO= (PersonalFileDAO) ctx
 	.getBean("personalFileDAO");

    InviteInfoDAO inviteInfoDAO= (InviteInfoDAO) ctx
 	.getBean("inviteInfoDAO");

    VersionedFileDAO versionedFileDAO= (VersionedFileDAO) ctx
		.getBean("versionedFileDAO");
    
    RoleService roleService= (RoleService) ctx
	.getBean("roleService");    

	public void InviteUserTest() throws FileSharingException, DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException {
		// determine if we should be sending emails 
		boolean sendEmail = new Boolean(properties.getProperty("send_emails")).booleanValue();

		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
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

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		PersonalFile pf = null;
		
		pf = userFileSystemService.addFileToUser(repo, f, user, 
				    "test file", "description");
		
		
        tm.commit(ts);

		//Start a transaction 
		ts = tm.getTransaction(td);
        VersionedFile vf = pf.getVersionedFile();
		// Share the file with other user
		SharedInboxFile inboxFile = inviteUserService.shareFile(user, user1, vf);
		tm.commit(ts);
		
		
		//Start a transaction - make sure the file exists
		ts = tm.getTransaction(td);
		VersionedFile otherVf = versionedFileDAO.getById(vf.getId(), false);
		IrUser u = userService.getUser(user1.getUsername());
		FileCollaborator fCollaborator = otherVf.getCollaborator(u);
		assert u.getSharedInboxFile(otherVf) != null : "Versioned file should exist should be in shared inbox";
		assert u.getSharedInboxFile(inboxFile.getId()).getVersionedFile().equals(otherVf) : "Versioned file should exist in the other user root";
		assert fCollaborator.getCollaborator().equals(u) : "Should be equal to user : u";
		
		tm.commit(ts);

		//Start a transaction 
		ts = tm.getTransaction(td);
		
		List<IrUser> userList = new ArrayList<IrUser>();
		userList.add(userService.getUser(user1.getUsername()));
		otherVf = versionedFileDAO.getById(vf.getId(), false);
		fCollaborator = otherVf.getCollaborator(u);
		
		// get email information
		String ownerEmail = fCollaborator.getVersionedFile().getOwner().getDefaultEmail().getEmail();
		String collaboratorEmail = fCollaborator.getCollaborator().getDefaultEmail().getEmail();
		String fileName = fCollaborator.getVersionedFile().getName();
		
		// UnShare the file 
		assert fCollaborator.getVersionedFile().getOwner().getUsername() != null : "Owner of the versioned file is null " + fCollaborator.getVersionedFile();
		inviteUserService.unshareFile(fCollaborator, fCollaborator.getVersionedFile().getOwner());
		
		log.debug("Send email for unshare user test = " + sendEmail);
		if( sendEmail )
		{
			log.debug("Sending unshare email to " + ownerEmail + " collaborator email = " + collaboratorEmail);
			inviteUserService.sendUnshareEmail(ownerEmail, collaboratorEmail, fileName);
		}
		

		tm.commit(ts);
		
		//Start a transaction 
		ts = tm.getTransaction(td);
		IrUser u1 = userService.getUser(user1.getUsername());
		VersionedFile otherVf1 = versionedFileDAO.getById(vf.getId(), false);		
		assert u1.getRootFile("myname") == null : " The file should be unshared from the user";
		assert otherVf1.getCollaborators().size() == 0 :"Collaborator size should be 0";
	
		tm.commit(ts);

		String strEmail = properties.getProperty("user_1_email");
		// Test InviteInfo persistence
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getUsername());
		vf = versionedFileDAO.getById(vf.getId(), false);
		InviteInfo t = new InviteInfo(user, vf);
		t.setEmail(strEmail);
		t.setToken("token");
		t.setInviteMessage("inviteMessage");
		inviteUserService.makeInviteInfoPersistent(t);
		
		tm.commit(ts);

		// Start a transaction 
		ts = tm.getTransaction(td);

		InviteInfo otherToken = inviteInfoDAO.getById(t.getId(), false);
		
		assert otherToken.getEmail().equals(strEmail) : "Email should be equal strEmail = " + strEmail + " other email = " + otherToken.getEmail();
		assert otherToken.getToken().equals("token"): "Token should be equal";
		assert otherToken.getUser().equals(user) : "User should be equal";
		assert otherToken.getFiles().contains(vf) :"Versioned file should be equal";

		tm.commit(ts);

		// Start a transaction 
		ts = tm.getTransaction(td);

		log.debug("Sending email to existing user");
		if(sendEmail)
		{
			log.debug("Sending email to existing user " + t);
		    inviteUserService.sendEmailToExistingUser(t);
		}

		tm.commit(ts);

		
		
		// Start a transaction 
		ts = tm.getTransaction(td);
		inviteInfoDAO.makeTransient(otherToken);

		tm.commit(ts);

		
		// Start a transaction 
		ts = tm.getTransaction(td);
		IrUser deleteUser = userService.getUser(user.getId(), false); 
		userService.deleteUser(deleteUser,deleteUser);
		
		IrUser deleteUser2 = userService.getUser(user1.getId(), false);
		userService.deleteUser(deleteUser2, deleteUser2);
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		assert userService.getUser(user1.getId(), false) == null : "User should be null"; 
		assert userService.getUser(user.getId(), false) == null : "User should be null";
		helper.cleanUpRepository();
		tm.commit(ts);	 
	}

    /**
     * Test scenario - User1 sends invite email to address "a@b.com" for sharing File1.doc. User2 adds new Email "a@b.com".
     * The File1.doc should be shared with User2 after adding new email "a@b.com".
     * @throws UserHasPublishedDeleteException 
     * @throws LocationAlreadyExistsException 
     */
	public void newEmailInviteUserTest() throws FileSharingException, DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException {

		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		String userEmail1 = properties.getProperty("user_1_email");
		UserEmail email = new UserEmail(userEmail1);
		IrUser user = userService.createUser("password", "username", email);
		
		
		String userEmail2 = properties.getProperty("user_2_email");
		UserEmail email1 = new UserEmail(userEmail2);
		IrUser user1 = userService.createUser("password1", "username1", email1);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		PersonalFile pf = null;
		
		pf = userFileSystemService.addFileToUser(repo, f, user, 
				    "test file", "description");
		
		
        tm.commit(ts);

        
		//Start a transaction 
		ts = tm.getTransaction(td);
		
		/* User shares the file with email address "new_email@yahoo.com".
		 * That email Id does not exist in the system. 
		 * So a token is created and email sent to the address with the link to login/register.
		 */
        VersionedFile vf = pf.getVersionedFile();
        
		Set<VersionedFile>  versionedFiles = new HashSet<VersionedFile>();
		versionedFiles.add(vf);
		
		// Create the list of permissions
		Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.add(securityService.getClassTypePermission(VersionedFile.class.getName(),InviteUserService.VIEW_PERMISSION));
		
		InviteInfo inviteInfo
			= new InviteInfo(user, versionedFiles);
	
		inviteInfo.setEmail("new_email@yahoo.com");
		inviteInfo.setInviteMessage("inviteMessage");
		
		inviteInfo.setToken(TokenGenerator.getToken());
		inviteInfo.setPermissions(permissions);
		inviteUserService.makeInviteInfoPersistent(inviteInfo);
		
		IrRole role = new IrRole();
		role.setName(IrRole.COLLABORATOR_ROLE);
		roleService.makeRolePersistent(role);
	   
		tm.commit(ts);
		
		
		//Start a transaction - make sure the file exists
		ts = tm.getTransaction(td);

		// User1 adds new email address "new_email@yahoo.com"
		UserEmail userEmail = new UserEmail("new_email@yahoo.com");
		user1.addUserEmail(userEmail, false);
		
		userService.makeUserPersistent(user1);
		
		// Share files pending for this email address
		inviteUserService.sharePendingFilesForEmail(user1.getId(), inviteInfo.getEmail());
		
		tm.commit(ts);
		
		//Start a transaction - make sure the file exists
		ts = tm.getTransaction(td);
		VersionedFile otherVf = versionedFileDAO.getById(vf.getId(), false);
		IrUser u = userService.getUser(user1.getUsername());
		assert u.getSharedInboxFile(otherVf) != null : "Versioned file should exist in shared inbox";
		
		tm.commit(ts);

		// Start a transaction 
		ts = tm.getTransaction(td);
		IrUser deleteUser = userService.getUser(user.getId(), false); 
		userService.deleteUser(deleteUser,deleteUser);
		
		IrUser deleteUser2 = userService.getUser(user1.getId(), false);
		userService.deleteUser(deleteUser2, deleteUser2);
		roleService.deleteRole(roleService.getRole(role.getId(), false));
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		assert userService.getUser(user1.getId(), false) == null : "User1 should be null"; 
		assert userService.getUser(user.getId(), false) == null : "User should be null";
		assert roleService.getRole(role.getId(), false) == null : "Role should be null";
		helper.cleanUpRepository();
		tm.commit(ts);
		
	}

}
