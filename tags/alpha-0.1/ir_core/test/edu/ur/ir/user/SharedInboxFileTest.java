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

import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileInfo;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.test.helper.PropertiesLoader;
import edu.ur.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.util.FileUtil;

/**
 * Tests creating a personal inbox file
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class SharedInboxFileTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	public void basicPersonalInboxFileTest() throws FileSharingException, IllegalFileSystemNameException
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

		// get the file database 
		FileDatabase fd = repo.getFileDatabase();
		
		// create the first file to store in the temporary folder
		File f = testUtil.creatFile(directory, "testInboxFile1",
				"Hello  - versionedIrFile This is text in a file"); 
		
		// create a new file info container
		FileInfo fileInfo1 = fd.addFile(f, "newFile1");
		fileInfo1.setDisplayName("displayName1");
		
		// create the first file to store in the temporary folder
		File f2 = testUtil.creatFile(directory, "testInboxFile2",
				"Hello  - versionedIrFile This is text in a file"); 
		
		// create a new file info container
		FileInfo fileInfo2 = fd.addFile(f2, "newFile1");
		fileInfo2.setDisplayName("displayName2");
		
		// create the owner of the folders
		IrUser user = new IrUser("nate", "password");
		
		// create the owner of the folders
		IrUser sharedWithUser = new IrUser("nate", "password");
		
		// create a new versioned file
		VersionedFile vif = new VersionedFile(user, fileInfo1, "displayName1");

		// create a new versioned file
		VersionedFile vif2 = new VersionedFile(user, fileInfo2, "displayName2");

		
		SharedInboxFile personalInboxFile1 = sharedWithUser.addToSharedFileInbox(user, vif);
		SharedInboxFile personalInboxFile2 = sharedWithUser.addToSharedFileInbox(user, vif);
		SharedInboxFile personalInboxFile3 = sharedWithUser.addToSharedFileInbox(user, vif2);
		
		assert personalInboxFile1.getSharedWithUser().equals(sharedWithUser) : 
			" Personal inbox should have shared with user " + sharedWithUser;
		
		assert personalInboxFile1.getSharingUser().equals(user) : 
			" Personal inbox should have sharing user " + user;
		
		assert user.getSharedInboxFiles().contains(personalInboxFile1) : 
			"user inbox should contain personalIboxFile1 but doesn't ";
		
		assert personalInboxFile1.equals(personalInboxFile2) : 
			"files should be equal but are not! " +
			"personal inbox file 1 = " + personalInboxFile1 + 
			" personal inbox file 2 = " + personalInboxFile2; 
			
		assert !personalInboxFile2.equals(personalInboxFile3) : 
			"personal file should not be equal personal inbox file 2 = " 
			+ personalInboxFile2 + 
			"personal inbox file 3 = " + personalInboxFile3; 
		
		repoHelper.cleanUpRepository();

	}

}
