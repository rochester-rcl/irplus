package edu.ur.ir.person.service;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.ir.person.PersonNameAuthorityIdentifierType;
import edu.ur.ir.repository.service.test.helper.ContextHolder;

@Test(groups = { "baseTests" }, enabled = true)
public class DefaultPersonNameAuthorityIdentifierTypeServiceTest {
	
ApplicationContext ctx =  ContextHolder.getApplicationContext();
 	
	// Check the repositories
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	
	/** contributor type data access */
	DefaultPersonNameAuthorityIdentifierTypeService personNameAuthorityIndentiferTypeService = (DefaultPersonNameAuthorityIdentifierTypeService) ctx
	.getBean("defaultPersonNameAuthorityIdentifierTypeService");
	
	/**
	 * Test getting contributor information
	 */
	public void testGetPersonNameAuthorityIndentiferType()
	{
		TransactionStatus ts = tm.getTransaction(td);
	    // create a contributor type
		PersonNameAuthorityIdentifierType ident1 = new PersonNameAuthorityIdentifierType("ORCID");
		ident1.setUniqueSystemCode("orcid");
		personNameAuthorityIndentiferTypeService.save(ident1);
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		
		PersonNameAuthorityIdentifierType other = personNameAuthorityIndentiferTypeService.getByUniqueSystemCode(ident1.getUniqueSystemCode());
		assert other.equals(ident1) : " other = " + other + 
		" should equalcontributorType 1 = " + ident1;
		
		personNameAuthorityIndentiferTypeService.delete(other);
		PersonNameAuthorityIdentifierType deleted = personNameAuthorityIndentiferTypeService.getByUniqueSystemCode(ident1.getUniqueSystemCode());
		assert(deleted == null);
 	
		tm.commit(ts);
	}


}
