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

import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.institution.DeletedInstitutionalItemService;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemDAO;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionService;
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
/**
 * @author ideazoft
 *
 */
public class DefaultInstitutionalItemService implements InstitutionalItemService {
	
	/** eclipse generated id */
	private static final long serialVersionUID = -319698084702094847L;

	/** Institutional item Data access. */
	private InstitutionalItemDAO institutionalItemDAO;
	
	/** Service for dealing with low level items   */
	private ItemService itemService;
	
	/** Item security service */
	private ItemSecurityService itemSecurityService;
	
	/** Service for dealing with researcher file system */
	private ResearcherFileSystemService researcherFileSystemService;
	
	/** Reviewable item service */
	private ReviewableItemService reviewableItemService;
	
	/** Service for dealing with institutional item information */
	private InstitutionalItemVersionService institutionalItemVersionService;
	
	/** Service for dealing with institutional item index processing  */
	private InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService;
	
	/** service for dealing with deleted institutional item information */
	private DeletedInstitutionalItemService deletedInstitutionalItemService;



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
		
		deletedInstitutionalItemService.addDeleteHistory(institutionalItem, deletingUser);
		 
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
	 * Get the institutional item.
	 * 
	 * @see edu.ur.ir.repository.RepositoryService#getInstitutionalItem(java.lang.Long, boolean)
	 */
	public InstitutionalItem getInstitutionalItem(Long id, boolean lock) {
		return institutionalItemDAO.getById(id, lock);
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
	 * Get a institutional collections  the generic item exists in
	 * 
	 * @param itemId Id of generic Item 
	 * @return the list of collections found
	 */
	public List<InstitutionalCollection> getInstitutionalCollectionsSubmittedForGenericItem(Long itemId) {
		return institutionalItemDAO.getInstitutionalCollectionsForGenericItem(itemId);
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
	public void setItemPrivatePermissions(GenericItem item, List<InstitutionalCollection> collections) 
	{
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
	 * Get institutional item by generic item id
	 * 
	 * @param genericItemId
	 * @return
	 */
	public List<InstitutionalItem> getInstitutionalItemsByGenericItemId(Long genericItemId) {
		return institutionalItemDAO.getInstitutionalItemsForGenericItemId(genericItemId);
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
		        	    institutionalItemVersionService.resetHandle(v);
		            }
		    	}
		    }
		    
		    rowStart = rowStart + batchSize;
		}

		
	}

	
	/**
	 * Service for institutional item version data.
	 * 
	 * @return institutional item service
	 */
	public InstitutionalItemVersionService getInstitutionalItemVersionService() {
		return institutionalItemVersionService;
	}

	/**
	 * Service for institutional item version data.
	 * 
	 * @param institutionalItemVersionService
	 */
	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalItemService#markAllInstitutionalItemsForIndexing(java.lang.Long, java.lang.String)
	 */
	public void markAllInstitutionalItemsForIndexing(Long genericItemId,
			IndexProcessingType processingType) {
		List<InstitutionalItem> institutionalItems = getInstitutionalItemsByGenericItemId(genericItemId);

		if (institutionalItems != null) {
	
			for(InstitutionalItem i : institutionalItems) {
				institutionalItemIndexProcessingRecordService.save(i.getId(), processingType);
			}
		}
	}


	/**
	 * Service for processing institutional item records.
	 * 
	 * @return InstitutionalItemIndexProcessingRecordService
	 */
	public InstitutionalItemIndexProcessingRecordService getInstitutionalItemIndexProcessingRecordService() {
		return institutionalItemIndexProcessingRecordService;
	}

	public void setInstitutionalItemIndexProcessingRecordService(
			InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService) {
		this.institutionalItemIndexProcessingRecordService = institutionalItemIndexProcessingRecordService;
	}
	
	public DeletedInstitutionalItemService getDeletedInstitutionalItemService() {
		return deletedInstitutionalItemService;
	}

	public void setDeletedInstitutionalItemService(
			DeletedInstitutionalItemService deletedInstitutionalItemService) {
		this.deletedInstitutionalItemService = deletedInstitutionalItemService;
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalItemService#getInstitutionalItem(java.lang.Long, java.lang.Long)
	 */
	public InstitutionalItem getInstitutionalItem(Long collectionId,
			Long genericItemId) {
		
		return institutionalItemDAO.getInstitutionalItem(collectionId, genericItemId);
	}


	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - the institutional collection 
	 * @param contentTypeId - content type id the items must have
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return list of items matching the specified criteria
	 */
	public List<InstitutionalItem> getCollectionItemsBetweenChar(int rowStart,
			int maxResults, InstitutionalCollection collection,
			Long contentTypeId, char firstChar, char lastChar,
			OrderType orderType) {
		return institutionalItemDAO.getCollectionItemsBetweenChar(rowStart, maxResults, collection, 
				contentTypeId, firstChar, lastChar, orderType);
	}

	/**
	 * Get a list of items for a specified collection by first character of the name 
	 * and the specified content type.  
	 * This INCLUDES items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param Institutionalcollection - parent collection
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCollectionItemsByChar(int, int, edu.ur.ir.institution.InstitutionalCollection, java.lang.Long, char, edu.ur.order.OrderType)
	 */
	public List<InstitutionalItem> getCollectionItemsByChar(int rowStart,
			int maxResults, InstitutionalCollection collection,
			Long contentTypeId, char firstChar, OrderType orderType) {
		
		return institutionalItemDAO.getCollectionItemsByChar(rowStart, 
				maxResults, 
				collection, 
				contentTypeId, 
				firstChar, 
				orderType);
	}

	/**
	 * Get the list of items for the specified collection with the given 
	 * content type id.  This includes items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults -  maximum number of results to return
	 * @param collection - the collection to get items 
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCollectionItemsByName(int, int, edu.ur.ir.institution.InstitutionalCollection, java.lang.Long, edu.ur.order.OrderType)
	 */
	public List<InstitutionalItem> getCollectionItemsOrderByName(int rowStart,
			int maxResults, InstitutionalCollection collection,
			Long contentTypeId, OrderType orderType) {
		return institutionalItemDAO.getCollectionItemsByName(rowStart, 
				maxResults, 
				collection, 
				contentTypeId, 
				orderType);
	}

	/**
	 * Get a  count of institutional items with the given content type id.
	 * 
	 * @param repositoryId - id of the repository
	 * @param contentTypeId - content type
	 * 
	 * @return the count of institutional items with the content type in the specified
	 * repository.
	 */
	public Long getCount(Long repositoryId, Long contentTypeId) {
		return institutionalItemDAO.getCount(repositoryId, contentTypeId);
	}

	/**
	 * Get a count of institutional items with the specified repository id
	 * has a name starting with the first character and the specified content type id.
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstChar - name of the first character
	 * @param contentTypeId - specified content type id
	 * 
	 * @return the count of items with the specified criteria
	 */
	public Long getCount(Long repositoryId, char nameFirstChar,
			Long contentTypeId) {
		return institutionalItemDAO.getCount( repositoryId, nameFirstChar, contentTypeId);
	}


	/**
	 * Get a count of all items within the specified repository with a name
	 * first character starting between the given character range and the content type id.
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstCharRange - starting character range for the first letter of the title
	 * @param namelastCharRange - ending character range for the first letter of the title
	 * @param contentTypeId - id of the content type
	 * 
	 * @return the count of items 
	 */
	public Long getCount(Long repositoryId, char nameFirstCharRange,
			char nameLastCharRange, Long contentTypeId) {
		return institutionalItemDAO.getCount(repositoryId, nameFirstCharRange, nameLastCharRange, contentTypeId);
	}



	/**
	 * Get a count of all items within the specified collection, has the specified first character 
	 * and a given content type id.  This includes a count of items within sub collections.
	 * 
	 * @param collection - collection items must be within sub collections
	 * @param nameFirstChar - name starts with the specified first character
	 * @param contentTypeId - id of the content type
	 * 
	 * @return count of items 
	 */
	public Long getCount(InstitutionalCollection collection,
			char nameFirstChar, Long contentTypeId) {
		return institutionalItemDAO.getCount(collection, nameFirstChar, contentTypeId);
	}



	/**
	 * Get a count of all institutional items in the specified collection with
	 * specified first character in the given character range with the given content type id-  THIS INCLUDES items in child collections 
	 * 
	 * @param institutional collection - parent collection
	 * @param nameFirstCharRange - first character in range
	 * @param nameLastCharRange - last character in the range
	 * 
	 * @return count of titles found that have a first character in the specified range
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getCount(edu.ur.ir.institution.InstitutionalCollection, char, char, java.lang.Long)
	 */
	public Long getCount(InstitutionalCollection collection,
			char nameFirstCharRange, char nameLastCharRange, Long contentTypeId) {
		return institutionalItemDAO.getCount(collection, nameFirstCharRange, nameLastCharRange, contentTypeId);
	}



	/**
	 * Get a count of institutional items in a collection and its children with
	 * the specified content type.
	 * 
	 * @param collection - collection to start counting from
	 * @param contentTypeId - id of the content type 
	 * 
	 * @return Items within the specified collection and its sub collection
	 */
	public Long getCount(InstitutionalCollection collection, Long contentTypeId) {
		return institutionalItemDAO.getCount(collection, contentTypeId);
	}



	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters with the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsBetweenChar(int rowStart,
			int maxResults, Long repositoryId, char firstChar, char lastChar,
			Long contentTypeId, OrderType orderType) {
		return institutionalItemDAO.getRepositoryItemsBetweenChar(rowStart, maxResults, repositoryId, 
				firstChar, lastChar, contentTypeId, orderType);
	}



	/**
	 * Get a list of items for a specified repository by first character of the name and
	 * the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsByChar(int rowStart,
			int maxResults, Long repositoryId, Long contentTypeId,
			char firstChar, OrderType orderType) {
		return institutionalItemDAO.getRepositoryItemsByChar(rowStart, maxResults, 
				repositoryId, contentTypeId, firstChar, orderType);
	}



	/**
	 * Get a list of items for a specified repository with the given content type id.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<InstitutionalItem> getRepositoryItemsOrderByName(int rowStart,
			int maxResults, Long repositoryId, Long contentTypeId,
			OrderType orderType) {
		return institutionalItemDAO.getRepositoryItemsOrderByName(rowStart, maxResults, 
				repositoryId, contentTypeId, orderType);
	}
}
