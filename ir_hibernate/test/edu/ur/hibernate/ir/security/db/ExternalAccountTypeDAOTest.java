package edu.ur.hibernate.ir.security.db;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.security.ExternalAccountType;
import edu.ur.ir.security.ExternalAccountTypeDAO;

/**
 * Test the persistence methods for external account types.
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ExternalAccountTypeDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	ExternalAccountTypeDAO externalAccountTypeDAO = (ExternalAccountTypeDAO) ctx
	.getBean("externalAccountTypeDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test Contributor type persistance
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseExternalAccountTypeDAOTest() throws Exception{

		
		ExternalAccountType externalAccountType = new ExternalAccountType("externalAccountTypeName");
		externalAccountType.setDescription("description");
         
        TransactionStatus ts = tm.getTransaction(td);
        externalAccountTypeDAO.makePersistent(externalAccountType);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 	    ExternalAccountType other = externalAccountTypeDAO.getById(externalAccountType.getId(), false);
        assert other.equals(externalAccountType) : "External account types should be equal";
         
        List<ExternalAccountType> externalAccountTypes =  externalAccountTypeDAO.getAllOrderByName(0, 1);
        assert externalAccountTypes.size() == 1 : "One external account type should be found";
        assert externalAccountTypeDAO.getAllNameOrder().size() == 1 : "One external account type should be found";
         
        ExternalAccountType externalAccountTypeByName =  externalAccountTypeDAO.findByUniqueName(externalAccountType.getName());
        assert externalAccountTypeByName.equals(externalAccountType) : "External type should be found " + externalAccountType;
         
        externalAccountTypeDAO.makeTransient(other);
        assert  externalAccountTypeDAO.getById(other.getId(), false) == null : "Should no longer be able to find content type";
	    tm.commit(ts);
	}

}
