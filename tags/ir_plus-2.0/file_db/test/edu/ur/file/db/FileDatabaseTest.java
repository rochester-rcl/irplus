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
package edu.ur.file.db;

import java.io.File;
import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import edu.ur.test.helper.PropertiesLoader;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.file.db.TreeFolderInfo;
import edu.ur.util.FileUtil;

import org.apache.commons.io.FilenameUtils;

/**
 * Test the TreeFolderInfo class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class FileDatabaseTest {

	/**
	 * Properties file for directory information 
	 */
	Properties properties;
	
	/**
	 * Setup for testing the file Database server
	 */
	@BeforeTest
	public void setUp() {
	    properties = new PropertiesLoader().getProperties();
	}
	
	/**
	 * Setters and Getters test 
	 */
	@Test
	public void basicFileDatabaseTest() {
		DefaultFileDatabase fileDatabaseImpl = new DefaultFileDatabase();
		
		fileDatabaseImpl.setDescription("dbDescription");
		fileDatabaseImpl.setName("dbName");
		fileDatabaseImpl.setId(44l);
		
		assert fileDatabaseImpl.getDescription().equals("dbDescription") : "Descriptions should be the same";
		assert fileDatabaseImpl.getName().equals("dbName") : "Names should be the same";
		assert fileDatabaseImpl.getId().equals(44l) : "Id's should be the same";
	}
	
	/**
	 * Test adding a root folder to the file database.
	 * @throws LocationAlreadyExistsException 
	 */
	public void createRootFolderTest() throws LocationAlreadyExistsException
	{
		DefaultFileServer fs = new DefaultFileServer();
		
		String databasePath = FilenameUtils.separatorsToSystem(properties.getProperty("FileDatabaseTest.db_path"));
		assert databasePath != null : "Path should not be null";
		DefaultFileDatabase fileDatabaseImpl = fs.createFileDatabase("displayName", "dbName_1", databasePath, "dbDescription");
		
		fileDatabaseImpl.setId(44l);
		
		assert fileDatabaseImpl.getPrefix() != null : "Prefix should not be null but is";
		assert fileDatabaseImpl.getPath() != null : "Base path shouldn't be null";
		assert fileDatabaseImpl.getPath().equals(databasePath) : 
			" Path should equal " + databasePath +
			" but is " + fileDatabaseImpl.getPath();
		
		
		TreeFolderInfo folder = fileDatabaseImpl.createRootFolder("rootFolder", "folderName");
		assert folder.getPath().equals(fileDatabaseImpl.getFullPath()) : "folder should have "
			+ " path equal to file database full path folder path = " + folder.getPath() +  
			" file database path = " + fileDatabaseImpl.getFullPath();
		
		assert fileDatabaseImpl.setCurrentFileStore(folder.getName()) : "Folder to store files should be set";
		
		assert fileDatabaseImpl.getCurrentFileStore().equals(folder) : "File Store should equal folder";
		
		TreeFolderInfo child = fileDatabaseImpl.getRootFolder("folderName").createChild("newDbFolder", "newDbFolder");
	    
		assert child.getParent() == folder : "Child should have parent folder";
		
		assert child.getBasePath().equals(folder.getBasePath()) : "Child folder base path = "
			+ child.getBasePath() + " parent folder path = " + folder.getBasePath();
		
		assert child.getPath().equals(folder.getFullPath()) : "Childs path = " 
			+ child.getPath() + " should equal the "
			+  "parents full path = " + folder.getFullPath();
		
		File f = new File(child.getFullPath());
		assert f.exists() : "Child folder should exist for full path " + child.getFullPath();
		
		
		File root = new File(folder.getFullPath());
		assert root.exists() : "Root folder " + root.getAbsolutePath() + " should exist";
		fileDatabaseImpl.removeRootFolder(folder);
		assert !root.exists() : "Root folder " + root.getAbsolutePath() + " should no longer exist";
	
		
		fs.deleteFileServer();
	}
	
	/**
	 * Test adding a file to the file database.
	 * @throws LocationAlreadyExistsException 
	 * @throws IllegalFileSystemNameException 
	 */
	public void addFileTest() throws LocationAlreadyExistsException, IllegalFileSystemNameException
	{
		DefaultFileServer fs = new DefaultFileServer();
		
		String databasePath = FilenameUtils.separatorsToSystem(properties.getProperty("FileDatabaseTest.db_path"));
		assert databasePath != null : "Path should not be null";
		DefaultFileDatabase fileDatabaseImpl = fs.createFileDatabase("displayName", "dbName_1", databasePath, "dbDescription");
		
		fileDatabaseImpl.setId(44l);
		
		TreeFolderInfo folder = fileDatabaseImpl.createRootFolder("rootFolder", "folderName");
		TreeFolderInfo child = folder.createChild("newDbFolder", "newDbFolder");
		
		// set the default file storage
		assert fileDatabaseImpl.setCurrentFileStore(child.getName()) : "the new default folder should be set";
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "testFile", "Hello  -  This file to add to a file info object");
        FileInfo info = fileDatabaseImpl.addFile(f, "uniqueFileName");

        assert fileDatabaseImpl.getFile(info.getName()) != null : "File should be found";
        
		fs.deleteFileServer();
	}
	
	/**
	 * Test creating an emtpy file in the file system.
	 * @throws LocationAlreadyExistsException 
	 * @throws IllegalFileSystemNameException 
	 */
	public void createEmptyFileTest() throws LocationAlreadyExistsException, IllegalFileSystemNameException
	{
		DefaultFileServer fs = new DefaultFileServer();
		
		String databasePath = FilenameUtils.separatorsToSystem(properties.getProperty("FileDatabaseTest.db_path"));
		assert databasePath != null : "Path should not be null";
		DefaultFileDatabase fileDatabaseImpl = fs.createFileDatabase("displayName", "dbName_1", databasePath, "dbDescription");
		
		fileDatabaseImpl.setId(44l);
		
		TreeFolderInfo folder = fileDatabaseImpl.createRootFolder("rootFolder", "folderName");
		TreeFolderInfo child = folder.createChild("newDbFolder", "newDbFolder");
		
		// set the default file storage
		assert fileDatabaseImpl.setCurrentFileStore(child.getName()) : "the new default folder should be set";
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
        FileInfo info = fileDatabaseImpl.addFile("anEmptyFileUniqueFileName");

        assert fileDatabaseImpl.getFile(info.getName()) != null : "File should be found";
        
		fs.deleteFileServer();
	}

	/**
	 * Test re-naming the file database.
	 * @throws LocationAlreadyExistsException 
	 */
	public void databaseReNameTest() throws LocationAlreadyExistsException
	{
		DefaultFileServer fs = new DefaultFileServer();
		
		String databasePath = FilenameUtils.separatorsToSystem(properties.getProperty("FileDatabaseTest.db_path"));
		assert databasePath != null : "Path should not be null";
		DefaultFileDatabase fileDatabaseImpl = fs.createFileDatabase("displayName_2", "dbName_2", databasePath, "dbDescription");
		
		fileDatabaseImpl.setId(44l);
		
		assert fileDatabaseImpl.getPrefix() != null : "Prefix should not be null but is";
		assert fileDatabaseImpl.getPath() != null : "Base path shouldn't be null";
		assert fileDatabaseImpl.getPath().equals(databasePath) : 
			" Path should equal " + databasePath +
			" but is " + fileDatabaseImpl.getPath();
		
		File f = new File(fileDatabaseImpl.getFullPath());
		assert f.exists() : "File database " + f.getAbsolutePath() + " should exist";
		
		fs.renameFileDatabase(fileDatabaseImpl.getName(), "renameDbName_2");
		
		assert fs.getFileDatabases().contains(fileDatabaseImpl) : "File database should contain file database " + fileDatabaseImpl;
		
		File fNew = new File( fileDatabaseImpl.getPath() + "renameDbName_2");
		assert fNew.exists() : "New file database " + fNew.getAbsolutePath() + " should exist";
		assert !f.exists() : " Old location should no longer exist " + f.getAbsolutePath();
		
		fs.deleteFileServer();
	}
	
	/**
	 * Test equals method 
	 */
	@Test
	public void testEquals() {
		DefaultFileDatabase fd1 = new DefaultFileDatabase();
		fd1.setName("name");

		DefaultFileDatabase fd2 = new DefaultFileDatabase();
		fd2.setName("name");

		DefaultFileDatabase fd3 = new DefaultFileDatabase();
		fd3.setName("name3");
		
		assert fd1.equals(fd2) : "File Databases equal";
		assert !fd1.equals(fd3) : "File databases not equal";
	}

	/**
	 * Test the hash code
	 */
	@Test
	public void testHashCode() {
		DefaultFileDatabase fd1 = new DefaultFileDatabase();
		fd1.setName("name");

		DefaultFileDatabase fd2 = new DefaultFileDatabase();
		fd2.setName("name");

		DefaultFileDatabase fd3 = new DefaultFileDatabase();
		fd3.setName("name3");
		
		assert fd1.hashCode() == fd2.hashCode() : "hash codes are equal";
		assert fd1.hashCode() != fd3.hashCode() : "hash codes are not equals";
	}
}
