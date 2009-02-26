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
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileDatabaseDAO;
import edu.ur.file.db.FileServer;
import edu.ur.file.db.DefaultFileServer;
import edu.ur.file.db.FileServerDAO;
import edu.ur.file.db.TreeFolderInfoDAO;
import edu.ur.file.db.TreeFolderInfo;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.hibernate.file.test.helper.ContextHolder;
import edu.ur.hibernate.file.test.helper.PropertiesLoader;

/**
 * Test the persistance methods for TreeFolderInfo Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class HbFileDatabaseDAOTest {
	
	/**
	 * Properties file with testing specific information.
	 */
	Properties properties;
	
	/** get the context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** Data access for the file server */
	FileServerDAO fileServerDAO = (FileServerDAO) ctx
	.getBean("fileServerDAO");
	
	/** file database data access */
	FileDatabaseDAO fileDatabaseDAO = (FileDatabaseDAO) ctx
			.getBean("fileDatabaseDAO");

	/** unique name generator */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) ctx.getBean("uniqueNameGenerator");

	/** folder tree data access */
	TreeFolderInfoDAO treeFolderInfoDAO = (TreeFolderInfoDAO) ctx
	.getBean("treeFolderInfoDAO");
	
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
	 * Test File Database persistance
	 * 
	 */
	@Test
	public void baseFileDatabaseDAOTest() {

		TransactionStatus ts = tm.getTransaction(td);

		assert uniqueNameGenerator.getNextName() != null : "Next unique should not be null";
		
		DefaultFileServer server = new DefaultFileServer("fileServerName");

		fileServerDAO.makePersistent(server);
		
		// create a new file database
		DefaultFileDatabase fileDatabase = server.createFileDatabase( "fDdisplayName1", "fileDatabase1",
				properties.getProperty("HbFileDatabaseDAOTest.server_path"), "Description");
		
		
		TreeFolderInfo rootFolder = fileDatabase.createRootFolder("folder1", "uniqueFolder");
		assert fileDatabase.setCurrentFileStore(rootFolder.getName()) : "Folder to store files in should be set";
		
		assert fileDatabase.getRootFolders().size() == 1 : "Should have one folder";

		// save to peristent storage
		fileDatabaseDAO.makePersistent(fileDatabase);
		tm.commit(ts);

		ts = tm.getTransaction(td);

		DefaultFileDatabase otherDatabase = (DefaultFileDatabase)fileDatabaseDAO.getById(fileDatabase.getId(), false);
		assert otherDatabase.equals(fileDatabase) : "should be able to find the database";
		assert otherDatabase.getCurrentFileStore().equals(rootFolder) : "Current file store should be " + rootFolder
		+ " but is " + otherDatabase.getCurrentFileStore();
		
		assert server.deleteDatabase(otherDatabase.getName()) : "Database should be deleted";
		
		File f = new File(fileDatabase.getFullPath());
		assert !f.exists() : "Path for database should be deleted";
		
		// delete the database from persistent storage
		fileDatabaseDAO.makeTransient(otherDatabase);
		
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);

		// Delete the database
		ts = tm.getTransaction(td);

		FileDatabase db = fileDatabaseDAO.getById(otherDatabase.getId(), false);
		assert db == null :
			"should NOT be able to find the database " + db.toString();
		
		// refresh the server from the delete
		FileServer fileServer = fileServerDAO.getById(server.getId(), false);
		assert fileServer.equals(server) : "File server should be equal";
		

		// delete the file server from the file system.
		assert server.deleteFileServer() : "File server and databases should be deleted";
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		// delete the file server from the database
		fileServerDAO.makeTransient(fileServer);
		
		// delete the full path
		assert server.deleteFileServer() : "File Server should be deleted";
		tm.commit(ts);
	}
	
	/**
	 * Test File Database selects 
	 * 
	 */
	@Test
	public void baseSelectDatabaseDAOTest() {
		

		TransactionStatus ts = tm.getTransaction(td);
		
		DefaultFileServer server1 = new DefaultFileServer("fileServerName1");
		fileServerDAO.makePersistent(server1);
		
		DefaultFileServer server2 = new DefaultFileServer("fileServerName2");
		fileServerDAO.makePersistent(server2);
		
		System.out.println("HbFileDatabaseDAOTest = " + properties.getProperty("HbFileDatabaseDAOTest.server_path"));
		// create a new file database
		DefaultFileDatabase fileDatabase1 = server1.createFileDatabase( "fDdisplayName1", "fileDatabase1",
				properties.getProperty("HbFileDatabaseDAOTest.server_path"), "Description");
		
		// create a new file database different server
		DefaultFileDatabase fileDatabase2 = server2.createFileDatabase( "fDdisplayName2", "fileDatabase2",
				properties.getProperty("HbFileDatabaseDAOTest.server_path"), "Description");

		// save to peristent storage
		fileDatabaseDAO.makePersistent(fileDatabase1);
		fileDatabaseDAO.makePersistent(fileDatabase2);
        tm.commit(ts);
		
        ts = tm.getTransaction(td);

		FileDatabase otherDatabase1 = fileDatabaseDAO.findByName(fileDatabase1.getName(), server1.getId());
		assert otherDatabase1 != null : "Other datbase should not be null";
		assert otherDatabase1.equals(fileDatabase1) : "File databases should be equal";

		FileDatabase otherDatabase1Again = fileDatabaseDAO.findByDisplayName(fileDatabase1.getDisplayName(), server1.getId());
		assert otherDatabase1Again != null : "Other datbase should not be null";
		assert otherDatabase1Again.equals(fileDatabase1) : "File databases should be equal";

		FileDatabase otherDatabase2 = fileDatabaseDAO.findByName(fileDatabase2.getName(), server2.getId());
		assert otherDatabase2 != null : "Other datbase should not be null";
		assert otherDatabase2.equals(fileDatabase2) : "File databases should be equal";

		FileDatabase otherDatabase2Again = fileDatabaseDAO.findByDisplayName(fileDatabase2.getDisplayName(), server2.getId());
		assert otherDatabase2Again != null : "Other datbase should not be null";
		assert otherDatabase2Again.equals(fileDatabase2) : "File databases should be equal";

		
		assert server1.deleteDatabase(otherDatabase1Again.getName()) : "Database should be deleted";
		assert server2.deleteDatabase(otherDatabase2Again.getName()) : "Database should be deleted";
		
		
		// delete the database from persistent storage
		fileDatabaseDAO.makeTransient(otherDatabase1Again);
		fileDatabaseDAO.makeTransient(otherDatabase2Again);
		
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		// delete the file server from the database
		fileServerDAO.makeTransient(fileServerDAO.getById(server1.getId(), false));
		fileServerDAO.makeTransient(fileServerDAO.getById(server2.getId(), false));
		tm.commit(ts);
		
		
		// delete the full path
		assert server1.deleteFileServer() : "File Server should be deleted";
		assert server2.deleteFileServer() : "File Server should be deleted";
		
	}
	
	/**
	 * Test Move folders
	 * 
	 */
	@Test
	public void testMoveSubFolderDatabaseDAOTest() {
		
		TransactionStatus ts = tm.getTransaction(td);
		
		DefaultFileServer server = new DefaultFileServer("fileServerName");

		fileServerDAO.makePersistent(server);
				
		// create a new file database
		DefaultFileDatabase fileDatabase = server.createFileDatabase( "fDdisplayName2", "fileDatabase2",
				properties.getProperty("HbFileDatabaseDAOTest.server_path"), "Description");
		
		fileDatabaseDAO.makePersistent(fileDatabase);
		
		TreeFolderInfo root1 = fileDatabase.createRootFolder("root1", "uniqueFolder1");
		TreeFolderInfo root2 = fileDatabase.createRootFolder("root2", "uniqueFolder2");
		treeFolderInfoDAO.makePersistent(root1);
		treeFolderInfoDAO.makePersistent(root2);
		
		// save to peristent storage
		fileDatabaseDAO.makePersistent(fileDatabase);
		
		TreeFolderInfo child1 = root1.createChild("child1", "child1"); 

		treeFolderInfoDAO.makePersistent(root1);
		
		root2.addChild(child1);
		
		treeFolderInfoDAO.makePersistent(root2);
		
		// delete the file server from the database
		fileServerDAO.makeTransient(server);
		server.deleteFileServer();
		tm.commit(ts);
	}

	
}
