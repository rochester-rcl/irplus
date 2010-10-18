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

package edu.ur.ir.person;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.NameListDAO;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.order.OrderType;

import java.util.List;


/**
 * Person name persistance.
 * 
 * @author Nathan Sarr
 *
 */
public interface PersonNameDAO extends CountableDAO, 
CrudDAO<PersonName>, NameListDAO
{
	
	/**
	 * Returns all people with a first name like the specified value.
	 * 
	 * @param firstName
	 * @param startRecord
	 * @param numRecords
	 */
	public List<PersonName> findPersonLikeFirstName(String firstName, int startRecord, int numRecords);
	
	/**
	 * Finds all people with a last name like the speicified last name.
	 * 
	 * @param lastName
	 * @param startRecord
	 * @param numRecords
	 */
	public List<PersonName> findPersonLikeLastName(String lastName, int startRecord, int numRecords);
	
	
	/**
	 * Finds all people with the specified first and last name.
	 * 
	 * @param firstName
	 * @param lastName
	 * @param startRecord
	 * @param numRecords
	 * @return
	 */
	public List<PersonName> findPersonLikeFirstLastName(String firstName, String lastName, int startRecord, 
			int numRecords);

	/**
	 * Get the list of person names with the specified person id.
	 * 
	 * 
	 * @param rowStart - start position
	 * @param rowEnd - number of rows to grab.
	 * @param personId - id of the person to get the names for
	 * 
	 * @return list of person names found.
	 */
	public List<PersonName> getPersonNames( final int rowStart, final int rowEnd, Long personId);
	
	/**
	 * Get the count of person names based on the filter criteria for the specified person
	 * 
	 * @return count of people for the given criteria
	 */
	public Integer getPersonNamesCount(Long personId);
	
	/**
	 * Get a list of person names by first character of the last name
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param firstChar - first character that the last name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<PersonName> getPersonNamesByChar(final int rowStart,
			final int maxResults, 
			final char firstChar,
			final OrderType orderType);
	
	/**
	 * Get a list of person names that have last names that start between the specified characters
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param firstChar - first character in range that the first letter of the last name can have
	 * @param lastChar - last character in range that the first letter of the last name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<PersonName> getPersonNamesBetweenChar(final int rowStart,
			final int maxResults, 
			final char firstChar,
			final char lastChar,
			final OrderType orderType);
	
	/**
	 * Get a list of person names by last name.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of person names
	 */
	public List<PersonName> getPersonNamesByLastName(final int rowStart,
			final int maxResults, 
			final OrderType orderType);
	
	/**
	 * Get a list of items for a specified collection by first character of the name
	 * 
	 * NOTE: This search includes all person names in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param institutional collection - the institutional collection 
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of person names
	 */
	public List<PersonName> getCollectionPersonNamesByChar(final int rowStart,
			int maxResults, 
			InstitutionalCollection institutionalCollection,
			char firstChar,
			OrderType orderType);
	
	/**
	 * Get a list of person names for a specified collection last name first character
	 * that start between the specified characters
	 * 
	 * NOTE: This search includes all person names in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - the institutional collection 
	 * @param firstChar - first character in range that the first letter of the last name can have
	 * @param lastChar - last character in range that the first letter of the last name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of person names
	 */
	public List<PersonName> getCollectionPersonNamesBetweenChar(final int rowStart,
			int maxResults, 
			InstitutionalCollection collection, 
			char firstChar,
			char lastChar,
			OrderType orderType);
	
	/**
	 * Get a list of person name for a specified collection by last name.
	 * 
	 * NOTE: This search includes all person names in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collectionId - id of the collection to get person names from
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of person names
	 */
	public List<PersonName> getCollectionPersonNamesByLastName(final int rowStart,
			int maxResults, 
			InstitutionalCollection collection, 
			OrderType orderType);

	
	/**
	 * Get a count of  person name with a last name
	 * that starts with the specified first character.
	 * 
	 * @param lastNameFirstChar - first character of the last name
	 * 
	 * @return the count found
	 */
	public Long getCount(char nameFirstChar);
	
	/**
	 * Get a count of person names in the repository with a last name
	 * that starts with the specified first character in the given range.
	 * 
	 * @param lastNameFirstCharRange - first character of the name start of range
	 * @param lastNameLastCharRange- first character of the name end of range
	 * 
	 * @return the count found
	 */
	public Long getCount(char lastNameFirstCharRange, char lastNamelastCharRange);
	
	/**
	 * Get a count of person names in the institutional collection
	 * 
	 * NOTE: This search includes all person names in child collections
	 * 
	 * @param collection - institutional collection to return the count for
	 * 
	 * @return the count found
	 */
	public Long getCount(InstitutionalCollection collection);
	
	/**
	 * Get a count of person names in the institutional collection with a name
	 * that starts with the specified first character.
	 * 
	 * NOTE: This search includes all person names in child collections
	 * 
	 * @param lastNameFirstChar - first character of the name
	 * 
	 * @return the count found
	 */
	public Long getCount(InstitutionalCollection collection, char lastNameFirstChar);
	
	/**
	 * Get a count of person names in the institutional collection with a name
	 * that starts with the specified first character in the given range.  
	 * NOTE: This search includes all person names in child collections
	 * 
	 * @param collection - institutional collection the names should be in
	 * @param lastNameFirstCharRange - first character of the name start of range
	 * @param lastNameLastCharRange- first character of the name end of range
	 * 
	 * @return the count found
	 */
	public Long getCount(InstitutionalCollection collection, char lastNameFirstCharRange, char lastNamelastCharRange);

	
}
