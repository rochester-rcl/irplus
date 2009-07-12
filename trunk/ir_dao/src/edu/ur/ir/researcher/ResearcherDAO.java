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

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.order.OrderType;

/**
 * Data access for a researcher 
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface ResearcherDAO extends CountableDAO, 
CrudDAO<Researcher>
{
	/**
	 * Get a all researchers having public page.
	 * 
	 * @return List of researchers 
	 */
	public List<Researcher> getAllPublicResearchers();
	
	/**
	 * Get researchers starting from the specified row and end at specified row.
     * The rows will be sorted by the last name then first name
     *
	 * @param rowStart Start row to fetch the data from
     * @param rowEnd End row to get data
     * @param orderType The order to sort by (ascending/descending) 
     *
     * @return List of researchers 
     */
	public List<Researcher> getResearchersByLastFirstName(final int rowStart, final int maxResults, final OrderType orderType);

	/**
	 * Get ONLY PUBLIC researchers starting from the specified row and end at specified row.
     * The rows will be sorted by the last then first name in given order type .
     *
	 * @param rowStart Start row to fetch the data from
     * @param rowEnd End row to get data
     * @param orderType The order to sort by (ascending/descending) 
     *
     * @return List of public researchers 
     */
	public List<Researcher> getPublicResearchersByLastFirstName(final int rowStart, final int maxResults, final OrderType orderType);
	
    
 	/**
 	 * Gets a count of the number of public researchers.
 	 * 
 	 * @return - number of public researchers
 	 */
 	public Long getPublicResearcherCount();

	/**
	 * Get researchers by given ids
	 * 
	 * @param researcherIds Ids of researcher
	 * 
	 * @return List of researchers
	 */
	public List<Researcher> getResearchers(final List<Long> researcherIds);
}
