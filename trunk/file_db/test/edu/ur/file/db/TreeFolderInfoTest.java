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
package edu.ur.file.db;

import java.io.File;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.ur.file.db.DefaultFileInfo;
import edu.ur.file.db.FileSystemManager;
import edu.ur.file.db.TreeFolderInfo;
import edu.ur.test.helper.PropertiesLoader;
import edu.ur.util.FileUtil;

/**
 * Test the TreeFolderInfo class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class TreeFolderInfoTest {
	
	/**
	 * Properties file with testing specific information.
	 */
	Properties properties;
	
	/**
	 * Setup for testing the file System manager
	 * this loads the properties file for getting
	 * the correct path for different file systems
	 */
	@BeforeTest
	public void setUp() {
	    properties = new PropertiesLoader().getProperties();
	}

 	/**
	 * Setters and Getters test 
 	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void basicTreeFolderInfoTest() throws LocationAlreadyExistsException {
		
		// directory to move the file to
		String folderPath = properties.getProperty("FolderInfoTest.folder_path");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName_a", "fileDB_a", folderPath, "description");
		
		TreeFolderInfo folder = fd.createRootFolder("displayName", "name");
		folder.setDescription("description");
		folder.setId(1l);
		
		assert folder.getDescription() == "description" : "Descriptions should be equal";
		assert folder.getId() == 1l : "Id's should be equal";
		assert folder.getName().equals("name") : "Names should be the same";
		assert folder.getDisplayName().equals("displayName") : "Display names should be the same"; 
		
		server.deleteDatabase(fd.getName());

	}
	
	/**
	 * Test adding a file to the folder.
	 * @throws LocationAlreadyExistsException 
	 */
	public void testAddFileInfo() throws LocationAlreadyExistsException
	{
		// this will create folders so we need to place them in directories
		String filePath = properties.getProperty("FolderInfoTest.file.test");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName_19", "fileDB_19", filePath, "description");
		
		TreeFolderInfo folder1 = FileSystemManager.createFolder("fileFolder", fd);
		folder1.setDisplayName("displayName1");
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", "Hello  -  This file to add to a file info object");		
		
		DefaultFileInfo fileInfo = folder1.createFileInfo(f, "uniqueName");
		fileInfo.setVersion(22);
		fileInfo.setDisplayName("displayName");
		fileInfo.setId(9l);
		
	    assert folder1.getFileInfo(9l).equals(fileInfo) : "File info should be found";
	    assert folder1.getFileInfo(fileInfo.getName()).equals(fileInfo) 
	    : "File info should be found";
	    
	    assert folder1.getFiles().contains(fileInfo) : "Should contain file info";
	    assert folder1.removeFileInfo(fileInfo) : "File info should be removed";
	    assert fileInfo.getFolderInfo() == null : "Parent should be set to null";
	    assert !folder1.removeFileInfo(fileInfo) : "Should not be able to remove it again";
	    
	    assert folder1.getFileInfo(22l) == null : "Info should not be found";
	    assert folder1.getFileInfo(fileInfo.getName()) == null : "Info should not be found";
	    
	    FileSystemManager.deleteDirectory(fd.getFullPath());
	}
	
	/**
	 * Test adding children functionality
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void testAddChildren() throws LocationAlreadyExistsException
	{
		// this will create folders so we need to place them in directories
		String childrenPath = properties.getProperty("FolderInfoTest.children.path");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName_20", "fileDB_20", childrenPath, "description");
		
		TreeFolderInfo folder1 = FileSystemManager.createFolder("TreeFolderInfo1", fd);
		folder1.setDisplayName("displayName1");
		
		String folderPath = fd.getFullPath();

		folderPath += "TreeFolderInfo1" + IOUtils.DIR_SEPARATOR; 
		assert folder1.getFullPath().equals(folderPath) : " Folder path should = " + folderPath 
		+ " but is " + folder1.getFullPath();
		
	    TreeFolderInfo child1 = folder1.createChild( "displayName2", "child1");
	    
	    folderPath += "child1" + IOUtils.DIR_SEPARATOR;
	    assert child1.getFullPath().equals(folderPath): "path should equal " + folderPath +
	    "but equals " + child1.getFullPath();
	    
		TreeFolderInfo child2 = folder1.createChild("displayName3", "child2");
		TreeFolderInfo subChild1 = child1.createChild("displayName4", "subChild1");
		
		assert subChild1.getParent() == child1 : "Child 1 should be sub child's parent";
		assert child1.getChildren().contains(subChild1) : "Child1 should contain sub child1";
		
		TreeFolderInfo subChild2 = child1.createChild("displayName5", "subChild2" );
		
		assert child1.getChildren().contains(subChild2) : "Child1 should contain child 2";
		
		assert folder1.getExists() : "Folder in file system";
		
		assert folder1.getChildren().size() == 2 : "The number of folders should be 2 but is " 
			+ folder1.getChildren().size();
		assert child1.getChildren().size() == 2 : "The number of folders should be 2 but is " + 
		    child1.getChildren().size();
		assert child2.getChildren().size() == 0 : "The number of children should be 0 but is " +
		    child2.getChildren().size();
		
		assert folder1.getChild("child1").equals(child1) : "Child one should be found";
		assert folder1.getChild("child2").equals(child2) : "Child two should be found";
		
		assert folder1.getChild("child1").getChild("subChild1").equals(subChild1): "sub child one should be found";
		assert folder1.getChild("child1").getChild("subChild2").equals(subChild2): "sub child two should be found";

	    assert folder1.isRoot() : "folder 1 should be root";
	    
	    assert child1.getParent().equals(folder1) : "Child one parent should be folder 1";
	    assert subChild1.getParent().equals(child1): "Child one should be parent of sub child1";
	    
	    assert child1.getPath().equals(folder1.getFullPath()) : "Paths should be the same child 1 = " +
	    	child1.getPath() + " folder 1 = " + folder1.getFullPath();
	    
	    assert child1.getChildren().contains(subChild2) : "Child1 should contain child 2";
	    assert child1.removeChild(subChild2) : "Sub child 2 should be removed";
	    
	    TreeFolderInfo other2 = child1.getChild("subChild2");
	    assert other2 == null : "Sub child information should not exist";
	    
	    assert child1.removeChild(subChild1) : "SubChild one should be deleted";
	    assert child1.getChild(subChild1.getName()) == null : "Sub Child 1 should not be in object";
	    assert subChild1.getParent() == null : "The child 1 should no longer have a parent";
	    assert subChild1.getFullPath() == null: "Subchild no longer has a path but path is " + subChild1.getFullPath();
	    
	    folder1.removeChild(child1);
	    assert folder1.getChild("child1") == null : "Should not find child 1";
	    
	    FileSystemManager.deleteDirectory(fd.getFullPath());
	}
	
    /**
	 * Test adding and creating files for a given folder.
     * @throws LocationAlreadyExistsException 
	 */
	public void testAddFiles() throws LocationAlreadyExistsException
	{
		// directory to move the file to
		String folderPath = properties.getProperty("FolderInfoTest.folder_path");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("display_name_21", "fileDB_21", folderPath, "description");
		
		TreeFolderInfo folderInfo = FileSystemManager.createFolder("addFileTest", fd);
		
		// directory to store the file first
		String tempPath = properties.getProperty("FolderInfoTest.move.initial");
		
		DefaultFileDatabase fd2 = server.createFileDatabase("displayName_21", "fileDB_22", tempPath, "description");
		
		FileSystemManager.createFolder("start1", fd2);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "testFile", "Hello  -  This file to add to a file info object");
		
		DefaultFileInfo fileInfo = folderInfo.createFileInfo(f, "fileInfoTest");
		
		assert folderInfo.getFileInfo("fileInfoTest")!= null : "The fileInfo should not be null";
		
		assert fileInfo.getName().equals("fileInfoTest") : "File name should be fileInfoTest";
		assert fileInfo.exists() : "File should be set as in the file system";
		
		File newFile = new File(fileInfo.getPath());
		assert newFile.exists() : "New File should exist";
		
		assert fileInfo.getFullPath().equals(folderInfo.getFullPath() + "fileInfoTest") : 
			"paths should be the same file path = " + fileInfo.getPath() + 
			" folder info path = " + folderInfo.getFullPath();
		
		assert folderInfo.removeFileInfo(fileInfo) : "Should be able to delete the file " +
		    fileInfo.getFullPath();
		
		assert newFile.exists() : "The file should no longer exist in the file system";
		
		DefaultFileInfo deletedFileInfo = folderInfo.getFileInfo("fileInfoTest");
		assert deletedFileInfo == null : "Child should no longer be found";
		assert fileInfo.getFolderInfo() == null : "File should no longer have parent";
		
		server.deleteDatabase(fd.getName());
		server.deleteDatabase(fd2.getName());
	}
	
    /**
	 * Test finding files recursively
     * @throws LocationAlreadyExistsException 
	 */
	public void testRecursiveFindFiles() throws LocationAlreadyExistsException
	{
		// directory to move the file to
		String folderPath = properties.getProperty("FolderInfoTest.folder_path");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("display_name_21", "fileDB_21", folderPath, "description");
		
		TreeFolderInfo folderInfo = FileSystemManager.createFolder("addFileTest", fd);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "testFile", "Hello  -  This file to add to a file info object");
		TreeFolderInfo childFolder = folderInfo.createChild("child1", "child1");
		DefaultFileInfo fileInfo = childFolder.createFileInfo(f, "fileInfoTest");
		fileInfo.setId(22L);
		
		assert folderInfo.getFileInfoInTree(22L) != null : "the folder info should be found by id";
		assert folderInfo.getFileInfoInTree("fileInfoTest") != null: "The folder info should be found by name";
		
		server.deleteDatabase(fd.getName());
	}
	
    /**
	 * Test removing files where it must be done recursively using unique id.
     * @throws LocationAlreadyExistsException 
	 */
	public void testRecursiveRemoveFilesById() throws LocationAlreadyExistsException
	{
		// directory to move the file to
		String folderPath = properties.getProperty("FolderInfoTest.folder_path");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("display_name_21", "fileDB_21", folderPath, "description");
		
		TreeFolderInfo folderInfo = FileSystemManager.createFolder("addFileTest", fd);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "testFile", "Hello  -  This file to add to a file info object");
		TreeFolderInfo childFolder = folderInfo.createChild("child1", "child1");
		DefaultFileInfo fileInfo = childFolder.createFileInfo(f, "fileInfoTest");
		fileInfo.setId(22L);
		
		assert folderInfo.removeFileInfoFromTree(22L) : "File should be removed";
		assert folderInfo.getFileInfoInTree(22L) == null : "the folder info should NOT be found by id";
		server.deleteDatabase(fd.getName());
	}

    /**
	 * Test removing files where it must be done recursively using unique name.
     * @throws LocationAlreadyExistsException 
	 */
	public void testRecursiveRemoveFilesByUniqueName() throws LocationAlreadyExistsException
	{
		// directory to move the file to
		String folderPath = properties.getProperty("FolderInfoTest.folder_path");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("display_name_21", "fileDB_21", folderPath, "description");
		
		TreeFolderInfo folderInfo = FileSystemManager.createFolder("addFileTest", fd);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "testFile", "Hello  -  This file to add to a file info object");
		TreeFolderInfo childFolder = folderInfo.createChild("child1", "child1");
		DefaultFileInfo fileInfo = childFolder.createFileInfo(f, "fileInfoTest");
		fileInfo.setId(22L);
		
		assert folderInfo.removeFileInfoFromTree("fileInfoTest") : "File should be removed";
		assert folderInfo.getFileInfoInTree("fileInfoTest") == null : "the folder info should NOT be found by id";
		server.deleteDatabase(fd.getName());
	}
	
	


    /**
	 * Test equals method 
     * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void testEquals() throws LocationAlreadyExistsException {
		
		// directory to move the file to
		String folderPath = properties.getProperty("FolderInfoTest.folder_path");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName_a", "fileDB_a", folderPath, "description");
		
		TreeFolderInfo folder1 = fd.createRootFolder("name", "name1");

		// this tricks the code.  In reality we should never have
		// two folders with the same name and path.
		TreeFolderInfo folder2 = fd.createRootFolder("name", "name2");
		folder2.setName("name1");

		TreeFolderInfo folder3 = fd.createRootFolder("name", "name3");
		
		assert folder1.equals(folder2) : "TreeFolderInfos are equal folder1 path = " + folder1.getPath()
		+ " folder 2 path =  " + folder2.getPath() ;
		assert !folder1.equals(folder3) : "TreeFolderInfos are not equals";
		
		server.deleteDatabase(fd.getName());
	}

	/**
	 * Test the hash code
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void testHashCode() throws LocationAlreadyExistsException {
		// directory to move the file to
		String folderPath = properties.getProperty("FolderInfoTest.folder_path");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("dispalName_a", "fileDB_a", folderPath, "description");
		
		TreeFolderInfo folder1 = fd.createRootFolder("name", "name1");

		// this tricks the code.  In reality we should never have
		// two folders with the same name and path.
		TreeFolderInfo folder2 = fd.createRootFolder("name", "name2");
		folder2.setName("name1");

		TreeFolderInfo folder3 = fd.createRootFolder("name", "name3");
		
		assert folder1.hashCode() == folder2.hashCode() : "hash codes are equal";
		assert folder1.hashCode() != folder3.hashCode() : "hash codes are not equals";
		
		server.deleteDatabase(fd.getName());
	}
	
	/**
	 *  Make sure the root folder is always returned correctly. 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testRoot() throws LocationAlreadyExistsException
	{
		// this will create folders so we need to place them in directories
		String childrenPath = properties.getProperty("FolderInfoTest.children.path");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName_23", "fileDB_23", childrenPath, "description");
		
		TreeFolderInfo folder1 = FileSystemManager.createFolder("TreeFolderInfo1", fd);
		folder1.setDisplayName("displayName1");
		
		assert folder1 == folder1.getTreeRoot() : "References should be the same";

		TreeFolderInfo folder2 = FileSystemManager.createFolder("TreeFolderInfo2", fd);
		assert folder2 == folder2.getTreeRoot() : "References should be the same";
		folder1.setDisplayName("displayName2");

	    TreeFolderInfo child1 = folder1.createChild( "displayName2", "child1");
        assert child1.getTreeRoot().equals(folder1) : "Child 1 root should equal folder 1";
	    
	    TreeFolderInfo child2 = folder1.createChild("displayName3", "child2");
	    assert child2.getTreeRoot().equals(folder1) : "Child 2 root should equal folder 1";
	    
		TreeFolderInfo subChild1 = child1.createChild("displayName4", "subChild1");
		assert subChild1.getTreeRoot().equals(folder1) : "Subchild 1 root folder should be folder1";
		
		TreeFolderInfo subChild2 = child1.createChild("displayName5", "subChild2" );
		assert subChild2.getTreeRoot().equals(folder1) : "Subchild 2 root folder should be folder1";
		
	    assert child1.getParent().equals(folder1) : "Child one parent should be folder 1";
	    assert subChild1.getParent().equals(child1): "Child one should be parent of sub child1";
	    
	    String subChild1Path = subChild1.getFullPath();
	    
	    assert child1.removeChild(subChild1) : "SubChild one should be deleted";
	    assert child1.getChild(subChild1.getName()) == null : "Sub Child 1 should not be in object";
	    assert subChild1.getParent() == null : "The child 1 should no longer have a parent";
	    assert subChild1.getTreeRoot() == subChild1 : "The child should be it's own root";
	    
	    File subChild1File = new File(subChild1Path);
	    assert !subChild1File.exists() : "File should not exist";
	    
	    folder1.removeChild(child1);
	    assert folder1.getChild("child1") == null : "Should not find child 1";
	  
	    FileSystemManager.deleteDirectory(fd.getFullPath());
	}
	
	/**
	 * Test removing children functionality
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void testRemoveChildren() throws LocationAlreadyExistsException
	{
		// this will create folders so we need to place them in directories
		String childrenPath = properties.getProperty("FolderInfoTest.children.path");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName_24", "fileDB_24", childrenPath, "description");
		
		// create the root folder
		TreeFolderInfo folder1 = FileSystemManager.createFolder("TreeFolderInfo1", fd);
		folder1.setDisplayName("displayName1");
		
		// create the first child
	    TreeFolderInfo child1 = folder1.createChild( "displayName2", "child1");
	    
	    // add child 2
		TreeFolderInfo child2 = folder1.createChild("displayName3", "child2");
		
		TreeFolderInfo subChild1 = child1.createChild("displayName4", "subChild1");
		TreeFolderInfo subChild2 = child1.createChild("displayName5", "subChild2" );

		assert folder1.getLeftValue() == 1 : "Folder 1 left value should equal 1 but equals "
			+ folder1.getLeftValue();
		
		assert folder1.getRightValue() == 10 : "Folder 1 right value should equal 10 but equals "
			+ folder1.getRightValue();
		
		assert child1.getLeftValue() == 2 : "Child 1 left value should equal 2 but equals "
			+ child1.getLeftValue();
		
		assert child1.getRightValue() == 7 : "Child 1 right value should equal 7 but equals "
			+ child1.getRightValue();
		
		assert subChild1.getLeftValue() == 3 : "subChild 1 left value should equal 3 but equals "
			+ subChild1.getLeftValue();
		
		assert subChild1.getRightValue() == 4 : "subChild 1 right value should equal 4 but equals "
			+ subChild1.getRightValue();

		assert subChild2.getLeftValue() == 5 : "subChild 2 left value should equal 5 but equals "
			+ subChild1.getLeftValue();
		
		assert subChild2.getRightValue() == 6 : "subChild 2 right value should equal 6 but equals "
			+ subChild1.getRightValue();
		
		assert child2.getLeftValue() == 8 : "Child 1 left value should equal 8 but equals "
			+ child2.getLeftValue();
		
		assert child2.getRightValue() == 9 : "Child 1 right value should equal 9 but equals "
			+ child2.getRightValue();
		
		
		// remove sub child 2
		assert child1.removeChild(subChild2) : "Sub child 2 should be removed";
	    
		
		// check tree numbers
		assert folder1.getLeftValue() == 1 : "Folder 1 left value should equal 1 but equals "
			+ folder1.getLeftValue();
		
		assert folder1.getRightValue() == 8 : "Folder 1 right value should equal 8 but equals "
			+ folder1.getRightValue();
		
		assert child1.getLeftValue() == 2 : "Child 1 left value should equal 2 but equals "
			+ child1.getLeftValue();
		
		assert child1.getRightValue() == 5 : "Child 1 right value should equal 7 but equals "
			+ child1.getRightValue();
		
		assert subChild1.getLeftValue() == 3 : "subChild 1 left value should equal 3 but equals "
			+ subChild1.getLeftValue();
		
		assert subChild1.getRightValue() == 4 : "subChild 1 right value should equal 4 but equals "
			+ subChild1.getRightValue();
		
		assert child2.getLeftValue() == 6 : "Child 1 left value should equal 8 but equals "
			+ child2.getLeftValue();
		
		assert child2.getRightValue() == 7 : "Child 1 right value should equal 9 but equals "
			+ child2.getRightValue();
		
		
		
	    TreeFolderInfo other2 = child1.getChild("subChild2");
	    assert other2 == null : "Sub child information should not exist";
	    
	    // remove anohter child
	    String subChild1Path = child1.getChild("subChild1").getFullPath();
	    
	    assert child1.removeChild(subChild1) : "SubChild one should be deleted";
	    
	    
		assert folder1.getLeftValue() == 1 : "Folder 1 left value should equal 1 but equals "
			+ folder1.getLeftValue();
		
		assert folder1.getRightValue() == 6 : "Folder 1 right value should equal 6 but equals "
			+ folder1.getRightValue();
		
		assert child1.getLeftValue() == 2 : "Child 1 left value should equal 2 but equals "
			+ child1.getLeftValue();
		
		assert child1.getRightValue() == 3 : "Child 1 right value should equal 3 but equals "
			+ child1.getRightValue();
		
		
		assert child2.getLeftValue() == 4 : "Child 1 left value should equal 4 but equals "
			+ child2.getLeftValue();
		
		assert child2.getRightValue() == 5 : "Child 1 right value should equal 5 but equals "
			+ child2.getRightValue();
	    
	    
	    
	    assert child1.getChild(subChild1.getName()) == null : "Sub Child 1 should not be in object";
	    assert subChild1.getParent() == null : "The child 1 should no longer have a parent";
	    
	    File subChild1File = new File(subChild1Path);
	    assert !subChild1File.exists() : "File should not exist";
	    
	    folder1.removeChild(child1);
	    assert folder1.getChild("child1") == null : "Should not find child 1";
	    
	    assert folder1.removeChild(child2) : "Child 2 should be removed";
	    
		assert folder1.getLeftValue() == 1 : "Folder 1 left value should equal 1 but equals "
			+ folder1.getLeftValue();
		
		assert folder1.getRightValue() == 2 : "Folder 1 right value should equal 6 but equals "
			+ folder1.getRightValue();
	    
	    FileSystemManager.deleteDirectory(fd.getFullPath());
	}
	
	/**
	 * Make sure when the root folder changes - it is changed
	 * on all children.
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void testChangeRootFolder() throws LocationAlreadyExistsException
	{
		// this will create folders so we need to place them in directories
		String childrenPath = properties.getProperty("FolderInfoTest.children.path");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName_25", "fileDB_25", childrenPath, "description");
		
		// create 2 root folders
		TreeFolderInfo folder1 = FileSystemManager.createFolder("RootFolderInfo1", fd);
		folder1.setDisplayName("displayName1");
		
		TreeFolderInfo folder2 = FileSystemManager.createFolder("RootFolderInfo2", fd);
		folder1.setDisplayName("displayName2");
		
		// create the first child
	    TreeFolderInfo child1 = folder1.createChild( "displayName2", "child1");
	    
	    // add child 2
		TreeFolderInfo child2 = folder1.createChild("displayName3", "child2");
		TreeFolderInfo subChild1 = child1.createChild("displayName4", "subChild1");
		TreeFolderInfo subChild2 = child1.createChild("displayName5", "subChild2" );

		
		assert child1.getTreeRoot().equals(folder1) : "Before move root should be folder1 but is" +
		   child1.getTreeRoot().toString();
		
		assert subChild1.getTreeRoot().equals(folder1) : "Before move root should be folder1 but is" +
		subChild1.getTreeRoot().toString();
		
		assert subChild2.getTreeRoot().equals(folder1) : "Before move root should be folder1 but is" +
		subChild2.getTreeRoot().toString();
		
		
		assert child2.getTreeRoot().equals(folder1) : "Before move root should be folder1 but is"
		 + child2.getTreeRoot().toString();

		
		File f = new File(child1.getFullPath());
		
		assert f.exists() : "Folder " + f.getAbsolutePath() + " should exist";
		
		folder2.addChild(child1);
		
		assert !f.exists() : "Folder " + f.getAbsolutePath() + " should not exist after the move";
		
		assert child1.getTreeRoot().equals(folder2) : "Before move root should be folder2 but is" +
		   child1.getTreeRoot().toString();
		
		assert subChild1.getTreeRoot().equals(folder2) : "Before move root should be folder2 but is" +
		subChild1.getTreeRoot().toString();
		
		assert subChild2.getTreeRoot().equals(folder2) : "Before move root should be folder2 but is" +
		subChild2.getTreeRoot().toString();
		
		
		assert child2.getTreeRoot().equals(folder1) : "Before move root should be folder2 but is"
		 + child2.getTreeRoot().toString();
	    
	    FileSystemManager.deleteDirectory(fd.getFullPath());
	}

}
