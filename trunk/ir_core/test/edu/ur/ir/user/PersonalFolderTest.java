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

package edu.ur.ir.user;

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
import edu.ur.ir.user.PersonalFolder;
import edu.ur.util.FileUtil;

/**
 * Test the PersonalFolder class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class PersonalFolderTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testBasicSets()  throws IllegalFileSystemNameException
	{
		PersonalFolder personalFolder = new PersonalFolder();
		
		personalFolder.setDescription("myDescription");
		personalFolder.setName("myName");
		
		assert personalFolder.getDescription().equals("myDescription") : "Descriptions should be equal";
		assert personalFolder.getName().equals("myName") : "Names should be equal";
		
		assert personalFolder.allowsChildren() : "Default is to allow children";
	}
	
	/**
	 * Make sure an error is thrown if duplicate folders are added
	 */
	public void testDuplicateFolders()  throws IllegalFileSystemNameException
	{
		IrUser u = new IrUser("nate", "password");
        PersonalFolder personalFolder = new PersonalFolder(u, "myFolder");
		
		personalFolder.setDescription("myDescription");
		
		
		try
		{
		    personalFolder.createChild("folder1");
		    personalFolder.createChild("FoldeR1");
		    assert false : "This should fail before this with a " +
		    		"duplicate name exception";
		}
		catch(DuplicateNameException dne)
		{
			assert true : "This should be ok";
		}
	}
	
	/**
	 * Test adding children.
	 */
	public void testAddChildren() throws IllegalFileSystemNameException
	{
		IrUser u = new IrUser("nate", "password");
		// create the root colleciton
		PersonalFolder personalFolder1 = new PersonalFolder(u, "nates_folder");
		
		assert personalFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + personalFolder1.getLeftValue();
		assert personalFolder1.getRightValue() == 2 : "Right value should equal 2 but equals " + personalFolder1.getRightValue();
		
		assert personalFolder1.getOwner().equals(u) : "users should be the same";
		
		assert personalFolder1.getPath().equals("/") : "Path should "
			+ "equal / but equals " + personalFolder1.getPath();

		
		assert personalFolder1.getFullPath().equals("/nates_folder/") : "Path should "
			+ "equal /nates_folder/ but equals " + personalFolder1.getFullPath();
		
		// add first child
		PersonalFolder personalFolder2 = null;
		// add second child child
		try
		{
		    personalFolder2 = personalFolder1.createChild("personalFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert personalFolder2.getFullPath().equals("/nates_folder/personalFolder2/") : "Path should "
			    + "equal /nates_folder/personalFolder2/ but equals " + personalFolder2.getFullPath();

		assert personalFolder2.getPath().equals("/nates_folder/") : "Path should "
			    + "equal /nates_folder/ but equals " + personalFolder2.getPath();

		
		assert personalFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + personalFolder1.getLeftValue();
		assert personalFolder1.getRightValue() == 4 : "Right value should equal 4 but equals " + personalFolder1.getRightValue();
		
		assert personalFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ personalFolder2.getLeftValue();
		
		assert personalFolder2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ personalFolder2.getRightValue();
		
		PersonalFolder personalFolder3 = null;
		// add second child child
		try
		{
		    personalFolder3 = personalFolder1.createChild("personalFolder3");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}

		
		assert personalFolder3.getPath().equals("/nates_folder/") : "Path should "
			+ "equal /nates_folder/ but equals " + personalFolder3.getPath();

		assert personalFolder3.getFullPath().equals("/nates_folder/personalFolder3/") : "Path should "
			+ "equal /nates_folder/personalFolder3/ but equals " + personalFolder3.getFullPath();

		
		assert personalFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + personalFolder1.getLeftValue();
		assert personalFolder1.getRightValue() == 6 : "Right value should equal 6 but equals " + personalFolder1.getRightValue();
		
		
		assert personalFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ personalFolder2.getLeftValue();
		
		assert personalFolder2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ personalFolder2.getRightValue();
		
		assert personalFolder3.getLeftValue() == 4 : "Collection 4 left value should equal 4 but is " 
			+ personalFolder3.getLeftValue();
		
		assert personalFolder3.getRightValue() == 5 : "Collection 3 right value should equal 5 but is " 
			+ personalFolder3.getRightValue();

		PersonalFolder personalSubFolder1 = null;
		// add sub folder
		try
		{
		    personalSubFolder1 = personalFolder2.createChild("personalSubFolder1");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert personalSubFolder1.getPath().equals("/nates_folder/personalFolder2/") : "Path should "
			+ "equal /nates_folder/personalFolder2/ but equals " + personalSubFolder1.getPath();

		assert personalSubFolder1.getFullPath().equals("/nates_folder/personalFolder2/personalSubFolder1/") : "Path should "
			+ "equal /nates_folder/personalFolder2/personalSubFolder1/ but equals " + personalSubFolder1.getFullPath();

		assert personalFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + personalFolder1.getLeftValue();
		assert personalFolder1.getRightValue() == 8 : "Right value should equal 8 but equals " + personalFolder1.getRightValue();
		assert personalFolder1.getOwner().equals(u);

		assert personalFolder2.getLeftValue() == 2 : "Personal Folder 2 left value should equal 2 but is " 
			+ personalFolder2.getLeftValue();
		assert personalFolder2.getOwner().equals(u);

		assert personalFolder2.getRightValue() == 5 : "Personal Folder 2 right value should equal 5 but is " 
			+ personalFolder2.getRightValue();
		
		assert personalSubFolder1.getLeftValue() == 3 : "sub Personal Folder 1 left value should equal 3 but is " 
			+ personalSubFolder1.getLeftValue();
		
		assert personalSubFolder1.getRightValue() == 4 : "Sub Personal Folder 1 right value should equal 4 but is " 
			+ personalSubFolder1.getRightValue();
		assert personalSubFolder1.getOwner().equals(u);
		
		
		assert personalFolder3.getLeftValue() == 6 : "Collection 3 left value should equal 6 but is " 
			+ personalFolder3.getLeftValue();
		
		assert personalFolder3.getRightValue() == 7 : "Collection 3 right value should equal 7 but is " 
			+ personalFolder3.getRightValue();
		assert personalFolder3.getOwner().equals(u);


		// add sub collection
		PersonalFolder personalSubFolder2 = null;
		try
		{
		    personalSubFolder2 = personalFolder3.createChild("personalSubFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}

		assert personalSubFolder2.getPath().equals("/nates_folder/personalFolder3/") : "Path should "
			+ "equal /nates_folder/personalFolder3/ but equals " + personalSubFolder1.getPath();

		assert personalSubFolder2.getFullPath().equals("/nates_folder/personalFolder3/personalSubFolder2/") : "Path should "
			+ "equal /nates_folder/personalFolder3/personalSubFolder2/ but equals " + personalSubFolder1.getFullPath();

		
		assert personalFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + personalFolder1.getLeftValue();
		assert personalFolder1.getRightValue() == 10 : "Right value should equal 10 but equals " + personalFolder1.getRightValue();
		
		assert personalFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ personalFolder2.getLeftValue();
		
		assert personalFolder2.getRightValue() == 5 : "Collection 2 right value should equal 5 but is " 
			+ personalFolder2.getRightValue();
		
		assert personalSubFolder1.getLeftValue() == 3 : "sub Collection 1 left value should equal 3 but is " 
			+ personalSubFolder1.getLeftValue();
		
		assert personalSubFolder1.getRightValue() == 4 : "Sub collection 1 right value should equal 4 but is " 
			+ personalSubFolder1.getRightValue();
		
		assert personalFolder3.getLeftValue() == 6 : "Collection 3 left value should equal 6 but is " 
			+ personalFolder3.getLeftValue();
		
		assert personalFolder3.getRightValue() == 9 : "Collection 3 right value should equal 7 but is " 
			+ personalFolder3.getRightValue();

		assert personalSubFolder2.getLeftValue() == 7 : "sub Collection 2 left value should equal 7 but is " 
			+ personalSubFolder2.getLeftValue();
		
		assert personalSubFolder2.getRightValue() == 8 : "Sub collection 2 right value should equal 8 but is " 
			+ personalSubFolder2.getRightValue();
	}

	/**
	 * Test removing a personal folder. 
	 */
	public void testRemovePersonalFolder() throws IllegalFileSystemNameException
	{
		// create the owner of the folders
		IrUser u = new IrUser("nate", "password");
		
		// create the root colleciton
		PersonalFolder personalFolder1 = new PersonalFolder(u, "personalFolder1");
		
		
		PersonalFolder personalFolder2 = null;
		PersonalFolder personalFolder3 = null;
		PersonalFolder personalSubFolder1 = null;
		PersonalFolder personalSubFolder2 = null;
		try
		{
		    // add first child
		    personalFolder2 = personalFolder1.createChild("personalFolder2");
				
		    // add second child child
		    personalFolder3 = personalFolder1.createChild("personalFolder3");
			
		    // add sub collection
		    personalSubFolder1 = personalFolder2.createChild("personalSubFolder1");
		
		    // add sub collection
		    personalSubFolder2 = personalFolder3.createChild("personalSubFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		// remove the irSubColleciton1
		assert personalFolder2.removeChild(personalSubFolder1) : "personalSubFolder1 should be removed";
		
		assert personalSubFolder1.getParent() == null : "Parent Should null but is " + 
		    personalSubFolder1.getParent().toString();
		assert personalSubFolder1.isRoot() : "Should be root";

		// check re-numbering
		assert personalFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + personalFolder1.getLeftValue();
		assert personalFolder1.getRightValue() == 8 : "Right value should equal 8 but equals " + personalFolder1.getRightValue();
		
		assert personalFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ personalFolder2.getLeftValue();
		
		assert personalFolder2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ personalFolder2.getRightValue();
		
		assert personalFolder3.getLeftValue() == 4 : "Collection 3 left value should equal 4 but is " 
			+ personalFolder3.getLeftValue();
		
		assert personalFolder3.getRightValue() == 7 : "Collection 3 right value should equal 7 but is " 
			+ personalFolder3.getRightValue();

		assert personalSubFolder2.getLeftValue() == 5 : "sub Collection 2 left value should equal 5 but is " 
			+ personalSubFolder2.getLeftValue();
		
		assert personalSubFolder2.getRightValue() == 6 : "Sub collection 2 right value should equal 6 but is " 
			+ personalSubFolder2.getRightValue();
		
		// remove collection 3
		assert personalFolder1.removeChild(personalFolder3) : "Collection 3 should be removed"; 
		assert personalFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + personalFolder1.getLeftValue();
		assert personalFolder1.getRightValue() == 4 : "Right value should equal 4 but equals " + personalFolder1.getRightValue();
		
		assert personalFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ personalFolder2.getLeftValue();
		
		assert personalFolder2.getRightValue() == 3 : "Collection 2 right value should equal 5 but is " 
			+ personalFolder2.getRightValue();
		
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
		
		
		// create the root colleciton
		PersonalFolder personalFolder1 = new PersonalFolder(u, "personalFolder1");
		
		PersonalFile pf = null;
		
		try
		{
		    pf = personalFolder1.addVersionedFile(vif);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert personalFolder1.getFile("displayName1").getVersionedFile().equals(vif) : 
			"Versioned file should be found";
		
		
		personalFolder1.removePersonalFile(pf);
		
		assert personalFolder1.getFile(pf.getName()) == null : 
			"Should not be able fo find the personal file " + pf;
		
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
		
		
		// create the root colleciton
		PersonalFolder personalFolder1 = new PersonalFolder(u, "personalFolder1");

		PersonalFile pf = null;
		try
		{
		    pf = personalFolder1.addVersionedFile(vif);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert personalFolder1.getFile("displayName1").getVersionedFile().equals(vif) : 
			"Versioned file should be found";
		
		PersonalFolder personalFolder2 = new PersonalFolder(u, "personalFolder2");

		try
		{
		    personalFolder2.addPersonalFile(pf);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert personalFolder1.getFile(pf.getName()) == null : 
			"should not be able to find " + pf;
		
		assert personalFolder2.getFile(pf.getName()) != null : 
			"Should be able fo find the personal file " + pf;
		
		repoHelper.cleanUpRepository();
	
	}
	
	/**
	 * Test moving a personal folder.
	 */
	public void testMovePersonalFolderWithinTree() throws IllegalFileSystemNameException
	{
		// create the owner of the folders
		IrUser u = new IrUser("nate", "password");
		
		// create the root colleciton
		PersonalFolder personalFolder1 = new PersonalFolder(u, "personalFolder1");
		
		PersonalFolder personalFolder2 = null;
		PersonalFolder personalFolder3 = null;
		PersonalFolder personalSubFolder1 = null;
		PersonalFolder personalSubFolder2 = null;
		try
		{

		    // add first child
		    personalFolder2 = personalFolder1.createChild("personalFolder2");
				
		    // add second child child
		    personalFolder3 = personalFolder1.createChild("personalFolder3");
			
		    // add sub folder
		    personalSubFolder1 = personalFolder2.createChild("personalSubFolder1");
		
		    // add sub folder
		    personalSubFolder2 = personalFolder3.createChild("personalSubFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert personalFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		personalFolder1.getLeftValue();
		assert personalFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		personalFolder1.getRightValue();
		
		assert personalFolder2.getLeftValue() == 2 : "Right value should be 2 but is " +
		personalFolder1.getLeftValue();
		assert personalFolder2.getRightValue() == 5 : "Right value sould be  5 but is " +
		personalFolder2.getRightValue();
		
		assert personalFolder3.getLeftValue() == 6 : "Right value should be 6 but is " +
		personalFolder1.getLeftValue();
		assert personalFolder3.getRightValue() == 9 : "Right value sould be 9 but is " +
		personalFolder3.getRightValue();
		
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
		    personalFolder1.addChild(personalSubFolder2);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert personalSubFolder2.getParent().equals(personalFolder1) :
			"Parent should be personal folder 1 but is " + personalSubFolder2.getParent();
		
		
		assert personalFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		personalFolder1.getLeftValue();
		assert personalFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		personalFolder1.getRightValue();
		
		assert personalFolder2.getLeftValue() == 2 : "Right value should be 2 but is " +
		personalFolder1.getLeftValue();
		assert personalFolder2.getRightValue() == 5 : "Right value sould be  5 but is " +
		personalFolder2.getRightValue();
		
		assert personalFolder3.getLeftValue() == 6 : "Right value should be 6 but is " +
		personalFolder1.getLeftValue();
		assert personalFolder3.getRightValue() == 7 : "Right value sould be 7 but is " +
		personalFolder3.getRightValue();
		
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
		    personalSubFolder2.addChild(personalFolder2);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert personalFolder2.getParent().equals(personalSubFolder2);
		
		assert personalFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		personalFolder1.getLeftValue();
		assert personalFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		personalFolder1.getRightValue();
		
		assert personalFolder2.getLeftValue() == 5 : "Right value should be 2 but is " +
		personalFolder1.getLeftValue();
		assert personalFolder2.getRightValue() == 8 : "Right value sould be  5 but is " +
		personalFolder2.getRightValue();
		
		assert personalFolder3.getLeftValue() == 2 : "Right value should be 6 but is " +
		personalFolder1.getLeftValue();
		assert personalFolder3.getRightValue() == 3 : "Right value sould be 7 but is " +
		personalFolder3.getRightValue();
		
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
		
		// create the root collection
		PersonalFolder personalFolder1 = new PersonalFolder(u, "personalFolder1");
		
		PersonalFolder personalFolder2 = null;
		PersonalFolder personalFolder3 = null;
		PersonalFolder personalSubFolder1 = null;
		PersonalFolder personalSubFolder2 = null;
		
		try
		{
		    // add first child
		    personalFolder2 = personalFolder1.createChild("personalFolder2");
				
		    // add second child child
		    personalFolder3 = personalFolder1.createChild("personalFolder3");
			
		    // add sub folder
		    personalSubFolder1 = personalFolder2.createChild("personalSubFolder1");
		
		    // add sub folder
		    personalSubFolder2 = personalFolder3.createChild("personalSubFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert personalFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		personalFolder1.getLeftValue();
		assert personalFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		personalFolder1.getRightValue();
		
		assert personalFolder2.getLeftValue() == 2 : "Right value should be 2 but is " +
		personalFolder1.getLeftValue();
		assert personalFolder2.getRightValue() == 5 : "Right value sould be  5 but is " +
		personalFolder2.getRightValue();
		
		assert personalFolder3.getLeftValue() == 6 : "Right value should be 6 but is " +
		personalFolder1.getLeftValue();
		assert personalFolder3.getRightValue() == 9 : "Right value sould be 9 but is " +
		personalFolder3.getRightValue();
		
		assert personalSubFolder1.getLeftValue() == 3 : "Right value should be 3 but is " +
		personalSubFolder1.getLeftValue();
		assert personalSubFolder1.getRightValue() == 4 : "Right value sould be 4 but is " +
		personalSubFolder1.getRightValue();
		
		assert personalSubFolder2.getLeftValue() == 7 : "Right value should be 7 but is " +
		personalSubFolder2.getLeftValue();
		assert personalSubFolder2.getRightValue() == 8 : "Right value sould be 8 but is " +
		personalSubFolder2.getRightValue();
		
		//make personal folder 2 a root folder by adding them to the user
		u.addRootFolder(personalFolder2);
		
		assert personalFolder2.getLeftValue() == 1 : "Right value should be 4 but is " 
			+ personalFolder2.getLeftValue();
		
		assert personalFolder2.getRightValue() == 4 : "Right value should be 4 but is " 
			+ personalFolder2.getRightValue();
		
		assert personalFolder2.getTreeRoot().equals(personalFolder2) : "personal folder 2 should be root but root = " + 
		personalFolder2.getTreeRoot();
		
		assert personalFolder2.getFullPath().equals("/personalFolder2/") : "Path should "
			+ "equal /personalFolder2/ but equals " + personalFolder2.getFullPath();

		assert personalSubFolder1.getLeftValue() == 2 : "Left value should be 2 but is " + personalSubFolder1.getLeftValue();
		assert personalSubFolder1.getRightValue() == 3 : "Right value should be 3 but is " + personalSubFolder1.getRightValue();
		
		assert personalSubFolder1.getFullPath().equals("/personalFolder2/personalSubFolder1/") : "Path should "
			+ "equal /personalFolder2/personalSubFolder1/ but equals " + personalSubFolder1.getFullPath();

	}
	
	/**
	 * Test re-naming folders.
	 * 
	 * @throws DuplicateNameException 
	 */
	public void testReNameFolder() throws DuplicateNameException,  IllegalFileSystemNameException
	{
		IrUser u = new IrUser("nate", "password");
		// create the root colleciton
		PersonalFolder personalFolder1 = new PersonalFolder(u, "nates_folder");
		
		
		assert personalFolder1.getOwner().equals(u) : "users should be the same";
		
		assert personalFolder1.getPath().equals("/") : "Path should "
			+ "equal / but equals " + personalFolder1.getPath();

		
		assert personalFolder1.getFullPath().equals("/nates_folder/") : "Path should "
			+ "equal /nates_folder/ but equals " + personalFolder1.getFullPath();
		
		// add first child
		PersonalFolder personalFolder2 = personalFolder1.createChild("personalFolder2");
		
		
		assert personalFolder2.getFullPath().equals("/nates_folder/personalFolder2/") : "Path should "
			    + "equal /nates_folder/personalFolder2/ but equals " + personalFolder2.getFullPath();

		assert personalFolder2.getPath().equals("/nates_folder/") : "Path should "
			    + "equal /nates_folder/ but equals " + personalFolder2.getPath();

		PersonalFolder personalFolder3 = personalFolder1.createChild("personalFolder3");
				
		assert personalFolder3.getPath().equals("/nates_folder/") : "Path should "
			+ "equal /nates_folder/ but equals " + personalFolder3.getPath();

		assert personalFolder3.getFullPath().equals("/nates_folder/personalFolder3/") : "Path should "
			+ "equal /nates_folder/personalFolder3/ but equals " + personalFolder3.getFullPath();

		
	
		PersonalFolder personalSubFolder1 =personalFolder2.createChild("personalSubFolder1");
	
		
		assert personalSubFolder1.getPath().equals("/nates_folder/personalFolder2/") : "Path should "
			+ "equal /nates_folder/personalFolder2/ but equals " + personalSubFolder1.getPath();

		assert personalSubFolder1.getFullPath().equals("/nates_folder/personalFolder2/personalSubFolder1/") : "Path should "
			+ "equal /nates_folder/personalFolder2/personalSubFolder1/ but equals " + personalSubFolder1.getFullPath();



		// add sub collection
		PersonalFolder personalSubFolder2 = personalFolder3.createChild("personalSubFolder2");

		assert personalSubFolder2.getPath().equals("/nates_folder/personalFolder3/") : "Path should "
			+ "equal /nates_folder/personalFolder3/ but equals " + personalSubFolder2.getPath();

		assert personalSubFolder2.getFullPath().equals("/nates_folder/personalFolder3/personalSubFolder2/") : "Path should "
			+ "equal /nates_folder/personalFolder3/personalSubFolder2/ but equals " + personalSubFolder2.getFullPath();

		

		// rename personalFolder3
		personalFolder3.reName("new3");
		
		assert personalSubFolder2.getPath().equals("/nates_folder/new3/") : "Path should "
			+ "equal /nates_folder/new3/ but equals " + personalSubFolder2.getPath();

		assert personalSubFolder2.getFullPath().equals("/nates_folder/new3/personalSubFolder2/") : "Path should "
			+ "equal /nates_folder/new3/personalSubFolder2/ but equals " + personalSubFolder1.getFullPath();
		
		
		// rename the root folder
		personalFolder1.reName("new1");
		
		assert personalSubFolder2.getPath().equals("/new1/new3/") : "Path should "
			+ "equal /new1/new3/ but equals " + personalSubFolder2.getPath();

		assert personalSubFolder2.getFullPath().equals("/new1/new3/personalSubFolder2/") : "Path should "
			+ "equal /new1/new3/personalSubFolder2/ but equals " + personalSubFolder2.getFullPath();
	}

	
}
