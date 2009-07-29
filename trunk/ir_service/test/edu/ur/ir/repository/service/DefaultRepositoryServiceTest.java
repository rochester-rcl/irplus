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


package edu.ur.ir.repository.service;

import java.io.File;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TemporaryFileCreator;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.TransformedFileTypeDAO;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.transformer.JpegThumbnailTransformer;
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
import edu.ur.util.FileUtil;

/**
 * Test the service methods for the default repository services.
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultRepositoryServiceTest {
	
	/** Spring application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
    /** Repository service  */
    RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
	
	/** transaction manager for dealing with transactions  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	 
	/** the transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

	/** User data access */
	UserService userService = (UserService) ctx
	.getBean("userService");
	
    /** transformed file type data access object */
    TransformedFileTypeDAO transformedFileTypeDAO = (TransformedFileTypeDAO) ctx
	.getBean("transformedFileTypeDAO");
    
    /** temporary file creator */
    TemporaryFileCreator temporaryFileCreator = (TemporaryFileCreator) 
    ctx.getBean("temporaryFileCreator");
    
    /** thumbnail generator */
    JpegThumbnailTransformer jpegThumbnail = (JpegThumbnailTransformer)
    ctx.getBean("jpegThumbnailTransformer");


	
	/**
	 * Test finding the repository
	 * @throws LocationAlreadyExistsException 
	 */
	public void getRepositoryTest() throws LocationAlreadyExistsException
	{
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
	 
		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);
		assert repositoryService.getRepository(repo.getId(), false) != null;
		tm.commit(ts);
		
 	    // Start the transaction 
		ts = tm.getTransaction(td);
		helper.cleanUpRepository();
		tm.commit(ts);
	}
	 
	/**
	 * Test creating a versioned file
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void createVersionedFileTest() throws IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
	{
		TransactionStatus ts = tm.getTransaction(td);
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
		
		UserEmail email = new UserEmail("email");
		
		IrUser user = userService.createUser("password", "username", email);
		VersionedFile vf = repositoryService.createVersionedFile(user, 
				repo, f,"repositoryServiceFile", "description");
		tm.commit(ts);
				
 	    // Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
	    assert repositoryService.getVersionedFile(vf.getId(), false) != null : 
	    	"Should be able to find versiond file";
	    
	    VersionedFile currentLoadedFile = repositoryService.getVersionedFile(vf.getId(), false);
	    assert  currentLoadedFile.equals(vf) : "Versioned files should be equal";
		
	    String fileLocation = currentLoadedFile.getCurrentVersion().getIrFile().getFileInfo().getFullPath();
	    
	    File myFile = new File(fileLocation);
	    assert myFile.exists() : "File should exist at loation " + fileLocation;

	    
	    repositoryService.deleteVersionedFile(repositoryService.getVersionedFile(vf.getId(), false));
		assert repositoryService.getVersionedFile(vf.getId(), false) == null : 
			"Should no longer be able to find the versioned file";
		
		IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
		assert !myFile.exists(): "File should no longer exist at location " + fileLocation;
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test creating a versioned file which is a jpeg
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void createVersionedJpegFileTest() throws IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
	{
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		
		// create a transformed file type
		TransformedFileType transformedFileType = new TransformedFileType("Primary Thumbnail");
		transformedFileType.setDescription("Thumbnail created by the system");
		transformedFileType.setSystemCode("PRIMARY_THUMBNAIL");
		
		transformedFileTypeDAO.makePersistent(transformedFileType);
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		String path = properties.getProperty("ir_service_location");
		String fileName = properties.getProperty("ur_research_home_jpg");
		File f = new File(path + fileName);
		
		assert f != null : "File should not be null";
		assert f.canRead(): "Should be able to read the file " + f.getAbsolutePath();

		UserEmail email = new UserEmail("email");

		IrUser user = userService.createUser("password", "username", email);
		VersionedFile vf = repositoryService.createVersionedFile(user, repo, 
				f,"repositoryServiceFile", "description");
		tm.commit(ts);
		
		// create a thumbnail image
		ts = tm.getTransaction(td);
		
		File tempFile = null;
		try
		{
		    tempFile = temporaryFileCreator.createTemporaryFile("jpg");
		    jpegThumbnail.transformFile(f, "jpg", tempFile);
		    
		}
		catch(Exception e)
		{
			throw new RuntimeException("could not create file", e);
		}
		
		IrFile irFile = vf.getCurrentVersion().getIrFile();
		repositoryService.addTransformedFile(repo, 
				irFile, tempFile, "JPEG Thumbnail", 
				jpegThumbnail.getFileExtension(), transformedFileType);
		
		
		tm.commit(ts);
		
		
 	    // Start the transaction 
		ts = tm.getTransaction(td);
	    assert repositoryService.getVersionedFile(vf.getId(), false) != null : "Should be able to find versiond file";
	    
	    VersionedFile currentLoadedFile = repositoryService.getVersionedFile(vf.getId(), false);
	    assert  currentLoadedFile.equals(vf) : "Versioned files should be equal";
		
	    assert currentLoadedFile.getCurrentVersion().getIrFile().getTransformedFileBySystemCode("PRIMARY_THUMBNAIL") != null :
	    	"Should be able to find the primary thumbnail";
	    String fileLocation = currentLoadedFile.getCurrentVersion().getIrFile().getFileInfo().getFullPath();
	    
	    File myFile = new File(fileLocation);
	    assert myFile.exists() : "File should exist at loation " + fileLocation;

	    
	    repositoryService.deleteVersionedFile(repositoryService.getVersionedFile(vf.getId(), false));
		assert repositoryService.getVersionedFile(vf.getId(), false) == null : 
			"Should no longer be able to find the versioned file";
	    
		assert !myFile.exists(): "File should no longer exist at location " + fileLocation;
		transformedFileTypeDAO.makeTransient(transformedFileTypeDAO.getById(transformedFileType.getId(), false));

		helper.cleanUpRepository();
		IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
		tm.commit(ts);	
	}
	
	/**
	 * Test finding the repository
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void addNewVersionTest() throws IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
	{
        TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);
		
        // Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		UserEmail email = new UserEmail("email");

		IrUser user = userService.createUser("password", "username", email);
		VersionedFile vf = repositoryService.createVersionedFile(user, repo, 
				f,"repositoryServiceFile", "description");
		
		tm.commit(ts);
		
 	    // Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);

		File versionTwo = testUtil.creatFile(directory, "testFile2", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test - second version");
		
		// lock the file so the user can upload a new version
		repositoryService.lockVersionedFile(vf, user);

		repositoryService.addNewFileToVersionedFile(repo, vf, versionTwo, "testFile2.txt", user);
		tm.commit(ts);	

		// Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		VersionedFile other = repositoryService.getVersionedFile(vf.getId(), false);
		assert other.getLargestVersion() == 2 : "Current number of versions should equal 2";
		
		repositoryService.deleteVersionedFile(repositoryService.getVersionedFile(vf.getId(), false));
				
		IrUser deleteUser = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser, deleteUser);	
		assert repositoryService.getVersionedFile(vf.getId(), false) == null : 
			"Should no longer be able to find the versioned file";
		
	    helper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test locking and unlocking a versioned file
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void lockUnLockVersionedFileTest() throws IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException
	{
		TransactionStatus ts = tm.getTransaction(td);
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
		
		// create the first user
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		
		// create the second user who cannot modifiy the file
		UserEmail email2 = new UserEmail("email2");
		IrUser user2 = userService.createUser("password2", "username2", email2);

		VersionedFile vf = repositoryService.createVersionedFile(user, 
				repo, f,"repositoryServiceFile", "description");
		tm.commit(ts);
				
 	    // Start the transaction this is for lazy loading
		// check to make sure locking is correct
		ts = tm.getTransaction(td);
	    assert repositoryService.getVersionedFile(vf.getId(), false) != null : 
	    	"Should be able to find versiond file";
	    
	    VersionedFile currentLoadedFile = repositoryService.getVersionedFile(vf.getId(), false);
	    
	    assert repositoryService.canLockVersionedFile(currentLoadedFile, user) : 
	    	"User " + user + " should be able to lock the file " + currentLoadedFile + " but can't";
	    
	    assert !repositoryService.canLockVersionedFile(currentLoadedFile, user2) : 
	    	"User " + user2 + " should NOT be able to lock the file but can ";
	    
	    assert repositoryService.lockVersionedFile(currentLoadedFile, user);

	    tm.commit(ts);	
	    
	    
	    // checked locked status
	    ts = tm.getTransaction(td);
	    VersionedFile lockedVersionFile = repositoryService.getVersionedFile(vf.getId(), false);
	    assert lockedVersionFile.isLocked() : "Versioned File " + lockedVersionFile + 
	    " should be locked but is NOT!"; 
	    
	    assert repositoryService.unlockVersionedFile(lockedVersionFile, user) :
	    	" Versioned file " + lockedVersionFile + " should be unlocked but isn't ";
	    
	    lockedVersionFile.addCollaborator(user2);
	    assert repositoryService.lockVersionedFile(lockedVersionFile, user) : 
	    	" user " + user2 + " should be able to lock the versioned file but cannot";
	    
	    tm.commit(ts);
	    
	    
	    ts = tm.getTransaction(td);
	    repositoryService.deleteVersionedFile(repositoryService.getVersionedFile(vf.getId(), false));
		assert repositoryService.getVersionedFile(vf.getId(), false) == null : 
			"Should no longer be able to find the versioned file";
		
		IrUser deleteUser1 = userService.getUser(user.getId(), false);
        userService.deleteUser(deleteUser1, deleteUser1);	
		
        IrUser deleteUser2 = userService.getUser(user2.getId(), false);
        userService.deleteUser(deleteUser2, deleteUser2);
  
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	
	/**
	 * Test deleting an ir file.  Make sure the underlying file information
	 * is also deleted. 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testDeleteIrFile() throws IllegalFileSystemNameException, LocationAlreadyExistsException
	{
		TransactionStatus ts = tm.getTransaction(td);
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
		
		IrFile myIrFile = repositoryService.createIrFile(repo, f, "fileName", 
				"description");
	
		tm.commit(ts);
		
	    ts = tm.getTransaction(td);
	    File mainFile = new File(myIrFile.getFileInfo().getFullPath());
	    assert mainFile.exists() : " main File " + mainFile.getAbsolutePath() 
	        + " should exist";
	    
	    IrFile foundIrFile = repositoryService.getIrFile(myIrFile.getId(), false);
	    assert foundIrFile != null : "Could not find irFile " + myIrFile;
	    
	    // delete the file
	    repositoryService.deleteIrFile(foundIrFile);
	
	    assert repositoryService.getIrFile(foundIrFile.getId(),  false ) == null : 
	    	"Should not be able to find ir file " + foundIrFile;
	    
		assert !mainFile.exists() : " main file " + mainFile.getAbsolutePath()
		   + " should no longer exist";
		
		helper.cleanUpRepository();
		tm.commit(ts);
	}
	
}
