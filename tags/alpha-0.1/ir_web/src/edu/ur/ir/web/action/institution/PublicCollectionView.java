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

package edu.ur.ir.web.action.institution;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.statistics.DownloadStatisticsService;

public class PublicCollectionView extends ActionSupport {

	/**  Eclipse generated id */
	private static final long serialVersionUID = 7238213751595032189L;

	/**  Logger. */
	private static final Logger log = Logger.getLogger(PublicCollectionView.class);

	/** Repository service for dealing with institutional repository information */
	RepositoryService repositoryService;
	
	/** Id of the collection */
	private InstitutionalCollection institutionalCollection;
	
	/** Id of the collection */
	private Long collectionId;
	
	/** Get the picture at the specified location */
	int pictureLoctation;
	
	/** set of institutional collections that are the path for the current 
	 * institutional collection
	 */
    private Collection <InstitutionalCollection> collectionPath;
    
	/** Institutional Collection service */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Download Statistics service */
	private DownloadStatisticsService downloadStatisticsService;
	
	/** Number of items within a collection and its sub-collection */
	private Long institutionalItemCount;
	
	/** Number of items within a collection */
	private Long institutionalItemsCountForACollection;
	
	/** Count of sub collections */
	private int subcollectionCount;
	
	/** Count of the subcollection and its children */
	private Long allSubcollectionCount;
	
	/** File download count for this collection */
	private Long fileDownloadCountForCollection; 

	/** File download count for this collection */
	private Long fileDownloadCountForCollectionAndItsChildren; 

	public String getNextPicture()
	{
		log.debug("get picture called");
		institutionalCollection = institutionalCollectionService.getCollection(collectionId, false);
		return SUCCESS;
	}
	
	/**
	 * View the institutional collection.
	 * 
	 * @return
	 */
	public String view()
	{
		
		institutionalCollection = 
			institutionalCollectionService.getCollection(collectionId, false);
		
		collectionPath = institutionalCollectionService.getPath(institutionalCollection);
		
		return "view";
	}

	/**
	 * Load statistics for collections
	 * 
	 * @return
	 */
	public String getInstitutionalCollectionStatistic() {

		institutionalCollection = 
			institutionalCollectionService.getCollection(collectionId, false);
		
		institutionalItemCount = institutionalCollectionService.getInstitutionalItemCountForCollectionAndChildren(institutionalCollection);
		institutionalItemsCountForACollection = institutionalCollectionService.getInstitutionalItemCountForCollection(institutionalCollection); 
		subcollectionCount = institutionalCollection.getChildCount();
		allSubcollectionCount =institutionalCollectionService.getTotalSubcollectionCount(institutionalCollection); 
		fileDownloadCountForCollection = downloadStatisticsService.getNumberOfDownloadsForCollection(institutionalCollection);
		fileDownloadCountForCollectionAndItsChildren = downloadStatisticsService.getNumberOfDownloadsForCollectionAndItsChildren(institutionalCollection);
		
		return SUCCESS;
	}
	
	/**
	 * Repository service to access repository and institutional information.
	 * 
	 * @return
	 */
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	/**
	 * Set the repository service 
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**
	 * Get the institutional collection.
	 * 
	 * @return
	 */
	public InstitutionalCollection getInstitutionalCollection() {
		return institutionalCollection;
	}

	/**
	 * Set the institutional collection 
	 * 
	 * @param collection
	 */
	public void setInstitutionalCollection(
			InstitutionalCollection institutionalCollection) {
		this.institutionalCollection = institutionalCollection;
	}

	/**
	 * Collection id to load
	 * 
	 * @return
	 */
	public Long getCollectionId() {
		return collectionId;
	}

	/**
	 * Set the collection id.
	 * 
	 * @param collectionId
	 */
	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}
	
	/**
	 * The number of collection pictures.
	 * 
	 * @return
	 */
	public int getNumberOfCollectionPictures() {
		return institutionalCollection.getPictures().size();
	}
	
	/**
	 * Get the number of children.
	 * 
	 * @return the number of children.
	 */
	public int getNumberOfChildren()
	{
		return institutionalCollection.getChildren().size();
	}

	/**
	 * Path to this collection
	 * 
	 * @return
	 */
	public Collection<InstitutionalCollection> getCollectionPath() {
		return collectionPath;
	}

	/**
	 * Set the path to this collection.
	 * 
	 * @param collectionPath
	 */
	public void setCollectionPath(Collection<InstitutionalCollection> collectionPath) {
		this.collectionPath = collectionPath;
	}

	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}
	
	public Long getInstitutionalItemCount() {
		return institutionalItemCount;
	}


	public Long getInstitutionalItemsCountForACollection() {
		return institutionalItemsCountForACollection;
	}

	public int getSubcollectionCount() {
		return subcollectionCount;
	}

	public Long getAllSubcollectionCount() {
		return allSubcollectionCount;
	}

	public Long getFileDownloadCountForCollection() {
		return fileDownloadCountForCollection;
	}

	public Long getFileDownloadCountForCollectionAndItsChildren() {
		return fileDownloadCountForCollectionAndItsChildren;
	}

	public void setDownloadStatisticsService(
			DownloadStatisticsService downloadStatisticsService) {
		this.downloadStatisticsService = downloadStatisticsService;
	}

}
