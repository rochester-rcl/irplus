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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.news.NewsDAO;
import edu.ur.ir.news.NewsItem;
import edu.ur.ir.news.NewsService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;

/**
 * News service for dealing with news.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultNewsService  implements NewsService {
	
    /**  Repository service for storing files */
    RepositoryService repositoryService;

	/**  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(DefaultNewsService.class);

	/**  Data access for a News item  */
	private NewsDAO newsDAO;
	
	/**
	 * Get the news data access
	 * 
	 * @return News dao object
	 */
	public NewsDAO getNewsDAO() {
		return newsDAO;
	}

	/**
	 * Set News data access 
	 * 
	 * @param newsDAO news data access object
	 */
	public void setNewsDAO(NewsDAO newsDAO) {
		this.newsDAO = newsDAO;
	}
	
	/**
	 * Save the news information
	 * 
	 * @see edu.ur.ir.news.NewsService#saveNewsItem(java.lang.Long)
	 */
	public void saveNewsItem(NewsItem newsItem) {
		newsDAO.makePersistent(newsItem);
	}	
	
	/**
	 * Delete a news item with the specified id.
	 * 
	 * @see edu.ur.ir.news.NewsService#deleteNewsItem(java.lang.Long)
	 */
	public boolean deleteNewsItem(NewsItem newsItem) {
		
		FileInfo article = newsItem.getArticle();
		
		Set<IrFile> filesToDelete = new HashSet<IrFile>();
		
		if( newsItem.getPrimaryPicture() != null )
		{
		    filesToDelete.add(newsItem.getPrimaryPicture());
		}
		
		Set<IrFile> pictures = newsItem.getPictures();
		for(IrFile file : pictures)
		{
			filesToDelete.add(file);
		}
		
		
		newsDAO.makeTransient(newsItem);
		
		if( article != null)
        {
        	repositoryService.deleteFileInfo(article);
        }
		
		
		for(IrFile file : filesToDelete)
		{
			repositoryService.deleteIrFile(file);
		}
		
		
		return true;
	}


	/**
	 * Get the news items based on the given criteria.
	 * 
	 * @see edu.ur.ir.news.NewsService#getNewsItemsOrderByName(int, int, String)
	 */
	public List<NewsItem> getNewsItemsOrderByName(
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return newsDAO.getNewsItems(rowStart, numberOfResultsToShow, sortType);
	}
	
	/**
     * Get a News Item by name.
     * 
     * @param name - name of the News Item.
     * @return - the found News Item or null if the News Item is not found.
     */
    public NewsItem getNewsItem(String name){
    	return newsDAO.findByUniqueName(name);
    }
	
	/**
	 * Get the news item by id.
	 * 
	 * @see edu.ur.ir.news.NewsService#getNewsItem(java.lang.Long, boolean)
	 */
	public NewsItem getNewsItem(Long id, boolean lock) {
		return newsDAO.getById(id, lock);
	}
	
 	/**
	 * Get the news item based on the criteria.
	 * 
	 * @see edu.ur.ir.news.NewsService#getNewsItemsCount()
	 */
	public Long getNewsItemsCount() {
		return newsDAO.getCount();
	}

	/**
	 * Add the picture to the news.
	 * @throws edu.ur.ir.IllegalFileSystemNameException 
	 * 
	 * @see edu.ur.ir.news.NewsService#addNewsItemPicture(java.lang.Long, java.lang.Long, java.io.File, java.lang.String, java.lang.String)
	 */
	public IrFile addNewsItemPicture(NewsItem newsItem,Repository repository,
			File f, String name, String description) throws IllegalFileSystemNameException {
		
		IrFile irFile =  null;
		try {
			irFile = repositoryService.createIrFile(repository, f, name, description);
		} catch (IllegalFileSystemNameException e) {
			// This Exception will not happen here since the name is extracted from uploaded file's name 
			// and not entered by user. So just catching and logging and not throwing.
			log.error("The IrFile name contains illegal special characters - " + e.getName());			
		}		
		newsItem.addPicture(irFile);
		
		return irFile;
	}
	
	/**
	 * Remove the news item picture.
	 * 
	 * @see edu.ur.ir.news.NewsService#removeNewsItemPicture(edu.ur.ir.news.NewsItem, edu.ur.ir.file.IrFile)
	 */
	public boolean removeNewsItemPicture(NewsItem newsItem, IrFile picture)
	{
		boolean removed = newsItem.removePicture(picture);
		if( removed )
		{
		    newsDAO.makePersistent(newsItem);
		    repositoryService.deleteIrFile(picture);
		}
		return removed;
	}
	
	public boolean removePrimaryNewsItemPicture(NewsItem newsItem)
	{
		IrFile picture = newsItem.getPrimaryPicture();
		
		if( picture != null)
		{
			newsItem.setPrimaryPicture(null);
			newsDAO.makePersistent(newsItem);
			repositoryService.deleteIrFile(picture);
		}
		return true;
	}
	
	/**
	 * Add the picture to the news.
	 * @throws edu.ur.ir.IllegalFileSystemNameException 
	 * @throws edu.ur.ir.IllegalFileSystemNameException 
	 * 
	 * @see edu.ur.ir.news.NewsService#addNewsItemPicture(java.lang.Long, java.lang.Long, java.io.File, java.lang.String, java.lang.String)
	 */
	public IrFile addPrimaryNewsItemPicture(NewsItem newsItem,Repository repository,
			File f, String name, String description) throws IllegalFileSystemNameException{
		
		if(newsItem.getPrimaryPicture() != null)
		{
			IrFile picture = newsItem.getPrimaryPicture();
			newsItem.setPrimaryPicture(null);
			newsDAO.makePersistent(newsItem);
			repositoryService.deleteIrFile(picture);
		}
		
		IrFile irFile = null;
		try {
			irFile = repositoryService.createIrFile(repository, f, name, description);
		} catch (IllegalFileSystemNameException e) {
			// This Exception will not happen here since the name is extracted from uploaded file's name 
			// and not entered by user. So just catching and logging and not throwing.
			log.error("The IrFile name contains illegal special characters - " + e.getName());			
		}
		newsItem.setPrimaryPicture(irFile);
		newsDAO.makePersistent(newsItem);
		return irFile;
	}

	/**
	 * Repository service for storing pictures.
	 * 
	 * @return
	 */
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	/**
	 * Repository service for storing pictures.
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**
	 * Return the news items
	 * 
	 * @see edu.ur.ir.news.NewsService#getNewsItems()
	 */
	@SuppressWarnings("unchecked")
	public List<NewsItem> getNewsItems() {
		return (List<NewsItem>)newsDAO.getAll();
	}

	/**
	 * Get the availabe news items.
	 * 
	 * @param d - date the news items should be available
	 * @param offset
	 * @param numToFetch
	 * @return- available news items
	 */
	public List<NewsItem> getAvailableNewsItems(Date d, int offset, int numToFetch)
	{
		return newsDAO.getAvailableNewsItems(d, offset, numToFetch);
	}


	/**
	 * Get a count of the availble news items for the given date.
	 * 
	 * @see edu.ur.ir.news.NewsService#getAvailableNewsItemsCount(java.util.Date)
	 */
	public Long getAvailableNewsItemsCount(Date d) 
	{
		return newsDAO.getAvailableNewsItemsCount(d);
	}



	

}
