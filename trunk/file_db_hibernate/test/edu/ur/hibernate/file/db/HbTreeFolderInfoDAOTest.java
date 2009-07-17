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

package edu.ur.hibernate.file.db;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.DefaultFileInfo;
import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.file.db.DefaultFileServer;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.TreeFolderInfo;
import edu.ur.file.db.TreeFolderInfoDAO;
import edu.ur.file.db.FileServerDAO;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.hibernate.file.test.helper.ContextHolder;
import edu.ur.hibernate.file.test.helper.PropertiesLoader;
import edu.ur.util.FileUtil;

import java.util.List;

/**
 * Test the persistance methods for TreeFolderInfo Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class HbTreeFolderInfoDAOTest {

	/**  Properties file with testing specific information. */
	Properties properties;
	
	/** Get the spring context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** folder data access */
	TreeFolderInfoDAO treeFolderInfoDAO = (TreeFolderInfoDAO) ctx
			.getBean("treeFolderInfoDAO");
	
	/** file server data access  */
	FileServerDAO fileServerDAO = (FileServerDAO) ctx
	.getBean("fileServerDAO");

	/** Unique name generator  */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) ctx.getBean("uniqueNameGenerator");
	
    // Start the transaction this is for lazy loading
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");

    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
 
	/**
	 * Setup for testing the file System manager this loads the properties file
	 * for getting the correct path for different file systems
	 */
	@BeforeClass
	public void setUp() {
	    properties = new PropertiesLoader().getProperties();
	}
	
	/**
	 * Setup for testing
	 * 
	 * this deletes exiting test directories if they exist
	 */
	@BeforeMethod
	public void cleanDirectory() {
		try {
			File f = new File( properties.getProperty("HbFolderDAOTest.folder_path") );
			if(f.exists())
			{
			    FileUtils.forceDelete(f);
			}
			f = new File( properties.getProperty("HbFolderDAOTest.children_path") );
			if(f.exists())
			{
			    FileUtils.forceDelete(f);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}


	/**
	 * Test TreeFolderInfo persistance
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void baseTreeFolderInfoDAOTest() throws LocationAlreadyExistsException {
		
		TransactionStatus ts = tm.getTransaction(td);
		DefaultFileServer fileServer = new DefaultFileServer("fileServer");
		
		DefaultFileDatabase fd = fileServer.createFileDatabase("display_file_db_1", "file_db_1", properties
				.getProperty("HbFolderDAOTest.folder_path"), "Description");
		
		fileServerDAO.makePersistent(fileServer);

		// get a unique name for the folder
		assert uniqueNameGenerator.getNextName() != null : "Next unique should not be null";

		// path for creating a folder or folders
		TreeFolderInfo TreeFolderInfo1 = fd.createRootFolder("displayName1", uniqueNameGenerator
				.getNextName());
		
		TreeFolderInfo TreeFolderInfo2 = fd.createRootFolder("displayName2", uniqueNameGenerator
				.getNextName());

		// persist the items
		treeFolderInfoDAO.makePersistent(TreeFolderInfo1);
		
		treeFolderInfoDAO.makePersistent(TreeFolderInfo2);

		tm.commit(ts);
		
		// start new transaction
		ts = tm.getTransaction(td);
		assert treeFolderInfoDAO.getCount() >= 2 : "At least 2 TreeFolderInfos should exist";

		// make sure object has been persisted
		TreeFolderInfo other = treeFolderInfoDAO.getById(TreeFolderInfo1.getId(), false);
		assert other != null : "TreeFolderInfo should be found";
		assert other.equals(TreeFolderInfo1) : "TreeFolderInfo should be the same as is1 ";

		// make sure types are correct
		TreeFolderInfo other2 = treeFolderInfoDAO.getById(TreeFolderInfo2.getId(), false);
		assert !other.equals(other2) : "TreeFolderInfos should be different";
		
		//complete the transaction
		tm.commit(ts);

		// start new transaction
		ts = tm.getTransaction(td);
		// start new transaction
		// test deleting an object
		treeFolderInfoDAO.makeTransient(TreeFolderInfo1);

		tm.commit(ts);
		
		// make sure it is gone
		// new transaction
	    ts = tm.getTransaction(td);
		assert treeFolderInfoDAO.getById(TreeFolderInfo1.getId(), false) == null : "Should not find  TreeFolderInfo1";

		assert treeFolderInfoDAO.getById(TreeFolderInfo2.getId(), false).equals(
				TreeFolderInfo2) : "should find TreeFolderInfo2";
				
		tm.commit(ts);

		ts = tm.getTransaction(td);
		// clean up the rest
		treeFolderInfoDAO.makeTransient(TreeFolderInfo2);
		assert treeFolderInfoDAO.getById(TreeFolderInfo2.getId(), false) == null : "should not find TreeFolderInfo2";

		fileServerDAO.makeTransient(fileServerDAO.getById(fileServer.getId(), false));
		fileServer.deleteDatabase(fd.getName());
		tm.commit(ts);
	}

	/**
	 * Test TreeFolderInfo persistance with children
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void TreeFolderInfoChildrenDAOTest() throws LocationAlreadyExistsException {
		
		
		TransactionStatus ts = tm.getTransaction(td);
		
		DefaultFileServer fileServer = new DefaultFileServer("fileServer");
		
		String childrenPath = properties
		.getProperty("HbFolderDAOTest.children_path");
		
		DefaultFileDatabase fd = fileServer.createFileDatabase("display_file_db_1", "file_db_1", 
				childrenPath, "Description");
		
		fileServerDAO.makePersistent(fileServer);
		tm.commit(ts);
		
	
		ts = tm.getTransaction(td);
		TreeFolderInfo treeFolderInfo1 = fd.createRootFolder("rootFolder1",
				uniqueNameGenerator.getNextName());
		
		
		// make sure parent starts out with 1 and 2 left/right values.
		assert treeFolderInfo1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		treeFolderInfo1.getLeftValue();

		assert treeFolderInfo1.getRightValue() == 2L : "Right value should be 2 but is " + 
		treeFolderInfo1.getRightValue();


		TreeFolderInfo treeFolderInfo2 = fd.createRootFolder("rootFolder2",
				uniqueNameGenerator.getNextName());
		
		assert treeFolderInfo2.getLeftValue() == 1L : "Left value should be 1 but is " + 
		treeFolderInfo2.getLeftValue();

		assert treeFolderInfo2.getRightValue() == 2L : "Right value should be 2 but is " + 
		treeFolderInfo2.getRightValue();
		
		
		// add a child
		TreeFolderInfo childTreeFolderInfo1 = treeFolderInfo1.createChild("display3",
				uniqueNameGenerator.getNextName());
		
		assert treeFolderInfo1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		treeFolderInfo1.getLeftValue();

		assert treeFolderInfo1.getRightValue() == 4L : "Right value should be 2 but is " + 
		treeFolderInfo1.getRightValue();
		
		assert childTreeFolderInfo1.getLeftValue() == 2L : "Left value should be 1 but is " + 
		childTreeFolderInfo1.getLeftValue();

		assert childTreeFolderInfo1.getRightValue() == 3L : "Right value should be 2 but is " + 
		childTreeFolderInfo1.getRightValue();
		
		
		// add another child
		TreeFolderInfo childTreeFolderInfo2 = treeFolderInfo1.createChild("display4",
				uniqueNameGenerator.getNextName());
		
		assert treeFolderInfo1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		treeFolderInfo1.getLeftValue();

		assert treeFolderInfo1.getRightValue() == 6L : "Right value should be 2 but is " + 
		treeFolderInfo1.getRightValue();
		
		assert childTreeFolderInfo1.getLeftValue() == 2L : "Left value should be 1 but is " + 
		childTreeFolderInfo1.getLeftValue();

		assert childTreeFolderInfo1.getRightValue() == 3L : "Right value should be 2 but is " + 
		childTreeFolderInfo1.getRightValue();
		
		assert childTreeFolderInfo2.getLeftValue() == 4L : "Left value should be 1 but is " + 
		childTreeFolderInfo2.getLeftValue();

		assert childTreeFolderInfo2.getRightValue() == 5L : "Right value should be 2 but is " + 
		childTreeFolderInfo2.getRightValue();
		
		
		// add another child
		TreeFolderInfo subTreeFolderInfo1 = childTreeFolderInfo1.createChild("display5",
				uniqueNameGenerator.getNextName());
		
		
		assert treeFolderInfo1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		treeFolderInfo1.getLeftValue();

		assert treeFolderInfo1.getRightValue() == 8L : "Right value should be 8 but is " + 
		treeFolderInfo1.getRightValue();
		
		assert childTreeFolderInfo1.getLeftValue() == 2L : "Left value should be 2 but is " + 
		childTreeFolderInfo1.getLeftValue();

		assert childTreeFolderInfo1.getRightValue() == 5L : "Right value should be 5 but is " + 
		childTreeFolderInfo1.getRightValue();
		
		assert subTreeFolderInfo1.getLeftValue() == 3L : "Left value should be 3 but is " + 
		subTreeFolderInfo1.getLeftValue();

		assert subTreeFolderInfo1.getRightValue() == 4L : "Right value should be 4 but is " + 
		subTreeFolderInfo1.getRightValue();
		
		assert childTreeFolderInfo2.getLeftValue() == 6L : "Left value should be 6 but is " + 
		childTreeFolderInfo2.getLeftValue();

		assert childTreeFolderInfo2.getRightValue() == 7L : "Right value should be 7 but is " + 
		childTreeFolderInfo2.getRightValue();
		
		// add another child
		TreeFolderInfo subTreeFolderInfo2 = childTreeFolderInfo1.createChild("display6",
				uniqueNameGenerator.getNextName());
		
		
		assert treeFolderInfo1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		treeFolderInfo1.getLeftValue();

		assert treeFolderInfo1.getRightValue() == 10L : "Right value should be 10 but is " + 
		treeFolderInfo1.getRightValue();
		
		assert childTreeFolderInfo1.getLeftValue() == 2L : "Left value should be 2 but is " + 
		childTreeFolderInfo1.getLeftValue();

		assert childTreeFolderInfo1.getRightValue() == 7L : "Right value should be 7 but is " + 
		childTreeFolderInfo1.getRightValue();
		
		assert subTreeFolderInfo1.getLeftValue() == 3L : "Left value should be 3 but is " + 
		subTreeFolderInfo1.getLeftValue();

		assert subTreeFolderInfo1.getRightValue() == 4L : "Right value should be 4 but is " + 
		subTreeFolderInfo1.getRightValue();
		
		assert subTreeFolderInfo2.getLeftValue() == 5L : "Left value should be 5 but is " + 
		subTreeFolderInfo1.getLeftValue();

		assert subTreeFolderInfo2.getRightValue() == 6L : "Right value should be 6 but is " + 
		subTreeFolderInfo1.getRightValue();
		
		assert childTreeFolderInfo2.getLeftValue() == 8L : "Left value should be 8 but is " + 
		childTreeFolderInfo2.getLeftValue();

		assert childTreeFolderInfo2.getRightValue() == 9L : "Right value should be 9 but is " + 
		childTreeFolderInfo2.getRightValue();
		

		treeFolderInfo1.setDisplayName("display1");
		treeFolderInfo2.setDisplayName("display2");

		// persist the tree
		treeFolderInfoDAO.makePersistent(treeFolderInfo1);
		treeFolderInfoDAO.makePersistent(treeFolderInfo2);
		
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		/**********************************
		 * TEST TREE SELECTS
		 **********************************/
		// get all tree for the root folder.
		TreeFolderInfo tree = treeFolderInfoDAO.getTree(treeFolderInfo1);
		assert tree.getTreeSize() == 5 : "The tree should have 5 nodes but has " + tree.getTreeSize();
		
		// get tree for child 1.
		tree = treeFolderInfoDAO.getTree(childTreeFolderInfo1);
		assert tree.getTreeSize() == 3 : "The tree should have 3 nodes but has " + tree.getTreeSize();
		

		TreeFolderInfo myTree = treeFolderInfoDAO.findRootByDisplayName(treeFolderInfo1.getDisplayName(), 
             treeFolderInfo1.getFileDatabase().getId());
		assert myTree != null : "My Tree should not be null";
		

		assert myTree.equals(treeFolderInfo1) : "my tree should equal treeFolderInfo1 but does not";
		
		TreeFolderInfo myTree2 = treeFolderInfoDAO.findByDisplayName(childTreeFolderInfo1.getDisplayName(), 
				childTreeFolderInfo1.getParent().getId());
		assert myTree2 != null : "myTree2 should not be null";
		
		assert myTree2.equals(childTreeFolderInfo1) : "MyTree2 should equal childTreeFolderInfo1";

		tm.commit(ts);
		
		// start new transaction
		ts = tm.getTransaction(td);
		
		// make sure we can grab nodes that have a left and right value greater than
		// a specified value.
		List<TreeFolderInfo> nodes = treeFolderInfoDAO.getNodesLeftRightGreaterEqual(treeFolderInfo1.getRightValue(),
				childTreeFolderInfo1.getTreeRoot().getId());
		
		assert nodes.size() == 1 : "Expected root node but got "  + nodes.size();
		
		nodes = treeFolderInfoDAO.getNodesLeftRightGreaterEqual(treeFolderInfo1.getLeftValue(),
				childTreeFolderInfo1.getTreeRoot().getId());
		
		assert nodes.size() == 5 : "Expected all 5 nodes but got " + nodes.size();

		
		// make sure tree without specified nodes can be retrieved
		nodes = treeFolderInfoDAO.getAllNodesNotInChildTree(treeFolderInfo1);
        assert nodes.size() == 0 : "Nodes size should be zero but is " + nodes.size() + "using data "
            + " id = " + treeFolderInfo1.getId() + " and root id = " + treeFolderInfo1.getTreeRoot().getId();
		
        nodes = treeFolderInfoDAO.getAllNodesNotInChildTree(childTreeFolderInfo1);
        assert nodes.size() == 2 : "Nodes size should be 2 but is " + nodes.size() + " using data "
         + " id = " + childTreeFolderInfo1.getId() + " and root id = " + treeFolderInfo1.getTreeRoot().getId();
        
        
        nodes = treeFolderInfoDAO.getAllFoldersInDatabase(fd.getId());
        assert nodes.size() == 6 : "Tree should have 6 nodes but has : " + nodes.size(); 
        
		tm.commit(ts);

        /*****************************************
         * END TEST TREE SELECTS
         *****************************************/
        ts = tm.getTransaction(td);

		// make sure object has been persisted
		TreeFolderInfo other = treeFolderInfoDAO.getById(treeFolderInfo1.getId(), false);
		assert other != null : "TreeFolderInfo should be found";
		assert other.equals(treeFolderInfo1) : "TreeFolderInfo should be the same as is1 ";

		// make sure types are correct
		TreeFolderInfo other2 = treeFolderInfoDAO.getById(treeFolderInfo2.getId(), false);
		assert !other.equals(other2) : "TreeFolderInfo should be different";

		assert other.getChild(childTreeFolderInfo1.getName()).equals(
				childTreeFolderInfo1) : "TreeFolderInfo should have child";
		assert other.getChild(childTreeFolderInfo2.getName()).equals(
				childTreeFolderInfo2) : "TreeFolderInfo should have child";

		String name = childTreeFolderInfo1.getName();
		assert other.getChild(name).getChild(subTreeFolderInfo1.getName())
				.equals(subTreeFolderInfo1) : "TreeFolderInfo should have child";
		assert other.getChild(name).getChild(subTreeFolderInfo2.getName())
				.equals(subTreeFolderInfo2) : "TreeFolderInfo should have child";

		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);

		
		
		//*************************************
		// Make sure we can add new nodes after
		// data has been committed.
        //*************************************
		ts = tm.getTransaction(td);

		//TreeFolderInfo myRoot = treeFolderInfoDAO.getById(treeFolderInfo1.getId(), false);
		// make sure object has been persisted
		TreeFolderInfo akaSubTreeFolderInfo1 = treeFolderInfoDAO.getById(subTreeFolderInfo1.getId(), false);
		
		assert akaSubTreeFolderInfo1.getLeftValue() == 3L : "Left value should be 5 but is " + 
		akaSubTreeFolderInfo1.getLeftValue();

		assert akaSubTreeFolderInfo1.getRightValue() == 4L : "Right value should be 6 but is " + 
		akaSubTreeFolderInfo1.getRightValue();
		
		assert !akaSubTreeFolderInfo1.isRoot(): "Should not be root folder";
		
		
		TreeFolderInfo akaSubTreeParent = akaSubTreeFolderInfo1.getParent();
		
		assert akaSubTreeParent.getLeftValue() == 2L : "Left value should be 2 but is " + 
		childTreeFolderInfo1.getLeftValue();

		assert akaSubTreeParent.getRightValue() == 7L : "Right value should be 7 but is " + 
		childTreeFolderInfo1.getRightValue();
		
		TreeFolderInfo akaRoot =  akaSubTreeParent.getParent();
		
		assert akaRoot.getLeftValue() == 1L : "Left value should be 1 but is " + 
		treeFolderInfo1.getLeftValue();

		assert akaRoot.getRightValue() == 10L : "Right value should be 10 but is " + 
		treeFolderInfo1.getRightValue();
		
		TreeFolderInfo subSubTreeFolderInfo1 = 
			akaSubTreeFolderInfo1.createChild("display7", "subSubTreeFolderInfo1");
		
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
		
		treeFolderInfoDAO.makePersistent(akaSubTreeFolderInfo1.getTreeRoot());
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		
		TreeFolderInfo topFolder = treeFolderInfoDAO.getById(treeFolderInfo1.getId(), false);
		
		assert topFolder.getLeftValue() == 1L : "Left value should be 1 but is " + 
		treeFolderInfo1.getLeftValue();

		assert topFolder.getRightValue() == 12L : "Right value should be 12 but is " + 
		treeFolderInfo1.getRightValue();
		

		// test deleting an object
		treeFolderInfoDAO.makeTransient(topFolder);

		tm.commit(ts);
		
	
        ts = tm.getTransaction(td);
        
		assert treeFolderInfoDAO.getById(treeFolderInfo1.getId(), false) == null : "Should not find  TreeFolderInfo1";
		assert treeFolderInfoDAO.getById(treeFolderInfo2.getId(), false).equals(
				treeFolderInfo2) : "should find TreeFolderInfo2";
				
		tm.commit(ts);

		
		ts = tm.getTransaction(td);
		// clean up the rest
		treeFolderInfoDAO.makeTransient(treeFolderInfo2);
		
		assert treeFolderInfoDAO.getById(treeFolderInfo2.getId(), false) == null : "should not find TreeFolderInfo2";

		
		fileServerDAO.makeTransient(fileServerDAO.getById(fileServer.getId(), false));
		
		assert fileServer.deleteDatabase(fd.getName()): "File Database should be deleted";
		tm.commit(ts);
	}

	/**
	 * Test adding files to a folder
	 * @throws LocationAlreadyExistsException 
	 * @throws IllegalFileSystemNameException 
	 * 
     */
	@Test
	public void TreeFolderInfoFileDAOTest() throws LocationAlreadyExistsException, IllegalFileSystemNameException {
		TransactionStatus ts = tm.getTransaction(td);
		// get a unique name for the folder
		assert uniqueNameGenerator.getNextName() != null : "Next unique should not be null";
		

		DefaultFileServer fileServer = new DefaultFileServer("fileServerFileDAOTest");
		
		DefaultFileDatabase fd = fileServer.createFileDatabase("display_file_db_1", "file_db_1", properties
				.getProperty("HbFolderDAOTest.folder_path"), "Description");
		
		fileServerDAO.makePersistent(fileServer);
		
		// path for creating a folder or folders
		TreeFolderInfo treeFolderInfo1 = fd.createRootFolder("displayName1", uniqueNameGenerator
				.getNextName());
		
		TreeFolderInfo treeFolderInfo2 = fd.createRootFolder("displayName2", uniqueNameGenerator
				.getNextName());

		
		File directory = new File(treeFolderInfo2.getFullPath());
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		// create the first file to store in the temporary folder
		File f = testUtil.creatFile(directory, "testFile", "Hello  -  This is text in a file");

		DefaultFileInfo fileInfo = treeFolderInfo1.createFileInfo(f, "testName");
		fileInfo.setDisplayName("hbTestFile1");
		
		assert treeFolderInfo1.getFiles().contains(fileInfo) : "tree folder info 1 should contain " + fileInfo;
		
		treeFolderInfoDAO.makePersistent(treeFolderInfo1);
		
		assert treeFolderInfo1.getId() != null : "tree folder info 1 id should not be null";
		
		tm.commit(ts);
		
		// start new transaction
	    ts = tm.getTransaction(td);
		
        TreeFolderInfo other = treeFolderInfoDAO.getById(treeFolderInfo1.getId(), false);
        
        assert other != null : "Folder info should be found";
        assert other.getFiles().contains(fileInfo) : "tree folder info 1 should contain " + fileInfo;
        assert other.getFileInfo(fileInfo.getId()).equals(fileInfo) : "File information should be the same"; 
		
        //make sure the count returns 1
        Long count = treeFolderInfoDAO.getFileCount(other.getId());
        assert count.equals(1l) : 
        	"Should find one file but found " + count;
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		// delete the database information
		treeFolderInfoDAO.makeTransient(other);
		fileServerDAO.makeTransient(fileServerDAO.getById(fileServer.getId(), false));
		assert fileServer.deleteDatabase(fd.getName());
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert fileServerDAO.getById(fileServer.getId(), false) == null : "Should not be able to find " + fileServer; 
		tm.commit(ts);
	}
	
	
	
	
	
	
	/**
	 * Test TreeFolderInfo persistance with children
	 * @throws LocationAlreadyExistsException 
	 * 
	 */
	@Test
	public void TreeFolderChildrenPathDAOTest() throws LocationAlreadyExistsException {
		
		TransactionStatus ts = tm.getTransaction(td);
		
		DefaultFileServer fileServer = new DefaultFileServer("fileServer");
		
		String childrenPath = properties
		.getProperty("HbFolderDAOTest.children_path");
		
		DefaultFileDatabase fd = fileServer.createFileDatabase("display_file_db_1", "file_db_1", 
				childrenPath, "Description");
		
		fileServerDAO.makePersistent(fileServer);
		

		TreeFolderInfo treeFolderInfo1 = fd.createRootFolder("rootFolder1",
				"root");
		
		assert treeFolderInfo1.getFullPath().equals(fd.getFullPath() + "root" + IOUtils.DIR_SEPARATOR) : 
			"Path should equal " + fd.getFullPath() + "root" + IOUtils.DIR_SEPARATOR +
		" but equals " + treeFolderInfo1.getFullPath();
		
		TreeFolderInfo treeFolderInfo2 = fd.createRootFolder("rootFolder2",
				"root2");
		
		assert treeFolderInfo2.getFullPath().equals(fd.getFullPath() + "root2" + IOUtils.DIR_SEPARATOR) : 
			"Path should equal " + fd.getFullPath() + "root2" + IOUtils.DIR_SEPARATOR +
		" but equals " + treeFolderInfo2.getFullPath();

		
		// add a child
		TreeFolderInfo childTreeFolderInfo1 = treeFolderInfo1.createChild("display3",
				"childTreeFolderInfo1");
		
		
		assert childTreeFolderInfo1.getFullPath().equals(treeFolderInfo1.getFullPath() + "childTreeFolderInfo1" + IOUtils.DIR_SEPARATOR) : 
			"Path should equal " 
			+ treeFolderInfo1.getFullPath() + "childTreeFolderInfo1" + IOUtils.DIR_SEPARATOR + 
		" but equals " + childTreeFolderInfo1.getFullPath();

		
		
		// add another child
		TreeFolderInfo childTreeFolderInfo2 = treeFolderInfo1.createChild("display4",
				"childTreeFolderInfo2");
		
	
		assert childTreeFolderInfo2.getFullPath().equals(treeFolderInfo1.getFullPath() + "childTreeFolderInfo2" + IOUtils.DIR_SEPARATOR) : 
			"Path should equal " 
			+ treeFolderInfo1.getFullPath() + "childTreeFolderInfo2" + IOUtils.DIR_SEPARATOR + 
		" but equals " + childTreeFolderInfo2.getFullPath();
		
		
		// add another child
		TreeFolderInfo subTreeFolderInfo1 = childTreeFolderInfo1.createChild("display5",
				"subTreeFolderInfo1");
		
		assert subTreeFolderInfo1.getFullPath().equals(childTreeFolderInfo1.getFullPath() + "subTreeFolderInfo1" + IOUtils.DIR_SEPARATOR) : 
			"Path should equal " 
			+ childTreeFolderInfo1.getFullPath() + "subTreeFolderInfo1" + IOUtils.DIR_SEPARATOR + 
		" but equals " +  subTreeFolderInfo1.getFullPath();
		
		// add another child
		TreeFolderInfo subTreeFolderInfo2 = childTreeFolderInfo1.createChild("display6",
				"subTreeFolderInfo2");
		
		
		assert subTreeFolderInfo2.getFullPath().equals(childTreeFolderInfo1.getFullPath() + "subTreeFolderInfo2" + IOUtils.DIR_SEPARATOR) : 
			"Path should equal " 
			+ childTreeFolderInfo1.getFullPath() + "subTreeFolderInfo2" + IOUtils.DIR_SEPARATOR + 
		" but equals " +  subTreeFolderInfo1.getFullPath();
		
		
		treeFolderInfo1.setDisplayName("display1");
		treeFolderInfo2.setDisplayName("display2");

		// persist the tree
		treeFolderInfoDAO.makePersistent(treeFolderInfo1);
		treeFolderInfoDAO.makePersistent(treeFolderInfo2);
		
		tm.commit(ts);

	    // start new transaction
		ts = tm.getTransaction(td);
		

		// reload the data from the database and re-check
		
		treeFolderInfo1 = treeFolderInfoDAO.getById(treeFolderInfo1.getId(), false);
		fd = treeFolderInfo1.getFileDatabase();
		
		assert treeFolderInfo1.getFullPath().equals(fd.getFullPath() + "root" + IOUtils.DIR_SEPARATOR) : 
			"Path should equal " + fd.getFullPath() + "root" + IOUtils.DIR_SEPARATOR +
		" but equals " + treeFolderInfo1.getFullPath();
			
		

		// clean up the rest
		fileServerDAO.makeTransient(fileServerDAO.getById(fileServer.getId(), false));
		
		assert fileServer.deleteDatabase(fd.getName()): "File Database should be deleted";
		
		tm.commit(ts);
	}
	
	
	
	
}
