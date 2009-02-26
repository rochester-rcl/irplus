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
import java.util.Date;
import java.util.List;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.repository.Repository;


/**
 * Service for dealing with News item.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface NewsService {

	/**
	 * Save the news information
	 * 
	 * @param newsItem News Item to save
	 */
	public void saveNewsItem(NewsItem newsItem);	
	
	/**
	 * Delete the news item
	 * 
	 * @param id Id of news item to be deleted
	 */
	public boolean deleteNewsItem(NewsItem newsItem);
	
	/**
	 * Get all news items.
	 * 
	 * @return
	 */
	public List<NewsItem> getNewsItems(); 
	
	/**
	 * Get  News item based on the given criteria.

	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - end position in paged set
	 * @param sortType - Order by (asc/desc)
	 * @return List of news items
	 */
	public List<NewsItem> getNewsItemsOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);
	
	/**
     * Get a News Item by name.
     * 
     * @param name - name of the News Item.
     * @return - the found News Item or null if the News Item is not found.
     */
    public NewsItem getNewsItem(String name);
    
    /**
     * Get the news item by id.
     * 
     * @param id - unique id of the news item.
     * @param lock - upgrade the lock on the data
     * @return - the found news item or null if the news item is not found.
     */
    public NewsItem getNewsItem(Long id, boolean lock);
    
    /**
     * Get a count of news items.
     *  
     * @return - the number of news items found
     */	
	public Long getNewsItemsCount();	
	

	/**
	 * Add a picture to the news item.  This makes no gaurantees the file
	 * uploaded is a picture.
	 * 
	 * @param newsItem - news item to add the picture to
	 * @param repository - repository to add the file to
	 * @param f - file containing the picture
	 * @param name - name of the file
	 * @param description - description of the file
	 * 
	 * @return the created irFile.
	 */
	public IrFile addNewsItemPicture(NewsItem newsItem,Repository repository,
			File f, String name, String description);
	
	/**
	 * Remove the picture in the news item
	 * 
	 * @param newsItem - news item to remove the picture from.
	 * @param picture - picture to remove
	 * 
	 * @return true if the picture is removed.
	 */
	public boolean removeNewsItemPicture(NewsItem newsItem, IrFile picture);
	
	/**
	 * Add the primary news item picture.
	 * 
	 * @param newsItem - the news item to remove the picture from
	 * @param repository - the repository to add the picture to. 
	 * @param f - file that contains the picture 
	 * @param name - name of the picture.
	 * @param description - description of the file.
	 * 
	 * @return the IrFile holding the picture information.
	 */
	public IrFile addPrimaryNewsItemPicture(NewsItem newsItem,Repository repository,
			File f, String name, String description);
	
	/**
	 * Remove the primary news item picture from the news item.
	 * 
	 * @param newsItem - news item to remove the primary picture from
	 * @return true if the picture was deleted.
	 */
	public boolean removePrimaryNewsItemPicture(NewsItem newsItem);
	
	/**
	 * Get a count of the total number of news items available for the given date
	 * 
	 * @param d - date the items should be available
	 * @return - a count of available dates
	 */
	public Long getAvailableNewsItemsCount(Date d);
	
	/**
	 * Get the availabe news items.
	 * 
	 * @param d - date the news items should be available
	 * @param offset
	 * @param numToFetch
	 * @return- available news items
	 */
	public List<NewsItem> getAvailableNewsItems(Date d, int offset, int numToFetch);
}
