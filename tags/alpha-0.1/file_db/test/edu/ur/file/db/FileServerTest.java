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

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.test.helper.PropertiesLoader;

import java.io.File;
import java.util.Properties;

/**
 * Test the FolderInfo class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class FileServerTest {
	
	/**
	 * File server that will be created
	 * 
	 */
	DefaultFileServer fileServer;
	
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
		fileServer = new DefaultFileServer("serverName");
		fileServer.setDescription("serverDescription");
		fileServer.setId(44l);
	}
	
	/**
	 * Test baisc file server operation
	 */
	@Test
	public void basicfileServerTest() {
		assert fileServer.getDescription().equals("serverDescription") : "Descriptions should be the same";
		assert fileServer.getName().equals("serverName") : "Names should be the same";
		assert fileServer.getId().equals(44l) : "Id's should be the same";
	}
	
	/**
	 * Test adding and deleting a file Database
	 */
	@Test
	public void addFileDatabaseTest()
	{
		DefaultFileDatabase fd = fileServer.createFileDatabase("displayName5", "db_5", 
				properties.getProperty("FileServerTest.server_path"), "description");
		
		File f = new File(fd.getFullPath());
		
		assert f.exists() : "Folder in file system should exist for specified file database";		
		DefaultFileDatabase myFd = fileServer.getFileDatabase("db_5");
		
       	assert myFd.equals(fd) : "should find the created file db";     	
       	assert myFd.getFileServer().equals(fileServer);
       	
       	assert fileServer.deleteDatabase("db_5") :
       		"File databse should be deleted";
       	
       	assert !f.exists() :
       		"The folder " + f.getAbsolutePath() + " in the file system should no longer exist " +
       		"for the specified file database";
	}

	
    /**
	 * Cleanup the file system manager path
	 */
	@AfterTest
	public void tearDown() 
	{
	    if( !fileServer.deleteFileServer() )
	    {
	    	throw new IllegalStateException("File Databases could not be deleted");
	    }
	}
}
