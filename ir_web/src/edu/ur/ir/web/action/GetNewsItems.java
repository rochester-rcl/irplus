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

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.news.NewsItem;
import edu.ur.ir.news.NewsService;

/**
 * Get the news items.
 * 
 * @author Nathan Sarr
 *
 */
public class GetNewsItems extends ActionSupport{
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = -4925446526690331640L;
	
	/**  Logger for file upload */
	private static final Logger log = LogManager.getLogger(GetNewsItems.class);
	
	/** news service for getting news items */
	private NewsService newsService;
	
	/**  List of news items to show */
	private List<NewsItem> newsItems = new LinkedList<NewsItem>();

	/** helper for getting news items */
	private DateOrderNewsItemHelper newsHelper = new DateOrderNewsItemHelper();
	
	/**
	 * Determine if the user is initializing wants the next or previous
	 * picture
	 */
	private String type;
	
	/**  Current news location */
	private int currentNewsItemLocation;
	
	/** total count of available news items */
	private int newsItemCount;

	
	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	log.debug("Get news items");
    	newsItems = newsHelper.getNewsItems(type, newsService, currentNewsItemLocation);
    	currentNewsItemLocation = newsHelper.getCurrentLocation();
    	newsItemCount = newsHelper.getNewsItemCount();
    	return SUCCESS;
    }

	public NewsService getNewsService() {
		return newsService;
	}

	public void setNewsService(NewsService newsService) {
		this.newsService = newsService;
	}

	
	public List<NewsItem> getNewsItems() {
		return newsItems;
	}

	public void setNewsItems(List<NewsItem> newsItems) {
		this.newsItems = newsItems;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public int getCurrentNewsItemLocation() {
		return currentNewsItemLocation;
	}

	public void setCurrentNewsItemLocation(int currentNewsItemLocation) {
		this.currentNewsItemLocation = currentNewsItemLocation;
	}
	
	public int getNewsItemCount() {
		return newsItemCount;
	}


}
