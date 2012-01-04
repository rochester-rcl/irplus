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

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.ir.FileSystem;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.UserHasParentFolderPermissionsException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;
import edu.ur.util.FileUtil;

/**
 * Test the service methods for the default group workspace file system services.
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultGroupWorkspaceFileSystemServiceTest {

	/* Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/* User data access */
	UserService userService = (UserService) ctx.getBean("userService");
	
	/* Group workspace file system data access */
	GroupWorkspaceFileSystemService groupWorkspaceFileSystemService = 
		(GroupWorkspaceFileSystemService) ctx.getBean("groupWorkspaceFileSystemService");
	
	/* service to deal with group workspace information */
	GroupWorkspaceService groupWorkspaceService = (GroupWorkspaceService) ctx.getBean("groupWorkspaceService");
	
	/* Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/* Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	/* Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/* Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/* versioned file data access */
    VersionedFileDAO versionedFileDAO= (VersionedFileDAO) ctx
	.getBean("versionedFileDAO");
    
	/* unique name generator for file system information */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) ctx.getBean("uniqueNameGenerator");
	
	SecurityService securityService = (SecurityService) ctx.getBean("securityService");
	
	
	/**
	 * Test creating a file
	 * 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 * @throws PermissionNotGrantedException 
	 */
	public void createFileTest() throws UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, DuplicateNameException, PermissionNotGrantedException
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);

        // Start the transaction 
		ts = tm.getTransaction(td);
		UserEmail email = new UserEmail("email");
		
		// create the group workspace
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupSpace");
		groupWorkspaceService.save(groupWorkspace);
		IrUser user = userService.createUser("password", "username", email);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		assert groupWorkspace.getId() != null : "Group workspace should be found";
		
		// give the user edit permissions on workspace
		HashSet<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.addAll(securityService.getClassTypePermissions(GroupWorkspace.class.getName()));
		assert permissions.size() > 0 : "Should have more than one permission";
		IrClassTypePermission groupEdit = securityService.getPermissionForClass(groupWorkspace, GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
		assert groupEdit != null: "Group edit should not be null";
		assert permissions.contains(groupEdit) : "permissions should contain group edit but does not";
		
		groupWorkspaceService.addUserToGroup(user, groupWorkspace, permissions, true);
		
		tm.commit(ts);
		
		// new transaction
		ts = tm.getTransaction(td);
		try
		{	
			String editPermission = GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION;
			assert securityService.hasPermission(groupWorkspace, user, editPermission)	: "User should have edit permission but does not";
			// add the file
		    groupWorkspaceFileSystemService.addFile(repo, groupWorkspace, user, f,  "test_file", "description");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		tm.commit(ts);
     
        //new transaction
		ts = tm.getTransaction(td);
		GroupWorkspace otherWorkspace = groupWorkspaceService.get(groupWorkspace.getId(), false);
		assert otherWorkspace.getRootFile("test_file") != null;
		IrUser deletingUser = userService.getUser(user.getId(), false);
		groupWorkspaceService.delete(groupWorkspaceService.get(groupWorkspace.getId(), false), deletingUser );
		userService.deleteUser( deletingUser, userService.getUser(user.getId(), false));

		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test deleting files and folders within a subfolder.
	 * 
	 * @throws DuplicateNameException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws PermissionNotGrantedException 
	 */
	public void deleteFileFolderTest() throws DuplicateNameException, IllegalFileSystemNameException, 
	UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, 
	PermissionNotGrantedException
	{
		
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);

        // Start the transaction 
		ts = tm.getTransaction(td);
		UserEmail email = new UserEmail("email");
		
		// create the group workspace
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupSpace");
		groupWorkspaceService.save(groupWorkspace);
		IrUser user = userService.createUser("password", "username", email);
		
		
		
		// give the user edit permissions on workspace
		HashSet<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.addAll(securityService.getClassTypePermissions(GroupWorkspace.class.getName()));
		assert permissions.size() > 0 : "Should have more than one permission";
		IrClassTypePermission groupEdit = securityService.getPermissionForClass(groupWorkspace, GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
		assert groupEdit != null: "Group edit should not be null";
		assert permissions.contains(groupEdit) : "permissions should contain group edit but does not";
		
		groupWorkspaceService.addUserToGroup(user, groupWorkspace, permissions, true);
		
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		assert groupWorkspace.getId() != null : "Group workspace should be found";
		GroupWorkspaceFolder myFolder = groupWorkspaceFileSystemService.addFolder(groupWorkspace, "myFolder", null, user);
		GroupWorkspaceFile gf = groupWorkspaceFileSystemService.addFile(repo, myFolder, user, "groupFile", "a group file in a folder");

		tm.commit(ts);
		
		// new transaction - create two new folders and a personal file
		ts = tm.getTransaction(td);
		
		myFolder = groupWorkspaceFileSystemService.getFolder(myFolder.getId(), false);
		assert myFolder != null : "folder should exist";
		
		user = userService.getUser(user.getId(), false);
		 
		// add sub Folder
		GroupWorkspaceFolder subFolder = groupWorkspaceFileSystemService.addFolder(myFolder, "subFolder", null, user);
		
		// add file to sub folder
		GroupWorkspaceFile gf2 = groupWorkspaceFileSystemService.addFile(repo, subFolder, user, "groupFile", "a group file in a folder");

		
		
		tm.commit(ts);
        
		
		// start new transaction
        ts = tm.getTransaction(td);
        subFolder = groupWorkspaceFileSystemService.getFolder(subFolder.getId(), false);
        gf2 = groupWorkspaceFileSystemService.getFile(gf2.getId(), false);
        assert subFolder.getFiles().contains(gf2) : "Sub folder 2 should contain " + gf2;
        
        String fullPath = gf2.getVersionedFile().getCurrentVersion().getIrFile().getFileInfo().getFullPath();
        File f1 = new File(fullPath);
        assert f1.exists() : "File " + f1.getAbsolutePath() + " should exist";
        
        // reload user for transaction
        user = userService.getUser(user.getId(), false);
        groupWorkspaceFileSystemService.delete(subFolder, user, "test");
        assert !f1.exists() : "File " + f1.getAbsolutePath() + " should NOT exist";
       
        gf = groupWorkspaceFileSystemService.getFile(gf.getId(), false);
        String fullPath2 = gf.getVersionedFile().getCurrentVersion().getIrFile().getFileInfo().getFullPath();
        File f2 = new File(fullPath2);
        assert f2.exists() : "File " + f2.getAbsolutePath() + " should exist ";
       
        groupWorkspaceFileSystemService.delete(gf, user, "test");
        assert !f2.exists() : "File " + f2.getAbsolutePath() + " should NOT exist ";
        
        tm.commit(ts);
     
        ts = tm.getTransaction(td);
     	IrUser deleteUser = userService.getUser(user.getId(), false);
        groupWorkspaceService.delete(groupWorkspaceService.get(groupWorkspace.getId(), false), deleteUser );
        tm.commit(ts);
		
		// cleanup
		ts = tm.getTransaction(td);
		deleteUser = userService.getUser(user.getId(), false);
		userService.deleteUser(deleteUser, deleteUser);
		helper.cleanUpRepository();
		tm.commit(ts);	
		
	}
	
	/**
	 * Test changing the user permissions for the folder 
	 * 
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws PermissionNotGrantedException
	 * @throws LocationAlreadyExistsException
	 * @throws UserHasPublishedDeleteException
	 * @throws UserDeletedPublicationException
	 * @throws UserHasParentFolderPermissionsException 
	 */
	public void changeUserPermissionsForFolderAllPermissionsTest() throws DuplicateNameException, 
	    IllegalFileSystemNameException, 
	    PermissionNotGrantedException, 
	    LocationAlreadyExistsException, 
	    UserHasPublishedDeleteException, 
	    UserDeletedPublicationException, UserHasParentFolderPermissionsException
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);

        // Start the transaction 
		ts = tm.getTransaction(td);
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		
		UserEmail email2 = new UserEmail("email2");
		IrUser user2 = userService.createUser("password", "username2", email2);
		
		// create the group workspace
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupSpace");
		groupWorkspaceService.save(groupWorkspace);
		
		
		// give the user edit permissions on workspace
		HashSet<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.addAll(securityService.getClassTypePermissions(GroupWorkspace.class.getName()));
		assert permissions.size() > 0 : "Should have more than one permission";
		IrClassTypePermission groupEdit = securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
		assert groupEdit != null: "Group edit should not be null";
		assert permissions.contains(groupEdit) : "permissions should contain group edit but does not";
		
		// give user1 all permissions
		groupWorkspaceService.addUserToGroup(user, groupWorkspace, permissions, true);
		
		IrClassTypePermission groupRead= securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_READ_PERMISSION);
		HashSet<IrClassTypePermission> readPermissions = new HashSet<IrClassTypePermission>();
		readPermissions.add(groupRead);
		groupWorkspaceService.addUserToGroup(user2, groupWorkspace, readPermissions, false);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		assert groupWorkspace.getId() != null : "Group workspace should be found";
		GroupWorkspaceFolder myFolder = groupWorkspaceFileSystemService.addFolder(groupWorkspace, "myFolder", null, user);
		GroupWorkspaceFile gf = groupWorkspaceFileSystemService.addFile(repo, myFolder, user, "groupFile", "a group file in a folder");

		tm.commit(ts);
		
		// new transaction - create two new folders and a personal file
		ts = tm.getTransaction(td);
		
		myFolder = groupWorkspaceFileSystemService.getFolder(myFolder.getId(), false);
		assert myFolder != null : "folder should exist";
		
		user = userService.getUser(user.getId(), false);
		 
		// add sub Folder
		GroupWorkspaceFolder subFolder = groupWorkspaceFileSystemService.addFolder(myFolder, "subFolder", null, user);
		
		// add file to sub folder
		GroupWorkspaceFile gf2 = groupWorkspaceFileSystemService.addFile(repo, subFolder, user, "groupFile", "a group file in a folder");
		tm.commit(ts);
        
		
		
		// start new transaction
        ts = tm.getTransaction(td);
        user = userService.getUser(user.getId(), false);
        user2 = userService.getUser(user2.getId(), false);
        myFolder = groupWorkspaceFileSystemService.getFolder(myFolder.getId(), false);
        subFolder = groupWorkspaceFileSystemService.getFolder(subFolder.getId(), false);
        gf2 = groupWorkspaceFileSystemService.getFile(gf2.getId(), false); 
        
        // check folder permissions
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION)  : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION)  : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION)  : 
        	"User should have folder read permissions but does not";
        
        assert !securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should NOT have edit permissions but does";
        
        assert !securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION): 
        	"User should NOT have add file permissions but does";
        
        assert securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have read permissions but does not";
        
        // check sub folder permissions
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION)  : 
        	"User should have folder read permissions but does not";
        
        assert !securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION): 
        	"User should NOT have edit permissions but does";
        
        assert !securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION)  : 
        	"User should NOT have add file permissions but does";
        
        assert securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION)  : 
        	"User should have read permissions but does not";
        
        List<GroupWorkspaceFolder> folders = groupWorkspaceFileSystemService.getAllFoldersUserHasPermissionFor(user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION);
        
        assert folders.size() == 2 : "Should have 2 folders but has " + folders.size();
        assert folders.contains(subFolder) : "Should contain subfolder " + subFolder + " but does not";
        assert folders.contains(myFolder) : "Should contain myfolder " + myFolder + " but does not ";
        
        
        // check file permissions
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.EDIT_PERMISSION)  : 
        	"User should have file edit permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.SHARE_PERMISSION)  : 
        	"User should have fils share permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.VIEW_PERMISSION) : 
        	"User should have file view permissions but does not";
        
        assert !securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.EDIT_PERMISSION) : 
        	"User should NOT have file edit permissions but does";
        
        assert !securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.SHARE_PERMISSION) : 
        	"User should NOT have file share permissions but does";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.VIEW_PERMISSION)  : 
        	"User should have file read but does not";
        
        // second file
        assert securityService.hasPermission(gf2.getVersionedFile(), user, VersionedFile.EDIT_PERMISSION)  : 
        	"User should have file edit permissions but does not";
        
        assert securityService.hasPermission(gf2.getVersionedFile(), user, VersionedFile.SHARE_PERMISSION)  : 
        	"User should have fils share permissions but does not";
        
        assert securityService.hasPermission(gf2.getVersionedFile(), user, VersionedFile.VIEW_PERMISSION) : 
        	"User should have file view permissions but does not";
        
        assert !securityService.hasPermission(gf2.getVersionedFile(), user2, VersionedFile.EDIT_PERMISSION) : 
        	"User should NOT have file edit permissions but does";
        
        assert !securityService.hasPermission(gf2.getVersionedFile(), user2, VersionedFile.SHARE_PERMISSION) : 
        	"User should NOT have file share permissions but does";
        
        assert securityService.hasPermission(gf2.getVersionedFile(), user2, VersionedFile.VIEW_PERMISSION)  : 
        	"User should have file read but does not";
        
        
        List<GroupWorkspaceFile> files = groupWorkspaceFileSystemService.getAllFilesUserHasPermissionFor(user2, VersionedFile.VIEW_PERMISSION);
        
        assert files.size() == 2 : "Should have 2 files but has " + files.size();
        assert files.contains(gf) : "Should contain group file file " + gf + " but does not";
        assert files.contains(gf2) : "Should contain group file file " + gf2 + " but does not";
        
        
        tm.commit(ts);
        
       
        // change the permissions
        ts = tm.getTransaction(td);
        user2 = userService.getUser(user2.getId(), false);
        myFolder = groupWorkspaceFileSystemService.getFolder(myFolder.getId(), false);
        HashSet<IrClassTypePermission> folderPermissions = new HashSet<IrClassTypePermission>();
		folderPermissions.addAll(securityService.getClassTypePermissions(GroupWorkspaceFolder.class.getName()));
        
        groupWorkspaceFileSystemService.changeUserPermissionsForFolder(user2, myFolder, folderPermissions, false);
        
        // check folder permissions
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION)  : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have folder read permissions but does not";
        
        assert securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION): 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION): 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION): 
        	"User should have read permissions but does not";
        
        // check sub folder permissions
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION): 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have folder read permissions but does not";
        
        assert securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION): 
        	"User should have read permissions but does not";
        
        // check file permissions
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.EDIT_PERMISSION): 
        	"User should have file edit permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.SHARE_PERMISSION): 
        	"User should have fils share permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.VIEW_PERMISSION): 
        	"User should have file view permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.EDIT_PERMISSION): 
        	"User should have file edit permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.SHARE_PERMISSION): 
        	"User should have file share permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.VIEW_PERMISSION): 
        	"User should have file read but does not";
        
        
        
        tm.commit(ts);
        
     
        ts = tm.getTransaction(td);
     	IrUser deleteUser = userService.getUser(user.getId(), false);
        groupWorkspaceService.delete(groupWorkspaceService.get(groupWorkspace.getId(), false), deleteUser );
        tm.commit(ts);
		
		// cleanup
		ts = tm.getTransaction(td);
		deleteUser = userService.getUser(user.getId(), false);
		
		user2 = userService.getUser(user2.getId(), false);
		userService.deleteUser(user2, deleteUser);
		
		userService.deleteUser(deleteUser, deleteUser);
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	
	/**
	 * Test changing the user permissions for the folder 
	 * 
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws PermissionNotGrantedException
	 * @throws LocationAlreadyExistsException
	 * @throws UserHasPublishedDeleteException
	 * @throws UserDeletedPublicationException
	 * @throws UserHasParentFolderPermissionsException 
	 */
	public void changeUserPermissionsForFolderAddFilePermissionsTest() throws DuplicateNameException, 
	    IllegalFileSystemNameException, 
	    PermissionNotGrantedException, 
	    LocationAlreadyExistsException, 
	    UserHasPublishedDeleteException, 
	    UserDeletedPublicationException, UserHasParentFolderPermissionsException
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);

        // Start the transaction 
		ts = tm.getTransaction(td);
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		
		UserEmail email2 = new UserEmail("email2");
		IrUser user2 = userService.createUser("password", "username2", email2);
		
		// create the group workspace
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupSpace");
		groupWorkspaceService.save(groupWorkspace);
		
		
		// give the user edit permissions on workspace
		HashSet<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.addAll(securityService.getClassTypePermissions(GroupWorkspace.class.getName()));
		assert permissions.size() > 0 : "Should have more than one permission";
		IrClassTypePermission groupEdit = securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
		assert groupEdit != null: "Group edit should not be null";
		assert permissions.contains(groupEdit) : "permissions should contain group edit but does not";
		
		// give user1 all permissions
		groupWorkspaceService.addUserToGroup(user, groupWorkspace, permissions, true);
		
		IrClassTypePermission groupRead= securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_READ_PERMISSION);
		HashSet<IrClassTypePermission> readPermissions = new HashSet<IrClassTypePermission>();
		readPermissions.add(groupRead);
		groupWorkspaceService.addUserToGroup(user2, groupWorkspace, readPermissions, false);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		assert groupWorkspace.getId() != null : "Group workspace should be found";
		GroupWorkspaceFolder myFolder = groupWorkspaceFileSystemService.addFolder(groupWorkspace, "myFolder", null, user);
		GroupWorkspaceFile gf = groupWorkspaceFileSystemService.addFile(repo, myFolder, user, "groupFile", "a group file in a folder");

		tm.commit(ts);
		
		// new transaction - create two new folders and a personal file
		ts = tm.getTransaction(td);
		
		myFolder = groupWorkspaceFileSystemService.getFolder(myFolder.getId(), false);
		assert myFolder != null : "folder should exist";
		
		user = userService.getUser(user.getId(), false);
		 
		// add sub Folder
		GroupWorkspaceFolder subFolder = groupWorkspaceFileSystemService.addFolder(myFolder, "subFolder", null, user);
		
		// add file to sub folder
		GroupWorkspaceFile gf2 = groupWorkspaceFileSystemService.addFile(repo, subFolder, user, "groupFile", "a group file in a folder");
		tm.commit(ts);
        
		
		
		// start new transaction
        ts = tm.getTransaction(td);
        user = userService.getUser(user.getId(), false);
        user2 = userService.getUser(user2.getId(), false);
        myFolder = groupWorkspaceFileSystemService.getFolder(myFolder.getId(), false);
        subFolder = groupWorkspaceFileSystemService.getFolder(subFolder.getId(), false);
        gf2 = groupWorkspaceFileSystemService.getFile(gf2.getId(), false); 
        
        // check folder permissions
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION): 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION): 
        	"User should have folder read permissions but does not";
        
        assert !securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should NOT have edit permissions but does";
        
        assert !securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should NOT have add file permissions but does";
        
        assert securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have read permissions but does not";
        
        // check sub folder permissions
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION): 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have folder read permissions but does not";
        
        assert !securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION): 
        	"User should NOT have edit permissions but does";
        
        assert !securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should NOT have add file permissions but does";
        
        assert securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have read permissions but does not";
        
        // check file permissions
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.EDIT_PERMISSION) : 
        	"User should have file edit permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.SHARE_PERMISSION) : 
        	"User should have fils share permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.VIEW_PERMISSION): 
        	"User should have file view permissions but does not";
        
        assert !securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.EDIT_PERMISSION) : 
        	"User should NOT have file edit permissions but does";
        
        assert !securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.SHARE_PERMISSION) : 
        	"User should NOT have file share permissions but does";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.VIEW_PERMISSION) : 
        	"User should have file read but does not";
        
        
        tm.commit(ts);
        
       
        // change the permissions
        ts = tm.getTransaction(td);
        user2 = userService.getUser(user2.getId(), false);
        myFolder = groupWorkspaceFileSystemService.getFolder(myFolder.getId(), false);
        HashSet<IrClassTypePermission> folderPermissions = new HashSet<IrClassTypePermission>();
		folderPermissions.add(securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION));
        
        groupWorkspaceFileSystemService.changeUserPermissionsForFolder(user2, myFolder, folderPermissions, false);
        
        // check folder permissions
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have folder read permissions but does not";
        
        assert !securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION): 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION): 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have read permissions but does not";
        
        // check sub folder permissions
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have folder read permissions but does not";
        
        assert !securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should have edit permissions but does not";
        
        assert !securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION): 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have read permissions but does not";
        
        // check file permissions
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.EDIT_PERMISSION) : 
        	"User should have file edit permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.SHARE_PERMISSION) : 
        	"User should have fils share permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.VIEW_PERMISSION): 
        	"User should have file view permissions but does not";
        
        assert !securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.EDIT_PERMISSION) : 
        	"User should have file edit permissions but does not";
        
        assert !securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.SHARE_PERMISSION) : 
        	"User should have file share permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.VIEW_PERMISSION) : 
        	"User should have file read but does not";
        
        
        
        tm.commit(ts);
        
     
        ts = tm.getTransaction(td);
     	IrUser deleteUser = userService.getUser(user.getId(), false);
        groupWorkspaceService.delete(groupWorkspaceService.get(groupWorkspace.getId(), false), deleteUser );
        tm.commit(ts);
		
		// cleanup
		ts = tm.getTransaction(td);
		deleteUser = userService.getUser(user.getId(), false);
		
		user2 = userService.getUser(user2.getId(), false);
		userService.deleteUser(user2, deleteUser);
		
		userService.deleteUser(deleteUser, deleteUser);
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test changing the user permissions for the folder 
	 * 
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws PermissionNotGrantedException
	 * @throws LocationAlreadyExistsException
	 * @throws UserHasPublishedDeleteException
	 * @throws UserDeletedPublicationException
	 * @throws UserHasParentFolderPermissionsException 
	 */
	public void changeUserPermissionsForFolderRemoveAllPermissionsCascadeTest() throws DuplicateNameException, 
	    IllegalFileSystemNameException, 
	    PermissionNotGrantedException, 
	    LocationAlreadyExistsException, 
	    UserHasPublishedDeleteException, 
	    UserDeletedPublicationException, UserHasParentFolderPermissionsException
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);

        // Start the transaction 
		ts = tm.getTransaction(td);
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		
		UserEmail email2 = new UserEmail("email2");
		IrUser user2 = userService.createUser("password", "username2", email2);
		
		// create the group workspace
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupSpace");
		groupWorkspaceService.save(groupWorkspace);
		
		
		// give the user edit permissions on workspace
		HashSet<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.addAll(securityService.getClassTypePermissions(GroupWorkspace.class.getName()));
		assert permissions.size() > 0 : "Should have more than one permission";
		IrClassTypePermission groupEdit = securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
		assert groupEdit != null: "Group edit should not be null";
		assert permissions.contains(groupEdit) : "permissions should contain group edit but does not";
		
		// give user1 all permissions
		groupWorkspaceService.addUserToGroup(user, groupWorkspace, permissions, true);
		
		IrClassTypePermission groupRead= securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_READ_PERMISSION);
		HashSet<IrClassTypePermission> readPermissions = new HashSet<IrClassTypePermission>();
		readPermissions.add(groupRead);
		groupWorkspaceService.addUserToGroup(user2, groupWorkspace, readPermissions, false);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		assert groupWorkspace.getId() != null : "Group workspace should be found";
		GroupWorkspaceFolder myFolder = groupWorkspaceFileSystemService.addFolder(groupWorkspace, "myFolder", null, user);
		GroupWorkspaceFile gf = groupWorkspaceFileSystemService.addFile(repo, myFolder, user, "groupFile", "a group file in a folder");

		tm.commit(ts);
		
		// new transaction - create two new folders and a personal file
		ts = tm.getTransaction(td);
		
		myFolder = groupWorkspaceFileSystemService.getFolder(myFolder.getId(), false);
		assert myFolder != null : "folder should exist";
		
		user = userService.getUser(user.getId(), false);
		 
		// add sub Folder
		GroupWorkspaceFolder subFolder = groupWorkspaceFileSystemService.addFolder(myFolder, "subFolder", null, user);
		
		// add file to sub folder
		GroupWorkspaceFile gf2 = groupWorkspaceFileSystemService.addFile(repo, subFolder, user, "groupFile", "a group file in a folder");
		tm.commit(ts);
        
		
		
		// start new transaction
        ts = tm.getTransaction(td);
        user = userService.getUser(user.getId(), false);
        user2 = userService.getUser(user2.getId(), false);
        myFolder = groupWorkspaceFileSystemService.getFolder(myFolder.getId(), false);
        subFolder = groupWorkspaceFileSystemService.getFolder(subFolder.getId(), false);
        gf2 = groupWorkspaceFileSystemService.getFile(gf2.getId(), false); 
        
        // check folder permissions
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION)  : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have folder read permissions but does not";
        
        assert !securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should NOT have edit permissions but does";
        
        assert !securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should NOT have add file permissions but does";
        
        assert securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have read permissions but does not";
        
        // check sub folder permissions
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have folder read permissions but does not";
        
        assert !securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should NOT have edit permissions but does";
        
        assert !securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should NOT have add file permissions but does";
        
        assert securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have read permissions but does not";
        
        // check file permissions
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.EDIT_PERMISSION) : 
        	"User should have file edit permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.SHARE_PERMISSION) : 
        	"User should have fils share permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.VIEW_PERMISSION) : 
        	"User should have file view permissions but does not";
        
        assert !securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.EDIT_PERMISSION) : 
        	"User should NOT have file edit permissions but does";
        
        assert !securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.SHARE_PERMISSION) : 
        	"User should NOT have file share permissions but does";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.VIEW_PERMISSION) : 
        	"User should have file read but does not";
        
        
        tm.commit(ts);
        
       
        // change the permissions
        ts = tm.getTransaction(td);
        user2 = userService.getUser(user2.getId(), false);
        myFolder = groupWorkspaceFileSystemService.getFolder(myFolder.getId(), false);
        HashSet<IrClassTypePermission> folderPermissions = new HashSet<IrClassTypePermission>();
		
        
        groupWorkspaceFileSystemService.changeUserPermissionsForFolder(user2, myFolder, folderPermissions, true);
        
        // check folder permissions
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION)  : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have folder read permissions but does not";
        
        assert !securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should have edit permissions but does not";
        
        assert !securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert !securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have read permissions but does not";
        
        // check sub folder permissions
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION)  : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have folder read permissions but does not";
        
        assert !securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should have edit permissions but does not";
        
        assert !securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION)  : 
        	"User should have add file permissions but does not";
        
        assert !securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have read permissions but does not";
        
        // check file permissions
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.EDIT_PERMISSION) : 
        	"User should have file edit permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.SHARE_PERMISSION): 
        	"User should have fils share permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.VIEW_PERMISSION) : 
        	"User should have file view permissions but does not";
        
        assert !securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.EDIT_PERMISSION) : 
        	"User should have file edit permissions but does not";
        
        assert !securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.SHARE_PERMISSION) : 
        	"User should have file share permissions but does not";
        
        assert !securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.VIEW_PERMISSION) : 
        	"User should have file read but does not";
        
        List<GroupWorkspaceFile> files = groupWorkspaceFileSystemService.getAllFilesUserHasPermissionFor(user2, VersionedFile.VIEW_PERMISSION);
        assert files.size() == 0 : "Should have no files but has " + files.size();
        
        List<GroupWorkspaceFolder> folders = groupWorkspaceFileSystemService.getAllFoldersUserHasPermissionFor(user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION);
        assert folders.size() == 0 : "Should have 0 folders but has " + folders.size();
        
        
        tm.commit(ts);
        
     
        ts = tm.getTransaction(td);
     	IrUser deleteUser = userService.getUser(user.getId(), false);
        groupWorkspaceService.delete(groupWorkspaceService.get(groupWorkspace.getId(), false), deleteUser );
        tm.commit(ts);
		
		// cleanup
		ts = tm.getTransaction(td);
		deleteUser = userService.getUser(user.getId(), false);
		
		user2 = userService.getUser(user2.getId(), false);
		userService.deleteUser(user2, deleteUser);
		
		userService.deleteUser(deleteUser, deleteUser);
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	
	/**
	 * Test changing the user permissions for the folder 
	 * 
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws PermissionNotGrantedException
	 * @throws LocationAlreadyExistsException
	 * @throws UserHasPublishedDeleteException
	 * @throws UserDeletedPublicationException
	 * @throws UserHasParentFolderPermissionsException 
	 */
	public void changeUserPermissionsForFolderRemoveAllPermissionsNoCascadeTest() throws DuplicateNameException, 
	    IllegalFileSystemNameException, 
	    PermissionNotGrantedException, 
	    LocationAlreadyExistsException, 
	    UserHasPublishedDeleteException, 
	    UserDeletedPublicationException, UserHasParentFolderPermissionsException
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);

        // Start the transaction 
		ts = tm.getTransaction(td);
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		
		UserEmail email2 = new UserEmail("email2");
		IrUser user2 = userService.createUser("password", "username2", email2);
		
		// create the group workspace
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupSpace");
		groupWorkspaceService.save(groupWorkspace);
		
		
		// give the user edit permissions on workspace
		HashSet<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.addAll(securityService.getClassTypePermissions(GroupWorkspace.class.getName()));
		assert permissions.size() > 0 : "Should have more than one permission";
		IrClassTypePermission groupEdit = securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
		assert groupEdit != null: "Group edit should not be null";
		assert permissions.contains(groupEdit) : "permissions should contain group edit but does not";
		
		// give user1 all permissions
		groupWorkspaceService.addUserToGroup(user, groupWorkspace, permissions, true);
		
		IrClassTypePermission groupRead= securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_READ_PERMISSION);
		HashSet<IrClassTypePermission> readPermissions = new HashSet<IrClassTypePermission>();
		readPermissions.add(groupRead);
		groupWorkspaceService.addUserToGroup(user2, groupWorkspace, readPermissions, false);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		assert groupWorkspace.getId() != null : "Group workspace should be found";
		GroupWorkspaceFolder myFolder = groupWorkspaceFileSystemService.addFolder(groupWorkspace, "myFolder", null, user);
		GroupWorkspaceFile gf = groupWorkspaceFileSystemService.addFile(repo, myFolder, user, "groupFile", "a group file in a folder");

		tm.commit(ts);
		
		// new transaction - create two new folders and a personal file
		ts = tm.getTransaction(td);
		
		myFolder = groupWorkspaceFileSystemService.getFolder(myFolder.getId(), false);
		assert myFolder != null : "folder should exist";
		
		user = userService.getUser(user.getId(), false);
		 
		// add sub Folder
		GroupWorkspaceFolder subFolder = groupWorkspaceFileSystemService.addFolder(myFolder, "subFolder", null, user);
		
		// add file to sub folder
		GroupWorkspaceFile gf2 = groupWorkspaceFileSystemService.addFile(repo, subFolder, user, "groupFile", "a group file in a folder");
		tm.commit(ts);
        
		
		
		// start new transaction
        ts = tm.getTransaction(td);
        user = userService.getUser(user.getId(), false);
        user2 = userService.getUser(user2.getId(), false);
        myFolder = groupWorkspaceFileSystemService.getFolder(myFolder.getId(), false);
        subFolder = groupWorkspaceFileSystemService.getFolder(subFolder.getId(), false);
        gf2 = groupWorkspaceFileSystemService.getFile(gf2.getId(), false); 
        
        // check folder permissions
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have folder read permissions but does not";
        
        assert !securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should NOT have edit permissions but does";
        
        assert !securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should NOT have add file permissions but does";
        
        assert securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have read permissions but does not";
        
        // check sub folder permissions
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION): 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have folder read permissions but does not";
        
        assert !securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should NOT have edit permissions but does";
        
        assert !securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should NOT have add file permissions but does";
        
        assert securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have read permissions but does not";
        
        // check file permissions
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.EDIT_PERMISSION) : 
        	"User should have file edit permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.SHARE_PERMISSION) : 
        	"User should have fils share permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.VIEW_PERMISSION) : 
        	"User should have file view permissions but does not";
        
        assert !securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.EDIT_PERMISSION) : 
        	"User should NOT have file edit permissions but does";
        
        assert !securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.SHARE_PERMISSION) : 
        	"User should NOT have file share permissions but does";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.VIEW_PERMISSION) : 
        	"User should have file read but does not";
        
        
        tm.commit(ts);
        
       
        // change the permissions
        ts = tm.getTransaction(td);
        user2 = userService.getUser(user2.getId(), false);
        myFolder = groupWorkspaceFileSystemService.getFolder(myFolder.getId(), false);
        HashSet<IrClassTypePermission> folderPermissions = new HashSet<IrClassTypePermission>();
		folderPermissions.addAll(securityService.getClassTypePermissions(GroupWorkspaceFolder.class.getName()));
        
        groupWorkspaceFileSystemService.changeUserPermissionsForFolder(user2, myFolder, folderPermissions, false);
        
        // check folder permissions
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION) : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(myFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have folder read permissions but does not";
        
        assert securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION): 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION)  : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(myFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION) : 
        	"User should have read permissions but does not";
        
        // check sub folder permissions
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION)  : 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(subFolder, user, GroupWorkspaceFolder.FOLDER_READ_PERMISSION)  : 
        	"User should have folder read permissions but does not";
        
        assert securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION): 
        	"User should have edit permissions but does not";
        
        assert securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION) : 
        	"User should have add file permissions but does not";
        
        assert securityService.hasPermission(subFolder, user2, GroupWorkspaceFolder.FOLDER_READ_PERMISSION)  : 
        	"User should have read permissions but does not";
        
        // check file permissions
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.EDIT_PERMISSION)  : 
        	"User should have file edit permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.SHARE_PERMISSION)  : 
        	"User should have fils share permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user, VersionedFile.VIEW_PERMISSION)  : 
        	"User should have file view permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.EDIT_PERMISSION) : 
        	"User should have file edit permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.SHARE_PERMISSION)  : 
        	"User should have file share permissions but does not";
        
        assert securityService.hasPermission(gf.getVersionedFile(), user2, VersionedFile.VIEW_PERMISSION)  : 
        	"User should have file read but does not";
        
        
        
        tm.commit(ts);
        
     
        ts = tm.getTransaction(td);
     	IrUser deleteUser = userService.getUser(user.getId(), false);
        groupWorkspaceService.delete(groupWorkspaceService.get(groupWorkspace.getId(), false), deleteUser );
        tm.commit(ts);
		
		// cleanup
		ts = tm.getTransaction(td);
		deleteUser = userService.getUser(user.getId(), false);
		
		user2 = userService.getUser(user2.getId(), false);
		userService.deleteUser(user2, deleteUser);
		
		userService.deleteUser(deleteUser, deleteUser);
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test moving files and folders to an existing folder
	 * @throws DuplicateNameException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws PermissionNotGrantedException 
	 */
	public void moveFileFolderTest() throws DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, PermissionNotGrantedException
	{
		
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		UserEmail email = new UserEmail("email");

		IrUser user = userService.createUser("password", "username", email);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		
		// create the group workspace
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupSpace");
		groupWorkspaceService.save(groupWorkspace);
		
		
		// give the user edit permissions on workspace
		HashSet<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.addAll(securityService.getClassTypePermissions(GroupWorkspace.class.getName()));
		assert permissions.size() > 0 : "Should have more than one permission";
		IrClassTypePermission groupEdit = securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
		assert groupEdit != null: "Group edit should not be null";
		assert permissions.contains(groupEdit) : "permissions should contain group edit but does not";
		
		// give user1 all permissions
		groupWorkspaceService.addUserToGroup(user, groupWorkspace, permissions, true);
		
		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		tm.commit(ts);
		
		// new transaction - create two new folders and a personal file
		ts = tm.getTransaction(td);
		groupWorkspace = groupWorkspaceService.get(groupWorkspace.getId(), false);
		GroupWorkspaceFolder myFolder = groupWorkspaceFileSystemService.addFolder(groupWorkspace, "myFolder1", null, user);
		    assert myFolder != null : "folder should be created";
		    
		GroupWorkspaceFolder destination = groupWorkspaceFileSystemService.addFolder(groupWorkspace, "myFolder2", null, user);
	
	    assert destination != null : "folder should be created";
		assert groupWorkspace.getRootFolder(destination.getName()) != null : 
			"Should be able to find folder " + destination;
		
		GroupWorkspaceFile gf = groupWorkspaceFileSystemService.addFile(repo, myFolder, user, f, "test file", "description");
		
		tm.commit(ts);
        
		
		// start new transaction
        ts = tm.getTransaction(td);
        assert gf.getId() != null : "Group workspace file should not have a null id " + gf;
        List<GroupWorkspaceFolder> foldersToMove = new LinkedList<GroupWorkspaceFolder>();
        foldersToMove.add(myFolder);
        
        // move the folder into other folder
        List<FileSystem> objectsNotMoved = groupWorkspaceFileSystemService.moveFolderSystemInformation(user, destination, foldersToMove, null);
        assert objectsNotMoved.size() == 0 : "All objects should have been moved but were not";
        
        tm.commit(ts);
     
        //new transaction - make sure the folder was moved.
		ts = tm.getTransaction(td);
		IrUser otherUser2 = userService.getUser(user.getUsername());
		groupWorkspace = groupWorkspaceService.get(groupWorkspace.getId(), false);
		GroupWorkspaceFolder theDestination = groupWorkspace.getRootFolder(destination.getName());
		GroupWorkspaceFolder newChild = theDestination.getChild(myFolder.getName());
		gf = groupWorkspaceFileSystemService.getFile(gf.getId(), false);
		assert  newChild != null : "Was not able to find " + myFolder
		+ " in children of " +
		theDestination ;
		
		assert newChild.getFile(gf.getName()) != null : "File " + gf.getName() 
		+ "was not found ";
		
		// move the file now to the folder above (parent folder)
		List<GroupWorkspaceFile> filesToMove = new LinkedList<GroupWorkspaceFile>();
        filesToMove.add(gf);
        
        //move the file to the parent folder
        objectsNotMoved = groupWorkspaceFileSystemService.moveFolderSystemInformation(otherUser2, theDestination, null, filesToMove);
        assert objectsNotMoved.size() == 0 : "All objects should have been moved but were not";
        tm.commit(ts);
		
		
		// make sure move occurred
		ts = tm.getTransaction(td);
		destination = groupWorkspaceFileSystemService.getFolder(destination.getId(), false);
		assert destination.getFiles().contains(gf) : "Destination folder " + destination + " should have file " + gf;
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		otherUser2 = userService.getUser(otherUser2.getId(), false);
		groupWorkspace = groupWorkspaceService.get(groupWorkspace.getId(), false);
		groupWorkspaceService.delete(groupWorkspace, otherUser2);
		userService.deleteUser(otherUser2, otherUser2);
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		assert userService.getUser(otherUser2.getId(), false) == null : "User should be null"; 
		helper.cleanUpRepository();
		tm.commit(ts);	
		
	}
	
	/**
	 * Test moving files and folders to the root (the User)
	 * @throws DuplicateNameException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws PermissionNotGrantedException 
	 */
	public void moveFileFolderToRootTest() throws DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, PermissionNotGrantedException
	{
		
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		UserEmail email = new UserEmail("email");

		IrUser user = userService.createUser("password", "username", email);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		
		// create the group workspace
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupSpace");
		groupWorkspaceService.save(groupWorkspace);
		
		
		// give the user edit permissions on workspace
		HashSet<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.addAll(securityService.getClassTypePermissions(GroupWorkspace.class.getName()));
		assert permissions.size() > 0 : "Should have more than one permission";
		IrClassTypePermission groupEdit = securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
		assert groupEdit != null: "Group edit should not be null";
		assert permissions.contains(groupEdit) : "permissions should contain group edit but does not";
		
		// give user1 all permissions
		groupWorkspaceService.addUserToGroup(user, groupWorkspace, permissions, true);
		
		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		tm.commit(ts);
		
		// new transaction - create two new folders
		ts = tm.getTransaction(td);
		
		user = userService.getUser(user.getId(), false);
		groupWorkspace = groupWorkspaceService.get(groupWorkspace.getId(), false);
		GroupWorkspaceFolder myFolder = groupWorkspaceFileSystemService.addFolder(groupWorkspace, "myFolder1", null, user);
		    assert myFolder != null : "folder should be created";

		GroupWorkspaceFolder subFolder = groupWorkspaceFileSystemService.addFolder(myFolder, "subFolder", null, user);
			
		assert subFolder != null : "folder should be created";
		
		groupWorkspaceFileSystemService.save(myFolder);
		
		GroupWorkspaceFile gf = groupWorkspaceFileSystemService.addFile(repo, myFolder, user, f, "test file", "description");
		
		tm.commit(ts);
        
		// start new transaction
        ts = tm.getTransaction(td);
        assert gf.getId() != null : "Group workspace file should not have a null id " + gf;
        groupWorkspace = groupWorkspaceService.get(groupWorkspace.getId(), false);
        user = userService.getUser(user.getUsername());
        List<GroupWorkspaceFolder> foldersToMove = new LinkedList<GroupWorkspaceFolder>();
        foldersToMove.add(subFolder);
        List<FileSystem> objectsNotMoved =  groupWorkspaceFileSystemService.moveFolderSystemInformation(user, groupWorkspace,
        		foldersToMove, null);
        assert objectsNotMoved.size() == 0 : "All object should have been moved but were not";
        tm.commit(ts);

        // start new transaction
        ts = tm.getTransaction(td);
        groupWorkspace = groupWorkspaceService.get(groupWorkspace.getId(), false);
		GroupWorkspaceFolder newRoot = groupWorkspace.getRootFolder(subFolder.getName());
		assert newRoot != null : "Should be able to find the folder " + subFolder;
		assert newRoot.getId().equals(subFolder.getId()) : "folder id's should be the same newRoot = " + newRoot
		+ " subFolder = " + subFolder;
        tm.commit(ts);
 
        ts = tm.getTransaction(td);
		GroupWorkspaceFolder oldParent = groupWorkspaceFileSystemService.getFolder(myFolder.getId(), false);
		assert oldParent.getChildren().size() == 0 : "Should not have any children but has " +
		oldParent.getChildren().size();
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		groupWorkspace = groupWorkspaceService.get(groupWorkspace.getId(), false);
		user = userService.getUser(user.getUsername());
		// move the file now to the folder above
		List<GroupWorkspaceFile> filesToMove = new LinkedList<GroupWorkspaceFile>();
        filesToMove.add(groupWorkspaceFileSystemService.getFile(gf.getId(),false));
        objectsNotMoved =  groupWorkspaceFileSystemService.moveFolderSystemInformation(user,groupWorkspace, 
				null, filesToMove);
        assert objectsNotMoved.size() == 0 : "All object should have been moved but were not";
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		groupWorkspace = groupWorkspaceService.get(groupWorkspace.getId(), false);
		user = userService.getUser(user.getUsername());
		// make sure the file has been moved
		gf = groupWorkspaceFileSystemService.getFile(gf.getId(), false);
		assert gf.getGroupWorkspaceFolder() == null : "GroupWorkspaceFile should no longer have a parent folder " + 
		gf.getGroupWorkspaceFolder();
		
		assert groupWorkspace.getRootFile(gf.getName()) != null : "Should be able to find the file";
		tm.commit(ts);
		
		 // Start new transaction
		ts = tm.getTransaction(td);
		IrUser deleteUser = userService.getUser(user.getId(), false);
		groupWorkspace = groupWorkspaceService.get(groupWorkspace.getId(), false);
		groupWorkspaceService.delete(groupWorkspace, deleteUser);
		userService.deleteUser(deleteUser, deleteUser);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert userService.getUser(user.getId(), false) == null : "User should be null"; 
		helper.cleanUpRepository();
		tm.commit(ts);
	}



}
