/**  
   Copyright 2008 - 2012 University of Rochester

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


package edu.ur.ir.groupspace;

import java.io.File;
import java.util.Properties;

import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.test.helper.PropertiesLoader;
import edu.ur.ir.test.helper.RepositoryBasedTestHelper;

import edu.ur.util.FileUtil;

@Test(groups = { "baseTests" }, enabled = true)
public class GroupWorkspaceProjectPageFolderTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		GroupWorkspaceProjectPageFolder GroupWorkspaceProjectPageFolder = new GroupWorkspaceProjectPageFolder();
		
		GroupWorkspaceProjectPageFolder.setDescription("myDescription");
		GroupWorkspaceProjectPageFolder.setName("myName");
		
		assert GroupWorkspaceProjectPageFolder.getDescription().equals("myDescription") : "Descriptions should be equal";
		assert GroupWorkspaceProjectPageFolder.getName().equals("myName") : "Names should be equal";
		
		assert GroupWorkspaceProjectPageFolder.allowsChildren() : "Default is to allow children";
	}
	
	/**
	 * Make sure an error is thrown if duplicate folders are added
	 */
	public void testDuplicateFolders()
	{
		
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupWorkspace");
		GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        GroupWorkspaceProjectPageFolder GroupWorkspaceProjectPageFolder = new GroupWorkspaceProjectPageFolder(groupWorkspaceProjectPage, "myFolder");
		
		GroupWorkspaceProjectPageFolder.setDescription("myDescription");
		
		
		try
		{
		    GroupWorkspaceProjectPageFolder.createChild("folder1");
		    GroupWorkspaceProjectPageFolder.createChild("FoldeR1");
		    assert false : "This should fail before this with a " +
		    		"duplicate name exception";
		}
		catch(DuplicateNameException dne)
		{
			assert true;
		}
	}
	
	/**
	 * Test adding children.
	 */
	public void testAddChildren()
	{
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupWorkspace");
		GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
		
		// create the root collection
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder1 = new GroupWorkspaceProjectPageFolder(groupWorkspaceProjectPage, "nates_folder");
		
		assert groupWorkspaceProjectPageFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder1.getRightValue() == 2 : "Right value should equal 2 but equals " + groupWorkspaceProjectPageFolder1.getRightValue();
		
		assert groupWorkspaceProjectPageFolder1.getGroupWorkspaceProjectPage().equals(groupWorkspaceProjectPage) : "Researchers should be the same";
		
		assert groupWorkspaceProjectPageFolder1.getPath().equals("/") : "Path should "
			+ "equal / but equals " + groupWorkspaceProjectPageFolder1.getPath();

		
		assert groupWorkspaceProjectPageFolder1.getFullPath().equals("/nates_folder/") : "Path should "
			+ "equal /nates_folder/ but equals " + groupWorkspaceProjectPageFolder1.getFullPath();
		
		// add first child
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder2 = null;
		// add second child child
		try
		{
		    groupWorkspaceProjectPageFolder2 = groupWorkspaceProjectPageFolder1.createChild("groupWorkspaceProjectPageFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert groupWorkspaceProjectPageFolder2.getFullPath().equals("/nates_folder/groupWorkspaceProjectPageFolder2/") : "Path should "
			    + "equal /nates_folder/groupWorkspaceProjectPageFolder2/ but equals " + groupWorkspaceProjectPageFolder2.getFullPath();

		assert groupWorkspaceProjectPageFolder2.getPath().equals("/nates_folder/") : "Path should "
			    + "equal /nates_folder/ but equals " + groupWorkspaceProjectPageFolder2.getPath();

		
		assert groupWorkspaceProjectPageFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder1.getRightValue() == 4 : "Right value should equal 4 but equals " + groupWorkspaceProjectPageFolder1.getRightValue();
		
		assert groupWorkspaceProjectPageFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ groupWorkspaceProjectPageFolder2.getLeftValue();
		
		assert groupWorkspaceProjectPageFolder2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ groupWorkspaceProjectPageFolder2.getRightValue();
		
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder3 = null;
		// add second child child
		try
		{
		    groupWorkspaceProjectPageFolder3 = groupWorkspaceProjectPageFolder1.createChild("groupWorkspaceProjectPageFolder3");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}

		
		assert groupWorkspaceProjectPageFolder3.getPath().equals("/nates_folder/") : "Path should "
			+ "equal /nates_folder/ but equals " + groupWorkspaceProjectPageFolder3.getPath();

		assert groupWorkspaceProjectPageFolder3.getFullPath().equals("/nates_folder/groupWorkspaceProjectPageFolder3/") : "Path should "
			+ "equal /nates_folder/groupWorkspaceProjectPageFolder3/ but equals " + groupWorkspaceProjectPageFolder3.getFullPath();

		
		assert groupWorkspaceProjectPageFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder1.getRightValue() == 6 : "Right value should equal 6 but equals " + groupWorkspaceProjectPageFolder1.getRightValue();
		
		
		assert groupWorkspaceProjectPageFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ groupWorkspaceProjectPageFolder2.getLeftValue();
		
		assert groupWorkspaceProjectPageFolder2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ groupWorkspaceProjectPageFolder2.getRightValue();
		
		assert groupWorkspaceProjectPageFolder3.getLeftValue() == 4 : "Collection 4 left value should equal 4 but is " 
			+ groupWorkspaceProjectPageFolder3.getLeftValue();
		
		assert groupWorkspaceProjectPageFolder3.getRightValue() == 5 : "Collection 3 right value should equal 5 but is " 
			+ groupWorkspaceProjectPageFolder3.getRightValue();

		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageSubFolder1 = null;
		// add sub folder
		try
		{
		    groupWorkspaceProjectPageSubFolder1 = groupWorkspaceProjectPageFolder2.createChild("groupWorkspaceProjectPageSubFolder1");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert groupWorkspaceProjectPageSubFolder1.getPath().equals("/nates_folder/groupWorkspaceProjectPageFolder2/") : "Path should "
			+ "equal /nates_folder/groupWorkspaceProjectPageFolder2/ but equals " + groupWorkspaceProjectPageSubFolder1.getPath();

		assert groupWorkspaceProjectPageSubFolder1.getFullPath().equals("/nates_folder/groupWorkspaceProjectPageFolder2/groupWorkspaceProjectPageSubFolder1/") : "Path should "
			+ "equal /nates_folder/groupWorkspaceProjectPageFolder2/groupWorkspaceProjectPageSubFolder1/ but equals " + groupWorkspaceProjectPageSubFolder1.getFullPath();

		assert groupWorkspaceProjectPageFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder1.getRightValue() == 8 : "Right value should equal 8 but equals " + groupWorkspaceProjectPageFolder1.getRightValue();
		assert groupWorkspaceProjectPageFolder1.getGroupWorkspaceProjectPage().equals(groupWorkspaceProjectPage);

		assert groupWorkspaceProjectPageFolder2.getLeftValue() == 2 : "GroupWorkspaceProjectPage GroupWorkspaceProjectPage 2 left value should equal 2 but is " 
			+ groupWorkspaceProjectPageFolder2.getLeftValue();
		assert groupWorkspaceProjectPageFolder2.getGroupWorkspaceProjectPage().equals(groupWorkspaceProjectPage);

		assert groupWorkspaceProjectPageFolder2.getRightValue() == 5 : "GroupWorkspaceProjectPage GroupWorkspaceProjectPage 2 right value should equal 5 but is " 
			+ groupWorkspaceProjectPageFolder2.getRightValue();
		
		assert groupWorkspaceProjectPageSubFolder1.getLeftValue() == 3 : "sub GroupWorkspaceProjectPage GroupWorkspaceProjectPage 1 left value should equal 3 but is " 
			+ groupWorkspaceProjectPageSubFolder1.getLeftValue();
		
		assert groupWorkspaceProjectPageSubFolder1.getRightValue() == 4 : "Sub GroupWorkspaceProjectPage GroupWorkspaceProjectPage 1 right value should equal 4 but is " 
			+ groupWorkspaceProjectPageSubFolder1.getRightValue();
		assert groupWorkspaceProjectPageSubFolder1.getGroupWorkspaceProjectPage().equals(groupWorkspaceProjectPage);
		
		
		assert groupWorkspaceProjectPageFolder3.getLeftValue() == 6 : "Collection 3 left value should equal 6 but is " 
			+ groupWorkspaceProjectPageFolder3.getLeftValue();
		
		assert groupWorkspaceProjectPageFolder3.getRightValue() == 7 : "Collection 3 right value should equal 7 but is " 
			+ groupWorkspaceProjectPageFolder3.getRightValue();
		assert groupWorkspaceProjectPageFolder3.getGroupWorkspaceProjectPage().equals(groupWorkspaceProjectPage);


		// add sub collection
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageSubFolder2 = null;
		try
		{
		    groupWorkspaceProjectPageSubFolder2 = groupWorkspaceProjectPageFolder3.createChild("groupWorkspaceProjectPageSubFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}

		assert groupWorkspaceProjectPageSubFolder2.getPath().equals("/nates_folder/groupWorkspaceProjectPageFolder3/") : "Path should "
			+ "equal /nates_folder/groupWorkspaceProjectPageFolder3/ but equals " + groupWorkspaceProjectPageSubFolder1.getPath();

		assert groupWorkspaceProjectPageSubFolder2.getFullPath().equals("/nates_folder/groupWorkspaceProjectPageFolder3/groupWorkspaceProjectPageSubFolder2/") : "Path should "
			+ "equal /nates_folder/groupWorkspaceProjectPageFolder3/groupWorkspaceProjectPageSubFolder2/ but equals " + groupWorkspaceProjectPageSubFolder1.getFullPath();

		
		assert groupWorkspaceProjectPageFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder1.getRightValue() == 10 : "Right value should equal 10 but equals " + groupWorkspaceProjectPageFolder1.getRightValue();
		
		assert groupWorkspaceProjectPageFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ groupWorkspaceProjectPageFolder2.getLeftValue();
		
		assert groupWorkspaceProjectPageFolder2.getRightValue() == 5 : "Collection 2 right value should equal 5 but is " 
			+ groupWorkspaceProjectPageFolder2.getRightValue();
		
		assert groupWorkspaceProjectPageSubFolder1.getLeftValue() == 3 : "sub Collection 1 left value should equal 3 but is " 
			+ groupWorkspaceProjectPageSubFolder1.getLeftValue();
		
		assert groupWorkspaceProjectPageSubFolder1.getRightValue() == 4 : "Sub collection 1 right value should equal 4 but is " 
			+ groupWorkspaceProjectPageSubFolder1.getRightValue();
		
		assert groupWorkspaceProjectPageFolder3.getLeftValue() == 6 : "Collection 3 left value should equal 6 but is " 
			+ groupWorkspaceProjectPageFolder3.getLeftValue();
		
		assert groupWorkspaceProjectPageFolder3.getRightValue() == 9 : "Collection 3 right value should equal 7 but is " 
			+ groupWorkspaceProjectPageFolder3.getRightValue();

		assert groupWorkspaceProjectPageSubFolder2.getLeftValue() == 7 : "sub Collection 2 left value should equal 7 but is " 
			+ groupWorkspaceProjectPageSubFolder2.getLeftValue();
		
		assert groupWorkspaceProjectPageSubFolder2.getRightValue() == 8 : "Sub collection 2 right value should equal 8 but is " 
			+ groupWorkspaceProjectPageSubFolder2.getRightValue();
	}

	/**
	 * Test removing a GroupWorkspaceProjectPage folder. 
	 */
	public void testRemoveResearcherFolder()
	{
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupWorkspace");
		GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
	
		// create the root colleciton
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder1 = new GroupWorkspaceProjectPageFolder(groupWorkspaceProjectPage, "groupWorkspaceProjectPageFolder1");
		
		
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder2 = null;
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder3 = null;
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageSubFolder1 = null;
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageSubFolder2 = null;
		try
		{
		    // add first child
		    groupWorkspaceProjectPageFolder2 = groupWorkspaceProjectPageFolder1.createChild("groupWorkspaceProjectPageFolder2");
				
		    // add second child child
		    groupWorkspaceProjectPageFolder3 = groupWorkspaceProjectPageFolder1.createChild("groupWorkspaceProjectPageFolder3");
			
		    // add sub collection
		    groupWorkspaceProjectPageSubFolder1 = groupWorkspaceProjectPageFolder2.createChild("groupWorkspaceProjectPageSubFolder1");
		
		    // add sub collection
		    groupWorkspaceProjectPageSubFolder2 = groupWorkspaceProjectPageFolder3.createChild("groupWorkspaceProjectPageSubFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		// remove the irSubColleciton1
		assert groupWorkspaceProjectPageFolder2.removeChild(groupWorkspaceProjectPageSubFolder1) : "groupWorkspaceProjectPageSubFolder1 should be removed";
		
		assert groupWorkspaceProjectPageSubFolder1.getParent() == null : "Parent Should null but is " + 
		    groupWorkspaceProjectPageSubFolder1.getParent().toString();
		assert groupWorkspaceProjectPageSubFolder1.isRoot() : "Should be root";

		// check re-numbering
		assert groupWorkspaceProjectPageFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder1.getRightValue() == 8 : "Right value should equal 8 but equals " + groupWorkspaceProjectPageFolder1.getRightValue();
		
		assert groupWorkspaceProjectPageFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ groupWorkspaceProjectPageFolder2.getLeftValue();
		
		assert groupWorkspaceProjectPageFolder2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ groupWorkspaceProjectPageFolder2.getRightValue();
		
		assert groupWorkspaceProjectPageFolder3.getLeftValue() == 4 : "Collection 3 left value should equal 4 but is " 
			+ groupWorkspaceProjectPageFolder3.getLeftValue();
		
		assert groupWorkspaceProjectPageFolder3.getRightValue() == 7 : "Collection 3 right value should equal 7 but is " 
			+ groupWorkspaceProjectPageFolder3.getRightValue();

		assert groupWorkspaceProjectPageSubFolder2.getLeftValue() == 5 : "sub Collection 2 left value should equal 5 but is " 
			+ groupWorkspaceProjectPageSubFolder2.getLeftValue();
		
		assert groupWorkspaceProjectPageSubFolder2.getRightValue() == 6 : "Sub collection 2 right value should equal 6 but is " 
			+ groupWorkspaceProjectPageSubFolder2.getRightValue();
		
		// remove collection 3
		assert groupWorkspaceProjectPageFolder1.removeChild(groupWorkspaceProjectPageFolder3) : "Collection 3 should be removed"; 
		assert groupWorkspaceProjectPageFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder1.getRightValue() == 4 : "Right value should equal 4 but equals " + groupWorkspaceProjectPageFolder1.getRightValue();
		
		assert groupWorkspaceProjectPageFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ groupWorkspaceProjectPageFolder2.getLeftValue();
		
		assert groupWorkspaceProjectPageFolder2.getRightValue() == 3 : "Collection 2 right value should equal 5 but is " 
			+ groupWorkspaceProjectPageFolder2.getRightValue();
		
	}
	
	/**
	 * Test adding children.
	 * @throws LocationAlreadyExistsException 
	 */
	public void testAddIrFile() throws IllegalFileSystemNameException, LocationAlreadyExistsException
	{
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_core_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		// create the first file to store in the temporary folder
		File f = testUtil.creatFile(directory, "testFile",
				"Hello  - versionedIrFile This is text in a file"); 
		
		// get the file database 
		FileDatabase fd = repo.getFileDatabase();
		
		// create a new file info container
		FileInfo fileInfo1 = fd.addFile(f, "newFile1");
		
		// create the owner of the folders
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupWorkspace");
		GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();

		// create a new versioned file
		IrFile irf = new IrFile(fileInfo1, "displayName1");
		
		// create the root colleciton
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder1 = new GroupWorkspaceProjectPageFolder(groupWorkspaceProjectPage, "groupWorkspaceProjectPageFolder1");
		
		GroupWorkspaceProjectPageFile rf = null;
		
		try
		{
		    rf = groupWorkspaceProjectPageFolder1.addFile(irf);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert groupWorkspaceProjectPageFolder1.getGroupWorkspaceProjectPageFile("displayName1").equals(rf) : 
			"Ir file should be found";
		
		
		groupWorkspaceProjectPageFolder1.remove(rf);
		
		assert groupWorkspaceProjectPageFolder1.getGroupWorkspaceProjectPageFile(rf.getName()) == null : 
			"Should not be able fo find the GroupWorkspaceProjectPage file " + rf;
		
		repoHelper.cleanUpRepository();
	
	}
	
	/**
	 * Test adding children.
	 * @throws LocationAlreadyExistsException 
	 */
	public void testMoveResearcherFile() throws IllegalFileSystemNameException, LocationAlreadyExistsException
	{
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper();
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_core_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		// create the first file to store in the temporary folder
		File f = testUtil.creatFile(directory, "testFile",
				"Hello  - versionedIrFile This is text in a file"); 
		
		// get the file database 
		FileDatabase fd = repo.getFileDatabase();
		
		// create a new file info container
		FileInfo fileInfo1 = fd.addFile(f, "newFile1");
		
		// create the owner of the folders
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupWorkspace");
		
		GroupWorkspaceProjectPage groupWorkspaceProjectPage = new GroupWorkspaceProjectPage(groupWorkspace);

		// create a new versioned file
		IrFile irf = new IrFile(fileInfo1, "displayName1" );
		
		
		// create the root colleciton
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder1 = new GroupWorkspaceProjectPageFolder(groupWorkspaceProjectPage, "groupWorkspaceProjectPageFolder1");

		GroupWorkspaceProjectPageFile rf = null;
		try
		{
		    rf = groupWorkspaceProjectPageFolder1.addFile(irf);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert groupWorkspaceProjectPageFolder1.getGroupWorkspaceProjectPageFile("displayName1").equals(rf) : 
			"Ir file should be found";
		
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder2 = new GroupWorkspaceProjectPageFolder(groupWorkspaceProjectPage, "groupWorkspaceProjectPageFolder2");

		try
		{
		    groupWorkspaceProjectPageFolder2.addGroupWorkspaceProjectPageFile(rf);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert groupWorkspaceProjectPageFolder1.getGroupWorkspaceProjectPageFile(rf.getName()) == null : 
			"should not be able to find " + rf;
		
		assert groupWorkspaceProjectPageFolder2.getGroupWorkspaceProjectPageFile(rf.getName()) != null : 
			"Should be able fo find the GroupWorkspaceProjectPage file " + rf;
		
		repoHelper.cleanUpRepository();
	
	}
	
	/**
	 * Test moving a GroupWorkspaceProjectPage folder.
	 */
	public void testMoveResearcherFolderWithinTree()
	{
		// create the owner of the folders
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupWorkspace");
		GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
		
		// create the root folder
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder1 = new GroupWorkspaceProjectPageFolder(groupWorkspaceProjectPage, "groupWorkspaceProjectPageFolder1");
		
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder2 = null;
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder3 = null;
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageSubFolder1 = null;
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageSubFolder2 = null;
		try
		{

		    // add first child
		    groupWorkspaceProjectPageFolder2 = groupWorkspaceProjectPageFolder1.createChild("groupWorkspaceProjectPageFolder2");
				
		    // add second child child
		    groupWorkspaceProjectPageFolder3 = groupWorkspaceProjectPageFolder1.createChild("groupWorkspaceProjectPageFolder3");
			
		    // add sub folder
		    groupWorkspaceProjectPageSubFolder1 = groupWorkspaceProjectPageFolder2.createChild("groupWorkspaceProjectPageSubFolder1");
		
		    // add sub folder
		    groupWorkspaceProjectPageSubFolder2 = groupWorkspaceProjectPageFolder3.createChild("groupWorkspaceProjectPageSubFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert groupWorkspaceProjectPageFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		groupWorkspaceProjectPageFolder1.getRightValue();
		
		assert groupWorkspaceProjectPageFolder2.getLeftValue() == 2 : "Right value should be 2 but is " +
		groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder2.getRightValue() == 5 : "Right value sould be  5 but is " +
		groupWorkspaceProjectPageFolder2.getRightValue();
		
		assert groupWorkspaceProjectPageFolder3.getLeftValue() == 6 : "Right value should be 6 but is " +
		groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder3.getRightValue() == 9 : "Right value sould be 9 but is " +
		groupWorkspaceProjectPageFolder3.getRightValue();
		
		assert groupWorkspaceProjectPageSubFolder1.getLeftValue() == 3 : "Right value should be 3 but is " +
		groupWorkspaceProjectPageSubFolder1.getLeftValue();
		assert groupWorkspaceProjectPageSubFolder1.getRightValue() == 4 : "Right value sould be 4 but is " +
		groupWorkspaceProjectPageSubFolder1.getRightValue();
		
		assert groupWorkspaceProjectPageSubFolder2.getLeftValue() == 7 : "Right value should be 7 but is " +
		groupWorkspaceProjectPageSubFolder2.getLeftValue();
		assert groupWorkspaceProjectPageSubFolder2.getRightValue() == 8 : "Right value sould be 8 but is " +
		groupWorkspaceProjectPageSubFolder2.getRightValue();
		
		try
		{
		    groupWorkspaceProjectPageFolder1.addChild(groupWorkspaceProjectPageSubFolder2);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert groupWorkspaceProjectPageSubFolder2.getParent().equals(groupWorkspaceProjectPageFolder1) :
			"Parent should be GroupWorkspaceProjectPage folder 1 but is " + groupWorkspaceProjectPageSubFolder2.getParent();
		
		
		assert groupWorkspaceProjectPageFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		groupWorkspaceProjectPageFolder1.getRightValue();
		
		assert groupWorkspaceProjectPageFolder2.getLeftValue() == 2 : "Right value should be 2 but is " +
		groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder2.getRightValue() == 5 : "Right value sould be  5 but is " +
		groupWorkspaceProjectPageFolder2.getRightValue();
		
		assert groupWorkspaceProjectPageFolder3.getLeftValue() == 6 : "Right value should be 6 but is " +
		groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder3.getRightValue() == 7 : "Right value sould be 7 but is " +
		groupWorkspaceProjectPageFolder3.getRightValue();
		
		assert groupWorkspaceProjectPageSubFolder1.getLeftValue() == 3 : "Right value should be 6 but is " +
		groupWorkspaceProjectPageSubFolder1.getLeftValue();
		assert groupWorkspaceProjectPageSubFolder1.getRightValue() == 4 : "Right value sould be 7 but is " +
		groupWorkspaceProjectPageSubFolder1.getRightValue();
		
		assert groupWorkspaceProjectPageSubFolder2.getLeftValue() == 8 : "Right value should be 8 but is " +
		groupWorkspaceProjectPageSubFolder2.getLeftValue();
		assert groupWorkspaceProjectPageSubFolder2.getRightValue() == 9 : "Right value sould be 9 but is " +
		groupWorkspaceProjectPageSubFolder2.getRightValue();
		
		try
		{
		    groupWorkspaceProjectPageSubFolder2.addChild(groupWorkspaceProjectPageFolder2);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert groupWorkspaceProjectPageFolder2.getParent().equals(groupWorkspaceProjectPageSubFolder2);
		
		assert groupWorkspaceProjectPageFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		groupWorkspaceProjectPageFolder1.getRightValue();
		
		assert groupWorkspaceProjectPageFolder2.getLeftValue() == 5 : "Right value should be 2 but is " +
		groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder2.getRightValue() == 8 : "Right value sould be  5 but is " +
		groupWorkspaceProjectPageFolder2.getRightValue();
		
		assert groupWorkspaceProjectPageFolder3.getLeftValue() == 2 : "Right value should be 6 but is " +
		groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder3.getRightValue() == 3 : "Right value sould be 7 but is " +
		groupWorkspaceProjectPageFolder3.getRightValue();
		
		assert groupWorkspaceProjectPageSubFolder1.getLeftValue() == 6 : "Right value should be 6 but is " +
		groupWorkspaceProjectPageSubFolder1.getLeftValue();
		assert groupWorkspaceProjectPageSubFolder1.getRightValue() == 7 : "Right value sould be 7 but is " +
		groupWorkspaceProjectPageSubFolder1.getRightValue();
		
		assert groupWorkspaceProjectPageSubFolder2.getLeftValue() == 4 : "Right value should be 8 but is " +
		groupWorkspaceProjectPageSubFolder2.getLeftValue();
		assert groupWorkspaceProjectPageSubFolder2.getRightValue() == 9 : "Right value sould be 9 but is " +
		groupWorkspaceProjectPageSubFolder2.getRightValue();
	}
	
	/**
	 * Test moving a GroupWorkspaceProjectPage folder to the root of the person.
	 * @throws DuplicateNameException 
	 */
	public void testMoveResearcherFolderToRootOfUser() throws DuplicateNameException
	{
		// create the owner of the folders
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupWorkspace");
		GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
		
		// create the root collection
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder1 = new GroupWorkspaceProjectPageFolder(groupWorkspaceProjectPage, "groupWorkspaceProjectPageFolder1");
		
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder2 = null;
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder3 = null;
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageSubFolder1 = null;
		GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageSubFolder2 = null;
		
		try
		{
		    // add first child
		    groupWorkspaceProjectPageFolder2 = groupWorkspaceProjectPageFolder1.createChild("groupWorkspaceProjectPageFolder2");
				
		    // add second child child
		    groupWorkspaceProjectPageFolder3 = groupWorkspaceProjectPageFolder1.createChild("groupWorkspaceProjectPageFolder3");
			
		    // add sub folder
		    groupWorkspaceProjectPageSubFolder1 = groupWorkspaceProjectPageFolder2.createChild("groupWorkspaceProjectPageSubFolder1");
		
		    // add sub folder
		    groupWorkspaceProjectPageSubFolder2 = groupWorkspaceProjectPageFolder3.createChild("groupWorkspaceProjectPageSubFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert groupWorkspaceProjectPageFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		groupWorkspaceProjectPageFolder1.getRightValue();
		
		assert groupWorkspaceProjectPageFolder2.getLeftValue() == 2 : "Right value should be 2 but is " +
		groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder2.getRightValue() == 5 : "Right value sould be  5 but is " +
		groupWorkspaceProjectPageFolder2.getRightValue();
		
		assert groupWorkspaceProjectPageFolder3.getLeftValue() == 6 : "Right value should be 6 but is " +
		groupWorkspaceProjectPageFolder1.getLeftValue();
		assert groupWorkspaceProjectPageFolder3.getRightValue() == 9 : "Right value sould be 9 but is " +
		groupWorkspaceProjectPageFolder3.getRightValue();
		
		assert groupWorkspaceProjectPageSubFolder1.getLeftValue() == 3 : "Right value should be 3 but is " +
		groupWorkspaceProjectPageSubFolder1.getLeftValue();
		assert groupWorkspaceProjectPageSubFolder1.getRightValue() == 4 : "Right value sould be 4 but is " +
		groupWorkspaceProjectPageSubFolder1.getRightValue();
		
		assert groupWorkspaceProjectPageSubFolder2.getLeftValue() == 7 : "Right value should be 7 but is " +
		groupWorkspaceProjectPageSubFolder2.getLeftValue();
		assert groupWorkspaceProjectPageSubFolder2.getRightValue() == 8 : "Right value sould be 8 but is " +
		groupWorkspaceProjectPageSubFolder2.getRightValue();
		
		//make GroupWorkspaceProjectPage folder 2 a root folder by adding them to the user
		groupWorkspaceProjectPage.addRootFolder(groupWorkspaceProjectPageFolder2);
		
		assert groupWorkspaceProjectPageFolder2.getLeftValue() == 1 : "Right value should be 4 but is " 
			+ groupWorkspaceProjectPageFolder2.getLeftValue();
		
		assert groupWorkspaceProjectPageFolder2.getRightValue() == 4 : "Right value should be 4 but is " 
			+ groupWorkspaceProjectPageFolder2.getRightValue();
		
		assert groupWorkspaceProjectPageFolder2.getTreeRoot().equals(groupWorkspaceProjectPageFolder2) : "GroupWorkspaceProjectPage folder 2 should be root but root = " + 
		groupWorkspaceProjectPageFolder2.getTreeRoot();
		

	}

}
