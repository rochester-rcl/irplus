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


package edu.ur.ir.statistics.service;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.CollectionDoesNotAcceptItemsException;
import edu.ur.ir.institution.DeletedInstitutionalItemService;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.statistics.DownloadStatisticsService;
import edu.ur.ir.statistics.FileDownloadInfo;
import edu.ur.ir.statistics.IgnoreIpAddress;
import edu.ur.ir.statistics.IgnoreIpAddressService;
import edu.ur.ir.statistics.IpIgnoreFileDownloadInfo;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;
import edu.ur.util.FileUtil;

/**
 * Test for statistics
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultStatisticsServiceTest {
	
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	// Check the repositories
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	 
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);

	/** Service for dealing with news related information */
	DownloadStatisticsService statisticsService = (DownloadStatisticsService) ctx.getBean("downloadStatisticsService");

	/** Service for dealing with ignoring statistics information */
	IgnoreIpAddressService ignoreStatisticsService = (IgnoreIpAddressService) ctx.getBean("ignoreIpAddressService");
	
    /** Collection service  */
    InstitutionalCollectionService institutionalCollectionService = 
    	(InstitutionalCollectionService) ctx.getBean("institutionalCollectionService");
    
    /** Repository service  */
    RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
    
    /** Item service  */
    ItemService itemService = (ItemService) ctx.getBean("itemService");
    
    /** Item service  */
    UserService userService = (UserService) ctx.getBean("userService");
    
    /** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** service for dealing with institutional items */
	InstitutionalItemService institutionalItemService = (InstitutionalItemService)ctx.getBean("institutionalItemService");

    /** Deleted Institutional Item service  */
    DeletedInstitutionalItemService deletedInstitutionalItemService = 
    	(DeletedInstitutionalItemService) ctx.getBean("deletedInstitutionalItemService");
	/**
	 * Test number of downloads
	 * 
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws UserHasPublishedDeleteException
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void numberOfFileDownloadsForAllCollectionsTest() throws DuplicateNameException, 
	IllegalFileSystemNameException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException, 
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException{

		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		// save the repository
		tm.commit(ts);

		// Start the transaction - create collections
		ts = tm.getTransaction(td);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f1 = testUtil.creatFile(directory, "testFile1", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test1");

		File f2 = testUtil.creatFile(directory, "testFile2", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test2");

		IrFile irFile1 = repositoryService.createIrFile(repo, f1, "testFile", "Hello  - irFile This is text in a file - VersionedFileDAO test1");
		IrFile irFile2 = repositoryService.createIrFile(repo, f2, "testFile", "Hello  - irFile This is text in a file - VersionedFileDAO test2");
		
		// save the repository
		tm.commit(ts);	
		
        // Start the transaction - create collections
		ts = tm.getTransaction(td);
		
		GenericItem item1 = new GenericItem("itemName1");
		item1.addFile(irFile1);
		itemService.makePersistent(item1);

		GenericItem item2 = new GenericItem("itemName2");
		item2.addFile(irFile2);
		itemService.makePersistent(item2);

		// save the repository
		tm.commit(ts);	

        // Start the transaction - create collections
		ts = tm.getTransaction(td);		
		repo = helper.getRepository();
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		collection.createInstitutionalItem(item1);
		InstitutionalCollection subCollection = collection.createChild("subChild");
		subCollection.createInstitutionalItem(item2);
		institutionalCollectionService.saveCollection(collection);
		tm.commit(ts);

        ts = tm.getTransaction(td);
        FileDownloadInfo downloadInfo1 = new FileDownloadInfo("123.0.0.1", irFile1.getId(), new Date());
        downloadInfo1.setDownloadCount(1);
        statisticsService.save(downloadInfo1);
        FileDownloadInfo downloadInfo2 = new FileDownloadInfo("123.0.0.7", irFile2.getId(), new Date());
        downloadInfo2.setDownloadCount(2);
        statisticsService.save(downloadInfo2);  
        
        Long count1 = statisticsService.getNumberOfFileDownloadsForIrFile(irFile1.getId());
        irFile1 = repositoryService.getIrFile(irFile1.getId(), false);
        irFile1.setDownloadCount(count1);
        repositoryService.save(irFile1);
        
        Long count2= statisticsService.getNumberOfFileDownloadsForIrFile(irFile2.getId());
        irFile2 = repositoryService.getIrFile(irFile2.getId(), false);
        irFile2.setDownloadCount(count2);
        repositoryService.save(irFile2);
        
        assert statisticsService.getNumberOfDownloadsForAllCollections() == 3 : "Should be 3";
	    tm.commit(ts);

        ts = tm.getTransaction(td);
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(), false), user);
        deletedInstitutionalItemService.deleteAllInstitutionalItemHistory();
        IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);
        statisticsService.delete(statisticsService.getFileDownloadInfo(downloadInfo1.getId(), false));
        statisticsService.delete(statisticsService.getFileDownloadInfo(downloadInfo2.getId(), false));
                
        helper.cleanUpRepository();
        tm.commit(ts);
	}
	
	/**
	 * Test adding downloads number of downloads
	 * 
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws UserHasPublishedDeleteException
	 * @throws LocationAlreadyExistsException 
     */
	@Test
	public void testAddDownloads() throws DuplicateNameException, 
	IllegalFileSystemNameException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException, 
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException{

		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
	 
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);

		// save the repository
		tm.commit(ts);

		// Start the transaction - create collections
		ts = tm.getTransaction(td);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f1 = testUtil.creatFile(directory, "testFile1", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test1");

		File f2 = testUtil.creatFile(directory, "testFile2", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test2");

		IrFile irFile1 = repositoryService.createIrFile(repo, f1, "testFile", "Hello  - irFile This is text in a file - VersionedFileDAO test1");
		IrFile irFile2 = repositoryService.createIrFile(repo, f2, "testFile", "Hello  - irFile This is text in a file - VersionedFileDAO test2");
		
		// save the repository
		tm.commit(ts);	
		
        // Start the transaction - create collections
		ts = tm.getTransaction(td);
		
		GenericItem item1 = new GenericItem("itemName1");
		item1.addFile(irFile1);
		itemService.makePersistent(item1);

		GenericItem item2 = new GenericItem("itemName2");
		item2.addFile(irFile2);
		itemService.makePersistent(item2);

		// save the repository
		tm.commit(ts);	

        // Start the transaction - create collections
		ts = tm.getTransaction(td);		
		repo = helper.getRepository();
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		collection.createInstitutionalItem(item1);
		InstitutionalCollection subCollection = collection.createChild("subChild");
		subCollection.createInstitutionalItem(item2);
		institutionalCollectionService.saveCollection(collection);
		tm.commit(ts);

		
        ts = tm.getTransaction(td);
        irFile1 = repositoryService.getIrFile(irFile1.getId(), false);
        FileDownloadInfo info1 = statisticsService.addFileDownloadInfo("123.0.0.1", irFile1);
        Long count1 = statisticsService.getNumberOfFileDownloadsForIrFile(irFile1.getId());
        irFile1.setDownloadCount(count1);
        repositoryService.save(irFile1);
        
        
        
        
        
        
        
        
        
        
        irFile2 = repositoryService.getIrFile(irFile2.getId(), false);
       
        // these shoudl be the same info because the download happens on the same day
        FileDownloadInfo info2 = statisticsService.addFileDownloadInfo("123.0.0.7", irFile2);
        FileDownloadInfo info3 = statisticsService.addFileDownloadInfo("123.0.0.7", irFile2);
        
        Long count2 = statisticsService.getNumberOfFileDownloadsForIrFile(irFile2.getId());
        irFile2.setDownloadCount(count2);
        repositoryService.save(irFile2);
        
        Long count = statisticsService.getNumberOfDownloadsForAllCollections();
        assert info2.equals(info3) : " info 2 " + info2 + " should equal " + info3;
        assert info2.getId().equals(info3.getId()) : " info 2 id " + info2.getId() + " should equal " + info3.getId();
        assert count.equals(3l) : "Should be 3 but is " + count;
	    tm.commit(ts);

        ts = tm.getTransaction(td);
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(), false), user);
        deletedInstitutionalItemService.deleteAllInstitutionalItemHistory();
        IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);
        statisticsService.delete(statisticsService.getFileDownloadInfo(info1.getId(), false));
        statisticsService.delete(statisticsService.getFileDownloadInfo(info2.getId(), false));
        
        helper.cleanUpRepository();
        tm.commit(ts);
	}
		
	/**
	 * Make sure process file download count correctly
	 */
	@Test
	public void testProcessFileDownload() throws 
	    DuplicateNameException, 
	    IllegalFileSystemNameException,
	    UserHasPublishedDeleteException, 
	    UserDeletedPublicationException, 
	    LocationAlreadyExistsException 
    {
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
	 
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);

		// save the repository
		tm.commit(ts);

		// Start the transaction - create collections
		ts = tm.getTransaction(td);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f1 = testUtil.creatFile(directory, "testFile1", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test1");

		File f2 = testUtil.creatFile(directory, "testFile2", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test2");

		IrFile irFile1 = repositoryService.createIrFile(repo, f1, "testFile", "Hello  - irFile This is text in a file - VersionedFileDAO test1");
		IrFile irFile2 = repositoryService.createIrFile(repo, f2, "testFile", "Hello  - irFile This is text in a file - VersionedFileDAO test2");
		
		// save the repository
		tm.commit(ts);	
		
        // Start the transaction - create collections
		ts = tm.getTransaction(td);
		
		IgnoreIpAddress ignoreIpAddress1 = new IgnoreIpAddress(123, 0, 0, 1, 1);
		ignoreIpAddress1.setStoreCounts(true);
		ignoreStatisticsService.saveIgnoreIpAddress(ignoreIpAddress1);
		

		IgnoreIpAddress ignoreIpAddress2 = new IgnoreIpAddress(199, 0, 0, 1, 1);
		ignoreIpAddress2.setStoreCounts(true);
		ignoreStatisticsService.saveIgnoreIpAddress(ignoreIpAddress2);

		
		// save the repository
		tm.commit(ts);	
		
        ts = tm.getTransaction(td);
        irFile1 = repositoryService.getIrFile(irFile1.getId(), false);
        statisticsService.processFileDownload("123.0.0.1", irFile1);
        statisticsService.processFileDownload("199.0.0.1", irFile1);
        statisticsService.processFileDownload("199.0.0.3", irFile1);
        irFile2 = repositoryService.getIrFile(irFile2.getId(), false);
       
        // these shoudl be the same info because the download happens on the same day
        statisticsService.processFileDownload("123.0.0.7", irFile2);
        statisticsService.processFileDownload("199.0.0.1", irFile2);
        statisticsService.processFileDownload("199.0.0.3", irFile2);
        statisticsService.processFileDownload("123.0.0.7", irFile2);
        
	    tm.commit(ts);

        ts = tm.getTransaction(td);
        
        FileDownloadInfo info1 = statisticsService.getFileDownloadInfo("199.0.0.3", irFile1.getId(), new Date());
        FileDownloadInfo info2 = statisticsService.getFileDownloadInfo("199.0.0.3", irFile2.getId(), new Date());
        FileDownloadInfo info3 = statisticsService.getFileDownloadInfo("123.0.0.7", irFile2.getId(), new Date());

        assert info1.getDownloadCount() == 1 : "Count should equal 1 but equals " + info1.getDownloadCount();
        assert info2.getDownloadCount() == 1 : "Count should equal 1 but equals " + info2.getDownloadCount();
        assert info3.getDownloadCount() == 2 : "Count should equal 2 but equals " + info3.getDownloadCount();
        
        IpIgnoreFileDownloadInfo ignoreInfo1 = statisticsService.getIpIgnoreFileDownloadInfo("123.0.0.1", irFile1.getId(), new Date());
        assert ignoreInfo1.getDownloadCount() == 1 : "Count should equal 1 but equals " + ignoreInfo1.getDownloadCount();
        
        IpIgnoreFileDownloadInfo ignoreInfo2 = statisticsService.getIpIgnoreFileDownloadInfo("199.0.0.1", irFile1.getId(), new Date());
        assert ignoreInfo2.getDownloadCount() == 1 : "Count should equal 1 but equals " + ignoreInfo2.getDownloadCount();

        IpIgnoreFileDownloadInfo ignoreInfo3 = statisticsService.getIpIgnoreFileDownloadInfo("199.0.0.1", irFile2.getId(), new Date());
        assert ignoreInfo3.getDownloadCount() == 1 : "Count should equal 1 but equals " + ignoreInfo3.getDownloadCount();
        
        tm.commit(ts);
        
        ts = tm.getTransaction(td);
        
        statisticsService.delete(statisticsService.getFileDownloadInfo(info1.getId(), false));
        statisticsService.delete(statisticsService.getFileDownloadInfo(info2.getId(), false));
        statisticsService.delete(statisticsService.getFileDownloadInfo(info3.getId(), false));
        
        statisticsService.delete(statisticsService.getIpIgnoreFileDownloadInfo(ignoreInfo1.getId(), false));
        statisticsService.delete(statisticsService.getIpIgnoreFileDownloadInfo(ignoreInfo2.getId(), false));
        statisticsService.delete(statisticsService.getIpIgnoreFileDownloadInfo(ignoreInfo3.getId(), false));
        
        IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);

        repositoryService.deleteIrFile(repositoryService.getIrFile(irFile1.getId(), false));
        repositoryService.deleteIrFile(repositoryService.getIrFile(irFile2.getId(), false));
        
        ignoreStatisticsService.deleteIgnoreIpAddress(ignoreStatisticsService.getIgnoreIpAddress(ignoreIpAddress1.getId(), false));
        ignoreStatisticsService.deleteIgnoreIpAddress(ignoreStatisticsService.getIgnoreIpAddress(ignoreIpAddress2.getId(), false));
        
        helper.cleanUpRepository();
        tm.commit(ts);
	}
	
}
