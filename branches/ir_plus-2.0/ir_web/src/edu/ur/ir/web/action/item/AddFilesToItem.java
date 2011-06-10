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

package edu.ur.ir.web.action.item;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.FileSystem;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.file.FileVersion;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemLink;
import edu.ur.ir.item.ItemObject;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.order.AscendingOrderComparator;

/**
 * Action to allow a user to add files to an item.
 * 
 * @author Nathan Sarr
 *
 */
public class AddFilesToItem extends ActionSupport implements UserIdAware , Preparable {

	/**  Eclipse generated id */
	private static final long serialVersionUID = 309356905404014230L;
	
	/**  Logger for add files to item action */
	private static final Logger log = Logger.getLogger(AddFilesToItem.class);
	
	/** Service for item.  */
	private ItemService itemService;
	
	/** File system service for user */
	private UserFileSystemService userFileSystemService;
	
	/** Institutional item service */
	private InstitutionalItemService institutionalItemService;
	
	/** File system service for files */
	private RepositoryService repositoryService;
	
	/**  Id for the user */
	private Long userId;
	
	/** File id to add / remove files action*/
	private Long versionedFileId;
	
	/** Folder id to add files */
	private Long folderId;
	
	/** Id of the parent collection holding this personal item */
	private Long parentCollectionId;
	
	/** Id of the item file */
	private Long itemObjectId;
	
	/** Type of item object */
	private String itemObjectType;
	
	/** Id of the file version */
	private Long fileVersionId;
		
	/**  Personal folder id */
	private Long parentFolderId;
	
	/** A collection of folders and files for a user in a given location of
    their personal directory.*/
	private Collection<FileSystem> fileSystem;
	
	/** set of folders that are the path for the current folder */
	private Collection <PersonalFolder> folderPath;
	
	/** Generic item being edited */
	private GenericItem item;
	
	/** Item file and its versions */
	private List<ItemFileVersion> itemFileVersions; 	
	
	/** description for file */
	private String description;
	
	/** Service for dealing with user file system. */
	private UserPublishingFileSystemService userPublishingFileSystemService;
	
	/** Indicates if the file is added to Item */
	private boolean fileAdded;
	
	/** Message to display for user */
	private String message;
	
	/** Id of Generic item being edited */
	private Long genericItemId;
	
	/** Id of personal item */
	private Long personalItemId;

	/** Id of institutional item being edited */
	private Long institutionalItemId;
	
	/** index processing type service */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/** service for user data */
	private UserService userService;
	
	/** process for setting up personal workspace information to be indexed */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;
	
	/** service for dealing with institutional item version information */
	private InstitutionalItemVersionService institutionalItemVersionService;

	/**
	 * Prepare for action
	 */
	public void prepare() {
		
		log.debug("Item Id:"+ genericItemId);
		
		if (genericItemId != null) {
			item = itemService.getGenericItem(genericItemId, false);
		}

	}
	
	/**
	 * Create the file system to view.
	 */
	public String getFolders()
	{
		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
		    return "accessDenied";
		}
		if(parentFolderId != null && parentFolderId > 0)
		{
		    folderPath = userFileSystemService.getPersonalFolderPath(parentFolderId);
		}
		
		Collection<PersonalFolder> myPersonalFolders = userFileSystemService.getPersonalFoldersForUser(userId, parentFolderId);
		
		Collection<PersonalFile> myPersonalFiles = userFileSystemService.getPersonalFilesInFolder(userId, parentFolderId);
		
	    fileSystem = new LinkedList<FileSystem>();
	    
	    for(PersonalFolder o : myPersonalFolders)
	    {
	    	fileSystem.add(o);
	    }
	    
	    for(PersonalFile o: myPersonalFiles)
	    {
	    	fileSystem.add(o);
	    }
	    
