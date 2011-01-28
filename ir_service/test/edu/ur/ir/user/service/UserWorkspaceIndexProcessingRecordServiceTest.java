package edu.ur.ir.user.service;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
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