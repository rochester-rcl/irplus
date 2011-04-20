package edu.ur.hibernate.metadata.marc;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.metadata.helper.test.ContextHolder;
import edu.ur.metadata.marc.MarcSubField;
import edu.ur.metadata.marc.MarcSubFieldDAO;

public class MarcSubFieldDAOTest {
	
    /** spring application context manager  */
    ApplicationContext ctx = ContextHolder.getApplicationContext();
    
    /** dublin core element data access object */
    MarcSubFieldDAO marcSubFieldDAO = (MarcSubFieldDAO)ctx.getBean("marcSubFieldDAO");
    
    /** platform transaction manager  */
    PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
    
    /** transaction definition */
    TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
    
	/**
	 * Test dublin core term persistence
	 */
	@Test
	public void baseMarcSubFieldDAOTest() throws Exception{

		MarcSubField element = new MarcSubField("sub field");
 		element.setDescription("mdDescription");
 		
         
        TransactionStatus ts = tm.getTransaction(td);
 		marcSubFieldDAO.makePersistent(element);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		MarcSubField other = marcSubFieldDAO.getById(element.getId(), false);
        assert other.equals(element) : "Marc sub fields should be equal mt = " + element + " other = " + other;
         
        MarcSubField dublinCoreElementByName =  marcSubFieldDAO.findByUniqueName(element.getName());
        assert dublinCoreElementByName.equals(element) : "Marc  sub field should be found " + element; 
         
        marcSubFieldDAO.makeTransient(other);
        assert  marcSubFieldDAO.getById(other.getId(), false) == null : "Should no longer be able to find marc sub field";
	    tm.commit(ts);
	}

}
