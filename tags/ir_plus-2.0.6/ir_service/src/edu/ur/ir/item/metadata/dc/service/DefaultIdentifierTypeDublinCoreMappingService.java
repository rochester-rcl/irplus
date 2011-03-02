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

package edu.ur.ir.item.metadata.dc.service;

import java.util.List;

import edu.ur.ir.item.metadata.dc.IdentifierTypeDublinCoreMapping;
import edu.ur.ir.item.metadata.dc.IdentifierTypeDublinCoreMappingDAO;
import edu.ur.ir.item.metadata.dc.IdentifierTypeDublinCoreMappingService;

/**
 * @author NathanS
 *
 */
public class DefaultIdentifierTypeDublinCoreMappingService implements IdentifierTypeDublinCoreMappingService{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -4215186660078355311L;
	
	/** Identifier type dublin core mappign dao */
	private IdentifierTypeDublinCoreMappingDAO identifierTypeDublinCoreMappingDAO;



	/**
	 * Get a count of the mappings
	 * 
	 * @return
	 */
	public Long getCount()
	{
		return identifierTypeDublinCoreMappingDAO.getCount();
	}

	/**
	 * Get all mappings.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<IdentifierTypeDublinCoreMapping> getAll()
	{
	    return identifierTypeDublinCoreMappingDAO.getAll();	
	}

	/**
	 * Delete the mapping
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public IdentifierTypeDublinCoreMapping getById(Long id, boolean lock)
	{
		return identifierTypeDublinCoreMappingDAO.getById(id, lock);
	}

	/**
	 * Save the mapping.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void save(IdentifierTypeDublinCoreMapping entity)
	{
		identifierTypeDublinCoreMappingDAO.makePersistent(entity);
	}

	/**
	 * Delete the mapping.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void delete(IdentifierTypeDublinCoreMapping entity)
	{
		identifierTypeDublinCoreMappingDAO.makeTransient(entity);
	}

	/**
	 * Get the mapping by identifier type id and dublin core element id.
	 * 
	 * @param identifierTypeId
	 * @param dublinCoreElementId
	 * 
	 * @return the mapping if found otherwise null
	 */
	public IdentifierTypeDublinCoreMapping get(Long identifierTypeId, Long dublinCoreElementId)
	{
		return identifierTypeDublinCoreMappingDAO.get(identifierTypeId, dublinCoreElementId);
	}
	
	/**
	 * Get the mapping by identifier type id.
	 * 
	 * @param identifierTypeId
	 * 
	 * @return the mapping if found otherwise null
	 */
	public IdentifierTypeDublinCoreMapping get(Long identifierTypeId)
	{
		return identifierTypeDublinCoreMappingDAO.get(identifierTypeId);
	}
	
	public IdentifierTypeDublinCoreMappingDAO getIdentifierTypeDublinCoreMappingDAO() {
		return identifierTypeDublinCoreMappingDAO;
	}

	public void setIdentifierTypeDublinCoreMappingDAO(
			IdentifierTypeDublinCoreMappingDAO identifierTypeDublinCoreMappingDAO) {
		this.identifierTypeDublinCoreMappingDAO = identifierTypeDublinCoreMappingDAO;
	}


}
