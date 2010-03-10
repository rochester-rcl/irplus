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


package edu.ur.ir.institution.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.handle.HandleInfoDAO;
import edu.ur.ir.institution.DeletedInstitutionalItem;
import edu.ur.ir.institution.DeletedInstitutionalItemDAO;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemDAO;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionDAO;
import edu.ur.ir.institution.InstitutionalItemVersionDownloadCount;
import edu.ur.ir.institution.ReviewableItemService;
import edu.ur.ir.institution.VersionedInstitutionalItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemSecurityService;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.user.IrUser;
import edu.ur.order.OrderType;

/**
 * Default service for accessing institutional items.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultInstitutionalItemService implements InstitutionalItemService {
	
	/** Institutional item Data access. */
	private InstitutionalItemDAO institutionalItemDAO;
	
	/** Service for dealing with low level items   */
	private ItemService itemService;
	
	/** Institutional item version Data access. */
	private InstitutionalItemVersionDAO institutionalItemVersionDAO;
	
	/** Deleted Institutional item data access */
	private DeletedInstitutionalItemDAO deletedInstitutionalItemDAO;
	
	/** Item security service */
	private ItemSecurityService itemSecurityService;
	
	/** Service for dealing with researcher file system */
	private ResearcherFileSystemService researcherFileSystemService;
	
	/** Reviewable item service */
	private ReviewableItemService reviewableItemService;
	
	/** url generator for institutional items. */
	private InstitutionalItemVersionUrlGenerator institutionalItemVersionUrlGenerator;
	
	/** data access for handle information */
	private HandleInfoDAO handleInfoDAO;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultInstitutionalItemService.class);
	
	/**
	 * Delete an institutional item and all files that are not connected to other resources
	 * 
	 * @see edu.ur.ir.repository.RepositoryService#deleteInstitutionalItem(java.lang.Long)
	 */
	public void deleteInstitutionalItem(InstitutionalItem institutionalItem, IrUser deletingUser) {
		
		Set<GenericItem> genericItemsToDelete = new HashSet<GenericItem>();
		Set<InstitutionalItemVersion> versions = institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersions();
		
		// get the generic item for each version
		for(InstitutionalItemVersion version : versions)
		{
			GenericItem genericItem = version.getItem();
			genericItemsToDelete.add(genericItem);
		}
		
		// Delete ResearcherInstitutionalItem referring to this institutional item
		researcherFileSystemService.deleteResearcherInstitutionalItem(institutionalItem);
		
		addDeleteHistory(institutionalItem, deletingUser);
		 
		institutionalItem.getInstitutionalCollection().removeItem(institutionalItem);
		 
		institutionalItemDAO.makeTransient(institutionalItem);

		// Check to see if generic item can be deleted
		for(GenericItem genericItem : genericItemsToDelete)
		{
			
			 // determine if the generic item still exists in a users personal files or is
			 // published in another item
			 Long countForUsers = itemService.getItemVersionCount(genericItem);
			 Long countInstitutionalItems = institutionalItemDAO.getCountByGenericItem(genericItem.getId());
			 Long countResearcherPublications = researcherFileSystemService.getResearcherPublicationCount(genericItem);
			 
			 if( countForUsers == 0l && countInstitutionalItems == 0l && countResearcherPublications == 0l)
			 {
		          itemService.deleteItem(genericItem);
			 }
			 else
			 {
				 // we didn't delete it but it is not published and ACL should be removed
			     if(countInstitutionalItems == 0l)
			     {
				     genericItem.setPublishedToSystem(false);
				     itemService.makePersistent(genericItem);
				     itemSecurityService.deleteItemAcl(genericItem);
				     reviewableItemService.deleteReviewHistoryForItem(genericItem);
			     }
			 }
		}

	}
	
	/**
	 * Adds delete history for item
	 */
	public void addDeleteHistory(InstitutionalItem institutionalItem, IrUser deletingUser) {
		
		DeletedInstitutionalItem i = new DeletedInstitutionalItem(
				institutionalItem.getId(), institutionalItem.getName(), institutionalItem.getInstitutionalCollection().getName());

		i.setDeletedBy(deletingUser);
		i.setDeletedDate(new Date());
		
		deletedInstitutionalItemDAO.makePersistent(i);
		
	}

	/**
	 * Delete institutional item history
	 * 
	 * @param deletedInstitutionalItem
	 */
	public void deleteInstitutionalItemHistory(DeletedInstitutionalItem entity) {
		deletedInstitutionalItemDAO.makeTransient(entity);
	}

	/**
	 * Get Delete info for institutional item 
	 * 
	 * @param institutionalItemId Id of institutional item
	 * @return Information about deleted institutional item
	 */
	public DeletedInstitutionalItem getDeleteInfoForInstitutionalItem(Long institutionalItemId) {
		return deletedInstitutionalItemDAO.getDeleteInfoForInstitutionalItem(institutionalItemId);
	}
	
	/**
	 * Delete institutional item history
	 * 
	 * @param deletedInstitutionalItem
	 */
	public void deleteAllInstitutionalItemHistory() {
		deletedInstitutionalItemDAO.deleteAll();
	}
	
	/**
	 * Get the institutional item.
	 * 
	 * @see edu.ur.ir.repository.RepositoryService#getInstitutionalItem(java.lang.Long, boolean)
	 */
	public InstitutionalItem getInstitutionalItem(Long id, boolean lock) {
		return institutionalItemDAO.getById(id, lock);
	}

	/**
	 * Get the institutional item version.
	 * 
	 * @see edu.ur.ir.repository.RepositoryService#getInstitutionalItemVersion(java.lang.Long, boolean)
	 */
	public InstitutionalItemVersion getInstitutionalItemVersion(Long id, boolean lock) {
		return institutionalItemVersionDAO.getById(id, lock);
	}
	
	/**
	 * Return the list of found items 
	 * 
	 * @see edu.ur.ir.repository.RepositoryService#getInstitutionalItems(java.util.List)
	 */
	public List<InstitutionalItem> getInstitutionalItems(List<Long> itemIds) {
		return institutionalItemDAO.getInstitutionalItems(itemIds);
	}

	
	/**
	 * Get the specified number of results from the start position
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCollectionItems(int, int, java.lang.Long, java.lang.String, java.lang.String)
	 */
	public List<InstitutionalItem> getCollectionItemsOrderByName(int rowStart, int maxResults,
			InstitutionalCollection collection, OrderType orderType) {
		return institutionalItemDAO.getCollectionItemsByName(rowStart, maxResults, collection, 
				 orderType);
	}

	
	/**
	 * Get the count of institutional items
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCount()
	 */
	public Long getCount() {
		return institutionalItemDAO.getCount();
	}
	
	/**
	 * Get the count of institutional items for the specified repository id.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCount(java.lang.Long)
	 */
	public Long getCount(Long repositoryId) {
		return institutionalItemDAO.getCount(repositoryId);
	}
	
	/**
	 * Get the repository items ordered by name.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getRepositoryItems(int, int, java.lang.Long, java.lang.String, java.lang.String)
	 */
	public List<InstitutionalItem> getRepositoryItemsOrderByName(int rowStart, int maxResults,
			Long repositoryId, OrderType orderType) {
		return institutionalItemDAO.getRepositoryItemsByName(rowStart, maxResults, repositoryId, 
				orderType);
	}

	/**
	 * Data access for institutional items.
	 * 
	 * @return
	 */
	public InstitutionalItemDAO getInstitutionalItemDAO() {
		return institutionalItemDAO;
	}

	/**
	 * Set institutional item data access.
	 * 
	 * @param institutionalItemDAO
	 */
	public void setInstitutionalItemDAO(InstitutionalItemDAO institutionalItemDAO) {
		this.institutionalItemDAO = institutionalItemDAO;
	}

	/**
	 * Save Institutional Item
	 * 
	 * @param InstitutionalItem
	 */
	public void saveInstitutionalItem(InstitutionalItem institutionalItem) {
		institutionalItemDAO.makePersistent(institutionalItem);
	}

	/**
	 * Get the item service.
	 * 
	 * @return item service used
	 */
	public ItemService getItemService() {
		return itemService;
	}

	/**
	 * Set the item service.
	 * 
	 * @param itemService
	 */
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	/**
	 *  
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCountByGenericItem(java.lang.Long)
	 */
	public Long getCountByGenericItem(Long genericItemId) {
		
		return institutionalItemDAO.getCountByGenericItem(genericItemId);
	}

	/**
	 * Get repository items with the specified first character and orderd by name
	 * with the specified order type.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getRepositoryItemsByChar(int, int, java.lang.Long, char, java.lang.String)
	 */
	public List<InstitutionalItem> getRepositoryItemsByChar(int rowStart,
			int maxResults, Long repositoryId, char firstChar, OrderType orderType) {
		return institutionalItemDAO.getRepositoryItemsByChar(rowStart, maxResults, repositoryId, firstChar, orderType);
	}
	
	/**
	 * Get a count of items in the repository with the given character.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCount(java.lang.Long, char)
	 */
	public Long getCount(Long repositoryId, char nameFirstChar) {
		return institutionalItemDAO.getCount(repositoryId, nameFirstChar);
	}

	
	/**
	 * Get a count of items with a name that starts with the specified range.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCount(java.lang.Long, char, char)
	 */
	public Long getCount(Long repositoryId, char nameFirstCharRange,
			char namelastCharRange) {
		return institutionalItemDAO.getCount(repositoryId, nameFirstCharRange, namelastCharRange);
	}

	/**
	 * Get a count of items with a name that starts with the specified range.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCount(java.lang.Long, char, char)
	 */
	public List<InstitutionalItem> getRepositoryItemsBetweenChar(int rowStart,
			int maxResults, Long repositoryId, char firstChar, char lastChar,
			OrderType orderType) {
		return institutionalItemDAO.getRepositoryItemsBetweenChar(rowStart, maxResults, 
				repositoryId, firstChar, lastChar, orderType);
	}

	
	/**
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCollectionItemsBetweenChar(int, int, edu.ur.ir.institution.InstitutionalCollection, char, char, java.lang.String)
	 */
	public List<InstitutionalItem> getCollectionItemsBetweenChar(int rowStart,
			int maxResults, InstitutionalCollection collection,
			char firstChar, char lastChar, OrderType orderType) {
		return institutionalItemDAO.getCollectionItemsBetweenChar(rowStart, maxResults, collection, firstChar, lastChar, orderType);
	}

	
	/**
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCollectionItemsByChar(int, int, edu.ur.ir.institution.InstitutionalCollection, char, java.lang.String)
	 */
	public List<InstitutionalItem> getCollectionItemsByChar(int rowStart,
			int maxResults, InstitutionalCollection institutionalCollection,
			char firstChar, OrderType orderType) {
		return institutionalItemDAO.getCollectionItemsByChar(rowStart, maxResults, institutionalCollection, firstChar, orderType);
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCount(edu.ur.ir.institution.InstitutionalCollection, char)
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstChar) {
		return institutionalItemDAO.getCount(collection, nameFirstChar);
	}
	
	/**
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCount(edu.ur.ir.institution.InstitutionalCollection, char, char)
	 */
	public Long getCount(InstitutionalCollection collection,
			char nameFirstCharRange, char nameLastCharRange) {
		return institutionalItemDAO.getCount(collection, nameFirstCharRange, nameLastCharRange);
	}

	
	/**
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCountForCollectionAndChildren(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public Long getCountForCollectionAndChildren(
			InstitutionalCollection collection) {
		return institutionalItemDAO.getCountForCollectionAndChildren(collection);
	}

	/**
	 * Get a count of distinct institutional items in the repository.
	 * 
	 * @return
	 */
	public Long getDistinctInstitutionalItemCount() {
		return institutionalItemDAO.getDistinctInstitutionalItemCount();
	}

	/**
	 * Get the institutional item version data access object.
	 * 
	 * @return
	 */
	public InstitutionalItemVersionDAO getInstitutionalItemVersionDAO() {
		return institutionalItemVersionDAO;
	}

	/**
	 * Set the institutional item version data access object
	 * 
	 * @param institutionalItemVersionDAO
	 */
	public void setInstitutionalItemVersionDAO(
			InstitutionalItemVersionDAO institutionalItemVersionDAO) {
		this.institutionalItemVersionDAO = institutionalItemVersionDAO;
	}

	/**
	 * Save Institutional Item Version
	 * 
	 * @param institutionalItemVersion
	 */
	public void saveInstitutionalItemVersion(InstitutionalItemVersion institutionalItemVersion) {
		institutionalItemVersionDAO.makePersistent(institutionalItemVersion);
	}

	/**
	 * Get a institutional collections  the generic item exists in
	 * 
	 * @param itemId Id of generic Item 
	 * @return the list of collections found
	 */
	public List<InstitutionalCollection> getInstitutionalCollectionsSubmittedForGenericItem(Long itemId) {
		return institutionalItemDAO.getInstitutionalCollectionsForGenericItem(itemId);
	}

	/**
	 * Get the deleted institutional item data access object.
	 * 
	 * @return
	 */
	public DeletedInstitutionalItemDAO getDeletedInstitutionalItemDAO() {
		return deletedInstitutionalItemDAO;
	}

	/**
	 * Set the deleted institutional item data acess object 
	 * 
	 * @param deletedInstitutionalItemDAO
	 */
	public void setDeletedInstitutionalItemDAO(
			DeletedInstitutionalItemDAO deletedInstitutionalItemDAO) {
		this.deletedInstitutionalItemDAO = deletedInstitutionalItemDAO;
	}

	/**
	 * Get the number of publications contributed by given person names
	 *  
	 * @see edu.ur.ir.institution.InstitutionalItemService#getPublicationCountByPersonName(Set)
	 */
	public Long getPublicationCountByPersonName(Set<PersonName> personNames) {
		List<Long> ids = new ArrayList<Long>();
		for (PersonName p: personNames) {
			ids.add(p.getId());
		}
		return institutionalItemDAO.getPublicationCountByPersonName(ids);
	}
	
	/**
	 * Set item as private and assign submitted collections user group permissions
	 * 
	 * @param item Item to be assigned private permission
	 * @param collections Collections the item is submitted to
	 */
	public void setItemPrivatePermissions(GenericItem item, List<InstitutionalCollection> collections) {
		
		item.setPubliclyViewable(false);
		
		for(ItemFile file:item.getItemFiles()) {
			file.setPublic(false);
		}
		
		itemService.makePersistent(item);
		
		for(InstitutionalCollection collection:collections) {
			itemSecurityService.assignGroupsToItem(item, collection);
		}
		
	}

	/**
	 * Set the item security service.
	 * 
	 * @param itemSecurityService
	 */
	public void setItemSecurityService(ItemSecurityService itemSecurityService) {
		this.itemSecurityService = itemSecurityService;
	}

	/**
	 * Set the reviewable item service.
	 * 
	 * @param reviewableItemService
	 */
	public void setReviewableItemService(ReviewableItemService reviewableItemService) {
		this.reviewableItemService = reviewableItemService;
	}

	/**
	 * Get number of deleted institutional items for user
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getDeletedInstitutionalItemCountForUser(Long)
	 */
	public Long getDeletedInstitutionalItemCountForUser(Long userId) {
		return deletedInstitutionalItemDAO.getDeletedInstitutionalItemCountForUser(userId);
	}

	/**
	 * Get Institutional item by given version id
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getInstitutionalItemByVersionId(Long)
	 */
	public InstitutionalItem getInstitutionalItemByVersionId(Long institutionalVersionId) {
		
		return institutionalItemDAO.getInstitutionalItemByVersionId(institutionalVersionId);
		
	}
	
	/**
	 * Get institutional item by generic item id
	 * 
	 * @param genericItemId
	 * @return
	 */
	public List<InstitutionalItem> getInstitutionalItemsByGenericItemId(Long genericItemId) {
		return institutionalItemDAO.getInstitutionalItemsForGenericItemId(genericItemId);
	}

	
	/**
	 * Get the institutional item version by the handle id.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getInstitutionalItemByHandleId(java.lang.Long)
	 */
	public InstitutionalItemVersion getInstitutionalItemByHandleId(Long handleId) {
		return institutionalItemVersionDAO.getItemVersionByHandleId(handleId);
	}

	
	/**
	 * Get all institutional items that were submitted into the collection between the given
	 * start and end date.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getItems(edu.ur.ir.institution.InstitutionalCollection, java.util.Date, java.util.Date)
	 */
	public List<InstitutionalItem> getItems(InstitutionalCollection collection,
			Date startDate, Date endDate) {
		 return institutionalItemDAO.getItems(collection, startDate, endDate);
	}

	
	/**
	 *  Get all institutional items that were submitted into the collection between the given
	 *  start and end date
	 *  
	 * @see edu.ur.ir.institution.InstitutionalItemService#getItems(int, int, edu.ur.ir.institution.InstitutionalCollection, java.util.Date, java.util.Date)
	 */
	public List<InstitutionalItem> getItemsOrderByDate(int rowStart, int maxResults,
			InstitutionalCollection collection, OrderType orderType) {
		return institutionalItemDAO.getItemsOrderByDate(rowStart, maxResults, collection, orderType);
	}

	/**
	 * Get the researcher file system service.
	 * 
	 * @return
	 */
	public ResearcherFileSystemService getResearcherFileSystemService() {
		return researcherFileSystemService;
	}

	/**
	 * Set the researcher file system service.
	 * 
	 * @param researcherFileSystemService
	 */
	public void setResearcherFileSystemService(
			ResearcherFileSystemService researcherFileSystemService) {
		this.researcherFileSystemService = researcherFileSystemService;
	}


	/**
	 * Get the list of institutional item ids in the collection.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCollectionItemsIds(int, int, edu.ur.ir.institution.InstitutionalCollection, edu.ur.order.OrderType)
	 */
	public List<Long> getCollectionItemsIds(int rowStart, int maxResults,
			InstitutionalCollection collection, OrderType orderType) {
		return institutionalItemDAO.getCollectionItemsIds(rowStart, maxResults, collection, orderType);
	}
	
	/**
	 * Update the handle for a given item.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#resetHandle(edu.ur.ir.institution.InstitutionalItem, edu.ur.ir.institution.InstitutionalItemVersion)
	 */
	public void resetHandle(InstitutionalItem institutionalItem, InstitutionalItemVersion institutionalItemVersion)
	{
		HandleInfo info = institutionalItemVersion.getHandleInfo();
		if( info != null )
		{	
		    String url = institutionalItemVersionUrlGenerator.createUrl(institutionalItem, institutionalItemVersion.getVersionNumber());
		    info.setData(url);
		    handleInfoDAO.makePersistent(info);
		}
	}
	
	/**
	 * Reset all handles in the system.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#resetAllHandles(int, java.lang.Long)
	 */
	public void resetAllHandles(int batchSize, Long repositoryId )
	{
		if(batchSize <= 0 )
		{
			throw new IllegalStateException("Batch size cannot be less than or equal to 0 batch Size = " + batchSize);
		}
		
		int rowStart = 0;
		
		int numberInstitutionalItems = institutionalItemDAO.getCount().intValue();
		log.debug("processing a total of " + numberInstitutionalItems + " institutional Items ");
		
		
		// increase number of users by batch size to make sure 
		// all users are processed 
		while(rowStart <= (numberInstitutionalItems + batchSize))
		{
			log.error("row start = " + rowStart);
			log.error("batch size = " +  batchSize);
			
			// notice the minus one because we are starting at 0
			log.error("processing " + rowStart + " to " + (rowStart + batchSize - 1) );
			
			
		    List<InstitutionalItem> institutionalItems = institutionalItemDAO.getRepositoryItemsByName(rowStart, batchSize, repositoryId, OrderType.ASCENDING_ORDER);
		    for( InstitutionalItem i : institutionalItems )
		    {
		    	VersionedInstitutionalItem versionedItem = i.getVersionedInstitutionalItem();
		    	if( versionedItem != null )
		    	{
		    	    Set<InstitutionalItemVersion> versions = versionedItem.getInstitutionalItemVersions();
		            for( InstitutionalItemVersion v : versions)
		            {
		        	    resetHandle(i, v);
		            }
		    	}
		    }
		    
		    rowStart = rowStart + batchSize;
		}

		
	}

	/**
	 * Get the institutional item version url generator.
	 * 
	 * @return
	 */
	public InstitutionalItemVersionUrlGenerator getInstitutionalItemVersionUrlGenerator() {
		return institutionalItemVersionUrlGenerator;
	}

	/**
	 * Set the institutional item version url generator.
	 * 
	 * @param institutionalItemVersionUrlGenerator
	 */
	public void setInstitutionalItemVersionUrlGenerator(
			InstitutionalItemVersionUrlGenerator institutionalItemVersionUrlGenerator) {
		this.institutionalItemVersionUrlGenerator = institutionalItemVersionUrlGenerator;
	}

	/**
	 * Get the handle info data access object.
	 * 
	 * @return
	 */
	public HandleInfoDAO getHandleInfoDAO() {
		return handleInfoDAO;
	}

	/**
	 * Set the handle info data acess object.
	 * 
	 * @param handleInfoDAO
	 */
	public void setHandleInfoDAO(HandleInfoDAO handleInfoDAO) {
		this.handleInfoDAO = handleInfoDAO;
	}

	
	/**
	 * Get the items by sponsor count.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getItemsBySponsorCount(long)
	 */
	public Long getItemsBySponsorCount(long sponsorId) {
		return institutionalItemVersionDAO.getItemsBySponsorCount(sponsorId);
	}

	
	/**
	 * Get the item sponsored by the given sponsor ordered by item name.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getItemsBySponsorItemNameOrder(int, int, long, edu.ur.order.OrderType)
	 */
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemNameOrder(
			int rowStart, int maxResults, long sponsorId, OrderType orderType) {
		return institutionalItemVersionDAO.getItemsBySponsorItemNameOrder(rowStart, maxResults, sponsorId, orderType);
	}

	/**
	 * Get the items sponsored by the given sponsor ordered by deposit date.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getItemsBySponsorItemDepositDateOrder(int, int, long, edu.ur.order.OrderType)
	 */
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemDepositDateOrder(
			int rowStart, int maxResults, long sponsorId, OrderType orderType) {
		return institutionalItemVersionDAO.getItemsBySponsorItemDepositDateOrder(rowStart, maxResults, sponsorId, orderType);
	}

	/**
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getItemsBySponsorItemDownloadOrder(int, int, long, edu.ur.order.OrderType)
	 */
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemDownloadOrder(
			int rowStart, int maxResults, long sponsorId, OrderType orderType) {
		return institutionalItemVersionDAO.getItemsBySponsorItemDownloadOrder(rowStart, maxResults, sponsorId, orderType);
	}

	/**
	 * Get the list of publication versions for names ordered by download.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getPublicationVersionsForNamesByDownload(int, int, java.util.Set, edu.ur.order.OrderType)
	 */
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesByDownload(
			int rowStart, int maxResults, Set<PersonName> personNames,
			OrderType orderType) {
		List<Long> ids = new ArrayList<Long>();
		for (PersonName p: personNames) {
			ids.add(p.getId());
		}
		return institutionalItemVersionDAO.getPublicationVersionsForNamesByDownload(rowStart, maxResults, ids, orderType);
	}

	/**
	 * Get publication verssions for a set of names ordered by title.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getPublicationVersionsForNamesByTitle(int, int, java.util.Set, edu.ur.order.OrderType)
	 */
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesByTitle(
			int rowStart, int maxResults, Set<PersonName> personNames,
			OrderType orderType) {
		List<Long> ids = new ArrayList<Long>();
		for (PersonName p: personNames) {
			ids.add(p.getId());
		}
		return institutionalItemVersionDAO.getPublicationVersionsForNamesByTitle(rowStart, maxResults, ids, orderType);
	}
	
	/**
	 * Get the number of downloads for a given set of name ids.
	 * 
	 * @see edu.ur.ir.statistics.DownloadStatisticsService#getNumberOfDownlodsForPersonNames(java.util.List)
	 */
	public Long getNumberOfDownlodsForPersonNames(Set<PersonName> personNames) {
		List<Long> ids = new ArrayList<Long>();
		for (PersonName p: personNames) {
			ids.add(p.getId());
		}
		return institutionalItemVersionDAO.getDownloadCountByPersonName(ids);
	}

}
