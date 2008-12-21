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


package edu.ur.ir.institution.service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.FacetSearchHelper;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexService;
import edu.ur.ir.institution.InstitutionalItemSearchService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeService;
import edu.ur.ir.item.DuplicateContributorException;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeService;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeService;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.PublisherService;
import edu.ur.ir.item.Series;
import edu.ur.ir.item.SeriesService;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeService;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.util.FileUtil;


/**
 * Test the service methods for the institutional item index services
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultInstitutionalItemIndexServiceTest {
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");
	
	/** Repository data access */
	RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
	
	/** User File System data access */
	UserFileSystemService userFileSystemService = (UserFileSystemService) ctx.getBean("userFileSystemService");
	
	/** Service for dealing with contributors */
	ContributorTypeService contributorTypeService = (ContributorTypeService) ctx.getBean("contributorTypeService");
	
	/** Item services */
	ItemService itemService = (ItemService) ctx.getBean("itemService");

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
	
	/** User data access */
	UserPublishingFileSystemService userPublishingFileSystemService = 
		(UserPublishingFileSystemService) ctx.getBean("userPublishingFileSystemService");
	
	/** person data access */
	PersonService personService = (PersonService) ctx
	.getBean("personService");
	
	/** Service for creating identifier types  */
	IdentifierTypeService identifierTypeService= (IdentifierTypeService)ctx
	.getBean("identifierTypeService");
	
	/** Series service for dealing with series */
	SeriesService seriesService = (SeriesService) ctx.getBean("seriesService");
	
	/** Service for dealing with content types */
	ContentTypeService contentTypeService = (ContentTypeService) ctx.getBean("contentTypeService");
	
	/** Service for dealing with publishers */
	PublisherService publisherService = (PublisherService) ctx.getBean("publisherService");
	
	/** Service for dealing with language types */
	LanguageTypeService languageTypeService = (LanguageTypeService) ctx.getBean("languageTypeService");
	
	/** Service for indexing institutional items */
	InstitutionalItemIndexService institutionalItemIndexService = 
		(InstitutionalItemIndexService) ctx.getBean("institutionalItemIndexService");
	
	/** service for dealing with institutional items */
	InstitutionalItemService institutionalItemService = (InstitutionalItemService)ctx.getBean("institutionalItemService");
	
	/** Service for searching institutional items */
	InstitutionalItemSearchService institutionalItemSearchService = 
		(InstitutionalItemSearchService) ctx.getBean("institutionalItemSearchService");
	
	

	
	/**
	 * Executes the query returning the number of hits.
	 * 
	 * @param field - field to search on
	 * @param queryString - query string
	 * @param dir - lucene index to search
	 * 
	 * @return - number of hits
	 * 
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 */
	private int executeQuery(String field, String queryString, Directory dir)
			throws CorruptIndexException, IOException, ParseException {
		IndexSearcher searcher = new IndexSearcher(dir);
		QueryParser parser = new QueryParser(field, new StandardAnalyzer());
		Query q1 = parser.parse(queryString);
		Hits hits = searcher.search(q1);
		int hitCount = hits.length();

		searcher.close();

		return hitCount;
	}
	
	/**
	 * Test indexing an institutional item
	 * 
	 * @throws NoIndexFoundException 
	 * @throws IllegalFileSystemNameException 
	 * @throws DuplicateNameException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws DuplicateContributorException 
	 */
	public void testIndexInstitutionalItem() throws NoIndexFoundException, IllegalFileSystemNameException, DuplicateNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, DuplicateContributorException 
	{
		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		institutionalCollectionService.saveCollection(collection);
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - Institutional Item Index Service test");

		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		
		// create a personal file to add to the item
		PersonalFile pf = userFileSystemService.addFileToUser(repo, f, user, 
		    		"test_file.txt", "description");
		
		// create a person name to add to the item as a contributor
		PersonName personName = new PersonName();
		personName.setFamilyName("familyName");
		personName.setForename("forename");
		personName.setInitials("n.d.s.");
		personName.setMiddleName("MiddleName");
		personName.setNumeration("III");
		personName.setSurname("surname");
		
		PersonNameAuthority p = new PersonNameAuthority(personName);
		personService.save(p);
		
        // create a contributor type
		ContributorType contributorType1 = new ContributorType("contributorType1");
		contributorTypeService.save(contributorType1);
		
		// create the contributor
		Contributor c = new Contributor();
		c.setPersonName(personName);
		c.setContributorType(contributorType1);
		
		// create an identifier
	    IdentifierType identType = new IdentifierType();
		identType.setName("identTypeName");
 		identType.setDescription("identTypeDescription");
 		
 		identifierTypeService.save(identType);
 		
		// create a series
 		Series series = new Series("my series", "12345");
 		seriesService.saveSeries(series);
 		
 		// create a content type
 		ContentType contentType = new ContentType("contentType");
 		contentTypeService.saveContentType(contentType);
 		
 		// create a publisher
 		Publisher publisher = new Publisher("publisher");
 		publisherService.savePublisher(publisher);
 		
 		// create a language
 		LanguageType languageType = new LanguageType();
 		languageType.setName("language");
 		languageTypeService.save(languageType);
 		
		// create a personal item to publish into the repository
		PersonalItem item = userPublishingFileSystemService.createRootPersonalItem(user, "personalItem");
		GenericItem genericItem = item.getVersionedItem().getCurrentVersion().getItem();
		genericItem.addFile(pf.getVersionedFile().getCurrentVersion().getIrFile());
		genericItem.addContributor(c);
		genericItem.addFirstAvailableDate(9, 8, 1977);
        genericItem.addItemIdentifier("identifier value", identType);
        genericItem.createLink("msnbc", "http://www.msnbc.com");
        genericItem.addOriginalItemCreationDate(9, 8, 2001);
        genericItem.addReport(series, "report 3456");
        genericItem.addSubTitle("the sub title");
        genericItem.setItemAbstract("abstract");
        genericItem.setItemKeywords("biology, keyword");
        genericItem.setDescription("description");
        
        genericItem.setPrimaryContentType(contentType);
        genericItem.setLanguageType(languageType);
        genericItem.setName("name");
        ExternalPublishedItem externalPublishedItem = new ExternalPublishedItem();
        
        externalPublishedItem.setCitation("citation");
        externalPublishedItem.setPublisher(publisher);
        externalPublishedItem.addPublishedDate(10, 1, 2005);
        
        genericItem.setExternalPublishedItem(externalPublishedItem);
        userPublishingFileSystemService.makePersonalItemPersistent(item);
        
        // add the item to the collection
        InstitutionalItem institutionalItem = collection.createInstitutionalItem(genericItem);
        institutionalItemService.saveInstitutionalItem(institutionalItem);
        
        // add the item to the index
		tm.commit(ts);
		
		// test searching for the data
		ts = tm.getTransaction(td);
        institutionalItemIndexService.addItem(institutionalItem, new File(repo.getInstitutionalItemIndexFolder().getFullPath()));

		Directory lucenDirectory;
		try {
			lucenDirectory = FSDirectory.getDirectory(repo.getInstitutionalItemIndexFolder().getFullPath());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultInstitutionalItemIndexService.ABSTRACT, 
					"abstract", 
					lucenDirectory);

			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.ABSTRACT + " " 
			+ "abstract";
			

			hits = executeQuery(DefaultInstitutionalItemIndexService.CITATION,
					"citation", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.CITATION + " " 
			+ "abstract";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.CONTRIBUTOR_NAMES,
					"familyName", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.CONTRIBUTOR_NAMES + " " 
			+ "familyName";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.FILE_TEXT,
					"Institutional Item Index", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.FILE_TEXT + " " 
			+ "Institutional Item Index";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.IDENTIFIERS,
					"identifier", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.IDENTIFIERS + " " 
			+ "identifier";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.KEY_WORDS,
					"biology", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.KEY_WORDS + " " 
			+ "biology";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.LANGUAGE,
					"language", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.LANGUAGE + " " 
			+ "language";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.LINK_NAMES,
					"msnbc", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.LINK_NAMES + " " 
			+ "msnbc";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.PUBLISHER,
					"publisher", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.PUBLISHER + " " 
			+ "publisher";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.SUB_TITLES,
					"sub title", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.SUB_TITLES + " " 
			+ "sub title";
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		institutionalItemIndexService.deleteItem(institutionalItem, new File(repo.getInstitutionalItemIndexFolder().getFullPath()));
		
		try {
			lucenDirectory = FSDirectory.getDirectory(repo.getInstitutionalItemIndexFolder().getFullPath());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can NOT find the stored data
		try {

			int hits = executeQuery(DefaultInstitutionalItemIndexService.ABSTRACT, 
					"abstract", 
					lucenDirectory);

			assert hits == 0 : "Hit count should equal 0 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.ABSTRACT + " " 
			+ "abstract";
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		tm.commit(ts);
		
	    // Start new transaction - clean up the data
		ts = tm.getTransaction(td);
		institutionalItemService.deleteInstitutionalItem(institutionalItemService.getInstitutionalItem(institutionalItem.getId(), false), user);
		institutionalItemService.deleteAllInstitutionalItemHistory();
        userService.deleteUser(userService.getUser(user.getId(), false));	
		helper.cleanUpRepository();
		personService.delete(personService.getAuthority(p.getId(), false));
		identifierTypeService.delete(identifierTypeService.get(identType.getId(), false));
		seriesService.deleteSeries(series.getId());
		contentTypeService.deleteContentType(contentType.getId());
		publisherService.deletePublisher(publisher.getId());
		languageTypeService.delete(languageTypeService.get(languageType.getId(), false));
		contributorTypeService.delete(contributorTypeService.get(contributorType1.getId(), false));
		tm.commit(ts);	
	}
	
	/**
	 * Test deleting a collection - make sure the item is removed from the index
	 * 
	 * @throws NoIndexFoundException 
	 * @throws IllegalFileSystemNameException 
	 * @throws DuplicateNameException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws DuplicateContributorException 
	 */
	public void testDeleteSearchCollectionItemIndex() throws NoIndexFoundException, IllegalFileSystemNameException, DuplicateNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, DuplicateContributorException 
	{
		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		institutionalCollectionService.saveCollection(collection);
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - Institutional Item Index Service test");

		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		
		// create a personal file to add to the item
		PersonalFile pf = userFileSystemService.addFileToUser(repo, f, user, 
		    		"test_file.txt", "description");
		
		// create a person name to add to the item as a contributor
		PersonName personName = new PersonName();
		personName.setFamilyName("familyName");
		personName.setForename("forename");
		personName.setInitials("n.d.s.");
		personName.setMiddleName("MiddleName");
		personName.setNumeration("III");
		personName.setSurname("surname");
		
		PersonNameAuthority p = new PersonNameAuthority(personName);
		personService.save(p);
		
        // create a contributor type
		ContributorType contributorType1 = new ContributorType("contributorType1");
		contributorTypeService.save(contributorType1);

		
		// create the contributor
		Contributor c = new Contributor();
		c.setPersonName(personName);
		c.setContributorType(contributorType1);
		
		// create an identifier
	    IdentifierType identType = new IdentifierType();
		identType.setName("identTypeName");
 		identType.setDescription("identTypeDescription");
 		
 		identifierTypeService.save(identType);
 		
		// create a series
 		Series series = new Series("my series", "12345");
 		seriesService.saveSeries(series);
 		
 		// create a content type
 		ContentType contentType = new ContentType("contentType");
 		contentTypeService.saveContentType(contentType);
 		
 		// create a publisher
 		Publisher publisher = new Publisher("publisher");
 		publisherService.savePublisher(publisher);
 		
 		// create a language
 		LanguageType languageType = new LanguageType();
 		languageType.setName("language");
 		languageTypeService.save(languageType);
 		
		// create a personal item to publish into the repository
		PersonalItem item = userPublishingFileSystemService.createRootPersonalItem(user, "personalItem");
		GenericItem genericItem = item.getVersionedItem().getCurrentVersion().getItem();
		genericItem.addFile(pf.getVersionedFile().getCurrentVersion().getIrFile());
		genericItem.addContributor(c);
		genericItem.addFirstAvailableDate(9, 8, 1977);
        genericItem.addItemIdentifier("identifier value", identType);	
        genericItem.createLink("msnbc", "http://www.msnbc.com");
        genericItem.addOriginalItemCreationDate(9, 8, 2001);
        genericItem.addReport(series, "report 3456");
        genericItem.addSubTitle("the sub title");
        genericItem.setItemAbstract("abstract");
        genericItem.setItemKeywords("biology, keyword");
        genericItem.setDescription("description");
        genericItem.setPrimaryContentType(contentType);
        genericItem.setLanguageType(languageType);
        genericItem.setName("name");
        ExternalPublishedItem externalPublishedItem = new ExternalPublishedItem();
        
        externalPublishedItem.setCitation("citation");
        externalPublishedItem.setPublisher(publisher);
        externalPublishedItem.addPublishedDate(10, 1, 2005);
        
        genericItem.setExternalPublishedItem(externalPublishedItem);
        userPublishingFileSystemService.makePersonalItemPersistent(item);
        
        // add the item to the collection
        InstitutionalItem institutionalItem = collection.createInstitutionalItem(genericItem);
        institutionalItemService.saveInstitutionalItem(institutionalItem);
        
        // add the item to the index
		tm.commit(ts);
		
		// test searching for the data
		ts = tm.getTransaction(td);
        institutionalItemIndexService.addItem(institutionalItem, new File(repo.getInstitutionalItemIndexFolder().getFullPath()));

		Directory lucenDirectory;
		try {
			lucenDirectory = FSDirectory.getDirectory(repo.getInstitutionalItemIndexFolder().getFullPath());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultInstitutionalItemIndexService.ABSTRACT, 
					"abstract", 
					lucenDirectory);

			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.ABSTRACT + " " 
			+ "abstract";
			

			hits = executeQuery(DefaultInstitutionalItemIndexService.CITATION,
					"citation", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.CITATION + " " 
			+ "abstract";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.CONTRIBUTOR_NAMES,
					"familyName", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.CONTRIBUTOR_NAMES + " " 
			+ "familyName";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.FILE_TEXT,
					"Institutional Item Index", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.FILE_TEXT + " " 
			+ "Institutional Item Index";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.IDENTIFIERS,
					"identifier", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.IDENTIFIERS + " " 
			+ "identifier";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.KEY_WORDS,
					"biology", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.KEY_WORDS + " " 
			+ "biology";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.LANGUAGE,
					"language", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.LANGUAGE + ": " 
			+ "language";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.LINK_NAMES,
					"msnbc", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.LINK_NAMES + " " 
			+ "msnbc";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.PUBLISHER,
					"publisher", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.PUBLISHER + " " 
			+ "publisher";
			
			hits = executeQuery(DefaultInstitutionalItemIndexService.SUB_TITLES,
					"sub title", 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.SUB_TITLES + " " 
			+ "sub title";
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		institutionalItemIndexService.deleteItemsForCollection( collection, new File(collection.getRepository().getInstitutionalItemIndexFolder().getFullPath()) );
		
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		try {
			repo = helper.getRepository();
			lucenDirectory = FSDirectory.getDirectory(repo.getInstitutionalItemIndexFolder().getFullPath());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can NOT find the stored data
		try {

			int hits = executeQuery(DefaultInstitutionalItemIndexService.ABSTRACT, 
					"abstract", 
					lucenDirectory);

			assert hits == 0 : "Hit count should equal 0 but equals " + hits 
			+ " for finding " + DefaultInstitutionalItemIndexService.ABSTRACT + " " 
			+ "abstract";
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	
		
	    // Start new transaction - clean up the data
		institutionalCollectionService.deleteCollection(institutionalCollectionService.getCollection(collection.getId(), false), user);
		institutionalItemService.deleteAllInstitutionalItemHistory();
        userService.deleteUser(userService.getUser(user.getId(), false));	
		helper.cleanUpRepository();
		personService.delete(personService.getAuthority(p.getId(), false));
		identifierTypeService.delete(identifierTypeService.get(identType.getId(), false));
		seriesService.deleteSeries(series.getId());
		contentTypeService.deleteContentType(contentType.getId());
		publisherService.deletePublisher(publisher.getId());
		languageTypeService.delete(languageTypeService.get(languageType.getId(), false));
		contributorTypeService.delete(contributorTypeService.get(contributorType1.getId(), false));
		tm.commit(ts);	
	}
	
	/**
	 * Test reIndexing the items in a collection
	 * @throws DuplicateNameException 
	 * @throws IllegalFileSystemNameException 
	 * @throws NoIndexFoundException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws DuplicateContributorException 
	 */
	public void testReIndexItemsInCollection() throws DuplicateNameException, IllegalFileSystemNameException, NoIndexFoundException, UserHasPublishedDeleteException, UserDeletedPublicationException, DuplicateContributorException
	{
		// Start the transaction - create the repository
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		
		repo = repositoryService.getRepository(repo.getId(), false);
		InstitutionalCollection collection = repo.createInstitutionalCollection("collection");
		InstitutionalCollection collection2 = repo.createInstitutionalCollection("collection2");

		institutionalCollectionService.saveCollection(collection);
		institutionalCollectionService.saveCollection(collection2);
		
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

        /***************** First Item ***************************/

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - Institutional Item Index Service test");

		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		
		// create a personal file to add to the item
		PersonalFile pf = userFileSystemService.addFileToUser(repo, f, user, 
		    		"test_file.txt", "description");
		
		// create a person name to add to the item as a contributor
		PersonName personName = new PersonName();
		personName.setFamilyName("familyName");
		personName.setForename("forename");
		personName.setInitials("n.d.s.");
		personName.setMiddleName("MiddleName");
		personName.setNumeration("III");
		personName.setSurname("surname");
		
		PersonNameAuthority p = new PersonNameAuthority(personName);
		personService.save(p);
			
        // create a contributor type
		ContributorType contributorType1 = new ContributorType("contributorType1");
		contributorTypeService.save(contributorType1);

		// create the contributor
		Contributor c = new Contributor();
		c.setPersonName(personName);
		c.setContributorType(contributorType1);
		
		
		// create an identifier
	    IdentifierType identType = new IdentifierType();
		identType.setName("identTypeName");
 		identType.setDescription("identTypeDescription");
 		
 		identifierTypeService.save(identType);
 		
		// create a series
 		Series series = new Series("my series", "12345");
 		seriesService.saveSeries(series);
 		
 		// create a content type
 		ContentType contentType = new ContentType("contentType");
 		contentTypeService.saveContentType(contentType);
 		
 		// create a publisher
 		Publisher publisher = new Publisher("publisher");
 		publisherService.savePublisher(publisher);
 		
 		// create a language
 		LanguageType languageType = new LanguageType();
 		languageType.setName("language");
 		languageTypeService.save(languageType);
 		
		// create a personal item to publish into the repository
		PersonalItem item = userPublishingFileSystemService.createRootPersonalItem(user, "personalItem");
		GenericItem genericItem = item.getVersionedItem().getCurrentVersion().getItem();
		genericItem.addFile(pf.getVersionedFile().getCurrentVersion().getIrFile());
		genericItem.addContributor(c);
		genericItem.addFirstAvailableDate(9, 8, 1977);
        genericItem.addItemIdentifier("identifier value", identType);	
        genericItem.createLink("msnbc", "http://www.msnbc.com");
        genericItem.addOriginalItemCreationDate(9, 8, 2001);
        genericItem.addReport(series, "report 3456");
        genericItem.addSubTitle("the sub title");
        genericItem.setItemAbstract("abstract");
        genericItem.setItemKeywords("biology, keyword");
        genericItem.setDescription("description");
        genericItem.setPrimaryContentType(contentType);
        genericItem.setLanguageType(languageType);
        genericItem.setName("name");
        ExternalPublishedItem externalPublishedItem = new ExternalPublishedItem();
        
        externalPublishedItem.setCitation("citation");
        externalPublishedItem.setPublisher(publisher);
        externalPublishedItem.addPublishedDate(10, 1, 2005);
        
        genericItem.setExternalPublishedItem(externalPublishedItem);
        userPublishingFileSystemService.makePersonalItemPersistent(item);
        
        // add the item to the collection
        InstitutionalItem institutionalItem = collection.createInstitutionalItem(genericItem);
        institutionalItemService.saveInstitutionalItem(institutionalItem);
        /***************** End First Item ***************************/
        
        
        
        /***************** Second Item ***************************/
		File f2 = testUtil.creatFile(directory, "testFile2", 
		"Hello  - Another file - Institutional Item Index Service test");

		assert f2 != null : "File should not be null";
		
		
		// create a personal file to add to the item
		PersonalFile pf2 = userFileSystemService.addFileToUser(repo, f2, user, 
		    		"another_test_file.txt", "description");
		
		// create a person name to add to the item as a contributor
		PersonName personName2 = new PersonName();
		personName2.setFamilyName("Doe");
		personName2.setForename("John");
		personName2.setInitials("j.d");
		personName2.setMiddleName("Blah");
		personName2.setNumeration("III");
		personName2.setSurname("Doe");
		
		PersonNameAuthority p2 = new PersonNameAuthority(personName2);
		personService.save(p2);
		
		// create the contributor
		Contributor c2 = new Contributor();
		c2.setPersonName(personName2);
		c2.setContributorType(contributorType1);
		
		// create an identifier
	    IdentifierType identType2 = new IdentifierType();
		identType2.setName("ISBN");
 		identType2.setDescription("differentIdentTypeDescription");
 		
 		identifierTypeService.save(identType2);
 		
		// create a series
 		Series series2 = new Series("second series", "78910");
 		seriesService.saveSeries(series2);
 		
 		// create a content type
 		ContentType contentType2 = new ContentType("Book");
 		contentTypeService.saveContentType(contentType2);
 		
 		// create a publisher
 		Publisher publisher2 = new Publisher("Barnes");
 		publisherService.savePublisher(publisher2);
 		
 		// create a language
 		LanguageType languageType2 = new LanguageType();
 		languageType2.setName("english");
 		languageTypeService.save(languageType2);
 		
		// create a personal item to publish into the repository
		PersonalItem item2 = userPublishingFileSystemService.createRootPersonalItem(user, 
				"myItem");
		GenericItem genericItem2 = item2.getVersionedItem().getCurrentVersion().getItem();
		genericItem2.addFile(pf2.getVersionedFile().getCurrentVersion().getIrFile());
		genericItem2.addContributor(c2);
		genericItem2.addFirstAvailableDate(9, 8, 1977);
        genericItem2.addItemIdentifier("173943037", identType2);	
        genericItem2.createLink("hotmail", "http://www.hotmail.com");
        genericItem2.addOriginalItemCreationDate(9, 8, 2001);
        genericItem2.addReport(series2, "report 3456");
        genericItem2.addSubTitle("generic 2");
        genericItem2.setItemAbstract("words go here");
        genericItem2.setItemKeywords("biology, science, computer");
        genericItem2.setDescription("description");
        genericItem2.setPrimaryContentType(contentType2);
        genericItem2.setLanguageType(languageType2);
        genericItem2.setName("name");
        ExternalPublishedItem externalPublishedItem2 = new ExternalPublishedItem();
        
        externalPublishedItem2.setCitation("work blah");
        externalPublishedItem2.setPublisher(publisher2);
        externalPublishedItem2.addPublishedDate(10, 1, 2005);
        
        genericItem2.setExternalPublishedItem(externalPublishedItem2);
        userPublishingFileSystemService.makePersonalItemPersistent(item2);
        
        // add the item to the collection
        InstitutionalItem institutionalItem2 = collection2.createInstitutionalItem(genericItem2);
        institutionalItemService.saveInstitutionalItem(institutionalItem2);
        
        /***************** End Second Item ***************************/

        // add the item to the index
		tm.commit(ts);
		
		// test searching for the data
		ts = tm.getTransaction(td);
        institutionalItemIndexService.addItem(institutionalItem, new File(repo.getInstitutionalItemIndexFolder().getFullPath()));
        institutionalItemIndexService.addItem(institutionalItem2, new File(repo.getInstitutionalItemIndexFolder().getFullPath()));

		
		// search the document and make sure we can find the stored data across each
        // individual collection
		try {

			FacetSearchHelper searchHelper = 
				institutionalItemSearchService.executeSearchWithFacets("biology", 
						repo.getInstitutionalItemIndexFolder().getFullPath(), 100, 100, 100, 100, 1, collection);
			assert searchHelper.getHitSize() == 1 : "Should have 1 hits but have " + searchHelper;

			searchHelper = institutionalItemSearchService.executeSearchWithFacets("biology",  
					repo.getInstitutionalItemIndexFolder().getFullPath(), 100, 100, 100, 100, 1, collection2);
			assert searchHelper.getHitSize() == 1 : "size should equal one " + searchHelper;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	
		tm.commit(ts);
		
		//move the item, re-index and re-test
		ts = tm.getTransaction(td);
		collection = institutionalCollectionService.getCollection(collection.getId(), false);
		institutionalItem2 = institutionalItemService.getInstitutionalItem(institutionalItem2.getId(), false);
		
		List<InstitutionalItem> itemsToMove = new LinkedList<InstitutionalItem>();
		itemsToMove.add(institutionalItem2);
		assert institutionalCollectionService.moveCollectionInformation(collection, null, itemsToMove).size() == 0 
		: "Move did not work correctly";
		institutionalItemIndexService.reIndexItemsInCollection(collection, new File(repo.getInstitutionalItemIndexFolder().getFullPath()), 40);

		try{
			institutionalItem2 = institutionalItemService.getInstitutionalItem(institutionalItem2.getId(), false);
			institutionalItem = institutionalItemService.getInstitutionalItem(institutionalItem.getId(), false);
			assert collection.getItems().contains(institutionalItem2);
			assert collection.getItems().contains(institutionalItem);
			FacetSearchHelper searchHelper = institutionalItemSearchService.executeSearchWithFacets("biology",  
						repo.getInstitutionalItemIndexFolder().getFullPath(), 100, 100, 100, 100, 1, collection2);
				    assert searchHelper.getHitSize() == 0 : "size should equal 0 " + searchHelper;
				    
		     searchHelper = 
			    institutionalItemSearchService.executeSearchWithFacets("biology", 
					repo.getInstitutionalItemIndexFolder().getFullPath(), 100, 100, 100, 100, 1, collection);
		    assert searchHelper.getHitSize() == 2 : "Should have 2 hits but have " + searchHelper;

		   
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}

		tm.commit(ts);
		
	    // Start new transaction - clean up the data
		ts = tm.getTransaction(td);
		institutionalItemService.deleteInstitutionalItem(institutionalItemService.getInstitutionalItem(institutionalItem.getId(), false), user);
		institutionalItemService.deleteInstitutionalItem(institutionalItemService.getInstitutionalItem(institutionalItem2.getId(), false), user);
		institutionalItemService.deleteAllInstitutionalItemHistory();
		userService.deleteUser(userService.getUser(user.getId(), false));	
		helper.cleanUpRepository();
		personService.delete(personService.getAuthority(p.getId(), false));
		identifierTypeService.delete(identifierTypeService.get(identType.getId(), false));
		seriesService.deleteSeries(series.getId());
		contentTypeService.deleteContentType(contentType.getId());
		publisherService.deletePublisher(publisher.getId());
		languageTypeService.delete(languageTypeService.get(languageType.getId(), false));
		
		identifierTypeService.delete(identifierTypeService.get(identType2.getId(), false));
		seriesService.deleteSeries(series2.getId());
		contentTypeService.deleteContentType(contentType2.getId());
		publisherService.deletePublisher(publisher2.getId());
		languageTypeService.delete(languageTypeService.get(languageType2.getId(), false));
		personService.delete(personService.getAuthority(p2.getId(), false));
		contributorTypeService.delete(contributorTypeService.get(contributorType1.getId(), false));

		tm.commit(ts);	

		
		
	}
	

	

}
