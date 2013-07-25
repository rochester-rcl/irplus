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
 * Publisher service interface for creating and getting publisher.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface PublisherService  extends Serializable{
	
	/**
	 * Get publisher sorted according to the sort information .  
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - number of results to get
	 * @param sortType - Order by asc/desc
	 *
	 * @return List of publishers
	 */
	public List<Publisher> getPublishersOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);

    /**
     * Get a count of publisher .
     *  
     * @return - the number of publishers found
     */
    public Long getPublishersCount();
    
    /**
     * Delete a publisher with the specified name.
     * 
     * @param id
     */
    public boolean deletePublisher(Long id);

    /**
     * Get a publisher by id
     * 
     * @param id - unique id of the publisher .
     * @param lock - upgrade the lock on the data
     * @return - the found publisher or null if the publisher is not found.
     */
    public Publisher getPublisher(Long id, boolean lock);
    
    /**
     * Save the publisher .
     * 
     * @param publisher
     */
    public void savePublisher(Publisher publisher );
 
	/**
	 * Get all publisher.
	 * 
	 * @return List of all publisher 
	 */
	public List<Publisher> getAllPublisher();
	
    /** 
     * Delete the publisher with the specified name.
     * 
     * @param name
     */
    public boolean deletePublisher(String name);
    
    /**
     * Get a Publisher by name.
     * 
     * @param name - name of the Publisher .
     * @return - the found Publisher or null if the Publisher is not found.
     */
    public Publisher getPublisher(String name);


}
