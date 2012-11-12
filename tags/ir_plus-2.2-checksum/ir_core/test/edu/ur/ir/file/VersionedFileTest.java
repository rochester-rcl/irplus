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

package edu.ur.ir.file;

import java.io.File;
import java.util.Properties;

import org.testng.annotations.Test;

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
 * Test the VersionedIrFile Class
 * 
 * @author Nathan Sarr
 * 
 */

@Test(groups = { "baseTests" }, enabled = true)
public class VersionedFileTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	

	/**
	 * Test creating a versioned file 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testCreateFileVersion() throws IllegalFileSystemNameException, LocationAlreadyExistsException
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
		
		// create a new user
		IrUser user = new IrUser("username", "password");
		
		// create a new user
		IrUser user1 = new IrUser("username1", "password1");
		
		VersionedFile vif = new VersionedFile(user, fileInfo1, "displayName1");
		assert vif.getCurrentVersion().getIrFile().getFileInfo().equals(fileInfo1): "File info 1 " + fileInfo1 + " should equal "; 
		assert vif.getCurrentVersion().getVersionNumber() == 1: " File versions should be equal";

		FileCollaborator collaborator =  vif.addCollaborator(user1);
		
		assert vif.isShared() : "File should be shared and should return true";
		
		assert vif.getCollaborators().iterator().next().equals(collaborator) : "Collaborators should be the same.";
		
		// add a new version to the file
		FileVersion version = vif.addNewVersion(fileInfo2, user1);
		assert version.equals(vif.getCurrentVersion()): 
			"Version should be the new version but is " + vif.getCurrentVersion();
		
		// make sure the version number has changed
		assert vif.getLargestVersion() == vif.getCurrentVersion().getVersionNumber(): "Largest version should equal 2";
		
		// switch the current version - user wants the older version to be the current version
		// create a new version with the same file information
		assert vif.changeCurrentIrVersion(1, user1): "New version should be changed";
		assert vif.getCurrentVersion().getIrFile().getFileInfo().equals(fileInfo1) : "Current file should be found " +
		" current = " + vif.getCurrentVersion() + "fileInfo1 = " + fileInfo1;
		

		assert vif.lock(user1) : "User1 should be able to lock the file ";
		
		assert vif.isLocked() : "File should be locked";
		
		assert vif.getLockedBy().equals(user1) : "Should be locked by user1";
		
		vif.unLock();
		
		assert !(vif.isLocked()): " File should be unlocked";
		
		vif.removeCollaborator(collaborator);
		
		assert vif.getCollaborators().size() == 0 : " Collaborators size should be 0";
		
		repoHelper.cleanUpRepository();
	}
	
	/**
	 * Test removing a collaborator - if they have locked the file, the lock should be removed
	 * @throws LocationAlreadyExistsException 
	 */
	public void testRemoveCollaboratorLockedFileVersion() throws IllegalFileSystemNameException, LocationAlreadyExistsException
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
		
		// create a new user
		IrUser user = new IrUser("username", "password");
		
		// create a new user
		IrUser user1 = new IrUser("username1", "password1");
		
		VersionedFile vif = new VersionedFile(user, fileInfo1, "displayName1");
		assert vif.getCurrentVersion().getIrFile().getFileInfo().equals(fileInfo1): "File info 1 " + fileInfo1 + " should equal "; 
		assert vif.getCurrentVersion().getVersionNumber() == 1: " File versions should be equal";

		FileCollaborator collaborator =  vif.addCollaborator(user1);
		
		assert vif.isShared() : "File should be shared and should return true";
		
		assert vif.getCollaborators().iterator().next().equals(collaborator) : "Collaborators should be the same.";
		
		// add a new version to the file
		FileVersion version = vif.addNewVersion(fileInfo2, user1);
		assert version.equals(vif.getCurrentVersion()): 
			"Version should be the new version but is " + vif.getCurrentVersion();
		
		// make sure the version number has changed
		assert vif.getLargestVersion() == vif.getCurrentVersion().getVersionNumber(): "Largest version should equal 2";
		
		// switch the current version - user wants the older version to be the current version
		// create a new version with the same file information
		assert vif.changeCurrentIrVersion(1, user1): "New version should be changed";
		assert vif.getCurrentVersion().getIrFile().getFileInfo().equals(fileInfo1) : "Current file should be found " +
		" current = " + vif.getCurrentVersion() + "fileInfo1 = " + fileInfo1;
		

		assert vif.lock(user1) : "User1 should be able to lock the file ";
		
		assert vif.isLocked() : "File should be locked";
		
		assert vif.getLockedBy().equals(user1) : "Should be locked by user1";
		
		vif.removeCollaborator(collaborator);

		assert !(vif.isLocked()): " File should be unlocked";
		
		assert vif.getCollaborators().size() == 0 : " Collaborators size should be 0";
		
		repoHelper.cleanUpRepository();
	}
	
	/**
	 * Test renaming a file with an extension.
	 * @throws LocationAlreadyExistsException 
	 * @throws IllegalFileSystemNameException 
	 */
	public void testReName() throws LocationAlreadyExistsException, IllegalFileSystemNameException
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
		
		// create a new user
		IrUser user = new IrUser("username", "password");
		
	
		VersionedFile vif = new VersionedFile(user, fileInfo1, "displayName1");
		assert vif.getCurrentVersion().getIrFile().getFileInfo().equals(fileInfo1): "File info 1 " + fileInfo1 + " should equal "; 
		assert vif.getCurrentVersion().getVersionNumber() == 1: " File versions should be equal";

	
		// add a new version to the file
		FileVersion version = vif.addNewVersion(fileInfo2, user);
		assert version.equals(vif.getCurrentVersion()): 
			"Version should be the new version but is " + vif.getCurrentVersion();
		
		// make sure the version number has changed
		assert vif.getLargestVersion() == vif.getCurrentVersion().getVersionNumber(): "Largest version should equal 2";
		
		// switch the current version - user wants the older version to be the current version
		// create a new version with the same file information
		assert vif.changeCurrentIrVersion(1, user): "New version should be changed";
		assert vif.getCurrentVersion().getIrFile().getFileInfo().equals(fileInfo1) : "Current file should be found " +
		" current = " + vif.getCurrentVersion() + "fileInfo1 = " + fileInfo1;
		
		// rename the most current version with a new file name and extension
		vif.reName("fileName4.doc");
		assert vif.getNameWithExtension().equals("fileName4.doc") : "Name with extension should be fileName4.doc but is: " +  vif.getNameWithExtension();
		assert vif.getName().equals("fileName4") : "Name should be fileName4 but is : " + vif.getName();
        assert vif.getCurrentVersion().getIrFile().getName().equals("fileName4") : "Name with extension should be fileName4 but is: " +  vif.getCurrentVersion().getIrFile().getName();
        assert vif.getCurrentVersion().getIrFile().getFileInfo().getExtension().equals("doc") : "extension should be .doc but is: " +  vif.getCurrentVersion().getIrFile().getFileInfo().getExtension();
		
		repoHelper.cleanUpRepository();
	}
	

}
