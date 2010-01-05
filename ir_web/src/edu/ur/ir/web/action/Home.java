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


package edu.ur.ir.web.action;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.statistics.DownloadStatisticsService;
import edu.ur.simple.type.AscendingNameComparator;


/**
 * Home  Action - this is for loading a majority of the home page information.
 * 
 * @author Nathan Sarr
 *
 */
public class Home extends ActionSupport implements Preparable, UserIdAware{

	/**  Repository data access */
	private RepositoryService repositoryService;
	
	/** Institutional Collection data access */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Institutional Item data access */
	private InstitutionalItemService institutionalItemService;
	
	/** File Download Statistics data access */
	private DownloadStatisticsService downloadStatisticsService;
	
	/** The repository for this system  */
	private Repository repository;
	
	/**  Logger for editing files */
	private static final Logger log = Logger.getLogger(Home.class);

    /**  Eclipse generated serial id */
	private static final long serialVersionUID = -7590752368186801859L;
	
	/** User id */
	private Long userId;
	
	/** Number of collections in the repository */
	private Long numberOfCollections;

	/** Number of publications in the repository */
	private Long numberOfPublications;
	
	/** Number of file downloads in the repository */
	private Long numberOfFileDownloads;
	
	/** Set of institutional collections */
	private List<InstitutionalCollection> institutionalCollections = new LinkedList<InstitutionalCollection>();
	
	/** Used for sorting name based entities */
	private AscendingNameComparator nameComparator = new AscendingNameComparator();
	
	/**  Helper to get the next repository picture */
	private RandomRepositoryPictureHelper repositoryPictureHelper = new RandomRepositoryPictureHelper();
	
	/**  Current picture location */
	private int currentRepositoryPictureLocation = 0;
	
	/**  Ir file that should be shown. */
	private IrFile repositoryImageFile;
	
	/**  number of repository pictures */
	private int numRepositoryPictures;
	
	/** Service for dealing with researcher information*/
	private ResearcherService researcherService;
	
	/** helper for dealing with researcher information */
	private RandomResearcherPictureHelper researcherPictureHelper = new RandomResearcherPictureHelper();
	
	/**  Current picture location */
	private int currentResearcherLocation = 0;
	
	/** researchers found */
	private List<Researcher> researchers = new LinkedList<Researcher>();
	
	/**
	 * Prepare the ur published object
	 * 
	 * @see com.opensymphony.xwork.Preparable#prepare()
	 */
	public void prepare() {
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		log.debug("prepare called");
	}
	
	/**
     * A default implementation that does nothing and returns "success".
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception 
    {
    	log.debug("execute called");
    	// get the statistics
    	numberOfCollections = institutionalCollectionService.getCount();
		numberOfPublications = institutionalItemService.getDistinctInstitutionalItemCount();
		numberOfFileDownloads = downloadStatisticsService.getNumberOfDownloadsForAllCollections();
		
		// get and sort the institutional collections
		institutionalCollections.addAll(repository.getInstitutionalCollections());
		Collections.sort(institutionalCollections, nameComparator);
		
		// get the first repository image
		repositoryImageFile = repositoryPictureHelper.getNextPicture(RandomRepositoryPictureHelper.INIT, repository, currentRepositoryPictureLocation);
        currentRepositoryPictureLocation = repositoryPictureHelper.getCurrentRepositoryPictureLocation();
        numRepositoryPictures = repositoryPictureHelper.getNumRepositoryPictures();
        
        // get the first set of researchers
        researchers = researcherPictureHelper.getResearchers(RandomResearcherPictureHelper.INIT, researcherService, currentResearcherLocation);
        currentResearcherLocation = researcherPictureHelper.getCurrentResearcherLocation();
        
		return SUCCESS;
    }

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}
    
	public Long getNumberOfCollections() {
		return numberOfCollections;
	}

	public Long getNumberOfPublications() {
		return numberOfPublications;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public void setDownloadStatisticsService(
			DownloadStatisticsService downloadStatisticsService) {
		this.downloadStatisticsService = downloadStatisticsService;
	}

	public Long getNumberOfFileDownloads() {
		return numberOfFileDownloads;
	}

	public List<InstitutionalCollection> getInstitutionalCollections() {
		return institutionalCollections;
	}

	public void setInstitutionalCollections(
			List<InstitutionalCollection> institutionalCollections) {
		this.institutionalCollections = institutionalCollections;
	}
	

	public IrFile getRepositoryImageFile() {
		return repositoryImageFile;
	}

	public int getCurrentRepositoryPictureLocation() {
		return currentRepositoryPictureLocation;
	}

	public int getNumRepositoryPictures() {
		return numRepositoryPictures;
	}

	public ResearcherService getResearcherService() {
		return researcherService;
	}

	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}

	public int getCurrentResearcherLocation() {
		return currentResearcherLocation;
	}

	public List<Researcher> getResearchers() {
		return researchers;
	}


}
