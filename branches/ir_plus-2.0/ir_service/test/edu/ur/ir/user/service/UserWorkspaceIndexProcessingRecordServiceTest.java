package edu.ur.ir.user.service;

import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.PublisherService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecord;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;


/**
 * Tests for the user workspace indexing service
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class UserWorkspaceIndexProcessingRecordServiceTest {
	
	/** Spring application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");
	
	/** User publishing data access */
	UserPublishingFileSystemService userPublishingFileSystemService = 
		(UserPublishingFileSystemService) ctx.getBean("userPublishingFileSystemService");


	
    /** Institutional Item index processing record service  */
	UserWorkspaceIndexProcessingRecordService recordProcessingService = 
    	(UserWorkspaceIndexProcessingRecordService) ctx.getBean("userWorkspaceIndexProcessingRecordService");

    /** index processing type record service  */
	IndexProcessingTypeService indexProcessingTypeService = 
    	(IndexProcessingTypeService) ctx.getBean("indexProcessingTypeService");
	
	/** transaction manager for dealing with transactions  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	/** the transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Item services */
	ItemService itemService = (ItemService) ctx.getBean("itemService");
	
	/** Publisher services */
	PublisherService publisherService = (PublisherService) ctx.getBean("publisherService");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	/**
	 * Test setting records
	 * 
	 */
	public void insertRecordTest()
	{
		
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		IndexProcessingType updateProcessingType = new IndexProcessingType(IndexProcessingTypeService.UPDATE);
		indexProcessingTypeService.save(updateProcessingType);
		
		IndexProcessingType deleteProcessingType = new IndexProcessingType(IndexProcessingTypeService.DELETE);
		indexProcessingTypeService.save(deleteProcessingType);
		
		IndexProcessingType insertProcessingType =  new IndexProcessingType(IndexProcessingTypeService.INSERT);
		indexProcessingTypeService.save(insertProcessingType);
		
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		MyTestFileSystem myTestFileSystem = new MyTestFileSystem();
		UserWorkspaceIndexProcessingRecord insertProcessingRecord = recordProcessingService.save(42l, myTestFileSystem, insertProcessingType);
		tm.commit(ts);
		 
		
		ts = tm.getTransaction(td);
		assert  recordProcessingService.get(42l, myTestFileSystem, insertProcessingType) != null : "Should find record " + insertProcessingRecord;
		UserWorkspaceIndexProcessingRecord update= recordProcessingService.save(42l, myTestFileSystem, updateProcessingType);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert  recordProcessingService.get(42l, myTestFileSystem, insertProcessingType) != null : "Should find inser record  after tyring update " + insertProcessingRecord;
		assert  recordProcessingService.get(42l, myTestFileSystem, updateProcessingType) == null : "Should not find update record " + update;
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert  recordProcessingService.get(42l, myTestFileSystem, insertProcessingType) != null : "Should find record  after tyring no file change " + insertProcessingRecord;
		UserWorkspaceIndexProcessingRecord deleteProcessingRecord = recordProcessingService.save(42l, myTestFileSystem, deleteProcessingType);
		tm.commit(ts);
		 
		 
		ts = tm.getTransaction(td);
		assert recordProcessingService.get(42l, myTestFileSystem, insertProcessingType) == null : " Should not be able to find record " + insertProcessingRecord;
		assert recordProcessingService.get(42l, myTestFileSystem, deleteProcessingType) != null : "Should find record " + deleteProcessingRecord;

		recordProcessingService.delete(recordProcessingService.get(deleteProcessingRecord.getId(), false));

		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.DELETE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));

		tm.commit(ts);
		 

	}
	
	/**
	 * Test update record insert 
	 * 
	 */
	public void updateRecordTest()
	{
		
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		IndexProcessingType updateProcessingType = new IndexProcessingType(IndexProcessingTypeService.UPDATE);
		indexProcessingTypeService.save(updateProcessingType);
		
		IndexProcessingType deleteProcessingType = new IndexProcessingType(IndexProcessingTypeService.DELETE);
		indexProcessingTypeService.save(deleteProcessingType);
		
		IndexProcessingType insertProcessingType =  new IndexProcessingType(IndexProcessingTypeService.INSERT);
		indexProcessingTypeService.save(insertProcessingType);
		
		
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		MyTestFileSystem myTestFileSystem = new MyTestFileSystem();
		UserWorkspaceIndexProcessingRecord updateProcessingRecord = recordProcessingService.save(42l, myTestFileSystem, updateProcessingType);
		tm.commit(ts);
		 
		
		ts = tm.getTransaction(td);
		assert  recordProcessingService.get(42l, myTestFileSystem, updateProcessingType) != null : "Should find record " + updateProcessingRecord;
		tm.commit(ts);
		
	
		ts = tm.getTransaction(td);
		
		assert  recordProcessingService.get(42l, myTestFileSystem, updateProcessingType) != null : "Should find record  after tyring no file change " + updateProcessingRecord;
		UserWorkspaceIndexProcessingRecord deleteProcessingRecord = recordProcessingService.save(42l, myTestFileSystem, deleteProcessingType);
		tm.commit(ts);
		 
		 
		ts = tm.getTransaction(td);
		assert recordProcessingService.get(42l, myTestFileSystem, updateProcessingType) == null : " Should not be able to find record " + updateProcessingRecord;
		assert recordProcessingService.get(42l, myTestFileSystem, deleteProcessingType) != null : "Should find record " + deleteProcessingRecord;

		recordProcessingService.delete(recordProcessingService.get(deleteProcessingRecord.getId(), false));

		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.DELETE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));


		tm.commit(ts);
		 

	}
	
	/**
	 * Test update record insert with a publisher
	 * @throws UserDeletedPublicationException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	public void updateRecordTestWithPublisher() throws UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
	{
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		IndexProcessingType updateProcessingType = new IndexProcessingType(IndexProcessingTypeService.UPDATE);
		indexProcessingTypeService.save(updateProcessingType);
		
		IndexProcessingType deleteProcessingType = new IndexProcessingType(IndexProcessingTypeService.DELETE);
		indexProcessingTypeService.save(deleteProcessingType);
		
		IndexProcessingType insertProcessingType =  new IndexProcessingType(IndexProcessingTypeService.INSERT);
		indexProcessingTypeService.save(insertProcessingType);
		
		
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		helper.createTestRepositoryDefaultFileServer(properties);
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		assert user.getId() != null : "User id should not be null";
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		
		Publisher publisher = new Publisher("publisher");
		publisherService.savePublisher(publisher);
		
		PersonalItem personalItem = userPublishingFileSystemService.createRootPersonalItem(user, "articles", "rootCollection");
		GenericItem item1 = personalItem.getVersionedItem().getCurrentVersion().getItem();
		item1.setName("item1");
		ExternalPublishedItem externalPublishedItem = item1.createExternalPublishedItem();
		externalPublishedItem.setPublisher(publisher);
		externalPublishedItem.updatePublishedDate(05,13,2008);
		externalPublishedItem.setCitation("citation");
		UserWorkspaceIndexProcessingRecord updateProcessingRecord = recordProcessingService.save(personalItem.getOwner().getId(), personalItem, updateProcessingType);
		tm.commit(ts);
		 
		// test deleting the publisher
		ts = tm.getTransaction(td);
		item1 = itemService.getGenericItem(item1.getId(), false);
		item1.setName("item1");
		item1.deleteExternalPublishedItem();
		itemService.makePersistent(item1);
		personalItem = userPublishingFileSystemService.getPersonalItem(item1);
		recordProcessingService.save(personalItem.getOwner().getId(), personalItem, 
				updateProcessingType);
		tm.commit(ts);
	
		ts = tm.getTransaction(td);
		personalItem = userPublishingFileSystemService.getPersonalItem(personalItem.getId(), false);
		assert  recordProcessingService.get(personalItem.getOwner().getId(), personalItem, updateProcessingType) != null : "Should find record  " + updateProcessingRecord;
		UserWorkspaceIndexProcessingRecord deleteProcessingRecord = recordProcessingService.save(personalItem.getOwner().getId(), personalItem, deleteProcessingType);
		tm.commit(ts);
		 
		 
		ts = tm.getTransaction(td);
		personalItem = userPublishingFileSystemService.getPersonalItem(personalItem.getId(), false);
		assert recordProcessingService.get(personalItem.getOwner().getId(), personalItem, updateProcessingType) == null : " Should not be able to find record " + updateProcessingRecord;
		assert recordProcessingService.get(personalItem.getOwner().getId(), personalItem, deleteProcessingType) != null : "Should find record " + deleteProcessingRecord;

		user = userService.getUser(user.getId(), false);
		userService.deleteUser(user, user);
		
		publisherService.deletePublisher("publisher");
		
		recordProcessingService.delete(recordProcessingService.get(deleteProcessingRecord.getId(), false));
		assert recordProcessingService.get(personalItem.getOwner().getId(), personalItem, deleteProcessingType) == null : "Should not find record " + deleteProcessingRecord;
		
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.DELETE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
		helper.cleanUpRepository();

		tm.commit(ts);
		 
	}
	
	/**
	 * Simple class for testing
	 * @author Nathan Sarr
	 *
	 */
	class MyTestFileSystem implements FileSystem
	{
		
		/** eclipse generated test*/
		private static final long serialVersionUID = 8066616160887516079L;


		public FileSystemType getFileSystemType() {
			return FileSystemType.FILE;
		}

		public String getPath() {
			return "MyTestFileSystem - path";
		}

		public Long getId() {
			return 25l;
		}

		public int getVersion() {
			return 0;
		}

		public String getDescription() {
			return "MyTestFileSystem - description";
		}

		
		public String getName() {
			return "MyTestFileSystem - name";
		}
		
	}
}