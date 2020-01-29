package edu.ur.hibernate.ir.person.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonNameAuthorityDAO;
import edu.ur.ir.person.PersonNameAuthorityIdentifier;
import edu.ur.ir.person.PersonNameAuthorityIdentifierDAO;
import edu.ur.ir.person.PersonNameAuthorityIdentifierType;
import edu.ur.ir.person.PersonNameAuthorityIdentifierTypeDAO;

@Test(groups = { "baseTests" }, enabled = true)
public class PersonNameAuthorityIdentifierDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
			
	/** Identifier relational data access */
	PersonNameAuthorityIdentifierTypeDAO identifierTypeDAO = (PersonNameAuthorityIdentifierTypeDAO) ctx.getBean("personNameAuthorityIdentifierTypeDAO");
			
	/** Item identifier relatioanal data access. */
	PersonNameAuthorityIdentifierDAO personNameAuthorityIdentifierDAO = (PersonNameAuthorityIdentifierDAO) ctx.getBean("personNameAuthorityIdentifierDAO");		
	
	/**Person name authority relational data access. */
	PersonNameAuthorityDAO personNameAuthorityDAO = (PersonNameAuthorityDAO) ctx.getBean("personNameAuthorityDAO");

	/** Platform transaction manager.  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
			.getBean("transactionManager");

	/** transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test Contributor type persistence
	 */
	@Test
	public void basePersonNameAuthorityIdentifierDAOTest() throws Exception{

		PersonNameAuthorityIdentifierType identType = new PersonNameAuthorityIdentifierType("identTypeNameB","identTypeDescription" );
 		identifierTypeDAO.makePersistent(identType);

		TransactionStatus ts = tm.getTransaction(td);
		PersonName name = new PersonName();
		name.setFamilyName("familyName");
		name.setForename("forename");
		name.setInitials("n.d.s.");
		name.setMiddleName("MiddleName");
		name.setNumeration("III");
		name.setSurname("surname");
		
		PersonNameAuthority p = new PersonNameAuthority(name);
		personNameAuthorityDAO.makePersistent(p);
		
        //commit the transaction 
		tm.commit(ts);
		
		PersonNameAuthorityIdentifier identifier = p.addIdentifier("333-44-7856", identType);
		personNameAuthorityIdentifierDAO.makePersistent(identifier);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		assert personNameAuthorityIdentifierDAO.getById(identifier.getId(), false).equals(identifier) : "item identifier should be found";
		p.removeIdentifier(identifier);
        
        tm.commit(ts);
        
        ts = tm.getTransaction(td);
        personNameAuthorityIdentifierDAO.makeTransient(identifier);
        assert personNameAuthorityIdentifierDAO.getById(identifier.getId(), false) == null : "Should not find the item identifier"; 
        
        identifierTypeDAO.makeTransient(identifierTypeDAO.getById(identType.getId(), false));
        
		// delete the item
        personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p.getId(), false));
        tm.commit(ts);
	}
	
	

}
