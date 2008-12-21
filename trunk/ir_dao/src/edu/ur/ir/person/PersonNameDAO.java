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
import edu.ur.dao.CriteriaHelper;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.NameListDAO;
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
	 * @param criteria - how to sort and filter the list
	 * @param rowStart - start position
	 * @param rowEnd - number of rows to grab.
	 * @param personId - id of the person to get the names for
	 * 
	 * @return list of person names found.
	 */
	public List<PersonName> getPersonNames( final List<CriteriaHelper> criteriaHelpers,
			final int rowStart, final int rowEnd, Long personId);
	
	/**
	 * Get the count of person names based on the filter criteria for the specified person
	 * 
	 * @param criteria - criteria to use to get the contributor types.
	 * @return count of people for the given criteria
	 */
	public Integer getPersonNamesCount(final List<CriteriaHelper> criteriaHelpers, Long personId);
	
	/**
	 * Searches for person name containing the given string
	 * 
	 * @param name the part of the name to be searched
	 * 
	 * @return List of person names containing the given search string
	 */
	public List<PersonName> searchPersonName(String name);
	
	
}
