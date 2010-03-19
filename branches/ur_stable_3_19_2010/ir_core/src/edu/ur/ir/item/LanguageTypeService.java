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
 * Service class for dealing with language types.
 * 
 * @author Nathan Sarr
 *
 */
public interface LanguageTypeService {
	
	/**
	 * Get language types sorting according to the sort and filter information .  
	 * 
	 * Sort is applied based on the order of sort information in the list (1st to last).
	 * Starts at the specified row start location and stops at specified row end.
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - number of results to get
	 * @param sortType - Order by asc/desc

	 * @return List of language types.
	 */
	public List<LanguageType> getLanguageTypesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);

    /**
     * Get a count of language types 
     *  
     * @return - the number of language types
     */
    public Long getLanguageTypesCount();
    
    /**
     * Delete the specified language type.
     * 
     * @param languageType
     */
    public void delete(LanguageType languageType);
    
    /**
     * Get a language type by name.
     * 
     * @param name - name of the language type.
     * @return - the found language type or null if the language type is not found.
     */
    public LanguageType get(String name);
    
    /**
     * Get a language type by id
     * 
     * @param id - unique id of the language type.
     * @param lock - upgrade the lock on the data
     * @return - the found language type or null if the language type is not found.
     */
    public LanguageType get(Long id, boolean lock);
    
    /**
     * Save the language type.
     * 
     * @param languageType
     */
    public void save(LanguageType languageType);
	
    /**
     * Get all the lnague types.
     * 
     * @return
     */
    public List<LanguageType> getAll();	
    
    /**
     * Get the language type by it's unique system code.  Returns null if 
     * lanuage type is not found.
     * 
     * @param uniqueSystemCode
     */
    public LanguageType getByUniqueSystemCode(String uniqueSystemCode);

}
