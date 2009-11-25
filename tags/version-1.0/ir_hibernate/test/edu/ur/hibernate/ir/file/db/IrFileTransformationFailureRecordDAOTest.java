package edu.ur.hibernate.ir.file.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.file.IrFileTransformationFailureRecord;
import edu.ur.ir.file.IrFileTransformationFailureRecordDAO;

/**
 * Test the persistence methods for file transformation failure records
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrFileTransformationFailureRecordDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	IrFileTransformationFailureRecordDAO irFileTransformationFailureRecordDAO = 
		(IrFileTransformationFailureRecordDAO)ctx.getBean("irFileTransformationFailureRecordDAO");
	
	// Transaction manager
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
    		TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test personal file delete record persistence
	 */
	@Test
	public void baseIrFileTransformationFailureRecordDAOTest() throws Exception{

        TransactionStatus ts = tm.getTransaction(td);
        IrFileTransformationFailureRecord  irFileTransformationFailureRecord = new IrFileTransformationFailureRecord(45l, "Testing index failure");
	
        irFileTransformationFailureRecordDAO .makePersistent(irFileTransformationFailureRecord);
 		tm.commit(ts);
 		
 		ts = tm.getTransaction(td);
 		IrFileTransformationFailureRecord other = irFileTransformationFailureRecordDAO.getById(irFileTransformationFailureRecord.getId(), false);
        assert other.equals(irFileTransformationFailureRecord) : "index failure records should be equal";
         
        assert irFileTransformationFailureRecordDAO.getCount() == 1 : "Should find one record but found " +  irFileTransformationFailureRecordDAO.getCount();
        irFileTransformationFailureRecordDAO.makeTransient(other);
        assert   irFileTransformationFailureRecordDAO.getById(other.getId(), false) == null : "Should no longer be able to record";
        tm.commit(ts);
	}
	


}
