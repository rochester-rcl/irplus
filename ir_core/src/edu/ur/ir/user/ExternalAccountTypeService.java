/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.user;

import java.io.Serializable;
import java.util.List;

import edu.ur.order.OrderType;


/**
 * Service for dealing with external account types.
 * 
 * @author Nathan Sarr
 *
 */
public interface ExternalAccountTypeService extends Serializable{
	
	/**
	 * Get external account types sorted according to the sort information .  
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - number of results to get
	 * @param orderType - Order by asc/desc
	 * 
	 * @return List of external account types.
	 */
	public List<ExternalAccountType> getExternalAccountTypesOrderByName( final int rowStart, 
    		final int numberOfResultsToShow, final OrderType orderType);

    /**
     * Get a count of external account types.
     *  
     * @return - the number of external account types found
     */
    public Long getCount();
    
    /**
     * Delete the external account type
     * 
     * @param external account type
     */
    public void delete(ExternalAccountType externalAccountType);
    
    /**
     * Get an external account type by name.
     * 
     * @param name - name of the external account type.
     * @return - the found external account type or null if the external account type is not found.
     */
    public ExternalAccountType get(String name);
    
    /**
     * Get an external account type by id
     * 
     * @param id - unique id of the external account type.
     * @param lock - upgrade the lock on the data
     * @return - the found external account type or null if the external account type is not found.
     */
    public ExternalAccountType get(Long id, boolean lock);
    
    /**
     * Save the external account type.
     * 
     * @param external account type
     */
    public void save(ExternalAccountType externalAccountType);
    
 
    /**
     * Get all external account types
     * 
     * @return List of all external account types
     */
    public List<ExternalAccountType> getAll();

}
