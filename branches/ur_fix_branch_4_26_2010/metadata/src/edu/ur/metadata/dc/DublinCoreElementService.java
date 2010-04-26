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
package edu.ur.metadata.dc;

import java.util.List;

/**
 * Service to help with dublin core element access
 * 
 * @author Nathan Sarr
 *
 */
public interface DublinCoreElementService {
	
	
	/**
	 * Get a count of dublin core elements.
	 * 
	 * @return the count of dublin core elements
	 */
	public Long getCount();

	/**
	 * Get all dublinc core elements.
	 * 
	 * @return - all dublin core elements.
	 */
	public List<DublinCoreElement> getAll();

	/**
	 * Get a dublin core element by id.
	 * 
	 * @param id - id of the dublin core element
	 * @param lock - upgrade the lock
	 * 
	 * @return the found dublin core element
	 */
	public DublinCoreElement getById(Long id, boolean lock);

	/**
	 * Save the dublin core element.
	 * 
	 * @param entity - save the dublin core element
	 */
	public void save(DublinCoreElement entity);

	/**
	 * Delete the dublin core element.
	 * 
	 * @param entity - the dublin core element
	 */
	public void delete(DublinCoreElement entity);

	/**
	 * Find a dublin core element by its unique name.
	 * 
	 * @param name - name of the element
	 * @return - found dublin core element
	 */
	public DublinCoreElement findByName(String name);

}
