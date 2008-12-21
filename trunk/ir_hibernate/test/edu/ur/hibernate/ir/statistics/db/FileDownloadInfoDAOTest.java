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

package edu.ur.hibernate.ir.statistics.db;

import java.io.File;
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
import edu.ur.file.db.FileInfo;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionDAO;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemDownloadCount;
import edu.ur.ir.item.DuplicateContributorException;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorDAO;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeDAO;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonNameAuthorityDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.statistics.FileDownloadInfo;
import edu.ur.ir.statistics.FileDownloadInfoDAO;
import edu.ur.ir.statistics.IgnoreIpAddress;
import edu.ur.ir.statistics.IgnoreIpAddressDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.util.FileUtil;


/**
 * Test file download info persistance
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class FileDownloadInfoDAOTest {

	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	FileDownloadInfoDAO fileDownloadInfoDAO = (FileDownloadInfoDAO) ctx
	.getBean("fileDownloadInfoDAO");
	
	/** persistance for dealing with ignoring ip addesses */
	IgnoreIpAddressDAO ignoreIpAddressDAO = (IgnoreIpAddressDAO) ctx
	.getBean("ignoreIpAddressDAO");
	
	/** persistance for dealing with contributor Type*/
	ContributorTypeDAO contributorTypeDAO = (ContributorTypeDAO) ctx
	.getBean("contributorTypeDAO");
	
	/** persistance for dealing with contributor */
	ContributorDAO contributorDAO = (ContributorDAO) ctx
	.getBean("contributorDAO");
	
	PersonNameAuthorityDAO personNameAuthorityDAO = (PersonNameAuthorityDAO) ctx.getBean("personNameAuthorityDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);

	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	 /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");
    
    /** ir file data access */
    IrFileDAO irFileDAO = 
    	(IrFileDAO) ctx.getBean("irFileDAO");
    
    /** Generic item data access */
    GenericItemDAO itemDAO = (GenericItemDAO) ctx.getBean("itemDAO");
    
	/** Institution collection data access.  */
	InstitutionalCollectionDAO institutionalCollectionDAO = (InstitutionalCollectionDAO) ctx
	.getBean("institutionalCollectionDAO");
    
	/**
	 * Test download info persistance
	 */
	@Test
	public void baseDownloadInfoDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);

	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy");
	    Date d = simpleDateFormat.parse("1/1/2008");
        FileDownloadInfo downloadInfo1 = new FileDownloadInfo("123.0.0.1", 1l,d);
        downloadInfo1.setCount(1);
        
        fileDownloadInfoDAO.makePersistent(downloadInfo1);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		FileDownloadInfo other = fileDownloadInfoDAO.getById(downloadInfo1.getId(), false);
        assert other.equals(downloadInfo1) : "File download info should be equal other = \n" + other + "\n downloadInfo1 = " + downloadInfo1;
         
        fileDownloadInfoDAO.makeTransient(other);
        assert  fileDownloadInfoDAO.getById(other.getId(), false) == null : "Should no longer be able to find file download info";
	    tm.commit(ts);
	}
	

	
	/**
	 * Test download counts.
	 * 
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws UserHasPublishedDeleteException
	 * @throws ParseException 
	 */
	@Test
	public void numberOfFileDownloadsForCollectionsTest() throws DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, ParseException {

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
		IrFile irFile1 = new IrFile(fileInfo1, "myIrFile");
		irFileDAO.makePersistent(irFile1);

		File f2 = testUtil.creatFile(directory, "testFile2", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test2");
		FileInfo fileInfo2 = repo.getFileDatabase().addFile(f2, "newFile2");
		IrFile irFile2 = new IrFile(fileInfo2, "myIrFile2");
		irFileDAO.makePersistent(irFile2);
		// save the repository
		tm.commit(ts);	
		
        // Start the transaction - create collections
		ts = tm.getTransaction(td);
		
		GenericItem item1 = new GenericItem("itemName1");
		item1.addFile(irFile1);
		itemDAO.makePersistent(item1);

		GenericItem item2 = new GenericItem("itemName2");
		item2.addFile(irFile2);
		itemDAO.makePersistent(item2);
		
		// create the collections
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		collection.createInstitutionalItem(item1);
		InstitutionalCollection subCollection = collection.createChild("subChild");
		subCollection.createInstitutionalItem(item2);
		
		institutionalCollectionDAO.makePersistent(collection);
   
		// save the repository
		tm.commit(ts);	

		// create some downloads
        ts = tm.getTransaction(td);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy");
	    Date d = simpleDateFormat.parse("1/1/2008");
	    
        FileDownloadInfo downloadInfo1 = new FileDownloadInfo("123.0.0.1", irFile1.getId(), d);
        downloadInfo1.setCount(1);
        fileDownloadInfoDAO.makePersistent(downloadInfo1);
       
        FileDownloadInfo downloadInfo2 = new FileDownloadInfo("123.0.0.7", irFile2.getId(), d);
        downloadInfo2.setCount(2);
        fileDownloadInfoDAO.makePersistent(downloadInfo2);
	    tm.commit(ts);

        
        ts = tm.getTransaction(td);
		Long count = fileDownloadInfoDAO.getNumberOfFileDownloadsForRepository();
		assert count.equals(3l) : "Should find 3 but found " + count;
		
		// test with count for collection files only
		Long collectionCount1 = fileDownloadInfoDAO.getNumberOfFileDownloadsForCollection(collection);
		assert collectionCount1.equals(1l) : "Should find 1 but found " + collectionCount1 ;
		
		// test with count for sub-collection files only
		Long collectionCount2 = fileDownloadInfoDAO.getNumberOfFileDownloadsForCollection(subCollection);
		assert collectionCount2.equals(2l) : "Should find 2 but found " + collectionCount2 ;
		
		// test with count for ir file
		Long irFileCount = fileDownloadInfoDAO.getNumberOfFileDownloadsForIrFile(irFile1);
		assert irFileCount.equals(1l) : "Should find 1 but found " + irFileCount ;
		
		irFileCount = fileDownloadInfoDAO.getNumberOfFileDownloadsForIrFile(irFile2);
		assert irFileCount.equals(2l) : "Should find 2 but found " + irFileCount ;
		
		// test with parent
		Long collectionCountWithChildren = fileDownloadInfoDAO.getNumberOfFileDownloadsForCollectionIncludingChildren(collection);
		assert collectionCountWithChildren.equals(3l) : "Should find 3 but found " + collectionCountWithChildren;
		
		// test with sub collection
		collectionCountWithChildren = fileDownloadInfoDAO.getNumberOfFileDownloadsForCollectionIncludingChildren(subCollection);
		assert collectionCountWithChildren.equals(2l) : "Should find 2 but found " + collectionCountWithChildren;
		tm.commit(ts);
		
		
		// add an ip address to the ignore list
	    //create a new transaction
		ts = tm.getTransaction(td);
	    IgnoreIpAddress ip1 = new IgnoreIpAddress(123,0,0,7, 7);
        ignoreIpAddressDAO.makePersistent(ip1);
        tm.commit(ts);
        
		
	    //create a new transaction
		ts = tm.getTransaction(td);
		count = fileDownloadInfoDAO.getNumberOfFileDownloadsForRepository();
		assert count.equals(1l) : "Should find 1 but found " + count;
		
		collectionCount1 = fileDownloadInfoDAO.getNumberOfFileDownloadsForCollection(collection);
		assert collectionCount1.equals(1l) : "Should find 1 but found " + collectionCount1 ;
		
		collectionCount2 = fileDownloadInfoDAO.getNumberOfFileDownloadsForCollection(subCollection);
		assert collectionCount2.equals(0l) : "Should find 0 but found " + collectionCount2 ;
		
		irFileCount = fileDownloadInfoDAO.getNumberOfFileDownloadsForIrFile(irFile1);
		assert irFileCount.equals(1l) : "Should find 1 but found " + irFileCount ;
		
		irFileCount = fileDownloadInfoDAO.getNumberOfFileDownloadsForIrFile(irFile2);
		assert irFileCount.equals(0l) : "Should find 0 but found " + irFileCount ;
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		ignoreIpAddressDAO.makeTransient(ignoreIpAddressDAO.getById(ip1.getId(), false));
		institutionalCollectionDAO.makeTransient(institutionalCollectionDAO.getById(collection.getId(), false));
		
        itemDAO.makeTransient(itemDAO.getById(item1.getId(), false));
        itemDAO.makeTransient(itemDAO.getById(item2.getId(), false));
        
		irFileDAO.makeTransient(irFileDAO.getById(irFile1.getId(), false));
		irFileDAO.makeTransient(irFileDAO.getById(irFile2.getId(), false));
		
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		fileDownloadInfoDAO.makeTransient(fileDownloadInfoDAO.getById(downloadInfo1.getId(), false));
		fileDownloadInfoDAO.makeTransient(fileDownloadInfoDAO.getById(downloadInfo2.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}
	

	
	/**
	 * Test download counts for institutional item by person name.
	 * 
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws UserHasPublishedDeleteException
	 * @throws ParseException 
	 */
	@Test
	public void getInstitutionalItemDownloadCountByPersonNameTest() 
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
		IrFile irFile1 = new IrFile(fileInfo1, "myIrFile");
		irFileDAO.makePersistent(irFile1);

		File f2 = testUtil.creatFile(directory, "testFile2", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test2");
		FileInfo fileInfo2 = repo.getFileDatabase().addFile(f2, "newFile2");
		IrFile irFile2 = new IrFile(fileInfo2, "myIrFile2");
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
        downloadInfo1.setCount(1);
        fileDownloadInfoDAO.makePersistent(downloadInfo1);
       
        FileDownloadInfo downloadInfo2 = new FileDownloadInfo("123.0.0.7", irFile2.getId(), d);
        downloadInfo2.setCount(2);
        fileDownloadInfoDAO.makePersistent(downloadInfo2);
	    tm.commit(ts);

        
        ts = tm.getTransaction(td);
		long itemFileCount = fileDownloadInfoDAO.getNumberOfFileDownloadsForItem(item1);
		assert itemFileCount == 1 : "Should find 1 but found " + itemFileCount ;
		
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
		InstitutionalItemDownloadCount c = fileDownloadInfoDAO.getInstitutionalItemDownloadCountByPersonName(personNameIds);
		
		assert c.getCount() == 2 : "Count should be 2 but " + c.getCount();
		assert c.getInstitutionalItem().equals(ii2) : "Istitutional item should be equal" ;
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


}
