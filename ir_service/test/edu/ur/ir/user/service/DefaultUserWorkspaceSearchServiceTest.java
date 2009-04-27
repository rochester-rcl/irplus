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

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.ir.FileSystem;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.SearchResults;
import edu.ur.ir.file.FileVersion;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexService;
import edu.ur.ir.user.UserWorkspaceSearchService;
import edu.ur.util.FileUtil;

/**
 * Test the service methods for the searching user workspace information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultUserWorkspaceSearchServiceTest {
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");

	/** User indexing service */
	UserWorkspaceIndexService userIndexService = (UserWorkspaceIndexService) ctx.getBean("userWorkspaceIndexService");
	
	/** User search service */
	UserWorkspaceSearchService userSearchService = (UserWorkspaceSearchService) ctx.getBean("userWorkspaceSearchService");

	/** User file system data access */
	UserFileSystemService userFileSystemService = 
		(UserFileSystemService) ctx.getBean("userFileSystemService");
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** unique name generator */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) ctx.getBean("uniqueNameGenerator");
	
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/**
	 * Test indexing a personal file - which may have multiple versions 
	 * in it.
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws IOException 
	 * @throws NoUserIndexFolderException 
	 */
	public void testSearchPersonalFile() throws NoIndexFoundException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, IOException
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
		String name = uniqueNameGenerator.getNextName();
		userFileSystemService.createIndexFolder(user, repo, name);
		assert user.getId() != null : "User id should not be null";
		tm.commit(ts);
		
        //new transaction - add a file to a user
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		// add the first file
		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		
		PersonalFile personalFile = null;
		try
		{
		    personalFile = userFileSystemService.addFileToUser(repo, 
				f, 
				user, 
				"test_file.txt", 
				"description");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		File indexFolder = new File(user.getPersonalIndexFolder());
		userIndexService.addToIndex(repo, personalFile);
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		
		VersionedFile versionedFile = personalFile.getVersionedFile();
		FileVersion currentVersion = versionedFile.getCurrentVersion();
		
		
		SearchResults<FileSystem> searchResults = 
			userSearchService.search(indexFolder, versionedFile.getNameWithExtension(), 0, 1);
		
		assert searchResults.getTotalHits() == 1 : "Hit count should equal 1 but equals " +
		searchResults.getTotalHits()+ " for finding versioned_file_name " +
		versionedFile.getNameWithExtension();
		
		
		searchResults = 
			userSearchService.search(indexFolder, versionedFile.getNameWithExtension(), 0, 1);
		
		
		assert searchResults.getTotalHits() == 1 : "Hit count should equal 1 but equals " + 
		searchResults.getTotalHits() 
			+ " for finding file_version_creator " + currentVersion.getVersionCreator().getUsername();
			
		userIndexService.deleteFromIndex(personalFile);
		

		searchResults = 
			userSearchService.search(indexFolder,versionedFile.getNameWithExtension(), 0, 1);
		
		assert searchResults.getTotalHits() == 0 : "Hit count should equal 0 but equals " + 
		    searchResults.getTotalHits() 
			+ " for finding versioned_file_name " + versionedFile.getNameWithExtension();
			
		
		searchResults = 
				userSearchService.search(indexFolder, currentVersion.getVersionCreator().getUsername(), 0, 1);
			
		assert searchResults.getTotalHits() == 0 : "Hit count should equal 0 but equals " + 
			searchResults.getTotalHits() 
			+ " for finding file_version_creator " + currentVersion.getVersionCreator().getUsername();
	
		userService.deleteUser(user);
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test indexing a personal folder
	 * in it.
	 * @throws NoIndexFoundException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws IOException 
	 */
	public void testSearchPersonalFolder() throws NoIndexFoundException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, IOException
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
		String name = uniqueNameGenerator.getNextName();
		userFileSystemService.createIndexFolder(user, repo, name);
		assert user.getId() != null : "User id should not be null";
		tm.commit(ts);
		
        //new transaction - add a file to a user
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		
		PersonalFolder personalFolder = null;
		try
		{
		    personalFolder = userFileSystemService.createNewFolder(user, "testFolder");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		File indexFolder = new File(user.getPersonalIndexFolder());
		userIndexService.addToIndex(repo, personalFolder);
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		user = userService.getUser(user.getId(), false);
		
		SearchResults<FileSystem> searchResults = 
				userSearchService.search(indexFolder, personalFolder.getName(), 0, 1);

		assert searchResults.getTotalHits() == 1 : "Hit count should equal 1 but equals " 
			+ searchResults.getTotalHits() 
			+ " for finding personal folder name " + personalFolder.getName();
			
			
		userIndexService.deleteFromIndex(personalFolder);
		

		searchResults = userSearchService.search(indexFolder, personalFolder.getName(), 0, 1); 
	
		assert searchResults.getTotalHits() == 0 : "Hit count should equal 0 but equals " + 
		    searchResults.getTotalHits() + " for finding personal_folder_name " + personalFolder.getName();
	
		userService.deleteUser(user);
		helper.cleanUpRepository();
		tm.commit(ts);	
	}

}
