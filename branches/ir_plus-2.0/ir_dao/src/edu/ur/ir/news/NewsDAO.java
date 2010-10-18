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

import java.util.Date;
import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.NameListDAO;
import edu.ur.dao.UniqueNameDAO;


/**
 * Interface for persisting a news item.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface NewsDAO extends CountableDAO, 
CrudDAO<NewsItem>, NameListDAO, UniqueNameDAO<NewsItem> {

	/**
	 * Get the list of News item .
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param sortType - sort order(Asc/desc)
	 * 
	 * @return list of contributor type found.
	 */
	public List<NewsItem> getNewsItems(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);
	
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
