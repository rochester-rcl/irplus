package edu.ur.hibernate.metadata.marc;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.metadata.helper.test.ContextHolder;
import edu.ur.metadata.marc.MarcRelatorCode;
import edu.ur.metadata.marc.MarcRelatorCodeDAO;

public class MarcRelatorCodeDAOTest {
	
    /** spring application context manager  */
    ApplicationContext ctx = ContextHolder.getApplicationContext();
    
    /** dublin core element data access object */
    MarcRelatorCodeDAO marcRelatorCodeDAO = (MarcRelatorCodeDAO)ctx.getBean("marcRelatorCodeDAO");
    
    /** platform transaction manager  */
    PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
    
    /** transaction definition */
    TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
    
	/**
	 * Test dublin core term persistence
	 */
	@Test
	public void baseMarcRelatorCodeDAOTest() throws Exception{

		MarcRelatorCode element = new MarcRelatorCode("name", "relator code" );
 		element.setDescription("mdDescription");
         
        TransactionStatus ts = tm.getTransaction(td);
 		marcRelatorCodeDAO.makePersistent(element);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		MarcRelatorCode other = marcRelatorCodeDAO.getById(element.getId(), false);
        assert other.equals(element) : "Marc relator code should be equal mt = " + element + " other = " + other;
         
        MarcRelatorCode relatorCodeByName =  marcRelatorCodeDAO.findByUniqueName(element.getName());
        assert relatorCodeByName.equals(element) : "Marc relator code should be found " + element; 
        
        MarcRelatorCode relatorCodeByRelatorCode =  marcRelatorCodeDAO.getByRelatorCode(element.getRelatorCode());
        assert relatorCodeByRelatorCode.equals(element) : "Marc relator code should be found " + element; 
         
        marcRelatorCodeDAO.makeTransient(other);
        assert  marcRelatorCodeDAO.getById(other.getId(), false) == null : "Should no longer be able to find marc relator code";
	    tm.commit(ts);
	}

}
