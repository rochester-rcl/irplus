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

package edu.ur.ir.web.action.news;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.ir.news.NewsItem;
import edu.ur.ir.news.NewsService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.web.table.Pager;

/**
 * Allows for management of news information.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageNews extends Pager implements  Preparable{
	
	/** generated version id. */
	private static final long serialVersionUID = -6170012623168874297L;

	/** News service */
	private NewsService newsService;
	
	/** Repository service */
	private RepositoryService repositoryService;
	
	/**  Logger for managing news items*/
	private static final Logger log = Logger.getLogger(ManageNews.class);
	
	/** Set of news items for viewing the news items */
	private Collection<NewsItem> newsItems;
	
	/**  News Item for loading  */
	private NewsItem newsItem;

	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the news has been added*/
	private boolean added = false;
	
	/** Indicates the news item have been deleted */
	private boolean deleted = false;
	
	/** id of the news item  */
	private Long id;
	
	/** article information to be saved as a file */
	private String article;
	
	/** Set of news item ids */
	private long[] newsItemIds;
	
	/** holds the number of news pictures if a news item is loaded*/
	private int numberOfNewsPictures = 0;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of ip addresses to ignore  */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Default constructor */
	public  ManageNews()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}
	
	/**
	 * Method to create a new news item.
	 * 
	 * Create a new news item
	 * @throws IllegalFileSystemNameException 
	 */
	public String create() throws IllegalFileSystemNameException
	{
		log.debug("creating a news item = " + newsItem.getName());
		added = false;
		NewsItem other = newsService.getNewsItem(newsItem.getName());

		Repository repo = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, 
				false);
		if( other == null)
		{
			// create the file to hold news information
			FileInfo info = repositoryService.createFileInfo(repo, 
					"article_for_news.html");
			
			newsItem.setArticle(info);
			newsService.saveNewsItem(newsItem);
		    added = true;
		}
		else
		{
			message = getText("newsItemAlreadyExists", 
					new String[]{newsItem.getName()});
		}
        return "added";
	}
	
	/**
	 * Create/Edit a news item.
	 * 
	 * @return
	 * @throws IOException 
	 */
	public String edit() throws IOException
	{
		newsItem = newsService.getNewsItem(id, false);
		article = this.readArticleFromFile();
		numberOfNewsPictures = newsItem.getPictures().size();
		if( log.isDebugEnabled())
		{
		    log.debug("loaded item " + newsItem);
		}
		return "edit";
	}
	
	/**
	 * Save the news information.
	 * 
	 * @return
	 */
	public String save() throws Exception
	{
		log.debug("Saving news item");
		
		FileInfo info = newsItem.getArticle();
		log.debug( "news = " + newsItem + " info = " + info);
		String filePath = info.getFullPath();
		
		File f = new File(filePath);
		FileWriter fileWriter = new FileWriter(f);
		
		log.debug("Writing article " + article);
		fileWriter.write(article);
		fileWriter.flush();
		fileWriter.close();
		info.setSize(f.length());
		
		if( log.isDebugEnabled())
		{
			log.debug("Updated file " + info);
		}
		
		newsService.saveNewsItem(newsItem);
		return SUCCESS;
	}
	

	
	/**
	 * Method to update an existing news item.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updating news item id = " + newsItem.getId());
		added = false;

		NewsItem other = newsService.getNewsItem(newsItem.getName());
		
		if( other == null || other.getId().equals(newsItem.getId()))
		{
			newsService.saveNewsItem(newsItem);
			added = true;
		}
		else
		{
			message = getText("newsItemAlreadyExists", 
					new String[]{newsItem.getName()});
		}
        return "added";
	}
	
	/**
	 * Removes the selected new items and collections.
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("Delete news items called");
		if( newsItemIds != null )
		{
		    for(int index = 0; index < newsItemIds.length; index++)
		    {
			    log.debug("Deleting news items  with id " + newsItemIds[index]);
			    NewsItem newsItem = newsService.getNewsItem(newsItemIds[index], false);
			    newsService.deleteNewsItem(newsItem);
		    }
		}
		deleted = true;
		return "deleted";
	}
	
	/**
	 * Get the news item table data.
	 * 
	 * @return
	 */
	public String viewNewsItems()
	{
		rowEnd = rowStart + numberOfResultsToShow;
	    
		newsItems = newsService.getNewsItemsOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = newsService.getNewsItemsCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;
	}

	/**
	 * Get the news service.
	 * 
	 * @return
	 */
	public NewsService getNewsService() {
		return newsService;
	}

	/**
	 * Set the news service.
	 * 
	 * @param newsService
	 */
	public void setNewsService(NewsService newsService) {
		this.newsService = newsService;
	}
	
	/**
	 * List of news items for display.
	 * 
	 * @return
	 */
	public Collection<NewsItem> getNewsItems() {
		return newsItems;
	}
	
	/**
	 * Set the list of news items.
	 * 
	 * @param newsItems
	 */
	public void setNewsItems(Collection<NewsItem> newsItems) {
		this.newsItems = newsItems;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isAdded() {
		return added;
	}
	public void setAdded(boolean added) {
		this.added = added;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long[] getNewsItemIds() {
		return newsItemIds;
	}

	public void setNewsItemIds(long[] newsItemIds) {
		this.newsItemIds = newsItemIds;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public NewsItem getNewsItem() {
		return newsItem;
	}

	public void setNewsItem(NewsItem newsItem) {
		this.newsItem = newsItem;
	}

	public void prepare() throws Exception {
		log.debug("Prepare called" );
		if( id != null)
		{
			newsItem = newsService.getNewsItem(id, false);
			log.debug("News Item loaded with " + id);
		}
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}
	
	/**
	 * Read the article from the file so a user can edit it.
	 * 
	 * @return - the file read
	 * @throws IOException 
	 * @throws IOException
	 */
	private String readArticleFromFile() throws IOException 
	{
		
		FileReader reader = null;
		StringBuffer sb = new StringBuffer();
		try
		{
		    reader = new FileReader(newsItem.getArticle().getFullPath());
		    char[] characters = new char[1024];
		    
		    int count = 0;
		    while (count != -1)
            {
                count = reader.read(characters, 0, characters.length);
                // write out those same bytes
                if( count > 0 )
                {
                    sb.append(characters, 0, count);
                }
            }
		}
		finally
		{
			if( reader != null )
			{
				try {
					reader.close();
				} catch (IOException e) {
					reader = null;
				}
			}
		}
		return sb.toString();
	}

	public int getNumberOfNewsPictures() {
		return numberOfNewsPictures;
	}

	public void setNumberOfNewsPictures(int numberOfNewsPictures) {
		this.numberOfNewsPictures = numberOfNewsPictures;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

}
