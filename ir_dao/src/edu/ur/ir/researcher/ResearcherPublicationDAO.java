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
 * Interface for accessing and storing researcher publication information.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface ResearcherPublicationDAO  extends CrudDAO<ResearcherPublication>
{
	/**
	 * Get the root researcher publications for given researcher
	 * 
     * @param researcherId - the id of the researcher
	 * @return List of root publications found .
	 */
	public List<ResearcherPublication> getRootResearcherPublications(final Long researcherId);
   
    
	/**
	 * Get researcher publications for specified researcher and specified parent folder
	 * 
	 * @param researcherId - the id of the researcher
     * @param parentFolderId - the id of the parent folder 
     * 
	 * @return List of publications found.
	 */
	public List<ResearcherPublication> getSubResearcherPublications(final Long researcherId, final Long parentFolderId);
	
	
    /**
	 * Find the specified publications for the given researcher.
	 * 
	 * @param researcherId Researcher of the publication
	 * @param publicationIds Ids of  the publication
	 * 
	 * @return List of publications found
	 */
	public List<ResearcherPublication> getResearcherPublications(final Long researcherId, final List<Long> publicationIds);

	/**
	 * Get a count of the researcher publications containing this item
	 * 
	 * @param itemId Item id to search for in the researcher publications
	 * 
	 * @return Count of generic item found in researcher
	 */
	public Long getResearcherPublicationCount(Long itemId);
}
