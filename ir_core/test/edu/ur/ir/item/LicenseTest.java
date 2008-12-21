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

package edu.ur.ir.item;

import java.io.File;
import java.util.Properties;

import org.testng.annotations.Test;

import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileInfo;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.item.License;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.test.helper.PropertiesLoader;
import edu.ur.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.util.FileUtil;

/**
 * Test the License Class
 * 
 * @author Nathan Sarr
 * 
 */

@Test(groups = { "baseTests" }, enabled = true)
public class LicenseTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testLicenseBasicSets() 
	{
		License license = new License();
		license.setName("licenseName");
		license.setLicenseVersion("12345");
		
		assert license.getName().equals("licenseName");
		assert license.getLicenseVersion().equals("12345");
	}
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
		License license1  = new License();
		License license2  = new License();
		License license3  = new License();
		License license4  = new License();
		
		license1.setName("name1");
		license2.setName("name2");
		license3.setName("name1");
		license4.setName("name1");

		license1.setLicenseVersion("1");
		license2.setLicenseVersion("3");
		license3.setLicenseVersion("1");
		license4.setLicenseVersion("2");

		
		assert license1.equals(license3): "License should be the same";
		assert !license2.equals(license1): "The licenses should not be the same";
		assert !license1.equals(license4): "License should NOT be the same";

		assert license1.hashCode() == license3.hashCode() : "Hash codes should be the same";
		assert license2.hashCode() != license3.hashCode() : "Hash codes should not be the same";
		assert license1.hashCode() != license4.hashCode() : "Hash codes should not be the same";

	}

	/**
	 * Test getting a file from a License
	 */
	public void testLicenseFile() throws IllegalFileSystemNameException {
		
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

		File f1 = testUtil.creatFile(directory, "licenseFile", 
		    "Hello  -  This is text in a License file");

		// get the file database 
		FileDatabase fd = repo.getFileDatabase();
		
		FileInfo fileInfo1 = fd.addFile(f1, "newLicense1");
		fileInfo1.setDisplayName("licenseDisplayName1");
		
		License license = new License();
		license.setDescription("My License");
		IrFile irFile = new IrFile(fileInfo1, "My License");
		license.setIrFile(irFile);
		license.setLicenseVersion("123");
		
		assert license.getIrFile().getFileInfo().equals(fileInfo1);
		assert license.getLicenseVersion().equals("123");
	
		repoHelper.cleanUpRepository();
	}



}
