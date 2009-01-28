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


package edu.ur.ir.web.action.researcher;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.item.ItemVersion;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherInstitutionalItem;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherPublication;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action to add publications to researcher page.
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class AddResearcherPublication extends ActionSupport implements UserIdAware{

	/**  Eclipse generated id */
	private static final long serialVersionUID = -6004377183730549814L;

	/** id of the user making the change */
	private Long userId;
	
	/** user service for accessing user information */
	private UserService userService;
	

	/**  Logger for add files to item action */
	private static final Logger log = Logger.getLogger(AddResearcherPublication.class);
	
	/** Service for item.  */
	private ResearcherService researcherService;
	
	/** File system service for user */
	private ItemService itemService;
	
	/** File system service for files */
	private RepositoryService repositoryService;
	
	/** File id to add / remove files action*/
	private Long versionedItemId;
	
	/** User logged in */
	private Researcher researcher;
		
	/**  Personal folder id */
	private Long parentFolderId;
	
	/** id of the publication version */
	private Long itemVersionId;
	
	/** id of the researcher publication to change */
	private Long publicationId;

	/** A collection of folders and files for a user in a given location of
    their personal directory.*/
	private Collection<FileSystem> collectionFileSystem;
	
	 /** A collection of folders and files for a user in a given location of
    their personal directory.*/
	private Collection<FileSystem> researcherFileSystem;
	
	/** set of folders that are the path for the current folder */
	private Collection <ResearcherFolder> researcherFolderPath;

	/** set of folders that are the path for the current folder */
	private Collection <PersonalCollection> collectionPath;

	/** Id of the parent collection */
	private Long parentCollectionId;	
	
	/** description for file */
	private String description;
	
	/** Service for dealing with user file system. */
	private UserPublishingFileSystemService userPublishingFileSystemService;
	
	 /** A collection of folders and files for a user in a given location of
    their personal directory.*/
	List<ResearcherItemFileSystemVersion> researcherItemFileSystemVersions = new LinkedList<ResearcherItemFileSystemVersion>();


    private void loadResearcher()
    {
    	researcher = userService.getUser(userId, false).getResearcher();
    }

	/**
	 * Create the collection file system to view.
	 */
	public String getPersonalCollections()
	{
		
		log.debug("getPersonalCollections");
		loadResearcher();
		

		if(parentCollectionId != null && parentCollectionId > 0)
		{
		    collectionPath = userPublishingFileSystemService.getPersonalCollectionPath(parentCollectionId);
		}
		
		Collection<PersonalCollection> myPersonalCollections = userPublishingFileSystemService.getPersonalCollections(userId, parentCollectionId);
		
		Collection<PersonalItem> myPersonalItems = userPublishingFileSystemService.getPersonalItems(userId, parentCollectionId);
		
	    collectionFileSystem = new LinkedList<FileSystem>();
	    
	    for(PersonalCollection o : myPersonalCollections)
	    {
	    	log.debug("Collection:"+o);
	    	collectionFileSystem.add(o);
	    }
	    
	    for(PersonalItem o: myPersonalItems)
	    {
	    	log.debug("Item:"+o);
	    	collectionFileSystem.add(o);
	    }
	    
	    return SUCCESS;
	    
	}
	
	/**
	 * Add publication to researcher
	 * 
	 */
	public String addResearcherPublication() {

		log.debug("Add publication versionedItemId = " + versionedItemId + " parent folder id = " + parentFolderId);
				
		VersionedItem vi = itemService.getVersionedItem(versionedItemId, false);
		if( !userId.equals(vi.getOwner().getId()))
		{
			return "accessDenied";
		}
		
		loadResearcher();
		if (parentFolderId != null && parentFolderId > 0) {
			
			ResearcherFolder parentFolder = researcherService.getResearcherFolder(parentFolderId, false);
			researcherService.createPublication(parentFolder, vi.getCurrentVersion().getItem(), vi.getLargestVersion());
		} else {
			ItemVersion currentVersion = vi.getCurrentVersion();
			researcher.createRootPublication(currentVersion.getItem(), currentVersion.getVersionNumber());
			researcherService.saveResearcher(researcher);
		}

		return SUCCESS;
	}
	
	/**
	 * Get researcher file sysetm
	 * 
	 */
	public String getResearcherFolders() {
		loadResearcher();
		if(parentFolderId != null && parentFolderId > 0)
		{
			researcherFolderPath = researcherService.getResearcherFolderPath(parentFolderId);
		}
		
		log.debug("Folder Path ::" + researcherFolderPath);
		
		log.debug("**** Parent Folder Id ::" + parentFolderId);
		
		Collection<ResearcherFolder> myResearcherFolders = researcherService.getFoldersForResearcher(researcher.getId(), parentFolderId);
		
		Collection<ResearcherFile> myResearcherFiles = researcherService.getResearcherFiles(researcher.getId(), parentFolderId);

		Collection<ResearcherPublication> myResearcherPublications = researcherService.getResearcherPublications(researcher.getId(), parentFolderId);
		
		Collection<ResearcherLink> myResearcherLinks = researcherService.getResearcherLinks(researcher.getId(), parentFolderId);
		
		Collection<ResearcherInstitutionalItem> myResearcherInstitutionalItems = researcherService.getResearcherInstitutionalItems(researcher.getId(), parentFolderId);
		
		researcherFileSystem = new LinkedList<FileSystem>();
		
    	researcherFileSystem.addAll(myResearcherFolders);
    	researcherFileSystem.addAll(myResearcherFiles);
    	researcherFileSystem.addAll(myResearcherPublications);
    	researcherFileSystem.addAll(myResearcherLinks);
    	researcherFileSystem.addAll(myResearcherInstitutionalItems);
    	createResearcherFileSystemForDisplay(researcherFileSystem);
    	return SUCCESS;
	}
	
	
	/**
	 * Simple class to help with the display of the 
	 * item versions available.
	 * 
	 * @author Nathan Sarr
	 *
	 */
	public class ResearcherItemFileSystemVersion
	{
		private FileSystem researcherFileSystem;
		
		private VersionedItem versions;
		
		public ResearcherItemFileSystemVersion(FileSystem researcherFileSystem, VersionedItem versions)
		{
			this.researcherFileSystem = researcherFileSystem;
			this.versions= versions;
		}

		public FileSystem getResearcherFileSystem() {
			return researcherFileSystem;
		}

		public VersionedItem getVersionedItem() {
			return versions;
		}
	}
	
	/**
	 * Change version of publication in researcher
	 * 
	 */
	public String changePublicationVersion() {
		log.debug("change publication version itemVersionId = " + itemVersionId + " publication Id = " + publicationId);
		loadResearcher();
		ItemVersion itemVersion = itemService.getItemVersion(itemVersionId, false);
		if( itemVersion.getVersionedItem().getOwner().getId() != userId )
		{
			return "accessDenied";
		}
		ResearcherPublication researcherPublication = researcherService.getResearcherPublication(publicationId, false);
		researcherPublication.setPublication(itemVersion.getItem());
		researcherPublication.setVersionNumber(itemVersion.getVersionNumber());
		researcherService.saveResearcher(researcherPublication.getResearcher());
		return SUCCESS;
	}
	
	/**
	 * Execute method
	 * 
	 */
	public String execute() { 
		loadResearcher();
		return SUCCESS;
	}
	
	/*
	 * Retrieves the file version of the IrFile for the display of versions
	 */
	private void createResearcherFileSystemForDisplay(Collection<FileSystem> researcherFileSystem) {
		
		researcherItemFileSystemVersions = new LinkedList<ResearcherItemFileSystemVersion>(); 
			
		for (FileSystem fileSystem:researcherFileSystem) {
			ResearcherItemFileSystemVersion researcherItemFileSystemVersion = null;
			
			if (fileSystem.getFileSystemType().equals(FileSystemType.RESEARCHER_PUBLICATION)) {
				log.debug("getting personal item for generic item " + ((ResearcherPublication)fileSystem).getPublication());
				PersonalItem pi = userPublishingFileSystemService.getPersonalItem( ((ResearcherPublication)fileSystem).getPublication() );
				VersionedItem vi = pi.getVersionedItem();
				researcherItemFileSystemVersion = new ResearcherItemFileSystemVersion(fileSystem, vi);
			} else {
				researcherItemFileSystemVersion = new ResearcherItemFileSystemVersion(fileSystem, null);
			}
			
			
			researcherItemFileSystemVersions.add(researcherItemFileSystemVersion);
		}
		
	}
	
	/**
	 * Id of the version of the publication to set.
	 * 
	 * @return id of the publication to set
	 */
	public Long getItemVersionId() {
		return itemVersionId;
	}

	/**
	 * The id of the publication version to set.
	 * 
	 * @param publicationVersionId
	 */
	public void setItemVersionId(Long itemVersionId) {
		this.itemVersionId = itemVersionId;
	}
	
	/**
	 * Researcher publication to change.
	 * 
	 * @return
	 */
	public Long getPublicationId() {
		return publicationId;
	}

	/**
	 * Researcher publication to change.
	 * 
	 * @param publicationId
	 */
	public void setPublicationId(Long publicationId) {
		this.publicationId = publicationId;
	}
	

	/**
	 * Get the parent folder id
	 * 
	 * @return parent folder id 
	 */
	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	/**
	 * Set the parent folder id
	 * 
	 * @param parentCollectionId parent folder id
	 */
	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}

	/**
	 * Get the researcher
	 * 
	 * @return researcher
	 */
	public Researcher getResearcher() {
		return researcher;
	}

	/**
	 * Set the researcher
	 * 
	 * @param researcher researcher
	 */
	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}

	/**
	 * Get researcher service
	 * 
	 * @return
	 */
	public ResearcherService getResearcherService() {
		return researcherService;
	}

	/**
	 * Set researcher service
	 * 
	 * @param researcherService
	 */
	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}

	/**
	 * Get parent collection id
	 * 
	 * @return
	 */
	public Long getParentFolderId() {
		return parentFolderId;
	}

	/**
	 * Set parent collection id
	 * 
	 * @param parentCollectionId
	 */
	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Collection<ResearcherFolder> getResearcherFolderPath() {
		return researcherFolderPath;
	}

	public void setResearcherFolderPath(
			Collection<ResearcherFolder> researcherFolderPath) {
		this.researcherFolderPath = researcherFolderPath;
	}

	public Collection<PersonalCollection> getCollectionPath() {
		return collectionPath;
	}

	public void setCollectionPath(Collection<PersonalCollection> collectionPath) {
		this.collectionPath = collectionPath;
	}

	public Collection<FileSystem> getCollectionFileSystem() {
		return collectionFileSystem;
	}

	public void setCollectionFileSystem(Collection<FileSystem> collectionFileSystem) {
		this.collectionFileSystem = collectionFileSystem;
	}

	public Collection<FileSystem> getResearcherFileSystem() {
		return researcherFileSystem;
	}

	public void setResearcherFileSystem(Collection<FileSystem> researcherFileSystem) {
		this.researcherFileSystem = researcherFileSystem;
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public Long getVersionedItemId() {
		return versionedItemId;
	}

	public void setVersionedItemId(Long versionedItemId) {
		this.versionedItemId = versionedItemId;
	}

	public UserPublishingFileSystemService getUserPublishingFileSystemService() {
		return userPublishingFileSystemService;
	}

	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}
	
	public List<ResearcherItemFileSystemVersion> getResearcherItemFileSystemVersions() {
		return researcherItemFileSystemVersions;
	}

	/**
	 * id of the user making the change
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
