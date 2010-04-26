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

package edu.ur.ir.institution;

import java.util.List;

import edu.ur.dao.CriteriaHelper;
import edu.ur.dao.CrudDAO;
import edu.ur.ir.institution.ReviewableItem;


/**
 * Data access for reviewable items.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface ReviewableItemDAO extends CrudDAO<ReviewableItem>
{
	
 	/**
	 * Get reviewable items sorting according to the sort and filter information 
	 * 
	 * Sort is applied based on the order of sort information in the list (1st to last).
	 * Starts at the specified row start location and stops at specified row end.
	 * 
	 * @param sortInformation - the sort information 
	 * @param filterInformation - filter requirements
     * @param parentCollectionId - the id of the parent reviewable collection.
	 * @param rowStart - start position in paged set
	 * @param rowEnd - end position in paged set
	 * @return List of items containing the specified information.
	 */
	public List<ReviewableItem> getReviewableItems( final List<CriteriaHelper> criteriaHelpers,
			final Long parentCollectionId,
			final int rowStart, final int rowEnd);
	
	
    /**
     * Get a count of items with given filter list for the 
     * specified reviewable collection 
     *  
     * @param filters - list of filters to apply to the object
     * @param parentCollectionId - the parent reviewable collection id of the items
     * @return - the number of folders found
     */
    public Integer getReviewableItemsCount(final List<CriteriaHelper> criteriaHelpers, 
    		final Long parentCollectionId);

    /**
	 * Get all review pending items
	 * 
	 * @return Items pending review
	 */
	public List<ReviewableItem> getAllPendingItems();

	/**
	 * Get review history for specified item id
	 * 
	 * @param itemId Id of item
	 */
	public List<ReviewableItem> getReviewHistoryByItem(Long itemId) ;
}
