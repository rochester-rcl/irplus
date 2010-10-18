package edu.ur.ir.handle.service;

import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.handle.HandleService;
import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.handle.HandleNameAuthority;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;

/**
 * Test the service methods for the handle services.
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class HandleServiceTest {

	/** Spring application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
    /** Service for dealing with handles  */
    HandleService handleService = (HandleService) ctx.getBean("handleService");
  
	/** transaction manager for dealing with transactions  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	/** the transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	

	
	/**
	 * Test creating the default groups
	 * @throws DuplicateNameException 
	 */
	public void getHandleByAuthorityLocalNameTest() throws DuplicateNameException
	{
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);

		HandleNameAuthority authority = new HandleNameAuthority("1802");
		HandleInfo handleInfo = new HandleInfo("12345", "http://www.google.com", authority);
		handleService.save(handleInfo);
		
		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);
		HandleInfo other = handleService.getHandleInfo(handleInfo.getId(), false);
		assert other != null : " other should be found for id " + handleInfo;
		
		tm.commit(ts);
		
		
		
		
		ts = tm.getTransaction(td);
		HandleInfo other2 = handleService.getHandleInfo("1802/12345");
		assert other2 != null : "Other 2 should be found with 1802/12345";
		
		handleService.delete(handleService.getHandleInfo(handleInfo.getId(), false));
		handleService.delete(handleService.getNameAuthority(authority.getId(), false));
		
		tm.commit(ts);
	}
}
