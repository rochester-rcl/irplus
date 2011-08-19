package edu.ur.ir.institution.service;

import java.io.File;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;


import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.SearchResults;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionIndexService;
import edu.ur.ir.institution.InstitutionalCollectionSearchService;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;

/**
 * Test the institutional collection index service.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultInstitutionalCollectionIndexServiceTest {
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** Repository data access */
	RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
    /** Collection service  */
    InstitutionalCollectionService institutionalCollectionService = 
    	(InstitutionalCollectionService) ctx.getBean("institutionalCollectionService");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** service to deal with searching institutional collection information */
	InstitutionalCollectionSearchService institutionalCollectionSearchService = (InstitutionalCollectionSearchService) ctx.getBean("institutionalCollectionSearchService");
	
	/** institutional collection index service */
	InstitutionalCollectionIndexService institutionalCollectionIndexService = (InstitutionalCollectionIndexService) ctx.getBean("institutionalCollectionIndexService");
	
	/**
	 * Test indexing an institutional item
	 * @throws LocationAlreadyExistsException 
	 * 
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 * @throws NoIndexFoundException 
	 */
	public void testIndexInstitutionalCollection() throws LocationAlreadyExistsException, DuplicateNameException, NoIndexFoundException{
		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("nate collection");
		collection.setDescription("my description");
		institutionalCollectionService.saveCollection(collection);
		
        // add the item to the index
		tm.commit(ts);
		
		// test searching for the data
		ts = tm.getTransaction(td);
        institutionalCollectionIndexService.add(collection, new File(repo.getInstitutionalCollectionIndexFolder()));
	
		// search the document and make sure we can find the stored data
		try {
			SearchResults<InstitutionalCollection> results = 
				institutionalCollectionSearchService.search(new File(repo.getInstitutionalCollectionIndexFolder()), "Nate Collection", 0, 10);
			assert results.getObjects().size() == 1 : "Hit count should equal 1 but equals " + results.getObjects().size() 
			+ " for finding " + DefaultInstitutionalCollectionIndexService.NAME;
			
			results = 
				institutionalCollectionSearchService.search(new File(repo.getInstitutionalCollectionIndexFolder()), "my description", 0, 10);
			assert results.getObjects().size() == 1 : "Hit count should equal 1 but equals " + results.getObjects().size() 
			+ " for finding " + DefaultInstitutionalCollectionIndexService.DESCRIPTION;
			
		
		institutionalCollectionIndexService.delete(collection.getId(), new File(repo.getInstitutionalCollectionIndexFolder()));

		results = 
			institutionalCollectionSearchService.search(new File(repo.getInstitutionalCollectionIndexFolder()), "Nate Collection", 0, 10);
		assert results.getObjects().size() == 0 : "Hit count should equal 1 but equals " + results.getObjects().size() 
		+ " for finding " + DefaultInstitutionalCollectionIndexService.NAME;
		
		results = 
			institutionalCollectionSearchService.search(new File(repo.getInstitutionalCollectionIndexFolder()), "my Collection", 0, 10);
		assert results.getObjects().size() == 0 : "Hit count should equal 1 but equals " + results.getObjects().size() 
		+ " for finding " + DefaultInstitutionalCollectionIndexService.DESCRIPTION;
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		tm.commit(ts);
		
	    // Start new transaction - clean up the data
		ts = tm.getTransaction(td);
		helper.cleanUpRepository();
		tm.commit(ts);	
	}

}
