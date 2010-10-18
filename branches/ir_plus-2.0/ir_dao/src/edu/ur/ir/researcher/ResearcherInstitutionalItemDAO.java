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

import edu.ur.dao.CrudDAO;

/**
 * Interface for accessing and storing researcher institutional Item information.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface ResearcherInstitutionalItemDAO  extends CrudDAO<ResearcherInstitutionalItem>
{
	/**
	 * Get the root researcher institutional Items for given researcher
	 * 
     * @param researcherId - the id of the researcher
	 * @return List of root institutional Items found .
	 */
	public List<ResearcherInstitutionalItem> getRootResearcherInstitutionalItems(final Long researcherId);
   
    
	/**
	 * Get researcher institutional Items for specified researcher and specified parent folder
	 * 
	 * @param researcherId - the id of the researcher
     * @param parentFolderId - the id of the parent folder 
     * 
	 * @return List of institutional Items found.
	 */
	public List<ResearcherInstitutionalItem> getSubResearcherInstitutionalItems(final Long researcherId, final Long parentFolderId);
	
	
    /**
	 * Find the specified institutional Items for the given researcher.
	 * 
	 * @param researcherId Researcher of the institutional Item
	 * @param institutional ItemIds Ids of  the institutional Item
	 * 
	 * @return List of institutional Items found
	 */
	public List<ResearcherInstitutionalItem> getResearcherInstitutionalItems(final Long researcherId, final List<Long> institutionalItemIds);

	/**
	 * Get a count of the researcher institutional Items containing this item
	 * 
	 * @param itemId Item id to search for in the researcher institutional Items
	 * 
	 * @return Count of generic item found in researcher
	 */
	public Long getResearcherInstitutionalItemCount(Long itemId);

	/**
	 * Get researcher institutional Items containing this item
	 * 
	 * @param itemId Item id to search for in the researcher institutional Items
	 * 
	 * @return List of researcher Institutional item
	 */
	public List<ResearcherInstitutionalItem> getResearcherInstitutionalItem(Long itemId);

}
