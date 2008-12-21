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


package edu.ur.ir.item.service;

import java.util.List;

import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeDAO;
import edu.ur.ir.item.IdentifierTypeService;

/**
 * Default service for dealing with identifier types.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultIdentifierTypeService implements IdentifierTypeService{
	
	/**  Content type data access. */
	private IdentifierTypeDAO identifierTypeDAO;


	/**
	 * Get the content type with the name.
	 * 
	 * @see edu.ur.ir.item.IdentifierTypeService#get(java.lang.String)
	 */
	public IdentifierType get(String name) {
		return identifierTypeDAO.findByUniqueName(name);
	}

	/**
	 * Get the content type by id.
	 * 
	 * @see edu.ur.ir.item.IdentifierTypeService#get(java.lang.Long, boolean)
	 */
	public IdentifierType get(Long id, boolean lock) {
		return identifierTypeDAO.getById(id, lock);
	}

	/**
	 * Get identifier types order by name
	 * 
	 * @see edu.ur.ir.item.IdentifierTypeService#getIdentifierTypesOrderByName(int, int, String)
	 */
	public List<IdentifierType> getIdentifierTypesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return identifierTypeDAO.getIdentifierTypesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	}
	
	/**
	 * Get the content types count.
	 * 
	 * @see edu.ur.ir.item.IdentifierTypeService#getCount()
	 */
	public Long getIdentifierTypesCount() {
		return identifierTypeDAO.getCount();
	}

	/**
	 * Content type data access.
	 * 
	 * @return
	 */
	public IdentifierTypeDAO getIdentifierTypeDAO() {
		return identifierTypeDAO;
	}

	/**
	 * Set the content type data access.
	 * 
	 * @param identifierTypeDAO
	 */
	public void setIdentifierTypeDAO(IdentifierTypeDAO identifierTypeDAO) {
		this.identifierTypeDAO = identifierTypeDAO;
	}

	/**
	 * Save the content type.
	 * 
	 * @see edu.ur.ir.item.IdentifierTypeService#save(edu.ur.ir.item.IdentifierType)
	 */
	public void save(IdentifierType contentType) {
		identifierTypeDAO.makePersistent(contentType);
	}

    /**
     * Get all identifier types
     * 
     * @see edu.ur.ir.item.IdentifierTypeService#getAll()
     */
	@SuppressWarnings("unchecked")
	public List<IdentifierType> getAll() {
		return identifierTypeDAO.getAll();
	}

	
	/**
	 * Delete the specified identifier type.
	 * 
	 * @see edu.ur.ir.item.IdentifierTypeService#delete(edu.ur.ir.item.IdentifierType)
	 */
	public void delete(IdentifierType identifierType) {
		identifierTypeDAO.makeTransient(identifierType);
	}

	
	/**
	 * Find the identifier by the unique system code.
	 * 
	 * @see edu.ur.ir.item.IdentifierTypeService#getByUniqueSystemCode(java.lang.String)
	 */
	public IdentifierType getByUniqueSystemCode(String uniqueSystemCode) {
	   return identifierTypeDAO.getByUniqueSystemCode(uniqueSystemCode);
	}
}
