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


package edu.ur.ir.researcher.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherDAO;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherPublication;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.order.OrderType;

/**
 * Default Service for dealing with the researchers page
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class DefaultResearcherService implements ResearcherService{
	
	public static final long USE_RESEARCHER_AS_ROOT = 0L;
	
	/** Data access for researcher */
	private ResearcherDAO researcherDAO;
	
	/** File system service for researcher file system information/actions */
	private ResearcherFileSystemService researcherFileSystemService;
	
	/**  Logger  */
	private static final Logger log = Logger.getLogger(DefaultResearcherService.class);

	
	/**
	 * Delete researcher and all related information within it.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#deleteResearcher(edu.ur.ir.researcher.Researcher)
	 */
	public void deleteResearcher(Researcher researcher) {
		log.debug("delete researcher called");
		// delete the researcher's root files
		LinkedList<ResearcherFile> rootFiles = new LinkedList<ResearcherFile>();
		rootFiles.addAll(researcher.getRootFiles());
		
		for(ResearcherFile rf : rootFiles)
		{
			researcher.removeRootFile(rf);
		    researcherFileSystemService.deleteFile(rf);
		}
		
		
		// delete the researcher's root files
		LinkedList<ResearcherPublication> rootPublications = new LinkedList<ResearcherPublication>();
		rootPublications.addAll(researcher.getRootPublications());
		for(ResearcherPublication publication : rootPublications)
		{
			researcher.removeRootPublication(publication);
			researcherFileSystemService.deletePublication(publication);
		    
		}

		// delete the researcher's root links
		LinkedList<ResearcherLink> rootLinks = new LinkedList<ResearcherLink>();
		rootLinks.addAll(researcher.getRootLinks());
		for(ResearcherLink link : rootLinks)
		{
			researcher.removeRootLink(link);
			researcherFileSystemService.deleteLink(link);
		}

		// Delete researcher's root folders
		LinkedList<ResearcherFolder> rootFolders = new LinkedList<ResearcherFolder>();
		rootFolders.addAll(researcher.getRootFolders());
		for(ResearcherFolder rootFolder : rootFolders)
		{
			researcher.removeRootFolder(rootFolder);
			researcherFileSystemService.deleteFolder(rootFolder);
		}
		
		researcherDAO.makeTransient(researcher);
	}
	
	/**
	 * Get the researcher by id.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcher(java.lang.Long, boolean)
	 */
	public Researcher getResearcher(Long id, boolean lock) {
		return researcherDAO.getById(id, lock);
	}

	/**
	 * Make the researcher persistent.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#saveResearcher(edu.ur.ir.researcher.Researcher)
	 */
	public void saveResearcher(Researcher researcher) {
		researcherDAO.makePersistent(researcher);
	}

	/**
	 * Get researcher dao
	 * 
	 * @return
	 */
	public ResearcherDAO getResearcherDAO() {
		return researcherDAO;
	}

	/**
	 * Set researcher dao
	 * 
	 * @param researcherDAO
	 */
	public void setResearcherDAO(ResearcherDAO researcherDAO) {
		this.researcherDAO = researcherDAO;
	}
	
    /**
     * Get all the researchers.
     * 
     * @see edu.ur.ir.researcher.ResearcherService#getAllResearchers()
     */
    @SuppressWarnings("unchecked")
	public List<Researcher> getAllResearchers() {
    	return researcherDAO.getAll();
    }

	/**
	 * Get all researchers having researcher page public
	 *  
     * @see edu.ur.ir.researcher.ResearcherService#getAllPublicResearchers()
     */
	public List<Researcher> getAllPublicResearchers() {
   		return researcherDAO.getAllPublicResearchers();
   }

	/**
	 * Get researchers starting from the specified row and end at specified row.
     * The rows will be sorted by the specified parameter in given order.
	 *  
     * @return List of researchers 
     */
	public List<Researcher> getResearchersByLastFirstName(int rowStart, int rowEnd, OrderType orderType)  {
		return researcherDAO.getResearchersByLastFirstName(rowStart, rowEnd, orderType);
	
	}
	
	/**
	 * Get only PUBLIC researchers starting from the specified row and end at specified row.
     * The rows will be sorted by the specified parameter in given order.
	 *  
     * @return List of researchers 
     */
	public List<Researcher> getPublicResearchersByLastFirstName(int rowStart, int rowEnd, OrderType orderType)  {
		return researcherDAO.getPublicResearchersByLastFirstName(rowStart, rowEnd, orderType);
	
	}


	/**
	 * Get a count of researchers.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearcherCount()
	 */
	public Long getResearcherCount() {
		return researcherDAO.getCount();
	}

	
	/**
	 * Get a count of public researchers.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getPublicResearcherCount()
	 */
	public Long getPublicResearcherCount() {
		return researcherDAO.getPublicResearcherCount();
	}

	/**
	 * Return the list of found researchers 
	 * 
	 * @see edu.ur.ir.researcher.ResearcherService#getResearchers(java.util.List)
	 */
	public List<Researcher> getResearchers(List<Long> researcherIds) {
		return researcherDAO.getResearchers(researcherIds);
	}

	public ResearcherFileSystemService getResearcherFileSystemService() {
		return researcherFileSystemService;
	}

	public void setResearcherFileSystemService(
			ResearcherFileSystemService researcherFileSystemService) {
		this.researcherFileSystemService = researcherFileSystemService;
	}
}
