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

package edu.ur.ir.news;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Properties;

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
 * Test the News item Class
 * 
 * @author Nathan Sarr
 * 
 */

@Test(groups = { "baseTests" }, enabled = true)
public class NewsItemTest {
	
	/**  Used for date formatting */
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/**
	 * Test the basic get and set  methods
	 * 
	 */
	public void testBasicSets() 
	{
		NewsItem newsItem = new NewsItem();
		
		newsItem.setName("myName");
		
		// catch parsing errors
		try
		{
		    newsItem.setDateAvailable(sdf.parse("1/1/2007"));
		    newsItem.setDateRemoved(sdf.parse("1/1/2008"));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		
		assert newsItem.getName().equals("myName") : "Names should be equal";
		
		// catch parsing errors
		try
		{
		    assert newsItem.getDateAvailable().equals(sdf.parse("1/1/2007")) : "Date available should be equal";
		    assert newsItem.getDateRemoved().equals(sdf.parse("1/1/2008")) : "Date removed should be equal";
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}

		
	}
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
		NewsItem newsItem1  = new NewsItem("name1");
		NewsItem newsItem2  = new NewsItem("name2");
		NewsItem newsItem3  = new NewsItem("name1");

		
		try
		{
  		    newsItem1.setDateAvailable(sdf.parse("1/1/2007"));
		    newsItem1.setDateRemoved(sdf.parse("1/1/2008"));
		    
		    newsItem2.setDateAvailable(sdf.parse("1/2/2007"));
		    newsItem2.setDateRemoved(sdf.parse("1/2/2008"));
		
		    newsItem3.setDateAvailable(sdf.parse("1/1/2007"));
		    newsItem3.setDateRemoved(sdf.parse("1/1/2008"));
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}

		
		assert newsItem1.equals(newsItem3): "News Item should be the same";
		assert !newsItem2.equals(newsItem1): "The News Item  should not be the same";
		assert newsItem1.hashCode() == newsItem3.hashCode() : "Hash codes should be the same";
		assert newsItem2.hashCode() != newsItem3.hashCode() : "Hash codes should not be the same"; 
	}	
	
	
	/**
	 * Make sure we can add the article. 
	 * @throws LocationAlreadyExistsException 
	 * @throws IllegalFileSystemNameException 
	 */
	public void testAddArticle() throws LocationAlreadyExistsException, IllegalFileSystemNameException
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
		
		// get the file database 
		FileDatabase fd = repo.getFileDatabase();

		// create a file in the file database
		FileInfo fileInfo1 = fd.addFile(f1, "newFile1");
		fileInfo1.setDisplayName("displayName1");
		
		NewsItem newsItem1  = new NewsItem("name1");
		newsItem1.setArticle(fileInfo1);
		
		assert newsItem1.getArticle().equals(fileInfo1) : "Should find news article 1";

		
		repoHelper.cleanUpRepository();
	}

}
