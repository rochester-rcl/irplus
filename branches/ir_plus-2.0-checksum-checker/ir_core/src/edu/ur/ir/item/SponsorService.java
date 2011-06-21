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

import java.io.Serializable;
import java.util.List;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.order.OrderType;

/**
 * Sponsor service interface for creating and getting Sponsor.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface SponsorService extends Serializable{
	
	/**
	 * Get Sponsor sorted according to the sort information .  
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - number of results to get
	 * @param orderType - Order by asc/desc
	 * 
	 * @return List of sponsors
	 */
	public List<Sponsor> getOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final OrderType orderType);
	
    /**
     * Get a count of Sponsor.
     *  
     * @return - the number of sponsors found
     */
    public Long getCount();
    
    /**
     * Delete a Sponsor 
     * 
     * @param sponsor to delete
     */
    public void delete(Sponsor sponsor);
    
    /**
     * Get a Sponsor by name.
     * 
     * @param name - name of the Sponsor .
     * @return - the found Sponsor or null if the Sponsor is not found.
     */
    public Sponsor get(String name);
    
    /**
     * Get a Sponsor by id
     * 
     * @param id - unique id of the Sponsor .
     * @param lock - upgrade the lock on the data
     * @return - the found Sponsor or null if the Sponsor is not found.
     */
    public Sponsor get(Long id, boolean lock);
    
    /**
     * Save the Sponsor .
     * 
     * @param sponsor
     */
    public void save(Sponsor sponsor);
 
	/**
	 * Get all Sponsor.
	 * 
	 * @return List of all Sponsor 
	 */
	public List<Sponsor> getAll();
	
	
	/**
	 * Get a count of all sponsor names in the system with
	 * specified name first letter character 
	 * 
	 * @param sponsorNameFirstChar - first character in the name of the sponsor name
	 * @return count of sponsors found
	 */
	public Long getCount(char lastNameFirstChar);
	
	/**
	 * Get a count of all sponsor names in the system with
	 * specified first characters 
	 * 
	 * @param firstCharRange - first character in range
	 * @param lastCharRange - last character in the range
	 * 
	 * @return count of sponsor names found that have a name with the 
	 * a first character in the specified range
	 */
	public Long getCount(char firstCharRange, char lastCharRange);
	
	/**
	 * Get a list of sponsors in the system with the first character of the name
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of sponsors
	 */
	public List<Sponsor> getByNameFirstChar(int rowStart,
			int maxResults, 
			char firstChar,
			OrderType orderType);
	
	/**
	 * Get a list of sponsors that have names
	 * that have a beginning character that start between the specified characters
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of sponsors
	 */
	public List<Sponsor> getSponsorsByNameBetweenChar(final int rowStart,
			int maxResults, 
			char firstChar,
			char lastChar,
			OrderType orderType);
	
	/**
	 * Get a count of all sponsors in the specified collection 
	 *   THIS INCLUDES sponsor names in child collections 
	 * 
	 * @param institutional collection - parent collection
	 * @return count of sponsors found
	 */
	public Long getCount(InstitutionalCollection collection);
	
	/**
	 * Get a count of all sponsors in the specified collection with
	 * specified first character in the name.  THIS INCLUDES sponsor names in child collections 
	 * 
	 * @param institutional collection - parent collection
	 * @param nameFirstChar - first character in the name of the sponsor
	 * @return count of sponsors found
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstChar);
	
	/**
	 * Get a count of all institutional sponsors in the specified collection with
	 * specified first character -  THIS INCLUDES sponsors in child collections 
	 * 
	 * @param institutional collection - parent collection
	 * @param nameFirstCharRange - sponsor name first character in range
	 * @param nameLastCharRange - sponsor name with character in the range
	 * 
	 * @return count of sponsors found that have a first character in the specified range
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstCharRange, char nameLastCharRange);
	
	
	/**
	 * Get the list of sponsors for the specified collection.  This includes sponsors in sub collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults -  Max number of results to grab
	 * @param Institutional collection -   collection to get sponsors
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of s[pmsprs
	 */
	public List<Sponsor> getCollectionSponsorsOrderByName(int rowStart, 
			int maxResults, 
			InstitutionalCollection collection, 
			OrderType orderType) ;


	/**
	 * Get a list of sponsors for a specified collection by first character of the sponsor name.  
	 * This INCLUDES sponsor names in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param Institutionalcollection - parent collection
	 * @param firstChar - first character that the sponsor name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of sponsors
	 */
	public List<Sponsor> getCollectionSponsorsByChar(final int rowStart,
			int maxResults, 
			InstitutionalCollection institutionalCollection,
			char firstChar,
			OrderType orderType);
	
	/**
	 * Get a list of sponsors for a specified collection by sponsors
	 * that start between the specified characters in the sponsor name.  This 
	 * INCLUDES person names in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param Institutional Collection - parent collection 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of sponsors
	 */
	public List<Sponsor> getCollectionSponsorsBetweenChar(int rowStart,
			int maxResults, 
			InstitutionalCollection institutionalCollection,
			char firstChar,
			char lastChar,
			OrderType orderType);
}
