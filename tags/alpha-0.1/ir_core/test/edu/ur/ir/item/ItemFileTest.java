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
import edu.ur.ir.repository.Repository;
import edu.ur.ir.test.helper.PropertiesLoader;
import edu.ur.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.util.FileUtil;

/**
 * Test an item file.
 * 
 * @author Nathan Sarr
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ItemFileTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets()  throws IllegalFileSystemNameException
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
		
		File f1 = testUtil.creatFile(directory, "testItemFile", 
				"Hello  - irFile This is text in a file");
		
		// get the file database 
		FileDatabase fd = repo.getFileDatabase();

		// create a file in the file database
		FileInfo fileInfo1 = fd.addFile(f1, "newFile1");
		fileInfo1.setDisplayName("displayName1");
		
		IrFile irFile = new IrFile(fileInfo1, "testItemFile");
		GenericItem item = new GenericItem("testItem");
		ItemFile itemFile = new ItemFile(item, irFile);
		
		assert itemFile.getIrFile().equals(irFile) : "The ir file should be found";
		assert itemFile.getItem().equals(item) : "The items should be equal";
		
		repoHelper.cleanUpRepository();
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals() throws IllegalFileSystemNameException
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
		
		File f1 = testUtil.creatFile(directory, "testItemFile", 
		"Hello  - irFile This is text in a file");
		
		File f2 = testUtil.creatFile(directory, "testFile2", 
		"Hello  - irFile This is text in a file 2");
		
		// get the file database 
		FileDatabase fd = repo.getFileDatabase();

		// create a file in the file database
		FileInfo fileInfo1 = fd.addFile(f1, "newFile1");
		fileInfo1.setDisplayName("displayName1");
		
		// create a second file in the database
		FileInfo fileInfo2 = fd.addFile(f2, "newFile2");
		fileInfo2.setDisplayName("displayName2");
		
		IrFile irFile1 = new IrFile(fileInfo1, "testItemFile");
		irFile1.setId(1l);
		IrFile irFile2 = new IrFile(fileInfo2, "testItemFile2");
		irFile2.setId(2l);
		
		GenericItem item = new GenericItem("testItem");
		
		ItemFile itemFile1 = new ItemFile(item, irFile1);
		ItemFile itemFile2 = new ItemFile(item, irFile1);
		ItemFile itemFile3 = new ItemFile(item, irFile2);
		
		assert itemFile1.equals(itemFile2) : "Item files should be the same";
		assert !itemFile1.equals(itemFile3) : "Item files should not be the same";
		
		repoHelper.cleanUpRepository();
	}

}
