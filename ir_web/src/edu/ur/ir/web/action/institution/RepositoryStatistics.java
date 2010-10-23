package edu.ur.ir.web.action.institution;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.ContentTypeCount;
import edu.ur.ir.item.SponsorService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.statistics.DownloadStatisticsService;
import edu.ur.ir.user.UserService;
import edu.ur.simple.type.AscendingNameComparator;

/**
 * Allows the viewing of repository information.
 * 
 * @author Nathan Sarr
 *
 */
public class RepositoryStatistics extends ActionSupport{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -7952557504407745226L;

	/** Number of collections in the repository */
	private Long numberOfCollections;

	/** Number of publications in the repository */
	private Long numberOfPublications;
	
	/** Number of file downloads in the repository */
	private Long numberOfFileDownloads;
	
	/** Number of users in the system */
	private Long numberOfUsers;
	
	/** number of researchers */
	private Long numberOfPublicResearchers;
	
	/** number of researchers  */
	private Long numberOfResearchers;
	
	/** Institutional Item data access */
	private InstitutionalItemService institutionalItemService;
	
	/** File Download Statistics data access */
	private DownloadStatisticsService downloadStatisticsService;
	
	/** Institutional Collection data access */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Service to get user information */
	private UserService userService;
	
	/** Service for dealing with researcher information*/
	private ResearcherService researcherService;
	
	/** get a count of content types */
	private List<ContentTypeCount> contentTypeCounts = new LinkedList<ContentTypeCount>();
	
	/** Service for sponsor information */
	private SponsorService sponsorService;

	/** count for the name of the sponsors */
	private Long sponsorCount;
	
	/** repository for the system  */
	private Repository repository;
		
	/** Used for sorting name based entities */
	private AscendingNameComparator nameComparator = new AscendingNameComparator();

	/**
     * Get the statistics information
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception 
    {
	
	    // get the statistics
	    numberOfCollections = institutionalCollectionService.getCount();
	    numberOfPublications = institutionalItemService.getDistinctInstitutionalItemCount();
	    numberOfFileDownloads = downloadStatisticsService.getNumberOfDownloadsForAllCollections();
	    numberOfUsers = userService.getUserCount();
	    numberOfResearchers = researcherService.getResearcherCount();
	    numberOfPublicResearchers = researcherService.getPublicResearcherCount();
	    
	    contentTypeCounts = institutionalItemService.getRepositoryContentTypeCount(Repository.DEFAULT_REPOSITORY_ID);
	    Collections.sort(contentTypeCounts, nameComparator);
	    
	    sponsorCount = sponsorService.getCount();
	    return SUCCESS;
    }
    
	public Long getNumberOfCollections() {
		return numberOfCollections;
	}


	public Long getNumberOfPublications() {
		return numberOfPublications;
	}


	public Long getNumberOfFileDownloads() {
		return numberOfFileDownloads;
	}


	public Long getNumberOfUsers() {
		return numberOfUsers;
	}


	public Long getNumberOfResearchers() {
		return numberOfResearchers;
	}


	public List<ContentTypeCount> getContentTypeCounts() {
		return contentTypeCounts;
	}


	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}


	public void setDownloadStatisticsService(
			DownloadStatisticsService downloadStatisticsService) {
		this.downloadStatisticsService = downloadStatisticsService;
	}


	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}

	public Long getSponsorCount() {
		return sponsorCount;
	}

	public void setSponsorService(SponsorService sponsorService) {
		this.sponsorService = sponsorService;
	}

	public Repository getRepository() {
		return repository;
	}
	
	public Long getNumberOfPublicResearchers() {
		return numberOfPublicResearchers;
	}

}
