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

import java.io.File;
import java.util.Collection;

import org.apache.log4j.Logger;

import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionIndexService;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;

/**
 * Action to view institutional collections.
 * 
 * @author Nathan Sarr
 *
 */
/**
 * @author ideazoft
 *
 */
public class ManageInstitutionalCollections extends Pager implements UserIdAware {
	
	/** Eclipse generated id */
	private static final long serialVersionUID = -186085179160200023L;

	/**  Id of the user who has the collections  */
	private Long userId;
	
	/**  Repository information data access  */
	private RepositoryService repositoryService;
	
	/**  Repository information data access  */
	private UserService userService;

    /** A collection of personal collections for a user in a given location of
        their personal directory.*/
    private Collection<InstitutionalCollection> institutionalCollections;
    
    /** set of personal collections that are the path for the current personal collection */
    private Collection <InstitutionalCollection> collectionPath;
	
	/** The collection that owns the listed items and personal collections */
	private Long parentCollectionId = Long.valueOf(0);
	
	/** current parent collection null if at the root */
	private InstitutionalCollection parent = null;
	
	/** list of collection ids to perform actions on*/
	private Long[] collectionIds;
	
	/** True indicates the collections were deleted */
	private boolean collectionsDeleted = true;
	
	/** Message used to indicate there is a problem with deleting the collections. */
	private String collectionsDeletedMessage;
	
	/** Institutional Collection service */
	private InstitutionalCollectionService institutionalCollectionService;
	


	/** service for marking items that need to be indexed */
	private InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService;

	/** index processing type service */
	private IndexProcessingTypeService indexProcessingTypeService;

	
	/**  Logger for view personal collections action */
	private static final Logger log = Logger.getLogger(ManageInstitutionalCollections.class);

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of ip addresses to ignore  */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** indicates this the first time the user viewing the search */
	private boolean searchInit = true;
	
	/** Indicates this is a browse view */
	private String viewType = "browse";
	
	/** institutional collection index service */
	private InstitutionalCollectionIndexService institutionalCollectionIndexService;
	


