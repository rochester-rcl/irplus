/**  
   Copyright 2008-2010 University of Rochester

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

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ur.ir.news.NewsItem;
import edu.ur.ir.news.NewsService;

/**
 * Helper class to get news information.
 * 
 * @author Nathan Sarr
 *
 */
public class DateOrderNewsItemHelper implements Serializable{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 3512760784892154155L;

	// this if when the page is first initalized
	public static final String INIT = "INIT";
	
	// get the next picture
	public static final String NEXT = "NEXT";
	
	// get the previous picture
	public static final String PREV = "PREV";
	
	/**  Logger for file upload */
	private static final Logger log = LogManager.getLogger(DateOrderNewsItemHelper.class);
	
	/**  Current news location */
	private int currentLocation;
	
	/** count of news items available   */
	private int newsItemCount;
	

	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public List<NewsItem> getNewsItems(String type, NewsService newsService, int location){
    	
    	currentLocation = location;
    	List<NewsItem> newsItems = new LinkedList<NewsItem>();
    	if( log.isDebugEnabled())
    	{
    		log.debug("Next news items");
    	}
    	Date d = new Date();
    	newsItemCount = newsService.getAvailableNewsItemsCount(d).intValue();
    	
    	if( newsItemCount > 0 )
    	{
    		// we can grab them all at once
    		if(  newsItemCount == 1 || newsItemCount == 2 )
    		{
    			currentLocation = 0;
    			newsItems.addAll(newsService.getAvailableNewsItems(d,0, 2));
    			log.debug("number of news items = " + newsItems.size());
    		}
    		else if( type.equals(INIT))
    		{
    			newsItems.addAll(newsService.getAvailableNewsItems(d,0, 2));
    			currentLocation = 1;
    			log.debug("INIT current location " + currentLocation);
    		}
    		else if( type.equals(NEXT))
    	    {
    			log.debug( "NEXT = " + currentLocation  );
    			// at end of list start from beginning
    			if( (currentLocation + 1) >= newsItemCount )
    			{
    				currentLocation = 0;
    				log.debug( "NEXT A fetching = " + currentLocation  );
     			    newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 2));
     		        currentLocation = currentLocation + 1;
    			}
    		    else
    		    {
    		    	log.debug("NEXT  1 = " + currentLocation  );
    			    // can't grab both at once  one at very end and one at the beginning
    			    if( (currentLocation + 2) == newsItemCount)
    			    {
    			    	currentLocation = currentLocation + 1;
    			    	log.debug( "NEXT B fetching = " + currentLocation  );
    			    	newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 1));
    			    	currentLocation = 0;
    			    	log.debug( "NEXT C fetching = " + currentLocation  );
             	    	newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 1));
    			    }
    			    else
    			    {
    			    	currentLocation = currentLocation + 1;
    			    	log.debug("NEXT D fetching = " + currentLocation  );
    			    	newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 2));
    			    	currentLocation = currentLocation + 1;
    			    }
     		    }
    			log.debug( "NEXT End current = " + currentLocation  );
    	    }
    		else if( type.equals(PREV))
    	    {
    			log.debug( "PREV = " + currentLocation  );
    			
    			// at position 0 get - so very last and 0 are loaded
    			// get two before very last
    	    	if (currentLocation == 0) {
    	    		currentLocation = newsItemCount - 3;
    	    		log.debug("PREV A fetching = " + currentLocation);
    	    		newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 2));
    	    		currentLocation = currentLocation + 1;
    	    	} 
    	    	// position 0 and 1 are loaded - so get very last two
    	    	else if( currentLocation == 1)
    	    	{
    	    		currentLocation = newsItemCount - 2;
    	    		newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 2));
    	    		currentLocation = currentLocation + 1;
    	    	}
    	    	// 1 and 2 are loaded - get very last and first
    	    	else if ( currentLocation ==  2 ) 
    	    	{
    	    		currentLocation = newsItemCount - 1;
    	    		log.debug("PREV B fetching = " + currentLocation);
    	    		newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 1));
    	    		currentLocation = 0;
    	    		log.debug("PREV C fetching = " + currentLocation);
    	    		newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 1));
    	    	}
    	    	// at position grab last 2 from current positions
    	    	else 
    		    {
    			    currentLocation = currentLocation - 3;
    			    log.debug("PREV E fetching = " + currentLocation);
    			    //get the last 2
    			    newsItems.addAll(newsService.getAvailableNewsItems(d,currentLocation, 2));
    			    currentLocation = currentLocation + 1;
    		    }
    	    	log.debug( "PREV End current = " + currentLocation  );
    		   
    	    }
    	}
    	
    	return newsItems;
    }
 
	public int getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(int currentLocation) {
		this.currentLocation = currentLocation;
	}

	public int getNewsItemCount() {
		return newsItemCount;
	}

}
