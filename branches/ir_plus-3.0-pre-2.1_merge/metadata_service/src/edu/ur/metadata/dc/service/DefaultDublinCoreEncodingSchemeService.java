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

import edu.ur.metadata.dc.DublinCoreEncodingScheme;
import edu.ur.metadata.dc.DublinCoreEncodingSchemeDAO;
import edu.ur.metadata.dc.DublinCoreEncodingSchemeService;

/**
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultDublinCoreEncodingSchemeService implements DublinCoreEncodingSchemeService{

	/** eclipse generated id */
	private static final long serialVersionUID = 9110130304125058877L;
	
	/** Data access for dublin core encoding schemes */
	private DublinCoreEncodingSchemeDAO dublinCoreEncodingSchemeDAO;
	
	/**
	 * Delete the encoding scheme.
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreEncodingSchemeService#delete(edu.ur.metadata.dc.DublinCoreEncodingScheme)
	 */
	public void delete(DublinCoreEncodingScheme entity) {
		dublinCoreEncodingSchemeDAO.makeTransient(entity);
	}

	/**
	 * Find the encoding scheme by name.
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreEncodingSchemeService#findByName(java.lang.String)
	 */
	public DublinCoreEncodingScheme findByName(String name) {
		return dublinCoreEncodingSchemeDAO.findByUniqueName(name);
	}

	/**
	 * Get all encoding schemes.
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreEncodingSchemeService#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<DublinCoreEncodingScheme> getAll() {
		return dublinCoreEncodingSchemeDAO.getAll();
	}

	/**
	 * Get the dublin core encoding scheme by id.
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreEncodingSchemeService#getById(java.lang.Long, boolean)
	 */
	public DublinCoreEncodingScheme getById(Long id, boolean lock) {
		return dublinCoreEncodingSchemeDAO.getById(id, lock);
	}

	/**
	 * Get a count of the dublin core encoding schemes.
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreEncodingSchemeService#getCount()
	 */
	public Long getCount() {
		return dublinCoreEncodingSchemeDAO.getCount();
	}

	/**
	 * Save the encoding scheme
	 * 
	 * @see edu.ur.metadata.dc.DublinCoreEncodingSchemeService#save(edu.ur.metadata.dc.DublinCoreEncodingScheme)
	 */
	public void save(DublinCoreEncodingScheme entity) {
		dublinCoreEncodingSchemeDAO.makePersistent(entity);
	}
	
	public DublinCoreEncodingSchemeDAO getDublinCoreEncodingSchemeDAO() {
		return dublinCoreEncodingSchemeDAO;
	}

	public void setDublinCoreEncodingSchemeDAO(
			DublinCoreEncodingSchemeDAO dublinCoreEncodingSchemeDAO) {
		this.dublinCoreEncodingSchemeDAO = dublinCoreEncodingSchemeDAO;
	}

}
