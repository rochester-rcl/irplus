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

import edu.ur.file.db.FileInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
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
import edu.ur.ir.user.UserEmail;
import edu.ur.util.FileUtil;

/**
 * Test for file collaborator test
 * 
 * @author Sharmila Ranganathan
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class FileCollaboratorDAOTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** versioned file relational data access  */
	VersionedFileDAO versionedIrFileDAO = (VersionedFileDAO) ctx
			.getBean("versionedFileDAO");
	
	/** platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
			.getBean("transactionManager");

	/** transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
    /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx
	.getBean("irUserDAO");
	
    /** file collaborator access  */
    FileCollaboratorDAO collaboratorDAO= (FileCollaboratorDAO) ctx
	.getBean("fileCollaboratorDAO");
	
    /** IrFile relational data access   */
    IrFileDAO fileDAO= (IrFileDAO) ctx
	.getBean("irFileDAO");

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
	public void versionedFileAddFileDAOTest() throws IllegalFileSystemNameException, LocationAlreadyExistsException {

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

		// create the file in the file system.
		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f, "newFile1");
		fileInfo1.setDisplayName("displayName1");

		UserEmail userEmail = new UserEmail("email");
		
		UserEmail userEmail1 = new UserEmail("email1");
		
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		userDAO.makePersistent(user);

		IrUser user1 = new IrUser("user1", "password1");
		user1.setPasswordEncoding("encoding");
		user1.addUserEmail(userEmail1, true);
		userDAO.makePersistent(user1);
		
		tm.commit(ts);
		
		 // Start the transaction 
		ts = tm.getTransaction(td);

		VersionedFile vif = new VersionedFile(user, fileInfo1, "new file test");
		FileCollaborator fc = vif.addCollaborator(user1);

		versionedIrFileDAO.makePersistent(vif);
		Long irFileId = vif.getCurrentVersion().getIrFile().getId();
		
		tm.commit(ts);
		
		  // Start the transaction 
		ts = tm.getTransaction(td);
		FileCollaborator otherFc = collaboratorDAO.getById(fc.getId(), false);
		vif = otherFc.getVersionedFile();
		assert otherFc.getVersionedFile().equals(vif) : "Should be equal to versioned file vif";
		assert otherFc.getCollaborator().equals(user1) :"Should be equal to user ";
		
		assert collaboratorDAO.findCollaboratorsForVerionedFileId(vif.getId()).size() == 1 : "Should be equal to 1";
		assert collaboratorDAO.getCount() == 1 : "Should be equal to 1";
		vif.removeCollaborator(otherFc);
		versionedIrFileDAO.makePersistent(vif);
		tm.commit(ts);
		
		  // Start the transaction 
		ts = tm.getTransaction(td);
		versionedIrFileDAO.makeTransient(versionedIrFileDAO.getById(vif.getId(),false));
		fileDAO.makeTransient(fileDAO.getById(irFileId, false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		userDAO.makeTransient(userDAO.getById(user1.getId(), false));
		
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}

}
