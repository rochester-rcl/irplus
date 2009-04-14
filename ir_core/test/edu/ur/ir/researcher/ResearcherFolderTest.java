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

package edu.ur.ir.researcher;

import java.io.File;
import java.util.Properties;

import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.test.helper.PropertiesLoader;
import edu.ur.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.util.FileUtil;

/**
 * Test the ResearcherFolder class
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ResearcherFolderTest {
	
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
		ResearcherFolder ResearcherFolder = new ResearcherFolder();
		
		ResearcherFolder.setDescription("myDescription");
		ResearcherFolder.setName("myName");
		
		assert ResearcherFolder.getDescription().equals("myDescription") : "Descriptions should be equal";
		assert ResearcherFolder.getName().equals("myName") : "Names should be equal";
		
		assert ResearcherFolder.allowsChildren() : "Default is to allow children";
	}
	
	/**
	 * Make sure an error is thrown if duplicate folders are added
	 */
	public void testDuplicateFolders()
	{
		IrUser u = new IrUser("nate", "password");
		
		Researcher r = new Researcher(u);
        ResearcherFolder ResearcherFolder = new ResearcherFolder(r, "myFolder");
		
		ResearcherFolder.setDescription("myDescription");
		
		
		try
		{
		    ResearcherFolder.createChild("folder1");
		    ResearcherFolder.createChild("FoldeR1");
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
	public void testAddChildren()
	{
		IrUser u = new IrUser("nate", "password");
		Researcher r = new Researcher(u);
		
		// create the root collection
		ResearcherFolder ResearcherFolder1 = new ResearcherFolder(r, "nates_folder");
		
		assert ResearcherFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + ResearcherFolder1.getLeftValue();
		assert ResearcherFolder1.getRightValue() == 2 : "Right value should equal 2 but equals " + ResearcherFolder1.getRightValue();
		
		assert ResearcherFolder1.getResearcher().equals(r) : "Researchers should be the same";
		
		assert ResearcherFolder1.getPath().equals("/") : "Path should "
			+ "equal / but equals " + ResearcherFolder1.getPath();

		
		assert ResearcherFolder1.getFullPath().equals("/nates_folder/") : "Path should "
			+ "equal /nates_folder/ but equals " + ResearcherFolder1.getFullPath();
		
		// add first child
		ResearcherFolder ResearcherFolder2 = null;
		// add second child child
		try
		{
		    ResearcherFolder2 = ResearcherFolder1.createChild("ResearcherFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert ResearcherFolder2.getFullPath().equals("/nates_folder/ResearcherFolder2/") : "Path should "
			    + "equal /nates_folder/ResearcherFolder2/ but equals " + ResearcherFolder2.getFullPath();

		assert ResearcherFolder2.getPath().equals("/nates_folder/") : "Path should "
			    + "equal /nates_folder/ but equals " + ResearcherFolder2.getPath();

		
		assert ResearcherFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + ResearcherFolder1.getLeftValue();
		assert ResearcherFolder1.getRightValue() == 4 : "Right value should equal 4 but equals " + ResearcherFolder1.getRightValue();
		
		assert ResearcherFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ ResearcherFolder2.getLeftValue();
		
		assert ResearcherFolder2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ ResearcherFolder2.getRightValue();
		
		ResearcherFolder ResearcherFolder3 = null;
		// add second child child
		try
		{
		    ResearcherFolder3 = ResearcherFolder1.createChild("ResearcherFolder3");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}

		
		assert ResearcherFolder3.getPath().equals("/nates_folder/") : "Path should "
			+ "equal /nates_folder/ but equals " + ResearcherFolder3.getPath();

		assert ResearcherFolder3.getFullPath().equals("/nates_folder/ResearcherFolder3/") : "Path should "
			+ "equal /nates_folder/ResearcherFolder3/ but equals " + ResearcherFolder3.getFullPath();

		
		assert ResearcherFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + ResearcherFolder1.getLeftValue();
		assert ResearcherFolder1.getRightValue() == 6 : "Right value should equal 6 but equals " + ResearcherFolder1.getRightValue();
		
		
		assert ResearcherFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ ResearcherFolder2.getLeftValue();
		
		assert ResearcherFolder2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ ResearcherFolder2.getRightValue();
		
		assert ResearcherFolder3.getLeftValue() == 4 : "Collection 4 left value should equal 4 but is " 
			+ ResearcherFolder3.getLeftValue();
		
		assert ResearcherFolder3.getRightValue() == 5 : "Collection 3 right value should equal 5 but is " 
			+ ResearcherFolder3.getRightValue();

		ResearcherFolder ResearcherSubFolder1 = null;
		// add sub folder
		try
		{
		    ResearcherSubFolder1 = ResearcherFolder2.createChild("ResearcherSubFolder1");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert ResearcherSubFolder1.getPath().equals("/nates_folder/ResearcherFolder2/") : "Path should "
			+ "equal /nates_folder/ResearcherFolder2/ but equals " + ResearcherSubFolder1.getPath();

		assert ResearcherSubFolder1.getFullPath().equals("/nates_folder/ResearcherFolder2/ResearcherSubFolder1/") : "Path should "
			+ "equal /nates_folder/ResearcherFolder2/ResearcherSubFolder1/ but equals " + ResearcherSubFolder1.getFullPath();

		assert ResearcherFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + ResearcherFolder1.getLeftValue();
		assert ResearcherFolder1.getRightValue() == 8 : "Right value should equal 8 but equals " + ResearcherFolder1.getRightValue();
		assert ResearcherFolder1.getResearcher().equals(r);

		assert ResearcherFolder2.getLeftValue() == 2 : "Researcher Folder 2 left value should equal 2 but is " 
			+ ResearcherFolder2.getLeftValue();
		assert ResearcherFolder2.getResearcher().equals(r);

		assert ResearcherFolder2.getRightValue() == 5 : "Researcher Folder 2 right value should equal 5 but is " 
			+ ResearcherFolder2.getRightValue();
		
		assert ResearcherSubFolder1.getLeftValue() == 3 : "sub Researcher Folder 1 left value should equal 3 but is " 
			+ ResearcherSubFolder1.getLeftValue();
		
		assert ResearcherSubFolder1.getRightValue() == 4 : "Sub Researcher Folder 1 right value should equal 4 but is " 
			+ ResearcherSubFolder1.getRightValue();
		assert ResearcherSubFolder1.getResearcher().equals(r);
		
		
		assert ResearcherFolder3.getLeftValue() == 6 : "Collection 3 left value should equal 6 but is " 
			+ ResearcherFolder3.getLeftValue();
		
		assert ResearcherFolder3.getRightValue() == 7 : "Collection 3 right value should equal 7 but is " 
			+ ResearcherFolder3.getRightValue();
		assert ResearcherFolder3.getResearcher().equals(r);


		// add sub collection
		ResearcherFolder ResearcherSubFolder2 = null;
		try
		{
		    ResearcherSubFolder2 = ResearcherFolder3.createChild("ResearcherSubFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}

		assert ResearcherSubFolder2.getPath().equals("/nates_folder/ResearcherFolder3/") : "Path should "
			+ "equal /nates_folder/ResearcherFolder3/ but equals " + ResearcherSubFolder1.getPath();

		assert ResearcherSubFolder2.getFullPath().equals("/nates_folder/ResearcherFolder3/ResearcherSubFolder2/") : "Path should "
			+ "equal /nates_folder/ResearcherFolder3/ResearcherSubFolder2/ but equals " + ResearcherSubFolder1.getFullPath();

		
		assert ResearcherFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + ResearcherFolder1.getLeftValue();
		assert ResearcherFolder1.getRightValue() == 10 : "Right value should equal 10 but equals " + ResearcherFolder1.getRightValue();
		
		assert ResearcherFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ ResearcherFolder2.getLeftValue();
		
		assert ResearcherFolder2.getRightValue() == 5 : "Collection 2 right value should equal 5 but is " 
			+ ResearcherFolder2.getRightValue();
		
		assert ResearcherSubFolder1.getLeftValue() == 3 : "sub Collection 1 left value should equal 3 but is " 
			+ ResearcherSubFolder1.getLeftValue();
		
		assert ResearcherSubFolder1.getRightValue() == 4 : "Sub collection 1 right value should equal 4 but is " 
			+ ResearcherSubFolder1.getRightValue();
		
		assert ResearcherFolder3.getLeftValue() == 6 : "Collection 3 left value should equal 6 but is " 
			+ ResearcherFolder3.getLeftValue();
		
		assert ResearcherFolder3.getRightValue() == 9 : "Collection 3 right value should equal 7 but is " 
			+ ResearcherFolder3.getRightValue();

		assert ResearcherSubFolder2.getLeftValue() == 7 : "sub Collection 2 left value should equal 7 but is " 
			+ ResearcherSubFolder2.getLeftValue();
		
		assert ResearcherSubFolder2.getRightValue() == 8 : "Sub collection 2 right value should equal 8 but is " 
			+ ResearcherSubFolder2.getRightValue();
	}

	/**
	 * Test removing a Researcher folder. 
	 */
	public void testRemoveResearcherFolder()
	{
		// create the owner of the folders
		IrUser u = new IrUser("nate", "password");
		
		Researcher r = new Researcher(u);
		
		// create the root colleciton
		ResearcherFolder ResearcherFolder1 = new ResearcherFolder(r, "ResearcherFolder1");
		
		
		ResearcherFolder ResearcherFolder2 = null;
		ResearcherFolder ResearcherFolder3 = null;
		ResearcherFolder ResearcherSubFolder1 = null;
		ResearcherFolder ResearcherSubFolder2 = null;
		try
		{
		    // add first child
		    ResearcherFolder2 = ResearcherFolder1.createChild("ResearcherFolder2");
				
		    // add second child child
		    ResearcherFolder3 = ResearcherFolder1.createChild("ResearcherFolder3");
			
		    // add sub collection
		    ResearcherSubFolder1 = ResearcherFolder2.createChild("ResearcherSubFolder1");
		
		    // add sub collection
		    ResearcherSubFolder2 = ResearcherFolder3.createChild("ResearcherSubFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		// remove the irSubColleciton1
		assert ResearcherFolder2.removeChild(ResearcherSubFolder1) : "ResearcherSubFolder1 should be removed";
		
		assert ResearcherSubFolder1.getParent() == null : "Parent Should null but is " + 
		    ResearcherSubFolder1.getParent().toString();
		assert ResearcherSubFolder1.isRoot() : "Should be root";

		// check re-numbering
		assert ResearcherFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + ResearcherFolder1.getLeftValue();
		assert ResearcherFolder1.getRightValue() == 8 : "Right value should equal 8 but equals " + ResearcherFolder1.getRightValue();
		
		assert ResearcherFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ ResearcherFolder2.getLeftValue();
		
		assert ResearcherFolder2.getRightValue() == 3 : "Collection 2 right value should equal 3 but is " 
			+ ResearcherFolder2.getRightValue();
		
		assert ResearcherFolder3.getLeftValue() == 4 : "Collection 3 left value should equal 4 but is " 
			+ ResearcherFolder3.getLeftValue();
		
		assert ResearcherFolder3.getRightValue() == 7 : "Collection 3 right value should equal 7 but is " 
			+ ResearcherFolder3.getRightValue();

		assert ResearcherSubFolder2.getLeftValue() == 5 : "sub Collection 2 left value should equal 5 but is " 
			+ ResearcherSubFolder2.getLeftValue();
		
		assert ResearcherSubFolder2.getRightValue() == 6 : "Sub collection 2 right value should equal 6 but is " 
			+ ResearcherSubFolder2.getRightValue();
		
		// remove collection 3
		assert ResearcherFolder1.removeChild(ResearcherFolder3) : "Collection 3 should be removed"; 
		assert ResearcherFolder1.getLeftValue() == 1 : "Left value should equal 1 but equals " + ResearcherFolder1.getLeftValue();
		assert ResearcherFolder1.getRightValue() == 4 : "Right value should equal 4 but equals " + ResearcherFolder1.getRightValue();
		
		assert ResearcherFolder2.getLeftValue() == 2 : "Collection 2 left value should equal 2 but is " 
			+ ResearcherFolder2.getLeftValue();
		
		assert ResearcherFolder2.getRightValue() == 3 : "Collection 2 right value should equal 5 but is " 
			+ ResearcherFolder2.getRightValue();
		
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
		
		Researcher r = new Researcher(u);

		// create a new versioned file
		IrFile irf = new IrFile(fileInfo1, "displayName1");
		
		
		// create the root colleciton
		ResearcherFolder ResearcherFolder1 = new ResearcherFolder(r, "ResearcherFolder1");
		
		ResearcherFile rf = null;
		
		try
		{
		    rf = ResearcherFolder1.addFile(irf);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert ResearcherFolder1.getResearcherFile("displayName1").equals(rf) : 
			"Ir file should be found";
		
		
		ResearcherFolder1.removeResearcherFile(rf);
		
		assert ResearcherFolder1.getResearcherFile(rf.getName()) == null : 
			"Should not be able fo find the Researcher file " + rf;
		
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
		
		Researcher r = new Researcher(u);

		// create a new versioned file
		IrFile irf = new IrFile(fileInfo1, "displayName1");
		
		
		// create the root colleciton
		ResearcherFolder ResearcherFolder1 = new ResearcherFolder(r, "ResearcherFolder1");

		ResearcherFile rf = null;
		try
		{
		    rf = ResearcherFolder1.addFile(irf);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert ResearcherFolder1.getResearcherFile("displayName1").equals(rf) : 
			"Ir file should be found";
		
		ResearcherFolder ResearcherFolder2 = new ResearcherFolder(r, "ResearcherFolder2");

		try
		{
		    ResearcherFolder2.addResearcherFile(rf);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert ResearcherFolder1.getResearcherFile(rf.getName()) == null : 
			"should not be able to find " + rf;
		
		assert ResearcherFolder2.getResearcherFile(rf.getName()) != null : 
			"Should be able fo find the Researcher file " + rf;
		
		repoHelper.cleanUpRepository();
	
	}
	
	/**
	 * Test moving a Researcher folder.
	 */
	public void testMoveResearcherFolderWithinTree()
	{
		// create the owner of the folders
		IrUser u = new IrUser("nate", "password");
		
		Researcher r = new Researcher(u);
		
		// create the root colleciton
		ResearcherFolder ResearcherFolder1 = new ResearcherFolder(r, "ResearcherFolder1");
		
		ResearcherFolder ResearcherFolder2 = null;
		ResearcherFolder ResearcherFolder3 = null;
		ResearcherFolder ResearcherSubFolder1 = null;
		ResearcherFolder ResearcherSubFolder2 = null;
		try
		{

		    // add first child
		    ResearcherFolder2 = ResearcherFolder1.createChild("ResearcherFolder2");
				
		    // add second child child
		    ResearcherFolder3 = ResearcherFolder1.createChild("ResearcherFolder3");
			
		    // add sub folder
		    ResearcherSubFolder1 = ResearcherFolder2.createChild("ResearcherSubFolder1");
		
		    // add sub folder
		    ResearcherSubFolder2 = ResearcherFolder3.createChild("ResearcherSubFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert ResearcherFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		ResearcherFolder1.getLeftValue();
		assert ResearcherFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		ResearcherFolder1.getRightValue();
		
		assert ResearcherFolder2.getLeftValue() == 2 : "Right value should be 2 but is " +
		ResearcherFolder1.getLeftValue();
		assert ResearcherFolder2.getRightValue() == 5 : "Right value sould be  5 but is " +
		ResearcherFolder2.getRightValue();
		
		assert ResearcherFolder3.getLeftValue() == 6 : "Right value should be 6 but is " +
		ResearcherFolder1.getLeftValue();
		assert ResearcherFolder3.getRightValue() == 9 : "Right value sould be 9 but is " +
		ResearcherFolder3.getRightValue();
		
		assert ResearcherSubFolder1.getLeftValue() == 3 : "Right value should be 3 but is " +
		ResearcherSubFolder1.getLeftValue();
		assert ResearcherSubFolder1.getRightValue() == 4 : "Right value sould be 4 but is " +
		ResearcherSubFolder1.getRightValue();
		
		assert ResearcherSubFolder2.getLeftValue() == 7 : "Right value should be 7 but is " +
		ResearcherSubFolder2.getLeftValue();
		assert ResearcherSubFolder2.getRightValue() == 8 : "Right value sould be 8 but is " +
		ResearcherSubFolder2.getRightValue();
		
		try
		{
		    ResearcherFolder1.addChild(ResearcherSubFolder2);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert ResearcherSubFolder2.getParent().equals(ResearcherFolder1) :
			"Parent should be Researcher folder 1 but is " + ResearcherSubFolder2.getParent();
		
		
		assert ResearcherFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		ResearcherFolder1.getLeftValue();
		assert ResearcherFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		ResearcherFolder1.getRightValue();
		
		assert ResearcherFolder2.getLeftValue() == 2 : "Right value should be 2 but is " +
		ResearcherFolder1.getLeftValue();
		assert ResearcherFolder2.getRightValue() == 5 : "Right value sould be  5 but is " +
		ResearcherFolder2.getRightValue();
		
		assert ResearcherFolder3.getLeftValue() == 6 : "Right value should be 6 but is " +
		ResearcherFolder1.getLeftValue();
		assert ResearcherFolder3.getRightValue() == 7 : "Right value sould be 7 but is " +
		ResearcherFolder3.getRightValue();
		
		assert ResearcherSubFolder1.getLeftValue() == 3 : "Right value should be 6 but is " +
		ResearcherSubFolder1.getLeftValue();
		assert ResearcherSubFolder1.getRightValue() == 4 : "Right value sould be 7 but is " +
		ResearcherSubFolder1.getRightValue();
		
		assert ResearcherSubFolder2.getLeftValue() == 8 : "Right value should be 8 but is " +
		ResearcherSubFolder2.getLeftValue();
		assert ResearcherSubFolder2.getRightValue() == 9 : "Right value sould be 9 but is " +
		ResearcherSubFolder2.getRightValue();
		
		try
		{
		    ResearcherSubFolder2.addChild(ResearcherFolder2);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert ResearcherFolder2.getParent().equals(ResearcherSubFolder2);
		
		assert ResearcherFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		ResearcherFolder1.getLeftValue();
		assert ResearcherFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		ResearcherFolder1.getRightValue();
		
		assert ResearcherFolder2.getLeftValue() == 5 : "Right value should be 2 but is " +
		ResearcherFolder1.getLeftValue();
		assert ResearcherFolder2.getRightValue() == 8 : "Right value sould be  5 but is " +
		ResearcherFolder2.getRightValue();
		
		assert ResearcherFolder3.getLeftValue() == 2 : "Right value should be 6 but is " +
		ResearcherFolder1.getLeftValue();
		assert ResearcherFolder3.getRightValue() == 3 : "Right value sould be 7 but is " +
		ResearcherFolder3.getRightValue();
		
		assert ResearcherSubFolder1.getLeftValue() == 6 : "Right value should be 6 but is " +
		ResearcherSubFolder1.getLeftValue();
		assert ResearcherSubFolder1.getRightValue() == 7 : "Right value sould be 7 but is " +
		ResearcherSubFolder1.getRightValue();
		
		assert ResearcherSubFolder2.getLeftValue() == 4 : "Right value should be 8 but is " +
		ResearcherSubFolder2.getLeftValue();
		assert ResearcherSubFolder2.getRightValue() == 9 : "Right value sould be 9 but is " +
		ResearcherSubFolder2.getRightValue();
	}
	
	/**
	 * Test moving a Researcher folder to the root of the person.
	 * @throws DuplicateNameException 
	 */
	public void testMoveResearcherFolderToRootOfUser() throws DuplicateNameException
	{
		// create the owner of the folders
		IrUser u = new IrUser("nate", "password");
		Researcher r = new Researcher(u);
		
		// create the root collection
		ResearcherFolder ResearcherFolder1 = new ResearcherFolder(r, "ResearcherFolder1");
		
		ResearcherFolder ResearcherFolder2 = null;
		ResearcherFolder ResearcherFolder3 = null;
		ResearcherFolder ResearcherSubFolder1 = null;
		ResearcherFolder ResearcherSubFolder2 = null;
		
		try
		{
		    // add first child
		    ResearcherFolder2 = ResearcherFolder1.createChild("ResearcherFolder2");
				
		    // add second child child
		    ResearcherFolder3 = ResearcherFolder1.createChild("ResearcherFolder3");
			
		    // add sub folder
		    ResearcherSubFolder1 = ResearcherFolder2.createChild("ResearcherSubFolder1");
		
		    // add sub folder
		    ResearcherSubFolder2 = ResearcherFolder3.createChild("ResearcherSubFolder2");
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		assert ResearcherFolder1.getLeftValue() == 1 : "Right value should be 1 but is " +
		ResearcherFolder1.getLeftValue();
		assert ResearcherFolder1.getRightValue() == 10 : "Left value should be 10 but is " +
		ResearcherFolder1.getRightValue();
		
		assert ResearcherFolder2.getLeftValue() == 2 : "Right value should be 2 but is " +
		ResearcherFolder1.getLeftValue();
		assert ResearcherFolder2.getRightValue() == 5 : "Right value sould be  5 but is " +
		ResearcherFolder2.getRightValue();
		
		assert ResearcherFolder3.getLeftValue() == 6 : "Right value should be 6 but is " +
		ResearcherFolder1.getLeftValue();
		assert ResearcherFolder3.getRightValue() == 9 : "Right value sould be 9 but is " +
		ResearcherFolder3.getRightValue();
		
		assert ResearcherSubFolder1.getLeftValue() == 3 : "Right value should be 3 but is " +
		ResearcherSubFolder1.getLeftValue();
		assert ResearcherSubFolder1.getRightValue() == 4 : "Right value sould be 4 but is " +
		ResearcherSubFolder1.getRightValue();
		
		assert ResearcherSubFolder2.getLeftValue() == 7 : "Right value should be 7 but is " +
		ResearcherSubFolder2.getLeftValue();
		assert ResearcherSubFolder2.getRightValue() == 8 : "Right value sould be 8 but is " +
		ResearcherSubFolder2.getRightValue();
		
		//make Researcher folder 2 a root folder by adding them to the user
		r.addRootFolder(ResearcherFolder2);
		
		assert ResearcherFolder2.getLeftValue() == 1 : "Right value should be 4 but is " 
			+ ResearcherFolder2.getLeftValue();
		
		assert ResearcherFolder2.getRightValue() == 4 : "Right value should be 4 but is " 
			+ ResearcherFolder2.getRightValue();
		
		assert ResearcherFolder2.getTreeRoot().equals(ResearcherFolder2) : "Researcher folder 2 should be root but root = " + 
		ResearcherFolder2.getTreeRoot();
		

	}
	
}
