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

import java.io.Serializable;
import java.util.List;

/**
 * Extent type service interface for creating and getting extent types.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface ExtentTypeService extends Serializable{
	
	/**
	 * Get extent types sorted according to order information .  
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - number of results to get
	 * @param sortType - Order by asc/desc
	 * 
	 * @return extent types found
	 */
	public List<ExtentType> getExtentTypesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);

    /**
     * Get a count of extent types .
     *  
     * @return - the number of extent types found
     */
    public Long getExtentTypesCount();
    
    /**
     * Delete a extent type with the specified name.
     * 
     * @param id
     */
    public boolean deleteExtentType(Long id);
    
    /** 
     * Delete the extent type with the specified name.
     * 
     * @param name
     */
    public boolean deleteExtentType(String name);
    
    /**
     * Get a extent type by name.
     * 
     * @param name - name of the extent type.
     * @return - the found extent type or null if the extent type is not found.
     */
    public ExtentType getExtentType(String name);
    
    /**
     * Get a extent type by id
     * 
     * @param id - unique id of the extent type.
     * @param lock - upgrade the lock on the data
     * @return - the found extent type or null if the extent type is not found.
     */
    public ExtentType getExtentType(Long id, boolean lock);
    
    /**
     * Save the extent type.
     * 
     * @param extentType
     */
    public void saveExtentType(ExtentType extentType);
 
    /**
     * Get all extent types
     * 
     * @return List of all extent types
     */
    public List<ExtentType> getAllExtentTypes();
}
