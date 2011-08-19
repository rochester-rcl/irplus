/**  
   Copyright 2008-2011 University of Rochester

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
public class GroupWorkspaceTest {
	
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

	/**
	 * Base tests for group work spaces.
	 * @throws IllegalFileSystemNameException 
	 */
	public void baseGroupSpaceTest() throws IllegalFileSystemNameException
	{
		GroupWorkspace groupSpace = new GroupWorkspace("test group","group description");
	    assert groupSpace.getName().equals("test group") : " group name should equal test group but equals " + groupSpace.getName();
	}
	
	/**
	 * Test adding a file to a group space.
	 * 
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testAddFile() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException
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
				"Hello  - user versionedFile This is text in a file"); 
		
		// get the file database 
		FileDatabase fd = repo.getFileDatabase();
		
		// create a new file info container
		FileInfo fileInfo1 = fd.addFile(f, "newFile1");
		fileInfo1.setDisplayName("displayName1");
		
		// create a new versioned file
		IrUser user = new IrUser("username", "password");
		VersionedFile vf = new VersionedFile(user, fileInfo1, "displayName1");
		GroupWorkspace groupSpace = new GroupWorkspace("test group","group description");
		
		groupSpace.createRootFile(vf);
		GroupWorkspaceFile rootFile = groupSpace.getRootFile(vf.getName());
		assert rootFile != null : "Group file should be found ";
		assert groupSpace.removeRootFile(rootFile) : "Group file " + rootFile + " should be removed";
		assert groupSpace.getRootFile(rootFile.getName()) == null : "Should no longer find group file";
		
		repoHelper.cleanUpRepository();
	}
	
	/**
	 * Test adding a folder to the group as a root
	 * 
	 * @throws DuplicateNameException 
	 */
	public void testAddFolder() throws DuplicateNameException, IllegalFileSystemNameException
	{
		IrUser user = new IrUser();
		user.setUsername("aUser");
		GroupWorkspace groupSpace = new GroupWorkspace("test group","group description");
		GroupWorkspaceFolder groupFolder = groupSpace.createRootFolder(user, "rootFolder");
		
		assert groupFolder.getOwner().equals(user) : "Owner of folder should equal " +
		user + " but equals " + groupFolder.getOwner();
		
		assert groupFolder.getFullPath().equals("/rootFolder/") : 
			"Path Should equal /rootFolder/ but is: " + groupFolder.getFullPath();
		
		assert groupSpace.getRootFolder(groupFolder.getName()).equals(groupFolder) : 
			"Should find group folder " + groupFolder;
		assert groupSpace.removeRootFolder(groupFolder) : "Should be able to remove " + groupFolder;
		assert user.getRootFolder(groupFolder.getName()) == null : "Root folder should be removed";
	}

}
