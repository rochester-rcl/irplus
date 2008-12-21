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

package edu.ur.hibernate.file.db;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileServer;
import edu.ur.file.db.FileServerDAO;
import edu.ur.file.db.DefaultFileServer;
import edu.ur.hibernate.file.test.helper.ContextHolder;
import edu.ur.hibernate.file.test.helper.PropertiesLoader;

/**
 * Test the persistance methods for File Server Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class HbFileServerDAOTest {
	
	/**  Properties file with testing specific information. */
	Properties properties;
	
	/** Get the spring context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** File server data access */
	FileServerDAO fileServerDAO = (FileServerDAO) ctx
			.getBean("fileServerDAO");

	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

    // Start the transaction this is for lazy loading
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

	
	/**
	 * Setup for testing the file System manager
	 * this loads the properties file for getting
	 * the correct path for different file systems
	 */
	@BeforeClass
	public void setUp() {
	    properties = new PropertiesLoader().getProperties();
	}
	
	
	/**
	 * Setup for testing
	 * 
	 * this deletes exiting test directories if they exist
	 */
	@BeforeMethod
	public void cleanDirectory() {
		try {
			File f = new File( properties.getProperty("HbFileServerDAOTest.test_path") );
			if(f.exists())
			{
			    FileUtils.forceDelete(f);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	
	/**
	 * Test File Database persistance
	 * 
	 */
	@Test
	public void baseFileDatabaseDAOTest() {
		
		TransactionStatus ts = tm.getTransaction(td);
		DefaultFileServer fileServer = new DefaultFileServer("serverName");
		
		FileDatabase fileDb1 = fileServer.createFileDatabase("display_db1", "db1", 
				properties.getProperty("HbFileServerDAOTest.test_path"), "Description");
		
		fileServerDAO.makePersistent(fileServer);
		
		
		//make sure object has been persisted
		FileServer other = fileServerDAO.getById(fileServer.getId(), false);
		
		assert other != null : "FileServer should be found";
		assert other.equals(fileServer) : "FileServer should be the same as is1 ";
		
		FileDatabase otherDb = other.getFileDatabase(fileDb1.getId());
		assert otherDb.equals(fileDb1) : "The file databases should be the same";
		
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		// clean up the file server
		fileServer.deleteFileServer();
		fileServerDAO.makeTransient(fileServer);
	}

}
