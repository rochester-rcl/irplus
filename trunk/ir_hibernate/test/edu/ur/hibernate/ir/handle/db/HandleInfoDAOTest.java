package edu.ur.hibernate.ir.handle.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.handle.HandleInfoDAO;
import edu.ur.ir.handle.HandleNameAuthority;
import edu.ur.ir.handle.HandleNameAuthorityDAO;

/**
 * Class for testing handle name authority persistence.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class HandleInfoDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** used to store handle name authority data */
	HandleNameAuthorityDAO handleNameAuthorityDAO = (HandleNameAuthorityDAO) ctx
	.getBean("handleNameAuthorityDAO");
	
	/** used to store handle name authority data */
	HandleInfoDAO handleInfoDAO = (HandleInfoDAO) ctx
	.getBean("handleInfoDAO");
	
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
		
		HandleInfo info = new HandleInfo("1234", "http://www.google.com", handleNameAuthority);
 		
        TransactionStatus ts = tm.getTransaction(td);
        handleNameAuthorityDAO.makePersistent(handleNameAuthority);
        handleInfoDAO.makePersistent(info);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 	    HandleInfo other = handleInfoDAO.getById(info.getId(), false);
        assert other.equals(info) : "handle info's should be equal other = " + 
        other + " handle info = " + info;
        tm.commit(ts);
        
        ts = tm.getTransaction(td);
        handleInfoDAO.makeTransient(handleInfoDAO.getById(info.getId(), false));
        handleNameAuthorityDAO.makeTransient(handleNameAuthorityDAO.getById(handleNameAuthority.getId(), false));
        assert  handleInfoDAO.getById(other.getId(), false) == null : "Should no longer be able to find handle info";
	    tm.commit(ts);
	}

}