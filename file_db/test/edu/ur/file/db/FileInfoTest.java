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

import edu.ur.file.db.DefaultFileInfo;
import edu.ur.file.db.TreeFolderInfo;

import edu.ur.test.helper.PropertiesLoader;
import edu.ur.util.FileUtil;


/**
 * Test the FileInfo class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class FileInfoTest {

	/**
	 * Properties file with testing specific information.
	 */
	Properties properties;
	
	/**
	 * Setup for testing the file System manager
	 * this loads the properties file for getting
	 * the correct path for different file systems
	 */
	@BeforeTest
	public void setUp() {
	    properties = new PropertiesLoader().getProperties();
	}
	
	/**
	 * Setters and Getters test 
	 */
	@Test
	public void basicFileInfoTest() {
		
		// this will create folders so we need to place them in directories
		String folderPath = properties.getProperty("FileInfoTest.base.equals");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName2", "fileDB_2", folderPath, "description");
		
		TreeFolderInfo folder1 = fd.createRootFolder("displayName1", "TreeFolderInfo1");
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "basicFile1","Hello  -  This file is for equals 1");
		DefaultFileInfo fileInfo = folder1.createFileInfo(f, "basicFile1");
		
		fileInfo.setVersion(22);
		fileInfo.setDisplayName("displayName");
		fileInfo.setId(9l);
		
		assert fileInfo.getVersion() == 22 : "Versions should be the same";
		assert fileInfo.getDisplayName().equals("displayName") : 
			"Display names should be the same dsplay name = " + fileInfo.getDisplayName();
		
		assert fileInfo.getName().equals("basicFile1") : "File names should be the same but is " + fileInfo.getName();
		assert fileInfo.getId().equals(9l) : "Ids should be the same";
		
		assert fileInfo.getPrefix().equals(folder1.getPrefix()) : "File should have the same prefix as it's folder " +
		" folder prefix = " + folder1.getPrefix() + " fileInfo prefix = " + fileInfo.getPrefix();
		
	    assert fileInfo.getPath().equals(folder1.getFullPath()) : "folder path should equal the file info path file" +
	    		" info path = " + fileInfo.getPath() + " folderInfo full path = " + folder1.getFullPath();
	    
	    assert fileInfo.getFullPath().equals(folder1.getFullPath() + fileInfo.getName() ) : " Name path should = "
	    	+ folder1.getFullPath() + fileInfo.getName() + " but equals " + fileInfo.getFullPath();
	    
	    assert server.deleteDatabase(fd.getName()) : "File Database should be deleted";
	   
	
	}
	
	/**
	 * Test adding a parent folder
	 */
	@Test
	public void testAddTreeFolderInfo()
	{
		TreeFolderInfo folderInfo = new TreeFolderInfo();
		
		DefaultFileInfo fileInfo = new DefaultFileInfo();
		fileInfo.setFolderInfo(folderInfo);
		assert fileInfo.getFolderInfo() == folderInfo : "Parent should have the same reference";
	}
	
	/**
	 * Test the hash code.
	 */
	@Test
	public void testHashCode()
	{
		
		// this will create folders so we need to place them in directories
		String folderPath = properties.getProperty("FileInfoTest.base.equals");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName3", "fileDB_3", folderPath, "description");
		
		TreeFolderInfo folder1 = fd.createRootFolder("displayName1", "TreeFolderInfo1");
		folder1.setDisplayName("displayName1");

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		// create the first file
		File f = testUtil.creatFile(directory, "basicFile1","Hello  -  This file is for equals 1");
		DefaultFileInfo fileInfo1 = folder1.createFileInfo(f, "basicFile1");
		
		
		File f2 = testUtil.creatFile(directory, "basicFile2","Hello  -  This file is for equals 1");
		DefaultFileInfo fileInfo2 = folder1.createFileInfo(f2, "basicFile2");
		
		// trick the system because we need files
		// with the same name and path to have the same hash code
		fileInfo2.setName("basicFile1");
		
		assert fileInfo2.hashCode() == fileInfo1.hashCode() : "Hash codes should be the same "
			+ " fileInfo1 hashCode =  " + fileInfo1.hashCode() + " FileInfo2 hashCode = " + fileInfo2.hashCode();
		
		assert server.deleteDatabase(fd.getName()) : "File Database should be deleted";
	    //assert FileSystemManager.deleteFolder(folder1 ): "Folder path should be deleted";
	
	}
	
	/**
	 * Test the hash code.
	 */
	@Test
	public void testEquals()
	{
		
		// this will create folders so we need to place them in directories
		String folderPath = properties.getProperty("FileInfoTest.base.equals");
		
		DefaultFileServer server  = new DefaultFileServer("fileServer");
		DefaultFileDatabase fd = server.createFileDatabase("displayName4", "fileDB_4", folderPath, "description");
		
		TreeFolderInfo folder1 = fd.createRootFolder("displayName1", "TreeFolderInfo1");
		
		folder1.setDisplayName("displayName1");
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("file_db_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "basicFile1", "Hello  -  This file is for equals 1");
		DefaultFileInfo fileInfo1 = folder1.createFileInfo(f, "basicFile1");
		
		
		File f2 = testUtil.creatFile(directory, "basicFile2", "Hello  -  This file is for equals 1");
		DefaultFileInfo fileInfo2 = folder1.createFileInfo(f2, "basicFile2");
		
		// trick the system because we need files
		// with the same name and path to have the same hash code
		fileInfo2.setName("basicFile1");
		
		File f3 = testUtil.creatFile(directory, "basicFile3", "Hello  -  This file is for equals 1");
		DefaultFileInfo fileInfo3 = folder1.createFileInfo(f3, "basicFile3");
		
		assert fileInfo2.hashCode() == fileInfo1.hashCode() : "Hash codes should be the same "
			+ " fileInfo1 hashCode =  " + fileInfo1.hashCode() + " FileInfo2 hashCode = " + fileInfo2.hashCode();
		
		assert fileInfo2.hashCode() != fileInfo3.hashCode() : "Hash codes should NOT be the same "
			+ " fileInfo2 hashCode =  " + fileInfo2.hashCode() + " FileInfo3 hashCode = " + fileInfo3.hashCode();
		
		assert fileInfo2.equals(fileInfo1) : "File info 2 should equal file Info 1";
		assert !fileInfo1.equals(fileInfo3) : "File info 1 should Not equal fileInfo 3";
		
		assert server.deleteDatabase(fd.getName()) : "File Database should be deleted";
	    //assert FileSystemManager.deleteFolder(folder1): "Folder path should be deleted";
	
	}

}
