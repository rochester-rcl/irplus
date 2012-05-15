/**  
   Copyright 2008-2012 University of Rochester

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

package edu.ur.ir.groupspace.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFile;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemLink;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemLinkDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolder;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolderDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageInstitutionalItem;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageInstitutionalItemDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPagePublication;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPagePublicationDAO;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.user.UserFileSystemService;

/**
 * Default implementation of the group workspace project page file system service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultGroupWorkspaceProjectPageFileSystemService implements GroupWorkspaceProjectPageFileSystemService{

	// eclipse generated id
	private static final long serialVersionUID = -6275337346010797410L;

	/** Data access for group workspace project page folder */
	private GroupWorkspaceProjectPageFolderDAO groupWorkspaceProjectPageFolderDAO;



	/** Data access for group workspace project page file */
	private GroupWorkspaceProjectPageFileDAO groupWorkspaceProjectPageFileDAO;
	
	/** Data access for group workspace project page publication*/
	private GroupWorkspaceProjectPagePublicationDAO groupWorkspaceProjectPagePublicationDAO;

	/** Data access for group workspace project page institutional item */
	private GroupWorkspaceProjectPageInstitutionalItemDAO groupWorkspaceProjectPageInstitutionalItemDAO;

	/** Data access for group workspace project page link*/
	private GroupWorkspaceProjectPageFileSystemLinkDAO groupWorkspaceProjectPageFileSystemLinkDAO;
	
	/** Service for dealing with the repository */
	private RepositoryService repositoryService;
	
	/** Service for dealing with items. */
	private ItemService itemService;
	
	/** service for dealing with user file system */
	private UserFileSystemService userFileSystemService;
	
	/** Service for dealing with group workspace project page information */
	private GroupWorkspaceProjectPageDAO groupWorkspaceProjectPageDAO;
	
	/** Service for dealing with group workspace file sytem */
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;
	
	
	/** service for dealing with researcher file system information */
	private ResearcherFileSystemService researcherFileSystemService;
	
	/**  Logger for add files to item action */
	private static final Logger log = Logger.getLogger(DefaultGroupWorkspaceProjectPageFileSystemService.class);

	/**
	 * Save the group workspace project page file system link.
	 * 
	 * @param link
	 */
	public void save(GroupWorkspaceProjectPageFileSystemLink link) {
		groupWorkspaceProjectPageFileSystemLinkDAO.makePersistent(link);
	}
	
	/**
	 * Create the root publication.
	 * 
	 * @param projectPage - project page 
	 * @param item - item to use in the publication
	 * @param versionNumber - version number of the item 
	 * 
	 * @return the created publication
	 */
	public GroupWorkspaceProjectPagePublication createRootPublication(GroupWorkspaceProjectPage projectPage, GenericItem item, int versionNumber) {
	
		GroupWorkspaceProjectPagePublication publication = projectPage.createRootPublication(item, versionNumber);
		groupWorkspaceProjectPagePublicationDAO.makePersistent(publication);
		return publication;
	}

	/**
	 * Create the root publication.
     *
	 * @param projectPage - Project page to add the institutional item to
	 * @param institutionalItem - institutional item
	 * @return the root group workspace project page institutional item
	 */
	public GroupWorkspaceProjectPageInstitutionalItem createRootInstitutionalItem(GroupWorkspaceProjectPage projectPage, InstitutionalItem institutionalItem) {
	
		GroupWorkspaceProjectPageInstitutionalItem instItem = projectPage.createRootInstitutionalItem(institutionalItem);
		groupWorkspaceProjectPageInstitutionalItemDAO.makePersistent(instItem);
		return instItem;
	}
	
	/**
	 * Add a file to the given folder.
	 * 
	 * @param parentFolder - parent folder to add the file to
	 * @param irFile - the ir file to add
	 * @param versionNumber - version number
	 * 
	 * @return - the created group workspace project page file
	 */
	public GroupWorkspaceProjectPageFile addFile(
			GroupWorkspaceProjectPageFolder parentFolder,
			IrFile irFile, int versionNumber) {
		
			GroupWorkspaceProjectPageFile file = parentFolder.addFile(irFile);
			file.setVersionNumber(versionNumber);
			save(parentFolder);
			return file;
	}
	
	/**
	 * Allows a researcher to create a new publication.
	 * 
	 * @param parentFolder - parent folder to add the publication to
	 * @param item - item to add
	 * @param versionNumber - version number of the item to add
	 * 
	 * @return the created publication.
	 */
	public GroupWorkspaceProjectPagePublication createPublication(GroupWorkspaceProjectPageFolder parentFolder, GenericItem item, int versionNumber) {
		
		GroupWorkspaceProjectPagePublication publication = parentFolder.createPublication(item, versionNumber);
		groupWorkspaceProjectPageFolderDAO.makePersistent(parentFolder);
		return publication;
	}

	/**
	 * Create the institutional item
	 * 
	 * @param parentFolder - parent folder to add the item to
	 * @param item - the institutional item
	 * 
	 * @return - the created institutional item
	 */
	public GroupWorkspaceProjectPageInstitutionalItem createInstitutionalItem(GroupWorkspaceProjectPageFolder parentFolder, InstitutionalItem item) {
		
		GroupWorkspaceProjectPageInstitutionalItem institutionalItem = parentFolder.create(item);
		groupWorkspaceProjectPageFolderDAO.makePersistent(parentFolder);
		return institutionalItem;
	}
	
	/**
	 * Create a new root folder for the project page.
	 * 
	 * @param projectPage - project page to create the root folder for
	 * @param folderName - name for the folder
	 * @return the created folder
	 * @throws DuplicateNameException - if the name already exists
	 */
	public GroupWorkspaceProjectPageFolder createNewRootFolder(GroupWorkspaceProjectPage projectPage, String folderName) throws DuplicateNameException {
		
		GroupWorkspaceProjectPageFolder folder = projectPage.createRootFolder(folderName);
		groupWorkspaceProjectPageFolderDAO.makePersistent(folder);
		return folder;
	}

	/**
	 * Create a new project page folder in the parent folder.
	 * 
	 * @param parentFolder - parent folder to create the project page for
	 * @param folderName - name to give the folder
	 * @return created folder
	 * @throws DuplicateNameException
	 */
	public GroupWorkspaceProjectPageFolder createFolder(GroupWorkspaceProjectPageFolder parentFolder,
			String folderName) throws DuplicateNameException{
		GroupWorkspaceProjectPageFolder folder = parentFolder.createChild(folderName);
		groupWorkspaceProjectPageFolderDAO.makePersistent(folder);
		return folder;
	}

	

	public List<GroupWorkspaceProjectPageFolder> getFoldersForProjectPage(Long projectPageId, Long parentFolderId) {
	    
		if( parentFolderId == null || parentFolderId == USE_GROUP_WORKSPACE_PROJECT_PAGE_AS_ROOT)
		{
			return  groupWorkspaceProjectPageFolderDAO.getRootFolders(projectPageId);
		}
		else
		{
		   return groupWorkspaceProjectPageFolderDAO.getSubFoldersForFolder(projectPageId, parentFolderId);
		}
	}

	
	/**
	 * Get the folders for the given project page.
	 * 
	 * @param projectPageId - id of the project page
	 * @param folderIds - list of ids for the folder
	 * 
	 * @return list of folders for the group workspace project page
	 */
	public List<GroupWorkspaceProjectPageFolder> getFolders(Long projectPageId, List<Long> folderIds) {
		return groupWorkspaceProjectPageFolderDAO.getFolders(projectPageId, folderIds);
	}

	
	/**
	 * Delete the group workspace project page file 
	 * 
	 * @param projectFile - project page file to delte
	 */
	public void delete(GroupWorkspaceProjectPageFile projectFile)
	{
		IrFile irFile = projectFile.getIrFile();
		
		// remove the file from the parent folder
		if( projectFile.getParentFolder() != null )
		{
			projectFile.getParentFolder().remove(projectFile);
		}
		
		groupWorkspaceProjectPageFileDAO.makeTransient(projectFile);

		// Check if irFile is used by PersonalFile or Item or researcher 
		if ((userFileSystemService.getPersonalFileCount(irFile) == 0) 
				&& (itemService.getItemFileCount(irFile) == 0)
				&& (getGroupWorkspaceProjectPageFileCount(irFile) == 0)
				&& (researcherFileSystemService.getResearcherFileCount(irFile) == 0)
				&& (groupWorkspaceFileSystemService.getGroupWorkspaceFileCount(irFile) == 0)) {
			repositoryService.deleteIrFile(irFile);
		}

	}

	/**
	 * Delete a group workspace project page publication.
	 * 
	 * @param publication - delete the publication
	 */
	public void delete(GroupWorkspaceProjectPagePublication publication)
	{
		GenericItem item = publication.getPublication();
		groupWorkspaceProjectPagePublicationDAO.makeTransient(publication);
		log.debug("itemService.getItemVersionCount(item)::"+itemService.getItemVersionCount(item));
		log.debug("getGroupWorkspacePublicationCount(item))::"+getGroupWorkspaceProjectPagePublicationCount(item));
		log.debug("getResearcherePublicationCount(item))::"+researcherFileSystemService.getResearcherPublicationCount(item));

		// Check if generic item is used in ItemVersion / researcher page  or is published
		if ( !item.isPublishedToSystem() 
			 && (itemService.getItemVersionCount(item) == 0)
			 && (researcherFileSystemService.getResearcherPublicationCount(item) == 0)
			 && (getGroupWorkspaceProjectPagePublicationCount(item) == 0)) {
			
			log.debug("Delete item");
			itemService.deleteItem(item);
		}
	}

	/**
	 * Delete a group workspace project page institutional item.
	 * 
	 * @param item
	 */
	public void delete(GroupWorkspaceProjectPageInstitutionalItem item)
	{
		groupWorkspaceProjectPageInstitutionalItemDAO.makeTransient(item);
	}
	
	/**
	 * Delete a group workspace project page file system link.
	 * 
	 * @param link - link to delete
	 */
	public void delete(GroupWorkspaceProjectPageFileSystemLink link)
	{
		groupWorkspaceProjectPageFileSystemLinkDAO.makeTransient(link);
	}


	/**
	 * Delete the folder.
	 * 
	 * @param folder - deletes the folder and all contents within it
	 */
	public void delete(GroupWorkspaceProjectPageFolder folder) {
		
		List<GroupWorkspaceProjectPageFile> files = groupWorkspaceProjectPageFolderDAO.getAllFiles(folder);
		log.debug("files="+files);
		List<GroupWorkspaceProjectPagePublication> publications = groupWorkspaceProjectPageFolderDAO.getAllPublications(folder);
		log.debug("publications="+publications);
		List<GroupWorkspaceProjectPageFileSystemLink> links = groupWorkspaceProjectPageFolderDAO.getAllLinks(folder);
		log.debug("researcherLinks="+links);
		
		// deleting files and researcher publications 
		// require special checking
		for( GroupWorkspaceProjectPageFile aFile : files)
		{
		    delete(aFile);
		}

		for( GroupWorkspaceProjectPagePublication aPublication : publications)
		{
		    delete(aPublication);
		}
		
		log.debug("Deleted all contents");
		if( folder.isRoot() )
		{
			GroupWorkspaceProjectPage projectPage = folder.getGroupWorkspaceProjectPage();
			projectPage.remove(folder);
		}
		else
		{
			GroupWorkspaceProjectPageFolder parent = folder.getParent();
			parent.removeChild(folder);
		}
		groupWorkspaceProjectPageFolderDAO.makeTransient(folder);

	}

	/**
	 * Get the group workspace project page folder
	 * 
	 * @param name - name of the folder
	 * @param parentId - id of the parent folder
	 * 
	 * @return - the folder if found - otherwise null
	 */
	public GroupWorkspaceProjectPageFolder getFolder(String name, Long parentId) {
		return groupWorkspaceProjectPageFolderDAO.getFolder(name, parentId);
	}

	/**
	 * Get the folder
	 * 
	 * @param id - id of the folder
	 * @param lock - if true will upgrade the lock mode
	 * 
	 * @return the group workspace project page folder if found otherwise null
	 */
	public GroupWorkspaceProjectPageFolder getFolder(Long id, boolean lock) {
		return groupWorkspaceProjectPageFolderDAO.getById(id, lock);
	}

	/**
	 * Get the root folder.
	 * 
	 * @param name - name of the folder
	 * @param projectPageId - project page id for the folder
	 * 
	 * @return - the root folder if found otherwise null
	 */
	public GroupWorkspaceProjectPageFolder getRootFolder(String name, Long projectPageId) {
		return groupWorkspaceProjectPageFolderDAO.getRootFolder(name, projectPageId);
	}

    /**
     * Get all folders for the project page
     * 
     * @param projectPageId - id of the project page
     * @return - list of all folders for the project page
     */
    public List<GroupWorkspaceProjectPageFolder> getAllFolders(Long projectPageId) {
		return groupWorkspaceProjectPageFolderDAO.getAllFolders(projectPageId);
	}


	/**
	 * Save the group workspace project page
	 * 
	 * @param entity
	 */
	public void save(GroupWorkspaceProjectPageFolder entity) {
		groupWorkspaceProjectPageFolderDAO.makePersistent(entity);
	}

	/**
	 * Save the group workspace project page file.
	 * 
	 * @param entity
	 */
	public void save(GroupWorkspaceProjectPageFile entity) {
		groupWorkspaceProjectPageFileDAO.makePersistent(entity);
	}
	
	/**
	 * Get the project page folders within the specified parent folder.
	 * 
	 * @param projectPageId - project page id
	 * @param parentFolderId - parent folder id
	 * 
	 * @return - list of folders found within the parent folder 
	 */
	public List<GroupWorkspaceProjectPageFolder> getFolders( 
			Long projectPageId, 
			Long parentFolderId) 
	{
		if( parentFolderId == null || parentFolderId == USE_GROUP_WORKSPACE_PROJECT_PAGE_AS_ROOT)
		{
			return groupWorkspaceProjectPageFolderDAO.getRootFolders(projectPageId);
		}
		else
		{
			return groupWorkspaceProjectPageFolderDAO.getSubFoldersForFolder(projectPageId, parentFolderId);
		}
	}
	
	/**
	 * Get the list of folders in path order for the folder with the specified id.
	 * 
	 * @param projectPageFolderId - id of the group workspace project page folder
	 * @return - list of folders in path order
	 */
	public List<GroupWorkspaceProjectPageFolder> getFolderPath(Long projectPageFolderId) {
		if( projectPageFolderId == null )
		{
			return new LinkedList<GroupWorkspaceProjectPageFolder>();
		}
		
		GroupWorkspaceProjectPageFolder f = this.getFolder(projectPageFolderId, false);
		
		if( f != null )
		{	
		    return groupWorkspaceProjectPageFolderDAO.getPath(f);
		}
		else
		{
			return new LinkedList<GroupWorkspaceProjectPageFolder>();
		}
	}
	
	/**
	 * Get group workspace project page files within the specified parent folder
	 * 
	 * @param groupWorkspaceProjectPageId - project page id
	 * @param parentFolderId - parent folder id
	 * 
	 * @return - list of group workspace project page files
	 */
	public List<GroupWorkspaceProjectPageFile> getFiles(Long groupWorkspaceProjectPageId, Long parentFolderId) {
	    
		if( parentFolderId == null || parentFolderId == USE_GROUP_WORKSPACE_PROJECT_PAGE_AS_ROOT)
		{
			return  groupWorkspaceProjectPageFileDAO.getRootFiles(groupWorkspaceProjectPageId);
		}
		else
		{
		   return groupWorkspaceProjectPageFileDAO.getFiles(groupWorkspaceProjectPageId, parentFolderId);
		}
	}

	/**
	 * Get group workspace project page publications within the specified parent folder
	 * 
	 * @param projectPageId - id of the project page 
	 * @param parentFolderId - parent folder id - if id is 0 then root project page is used
	 * @return
	 */
	public List<GroupWorkspaceProjectPagePublication> getPublications(Long projectPageId, Long parentFolderId) {
		
		log.debug("In service parentFolderId :"+parentFolderId);
	    
		if( parentFolderId == null || parentFolderId == USE_GROUP_WORKSPACE_PROJECT_PAGE_AS_ROOT)
		{
			log.debug("In service parentFolderId NUll :"+parentFolderId);
			return  groupWorkspaceProjectPagePublicationDAO.getRootPublications(projectPageId);
		}
		else
		{
			log.debug("In service parentFolderId not null :"+parentFolderId);
		   return groupWorkspaceProjectPagePublicationDAO.getPublications(projectPageId, parentFolderId);
		}
	}
	
	/**
	 * Get group workspace project page Institutional Item within the specified parent folder
	 * 
	 * @param projectPageId - project page id 
	 * @param parentFolderId - parent folder id - if id is 0 then root project page is used
	 * 
	 * @return - the group workspace project page institutional items
	 */
	public List<GroupWorkspaceProjectPageInstitutionalItem> getInstitutionalItems(Long projectPageId, Long parentFolderId) {
		
		log.debug("In service parentFolderId :"+parentFolderId);
	    
		if( parentFolderId == null || parentFolderId == USE_GROUP_WORKSPACE_PROJECT_PAGE_AS_ROOT)
		{
			log.debug("In service parentFolderId NUll :"+parentFolderId);
			return  groupWorkspaceProjectPageInstitutionalItemDAO.getRootItems(projectPageId);
		}
		else
		{
			log.debug("In service parentFolderId not null :"+parentFolderId);
		    return groupWorkspaceProjectPageInstitutionalItemDAO.getItems(projectPageId, parentFolderId);
		}
	}	

	/**
	 * Get links for a group workspace project page in the specified folder
	 * 
	 * @param projectPageId - id of the project page
	 * @param parentFolderId - id of the parent folder - if id is 0 then root project page is used
	 * 
	 * @return - list of file system links
	 */
	public List<GroupWorkspaceProjectPageFileSystemLink> getLinks(Long projectPageId, Long parentFolderId) {
		
		log.debug("In service parentFolderId :"+parentFolderId);
	    
		if( parentFolderId == null || parentFolderId == USE_GROUP_WORKSPACE_PROJECT_PAGE_AS_ROOT)
		{
			log.debug("In service parentFolderId NUll :"+parentFolderId);
			return  groupWorkspaceProjectPageFileSystemLinkDAO.getRootLinks(projectPageId);
		}
		else
		{
			log.debug("In service parentFolderId not null :"+parentFolderId);
		    return groupWorkspaceProjectPageFileSystemLinkDAO.getLinks(projectPageId, parentFolderId);
		}
	}
	
	/**
	 * Get the project page file.
	 * 
	 * @param id - id of the project page file
	 * @param lock - upgrade the lock mode if true
	 * 
	 * @return - the file if found otherwise null
	 */
	public GroupWorkspaceProjectPageFile getFile(Long id, boolean lock)
	{
		return groupWorkspaceProjectPageFileDAO.getById(id, lock);
	}

	/**
	 * Get the group workspace project page file system link with the specified id.
	 * 
	 * @param id - id of the link
	 * @param lock - if true upgrade the lock mode
	 * 
	 * @return - the link if found otherwise null
	 */
	public GroupWorkspaceProjectPageFileSystemLink getLink(Long id, boolean lock)
	{
		return groupWorkspaceProjectPageFileSystemLinkDAO.getById(id, lock);
	}
	
	/**
	 * Get the group workspace project page publication with the specified id.
	 * 
	 * @param id - id of the publication
	 * @param lock - if true upgrade the lock mode.
	 * 
	 * @return - the publication if found otherwise null
	 */
	public GroupWorkspaceProjectPagePublication getPublication(Long id, boolean lock)
	{
		return groupWorkspaceProjectPagePublicationDAO.getById(id, lock);
	}

	/**
	 * Get the group workspace project page Institutional Item with the specified id.
	 * 
	 * @param id - of the group workspace project page institutional item
	 * @param lock - if true upgrade the lock mode
	 * 
	 * @return - the institutional item 
	 */
	public GroupWorkspaceProjectPageInstitutionalItem getInstitutionalItem(Long id, boolean lock)
	{
		return groupWorkspaceProjectPageInstitutionalItemDAO.getById(id, lock);
	}
	
	/**
	 * Get the count of group workspace project page files using this Irfile
	 * 
	 * @param irFile - ir file to check to see if the file is used
	 * @return - the count of project page files with the specified ir file
	 */
	public Long getGroupWorkspaceProjectPageFileCount(IrFile irFile) {
		return groupWorkspaceProjectPageFileDAO.getCountWithSpecifiedIrFile(irFile.getId());
	}
	
	/**
	 * Get the count of group workspace project page publication using this generic item
	 * 
	 * @param item - item to check for
	 * @return - number found
	 */
	public Long getGroupWorkspaceProjectPagePublicationCount(GenericItem item) {
		return groupWorkspaceProjectPagePublicationDAO.getCount(item.getId());
	}

	/**
	 * Get the count of group workspace project page Institutional Item using this institutional item
	 * 
	 * @param item - institutional item to check for
	 * @return - count found
	 */
	public Long getInstitutionalItemCount(InstitutionalItem item) {
		return groupWorkspaceProjectPageInstitutionalItemDAO.getCount(item.getId());
	}
	
	/**
	 * Get list of group workspace project page institutional item containing this item and delete them
	 * 
	 * @param institutionalItem - institutional item to remove from the group workspace
	 */
	public void delete(InstitutionalItem institutionalItem) {
		
		List<GroupWorkspaceProjectPageInstitutionalItem> items = groupWorkspaceProjectPageInstitutionalItemDAO.getItems(institutionalItem.getId());
		
		for(GroupWorkspaceProjectPageInstitutionalItem item : items) {
			groupWorkspaceProjectPageInstitutionalItemDAO.makeTransient(item);
		}
	}


	/**
	 * Get the list of files for the specified group workspace project page.
	 * 
	 * @param projectPageId - id of the project page
	 * @param fileIds - list of files
	 * 
	 * @return - list of group workspace project page files
	 */
	public List<GroupWorkspaceProjectPageFile> getFiles(Long projectPageId, List<Long> fileIds) {
		return groupWorkspaceProjectPageFileDAO.getFiles(projectPageId, fileIds);
	}

	/**
	 * Get the list of group workspace project page institutional items.
	 * 
	 * @param projectPageId - id of the project page
	 * @param institutionalItemIds - ids for the institutional items
	 * @return
	 */
	public List<GroupWorkspaceProjectPageInstitutionalItem> getInstitutionalItems(
			Long projectPageId, List<Long> institutionalItemIds) {
		return groupWorkspaceProjectPageInstitutionalItemDAO.getItems(projectPageId, institutionalItemIds);
	}

	/**
	 * Get the specified links for the specified group workspace project page.
	 * 
	 * @param projectPageId - id of the project page
	 * @param linkIds - list of ids for the links
	 * 
	 * @return - list of links otherwise null
	 */
	public List<GroupWorkspaceProjectPageFileSystemLink> getLinks(Long projectPageId,
			List<Long> linkIds) {
		return groupWorkspaceProjectPageFileSystemLinkDAO.getLinks(projectPageId, linkIds);
	}

	/**
	 * Get the specified publications for the specified group workspace project page id.
	 * 
	 * @param projectPageId - id of the project page
	 * @param publicationIds - list of publication ids
	 * 
	 * @return - list of group workspace project page ids
	 */
	public List<GroupWorkspaceProjectPagePublication> getPublications(
			Long projectPageId, List<Long> publicationIds) {
		return groupWorkspaceProjectPagePublicationDAO.getPublications(projectPageId, publicationIds);
	}
	
	/**
	 * Allow a user to move files and folders into a given folder
	 * 
	 *
	 * @param destination - destination to move the data to
	 * @param foldersToMove - list of folders to move
	 * @param filesToMove - list of files to move
	 * @param linksToMove - list of links to move
	 * @param itemsToMove - list of items to move
	 * @param publicationsToMove - list of publications to move
	 * 
	 * @return - list of items that could not be moved.
	 */
	public List<FileSystem> moveFileSystemInformation(GroupWorkspaceProjectPageFolder destination,
			List<GroupWorkspaceProjectPageFolder> foldersToMove, 
			List<GroupWorkspaceProjectPageFile> filesToMove, 
			List<GroupWorkspaceProjectPageFileSystemLink> linksToMove,
			List<GroupWorkspaceProjectPageInstitutionalItem> itemsToMove,
			List<GroupWorkspaceProjectPagePublication> publicationsToMove) 
	{
		
		LinkedList<FileSystem> notMoved = new LinkedList<FileSystem>();
		
		// move folders first
		if( foldersToMove != null )
		{
		    for( GroupWorkspaceProjectPageFolder folder : foldersToMove)
		    {
		    	log.debug("Adding folder " + folder + " to destination " + destination);
			    try {
			    	 destination.addChild(folder);
				} catch (DuplicateNameException e) {
					notMoved.add(folder);
				}
		    }
		}
	
		
		
		if( filesToMove != null  && notMoved.size() == 0)
		{
		    for( GroupWorkspaceProjectPageFile file : filesToMove)
		    {
		    	log.debug("Adding file " + file + " to destination " + destination);
		    	if( !destination.getFiles().contains(file))
		    	{
		    		destination.add(file);
		    	}
		    	else
		    	{
		    		notMoved.add(file);
		    	}
		    }
		}
		
		if( linksToMove != null  && notMoved.size() == 0)
		{
		    for( GroupWorkspaceProjectPageFileSystemLink link : linksToMove)
		    {
		    	log.debug("Adding link " + link + " to destination " + destination);
		    	if( destination.getLink(link.getName()) == null)
		    	{
			        destination.addLink(link);
		    	}
		    	else
		    	{
		    		notMoved.add(link);
		    	}
		    }
		}
		
		if( itemsToMove != null  && notMoved.size() == 0)
		{
		    for( GroupWorkspaceProjectPageInstitutionalItem institutionalItem : itemsToMove)
		    {
		    	log.debug("Adding item " +  institutionalItem + " to destination " + destination);
		    	if( !destination.getInstitutionalItems().contains(institutionalItem))
		    	{
		    		 destination.addInstitutionalItem(institutionalItem);
		    	}
		    	else
		    	{
		    		notMoved.add(institutionalItem);
		    	}
			   
		    }
		}
		
		if( publicationsToMove != null  && notMoved.size() == 0)
		{
		    for( GroupWorkspaceProjectPagePublication publication : publicationsToMove)
		    {
		    	log.debug("Adding publication " +  publication + " to destination " + destination);
		    	
		    	if( !destination.getPublications().contains(publication))
		    	{
		    		   destination.addPublication(publication);
		    	}
		    	else
		    	{
		    		notMoved.add(publication);
		    	}
			 
		    }
		}
		
		if( notMoved.size() == 0)
		{
			groupWorkspaceProjectPageFolderDAO.makePersistent(destination);
		}
		
		return notMoved;
	}
	
	/**
	 * Move the folders into the root location of the researcher
	 * 
	 * @throws DuplicateNameException 
	 * 
	 */
	public List<FileSystem> moveResearcherFileSystemInformation(GroupWorkspaceProjectPage projectPage,
			List<GroupWorkspaceProjectPageFolder> foldersToMove, 
			List<GroupWorkspaceProjectPageFile> filesToMove, 
			List<GroupWorkspaceProjectPageFileSystemLink> linksToMove,
			List<GroupWorkspaceProjectPageInstitutionalItem> itemsToMove,
			List<GroupWorkspaceProjectPagePublication> publicationsToMove)  {

		LinkedList<FileSystem> notMoved = new LinkedList<FileSystem>();
		
		// move folders first
		if( foldersToMove != null )
		{
		    for( GroupWorkspaceProjectPageFolder folder : foldersToMove)
		    {
		    	log.debug("Adding folder " + folder + " to projectPage " + projectPage);
			    try {
			    	 projectPage.addRootFolder(folder);
				} catch (DuplicateNameException e) {
					notMoved.add(folder);
				}
		    }
		}
	
		
		if( filesToMove != null  && notMoved.size() == 0)
		{
		    for( GroupWorkspaceProjectPageFile file : filesToMove)
		    {
		    	log.debug("Adding file " + file + " to researcher " + projectPage);
		    	if( !projectPage.getRootFiles().contains(file))
		    	{
			        projectPage.addRootFile(file);
		    	}
		    	else
		    	{
		    		notMoved.add(file);
		    	}
		    }
		}
		
		if( linksToMove != null  && notMoved.size() == 0)
		{
		    for( GroupWorkspaceProjectPageFileSystemLink link : linksToMove)
		    {
		    	log.debug("Adding link " + link + " to project page " + projectPage);
			    projectPage.addRootLink(link);
		    }
		}
		
		if( itemsToMove != null  && notMoved.size() == 0)
		{
		    for( GroupWorkspaceProjectPageInstitutionalItem institutionalItem : itemsToMove)
		    {
		    	log.debug("Adding item " +  institutionalItem + " to project page " + projectPage);
		    	if( !projectPage.getRootInstitutionalItems().contains(institutionalItem))
		    	{
			        projectPage.addRootInstitutionalItem(institutionalItem);
		    	}
		    	else
		    	{
		    		notMoved.add(institutionalItem);
		    	}
		    }
		}
		
		if( publicationsToMove != null  && notMoved.size() == 0)
		{
		    for( GroupWorkspaceProjectPagePublication publication : publicationsToMove)
		    {
		    	log.debug("Adding publication " +  publication + " to project page " + projectPage);
		    	if( !projectPage.getRootPublications().contains(publication))
		    	{
		    	    projectPage.addRootPublication(publication);
		    	}
		    	else
		    	{
		    		notMoved.add(publication);
		    	}
		    }
		}
	
		if( notMoved.size() == 0)
		{
			groupWorkspaceProjectPageDAO.makePersistent(projectPage);
		}
		
		return notMoved;
	}

	/**
	 * Set the group workspace project page folder.
	 * 
	 * @param groupWorkspaceProjectPageFolderDAO
	 */
	public void setGroupWorkspaceProjectPageFolderDAO(GroupWorkspaceProjectPageFolderDAO groupWorkspaceProjectPageFolderDAO) {
		this.groupWorkspaceProjectPageFolderDAO = groupWorkspaceProjectPageFolderDAO;
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
	 * Set the item service.
	 * 
	 * @param itemService
	 */
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	/**
	 * Set the user file system service.
	 * 
	 * @param userFileSystemService
	 */
	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}


	/**
	 * Save the institutional item
	 * 
	 * @param entity
	 */
	public void save( GroupWorkspaceProjectPageInstitutionalItem entity) {
		groupWorkspaceProjectPageInstitutionalItemDAO.makePersistent(entity);
	}


	/**
	 * Save the publicaiton.
	 * 
	 * @param entity
	 */
	public void save(GroupWorkspaceProjectPagePublication entity) {
		groupWorkspaceProjectPagePublicationDAO.makePersistent(entity);
	}
	
	public void setGroupWorkspaceProjectPageFileDAO(
			GroupWorkspaceProjectPageFileDAO groupWorkspaceProjectPageFileDAO) {
		this.groupWorkspaceProjectPageFileDAO = groupWorkspaceProjectPageFileDAO;
	}



	public void setGroupWorkspaceProjectPagePublicationDAO(
			GroupWorkspaceProjectPagePublicationDAO groupWorkspaceProjectPagePublicationDAO) {
		this.groupWorkspaceProjectPagePublicationDAO = groupWorkspaceProjectPagePublicationDAO;
	}



	public void setGroupWorkspaceProjectPageInstitutionalItemDAO(
			GroupWorkspaceProjectPageInstitutionalItemDAO groupWorkspaceProjectPageInstitutionalItemDAO) {
		this.groupWorkspaceProjectPageInstitutionalItemDAO = groupWorkspaceProjectPageInstitutionalItemDAO;
	}



	public void setGroupWorkspaceProjectPageFileSystemLinkDAO(
			GroupWorkspaceProjectPageFileSystemLinkDAO groupWorkspaceProjectPageFileSystemLinkDAO) {
		this.groupWorkspaceProjectPageFileSystemLinkDAO = groupWorkspaceProjectPageFileSystemLinkDAO;
	}



	public void setGroupWorkspaceProjectPageDAO(
			GroupWorkspaceProjectPageDAO groupWorkspaceProjectPageDAO) {
		this.groupWorkspaceProjectPageDAO = groupWorkspaceProjectPageDAO;
	}



	public void setGroupWorkspaceFileSystemService(
			GroupWorkspaceFileSystemService groupWorkspaceFileSystemService) {
		this.groupWorkspaceFileSystemService = groupWorkspaceFileSystemService;
	}



	public void setResearcherFileSystemService(
			ResearcherFileSystemService researcherFileSystemService) {
		this.researcherFileSystemService = researcherFileSystemService;
	}

	/**
     * Get the count of files project page files with the specified file
     * 
     * @param irFile - ir file to check
     * @return - count of files
     */
	public Long getFileCount(IrFile irFile) {
		return groupWorkspaceProjectPageFileDAO.getCountWithSpecifiedIrFile(irFile.getId());
	}

}
