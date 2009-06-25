/**  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  

package edu.ur.hibernate.ir.institution.db;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.dao.CriteriaHelper;
import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionDAO;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemDAO;
import edu.ur.ir.institution.VersionedInstitutionalItemDAO;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.item.VersionedItemDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.order.OrderType;

import java.text.SimpleDateFormat;

/**
 * Test the persistence methods for an institutional item Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class InstitutionalItemDAOTest {
	
	/**  Logger for add files to item action */
	private static final Logger log = Logger.getLogger(InstitutionalItemDAOTest.class);
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Institution collection data access.  */
	InstitutionalCollectionDAO institutionalCollectionDAO = (InstitutionalCollectionDAO) ctx
	.getBean("institutionalCollectionDAO");
	
	/** Institution item data access.  */
	InstitutionalItemDAO institutionalItemDAO = (InstitutionalItemDAO) ctx
	.getBean("institutionalItemDAO");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
    /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");

    /** Versioned item data access*/
    VersionedItemDAO versionedItemDAO = (VersionedItemDAO)ctx.getBean("versionedItemDAO");
    
    /** Versioned Institutional item data access*/
    VersionedInstitutionalItemDAO versionedInstitutionalItemDAO = (VersionedInstitutionalItemDAO)ctx.getBean("versionedInstitutionalItemDAO");
    
    /** Generic item data access*/
    GenericItemDAO itemDAO = (GenericItemDAO)ctx.getBean("itemDAO");
    
    /** Simple date format */
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");


	
	/**
	 * Test Institutional item persistence
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void baseInstitutionalItemDAOTest() throws DuplicateNameException, LocationAlreadyExistsException {

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
		InstitutionalCollection col = repo.createInstitutionalCollection("colName");
		col.setDescription("colDescription");
		
		institutionalCollectionDAO.makePersistent(col);
		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);
		
		UserEmail userEmail = new UserEmail("email");
				
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);

        userDAO.makePersistent(user);
		col = institutionalCollectionDAO.getById(col.getId(), false);
		GenericItem genericItem = new GenericItem("genericItem");
		
		InstitutionalItem institutionalItem = col.createInstitutionalItem(genericItem);
		institutionalItemDAO.makePersistent(institutionalItem);

		tm.commit(ts);

		ts = tm.getTransaction(td);
		assert institutionalItemDAO.getById(institutionalItem.getId(), false).equals(institutionalItem) :
			"Should be able to find item " + institutionalItem;
		
		
		// check selects
		
		
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		
		tm.commit(ts);	
		
		assert institutionalItemDAO.getById(institutionalItem.getId(), false) == null : 
			"Should not be able to find the insitutional item" + institutionalItem;
	}
	
	/**
	 * Test Institutional item searching
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void institutionalItemSearchDAOTest() throws DuplicateNameException, LocationAlreadyExistsException {

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
		InstitutionalCollection col = repo.createInstitutionalCollection("colName");
		col.setDescription("colDescription");
		
		institutionalCollectionDAO.makePersistent(col);
		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);
		
		UserEmail userEmail = new UserEmail("email");
				
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);

        userDAO.makePersistent(user);
		col = institutionalCollectionDAO.getById(col.getId(), false);
		GenericItem genericItem = new GenericItem("genericItem");
		
		InstitutionalItem institutionalItem = col.createInstitutionalItem(genericItem);
		institutionalItemDAO.makePersistent(institutionalItem);
		
		GenericItem genericItem2 = new GenericItem("hGenericItem2");
		InstitutionalItem institutionalItem2 = col.createInstitutionalItem(genericItem2);
		institutionalItemDAO.makePersistent(institutionalItem2);

		tm.commit(ts);

		ts = tm.getTransaction(td);
		
		InstitutionalItem other =  institutionalItemDAO.getById(institutionalItem.getId(), false);
		assert other.equals(institutionalItem) :
			"other " + other + " \n Should be equal to  \n" + institutionalItem;
		
		InstitutionalItem other2 = institutionalItemDAO.getById(institutionalItem2.getId(), false);
		assert other2.equals(institutionalItem2) :
			"other 2 \n" + other2 + " \n Should be able to find item \n" + institutionalItem2;
		
		assert institutionalItemDAO.getCount() == 2 : " count should equal 2 but equals " + institutionalItemDAO.getCount();
		assert institutionalItemDAO.getCount(repo.getId()) == 2 : " count should equal 2 but equals " + institutionalItemDAO.getCount(repo.getId());
		
		List<InstitutionalItem> items = institutionalItemDAO.getRepositoryItemsByName(0, 100, repo.getId(), OrderType.ASCENDING_ORDER);
		assert items.size() == 2;
		
		assert items.get(0).equals(other) : " item at index 0 should equal " + other + 
		" \n but equals " + items.get(0);
		
		assert items.get(1).equals(other2) : " item at index 0 should equal " + other2 + 
		" \n but equals " + items.get(1);
		
		//reverse the sort
		items = institutionalItemDAO.getRepositoryItemsByName(0, 100, repo.getId(), OrderType.DESCENDING_ORDER);
		assert items.size() == 2;
		
		assert items.get(0).equals(other2) : " item at index 0 should equal " + other2 + 
		" \n but equals " + items.get(0);
		
		assert items.get(1).equals(other) : " item at index 0 should equal " + other + 
		" \n but equals " + items.get(1);
		
		CriteriaHelper helper = new CriteriaHelper("name");
		helper.addAssocationPathElement("versionedItem");
		
		
		assert institutionalItemDAO.getCount(repo.getId(), 'H') == 1 : "Count should equal one but equals " + institutionalItemDAO.getCount(repo.getId(), 'A');
		
		items = institutionalItemDAO.getRepositoryItemsByChar(0, 100, repo.getId(), 'G', OrderType.ASCENDING_ORDER);
		assert items.size() == 1 : "Should have one item but have " + items.size();
		
		items = institutionalItemDAO.getRepositoryItemsByChar(0, 100, repo.getId(), 'h', OrderType.ASCENDING_ORDER);
		assert items.size() == 1 : "Should have one item but have " + items.size();
		
		
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItem2.getId(), false));

		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		
		tm.commit(ts);	
		
		assert institutionalItemDAO.getById(institutionalItem.getId(), false) == null : 
			"Should not be able to find the insitutional item" + institutionalItem;
	}
	
	/**
	 * Test Institutional item searching
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void institutionalItemSearchisPublishedDAOTest() throws DuplicateNameException, LocationAlreadyExistsException {

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
		InstitutionalCollection col = repo.createInstitutionalCollection("colName");
		col.setDescription("colDescription");
		
		institutionalCollectionDAO.makePersistent(col);
		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);
		
		UserEmail userEmail = new UserEmail("email");
				
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);

        userDAO.makePersistent(user);
		col = institutionalCollectionDAO.getById(col.getId(), false);
		GenericItem genericItem = new GenericItem("genericItem");
		
		InstitutionalItem institutionalItem = col.createInstitutionalItem(genericItem);
		institutionalItemDAO.makePersistent(institutionalItem);

		tm.commit(ts);

		ts = tm.getTransaction(td);
		
		assert institutionalItemDAO.isItemPublishedToCollection(col.getId(), genericItem.getId()) : 
			"Generic item " + genericItem + " should be published";
		

		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));

		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		
		tm.commit(ts);	
		
		assert institutionalItemDAO.getById(institutionalItem.getId(), false) == null : 
			"Should not be able to find the insitutional item" + institutionalItem;
	}


	/**
	 * Test for counting the Institutional item in a collection and its children
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void itemCountTest() throws DuplicateNameException, LocationAlreadyExistsException {

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
		InstitutionalCollection col = repo.createInstitutionalCollection("colName");
		col.setDescription("colDescription");
		
		// add the children
		InstitutionalCollection childCol1 = col.createChild("child1");
		
		institutionalCollectionDAO.makePersistent(col);
		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);
		
		UserEmail userEmail = new UserEmail("email");
				
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);

        userDAO.makePersistent(user);
		col = institutionalCollectionDAO.getById(col.getId(), false);
		GenericItem genericItem = new GenericItem("genericItem");
		GenericItem genericItem2 = new GenericItem("genericItem2");
		
		InstitutionalItem institutionalItem = col.createInstitutionalItem(genericItem);
		institutionalItemDAO.makePersistent(institutionalItem);

		InstitutionalItem institutionalItem2 = childCol1.createInstitutionalItem(genericItem2);
		institutionalItemDAO.makePersistent(institutionalItem2);
		tm.commit(ts);

		ts = tm.getTransaction(td);
		Long count = institutionalItemDAO.getCountForCollectionAndChildren(col);
		assert count == 2l :
			"Should be equal to 2 but is " + count;
		
		List<Long> itemIds = institutionalItemDAO.getCollectionItemsIds(0, 100, col, OrderType.DESCENDING_ORDER);
		
		assert itemIds.size() == 2 : "Size should be two but is " + itemIds.size();
		assert itemIds.contains(institutionalItem.getId()) : "Should contain " + genericItem.getId();
		
		itemIds = institutionalItemDAO.getCollectionItemsIds(0, 100, col, OrderType.ASCENDING_ORDER);
		assert itemIds.size() == 2 : "Size should be two but is " + itemIds.size();
		
		assert itemIds.contains(institutionalItem.getId()) : "Should contain " + genericItem.getId();
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);

		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(childCol1.getId(), false));
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		
		
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItem2.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		
		tm.commit(ts);	
		
		assert institutionalItemDAO.getById(institutionalItem.getId(), false) == null : 
			"Should not be able to find the insitutional item" + institutionalItem;
	}
	
	
	/**
	 * Test Institutional item searching
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void institutionalItemBySubmittedDateDAOTest() throws DuplicateNameException, LocationAlreadyExistsException {

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
		InstitutionalCollection col = repo.createInstitutionalCollection("colName");
		col.setDescription("colDescription");
		
		institutionalCollectionDAO.makePersistent(col);
		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);
		
		UserEmail userEmail = new UserEmail("email");
				
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);

        userDAO.makePersistent(user);
		col = institutionalCollectionDAO.getById(col.getId(), false);
		GenericItem genericItem = new GenericItem("genericItem");
		
		InstitutionalItem institutionalItem = col.createInstitutionalItem(genericItem);
		institutionalItemDAO.makePersistent(institutionalItem);
		
		GenericItem genericItem2 = new GenericItem("hGenericItem2");
		InstitutionalItem institutionalItem2 = col.createInstitutionalItem(genericItem2);
		institutionalItemDAO.makePersistent(institutionalItem2);


		tm.commit(ts);

		ts = tm.getTransaction(td);
		
		/* get the calendar and subtract one day*/
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getDateOfDeposit());
		calendar.add(Calendar.SECOND, -1);
		Date startDate = calendar.getTime();
		
		
		calendar.setTime(institutionalItem2.getVersionedInstitutionalItem().getCurrentVersion().getDateOfDeposit());
		calendar.add(Calendar.SECOND, 1);
		Date endDate = calendar.getTime();
		
		
		List<InstitutionalItem> institutionalItems = institutionalItemDAO.getItems(col, startDate, endDate);
		assert institutionalItems.size() == 2 : "Should find two items but found " + institutionalItems.size() 
		+ " for dates " + startDate + " to " + endDate;
		
		institutionalItems = institutionalItemDAO.getItemsOrderByDate(0, 1, col, OrderType.DESCENDING_ORDER);
        assert institutionalItems.size() == 1 : "Should have found 1 item but found " + institutionalItems.size();
		
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItem2.getId(), false));

		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		
		tm.commit(ts);	
		
		assert institutionalItemDAO.getById(institutionalItem.getId(), false) == null : 
			"Should not be able to find the insitutional item" + institutionalItem;
	}


}
