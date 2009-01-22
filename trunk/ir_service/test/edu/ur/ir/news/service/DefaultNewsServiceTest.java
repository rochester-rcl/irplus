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


package edu.ur.ir.news.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TemporaryFileCreator;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.TransformedFileTypeDAO;
import edu.ur.ir.file.transformer.JpegThumbnailTransformer;
import edu.ur.ir.news.NewsItem;
import edu.ur.ir.news.NewsService;
import edu.ur.util.FileUtil;


/**
 * Test the service methods for the default news services.
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultNewsServiceTest {
	
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	// Check the repositories
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	 
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
    /** Service for dealing with news related information */
    NewsService newsService = (NewsService) ctx.getBean("newsService");
    
    /** Simple date format */
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
    /** Main repostiory service for dealing with repository information */
    RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
    
    TransformedFileTypeDAO transformedFileTypeDAO = (TransformedFileTypeDAO) ctx
	.getBean("transformedFileTypeDAO");
    
    /**  Creates temporary files */
    TemporaryFileCreator temporaryFileCreator = (TemporaryFileCreator) 
    ctx.getBean("temporaryFileCreator");
    
    /** Creates Jpeg Thumbnails */
    JpegThumbnailTransformer jpegThumbnail = (JpegThumbnailTransformer)
    ctx.getBean("jpegThumbnailTransformer");

    
    	
	/**
	 * Test finding the repository
	 */
	public void deleteNewsTest() throws Exception
	{
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);

		NewsItem newsItem = new NewsItem("myName");
		newsItem.setDateAvailable(sdf.parse("1/1/2007"));
		newsItem.setDateRemoved(sdf.parse("1/1/2008"));
		
		newsService.saveNewsItem(newsItem);
		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);
		assert newsService.getNewsItem(newsItem.getName()) != null;
		NewsItem other = newsService.getNewsItem(newsItem.getName());
		assert other.equals(newsItem) : "Should be equal to newsItem";
		tm.commit(ts);
		
		// Start the transaction 
		ts = tm.getTransaction(td);
		newsService.deleteNewsItem(newsService.getNewsItem(newsItem.getName()));
		tm.commit(ts);
	
		// Start the transaction 
		ts = tm.getTransaction(td);
		assert newsService.getNewsItem(newsItem.getName()) == null;
		tm.commit(ts);

	}
	
	/**
	 * Test adding an article to a news item.
	 */
	public void newsArticleTest() throws Exception
	{
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		
		NewsItem newsItem = new NewsItem("myName");
		newsItem.setDateAvailable(sdf.parse("1/1/2007"));
		newsItem.setDateRemoved(sdf.parse("1/1/2008"));
		
		newsService.saveNewsItem(newsItem);
		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		tm.commit(ts);
		
		
		FileInfo article = repositoryService.createFileInfo(repo, f, "uniqueName");
		newsItem.setArticle(article);
		newsService.saveNewsItem(newsItem);
		
 	    // Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		newsService.deleteNewsItem(newsItem);
	 	helper.cleanUpRepository();
		tm.commit(ts);	

	}
	
	/**
	 * Test adding an article to a news item.
	 */
	public void newsPictureTest() throws Exception
	{
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		
		NewsItem newsItem = new NewsItem("myName");
		newsItem.setDateAvailable(sdf.parse("1/1/2007"));
		newsItem.setDateRemoved(sdf.parse("1/1/2008"));
		
		newsService.saveNewsItem(newsItem);
		
		// create a transformed file type
		TransformedFileType transformedFileType = new TransformedFileType("Primary Thumbnail");
		transformedFileType.setDescription("Thumbnail created by the system");
		transformedFileType.setSystemCode("PRIMARY_THUMBNAIL");
		
		transformedFileTypeDAO.makePersistent(transformedFileType);
		// save the repository
		tm.commit(ts);
		
        // Start the transaction 
		ts = tm.getTransaction(td);
		
		String path = properties.getProperty("ir_service_location");
		String fileName = properties.getProperty("ur_research_home_jpg");
		
		File f = new File(path + fileName);
		
		assert f.exists() : "File should exist " + f.getAbsolutePath();
		newsItem = newsService.getNewsItem(newsItem.getId(), false);
		IrFile primaryPicture = newsService.addPrimaryNewsItemPicture(newsItem, repo, f, "apicture", "test adding picture");
		
		File tempFile = null;
		
		// create a thumbnail of the picture
		try
		{
		    tempFile = temporaryFileCreator.createTemporaryFile("jpg");
		    jpegThumbnail.transformFile(f, "jpg", tempFile);
		    
		}
		catch(Exception e)
		{
			throw new RuntimeException("could not create file", e);
		}
		
		
		repositoryService.addTransformedFile(repo, 
				primaryPicture, 
	    		tempFile, 
	    		"JPEG file", 
	    		jpegThumbnail.getFileExtension(), 
	    		transformedFileType);
		
		tm.commit(ts);
		
 	    // Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		File deletedFile = new File(primaryPicture.getFileInfo().getFullPath());
		newsService.deleteNewsItem(newsService.getNewsItem(newsItem.getId(), false));
		
		assert deletedFile.exists() == false : " deletedFile " + deletedFile.getAbsolutePath() + 
		    " should not exist ";
		
		transformedFileTypeDAO.makeTransient(transformedFileTypeDAO.getById(transformedFileType.getId(), false));
	 	
		helper.cleanUpRepository();
		tm.commit(ts);	

	}
	 

}
