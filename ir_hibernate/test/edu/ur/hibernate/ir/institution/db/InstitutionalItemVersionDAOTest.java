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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.handle.HandleInfoDAO;
import edu.ur.ir.handle.HandleNameAuthority;
import edu.ur.ir.handle.HandleNameAuthorityDAO;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionDAO;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemDAO;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionDAO;
import edu.ur.ir.item.DuplicateContributorException;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.item.VersionedItemDAO;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorDAO;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeDAO;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonNameAuthorityDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;

/**
 * Test the persistence methods for published version Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class InstitutionalItemVersionDAOTest {
	
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
	
	InstitutionalItemVersionDAO institutionalItemVersionDAO = (InstitutionalItemVersionDAO) ctx.getBean("institutionalItemVersionDAO");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
    /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");

    /** Versioned item data access*/
    VersionedItemDAO versionedItemDAO = (VersionedItemDAO)ctx.getBean("versionedItemDAO");
    
	/** persistance for dealing with contributor Type*/
	ContributorTypeDAO contributorTypeDAO = (ContributorTypeDAO) ctx
	.getBean("contributorTypeDAO");
	
	/** persistance for dealing with contributor */
	ContributorDAO contributorDAO = (ContributorDAO) ctx
	.getBean("contributorDAO");

	/** Persistence of person name */
	PersonNameAuthorityDAO personNameAuthorityDAO = (PersonNameAuthorityDAO) ctx.getBean("personNameAuthorityDAO");

    /** Generic item data access */
    GenericItemDAO itemDAO = (GenericItemDAO) ctx.getBean("itemDAO");
    
	/** used to store handle name authority data */
	HandleNameAuthorityDAO handleNameAuthorityDAO = (HandleNameAuthorityDAO) ctx
	.getBean("handleNameAuthorityDAO");
	
	/** used to store handle name authority data */
	HandleInfoDAO handleInfoDAO = (HandleInfoDAO) ctx
	.getBean("handleInfoDAO");
	
	/**
	 * Test Institutional Item persistence
	 * @throws DuplicateNameException 
	 */
	@Test
	public void baseInstitutionalItemDAOTest() throws DuplicateNameException {

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
		
		InstitutionalItemVersion institutionalItemVersion = 
			institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersion(institutionalItem.getVersionedInstitutionalItem().getLargestVersion());
		institutionalItemDAO.makePersistent(institutionalItem);
		tm.commit(ts);

		ts = tm.getTransaction(td);
		InstitutionalItemVersion other = institutionalItemVersionDAO.getById(institutionalItemVersion.getId(), false);
		assert other.equals(institutionalItemVersion) : "Should be able to find item " + institutionalItemVersion;
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
		
		assert institutionalItemDAO.getById(institutionalItem.getId(), false) == null : 
			"Should not be able to find the insitutional item" + institutionalItem;
	}
	
	/**
	 * Test withdrawing an item
	 * @throws DuplicateNameException 
	 */
	@Test
	public void institutionalItemWithdrawDAOTest() throws DuplicateNameException {

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
		
		InstitutionalItemVersion institutionalItemVersion = 
			institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersion(institutionalItem.getVersionedInstitutionalItem().getLargestVersion());

		tm.commit(ts);

		ts = tm.getTransaction(td);
		
		InstitutionalItemVersion other = institutionalItemVersionDAO.getById(institutionalItemVersion.getId(), false);
		assert other.equals(institutionalItemVersion) :
			"Should be able to find item " + institutionalItemVersion;
		other.withdraw(user, "test withdraw", true);
		
		institutionalItemVersionDAO .makePersistent(other);
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);
		
		other = institutionalItemVersionDAO.getById(institutionalItemVersion.getId(), false);
		assert other.getWithdrawnToken() != null : "Withdrawn token is null for " + other;
		assert other.isWithdrawn() : "Institutional item version " + other + " should be withdrawn ";
		
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		
		tm.commit(ts);	
		
		assert institutionalItemDAO.getById(institutionalItem.getId(), false) == null : 
			"Should not be able to find the insitutional item" + institutionalItem;
	}
	
	/**
	 * Test reInstating an item
	 * @throws DuplicateNameException 
	 */
	@Test
	public void institutionalItemReInstateDAOTest() throws DuplicateNameException {

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
		
		InstitutionalItemVersion institutionalItemVersion = 
			institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersion(institutionalItem.getVersionedInstitutionalItem().getLargestVersion());

		tm.commit(ts);

		ts = tm.getTransaction(td);
		
		InstitutionalItemVersion other = institutionalItemVersionDAO.getById(institutionalItemVersion.getId(), false);
		assert other.equals(institutionalItemVersion) :
			"Should be able to find item " + institutionalItemVersion;
		other.withdraw(user, "test withdraw", true);
		
		institutionalItemVersionDAO .makePersistent(other);
		tm.commit(ts);

		//create a new transaction - withdraw
		ts = tm.getTransaction(td);
		
		other = institutionalItemVersionDAO.getById(institutionalItemVersion.getId(), false);
		assert other.getWithdrawnToken() != null : "Withdrawn token is null for " + other;
		assert other.isWithdrawn() : "Institutional item version " + other + " should be withdrawn ";
		
		other.reInstate(user, "it's a cool publication again");
		
		
		tm.commit(ts);	
		
		
		//create a new transaction - reInstate
		ts = tm.getTransaction(td);
		
		other = institutionalItemVersionDAO.getById(institutionalItemVersion.getId(), false);
		assert other.getWithdrawnToken() == null : "Withdrawn token should be null for " + other;
		assert !other.isWithdrawn() : "Institutional item version " + other + " should be withdrawn ";
		assert other.getReinstateHistory().size() == 1 : "There should be reInstate history for " + other;
		
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		
		tm.commit(ts);	
		
		
		assert institutionalItemDAO.getById(institutionalItem.getId(), false) == null : 
			"Should not be able to find the insitutional item" + institutionalItem;
	}

	/**
	 * Test get institutional item version by person name.
	 * 
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws UserHasPublishedDeleteException
	 * @throws ParseException 
	 */
	@Test
	public void getPublicationVersionsByPersonNameTest() 
		throws DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, ParseException, DuplicateContributorException {

		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);

		// create a repository to store files in.
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");
	
		UserEmail userEmail = new UserEmail("email");
		
		// create a user and add the versioned file to the item
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		userDAO.makePersistent(user);


		// save the repository
		tm.commit(ts);
		
 		PersonName name = new PersonName();
		name.setFamilyName("familyName");
		name.setForename("forename");
		name.setInitials("n.d.s.");
		name.setMiddleName("MiddleName");
		name.setNumeration("III");
		name.setSurname("surname");

 		PersonName name1 = new PersonName();
		name1.setFamilyName("familyName1");
		name1.setForename("forename1");
		name1.setInitials("n.d.s.1");
		name1.setMiddleName("MiddleName1");
		name1.setNumeration("III1");
		name1.setSurname("surname1");

		PersonNameAuthority p = new PersonNameAuthority(name);
		p.addBirthDate(1,1,2005);
		p.addDeathDate(1, 1, 2105);
		
		p.addName(name, true);
		p.addName(name1, false);

        ts = tm.getTransaction(td);
		personNameAuthorityDAO.makePersistent(p);
		
		
		ContributorType ct1 = new ContributorType("Author");
		contributorTypeDAO.makePersistent(ct1);
        
		ContributorType ct2 = new ContributorType("Advisor");
		contributorTypeDAO.makePersistent(ct2);
		
		//complete the transaction
		tm.commit(ts);

		
        // Start the transaction - create collections
		ts = tm.getTransaction(td);
		
		GenericItem item1 = new GenericItem("itemName1");
		Contributor c1 = new Contributor(name, ct1);
		item1.addContributor(c1);
		itemDAO.makePersistent(item1);

		GenericItem item2 = new GenericItem("itemName2");
		Contributor c2 = new Contributor(name1, ct2);
		item2.addContributor(c2);
		itemDAO.makePersistent(item2);
		
		// create the collections
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		InstitutionalItem ii1 = collection.createInstitutionalItem(item1);
		InstitutionalCollection subCollection = collection.createChild("subChild");
		InstitutionalItem ii2 = subCollection.createInstitutionalItem(item2);
		
		institutionalCollectionDAO.makePersistent(collection);
   
		// save the repository
		tm.commit(ts);	

		
	    //create a new transaction
		ts = tm.getTransaction(td);
		List<Long> personNameIds = new ArrayList<Long>();
		personNameIds.add(name.getId());
		personNameIds.add(name1.getId());
		List<InstitutionalItemVersion> itemVersions = institutionalItemVersionDAO.getPublicationVersionsByPersonName(personNameIds);
		
		assert itemVersions.size()  == 2 : "Should be 2 " ;
		assert itemVersions.contains(ii2.getVersionedInstitutionalItem().getCurrentVersion()) : "Institutional item version 1 should exist" ;
		assert itemVersions.contains(ii1.getVersionedInstitutionalItem().getCurrentVersion()) : "Institutional item version 2 should exist" ;
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(collection.getId(), false));
		
        itemDAO.makeTransient(itemDAO.getById(item1.getId(), false));
        itemDAO.makeTransient(itemDAO.getById(item2.getId(), false));
        
		contributorDAO.makeTransient(contributorDAO.getById(c1.getId(), false));
		contributorDAO.makeTransient(contributorDAO.getById(c2.getId(), false));

		contributorTypeDAO.makeTransient(contributorTypeDAO.getById(ct1.getId(), false));
		contributorTypeDAO.makeTransient(contributorTypeDAO.getById(ct2.getId(), false));
		
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p.getId(), false));
		
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test handle infor with institutional item
	 * 
	 * @throws DuplicateNameException 
	 */
	@Test
	public void institutionalItemHandleInfoDAOTest() throws DuplicateNameException {

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
		
		HandleNameAuthority handleNameAuthority = new HandleNameAuthority("12345678");
		
		HandleInfo handleInfo = new HandleInfo("1234", "http://www.google.com", handleNameAuthority);
 		
	    handleNameAuthorityDAO.makePersistent(handleNameAuthority);
	    handleInfoDAO.makePersistent(handleInfo);
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
		
		InstitutionalItemVersion institutionalItemVersion = 
			institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersion(institutionalItem.getVersionedInstitutionalItem().getLargestVersion());

		HandleInfo myInfo = handleInfoDAO.getById(handleInfo.getId(), false);
		assert myInfo != null : "Should be able to find handle info " + myInfo;
		institutionalItemVersion.setHandleInfo(myInfo);
		assert institutionalItemVersion.getHandleInfo() != null : "Should be able to find handle information " + myInfo;
		institutionalItemDAO.makePersistent(institutionalItem);
		tm.commit(ts);

		ts = tm.getTransaction(td);
		InstitutionalItemVersion other = institutionalItemVersionDAO.getById(institutionalItemVersion.getId(), false);
		assert other.equals(institutionalItemVersion) : "Should be able to find item " + institutionalItemVersion;
		assert other.getHandleInfo().equals(handleInfo) : " Item should have handle info " + handleInfo;
		InstitutionalItemVersion other2 = institutionalItemVersionDAO.getItemVersionByHandleId(handleInfo.getId());
		assert other2 != null : "should find item for handle " + handleInfo;
		assert other2.equals(other) : "Other 2: " + other2 + " should equal other: " + other;
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		handleInfoDAO.makeTransient(handleInfoDAO.getById(handleInfo.getId(), false));
		handleNameAuthorityDAO.makeTransient(handleNameAuthorityDAO.getById(handleNameAuthority.getId(), false));
		tm.commit(ts);	
		
		assert institutionalItemDAO.getById(institutionalItem.getId(), false) == null : 
			"Should not be able to find the insitutional item" + institutionalItem;
	}

}
