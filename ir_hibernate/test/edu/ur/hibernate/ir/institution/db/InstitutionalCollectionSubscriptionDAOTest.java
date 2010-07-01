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
		institutionalCollectionSubscriptionDAO.makePersistent(subscription);
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
		
		subscription = institutionalCollectionSubscriptionDAO.getSubscriptionForUser(collection, user);
		assert subscription != null : "Subscription should not be null";
		assert subscription.getUser().equals(user) : "subscription user " + subscription.getUser() + " should equal " + user;
		assert subscription.getInstitutionalCollection().equals(collection) : " subscription collection " + subscription.getInstitutionalCollection() + " should equal " + subscription.getInstitutionalCollection();
		
		
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
	
	/**
	 * Test getting unique subscribers
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void collectionSubscriberUniqueUserIdDAOTest() throws DuplicateNameException, LocationAlreadyExistsException {

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
		InstitutionalCollection collection1 = repo.createInstitutionalCollection("colName1");
		institutionalCollectionDAO.makePersistent(collection1);

		InstitutionalCollection collection2 = repo.createInstitutionalCollection("colName2");
		institutionalCollectionDAO.makePersistent(collection2);
		
		InstitutionalCollection collection3 = repo.createInstitutionalCollection("colName3");
		institutionalCollectionDAO.makePersistent(collection3);


		
  		UserManager userManager = new UserManager();
		IrUser user1 = userManager.createUser("passowrd1", "userName1");
		userDAO.makePersistent(user1);
		
		IrUser user2 = userManager.createUser("passowrd2", "userName2");
		userDAO.makePersistent(user2);
		
		IrUser user3 = userManager.createUser("passowrd3", "userName3");
		userDAO.makePersistent(user3);
		
		tm.commit(ts);
		
		
		// start a new transaction
		ts = tm.getTransaction(td);
		collection1 = institutionalCollectionDAO.getById(collection1.getId(), false);
		assert collection1.getSubscriptions().size() == 0 : "Should have 0 subscriptions but has " + collection1.getSubscriptions().size();
		
		InstitutionalCollectionSubscription subscription1 = collection1.addSuscriber(user1);
		InstitutionalCollectionSubscription subscription2 = collection2.addSuscriber(user1);
		InstitutionalCollectionSubscription subscription3 = collection2.addSuscriber(user2);
		InstitutionalCollectionSubscription subscription4 = collection3.addSuscriber(user3);
		InstitutionalCollectionSubscription subscription5 = collection3.addSuscriber(user2);
		institutionalCollectionSubscriptionDAO.makePersistent(subscription1);
		institutionalCollectionSubscriptionDAO.makePersistent(subscription2);
		institutionalCollectionSubscriptionDAO.makePersistent(subscription3);
		institutionalCollectionSubscriptionDAO.makePersistent(subscription4);
		institutionalCollectionSubscriptionDAO.makePersistent(subscription5);
		tm.commit(ts);
		
		
		
		// start a new transaction
		ts = tm.getTransaction(td);
		collection1 = institutionalCollectionDAO.getById(collection1.getId(), false);
		collection2 = institutionalCollectionDAO.getById(collection2.getId(), false);
		collection3 = institutionalCollectionDAO.getById(collection3.getId(), false);
		assert collection1.getSubscriptions().size() == 1 : "Should have 1 subscriptions but has " + collection1.getSubscriptions().size();
		assert collection2.getSubscriptions().size() == 2 : "Should have 2 subscriptions but has " + collection2.getSubscriptions().size();
		assert collection3.getSubscriptions().size() == 2 : "Should have 2 subscriptions but has " + collection3.getSubscriptions().size();

	
		List<Long> uniqueUserIds = institutionalCollectionSubscriptionDAO.getUniqueSubsciberUserIds();
		assert uniqueUserIds.size() == 3 : "Should find 3 user id's but found " + uniqueUserIds.size();
		assert uniqueUserIds.contains(user1.getId()) : "Should have user 1 id ";
		assert uniqueUserIds.contains(user2.getId()) : "Should have user 2 id ";
		assert uniqueUserIds.contains(user3.getId()) : "Should have user 3 id ";

		
		collection1.removeSubscriber(user1);
		collection2.removeSubscriber(user1);
		collection2.removeSubscriber(user2);
		collection3.removeSubscriber(user2);
		collection3.removeSubscriber(user3);
		
		institutionalCollectionSubscriptionDAO.makeTransient(institutionalCollectionSubscriptionDAO.getById(subscription1.getId(), false));
		institutionalCollectionSubscriptionDAO.makeTransient(institutionalCollectionSubscriptionDAO.getById(subscription2.getId(), false));
		institutionalCollectionSubscriptionDAO.makeTransient(institutionalCollectionSubscriptionDAO.getById(subscription3.getId(), false));
		institutionalCollectionSubscriptionDAO.makeTransient(institutionalCollectionSubscriptionDAO.getById(subscription4.getId(), false));
		institutionalCollectionSubscriptionDAO.makeTransient(institutionalCollectionSubscriptionDAO.getById(subscription5.getId(), false));
		tm.commit(ts);
		
			
		
  	    // start a new transaction
		ts = tm.getTransaction(td);
		
		// delete the institutional collection
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(collection1.getId(), false));
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(collection2.getId(), false));
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(collection3.getId(), false));

		repoHelper.cleanUpRepository();
		userDAO.makeTransient(userDAO.getById(user1.getId(), false));
		userDAO.makeTransient(userDAO.getById(user2.getId(), false));
		userDAO.makeTransient(userDAO.getById(user3.getId(), false));
		tm.commit(ts);
		

	}
	
	
	

}
