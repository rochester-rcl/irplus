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


package edu.ur.ir.item.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TransformedFile;
import edu.ur.ir.institution.ReviewableItemService;
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.ExternalPublishedItemDAO;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.item.ItemContributor;
import edu.ur.ir.item.ItemExtent;
import edu.ur.ir.item.ItemExtentDAO;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemFileDAO;
import edu.ur.ir.item.ItemIdentifier;
import edu.ur.ir.item.ItemIdentifierDAO;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.item.ItemVersion;
import edu.ur.ir.item.ItemVersionDAO;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.item.VersionedItemDAO;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorDAO;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserFileSystemService;

/**
 * Service methods for item
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 * 
 *
 */
public class DefaultItemService implements ItemService {

	/** eclipse generated id  */
	private static final long serialVersionUID = 3943102947344838093L;

	/**  Get the logger for this class */
	private static final Logger log = LogManager.getLogger(DefaultItemService.class);
	
	/**  Data access for versioned items. */
	private VersionedItemDAO versionedItemDAO;
	
	/**  Data access for item file. */
	private ItemFileDAO itemFileDAO;
	
	/**  Data access for item version */
	private ItemVersionDAO itemVersionDAO;
	
	/**  Data access for item version */
	private ExternalPublishedItemDAO externalPublishedItemDAO;	

	/**  Service for user file system */
	private UserFileSystemService userFileSystemService;
	
	/**  Data access for items */
	private GenericItemDAO itemDAO;
	
	/**  Data access for item identifiers */
	private ItemIdentifierDAO itemIdentifierDAO;
	
	/**  Data access for item extents types */
	private ItemExtentDAO itemExtentDAO;
	
	/**  repository service  */
	private RepositoryService repositoryService;
	
	/** Services for dealing with researcher file system information */
	private ResearcherFileSystemService researcherFileSystemService;
	
	/** Security service */
	private SecurityService securityService;
	
	/** deal with contribution information */
	private ContributorDAO contributorDAO;
	
	/** Reviewable item service */
	private ReviewableItemService reviewableItemService;
	

	/**
	 * Get the item version data access
	 * 
	 * @return
	 */
	public ItemVersionDAO getItemVersionDAO() {
		return itemVersionDAO;
	}

	/**
	 * Set item version data access
	 * 
	 * @param itemVersionDAO
	 */
	public void setItemVersionDAO(ItemVersionDAO itemVersionDAO) {
		this.itemVersionDAO = itemVersionDAO;
	}

	/**
	 * Get item file data access
	 * 
	 * @return
	 */
	public ItemFileDAO getItemFileDAO() {
		return itemFileDAO;
	}

	/**
	 * Set item file data access
	 * 
	 * @param itemFileDAO
	 */
	public void setItemFileDAO(ItemFileDAO itemFileDAO) {
		this.itemFileDAO = itemFileDAO;
	}

	/**
	 * Deletes a versioned item.
	 * 
	 * @see edu.ur.ir.item.ItemService#deleteVersionedItem(edu.ur.ir.item.VersionedItem)
	 */
	public void deleteVersionedItem(VersionedItem versionedItem) {
		Set<ItemVersion> itemVersions = versionedItem.getItemVersions();
		List<GenericItem> itemsToDelete = new LinkedList<GenericItem>();
		
		for(ItemVersion itemVersion : itemVersions)
		{
			// Check if GenericItem is used by researcher page or published to Institutional Collection*/
			if ((researcherFileSystemService.getResearcherPublicationCount(itemVersion.getItem()) == 0)
					&& (!itemVersion.getItem().isPublishedToSystem())) 
			{
				itemsToDelete.add(itemVersion.getItem());
			}
		}
		versionedItemDAO.makeTransient(versionedItem);
		
		for( GenericItem item : itemsToDelete)
		{
			deleteItem(item);
		}
	}
	
