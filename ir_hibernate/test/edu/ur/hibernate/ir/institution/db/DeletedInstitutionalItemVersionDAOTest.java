/**  
   Copyright 2008-2010 University of Rochester

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
import edu.ur.ir.institution.CollectionDoesNotAcceptItemsException;
import edu.ur.ir.institution.DeletedInstitutionalItem;
import edu.ur.ir.institution.DeletedInstitutionalItemDAO;
import edu.ur.ir.institution.DeletedInstitutionalItemVersion;
import edu.ur.ir.institution.DeletedInstitutionalItemVersionDAO;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionDAO;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemDAO;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;

/**
 * Test the persistence of deleted insitutional item versions.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DeletedInstitutionalItemVersionDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

	/** Institution item data access.  */
	InstitutionalItemDAO institutionalItemDAO = (InstitutionalItemDAO) ctx
	.getBean("institutionalItemDAO");
	
	  /** Generic item data access*/
    GenericItemDAO itemDAO = (GenericItemDAO)ctx.getBean("itemDAO");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
    /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");

    /** Deleted Institutional Item data access*/
    DeletedInstitutionalItemDAO deletedInstitutionalItemDAO = (DeletedInstitutionalItemDAO)ctx.getBean("deletedInstitutionalItemDAO");

    /** Deleted Institutional Item data access*/
    DeletedInstitutionalItemVersionDAO deletedInstitutionalItemVersionDAO = (DeletedInstitutionalItemVersionDAO)ctx.getBean("deletedInstitutionalItemVersionDAO");

    
	/** Institution collection data access.  */
	InstitutionalCollectionDAO institutionalCollectionDAO = (InstitutionalCollectionDAO) ctx
	.getBean("institutionalCollectionDAO");

	
	/**
	 * Test deleted Institutional item persistence
	 * @throws CollectionDoesNotAcceptItemsException 
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 * 
	 */
	@Test
	public void baseDeletedInstitutionalItemVersionDAOTest() throws CollectionDoesNotAcceptItemsException, LocationAlreadyExistsException, DuplicateNameException {

	    // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		UserEmail userEmail = new UserEmail("email");
				
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		
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

        userDAO.makePersistent(user);
        
		col = institutionalCollectionDAO.getById(col.getId(), false);
		GenericItem genericItem = new GenericItem("genericItem");
		
		InstitutionalItem institutionalItem = col.createInstitutionalItem(genericItem);
		institutionalItemDAO.makePersistent(institutionalItem);
        
        tm.commit(ts);
        
        
        ts = tm.getTransaction(td);
        
        DeletedInstitutionalItem deletedInstitutionalItem = new DeletedInstitutionalItem(institutionalItem);
        deletedInstitutionalItem.setDeletedBy(user);
        deletedInstitutionalItem.setDeletedDate(new Date());
		deletedInstitutionalItemDAO.makePersistent(deletedInstitutionalItem);
		Set<DeletedInstitutionalItemVersion> versions = deletedInstitutionalItem.getDeletedInstitutionalItemVersions();
		assert versions.size() == 1 : "Should have at least one deleted version but has "  + versions.size();
		tm.commit(ts);

		ts = tm.getTransaction(td);
		
		DeletedInstitutionalItemVersion version = (DeletedInstitutionalItemVersion) versions.toArray()[0];
		assert version != null : "Should find the version";
		
		DeletedInstitutionalItemVersion other = deletedInstitutionalItemVersionDAO.getById(version.getId(), false);
		
		assert other.equals(version) : "version " + version + " should be eqal to " + other;
		
		other = deletedInstitutionalItemVersionDAO.get(institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getId());
        assert other != null : "Should be able to find deleted version by original version identifier " + institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getId();
        
		other = deletedInstitutionalItemVersionDAO.get(institutionalItem.getId(), institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getVersionNumber());
        assert other != null : "Should be able to find deleted version by original item id and version number id = " 
        	+ institutionalItem.getId() + "version = " + institutionalItem.getVersionedInstitutionalItem().getCurrentVersion().getVersionNumber();
		
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);

		deletedInstitutionalItemDAO.makeTransient(deletedInstitutionalItemDAO.getById(deletedInstitutionalItem.getId(), false));
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		
		
		tm.commit(ts);	
	}
	
	/**
	 * Test Institutional getting items within different collections with different modification
	 * dates.
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 * @throws ParseException 
	 */
	@Test
	public void deletedInstitutionalItemGetByIdModificationDAOTest() throws DuplicateNameException, 
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException, ParseException{
	    // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		UserEmail userEmail = new UserEmail("email");
		
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		userDAO.makePersistent(user);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		//commit the transaction 
		// create a collection
		InstitutionalCollection col1 = repo.createInstitutionalCollection("Collection 1 Name");
		col1.setDescription("colDescription");
		
		InstitutionalCollection col2 = col1.createChild("Collection 2 Name");
		InstitutionalCollection col3 = col1.createChild("Collection 3 Name");
 		
		institutionalCollectionDAO.makePersistent(col1);
		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);
	

		col1 = institutionalCollectionDAO.getById(col1.getId(), false);
		GenericItem genericItemA = new GenericItem("generic Item A");
		GenericItem genericItemB = new GenericItem("generic Item B");
		GenericItem genericItemC = new GenericItem("generic Item C");
		GenericItem genericItemD = new GenericItem("generic Item D");
		GenericItem genericItemE = new GenericItem("generic Item E");
		GenericItem genericItemF = new GenericItem("generic Item F");
		
		InstitutionalItem institutionalItemA = col1.createInstitutionalItem(genericItemA);
		InstitutionalItem institutionalItemB = col2.createInstitutionalItem(genericItemB);
		InstitutionalItem institutionalItemC = col2.createInstitutionalItem(genericItemC);
		InstitutionalItem institutionalItemD = col2.createInstitutionalItem(genericItemD);
		InstitutionalItem institutionalItemE = col3.createInstitutionalItem(genericItemE);
		InstitutionalItem institutionalItemF = col3.createInstitutionalItem(genericItemF);
		
		
		institutionalItemDAO.makePersistent(institutionalItemA);
		institutionalItemDAO.makePersistent(institutionalItemB);
		institutionalItemDAO.makePersistent(institutionalItemC);
		institutionalItemDAO.makePersistent(institutionalItemD);
		institutionalItemDAO.makePersistent(institutionalItemE);
		institutionalItemDAO.makePersistent(institutionalItemF);
		
		
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date d1 = simpleDateFormat.parse("01/01/1980");
		
		DeletedInstitutionalItem deletedInstitutionalItemA = new DeletedInstitutionalItem(institutionalItemA);	
        deletedInstitutionalItemA.setDeletedDate(d1);
        deletedInstitutionalItemA.setDeletedBy(user);
		deletedInstitutionalItemDAO.makePersistent(deletedInstitutionalItemA);
		
		Date d2 = simpleDateFormat.parse("01/01/1981");
		DeletedInstitutionalItem deletedInstitutionalItemB = new DeletedInstitutionalItem(institutionalItemB);	
        deletedInstitutionalItemB.setDeletedDate(d2);
        deletedInstitutionalItemB.setDeletedBy(user);
		deletedInstitutionalItemDAO.makePersistent(deletedInstitutionalItemB);
		
		DeletedInstitutionalItem deletedInstitutionalItemC = new DeletedInstitutionalItem(institutionalItemC);	
        deletedInstitutionalItemC.setDeletedDate(d2);
        deletedInstitutionalItemC.setDeletedBy(user);
		deletedInstitutionalItemDAO.makePersistent(deletedInstitutionalItemC);
		
		Date d3 = simpleDateFormat.parse("01/01/1982");
		
		DeletedInstitutionalItem deletedInstitutionalItemD = new DeletedInstitutionalItem(institutionalItemD);	
        deletedInstitutionalItemD.setDeletedDate(d3);
        deletedInstitutionalItemD.setDeletedBy(user);
		deletedInstitutionalItemDAO.makePersistent(deletedInstitutionalItemD);
		
		DeletedInstitutionalItem deletedInstitutionalItemE = new DeletedInstitutionalItem(institutionalItemE);	
        deletedInstitutionalItemE.setDeletedDate(d3);
        deletedInstitutionalItemE.setDeletedBy(user);
		deletedInstitutionalItemDAO.makePersistent(deletedInstitutionalItemE);
	
		Date d4 = simpleDateFormat.parse("01/01/1983");
		
		DeletedInstitutionalItem deletedInstitutionalItemF = new DeletedInstitutionalItem(institutionalItemF);	
        deletedInstitutionalItemF.setDeletedDate(d4);
        deletedInstitutionalItemF.setDeletedBy(user);
		deletedInstitutionalItemDAO.makePersistent(deletedInstitutionalItemF);


		tm.commit(ts);

		
		ts = tm.getTransaction(td);
		
		Long count = 0l;
		
		// test get items from dates count
		count = deletedInstitutionalItemVersionDAO.getItemsFromDeletedDateCount(d1);
		assert count.equals(6l) : "Should equal 6 but equals " + count;
		
		count = deletedInstitutionalItemVersionDAO.getItemsFromDeletedDateCount(d2);
		assert count.equals(5l) : "Should equal 5 but equals " + count;
				
		count = deletedInstitutionalItemVersionDAO.getItemsFromDeletedDateCount(d3);
		assert count.equals(3l) : "Should equal 3 but equals " + count;
		
		count = deletedInstitutionalItemVersionDAO.getItemsFromDeletedDateCount(d4);
		assert count.equals(1l) : "Should equal 1 but equals " + count;
		
		// test get items between modified dates count
		count = deletedInstitutionalItemVersionDAO.getItemsBetweenDeletedDatesCount(d1, d4);
		assert count.equals(6l) : "Should equal 6 but equals " + count;
		
		count = deletedInstitutionalItemVersionDAO.getItemsBetweenDeletedDatesCount(d2, d3);
		assert count.equals(4l) : "Should equal 4 but equals " + count;
		
		count = deletedInstitutionalItemVersionDAO.getItemsBetweenDeletedDatesCount(d4, d4);
		assert count.equals(1l) : "Should equal 1 but equals " + count;
		
		// test between dates count with collection
		LinkedList<Long> col1Ids = new LinkedList<Long>();
		col1Ids.add(col1.getId());
		col1Ids.add(col2.getId());
		col1Ids.add(col3.getId());
		
		LinkedList<Long> col2Ids = new LinkedList<Long>();
		col2Ids.add(col2.getId());
		
		LinkedList<Long> col3Ids = new LinkedList<Long>();
		col3Ids.add(col3.getId());
		
		
		count = deletedInstitutionalItemVersionDAO.getItemsBetweenDeletedDatesCount(d1, d4, col1Ids);
		assert count.equals(6l) : "Should equal 6 but equals " + count;
		
		count = deletedInstitutionalItemVersionDAO.getItemsBetweenDeletedDatesCount(d1, d4, col2Ids);
		assert count.equals(3l) : "Should equal 3 but equals " + count;
		
		count = deletedInstitutionalItemVersionDAO.getItemsBetweenDeletedDatesCount(d1, d4, col3Ids);
		assert count.equals(2l) : "Should equal 2 but equals " + count;
		
		// test until modified dates
		count = deletedInstitutionalItemVersionDAO.getItemsUntilDeletedDateCount(d1);
		assert count.equals(1l) : "Should equal 1 but equals " + count;
		
		count = deletedInstitutionalItemVersionDAO.getItemsUntilDeletedDateCount(d2);
		assert count.equals(3l) : "Should equal 3 but equals " + count;
		
		count = deletedInstitutionalItemVersionDAO.getItemsUntilDeletedDateCount(d3);
		assert count.equals(5l) : "Should equal 5 but equals " + count;
		
		count = deletedInstitutionalItemVersionDAO.getItemsUntilDeletedDateCount(d4);
		assert count.equals(6l) : "Should equal 6 but equals " + count;
		
		// test until modified dates with collections
		count = deletedInstitutionalItemVersionDAO.getItemsUntilDeletedDateCount(d4, col1Ids);
		assert count.equals(6l) : "Should equal 6 but equals " + count;
		
		count = deletedInstitutionalItemVersionDAO.getItemsUntilDeletedDateCount(d1, col2Ids);
		assert count.equals(0l) : "Should eqal 0 but equals " + count;
		
		count = deletedInstitutionalItemVersionDAO.getItemsUntilDeletedDateCount(d3, col3Ids);
		assert count.equals(1l) : "Should eqal 1 but equals " + count;
		
		
		// test from dates with collections
		count = deletedInstitutionalItemVersionDAO.getItemsFromDeletedDateCount(d1, col1Ids);
		assert count.equals(6l) : "Should equal 6 but equals " + count;
		
		count = deletedInstitutionalItemVersionDAO.getItemsFromDeletedDateCount(d4, col2Ids);
		assert count.equals(0l) : "Should equal 0 but equals " + count;
		
		count = deletedInstitutionalItemVersionDAO.getItemsFromDeletedDateCount(d2, col3Ids);
		assert count.equals(2l) : "Should equal 2 but equals " + count;
		
		List<DeletedInstitutionalItemVersion> versions;

		// get all items with a batch size 
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrder(0l, 3);
		assert versions.size() == 3 : "Should have found 3 but found " + versions.size();
		
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrder(0l, 6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();
		
		// get all items within a specific collection and max number of results
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrder(0l, col1Ids, 6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();
		
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrder(0l, col1Ids, 3);
		assert versions.size() == 3 : "Should have found 3 but found " + versions.size();
		
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrder(0l, col2Ids, 3);
		assert versions.size() == 3 : "Should have found 3 but found " + versions.size();
		
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrder(0l, col3Ids, 3);
		assert versions.size() == 2 : "Should have found 2 but found " + versions.size();
		
		// get all items modified between specific dates and batch size
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrderBetweenDeletedDates(0l, d1, d4, 6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();
		
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrderBetweenDeletedDates(0l, d2, d3, 6);
		assert versions.size() == 4 : "Should have found 4 but found " + versions.size();
		
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrderBetweenDeletedDates(0l, d4, d4, 6);
		assert versions.size() == 1 : "Should have found 1 but found " + versions.size();
		
		// get all items modified between dates for a specific collection
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrderBetweenDeletedDates(0l, d1, d4, col1Ids,6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();

		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrderBetweenDeletedDates(0l, d2, d3, col2Ids, 6);
		assert versions.size() == 3 : "Should have found 3 but found " + versions.size();
		
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrderBetweenDeletedDates(0l, d4, d4, col3Ids, 6);
		assert versions.size() == 1 : "Should have found 1 but found " + versions.size();

		// get all items modified from the specific date
		versions =  deletedInstitutionalItemVersionDAO.getItemsIdOrderFromDeletedDate(0l, d1, 6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();
		
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrderFromDeletedDate(0l, d3, 6);
		assert versions.size() == 3 : "Should have found 3 but found " + versions.size();
		
        //get all items from modified date for a given collections 
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrderFromDeletedDate(0l, d1, col1Ids, 6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();
		
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrderFromDeletedDate(0l, d3, col3Ids, 6);
		assert versions.size() == 2 : "Should have found 2 but found " + versions.size();

        // get all items until modified date
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrderUntilDeletedDate(0l, d4, 6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();
		
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrderUntilDeletedDate(0l, d2, 6);
		assert versions.size() == 3 : "Should have found 3 but found " + versions.size();
		
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrderUntilDeletedDate(0l, d4, col1Ids, 6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();
		
		versions = deletedInstitutionalItemVersionDAO.getItemsIdOrderUntilDeletedDate(0l, d2, col2Ids, 6);
		assert versions.size() == 2 : "Should have found 2 but found " + versions.size();
		
		tm.commit(ts);
		
		

		//create a new transaction
		ts = tm.getTransaction(td);
		
		deletedInstitutionalItemDAO.makeTransient(deletedInstitutionalItemDAO.getById(deletedInstitutionalItemA.getId(), false));
		deletedInstitutionalItemDAO.makeTransient(deletedInstitutionalItemDAO.getById(deletedInstitutionalItemB.getId(), false));
		deletedInstitutionalItemDAO.makeTransient(deletedInstitutionalItemDAO.getById(deletedInstitutionalItemC.getId(), false));
		deletedInstitutionalItemDAO.makeTransient(deletedInstitutionalItemDAO.getById(deletedInstitutionalItemD.getId(), false));
		deletedInstitutionalItemDAO.makeTransient(deletedInstitutionalItemDAO.getById(deletedInstitutionalItemE.getId(), false));
		deletedInstitutionalItemDAO.makeTransient(deletedInstitutionalItemDAO.getById(deletedInstitutionalItemF.getId(), false));

		
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col1.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItemA.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItemB.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItemC.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItemD.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItemE.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItemF.getId(), false));
		repoHelper.cleanUpRepository();
		
		tm.commit(ts);	
		
	}

}
