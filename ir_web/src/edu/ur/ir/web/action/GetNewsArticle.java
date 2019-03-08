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


package edu.ur.ir.web.action;

import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.news.NewsItem;
import edu.ur.ir.news.NewsService;

/**
 * Get the new article.
 * 
 * @author Nathan Sarr
 *
 */
public class GetNewsArticle extends ActionSupport {
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = 5077640228407715934L;

	/**  Logger for file upload */
	private static final Logger log = LogManager.getLogger(GetNewsArticle.class);
	
	/**  News service */
	private NewsService newsService;
	
	/**  Id for the news item */
	private Long newsItemId;
	
	/** String of data representing the article */
	private String article;

	
	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	
    	if( log.isDebugEnabled())
    	{
	        log.debug("Trying to download news article");
    	}
	    
	    // get the article to see
		NewsItem newsItem = newsService.getNewsItem(newsItemId, false);
		if( newsItem != null )
		{
			article = this.readArticleFromFile(newsItem);
           
		}
        return SUCCESS;
    }
	

	/**
	 * News service for accessing news information
	 * 
	 * @return
	 */
	public NewsService getNewsService() {
		return newsService;
	}


	/**
	 * News service for accessing news information.
	 * 
	 * @param newsService
	 */
	public void setNewsService(NewsService newsService) {
		this.newsService = newsService;
	}


	/**
	 * Id of the news item to get.
	 * 
	 * @return
	 */
	public Long getNewsItemId() {
		return newsItemId;
	}


	/**
	 * Set the id of the news item to access.
	 * 
	 * @param newsItemId
	 */
	public void setNewsItemId(Long newsItemId) {
		this.newsItemId = newsItemId;
	}
	
	/**
	 * Read the article from the file so a user can edit it.
	 * 
	 * @return - the file read
	 * @throws IOException
	 */
	private String readArticleFromFile(NewsItem newsItem) throws IOException
	{
		FileReader reader = new FileReader(newsItem.getArticle().getFullPath());
		char[] characters = new char[1024];
		 StringBuffer sb = new StringBuffer();
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
		
		return sb.toString();
	}

	/**
	 * Get the article to display.
	 * 
	 * @return
	 */
	public String getArticle() {
		return article;
	}

	/**
	 * Set the article to display.
	 * 
	 * @param article
	 */
	public void setArticle(String article) {
		this.article = article;
	}
}
