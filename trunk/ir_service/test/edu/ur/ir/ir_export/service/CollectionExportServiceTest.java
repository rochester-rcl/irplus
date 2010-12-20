package edu.ur.ir.ir_export.service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.ir_export.CollectionExportService;
import edu.ur.ir.ir_import.CollectionImportService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;

/**
 * Helper to test the collection export/import system
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class CollectionExportServiceTest {
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** Repository data access */
	RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");

	/** Collection export service */
	CollectionExportService collectionExportService = (CollectionExportService) ctx.getBean("collectionExportService");
	
	/** Collection import service */
	CollectionImportService collectionImportService = (CollectionImportService) ctx.getBean("collectionImportService");
	
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
	
    /** user service */
    UserService userService = (UserService) ctx.getBean("userService");

	
	/**
	 * Test exporting an institutional collection
	 * 
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 * @throws IOException 
	 * @throws UserDeletedPublicationException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws IllegalFileSystemNameException 
	 */
	public void testCreateXmlFile() throws LocationAlreadyExistsException, 
	DuplicateNameException,
	IOException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException
	{
		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("nate collection");
		collection.setDescription("my description");
		collection.setCopyright("copyright statement");
        collection.addLink("msnbc", "http://www.msnbc.com");
        collection.addLink("hotmail", "http://www.hotmail.com");
		
        InstitutionalCollection child = collection.createChild("sub child");
        InstitutionalCollection subChild = child.createChild("sub sub child");
        subChild.addLink("google", "http://www.google.com");
		institutionalCollectionService.saveCollection(collection);
		
		String userEmail1 = properties.getProperty("user_1_email");
		UserEmail email = new UserEmail(userEmail1);
		IrUser user = userService.createUser("password", "username", email);

		
	  	// location for the default file database
		String collectionXml = properties.getProperty("collection_xml_file");
		
		// create the export folder
		File collectionXmlFile = new File(collectionXml);
		
        // add the item to the index
		tm.commit(ts);
		
		// test searching for the data
		ts = tm.getTransaction(td);
		LinkedList<InstitutionalCollection> collections = new LinkedList<InstitutionalCollection>(); 
		collections.add(collection);
        collectionExportService.createXmlFile(collectionXmlFile, collections, true);
		tm.commit(ts);
		
	    // Start new transaction - clean up the data
		ts = tm.getTransaction(td);
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(),false), user);
        IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test exporting an institutional collection to z zip file
	 * 
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 * @throws IOException 
	 * @throws UserDeletedPublicationException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws IllegalFileSystemNameException 
	 */
	public void testCreateZipFile() throws LocationAlreadyExistsException, 
	DuplicateNameException,
	IOException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException, IllegalFileSystemNameException 
	{
		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("nate collection");
		collection.setDescription("my description");
		collection.setCopyright("copyright statement");
        collection.addLink("msnbc", "http://www.msnbc.com");
        collection.addLink("hotmail", "http://www.hotmail.com");

        
        InstitutionalCollection child = collection.createChild("sub collection");
        child.setDescription("child description");
		child.setCopyright("child copyright statement");
        child.addLink("child msnbc", "http://www.msnbc.com");
        child.addLink("child hotmail", "http://www.hotmail.com");
        
        InstitutionalCollection subChild = child.createChild("sub sub collection");
        subChild.setDescription("sub child description");
        subChild.setCopyright("sub child copyright statement");
        subChild.addLink("sub child msnbc", "http://www.msnbc.com");
        subChild.addLink("sub child hotmail", "http://www.hotmail.com");
        
		institutionalCollectionService.saveCollection(collection);
		
		String userEmail1 = properties.getProperty("user_1_email");
		UserEmail email = new UserEmail(userEmail1);
		IrUser user = userService.createUser("password", "username", email);

		
	  	// location for the default file database
		String collectionZip = properties.getProperty("collection_zip_file");
		
		// create the export folder
		File collectionZipFile = new File(collectionZip);
		
        // add the item to the index
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		
		String path = properties.getProperty("ir_service_location");
		String fileName = properties.getProperty("ur_research_home_jpg");
		File f = new File(path + fileName);
		
		collection = institutionalCollectionService.getCollection(collection.getId(), false);
		IrFile collectionLogo = repositoryService.createIrFile(repo, f, "logo_1.jpg", "logo for collection");
        collection.setPrimaryPicture(collectionLogo);
        institutionalCollectionService.saveCollection(collection);
        
		child = institutionalCollectionService.getCollection(child.getId(), false);
		collectionLogo = repositoryService.createIrFile(repo, f, "logo_2.jpg", "logo for collection");
        child.setPrimaryPicture(collectionLogo);
        institutionalCollectionService.saveCollection(child);
        
		subChild = institutionalCollectionService.getCollection(subChild.getId(), false);
		collectionLogo = repositoryService.createIrFile(repo, f, "logo_3.jpg", "logo for collection");
        subChild.setPrimaryPicture(collectionLogo);
        institutionalCollectionService.saveCollection(subChild);
        
        
		tm.commit(ts);
		
		
		// test searching for the data
		ts = tm.getTransaction(td);
		collection = institutionalCollectionService.getCollection(collection.getId(), false);
		collectionExportService.export(collection, true, collectionZipFile);
		
		tm.commit(ts);
		
	    // Start new transaction - clean up the data
		ts = tm.getTransaction(td);
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(),false), user);
        collection = institutionalCollectionService.getCollection(collection.getId(), false);
        child = institutionalCollectionService.getCollection(child.getId(), false);
        subChild = institutionalCollectionService.getCollection(subChild.getId(), false);

        assert collection == null : "Collection should be deleted";
        assert child == null : "Child should be deleted";
        assert subChild == null : "Sub child should be deleted";

        tm.commit(ts);
        
	    // Start new transaction - clean up the data
		ts = tm.getTransaction(td);
		repo = repositoryService.getRepository(repo.getId(), false);		
		collectionImportService.importCollections(collectionZipFile, repo);
		tm.commit(ts);       
		
		ts = tm.getTransaction(td);
		repo = repositoryService.getRepository(repo.getId(), false);				
		assert repo.getInstitutionalCollections().size() == 1 : "Should have one collection";
		collection = repo.getInstitutionalCollection("nate collection");
		assert collection != null : "Collection should exist";
		assert collection.getPrimaryPicture() != null : "Primary picture should exist";
		assert collection.getLink("msnbc") != null : "Should find link for msnbc";
		assert collection.getLink("hotmail") != null : "Should find link for hotmail";
		assert collection.getCopyright().equals("copyright statement") : "Copyright should be equal " + collection.getCopyright();
		assert collection.getDescription().equals("my description") : "Description should be equal " + collection.getDescription();

        child = collection.getChild("sub collection");
		assert child != null : "Collection should exist";
		assert child.getPrimaryPicture() != null : "Primary picture should exist";
		assert child.getLink("child msnbc") != null : "Should find link for msnbc";
		assert child.getLink("child hotmail") != null : "Should find link for hotmail";
		assert child.getCopyright().equals("child copyright statement") : "Copyright should be equal " + child.getCopyright();
		assert child.getDescription().equals("child description") : "Description should be equal " + child.getDescription();

        
        subChild = child.getChild("sub sub collection");
		assert subChild != null : "Collection should exist";
		assert subChild.getPrimaryPicture() != null : "Primary picture should exist";
		assert subChild.getLink("sub child msnbc") != null : "Should find link for msnbc";
		assert subChild.getLink("sub child hotmail") != null : "Should find link for hotmail";
		assert subChild.getCopyright().equals("sub child copyright statement") : "Copyright should be equal " + subChild.getCopyright();
		assert subChild.getDescription().equals("sub child description") : "Description should be equal " + subChild.getDescription();

		
		tm.commit(ts);        
        
        ts = tm.getTransaction(td);
        IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(),false), user);
        helper.cleanUpRepository();
		tm.commit(ts);	

	}
	
	/**
	 * Test exporting a repository to a zip file
	 * 
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 * @throws IOException 
	 * @throws UserDeletedPublicationException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws IllegalFileSystemNameException 
	 */
	public void testExportRepository() throws LocationAlreadyExistsException, 
	DuplicateNameException,
	IOException, 
	UserHasPublishedDeleteException, 
	UserDeletedPublicationException, IllegalFileSystemNameException 
	{
		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("nate collection");
		collection.setDescription("my description");
		collection.setCopyright("copyright statement");
        collection.addLink("msnbc", "http://www.msnbc.com");
        collection.addLink("hotmail", "http://www.hotmail.com");

        
        InstitutionalCollection child = collection.createChild("sub collection");
        child.setDescription("child description");
		child.setCopyright("child copyright statement");
        child.addLink("child msnbc", "http://www.msnbc.com");
        child.addLink("child hotmail", "http://www.hotmail.com");
        
        InstitutionalCollection subChild = child.createChild("sub sub collection");
        subChild.setDescription("sub child description");
        subChild.setCopyright("sub child copyright statement");
        subChild.addLink("sub child msnbc", "http://www.msnbc.com");
        subChild.addLink("sub child hotmail", "http://www.hotmail.com");
        
		institutionalCollectionService.saveCollection(collection);
		
		String userEmail1 = properties.getProperty("user_1_email");
		UserEmail email = new UserEmail(userEmail1);
		IrUser user = userService.createUser("password", "username", email);

		
	  	// location for the default file database
		String collectionZip = properties.getProperty("collection_zip_file");
		
		// create the export folder
		File collectionZipFile = new File(collectionZip);
		
        // add the item to the index
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		
		String path = properties.getProperty("ir_service_location");
		String fileName = properties.getProperty("ur_research_home_jpg");
		File f = new File(path + fileName);
		
		collection = institutionalCollectionService.getCollection(collection.getId(), false);
		IrFile collectionLogo = repositoryService.createIrFile(repo, f, "logo_1.jpg", "logo for collection");
        collection.setPrimaryPicture(collectionLogo);
        institutionalCollectionService.saveCollection(collection);
        
		child = institutionalCollectionService.getCollection(child.getId(), false);
		collectionLogo = repositoryService.createIrFile(repo, f, "logo_2.jpg", "logo for collection");
        child.setPrimaryPicture(collectionLogo);
        institutionalCollectionService.saveCollection(child);
        
		subChild = institutionalCollectionService.getCollection(subChild.getId(), false);
		collectionLogo = repositoryService.createIrFile(repo, f, "logo_3.jpg", "logo for collection");
        subChild.setPrimaryPicture(collectionLogo);
        institutionalCollectionService.saveCollection(subChild);
        
        
		tm.commit(ts);
		
		
		// test searching for the data
		ts = tm.getTransaction(td);
		repo = repositoryService.getRepository(repo.getId(), false);;
		collectionExportService.export(repo, collectionZipFile);
		
		tm.commit(ts);
		
	    // Start new transaction - clean up the data
		ts = tm.getTransaction(td);
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(),false), user);
        collection = institutionalCollectionService.getCollection(collection.getId(), false);
        child = institutionalCollectionService.getCollection(child.getId(), false);
        subChild = institutionalCollectionService.getCollection(subChild.getId(), false);

        assert collection == null : "Collection should be deleted";
        assert child == null : "Child should be deleted";
        assert subChild == null : "Sub child should be deleted";

        tm.commit(ts);
        
	    // Start new transaction - clean up the data
		ts = tm.getTransaction(td);
		repo = repositoryService.getRepository(repo.getId(), false);		
		collectionImportService.importCollections(collectionZipFile, repo);
		tm.commit(ts);       
		
		ts = tm.getTransaction(td);
		repo = repositoryService.getRepository(repo.getId(), false);				
		assert repo.getInstitutionalCollections().size() == 1 : "Should have one collection";
		collection = repo.getInstitutionalCollection("nate collection");
		assert collection != null : "Collection should exist";
		assert collection.getPrimaryPicture() != null : "Primary picture should exist";
		assert collection.getLink("msnbc") != null : "Should find link for msnbc";
		assert collection.getLink("hotmail") != null : "Should find link for hotmail";
		assert collection.getCopyright().equals("copyright statement") : "Copyright should be equal " + collection.getCopyright();
		assert collection.getDescription().equals("my description") : "Description should be equal " + collection.getDescription();

        child = collection.getChild("sub collection");
		assert child != null : "Collection should exist";
		assert child.getPrimaryPicture() != null : "Primary picture should exist";
		assert child.getLink("child msnbc") != null : "Should find link for msnbc";
		assert child.getLink("child hotmail") != null : "Should find link for hotmail";
		assert child.getCopyright().equals("child copyright statement") : "Copyright should be equal " + child.getCopyright();
		assert child.getDescription().equals("child description") : "Description should be equal " + child.getDescription();

        
        subChild = child.getChild("sub sub collection");
		assert subChild != null : "Collection should exist";
		assert subChild.getPrimaryPicture() != null : "Primary picture should exist";
		assert subChild.getLink("sub child msnbc") != null : "Should find link for msnbc";
		assert subChild.getLink("sub child hotmail") != null : "Should find link for hotmail";
		assert subChild.getCopyright().equals("sub child copyright statement") : "Copyright should be equal " + subChild.getCopyright();
		assert subChild.getDescription().equals("sub child description") : "Description should be equal " + subChild.getDescription();

		
		tm.commit(ts);        
        
        ts = tm.getTransaction(td);
        IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
        institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(),false), user);
        helper.cleanUpRepository();
		tm.commit(ts);	

	}
}
