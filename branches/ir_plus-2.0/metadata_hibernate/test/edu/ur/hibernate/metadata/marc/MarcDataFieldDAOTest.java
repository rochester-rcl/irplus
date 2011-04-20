package edu.ur.hibernate.metadata.marc;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.metadata.helper.test.ContextHolder;
import edu.ur.metadata.marc.MarcDataField;
import edu.ur.metadata.marc.MarcDataFieldDAO;

public class MarcDataFieldDAOTest {
	
    /** spring application context manager  */
    ApplicationContext ctx = ContextHolder.getApplicationContext();
    
    /** dublin core element data access object */
    MarcDataFieldDAO marcDataFieldDAO = (MarcDataFieldDAO)ctx.getBean("marcDataFieldDAO");
    
    /** platform transaction manager  */
    PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
    
    /** transaction definition */
    TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
    
	/**
	 * Test dublin core term persistence
	 */
	@Test
	public void baseMarcDataFieldDAOTest() throws Exception{

		MarcDataField element = new MarcDataField("field", true, "100");
 		element.setDescription("mdDescription");
 		
         
        TransactionStatus ts = tm.getTransaction(td);
 		marcDataFieldDAO.makePersistent(element);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		MarcDataField other = marcDataFieldDAO.getById(element.getId(), false);
        assert other.equals(element) : "Marc data fields should be equal mt = " + element + " other = " + other;
         
        MarcDataField dublinCoreElementByName =  marcDataFieldDAO.findByUniqueName(element.getCode());
        assert dublinCoreElementByName.equals(element) : "Marc data field should be found " + element; 
         
        marcDataFieldDAO.makeTransient(other);
        assert  marcDataFieldDAO.getById(other.getId(), false) == null : "Should no longer be able to find marc data field";
	    tm.commit(ts);
	}

}
