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
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.DefaultFileInfo;
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

/**
 * Test the persistance methods for FileInfo Information
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class HbFileInfoDAOTest {
	/**
	 * Properties file with testing specific information.
	 */
	Properties properties;
	
	/** Spring context  */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** File information data access */  
	FileInfoDAO fileInfoDAO = (FileInfoDAO)ctx.getBean("fileInfoDAO");
	
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
	 *
	 */
	@Test
	public void baseFileInfoDAOTest() throws LocationAlreadyExistsException
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
		
		//persist the items
		fileInfoDAO.makePersistent(fileInfo1);
		fileInfoDAO.makePersistent(fileInfo2);
		
		assert fileInfoDAO.getCount() == 2l : "2 FileInfos should exist but there are " + fileInfoDAO.getCount();
		assert fileInfoDAO.getAllNameOrder().size() == 2 : "2 File Infos should exist";
		assert fileInfoDAO.findByUniqueName(fileInfo1.getName()) != null : "Should find file info";
 		

		
		// make sure object has been persisted
		FileInfo other = fileInfoDAO.getById(fileInfo1.getId(), false);
		assert other != null : "FileInfo should be found";
		assert other.equals(fileInfo1) : "Other should be the same as fileInfo1 ";
		
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		assert other.getCreatedDate() != null : "Created date should be set";
		assert other.getModifiedDate() != null : "Modified date should be set";
		tm.commit(ts);
		

		
	    // make sure types are correct
		// start a new transaction
		ts = tm.getTransaction(td);
		FileInfo other2 = fileInfoDAO.getById(fileInfo2.getId(), false);
		assert !other.equals(other2) : "FileInfos should be different";
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		// test deleting an object
		fileInfoDAO.makeTransient(fileInfo1);
		
		// make sure it is gone
		assert fileInfoDAO.getById(fileInfo1.getId(), false) == null : "Should not find fileInfo1";
		
		assert fileInfoDAO.getById(fileInfo2.getId(), false).equals(fileInfo2) : "should find fileInfo2";
        //commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		// clean up the rest
		fileInfoDAO.makeTransient(fileInfoDAO.getById(fileInfo2.getId(), false));
		assert fileInfoDAO.getById(fileInfo2.getId(), false) == null : "should not find fileInfo2";
	
		FileServer otherServer = fileServerDAO.getById(server.getId(), false);
		fileServerDAO.makeTransient(otherServer);
		assert server.deleteDatabase(fileDatabase.getName());
		server.deleteFileServer();
		tm.commit(ts);
	}
	
	/**
	 * Test FileInfo persistance
	 * @throws LocationAlreadyExistsException 
	 *
	 */
	@Test
	public void testFileInfoChecksumDAOTest() throws LocationAlreadyExistsException
	{

		TransactionStatus ts = tm.getTransaction(td);
		DefaultFileServer server = new DefaultFileServer("baseFileServerName2");
		fileServerDAO.makePersistent(server);
		
		// create a new file database
		DefaultFileDatabase fileDatabase = server.createFileDatabase("display_file_db_1", "file_db_1", 
				properties.getProperty("HbFileInfoTest"), "Description");
		
		fileServerDAO.makePersistent(server);
		TreeFolderInfo rootFolderInfo1 = fileDatabase.createRootFolder("displayName", "uniqueName");
		
		// start location for the file
		TreeFolderInfo treeFolderInfo2 = rootFolderInfo1.createChild("displayName2", "uniqueName2");

		//persist the root folder
		treeFolderInfoDAO.makePersistent(rootFolderInfo1);
		
		File directory = new File(treeFolderInfo2.getFullPath());
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		File f = testUtil.creatFile(directory, "testFile", "Hello  -  This is text in a file");
		
		DefaultFileInfo fileInfo1 = rootFolderInfo1.createFileInfo(f, "newFile1");
		fileInfo1.setDisplayName("displayName1");
		
		Md5ChecksumCalculator checksumCalculator = new Md5ChecksumCalculator();
		
		File newFile = new File(fileInfo1.getPath());
		assert newFile.exists() : "New File should exist";
		
		fileInfo1.addFileInfoChecksum(checksumCalculator);
		
		FileInfoChecksum checksum = fileInfo1.getFileInfoChecksum(checksumCalculator.getAlgorithmType());
		//persist the items
		fileInfoDAO.makePersistent(fileInfo1);
		
 		

		
		// make sure object has been persisted
		FileInfo other = fileInfoDAO.getById(fileInfo1.getId(), false);
		assert other != null : "FileInfo should be found";
		assert other.equals(fileInfo1) : "Other should be the same as fileInfo1 ";
		
		FileInfoChecksum myChecksum = other.getFileInfoChecksum(checksumCalculator.getAlgorithmType());
		assert myChecksum != null : "File info checksum should not be null";
		assert myChecksum.equals(checksum): "checksums should be equal My Checksum = " 
			+ myChecksum + " checksum = " + checksum;
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		
		FileServer otherServer = fileServerDAO.getById(server.getId(), false);
		fileServerDAO.makeTransient(otherServer);
		assert server.deleteDatabase(fileDatabase.getName());
		server.deleteFileServer();
		tm.commit(ts);
		
	}
	
	
}
