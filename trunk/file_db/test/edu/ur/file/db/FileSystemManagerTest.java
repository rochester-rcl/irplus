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

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.ur.file.db.DefaultFileInfo;
import edu.ur.file.db.FileSystemManager;
import edu.ur.file.db.TreeFolderInfo;
import edu.ur.test.helper.PropertiesLoader;
import edu.ur.util.FileUtil;

import java.util.Properties;
import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

/**
 * Test the FileSystemManager
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class FileSystemManagerTest {
	
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
	 * Creating and deleting a folder Info 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void createFolderPathTest() throws LocationAlreadyExistsException
	{
		String path = properties.getProperty("FilsSystemManagerTest.folder_path");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName6", "fileDB_6", path, "description");
		
		TreeFolderInfo info = FileSystemManager.createFolder("test1", fd);
		File f = new File(info.getFullPath());
		assert f.exists() : "Folder system should be created";
		assert FileSystemManager.deleteDirectory(fd.getFullPath());
	}
	
	/**
	 * Test moving a folder. 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void moveFolderTest() throws LocationAlreadyExistsException
	{
	    String startPath = properties.getProperty("FileSystemManagerTest.move.initial");
	    
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName7", "fileDB_7", startPath, "description");
		
	    TreeFolderInfo infoCurrent = FileSystemManager.createFolder("start1", fd);
	    File f = new File(infoCurrent.getFullPath());
	    assert f.exists(): "Folder should be created";

	    String endPath = properties.getProperty("FileSystemManagerTest.move.final");
	    DefaultFileDatabase fd2 = server.createFileDatabase("displayName8", "fileDB_8", endPath, "description");
	    TreeFolderInfo infoDest = FileSystemManager.createFolder("end1", fd2);
	    
	    File oldFolder = new File(infoCurrent.getFullPath());
	    assert oldFolder.exists() : "Folder should exist";
	    
	    infoDest.addChild(infoCurrent);
	    
	    assert !oldFolder.exists() : "Folder " + f.getAbsolutePath() + " should no longer exist because it was moved ";
	    
	    assert infoCurrent.getExists() : "Child should still exist";
	    
		assert FileSystemManager.deleteDirectory(fd.getFullPath());
		assert FileSystemManager.deleteDirectory(fd2.getFullPath());
	}
	
	/**
	 * Test moving a folder that has children. 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void moveFolderTreeTest() throws LocationAlreadyExistsException
	{
	    String startPath = properties.getProperty("FileSystemManagerTest.move.initial");
	    
	    // start location for the tree.
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName9", "fileDB_9", startPath, "description");
	    
	    TreeFolderInfo infoCurrent = FileSystemManager.createFolder("start1", fd);
	    File f = new File(infoCurrent.getFullPath());
	    assert f.exists(): "Folder should be created";

	    String endPath = properties.getProperty("FileSystemManagerTest.move.final");
	    
	    // location to move the tree to
	    DefaultFileDatabase fd2 = server.createFileDatabase("displayName10", "fileDB_10", endPath, "description");
	    TreeFolderInfo infoDest = FileSystemManager.createFolder("end1", fd2);
	    
	    TreeFolderInfo child1 = infoCurrent.createChild("child1", "child1");
	    TreeFolderInfo subChild1 = infoCurrent.createChild("subChild1", "subChild1");
	    
	    File oldLocation = new File(infoCurrent.getFullPath());
	    assert oldLocation.exists() : "Folder " + oldLocation.getAbsolutePath() + " should exist";
        infoDest.addChild(infoCurrent);
        
        assert !oldLocation.exists() : "Folder " + oldLocation.getAbsolutePath() + " should not exist";
        assert child1.getExists() : "Child should be set to exists";
	    
	    File fileChild1 = new File(child1.getFullPath());
	    assert fileChild1.exists() : "File " + fileChild1.getAbsolutePath() + " should exist";
	    
	    File subChildFile = new File(subChild1.getFullPath());
	    assert subChildFile.exists() : "File " + subChildFile.getAbsolutePath() + " should exist";
	    
		assert FileSystemManager.deleteDirectory(fd.getFullPath());
		assert FileSystemManager.deleteDirectory(fd2.getFullPath());
	}
	
	/**
	 * Test moving a folder. 
	 * @throws LocationAlreadyExistsException 
	 */
	@Test
	public void moveFolderWithFilesTest() throws LocationAlreadyExistsException
	{
		// create a file server with the specified path
	    String serverPath = properties.getProperty("FileSystemManagerTest.move.initial");
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName11", "fileDB_11", serverPath, "description");
		
	    TreeFolderInfo infoCurrent = FileSystemManager.createFolder("start1", fd);
	    
	    File f = new File(infoCurrent.getFullPath());
	    assert f.exists(): "Folder should be created";
	    
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		// temporary path that the file will start in and then move it into the folder
		File folderFile = testUtil.creatFile(directory, "inFolderFile", 
				"Hello  -  This is text in a file that is going to be deleted");
		
		DefaultFileInfo myFileInfo = infoCurrent.createFileInfo(folderFile, "myFile");
		myFileInfo.setId(1l);

	    String endPath = properties.getProperty("FileSystemManagerTest.move.final");
	    
		DefaultFileDatabase fd2 = server.createFileDatabase("displayName12", "fileDB_12", endPath, "description");
		
	    TreeFolderInfo infoDest = FileSystemManager.createFolder("end1", fd2);
	    
	    File oldLocation = new File(infoCurrent.getFullPath());
	    assert oldLocation.exists() : "Folder " + oldLocation.getAbsolutePath() + " should exist";
	    infoDest.addChild(infoCurrent);
	    
	    assert !oldLocation.exists() : " Folder " + oldLocation.getAbsolutePath() + " should no longer exist";
	    
	    String correctFolderName = FilenameUtils.separatorsToSystem(infoDest.getFullPath() + "start1" + IOUtils.DIR_SEPARATOR);
	    assert infoCurrent.getFullPath().equals(correctFolderName) : "New path should equal " + correctFolderName + " but " +
	    		"equals " + infoCurrent.getFullPath();
	    
	    assert infoCurrent.getFiles().size() == 1 : "Info Dest should have one child but has " + 
	    infoDest.getFiles().size();
	    
	    DefaultFileInfo childFile =  infoCurrent.getFileInfo(1L);
	    assert childFile != null : "Child should not be null";
	    
	    String nameToEqual = FilenameUtils.separatorsToSystem(infoCurrent.getFullPath() + childFile.getName());
	    assert childFile.getFullPath().equals(nameToEqual) :
	    	"File name should equal " +  nameToEqual + " but "
	    	+ " = " + childFile.getFullPath(); 
	    
	    
	    assert FileSystemManager.deleteFolder(infoDest) : "Folder Path should be deleted";
	    assert FileSystemManager.deleteFolder(infoCurrent) : "Folder Path should be deleted";
	    
		assert FileSystemManager.deleteDirectory(fd.getFullPath());
		assert FileSystemManager.deleteDirectory(fd2.getFullPath());
	   
	}
	
	/**
	 * Test adding a file to a folder
	 * @throws LocationAlreadyExistsException 
	 */
	public void addFileTest() throws LocationAlreadyExistsException
	{
		// create a file server with the specified path
		String folderPath = properties.getProperty("FilsSystemManagerTest.folder_path");
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName13", "fileDB_13", folderPath, "description");
		
		
		TreeFolderInfo info = FileSystemManager.createFolder("addFileTest", fd);

		DefaultFileDatabase fd2 = server.createFileDatabase("displayName14", "fileDB_14", folderPath, "description");
		TreeFolderInfo infoCurrent = FileSystemManager.createFolder("start1", fd2);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "testFile", "Hello  -  This is text in a file");
		DefaultFileInfo fileInfo = FileSystemManager.addFile(info, f, "newTestFile");
		
		assert fileInfo.getName().equals("newTestFile") : "File name should be newTestFile";
		assert fileInfo.exists() : "File should exist";
		assert fileInfo.getCreatedDate() != null : "Created date should be set";
		assert fileInfo.getModifiedDate() != null : "Modified date should be set";
		
		File newFile = new File(fileInfo.getPath());
		assert newFile.exists() : "New File should exist";
		
		assert fileInfo.getFullPath().equals(info.getFullPath() + "newTestFile") : 
			"paths should be the same file path = " + fileInfo.getPath() + 
			" folder info path = " + info.getFullPath();
		
		assert FileSystemManager.deleteFolder(infoCurrent);
		assert FileSystemManager.deleteFolder(info);
		
		assert FileSystemManager.deleteDirectory(fd.getFullPath());
		assert FileSystemManager.deleteDirectory(fd2.getFullPath());
	}
	
	/**
	 * Test deleting a file 
	 * @throws LocationAlreadyExistsException 
	 */
	public void deleteFileTest() throws LocationAlreadyExistsException
	{
		// where the file will be copied into
		String folderPath = properties.getProperty("FilsSystemManagerTest.folder_path");
		
		// the final destination of where the file will reside.
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName15", "fileDB_15", folderPath, "description");
		
		TreeFolderInfo info = FileSystemManager.createFolder("addFileTest", fd);	
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
				"Hello  -  This is text in a file that is going to be deleted");
		
		DefaultFileInfo fileInfo = FileSystemManager.addFile(info, f, "newTestFile");
		
		assert fileInfo.getName().equals("newTestFile") : "File name should be newTestFile";
		assert fileInfo.getExists() : "File should be set as in the file system";
		
		File newFile = new File(fileInfo.getFullPath());
		assert newFile.exists() : "New File should exist";
		
		assert fileInfo.getFullPath().equals(info.getFullPath() + "newTestFile") : 
			"paths should be the same file path = " + fileInfo.getPath() + 
			" folder info path = " + info.getFullPath();
		
		assert FileSystemManager.deleteFile(fileInfo) : "The file should be deleted";
		assert !fileInfo.getExists() : "fileInfo should not be in the file system";
		assert !newFile.exists() : "File should no longer exist";
		
		assert FileSystemManager.deleteFolder(info) : "Should be able to delete the folder";
		assert FileSystemManager.deleteDirectory(fd.getFullPath());
	}
	
	/**
	 * Test deleteing a folder 
	 * @throws LocationAlreadyExistsException 
	 */
	public void deleteFolderTest() throws LocationAlreadyExistsException
	{
		// where the file will be copied into
		String folderPath = properties.getProperty("FileSystemManagerTest.delete_folder");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName17", "fileDB_17", folderPath, "description");
		
		
		TreeFolderInfo info = FileSystemManager.createFolder("folder_to_delete", fd);
	    File f = new File(info.getFullPath());
	    assert f.exists() : "Folder should exist";
	    assert f.isDirectory() : "Should be a directory";
	    
	    FileSystemManager.deleteFolder(info);
	    
	    assert !f.exists() : "Folder should no longer exist";
	    assert !info.getExists() : "File should not be in the file system";
	    
	    assert FileSystemManager.deleteDirectory(fd.getFullPath());
	}
	
	
	/**
	 * Test moving a file from one folder into another.
	 * @throws LocationAlreadyExistsException 
	 */
	public void moveFileTest() throws LocationAlreadyExistsException
	{
		String folderPath = properties.getProperty("FileSystemManagerTest.file.move");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName18", "fileDB_18", folderPath, "description");
		
		TreeFolderInfo folder1 = FileSystemManager.createFolder("folder1", fd);
		TreeFolderInfo folder2 = FileSystemManager.createFolder("folder2", fd);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		// create a file to move
		File f = testUtil.creatFile(directory, "testFile", "Hello  -  This is text in a file");
		
		// add the file to folder 1
		DefaultFileInfo fileInfo = FileSystemManager.addFile(folder1, f, "newTestFile");
		
		assert fileInfo.getName().equals("newTestFile") : "File name should be newTestFile";
		assert fileInfo.exists() : "File should exist";
		assert fileInfo.getCreatedDate() != null : "Created date should be set";
		assert fileInfo.getModifiedDate() != null : "Modified date should be set";
		
		File newFile = new File(fileInfo.getPath());
		assert newFile.exists() : "New File " + newFile.getAbsolutePath() + " should exist ";
		
		assert fileInfo.getFullPath().equals(folder1.getFullPath() + "newTestFile") : 
			"paths should be the same file path = " + fileInfo.getPath() + 
			" folder info path = " + folder1.getFullPath();
		
		FileSystemManager.moveFile(folder2, fileInfo);
		
		assert fileInfo.getName().equals("newTestFile") : "File name should be newTestFile";
		assert fileInfo.exists() : "File should exist";
		assert fileInfo.getCreatedDate() != null : "Created date should be set";
		assert fileInfo.getModifiedDate() != null : "Modified date should be set";
		
		newFile = new File(fileInfo.getPath());
		assert newFile.exists() : "New File " + newFile.getAbsolutePath() + " should exist ";
		
		assert fileInfo.getFullPath().equals(folder2.getFullPath() + "newTestFile") : 
			"paths should be the same file path = " + fileInfo.getPath() + 
			" folder info path = " + folder2.getFullPath();
		
		
		assert FileSystemManager.deleteDirectory(fd.getFullPath());
	}
	
	/**
	 * Test moving a folder from somewhere inside the folder to a root folder.
	 * @throws LocationAlreadyExistsException 
	 */
	public void moveFolderToRootTest() throws LocationAlreadyExistsException
	{
		String folderPath = properties.getProperty("FileSystemManagerTest.file.move.root");
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName18", "fileDB_18", folderPath, "description");
		
		TreeFolderInfo folder1 = FileSystemManager.createFolder("folder1", fd);
		
		TreeFolderInfo child1 = folder1.createChild("display_1", "name_1");
		TreeFolderInfo subChild1 = child1.createChild("sub_display_1", "sub_name_1");
		TreeFolderInfo subSubChild1 = subChild1.createChild("sus_sub_display_1", "sub_sub_name_1");
		TreeFolderInfo subSubChild2 = subChild1.createChild("sus_sub_display_2", "sub_sub_name_2");
		
		// check left and right values
		assert folder1.getLeftValue() == 1L : " Should equal 1 but equals " + folder1.getLeftValue();
		assert folder1.getRightValue() == 10L : " Should equal 10 but equals " + folder1.getRightValue();

		assert child1.getLeftValue() == 2L : " Should equal 2 but equals " + child1.getLeftValue();
		assert child1.getRightValue() == 9L : " Should equal 9 but equals " + child1.getRightValue();

		assert subChild1.getLeftValue() == 3L : " Should equal 3 but equals " + subChild1.getLeftValue();
		assert subChild1.getRightValue() == 8L : " Should equal 8 but equals " + subChild1.getRightValue();
		
		assert subSubChild1.getLeftValue() == 4L : " Should equal 4 but equals " + subSubChild1.getLeftValue();
		assert subSubChild1.getRightValue() == 5L : " Should equal 5 but equals " + subSubChild1.getRightValue();

		assert subSubChild2.getLeftValue() == 6L : " Should equal 6 but equals " + subSubChild2.getLeftValue();
		assert subSubChild2.getRightValue() == 7L : " Should equal 7 but equals " + subSubChild2.getRightValue();
		
		//move sub child 1 to root location in file database
		FileSystemManager.moveFolder(subChild1, fd);
		
		// check left and right values of un-moved children
		assert folder1.getLeftValue() == 1L : " Should equal 1 but equals " + folder1.getLeftValue();
		assert folder1.getRightValue() == 4L : " Should equal 4 but equals " + folder1.getRightValue();

		assert child1.getLeftValue() == 2L : " Should equal 2 but equals " + child1.getLeftValue();
		assert child1.getRightValue() == 3L : " Should equal 3 but equals " + child1.getRightValue();
		
		// check the moved child
		assert subChild1.getTreeRoot().equals(subChild1) : "Should be it's own root but root is "
			+ subChild1.getTreeRoot().toString();
		assert subChild1.isRoot() : "Should be a root folder";
		
		assert subChild1.getLeftValue() == 1L : " Should equal 1 but equals " + subChild1.getLeftValue();
		assert subChild1.getRightValue() == 6L : " Should equal 6 but equals " + subChild1.getRightValue();
		
		assert subSubChild1.getLeftValue() == 2L : " Should equal 2 but equals " + subSubChild1.getLeftValue();
		assert subSubChild1.getRightValue() == 3L : " Should equal 3 but equals " + subSubChild1.getRightValue();

		assert subSubChild2.getLeftValue() == 4L : " Should equal 4 but equals " + subSubChild2.getLeftValue();
		assert subSubChild2.getRightValue() == 5L : " Should equal 5 but equals " + subSubChild2.getRightValue();
		
		assert subChild1.getPath().equals(fd.getFullPath()): "Full path should be equal to db path : " +  
		    fd.getFullPath() + " but equals " + subChild1.getPath();
		
		String childPath = fd.getFullPath() + subChild1.getName() + IOUtils.DIR_SEPARATOR;
		
		assert subChild1.getFullPath().equals(childPath) :
			"Sub ehcild full path should equal " + childPath + " but equals " + subChild1.getFullPath();
		
		String subSubChildPath1 = subChild1.getFullPath() + subSubChild1.getName() + IOUtils.DIR_SEPARATOR;
		String subSubChildPath2 = subChild1.getFullPath() + subSubChild2.getName() + IOUtils.DIR_SEPARATOR;
		
		assert subSubChild1.getFullPath().equals(subSubChildPath1) :
			"Sub sub hcild full path should equal " + subSubChildPath1 + " but equals " + subSubChild1.getFullPath();

		assert subSubChild2.getFullPath().equals(subSubChildPath2) :
			"Sub sub hcild full path should equal " + subSubChildPath2 + " but equals " + subSubChild2.getFullPath();

		assert FileSystemManager.deleteDirectory(fd.getFullPath());
	}
	
	/**
	 * Test moving a folder from one root folder to another.
	 * @throws LocationAlreadyExistsException 
	 */
	public void moveFolderToDifferentRootTest() throws LocationAlreadyExistsException
	{
		String folderPath = properties.getProperty("FileSystemManagerTest.file.move.root");
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName77", "fileDB_77", folderPath, "description");
		
		TreeFolderInfo folder1 = FileSystemManager.createFolder("folder1", fd);
		TreeFolderInfo folder2 = FileSystemManager.createFolder("folder2", fd);
		
		TreeFolderInfo child1 = folder1.createChild("display_1", "name_1");
		
		File oldFolder = new File(child1.getFullPath());
		assert oldFolder.exists() : " old folder " + oldFolder.getAbsolutePath() + " should exist"; 
		
		folder2.addChild(child1);

		
		assert !oldFolder.exists() : " old folder " + oldFolder.getAbsolutePath() + " should not exist";
		assert child1.getExists() : "Child should still exist";
		
		// check the moved child
		assert child1.getTreeRoot().equals(folder2) : "Should be folder2 but root is " + child1.getTreeRoot().toString();
		assert !child1.isRoot() : "Should be a root folder";
		
		assert child1.getPath().equals(folder2.getFullPath()): "Full path should be equal to db path : " +  
		    folder2.getFullPath() + " but equals " + child1.getPath();
		
		assert folder1.getChild(child1.getName()) == null : "Should not find child1";

		assert FileSystemManager.deleteDirectory(fd.getFullPath());
	}

}
