package edu.ur.ir.user.service;

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
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupIndexService;
import edu.ur.ir.user.UserGroupSearchService;
import edu.ur.ir.user.UserGroupService;

/**
 * Testing for user group indexing.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class UserGroupIndexServiceTest {

	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** Repository data access */
	RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
    /** Collection service  */
    UserGroupService userGroupService = 
    	(UserGroupService) ctx.getBean("userGroupService");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** service to deal with searching institutional collection information */
	UserGroupSearchService userGroupSearchService = (UserGroupSearchService) ctx.getBean("userGroupSearchService");
	
	/** institutional collection index service */
	UserGroupIndexService userGroupIndexService = (UserGroupIndexService) ctx.getBean("userGroupIndexService");
	
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
		IrUserGroup userGroup = new IrUserGroup("nate group", "cool users");
		userGroupService.save(userGroup);
		
        // add the item to the index
		tm.commit(ts);
		
		// test searching for the data
		ts = tm.getTransaction(td);
        userGroupIndexService.add(userGroup, new File(repo.getUserGroupIndexFolder()));
	
		// search the document and make sure we can find the stored data
		try {
			SearchResults<IrUserGroup> results = 
				userGroupSearchService.search(new File(repo.getUserGroupIndexFolder()), "Nate Group", 0, 10);
			assert results.getObjects().size() == 1 : "Hit count should equal 1 but equals " + results.getObjects().size() 
			+ " for finding " + DefaultUserGroupIndexService.NAME;
			
			results = 
				userGroupSearchService.search(new File(repo.getUserGroupIndexFolder()), "cool users", 0, 10);
			assert results.getObjects().size() == 1 : "Hit count should equal 1 but equals " + results.getObjects().size() 
			+ " for finding " + DefaultUserGroupIndexService.DESCRIPTION;
			
		
		userGroupIndexService.delete(userGroup.getId(), new File(repo.getUserGroupIndexFolder()));

		results = 
			userGroupSearchService.search(new File(repo.getUserGroupIndexFolder()), "Nate Collection", 0, 10);
		assert results.getObjects().size() == 0 : "Hit count should equal 1 but equals " + results.getObjects().size() 
		+ " for finding " + DefaultUserGroupIndexService.NAME;
		
		results = 
			userGroupSearchService.search(new File(repo.getUserGroupIndexFolder()), "my Collection", 0, 10);
		assert results.getObjects().size() == 0 : "Hit count should equal 1 but equals " + results.getObjects().size() 
		+ " for finding " + DefaultUserGroupIndexService.DESCRIPTION;
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		tm.commit(ts);
		
	    // Start new transaction - clean up the data
		ts = tm.getTransaction(td);
		userGroupService.delete(userGroupService.get(userGroup.getId(), false));
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	
}