	/** Default constructor */
	public  ManageInstitutionalCollections()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}


	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Returns success.
	 * 
	 * @return
	 */
	public String execute()
	{
		log.debug("execute called");
		createCollectionFileSystem();
		return SUCCESS;
	}
	
	/**
	 * Removes the selected items and collections.
	 * 
	 * @return
	 */
	public String deleteCollectionSystemObjects()
	{
		log.debug("Delete collection system objects called");
		
		IrUser user = userService.getUser(userId, false);
		Repository repo = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
        File collectionFileIndex = new File(repo.getInstitutionalCollectionIndexFolder());		
		if( collectionIds != null )
		{
		    for(int index = 0; index < collectionIds.length; index++)
		    {
			    log.debug("Deleting collection with id " + collectionIds[index]);
			    InstitutionalCollection collection = 
			    	institutionalCollectionService.getCollection(collectionIds[index], false);
			    
				// only index if the item was added directly to the collection
				IndexProcessingType processingType = indexProcessingTypeService.get(IndexProcessingTypeService.DELETE); 
				institutionalItemIndexProcessingRecordService.processItemsInCollection(collection, processingType);
				institutionalCollectionIndexService.delete(collection.getId(), collectionFileIndex);
			    institutionalCollectionService.deleteCollection(collection, user);
		    }
		}
		
		createCollectionFileSystem();
		return SUCCESS;
	}
	
	
	/**
	 * Create the file system to view.
	 */
	private void createCollectionFileSystem()
	{
		log.debug("create collection file system called");
		
		// don't hit the database unless we need to
		if(parentCollectionId != null && parentCollectionId > 0)
		{
			parent = institutionalCollectionService.getCollection(parentCollectionId, false);
		    collectionPath = institutionalCollectionService.getPath(parent);
		}
		
		rowEnd = rowStart + numberOfResultsToShow;
	    
		institutionalCollections = institutionalCollectionService.getCollections(Repository.DEFAULT_REPOSITORY_ID,
																	parentCollectionId,
																	rowStart, 
														    		numberOfResultsToShow,
														    		sortType);
	    totalHits = institutionalCollectionService.getCollectionsCount(Repository.DEFAULT_REPOSITORY_ID,
				parentCollectionId).intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	}
	
	/**
	 * Get the parent collection id.
	 * 
	 * @return
	 */
	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	/**
	 * Set the parent collection id.
	 * 
	 * @param parentCollectionId
	 */
	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

	/**
	 * Get the collection ids 
	 * 
	 * @return
	 */
	public Long[] getCollectionIds() {
		return collectionIds;
	}

	/**
	 * Set the collection ids.
	 * 
	 * @param collectionIds
	 */
	public void setCollectionIds(Long[] collectionIds) {
		this.collectionIds = collectionIds;
	}

	/**
	 * Returns true if the collection is deleted.
	 * 
	 * @return
	 */
	public boolean isCollectionsDeleted() {
		return collectionsDeleted;
	}

	/**
	 * @param collectionsDeleted
	 */
	public void setCollectionsDeleted(boolean collectionsDeleted) {
		this.collectionsDeleted = collectionsDeleted;
	}

	/**
	 * Get the collection deleted message.
	 * 
	 * @return
	 */
	public String getcollectionsDeletedMessage() {
		return collectionsDeletedMessage;
	}
	
	/**
	 * Get the set of collections that make up the path to this personal collection.
	 * 
	 * @return
	 */
	public Collection<InstitutionalCollection> getCollectionPath() {
		return collectionPath;
	}

	/**
	 * Get the user who owns the personal collections
	 * 
	 * @return
	 */
	public IrUser getUser() {
		return userService.getUser(userId,false);
	}

	/**
	 * Set the repository service.
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Get the deleted collection deleted message.
	 * 
	 * @return
	 */
	public String getCollectionsDeletedMessage() {
		return collectionsDeletedMessage;
	}

	/**
	 * Get the user id.
	 * 
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Get the repository.
	 * 
	 * @return
	 */
	public Repository getRepository() {
		return repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
	}
	
	/**
	 * Get the parent.
	 * 
	 * @return
	 */
	public InstitutionalCollection getParent() {
		return parent;
	}

	/**
	 * Get the institutional collections
	 * 
	 * @return
	 */
	public Collection<InstitutionalCollection> getInstitutionalCollections() {
		return institutionalCollections;
	}

	/**
	 * Get the sort type.
	 * 
	 * @return
	 */
	public String getSortType() {
		return sortType;
	}
	
	/**
	 * Set the sort type.
	 * 
	 * @param sortType
	 */
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	
	/**
	 * Get the total hits found.
	 * 
	 * @see edu.ur.ir.web.table.Pager#getTotalHits()
	 */
	public int getTotalHits() {
		return totalHits;
	}
	
	/**
	 * Get the row end.
	 * 
	 * @return
	 */
	public int getRowEnd() {
		return rowEnd;
	}
	
	/**
	 * Set the row end.
	 * 
	 * @param rowEnd
	 */
	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

	/**
	 * Set the institutional item index processing record service.
	 * 
	 * @param institutionalItemIndexProcessingRecordService
	 */
	public void setInstitutionalItemIndexProcessingRecordService(
			InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService) {
		this.institutionalItemIndexProcessingRecordService = institutionalItemIndexProcessingRecordService;
	}

	/**
	 * Set the index processing type service.
	 *  
	 * @param indexProcessingTypeService
	 */
	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}
	
	/**
	 * Get the search init setting.
	 * 
	 * @return
	 */
	public boolean getSearchInit() {
		return searchInit;
	}

	/**
	 * Get the view type.
	 * 
	 * @return
	 */
	public String getViewType() {
		return viewType;
	}
	
	/**
	 * Set the institutional collection index service.
	 * 
	 * @param institutionalCollectionIndexService
	 */
	public void setInstitutionalCollectionIndexService(
			InstitutionalCollectionIndexService institutionalCollectionIndexService) {
		this.institutionalCollectionIndexService = institutionalCollectionIndexService;
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


}
