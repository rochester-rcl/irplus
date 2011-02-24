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
import java.util.LinkedList;
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
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.invite.InviteToken;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.FileSharingException;
import edu.ur.ir.user.FolderAutoShareInfo;
import edu.ur.ir.user.FolderInviteInfo;
import edu.ur.ir.user.InviteInfo;
import edu.ur.ir.user.InviteInfoDAO;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFileDAO;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.RoleService;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecord;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
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
    
    /** index processing type record service  */
	IndexProcessingTypeService indexProcessingTypeService = 
    	(IndexProcessingTypeService) ctx.getBean("indexProcessingTypeService");
	
    /** User index processing record service  */
	UserWorkspaceIndexProcessingRecordService recordProcessingService = 
    	(UserWorkspaceIndexProcessingRecordService) ctx.getBean("userWorkspaceIndexProcessingRecordService");

	/**
	 * Test sharing files.
	 * 
	 * @throws FileSharingException
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws UserHasPublishedDeleteException
	 * @throws UserDeletedPublicationException
	 * @throws LocationAlreadyExistsException
	 */
	public void inviteShareFileTest() throws FileSharingException, DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException {
		// determine if we should be sending emails 
		boolean sendEmail = Boolean.valueOf(properties.getProperty("send_emails")).booleanValue();

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
		
		InviteToken inviteToken = new InviteToken(strEmail, "token", user);
		inviteToken.setInviteMessage("invite Message to share file");
		
		Set<VersionedFile> files = new HashSet<VersionedFile>();
		files.add(vf);
		InviteInfo t = new InviteInfo(files, null, inviteToken);
		
		inviteUserService.makeInviteInfoPersistent(t);
		
		tm.commit(ts);

		// Start a transaction 
		ts = tm.getTransaction(td);

		InviteInfo otherInfo = inviteInfoDAO.getById(t.getId(), false);
		
		assert otherInfo.getInviteToken().getEmail().equals(strEmail) : "Email should be equal strEmail = " + strEmail + " other email = " + otherInfo.getInviteToken().getEmail();
		assert otherInfo.getInviteToken().getToken().equals("token"): "Token should be equal other token = " + otherInfo.getInviteToken().getToken();
		assert otherInfo.getInviteToken().getInvitingUser().equals(user) : "User should be equal";
		assert otherInfo.getFiles().contains(vf) :"Versioned file should be equal";

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
		inviteInfoDAO.makeTransient(inviteInfoDAO.getById(otherInfo.getId(), false));

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
	public void sharePendingFilesForEmailTest() throws FileSharingException, DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException {

		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);


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
		
		PersonalFile pf = userFileSystemService.addFileToUser(repo, f, user, 
				    "test file", "description");
		
		
        tm.commit(ts);

        
		//Start a transaction 
		ts = tm.getTransaction(td);
		
		/* User shares the file with email address "new_email@yahoo.com".
		 * That email Id does not exist in the system. 
		 * So a token is created and email sent to the address with the link to login/register.
		 */
		pf = userFileSystemService.getPersonalFile(pf.getId(), false);
        VersionedFile vf = pf.getVersionedFile();
        
		Set<VersionedFile>  versionedFiles = new HashSet<VersionedFile>();
		versionedFiles.add(vf);
		
		// Create the list of permissions
		Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.add(securityService.getClassTypePermission(VersionedFile.class.getName(),InviteUserService.VIEW_PERMISSION));
		
		
		String userEmail3 = properties.getProperty("user_3_email");
		InviteToken inviteToken = new InviteToken(userEmail3, TokenGenerator.getToken(), user);
		
		InviteInfo inviteInfo
			= new InviteInfo(versionedFiles, permissions, inviteToken);
		
		inviteUserService.makeInviteInfoPersistent(inviteInfo);
		
		IrRole role = new IrRole();
		role.setName(IrRole.COLLABORATOR_ROLE);
		roleService.makeRolePersistent(role);
	   
		tm.commit(ts);
		
		
		//Start a transaction - make sure the file exists
		ts = tm.getTransaction(td);

		// User1 adds new email address "new_email@yahoo.com" with
		// email set to verified.
		UserEmail userEmail = new UserEmail(userEmail3);
		userEmail.setVerifiedTrue();
		user1.addUserEmail(userEmail, false);
		
		
		userService.makeUserPersistent(user1);
		
		// Share files pending for this email address
		inviteUserService.sharePendingFilesForEmail(user1.getId(), inviteInfo.getInviteToken().getEmail());
		
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
	
	/**
	 * This will test inviting users who are part of the system and users who are not part of the system.
	 * 
	 * @throws LocationAlreadyExistsException 
	 * @throws IllegalFileSystemNameException 
	 * @throws DuplicateNameException 
	 * @throws FileSharingException 
	 * @throws UserDeletedPublicationException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws PermissionNotGrantedException 
	 */
	public void inviteUsersTest() throws LocationAlreadyExistsException, 
	    DuplicateNameException, IllegalFileSystemNameException, FileSharingException,
	    UserHasPublishedDeleteException, UserDeletedPublicationException, PermissionNotGrantedException
	{
		// determine if we should be sending emails 
		// if not do not try this test
		boolean sendEmail = Boolean.valueOf(properties.getProperty("send_emails")).booleanValue();
		if( !sendEmail )
		{
			return;
		}
		
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);


		// create the sharing user
		String userEmail1 = properties.getProperty("user_1_email");
		UserEmail email = new UserEmail(userEmail1);
		IrUser user = userService.createUser("password", "username", email);
		
		// create the non-existing user
		String userEmail2 = properties.getProperty("user_2_email");
		
		// create the existing user
		String userEmail3 = properties.getProperty("user_3_email");
		UserEmail email3 = new UserEmail(userEmail3);
		email3.setVerifiedTrue();
		IrUser user3 = userService.createUser("password3", "username3", email3);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		// add file to user
		PersonalFile pf  = userFileSystemService.addFileToUser(repo, f, user, 
				    "test file", "description");
		
		IndexProcessingType updateProcessingType = new IndexProcessingType(IndexProcessingTypeService.UPDATE);
		indexProcessingTypeService.save(updateProcessingType);
		
		IndexProcessingType deleteProcessingType = new IndexProcessingType(IndexProcessingTypeService.DELETE);
		indexProcessingTypeService.save(deleteProcessingType);
		
		IndexProcessingType insertProcessingType =  new IndexProcessingType(IndexProcessingTypeService.INSERT);
		indexProcessingTypeService.save(insertProcessingType);
        tm.commit(ts);

        
		//Start a transaction 
		ts = tm.getTransaction(td);
		
		// create permissions to give user
		// Create the list of permissions
		Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		IrClassTypePermission view = securityService.getClassTypePermission(VersionedFile.class.getName(),InviteUserService.VIEW_PERMISSION);
		permissions.add(view);

		// create the role
		IrRole role = new IrRole();
		role.setName(IrRole.COLLABORATOR_ROLE);
		roleService.makeRolePersistent(role);
		
		// create list of emails to give user
		LinkedList<String> emails = new LinkedList<String>();
		emails.add(userEmail2);
		emails.add(userEmail3);
		
		/* User shares the file with email address "new_email@yahoo.com".
		 * That email Id does not exist in the system. 
		 * So a token is created and email sent to the address with the link to login/register.
		 */
		List<PersonalFile> personalFiles = new LinkedList<PersonalFile>();
		personalFiles.add(userFileSystemService.getPersonalFile(pf.getId(), false));
		
		inviteUserService.inviteUsers(user, emails, permissions, personalFiles, "test message");
        VersionedFile vf = pf.getVersionedFile();
        
		List<UserWorkspaceIndexProcessingRecord> records = recordProcessingService.getAllOrderByIdDate();
		for( UserWorkspaceIndexProcessingRecord record : records )
		{
			recordProcessingService.delete(record);
		}
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.DELETE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
		tm.commit(ts);

		ts = tm.getTransaction(td);
		List<InviteInfo> infos = inviteUserService.getInviteInfo(userEmail2);
		assert infos.size() == 1 : "Should have one invite info";
		InviteInfo info = infos.get(0);
		assert info.getPermissions().contains(view);
		
		user3 = userService.getUser(user3.getId(), false);
		assert user3.getSharedInboxFile(vf) != null : "User should have shared inbox file";
		assert user3.getRole(IrRole.COLLABORATOR_ROLE) != null : "User should have collaborator role";
		tm.commit(ts);
		

		// Start a transaction 
		ts = tm.getTransaction(td);
		IrUser deleteUser = userService.getUser(user.getId(), false); 
		userService.deleteUser(deleteUser,deleteUser);
		
		IrUser deleteUser3 = userService.getUser(user3.getId(), false); 
		userService.deleteUser(deleteUser3,deleteUser3);
		
		roleService.deleteRole(roleService.getRole(role.getId(), false));
		
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		assert userService.getUser(user.getId(), false) == null : "User should be null";
		assert userService.getUser(user3.getId(), false) == null : "User 3 should be null";
		assert roleService.getRole(role.getId(), false) == null : "Role should be null";
		helper.cleanUpRepository();
		tm.commit(ts);
	}
	
	/**
	 * Test the auto share of folders.
	 * 
	 * @throws LocationAlreadyExistsException 
	 * @throws IllegalFileSystemNameException 
	 * @throws DuplicateNameException 
	 * @throws PermissionNotGrantedException 
	 * @throws FileSharingException 
	 * @throws UserDeletedPublicationException 
	 * @throws UserHasPublishedDeleteException 
	 */
	public void autoShareFoldersTest() throws LocationAlreadyExistsException, 
	    DuplicateNameException, 
	    IllegalFileSystemNameException, 
	    FileSharingException, 
	    PermissionNotGrantedException, 
	    UserHasPublishedDeleteException, 
	    UserDeletedPublicationException
	{
		// determine if we should be sending emails 
		// if not do not try this test
		boolean sendEmail = Boolean.valueOf(properties.getProperty("send_emails")).booleanValue();
		if( !sendEmail )
		{
			return;
		}
		
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);


		// create the sharing user
		String userEmail1 = properties.getProperty("user_1_email");
		UserEmail email = new UserEmail(userEmail1);
		IrUser user = userService.createUser("password", "username", email);
		
		// create the non-existing user
		String userEmail2 = properties.getProperty("user_2_email");
		
		// create the existing user
		String userEmail3 = properties.getProperty("user_3_email");
		UserEmail email3 = new UserEmail(userEmail3);
		email3.setVerifiedTrue();
		IrUser user3 = userService.createUser("password3", "username3", email3);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		// create a root folder and sub folder
		PersonalFolder rootFolder = user.createRootFolder("root folder");
		PersonalFolder subFolder = rootFolder.createChild("sub folder");
		
		userService.makeUserPersistent(user);
		
		
		// add file to user
		PersonalFile pf = userFileSystemService.addFileToUser(repo, f, subFolder, "test file", "description");
		
		IndexProcessingType updateProcessingType = new IndexProcessingType(IndexProcessingTypeService.UPDATE);
		indexProcessingTypeService.save(updateProcessingType);
		
		IndexProcessingType deleteProcessingType = new IndexProcessingType(IndexProcessingTypeService.DELETE);
		indexProcessingTypeService.save(deleteProcessingType);
		
		IndexProcessingType insertProcessingType =  new IndexProcessingType(IndexProcessingTypeService.INSERT);
		indexProcessingTypeService.save(insertProcessingType);
        tm.commit(ts);

        
		//Start a transaction 
		ts = tm.getTransaction(td);
		
		// create permissions to give user
		// Create the list of permissions
		Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		IrClassTypePermission view = securityService.getClassTypePermission(VersionedFile.class.getName(),InviteUserService.VIEW_PERMISSION);
		permissions.add(view);

		// create the role
		IrRole role = new IrRole();
		role.setName(IrRole.COLLABORATOR_ROLE);
		roleService.makeRolePersistent(role);
		
		// create list of emails to give user
		LinkedList<String> emails = new LinkedList<String>();
		emails.add(userEmail2);
		emails.add(userEmail3);
		
		/* User shares the file with email address "new_email@yahoo.com".
		 * That email Id does not exist in the system. 
		 * So a token is created and email sent to the address with the link to login/register.
		 */
		
		rootFolder = userFileSystemService.getPersonalFolder(rootFolder.getId(), false);
		inviteUserService.autoShareFolder(emails, rootFolder, permissions, true);
        VersionedFile vf = pf.getVersionedFile();
  
		List<UserWorkspaceIndexProcessingRecord> records = recordProcessingService.getAllOrderByIdDate();
		for( UserWorkspaceIndexProcessingRecord record : records )
		{
			recordProcessingService.delete(record);
		}
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.DELETE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
		tm.commit(ts);

		ts = tm.getTransaction(td);
		List<InviteInfo> infos = inviteUserService.getInviteInfo(userEmail2);
		assert infos.size() == 1 : "Should have one invite info";
		InviteInfo info = infos.get(0);
		assert info.getPermissions().contains(view);
		
		user3 = userService.getUser(user3.getId(), false);
		assert user3.getSharedInboxFile(vf) != null : "User should have shared inbox file";
		assert user3.getRole(IrRole.COLLABORATOR_ROLE) != null : "User should have collaborator role";
		
		rootFolder = userFileSystemService.getPersonalFolder(rootFolder.getId(), false);
		subFolder = userFileSystemService.getPersonalFolder(subFolder.getId(), false);
		
		Set<FolderAutoShareInfo> rootShares = rootFolder.getAutoShareInfos();
		assert rootShares.size() == 1 : "Should have one share but has " + rootShares.size();
		assert rootFolder.getAutoShareInfo(user3) != null : "Should have share for user " + user3;
		Set<FolderAutoShareInfo> subShares = subFolder.getAutoShareInfos();
		assert subShares.size() == 1 : "Should have one share but has " + subShares.size();
		assert subFolder.getAutoShareInfo(user3) != null : "Should have share for user " + user3;
		
		Set<FolderInviteInfo> rootInvites = rootFolder.getFolderInviteInfos();
		assert rootInvites.size() == 1 : "Should have one invite but has " + rootInvites.size();
		assert rootFolder.getFolderInviteInfo(userEmail2) != null : "Should find invite for user email 2";
		Set<FolderInviteInfo> subInvites = subFolder.getFolderInviteInfos();
		assert subInvites.size() == 1 : "Should have one invite but has " + subInvites.size();
		assert subFolder.getFolderInviteInfo(userEmail2) != null : "Should find invite for user email 2";
		tm.commit(ts);
		

		// Start a transaction 
		ts = tm.getTransaction(td);
		IrUser deleteUser = userService.getUser(user.getId(), false); 
		userService.deleteUser(deleteUser,deleteUser);
		
		IrUser deleteUser3 = userService.getUser(user3.getId(), false); 
		userService.deleteUser(deleteUser3,deleteUser3);
		
		roleService.deleteRole(roleService.getRole(role.getId(), false));
		
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		assert userService.getUser(user.getId(), false) == null : "User should be null";
		assert userService.getUser(user3.getId(), false) == null : "User 3 should be null";
		assert roleService.getRole(role.getId(), false) == null : "Role should be null";
		helper.cleanUpRepository();
		tm.commit(ts);
	}

}
