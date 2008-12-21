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

/**
 * Contributor type service interface for creating and getting content types.
 * 
 * @author Nathan Sarr
 *
 */
public interface ContributorTypeService {
	
	/**
	 * Get contributor types sorting according to the sort information .  
	 * 
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - end position in paged set
	 * @param sortType - Order by (asc/desc)
	 * @return List of people containing the specified information.
	 */
	public List<ContributorType> getContributorTypesOrderByName( final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);

    /**
     * Get a count of contributor types 
     *  
     * @return - the number of Contributor Types found
     */
    public Long getContributorTypesCount();
    
    /**
     * Delete a contributor type with the specified id.
     * 
     * @param id
     */
    public boolean delete(ContributorType contributorType);
    
    /**
     * Get a contributor type by name.
     * 
     * @param name - name of the contributor type.
     * @return - the found contributor type or null if the contributor type is not found.
     */
    public ContributorType get(String name);
    
    /**
     * Get a contributor type by it's system code
     * 
     * @param systemCode - system code for the contributor type.
     * @return - the found contributor type or null if the contributor type is not found.
     */
    public ContributorType getByUniqueSystemCode(String systemCode);
    
    /**
     * Get a contributor type by id
     * 
     * @param id - unique id of the contributor type.
     * @param lock - upgrade the lock on the data
     * @return - the found contributor type or null if the contributor type is not found.
     */
    public ContributorType get(Long id, boolean lock);
    
    /**
     * Save the contributor type.
     * 
     * @param contributorType
     */
    public void save(ContributorType contributorType);

    /**
     * Get all the contributor types.
     * 
     * @return List of all contributorTypes
     */
    public List<ContributorType> getAll();

}
