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

import java.io.Serializable;
import java.util.List;

import edu.ur.order.OrderType;

/**
 * Service for dealing with person information.
 * 
 * @author Nathan Sarr
 *
 */
public interface PersonService extends Serializable{
	
	/**
	 * Get a list of person name authorities ordered by the authoritative last name.
	 * 
	 * @param rowStart - row start 
	 * @param maxResults - maximum number of results
	 * @param orderType - order type (asc/desc)
	 * 
	 * @return the set of person name authorities found starting at row start and up to max results will
	 * be returned.
	 */
	public List<PersonNameAuthority> getPersonNameAuthorityByLastName(int rowStart,
			int maxResults, OrderType orderType);

    /**
     * Get a count of person name authorities.
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
	public List<PersonName> get(final int rowStart, final int rowEnd, Long personId);

    /**
     * Get a count of peerson names with given filter list.
     *  
     * @param id of the person to which the names should belong.
     * 
     * @return - the number of collections found
     */
    public Integer getCount(Long personId);
    
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
	 * Get the person name authority identifier.
	 * 
	 * @param id - of the person name authority identifier
	 * @param lock
	 * @return
	 */
	public PersonNameAuthorityIdentifier getPersonNameAuthorityIdentifier(Long id, boolean lock);
	
	/**
	 * Delete the person name authority identifier
	 * 
	 * @param personNameAuthorityIdentifier
	 */
	public void deletePersonNameAuthority(PersonNameAuthorityIdentifier personNameAuthorityIdentifier);
	
	/**
	 * Get unused authority types available for the specified authority id.
	 * 
	 * @param authorityId - unique authority id of the person name
	 * @return
	 */
	public List<PersonNameAuthorityIdentifierType> getPossibleIdentifierTypes(Long authorityId);
	
	/**
	 * Get the identifier for the specified person name authority if it exists.
	 * 
	 * @param identifierTypeId - id for the type of identifier
	 * @param personNameAuthorityId - id for the person name authority
	 * 
	 * @return - person name authority identifier
	 */
	public PersonNameAuthorityIdentifier getByTypeAuthority(Long identifierTypeId, Long personNameAuthorityId);
	
	
	
	/**
	 * Find all any person name identifiers with the specified value
	 *  
	 * @param identfierTypeId - identifier type id
	 * @param value - value of the identifier
	 * @return
	 */
	public PersonNameAuthorityIdentifier getByTypeValue(Long identfierTypeId, String value);
 
}
