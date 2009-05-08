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

package edu.ur.hibernate.ir.file.db;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FileServerService;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.file.FileCollaborator;
import edu.ur.ir.file.FileCollaboratorDAO;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.UserEmail;
import edu.ur.util.FileUtil;


/**
 * Test the persistance methods for versioned file Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class VersionedFileDAOTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** versioned file relational data access  */
	VersionedFileDAO versionedIrFileDAO = (VersionedFileDAO) ctx
			.getBean("versionedFileDAO");

	/** file collaborator data access  */
	FileCollaboratorDAO fileCollaboratorDAO = (FileCollaboratorDAO) ctx
			.getBean("fileCollaboratorDAO");

	/** platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
			.getBean("transactionManager");

	/** transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
    /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx
	.getBean("irUserDAO");
    
    /** IrFile relational data access   */
    IrFileDAO fileDAO= (IrFileDAO) ctx
	.getBean("irFileDAO");

    /** Unique name generator  */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) 
	ctx.getBean("uniqueNameGenerator");
	
	/**
	 * Setup for testing
	 * 
	 * this deletes exiting test directories if they exist
	 */
	@BeforeMethod
	public void cleanDirectory() {
		try {
			File f = new File( properties.getProperty("a_repo_path") );
			if(f.exists())
			{
			    FileUtils.forceDelete(f);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Test add a file to an irFile
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void versionedFileAddSingleFileDAOTest() throws IllegalFileSystemNameException, LocationAlreadyExistsException {
		cleanDirectory();
		TransactionStatus ts = tm.getTransaction(td);
		
		// create a repository to store files in.
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		long size = f.length();
		assert size > 0 : "File size shold be greater than 0";

		// create the file in the file system.
		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f, "newFile1");
		fileInfo1.setDisplayName("displayName1");

		UserEmail userEmail = new UserEmail("email");
		
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		userDAO.makePersistent(user);

		UserEmail userEmail1 = new UserEmail("email1");
		
		IrUser user1 = new IrUser("user1", "password1");
		user1.setPasswordEncoding("encoding");
		user1.addUserEmail(userEmail1, true);
		userDAO.makePersistent(user1);

		// create the versioned file
		VersionedFile vif = new VersionedFile(user, fileInfo1, "new file test");
		versionedIrFileDAO.makePersistent(vif);
		Long irFileId = vif.getCurrentVersion().getIrFile().getId();  
		
		
		tm.commit(ts);	
		
		/* Test "Add Collaborator" and "Lock" */
		// Start the transaction
		ts = tm.getTransaction(td);
		FileCollaborator fc = vif.addCollaborator(user1);
		vif.lock(user1);
		versionedIrFileDAO.makePersistent(vif);
		
		tm.commit(ts);
		
		// Start the transaction
		ts = tm.getTransaction(td);
		VersionedFile otherVf = versionedIrFileDAO.getById(vif.getId(),false);
		
		// make sure file size is correct
		assert otherVf.getCurrentFileSizeBytes() == size : "Size " + size + " should be equal to " +
		otherVf.getCurrentFileSizeBytes();
		
		assert otherVf.getLockedBy().equals(user1) : "Should be equal to user1";
		assert otherVf.getCollaborators().iterator().next().equals(fc) :"Should be equal to collaborator fc ";
		
		
		tm.commit(ts);
		
		/*
		 * Test UnLock and Remove collaborator 
		 */
		//Start the transaction
		ts = tm.getTransaction(td);
		vif.unLock();
		vif.removeCollaborator(fc);
		versionedIrFileDAO.makePersistent(vif);
		fileCollaboratorDAO.makeTransient(fc);
		tm.commit(ts);
		
		//Start the transaction
		ts = tm.getTransaction(td);
				
		VersionedFile otherVf1 = versionedIrFileDAO.getById(vif.getId(),false);
		assert otherVf1.getCollaborators().size() == 0: "Collaborator size should be equal to 0";
		assert !(otherVf1.isLocked()) :"Lock should be false";  
		
		tm.commit(ts);
		
		/*
		 * Test versioned file and delete versioned file and user
		 */
        // Start the transaction
		ts = tm.getTransaction(td);
		VersionedFile other = versionedIrFileDAO.getById(vif.getId(), false);
		assert other != null : "Other should not be null";
		versionedIrFileDAO.makeTransient(other);
		fileDAO.makeTransient(fileDAO.getById(irFileId, false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		userDAO.makeTransient(userDAO.getById(user1.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test add a file to an irFile
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void versionedFileAddMultiFileDAOTest() throws IllegalFileSystemNameException, LocationAlreadyExistsException {
		cleanDirectory();
		TransactionStatus ts = tm.getTransaction(td);
		
		// create a repository to store files in.
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		long fileSize1 = f.length();
		assert fileSize1 > 0 : "File size shold be greater than 0";

		long totalSize = fileSize1;
		
		// create the file in the file system.
		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f, "newFile1");
		fileInfo1.setDisplayName("displayName1");

		
		// add a second file
		File f2 = testUtil.creatFile(directory, "testFile2", 
		"Hello2  - irFile This is text in second file - VersionedFileDAO test");

		// create the file in the file system.
		FileInfo fileInfo2 = repo.getFileDatabase().addFile(f2, "newFile2");
		fileInfo1.setDisplayName("displayName2");
		
		long fileSize2  = f2.length();
		assert fileSize2 > 0 : "File 2 size shold be greater than 0";
		
		totalSize += fileSize2;
		
		assert totalSize == (fileSize1 + fileSize2) : "total size should equal " + fileSize1 +
		" + " + fileSize2 + " but equals " + totalSize;
		
		UserEmail userEmail = new UserEmail("email");
		
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		userDAO.makePersistent(user);


		// create the versioned file
		VersionedFile vif = new VersionedFile(user, fileInfo1, "new file test");

		// add the second version of the file
		vif.addNewVersion(fileInfo2, user);
		versionedIrFileDAO.makePersistent(vif);
		tm.commit(ts);	
		
		
		// Start the transaction
		ts = tm.getTransaction(td);
		VersionedFile otherVf = versionedIrFileDAO.getById(vif.getId(),false);

		Long irFileId1 = vif.getVersion(1).getIrFile().getId();  
		Long irFileId2 = vif.getCurrentVersion().getIrFile().getId();

		assert irFileId1 != null : "File id should not be equal to null";
		assert irFileId1 > 0 : "File id should be greater than 0 ";

		
		assert irFileId2 != null : "File id should not be equal to null";
		assert irFileId2 > 0 : "File id should be greater than 0 ";
		
		assert !irFileId1.equals(irFileId2) : "file ids should be different but are not"; 
 		assert vif.getLargestVersion() == 2 : "Current version should be 2 but is " + vif.getLargestVersion();

		
		// make sure file size is correct
		assert otherVf.getCurrentFileSizeBytes() == fileSize2 : " current version file size " +
		" should be " + fileSize2 + " but is " + otherVf.getCurrentFileSizeBytes();
		
		assert otherVf.getTotalSizeForAllFilesBytes() == totalSize : " Total size is " +
		totalSize + " and get total size = " +  otherVf.getTotalSizeForAllFilesBytes();
		tm.commit(ts);
		
		
		/*
		 * Test versioned file and delete versioned file and user
		 */
        // Start the transaction
		ts = tm.getTransaction(td);
		VersionedFile other = versionedIrFileDAO.getById(vif.getId(), false);
		assert other != null : "Other should not be null";
		versionedIrFileDAO.makeTransient(other);
		fileDAO.makeTransient(fileDAO.getById(irFileId1, false));
		fileDAO.makeTransient(fileDAO.getById(irFileId2, false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}

	/**
	 * Test - get file system size
	 * @throws LocationAlreadyExistsException 
	 * 
     */
	@Test
	public void getFileSystemSizeDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException, LocationAlreadyExistsException {
  		
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
		
		PersonalFile pf = user.createRootFile(versionedFile);
		// create the user and their folder.
		userDAO.makePersistent(user);
		tm.commit(ts);

		
		ts = tm.getTransaction(td);
		Long irFileId = versionedFile.getCurrentVersion().getIrFile().getId();
		assert versionedIrFileDAO.getSumOfVersionedFilesSizeForUser(user.getId()).equals(versionedFile.getTotalSizeForAllFilesBytes()) : "Size should be equal";
		user.removeRootFile(pf);
		// create the user and their folder.
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		// Start the transaction 
        ts = tm.getTransaction(td);
        versionedIrFileDAO.makeTransient(versionedIrFileDAO.getById(versionedFile.getId(), false));
		fileDAO.makeTransient(fileDAO.getById(irFileId, false));
	    
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
		
	}
}
