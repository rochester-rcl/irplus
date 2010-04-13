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
package edu.ur.metadata.dc.service;

import java.util.List;

import edu.ur.metadata.dc.DublinCoreElement;
import edu.ur.metadata.dc.DublinCoreElementDAO;
import edu.ur.metadata.dc.DublinCoreElementService;

/**
 * Service to deal with Simple Dublin Core elements.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultDublinCoreElementService implements DublinCoreElementService {

	/**
	 * Dublin Core element data access object
	 */
	private DublinCoreElementDAO dublinCoreElementDAO;
	

	/**
	 * Delete the given dublin core element.
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreElementService#delete(edu.ur.metadata.dc.DublinCoreElement)
	 */
	public void delete(DublinCoreElement entity) {
		dublinCoreElementDAO.makeTransient(entity);
	}

	/**
	 * Find a dublin core element by name.
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreElementService#findByName(java.lang.String)
	 */
	public DublinCoreElement findByName(String name) {
		return dublinCoreElementDAO.findByUniqueName(name);
	}

	/**
	 * Get all dublin core elements by name
	 *  
	 * @see edu.ur.metadata.dc.DublinCoreElementService#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<DublinCoreElement> getAll() {
		return dublinCoreElementDAO.getAll();
	}

	/**
	 * Get a dublin core element by id.
	 * '
	 * @see edu.ur.metadata.dc.DublinCoreElementService#getById(java.lang.Long, boolean)
	 */
	public DublinCoreElement getById(Long id, boolean lock) {
		return dublinCoreElementDAO.getById(id, lock);
	}

	/**
	 * Get a count of the dublin core elements.
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreElementService#getCount()
	 */
	public Long getCount() {
		return dublinCoreElementDAO.getCount();
	}

	/**
	 * Save the dublin core element.
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreElementService#save(edu.ur.metadata.dc.DublinCoreElement)
	 */
	public void save(DublinCoreElement entity) {
		dublinCoreElementDAO.makePersistent(entity);
	}
	
	public DublinCoreElementDAO getDublinCoreElementDAO() {
		return dublinCoreElementDAO;
	}

	public void setDublinCoreElementDAO(DublinCoreElementDAO dublinCoreElementDAO) {
		this.dublinCoreElementDAO = dublinCoreElementDAO;
	}


}
