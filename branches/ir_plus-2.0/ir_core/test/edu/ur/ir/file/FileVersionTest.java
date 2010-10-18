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

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.test.helper.PropertiesLoader;
import edu.ur.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.util.FileUtil;

/**
 * Test file versions.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class FileVersionTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 * @throws DuplicateNameException 
	 * @throws LocationAlreadyExistsException 
	 */
	public void testVersionBasics() throws DuplicateNameException, IllegalFileSystemNameException, LocationAlreadyExistsException 
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
		
		File f1 = testUtil.creatFile(directory, "testFile", 
				"Hello  - irFile This is text in a file");

		File f2 = testUtil.creatFile(directory, "testFile2", 
				"Hello  - irFile This is text in a file 2");
		
		// get the file database 
		FileDatabase fd = repo.getFileDatabase();

		// create a file in the file database
		FileInfo fileInfo1 = fd.addFile(f1, "newFile1");
		fileInfo1.setDisplayName("displayName1");
		
		// create a file in the database
		FileInfo fileInfo2 = fd.addFile(f2, "newFile2");
		fileInfo2.setDisplayName("displayName2");

		// create a collection
		InstitutionalCollection col = repo.createInstitutionalCollection("colName");
		col.setDescription("colDescription");

		IrUser user = new IrUser("username", "password");
		
		IrUser user1 = new IrUser("username1", "password1");
		
		// create a versioned file and add the file to the collection 
		VersionedFile vf = new VersionedFile(user, fileInfo1, "myNewFile");
		vf.addCollaborator(user1);
		
        FileVersion original = vf.getCurrentVersion();
        assert vf.getCurrentVersion().getVersionNumber() == 1 : "Version should be equal to 1 but is " +
        vf.getCurrentVersion().getVersionNumber();
        
		FileVersion newVersion = vf.addNewVersion(fileInfo1, user1);
		
		assert newVersion.equals(vf.getCurrentVersion()) : "Verions should be equal";
        assert newVersion.getVersionNumber() == 2 : "Version should be equal to 2 but equals " + 
        newVersion.getVersionNumber();
		
		// make sure the new file is the current version
		assert !newVersion.equals(original) : "Version should NOT be equal";
	
		assert original.hashCode() != newVersion.hashCode() : "hash codes should not be equal";

		repoHelper.cleanUpRepository();
	}
	
	
}
