package edu.ur.hibernate.ir.person.db;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.person.PersonNameAuthorityIdentifierType;
import edu.ur.ir.person.PersonNameAuthorityIdentifierTypeDAO;

/**
 * Test the persistance methods for person name authority identifier type Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class PersonNameAuthorityIdentifierTypeDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	PersonNameAuthorityIdentifierTypeDAO identifierType = (PersonNameAuthorityIdentifierTypeDAO) ctx
	.getBean("personNameAuthorityIdentifierTypeDAO");

	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

	
	/**
	 * Test identifier type persistance
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseIdentifierTypeDAOTest() throws Exception{
		
		PersonNameAuthorityIdentifierType identType = new PersonNameAuthorityIdentifierType("identTypeName","identTypeDescription" );

        // Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
 		 identifierType.makePersistent(identType);
 		tm.commit(ts);
 		
 		ts = tm.getTransaction(td);
 		PersonNameAuthorityIdentifierType other = identifierType.getById(identType.getId(), false);
         assert other.equals(identType) : "Idententifier types should be equal";
         
         List<PersonNameAuthorityIdentifierType> itemIdentifierTypes =  identifierType.getAllOrderByName(0, 1);
         assert itemIdentifierTypes.size() == 1 : "One identifier type should be found";
         
         assert identifierType.getAllNameOrder().size() == 1 : "One identifier type should be found";
         
         PersonNameAuthorityIdentifierType itemIdentifierTypeByName =  identifierType.findByUniqueName(identType.getName());
         assert itemIdentifierTypeByName.equals(identType) : "Identifier types should be found";
         
         identifierType.makeTransient(other);
         assert  identifierType.getById(other.getId(), false) == null : 
        	 "Should no longer be able to find identifier type";
         tm.commit(ts);
	}

}
