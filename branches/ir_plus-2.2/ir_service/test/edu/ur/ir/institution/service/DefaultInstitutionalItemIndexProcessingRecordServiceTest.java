package edu.ur.ir.institution.service;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecord;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;


/**
 * Tests for the Institutional item indexing service
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultInstitutionalItemIndexProcessingRecordServiceTest {
	
	
	/** Spring application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
    /** Institutional Item index processing record service  */
	InstitutionalItemIndexProcessingRecordService recordProcessingService = 
    	(InstitutionalItemIndexProcessingRecordService) ctx.getBean("institutionalItemIndexProcessingRecordService");

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
		InstitutionalItemIndexProcessingRecord insertProcessingRecord = recordProcessingService.save(1l, insertProcessingType);
		tm.commit(ts);
		 
		
		ts = tm.getTransaction(td);
		assert  recordProcessingService.get(1l, insertProcessingType) != null : "Should find record " + insertProcessingRecord;
		InstitutionalItemIndexProcessingRecord update= recordProcessingService.save(1l, updateProcessingType);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert  recordProcessingService.get(1l, insertProcessingType) != null : "Should find inser record  after tyring update " + insertProcessingRecord;
		assert  recordProcessingService.get(1l, updateProcessingType) == null : "Should not find update record " + update;
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert  recordProcessingService.get(1l, insertProcessingType) != null : "Should find record  after tyring no file change " + insertProcessingRecord;
		InstitutionalItemIndexProcessingRecord deleteProcessingRecord = recordProcessingService.save(1l, deleteProcessingType);
		tm.commit(ts);
		 
		 
		ts = tm.getTransaction(td);
		assert recordProcessingService.get(1l, insertProcessingType) == null : " Should not be able to find record " + insertProcessingRecord;
		assert recordProcessingService.get(1l, deleteProcessingType) != null : "Should find record " + deleteProcessingRecord;

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
		InstitutionalItemIndexProcessingRecord updateProcessingRecord = recordProcessingService.save(1l, updateProcessingType);
		tm.commit(ts);
		 
		
		ts = tm.getTransaction(td);
		assert  recordProcessingService.get(1l, updateProcessingType) != null : "Should find record " + updateProcessingRecord;
		tm.commit(ts);
		
	
		ts = tm.getTransaction(td);
		assert  recordProcessingService.get(1l, updateProcessingType) != null : "Should find record  after tyring no file change " + updateProcessingRecord;
		InstitutionalItemIndexProcessingRecord deleteProcessingRecord = recordProcessingService.save(1l, deleteProcessingType);
		tm.commit(ts);
		 
		 
		ts = tm.getTransaction(td);
		assert recordProcessingService.get(1l, updateProcessingType) == null : " Should not be able to find record " + updateProcessingRecord;
		assert recordProcessingService.get(1l, deleteProcessingType) != null : "Should find record " + deleteProcessingRecord;

		recordProcessingService.delete(recordProcessingService.get(deleteProcessingRecord.getId(), false));

		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.DELETE));
		indexProcessingTypeService.delete(indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));


		tm.commit(ts);
		 

	}
	
	
}

















