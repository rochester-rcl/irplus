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

package edu.ur.hibernate.ir.user.db;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FileServerService;
import edu.ur.file.db.UniqueNameGenerator;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.PersonalFolderDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserManager;
import edu.ur.util.FileUtil;

/**
 * Test the persistence methods for personal folders
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class PersonalFolderDAOTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/**  Transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
			.getBean("transactionManager");

	/** transaction definition  */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Personal folder data access */
	PersonalFolderDAO personalFolderDAO = (PersonalFolderDAO) ctx
	.getBean("personalFolderDAO");

    /** User data access */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");
    
	/** versioned file data access object  */
	VersionedFileDAO versionedFileDAO = (VersionedFileDAO) ctx
			.getBean("versionedFileDAO");
	
	/** IrFile relational data access   */
    IrFileDAO fileDAO= (IrFileDAO) ctx
	.getBean("irFileDAO");
    
    /** Unique name generator  */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) 
	ctx.getBean("uniqueNameGenerator");
	
	/** Logger */
	private static final Logger log = Logger.getLogger(PersonalFolderDAOTest.class);
	
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
	 * Test personal folder persistence
	 * @throws DuplicateNameException 
	 * 
	*/
	@Test
	public void basePersonalFolderDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException {
  		
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");

        // create a user who has their own folder
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		// create the user and their folder.
		userDAO.makePersistent(user);
		PersonalFolder personalFolder = user.createRootFolder("topFolder");
		personalFolderDAO.makePersistent(personalFolder);
		
		assert personalFolderDAO.getById(personalFolder.getId(), false) != null : "Should be able to find "
			+ " personal Folder " + personalFolder;
		
        // Start the transaction 
        TransactionStatus ts = tm.getTransaction(td);
		assert personalFolderDAO.getById(personalFolder.getId(), false).equals(personalFolder) : 
			"Folders should be equal";
		
		assert personalFolderDAO.getRootPersonalFolder("topFolder", user.getId()).equals(personalFolder) : 
			"Should be able to find folder";
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		personalFolderDAO.makeTransient(personalFolderDAO.getById(personalFolder.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert personalFolderDAO.getById(personalFolder.getId(), false) == null: 
			"Should not be able to find personal folder";
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		
	}
	 
	/**
	 * Test PersonalFolder persistence with children
	 * @throws DuplicateNameException 
	 * 
	 */
	@Test
	public void personalFolderWithChildrenDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException {

		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");

         // create a user who has their own folder
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		// create the user and their folder.
		userDAO.makePersistent(user);
		PersonalFolder personalFolder1 = user.createRootFolder("topFolder");
		personalFolderDAO.makePersistent(personalFolder1);
		
		// make sure parent starts out with 1 and 2 left/right values.
		assert personalFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalFolder1.getLeftValue();

		assert personalFolder1.getRightValue() == 2L : "Right value should be 2 but is " + 
		personalFolder1.getRightValue();

		PersonalFolder personalFolder2 = user.createRootFolder("topFolder2");
		
		assert personalFolder2.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalFolder2.getLeftValue();

		assert personalFolder2.getRightValue() == 2L : "Right value should be 2 but is " + 
		personalFolder2.getRightValue();
		
		// add a child
		PersonalFolder childFolder1 = null;
		try
		{
		    childFolder1 = personalFolder1.createChild("childFolder1");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert personalFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalFolder1.getLeftValue();

		assert personalFolder1.getRightValue() == 4L : "Right value should be 2 but is " + 
		personalFolder1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 1 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 3L : "Right value should be 2 but is " + 
		childFolder1.getRightValue();
		
		
		// add another child
		 PersonalFolder childFolder2 = null;
		try
		{
		   childFolder2 = personalFolder1.createChild("child2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert personalFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalFolder1.getLeftValue();

		assert personalFolder1.getRightValue() == 6L : "Right value should be 2 but is " + 
		personalFolder1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 1 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 3L : "Right value should be 2 but is " + 
		childFolder1.getRightValue();
		
		assert childFolder2.getLeftValue() == 4L : "Left value should be 1 but is " + 
		childFolder2.getLeftValue();

		assert childFolder2.getRightValue() == 5L : "Right value should be 2 but is " + 
		childFolder2.getRightValue();
		
		
		// add another child
		PersonalFolder subFolder1 = null;
		try
		{
		    subFolder1 = childFolder1.createChild("subFolder1");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		
		assert personalFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalFolder1.getLeftValue();

		assert personalFolder1.getRightValue() == 8L : "Right value should be 8 but is " + 
		personalFolder1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 2 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 5L : "Right value should be 5 but is " + 
		childFolder1.getRightValue();
		
		assert subFolder1.getLeftValue() == 3L : "Left value should be 3 but is " + 
		subFolder1.getLeftValue();

		assert subFolder1.getRightValue() == 4L : "Right value should be 4 but is " + 
		subFolder1.getRightValue();
		
		assert childFolder2.getLeftValue() == 6L : "Left value should be 6 but is " + 
		childFolder2.getLeftValue();

		assert childFolder2.getRightValue() == 7L : "Right value should be 7 but is " + 
		childFolder2.getRightValue();
		
		// add another child
		 PersonalFolder subFolder2 = null;
		try
		{
		    subFolder2 = childFolder1.createChild("subFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		
		
		assert personalFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalFolder1.getLeftValue();

		assert personalFolder1.getRightValue() == 10L : "Right value should be 10 but is " + 
		personalFolder1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 2 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 7L : "Right value should be 7 but is " + 
		childFolder1.getRightValue();
		
		assert subFolder1.getLeftValue() == 3L : "Left value should be 3 but is " + 
		subFolder1.getLeftValue();

		assert subFolder1.getRightValue() == 4L : "Right value should be 4 but is " + 
		subFolder1.getRightValue();
		
		assert subFolder2.getLeftValue() == 5L : "Left value should be 5 but is " + 
		subFolder1.getLeftValue();

		assert subFolder2.getRightValue() == 6L : "Right value should be 6 but is " + 
		subFolder1.getRightValue();
		
		assert childFolder2.getLeftValue() == 8L : "Left value should be 8 but is " + 
		childFolder2.getLeftValue();

		assert childFolder2.getRightValue() == 9L : "Right value should be 9 but is " + 
		childFolder2.getRightValue();
		


		// persist the tree
		personalFolderDAO.makePersistent(personalFolder1);
		personalFolderDAO.makePersistent(personalFolder2);
		
		
		/**********************************
		 * TEST TREE SELECTS
		 **********************************/
 
	   
        TransactionStatus ts = tm.getTransaction(td);

		
		// make sure we can grab nodes that have a left and right value greater than
		// a specified value.
		List<PersonalFolder> nodes = personalFolderDAO.getNodesLeftRightGreaterEqual(personalFolder1.getRightValue(),
				childFolder1.getTreeRoot().getId());
		
		assert nodes.size() == 1 : "Expected root node but got "  + nodes.size();
		
		nodes = personalFolderDAO.getNodesLeftRightGreaterEqual(personalFolder1.getLeftValue(),
				childFolder1.getTreeRoot().getId());
		
		assert nodes.size() == 5 : "Expected all 5 nodes but got " + nodes.size();

		
		// make sure tree without specified nodes can be retrieved
		nodes = personalFolderDAO.getAllNodesNotInChildFolder(personalFolder1);
        assert nodes.size() == 0 : "Nodes size should be zero but is " + nodes.size() + "using data "
            + " id = " + personalFolder1.getId() + " and root id = " + personalFolder1.getTreeRoot().getId();
		
        // try now with folder 1.
        nodes = personalFolderDAO.getAllNodesNotInChildFolder(childFolder1);
        assert nodes.size() == 2 : "Nodes size should be 2 but is " + nodes.size() + " using data "
         + " id = " + childFolder1.getId() + " and root id = " + childFolder1.getTreeRoot().getId();
        tm.commit(ts);
        
         /*****************************************
         * Test selects with multiple folders
         *****************************************/
	
        
        List<Long> folderIds = new LinkedList<Long>();
        folderIds.add(personalFolder1.getId());
        folderIds.add(childFolder1.getId());
        folderIds.add(subFolder1.getId());
        folderIds.add(childFolder2.getId());
        
        List<PersonalFolder> inFolders = personalFolderDAO.getFolders(user.getId(), folderIds);
        assert inFolders.size() == folderIds.size() : "The folders size should be equal";
        
        for( PersonalFolder pf : inFolders)
        {
        	assert folderIds.contains(pf.getId()) : "Folder Ids should contain " + pf.getId();
        }
        
        // test grabbing a set of folders
        List<PersonalFolder> folders = new LinkedList<PersonalFolder>();
        folders.add(personalFolder1);
        nodes = personalFolderDAO.getAllFoldersNotInChildFolders(folders, personalFolder1.getOwner().getId(), 
        		personalFolder1.getTreeRoot().getId());
        assert nodes.size() == 0 : "Nodes size should be zero but is " + nodes.size() + "using data "
        + " id = " + personalFolder1.getId() + " and root id = " + personalFolder1.getTreeRoot().getId();
        
        
        folders = new LinkedList<PersonalFolder>();
        folders.add(childFolder1);
        
        // try now with child folder 1.
        nodes = personalFolderDAO.getAllFoldersNotInChildFolders(folders, childFolder1.getOwner().getId(), 
        		childFolder1.getTreeRoot().getId());
        assert nodes.size() == 2 : "Nodes size should be 2 but is " + nodes.size() + " using data "
         + " id = " + childFolder1.getId() + " and root id = " + childFolder1.getTreeRoot().getId();
        
        /*****************************************
         * END TEST TREE SELECTS
         *****************************************/
	
	    
        // Start the transaction this is for lazy loading
        ts = tm.getTransaction(td);
        
		// make sure object has been persisted
		PersonalFolder other = personalFolderDAO.getById(personalFolder1.getId(), false);
		assert other != null : "PersonalFolder should be found";
		assert other.equals(personalFolder1) : "PersonalFolder should be the same as is1 ";

		// make sure types are correct
		PersonalFolder other2 = personalFolderDAO.getById(personalFolder2.getId(), false);
		assert !other.equals(other2) : "PersonalFolder should be different";

		assert other.getChild(childFolder1.getName()).equals(
				childFolder1) : "PersonalFolder should have child";
		assert other.getChild(childFolder2.getName()).equals(
				childFolder2) : "PersonalFolder should have child";

		String name = childFolder1.getName();
		assert other.getChild(name).getChild(subFolder1.getName())
				.equals(subFolder1) : "PersonalFolder should have child";
		assert other.getChild(name).getChild(subFolder2.getName())
				.equals(subFolder2) : "PersonalFolder should have child";

		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		//*************************************
		// Make sure we can add new nodes after
		// data has been committed.
        //*************************************
	
		// Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);

		//PersonalFolder myRoot = personalFolderDAO.getById(personalFolder1.getId(), false);
		// make sure object has been persisted
		PersonalFolder akaSubTreeFolderInfo1 = personalFolderDAO.getById(subFolder1.getId(), false);
		
		assert akaSubTreeFolderInfo1.getLeftValue() == 3L : "Left value should be 5 but is " + 
		akaSubTreeFolderInfo1.getLeftValue();

		assert akaSubTreeFolderInfo1.getRightValue() == 4L : "Right value should be 6 but is " + 
		akaSubTreeFolderInfo1.getRightValue();
		
		assert !akaSubTreeFolderInfo1.isRoot(): "Should not be root folder";
		
		
		PersonalFolder akaSubTreeParent = akaSubTreeFolderInfo1.getParent();
		
		assert akaSubTreeParent.getLeftValue() == 2L : "Left value should be 2 but is " + 
		childFolder1.getLeftValue();

		assert akaSubTreeParent.getRightValue() == 7L : "Right value should be 7 but is " + 
		childFolder1.getRightValue();
		
		PersonalFolder akaRoot =  akaSubTreeParent.getParent();
		
		assert akaRoot.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalFolder1.getLeftValue();

		assert akaRoot.getRightValue() == 10L : "Right value should be 10 but is " + 
		personalFolder1.getRightValue();
		
		PersonalFolder subSubTreeFolderInfo1 = null;
		try
		{
		    subSubTreeFolderInfo1 = 
			    akaSubTreeFolderInfo1.createChild("subSubFolder1");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert akaSubTreeFolderInfo1.getLeftValue() == 3L : "Left value should be 3 but is " + 
		akaSubTreeFolderInfo1.getLeftValue();

		assert akaSubTreeFolderInfo1.getRightValue() == 6L : "Right value should be 6 but is " + 
		akaSubTreeFolderInfo1.getRightValue();
		
		assert subSubTreeFolderInfo1.getLeftValue() == 4L : "Left value should be 4 but is " + 
		subSubTreeFolderInfo1.getLeftValue();

		assert subSubTreeFolderInfo1.getRightValue() == 5L : "Right value should be 5 but is " + 
		subSubTreeFolderInfo1.getRightValue();
		
		assert akaSubTreeFolderInfo1.getTreeRoot().getLeftValue() == 1L : "Left value should be 1 but is " + 
		akaSubTreeFolderInfo1.getTreeRoot().getLeftValue();

		assert akaSubTreeFolderInfo1.getTreeRoot().getRightValue() == 12L : "Right value should be 12 but is " + 
		akaSubTreeFolderInfo1.getTreeRoot().getRightValue();
		
		personalFolderDAO.makePersistent(akaSubTreeFolderInfo1.getTreeRoot());
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		
        //*************************************
		// Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		
		PersonalFolder topFolder = personalFolderDAO.getById(personalFolder1.getId(), true);
		
		assert topFolder.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalFolder1.getLeftValue();

		assert topFolder.getRightValue() == 12L : "Right value should be 12 but is " + 
		personalFolder1.getRightValue();
		
		// test deleting an object
		personalFolderDAO.makeTransient(topFolder);
		tm.commit(ts);
		

		// make sure it is gone
        // Start the transaction this is for lazy loading
        ts = tm.getTransaction(td);
        
		assert personalFolderDAO.getById(personalFolder1.getId(), true) == null : "Should not find  TreeFolderInfo1";
		assert personalFolderDAO.getById(personalFolder2.getId(), true).equals(
				personalFolder2) : "should find TreeFolderInfo2";
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		// clean up the rest
		personalFolderDAO.makeTransient(personalFolderDAO.getById(personalFolder2.getId(), true));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);

		assert personalFolderDAO.getById(personalFolder2.getId(), false) == null : "should not find TreeFolderInfo2";

	}
	
	
	/**
	 * Test adding files to a folder
	 * @throws DuplicateNameException 
	 * 
     */
	@Test
	public void personalFolderFileDAOTest() throws DuplicateNameException, IllegalFileSystemNameException {
		
		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");
		tm.commit(ts);
	
        // Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - PersonalFolderFileDAOTest");

		// create the file in the file system.
		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f, "newFile1");
		
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");

		IrUser user = new IrUser("user", "password");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		user.setPasswordEncoding("none");
		
		VersionedFile vif = new VersionedFile(user, fileInfo1, "new file test");
		versionedFileDAO.makePersistent(vif);
		Long irFileId = vif.getCurrentVersion().getIrFile().getId();

		// create the user and their folder.
		userDAO.makePersistent(user);
		PersonalFolder personalFolder1 = user.createRootFolder("topFolder");
		
		try
		{
		    personalFolder1.addVersionedFile(vif);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		personalFolderDAO.makePersistent(personalFolder1);
		tm.commit(ts);	
		
        // Start the transaction this is for lazy loading
		// find the folder and make sure it has the versioned file
		ts = tm.getTransaction(td);
		PersonalFolder topFolder = personalFolderDAO.getById(personalFolder1.getId(), true);
		assert topFolder.getFile(vif.getName()) != null : "Should be able to find the versioned file";
		
		VersionedFile otherVersionedFile = topFolder.getFile(vif.getName()).getVersionedFile();
		assert otherVersionedFile.equals(vif) : "Should be equal but other = " +
		    otherVersionedFile + " vif = " + vif;
		;
		
		List<PersonalFile> personalFiles = personalFolderDAO.getAllFilesForFolder(topFolder);
		assert personalFiles != null : "Should find files";
		assert personalFiles.size() == 1 : "Should find 1 personal file but found " + personalFiles.size();
		
		List<VersionedFile> versionedFiles = personalFolderDAO.getAllVersionedFilesForFolder(topFolder);
		assert versionedFiles != null;
		assert versionedFiles.size() == 1 : "Should find 1 versioned file but found " + versionedFiles.size();
		tm.commit(ts);
		
        // clean up
		ts = tm.getTransaction(td);
		
		personalFolderDAO.makeTransient(personalFolderDAO.getById(personalFolder1.getId(), true));
		versionedFileDAO.makeTransient(versionedFileDAO.getById(otherVersionedFile.getId(), false));
		fileDAO.makeTransient(fileDAO.getById(irFileId, false));
		userDAO.makeTransient(userDAO.getById(user.getId(), true));
		
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test moving personal folders accross two different root folders.
	 */
	@Test
	public void moveFoldersTest()
	{
		try
		{
		TransactionStatus ts = tm.getTransaction(td);
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");

        // create a user who has their own folder
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		// create the user and their folder.
		userDAO.makePersistent(user);
		PersonalFolder folderA = user.createRootFolder("Folder A");
		PersonalFolder folderC = user.createRootFolder("Folder C");
		
		PersonalFolder folderA1 = folderA.createChild("Folder A1");
		
		
		PersonalFolder folderAA1 = folderA1.createChild("Folder AA1");
		PersonalFolder folderAA2 = folderA1.createChild("Folder AA2");
		
		folderC.createChild("Folder C1");

		// save folder c first because it is the new parent
		personalFolderDAO.makePersistent(folderC);
		personalFolderDAO.makePersistent(folderA);
		
		folderC.addChild(folderA1);
		tm.commit(ts);
		
		assert folderAA1.getTreeRoot().equals(folderC) : "Root folder should be " + 
		folderC + " but is " + folderAA1.getTreeRoot();
		assert folderAA2.getTreeRoot().equals(folderC) : "Root folder should be " + 
		folderC + " but is " + folderAA2.getTreeRoot();
		
		ts = tm.getTransaction(td);
		personalFolderDAO.makeTransient(personalFolderDAO.getById(folderA.getId(), true));
		personalFolderDAO.makeTransient(personalFolderDAO.getById(folderC.getId(), true));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		}
		catch(Exception e)
		{
			
			log.debug("failure", e);
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * Test - get root personal folder 
	 * @throws DuplicateNameException 
	 * 
	*/
	@Test
	public void baseGetRootPersonalFolderDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException {
  		
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");

        // create a user who has their own folder
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		// create the user and their folder.
		userDAO.makePersistent(user);
		PersonalFolder personalFolder = user.createRootFolder("topFolder");
		personalFolderDAO.makePersistent(personalFolder);
		
		PersonalFolder personalFolder1 = user.createRootFolder("topFolder1");
		personalFolderDAO.makePersistent(personalFolder1);
		
		assert personalFolderDAO.getRootFolders(user.getId()).size() == 2 : "Should be equal to 2 but equals " + personalFolderDAO.getRootFolders(user.getId());
		
		// Start the transaction 
        TransactionStatus ts = tm.getTransaction(td);
		personalFolderDAO.makeTransient(personalFolderDAO.getById(personalFolder.getId(), false));
		personalFolderDAO.makeTransient(personalFolderDAO.getById(personalFolder1.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert personalFolderDAO.getById(personalFolder.getId(), false) == null: 
			"Should not be able to find personal folder";
		assert personalFolderDAO.getById(personalFolder1.getId(), false) == null: 
			"Should not be able to find personal folder";

		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		
	}

	/**
	 * Test - get folders within a folder
	 * @throws DuplicateNameException 
	 * 
	*/
	@Test
	public void baseGetFolderInsideFolderDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException {
  		
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");

        // create a user who has their own folder
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		// create the user and their folder.
		userDAO.makePersistent(user);
		PersonalFolder personalFolder = user.createRootFolder("topFolder");
		personalFolderDAO.makePersistent(personalFolder);
		
		PersonalFolder personalFolder1 = null;
		PersonalFolder personalFolder2 = null;
		try
		{
		    personalFolder1 = personalFolder.createChild("subFolder1");
		    personalFolderDAO.makePersistent(personalFolder1);
		
		    personalFolder2 = personalFolder.createChild("subFolder2");
		    personalFolderDAO.makePersistent(personalFolder2);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert personalFolderDAO.getSubFoldersForFolder(user.getId(), personalFolder.getId()).size() == 2 : "Should be equal to 2";
		
		// Start the transaction 
        TransactionStatus ts = tm.getTransaction(td);
		personalFolderDAO.makeTransient(personalFolderDAO.getById(personalFolder1.getId(), false));
		personalFolderDAO.makeTransient(personalFolderDAO.getById(personalFolder2.getId(), false));
		personalFolderDAO.makeTransient(personalFolderDAO.getById(personalFolder.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert personalFolderDAO.getById(personalFolder.getId(), false) == null: 
			"Should not be able to find personal folder";
		assert personalFolderDAO.getById(personalFolder1.getId(), false) == null: 
			"Should not be able to find personal folder";

		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		
		tm.commit(ts);
		
	}

	/**
	 * Test - get folder size
     */
	@Test
	public void getFolderSizeDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException {
  		
		TransactionStatus ts = tm.getTransaction(td);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer",
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");
		
		// save the repository
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		FileServerService fileServerService = repoHelper.getFileServerService();
		FileInfo info = fileServerService.addFile(repo.getFileDatabase(), f,
				uniqueNameGenerator.getNextName(), "txt");
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("none");
		VersionedFile versionedFile = new VersionedFile(user, info, "test file");
        
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");
		
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		PersonalFolder personalFolder = user.createRootFolder("topFolder");
		PersonalFile pf = personalFolder.addVersionedFile(versionedFile);
		// create the user and their folder.
		userDAO.makePersistent(user);
		tm.commit(ts);

		
		ts = tm.getTransaction(td);
		Long irFileId = versionedFile.getCurrentVersion().getIrFile().getId();
		assert personalFolderDAO.getFolderSize(user.getId(), personalFolder.getId()).equals(versionedFile.getTotalSizeForAllFilesBytes()) : "Size should be equal";
		personalFolder.removePersonalFile(pf);
		// create the user and their folder.
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		// Start the transaction 
        ts = tm.getTransaction(td);
        personalFolderDAO.makeTransient(personalFolderDAO.getById(personalFolder.getId(), false));
		versionedFileDAO.makeTransient(versionedFileDAO.getById(versionedFile.getId(), false));
		fileDAO.makeTransient(fileDAO.getById(irFileId, false));
	    
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
		
	}

}
