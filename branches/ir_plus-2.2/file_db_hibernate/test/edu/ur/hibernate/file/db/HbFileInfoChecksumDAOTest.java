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

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.DefaultFileInfo;
import edu.ur.file.db.FileInfoChecksumDAO;
import edu.ur.file.db.FileInfoDAO;
import edu.ur.file.db.FileServerDAO;
import edu.ur.file.db.DefaultFileServer;
import edu.ur.file.db.FileServer;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.TreeFolderInfo;
import edu.ur.file.db.TreeFolderInfoDAO;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.hibernate.file.test.helper.ContextHolder;
import edu.ur.hibernate.file.test.helper.PropertiesLoader;
import edu.ur.util.FileUtil;
import edu.ur.file.checksum.Md5ChecksumCalculator;
import edu.ur.file.db.FileInfoChecksum;

public class HbFileInfoChecksumDAOTest {

	/**
	 * Properties file with testing specific information.
	 */
	Properties properties;
	
	/** Spring context  */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** File information data access */  
	FileInfoDAO fileInfoDAO = (FileInfoDAO)ctx.getBean("fileInfoDAO");

	/** File information data access */  
	FileInfoChecksumDAO fileInfoChecksumDAO = (FileInfoChecksumDAO)ctx.getBean("fileInfoChecksumDAO");

	/** Folder data access */
	TreeFolderInfoDAO treeFolderInfoDAO = (TreeFolderInfoDAO)ctx.getBean("treeFolderInfoDAO");
	
	/** File server data access */ 
	FileServerDAO fileServerDAO = (FileServerDAO) ctx.getBean("fileServerDAO");
	
	/** Unique name generator  */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) ctx.getBean("uniqueNameGenerator");
	
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
	 * Test FileInfo persistance
	 * @throws LocationAlreadyExistsException 
	 * @throws IllegalFileSystemNameException 
	 *
	 */
	@Test
	public void baseFileInfoChecksumDAOTest() throws LocationAlreadyExistsException, IllegalFileSystemNameException
	{

		TransactionStatus ts = tm.getTransaction(td);
		DefaultFileServer server = new DefaultFileServer("baseFileServerName2");
		fileServerDAO.makePersistent(server);
		
		// create a new file database
		DefaultFileDatabase fileDatabase = server.createFileDatabase("display_file_db_1", "file_db_1", 
				properties.getProperty("HbFileInfoTest"), "Description");
		
		fileServerDAO.makePersistent(server);
		assert uniqueNameGenerator.getNextName() != null : "Next unique should not be null";
		TreeFolderInfo rootFolderInfo1 = fileDatabase.createRootFolder("displayName", "uniqueName");
		
		// final file to store the file in
		TreeFolderInfo treeFolderInfo1 = rootFolderInfo1.createChild("displayName1", "uniqueName1");

		// start location for the file
		TreeFolderInfo treeFolderInfo2 = rootFolderInfo1.createChild("displayName2", "uniqueName2");
		
		//persist the folder
		treeFolderInfoDAO.makePersistent(rootFolderInfo1);
		
		assert uniqueNameGenerator.getNextName() != null : "Next unique should not be null";
		
		File directory = new File(treeFolderInfo2.getFullPath());
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		File f = testUtil.creatFile(directory, "testFile", "Hello  -  This is text in a file");
		
		DefaultFileInfo fileInfo1 = treeFolderInfo1.createFileInfo(f, "newFile1");
		fileInfo1.setDisplayName("displayName1");
		
		File newFile = new File(fileInfo1.getPath());
		assert newFile.exists() : "New File should exist";
		
		assert fileInfo1.getFullPath().equals(treeFolderInfo1.getFullPath() + "newFile1") : 
			"paths should be the same file path = " + fileInfo1.getPath() + 
			" folder info path = " + treeFolderInfo1.getFullPath();

		// create a file to store in the temporary folder
		File f2 =testUtil.creatFile(directory, "testFile2", "Hello  -  This is another text in a file"); 
			
		
		DefaultFileInfo fileInfo2 = treeFolderInfo1.createFileInfo(f2, "newFile2");
		fileInfo2.setDisplayName("displayName2");
		
		Md5ChecksumCalculator checksumCalculator = new Md5ChecksumCalculator();
		fileInfo1.addFileInfoChecksum(checksumCalculator);
		
		//persist the items
		fileInfoDAO.makePersistent(fileInfo1);
		fileInfoDAO.makePersistent(fileInfo2);
		tm.commit(ts);
		
		
		// restart the transaction
		ts = tm.getTransaction(td);
		// make sure object has been persisted
		FileInfo other = fileInfoDAO.getById(fileInfo1.getId(), false);
		
		FileInfoChecksum checksum = other.getFileInfoChecksum("MD5");
		assert checksum != null : "Should find checksum for " + other;
		checksum.setReCalculatedValue("recalculatedValue");
	    fileInfoChecksumDAO.makePersistent(checksum);
		
		tm.commit(ts);
		

		
	    // make sure types are correct
		// start a new transaction
		ts = tm.getTransaction(td);
		
		FileInfoChecksum otherChecksum = fileInfoChecksumDAO.getById(checksum.getId(), false);
		assert otherChecksum != null : "should find other checksum";
		
		assert otherChecksum.getReCalculatedValue().equals("recalculatedValue") : 
			"Recalculated value should be recalculatedValue but is " + otherChecksum.getReCalculatedValue();
		
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		// clean up the rest
		fileInfoDAO.makeTransient(fileInfoDAO.getById(fileInfo1.getId(), false));
		fileInfoDAO.makeTransient(fileInfoDAO.getById(fileInfo2.getId(), false));
		assert fileInfoDAO.getById(fileInfo2.getId(), false) == null : "should not find fileInfo2";
	
		FileServer otherServer = fileServerDAO.getById(server.getId(), false);
		fileServerDAO.makeTransient(otherServer);
		assert server.deleteDatabase(fileDatabase.getName());
		server.deleteFileServer();
		tm.commit(ts);
	}
	

}
