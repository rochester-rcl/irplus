package edu.ur.file.db.service;

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
import edu.ur.file.db.ChecksumCheckerService;
import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.file.db.DefaultFileInfo;
import edu.ur.file.db.DefaultFileServer;
import edu.ur.file.db.FileInfoChecksum;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.TreeFolderInfo;
import edu.ur.file.db.service.test.helper.ContextHolder;
import edu.ur.file.db.service.test.helper.PropertiesLoader;
import edu.ur.util.FileUtil;

@Test(groups = { "baseTests" }, enabled = true)
public class DefaultChecksumCheckerServiceTest {
	
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


	/** Default file server service data access */
	ChecksumCheckerService checksumCheckerService = (ChecksumCheckerService) ctx
			.getBean("checksumCheckerService");
	
	/**  Properties file with testing specific information. */
	Properties properties;
	
	
	
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
	 * Test Basic checksum functionality
	 * 
	 * @throws LocationAlreadyExistsException 
	 * @throws IllegalFileSystemNameException 
	 * 
	 */
	@Test
	public void basicChecksumCheckerServiceTest() throws LocationAlreadyExistsException, IllegalFileSystemNameException {
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
		
		FileInfoChecksum checksum = myInfo.getFileInfoChecksum("MD5");
		checksum = checksumCheckerService.checkChecksum(checksum);
		assert checksum.getDateReCalculated() != null : "Date re-calculated should be set";
		assert checksum.getReCalculatedPassed() == true : "Recalucation should have passed";
		assert checksum.getReCalculatedValue() != null : "Re-calculated value should be set";
		assert checksum.getReCalculateChecksum() == true : "Should be set to recalculate the value";
		assert dfss.findFile(info.getName()).equals(info) :
			"The file information should be the same";
		
		
	    tm.commit(ts);		

	    ts = tm.getTransaction(td);
	    dfss.deleteFile(dfss.getFileById(info.getId(), false));
		dfss.deleteFileServer(fs.getName());
		assert dfss.getFileServer(fs.getName()) == null : "Should not find the file server";
		tm.commit(ts);		
	}
	
	
	/**
	 * Test Basic checksum functionality
	 * 
	 * @throws LocationAlreadyExistsException 
	 * @throws IllegalFileSystemNameException 
	 * 
	 */
	@Test
	public void badFileChecksumCheckerServiceTest() throws LocationAlreadyExistsException, IllegalFileSystemNameException {
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
		
		FileInfoChecksum checksum = myInfo.getFileInfoChecksum("MD5");
		checksum.setChecksum("badchecksum");
		checksum = checksumCheckerService.checkChecksum(checksum);
		assert checksum.getDateReCalculated() != null : "Date re-calculated should be set";
		assert checksum.getReCalculatedPassed() == false : "Recalucation should have NOT passed";
		assert checksum.getReCalculatedValue() != null : "Re-calculated value should be set";
		assert checksum.getReCalculateChecksum() == false : "Should be set to NOT recalculate the value";
		
	    tm.commit(ts);		

	    ts = tm.getTransaction(td);
	    dfss.deleteFile(dfss.getFileById(info.getId(), false));
		dfss.deleteFileServer(fs.getName());
		assert dfss.getFileServer(fs.getName()) == null : "Should not find the file server";
		tm.commit(ts);		
	}


}
