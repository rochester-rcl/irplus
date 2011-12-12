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


package edu.ur.ir.person.service;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.service.DefaultRepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;

/**
 * Test the service methods for the indexing person name information
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultNameAuthorityIndexServiceTest {
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User indexing service */
	DefaultNameAuthorityIndexService nameIndexService = (DefaultNameAuthorityIndexService) ctx.getBean("nameAuthorityIndexService");
	
	/** User indexing service */
	DefaultRepositoryService repositoryService = (DefaultRepositoryService) ctx.getBean("repositoryService");

	/** User file system data access */
	PersonService personService = 
		(PersonService) ctx.getBean("personService");
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
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
		IndexReader reader = IndexReader.open(dir, true);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser(Version.LUCENE_35, field, new StandardAnalyzer(Version.LUCENE_35));
		Query q1 = parser.parse(queryString);
		TopDocs hits = searcher.search(q1, 1000);
		int hitCount = hits.totalHits;

		searcher.close();
		reader.close();

		return hitCount;
	}
	
	/**
	 * Test indexing a name
	 * @throws LocationAlreadyExistsException 
	 * 
	 * @throws NoNameIndexException 
	 */
	public void testIndexName() throws NoIndexFoundException, LocationAlreadyExistsException
	{
		
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
	 
		// save the repository
		tm.commit(ts);
		
		
		 // Start new transaction
		ts = tm.getTransaction(td);
		PersonName n = new PersonName();
		n.setForename("forename");
		n.setFamilyName("familyName");
		n.setMiddleName("middleName");
		n.setSurname("surname");
		
		PersonNameAuthority personNameAuthority = new PersonNameAuthority(n);
		personService.save(personNameAuthority);
		
		File indexDir = new File(repo.getNameIndexFolder());
		nameIndexService.addToIndex(personNameAuthority, indexDir);
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		
		String indexFolder = repo.getNameIndexFolder();
		
		Directory lucenDirectory;
		try {
			lucenDirectory = FSDirectory.open(new File(indexFolder));
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultNameAuthorityIndexService.NAMES, 
					n.getForename(), 
					lucenDirectory);

			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultNameAuthorityIndexService.NAMES+ " "
			+ n.getForename();
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		nameIndexService.deleteFromIndex(personNameAuthority, indexDir);
		
		try {
			lucenDirectory = FSDirectory.open(new File(indexFolder));
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultNameAuthorityIndexService.NAMES, 
					n.getForename(), 
					lucenDirectory);
			
			assert hits == 0 : "Hit count should equal 0 but equals " + hits 
			+ " for finding " + DefaultNameAuthorityIndexService.NAMES + " " + n.getForename();
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		personService.delete(personNameAuthority);
		tm.commit(ts);	
		
		// Start the transaction 
		ts = tm.getTransaction(td);
		helper.cleanUpRepository();
		tm.commit(ts);
	}

}
