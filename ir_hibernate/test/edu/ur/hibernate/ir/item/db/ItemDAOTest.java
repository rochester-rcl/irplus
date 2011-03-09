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

package edu.ur.hibernate.ir.item.db;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.file.FileVersionDAO;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeDAO;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.FirstAvailableDate;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeDAO;
import edu.ur.ir.item.ItemContentType;
import edu.ur.ir.item.ItemContributor;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemIdentifier;
import edu.ur.ir.item.ItemIdentifierDAO;
import edu.ur.ir.item.ItemLink;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeDAO;
import edu.ur.ir.item.OriginalItemCreationDate;
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
import edu.ur.util.FileUtil;

/**
 * Test the persistance methods for Item Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ItemDAOTest {

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	/** transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** Item relational data access */
	GenericItemDAO itemDAO = (GenericItemDAO) ctx .getBean("itemDAO");
	
	/** Content type relational data access*/
	ContentTypeDAO contentTypeDAO = (ContentTypeDAO) ctx.getBean("contentTypeDAO");
	
    /**Language type relational data access*/
    LanguageTypeDAO languageTypeDAO = (LanguageTypeDAO) ctx.getBean("languageTypeDAO");
    
	/** file version relational data access */
	FileVersionDAO fileVersionDAO = (FileVersionDAO) ctx .getBean("fileVersionDAO");
	
	/** Person relational data access  */
	PersonNameAuthorityDAO personNameAuthorityDAO = (PersonNameAuthorityDAO) ctx.getBean("personNameAuthorityDAO");
		
    /** Contributor type relational data access  */
    ContributorTypeDAO contributorTypeDAO = (ContributorTypeDAO) ctx .getBean("contributorTypeDAO");

    /** Contributor data access.  */
    ContributorDAO contributorDAO = (ContributorDAO) ctx .getBean("contributorDAO");
    
	/** Identifier type relational data access  */
	IdentifierTypeDAO identifierTypeDAO = (IdentifierTypeDAO) ctx.getBean("identifierTypeDAO");
	
	/** Item identifier relational data access  */
	ItemIdentifierDAO itemIdentifierDAO = (ItemIdentifierDAO) ctx.getBean("itemIdentifierDAO");

    /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx
	.getBean("irUserDAO");
    
    IrFileDAO irFileDAO = 
    	(IrFileDAO) ctx.getBean("irFileDAO");
    
 	
	/**
	 * Setup for testing
	 * 
	 * this deletes exiting test directories if they exist
	 */
	@BeforeMethod
	public void cleanDirectory() {
		try {
			File f = new File( properties.getProperty("a_repo_path") );
			if(f.exists())
			{
			    FileUtils.forceDelete(f);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * Test Item persistence
	 */
	@Test
	public void baseItemDAOTest() {
		
		TransactionStatus ts = tm.getTransaction(td);
		ContentType ct = new ContentType("contentType");

		// persist the content type
		contentTypeDAO.makePersistent(ct);
		
 		LanguageType lt = new LanguageType();
		lt.setName("languageName");
 		lt.setDescription("languageDescription");
        
 		// persist the language type
        languageTypeDAO.makePersistent(lt);
        

		tm.commit(ts);
		
		ts = tm.getTransaction(td);

		GenericItem item = new GenericItem("the", "item1");
		
		ItemContentType itemContentType = item.setPrimaryContentType(contentTypeDAO.getById(ct.getId(), false));
		item.setLanguageType(languageTypeDAO.getById(lt.getId(), false));
		itemDAO.makePersistent(item);
		
		tm.commit(ts);
		
	
        // Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		
		GenericItem other = itemDAO.getById(item.getId(), false);
		assert other != null : "Should be able to find " +
		"the item by id";
		
		assert other.equals(item) : "GenericItem should be equal";
		assert other.getPrimaryItemContentType().equals(itemContentType) : other.getPrimaryItemContentType() + " Content type should be equal to " + itemContentType;
		assert other.getLanguageType().equals(lt) : "Langauge types should be equal";
		assert item.getName().equals("item1") : " should equal item1 but equal " + item.getName();
		assert item.getLeadingNameArticles().equals("the") : " should equal the but equals " + item.getLeadingNameArticles();
		
		// delete the ir collection
		itemDAO.makeTransient(other);
		
		assert itemDAO.getById(other.getId(), false ) == null : "Should not be able to find " +
			"other";
		
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
 		//create a new transaction
		ts = tm.getTransaction(td);
		
		languageTypeDAO.makeTransient(languageTypeDAO.getById(lt.getId(), false));
		contentTypeDAO.makeTransient(contentTypeDAO.getById(ct.getId(), false));
		assert contentTypeDAO.getById(ct.getId(), false) == null : "Should not find content type";
		tm.commit(ts);	
	}
	
	
	
	/**
	 * Test deleting the first available date
	 */
	@Test
	public void itemDAODeleteFirstAvailableDateTest() {
		
		TransactionStatus ts = tm.getTransaction(td);
		GenericItem item = new GenericItem("the", "item1");
		item.updateFirstAvailableDate(5,13, 2008);
		itemDAO.makePersistent(item);
		
		tm.commit(ts);

        // Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		
		GenericItem other = itemDAO.getById(item.getId(), false);
		assert other != null : "Should be able to find " +
		"the item by id";
		
		FirstAvailableDate d = other.getFirstAvailableDate();
 		assert d.getMonth() == 5 : "Month equals " + d.getMonth();
 		assert d.getDay() == 13 : "Day equals " + d.getDay();
 		assert d.getYear() == 2008 : "Day equals " + d.getYear();
 		
 		other.updateFirstAvailableDate(0,0,0);
 		itemDAO.makePersistent(other);
 		tm.commit(ts);
 		
 		ts = tm.getTransaction(td);
 		other = itemDAO.getById(item.getId(), false);
 		assert other.getFirstAvailableDate() == null : "fist available date should be null";
		// delete the ir collection
		itemDAO.makeTransient(other);
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		assert itemDAO.getById(other.getId(), false ) == null : "Should not be able to find " +
			"other";
		
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
	}
	
	/**
	 * Test deleting the original creation date
	 */
	@Test
	public void itemDAODeleteOriginalItemCreationDateTest() {
		
		TransactionStatus ts = tm.getTransaction(td);
		GenericItem item = new GenericItem("the", "item1");
		item.updateOriginalItemCreationDate(5,13, 2008);
		itemDAO.makePersistent(item);
		
		tm.commit(ts);
		

        // Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		
		GenericItem other = itemDAO.getById(item.getId(), false);
		assert other != null : "Should be able to find " +
		"the item by id";
		
		OriginalItemCreationDate d = other.getOriginalItemCreationDate();
 		assert d.getMonth() == 5 : "Month equals " + d.getMonth();
 		assert d.getDay() == 13 : "Day equals " + d.getDay();
 		assert d.getYear() == 2008 : "Day equals " + d.getYear();
 		
 		other.updateOriginalItemCreationDate(0,0,0);
 		itemDAO.makePersistent(other);
 		tm.commit(ts);
 		
 		
 		
 		
 		ts = tm.getTransaction(td);
 		other = itemDAO.getById(item.getId(), false);
 		assert other.getOriginalItemCreationDate() == null : "original item creation date should be null";
		// delete the ir collection
		itemDAO.makeTransient(other);
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		assert itemDAO.getById(other.getId(), false ) == null : "Should not be able to find " +
			"other";
		
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
	}
	
	/**
	 * Test deleting the first available date
	 */
	@Test
	public void itemDAODeleteExternalPublishedItemTest() {
		
		TransactionStatus ts = tm.getTransaction(td);
		GenericItem item = new GenericItem("the", "item1");
		ExternalPublishedItem externalPublishedItem = item.createExternalPublishedItem();
		externalPublishedItem.updatePublishedDate(1, 1, 2010);
		itemDAO.makePersistent(item);
		
		tm.commit(ts);

        // Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		
		GenericItem other = itemDAO.getById(item.getId(), false);
		assert other != null : "Should be able to find " +
		"the item by id";
		externalPublishedItem = other.getExternalPublishedItem();
		assert externalPublishedItem != null : "Should find extrnally publised item";
		assert externalPublishedItem.getPublishedDate().getMonth() == 1 : " Equals " +  externalPublishedItem.getPublishedDate().getMonth();
		assert externalPublishedItem.getPublishedDate().getDay() == 1 : " Equals " +  externalPublishedItem.getPublishedDate().getDay();
		assert externalPublishedItem.getPublishedDate().getYear() == 2010 : " Equals " +  externalPublishedItem.getPublishedDate().getYear(); ;
		other.deleteExternalPublishedItem();
		assert other.getExternalPublishedItem() == null : "external published item should be null";
 		itemDAO.makePersistent(other);
 		tm.commit(ts);
 		
 		ts = tm.getTransaction(td);
 		other = itemDAO.getById(item.getId(), false);
 		assert other.getExternalPublishedItem() == null : "published item should be null";
		// delete the item
		itemDAO.makeTransient(other);
		tm.commit(ts);
		
	}
	
	/**
	 * Test add a file to an item.
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void addFileItemDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException, LocationAlreadyExistsException {

		TransactionStatus ts = tm.getTransaction(td);
		
		// create a repository to store files in.
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		tm.commit(ts);

		// start a new transaction
		ts = tm.getTransaction(td);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - ItemDAO test");
		
		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f, "myIrFile");
		GenericItem item = new GenericItem("item1");

		UserEmail userEmail = new UserEmail("email");
		
		// create a user and add the versioned file to the item
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		userDAO.makePersistent(user);

		IrFile irFile = new IrFile(fileInfo1, "myIrFile");
		irFileDAO.makePersistent(irFile);
		
		ItemFile myItemFile = item.addFile(irFile);
		myItemFile.setOrder(1);
		
		// save the versioned ir file
		//fileVersionDAO.makePersistent(fileVersion);
		itemDAO.makePersistent(item);
		
		// save everything.
		tm.commit(ts);
		
        // Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		
		GenericItem other = itemDAO.getById(item.getId(), false);
		assert other != null : "Should be able to find " +
		"the item by id";
		
		assert other.equals(item) : "GenericItem should be equal";
		assert other.getItemFile("myIrFile") != null;
		assert myItemFile.equals(other.getItemFile("myIrFile")) : "GenericItem File = " + myItemFile + " other = " + 
		other.getItemFile("myIrFile");
		itemDAO.makeTransient(other);
		
		assert itemDAO.getById(other.getId(), false ) == null : "Should not be able to find other";
		irFileDAO.makeTransient(irFileDAO.getById(irFile.getId(), false));

		// commit the transaction
		tm.commit(ts);

			
		//create a new transaction
		ts = tm.getTransaction(td);
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test add a primary image file to an item.
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void addPrimaryImageFileItemDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException, LocationAlreadyExistsException {

		TransactionStatus ts = tm.getTransaction(td);
		
		// create a repository to store files in.
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		tm.commit(ts);

		// start a new transaction
		ts = tm.getTransaction(td);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - ItemDAO test");
		
		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f, "myIrFile");
		GenericItemDAO itemDAO = (GenericItemDAO) ctx.getBean("itemDAO");
		
		GenericItem item = new GenericItem("item1");

		UserEmail userEmail = new UserEmail("email");
		
		// create a user and add the versioned file to the item
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		userDAO.makePersistent(user);

		IrFile irFile = new IrFile(fileInfo1, "myIrFile");
		irFileDAO.makePersistent(irFile);
		
		ItemFile myItemFile = item.addFile(irFile);
		myItemFile.setOrder(1);
		
		assert item.addPrimaryImageFile(myItemFile) == true : "Should be able to set primary image file";
		assert item.getPrimaryImageFile() != null : "primary image file should not be null";
		// save the versioned ir file
		//fileVersionDAO.makePersistent(fileVersion);
		itemDAO.makePersistent(item);
		
		// save everything.
		tm.commit(ts);
		
        // Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		
		GenericItem other = itemDAO.getById(item.getId(), false);
		assert other != null : "Should be able to find " +
		"the item by id";
		
		assert other.equals(item) : "GenericItem should be equal";
		assert other.getItemFile("myIrFile") != null;
		assert other.getPrimaryImageFile() != null : "Should be able to get primary image file";
		assert myItemFile.equals(other.getItemFile("myIrFile")) : "GenericItem File = " + myItemFile + " other = " + other; 
		assert myItemFile.equals(other.getPrimaryImageFile()) : "Primary image file = " + other.getPrimaryImageFile() + " myItemFile = " + myItemFile;
		other.getItemFile("myIrFile");
		itemDAO.makeTransient(other);
		
		assert itemDAO.getById(other.getId(), false ) == null : "Should not be able to find other";
		irFileDAO.makeTransient(irFileDAO.getById(irFile.getId(), false));

		// commit the transaction
		tm.commit(ts);

			
		//create a new transaction
		ts = tm.getTransaction(td);
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test add a contributor to an item.
	 */
	@Test
	public void addItemContributorDAOTest() throws Exception{
		
		TransactionStatus ts = tm.getTransaction(td);
   		ContributorType ct = new ContributorType("ctName");
 		ct.setDescription("ctDescription");

  		ContributorType ct2 = new ContributorType("ctName2");
 		
 		ct2.setDescription("ctDescription2");
        contributorTypeDAO.makePersistent(ct);
        contributorTypeDAO.makePersistent(ct2);

 		PersonName name = new PersonName();
		name.setFamilyName("familyName");
		name.setForename("forename");
		name.setInitials("n.d.s.");
		name.setMiddleName("MiddleName");
		name.setNumeration("III");
		name.setSurname("surname");
		
		PersonNameAuthority p = new PersonNameAuthority(name);
		p.addBirthDate(2005);
		p.addDeathDate(2105);
		
		
		personNameAuthorityDAO.makePersistent(p);
		tm.commit(ts);

		ts = tm.getTransaction(td);
		
		GenericItem item = new GenericItem("item1");
		Contributor contrib = new Contributor();
		contrib.setContributorType(ct);
		contrib.setPersonName(name);
		
		//add the contributor and persist the item
		ItemContributor itemContributor = item.addContributor(contrib);
		itemDAO.makePersistent(item);
		
	    //complete the transaction
		tm.commit(ts);
        
		// Start the transaction
        ts = tm.getTransaction(td);
        
		GenericItem other = itemDAO.getById(item.getId(), false);
		assert other.getContributors().contains(itemContributor) : "The item should contain " + itemContributor;
		// check the count for all the contributions to "ALL ITEMS" this person name has made
		assert itemDAO.getItemContributionCount(contrib) == 1 : "Should only have contributed to one item";
		List<ContributorType> contribTypes = itemDAO.getPossibleContributions(contrib.getPersonName().getId(), 
				item.getId());
		
		// there should only be one contributor type for the person 
		assert contribTypes.size() == 1 : "There should only be one contributor type for person but there are " 
			+ contribTypes.size();
		assert contribTypes.contains(ct2) : "Should contain " + ct2;
		
		
		// you should get all contributor types when you don't find the person
		contribTypes = itemDAO.getPossibleContributions(0l, 0l);
		
		assert contribTypes.size() == 2 : "Both contributor types should be available but ther are " 
			+ contribTypes.size();
		
		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		item = itemDAO.getById(item.getId(), false);
		itemContributor= item.getContributor(contrib);
		item.removeContributor(itemContributor);
		itemDAO.makePersistent(item);
		assert itemDAO.getItemContributionCount(contrib) == 0 : "This person should have not cont";
		
		contributorDAO.makeTransient(itemContributor.getContributor());
		
		
		ct = contributorTypeDAO.getById(ct.getId(), false);
		contributorTypeDAO.makeTransient(ct);
		
		ct2 = contributorTypeDAO.getById(ct2.getId(), false);
		contributorTypeDAO.makeTransient(ct2);
		
		p = personNameAuthorityDAO.getById(p.getId(), false);
		personNameAuthorityDAO.makeTransient(p);
		itemDAO.makeTransient(item);
		tm.commit(ts);
	}
	

	
	/**
	 * Test add a item identifier.
	 */
	@Test
	public void addItemIdentifierDAOTest() throws Exception{

		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		IdentifierType identifierType = new IdentifierType("identTypeName", "identTypeDescription");
 		identifierTypeDAO.makePersistent(identifierType);
 
		GenericItem item = new GenericItem("item1");

		//add the item identifier and persist the item
		ItemIdentifier itemIdentifier = item.addItemIdentifier("122345", identifierType);
		itemDAO.makePersistent(item);
		tm.commit(ts);
		
        // Start the transaction 
        ts = tm.getTransaction(td);
        
		GenericItem other = itemDAO.getById(item.getId(), false);
		assert other.getItemIdentifiers().contains(itemIdentifier) : "The item should contain " + itemIdentifier;
    	List<IdentifierType> identifierTypes = itemDAO.getPossibleIdentifierTypes(other.getId());
		
		// there should only be one contributor type for the person 
		assert identifierTypes.size() == 0 : "There should only be 0 identifier types that can be used " 
			+ identifierTypes.size();

		// you should get all identifier types when you don't find the person
		identifierTypes = itemDAO.getPossibleIdentifierTypes(0L);
		
		assert identifierTypes.size() == 1 : "One Identifier type should be available but ther are " 
			+ identifierTypes.size();
		
		//complete the transaction
		tm.commit(ts);
		
		item.removeItemIdentifier(itemIdentifier);
		itemIdentifierDAO.makeTransient(itemIdentifier);
		
		ts = tm.getTransaction(td);
		// reload the item
		item = itemDAO.getById(item.getId(), false);
		assert item.getItemIdentifiers().size() == 0 : "Should not have any item identifiers";
		//complete the transaction
		
		identifierTypeDAO.makeTransient(identifierType);
		itemDAO.makeTransient(item);
		tm.commit(ts);
		
	
		
		
	}
	
	
	/**
	 * Test add links to an item.
	 */
	@Test
	public void addItemLinkDAOTest() throws Exception{
		
        // Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
   
		GenericItem item = new GenericItem("item1");

		ItemLink itemLink1 = item.createLink("link1", "http://www.hotmail.com");

		ItemLink itemLink2 = item.createLink("link2",
				"http://www.msnbc.com");
		

		itemDAO.makePersistent(item);
        //complete the transaction
		tm.commit(ts);
		
	    // Start the transaction 
		ts = tm.getTransaction(td);
		
		GenericItem other = itemDAO.getById(item.getId(), false);
		assert other != null;
		
		// should have 2 links
		assert other.getLinks().size() == 2 : "Size should be 2 but is " + other.getLinks().size();
		assert other.getLinks().contains(itemLink1) : "Link one should be in the set";
		assert other.getLinks().contains(itemLink2) : "Link two should be in the set";
		itemDAO.makeTransient(other);
		tm.commit(ts);
		
	}
	
   

}
