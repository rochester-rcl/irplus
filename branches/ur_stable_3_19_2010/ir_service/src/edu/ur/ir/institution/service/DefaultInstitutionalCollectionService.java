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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.StringUtils;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionDAO;
import edu.ur.ir.institution.InstitutionalCollectionSecurityService;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemDAO;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.security.Sid;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;


/**
 * Default service for dealing with institutional collections.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultInstitutionalCollectionService implements 
    	InstitutionalCollectionService{

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultInstitutionalCollectionService.class);
	
	/**  Data access for an institutional collection  */
	private InstitutionalCollectionDAO institutionalCollectionDAO;
	
	/** institutional item data access */
	private InstitutionalItemDAO institutionalItemDAO;	
	
	/** security service for dealing with collection specific information */
	private InstitutionalCollectionSecurityService institutionalCollectionSecurityService;
	
	/** Repository data access */
	private RepositoryService repositoryService;
	
	/** Service for institutional items */
	private InstitutionalItemService institutionalItemService;
	
	/** Mail message for reviewing an item */
	private SimpleMailMessage itemReviewMessage;
	
	/** Service to send email */
	private MailSender mailSender;
	
	/**
	 * Delete an institutional collection and all related information within it.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#deleteInstitutionalCollection(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public void deleteCollection(InstitutionalCollection collection, IrUser deletingUser) {
		
		List<InstitutionalCollection> allChildren = institutionalCollectionDAO.getAllChildrenForCollection(collection);
		
		// delete all security for children and all items
		for(InstitutionalCollection child : allChildren)
		{
			 institutionalCollectionSecurityService.deleteAcl(child);
			 LinkedList<InstitutionalItem> items = new LinkedList<InstitutionalItem>();
			 items.addAll(child.getItems());
			 for(InstitutionalItem item : items)
			 {
				 institutionalItemService.deleteInstitutionalItem(item, deletingUser);
			 }
		}

		LinkedList<InstitutionalItem> items = new LinkedList<InstitutionalItem>();
		items.addAll(collection.getItems());
		for(InstitutionalItem item : items)
		{
			institutionalItemService.deleteInstitutionalItem(item, deletingUser);
		}

		// delete security for parent
		institutionalCollectionSecurityService.deleteAcl(collection);
	    
	    List<GenericItem> genericItemsToDelete = institutionalCollectionDAO.getAllGenericItemsIncludingChildren(collection);
	    log.debug("Found generic items to delete : " + genericItemsToDelete.size());
	    
	    if( collection.getParent() != null )
	    {
	        collection.getParent().removeChild(collection);
	    }
	    
	    institutionalCollectionDAO.makeTransient(collection);
	    
	}
	
	/**
	 * Get the path for the given folder id.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#getInstitutionalCollectionPath(java.lang.Long)
	 */
	public List<InstitutionalCollection> getPath(
			InstitutionalCollection collection) {
		return institutionalCollectionDAO.getPath(collection);
	}
	
	/**
	 * Get the institutional collection by id.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#getInstitutionalCollection(java.lang.Long, boolean)
	 */
	public InstitutionalCollection getCollection(Long id,
			boolean lock) {
		return institutionalCollectionDAO.getById(id, lock);
	}
	
	/**
	 * Get institutional collections sorting according to the sort and filter information .  
	 * If the parent collection id is null then the root set of collections are 
	 * returned
	 * 
	 * Sort is applied based on the order of sort information in the list (1st to last).
	 * Starts at the specified row start location and stops at specified row end.
	 * 
	 * @param criteria - the criteria of how to sort and filter the information
	 * @param repositoryId - id of the repository
	 * @param parentCollectionId - id of the parent collection 
	 * @param rowStart - start position in paged set
	 * @param rowEnd - end position in paged set
	 * @return List of root collections containing the specified information.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#getInstitutionalCollections(java.util.List, java.lang.Long, java.lang.Long, int, int)
	 */
	public List<InstitutionalCollection> getCollections(
			final Long repositoryId,
			final Long parentCollectionId,
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType)
	{
		
		if( parentCollectionId == null || parentCollectionId.equals(ROOT_COLLECTION_ID))
		{
		    return institutionalCollectionDAO.getRootInstituionalCollections(
				repositoryId, rowStart, 
	    		numberOfResultsToShow, sortType);
		}
		else
		{
			return institutionalCollectionDAO.getSubInstituionalCollections(repositoryId, 
					parentCollectionId, rowStart, 
		    		numberOfResultsToShow, sortType);
		}
	}

    /**
     * Get a count of root collections with given filter list for the specified user.
     * This only returns root collections for the user.  If the parent collections id
     * is null, then selection from the root is assumed.
     *  
     * @param criteria to apply to the selections
     * @param repositoryId - the repository the collection is in
     * @param parentCollectionId - id of the parent collection
     * 
     * @return - the number of collections found
     * 
     * @see edu.ur.ir.institution.InstitutionalCollectionService#getCollectionsCount(List, Long, Long)
     */
    public Long getCollectionsCount(
    		final Long repositoryId,
    		final Long parentCollectionId)
    {
		if( parentCollectionId == null || parentCollectionId.equals(ROOT_COLLECTION_ID))
		{

    	    return institutionalCollectionDAO.getRootInstitutionalCollectionsCount(repositoryId);
		}
		else
		{
			return institutionalCollectionDAO.getSubInstitutionalCollectionsCount(repositoryId, parentCollectionId);
		}
    }

	/**
	 * Make the institutional collection persistent.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#saveInstitutionalCollection(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public void saveCollection(
			InstitutionalCollection institutionalCollection) {
	    institutionalCollectionDAO.makePersistent(institutionalCollection);
	}
	

	/**
	 * Get institutional collection for the given Ids
	 * 
	 * @param collectionIds List of collection ids
	 * 
	 * @return List of institutional collections
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#getCollections(List)
	 */
	public List<InstitutionalCollection> getCollections(List<Long> collectionIds){
		return institutionalCollectionDAO.getInstituionalCollections(collectionIds);
	}
	
	/**
	 * Find if the generic item is already published to this collection.
	 * 
	 * @param  institutionalCollectionId Id of the institutional collection
	 * @param generic item Id 
	 * 
	 * @return True if the generic item is published to the collection else false
	 */
	public boolean isItemPublishedToCollection(Long institutionalCollectionId, Long genericItemId) {
		return institutionalItemDAO.isItemPublishedToCollection(institutionalCollectionId, genericItemId);
	}


	/**
	 * Get all children 
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#getAllChildrenForCollection(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public List<InstitutionalCollection> getAllChildrenForCollection(
			InstitutionalCollection parent) {
		return institutionalCollectionDAO.getAllChildrenForCollection(parent);
	}

	/**
	 * Moves the collections.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#moveCollectionInformation(edu.ur.ir.institution.InstitutionalCollection, java.util.List, java.util.List)
	 */
	public List<InstitutionalCollection> moveCollectionInformation(InstitutionalCollection destination,
			List<InstitutionalCollection> collectionsToMove,
			List<InstitutionalItem> itemsToMove) {
		
		// collections may not be moved due to duplicate collection name
		LinkedList<InstitutionalCollection> collectionsNotMoved = new LinkedList<InstitutionalCollection>();
		// move collections first
		if( collectionsToMove != null )
		{
		    for( InstitutionalCollection collection : collectionsToMove)
		    {
		    	log.debug("Adding collection " + collection + " to destination " + destination);
			    try {
					destination.addChild(collection);
				} catch (DuplicateNameException e) {
					collectionsNotMoved.add(collection);
				}
		    }
		}
		
		if( itemsToMove != null && collectionsNotMoved.size() == 0)
		{
		    for( InstitutionalItem item : itemsToMove)
		    {
		    	log.debug("Adding file " + item + " to destination " + destination);
		        destination.addItem(item);
		    }
		}
		
		if( collectionsNotMoved.size() == 0)
		{
		    institutionalCollectionDAO.makePersistent(destination);
		  
		    if( collectionsToMove != null)
		    {
		        for(InstitutionalCollection movedCollection : collectionsToMove)
		        {
		     	    institutionalCollectionSecurityService.giveAdminPermissionsToParentCollections(movedCollection);
		        }
		    }
		}
		
		return collectionsNotMoved;
	}

	/**
	 * Move the collection to the root of the repository.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#moveCollectionInformation(edu.ur.ir.repository.Repository, java.util.List)
	 */
	public List<InstitutionalCollection> moveCollectionInformation(Repository repository,
			List<InstitutionalCollection> collectionsToMove){

		LinkedList<InstitutionalCollection> collectionsNotMoved = new LinkedList<InstitutionalCollection>();

		if( collectionsToMove != null )
		{
		    for( InstitutionalCollection collection : collectionsToMove)
		    {
		    	log.debug("Adding collection " + collection + " to repository " + repository);
			    try {
					repository.addInstitutionalCollection(collection);
				} catch (DuplicateNameException e) {
					collectionsNotMoved.add(collection);
				}
		    }
		}
		
		if( collectionsNotMoved.size() == 0)
		{
		    repositoryService.saveRepository(repository);
		    for(InstitutionalCollection movedCollection : collectionsToMove)
		    {
		    	institutionalCollectionSecurityService.giveAdminPermissionsToParentCollections(movedCollection);
		    }
		}
		
		return collectionsNotMoved;
	}
	
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	/**
	 * Institutional collection data access.
	 * 
	 * @return
	 */
	public InstitutionalCollectionDAO getInstitutionalCollectionDAO() {
		return institutionalCollectionDAO;
	}

	/**
	 * Institutional collection data access.
	 * 
	 * @param institutionalCollectionDAO
	 */
	public void setInstitutionalCollectionDAO(
			InstitutionalCollectionDAO institutionalCollectionDAO) {
		this.institutionalCollectionDAO = institutionalCollectionDAO;
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
	 * Data access for institutional items.
	 * 
	 * @param institutionalItemDAO
	 */
	public void setInstitutionalItemDAO(InstitutionalItemDAO institutionalItemDAO) {
		this.institutionalItemDAO = institutionalItemDAO;
	}

	public InstitutionalCollectionSecurityService getInstitutionalCollectionSecurityService() {
		return institutionalCollectionSecurityService;
	}

	public void setInstitutionalCollectionSecurityService(
			InstitutionalCollectionSecurityService institutionalCollectionSecurityService) {
		this.institutionalCollectionSecurityService = institutionalCollectionSecurityService;
	}

	/**
	 * Get the count of number of items in a collection and its sub-collections
	 * 
	 * @param collection
	 *  
	 * @return Number of items
	 */
	public Long getInstitutionalItemCountForCollectionAndChildren(InstitutionalCollection collection) {
		return institutionalItemDAO.getCountForCollectionAndChildren(collection);
	}

	/**
	 * Get the count of number of items in a collection
	 * 
	 * @param collection
	 *  
	 * @return Number of items
	 */
	public Long getInstitutionalItemCountForCollection(InstitutionalCollection collection) {
		return institutionalItemDAO.getCount(collection);
	}

	/**
	 * Get the count of institutional collections
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#getCount()
	 */
	public Long getCount() {
		return institutionalCollectionDAO.getCount();
	}
	
	
	/**
	 * Get the count of sub collections and its children
	 * 
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#getTotalSubcollectionCount(InstitutionalCollection)
	 */
	public Long getTotalSubcollectionCount(InstitutionalCollection institutionalCollection) {
		return institutionalCollectionDAO.getAllChildrenCountForCollection(institutionalCollection);
	}
	
	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#getItemIrFileIdsForCollection(Long)
	 */
	public List<Long> getItemIrFileIdsForCollection(Long collectionId) {
		return institutionalCollectionDAO.getIrFileIdsForCollection(collectionId);
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#getItemIrFileIdsForCollectionAndItsChildren(InstitutionalCollection)
	 */
	public List<Long> getItemIrFileIdsForCollectionAndItsChildren(InstitutionalCollection institutionalCollection) {
		return institutionalCollectionDAO.getIrFileIdsForCollectionAndItsChildren(institutionalCollection);
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalCollectionService#getItemIrFileIdsForAllCollections()
	 */
	public List<Long> getItemIrFileIdsForAllCollections() {
		return institutionalCollectionDAO.getIrFileIdsForAllCollections();
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	/**
	 * Send email to reviewer of the item
	 * 
	 * @param institutionalCollection
	 * @param itemName
	 */
	public void sendEmailToReviewer(InstitutionalCollection institutionalCollection, String itemName) {
		
		Set<Sid> sids = institutionalCollectionSecurityService.getSidsWithPermission(institutionalCollection, InstitutionalCollectionSecurityService.REVIEWER_PERMISSION);
		
		for(Sid sid:sids) {
			for(IrUser user : ((IrUserGroup)sid).getUsers()) {
				SimpleMailMessage message = new SimpleMailMessage(itemReviewMessage);
				message.setTo(user.getDefaultEmail().getEmail());
				String text = message.getText();
				text = StringUtils.replace(text, "%FIRSTNAME%", user.getFirstName());
				text = StringUtils.replace(text, "%LASTNAME%", user.getLastName());
				text = StringUtils.replace(text, "%ITEMNAME%", itemName);
				message.setText(text);
		
				try {
					mailSender.send(message);
				} catch (Exception e) {
					log.error(e.getMessage());
					throw new IllegalStateException(e);
				}
			}
		}
	}

	public SimpleMailMessage getItemReviewMessage() {
		return itemReviewMessage;
	}

	public void setItemReviewMessage(SimpleMailMessage itemReviewMessage) {
		this.itemReviewMessage = itemReviewMessage;
	}

	public MailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * Sets all items within a collection as public
	 * 
	 * @param institutionalCollection Collection that has to be set as public
	 */
	public void setAllItemsWithinCollectionPublic(InstitutionalCollection institutionalCollection) {
		
		for(InstitutionalItem institutionalItem: institutionalCollection.getItems()) {
			for(InstitutionalItemVersion version :institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersions()) {
				GenericItem item = version.getItem();
				item.setPubliclyViewable(true);
				
				for(ItemFile file:item.getItemFiles()) {
					file.setPublic(true);
				}
			}
			institutionalItemDAO.makePersistent(institutionalItem);
		}
		
	}

	/**
	 * Sets all items within a collection as private
	 * 
	 * @param institutionalCollection Collection whose items has to be set as private
	 */
	public void setAllItemsWithinCollectionPrivate(InstitutionalCollection institutionalCollection) {

		for(InstitutionalItem institutionalItem: institutionalCollection.getItems()) {
			for(InstitutionalItemVersion version :institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersions()) {
				GenericItem item = version.getItem();
				item.setPubliclyViewable(false);
				
				for(ItemFile file:item.getItemFiles()) {
					file.setPublic(false);
				}
			}
			institutionalItemDAO.makePersistent(institutionalItem);
		}
	}

	
}
