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

import java.util.List;

import edu.ur.dao.CrudDAO;

/**
 * Data access for the mapping between a contributor type and relator code.
 * 
 * @author Nathan Sarr
 *
 */
public interface MarcContributorTypeRelatorCodeDAO extends CrudDAO<MarcContributorTypeRelatorCode>
{
	/**
	 * Get the mapping by contributor id.
	 * 
	 * @param contributorId - id of the contributor
	 * @return - the mapping otherwise null
	 */
	public MarcContributorTypeRelatorCode getByContributorType(Long contributorId);
	
	/**
	 * Get the mapping by relator code.
	 * 
	 * @param relatorCodeId - relator code to look for
	 * @return the mapping otherwise null.
	 */
	public MarcContributorTypeRelatorCode getByRelatorCode( Long relatorCodeId);
	
	/**
	 * Returns the list of contributor types that have the specified relator code.
	 * 
	 * @param relatorCode - relator code 
	 * @return the list of records found with the contributor type relator codes.
	 */
	public List<MarcContributorTypeRelatorCode> getByRelatorCode(String relatorCode);
	
}
