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

import java.util.List;

import edu.ur.dao.CriteriaHelper;

/**
 * Service for dealing with person information.
 * 
 * @author Nathan Sarr
 *
 */
public interface PersonService {
	
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
	public List<PersonNameAuthority> get(int rowStart, 
    		int numberOfResultsToShow, String sortElement, String sortType);

    /**
     * Get a count of people with given filter list.
     *  
     * @return - the number of persons found
     */
    public Long getCount();
    
    /**
     * Delete a person with the specified name.
     * 
     * @param id
     */
    public void delete(PersonNameAuthority p);
    
    /**
     * Get a person by id
     * 
     * @param id - unique id of the person.
     * @param lock - upgrade the lock on the data
     * @return - the found person or null if the person is not found.
     */
    public PersonNameAuthority getAuthority(Long id, boolean lock);
    
    /**
     * Save the person.
     * 
     * @param personNameAuthority
     */
    public void save(PersonNameAuthority personNameAuthority);
    
	/**
	 * Get people sorting according to the sort and filter information .  
	 * 
	 * Sort is applied based on the order of sort information in the list (1st to last).
	 * Starts at the specified row start location and stops at specified row end.
	 * 
	 * @param criteria - the criteria of how to sort and filter the information 
	 * @param rowStart - start position in paged set
	 * @param rowEnd - end position in paged set
	 * @param personId - id of the person to which the name belongs
	 * 
	 * @return List of people containing the specified information.
	 */
	public List<PersonName> get( final List<CriteriaHelper> criteriaHelpers,
			final int rowStart, final int rowEnd, Long personId);

    /**
     * Get a count of peerson names with given filter list.
     *  
     * @param criteria to apply to the selections
     * @param id of the person to which the names should belong.
     * 
     * @return - the number of collections found
     */
    public Integer getCount(final List<CriteriaHelper> criteriaHelpers, Long personId);
    
    /**
     * Delete a person name.
     * 
     * @param the person name to delete
     */
    public void delete(PersonName name);
    
    /**
     * Get a person name by id
     * 
     * @param id - unique id of the person name.
     * @param lock - upgrade the lock on the data
     * @return - the found person or null if the person is not found.
     */
    public PersonName getName(Long id, boolean lock);
    
    /**
     * Save the person name.
     * 
     * @param person name
     */
    public void save(PersonName name);

    /**
	 * Search the person name having the specified first and last name.
	 * 
	 * @param firstName first name to search for
	 * @param lastName last name to search for
	 * 
	 * @return List of Person names containing the specified first and last name.
	 */
	public List<PersonName> search(String firstName, String lastName);
}
