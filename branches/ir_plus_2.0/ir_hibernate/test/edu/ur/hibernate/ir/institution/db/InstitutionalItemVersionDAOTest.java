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

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.handle.HandleInfoDAO;
import edu.ur.ir.handle.HandleNameAuthority;
import edu.ur.ir.handle.HandleNameAuthorityDAO;
import edu.ur.ir.institution.CollectionDoesNotAcceptItemsException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionDAO;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemDAO;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionDAO;
import edu.ur.ir.institution.InstitutionalItemVersionDownloadCount;
import edu.ur.ir.item.DuplicateContributorException;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.SponsorDAO;
import edu.ur.ir.item.VersionedItemDAO;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorDAO;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeDAO;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonNameAuthorityDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.VersionedLicense;
import edu.ur.ir.repository.VersionedLicenseDAO;
import edu.ur.ir.statistics.FileDownloadInfo;
import edu.ur.ir.statistics.FileDownloadInfoDAO;
import edu.ur.ir.statistics.IgnoreIpAddress;
import edu.ur.ir.statistics.IgnoreIpAddressDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.order.OrderType;
import edu.ur.util.FileUtil;

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
	
    /** versioned license data access  */
    VersionedLicenseDAO versionedLicenseDAO= (VersionedLicenseDAO) ctx.getBean("versionedLicenseDAO");
    
    /** sponsor data access */
    SponsorDAO sponsorDAO = (SponsorDAO) ctx.getBean("sponsorDAO");
    
    /** ir file data access */
    IrFileDAO irFileDAO = 
    	(IrFileDAO) ctx.getBean("irFileDAO");
    
	/**  File download information */
	FileDownloadInfoDAO fileDownloadInfoDAO = (FileDownloadInfoDAO) ctx
	.getBean("fileDownloadInfoDAO");
	
	/** persistance for dealing with ignoring ip addesses */
	IgnoreIpAddressDAO ignoreIpAddressDAO = (IgnoreIpAddressDAO) ctx
	.getBean("ignoreIpAddressDAO");


	
	/**
	 * Test Institutional Item persistence
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void baseInstitutionalItemDAOTest() throws DuplicateNameException, 
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException{

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
		List<InstitutionalItemVersion> versions = institutionalItemVersionDAO.getInstitutionalItemVersionsByGenericItemId(genericItem.getId());
		assert versions.size() == 1 : "Versions should have size 1 but has " + versions.size();
		assert versions.contains(other) : "Versions should contain other but does not other = " + other;
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
		
		assert institutionalItemDAO.getById(institutionalItem.getId(), false) == null : 
			"Should not be able to find the insitutional item" + institutionalItem;
	}
	
	/**
	 * Test withdrawing an item
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void institutionalItemWithdrawDAOTest() throws DuplicateNameException, 
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException{

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
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		repoHelper.cleanUpRepository();
		
		tm.commit(ts);	
		
		assert institutionalItemDAO.getById(institutionalItem.getId(), false) == null : 
			"Should not be able to find the insitutional item" + institutionalItem;
	}
	
	/**
	 * Test reInstating an item
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void institutionalItemReInstateDAOTest() throws DuplicateNameException, 
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException{

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
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
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
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void getPublicationVersionsByPersonNameTest() 
		throws DuplicateNameException, IllegalFileSystemNameException, 
		UserHasPublishedDeleteException, 
		ParseException, 
		DuplicateContributorException, 
		LocationAlreadyExistsException,
		CollectionDoesNotAcceptItemsException{

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
		p.addBirthDate(2005);
		p.addDeathDate(2105);
		
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
		List<InstitutionalItemVersionDownloadCount> itemVersions = institutionalItemVersionDAO.getPublicationVersionsForNamesByDownload(0, 10, personNameIds, OrderType.ASCENDING_ORDER);
		
		assert itemVersions.size()  == 2 : "Should be 2 but is " + itemVersions.size() ;
		InstitutionalItemVersionDownloadCount one = new InstitutionalItemVersionDownloadCount(ii2.getVersionedInstitutionalItem().getCurrentVersion(), 0l);
		InstitutionalItemVersionDownloadCount two = new InstitutionalItemVersionDownloadCount(ii1.getVersionedInstitutionalItem().getCurrentVersion(),0l);

		for(InstitutionalItemVersionDownloadCount count : itemVersions)
		{
			System.out.println("Found " + count);
		}
		assert itemVersions.contains(one) : "download count 1 should exist " + one;
		assert itemVersions.contains(two) : "download count 2 should exist " + two;
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
	 * Test handle information with institutional item
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void institutionalItemHandleInfoDAOTest() throws DuplicateNameException, 
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException{

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
		assert myInfo != null : "Should be able to find handle info " + handleInfo.getId();
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
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		tm.commit(ts);	
		
		assert institutionalItemDAO.getById(institutionalItem.getId(), false) == null : 
			"Should not be able to find the insitutional item" + institutionalItem;
	}
	
	/**
	 * Test setting the repository license for the institutional item version
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void institutionalItemRepositoryLicenseDAOTest() throws DuplicateNameException, 
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException{

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
		
	    UserEmail userEmail = new UserEmail("email");
    	
		// create a user add them to license
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		userDAO.makePersistent(user);
		
		VersionedLicense versionedLicense = new VersionedLicense(user, "this is license text", "Main License");
		versionedLicenseDAO.makePersistent(versionedLicense);

		tm.commit(ts);
		
		// start a new transaction
		ts = tm.getTransaction(td);
		
        user = userDAO.getById(user.getId(), false);
     	col = institutionalCollectionDAO.getById(col.getId(), false);
		GenericItem genericItem = new GenericItem("genericItem");
		
		InstitutionalItem institutionalItem = col.createInstitutionalItem(genericItem);
		
		InstitutionalItemVersion institutionalItemVersion = 
			institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersion(institutionalItem.getVersionedInstitutionalItem().getLargestVersion());

		versionedLicense = versionedLicenseDAO.getById(versionedLicense.getId(), false);

		institutionalItemVersion.addRepositoryLicense(versionedLicense.getCurrentVersion(), user);
		institutionalItemDAO.makePersistent(institutionalItem);
		tm.commit(ts);

		ts = tm.getTransaction(td);
		InstitutionalItemVersion other = institutionalItemVersionDAO.getById(institutionalItemVersion.getId(), false);
		assert other.equals(institutionalItemVersion) : "Should be able to find item " + institutionalItemVersion;
		assert other.getRepositoryLicense() != null : "Should have repository license but is null";
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		versionedLicenseDAO.makeTransient(versionedLicenseDAO.getById(versionedLicense.getId(), false));
		repoHelper.cleanUpRepository();
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		tm.commit(ts);	
		
		assert institutionalItemDAO.getById(institutionalItem.getId(), false) == null : 
			"Should not be able to find the insitutional item" + institutionalItem;
	}

	/**
	 * Test Institutional item persistence
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void institutionalItemSponsorDAOTest() throws DuplicateNameException, 
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException{

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
		
		Sponsor sponsor1 = new Sponsor("sponsor1");
		sponsorDAO.makePersistent(sponsor1);

		genericItem.addItemSponsor(sponsor1);
		
		InstitutionalItem institutionalItem = col.createInstitutionalItem(genericItem);
		institutionalItemDAO.makePersistent(institutionalItem);

		tm.commit(ts);

		ts = tm.getTransaction(td);
		institutionalItem = institutionalItemDAO.getById(institutionalItem.getId(), false);
		assert institutionalItemDAO.getById(institutionalItem.getId(), false).equals(institutionalItem) :
			"Should be able to find item " + institutionalItem;
		
		List<InstitutionalItemVersionDownloadCount> items = institutionalItemVersionDAO.getItemsBySponsorItemNameOrder(0, 10, sponsor1.getId(), OrderType.ASCENDING_ORDER);
		assert items.size() == 1 : "Should have one item but got " + items.size();
		
		List<InstitutionalItemVersionDownloadCount> downloadItems = institutionalItemVersionDAO.getItemsBySponsorItemDownloadOrder(0, 10, sponsor1.getId(), OrderType.ASCENDING_ORDER);
		assert downloadItems.size() == 1 : "Should have one download item count but have " + downloadItems.size(); 
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(col.getId(), false));
		
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
        sponsorDAO.makeTransient(sponsorDAO.getById(sponsor1.getId(), false));
		
		repoHelper.cleanUpRepository();
		
		tm.commit(ts);	
		
		assert institutionalItemDAO.getById(institutionalItem.getId(), false) == null : 
			"Should not be able to find the insitutional item" + institutionalItem;
	}
	
	/**
	 * Test download counts for institutional item by person name.
	 * 
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws UserHasPublishedDeleteException
	 * @throws ParseException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void getInstitutionalItemDownloadCountByPersonNameTest() 
		throws DuplicateNameException, 
		IllegalFileSystemNameException, 
		UserHasPublishedDeleteException, 
		ParseException, 
		DuplicateContributorException, 
		LocationAlreadyExistsException,
		CollectionDoesNotAcceptItemsException{

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

		// Start the transaction - create collections
		ts = tm.getTransaction(td);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f1 = testUtil.creatFile(directory, "testFile1", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test1");
		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f1, "newFile1");
		IrFile irFile1 = new IrFile(fileInfo1, "newFile1");
		irFileDAO.makePersistent(irFile1);

		File f2 = testUtil.creatFile(directory, "testFile2", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test2");
		FileInfo fileInfo2 = repo.getFileDatabase().addFile(f2, "newFile2");
		IrFile irFile2 = new IrFile(fileInfo2, "newFile2");
		irFileDAO.makePersistent(irFile2);
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
		p.addBirthDate(2005);
		p.addDeathDate(2105);
		
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
		item1.addFile(irFile1);
		Contributor c1 = new Contributor(name, ct1);
		item1.addContributor(c1);
		itemDAO.makePersistent(item1);

		GenericItem item2 = new GenericItem("itemName2");
		item2.addFile(irFile2);
		Contributor c2 = new Contributor(name1, ct2);
		item2.addContributor(c2);
		itemDAO.makePersistent(item2);
		
		// create the collections
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		collection.createInstitutionalItem(item1);
		InstitutionalCollection subCollection = collection.createChild("subChild");
		InstitutionalItem ii2 = subCollection.createInstitutionalItem(item2);
		
		institutionalCollectionDAO.makePersistent(collection);
   
		// save the repository
		tm.commit(ts);	

		// create some downloads
        ts = tm.getTransaction(td);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy");
	    Date d = simpleDateFormat.parse("1/1/2008");
	    
        FileDownloadInfo downloadInfo1 = new FileDownloadInfo("123.0.0.1", irFile1.getId(), d);
        downloadInfo1.setDownloadCount(1);
        fileDownloadInfoDAO.makePersistent(downloadInfo1);
       
        FileDownloadInfo downloadInfo2 = new FileDownloadInfo("123.0.0.7", irFile2.getId(), d);
        downloadInfo2.setDownloadCount(2);
        fileDownloadInfoDAO.makePersistent(downloadInfo2);
        
        Long count1 = fileDownloadInfoDAO.getNumberOfFileDownloadsForIrFile(irFile1.getId());
        irFile1 = irFileDAO.getById(irFile1.getId(), false);
        irFile1.setDownloadCount(count1);
        irFileDAO.makePersistent(irFile1);
		
        Long count2 = fileDownloadInfoDAO.getNumberOfFileDownloadsForIrFile(irFile2.getId());
        irFile2 = irFileDAO.getById(irFile2.getId(), false);
        irFile2.setDownloadCount(count2);
        irFileDAO.makePersistent(irFile2);
        
	    tm.commit(ts);

        
        ts = tm.getTransaction(td);
		long itemFileCount = itemDAO.getDownloadCount(item1.getId());
		assert itemFileCount == 1 : "Should find 1 but found " + itemFileCount ;
		
		itemFileCount = itemDAO.getDownloadCount(item2.getId());
		assert itemFileCount == 2 : "Should find 2 but found " + itemFileCount ;
		
		tm.commit(ts);
		
		
		// add an ip address to the ignore list
	    //create a new transaction
		ts = tm.getTransaction(td);
	    IgnoreIpAddress ip1 = new IgnoreIpAddress(123,0,0,10, 15);
        ignoreIpAddressDAO.makePersistent(ip1);
        tm.commit(ts);
        
		
	    //create a new transaction
		ts = tm.getTransaction(td);
		List<Long> personNameIds = new ArrayList<Long>();
		personNameIds.add(name.getId());
		personNameIds.add(name1.getId());
		List<InstitutionalItemVersionDownloadCount> topDownloads = institutionalItemVersionDAO.getPublicationVersionsForNamesByDownload(0, 1, personNameIds, OrderType.DESCENDING_ORDER);
		assert topDownloads.size() == 1 :  " size should be one but is " + topDownloads.size();
		InstitutionalItemVersionDownloadCount c = topDownloads.get(0);
		
		assert c.getDownloadCount() == 2 : "Download count should be 2 but " + c.getDownloadCount();
		assert c.getInstitutionalItemVersion().equals(ii2.getVersionedInstitutionalItem().getCurrentVersion()) : "Institutional item should be equal" ;
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		ignoreIpAddressDAO.makeTransient(ignoreIpAddressDAO.getById(ip1.getId(), false));
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(collection.getId(), false));
		
        itemDAO.makeTransient(itemDAO.getById(item1.getId(), false));
        itemDAO.makeTransient(itemDAO.getById(item2.getId(), false));
        
		irFileDAO.makeTransient(irFileDAO.getById(irFile1.getId(), false));
		irFileDAO.makeTransient(irFileDAO.getById(irFile2.getId(), false));

		contributorDAO.makeTransient(contributorDAO.getById(c1.getId(), false));
		contributorDAO.makeTransient(contributorDAO.getById(c2.getId(), false));

		contributorTypeDAO.makeTransient(contributorTypeDAO.getById(ct1.getId(), false));
		contributorTypeDAO.makeTransient(contributorTypeDAO.getById(ct2.getId(), false));
		
		personNameAuthorityDAO.makeTransient(personNameAuthorityDAO.getById(p.getId(), false));
		
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		fileDownloadInfoDAO.makeTransient(fileDownloadInfoDAO.getById(downloadInfo1.getId(), false));
		fileDownloadInfoDAO.makeTransient(fileDownloadInfoDAO.getById(downloadInfo2.getId(), false));
		
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
	public void institutionalItemGetByIdModificationDAOTest() throws DuplicateNameException, 
	LocationAlreadyExistsException,
	CollectionDoesNotAcceptItemsException, ParseException{
		
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
		
		
        
		
		InstitutionalItemVersion institutionalItemVersionA = 
			institutionalItemA.getVersionedInstitutionalItem().getCurrentVersion();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date d1 = simpleDateFormat.parse("01/01/1980");
		institutionalItemVersionA.setDateLastModified(new Timestamp(d1.getTime()));
		institutionalItemVersionDAO.makePersistent(institutionalItemVersionA);

		Date d2 = simpleDateFormat.parse("01/01/1981");
		InstitutionalItemVersion institutionalItemVersionB = 
			institutionalItemB.getVersionedInstitutionalItem().getCurrentVersion();
		institutionalItemVersionB.setDateLastModified(new Timestamp(d2.getTime()));
		institutionalItemVersionDAO.makePersistent(institutionalItemVersionB);

		
		InstitutionalItemVersion institutionalItemVersionC = 
			institutionalItemC.getVersionedInstitutionalItem().getCurrentVersion();
		institutionalItemVersionC.setDateLastModified(new Timestamp(d2.getTime()));
		institutionalItemVersionDAO.makePersistent(institutionalItemVersionC);

		Date d3 = simpleDateFormat.parse("01/01/1982");
		InstitutionalItemVersion institutionalItemVersionD = 
			institutionalItemD.getVersionedInstitutionalItem().getCurrentVersion();
		institutionalItemVersionD.setDateLastModified(new Timestamp(d3.getTime()));
		institutionalItemVersionDAO.makePersistent(institutionalItemVersionD);

		
		InstitutionalItemVersion institutionalItemVersionE = 
			institutionalItemE.getVersionedInstitutionalItem().getCurrentVersion();
		institutionalItemVersionE.setDateLastModified(new Timestamp(d3.getTime()));
		institutionalItemVersionDAO.makePersistent(institutionalItemVersionE);

		
		Date d4 = simpleDateFormat.parse("01/01/1983");
		InstitutionalItemVersion institutionalItemVersionF = 
			institutionalItemF.getVersionedInstitutionalItem().getCurrentVersion();
		institutionalItemVersionF.setDateLastModified(new Timestamp(d4.getTime()));
		institutionalItemVersionDAO.makePersistent(institutionalItemVersionF);


		tm.commit(ts);

		
		ts = tm.getTransaction(td);
		
		Long count = 0l;
		
		// test get items from dates count
		count = institutionalItemVersionDAO.getItemsFromModifiedDateCount(d1);
		assert count.equals(6l) : "Should equal 6 but equals " + count;
		
		count = institutionalItemVersionDAO.getItemsFromModifiedDateCount(d2);
		assert count.equals(5l) : "Should equal 5 but equals " + count;
				
		count = institutionalItemVersionDAO.getItemsFromModifiedDateCount(d3);
		assert count.equals(3l) : "Should equal 3 but equals " + count;
		
		count = institutionalItemVersionDAO.getItemsFromModifiedDateCount(d4);
		assert count.equals(1l) : "Should equal 1 but equals " + count;
		
		// test get items between modified dates count
		count = institutionalItemVersionDAO.getItemsBetweenModifiedDatesCount(d1, d4);
		assert count.equals(6l) : "Should equal 6 but equals " + count;
		
		count = institutionalItemVersionDAO.getItemsBetweenModifiedDatesCount(d2, d3);
		assert count.equals(4l) : "Should equal 4 but equals " + count;
		
		count = institutionalItemVersionDAO.getItemsBetweenModifiedDatesCount(d4, d4);
		assert count.equals(1l) : "Should equal 1 but equals " + count;
		
		// test between dates count with collection
		count = institutionalItemVersionDAO.getItemsBetweenModifiedDatesCount(d1, d4, col1);
		assert count.equals(6l) : "Should equal 6 but equals " + count;
		
		count = institutionalItemVersionDAO.getItemsBetweenModifiedDatesCount(d1, d4, col2);
		assert count.equals(3l) : "Should equal 3 but equals " + count;
		
		count = institutionalItemVersionDAO.getItemsBetweenModifiedDatesCount(d1, d4, col3);
		assert count.equals(2l) : "Should equal 2 but equals " + count;
		
		// test until modified dates
		count = institutionalItemVersionDAO.getItemsUntilModifiedDateCount(d1);
		assert count.equals(1l) : "Should equal 1 but equals " + count;
		
		count = institutionalItemVersionDAO.getItemsUntilModifiedDateCount(d2);
		assert count.equals(3l) : "Should equal 3 but equals " + count;
		
		count = institutionalItemVersionDAO.getItemsUntilModifiedDateCount(d3);
		assert count.equals(5l) : "Should equal 5 but equals " + count;
		
		count = institutionalItemVersionDAO.getItemsUntilModifiedDateCount(d4);
		assert count.equals(6l) : "Should equal 6 but equals " + count;
		
		// test until modified dates with collections
		count = institutionalItemVersionDAO.getItemsUntilModifiedDateCount(d4, col1);
		assert count.equals(6l) : "Should equal 6 but equals " + count;
		
		count = institutionalItemVersionDAO.getItemsUntilModifiedDateCount(d1, col2);
		assert count.equals(0l) : "Should eqal 0 but equals " + count;
		
		count = institutionalItemVersionDAO.getItemsUntilModifiedDateCount(d3, col3);
		assert count.equals(1l) : "Should eqal 1 but equals " + count;
		
		
		// test from dates with collections
		count = institutionalItemVersionDAO.getItemsFromModifiedDateCount(d1, col1);
		assert count.equals(6l) : "Should equal 6 but equals " + count;
		
		count = institutionalItemVersionDAO.getItemsFromModifiedDateCount(d4, col2);
		assert count.equals(0l) : "Should equal 0 but equals " + count;
		
		count = institutionalItemVersionDAO.getItemsFromModifiedDateCount(d2, col3);
		assert count.equals(2l) : "Should equal 2 but equals " + count;
		
		List<InstitutionalItemVersion> versions;

		// get all items with a batch size 
		versions = institutionalItemVersionDAO.getItemsIdOrder(0l, 3);
		assert versions.size() == 3 : "Should have found 3 but found " + versions.size();
		
		versions = institutionalItemVersionDAO.getItemsIdOrder(0l, 6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();
		
		// get all items within a specific collection and max number of results
		versions = institutionalItemVersionDAO.getItemsIdOrder(0l, col1, 6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();
		
		versions = institutionalItemVersionDAO.getItemsIdOrder(0l, col1, 3);
		assert versions.size() == 3 : "Should have found 3 but found " + versions.size();
		
		versions = institutionalItemVersionDAO.getItemsIdOrder(0l, col2, 3);
		assert versions.size() == 3 : "Should have found 3 but found " + versions.size();
		
		versions = institutionalItemVersionDAO.getItemsIdOrder(0l, col3, 3);
		assert versions.size() == 2 : "Should have found 2 but found " + versions.size();
		
		// get all items modified between specific dates and batch size
		versions = institutionalItemVersionDAO.getItemsIdOrderBetweenModifiedDates(0l, d1, d4, 6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();
		
		versions = institutionalItemVersionDAO.getItemsIdOrderBetweenModifiedDates(0l, d2, d3, 6);
		assert versions.size() == 4 : "Should have found 4 but found " + versions.size();
		
		versions = institutionalItemVersionDAO.getItemsIdOrderBetweenModifiedDates(0l, d4, d4, 6);
		assert versions.size() == 1 : "Should have found 1 but found " + versions.size();
		
		// get all items modified between dates for a specific collection
		versions = institutionalItemVersionDAO.getItemsIdOrderBetweenModifiedDates(0l, d1, d4, col1,6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();

		versions = institutionalItemVersionDAO.getItemsIdOrderBetweenModifiedDates(0l, d2, d3, col2, 6);
		assert versions.size() == 3 : "Should have found 3 but found " + versions.size();
		
		versions = institutionalItemVersionDAO.getItemsIdOrderBetweenModifiedDates(0l, d4, d4, col3, 6);
		assert versions.size() == 1 : "Should have found 1 but found " + versions.size();

		// get all items modified from the specific date
		versions =  institutionalItemVersionDAO.getItemsIdOrderFromModifiedDate(0l, d1, 6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();
		
		versions = institutionalItemVersionDAO.getItemsIdOrderFromModifiedDate(0l, d3, 6);
		assert versions.size() == 3 : "Should have found 3 but found " + versions.size();
		
        //get all items from modified date for a given collections 
		versions = institutionalItemVersionDAO.getItemsIdOrderFromModifiedDate(0l, d1, col1, 6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();
		
		versions = institutionalItemVersionDAO.getItemsIdOrderFromModifiedDate(0l, d3, col3, 6);
		assert versions.size() == 2 : "Should have found 2 but found " + versions.size();

        // get all items until modified date
		versions = institutionalItemVersionDAO.getItemsIdOrderUntilModifiedDate(0l, d4, 6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();
		
		versions = institutionalItemVersionDAO.getItemsIdOrderUntilModifiedDate(0l, d2, 6);
		assert versions.size() == 3 : "Should have found 3 but found " + versions.size();
		
		versions = institutionalItemVersionDAO.getItemsIdOrderUntilModifiedDate(0l, d4, col1, 6);
		assert versions.size() == 6 : "Should have found 6 but found " + versions.size();
		
		versions = institutionalItemVersionDAO.getItemsIdOrderUntilModifiedDate(0l, d2, col2, 6);
		assert versions.size() == 2 : "Should have found 2 but found " + versions.size();
		
		tm.commit(ts);
		
		

		//create a new transaction
		ts = tm.getTransaction(td);
		
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
