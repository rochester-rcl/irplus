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

package edu.ur.ir.item;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.NameListDAO;
import edu.ur.dao.NonUniqueNameDAO;

import java.util.List;

import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.PersonName;

/**
 * Interface for persisting Item information.
 * 
 * @author Nathan Sarr
 *
 */
public interface GenericItemDAO extends CountableDAO, 
CrudDAO<GenericItem>, NameListDAO, NonUniqueNameDAO<GenericItem>
{
	/**
	 * Find the item for with the specified name and collection id.
	 * 
	 * @param name of the item
	 * @param id id of the parent collection
	 * @return the found item or null if the collection is not found.
	 */
	public GenericItem getItemForCollection(String name, Long irCollectionId);
	
	/**
	 * Get a count for the number of contributions made by a given contributor -
	 * this is a name/contribution type combination.
	 * 
	 * @param contributor that made contributions to an item
	 * @return count of item contributions.
	 */
	public Long getItemContributionCount(Contributor contributor);
	
	/**
	 * Get a count for the number of contributions made by a given contributor -
	 * this name
	 * 
	 * @param contributorId id of the contributor
	 * @return count of item contributions.
	 */
	public Long getContributionCountByPersonName(PersonName personName);
	
	/**
	 * Get the list of contributions this person has made based on a given name.  It
	 * should not include a contribution type that is already associated 
	 * to this item for the given person name.  
	 * 	
	 * @param personNameId - name id of the person who made the contribution
	 * @param itemId - GenericItem the contribution was made to.
	 * @return list of available contributions.
	 */
	public List<ContributorType> getPossibleContributions(Long personNameId, Long itemId);
	
	
	/**
	 * Get a list of identifier types that can be applied to this
	 * item.  Identifier types that have already been used will not
	 * be returned.
	 * 
	 * @param itemId the item to get possible identifier types for.
	 * @return
	 */
	public List<IdentifierType> getPossibleIdentifierTypes(Long itemId);

	/**
	 * Get the list of items owned by an user
	 * 	
	 * @param userId - User Id owning the items 
	 * @return list of Items
	 */
	public List<GenericItem> getAllItemsForUser(Long userId);
	
	/**
	 * Get the download count of all files within an item.
	 * 
	 * @param itemId - id of the item to get the downloads for
	 * @return - the count of downloads.
	 */
	public Long getDownloadCount(Long itemId);
	
	/**
	 * Get a count of items that have the specified content type.
	 * 
	 * @param contentType - the content type to check
	 * @return a count of the number of items using the content type
	 */
	public Long getContentTypeCount(ContentType contentType);
	
	/**
	 * Get a count of the total number of items that have the specified content type
	 * attached to it.
	 * 
	 * @param contentType - the content type to check for
	 * @return number of items that have the secondary content type attached.
	 */
	public Long getSecondaryContentTypeCount(ContentType contentType);
	
	/**
	 * Get a count of items that have the specified contributor type.
	 * 
	 * @param contributorType - the contributor type to check
	 * @return count of the number of items that have the specified contributor type.
	 */
	public Long getContributorTypeCount(ContributorType contributorType);
	
	

}
