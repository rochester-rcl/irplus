
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

package edu.ur.file.db.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
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

import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.file.db.DefaultFileInfo;
import edu.ur.file.db.DefaultFileServer;
import edu.ur.file.db.FolderInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.TreeFolderInfo;
import edu.ur.file.db.service.test.helper.ContextHolder;
import edu.ur.file.db.service.test.helper.PropertiesLoader;
import edu.ur.util.FileUtil;

/**
 * Test the service methods for the default file server service.
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultFileServerServiceTest {
	
	/**  Properties file with testing specific information. */
	Properties properties;
	
	/** get the spring context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** Default file server service data access */
	DefaultFileServerService dfss = (DefaultFileServerService) ctx
			.getBean("fileServerService");

	/** transaction manager  */
	PlatformTransactionManager tm = 
		(PlatformTransactionManager)ctx.getBean("transactionManager");


	/** Default file server service data access */
	MaxFilesStoreStrategy maxFileStoreStrategy = (MaxFilesStoreStrategy) ctx
			.getBean("maxFilesStoreStrategy");

	
	/**
	 * Setup for testing the file System manager
	 * this loads the properties file for getting
	 * the correct path for different file systems
	 */
	@BeforeClass
	public void setUp() {
		properties = new PropertiesLoader().getProperties();
		System.out.println("property 2= " +  properties.getProperty("defaultFileServerService.server_path") );
		System.out.println("property 1= " +  properties.getProperty("fileServerService.server_path") );

	}
	
	/**
	 * Setup for testing
	 * 
	 * this deletes exiting test directories if they exist
	 */
	@BeforeMethod 
	public void cleanDirectory() {
		System.out.println("TRYING to CLEAN");
		try {
			System.out.println("property = " +  properties.getProperty("fileServerService.server_path") );
			File f = new File( properties.getProperty("fileServerService.server_path") );
			if(f.exists())
			{
			    FileUtils.forceDelete(f);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * Test File file server persistence with server classes
	 * 
	 */
	@Test
	public void createFileServerTest() {

		DefaultFileServer fs = dfss.createFileServer("service_file_server");
		assert fs.getId() != null : "File server should have a database id";
		assert dfss.getFileServer(fs.getId(), false).equals(fs);
		
		assert dfss.getFileServer(fs.getId(), false).equals(fs) : "file servers should be equal";
		
		dfss.deleteFileServer(fs.getName());
		assert dfss.getFileServer(fs.getName()) == null : "Should not find the file server";
	}	
	
	/**
	 * Test File Database persistence
	 * 
	 * - Make sure a file database can be created.
	 * - Makes sure a file database can be found.
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void createFileDatabaseTest() throws LocationAlreadyExistsException {

        // Start the transaction this is for lazy loading
		TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus ts = tm.getTransaction(td);
		
		DefaultFileServer fs = dfss.createFileServer("service_file_server");
		
		DefaultFileDatabase fileDatabase = fs.createFileDatabase("nates file server", "file_server_1", 
				properties.getProperty("defaultFileServerService.server_path"), 
				"description");
		
		dfss.saveFileServer(fs);
		
		//commit the transaction this will assing an id to the 
		//file database
		tm.commit(ts);
		
		//begin a new transaction
		td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = tm.getTransaction(td);
		assert dfss.getDatabaseById(fileDatabase.getId(), false).equals(fileDatabase) : 
			"the databases should be equal";
		
		assert dfss.getDatabaseByName(fs.getId(), fileDatabase.getName()) != null 
		: "should be able to find the file database";
		
	    tm.commit(ts);		
		
		dfss.deleteFileServer(fs.getName());
		assert dfss.getFileServer(fs.getName()) == null : "Should not find the file server";
		
	}
	
	/**
	 * Test File Folder persistence
	 * 
	 * - Make sure a file folder can be created.
	 * - Makes sure a file folder can be found.
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void createFileFolderTest() throws LocationAlreadyExistsException {
        // Start the transaction this is for lazy loading
		TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus ts = tm.getTransaction(td);
		
		DefaultFileServer fs = dfss.createFileServer("service_file_server");
		
		DefaultFileDatabase fileDatabase = fs.createFileDatabase("nates file server", "file_server_1", 
				properties.getProperty("defaultFileServerService.server_path"), 
				"description");
		
		TreeFolderInfo treeFolderInfo = fileDatabase.createRootFolder("Nates Folder", "folder 1");
		
		
		dfss.saveFileServer(fs);
		
		//commit the transaction this will assing an id to the 
		//file database and folder information
		tm.commit(ts);
		
		//begin a new transaction
		td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = tm.getTransaction(td);
		
		assert dfss.getTreeFolderInfoById(treeFolderInfo.getId(), true).equals(treeFolderInfo) :
			"The folder information should be the same";
		
	    tm.commit(ts);		
		
		dfss.deleteFileServer(fs.getName());
		assert dfss.getFileServer(fs.getName()) == null : "Should not find the file server";
		
	}
	
	/**
	 * Test File persistence
	 * 
	 * - Make sure a file can be created.
	 * - Makes sure a file can be found.
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void createFileTest() throws LocationAlreadyExistsException {
        // Start the transaction this is for lazy loading
		TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus ts = tm.getTransaction(td);
		
		DefaultFileServer fs = dfss.createFileServer("service_file_server");
		
		DefaultFileDatabase fileDatabase = fs.createFileDatabase("nates file server", "file_server_1", 
				properties.getProperty("defaultFileServerService.server_path"), 
				"description");
		
		TreeFolderInfo treeFolderInfo = fileDatabase.createRootFolder("Nates Folder", "folder_1");
		assert fileDatabase.setCurrentFileStore(treeFolderInfo.getName()) : "Should be able to set current file store";
		
		dfss.saveFileServer(fs);

		//commit the transaction this will assing an id to the 
		//file database and folder information
		tm.commit(ts);
	
		//begin a new transaction
		td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = tm.getTransaction(td);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "basicFile1","Hello  -  This file is for equals 1");
		
	    DefaultFileInfo info = dfss.addFile(fileDatabase, f, "file_service_file", "txt");
	    
        // commit the transaction
	    tm.commit(ts);
		
		//begin a new transaction
		td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = tm.getTransaction(td);
		
		DefaultFileInfo myInfo = (DefaultFileInfo)dfss.getFileById(info.getId(), false);
		
		assert myInfo.equals(info) :
			"The file information should be the same";
		
		assert myInfo.getFileInfoChecksums().size() >= 1 : "The file checksum should not be calculated size = " + myInfo.getFileInfoChecksums().size();
		
		assert dfss.findFile(info.getName()).equals(info) :
			"The file information should be the same";
		
		File myFile = new File(info.getFullPath());
		assert myFile.exists() : "File " + myFile.getAbsolutePath() + " should exist ";
		assert dfss.deleteFile(dfss.getFileById(info.getId(), false)) : "File " + info + " should be deleted";
		
		assert !myFile.exists() : "File " + myFile.getAbsolutePath() + " Should no longer exist";
		
	    tm.commit(ts);		
		
		dfss.deleteFileServer(fs.getName());
		assert dfss.getFileServer(fs.getName()) == null : "Should not find the file server";
	}
	
	/**
	 * Test Empty File persistence
	 * 
	 * - Make sure an empty file can be created.
	 * - Makes sure an empty file can be found.
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void createEmptyFileTest() throws LocationAlreadyExistsException {
		
        // Start the transaction this is for lazy loading
		TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus ts = tm.getTransaction(td);
		
		DefaultFileServer fs = dfss.createFileServer("service_file_server");
		
		DefaultFileDatabase fileDatabase = fs.createFileDatabase("nates file server", "file_server_1", 
				properties.getProperty("defaultFileServerService.server_path"), 
				"description");
		
		TreeFolderInfo treeFolderInfo = fileDatabase.createRootFolder("Nates Folder", "folder_1");
		assert fileDatabase.setCurrentFileStore(treeFolderInfo.getName()) : "Should be able to set current file store";
		
		dfss.saveFileServer(fs);

		//commit the transaction this will assing an id to the 
		//file database and folder information
		tm.commit(ts);
	
		//begin a new transaction
		td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = tm.getTransaction(td);
		
	    
	    DefaultFileInfo info =  dfss.createEmptyFile(fileDatabase,  "empty_file_service_file", "txt");
        // commit the transaction
	    tm.commit(ts);
		
		//begin a new transaction
		td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = tm.getTransaction(td);
		
		assert dfss.getFileById(info.getId(), false).equals(info) :
			"The file information should be the same";
		
		assert dfss.findFile(info.getName()).equals(info) :
			"The file information should be the same";
		
		File myFile = new File(info.getFullPath());
		assert myFile.exists() : "File " + myFile.getAbsolutePath() + " should exist ";
		assert dfss.deleteFile(dfss.getFileById(info.getId(), false)) : "File " + info + " should be deleted";
		
		assert !myFile.exists() : "File " + myFile.getAbsolutePath() + " Should no longer exist";
		
	    tm.commit(ts);		
		
		dfss.deleteFileServer(fs.getName());
		assert dfss.getFileServer(fs.getName()) == null : "Should not find the file server";
	}
	
	/**
	 * Test creating a folder.
	 * 
	 * - Make sure a folder location can be created.
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void createFolderTest() throws LocationAlreadyExistsException {
        // Start the transaction this is for lazy loading
		TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus ts = tm.getTransaction(td);
		
		DefaultFileServer fs = dfss.createFileServer("service_file_server");
		
		DefaultFileDatabase fileDatabase = fs.createFileDatabase("nates file server", "file_server_1", 
				properties.getProperty("defaultFileServerService.server_path"), 
				"description");
		
		TreeFolderInfo treeFolderInfo = fileDatabase.createRootFolder("Nates Folder", "folder_1");
		assert fileDatabase.setCurrentFileStore(treeFolderInfo.getName()) : "Should be able to set current file store";
		
		dfss.saveFileServer(fs);
		//commit the transaction this will assigning an id to the 
		//file database and folder information
		tm.commit(ts);
		
		
		//begin a new transaction
		td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = tm.getTransaction(td);
		
		FolderInfo info = dfss.createFolder(fileDatabase, "a_folder_name");
		Long id = info.getId();
		
		URI uri = info.getUri();
		
		File f = new File(uri.getPath());
		
		assert f.exists() : "File " + f.getAbsolutePath() + " does not exist!";
		tm.commit(ts);
		
		
		//make sure the folder exists.
		//begin a new transaction
		td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = tm.getTransaction(td);
		
		info = dfss.getFolder(id, false);
		
		assert info != null : "folder info fo id " + id + " could not be found";
		
		dfss.deleteFolder(info);
		tm.commit(ts);
		
		//make sure the folder has been deleted.
		//begin a new transaction
		td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = tm.getTransaction(td);
		info = dfss.getFolder(id, false);
		assert info == null : "folder info fo id " + id + " was found and SHOULD NOT be";
		tm.commit(ts);
		
		dfss.deleteFileServer(fs.getName());
		
		
	}
	
	/**
	 * Test MaxFilesFileStorage Strategy 
	 * 
	 * - Make sure a new root folder is created when the max number of files
	 * is reached.
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void maxFilesStoreStrategy() throws LocationAlreadyExistsException {
        // Start the transaction this is for lazy loading
		TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus ts = tm.getTransaction(td);
		
		DefaultFileServer fs = dfss.createFileServer("service_file_server");
		
		// set the store strategy to 1 and set it in the service
		maxFileStoreStrategy.setMaxNumberOfFilesPerFolder(1l);
		dfss.setDefaultDatabaseFileStoreStrategy(maxFileStoreStrategy);
		
		DefaultFileDatabase fileDatabase = fs.createFileDatabase("nates file server", "file_server_1", 
				properties.getProperty("defaultFileServerService.server_path"), 
				"description");
		
		TreeFolderInfo treeFolderInfo = fileDatabase.createRootFolder("Nates Folder", "folder_1");
		assert fileDatabase.setCurrentFileStore(treeFolderInfo.getName()) : "Should be able to set current file store";
		
		dfss.saveFileServer(fs);

		//commit the transaction this will assing an id to the 
		//file database and folder information
		tm.commit(ts);
	
		//begin a new transaction
		td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = tm.getTransaction(td);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f1 = testUtil.creatFile(directory, "basicFile1","Hello  -  This file is for equals 1");
		File f2 = testUtil.creatFile(directory, "basicFile2","Hello  -  This file is for equals 2");
		
		fileDatabase = dfss.getDatabaseById(fileDatabase.getId(), false);
		assert fileDatabase.getFullPath() != null : "Path for file database is null " + fileDatabase.getFullPath();
		TreeFolderInfo currentDefaultFolder1 = fileDatabase.getCurrentFileStore();
		assert currentDefaultFolder1.getFileDatabase() != null : "file databas is null for " + 
		  currentDefaultFolder1.getFileDatabase();
		assert currentDefaultFolder1.getFullPath() != null : "should be able to get path but couldn't";
		
		DefaultFileInfo info1 = dfss.addFile(fileDatabase, f1, "file_service_file", "txt");
		TreeFolderInfo currentDefaultFolder2 = dfss.getDatabaseById(fileDatabase.getId(), false).getCurrentFileStore();
		DefaultFileInfo info2 = dfss.addFile(fileDatabase, f2, "file_service_file", "txt");
		assert !currentDefaultFolder1.equals(currentDefaultFolder2) : "Folders should NOT be the same but are "
			+ " current folder 1 = " + currentDefaultFolder1 + " current folder 2 = " + currentDefaultFolder2;
	    
        // commit the transaction
	    tm.commit(ts);
		
		//begin a new transaction
		td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = tm.getTransaction(td);
		
		DefaultFileInfo myInfo = (DefaultFileInfo)dfss.getFileById(info1.getId(), false);
		DefaultFileInfo myInfo2 = (DefaultFileInfo)dfss.getFileById(info2.getId(), false);
		
		assert myInfo != null : "Was not able to find " + info1;
		assert myInfo2 != null : "Was not able to find " + info2;
		
		
	    tm.commit(ts);		
		
		dfss.deleteFileServer(fs.getName());
		assert dfss.getFileServer(fs.getName()) == null : "Should not find the file server";
	}
		
	
	
}
