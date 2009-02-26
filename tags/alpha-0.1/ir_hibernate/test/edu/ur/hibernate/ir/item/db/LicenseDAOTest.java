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

package edu.ur.hibernate.ir.item.db;

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

import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FileServerService;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.item.License;
import edu.ur.ir.item.LicenseDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.util.FileUtil;

/**
 * Test the persistance methods for license Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class LicenseDAOTest {

	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
			
	/** License data access */
	LicenseDAO licenseDAO = (LicenseDAO) ctx .getBean("licenseDAO");
					
	/** transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	/** transaction definition  */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** File server service for adding file */
	FileServerService fileServerService = (FileServerService)ctx.getBean("fileServerService");
	
	IrFileDAO irFileDAO = (IrFileDAO)ctx.getBean("irFileDAO");
	
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
	 * Test License persistance without file
	 */
	@Test
	public void baseLicenseDAOTest() 
	{
		/*
		TransactionStatus ts = tm.getTransaction(td);
		License license = new License("licenseName", "licenseVersion");
		licenseDAO.makePersistent(license);

		License other = licenseDAO.findById(license.getId(), false);

		assert other.equals(license) : "Licenses should be equal";
		tm.commit(ts);

        // Start the transaction 
		ts = tm.getTransaction(td);

		License other2 =  licenseDAO.getLicense("licenseName", "licenseVersion");
		assert other2.equals(other) : "License should be found";

		tm.commit(ts);

		assert licenseDAO.getCount() == 1 : "The count should be 1";
		
		licenseDAO.makeTransient(other2);
		assert licenseDAO.getById(other2.getId(), false) == null : "The license should no longer exist";
	    */
	}
	
	/**
	 * Test License persistance with file
	 */
	@Test
	public void licenseFileDAOTest()  throws IllegalFileSystemNameException
	{

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
		
		assert repo.getFileDatabase() != null : "File database should not be null "; 
		
        // Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);

		LicenseDAO licenseDAO = (LicenseDAO) ctx
		.getBean("licenseDAO");
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - This is the license in a file - LicenceDAO test");
		
		FileDatabase fileDatabase = repo.getFileDatabase();
		FileInfo fileInfo1 = fileServerService.addFile(fileDatabase, f, "newFile", "txt");
		IrFile irFile = new IrFile(fileInfo1, "test");
		irFileDAO.makePersistent(irFile);
		tm.commit(ts);

		// Start the transaction 
		ts = tm.getTransaction(td);
		assert irFile.getId() != null : "Ir file id should not be null " + irFile;
		License license = new License("licenseName2", "licenseVersion2",irFile);
		assert license.getIrFile() != null : "file info should not be null";
		licenseDAO.makePersistent(license);
		License other = licenseDAO.getById(license.getId(), false);
		assert other.equals(license) : "The licenses should be equal";
		assert other.getIrFile().equals(irFile) : "File info's should be the same";
		tm.commit(ts);
		
		//start a new transaction
		ts = tm.getTransaction(td);
		licenseDAO.makeTransient(other);
		repoHelper.cleanUpRepository();
		tm.commit(ts);	

	}

}
