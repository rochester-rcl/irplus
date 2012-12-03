/**  
   Copyright 2008 - 2012 University of Rochester

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


package edu.ur.ir.repository.service;

import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryStatsCacheService;
import edu.ur.ir.statistics.DownloadStatisticsService;
import edu.ur.ir.user.UserService;

/**
 * Default implementation of the repository stats cache.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultRepositoryStatsCacheService implements RepositoryStatsCacheService{
	
	// total download count for the repository
	private Long downloadCount = 0l;
	
	// total item count for the repository
	private Long itemCount = 0l;
	
	// total collection count
	private Long collectionCount = 0l;
	
	// total number of users
	private Long userCount = 0l;
	
	// service to deal with item count
	private InstitutionalItemService institutionalItemService;
	
	// service to deal with download statsitcs
	private DownloadStatisticsService downloadStatisticsService;
	
	// service to deal with institutional colleciton count
	private InstitutionalCollectionService institutionalCollectionService;
	
	// service to get user count
	private UserService userService;


	public Long getDownloadCount(boolean forceSelect) {
		if( forceSelect || downloadCount == 0 ){
		    downloadCount = downloadStatisticsService.getNumberOfDownloadsForAllCollections(); 	
		}
		return downloadCount;
	}

	public Long getItemCount(boolean forceSelect) {
		if( forceSelect || itemCount == 0 ){
			itemCount = institutionalItemService.getCount(Repository.DEFAULT_REPOSITORY_ID);
		}
		return itemCount;
	}
	
	public Long getCollectionCount(boolean forceSelect) {
		if( forceSelect || itemCount == 0 ){
			collectionCount = institutionalCollectionService.getCount();
		}
		return collectionCount;
	}
	
	public Long getUserCount(boolean forceSelect) {
		if( forceSelect || userCount == 0 ){
			userCount = userService.getUserCount();
		}
		return userCount;
	}
	
	// force update on all counts 
	public void forceCacheUpdate(){
		getDownloadCount(true);
		getItemCount(true);
		getCollectionCount(true);
		getUserCount(true);
	}
	
	/**
	 * Set the institutional item service.
	 * 
	 * @param institutionalItemService
	 */
	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	/**
	 * Set the download statistics service.
	 * 
	 * @param downloadStatisticsService
	 */
	public void setDownloadStatisticsService(
			DownloadStatisticsService downloadStatisticsService) {
		this.downloadStatisticsService = downloadStatisticsService;
	}
	
	/**
	 * Set the institutional collection service.
	 * 
	 * @param institutionalCollectionService
	 */
	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

	/**
	 * Set the user service.
	 * 
	 * @param userSerivce
	 */
	public void setUserSerivce(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
