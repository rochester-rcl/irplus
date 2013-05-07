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

import java.io.File;
import java.util.LinkedList;
import java.util.List;
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
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFileDAO;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.PersonalFolderDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.util.FileUtil;


/**
 * Test the persistence methods for personal files
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class PersonalFileDAOTest {

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
    PersonalFileDAO personalFileDAO= (PersonalFileDAO) ctx
	.getBean("personalFileDAO");
    
    /** Personal folder relational data access  */
    PersonalFolderDAO personalFolderDAO= (PersonalFolderDAO) ctx
	.getBean("personalFolderDAO");
    
    /** Versioned file data access */
    VersionedFileDAO versionedFileDAO= (VersionedFileDAO) ctx
	.getBean("versionedFileDAO");
    
	/** Unique name generator  */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) 
	ctx.getBean("uniqueNameGenerator");

	
	/**
	 * Test personal file persistence
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void basePersonalFileDAOTest() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException {

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
		
		PersonalFile pf = user.createRootFile(versionedFile);
		// create the user and their folder.
		userDAO.makePersistent(user);
				
		Long irFileId = versionedFile.getCurrentVersion().getIrFile().getId();
		
		assert personalFileDAO.getById(pf.getId(), false) != null: "Should be able to find personal file " + pf;
	    
		Set<PersonalFile> files = user.getRootFiles();
		
		assert files.size() == 1 : "Files size = " + files.size() + " but should equal 1";
		
		LinkedList<Long> rootFileIds = new LinkedList<Long>();
		for(PersonalFile mf : files )
		{
			rootFileIds.add(mf.getId());
		}
		
		List<PersonalFile> rootFileList = personalFileDAO.getFiles(user.getId(), rootFileIds);
		assert rootFileList.size() == 1 : "Should have found one root file but found " +  rootFileList.size();
		
		assert personalFileDAO.getFileForUserWithSpecifiedIrFile(user.getId(), irFileId).equals(pf) : "The personal files should be equal";
		assert user.removeRootFile(pf) : " file should be removed from user " + pf;
		
		
		
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		// clean up
		ts = tm.getTransaction(td);
		personalFileDAO.makeTransient(personalFileDAO.getById(pf.getId(), false));
		versionedFileDAO.makeTransient(versionedFileDAO.getById(versionedFile.getId(), false));
		fileDAO.makeTransient(fileDAO.getById(irFileId, false));

		userDAO.makeTransient(userDAO.getById(user.getId(), false));
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
	public void getPersonalFileInAFolderDAOTest() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException {

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
		
		PersonalFolder personalFolder = user.createRootFolder("FolderName");
		
		PersonalFile pfile = null;
		try
		{
		    pfile= personalFolder.addVersionedFile(versionedFile);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert pfile != null : "pFile should not be null";
		
		// create the user and their folder.
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		user = userDAO.getById(user.getId(), false);
		Long irFileId = versionedFile.getCurrentVersion().getIrFile().getId();
		assert personalFileDAO.getFilesInFolderForUser(user.getId(), personalFolder.getId()).size() == 1 : "Should be equal to 1";
	    
		personalFolder.removePersonalFile(pfile);
		
		
		assert user.removeRootFolder(personalFolder) : " Folder should be removed from user " 
			+ personalFolder;
		
		userDAO.makePersistent(user);
		tm.commit(ts);
		
        // clean up
		ts = tm.getTransaction(td);
		personalFileDAO.makeTransient(personalFileDAO.getById(pfile.getId(), false));
		versionedFileDAO.makeTransient(versionedFileDAO.getById(versionedFile.getId(), false));
		fileDAO.makeTransient(fileDAO.getById(irFileId, false));
		
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
		
		PersonalFile pfile= user.createRootFile(versionedFile);
		
		// create the user and their folder.
		userDAO.makePersistent(user);
		
		Long irFileId = versionedFile.getCurrentVersion().getIrFile().getId();
		
		assert personalFileDAO.getRootFiles(user.getId()).size() == 1 : "Should be equal to 1";
	    
		assert user.removeRootFile(pfile) : " File should be removed from user " + pfile;
		
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		
        // clean up
		ts = tm.getTransaction(td);
		personalFileDAO.makeTransient(personalFileDAO.getById(pfile.getId(), false));
		versionedFileDAO.makeTransient(versionedFileDAO.getById(versionedFile.getId(), false));
		fileDAO.makeTransient(fileDAO.getById(irFileId, false));
	    
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	

	}

	/**
	 * Test change owner for personal file 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void changeOwnerTest() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException {

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
		info.setDisplayName("testFile");
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("none");
		
		IrUser newOwner = new IrUser("user1", "password1");
		newOwner.setPasswordEncoding("none1");
		
		VersionedFile versionedFile = new VersionedFile(user, info, "test file");
        
		UserEmail userEmail = new UserEmail("user@email");
		
		String email = properties.getProperty("user_1_email");
		UserEmail userEmail1 = new UserEmail(email);
		
		user.addUserEmail(userEmail, true);
		newOwner.addUserEmail(userEmail1, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		PersonalFile pfile= user.createRootFile(versionedFile);
		
		// create the user and their folder.
		userDAO.makePersistent(user);
		userDAO.makePersistent(newOwner);

		tm.commit(ts);

		ts = tm.getTransaction(td);

		PersonalFile otherFile = personalFileDAO.getById(pfile.getId(), false);
		Long irFileId = versionedFile.getCurrentVersion().getIrFile().getId();
		assert otherFile.equals(pfile): "Should be equal to personal file";
	    
		assert otherFile.getVersionedFile().getOwner().equals(user) : " Owner should be user " + user;

		otherFile.getVersionedFile().changeOwner(newOwner);
		
		personalFileDAO.makePersistent(otherFile);
		tm.commit(ts);
		
		
        // clean up
		ts = tm.getTransaction(td);
		assert otherFile.getVersionedFile().getOwner().equals(newOwner) :"Should be equal to new owner";
		assert otherFile.getVersionedFile().getCollaborator(user) != null :"Original owner should be collaborator";
		
		personalFileDAO.makeTransient(personalFileDAO.getById(otherFile.getId(), false));
		versionedFileDAO.makeTransient(versionedFileDAO.getById(versionedFile.getId(), false));
		fileDAO.makeTransient(fileDAO.getById(irFileId, false));
	    
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		userDAO.makeTransient(userDAO.getById(newOwner.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	

	}
	
}