	/**
	 * Get the versioned item with the specified id.
	 * 
	 * @see edu.ur.ir.item.ItemService#getVersionedItem(java.lang.Long, boolean)
	 */
	public VersionedItem getVersionedItem(Long id, boolean lock)
	{
		return versionedItemDAO.getById(id, lock);
	}

	/**
	 * Get the versioned item data access object.
	 * 
	 * @return
	 */
	public VersionedItemDAO getVersionedItemDAO() {
		return versionedItemDAO;
	}

	/**
	 * Set the versioned item data access object.
	 * 
	 * @param versionedItemDAO
	 */
	public void setVersionedItemDAO(VersionedItemDAO versionedItemDAO) {
		this.versionedItemDAO = versionedItemDAO;
	}

	/**
	 * Save the item file
	 * 
	 * @param itemFile
	 */
	public void saveItemFile(ItemFile itemFile) {
		itemFileDAO.makePersistent(itemFile);
	}
	
	/**
	 * Delete item file
	 * 
	 * @param itemFile
	 */
	public void deleteItemFile(ItemFile itemFile) {
		itemFileDAO.makeTransient(itemFile);
	}
	
	/**
	 * Get the count of items using this file
	 * 
	 * @param irFile irFile used by item
	 */
	public Long getItemFileCount(IrFile irFile) {
		return itemFileDAO.getItemFileCount(irFile.getId());
	}
	
	/**
	 * Get all item files uses the specified ir file.
	 * 
	 * @param irFile - ir file being used
	 * @return the list of item files being used.
	 */
	public List<ItemFile> getItemFilesWithIrFile(IrFile irFile){
		return itemFileDAO.getItemFilesWithIrFile(irFile);
	}

	/**
	 * Deletes the IrFiles that are not used by any item and PersonalFiles
	 * 
	 * @param irFileIds Set of ir file ids to be deleted
	 * 
	 */
	public void deleteUnUsedIrFiles(Set<Long> irFileIds) {
		
		LinkedList<IrFile> files = new LinkedList<IrFile>();
		LinkedList<FileInfo> fileInfos = new LinkedList<FileInfo>();
		
		for (Long irFileId : irFileIds) {
			IrFile irFile = repositoryService.getIrFile(irFileId, false);

			//Check if this IrFile is being used by any Item or PersonalFile.
			//If yes, then do not add the IrFile and FileInfo to the list to be deleted.
			if ( (userFileSystemService.getPersonalFileCount(irFile) == 0) && (getItemFileCount(irFile) == 0)
					&& (researcherFileSystemService.getResearcherFileCount(irFile) == 0)) {
				log.debug("Adding Ir file " + irFile);
				files.add(irFile);
				fileInfos.add(irFile.getFileInfo());
				
				// make sure we also remove any transformed files.
				// from the file system and file database
				Set<TransformedFile> transfromedFiles = irFile.getTransformedFiles();
				for( TransformedFile tf : transfromedFiles)
				{
				    FileInfo transformedFileInfo = tf.getTransformedFile();	
				    log.debug("Add transformed file id " + transformedFileInfo);
				    fileInfos.add(transformedFileInfo);
				}
			}
		}
		
		// Delete Irfiles
		for(IrFile file : files)
		{
			repositoryService.deleteIrFile(file);
		}
	}

