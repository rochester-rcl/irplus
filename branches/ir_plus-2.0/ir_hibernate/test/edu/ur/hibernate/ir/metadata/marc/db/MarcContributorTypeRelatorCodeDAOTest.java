package edu.ur.hibernate.ir.metadata.marc.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.marc.MarcContributorTypeRelatorCode;
import edu.ur.ir.marc.MarcContributorTypeRelatorCodeDAO;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeDAO;
import edu.ur.metadata.marc.MarcRelatorCode;
import edu.ur.metadata.marc.MarcRelatorCodeService;

/**
 * Test the persistence of the contributor type relator code mapping.
 * 
 * @author Nathan Sarr
 *
 */
public class MarcContributorTypeRelatorCodeDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** dublin core mapping data access object */
	MarcContributorTypeRelatorCodeDAO marcContributorTypeRelatorCodeDAO = (MarcContributorTypeRelatorCodeDAO) ctx
	.getBean("marcContributorTypeRelatorCodeDAO");
  
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
    
    /** contributor type data access object */
    ContributorTypeDAO contentTypeDAO = (ContributorTypeDAO) ctx
	.getBean("contributorTypeDAO");
    
    MarcRelatorCodeService marcRelatorCodeService  = (MarcRelatorCodeService) ctx.getBean("marcRelatorCodeService");
    
	/**
	 * Test mapping persistence
	 */
	@Test
	public void baseContributorTypeRelatorCodeDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);
 		
        // create a identifier type
 		ContributorType ct = new ContributorType("ctName");
 		contentTypeDAO.makePersistent(ct);	
 	   
 	    MarcRelatorCode mrc = new MarcRelatorCode("atuhor", "aut");
 	    marcRelatorCodeService.save(mrc);
 	    
 	    // create the mapping
 		MarcContributorTypeRelatorCode mapper = new MarcContributorTypeRelatorCode(mrc, ct);
 		marcContributorTypeRelatorCodeDAO.makePersistent(mapper);
 	    
 	    tm.commit(ts);
 	   
 	    ts = tm.getTransaction(td);
 	    
 	    MarcContributorTypeRelatorCode other = marcContributorTypeRelatorCodeDAO.getById(mapper.getId(), false);
 	    assert other.equals(mapper) : " Other " + other + "\n should be equal to " + mapper;
 	    
 	    other =  marcContributorTypeRelatorCodeDAO.getByContributorType(ct.getId());
 	    assert other.equals(mapper) : " Other " + other + "\n should be equal to " + mapper;
 	    tm.commit(ts);
 	    
 	    
	    ts = tm.getTransaction(td);
        // delete data
	    marcContributorTypeRelatorCodeDAO.makeTransient(marcContributorTypeRelatorCodeDAO.getById(mapper.getId(), false));
	    contentTypeDAO.makeTransient(contentTypeDAO.getById(ct.getId(), false));
	    marcRelatorCodeService.delete(marcRelatorCodeService.getById(mrc.getId(), false));
	    tm.commit(ts);
	}

}
