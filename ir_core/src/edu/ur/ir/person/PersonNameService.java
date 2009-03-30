package edu.ur.ir.person;

import java.util.List;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.order.OrderType;

/**
 * Service for dealing with person name information.  See also
 * person service.
 * 
 * @author Nathan Sarr
 *
 */
public interface PersonNameService {
	
	/**
	 * Get a count of all person names in the system.
	 * 
	 * @return count of all person names in the system
	 */
	public Long getCount();
	
	/**
	 * Get a count of all person names in the system with
	 * specified last name first letter characters 
	 * 
	 * @param lastNameFirstChar - first character in the last name of the person name
	 * @return count of person names found
	 */
	public Long getCount(char lastNameFirstChar);
	
	/**
	 * Get a count of all person names in the system with
	 * specified first characters 
	 * 
	 * @param lastNameFirstCharRange - first character in range
	 * @param lastNamelastCharRange - last character in the range
	 * 
	 * @return count of person names found that have a last name with the 
	 * first character in the specified range
	 */
	public Long getCount(char lastNameFirstCharRange, char lastNamelastCharRange);
	
	/**
	 * Get a count of all person names in the specified collection with
	 * specified first character in the last name.  THIS INCLUDES person names in child collections 
	 * 
	 * @param institutional collection - parent collection
	 * @param lastNameFirstChar - first character in the last name of the person name
	 * @return count of  person names found
	 */
	public Long getCount(InstitutionalCollection collection, char lastNameFirstChar);
	
	/**
	 * Get a count of all institutional  person names in the specified collection with
	 * specified first character -  THIS INCLUDES person names in child collections 
	 * 
	 * @param institutional collection - parent collection
	 * @param lastNameFirstCharRange - last name first character in range
	 * @param lastNameLastCharRange - last name with character in the range
	 * 
	 * @return count of titles found that have a first character in the specified range
	 */
	public Long getCount(InstitutionalCollection collection, char lastNameFirstCharRange, char nameLastCharRange);
	

	/**
	 * Get a list of person names that have last names
	 * that start between the specified characters
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param lastNamefirstChar - first character in range that the first letter of the last name can have
	 * @param lastNamelastChar - last character in range that the first letter of the last name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of person names
	 */
	public List<PersonName> getPersonNamesBetweenChar(final int rowStart,
			int maxResults, 
			char lastNamefirstChar,
			char lastNamelastChar,
			OrderType orderType);
	
	/**
	 * Get a list of person names ordered by name in the system.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param rowEnd -  End row to get data
	 * @param propertyName - The property to sort on
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of person names
	 */
	public List<PersonName> getPersonNamesOrderByLastName(int rowStart, int rowEnd,  
			OrderType orderType) ;	
	
	/**
	 * Get a list of person names in the system by first character of the last name
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of person names
	 */
	public List<PersonName> getPersonNamesByLastNameChar(int rowStart,
			int maxResults, 
			char firstChar,
			OrderType orderType);
		

	
	/**
	 * Get the list of person names for the specified collection.  This includes person names in sub collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param rowEnd -  End row to get data
	 * @param Institutional collection -   collection to get person names
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of person names
	 */
	public List<PersonName> getCollectionPersonNamesOrderByLastName(int rowStart, 
			int rowEnd, 
			InstitutionalCollection collection, 
			OrderType orderType) ;
	
	/**
	 * Get a list of person names for a specified collection by first character of the last name.  
	 * This INCLUDES person names in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param Institutionalcollection - parent collection
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of person names
	 */
	public List<PersonName> getPersonNamesByChar(final int rowStart,
			int maxResults, 
			InstitutionalCollection institutionalCollection,
			char firstChar,
			String orderType);
	
	/**
	 * Get a list of person names for a specified collection by  person last names
	 * that start between the specified first character in the last name.  This 
	 * INCLUDES person names in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param Institutional Collection - parent collection 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of person names
	 */
	public List<PersonName> getCollectionPersonNamesBetweenChar(int rowStart,
			int maxResults, 
			InstitutionalCollection institutionalCollection,
			char firstChar,
			char lastChar,
			OrderType orderType);
	
}
