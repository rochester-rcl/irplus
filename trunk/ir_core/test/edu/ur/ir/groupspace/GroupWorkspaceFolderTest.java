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
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.test.helper.PropertiesLoader;
import edu.ur.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.util.FileUtil;



/**
 * Testing for group spaces.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class GroupWorkspaceFolderTest 
{
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/**
	 * Make sure an error is thrown if duplicate folders are added
	 */
	public void testDuplicateFolders()  throws IllegalFileSystemNameException
	{
		IrUser u = new IrUser("user", "password");
		GroupWorkspace groupSpace = new GroupWorkspace("group" , "test group");
        GroupWorkspaceFolder groupFolder = new GroupWorkspaceFolder(groupSpace, u, "myFolder");
		
        groupFolder.setDescription("myDescription");
		
		
		try
		{
			groupFolder.createChild("folder1", u);
			groupFolder.createChild("FoldeR1", u);
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
	public void testAddChildren() throws IllegalFileSystemNameException
	{
		IrUser u = new IrUser("nate", "password");
		GroupWorkspace groupSpace = new GroupWorkspace("group" , "test group");
		// create the root colleciton
		GroupWorkspaceFolder groupFolder1 = new GroupWorkspaceFolder(groupSpace, u, "nates_folder");
		
		assert groupFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + groupFolder1.getLeftValue();
		assert groupFolder1.getRightValue() == 2 : "Right value should equal 2 but equals " + groupFolder1.getRightValue();
		
		assert groupFolder1.getOwner().equals(u) : "users should be the same";
		
		assert groupFolder1.getPath().equals("/") : "Path should "
			+ "equal / but equals " + groupFolder1.getPath();

		
		assert groupFolder1.getFullPath().equals("/nates_folder/") : "Path should "
			+ "equal /nates_folder/ but equals " + groupFolder1.getFullPath();
		
		// add first child
		GroupWorkspaceFolder groupFolder2 = null;
		// add second child child
		try
		{
		    groupFolder2 = groupFolder1.createChild("groupFolder2", u);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert groupFolder2.getFullPath().equals("/nates_folder/groupFolder2/") : "Path should "
			    + "equal /nates_folder/groupFolder2/ but equals " + groupFolder2.getFullPath();

		assert groupFolder2.getPath().equals("/nates_folder/") : "Path should "
			    + "equal /nates_folder/ but equals " + groupFolder2.getPath();

		
		assert groupFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + groupFolder1.getLeftValue();
		assert groupFolder1.getRightValue() == 4 : "Right value should equal 4 but equals " + groupFolder1.getRightValue();
		
		assert groupFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ groupFolder2.getLeftValue();
		
		assert groupFolder2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ groupFolder2.getRightValue();
		
		GroupWorkspaceFolder groupFolder3 = null;
		// add second child child
		try
		{
		    groupFolder3 = groupFolder1.createChild("groupFolder3",u);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}

		
		assert groupFolder3.getPath().equals("/nates_folder/") : "Path should "
			+ "equal /nates_folder/ but equals " + groupFolder3.getPath();

		assert groupFolder3.getFullPath().equals("/nates_folder/groupFolder3/") : "Path should "
			+ "equal /nates_folder/groupFolder3/ but equals " + groupFolder3.getFullPath();

		
		assert groupFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + groupFolder1.getLeftValue();
		assert groupFolder1.getRightValue() == 6 : "Right value should equal 6 but equals " + groupFolder1.getRightValue();
		
		
		assert groupFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ groupFolder2.getLeftValue();
		
		assert groupFolder2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ groupFolder2.getRightValue();
		
		assert groupFolder3.getLeftValue() == 4 : "Collection 4 left value should equal 4 but is " 
			+ groupFolder3.getLeftValue();
		
		assert groupFolder3.getRightValue() == 5 : "Collection 3 right value should equal 5 but is " 
			+ groupFolder3.getRightValue();

		GroupWorkspaceFolder personalSubFolder1 = null;
		// add sub folder
		try
		{
		    personalSubFolder1 = groupFolder2.createChild("personalSubFolder1",u);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert personalSubFolder1.getPath().equals("/nates_folder/groupFolder2/") : "Path should "
			+ "equal /nates_folder/groupFolder2/ but equals " + personalSubFolder1.getPath();

		assert personalSubFolder1.getFullPath().equals("/nates_folder/groupFolder2/personalSubFolder1/") : "Path should "
			+ "equal /nates_folder/groupFolder2/personalSubFolder1/ but equals " + personalSubFolder1.getFullPath();

		assert groupFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + groupFolder1.getLeftValue();
		assert groupFolder1.getRightValue() == 8 : "Right value should equal 8 but equals " + groupFolder1.getRightValue();
		assert groupFolder1.getOwner().equals(u);

		assert groupFolder2.getLeftValue() == 2 : "Personal Folder 2 left value should equal 2 but is " 
			+ groupFolder2.getLeftValue();
		assert groupFolder2.getOwner().equals(u);

		assert groupFolder2.getRightValue() == 5 : "Personal Folder 2 right value should equal 5 but is " 
			+ groupFolder2.getRightValue();
		
		assert personalSubFolder1.getLeftValue() == 3 : "sub Personal Folder 1 left value should equal 3 but is " 
			+ personalSubFolder1.getLeftValue();
		
		assert personalSubFolder1.getRightValue() == 4 : "Sub Personal Folder 1 right value should equal 4 but is " 
			+ personalSubFolder1.getRightValue();
		assert personalSubFolder1.getOwner().equals(u);
		
		
		assert groupFolder3.getLeftValue() == 6 : "Collection 3 left value should equal 6 but is " 
			+ groupFolder3.getLeftValue();
		
		assert groupFolder3.getRightValue() == 7 : "Collection 3 right value should equal 7 but is " 
			+ groupFolder3.getRightValue();
		assert groupFolder3.getOwner().equals(u);


		// add sub collection
		GroupWorkspaceFolder personalSubFolder2 = null;
		try
		{
		    personalSubFolder2 = groupFolder3.createChild("personalSubFolder2",u);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}

		assert personalSubFolder2.getPath().equals("/nates_folder/groupFolder3/") : "Path should "
			+ "equal /nates_folder/groupFolder3/ but equals " + personalSubFolder1.getPath();

		assert personalSubFolder2.getFullPath().equals("/nates_folder/groupFolder3/personalSubFolder2/") : "Path should "
			+ "equal /nates_folder/groupFolder3/personalSubFolder2/ but equals " + personalSubFolder1.getFullPath();

		
		assert groupFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + groupFolder1.getLeftValue();
		assert groupFolder1.getRightValue() == 10 : "Right value should equal 10 but equals " + groupFolder1.getRightValue();
		
		assert groupFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ groupFolder2.getLeftValue();
		
		assert groupFolder2.getRightValue() == 5 : "Collection 2 right value should equal 5 but is " 
			+ groupFolder2.getRightValue();
		
		assert personalSubFolder1.getLeftValue() == 3 : "sub Collection 1 left value should equal 3 but is " 
			+ personalSubFolder1.getLeftValue();
		
		assert personalSubFolder1.getRightValue() == 4 : "Sub collection 1 right value should equal 4 but is " 
			+ personalSubFolder1.getRightValue();
		
		assert groupFolder3.getLeftValue() == 6 : "Collection 3 left value should equal 6 but is " 
			+ groupFolder3.getLeftValue();
		
		assert groupFolder3.getRightValue() == 9 : "Collection 3 right value should equal 7 but is " 
			+ groupFolder3.getRightValue();

		assert personalSubFolder2.getLeftValue() == 7 : "sub Collection 2 left value should equal 7 but is " 
			+ personalSubFolder2.getLeftValue();
		
		assert personalSubFolder2.getRightValue() == 8 : "Sub collection 2 right value should equal 8 but is " 
			+ personalSubFolder2.getRightValue();
	}
	
	/**
	 * Test removing a folder. 
	 */
	public void testRemoveFolder() throws IllegalFileSystemNameException
	{
		// create the owner of the folders
		IrUser u = new IrUser("nate", "password");
		GroupWorkspace groupSpace = new GroupWorkspace("group" , "test group");
		// create the root colleciton
		GroupWorkspaceFolder groupFolder1 = new GroupWorkspaceFolder(groupSpace, u, "groupFolder1");
		
		
		GroupWorkspaceFolder groupFolder2 = null;
		GroupWorkspaceFolder groupFolder3 = null;
		GroupWorkspaceFolder personalSubFolder1 = null;
		GroupWorkspaceFolder personalSubFolder2 = null;
		try
		{
		    // add first child
		    groupFolder2 = groupFolder1.createChild("groupFolder2", u);
				
		    // add second child child
		    groupFolder3 = groupFolder1.createChild("groupFolder3",u);
			
		    // add sub collection
		    personalSubFolder1 = groupFolder2.createChild("personalSubFolder1",u);
		
		    // add sub collection
		    personalSubFolder2 = groupFolder3.createChild("personalSubFolder2",u);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		// remove the irSubColleciton1
		assert groupFolder2.removeChild(personalSubFolder1) : "personalSubFolder1 should be removed";
		
		assert personalSubFolder1.getParent() == null : "Parent Should null but is " + 
		    personalSubFolder1.getParent().toString();
		assert personalSubFolder1.isRoot() : "Should be root";

		// check re-numbering
		assert groupFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + groupFolder1.getLeftValue();
		assert groupFolder1.getRightValue() == 8 : "Right value should equal 8 but equals " + groupFolder1.getRightValue();
		
		assert groupFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ groupFolder2.getLeftValue();
		
		assert groupFolder2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ groupFolder2.getRightValue();
		
		assert groupFolder3.getLeftValue() == 4 : "Collection 3 left value should equal 4 but is " 
			+ groupFolder3.getLeftValue();
		
		assert groupFolder3.getRightValue() == 7 : "Collection 3 right value should equal 7 but is " 
			+ groupFolder3.getRightValue();

		assert personalSubFolder2.getLeftValue() == 5 : "sub Collection 2 left value should equal 5 but is " 
			+ personalSubFolder2.getLeftValue();
		
		assert personalSubFolder2.getRightValue() == 6 : "Sub collection 2 right value should equal 6 but is " 
			+ personalSubFolder2.getRightValue();
		
		// remove collection 3
		assert groupFolder1.removeChild(groupFolder3) : "Collection 3 should be removed"; 
		assert groupFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + groupFolder1.getLeftValue();
		assert groupFolder1.getRightValue() == 4 : "Right value should equal 4 but equals " + groupFolder1.getRightValue();
		
		assert groupFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ groupFolder2.getLeftValue();
		
		assert groupFolder2.getRightValue() == 3 : "Collection 2 right value should equal 5 but is " 
			+ groupFolder2.getRightValue();
		
	}
	
	
	/**
	 * Test adding children.
	 * @throws LocationAlreadyExistsException 
	 */
	public void testAddVersionedFile() throws IllegalFileSystemNameException, LocationAlreadyExistsException
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
		
        // create the first file to store in the temporary folder
		File f2 = testUtil.creatFile(directory, "testFile2", 
				"Hello  - versionedIrFile This is text in a file 2"); 

		// get the file database 
		FileDatabase fd = repo.getFileDatabase();
		
		// create a new file info container
		FileInfo fileInfo1 = fd.addFile(f, "newFile1");
		fileInfo1.setDisplayName("displayName1");
		
		// create a second file info container
		FileInfo fileInfo2 = fd.addFile(f2, "newFile2");
		fileInfo2.setDisplayName("displayName2");

		
		// create the owner of the folders
		IrUser u = new IrUser("nate", "password");

		// create a new versioned file
		VersionedFile vif = new VersionedFile(u, fileInfo1, "displayName1");
		
		GroupWorkspace groupSpace = new GroupWorkspace("group" , "test group");
		// create the root colleciton
		GroupWorkspaceFolder groupFolder1 = new GroupWorkspaceFolder(groupSpace, u, "groupFolder1");
		
		GroupWorkspaceFile gf = null;
		
		try
		{
		    gf = groupFolder1.addVersionedFile(vif);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert groupFolder1.getFile("displayName1").getVersionedFile().equals(vif) : 
			"Versioned file should be found";
		
		
		groupFolder1.removeGroupFile(gf);
		
		assert groupFolder1.getFile(gf.getName()) == null : 
			"Should not be able fo find the personal file " + gf;
		
		repoHelper.cleanUpRepository();
	
	}
	
	/**
	 * Test adding children.
	 * @throws LocationAlreadyExistsException 
	 */
	public void testMovePersonalFile() throws IllegalFileSystemNameException, LocationAlreadyExistsException
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
		
        // create the first file to store in the temporary folder
		File f2 = testUtil.creatFile(directory, "testFile2", 
				"Hello  - versionedIrFile This is text in a file 2"); 

		// get the file database 
		FileDatabase fd = repo.getFileDatabase();
		
		// create a new file info container
		FileInfo fileInfo1 = fd.addFile(f, "newFile1");
		fileInfo1.setDisplayName("displayName1");
		
		// create a second file info container
		FileInfo fileInfo2 = fd.addFile(f2, "newFile2");
		fileInfo2.setDisplayName("displayName2");

		
		// create the owner of the folders
		IrUser u = new IrUser("nate", "password");

		// create a new versioned file
		VersionedFile vif = new VersionedFile(u, fileInfo1, "displayName1");
		
		GroupWorkspace groupSpace = new GroupWorkspace("group" , "test group");

		// create the root colleciton
		GroupWorkspaceFolder groupFolder1 = new GroupWorkspaceFolder(groupSpace, u, "groupFolder1");

		GroupWorkspaceFile gf = null;
		try
		{
		    gf = groupFolder1.addVersionedFile(vif);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert groupFolder1.getFile("displayName1").getVersionedFile().equals(vif) : 
			"Versioned file should be found";
		
		GroupWorkspaceFolder groupFolder2 = new GroupWorkspaceFolder(groupSpace, u, "groupFolder2");

		try
		{
		    groupFolder2.addGroupFile(gf);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert groupFolder1.getFile(gf.getName()) == null : 
			"should not be able to find " + gf;
		
		assert groupFolder2.getFile(gf.getName()) != null : 
			"Should be able fo find the personal file " + gf;
		
		repoHelper.cleanUpRepository();
	
	}
	
	/**
	 * Test moving a personal folder.
	 */
	public void testMovePersonalFolderWithinTree() throws IllegalFileSystemNameException
	{
		// create the owner of the folders
		IrUser u = new IrUser("nate", "password");
		
		GroupWorkspace groupSpace = new GroupWorkspace("group" , "test group");

		// create the root colleciton
		GroupWorkspaceFolder groupFolder1 = new GroupWorkspaceFolder(groupSpace, u, "groupFolder1");
		
		GroupWorkspaceFolder groupFolder2 = null;
		GroupWorkspaceFolder groupFolder3 = null;
		GroupWorkspaceFolder personalSubFolder1 = null;
		GroupWorkspaceFolder personalSubFolder2 = null;
		try
		{

		    // add first child
		    groupFolder2 = groupFolder1.createChild("groupFolder2",u);
				
		    // add second child child
		    groupFolder3 = groupFolder1.createChild("groupFolder3",u);
			
		    // add sub folder
		    personalSubFolder1 = groupFolder2.createChild("personalSubFolder1",u);
		
		    // add sub folder
		    personalSubFolder2 = groupFolder3.createChild("personalSubFolder2",u);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert groupFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		groupFolder1.getLeftValue();
		assert groupFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		groupFolder1.getRightValue();
		
		assert groupFolder2.getLeftValue() == 2 : "Right value should be 2 but is " +
		groupFolder1.getLeftValue();
		assert groupFolder2.getRightValue() == 5 : "Right value sould be  5 but is " +
		groupFolder2.getRightValue();
		
		assert groupFolder3.getLeftValue() == 6 : "Right value should be 6 but is " +
		groupFolder1.getLeftValue();
		assert groupFolder3.getRightValue() == 9 : "Right value sould be 9 but is " +
		groupFolder3.getRightValue();
		
		assert personalSubFolder1.getLeftValue() == 3 : "Right value should be 3 but is " +
		personalSubFolder1.getLeftValue();
		assert personalSubFolder1.getRightValue() == 4 : "Right value sould be 4 but is " +
		personalSubFolder1.getRightValue();
		
		assert personalSubFolder2.getLeftValue() == 7 : "Right value should be 7 but is " +
		personalSubFolder2.getLeftValue();
		assert personalSubFolder2.getRightValue() == 8 : "Right value sould be 8 but is " +
		personalSubFolder2.getRightValue();
		
		try
		{
		    groupFolder1.addChild(personalSubFolder2);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert personalSubFolder2.getParent().equals(groupFolder1) :
			"Parent should be personal folder 1 but is " + personalSubFolder2.getParent();
		
		
		assert groupFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		groupFolder1.getLeftValue();
		assert groupFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		groupFolder1.getRightValue();
		
		assert groupFolder2.getLeftValue() == 2 : "Right value should be 2 but is " +
		groupFolder1.getLeftValue();
		assert groupFolder2.getRightValue() == 5 : "Right value sould be  5 but is " +
		groupFolder2.getRightValue();
		
		assert groupFolder3.getLeftValue() == 6 : "Right value should be 6 but is " +
		groupFolder1.getLeftValue();
		assert groupFolder3.getRightValue() == 7 : "Right value sould be 7 but is " +
		groupFolder3.getRightValue();
		
		assert personalSubFolder1.getLeftValue() == 3 : "Right value should be 6 but is " +
		personalSubFolder1.getLeftValue();
		assert personalSubFolder1.getRightValue() == 4 : "Right value sould be 7 but is " +
		personalSubFolder1.getRightValue();
		
		assert personalSubFolder2.getLeftValue() == 8 : "Right value should be 8 but is " +
		personalSubFolder2.getLeftValue();
		assert personalSubFolder2.getRightValue() == 9 : "Right value sould be 9 but is " +
		personalSubFolder2.getRightValue();
		
		try
		{
		    personalSubFolder2.addChild(groupFolder2);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert groupFolder2.getParent().equals(personalSubFolder2);
		
		assert groupFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		groupFolder1.getLeftValue();
		assert groupFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		groupFolder1.getRightValue();
		
		assert groupFolder2.getLeftValue() == 5 : "Right value should be 2 but is " +
		groupFolder1.getLeftValue();
		assert groupFolder2.getRightValue() == 8 : "Right value sould be  5 but is " +
		groupFolder2.getRightValue();
		
		assert groupFolder3.getLeftValue() == 2 : "Right value should be 6 but is " +
		groupFolder1.getLeftValue();
		assert groupFolder3.getRightValue() == 3 : "Right value sould be 7 but is " +
		groupFolder3.getRightValue();
		
		assert personalSubFolder1.getLeftValue() == 6 : "Right value should be 6 but is " +
		personalSubFolder1.getLeftValue();
		assert personalSubFolder1.getRightValue() == 7 : "Right value sould be 7 but is " +
		personalSubFolder1.getRightValue();
		
		assert personalSubFolder2.getLeftValue() == 4 : "Right value should be 8 but is " +
		personalSubFolder2.getLeftValue();
		assert personalSubFolder2.getRightValue() == 9 : "Right value sould be 9 but is " +
		personalSubFolder2.getRightValue();
	}
	
	/**
	 * Test moving a personal folder to the root of the person.
	 * @throws DuplicateNameException 
	 */
	public void testMovePersonalFolderToRootOfUser() throws DuplicateNameException,  IllegalFileSystemNameException
	{
		// create the owner of the folders
		IrUser u = new IrUser("nate", "password");
		
		GroupWorkspace groupSpace = new GroupWorkspace("group" , "test group");

		// create the root collection
		GroupWorkspaceFolder groupFolder1 = new GroupWorkspaceFolder(groupSpace, u, "groupFolder1");
		
		GroupWorkspaceFolder groupFolder2 = null;
		GroupWorkspaceFolder groupFolder3 = null;
		GroupWorkspaceFolder personalSubFolder1 = null;
		GroupWorkspaceFolder personalSubFolder2 = null;
		
		try
		{
		    // add first child
		    groupFolder2 = groupFolder1.createChild("groupFolder2",u);
				
		    // add second child child
		    groupFolder3 = groupFolder1.createChild("groupFolder3",u);
			
		    // add sub folder
		    personalSubFolder1 = groupFolder2.createChild("personalSubFolder1",u);
		
		    // add sub folder
		    personalSubFolder2 = groupFolder3.createChild("personalSubFolder2",u);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert groupFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		groupFolder1.getLeftValue();
		assert groupFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		groupFolder1.getRightValue();
		
		assert groupFolder2.getLeftValue() == 2 : "Right value should be 2 but is " +
		groupFolder1.getLeftValue();
		assert groupFolder2.getRightValue() == 5 : "Right value sould be  5 but is " +
		groupFolder2.getRightValue();
		
		assert groupFolder3.getLeftValue() == 6 : "Right value should be 6 but is " +
		groupFolder1.getLeftValue();
		assert groupFolder3.getRightValue() == 9 : "Right value sould be 9 but is " +
		groupFolder3.getRightValue();
		
		assert personalSubFolder1.getLeftValue() == 3 : "Right value should be 3 but is " +
		personalSubFolder1.getLeftValue();
		assert personalSubFolder1.getRightValue() == 4 : "Right value sould be 4 but is " +
		personalSubFolder1.getRightValue();
		
		assert personalSubFolder2.getLeftValue() == 7 : "Right value should be 7 but is " +
		personalSubFolder2.getLeftValue();
		assert personalSubFolder2.getRightValue() == 8 : "Right value sould be 8 but is " +
		personalSubFolder2.getRightValue();
		
		//make personal folder 2 a root folder by adding them to the user
		groupSpace.addRootFolder(groupFolder2);
		
		assert groupFolder2.getLeftValue() == 1 : "Right value should be 4 but is " 
			+ groupFolder2.getLeftValue();
		
		assert groupFolder2.getRightValue() == 4 : "Right value should be 4 but is " 
			+ groupFolder2.getRightValue();
		
		assert groupFolder2.getTreeRoot().equals(groupFolder2) : "personal folder 2 should be root but root = " + 
		groupFolder2.getTreeRoot();
		
		assert groupFolder2.getFullPath().equals("/groupFolder2/") : "Path should "
			+ "equal /groupFolder2/ but equals " + groupFolder2.getFullPath();

		assert personalSubFolder1.getLeftValue() == 2 : "Left value should be 2 but is " + personalSubFolder1.getLeftValue();
		assert personalSubFolder1.getRightValue() == 3 : "Right value should be 3 but is " + personalSubFolder1.getRightValue();
		
		assert personalSubFolder1.getFullPath().equals("/groupFolder2/personalSubFolder1/") : "Path should "
			+ "equal /groupFolder2/personalSubFolder1/ but equals " + personalSubFolder1.getFullPath();

	}
	
	/**
	 * Test re-naming folders.
	 * 
	 * @throws DuplicateNameException 
	 */
	public void testReNameFolder() throws DuplicateNameException,  IllegalFileSystemNameException
	{
		IrUser u = new IrUser("nate", "password");
		GroupWorkspace groupSpace = new GroupWorkspace("group" , "test group");

		
		// create the root colleciton
		GroupWorkspaceFolder groupFolder1 = new GroupWorkspaceFolder(groupSpace, u, "nates_folder");
		
		
		assert groupFolder1.getOwner().equals(u) : "users should be the same";
		
		assert groupFolder1.getPath().equals("/") : "Path should "
			+ "equal / but equals " + groupFolder1.getPath();

		
		assert groupFolder1.getFullPath().equals("/nates_folder/") : "Path should "
			+ "equal /nates_folder/ but equals " + groupFolder1.getFullPath();
		
		// add first child
		GroupWorkspaceFolder groupFolder2 = groupFolder1.createChild("groupFolder2",u);
		
		
		assert groupFolder2.getFullPath().equals("/nates_folder/groupFolder2/") : "Path should "
			    + "equal /nates_folder/groupFolder2/ but equals " + groupFolder2.getFullPath();

		assert groupFolder2.getPath().equals("/nates_folder/") : "Path should "
			    + "equal /nates_folder/ but equals " + groupFolder2.getPath();

		GroupWorkspaceFolder groupFolder3 = groupFolder1.createChild("groupFolder3",u);
				
		assert groupFolder3.getPath().equals("/nates_folder/") : "Path should "
			+ "equal /nates_folder/ but equals " + groupFolder3.getPath();

		assert groupFolder3.getFullPath().equals("/nates_folder/groupFolder3/") : "Path should "
			+ "equal /nates_folder/groupFolder3/ but equals " + groupFolder3.getFullPath();

		
	
		GroupWorkspaceFolder personalSubFolder1 =groupFolder2.createChild("personalSubFolder1",u);
	
		
		assert personalSubFolder1.getPath().equals("/nates_folder/groupFolder2/") : "Path should "
			+ "equal /nates_folder/groupFolder2/ but equals " + personalSubFolder1.getPath();

		assert personalSubFolder1.getFullPath().equals("/nates_folder/groupFolder2/personalSubFolder1/") : "Path should "
			+ "equal /nates_folder/groupFolder2/personalSubFolder1/ but equals " + personalSubFolder1.getFullPath();



		// add sub collection
		GroupWorkspaceFolder personalSubFolder2 = groupFolder3.createChild("personalSubFolder2",u);

		assert personalSubFolder2.getPath().equals("/nates_folder/groupFolder3/") : "Path should "
			+ "equal /nates_folder/groupFolder3/ but equals " + personalSubFolder2.getPath();

		assert personalSubFolder2.getFullPath().equals("/nates_folder/groupFolder3/personalSubFolder2/") : "Path should "
			+ "equal /nates_folder/groupFolder3/personalSubFolder2/ but equals " + personalSubFolder2.getFullPath();

		

		// rename groupFolder3
		groupFolder3.reName("new3");
		
		assert personalSubFolder2.getPath().equals("/nates_folder/new3/") : "Path should "
			+ "equal /nates_folder/new3/ but equals " + personalSubFolder2.getPath();

		assert personalSubFolder2.getFullPath().equals("/nates_folder/new3/personalSubFolder2/") : "Path should "
			+ "equal /nates_folder/new3/personalSubFolder2/ but equals " + personalSubFolder1.getFullPath();
		
		
		// rename the root folder
		groupFolder1.reName("new1");
		
		assert personalSubFolder2.getPath().equals("/new1/new3/") : "Path should "
			+ "equal /new1/new3/ but equals " + personalSubFolder2.getPath();

		assert personalSubFolder2.getFullPath().equals("/new1/new3/personalSubFolder2/") : "Path should "
			+ "equal /new1/new3/personalSubFolder2/ but equals " + personalSubFolder2.getFullPath();
	}

	
}
