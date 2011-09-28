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


package edu.ur.ir.index;

import java.io.Serializable;
import java.util.List;


/**
 * Interface to deal with index processing types.
 * 
 * @author Nathan Sarr
 *
 */
public interface IndexProcessingTypeService  extends Serializable {
	
	/**  default insert processing type */
	public static final String INSERT = "INSERT";
	
	/** default update processing type */
	public static final String UPDATE = "UPDATE";
	
	/** default delete processing type */
	public static final String DELETE = "DELETE";
	
    /**
     * Get a count of index processing types
     *  
     * @return - the number of index processing types found
     */
    public Long getCount();
    
    /**
     * Delete the index processing type 
     * 
     * @param  indexProcessingType
     */
    public void delete(IndexProcessingType indexProcessingType);    
    
    /**
     * Get an index processing type by name.
     * 
     * @param name - name of the copyright statement.
     * @return - the found index processing type or null if the index processint type is not found.
     */
    public IndexProcessingType get(String name);
    
    /**
     * Get an index processing type by id
     * 
     * @param id - unique id of the index processing type.
     * @param lock - upgrade the lock on the data
     * @return - the found index processing type or null if the index processing type is not found.
     */
    public IndexProcessingType get(Long id, boolean lock);
    
    /**
     * Save the index processing type
     * 
     * @param IndexProcessingType
     */
    public void save(IndexProcessingType indexProcessingType);
 
	/**
	 * Get all index processing types
	 * 
	 * @return List of all index processing types
	 */
	public List<IndexProcessingType> getAll();
	
}
