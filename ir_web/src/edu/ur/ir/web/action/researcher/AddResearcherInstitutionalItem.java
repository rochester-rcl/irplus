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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.FileSystem;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherInstitutionalItem;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherPublication;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action to add institutional item to researcher page.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class AddResearcherInstitutionalItem extends ActionSupport implements Preparable, UserIdAware{

	/**  Eclipse generated id */
	private static final long serialVersionUID = -5294823577282271716L;

	/**  Logger for add files to item action */
	private static final Logger log = LogManager.getLogger(AddResearcherInstitutionalItem.class);
	
	/** Service for item.  */
	private ResearcherService researcherService;
	
	/** Service for dealing with researcher file system information */
	private ResearcherFileSystemService researcherFileSystemService;
	
	/** File system service for user */
	private ItemService itemService;
	
	/** File system service for files */
	private RepositoryService repositoryService;
	
	/** File id to add / remove files action*/
	private Long institutionalItemId;
	
	/** Id of the personal item to add the files */
	private Long researcherId;
	
	/** User logged in */
	private Researcher researcher;
		
	/**  Personal folder id */
	private Long parentFolderId;
	
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
	
	/** Institutional item service */
	private InstitutionalItemService institutionalItemService;
	
	private InstitutionalItem institutionalItem;
	
	/** id of the user tyring to make the changes */
	private Long userId;


	/**
	 * Prepare for action
	 */
	public void prepare() {
		
		log.debug("researcherId Id:"+ researcherId);
		
		if (researcherId != null) {
			researcher = researcherService.getResearcher(researcherId, false);
		}

	}
	
	/**
	 * Add institutional item to researcher
	 * 
	 */
	public String addResearcherInstitutionalItem() {
		
		log.debug("Add  institutionalItemId = " + institutionalItemId);
	
		if( researcher == null || !researcher.getUser().getId().equals(userId))
		{
			return "accessDenied";
		}
		InstitutionalItem item = institutionalItemService.getInstitutionalItem(institutionalItemId, false);

		if (parentFolderId != null && parentFolderId > 0) 
		{
			
			ResearcherFolder parentFolder = researcherFileSystemService.getResearcherFolder(parentFolderId, false);
			if( !parentFolder.getResearcher().getId().equals(researcher.getId()))
			{
				return "accessDenied";
			}
			ResearcherInstitutionalItem researcherItem = researcherFileSystemService.createInstitutionalItem(parentFolder, item);
		    researcherItem.setDescription(item.getDescription());
		    researcherFileSystemService.saveResearcherInstitutionalItem(researcherItem);
		} 
		else 
		{
			ResearcherInstitutionalItem researcherItem = researcher.createRootInstitutionalItem(item);
			researcherItem.setDescription(item.getDescription());
			researcherService.saveResearcher(researcher);
		}

		return SUCCESS;
	}
	
	/**
	 * Get researcher file system
	 * 
	 */
	public String getResearcherFolders() {
		if( researcher == null || !researcher.getUser().getId().equals(userId))
		{
			return "accessDenied";
		}
		
		if(parentFolderId != null && parentFolderId > 0)
		{
			ResearcherFolder folder = researcherFileSystemService.getResearcherFolder(parentFolderId, false);
			if( !folder.getResearcher().getId().equals(researcher.getId()))
			{
				return "accessDenied";
			}
			researcherFolderPath = researcherFileSystemService.getResearcherFolderPath(parentFolderId);
		}
		
		log.debug("Folder Path ::" + researcherFolderPath);
		
		log.debug("**** Parent Folder Id ::" + parentFolderId);
		
		Collection<ResearcherFolder> myResearcherFolders = researcherFileSystemService.getFoldersForResearcher(researcherId, parentFolderId);
		
		Collection<ResearcherFile> myResearcherFiles = researcherFileSystemService.getResearcherFiles(researcherId, parentFolderId);

		Collection<ResearcherInstitutionalItem> myResearcherInstitutionalItems = researcherFileSystemService.getResearcherInstitutionalItems(researcherId, parentFolderId);
		
		Collection<ResearcherLink> myResearcherLinks = researcherFileSystemService.getResearcherLinks(researcherId, parentFolderId);
		
		Collection<ResearcherPublication> myResearcherPublications = researcherFileSystemService.getResearcherPublications(researcherId, parentFolderId);
		
		researcherFileSystem = new LinkedList<FileSystem>();
		
    	researcherFileSystem.addAll(myResearcherFolders);
    	researcherFileSystem.addAll(myResearcherFiles);
    	researcherFileSystem.addAll(myResearcherPublications);
    	researcherFileSystem.addAll(myResearcherLinks);
    	researcherFileSystem.addAll(myResearcherInstitutionalItems);
		return SUCCESS;
	}
	
	/**
	 * Execute method
	 * 
	 */
	public String execute() { 
		
		if (researcherId != null) {
			researcher = researcherService.getResearcher(researcherId, false);
		}
		if( researcher == null || !researcher.getUser().getId().equals(userId))
		{
			return "accessDenied";
		}
		institutionalItem  = institutionalItemService.getInstitutionalItem(institutionalItemId, false);
		
		return SUCCESS;
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
	 * Get the item id
	 * 
	 * @return item id
	 */
	public Long getItemId() {
		return researcherId;
	}

	/**
	 * Set the item id
	 * 
	 * @param researcherId item id
	 */
	public void setItemId(Long researcherId) {
		this.researcherId = researcherId;
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

	public Long getResearcherId() {
		return researcherId;
	}

	public void setResearcherId(Long researcherId) {
		this.researcherId = researcherId;
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

	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}

	public UserPublishingFileSystemService getUserPublishingFileSystemService() {
		return userPublishingFileSystemService;
	}

	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public InstitutionalItem getInstitutionalItem() {
		return institutionalItem;
	}

	public void setInstitutionalItem(InstitutionalItem institutionalItem) {
		this.institutionalItem = institutionalItem;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public ResearcherFileSystemService getResearcherFileSystemService() {
		return researcherFileSystemService;
	}

	public void setResearcherFileSystemService(
			ResearcherFileSystemService researcherFileSystemService) {
		this.researcherFileSystemService = researcherFileSystemService;
	}
}
