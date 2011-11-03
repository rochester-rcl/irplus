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
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceService;
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
			assert securityService.hasPermission(groupWorkspace, user, editPermission) > 0	: "User should have edit permission but does not";
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
	


}
