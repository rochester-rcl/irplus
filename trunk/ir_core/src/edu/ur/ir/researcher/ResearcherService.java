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

package edu.ur.ir.researcher;

import java.util.List;

import edu.ur.order.OrderType;

/**
 * Interface for adding files, folders, publications and links to the researcher page
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public interface ResearcherService {
	
	/**
	 * Delete researcher and all related information within it.
	 * 
	 * @param id - unique id of the researcher
	 */
	public void deleteResearcher(Researcher researcher) ;
	
	/**
	 * Get the researcher by id.
	 * 
	 * @param id - id of the researcher
     * @param lock - upgrade the lock
     * 
     * @return the researcher if found otherwise null.
	 */
	public Researcher getResearcher(Long id, boolean lock) ;

	/**
	 * Make the researcher persistent.
	 * 
	 * @param researcher
	 */
	public void saveResearcher(Researcher researcher) ;

    /**
     * Get all the researchers.
     * 
     * @return all researchers
     */
	public List<Researcher> getAllResearchers();
	

	
	/**
	 * Get all researchers having researcher page public
	 *  
     * @return List of researchers found
     */
	public List<Researcher> getAllPublicResearchers();
	
	/**
	 * Get a count of researchers in the system.
	 * 
	 * @return a count of the researchers in the system
	 */
	public Long getResearcherCount();
	
	/**
	 * Get researchers starting from the specified row and end at specified row.
     * The rows will be sorted by the specified parameter in given order.
	 *  
     * @param rowStart Start row to fetch the data from
     * @param rowEnd End row to get data
     * @param orderType The order to sort by (ascending/descending)
     *
     * @return List of researchers 
     */	
	public List<Researcher> getResearchersByLastFirstName(int rowStart, int rowEnd, OrderType orderType) ;
	
	/**
	 * Get PUBLIC researchers starting from the specified row and end at specified row.
     * The rows will be sorted by the specified parameter in given order.
	 *  
     * @param rowStart Start row to fetch the data from
     * @param rowEnd End row to get data
     * @param orderType The order to sort by (ascending/descending)
     *
     * @return List of researchers 
     */	
	public List<Researcher> getPublicResearchersByLastFirstName(int rowStart, int rowEnd, OrderType orderType) ;

	
	/**
	 * Get a count of public researchers in the system.
	 * 
	 * @return
	 */
	public Long getPublicResearcherCount();

	/**
	 * Return the list of found researchers 
	 * 
	 * @param researcherIds Ids of researchers
	 * 
	 * @return List of researchers
	 */
	public List<Researcher> getResearchers(List<Long> researcherIds);
}
