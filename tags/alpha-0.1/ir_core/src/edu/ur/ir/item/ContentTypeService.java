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
 * Content type service interface for creating and getting content types.
 * 
 * @author Nathan Sarr
 *
 */
public interface ContentTypeService {
	
	/**
	 * Get content types sorting according to the sort and filter information .  
	 * 
	 * Sort is applied based on the order of sort information in the list (1st to last).
	 * Starts at the specified row start location and stops at specified row end.
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - number of results to get
	 * @param sortType - Order by asc/desc
	 * 
	 * @return List of content types.
	 */
	public List<ContentType> getContentTypesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);

    /**
     * Get a count of content types 
     *  
     * @return - the number of content types found
     */
    public Long getContentTypesCount();
    
    /**
     * Delete a content type with the specified name.
     * 
     * @param id
     */
    public boolean deleteContentType(Long id);
    
    /** 
     * Delete the content type with the specified name.
     * 
     * @param name
     */
    public boolean deleteContentType(String name);
    
    /**
     * Get a content type by name.
     * 
     * @param name - name of the content type.
     * @return - the found content type or null if the content type is not found.
     */
    public ContentType getContentType(String name);
    
    /**
     * Get a content type by id
     * 
     * @param id - unique id of the content type.
     * @param lock - upgrade the lock on the data
     * @return - the found content type or null if the content type is not found.
     */
    public ContentType getContentType(Long id, boolean lock);
    
    /**
     * Save the content type.
     * 
     * @param contentType
     */
    public void saveContentType(ContentType contentType);
 
	/**
	 * Get all content types.
	 * 
	 * @return List of all content types
	 */
	public List<ContentType> getAllContentType();
	
	/**
	 * Get all content types by name asc order.
	 * 
	 * @return Content type by name order
	 */
	public List<ContentType> getAllContentTypeByNameOrder();

	   /**
     * Get a content type by it's system code
     * 
     * @param systemCode - system code for the content type.
     * @return - the found content type or null if the content type is not found.
     */
    public ContentType getByUniqueSystemCode(String systemCode);

}
