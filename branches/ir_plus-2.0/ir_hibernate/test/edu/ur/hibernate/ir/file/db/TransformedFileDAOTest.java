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
import edu.ur.file.db.FileInfoDAO;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.file.TransformedFile;
import edu.ur.ir.file.TransformedFileDAO;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.TransformedFileTypeDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.util.FileUtil;

/**
 * Test the transformed data access class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class TransformedFileDAOTest {
	

	/** Application context for loading spring information  */
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** Transaction manager information */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
	/** transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
    /** Transformed file data access */
    TransformedFileDAO transformedFileDAO = (TransformedFileDAO) ctx
	.getBean("transformedFileDAO");
    
    /** Transformed file type data access */
    TransformedFileTypeDAO transformedFileTypeDAO = (TransformedFileTypeDAO) ctx
	.getBean("transformedFileTypeDAO");

	
	/** Ir File relational data access */
	IrFileDAO irFileDAO = (IrFileDAO) ctx.getBean("irFileDAO");
	
	FileInfoDAO fileInfoDAO = (FileInfoDAO) ctx.getBean("fileInfoDAO");
	
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
	 * Test saving a transformed file
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void basicTransformedFileDAOTest()  throws IllegalFileSystemNameException, LocationAlreadyExistsException{

		TransactionStatus ts = tm.getTransaction(td);
		
		// create a repository to store files in.
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
		
		File f2 = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a transformed file");

		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f, "newFile1");
		FileInfo fileInfo2 = repo.getFileDatabase().addFile(f2, "transformed1");

		IrFile irFile = new IrFile(fileInfo1,  "newFile1");
		
		irFileDAO.makePersistent(irFile);
		fileInfoDAO.makePersistent(fileInfo2);

 		TransformedFileType transformedFileType = new TransformedFileType("myFileType");
 		transformedFileType.setDescription("transformedFileTypeDescription");
 		transformedFileType.setSystemCode("systemCode");
 		TransformedFile transformedFile = irFile.addTransformedFile(fileInfo2, transformedFileType);
        transformedFileTypeDAO.makePersistent(transformedFileType);
        transformedFileDAO.makePersistent(transformedFile);
		
		
		// Start a new transaction 
		ts = tm.getTransaction(td);

		IrFile other = irFileDAO.getById(irFile.getId(), false);
		assert other.getTransformedFiles().size() == 1 : "Should have one transformed file but has " + other.getTransformedFiles().size();
		TransformedFile tf = other.getTransformedFileBySystemCode(transformedFileType.getSystemCode());
		assert  tf != null : "Should be able to find transformed file";
		assert tf.equals(transformedFile) : "Transformed files should be equal";
		
		FileInfo info = transformedFileDAO.getTransformedFile(other.getId(), "systemCode");
		assert info!= null : "Should find tf with values other.id = " + other.getId() + " systemCode ";

		// commit the transaction
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);
		irFileDAO.makeTransient(irFileDAO.getById(irFile.getId(), false));
		transformedFileTypeDAO.makeTransient(transformedFileTypeDAO.getById(transformedFileType.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}
}
