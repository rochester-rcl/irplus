/**  
   Copyright 2008 - 2011 University of Rochester

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

import edu.ur.order.OrderType;

/**
 * Service for dealing with place of publication information
 * 
 * @author Nathan Sarr
 *
 */
public interface PlaceOfPublicationService extends Serializable{
	
	/**
	 * Get place of publication types sorting according to the sort and filter information .  
	 * 
	 * Sort is applied based on the order of sort information in the list (1st to last).
	 * Starts at the specified row start location and stops at specified row end.
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - number of results to get
	 * @param sortType - Order by asc/desc

	 * @return List of language types.
	 */
	public List<PlaceOfPublication> getOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final OrderType orderType);

    /**
     * Get a count of place of publication
     *  
     * @return - the number places
     */
    public Long getCount();
    
    /**
     * Delete the specified language type.
     * 
     * @param placeOfPublication
     */
    public void delete(PlaceOfPublication placeOfPublication);
    
    /**
     * Get a place of publication by name.
     * 
     * @param name - name of the language type.
     * @return - the found language type or null if the language type is not found.
     */
    public PlaceOfPublication get(String name);
    
    /**
     * Get a place of publication by id
     * 
     * @param id - unique id of the language type.
     * @param lock - upgrade the lock on the data
     * @return - the found language type or null if the language type is not found.
     */
    public PlaceOfPublication get(Long id, boolean lock);
    
    /**
     * Save the place of publication
     * 
     * @param placeOfPublication
     */
    public void save(PlaceOfPublication placeOfPublication);
	
    /**
     * Get all the places of publication
     * 
     * @return all places of publication
     */
    public List<PlaceOfPublication> getAll();	
    
    
    /**
     * Get the place of publication by letter code.
     * 
     * @param letterCode - letter code for the place of publication
     * @return the place of publication
     */
    public List<PlaceOfPublication> getByLetterCode(String letterCode);
  

}
