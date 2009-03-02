package edu.ur.hibernate.ir.handle.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.handle.HandleNameAuthority;
import edu.ur.ir.handle.HandleNameAuthorityDAO;


/**
 * Class for testing handle name authority persistence.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class HandleNameAuthorityDAOTest 
{
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** used to store handle name authority data */
	HandleNameAuthorityDAO handleNameAuthorityDAO = (HandleNameAuthorityDAO) ctx
	.getBean("handleNameAuthorityDAO");
	
	/** transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test basic persistance.
	 * 
	 * @throws Exception
	 */
	public void baseHandleNameAuthorityDAOTest() throws Exception
	{
		HandleNameAuthority handleNameAuthority = new HandleNameAuthority("0.NA", "12345678");
 		
        TransactionStatus ts = tm.getTransaction(td);
        handleNameAuthorityDAO.makePersistent(handleNameAuthority);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 	    HandleNameAuthority other = handleNameAuthorityDAO.getById(handleNameAuthority.getId(), false);
        assert other.equals(handleNameAuthority) : "Authorities should be equal other = " + 
        other + " handleNameAuthority = " + handleNameAuthority;
         
  
        handleNameAuthorityDAO.makeTransient(other);
        assert  handleNameAuthorityDAO.getById(other.getId(), false) == null : "Should no longer be able to find handle naming authority";
	    tm.commit(ts);
	}

}
