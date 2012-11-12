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
import edu.ur.file.checksum.Md5ChecksumCalculator;
import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.file.db.DefaultFileInfo;
import edu.ur.file.db.DefaultFileServer;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FileInfoChecksum;
import edu.ur.file.db.FileInfoChecksumDAO;
import edu.ur.file.db.FileInfoChecksumResetHistory;
import edu.ur.file.db.FileInfoChecksumResetHistoryDAO;
import edu.ur.file.db.FileInfoDAO;
import edu.ur.file.db.FileServer;
import edu.ur.file.db.FileServerDAO;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.TreeFolderInfo;
import edu.ur.file.db.TreeFolderInfoDAO;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.hibernate.file.test.helper.ContextHolder;
import edu.ur.hibernate.file.test.helper.PropertiesLoader;
import edu.ur.util.FileUtil;

public class HbFileInfoChecksumResetHistoryDAOTest {
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
	
	/** checksum history data access */  
	FileInfoChecksumResetHistoryDAO fileInfoChecksumResetHistoryDAO = (FileInfoChecksumResetHistoryDAO)ctx.getBean("fileInfoChecksumResetHistoryDAO");

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
	public void baseFileInfoChecksumHistoryDAOTest() throws LocationAlreadyExistsException, IllegalFileSystemNameException
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

		Md5ChecksumCalculator checksumCalculator = new Md5ChecksumCalculator();
		FileInfoChecksum checksumInfo = fileInfo1.addFileInfoChecksum(checksumCalculator);
		
		String originalChecksum = checksumInfo.getChecksum();
		//persist the items
		fileInfoDAO.makePersistent(fileInfo1);
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
		
		FileInfoChecksumResetHistory resetHistory = otherChecksum.reset("newChecksum", 1l, "checksum reset for testing");
		fileInfoChecksumDAO.makePersistent(otherChecksum);
		tm.commit(ts);
		
		// make sure types are correct
		// start a new transaction
		ts = tm.getTransaction(td);
		resetHistory = fileInfoChecksumResetHistoryDAO.getById(resetHistory.getId(), false);
		assert resetHistory != null : "Checksum history should exist";
		otherChecksum = fileInfoChecksumDAO.getById(checksum.getId(), false);
		assert otherChecksum.getResetHistory().contains(resetHistory) : "other checksum " + otherChecksum + " should contain history " + resetHistory;
		assert resetHistory.getOriginalChecksum().equals(originalChecksum) : "Original checksum " + originalChecksum + " does not equal " + resetHistory.getOriginalChecksum();
		assert resetHistory.getNewChecksum().equals("newChecksum") : "Should equal new checksum but equals " + resetHistory.getNewChecksum();
		
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		// clean up the rest
		fileInfoDAO.makeTransient(fileInfoDAO.getById(fileInfo1.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert fileInfoChecksumResetHistoryDAO.getById(resetHistory.getId(), false) == null : "Reset history should no longer exist";
		FileServer otherServer = fileServerDAO.getById(server.getId(), false);
		fileServerDAO.makeTransient(otherServer);
		assert server.deleteDatabase(fileDatabase.getName());
		server.deleteFileServer();
		tm.commit(ts);
	}
}
