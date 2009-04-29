package edu.ur.hibernate.ir.institution.db;

import java.util.List;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionDAO;
import edu.ur.ir.institution.InstitutionalCollectionSubscription;
import edu.ur.ir.institution.InstitutionalCollectionSubscriptionDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserManager;

/**
 * Class to test institutional collection subscription persistence.
 * 
 * @author Nathan Sarr
 *
 */
public class InstitutionalCollectionSubscriptionDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Institution collection data access.  */
	InstitutionalCollectionDAO institutionalCollectionDAO = (InstitutionalCollectionDAO) ctx
	.getBean("institutionalCollectionDAO");
	
	/** Institution collection data access.  */
	InstitutionalCollectionSubscriptionDAO institutionalCollectionSubscriptionDAO = (InstitutionalCollectionSubscriptionDAO) ctx
	.getBean("institutionalCollectionSubscriptionDAO");
	
	/** User service */
	 IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");
	
	
	/**
	 * Test Institutional Collection subscription persistence
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void collectionSubscriberDAOTest() throws DuplicateNameException, LocationAlreadyExistsException {

	    // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		//commit the transaction 
		// create a collection
		InstitutionalCollection collection = repo.createInstitutionalCollection("colName");
		collection.setDescription("colDescription");

		institutionalCollectionDAO.makePersistent(collection);
		
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		userDAO.makePersistent(user);
		
		tm.commit(ts);
		
		
		// start a new transaction
		ts = tm.getTransaction(td);
		collection = institutionalCollectionDAO.getById(collection.getId(), false);
		assert collection.getSubscriptions().size() == 0 : "Should have 0 subscriptions but has " + collection.getSubscriptions().size();
		
		InstitutionalCollectionSubscription subscription = collection.addSuscriber(user);
		institutionalCollectionSubscriptionDAO.makeTransient(subscription);
		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);
		collection = institutionalCollectionDAO.getById(collection.getId(), false);
		assert collection.getSubscriptions().size() == 1 : "Should have 1 subscriptions but has " + collection.getSubscriptions().size();

		assert subscription.getUser().equals(user) : "Collection subscription user should = " + user 
		+ " but = " + subscription.getUser();
		
		subscription = collection.getSubscription(user);
		assert subscription.getInstitutionalCollection().equals(collection) : "Collection subscription collection should = " + collection 
		+ " but = " + subscription.getInstitutionalCollection();
		
		assert collection.hasSubscriber(user) : " Collection should have user " + user;
		
		List<InstitutionalCollectionSubscription> subscriptions = institutionalCollectionSubscriptionDAO.getAllSubscriptionsForUser(user);
		
		assert subscriptions.size() == 1 : "Should have 1 subscription but has " + subscriptions.size();
		assert subscriptions.contains(subscription) : " Should contain subscription " + subscription;
		
		
		Long count = institutionalCollectionSubscriptionDAO.getSubscriberCount(collection);
		assert count == 1l : "Should have 1 subscriber but has " + count;
		
		
		count = institutionalCollectionSubscriptionDAO.isSubscriberCount(collection, user);
		assert count == 1l : "Should have 1 subscriber but has " + count;
		
		collection.removeSubscriber(user);
		institutionalCollectionSubscriptionDAO.makeTransient(subscription);
		tm.commit(ts);
		
			
		
  	    // start a new transaction
		ts = tm.getTransaction(td);
		
		collection = institutionalCollectionDAO.getById(collection.getId(), false);

		assert !collection.hasSubscriber(user) : " Collection should NOT have user " + user;
		// delete the institutional collection
		institutionalCollectionDAO.makeTransient(collection);
		repoHelper.cleanUpRepository();
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		

	}
	
	
	

}
