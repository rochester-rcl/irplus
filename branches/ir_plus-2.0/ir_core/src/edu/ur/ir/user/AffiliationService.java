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

package edu.ur.ir.user;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for managing with affiliation information.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface AffiliationService  extends Serializable{
	
	/**
	 * Get a count of the affiliations in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getAffiliationCount();
	
	/**
	 * Get all affiliations in name order.
	 */
	public List<Affiliation> getAllAffiliationsNameOrder();

	/**
	 * Get all affiliations in name order.
	 */
	public List<Affiliation> getAllAffiliationsOrderByName(int startRecord, int numRecords);

	/**
	 * Find the affiliation by unique name.
	 * 
	 * @param name - name of the affiliation.
	 * @return the affiliation or null if no affiliation was found.
	 */
	public Affiliation getAffiliation(String name);
	
	
	/**
	 * Get the list of affiliations. 
	 * 
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - end position in paged set
	 * @param sortType - Order by (asc/desc)	 * 
	 * @return List of users for the specified information.
	 */
	public List<Affiliation> getAffiliationsOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);

	/**
	 * Get all affiliations.
	 * 
	 * @return
	 */
	public List<Affiliation> getAllAffiliations();

	/**
	 * Get a affiliation by id.
	 * 
	 * @param id - id of the affiliation
	 * @param lock - lock mode
	 *  
	 * @return the Affiliation if found.
	 */
	public Affiliation getAffiliation(Long id, boolean lock);

	/**
	 * Make the affiliation information persistent.
	 * 
	 * @param affiliation to add/save
	 */
	public void makeAffiliationPersistent(Affiliation entity);

	/**
	 * Delete a affiliation from the system
	 * 
	 * @param affiliation to delete
	 */
	public void deleteAffiliation(Affiliation entity);
	
	/**
	 * Delete a set of affiliations from the system
	 * 
	 * @param affiliations to delete
	 */
	public void deleteAffiliations(List<Affiliation> affiliations);

}
