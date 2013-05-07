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

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.news.NewsItem;
import edu.ur.ir.news.NewsService;
import edu.ur.ir.repository.RepositoryService;

/**
 * Allows a news picture to be deleted.
 * 
 * @author Nathan Sarr
 *
 */
public class DeleteNewsPicture extends ActionSupport implements Preparable {

	/** Eclipse generated id */
	private static final long serialVersionUID = 3980266197680971615L;

	/**  News service for dealing with news */
	private NewsService newsService;
	
	/** News item  to remove the picture from*/
	private NewsItem newsItem;
	
	/** Id for the news item to load */
	private Long newsItemId;
	
	/** determine if the primary picture should be removed */
	private boolean primaryNewsPicture;
	
	/** picture to remove*/
	private Long pictureId;
	
	/** Repository service to deal with storing files */
	private RepositoryService repositoryService;
	
	/**
	 * Load the news service.
	 * 
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	public void prepare() throws Exception {
		newsItem = newsService.getNewsItem(newsItemId, false);
	}
	
	/**
	 * Execute the delete.
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		if( primaryNewsPicture)
		{
			newsService.removePrimaryNewsItemPicture(newsItem);
		}
		else
		{
			IrFile picture = repositoryService.getIrFile(pictureId, false);
		    newsService.removeNewsItemPicture(newsItem, picture);
		}
		newsItem = newsService.getNewsItem(newsItemId, false);
		
		return SUCCESS;
	}

	/**
	 * News service to perform delete
	 * 
	 * @return
	 */
	public NewsService getNewsService() {
		return newsService;
	}

	/**
	 * News service to perform delete.
	 * 
	 * @param newsService
	 */
	public void setNewsService(NewsService newsService) {
		this.newsService = newsService;
	}

	/**
	 * Get the news item to delete the picture from.
	 * 
	 * @return
	 */
	public NewsItem getNewsItem() {
		return newsItem;
	}

	/**
	 * Get the news item id.
	 * 
	 * @return
	 */
	public Long getNewsItemId() {
		return newsItemId;
	}

	/**
	 * Set the news item id.
	 * 
	 * @param newItemId
	 */
	public void setNewsItemId(Long newItemId) {
		this.newsItemId = newItemId;
	}

	/**
	 * Set to true if the picture to be deleted is the 
	 * primary picture.
	 * 
	 * @return
	 */
	public boolean isPrimaryNewsPicture() {
		return primaryNewsPicture;
	}

	/**
	 * Set to true if the picture to be deleted is the primary picture.
	 * 
	 * @param primaryNewsPicture
	 */
	public void setPrimaryNewsPicture(boolean primaryNewsPicture) {
		this.primaryNewsPicture = primaryNewsPicture;
	}

	/**
	 * Get the picture id to be deleted.
	 * 
	 * @return
	 */
	public Long getPictureId() {
		return pictureId;
	}

	/**
	 * Set the picture id to be deleted.
	 * 
	 * @param pictureId
	 */
	public void setPictureId(Long pictureId) {
		this.pictureId = pictureId;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

}
