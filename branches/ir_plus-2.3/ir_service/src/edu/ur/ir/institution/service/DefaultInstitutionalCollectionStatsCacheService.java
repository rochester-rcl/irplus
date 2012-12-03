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


package edu.ur.ir.institution.service;

import java.util.HashMap;
import java.util.List;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalCollectionStatsCacheService;
import edu.ur.ir.statistics.DownloadStatisticsService;

/**
 * Default implementation for institutional repository stats caching.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultInstitutionalCollectionStatsCacheService implements 
    InstitutionalCollectionStatsCacheService{

	/* list of cached collection info */
	private HashMap<Long, CollectionStatsInfo> collectionInfos = new HashMap<Long, CollectionStatsInfo>();
	
	/* service to deal with collection information */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/* service to deal with collection information */
	private DownloadStatisticsService downloadStatisticsService;
	

	public Long getCollectionCount(InstitutionalCollection collection, boolean forceSelect) {
		long value = 0l;
		if( collection != null ){
		    CollectionStatsInfo info = this.getInfo(collection.getId());
		    if( info.getCollectionCount() == 0 || forceSelect)
		    {
			    info.setCollectionCount(institutionalCollectionService.getTotalSubcollectionCount(collection));
			}
		    value = info.getCollectionCount();
		}
		return value;
	}

	public Long getDownloadCount(InstitutionalCollection collection, boolean forceSelect) {
		long value = 0l;
		if( collection != null ){
		    CollectionStatsInfo info = this.getInfo(collection.getId());
		    if( info.getDownloadCount() == 0 || forceSelect)
		    {
			    info.setDownloadCount(downloadStatisticsService.getNumberOfDownloadsForCollection(collection));
			}
		    value = info.getDownloadCount();
		}
		return value;
	}

	public Long getDownloadCountWithChildren(InstitutionalCollection collection,
			boolean forceSelect) {
		long value = 0l;
		if( collection != null ){
		    CollectionStatsInfo info = this.getInfo(collection.getId());
		    if( info.getDownloadCountWithChildren() == 0 || forceSelect)
		    {
			    info.setDownloadCountWithChildren(downloadStatisticsService.getDownloadCountWithChildren(collection));
			}
		    value = info.getDownloadCountWithChildren();
		}
		return value;
	}

	@Override
	public Long getItemCount(InstitutionalCollection collection, boolean forceSelect) {
		long value = 0l;
		if( collection != null ){
		    CollectionStatsInfo info = this.getInfo(collection.getId());
		    if( info.getItemCount() == 0 || forceSelect)
		    {
			    info.setItemCount(institutionalCollectionService.getItemCount(collection));
			}
		    value = info.getItemCount();
		}
		return value;
	}
	
	/**
	 * Get the total item count in the collection including child collections
	 * 
	 * @param forceSelect - if true will perform a select against the database and update results
	 * @return the total item count with children
	 */
	public Long getItemCountWithChildren(InstitutionalCollection collection, boolean forceSelect){
		long value = 0l;
		if( collection != null ){
		    CollectionStatsInfo info = this.getInfo(collection.getId());
		    if( info.getItemCountWithChildren() == 0 || forceSelect)
		    {
			    info.setItemCountWithChildren(institutionalCollectionService.getItemCountWithChildren(collection));
			}
		    value = info.getItemCountWithChildren();
		}
		return value;
	}



	@Override
	public void updateAllCollectionStats() {
		List<InstitutionalCollection> collections = institutionalCollectionService.getAll();
		for( InstitutionalCollection c : collections){
			updateCollectionCache(c);
		}
	}

	@Override
	public void updateChildToParentCollectionStats(InstitutionalCollection collection) {
		// update all collections from the parent down to the child that
		// is being updated
		List<InstitutionalCollection> parents = 
			institutionalCollectionService.getPath(collection);
		for( InstitutionalCollection c : parents){
			updateCollectionCache(c);
		}
	}
	
	private void updateCollectionCache(InstitutionalCollection collection){
		getCollectionCount(collection, true);
		getDownloadCount(collection, true);
		getDownloadCountWithChildren(collection, true);
		getItemCount(collection, true);
		getItemCountWithChildren(collection, true);
	}
	
	private synchronized CollectionStatsInfo getInfo(Long collectionId){
		// hit has first
		CollectionStatsInfo info = collectionInfos.get(collectionId);
		if( info == null ){
			info = new CollectionStatsInfo(collectionId);
			collectionInfos.put(collectionId, info);
		}
		return info;
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
	 * Set the download statistics service.
	 * 
	 * @param downloadStatisticsService
	 */
	public void setDownloadStatisticsService(
			DownloadStatisticsService downloadStatisticsService) {
		this.downloadStatisticsService = downloadStatisticsService;
	}

	
	/*
	 * Class to hold each collections stats information
	 */
	private class CollectionStatsInfo{
		
		private Long id;
	

		private Long downloadCount = 0l;
		private Long downloadCountWithChildren = 0l;
		private Long itemCount = 0l;
		private Long itemCountWithChildren = 0l;
		private Long collectionCount = 0l;
		


		public CollectionStatsInfo(Long id)
		{
			this.id = id;
		}
		
		public Long getDownloadCount() {
			return downloadCount;
		}

		public void setDownloadCount(Long downloadCount) {
			this.downloadCount = downloadCount;
		}

		public Long getDownloadCountWithChildren() {
			return downloadCountWithChildren;
		}

		public void setDownloadCountWithChildren(Long downloadCountWithChildren) {
			this.downloadCountWithChildren = downloadCountWithChildren;
		}

		public Long getItemCount() {
			return itemCount;
		}

		public void setItemCount(Long itemCount) {
			this.itemCount = itemCount;
		}

	
		public Long getCollectionCount() {
			return collectionCount;
		}

		public void setCollectionCount(Long collectionCount) {
			this.collectionCount = collectionCount;
		}
		
		public Long getItemCountWithChildren() {
			return itemCountWithChildren;
		}

		public void setItemCountWithChildren(Long itemCountWithChildren) {
			this.itemCountWithChildren = itemCountWithChildren;
		}
		
		public Long getId() {
			return id;
		}
	}
	
	

}
