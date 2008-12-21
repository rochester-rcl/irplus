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
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.FileSystem;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.item.ItemVersion;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
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
	
	/**  Repository information data access  */
	private RepositoryService repositoryService;
	
	/** Service for indexing institutional items */
	private InstitutionalItemIndexService institutionalItemIndexService;
	
	/** Institutional item to which version has to be added */
	private InstitutionalItem institutionalItem;
	
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
		Collection<PersonalItem> myPersonalItems = userPublishingFileSystemService.getPersonalItemsInCollection(userId, parentCollectionId);
		
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
		
		ItemVersion version = itemService.getItemVersion(itemVersionId, false);
		
		InstitutionalItemVersion institutionalItemVersion = institutionalItem.getVersionedInstitutionalItem().addNewVersion(version.getItem());
		
		institutionalItemService.saveInstitutionalItemVersion(institutionalItemVersion);
		
		/**
		 * If item is not yet published and the collection being published to is private
		 * then set item as private and assign the collection's user group permissions to item
		 */
		if (!version.getItem().isPublishedToSystem() && !institutionalItem.getInstitutionalCollection().isPubliclyViewable()) {
			List<InstitutionalCollection> collections = new LinkedList<InstitutionalCollection>();
			collections.add(institutionalItem.getInstitutionalCollection());
			institutionalItemService.setItemPrivatePermissions(version.getItem(), collections);
		}
		
		
		// update index
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		String indexFolder = repository.getInstitutionalItemIndexFolder().getFullPath();
		institutionalItemIndexService.updateItem(institutionalItem, new File(indexFolder));

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

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public void setInstitutionalItemIndexService(
			InstitutionalItemIndexService institutionalItemIndexService) {
		this.institutionalItemIndexService = institutionalItemIndexService;
	}

	public InstitutionalItem getInstitutionalItem() {
		return institutionalItem;
	}


}
