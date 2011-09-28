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
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.FileSystem;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;
import edu.ur.ir.institution.InstitutionalItemSearchService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.item.ItemVersion;
import edu.ur.ir.repository.RepositoryLicenseNotAcceptedException;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.action.user.FileSystemSortHelper;

public class AddNewInstitutionalItemVersion  extends ActionSupport implements UserIdAware {

	/** Eclipse generated ids */
	private static final long serialVersionUID = 8909050119352493560L;
	
	/**  Id of the user who has the collections  */
	private Long userId;
	
	/** The user who owns the collections  */
	private IrUser user;
	
	/**  User information data access  */
	private UserService userService;
		
    /** A collection of personal collections and items for a user in a given location of
        their personal directory.*/
    private List<FileSystem> fileSystem = new LinkedList<FileSystem>();
    
    /** set of personal collections that are the path for the current personal collection */
    private Collection <PersonalCollection> collectionPath;
	
	/** The collection that owns the listed items and personal collections */
	private Long parentCollectionId = 0l;

	/**  Logger for view personal collections action */
	private static final Logger log = Logger.getLogger(AddNewInstitutionalItemVersion.class);
	
	/** Service for dealing with user file system. */
	private UserPublishingFileSystemService userPublishingFileSystemService;
	
	/** Id of institutional item to which new version has to be added */
	private Long institutionalItemId;
	
	/** Id of Item Version to be added */
	private Long itemVersionId;
	
	/** Item service */
	private ItemService itemService;
	
	/** Item service */
	private InstitutionalItemService institutionalItemService;
	
	/** service for marking items that need to be indexed */
	private InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService;

	/** index processing type service */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/** Institutional item to which version has to be added */
	private InstitutionalItem institutionalItem;
	
	/** Service dealing with institutional item versions */
	private InstitutionalItemVersionService institutionalItemVersionService;
	


	/**
	 * Create the file system to view.
	 */
	public String getPersonalItems()
	{
		user = userService.getUser(userId, false);
		if(parentCollectionId != null && parentCollectionId > 0)
		{
			log.debug("getting path for parent collection id = " + parentCollectionId);
		    collectionPath = userPublishingFileSystemService.getPersonalCollectionPath(parentCollectionId);
		}
		else
		{
			parentCollectionId = 0l;
		}
		
		Collection<PersonalCollection> myPersonalCollections = userPublishingFileSystemService.getPersonalCollectionsForUser(userId, parentCollectionId);
		Collection<PersonalItem> myPersonalItems = userPublishingFileSystemService.getPersonalItems(userId, parentCollectionId);
		
	    fileSystem = new LinkedList<FileSystem>();
	    
	    fileSystem.addAll(myPersonalCollections);
	    fileSystem.addAll(myPersonalItems);
	    
	    FileSystemSortHelper sortHelper = new FileSystemSortHelper();

   		sortHelper.sort(fileSystem, FileSystemSortHelper.NAME_ASC);
   		
   		return SUCCESS;
	}
	
	/**
	 * Execute method
	 */
	public String execute() {
		log.debug("Institutional Item Id = " + institutionalItemId);
		institutionalItem = institutionalItemService.getInstitutionalItem(institutionalItemId, false);
		
		IrUser user = userService.getUser(userId, false);
			
		// only admin and owner of object can execute this action
		if( user == null || !user.hasRole(IrRole.ADMIN_ROLE))
		{
			if(!institutionalItem.getOwner().getId().equals(userId))
			{
				return "accessDenied";
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * Add new version
	 * 
	 * @return
	 * @throws NoIndexFoundException
	 */
	public String addVersion() throws NoIndexFoundException {
		
		institutionalItem = institutionalItemService.getInstitutionalItem(institutionalItemId, false);
		
		IrUser user = userService.getUser(userId, false);
		
		// only admin and owner of object can execute this action
		if( user == null || !user.hasRole(IrRole.ADMIN_ROLE))
		{
			if(!institutionalItem.getOwner().getId().equals(userId))
			{
				return "accessDenied";
			}
		}
		
		ItemVersion version = itemService.getItemVersion(itemVersionId, false);
		
		try {
			institutionalItemService.addNewVersionToItem(user, institutionalItem, version);
		} catch (RepositoryLicenseNotAcceptedException e) {
			return ERROR;
		}

		return SUCCESS;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

	public IrUser getUser() {
		return user;
	}

	public List<FileSystem> getFileSystem() {
		return fileSystem;
	}

	public Collection<PersonalCollection> getCollectionPath() {
		return collectionPath;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}

	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}

	public Long getItemVersionId() {
		return itemVersionId;
	}

	public void setItemVersionId(Long itemVersionId) {
		this.itemVersionId = itemVersionId;
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public InstitutionalItem getInstitutionalItem() {
		return institutionalItem;
	}

	public InstitutionalItemIndexProcessingRecordService getInstitutionalItemIndexProcessingRecordService() {
		return institutionalItemIndexProcessingRecordService;
	}

	public void setInstitutionalItemIndexProcessingRecordService(
			InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService) {
		this.institutionalItemIndexProcessingRecordService = institutionalItemIndexProcessingRecordService;
	}

	public IndexProcessingTypeService getIndexProcessingTypeService() {
		return indexProcessingTypeService;
	}

	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}

}
