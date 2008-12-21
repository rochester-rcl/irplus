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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

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

	// this if when the page is first initalized
	public static final String INIT = "INIT";
	
	// get the next picture
	public static final String NEXT = "NEXT";
	
	// get the previous picture
	public static final String PREV = "PREV";
	
	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(GetNewsItems.class);
	
	/** news service for gettting news items */
	private NewsService newsService;
	
	/**  Number of news items to grab */
	private int numNewsItems = 1;
	
	/**  List of news items to show */
	private List<NewsItem> newsItems = new LinkedList<NewsItem>();
	
	private int newsItemsSize = 0;
	
	/**
	 * Determine if the user is initializing wants the next or previous
	 * picture
	 */
	private String type;
	
	/**  Current news location */
	private int currentLocation;
	
	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	
    	if( log.isDebugEnabled())
    	{
    		log.debug("Next researcher Picture");
    	}
    	Date d = new Date();
    	int newsItemCount = newsService.getAvailableNewsItemsCount(d).intValue();
    	
    	System.out.println("Getting researhcer picture researcher count = " + newsItemCount);
    	if( newsItemCount > 0 )
    	{
    		// we can grab them all at once
    		if(  newsItemCount == 1 || newsItemCount == 2)
    		{
    			currentLocation = 0;
    			newsItems.addAll(newsService.getAvailableNewsItems(d,0, 2));
    			System.out.println("number of news items = " + newsItems.size());
    		}
    		else if ( type.equals(INIT))
     	    {
    			
     		    Random random = new Random();
     		    currentLocation = random.nextInt(newsItemCount);
     		    
     		   System.out.println( "init current Location = " + currentLocation  );
     		    
     		    // if past last position
         	    if ((currentLocation + 1) >= newsItemCount) {
         	    	// get last researcher
         	    	System.out.println( "init A fetching = " + currentLocation  );
         	    	newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 1));
         	    	
         	    	// reset current location to start
         	    	currentLocation = 0;
         	    	
         	    	System.out.println( "init B fetching = " + currentLocation  );
         	    	// get first researcher
         	    	newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 1));
         	       
         	    } else {
         	    	// otherwise in middle of list
         	    	newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 2));
         	    	currentLocation = currentLocation + 1;
         	    }
         	   System.out.println( "init final INIT Location = " + currentLocation  );
         	    
     	    }
    		else if( type.equals(NEXT))
    	    {
    			System.out.println( "NEXT = " + currentLocation  );
    			// end of list - just showed last researcher
    			if( (currentLocation + 1) >= newsItemCount )
    			{
    				currentLocation = 0;
    				System.out.println( "NEXT A fetching = " + currentLocation  );
     			    newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 2));
     		        currentLocation = currentLocation + 1;
    			}
    		    else
    		    {
    		    	System.out.println("NEXT - 1 = " + currentLocation  );
    			    // can't grab both at once  one at very end and one at the beginning
    			    if( (currentLocation + 2) == newsItemCount)
    			    {
    			    	currentLocation = currentLocation + 1;
    			    	System.out.println( "NEXT B fetching = " + currentLocation  );
    			    	newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 1));
    			    	currentLocation = 0;
    			    	System.out.println( "NEXT C fetching = " + currentLocation  );
             	    	newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 1));
    			    }
    			    else
    			    {
    			    	currentLocation = currentLocation + 1;
    			    	System.out.println("NEXT D fetching = " + currentLocation  );
    			    	newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 2));
    			    	currentLocation = currentLocation + 1;
    			    }
     		    }
    			System.out.println( "NEXT End current = " + currentLocation  );
    	    }
    		else if( type.equals(PREV))
    	    {
    			System.out.println( "PREV = " + currentLocation  );
    			
    			// at position 0 get - so very last and 0 are loaded
    			// get two before very last
    	    	if (currentLocation == 0) {
    	    		currentLocation = newsItemCount - 3;
    	    		System.out.println("PREV A fetching = " + currentLocation);
    	    		newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 2));
    	    		currentLocation = currentLocation + 1;
    	    	} 
    	    	// position 0 and 1 are loaded - so get very last two
    	    	if( currentLocation == 1)
    	    	{
    	    		currentLocation = newsItemCount - 2;
    	    		newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 2));
    	    		currentLocation = currentLocation + 1;
    	    	}
    	    	// 1 and 2 are loaded - get very last and first
    	    	else if ( currentLocation ==  2 ) 
    	    	{
    	    		currentLocation = newsItemCount - 1;
    	    		System.out.println("PREV B fetching = " + currentLocation);
    	    		newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 1));
    	    		currentLocation = 0;
    	    		System.out.println("PREV C fetching = " + currentLocation);
    	    		newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 1));
    	    	}
    	    	// at position grab last 2 from current positions
    	    	else 
    		    {
    			    currentLocation = currentLocation - 3;
    			    System.out.println("PREV E fetching = " + currentLocation);
    			    //get the last 2
    			    newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 2));
    			    currentLocation = currentLocation + 1;
    		    }
    	    	System.out.println( "PREV End current = " + currentLocation  );
    		   
    	    }
    	}
    	return SUCCESS;
    }
	
 

	public NewsService getNewsService() {
		return newsService;
	}

	public void setNewsService(NewsService newsService) {
		this.newsService = newsService;
	}

	public int getNumNewsItems() {
		return numNewsItems;
	}

	public void setNumNewsItems(int numNewsItems) {
		this.numNewsItems = numNewsItems;
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

	public int getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(int currentLocation) {
		this.currentLocation = currentLocation;
	}

	public int getNewsItemsSize() {
		return newsItemsSize;
	}

}
