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

package edu.ur.hibernate.ir.researcher.db;

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
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FileServerService;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherDAO;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFileDAO;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherFolderDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.util.FileUtil;


/**
 * Test the persistence methods for researcher files
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ResearcherFileDAOTest {

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
    
    /** Researcher file relational data access  */
    ResearcherFileDAO researcherFileDAO= (ResearcherFileDAO) ctx
	.getBean("researcherFileDAO");
    
    /** Researcher folder relational data access  */
    ResearcherFolderDAO researcherFolderDAO= (ResearcherFolderDAO) ctx
	.getBean("researcherFolderDAO");
    
    /** Researcher data access */
    ResearcherDAO researcherDAO= (ResearcherDAO) ctx.getBean("researcherDAO");
    
	/** Unique name generator  */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) 
	ctx.getBean("uniqueNameGenerator");

	
	/**
	 * Test researcher file persistence
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void baseResearcherFileDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException, LocationAlreadyExistsException {

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

		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("none");
		IrFile irFile = new IrFile(info, "test file");
        
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");
		
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		// create the user and their folder.
		userDAO.makePersistent(user);
		
		Researcher researcher = new Researcher(user);
		ResearcherFile rf = researcher.createRootFile(irFile, 1);
		researcherDAO.makePersistent(researcher);
		
		
		tm.commit(ts);
		
		Long irFileId = irFile.getId();
		
		assert researcherFileDAO.getById(rf.getId(), false) != null: "Should be able to find researcher file " + rf;
	    
		Set<ResearcherFile> files = researcher.getRootFiles();
		
		assert files.size() == 1 : "Files size = " + files.size() + " but should equal 1";
		
		for(ResearcherFile mf : files )
		{
			System.out.println(" Researcher file = " + mf);
		}
		
		
		ts = tm.getTransaction(td);
		assert researcherFileDAO.getFileForResearcherWithSpecifiedIrFile(researcher.getId(), irFileId).equals(rf) : "The researcher files should be equal";
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert researcher.removeRootFile(rf) : " file should be removed from researcher " + rf;
		
		researcherDAO.makePersistent(researcher);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		user.setResearcher(null);
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		// clean up
		ts = tm.getTransaction(td);
		fileDAO.makeTransient(fileDAO.getById(irFile.getId(), false));
		researcherDAO.makeTransient(researcherDAO.getById(researcher.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	

	}
	
	/**
	 * Test - get researcher file in a folder
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void getResearcherFileInAFolderDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException, LocationAlreadyExistsException {

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
		IrFile irFile = new IrFile(info, "test file");
        
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");
		
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		// create the user and their folder.
		userDAO.makePersistent(user);
		
		Researcher researcher = new Researcher(user);
		researcherDAO.makePersistent(researcher);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		ResearcherFolder researcherFolder = researcher.createRootFolder("FolderName");
		
		ResearcherFile rfile = null;
		try
		{
		    rfile= researcherFolder.addFile(irFile);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		researcherDAO.makePersistent(researcher);
		assert rfile != null : "rfile should not be null";
		tm.commit(ts);
		
		// start new transaction
		ts = tm.getTransaction(td);
		researcher = researcherDAO.getById(researcher.getId(), false);
		assert researcherFileDAO.getFilesInAFolderForResearcher(researcher.getId(), researcherFolder.getId()).size() == 1 : "Should be equal to 1";
	    
		ResearcherFolder existingFolder = researcher.getRootFolder(researcherFolder.getName());
		assert existingFolder != null : "Should find existing folder";
		assert researcherFolder.getResearcher().equals(researcher) : " folder researcher " +researcherFolder.getResearcher() + " should equal " + researcher; 
		assert researcherFolder.hashCode() == existingFolder.hashCode() : " hash code " + researcherFolder.hashCode() + " should equal " + existingFolder.hashCode();
		
		
		System.out.println("existing hash code = " + existingFolder.hashCode());
		for(ResearcherFolder folder : researcher.getRootFolders())
		{
		    System.out.println("Hash code = " + folder.hashCode());
		    
		    assert researcher.getRootFolders().contains(folder);
		}
		assert researcher.getRootFolders().contains(existingFolder) : "root folders should contain " + existingFolder;
		assert researcher.getRootFolders().contains(researcherFolder) : "should contain " + researcherFolder;

		researcherFolder.removeResearcherFile(rfile);

		assert researcherFolder.equals(existingFolder): "researcher folder " + researcherFolder + " should equal " + existingFolder;
		

		
		
		assert researcher.getRootFolders().size() == 1 : "Should have one root folder but has " + researcher.getRootFolders().size();
		assert researcher.removeRootFolder(researcherFolder) : " Folder should be removed from user " 
			+ researcherFolder;
		
		researcherDAO.makePersistent(researcher);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		user.setResearcher(null);
		userDAO.makePersistent(user);
		tm.commit(ts);
		
        // clean up
		ts = tm.getTransaction(td);
		fileDAO.makeTransient(fileDAO.getById(irFile.getId(), false));
		researcherDAO.makeTransient(researcherDAO.getById(researcher.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	

	}

	/**
	 * Test root researcher file 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void getRootResearcherFilesDAOTest() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException {

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
		IrFile irFile = new IrFile(info, "test file");
        
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");
		
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);

		// create the user and their folder.
		userDAO.makePersistent(user);
		
		Researcher researcher = new Researcher(user);
		researcherDAO.makePersistent(researcher);
		
		ResearcherFile rfile= researcher.createRootFile(irFile, 1);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		researcher = researcherDAO.getById(researcher.getId(), false);
		assert researcher.getRootFiles().size() == 1 : "root files size = " +  researcher.getRootFiles().size();
		
		assert researcherFileDAO.getRootFiles(researcher.getId()).size() == 1 : "Should be equal to 1";
		ResearcherFile setFile = researcher.getRootFile(irFile.getName());
		assert setFile != null : "Should be able to find " + setFile;
		assert setFile.equals(rfile) : " Set file " + setFile + " Should equal " + rfile;
		
		assert researcher.getRootFiles().contains(rfile);
		
		assert researcher.removeRootFile(rfile) : " File should be removed from user " + rfile;
		
		researcherDAO.makePersistent(researcher);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		user.setResearcher(null);
		userDAO.makePersistent(user);
		tm.commit(ts);
		
        // clean up
		ts = tm.getTransaction(td);
		fileDAO.makeTransient(fileDAO.getById(irFile.getId(), false));
	    researcherDAO.makeTransient(researcherDAO.getById(researcher.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	

	}

}