	/**
	 * Delete the item.
	 * 
	 * @param item - Item to be deleted
	 * 
	 */
	public void deleteItem(GenericItem item) {
		log.debug("deleting generic item " + item);
		LinkedList<IrFile> files = new LinkedList<IrFile>();
		Set<ItemFile> itemFiles = item.getItemFiles();

		// Delete review history
		reviewableItemService.deleteReviewHistoryForItem(item);

		// Delete Item Acl
		IrAcl itemAcl = securityService.getAcl(item);
		if (itemAcl != null) {
			securityService.deleteAcl(itemAcl);
		}
		
		// Loop through the itemFiles and get the IrFile and FileInfos 
		// that are used by PersonalFile or Item
		for (ItemFile itemFile : itemFiles) {
			
			// Delete Item file Acl
			IrAcl fileAcl = securityService.getAcl(itemFile);
			if (fileAcl != null) {
				securityService.deleteAcl(fileAcl);
			}
			
			IrFile irFile = itemFile.getIrFile();
					
			//Check if this IrFile is being used by any Item.
			//If yes, then do not add the IrFile and FileInfo to the list to be deleted.
			Long itemFileCount = getItemFileCount(irFile);
			Long fileSystemFileCount = userFileSystemService.getPersonalFileCount(irFile);
			Long researcherFileCount = researcherFileSystemService.getResearcherFileCount(irFile);
			
			if (itemFileCount == 1l && fileSystemFileCount == 0
						&& researcherFileCount == 0) {
				
				log.debug("Adding Ir file " + irFile);
				files.add(irFile);
			}
			else
			{
				log.debug("NOT adding file " + irFile + " itemFileCount = " + itemFileCount + 
						"file system file count  = " + fileSystemFileCount + " researcher file count = " + researcherFileCount);
			}
		}
		
		List<Contributor> contributors = new LinkedList<Contributor>();
		
		for(ItemContributor itemContributor : item.getContributors())
		{
			contributors.add(itemContributor.getContributor());
		}
		
		// Delete Item
		itemDAO.makeTransient(item);
		deleteOrphanContributors(contributors);
		
		// Delete Irfiles
		for(IrFile file : files)
		{
			repositoryService.deleteIrFile(file);
		}
				
	}
	
	/**
	 * Deletes contributors that are orphaned.
	 * 
	 * @param itemContributors
	 */
	private void deleteOrphanContributors(List<Contributor> contributors)
	{
		for( Contributor contributor : contributors)
		{
			Long count = itemDAO.getItemContributionCount(contributor);
			log.debug("item contributor count = " + count + " for contributor "  + contributor);
			if(count == 0l)
			{
				log.debug( " deleteing contributor " + contributor);
				contributorDAO.makeTransient(contributor);
			}
		}
	}

	/**
	 * Get user file system service
	 * 
	 * @return
	 */
	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	/**
	 * Set user file system service
	 * 
	 * @param userFileSystemService
	 */
	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

	/**
	 * Get item service
	 * 
	 * @return
	 */
	public GenericItemDAO getItemDAO() {
		return itemDAO;
	}

	/**
	 * Set item service
	 * 
	 * @param itemDAO
	 */
	public void setItemDAO(GenericItemDAO itemDAO) {
		this.itemDAO = itemDAO;
	}
	
	/**
	 * Save item
	 * 
	 * @param item
	 */
	public void makePersistent(GenericItem item) {
		itemDAO.makePersistent(item);
	}
	
	/**
	 * Get the item with the specified id.
	 * 
	 * @see edu.ur.ir.item.ItemService#getGenericItem(java.lang.Long, boolean)
	 */
	public GenericItem getGenericItem(Long id, boolean lock)
	{
		return itemDAO.getById(id, lock);
	}

	/**
	 * Get the item file with the specified id.
	 * 
	 * @see edu.ur.ir.item.ItemService#getItemFile(java.lang.Long, boolean)
	 */
	public ItemFile getItemFile(Long id, boolean lock)
	{
		return itemFileDAO.getById(id, lock);
	}

	/**
	 * Get the item version with the specified id.
	 * 
	 * @see edu.ur.ir.item.ItemService#getItemVersion(java.lang.Long, boolean)
	 */
	public ItemVersion getItemVersion(Long id, boolean lock)
	{
		return itemVersionDAO.getById(id, lock);
	}
	
	/**
	 * Get the list of items owned by an user
	 * 	
	 * @see edu.ur.ir.item.ItemService#getAllItemsForUser(IrUser)
	 */
	public List<GenericItem> getAllItemsForUser(IrUser user) {
		return itemDAO.getAllItemsForUser(user.getId());
	}