	    return SUCCESS;
	    
	}
	
	/**
	 * Add file to item
	 * 
	 */
	public String addFile() throws NoIndexFoundException {
		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
		    return "accessDenied";
		}
		fileAdded = true;
		
		// assume most recent version 
		if (versionedFileId != null) {
			VersionedFile vf = repositoryService.getVersionedFile(versionedFileId, false);
			
			for(ItemFile itemFile:item.getItemFiles()) {
					if (vf.getNameWithExtension().equals(itemFile.getIrFile().getNameWithExtension())) {
						fileAdded = false;
						message = getText("fileNameExistInItemError", 
								new String[]{vf.getName()});;
						break;
					}
			}
			
			if (fileAdded)
			{
				ItemFile itemFile = item.addFile(vf.getCurrentVersion().getIrFile());
				itemFile.setDescription(vf.getDescription());
				itemFile.setVersionNumber(vf.getLargestVersion());
				itemFile.setPublic(item.isPubliclyViewable());
				itemService.makePersistent(item);
			}
		}

		if (folderId != null) {
			PersonalFolder personalFolder = userFileSystemService.getPersonalFolder(folderId, false);
			List<PersonalFile> files = userFileSystemService.getAllFilesForFolder(personalFolder);
			List<VersionedFile> versionedFiles = repositoryService.getVersionedFilesForItem(item);
			
			StringBuffer buffer = new StringBuffer();
			for (PersonalFile pf:files) {
				fileAdded = true;

				if (!versionedFiles.contains(pf.getVersionedFile())) {
					
					// Add only if user is file owner
					if (pf.getVersionedFile().getOwner().getId().equals(userId)) {
					
						for(ItemFile itemFile:item.getItemFiles()) {
							if (pf.getVersionedFile().getName().equals(itemFile.getIrFile().getName())) {
								fileAdded = false;
								buffer.append(pf.getName());
								buffer.append(",");
								break;
							}
						}

					
						if(fileAdded) {
							ItemFile itemFile = item.addFile(pf.getVersionedFile().getCurrentVersion().getIrFile());
							if (itemFile != null) {
							    itemFile.setDescription(pf.getVersionedFile().getDescription());
								itemFile.setVersionNumber(pf.getVersionedFile().getLargestVersion());
							}
						}
					}
				}
			}
			itemService.makePersistent(item);

			if (buffer.length() > 0) {
				buffer.deleteCharAt(buffer.length() -1);
				message = getText("listOfFileNamesExist", 
						new String[]{buffer.toString()});
				fileAdded = false;
			}
		}
		
		// Update personal item index
		PersonalItem personalItem = userPublishingFileSystemService.getPersonalItem(item);

		// Check if personal item exist for this generic item - if not it means that user is editing the institutional item
		// in which case we don't have to update personal item index
		if (personalItem != null) {
			userWorkspaceIndexProcessingRecordService.save(personalItem.getOwner().getId(), personalItem, 
	    	indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		}
		
		institutionalItemService.markAllInstitutionalItemsForIndexing(genericItemId, indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		institutionalItemVersionService.setAllVersionsAsUpdated(user, genericItemId, "one or more files changed");

		return SUCCESS;
	}
	
	/**
	 * Change version of file in item
	 * 
	 */
	public String changeFileVersion() throws NoIndexFoundException {
		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
		    return "accessDenied";
		}
		FileVersion fileVersion = repositoryService.getFileVersion(fileVersionId, false);
		ItemFile itemFile = item.getItemFile(itemObjectId);
		itemFile.setIrFile(fileVersion.getIrFile());
		itemFile.setVersionNumber(fileVersion.getVersionNumber());

		itemService.makePersistent(item);

		// Update personal item index
		PersonalItem personalItem = userPublishingFileSystemService.getPersonalItem(item);

		// Check if personal item exist for this generic item - if not it means that user is editing the institutional item
		// in which case we don't have to update personal item index
		if (personalItem != null) {
			userWorkspaceIndexProcessingRecordService.save(personalItem.getOwner().getId(), personalItem, 
	    			indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		}
		
		institutionalItemService.markAllInstitutionalItemsForIndexing(genericItemId, indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		institutionalItemVersionService.setAllVersionsAsUpdated(user, genericItemId, "one or more files changed");

		return getFiles();
	}
	
	/**
	 * Update description for the file
	 * 
	 */
	public String updateDescription() {
		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
		    return "accessDenied";
		}
		ItemObject itemObject = item.getItemObject(itemObjectId, itemObjectType);
		itemObject.setDescription(description);

		itemService.makePersistent(item);

		return getFiles();
	}	
	
	/**
	 * Get personal files for the selected personal file ids
	 * 
	 */
	public String getFiles() {
		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
		    return "accessDenied";
		}
		List<ItemObject> itemObjects = item.getItemObjects();
		
		// Sort item objects by order
		Collections.sort(itemObjects,  new AscendingOrderComparator());
		
		createItemFileVersionForDisplay(itemObjects);
		
		return SUCCESS;
	}
	

	
	/*
	 * Retrieves the file version of the IrFile for the display of versions
	 */
	private void createItemFileVersionForDisplay(List<ItemObject> itemObjects) {
		
		itemFileVersions = new LinkedList<ItemFileVersion>(); 
			
		for (ItemObject itemObject:itemObjects) {
			ItemFileVersion itemFileVersion = null;
			
			if (itemObject.getType().equals(ItemFile.TYPE)) {
				VersionedFile vf = repositoryService.getVersionedFileByIrFile(((ItemFile)itemObject).getIrFile());
				
				Set<FileVersion> fileVersions = null;
				
				if (vf != null) {
					fileVersions = vf.getVersions();
				}
				
				itemFileVersion = new ItemFileVersion(itemObject, fileVersions);
			} else {
				itemFileVersion = new ItemFileVersion(itemObject, null);
			}
			
			
			itemFileVersions.add(itemFileVersion);
		}
		
	}
	
	/**
	 * Removes the file from the item
	 * 
	 */
	public String removeFile() throws NoIndexFoundException {
		log.debug("Remove FileId = " + itemObjectId);

		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
		    return "accessDenied";
		}
		ItemObject removeItemObject = item.getItemObject(itemObjectId, itemObjectType);
		
		if (itemObjectType.equalsIgnoreCase(ItemFile.TYPE)) {
			Set<Long> irFileId = new HashSet<Long>();
			ItemFile itemFile = (ItemFile) removeItemObject;
			irFileId.add(itemFile.getIrFile().getId());
			
			item.removeItemFile(itemFile);
			
			itemService.makePersistent(item);
			
			// Delete irFile if not used by PersonalFile, ItemFile or ResearcherFile
			itemService.deleteUnUsedIrFiles(irFileId);
		} else {
			ItemLink itemLink = (ItemLink) removeItemObject;
			
			item.removeLink(itemLink);
			
			itemService.makePersistent(item);
			
		}

		// Update personal item index
		PersonalItem personalItem = userPublishingFileSystemService.getPersonalItem(item);

		// Check if personal item exist for this generic item - if not it means that user is editing the institutional item
		// in which case we don't have to update personal item index
		if (personalItem != null) {
			userWorkspaceIndexProcessingRecordService.save(personalItem.getOwner().getId(), personalItem, 
	    			indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		}
		
		institutionalItemService.markAllInstitutionalItemsForIndexing(genericItemId, indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		institutionalItemVersionService.setAllVersionsAsUpdated(user, genericItemId, "one or more files changed");

		return getFiles();
	}
	
	
	/**
	 * Moves the file up for sorting
	 * 
	 */
	public String moveFileUp() {
		log.debug("Item file Id::"+itemObjectId);
		
		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
		    return "accessDenied";
		}
		
		ItemObject moveUpItemObject = item.getItemObject(itemObjectId, itemObjectType);
		item.moveItemObject(moveUpItemObject, moveUpItemObject.getOrder() - 1);
		
		// Save the item
		itemService.makePersistent(item);

		return SUCCESS;
		
	}
	
	/**
	 * Moves the file down for sorting
	 * 
	 */
	public String moveFileDown() {
		log.debug("Item file Id::"+itemObjectId);
		IrUser user = userService.getUser(userId, false);
		if( !item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE))
		{
		    return "accessDenied";
		}
		
		ItemObject moveDownItemObject = item.getItemObject(itemObjectId, itemObjectType);
		item.moveItemObject(moveDownItemObject, moveDownItemObject.getOrder() + 1);
		
		// Save the item
		itemService.makePersistent(item);

		
		return SUCCESS;
		
	}
	
	/**
	 * Shows the item edit page
	 * 
	 */
	public String execute() { 
		
		IrUser user = userService.getUser(userId, false);
		
		if (genericItemId != null) {
			item = itemService.getGenericItem(genericItemId, false);
		}
		
		if( item != null )
		{
			if( !item.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE))
			{
				return "accessDenied";
			}
		}
		
		return SUCCESS;
	} 
	
	/**
	 * Creates new publication version
     */
	public String createPublicationVersion() {
		PersonalItem personalItem = userPublishingFileSystemService.getPersonalItem(personalItemId, false);
		IrUser user = null;
		if( userId != null )
		{
		   user = userService.getUser(userId, false);
		}
		
		if( user == null || (!personalItem.getOwner().equals(user) && !user.hasRole(IrRole.ADMIN_ROLE)))
		{
		    return "accessDenied";
		}
		
		
		GenericItem oldItem = personalItem.getVersionedItem().getCurrentVersion().getItem();
		item = oldItem.clone();
		
		personalItem.getVersionedItem().addNewVersion(item);

		userPublishingFileSystemService.makePersonalItemPersistent(personalItem);		

		userWorkspaceIndexProcessingRecordService.save(personalItem.getOwner().getId(), personalItem, 
    			indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));

		return SUCCESS;
	}

	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Get the user id
	 * 
	 * @return Id of the user
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Get the folder path
	 * 
	 * @return Collection of personal folder
	 */
	public Collection<PersonalFolder> getFolderPath() {
		return folderPath;
	}

	/**
	 * Set the folders in the path
	 * 
	 * @param folderPath collection of folders in the path 
	 */
	public void setFolderPath(Collection<PersonalFolder> folderPath) {
		this.folderPath = folderPath;
	}

	/**
	 * Get the files and folders
	 * 
	 * @return Collection of files and folders
	 */
	public Collection<FileSystem> getFileSystem() {
		return fileSystem;
	}

	/**
	 * Set the files and folders
	 * 
	 * @param fileSystem Collection of files and folders
	 */
	public void setFileSystem(Collection<FileSystem> fileSystem) {
		this.fileSystem = fileSystem;
	}

	/**
	 * Get the parent folder id
	 * 
	 * @return parent folder id 
	 */
	public Long getParentFolderId() {
		return parentFolderId;
	}

	/**
	 * Set the parent folder id
	 * 
	 * @param parentFolderId parent folder id
	 */
	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	/**
	 * Get the file id
	 * 
	 * @return versioned file id
	 */
	public Long getVersionedFileId() {
		return versionedFileId;
	}

	/**
	 * Set versioned file id
	 * 
	 * @param versionedFileId file id
	 */
	public void setVersionedFileId(Long versionedFileId) {
		this.versionedFileId = versionedFileId;
	}

	/**
	 * Get the item id
	 * 
	 * @return item id
	 */
	public Long getPersonalItemId() {
		return personalItemId;
	}

	/**
	 * Set the item id
	 * 
	 * @param personalItemId item id
	 */
	public void setPersonalItemId(Long personalItemId) {
		this.personalItemId = personalItemId;
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
	public ItemService getItemService() {
		return itemService;
	}

	/**
	 * Set item service
	 * 
	 * @param itemService
	 */
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public GenericItem getItem() {
		return item;
	}

	public void setItem(GenericItem item) {
		this.item = item;
	}

	/**
	 * Simple class to help with the display of the 
	 * item file and the file versions available.
	 * 
	 * @author Sharmila Ranganathan
	 *
	 */
	public class ItemFileVersion
	{
		private ItemObject itemObject;
		
		private Set<FileVersion> versions = new HashSet<FileVersion>();
		
		public ItemFileVersion(ItemObject itemObject, Set<FileVersion> versions)
		{
			this.itemObject = itemObject;
			this.versions= versions;
		}

		public ItemObject getItemObject() {
			return itemObject;
		}

		public void setItemObject(ItemObject itemObject) {
			this.itemObject = itemObject;
		}

		public Set<FileVersion> getVersions() {
			return versions;
		}

		public void setVersions(Set<FileVersion> versions) {
			this.versions = versions;
		}
	}

	public List<ItemFileVersion> getItemFileVersions() {
		return itemFileVersions;
	}

	public void setItemFileVersions(List<ItemFileVersion> itemFileVersions) {
		this.itemFileVersions = itemFileVersions;
	}

	public Long getItemObjectId() {
		return itemObjectId;
	}

	public void setItemObjectId(Long itemObjectId) {
		this.itemObjectId = itemObjectId;
	}

	public Long getFileVersionId() {
		return fileVersionId;
	}

	public void setFileVersionId(Long fileVersionId) {
		this.fileVersionId = fileVersionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public UserPublishingFileSystemService getUserPublishingFileSystemService() {
		return userPublishingFileSystemService;
	}

	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}

	/**
	 * Get item object type
	 *  
	 * @return
	 */
	public String getItemObjectType() {
		return itemObjectType;
	}

	/**
	 * Set item object type
	 * 
	 * @param itemObjectType
	 */
	public void setItemObjectType(String itemObjectType) {
		this.itemObjectType = itemObjectType;
	}
	
	/**
	 * Get number of item objects
	 * 
	 * @return
	 */
	public int getNumberOfItemObjects() {
		return itemFileVersions.size();
	}

	public boolean isFileAdded() {
		return fileAdded;
	}

	public String getMessage() {
		return message;
	}

	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

	public Long getGenericItemId() {
		return genericItemId;
	}

	public void setGenericItemId(Long genericItemId) {
		this.genericItemId = genericItemId;
	}

	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	public IndexProcessingTypeService getIndexProcessingTypeService() {
		return indexProcessingTypeService;
	}

	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

	public UserWorkspaceIndexProcessingRecordService getUserWorkspaceIndexProcessingRecordService() {
		return userWorkspaceIndexProcessingRecordService;
	}

	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}
	
	
	
	

	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}
		
	
	
	


}
