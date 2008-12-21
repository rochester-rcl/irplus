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
import java.util.List;


/**
 * Interface for persisting base person information.
 * 
 * @author Nathan Sarr
 *
 */
public interface PersonNameAuthorityDAO extends CountableDAO, 
CrudDAO<PersonNameAuthority>{
	
	/**
	 * Gets a list of all users with in authoritative name order.
	 * 
	 * @param startRecord
	 * @param numRecords
	 * 
	 * @return users in given order
	 */
	public List<PersonNameAuthority> getAllAuthoritativeNameAsc(int startRecord, int numRecords);

	
	/**
	 * Get the list of people.
	 * 
	 * 
	 * @param criteria - how to sort and filter the list
	 * @param rowStart - start position
	 * @param rowEnd - number of rows to grab.
	 * 
	 * @return list of people found.
	 */
	public List<PersonNameAuthority> getPersons( final List<CriteriaHelper> criteriaHelpers,
			final int rowStart, final int rowEnd);
	
	/**
	 * Get the count of people based on the filter criteria
	 * 
	 * @param criteria - criteria to use to get the contributor types.
	 * @return count of people for the given criteria
	 */
	public Integer getPersonsCount(final List<CriteriaHelper> criteriaHelpers);

	/**
	 * Get a list of person authorities for a specified sort criteria.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param numberOfResultsToShow - maximum number of results to fetch
	 * @param sortElement - column to sort on 
	 * @param sortType - The order to sort by (ascending/descending)
	 * 
	 * @return List of person authorities
	 */
	public List<PersonNameAuthority> getPersons(final int rowStart, 
    		final int numberOfResultsToShow, final String sortElement, final String sortType);
}
