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

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.util.FileUtil;

/**
 * Test the persistance methods for ir file Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrFileDAOTest {
	
	/** Application context for loading beans*/
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
	
	/** Ir File relational data access.  */
	IrFileDAO irFileDAO = (IrFileDAO) ctx.getBean("irFileDAO");
	
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
	 * Test adding a file to an irFile
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void irFileAddFileDAOTest()  throws IllegalFileSystemNameException, LocationAlreadyExistsException{
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		tm.commit(ts);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "testFile", 
				"Hello  - irFile This is text in a file");

		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f, "newFile1");
		fileInfo1.setDescription("testThis");
		IrFile irFile = new IrFile(fileInfo1, "newName");
		
		irFileDAO.makePersistent(irFile);
		
		// Start the transaction 
		ts = tm.getTransaction(td);

		IrFile other = irFileDAO.getById(irFile.getId(), false);
		assert other != null : "Should be able to find the file by id " + irFile.getId();
		assert irFileDAO.findByName(irFile.getName()) != null : "Ir file should be found";
		assert irFileDAO.getCount() == 1 : "Count should be 1";
		assert other.equals(irFile) : "Files should be equal";

		// commit the transaction
		tm.commit(ts);

		// delete the ir file
		irFileDAO.makeTransient(irFileDAO.getById(irFile.getId(), false));

		//start a new transaction		
		ts = tm.getTransaction(td);
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}

}