	public ItemIdentifierDAO getItemIdentifierDAO() {
		return itemIdentifierDAO;
	}

	public ItemExtentDAO getItemExtentDAO() {
		return itemExtentDAO;
	}
	
	public void setItemIdentifierDAO(ItemIdentifierDAO itemIdentifierDAO) {
		this.itemIdentifierDAO = itemIdentifierDAO;
	}

	public void setItemExtentDAO(ItemExtentDAO itemExtentDAO) {
		this.itemExtentDAO = itemExtentDAO;
	}
	
	public void saveItemIdentifier(ItemIdentifier itemIdentifier) {
		itemIdentifierDAO.makePersistent(itemIdentifier);
	}
	
	public void deleteItemIdentifier(ItemIdentifier itemIdentifier) {
		itemIdentifierDAO.makeTransient(itemIdentifier);
	}
	
	public void saveItemExtent(ItemExtent itemExtent) {
		itemExtentDAO.makePersistent(itemExtent);
	}
	
	public void deleteItemExtent(ItemExtent itemExtent) {
		itemExtentDAO.makeTransient(itemExtent);
	}
	
	/**
	 * Number of items having the person as contributor
	 * 
	 * @see edu.ur.ir.item.ItemService#getItemCountByContributor(PersonName)
	 */
	public Long getItemCountByContributor(Contributor contributor) {
		return  itemDAO.getItemContributionCount(contributor);
	}

	/**
	 * Delete all versioned items for the user.
	 * 
	 * @see edu.ur.ir.item.ItemService#getAllVersionedItemsForUser(edu.ur.ir.user.IrUser)
	 */
	public List<VersionedItem> getAllVersionedItemsForUser(IrUser user) {
		return versionedItemDAO.getAllVersionedItemsForUser(user);
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**
	 * Get externally published data access
	 * 
	 * @return
	 */
	public ExternalPublishedItemDAO getExternalPublishedItemDAO() {
		return externalPublishedItemDAO;
	}

	/**
	 * Set externally published data access
	 * 
	 * @param externalPublishedItemDAO
	 */
	public void setExternalPublishedItemDAO(
			ExternalPublishedItemDAO externalPublishedItemDAO) {
		this.externalPublishedItemDAO = externalPublishedItemDAO;
	}
	
	/**
	 * Delete item's externally published data
	 * 
	 * @see edu.ur.ir.item.ItemService#deleteExternalPublishedItem(ExternalPublishedItem)
	 */
	public void deleteExternalPublishedItem(ExternalPublishedItem externalPublishedItem) {
		externalPublishedItemDAO.makeTransient(externalPublishedItem);
	}

	/**
	 * Get the count of item version using this generic item
	 * 
	 * @see edu.ur.ir.item.ItemService#getItemVersionCount(GenericItem)
	 */
	public Long getItemVersionCount(GenericItem item) {
		return itemVersionDAO.getItemVersionCount(item.getId());
	}

	public ContributorDAO getContributorDAO() {
		return contributorDAO;
	}

	public void setContributorDAO(ContributorDAO contributorDAO) {
		this.contributorDAO = contributorDAO;
	}

	
	public Long getItemCountByPersonName(PersonName personName) {
		return itemDAO.getContributionCountByPersonName(personName);
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public void setReviewableItemService(ReviewableItemService reviewableItemService) {
		this.reviewableItemService = reviewableItemService;
	}

	public ResearcherFileSystemService getResearcherFileSystemService() {
		return researcherFileSystemService;
	}

	public void setResearcherFileSystemService(
			ResearcherFileSystemService researcherFileSystemService) {
		this.researcherFileSystemService = researcherFileSystemService;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.item.ItemService#getContributorTypeCount(edu.ur.ir.person.ContributorType)
	 */
	public Long getContributorTypeCount(ContributorType contributorType) {
		return itemDAO.getContributorTypeCount(contributorType);
	}

}
