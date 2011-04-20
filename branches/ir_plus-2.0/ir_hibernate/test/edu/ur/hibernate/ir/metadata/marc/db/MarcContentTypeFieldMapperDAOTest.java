package edu.ur.hibernate.ir.metadata.marc.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeDAO;

import edu.ur.ir.marc.MarcContentTypeFieldMapper;
import edu.ur.ir.marc.MarcContentTypeFieldMapperDAO;

/**
 * Test for the MarcContentTypeFieldMapperDAO
 * 
 * @author Nathan Sarr
 *
 */
public class MarcContentTypeFieldMapperDAOTest  {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** dublin core mapping data access object */
	MarcContentTypeFieldMapperDAO marcContentTypeFieldMapperDAO = (MarcContentTypeFieldMapperDAO) ctx
	.getBean("marcContentTypeFieldMapperDAO");

  
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
    
    /** content type data access object */
    ContentTypeDAO contentTypeDAO = (ContentTypeDAO) ctx
	.getBean("contentTypeDAO");
    
	/**
	 * Test mapping persistence
	 */
	@Test
	public void baseMarcContentTypeFiledMapperDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);
 		
        // create a identifier type
 		ContentType ct = new ContentType("ctName", "ctDescription");
 		contentTypeDAO.makePersistent(ct);	
 	   
 	    
 	    // create the mapping
 		MarcContentTypeFieldMapper mapper = new MarcContentTypeFieldMapper(ct);
 		marcContentTypeFieldMapperDAO.makePersistent(mapper);
 	    
 	    tm.commit(ts);
 	   
 	    ts = tm.getTransaction(td);
 	    
 	    MarcContentTypeFieldMapper other = marcContentTypeFieldMapperDAO.getById(mapper.getId(), false);
 	    assert other.equals(mapper) : " Other " + other + "\n should be equal to " + mapper;
 	    
 	    other =  marcContentTypeFieldMapperDAO.getByContentTypeId(ct.getId());
 	    assert other.equals(mapper) : " Other " + other + "\n should be equal to " + mapper;
 	    tm.commit(ts);
 	    
 	    
	    ts = tm.getTransaction(td);
        // delete data
	    marcContentTypeFieldMapperDAO.makeTransient(marcContentTypeFieldMapperDAO.getById(mapper.getId(), false));
	    contentTypeDAO.makeTransient(contentTypeDAO.getById(ct.getId(), false));
	    tm.commit(ts);
	}

}
