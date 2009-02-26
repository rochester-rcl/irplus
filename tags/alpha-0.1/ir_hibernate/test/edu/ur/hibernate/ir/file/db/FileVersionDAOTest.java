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
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.file.FileVersion;
import edu.ur.ir.file.FileVersionDAO;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.util.FileUtil;



/**
 * Test the persistance methods for Version Information
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class FileVersionDAOTest {

	/** Application context for accessing needed classes  */
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** File version relational database persistance  */
	FileVersionDAO versionDAO = (FileVersionDAO) ctx
			.getBean("fileVersionDAO");
	
	
	/** Ir File relational database persistence  */
	IrFileDAO irFileDAO = (IrFileDAO) ctx
			.getBean("irFileDAO");
	
	/** Versioned file database persistence  */
	VersionedFileDAO versionedFileDAO = (VersionedFileDAO) ctx
			.getBean("versionedFileDAO");
	
    /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx
	.getBean("irUserDAO");

	
	/** Transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
			.getBean("transactionManager");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
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
	 * Test add a file to an Version
	 */
	@Test
	public void versionAddFileDAOTest() throws IllegalFileSystemNameException {


		// Start the transaction 
		TransactionDefinition td = new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		
		// save the repository commit transaction
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
		tm.commit(ts);	
		
        // Start the transaction - create a versioned file with a new version
		ts = tm.getTransaction(td);
		
		UserEmail userEmail = new UserEmail("email");
 
		IrUser user = new IrUser("aUser", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		userDAO.makePersistent(user);
		VersionedFile vif = new VersionedFile(user, fileInfo1, "new file test");
		FileVersion version = vif.getCurrentVersion();
		versionedFileDAO.makePersistent(vif);
		tm.commit(ts);
		
		Long irFileId = version.getIrFile().getId();
		
        // Start the transaction 
		ts = tm.getTransaction(td);
		FileVersion other = versionDAO.getById(version.getId(), false);
		
		assert other != null : "Other should not be null";
		assert other.getVersionNumber() == version.getVersionNumber() : " Version numbers should be equal";
		
		versionedFileDAO.makeTransient(versionedFileDAO.getById(vif.getId(), false));
		irFileDAO.makeTransient(irFileDAO.getById(irFileId, false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);

		// Start the transaction  - clean up the data
		ts = tm.getTransaction(td);
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}

}
