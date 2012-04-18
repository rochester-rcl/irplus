package edu.ur.hibernate.ir.groupspace.db;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFile;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolder;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolderDAO;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.util.FileUtil;

@Test(groups = { "baseTests" }, enabled = true)
public class GroupWorkspaceProjectPageFolderDAOTest {
	
	// Properties file with testing specific information. 
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	// Get the properties file  
	Properties properties = propertiesLoader.getProperties();
	
	// get the application context 
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	//  Transaction manager 
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
			.getBean("transactionManager");

	// transaction definition  
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	// Group workspace project page folder data access 
	GroupWorkspaceProjectPageFolderDAO groupWorkspaceProjectPageFolderDAO = (GroupWorkspaceProjectPageFolderDAO) ctx
		.getBean("groupWorkspaceProjectPageFolderDAO");

	// User data access 
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");
    
    // Group workspace project page  data access 
    GroupWorkspaceProjectPageDAO groupWorkspaceProjectPageDAO= (GroupWorkspaceProjectPageDAO) ctx.getBean("groupWorkspaceProjectPageDAO");
    
	// IrFile relational data access   
    IrFileDAO fileDAO= (IrFileDAO) ctx
	.getBean("irFileDAO");
    
	// group workspace data access
	GroupWorkspaceDAO groupWorkspaceDAO = (GroupWorkspaceDAO) ctx
	.getBean("groupWorkspaceDAO");
	
	
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
	 * Test groupWorkspace folder persistence
	 * @throws DuplicateNameException 
	 * 
	*/
	@Test
	public void baseGroupWorkspaceProjectPageFolderDAOTest() throws DuplicateNameException {
		
		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		
		GroupWorkspaceProjectPageFolder folder = groupWorkspaceProjectPage.createRootFolder("topFolder");
		groupWorkspaceProjectPageFolderDAO.makePersistent(folder);
		
		assert groupWorkspaceProjectPageFolderDAO.getById(folder.getId(), false) != null : "Should be able to find "
			+ " groupWorkspace Folder " + folder;
		
        // Start the transaction 
        TransactionStatus ts = tm.getTransaction(td);
		assert groupWorkspaceProjectPageFolderDAO.getById(folder.getId(), false).equals(folder) : 
			"Folders should be equal";
		
		assert groupWorkspaceProjectPageFolderDAO.getRootFolder("topFolder", groupWorkspace.getId()).equals(folder);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
	
		groupWorkspaceProjectPageFolderDAO.makeTransient(groupWorkspaceProjectPageFolderDAO.getById(folder.getId(), false));
		
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert groupWorkspaceProjectPageFolderDAO.getById(folder.getId(), false) == null: 
			"Should not be able to find groupWorkspace folder";
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupWorkspace.getId(), false));
		tm.commit(ts);
		
	}
	 
	/**
	 * Test folder persistence with children
	 * @throws DuplicateNameException 
	 * 
	 */
	@Test
	public void folderWithChildrenDAOTest() throws DuplicateNameException 
	{
		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		
		GroupWorkspaceProjectPageFolder folder1 = groupWorkspaceProjectPage.createRootFolder("topFolder");
		groupWorkspaceProjectPageFolderDAO.makePersistent(folder1);
		
		// make sure parent starts out with 1 and 2 left/right values.
		assert folder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		folder1.getLeftValue();

		assert folder1.getRightValue() == 2L : "Right value should be 2 but is " + 
		folder1.getRightValue();

		GroupWorkspaceProjectPageFolder folder2 = groupWorkspaceProjectPage.createRootFolder("topFolder2");
		
		assert folder2.getLeftValue() == 1L : "Left value should be 1 but is " + 
		folder2.getLeftValue();

		assert folder2.getRightValue() == 2L : "Right value should be 2 but is " + 
		folder2.getRightValue();
		
		// add a child
		GroupWorkspaceProjectPageFolder childFolder1 = null;
		try
		{
		    childFolder1 = folder1.createChild("childFolder1");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert folder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		folder1.getLeftValue();

		assert folder1.getRightValue() == 4L : "Right value should be 2 but is " + 
		folder1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 1 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 3L : "Right value should be 2 but is " + 
		childFolder1.getRightValue();
		
		
		// add another child
		GroupWorkspaceProjectPageFolder childFolder2 = null;
		try
		{
		   childFolder2 = folder1.createChild("child2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert folder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		folder1.getLeftValue();

		assert folder1.getRightValue() == 6L : "Right value should be 2 but is " + 
		folder1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 1 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 3L : "Right value should be 2 but is " + 
		childFolder1.getRightValue();
		
		assert childFolder2.getLeftValue() == 4L : "Left value should be 1 but is " + 
		childFolder2.getLeftValue();

		assert childFolder2.getRightValue() == 5L : "Right value should be 2 but is " + 
		childFolder2.getRightValue();
		
		// add another child
		GroupWorkspaceProjectPageFolder subFolder1 = null;
		try
		{
		    subFolder1 = childFolder1.createChild("subFolder1");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}

		assert folder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		folder1.getLeftValue();

		assert folder1.getRightValue() == 8L : "Right value should be 8 but is " + 
		folder1.getRightValue();
		
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
		GroupWorkspaceProjectPageFolder subFolder2 = null;
		try
		{
		    subFolder2 = childFolder1.createChild("subFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert folder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		folder1.getLeftValue();

		assert folder1.getRightValue() == 10L : "Right value should be 10 but is " + 
		folder1.getRightValue();
		
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
		groupWorkspaceProjectPageFolderDAO.makePersistent(folder1);
		groupWorkspaceProjectPageFolderDAO.makePersistent(folder2);
		
		/**********************************
		 * TEST TREE SELECTS
		 **********************************/
        TransactionStatus ts = tm.getTransaction(td);
		
		// make sure we can grab nodes that have a left and right value greater than
		// a specified value.
		List<GroupWorkspaceProjectPageFolder> nodes = groupWorkspaceProjectPageFolderDAO.getNodesLeftRightGreaterEqual(folder1.getRightValue(),
				childFolder1.getTreeRoot().getId());
		
		assert nodes.size() == 1 : "Expected root node but got "  + nodes.size();
		
		nodes = groupWorkspaceProjectPageFolderDAO.getNodesLeftRightGreaterEqual(folder1.getLeftValue(),
				childFolder1.getTreeRoot().getId());
		
		assert nodes.size() == 5 : "Expected all 5 nodes but got " + nodes.size();

		// make sure tree without specified nodes can be retrieved
		nodes = groupWorkspaceProjectPageFolderDAO.getAllNodesNotInChildFolder(folder1);
        assert nodes.size() == 0 : "Nodes size should be zero but is " + nodes.size() + "using data "
            + " id = " + folder1.getId() + " and root id = " + folder1.getTreeRoot().getId();
		
        // try now with folder 1.
        nodes = groupWorkspaceProjectPageFolderDAO.getAllNodesNotInChildFolder(childFolder1);
        assert nodes.size() == 2 : "Nodes size should be 2 but is " + nodes.size() + " using data "
         + " id = " + childFolder1.getId() + " and root id = " + childFolder1.getTreeRoot().getId();
        
        nodes = groupWorkspaceProjectPageFolderDAO.getSubFoldersForFolder(groupWorkspace.getId(), folder1.getId());
        assert nodes.size() == 2 : "Should find the two folders";
        assert nodes.contains(childFolder1) : "Should find child folder one";
        assert nodes.contains(childFolder2) : "Should find child folder 2";
        tm.commit(ts);

        /*****************************************
         * Test selects with multiple folders
         *****************************************/
        ts = tm.getTransaction(td);
        
        List<Long> folderIds = new LinkedList<Long>();
        folderIds.add(folder1.getId());
        folderIds.add(childFolder1.getId());
        folderIds.add(subFolder1.getId());
        folderIds.add(childFolder2.getId());
        
        List<GroupWorkspaceProjectPageFolder> inFolders = groupWorkspaceProjectPageFolderDAO.getFolders(groupWorkspace.getId(), folderIds);
        assert inFolders.size() == folderIds.size() : "The folders size should be equal " + inFolders.size() + " folderIds size = " + folderIds.size();
        
        for( GroupWorkspaceProjectPageFolder pf : inFolders)
        {
        	assert folderIds.contains(pf.getId()) : "Folder Ids should contain " + pf.getId();
        }
        
        // test grabbing a set of folders
        List<GroupWorkspaceProjectPageFolder> folders = new LinkedList<GroupWorkspaceProjectPageFolder>();
        folders.add(folder1);
        nodes = groupWorkspaceProjectPageFolderDAO.getAllFoldersNotInChildFolders(folders, folder1.getGroupWorkspaceProjectPage().getId(), 
        		folder1.getTreeRoot().getId());
        assert nodes.size() == 0 : "Nodes size should be zero but is " + nodes.size() + "using data "
        + " id = " + folder1.getId() + " and root id = " + folder1.getTreeRoot().getId();
        
        folders = new LinkedList<GroupWorkspaceProjectPageFolder>();
        folders.add(childFolder1);
        
        // try now with child folder 1.
        nodes = groupWorkspaceProjectPageFolderDAO.getAllFoldersNotInChildFolders(folders, childFolder1.getGroupWorkspaceProjectPage().getId(), 
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
        GroupWorkspaceProjectPageFolder other = groupWorkspaceProjectPageFolderDAO.getById(folder1.getId(), false);
		assert other != null : "folder should be found";
		assert other.equals(folder1) : "folder should be the same as is1 ";

		// make sure types are correct
		GroupWorkspaceProjectPageFolder other2 = groupWorkspaceProjectPageFolderDAO.getById(folder2.getId(), false);
		assert !other.equals(other2) : "folder should be different";

		assert other.getChild(childFolder1.getName()).equals(
				childFolder1) : "folder should have child";
		assert other.getChild(childFolder2.getName()).equals(
				childFolder2) : "folder should have child";

		String name = childFolder1.getName();
		assert other.getChild(name).getChild(subFolder1.getName())
				.equals(subFolder1) : "folder should have child";
		assert other.getChild(name).getChild(subFolder2.getName())
				.equals(subFolder2) : "folder should have child";

		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		//*************************************
		// Make sure we can add new nodes after
		// data has been committed.
        //*************************************
	
		// Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);

		//folder myRoot = groupWorkspaceProjectPageFolderDAO.getById(folder1.getId(), false);
		// make sure object has been persisted
		GroupWorkspaceProjectPageFolder akaSubTreeFolderInfo1 = groupWorkspaceProjectPageFolderDAO.getById(subFolder1.getId(), false);
		
		assert akaSubTreeFolderInfo1.getLeftValue() == 3L : "Left value should be 5 but is " + 
		akaSubTreeFolderInfo1.getLeftValue();

		assert akaSubTreeFolderInfo1.getRightValue() == 4L : "Right value should be 6 but is " + 
		akaSubTreeFolderInfo1.getRightValue();
		
		assert !akaSubTreeFolderInfo1.isRoot(): "Should not be root folder";
		
		
		GroupWorkspaceProjectPageFolder akaSubTreeParent = akaSubTreeFolderInfo1.getParent();
		
		assert akaSubTreeParent.getLeftValue() == 2L : "Left value should be 2 but is " + 
		childFolder1.getLeftValue();

		assert akaSubTreeParent.getRightValue() == 7L : "Right value should be 7 but is " + 
		childFolder1.getRightValue();
		
		GroupWorkspaceProjectPageFolder akaRoot =  akaSubTreeParent.getParent();
		
		assert akaRoot.getLeftValue() == 1L : "Left value should be 1 but is " + 
		folder1.getLeftValue();

		assert akaRoot.getRightValue() == 10L : "Right value should be 10 but is " + 
		folder1.getRightValue();
		
		GroupWorkspaceProjectPageFolder subSubTreeFolderInfo1 = null;
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
		
		groupWorkspaceProjectPageFolderDAO.makePersistent(akaSubTreeFolderInfo1.getTreeRoot());
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		
        //*************************************
		// Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		
		GroupWorkspaceProjectPageFolder topFolder = groupWorkspaceProjectPageFolderDAO.getById(folder1.getId(), true);
		
		assert topFolder.getLeftValue() == 1L : "Left value should be 1 but is " + 
		folder1.getLeftValue();

		assert topFolder.getRightValue() == 12L : "Right value should be 12 but is " + 
		folder1.getRightValue();
		
		// test deleting an object
		groupWorkspaceProjectPageFolderDAO.makeTransient(topFolder);
		tm.commit(ts);
		

		// make sure it is gone
        // Start the transaction this is for lazy loading
        ts = tm.getTransaction(td);
        
		assert groupWorkspaceProjectPageFolderDAO.getById(folder1.getId(), true) == null : "Should not find  TreeFolderInfo1";
		assert groupWorkspaceProjectPageFolderDAO.getById(folder2.getId(), true).equals(
				folder2) : "should find TreeFolderInfo2";
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		// clean up the rest
		groupWorkspaceProjectPageFolderDAO.makeTransient(groupWorkspaceProjectPageFolderDAO.getById(folder2.getId(), true));
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupWorkspace.getId(), false));
		tm.commit(ts);

		assert groupWorkspaceProjectPageFolderDAO.getById(folder2.getId(), false) == null : "should not find TreeFolderInfo2";

	}
	
	
	/**
	 * Test adding files to a folder
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 * 
     */
	@Test
	public void folderFileDAOTest() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException {
		
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
		"Hello  - irFile This is text in a file - folderFileDAOTest");

		// create the file in the file system.
		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f, "newFile1");
		
	
		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);

		IrFile irf = new IrFile(fileInfo1, "newFile1");
		fileDAO.makePersistent(irf);
		
		GroupWorkspaceProjectPageFolder folder1 = groupWorkspaceProjectPage.createRootFolder("topFolder");
		
		try
		{
		    folder1.addFile(irf);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		groupWorkspaceProjectPageFolderDAO.makePersistent(folder1);
		tm.commit(ts);	
		
        // Start the transaction this is for lazy loading
		// find the folder and make sure it has the versioned file
		ts = tm.getTransaction(td);
		GroupWorkspaceProjectPageFolder topFolder = groupWorkspaceProjectPageFolderDAO.getById(folder1.getId(), true);
		assert topFolder.getFile(irf) != null : "Should be able to find the versioned file";
		
		IrFile otherIrFile = topFolder.getFile(irf).getIrFile();
		assert otherIrFile.equals(irf) : "Should be equal but other = " +
		otherIrFile + " ifr = " + irf;
		
		
		List<GroupWorkspaceProjectPageFile> groupWorkspaceFiles = groupWorkspaceProjectPageFolderDAO.getAllFiles(topFolder);
		assert groupWorkspaceFiles != null : "Should find files";
		assert groupWorkspaceFiles.size() == 1 : "Should find 1 groupWorkspace file but found " + groupWorkspaceFiles.size();
		
		List<IrFile> irFiles = groupWorkspaceProjectPageFolderDAO.getAllIrFiles(topFolder);
		assert irFiles != null;
		assert irFiles.size() == 1 : "Should find 1 ir file but found " + irFiles.size();
		
		
		tm.commit(ts);
		
        // clean up
		ts = tm.getTransaction(td);
		
		groupWorkspaceProjectPageFolderDAO.makeTransient(groupWorkspaceProjectPageFolderDAO.getById(folder1.getId(), true));
		fileDAO.makeTransient(fileDAO.getById(otherIrFile.getId(), false));
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupWorkspace.getId(), false));
		
		repoHelper.cleanUpRepository();
		tm.commit(ts);	
	}
	
	/**
	 * Test moving groupWorkspace folders accross two different root folders.
	 * @throws DuplicateNameException 
	 */
	@Test
	public void moveFoldersTest() throws DuplicateNameException
	{
		TransactionStatus ts = tm.getTransaction(td);
		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		
		
		GroupWorkspaceProjectPageFolder folderA = groupWorkspaceProjectPage.createRootFolder("Folder A");
		GroupWorkspaceProjectPageFolder folderC = groupWorkspaceProjectPage.createRootFolder("Folder C");
		
		GroupWorkspaceProjectPageFolder folderA1 = folderA.createChild("Folder A1");
		
		
		GroupWorkspaceProjectPageFolder folderAA1 = folderA1.createChild("Folder AA1");
		GroupWorkspaceProjectPageFolder folderAA2 = folderA1.createChild("Folder AA2");
		
		folderC.createChild("Folder C1");

		// save folder c first because it is the new parent
		groupWorkspaceProjectPageFolderDAO.makePersistent(folderC);
		groupWorkspaceProjectPageFolderDAO.makePersistent(folderA);
		
		folderC.addChild(folderA1);
		tm.commit(ts);
		
		assert folderAA1.getTreeRoot().equals(folderC) : "Root folder should be " + 
		folderC + " but is " + folderAA1.getTreeRoot();
		assert folderAA2.getTreeRoot().equals(folderC) : "Root folder should be " + 
		folderC + " but is " + folderAA2.getTreeRoot();
		
		ts = tm.getTransaction(td);
		groupWorkspaceProjectPageFolderDAO.makeTransient(groupWorkspaceProjectPageFolderDAO.getById(folderA.getId(), true));
		groupWorkspaceProjectPageFolderDAO.makeTransient(groupWorkspaceProjectPageFolderDAO.getById(folderC.getId(), true));
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupWorkspace.getId(), false));
		tm.commit(ts);
		
	}
	
	
	/**
	 * Test - get root groupWorkspace folder 
	 * @throws DuplicateNameException 
	 * 
	*/
	@Test
	public void baseGetRootgroupWorkspaceProjectPageFolderDAOTest() throws DuplicateNameException {
  		
		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		
		GroupWorkspaceProjectPageFolder folder = groupWorkspaceProjectPage.createRootFolder("topFolder");
		groupWorkspaceProjectPageFolderDAO.makePersistent(folder);
		
		GroupWorkspaceProjectPageFolder folder1 = groupWorkspaceProjectPage.createRootFolder("topFolder1");
		groupWorkspaceProjectPageFolderDAO.makePersistent(folder1);
		
		assert groupWorkspaceProjectPageFolderDAO.getRootFolders(groupWorkspace.getId()).size() == 2 : "Should be equal to 2";
		
		// Start the transaction 
        TransactionStatus ts = tm.getTransaction(td);
		groupWorkspaceProjectPageFolderDAO.makeTransient(groupWorkspaceProjectPageFolderDAO.getById(folder.getId(), false));
		groupWorkspaceProjectPageFolderDAO.makeTransient(groupWorkspaceProjectPageFolderDAO.getById(folder1.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert groupWorkspaceProjectPageFolderDAO.getById(folder.getId(), false) == null: 
			"Should not be able to find groupWorkspace folder";
		assert groupWorkspaceProjectPageFolderDAO.getById(folder1.getId(), false) == null: 
			"Should not be able to find groupWorkspace folder";
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupWorkspace.getId(), false));
		
		tm.commit(ts);
		
	}

	/**
	 * Test - get folders within a folder
	 * @throws DuplicateNameException 
	 * 
	*/
	@Test
	public void baseGetFolderInsideFolderDAOTest() throws DuplicateNameException {
  		
		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
		
		GroupWorkspaceProjectPageFolder folder = groupWorkspaceProjectPage.createRootFolder("topFolder");
		groupWorkspaceProjectPageFolderDAO.makePersistent(folder);
		
		GroupWorkspaceProjectPageFolder folder1 = null;
		GroupWorkspaceProjectPageFolder folder2 = null;
		try
		{
		    folder1 = folder.createChild("subFolder1");
		    groupWorkspaceProjectPageFolderDAO.makePersistent(folder1);
		
		    folder2 = folder.createChild("subFolder2");
		    groupWorkspaceProjectPageFolderDAO.makePersistent(folder2);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert groupWorkspaceProjectPageFolderDAO.getSubFoldersForFolder(groupWorkspace.getId(), folder.getId()).size() == 2 : "Should be equal to 2";
		
		// Start the transaction 
        TransactionStatus ts = tm.getTransaction(td);
        
		groupWorkspaceProjectPageFolderDAO.makeTransient(groupWorkspaceProjectPageFolderDAO.getById(folder1.getId(), false));
		groupWorkspaceProjectPageFolderDAO.makeTransient(groupWorkspaceProjectPageFolderDAO.getById(folder2.getId(), false));
		groupWorkspaceProjectPageFolderDAO.makeTransient(groupWorkspaceProjectPageFolderDAO.getById(folder.getId(), false));
		
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert groupWorkspaceProjectPageFolderDAO.getById(folder.getId(), false) == null: 
			"Should not be able to find groupWorkspace folder";
		assert groupWorkspaceProjectPageFolderDAO.getById(folder1.getId(), false) == null: 
			"Should not be able to find groupWorkspace folder";
		
		groupWorkspaceDAO.makeTransient(groupWorkspaceDAO.getById(groupWorkspace.getId(), false));
		
		tm.commit(ts);
	}

}
