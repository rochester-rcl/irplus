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

import edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMapping;
import edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingDAO;
import edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingService;

/**
 * Default service for handeling mapping.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultContributorTypeDublinCoreMappingService implements ContributorTypeDublinCoreMappingService{
	
	/**  Data access for contributor type dublin core */
	private ContributorTypeDublinCoreMappingDAO contributorTypeDublinCoreMappingDAO;

	/**
	 *  Delete the specified contributor type.
	 * @see edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingService#delete(edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMapping)
	 */
	public void delete(ContributorTypeDublinCoreMapping entity) {
		contributorTypeDublinCoreMappingDAO.makeTransient(entity);
	}

	/**
	 * Get all mappings
	 * 
	 * @see edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingService#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<ContributorTypeDublinCoreMapping> getAll() {
		return contributorTypeDublinCoreMappingDAO.getAll();
	}

	/**
	 * Get a mapping by id.
	 * 
	 * @see edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingService#getById(java.lang.Long, boolean)
	 */
	public ContributorTypeDublinCoreMapping getById(Long id, boolean lock) {
		return contributorTypeDublinCoreMappingDAO.getById(id, lock);
	}

	/**
	 * Get a count of contributor type dublin core mappings
	 * 
	 * @see edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingService#getCount()
	 */
	public Long getCount() {
		return contributorTypeDublinCoreMappingDAO.getCount();
	}

	/**
	 * Save a contributor type dublin core mapping
	 * 
	 * @see edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingService#save(edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMapping)
	 */
	public void save(ContributorTypeDublinCoreMapping entity) {
		contributorTypeDublinCoreMappingDAO.makePersistent(entity);
	}
	
	/**
	 * Get the mapping by contributor type id and dublin core element id.
	 * 
	 * @param contributorTypeId
	 * @param dublinCoreElementId
	 * 
	 * @return the mapping if found otherwise null
	 */
	public ContributorTypeDublinCoreMapping get(Long contributorTypeId, Long dublinCoreElementId)
	{
		return contributorTypeDublinCoreMappingDAO.get(contributorTypeId, dublinCoreElementId);
	}
	
	/**
	 * Get the mapping by contributor type id.
	 * 
	 * @param contributorTypeId
	 * 
	 * @return the mapping if found otherwise null
	 */
	public ContributorTypeDublinCoreMapping get(Long contributorTypeId)
	{
		return contributorTypeDublinCoreMappingDAO.get(contributorTypeId);
	}

	
	public ContributorTypeDublinCoreMappingDAO getContributorTypeDublinCoreMappingDAO() {
		return contributorTypeDublinCoreMappingDAO;
	}

	public void setContributorTypeDublinCoreMappingDAO(
			ContributorTypeDublinCoreMappingDAO contributorTypeDublinCoreMappingDAO) {
		this.contributorTypeDublinCoreMappingDAO = contributorTypeDublinCoreMappingDAO;
	}

}
