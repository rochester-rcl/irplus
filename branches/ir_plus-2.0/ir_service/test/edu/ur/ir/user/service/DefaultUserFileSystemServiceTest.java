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
import java.util.LinkedList;
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
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.ir.file.FileCollaborator;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.FileSharingException;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;
import edu.ur.util.FileUtil;


/**
 * Test the service methods for the default user services.
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultUserFileSystemServiceTest {

		/** Application context  for loading information*/
		ApplicationContext ctx = ContextHolder.getApplicationContext();

		/** User data access */
		UserService userService = (UserService) ctx.getBean("userService");
		
		/** User file systemdata access */
		UserFileSystemService userFileSystemService = 
			(UserFileSystemService) ctx.getBean("userFileSystemService");
		
		/** Item services */
		ItemService itemService = (ItemService) ctx.getBean("itemService");
		
		/** Platform transaction manager  */
		PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
		
		/** Transaction definition */
		TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		
		
		/** Properties file with testing specific information. */
		PropertiesLoader propertiesLoader = new PropertiesLoader();
		
		/** User data access */
		InviteUserService inviteUserService = (InviteUserService) ctx
		.getBean("inviteUserService");
		
		/** Get the properties file  */
		Properties properties = propertiesLoader.getProperties();
		
	    VersionedFileDAO versionedFileDAO= (VersionedFileDAO) ctx
		.getBean("versionedFileDAO");
	    
		/** unique name generator */
		UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) ctx.getBean("uniqueNameGenerator");
		
		
		/**
		 * Test creating a file
		 * @throws UserHasPublishedDeleteException 
		 * @throws LocationAlreadyExistsException 
		 */
		public void createFileTest() throws UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
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
			// create the first file to store in the temporary folder
			String tempDirectory = properties.getProperty("ir_service_temp_directory");
			File directory = new File(tempDirectory);
			
	        // helper to create the file
			FileUtil testUtil = new FileUtil();
			testUtil.createDirectory(directory);

			File f = testUtil.creatFile(directory, "testFile", 
			"Hello  - irFile This is text in a file - VersionedFileDAO test");
			
			assert f != null : "File should not be null";
			assert user.getId() != null : "User id should not be null";
			assert repo.getFileDatabase().getId() != null : "File database id should not be null";
			tm.commit(ts);
			
			// new transaction
			ts = tm.getTransaction(td);
			try
			{
			    userFileSystemService.addFileToUser(repo, f, user, "test_file", "description");
			}
			catch(Exception e)
			{
				throw new IllegalStateException(e);
			}
			tm.commit(ts);
	     
	        //new transaction
			ts = tm.getTransaction(td);
			IrUser otherUser2 = userService.getUser(user.getUsername());
			assert otherUser2.getRootFile("test_file") != null;
			userService.deleteUser(otherUser2, otherUser2);
			tm.commit(ts);
			
		    // Start new transaction
			ts = tm.getTransaction(td);
			helper.cleanUpRepository();
			tm.commit(ts);	
		}
		

		
		/**
		 * Test adding an empty file to they system.
		 * @throws DuplicateNameException 
		 * @throws UserHasPublishedDeleteException 
		 * @throws LocationAlreadyExistsException 
		*/
		public void addVersionEmptyFileTest() throws DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
		{
			
			// Start the transaction 
			TransactionStatus ts = tm.getTransaction(td);
			UserEmail email = new UserEmail("email");

			IrUser user = userService.createUser("password", "username", email);
			RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
			Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
			// save the repository
			tm.commit(ts);
			

			// new transaction
			ts = tm.getTransaction(td);
			
			// Create an empty file for the user
			PersonalFile pf = userFileSystemService.addFileToUser(repo, 
					user, "test_file", "description");
	        
			assert pf != null : "Personal file sould not be null";
			tm.commit(ts);

	        //new transaction
			ts = tm.getTransaction(td);
			IrUser otherUser2 = userService.getUser(user.getUsername());
	        
			PersonalFile rootFile = otherUser2.getRootFile("test_file");
			assert rootFile != null : "Root file should not be equal to null";
			assert  rootFile.getVersionedFile().getCurrentVersion().getVersionNumber() == 1;
			userService.deleteUser(otherUser2, otherUser2);
			tm.commit(ts);
			
		    // Start new transaction
			ts = tm.getTransaction(td);
			assert userService.getUser(otherUser2.getId(), false) == null : "User should be null"; 
			assert userService.getUser(user.getId(), false) == null : "User should be null";
			helper.cleanUpRepository();
			tm.commit(ts);	
			
		}
		 
		/**
		 * Test moving files and folders to an existing folder
		 * @throws DuplicateNameException 
		 * @throws UserHasPublishedDeleteException 
		 * @throws LocationAlreadyExistsException 
		 */
		public void moveFileFolderTest() throws DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
		{
			
			// Start the transaction 
			TransactionStatus ts = tm.getTransaction(td);
			UserEmail email = new UserEmail("email");

			IrUser user = userService.createUser("password", "username", email);
			RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
			Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
			// save the repository
			tm.commit(ts);
			
	        // Start the transaction 
			ts = tm.getTransaction(td);

			// create the first file to store in the temporary folder
			String tempDirectory = properties.getProperty("ir_service_temp_directory");
			File directory = new File(tempDirectory);
			
	        // helper to create the file
			FileUtil testUtil = new FileUtil();
			testUtil.createDirectory(directory);

			File f = testUtil.creatFile(directory, "testFile", 
			"Hello  - irFile This is text in a file - VersionedFileDAO test");
			
			assert f != null : "File should not be null";
			assert user.getId() != null : "User id should not be null";
			assert repo.getFileDatabase().getId() != null : "File database id should not be null";
			
			tm.commit(ts);
			
			// new transaction - create two new folders and a personal file
			ts = tm.getTransaction(td);
			
			PersonalFolder myFolder = userFileSystemService.createNewFolder(user, "myFolder1");
			    assert myFolder != null : "folder should be created";
			    
			PersonalFolder destination = userFileSystemService.createNewFolder(user, 
				"myFolder2");
		
		    assert destination != null : "folder should be created";
			assert user.getRootFolder(destination.getName()) != null : 
				"Should be able to find folder " + destination;
			
			PersonalFile pf = userFileSystemService.addFileToUser(repo, 
					f, 
					myFolder, 
					"test file", 
					"description");
			
			tm.commit(ts);
	        
			
			// start new transaction
	        ts = tm.getTransaction(td);
	        assert pf.getId() != null : "Personal file should not have a null id " + pf;
	        List<PersonalFolder> foldersToMove = new LinkedList<PersonalFolder>();
	        foldersToMove.add(myFolder);
	        
	        // move the folder into other folder
	        userFileSystemService.moveFolderSystemInformation(destination, 
	        		foldersToMove, null);
	        
	        tm.commit(ts);
	     
	        //new transaction - make sure the folder was moved.
			ts = tm.getTransaction(td);
			IrUser otherUser2 = userService.getUser(user.getUsername());
			PersonalFolder theDestination = user.getRootFolder(destination.getName());
			PersonalFolder newChild = theDestination.getChild(myFolder.getName());
			
			assert  newChild != null : "Was not able to find " + myFolder
			+ " in children of " +
			theDestination ;
			
			assert newChild.getFile(pf.getName()) != null : "File " + pf.getName() 
			+ "was not found ";
			
			// move the file now to the folder above (parent folder)
			List<PersonalFile> filesToMove = new LinkedList<PersonalFile>();
	        filesToMove.add(pf);
	        
	        //move the file to the parent folder
			userFileSystemService.moveFolderSystemInformation(theDestination, 
					null, filesToMove);
			tm.commit(ts);
			
			
			// make sure move occured
			ts = tm.getTransaction(td);
			destination = userFileSystemService.getPersonalFolder(destination.getId(), false);
			assert destination.getFiles().contains(pf) : "Destination folder " + destination + " should have file " + pf;
			tm.commit(ts);
			
			
			ts = tm.getTransaction(td);
			otherUser2 = userService.getUser(otherUser2.getId(), false);
			userService.deleteUser(otherUser2, otherUser2);
			tm.commit(ts);
			
		    // Start new transaction
			ts = tm.getTransaction(td);
			assert userService.getUser(otherUser2.getId(), false) == null : "User should be null"; 
			assert userService.getUser(user.getId(), false) == null : "User should be null";
			helper.cleanUpRepository();
			tm.commit(ts);	
			
		}
		
		/**
		 * Test moving files and folders to the root (the User)
		 * @throws DuplicateNameException 
		 * @throws UserHasPublishedDeleteException 
		 * @throws LocationAlreadyExistsException 
		 */
		public void moveFileFolderToRootTest() throws DuplicateNameException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
		{
			
			// Start the transaction 
			TransactionStatus ts = tm.getTransaction(td);
			UserEmail email = new UserEmail("email");

			IrUser user = userService.createUser("password", "username", email);
			RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
			Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
			// save the repository
			tm.commit(ts);
			
	        // Start the transaction 
			ts = tm.getTransaction(td);

			// create the first file to store in the temporary folder
			String tempDirectory = properties.getProperty("ir_service_temp_directory");
			File directory = new File(tempDirectory);
			
	        // helper to create the file
			FileUtil testUtil = new FileUtil();
			testUtil.createDirectory(directory);

			File f = testUtil.creatFile(directory, "testFile", 
			"Hello  - irFile This is text in a file - VersionedFileDAO test");
			
			assert f != null : "File should not be null";
			assert user.getId() != null : "User id should not be null";
			assert repo.getFileDatabase().getId() != null : "File database id should not be null";
			
			tm.commit(ts);
			
			// new transaction - create two new folders
			ts = tm.getTransaction(td);
			
			PersonalFolder myFolder = userFileSystemService.createNewFolder(user, "myFolder1");
			PersonalFolder subFolder = myFolder.createChild("subFolder");
			
			userFileSystemService.makePersonalFolderPersistent(myFolder);
			assert myFolder != null : "folder should be created";
			
			// create a sub folder
			assert subFolder != null : "folder should be created";
			
			PersonalFile pf = userFileSystemService.addFileToUser(repo, 
					f, 
					myFolder, 
					"test file", 
					"description");
			tm.commit(ts);
	        
			// start new transaction
	        ts = tm.getTransaction(td);
	        assert pf.getId() != null : "Personal file should not have a null id " + pf;
	        List<PersonalFolder> foldersToMove = new LinkedList<PersonalFolder>();
	        foldersToMove.add(subFolder);
	        userFileSystemService.moveFolderSystemInformation(userService.getUser(user.getId(), false), 
	        		foldersToMove, null);
	        tm.commit(ts);

	        // start new transaction
	        ts = tm.getTransaction(td);
			user = userService.getUser(user.getId(), false);
			PersonalFolder newRoot = user.getRootFolder(subFolder.getName());
			assert newRoot != null : "Should be able to find the folder " + subFolder;
			assert newRoot.getId().equals(subFolder.getId()) : "folder id's should be the same newRoot = " + newRoot
			+ " subFolder = " + subFolder;
	        tm.commit(ts);
	 
	        ts = tm.getTransaction(td);
			PersonalFolder oldParent = userFileSystemService.getPersonalFolder(myFolder.getId(), false);
			assert oldParent.getChildren().size() == 0 : "Should not have any children but has " +
			oldParent.getChildren().size();
			tm.commit(ts);
			
			ts = tm.getTransaction(td);
			// move the file now to the folder above
			List<PersonalFile> filesToMove = new LinkedList<PersonalFile>();
	        filesToMove.add(userFileSystemService.getPersonalFile(pf.getId(),false));
			userFileSystemService.moveFolderSystemInformation(userService.getUser(user.getId(), false), 
					null, filesToMove);
			tm.commit(ts);
			
			ts = tm.getTransaction(td);
			// make sure the file has been moved
			pf = userFileSystemService.getPersonalFile(pf.getId(), false);
			assert pf.getPersonalFolder() == null : "PersonalFile should no longer have a parent folder " + 
			pf.getPersonalFolder();
			user = userService.getUser(user.getId(), false);
			assert user.getRootFile(pf.getName()) != null : "Should be able to find the file";
			tm.commit(ts);
			
			 // Start new transaction
			ts = tm.getTransaction(td);
			IrUser deleteUser = userService.getUser(user.getId(), false);
			userService.deleteUser(deleteUser, deleteUser);
			tm.commit(ts);
			
			ts = tm.getTransaction(td);
			assert userService.getUser(user.getId(), false) == null : "User should be null"; 
			helper.cleanUpRepository();
			tm.commit(ts);
		}
		
		/**
		 * Test adding a folder for indexing data to a user
		 * @throws UserHasPublishedDeleteException 
		 * @throws LocationAlreadyExistsException 
		 * @throws IOException 
		 */
		public void createUserIndexFolderTest() throws UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, IOException
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
			
	        //new transaction
			ts = tm.getTransaction(td);
			IrUser otherUser2 = userService.getUser(user.getUsername());
			assert otherUser2.getPersonalIndexFolder() != null;
			userService.deleteUser(otherUser2, otherUser2);
			tm.commit(ts);
			
		    // Start new transaction
			ts = tm.getTransaction(td);
			helper.cleanUpRepository();
			tm.commit(ts);	
		}
		
		/**
		 * Tests adding an inbox file to personal folders 
		 * @throws DuplicateNameException 
		 * @throws FileSharingException 
		 * @throws UserHasPublishedDeleteException 
		 * @throws LocationAlreadyExistsException 
		 */
		public void testAddInboxFileTest() throws DuplicateNameException, FileSharingException, IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
		{
	
			// Start the transaction 
			TransactionStatus ts = tm.getTransaction(td);

			RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
			Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
			// save the repository
			tm.commit(ts);
			
	        // Start the transaction 
			ts = tm.getTransaction(td);

			String userEmail1 = properties.getProperty("user_1_email");
			UserEmail email = new UserEmail(userEmail1);
			IrUser user = userService.createUser("password", "username", email);
			
			
			String userEmail2 = properties.getProperty("user_2_email");
			UserEmail email1 = new UserEmail(userEmail2);
			IrUser user1 = userService.createUser("password1", "username1", email1);

			// create the first file to store in the temporary folder
			String tempDirectory = properties.getProperty("ir_service_temp_directory");
			File directory = new File(tempDirectory);
			
	        // helper to create the file
			FileUtil testUtil = new FileUtil();
			testUtil.createDirectory(directory);

			File f = testUtil.creatFile(directory, "testFile", 
			"Hello  - irFile This is text in a file - VersionedFileDAO test");
			
			PersonalFile pf = null;
			
			pf = userFileSystemService.addFileToUser(repo, f, user, 
					    "test file", "description");
			
			
	        tm.commit(ts);

			//Start a transaction 
			ts = tm.getTransaction(td);
	        
	        VersionedFile vf = pf.getVersionedFile();
		
			// Share the file with other user
			SharedInboxFile inboxFile = inviteUserService.shareFile(user, user1, vf);
			tm.commit(ts);
			
			//Start a transaction 
			ts = tm.getTransaction(td);
			VersionedFile otherVf = versionedFileDAO.getById(vf.getId(), false);
			IrUser u = userService.getUser(user1.getUsername());
			FileCollaborator fCollaborator = otherVf.getCollaborator(u);;
			assert u.getSharedInboxFile(otherVf) != null : "Versioned file should exist should be in shared inbox";
			assert u.getSharedInboxFile(inboxFile.getId()).getVersionedFile().equals(otherVf) : "Versioned file should exist in the other user root";
			assert fCollaborator.getCollaborator().equals(u) : "Should be equal to user : u";
			
			tm.commit(ts);

			
			
			//Start a transaction 
			ts = tm.getTransaction(td);
			IrUser u1 = userService.getUser(user1.getUsername());
			PersonalFile sharedInboxPersonalFile = userFileSystemService.addSharedInboxFileToFolders(u1, u1.getSharedInboxFile(inboxFile.getId()));
			tm.commit(ts);

			// Test the file has been moved into the personal files
			ts = tm.getTransaction(td);

			assert userFileSystemService.getSharedInboxFile(inboxFile.getId(),  false) == null : 
				"Should not be able to find inbox file " + inboxFile;
			
			IrUser u1Other = userService.getUser(user1.getUsername());
			assert u1Other.getRootFiles().contains(sharedInboxPersonalFile) : 
				"Root files should contain the specified sharedInboxPersonalFile";
			
			tm.commit(ts);

			// Start a transaction 
			ts = tm.getTransaction(td);
			IrUser deleteUser = userService.getUser(user.getId(), false);
			userService.deleteUser(deleteUser, deleteUser);
			
			IrUser deleteUser2 = userService.getUser(user1.getId(), false);
			userService.deleteUser(deleteUser2, deleteUser2);
			tm.commit(ts);
			
		    // Start new transaction
			ts = tm.getTransaction(td);
			assert userService.getUser(user1.getId(), false) == null : "User should be null"; 
			assert userService.getUser(user.getId(), false) == null : "User should be null";
			helper.cleanUpRepository();
			tm.commit(ts);	 
		}
		


}
