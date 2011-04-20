/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.ir.marc.service;

import java.util.List;

import edu.ur.ir.marc.MarcContributorTypeRelatorCode;
import edu.ur.ir.marc.MarcContributorTypeRelatorCodeDAO;
import edu.ur.ir.marc.MarcContributorTypeRelatorCodeService;

/**
 * Default implementation of the marc contributor type relator code service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultMarcContributorTypeRelatorCodeService implements MarcContributorTypeRelatorCodeService {

	// eclipse generated id
	private static final long serialVersionUID = -2139773151196345434L;
	
	// marc contributor type relator code data access object
	private MarcContributorTypeRelatorCodeDAO marcContributorTypeRelatorCodeDAO;


	/**
	 * Delete the marc contributor type relator code.
	 * 
	 * @see edu.ur.ir.marc.MarcContributorTypeRelatorCodeService#delete(edu.ur.ir.marc.MarcContributorTypeRelatorCode)
	 */
	public void delete(MarcContributorTypeRelatorCode entity) {
		marcContributorTypeRelatorCodeDAO.makeTransient(entity);
	}

	/**
	 * Get all of the marc contributor type relator codes.
	 * 
	 * @see edu.ur.ir.marc.MarcContributorTypeRelatorCodeService#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<MarcContributorTypeRelatorCode> getAll() {
		return marcContributorTypeRelatorCodeDAO.getAll();
	}

	/**
	 * Get by the contributor type id.
	 * 
	 * @see edu.ur.ir.marc.MarcContributorTypeRelatorCodeService#getByContributorTypeId(java.lang.Long)
	 */
	public MarcContributorTypeRelatorCode getByContributorTypeId(
			Long contributorTypeId) {
		return marcContributorTypeRelatorCodeDAO.getByContributorType(contributorTypeId);
	}

	/**
	 * Get by id.
	 * 
	 * @see edu.ur.ir.marc.MarcContributorTypeRelatorCodeService#getById(java.lang.Long, boolean)
	 */
	public MarcContributorTypeRelatorCode getById(Long id, boolean lock) {
		return marcContributorTypeRelatorCodeDAO.getById(id, lock);
	}

	/**
	 * Save the contributor type relator code.
	 * 
	 * @see edu.ur.ir.marc.MarcContributorTypeRelatorCodeService#save(edu.ur.ir.marc.MarcContributorTypeRelatorCode)
	 */
	public void save(MarcContributorTypeRelatorCode entity) {
		marcContributorTypeRelatorCodeDAO.makePersistent(entity);
	}
	
	/**
	 * Set the marc contributor type relator code DAO.
	 * 
	 * @param marcContributorTypeRelatorCodeDAO
	 */
	public void setMarcContributorTypeRelatorCodeDAO(
			MarcContributorTypeRelatorCodeDAO marcContributorTypeRelatorCodeDAO) {
		this.marcContributorTypeRelatorCodeDAO = marcContributorTypeRelatorCodeDAO;
	}


}
