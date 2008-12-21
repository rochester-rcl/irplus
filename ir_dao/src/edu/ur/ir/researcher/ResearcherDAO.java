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
     * The rows will be sorted by the specified parameter in given order.
     *
	 * @param rowStart Start row to fetch the data from
     * @param rowEnd End row to get data
     * @param propertyName The property to sort on
     * @param orderType The order to sort by (ascending/descending) 
     *
     * @return List of researchers 
     */
	public List<Researcher> getResearchers(final int rowStart, final int maxResults, final String propertyName, final String orderType) ;

 	/**
 	 * Get the researchers ordered by last name then first name starting at the given offset.
 	 * 
 	 * @param offset - offset to start getting the researchers
 	 * @param maxNumToFetch - maximum number of researchers to fetch
 	 * @return list of found researchers
 	 */
 	public List<Researcher>  getPublicResearchersOrderedByLastFirstName( final int offset, final int maxNumToFetch);

    
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
