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

import edu.ur.metadata.dc.DublinCoreTerm;
import edu.ur.metadata.dc.DublinCoreTermDAO;
import edu.ur.metadata.dc.DublinCoreTermService;

/**
 * @author Nathan Sarr
 *
 */
public class DefaultDublinCoreTermService implements DublinCoreTermService{

    /** Data access for dublin core term information  */
    private DublinCoreTermDAO dublinCoreTermDAO;
    


	/**
	 * Delete the dublin core term.
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreTermService#delete(edu.ur.metadata.dc.DublinCoreTerm)
	 */
	public void delete(DublinCoreTerm entity) 
	{
		dublinCoreTermDAO.makeTransient(entity);
	}

	/**
	 * Find the dublin core term by name
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreTermService#findByName(java.lang.String)
	 */
	public DublinCoreTerm findByName(String name) 
	{
		return dublinCoreTermDAO.findByUniqueName(name);
	}

	/**
	 * Get alll dublin core terms
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreTermService#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<DublinCoreTerm> getAll() {
		return dublinCoreTermDAO.getAll();
	}

	/**
	 * Get the dublin core term by id.
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreTermService#getById(java.lang.Long, boolean)
	 */
	public DublinCoreTerm getById(Long id, boolean lock) {
		return dublinCoreTermDAO.getById(id, lock);
	}

	/**
	 * Get the count of dublin core terms.
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreTermService#getCount()
	 */
	public Long getCount() {
		return dublinCoreTermDAO.getCount();
	}

	/**
	 * Save the dublin core term
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreTermService#save(edu.ur.metadata.dc.DublinCoreTerm)
	 */
	public void save(DublinCoreTerm entity) {
		dublinCoreTermDAO.makePersistent(entity);
	}
	
	/**
	 * Get the data access object.
	 * 
	 * @return
	 */
	public DublinCoreTermDAO getDublinCoreTermDAO() {
		return dublinCoreTermDAO;
	}

	/**
	 * Set the dublin core term data access object.
	 * 
	 * @param dublinCoreTermDAO
	 */
	public void setDublinCoreTermDAO(DublinCoreTermDAO dublinCoreTermDAO) {
		this.dublinCoreTermDAO = dublinCoreTermDAO;
	}

}
