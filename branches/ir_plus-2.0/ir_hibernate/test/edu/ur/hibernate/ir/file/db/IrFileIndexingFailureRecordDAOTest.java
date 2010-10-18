package edu.ur.hibernate.ir.file.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.file.IrFileIndexingFailureRecord;
import edu.ur.ir.file.IrFileIndexingFailureRecordDAO;

/**
 * Test the persistence methods for file indexing failure records
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrFileIndexingFailureRecordDAOTest {

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	IrFileIndexingFailureRecordDAO irFileIndexingFailureRecordDAO = 
		(IrFileIndexingFailureRecordDAO)ctx.getBean("irFileIndexingFailureRecordDAO");
	
	// Transaction manager
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
    		TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test personal file delete record persistence
	 */
	@Test
	public void baseIrFileIndexingFailureRecordDAOTest() throws Exception{

        TransactionStatus ts = tm.getTransaction(td);
        IrFileIndexingFailureRecord  irFileIndexingFailureRecord = new IrFileIndexingFailureRecord(45l, "Testing index failure");
	
        irFileIndexingFailureRecordDAO .makePersistent(irFileIndexingFailureRecord);
 		tm.commit(ts);
 		
 		IrFileIndexingFailureRecord other = irFileIndexingFailureRecordDAO.getById(irFileIndexingFailureRecord.getId(), false);
        assert other.equals(irFileIndexingFailureRecord) : "index failure records should be equal other = " + irFileIndexingFailureRecord + " record =  " + irFileIndexingFailureRecord;
         
        assert irFileIndexingFailureRecordDAO.getCount() == 1 : "Should find one record but found " +  irFileIndexingFailureRecordDAO.getCount();
        irFileIndexingFailureRecordDAO.makeTransient(other);
        assert   irFileIndexingFailureRecordDAO.getById(other.getId(), false) == null : "Should no longer be able to record";
	}
	
}
