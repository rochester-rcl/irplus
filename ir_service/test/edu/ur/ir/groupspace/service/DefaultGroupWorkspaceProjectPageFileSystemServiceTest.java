/**  
   Copyright 2008 - 2012 University of Rochester

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
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FileServerService;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFile;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolder;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageService;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.item.ItemService;
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
 * Testing for the researcher file system service.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultGroupWorkspaceProjectPageFileSystemServiceTest {
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");
	
	/** User file systemdata access */
	GroupWorkspaceProjectPageService groupWorkspaceProjectPageService = 
		(GroupWorkspaceProjectPageService) ctx.getBean("groupWorkspaceProjectPageService");
	
	/** User file systemdata access */
	GroupWorkspaceProjectPageFileSystemService groupWorkspaceProjectPageFileSystemService = 
		(GroupWorkspaceProjectPageFileSystemService) ctx.getBean("groupWorkspaceProjectPageFileSystemService");
	
	/** Item services */
	ItemService itemService = (ItemService) ctx.getBean("itemService");
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	

	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** Unique name generator  */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) 
	ctx.getBean("uniqueNameGenerator");

	/* service to deal with group workspace information */
	GroupWorkspaceService groupWorkspaceService = (GroupWorkspaceService) ctx.getBean("groupWorkspaceService");

	SecurityService securityService = (SecurityService) ctx.getBean("securityService");
	
	/**
	 * Test creating a file
	 * 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 * @throws PermissionNotGrantedException 
	 */
	public void createFileFolderTest() throws IllegalFileSystemNameException, UserHasPublishedDeleteException, 
	UserDeletedPublicationException, LocationAlreadyExistsException, DuplicateNameException, PermissionNotGrantedException 
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
		
		// create the group workspace
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupSpace");
		groupWorkspaceService.save(groupWorkspace);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		FileServerService fileServerService = helper.getFileServerService();
		FileInfo info = fileServerService.addFile(repo.getFileDatabase(), f,
				uniqueNameGenerator.getNextName(), "txt");

		IrFile irFile = new IrFile(info, "testFile"); 
		

		// give the user edit permissions on workspace
		HashSet<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.addAll(securityService.getClassTypePermissions(GroupWorkspace.class.getName()));
		assert permissions.size() > 0 : "Should have more than one permission";
		IrClassTypePermission groupEdit = securityService.getPermissionForClass(groupWorkspace, GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
		assert groupEdit != null: "Group edit should not be null";
		assert permissions.contains(groupEdit) : "permissions should contain group edit but does not";
		
		groupWorkspaceService.addUserToGroup(user, groupWorkspace, permissions, true);
		
		tm.commit(ts);
		
		assert f != null : "File should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		// new transaction
		ts = tm.getTransaction(td);
		GroupWorkspaceProjectPage projectPage = groupWorkspace.getGroupWorkspaceProjectPage();
		GroupWorkspaceProjectPageFolder rootFolder = projectPage.createRootFolder("root");
		GroupWorkspaceProjectPageFolder subFolder = rootFolder.createChild("subFolder");
		GroupWorkspaceProjectPageFile rootFolderFile = rootFolder.addFile(irFile);
		GroupWorkspaceProjectPageFile subFolderFile = subFolder.addFile(irFile);
		groupWorkspaceProjectPageService.save(projectPage);
     	tm.commit(ts);
		
        //new transaction
		ts = tm.getTransaction(td);

		GroupWorkspaceProjectPage otherProjectPage2 = groupWorkspaceProjectPageService.getById(projectPage.getId(), false);
		rootFolder = otherProjectPage2.getRootFolder("root");
		assert rootFolder != null : "root folder should not be null";
		subFolder = rootFolder.getChild("subFolder");
		assert subFolder != null : "sub folder should not be null";
		
		rootFolderFile = groupWorkspaceProjectPageFileSystemService.getFile(rootFolderFile.getId(), false);
		
		assert rootFolder.getFiles().contains(rootFolderFile) : "Should contain root folder file";
		assert subFolder.getFiles().contains(subFolderFile) : "Should contain sub folder file";
		
		groupWorkspaceProjectPageFileSystemService.delete(subFolder);
		groupWorkspaceProjectPageFileSystemService.delete(rootFolderFile);
		
		IrUser deletingUser = userService.getUser(user.getId(), false);
		groupWorkspaceService.delete(groupWorkspaceService.get(groupWorkspace.getId(), false), deletingUser );
		userService.deleteUser( deletingUser, userService.getUser(user.getId(), false));
		
		tm.commit(ts);
     
	    // Start new transaction
		ts = tm.getTransaction(td);
		
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	


}
