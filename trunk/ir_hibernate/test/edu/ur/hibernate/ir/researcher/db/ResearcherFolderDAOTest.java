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

package edu.ur.hibernate.ir.researcher.db;

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
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherDAO;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherFolderDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserManager;
import edu.ur.util.FileUtil;

/**
 * Test the persistence methods for researcher folders
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ResearcherFolderDAOTest {
	
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
	
	/** Researcher folder data access */
	ResearcherFolderDAO researcherFolderDAO = (ResearcherFolderDAO) ctx
		.getBean("researcherFolderDAO");

    /** User data access */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");
    
    /** Researcher data access */
    ResearcherDAO researcherDAO= (ResearcherDAO) ctx.getBean("researcherDAO");
    
	/** IrFile relational data access   */
    IrFileDAO fileDAO= (IrFileDAO) ctx
	.getBean("irFileDAO");
	
	/** Logger */
	private static final Logger log = Logger.getLogger(ResearcherFolderDAOTest.class);
	
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
	 * Test researcher folder persistence
	 * @throws DuplicateNameException 
	 * 
	*/
	@Test
	public void baseResearcherFolderDAOTest() throws DuplicateNameException {
  		
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
		
		Researcher researcher = new Researcher(user);
		researcherDAO.makePersistent(researcher);
		
		ResearcherFolder researcherFolder = researcher.createRootFolder("topFolder");
		researcherFolderDAO.makePersistent(researcherFolder);
		
		assert researcherFolderDAO.getById(researcherFolder.getId(), false) != null : "Should be able to find "
			+ " researcher Folder " + researcherFolder;
		
        // Start the transaction 
        TransactionStatus ts = tm.getTransaction(td);
		assert researcherFolderDAO.getById(researcherFolder.getId(), false).equals(researcherFolder) : 
			"Folders should be equal";
		
		assert researcherFolderDAO.getRootResearcherFolder("topFolder", researcher.getId()).equals(researcherFolder) : 
			"Should be able to find folder";
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		researcherFolderDAO.makeTransient(researcherFolderDAO.getById(researcherFolder.getId(), false));
		user.setResearcher(null);
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert researcherFolderDAO.getById(researcherFolder.getId(), false) == null: 
			"Should not be able to find researcher folder";
		researcherDAO.makeTransient(researcherDAO.getById(researcher.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		
	}
	 
	/**
	 * Test ResearcherFolder persistence with children
	 * @throws DuplicateNameException 
	 * 
	 */
	@Test
	public void researcherFolderWithChildrenDAOTest() throws DuplicateNameException {

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
		
		Researcher researcher = new Researcher(user);
		researcherDAO.makePersistent(researcher);
		
		ResearcherFolder researcherFolder1 = researcher.createRootFolder("topFolder");
		researcherFolderDAO.makePersistent(researcherFolder1);
		
		// make sure parent starts out with 1 and 2 left/right values.
		assert researcherFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		researcherFolder1.getLeftValue();

		assert researcherFolder1.getRightValue() == 2L : "Right value should be 2 but is " + 
		researcherFolder1.getRightValue();

		ResearcherFolder researcherFolder2 = researcher.createRootFolder("topFolder2");
		
		assert researcherFolder2.getLeftValue() == 1L : "Left value should be 1 but is " + 
		researcherFolder2.getLeftValue();

		assert researcherFolder2.getRightValue() == 2L : "Right value should be 2 but is " + 
		researcherFolder2.getRightValue();
		
		// add a child
		ResearcherFolder childFolder1 = null;
		try
		{
		    childFolder1 = researcherFolder1.createChild("childFolder1");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert researcherFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		researcherFolder1.getLeftValue();

		assert researcherFolder1.getRightValue() == 4L : "Right value should be 2 but is " + 
		researcherFolder1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 1 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 3L : "Right value should be 2 but is " + 
		childFolder1.getRightValue();
		
		
		// add another child
		 ResearcherFolder childFolder2 = null;
		try
		{
		   childFolder2 = researcherFolder1.createChild("child2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert researcherFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		researcherFolder1.getLeftValue();

		assert researcherFolder1.getRightValue() == 6L : "Right value should be 2 but is " + 
		researcherFolder1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 1 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 3L : "Right value should be 2 but is " + 
		childFolder1.getRightValue();
		
		assert childFolder2.getLeftValue() == 4L : "Left value should be 1 but is " + 
		childFolder2.getLeftValue();

		assert childFolder2.getRightValue() == 5L : "Right value should be 2 but is " + 
		childFolder2.getRightValue();
		
		
		// add another child
		ResearcherFolder subFolder1 = null;
		try
		{
		    subFolder1 = childFolder1.createChild("subFolder1");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		
		assert researcherFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		researcherFolder1.getLeftValue();

		assert researcherFolder1.getRightValue() == 8L : "Right value should be 8 but is " + 
		researcherFolder1.getRightValue();
		
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
		 ResearcherFolder subFolder2 = null;
		try
		{
		    subFolder2 = childFolder1.createChild("subFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		
		
		assert researcherFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		researcherFolder1.getLeftValue();

		assert researcherFolder1.getRightValue() == 10L : "Right value should be 10 but is " + 
		researcherFolder1.getRightValue();
		
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
		researcherFolderDAO.makePersistent(researcherFolder1);
		researcherFolderDAO.makePersistent(researcherFolder2);
		
		
		/**********************************
		 * TEST TREE SELECTS
		 **********************************/
 
	   
        TransactionStatus ts = tm.getTransaction(td);

		
		// make sure we can grab nodes that have a left and right value greater than
		// a specified value.
		List<ResearcherFolder> nodes = researcherFolderDAO.getNodesLeftRightGreaterEqual(researcherFolder1.getRightValue(),
				childFolder1.getTreeRoot().getId());
		
		assert nodes.size() == 1 : "Expected root node but got "  + nodes.size();
		
		nodes = researcherFolderDAO.getNodesLeftRightGreaterEqual(researcherFolder1.getLeftValue(),
				childFolder1.getTreeRoot().getId());
		
		assert nodes.size() == 5 : "Expected all 5 nodes but got " + nodes.size();

		
		// make sure tree without specified nodes can be retrieved
		nodes = researcherFolderDAO.getAllNodesNotInChildFolder(researcherFolder1);
        assert nodes.size() == 0 : "Nodes size should be zero but is " + nodes.size() + "using data "
            + " id = " + researcherFolder1.getId() + " and root id = " + researcherFolder1.getTreeRoot().getId();
		
        // try now with folder 1.
        nodes = researcherFolderDAO.getAllNodesNotInChildFolder(childFolder1);
        assert nodes.size() == 2 : "Nodes size should be 2 but is " + nodes.size() + " using data "
         + " id = " + childFolder1.getId() + " and root id = " + childFolder1.getTreeRoot().getId();
        
        
        nodes = researcherFolderDAO.getSubFoldersForFolder(researcher.getId(), researcherFolder1.getId());
        assert nodes.size() == 2 : "Should find the two folders";
        assert nodes.contains(childFolder1) : "Should find child folder one";
        assert nodes.contains(childFolder2) : "Should find child folder 2";
        tm.commit(ts);

        /*****************************************
         * Test selects with multiple folders
         *****************************************/
        ts = tm.getTransaction(td);
        
        List<Long> folderIds = new LinkedList<Long>();
        folderIds.add(researcherFolder1.getId());
        folderIds.add(childFolder1.getId());
        folderIds.add(subFolder1.getId());
        folderIds.add(childFolder2.getId());
        
        List<ResearcherFolder> inFolders = researcherFolderDAO.getFolders(researcher.getId(), folderIds);
        assert inFolders.size() == folderIds.size() : "The folders size should be equal " + inFolders.size() + " folderIds size = " + folderIds.size();
        
        for( ResearcherFolder pf : inFolders)
        {
        	assert folderIds.contains(pf.getId()) : "Folder Ids should contain " + pf.getId();
        }
        
        // test grabbing a set of folders
        List<ResearcherFolder> folders = new LinkedList<ResearcherFolder>();
        folders.add(researcherFolder1);
        nodes = researcherFolderDAO.getAllFoldersNotInChildFolders(folders, researcherFolder1.getResearcher().getId(), 
        		researcherFolder1.getTreeRoot().getId());
        assert nodes.size() == 0 : "Nodes size should be zero but is " + nodes.size() + "using data "
        + " id = " + researcherFolder1.getId() + " and root id = " + researcherFolder1.getTreeRoot().getId();
        
        
        folders = new LinkedList<ResearcherFolder>();
        folders.add(childFolder1);
        
        // try now with child folder 1.
        nodes = researcherFolderDAO.getAllFoldersNotInChildFolders(folders, childFolder1.getResearcher().getId(), 
        		childFolder1.getTreeRoot().getId());
        assert nodes.size() == 2 : "Nodes size should be 2 but is " + nodes.size() + " using data "
         + " id = " + childFolder1.getId() + " and root id = " + childFolder1.getTreeRoot().getId();
        
        /*****************************************
         * END TEST TREE SELECTS
         *****************************************/
	    tm.commit(ts);
	    
        // Start the transaction this is for lazy loading
        ts = tm.getTransaction(td);
        
		// make sure object has been persisted
		ResearcherFolder other = researcherFolderDAO.getById(researcherFolder1.getId(), false);
		assert other != null : "ResearcherFolder should be found";
		assert other.equals(researcherFolder1) : "ResearcherFolder should be the same as is1 ";

		// make sure types are correct
		ResearcherFolder other2 = researcherFolderDAO.getById(researcherFolder2.getId(), false);
		assert !other.equals(other2) : "ResearcherFolder should be different";

		assert other.getChild(childFolder1.getName()).equals(
				childFolder1) : "ResearcherFolder should have child";
		assert other.getChild(childFolder2.getName()).equals(
				childFolder2) : "ResearcherFolder should have child";

		String name = childFolder1.getName();
		assert other.getChild(name).getChild(subFolder1.getName())
				.equals(subFolder1) : "ResearcherFolder should have child";
		assert other.getChild(name).getChild(subFolder2.getName())
				.equals(subFolder2) : "ResearcherFolder should have child";

		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		//*************************************
		// Make sure we can add new nodes after
		// data has been committed.
        //*************************************
	
		// Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);

		//ResearcherFolder myRoot = researcherFolderDAO.getById(researcherFolder1.getId(), false);
		// make sure object has been persisted
		ResearcherFolder akaSubTreeFolderInfo1 = researcherFolderDAO.getById(subFolder1.getId(), false);
		
		assert akaSubTreeFolderInfo1.getLeftValue() == 3L : "Left value should be 5 but is " + 
		akaSubTreeFolderInfo1.getLeftValue();

		assert akaSubTreeFolderInfo1.getRightValue() == 4L : "Right value should be 6 but is " + 
		akaSubTreeFolderInfo1.getRightValue();
		
		assert !akaSubTreeFolderInfo1.isRoot(): "Should not be root folder";
		
		
		ResearcherFolder akaSubTreeParent = akaSubTreeFolderInfo1.getParent();
		
		assert akaSubTreeParent.getLeftValue() == 2L : "Left value should be 2 but is " + 
		childFolder1.getLeftValue();

		assert akaSubTreeParent.getRightValue() == 7L : "Right value should be 7 but is " + 
		childFolder1.getRightValue();
		
		ResearcherFolder akaRoot =  akaSubTreeParent.getParent();
		
		assert akaRoot.getLeftValue() == 1L : "Left value should be 1 but is " + 
		researcherFolder1.getLeftValue();

		assert akaRoot.getRightValue() == 10L : "Right value should be 10 but is " + 
		researcherFolder1.getRightValue();
		
		ResearcherFolder subSubTreeFolderInfo1 = null;
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
		
		researcherFolderDAO.makePersistent(akaSubTreeFolderInfo1.getTreeRoot());
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		
        //*************************************
		// Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		
		ResearcherFolder topFolder = researcherFolderDAO.getById(researcherFolder1.getId(), true);
		
		assert topFolder.getLeftValue() == 1L : "Left value should be 1 but is " + 
		researcherFolder1.getLeftValue();

		assert topFolder.getRightValue() == 12L : "Right value should be 12 but is " + 
		researcherFolder1.getRightValue();
		
		// test deleting an object
		researcherFolderDAO.makeTransient(topFolder);
		tm.commit(ts);
		

		// make sure it is gone
        // Start the transaction this is for lazy loading
        ts = tm.getTransaction(td);
        
		assert researcherFolderDAO.getById(researcherFolder1.getId(), true) == null : "Should not find  TreeFolderInfo1";
		assert researcherFolderDAO.getById(researcherFolder2.getId(), true).equals(
				researcherFolder2) : "should find TreeFolderInfo2";
		
		IrUser otherUser = userDAO.getById(user.getId(), false);
		otherUser.setResearcher(null);
		userDAO.makePersistent(otherUser);
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		// clean up the rest
		researcherFolderDAO.makeTransient(researcherFolderDAO.getById(researcherFolder2.getId(), true));
		researcherDAO.makeTransient(researcherDAO.getById(researcher.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);

		assert researcherFolderDAO.getById(researcherFolder2.getId(), false) == null : "should not find TreeFolderInfo2";

	}
	
	
	/**
	 * Test adding files to a folder
	 * @throws DuplicateNameException 
	 * 
     */
	@Test
	public void researcherFolderFileDAOTest() throws DuplicateNameException, IllegalFileSystemNameException {
		
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
		"Hello  - irFile This is text in a file - ResearcherFolderFileDAOTest");

		// create the file in the file system.
		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f, "newFile1");
		
		UserEmail userEmail = new UserEmail("nathans@library.rochester.edu");

		IrUser user = new IrUser("user", "password");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		user.setPasswordEncoding("none");
		
		// create the user and their folder.
		userDAO.makePersistent(user);
		
		Researcher researcher = new Researcher(user);
		researcherDAO.makePersistent(researcher);

		IrFile irf = new IrFile(fileInfo1, "new file test");
		fileDAO.makePersistent(irf);
		
		ResearcherFolder researcherFolder1 = researcher.createRootFolder("topFolder");
		
		try
		{
		    researcherFolder1.addFile(irf);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		researcherFolderDAO.makePersistent(researcherFolder1);
		tm.commit(ts);	
		
        // Start the transaction this is for lazy loading
		// find the folder and make sure it has the versioned file
		ts = tm.getTransaction(td);
		ResearcherFolder topFolder = researcherFolderDAO.getById(researcherFolder1.getId(), true);
		assert topFolder.getResearcherFile(irf.getName()) != null : "Should be able to find the versioned file";
		
		IrFile otherIrFile = topFolder.getResearcherFile(irf.getName()).getIrFile();
		assert otherIrFile.equals(irf) : "Should be equal but other = " +
		otherIrFile + " ifr = " + irf;
		
		
		List<ResearcherFile> researcherFiles = researcherFolderDAO.getAllFilesForFolder(topFolder);
		assert researcherFiles != null : "Should find files";
		assert researcherFiles.size() == 1 : "Should find 1 researcher file but found " + researcherFiles.size();
		
		List<IrFile> irFiles = researcherFolderDAO.getAllIrFilesForFolder(topFolder);
		assert irFiles != null;
		assert irFiles.size() == 1 : "Should find 1 ir file but found " + irFiles.size();
		
		user = userDAO.getById(user.getId(), false);
		user.setResearcher(null);
		userDAO.makePersistent(user);

		tm.commit(ts);
		
        // clean up
		ts = tm.getTransaction(td);
		
		researcherFolderDAO.makeTransient(researcherFolderDAO.getById(researcherFolder1.getId(), true));
		fileDAO.makeTransient(fileDAO.getById(otherIrFile.getId(), false));
		researcherDAO.makeTransient(researcherDAO.getById(researcher.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), true));
		
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test moving researcher folders accross two different root folders.
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
		
		Researcher researcher = new Researcher(user);
		researcherDAO.makePersistent(researcher);
		
		ResearcherFolder folderA = researcher.createRootFolder("Folder A");
		ResearcherFolder folderC = researcher.createRootFolder("Folder C");
		
		ResearcherFolder folderA1 = folderA.createChild("Folder A1");
		
		
		ResearcherFolder folderAA1 = folderA1.createChild("Folder AA1");
		ResearcherFolder folderAA2 = folderA1.createChild("Folder AA2");
		
		folderC.createChild("Folder C1");

		// save folder c first because it is the new parent
		researcherFolderDAO.makePersistent(folderC);
		researcherFolderDAO.makePersistent(folderA);
		
		folderC.addChild(folderA1);
		tm.commit(ts);
		
		assert folderAA1.getTreeRoot().equals(folderC) : "Root folder should be " + 
		folderC + " but is " + folderAA1.getTreeRoot();
		assert folderAA2.getTreeRoot().equals(folderC) : "Root folder should be " + 
		folderC + " but is " + folderAA2.getTreeRoot();
		
		ts = tm.getTransaction(td);
		user.setResearcher(null);
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		researcherFolderDAO.makeTransient(researcherFolderDAO.getById(folderA.getId(), true));
		researcherFolderDAO.makeTransient(researcherFolderDAO.getById(folderC.getId(), true));
		researcherDAO.makeTransient(researcherDAO.getById(researcher.getId(), false));
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
	 * Test - get root researcher folder 
	 * @throws DuplicateNameException 
	 * 
	*/
	@Test
	public void baseGetRootResearcherFolderDAOTest() throws DuplicateNameException {
  		
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
		
		Researcher researcher = new Researcher(user);
		researcherDAO.makePersistent(researcher);
		
		ResearcherFolder researcherFolder = researcher.createRootFolder("topFolder");
		researcherFolderDAO.makePersistent(researcherFolder);
		
		ResearcherFolder researcherFolder1 = researcher.createRootFolder("topFolder1");
		researcherFolderDAO.makePersistent(researcherFolder1);
		
		assert researcherFolderDAO.getRootFolders(researcher.getId()).size() == 2 : "Should be equal to 2";
		
		// Start the transaction 
        TransactionStatus ts = tm.getTransaction(td);
		researcherFolderDAO.makeTransient(researcherFolderDAO.getById(researcherFolder.getId(), false));
		researcherFolderDAO.makeTransient(researcherFolderDAO.getById(researcherFolder1.getId(), false));
		user.setResearcher(null);
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert researcherFolderDAO.getById(researcherFolder.getId(), false) == null: 
			"Should not be able to find researcher folder";
		assert researcherFolderDAO.getById(researcherFolder1.getId(), false) == null: 
			"Should not be able to find researcher folder";
		researcherDAO.makeTransient(researcherDAO.getById(researcher.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		
	}

	/**
	 * Test - get folders within a folder
	 * @throws DuplicateNameException 
	 * 
	*/
	@Test
	public void baseGetFolderInsideFolderDAOTest() throws DuplicateNameException {
  		
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
		
		Researcher researcher = new Researcher(user);
		researcherDAO.makePersistent(researcher);
		
		ResearcherFolder researcherFolder = researcher.createRootFolder("topFolder");
		researcherFolderDAO.makePersistent(researcherFolder);
		
		ResearcherFolder researcherFolder1 = null;
		ResearcherFolder researcherFolder2 = null;
		try
		{
		    researcherFolder1 = researcherFolder.createChild("subFolder1");
		    researcherFolderDAO.makePersistent(researcherFolder1);
		
		    researcherFolder2 = researcherFolder.createChild("subFolder2");
		    researcherFolderDAO.makePersistent(researcherFolder2);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert researcherFolderDAO.getSubFoldersForFolder(researcher.getId(), researcherFolder.getId()).size() == 2 : "Should be equal to 2";
		
		// Start the transaction 
        TransactionStatus ts = tm.getTransaction(td);
		researcherFolderDAO.makeTransient(researcherFolderDAO.getById(researcherFolder1.getId(), false));
		researcherFolderDAO.makeTransient(researcherFolderDAO.getById(researcherFolder2.getId(), false));
		researcherFolderDAO.makeTransient(researcherFolderDAO.getById(researcherFolder.getId(), false));
		user.setResearcher(null);
		userDAO.makePersistent(user);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert researcherFolderDAO.getById(researcherFolder.getId(), false) == null: 
			"Should not be able to find researcher folder";
		assert researcherFolderDAO.getById(researcherFolder1.getId(), false) == null: 
			"Should not be able to find researcher folder";
		
		researcherDAO.makeTransient(researcherDAO.getById(researcher.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		
		tm.commit(ts);
		
	}


}
