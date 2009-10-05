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


package edu.ur.ir.user.service;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;


/**
 * Test the service methods for the indexing user information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultUserIndexServiceTest {
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");

	/** User indexing service */
	DefaultUserIndexService userIndexService = (DefaultUserIndexService) ctx.getBean("userIndexService");

	/** User file system data access */
	UserFileSystemService userFileSystemService = 
		(UserFileSystemService) ctx.getBean("userFileSystemService");
	
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
		IndexSearcher searcher = new IndexSearcher(dir);
		QueryParser parser = new QueryParser(field, new StandardAnalyzer());
		Query q1 = parser.parse(queryString);
		TopDocs hits = searcher.search(q1, 1000);
		int hitCount = hits.totalHits;

		searcher.close();

		return hitCount;
	}
	
	/**
	 * Test indexing a personal file - which may have multiple versions 
	 * in it.
	 * @throws NoIndexFoundException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testIndexPersonalFile() throws NoIndexFoundException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException 
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);
		
		UserEmail email = new UserEmail("email");

        // Start the transaction 
		ts = tm.getTransaction(td);
		IrUser user = userService.createUser("password", "username", email);
		assert user.getId() != null : "User id should not be null";
		tm.commit(ts);
		
        //new transaction - add a file to a user
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		
		File userIndex = new File(repo.getUserIndexFolder());
		userIndexService.addToIndex(user, userIndex);
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		
		Directory lucenDirectory;
		try {
			lucenDirectory = FSDirectory.getDirectory(repo.getUserIndexFolder());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultUserIndexService.USER_NAME, 
					user.getUsername(), 
					lucenDirectory);

			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultUserIndexService.USER_NAME + " " 
			+ user.getUsername();
			
			hits = executeQuery(DefaultUserIndexService.USER_EMAILS, 
					user.getDefaultEmail().getEmail(), 
					lucenDirectory);
			
			assert hits == 1 : "Hit count should equal 1 but equals " + hits 
			+ " for finding " + DefaultUserIndexService.USER_EMAILS + " " + user.getDefaultEmail();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		userIndexService.deleteFromIndex(user.getId(), userIndex);
		
		try {
			lucenDirectory = FSDirectory.getDirectory(repo.getUserIndexFolder());
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		// search the document and make sure we can find the stored data
		try {

			int hits = executeQuery(DefaultUserIndexService.USER_NAME, 
					user.getUsername(), 
					lucenDirectory);

			assert hits == 0 : "Hit count should equal 0 but equals " + hits 
			+ " for finding " + DefaultUserIndexService.USER_NAME + " " 
			+ user.getUsername();
			
			hits = executeQuery(DefaultUserIndexService.USER_EMAILS, 
					user.getDefaultEmail().getEmail(), 
					lucenDirectory);
			
			assert hits == 0 : "Hit count should equal 0 but equals " + hits 
			+ " for finding " + DefaultUserIndexService.USER_EMAILS + 
			" " + user.getDefaultEmail();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		userService.deleteUser(user, user);
		helper.cleanUpRepository();
		tm.commit(ts);	
	}

}
