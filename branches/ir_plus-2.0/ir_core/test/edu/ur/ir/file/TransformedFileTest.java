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
import java.io.IOException;

import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.test.helper.PropertiesLoader;
import edu.ur.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.util.FileUtil;

/**
 * Test the transformed file class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class TransformedFileTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	
	/**
	 * Setup for testing
	 * 
	 * this deletes exiting test directories if they exist
	 */
	@BeforeMethod 
	public void cleanDirectory() {
		try {
			File f = new File( properties.getProperty("a_repo_path") );
			if(f.exists())
			{
			    FileUtils.forceDelete(f);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * Test creating a transformed file 
	 * @throws LocationAlreadyExistsException 
	 */
	public void addTransformedFileToIrFileTest() throws IllegalFileSystemNameException, LocationAlreadyExistsException
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
		
		File f1 = testUtil.creatFile(directory, "transformedFile", 
				"Hello  - irFile This is text in a file");

		File f2 = testUtil.creatFile(directory, "transformedFile2", 
				"Hello  - irFile This is text in a file 2");
		
		// get the file database 
		FileDatabase fd = repo.getFileDatabase();

		// create a file in the file database
		FileInfo fileInfo1 = fd.addFile(f1, "newFile1");
		
		// create a file in the database
		FileInfo fileInfo2 = fd.addFile(f2, "transformedFile");

		IrFile irFile = new IrFile(fileInfo1, "myNewFile");
		
		TransformedFileType transformedFileType = new TransformedFileType("transformedFile", "DerivedFile");
		TransformedFile transformedFile = irFile.addTransformedFile(fileInfo2, transformedFileType);
		TransformedFile transformedFile2 = irFile.addTransformedFile(fileInfo2, transformedFileType);
		
        assert transformedFile.getActualFile().equals(irFile) : "Actual file should be the original irFile";		
		assert transformedFile2.equals(transformedFile) : "Derived files should be equal";
		assert irFile.getTransformedFiles().contains(transformedFile) : "irFile should contain " + transformedFile;
		assert irFile.removeTransformedFile(transformedFile) : "Should be able to remove the transformed file";
		
		repoHelper.cleanUpRepository();
	}

}
