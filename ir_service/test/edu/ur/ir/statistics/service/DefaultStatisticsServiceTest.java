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

	/**
	 * Test number of downloads
	 * 
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws UserHasPublishedDeleteException
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void numberOfFileDownloadsForAllCollectionsTest() throws DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException {

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
        statisticsService.saveFileDownloadInfo(downloadInfo1);
        FileDownloadInfo downloadInfo2 = new FileDownloadInfo("123.0.0.7", irFile2.getId(), new Date());
        downloadInfo2.setDownloadCount(2);
        statisticsService.saveFileDownloadInfo(downloadInfo2);  
        statisticsService.updateRollUpCount(irFile1.getId());
        statisticsService.updateRollUpCount(irFile2.getId());
        assert statisticsService.getNumberOfDownloadsForAllCollections() == 3 : "Should be 3";
	    tm.commit(ts);

        ts = tm.getTransaction(td);
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(), false), user);
        institutionalItemService.deleteAllInstitutionalItemHistory();
        IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);
        statisticsService.deleteFileDownloadInfo(statisticsService.getFileDownloadInfo(downloadInfo1.getId(), false));
        statisticsService.deleteFileDownloadInfo(statisticsService.getFileDownloadInfo(downloadInfo2.getId(), false));
        
        statisticsService.delete(statisticsService.getFileDownloadRollUpByIrFileId(irFile1.getId()));
        statisticsService.delete(statisticsService.getFileDownloadRollUpByIrFileId(irFile2.getId()));
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
	public void testAddDownloads() throws DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException {

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
        statisticsService.updateRollUpCount(irFile1.getId());
        irFile2 = repositoryService.getIrFile(irFile2.getId(), false);
       
        // these shoudl be the same info because the download happens on the same day
        FileDownloadInfo info2 = statisticsService.addFileDownloadInfo("123.0.0.7", irFile2);
        FileDownloadInfo info3 = statisticsService.addFileDownloadInfo("123.0.0.7", irFile2);
        statisticsService.updateRollUpCount(irFile2.getId());
        
        Long count = statisticsService.getNumberOfDownloadsForAllCollections();
        assert info2.equals(info3) : " info 2 " + info2 + " should equal " + info3;
        assert info2.getId().equals(info3.getId()) : " info 2 id " + info2.getId() + " should equal " + info3.getId();
        assert count.equals(3l) : "Should be 3 but is " + count;
	    tm.commit(ts);

        ts = tm.getTransaction(td);
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(), false), user);
        institutionalItemService.deleteAllInstitutionalItemHistory();
        IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);
        statisticsService.deleteFileDownloadInfo(statisticsService.getFileDownloadInfo(info1.getId(), false));
        statisticsService.deleteFileDownloadInfo(statisticsService.getFileDownloadInfo(info2.getId(), false));
        
        statisticsService.delete(statisticsService.getFileDownloadRollUpByIrFileId(irFile1.getId()));
        statisticsService.delete(statisticsService.getFileDownloadRollUpByIrFileId(irFile2.getId()));
        helper.cleanUpRepository();
        tm.commit(ts);
	}
}
