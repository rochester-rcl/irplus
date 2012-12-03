package edu.ur.ir.web.action.repository;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.institution.InstitutionalCollectionStatsCacheService;
import edu.ur.ir.repository.RepositoryStatsCacheService;


/**
 * @author Nathan Sarr
 *
 */
public class UpdateAllStatsCache extends ActionSupport {

	/* eclipse generated id */
	private static final long serialVersionUID = 6551923271349291516L;

	// service which caches counts for repository.
	private RepositoryStatsCacheService repositoryStatsCacheService;
	
	// service which caches counts for institutional collection 
	private InstitutionalCollectionStatsCacheService institutionalCollectionStatsCacheService;

	public void setInstitutionalCollectionStatsCacheService(
			InstitutionalCollectionStatsCacheService institutionalCollectionStatsCacheService) {
		this.institutionalCollectionStatsCacheService = institutionalCollectionStatsCacheService;
	}

	public String execute(){
		repositoryStatsCacheService.forceCacheUpdate();
		institutionalCollectionStatsCacheService.updateAllCollectionStats();
		return SUCCESS;
	}
	
	/**
	 * Set the repository stats cache service
	 * @param repositoryStatsCacheService
	 */
	public void setRepositoryStatsCacheService(
			RepositoryStatsCacheService repositoryStatsCacheService) {
		this.repositoryStatsCacheService = repositoryStatsCacheService;
	}
}
