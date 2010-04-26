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

/**
 * Identifier type service interface for creating and getting content types.
 * 
 * @author Nathan Sarr
 *
 */
public interface IdentifierTypeService {
	
	/**
	 * Get identifier types sorted according to the sort information .  
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - number of results to get
	 * @param sortType - Order by asc/desc
	 * 
	 * @return List of identifier types.
	 */
	public List<IdentifierType> getIdentifierTypesOrderByName( final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);

    /**
     * Get a count of identifier types .
     *  
     * @return - the number of identifiers found
     */
    public Long getIdentifierTypesCount();
    
    /**
     * Delete the identifier type
     * 
     * @param id
     */
    public void delete(IdentifierType identifierType);
    
    /**
     * Get a identifier type by name.
     * 
     * @param name - name of the identifier type.
     * @return - the found identifier type or null if the identifier type is not found.
     */
    public IdentifierType get(String name);
    
    /**
     * Get a identifier type by id
     * 
     * @param id - unique id of the identifier type.
     * @param lock - upgrade the lock on the data
     * @return - the found identifier type or null if the identifier type is not found.
     */
    public IdentifierType get(Long id, boolean lock);
    
    /**
     * Save the identifier type.
     * 
     * @param identifierType
     */
    public void save(IdentifierType identifierType);
    
    /**
     * Get the identifier by it's unique system code
     * @param uniqueSystemCode
     */
    public IdentifierType getByUniqueSystemCode(String uniqueSystemCode);
 
    /**
     * Get all identifier types
     * 
     * @return List of all identifier types
     */
    public List<IdentifierType> getAll();
}
