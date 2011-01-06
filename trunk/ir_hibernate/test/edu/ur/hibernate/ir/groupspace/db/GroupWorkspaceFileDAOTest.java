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

import java.io.File;
import java.util.Properties;
import java.util.Set;

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
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileDAO;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceFolderDAO;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.util.FileUtil;

/**
 * Test for group file persistence.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class GroupWorkspaceFileDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/**  Transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
			.getBean("transactionManager");

	/** transaction definition  */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);

	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

    /** User relational data access   */
    IrUserDAO userDAO= (IrUserDAO) ctx
	.getBean("irUserDAO");
    
    /** IrFile relational data access   */
    IrFileDAO fileDAO= (IrFileDAO) ctx
	.getBean("irFileDAO");
    
    /** Personal file relational data access  */
    GroupWorkspaceFileDAO groupWorkspaceFileDAO= (GroupWorkspaceFileDAO) ctx
	.getBean("groupWorkspaceFileDAO");
    
    /** Personal folder relational data access  */
    GroupWorkspaceFolderDAO groupWorkspaceFolderDAO= (GroupWorkspaceFolderDAO) ctx
	.getBean("groupWorkspaceFolderDAO");
    
    /** Versioned file data access */
    VersionedFileDAO versionedFileDAO= (VersionedFileDAO) ctx
	.getBean("versionedFileDAO");
    
	/** Unique name generator  */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) 
	ctx.getBean("uniqueNameGenerator");
	
	/** Group space data access */
	GroupWorkspaceDAO groupWorkspaceDAO = (GroupWorkspaceDAO) ctx
	.getBean("groupWorkspaceDAO");


	
	/**
	 * Test personal file persistence
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void baseGroupFileDAOTest() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException {

		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer",
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");
		
		// save the repository
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		FileServerService fileServerService = repoHelper.getFileServerService();
		FileInfo info = fileServerService.addFile(repo.getFileDatabase(), f,
				uniqueNameGenerator.getNextName(), "txt");
		info.setDisplayName("test file");
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("none");
		
        
		UserEmail userEmail = new UserEmail("user@email");
		
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		userDAO.makePersistent(user);

		VersionedFile versionedFile = new VersionedFile(user, info, "test file");

		
		GroupWorkspace groupSpace = new GroupWorkspace("grouName", "groupDescription");
		
		GroupWorkspaceFile gf = groupSpace.createRootFile(versionedFile);
		// create the user and their folder.
		groupWorkspaceDAO.makePersistent(groupSpace);
		tm.commit(ts);
		
		// clean up
		ts = tm.getTransaction(td);
		groupSpace = groupWorkspaceDAO.getById(groupSpace.getId(), false);
		Long irFileId = versionedFile.getCurrentVersion().getIrFile().getId();
		Set<GroupWorkspaceFile> files = groupSpace.getRootFiles();
		
		assert files.size() == 1 : "Files size = " + files.size() + " but should equal 1";

		
		assert groupWorkspaceFileDAO.getById(gf.getId(), false) != null: "Should be able to find personal file " + gf;

		groupWorkspaceFileDAO.makeTransient(groupWorkspaceFileDAO.getById(gf.getId(), false));
		versionedFileDAO.makeTransient(versionedFileDAO.getById(versionedFile.getId(), false));
		fileDAO.makeTransient(fileDAO.getById(irFileId, false));

		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupSpace.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	

	}
	
	/**
	 * Test - get personal file in a folder
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void getGroupFileInAFolderDAOTest() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException {

		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer",
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		// save the repository
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		FileServerService fileServerService = repoHelper.getFileServerService();
		FileInfo info = fileServerService.addFile(repo.getFileDatabase(), f,
				uniqueNameGenerator.getNextName(), "txt");
		info.setDisplayName("test file");
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("none");
		VersionedFile versionedFile = new VersionedFile(user, info, "test file");
        
		UserEmail userEmail = new UserEmail("user@email");
		
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
	
		// make the user persistent
		userDAO.makePersistent(user);

		
		GroupWorkspace groupSpace = new GroupWorkspace("grouName", "groupDescription");
		GroupWorkspaceFolder groupFolder = groupSpace.createRootFolder(user, "FolderName");

		GroupWorkspaceFile gfile = null;
		gfile= groupFolder.addVersionedFile(versionedFile);
		
		assert gfile != null : "pFile should not be null";

		
		groupWorkspaceDAO.makePersistent(groupSpace);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		user = userDAO.getById(user.getId(), false);
		Long irFileId = versionedFile.getCurrentVersion().getIrFile().getId();
	    
		groupFolder.removeGroupFile(gfile);
		
		
		assert groupSpace.removeRootFolder(groupFolder) : " Folder should be removed from user " 
			+ groupFolder;
		
		groupWorkspaceDAO.makePersistent(groupSpace);
		tm.commit(ts);
		
        // clean up
		ts = tm.getTransaction(td);
		groupWorkspaceFileDAO.makeTransient(groupWorkspaceFileDAO.getById(gfile.getId(), false));
		versionedFileDAO.makeTransient(versionedFileDAO.getById(versionedFile.getId(), false));
		fileDAO.makeTransient(fileDAO.getById(irFileId, false));
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupSpace.getId(), false));
		
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	

	}

	/**
	 * Test root personal file 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void getRootPersonalFilesDAOTest() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException {

		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer",
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		// save the repository
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		FileServerService fileServerService = repoHelper.getFileServerService();
		FileInfo info = fileServerService.addFile(repo.getFileDatabase(), f,
				uniqueNameGenerator.getNextName(), "txt");
		info.setDisplayName("test file");
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("none");
		VersionedFile versionedFile = new VersionedFile(user, info, "test file");
        
		UserEmail userEmail = new UserEmail("user@email");
		
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);

		
		// make the user persistent
		userDAO.makePersistent(user);
		

		GroupWorkspace groupSpace = new GroupWorkspace("grouName", "groupDescription");
		GroupWorkspaceFile gfile= groupSpace.createRootFile(versionedFile);

		groupWorkspaceDAO.makePersistent(groupSpace);
		
		
		Long irFileId = versionedFile.getCurrentVersion().getIrFile().getId();
		
	    
		assert groupSpace.removeRootFile(gfile) : " File should be removed from group " + gfile;
		
		groupWorkspaceDAO.makePersistent(groupSpace);
		tm.commit(ts);
		
		
        // clean up
		ts = tm.getTransaction(td);
		groupWorkspaceFileDAO.makeTransient(groupWorkspaceFileDAO.getById(gfile.getId(), false));
		versionedFileDAO.makeTransient(versionedFileDAO.getById(versionedFile.getId(), false));
		fileDAO.makeTransient(fileDAO.getById(irFileId, false));
	    
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupSpace.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	

	}



}
