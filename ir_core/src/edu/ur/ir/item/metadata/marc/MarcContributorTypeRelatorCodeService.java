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

package edu.ur.ir.item.metadata.marc;

import java.io.Serializable;
import java.util.List;

/**
 * Service interface to deal with marc
 * contributor type relator code data.
 *  
 * @author Nathan Sarr
 *
 */
public interface MarcContributorTypeRelatorCodeService extends Serializable{
	
	/**
	 * Get the contributor type relator code by contributor type id.
	 * 
	 * @param contentTypeId - id of the content type.
	 * @return the mapper if found otherwise null.
	 */
	public MarcContributorTypeRelatorCode getByContributorTypeId(Long contributorTypeId);
	
	/**
	 * Returns the list of contributor types that have the specified relator code.
	 * 
	 * @param relatorCode - relator code 
	 * @return the list of records found with the contributor type relator codes.
	 */
	public List<MarcContributorTypeRelatorCode> getByRelatorCode(String relatorCode);
	
	
	/**
	 * Get all of the contributor type relator codes
	 * @return all field mappers found
	 */
	public List<MarcContributorTypeRelatorCode> getAll();

	
	/**
	 * Get the marc contributor type relator code by id.
	 * 
	 * @param id - id of the marc content type field mapper
	 * @param lock - upgrade the lock on the data
	 * @return - the field mapper if found otherwise null.
	 */
	public MarcContributorTypeRelatorCode getById(Long id, boolean lock);

	
	/**
	 * Save the marc contributor type relator code
	 * 
	 * @param entity
	 */
	public void save(MarcContributorTypeRelatorCode entity);

	
	/**
	 * Delete the contributor type relator code.
	 * 
	 * @param entity
	 */
	public void delete(MarcContributorTypeRelatorCode entity);

}
