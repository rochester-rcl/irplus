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
 * Interface for accessing and storing researcher link information.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface ResearcherLinkDAO  extends CrudDAO<ResearcherLink>
{
	/**
	 * Get the root researcher links for given researcher
	 * 
     * @param researcherId - the id of the researcher
     * 
	 * @return List of root links found.
	 */	
	public List<ResearcherLink> getRootResearcherLinks(final Long researcherId);
    
	/**
	 * Get researcher links for specified researcher and specified parent folder
	 * 
	 * @param researcherId - the id of the researcher
     * @param parentFolderId - the id of the parent folder id for the folders
     * 
	 * @return List of links found.
	 */
	public List<ResearcherLink> getSubResearcherLinks(final Long researcherId, final Long parentFolderId);


    /**
	 * Find the specified links for the given researcher.
	 * 
	 * @param researcherId Researcher of the link
	 * @param linkIds Ids of  the link
	 * 
	 * @return List of links found
	 */
	public List<ResearcherLink> getResearcherLinks(final Long researcherId, final List<Long> linkIds);
	
	/**
	 * Find the folder for the specified researcherId and specified 
	 * folder name.
	 * 
	 * @param name of the collection
	 * @param researcherId - id of the researcher
	 * @return the found collection or null if the collection is not found.
	 */
	public ResearcherLink getRootResearcherLink(String name, Long researcherId);
	
	/**
	 * Find the researcher folder for the specified folder name and 
	 * parent id.
	 * 
	 * @param name of the folder
	 * @param parentId id of the parent folder
	 * @return the found folder or null if the folder is not found.
	 */
	public ResearcherLink getResearcherLink(String name, Long parentId);

}
