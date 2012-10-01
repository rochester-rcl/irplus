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

package edu.ur.hibernate.ir.news.db;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.file.db.FileInfo;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.news.NewsDAO;
import edu.ur.ir.news.NewsItem;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryDAO;
import edu.ur.util.FileUtil;


/**
 * Test the persistance methods for News Item Information
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class NewsItemDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	NewsDAO newsDAO = (NewsDAO) ctx.getBean("newsDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
    
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** Repository relational data access  */
	RepositoryDAO repositoryDAO = (RepositoryDAO) ctx.getBean("repositoryDAO");

	/** Ir File relational data access.  */
	IrFileDAO irFileDAO = (IrFileDAO) ctx.getBean("irFileDAO");
	
	

	/**
	 * Test NewsItem persistence
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseNewsDAOTest()throws Exception{
	   
		TransactionStatus ts = tm.getTransaction(td);
		
		// create a repository to store files in.
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		
		tm.commit(ts);
		
  		NewsItem newsItem = new NewsItem("sports");
  		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
  		
		File f1 = testUtil.creatFile(directory, "testFile", 
		"<b>Hello</b>  - irFile This is text in a file");
  		
  		FileInfo info = repo.getFileDatabase().addFile(f1, "newsArticle");
  		
  		newsItem.setArticle(info);
  		newsItem.setDateAvailable(sdf.parse("1/1/2007"));
  		newsItem.setDateRemoved(sdf.parse("1/1/2008"));
        newsDAO.makePersistent(newsItem);
		
        // Start the transaction 
		ts = tm.getTransaction(td);
		NewsItem other = newsDAO.findByUniqueName(newsItem.getName());
		assert other.equals(newsItem) : "The News item  " + other + " should be equal to " + newsItem;
		List<NewsItem> newsItems = (List<NewsItem>)newsDAO.getAllOrderByName(0, 1);
		
		assert newsItems.size() == 1 : "Should find one news item";
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		newsDAO.makeTransient(other);
		assert newsDAO.getById(other.getId(), false) == null : "Should not be able to find other";
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		repoHelper.cleanUpRepository();
		tm.commit(ts);
		
	}
	
	/**
	 * Test adding a picture to a news item
	 */
	public void addPictureNewsDAOTest() throws Exception
	{
		TransactionStatus ts = tm.getTransaction(td);
		
		// create a repository to store files in.
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer", 
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");

		
		tm.commit(ts);
		
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		// we are only testing the ability to add a file to
		// the news item rather than an actual picture.
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "testFile", 
				"Hello  - irFile This is text in a file");

		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f, "newFile1");
		fileInfo1.setDescription("testThis");
		IrFile irFile = new IrFile(fileInfo1, "newName");
		
		irFileDAO.makePersistent(irFile);
		
 		NewsItem newsItem = new NewsItem("sports");
  		newsItem.setDateAvailable(sdf.parse("1/1/2007"));
  		newsItem.setDateRemoved(sdf.parse("1/1/2008"));

        newsDAO.makePersistent(newsItem);
		
        ts = tm.getTransaction(td);
        newsItem = newsDAO.getById(newsItem.getId(), false);
		newsItem.addPicture(irFile);
		newsDAO.makePersistent(newsItem);
		tm.commit(ts);		
		
		//create a new transaction
		ts = tm.getTransaction(td);
		//reload the repository
		newsItem = newsDAO.getById(newsItem.getId(), false);
		
		assert newsItem.getPicture(irFile.getId()) != null : "The picture should be found";
		
		tm.commit(ts);	
		
		//create a new transaction
		ts = tm.getTransaction(td);
		newsItem = newsDAO.getById(newsItem.getId(), false);
		Set<IrFile> pictures = newsItem.getPictures();
		
		for(IrFile picture: pictures)
		{
			newsItem.removePicture(picture);
			irFileDAO.makeTransient(picture);
		}
		
		newsDAO.makeTransient(newsItem);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		repoHelper.cleanUpRepository();
		tm.commit(ts);
			
		
	}


}
