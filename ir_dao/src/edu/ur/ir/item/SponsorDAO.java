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


import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.NameListDAO;
import edu.ur.dao.UniqueNameDAO;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.order.OrderType;

/**
 * Sponsor data access.
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public interface SponsorDAO extends CountableDAO, 
CrudDAO<Sponsor>, NameListDAO, UniqueNameDAO<Sponsor>
{
	/**
	 * Get the list of Sponsor.
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param orderType - Order (Desc/Asc) 
	 * 
	 * @return list of Sponsor found.
	 */
	public List<Sponsor> getSponsorsOrderByName(
			final int rowStart, final int numberOfResultsToShow, final OrderType orderType);
	
	/**
	 * Get a count of all sponsor names in the system with
	 * specified name first letter character 
	 * 
	 * @param sponsorNameFirstChar - first character in the name of the sponsor name
	 * @return count of sponsors found
	 */
	public Long getCount(char nameFirstChar);
	
	/**
	 * Get a count of all sponsor names in the system with
	 * specified first characters 
	 * 
	 * @param sponsorFirstCharRange - first character in range
	 * @param sponsorlastCharRange - last character in the range
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
	 * @param lastNamefirstChar - first character in range that the first letter of the name can have
	 * @param lastNamelastChar - last character in range that the first letter of the name can have
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
	 * Get a list of sponsors for a specified collection by first character of the name
	 * 
	 * NOTE: This search includes all sponsors in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param institutional collection - the institutional collection 
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
	 * Get a list of sponsors for a specified collection sponsor name first character
	 * that start between the specified characters
	 * 
	 * NOTE: This search includes all sponsors in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - the institutional collection 
	 * @param firstChar - first character in range that the first letter of the  name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of sponsors
	 */
	public List<Sponsor> getCollectionSponsorsBetweenChar(final int rowStart,
			int maxResults, 
			InstitutionalCollection collection, 
			char firstChar,
			char lastChar,
			OrderType orderType);
	
	/**
	 * Get a list of sponsors for a specified collection by name.
	 * 
	 * NOTE: This search includes all sponsors in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - id of the collection to get sponsors  from
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of sponsors
	 */
	public List<Sponsor> getCollectionSponsorsByName(final int rowStart,
			int maxResults, 
			InstitutionalCollection collection, 
			OrderType orderType);
	
	
	/**
	 * Get a count of sponsors in the institutional collection 
	 * 
	 * NOTE: This search includes all sponsors in child collections
	 * 
	 * @param collection - institutional collection to return the count for
	 * 
	 * @return the count found
	 */
	public Long getCount(InstitutionalCollection collection);
	
	/**
	 * Get a count of sponsors in the institutional collection with a name
	 * that starts with the specified first character.
	 * 
	 * NOTE: This search includes all sponsors in child collections
	 * 
	 * @param nameFirstChar - first character of the name
	 * 
	 * @return the count found
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstChar);
	
	/**
	 * Get a count of sponsors in the institutional collection with a name
	 * that starts with the specified first character in the given range.  
	 * NOTE: This search includes all sponsors in child collections
	 * 
	 * @param collection - institutional collection the sponsors should be in
	 * @param nameFirstCharRange - first character of the name start of range
	 * @param nameLastCharRange- first character of the name end of range
	 * 
	 * @return the count found
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstCharRange, char nameLastCharRange);
	

}
