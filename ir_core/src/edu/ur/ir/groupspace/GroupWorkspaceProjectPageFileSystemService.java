/**  
   Copyright 2008 - 2012 University of Rochester

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

package edu.ur.ir.groupspace;

import java.io.Serializable;
import java.util.List;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.item.GenericItem;


/**
 * Service to deal with group workspace project page file system information.
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupWorkspaceProjectPageFileSystemService extends Serializable{
	
	/** indicates that the project page should be used as root */
	public static final long USE_GROUP_WORKSPACE_PROJECT_PAGE_AS_ROOT = 0L;
	
	/**
	 * Save the group workspace project page file system link.
	 * 
	 * @param link
	 */
	public void save(GroupWorkspaceProjectPageFileSystemLink link);
	
	/**
	 * Create the root publication.
	 * 
	 * @param projectPage - project page 
	 * @param item - item to use in the publication
	 * @param versionNumber - version number of the item 
	 * 
	 * @return the created publication
	 */
	public GroupWorkspaceProjectPagePublication createRootPublication(GroupWorkspaceProjectPage projectPage, 
			GenericItem item, int versionNumber);

	/**
	 * Create the root publication.
     *
	 * @param projectPage - Project page to add the institutional item to
	 * @param institutionalItem - institutional item
	 * @return the root group workspace project page institutional item
	 */
	public GroupWorkspaceProjectPageInstitutionalItem createRootInstitutionalItem(GroupWorkspaceProjectPage projectPage, 
			InstitutionalItem institutionalItem);
	
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
			IrFile irFile, int versionNumber) ;
	
	/**
	 * Allows a researcher to create a new publication.
	 * 
	 * @param parentFolder - parent folder to add the publication to
	 * @param item - item to add
	 * @param versionNumber - version number of the item to add
	 * 
	 * @return the created publication.
	 */
	public GroupWorkspaceProjectPagePublication createPublication(GroupWorkspaceProjectPageFolder parentFolder, 
			GenericItem item, int versionNumber);

	/**
	 * Create the institutional item
	 * 
	 * @param parentFolder - parent folder to add the item to
	 * @param item - the institutional item
	 * 
	 * @return - the created institutional item
	 */
	public GroupWorkspaceProjectPageInstitutionalItem createInstitutionalItem(GroupWorkspaceProjectPageFolder parentFolder, 
			InstitutionalItem item) ;
	
	/**
	 * Create a new root folder for the project page.
	 * 
	 * @param projectPage - project page to create the root folder for
	 * @param folderName - name for the folder
	 * @return the created folder
	 * @throws DuplicateNameException - if the name already exists
	 */
	public GroupWorkspaceProjectPageFolder createNewRootFolder(GroupWorkspaceProjectPage projectPage, 
			String folderName) throws DuplicateNameException;

	/**
	 * Create a new project page folder in the parent folder.
	 * 
	 * @param parentFolder - parent folder to create the project page for
	 * @param folderName - name to give the folder
	 * @return created folder
	 * @throws DuplicateNameException
	 */
	public GroupWorkspaceProjectPageFolder createFolder(GroupWorkspaceProjectPageFolder parentFolder,
			String folderName) throws DuplicateNameException;

	

	/**
	 * Get all folders for the project page folder.
	 * 
	 * @param projectPageId - id of the project page
	 * @param parentFolderId - id of the parent folder.
	 * 
	 * @return - list of group worksapce project page folders
	 */
	public List<GroupWorkspaceProjectPageFolder> getFoldersForProjectPage(Long projectPageId, Long parentFolderId);

	
	/**
	 * Get the folders for the given project page.
	 * 
	 * @param projectPageId - id of the project page
	 * @param folderIds - list of ids for the folder
	 * 
	 * @return list of folders for the group workspace project page
	 */
	public List<GroupWorkspaceProjectPageFolder> getFolders(Long projectPageId, List<Long> folderIds);

	
	/**
	 * Delete the group workspace project page file 
	 * 
	 * @param projectFile - project page file to delte
	 */
	public void delete(GroupWorkspaceProjectPageFile projectFile);
	
	/**
	 * Delete a group workspace project page publication.
	 * 
	 * @param publication - delete the publication
	 */
	public void delete(GroupWorkspaceProjectPagePublication publication);

	/**
	 * Delete a group workspace project page institutional item.
	 * 
	 * @param item
	 */
	public void delete(GroupWorkspaceProjectPageInstitutionalItem item);
	
	/**
	 * Delete a group workspace project page file system link.
	 * 
	 * @param link - link to delete
	 */
	public void delete(GroupWorkspaceProjectPageFileSystemLink link);


	/**
	 * Delete the folder.
	 * 
	 * @param folder - deletes the folder and all contents within it
	 */
	public void delete(GroupWorkspaceProjectPageFolder folder);

	/**
	 * Get the group workspace project page folder
	 * 
	 * @param name - name of the folder
	 * @param parentId - id of the parent folder
	 * 
	 * @return - the folder if found - otherwise null
	 */
	public GroupWorkspaceProjectPageFolder getFolder(String name, Long parentId);

	/**
	 * Get the folder
	 * 
	 * @param id - id of the folder
	 * @param lock - if true will upgrade the lock mode
	 * 
	 * @return the group workspace project page folder if found otherwise null
	 */
	public GroupWorkspaceProjectPageFolder getFolder(Long id, boolean lock);
	
	/**
	 * Get the root folder.
	 * 
	 * @param name - name of the folder
	 * @param projectPageId - project page id for the folder
	 * 
	 * @return - the root folder if found otherwise null
	 */
	public GroupWorkspaceProjectPageFolder getRootFolder(String name, Long projectPageId);

    /**
     * Get all folders for the project page
     * 
     * @param projectPageId - id of the project page
     * @return - list of all folders for the project page
     */
    public List<GroupWorkspaceProjectPageFolder> getAllFolders(Long projectPageId);


	/**
	 * Save the group workspace project page
	 * 
	 * @param entity
	 */
	public void save(GroupWorkspaceProjectPageFolder entity);

	/**
	 * Save the group workspace project page file.
	 * 
	 * @param entity
	 */
	public void save(GroupWorkspaceProjectPageFile entity);
	
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
			Long parentFolderId);
	
	/**
	 * Get the list of folders in path order for the folder with the specified id.
	 * 
	 * @param projectPageFolderId - id of the group workspace project page folder
	 * @return - list of folders in path order
	 */
	public List<GroupWorkspaceProjectPageFolder> getFolderPath(Long projectPageFolderId);
	
	/**
	 * Get group workspace project page files within the specified parent folder
	 * 
	 * @param groupWorkspaceProjectPageId - project page id
	 * @param parentFolderId - parent folder id
	 * 
	 * @return - list of group workspace project page files
	 */
	public List<GroupWorkspaceProjectPageFile> getFiles(Long groupWorkspaceProjectPageId, Long parentFolderId);

	/**
	 * Get group workspace project page publications within the specified parent folder
	 * 
	 * @param projectPageId - id of the project page 
	 * @param parentFolderId - parent folder id - if id is 0 then root project page is used
	 * @return
	 */
	public List<GroupWorkspaceProjectPagePublication> getPublications(Long projectPageId, Long parentFolderId);
	
	/**
	 * Get group workspace project page Institutional Item within the specified parent folder
	 * 
	 * @param projectPageId - project page id 
	 * @param parentFolderId - parent folder id - if id is 0 then root project page is used
	 * 
	 * @return - the group workspace project page institutional items
	 */
	public List<GroupWorkspaceProjectPageInstitutionalItem> getInstitutionalItems(Long projectPageId, Long parentFolderId);

	/**
	 * Get links for a group workspace project page in the specified folder
	 * 
	 * @param projectPageId - id of the project page
	 * @param parentFolderId - id of the parent folder - if id is 0 then root project page is used
	 * 
	 * @return - list of file system links
	 */
	public List<GroupWorkspaceProjectPageFileSystemLink> getLinks(Long projectPageId, Long parentFolderId);
	
	/**
	 * Get the project page file.
	 * 
	 * @param id - id of the project page file
	 * @param lock - upgrade the lock mode if true
	 * 
	 * @return - the file if found otherwise null
	 */
	public GroupWorkspaceProjectPageFile getFile(Long id, boolean lock);

	/**
	 * Get the group workspace project page file system link with the specified id.
	 * 
	 * @param id - id of the link
	 * @param lock - if true upgrade the lock mode
	 * 
	 * @return - the link if found otherwise null
	 */
	public GroupWorkspaceProjectPageFileSystemLink getLink(Long id, boolean lock);
	
	/**
	 * Get the group workspace project page publication with the specified id.
	 * 
	 * @param id - id of the publication
	 * @param lock - if true upgrade the lock mode.
	 * 
	 * @return - the publication if found otherwise null
	 */
	public GroupWorkspaceProjectPagePublication getPublication(Long id, boolean lock);

	/**
	 * Get the group workspace project page Institutional Item with the specified id.
	 * 
	 * @param id - of the group workspace project page institutional item
	 * @param lock - if true upgrade the lock mode
	 * 
	 * @return - the institutional item 
	 */
	public GroupWorkspaceProjectPageInstitutionalItem getInstitutionalItem(Long id, boolean lock);
	
	/**
	 * Get the count of group workspace project page files using this Irfile
	 * 
	 * @param irFile - ir file to check to see if the file is used
	 * @return - the count of project page files with the specified ir file
	 */
	public Long getGroupWorkspaceProjectPageFileCount(IrFile irFile);
	
	/**
	 * Get the count of group workspace project page publication using this generic item
	 * 
	 * @param item - item to check for
	 * @return - number found
	 */
	public Long getGroupWorkspaceProjectPagePublicationCount(GenericItem item);
	
	/**
	 * Get the count of group workspace project page Institutional Item using this institutional item
	 * 
	 * @param item - institutional item to check for
	 * @return - count found
	 */
	public Long getInstitutionalItemCount(InstitutionalItem item);
	
	/**
	 * Get list of group workspace project page institutional item containing this item and delete them
	 * 
	 * @param institutionalItem - institutional item to remove from the group workspace
	 */
	public void delete(InstitutionalItem institutionalItem);

	/**
	 * Get the list of files for the specified group workspace project page.
	 * 
	 * @param projectPageId - id of the project page
	 * @param fileIds - list of files
	 * 
	 * @return - list of group workspace project page files
	 */
	public List<GroupWorkspaceProjectPageFile> getFiles(Long projectPageId, List<Long> fileIds);

	/**
	 * Get the list of group workspace project page institutional items.
	 * 
	 * @param projectPageId - id of the project page
	 * @param institutionalItemIds - ids for the institutional items
	 * @return
	 */
	public List<GroupWorkspaceProjectPageInstitutionalItem> getInstitutionalItems(
			Long projectPageId, List<Long> institutionalItemIds);

	/**
	 * Get the specified links for the specified group workspace project page.
	 * 
	 * @param projectPageId - id of the project page
	 * @param linkIds - list of ids for the links
	 * 
	 * @return - list of links otherwise null
	 */
	public List<GroupWorkspaceProjectPageFileSystemLink> getLinks(Long projectPageId,
			List<Long> linkIds);

	/**
	 * Get the specified publications for the specified group workspace project page id.
	 * 
	 * @param projectPageId - id of the project page
	 * @param publicationIds - list of publication ids
	 * 
	 * @return - list of group workspace project page ids
	 */
	public List<GroupWorkspaceProjectPagePublication> getPublications(
			Long projectPageId, List<Long> publicationIds);
	
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
			List<GroupWorkspaceProjectPagePublication> publicationsToMove);
	
	/**
	 * Move the folders into the root location of the researcher
	 * 
	 * @throws DuplicateNameException 
	 * 
	 */
	public List<FileSystem> moveFileSystemInformation(GroupWorkspaceProjectPage projectPage,
			List<GroupWorkspaceProjectPageFolder> foldersToMove, 
			List<GroupWorkspaceProjectPageFile> filesToMove, 
			List<GroupWorkspaceProjectPageFileSystemLink> linksToMove,
			List<GroupWorkspaceProjectPageInstitutionalItem> itemsToMove,
			List<GroupWorkspaceProjectPagePublication> publicationsToMove);

    /**
     * Get the count of files project page files with the specified file
     * 
     * @param irFile - ir file to check
     * @return - count of files
     */
    public Long getFileCount(IrFile irFile);
}
