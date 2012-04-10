package edu.ur.ir.groupspace;

import java.io.Serializable;
import java.util.List;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.item.GenericItem;


public interface GroupWorkspaceProjectPageFileSystemService extends Serializable{
	
	/** indicates that the project page should be used as root */
	public static final long USE_PROJECT_PAGE_AS_ROOT = 0L;
	
	/**
	 * Allows a user to create a new folder at root level.
	 * 
	 * @param groupWorkspaceProjectPage for creating folder
	 * @param folderName name of the folder
	 * 
	 * @return new folder
	 * 
	 * @throws DuplicateNameException 
	 */
	public GroupWorkspaceProjectPageFolder createNewRootFolder(GroupWorkspaceProjectPage groupWorkspaceProjectPage, String folderName) throws DuplicateNameException ;
	
	/**
	 * Create a group workspace project page folder setting the parent as the parent folder.
	 * 
	 * @param parentFolder Parent folder of new folder to be created
	 * @param folderName Name of folder
	 * 
	 * @return New folder
	 * 
	 * @throws DuplicateNameException
	 */
	public GroupWorkspaceProjectPageFolder createNewFolder(GroupWorkspaceProjectPageFolder parentFolder,
			String folderName) throws DuplicateNameException;
	
	/**
	 * Get sub folders within parent folder for a group workspace project page 
	 * 
	 * @param groupWorkspaceProjectPageId Id of the group workspace project page
	 * @param parentFolderId Id of the parent folder
	 * 
	 * @return List of folders within the parent folder
	 * 
	 */
	public List<GroupWorkspaceProjectPageFolder> getFoldersForProjectPage(Long groupWorkspaceProjectPageId, Long parentFolderId) ;

	/**
	 * Get the specified folders for the group workspace project page.
	 * 
	 * @param groupWorkspaceProjectPageId Id of group workspace project page
	 * @param folderIds List of folder ids
	 * 
	 * @return List of folders
	 */
	public List<GroupWorkspaceProjectPageFolder> getFolders(Long groupWorkspaceProjectPageId, List<Long> folderIds);
	
	/**
	 * Delete all sub folders, files, publicatons and links from the system for the specified folder id.
	 * This physically removes the files stored on the file system.
	 * 
	 * @param Folder to be deleted
	 */
	public void deleteFolder(GroupWorkspaceProjectPageFolder groupWorkspaceProjectPageFolder) ;

	/**
	 * Get the group workspace project page folder with the specified name and parent id.
	 * 
	 * @param name Name of folder
	 * @param parentId Id of the parent folder
	 */
	public GroupWorkspaceProjectPageFolder getFolder(String name, Long parentId) ;

	/**
	 * Get the group workspace project page folder with the specified id.
	 * 
	 * @param id - id of the group workspace project page folder
     * @param lock - upgrade the lock
     * 
     * @return the group workspace project page folder if found otherwise null.
     */
	public GroupWorkspaceProjectPageFolder getFolder(Long id, boolean lock) ;

	/**
	 * Get root group workspace project page folder with the specified name for the specified researcher.
	 * 
	 * @param  name Name of root folder
	 * @param researcherId Id of researcher
	 * 
	 * @return Folder if found
	 */
	public GroupWorkspaceProjectPageFolder getRootFolder(String name, Long researcherId) ;

	/**
	 * Get all folders for the specified group workspace project apge.
	 * 
	 * @param group workspace project page Id Id of researcher to get all folders
	 * 
	 * @return List of all folders
	 */
	public List<GroupWorkspaceProjectPageFolder> getAllFolders(Long groupWorkspaceProjectPageId) ;
	
	/**
	 * Save the group workspace project page folder.
	 * 
	 * @param entity group workspace project page folder to be saved
	 */
	public void save(GroupWorkspaceProjectPageFolder entity);

	/**
	 * Get the group workspace project apge folders within the specified parent folder.
	 * 
	 * @param groupWorkspaceProjectPageId Id of group workspace project page
	 * @param parentFolderId Id of parent folder
	 * 
	 * @return List of folders
	 */
	public List<GroupWorkspaceProjectPageFolder> getFolders( 
			Long groupWorkspaceProjectPageId, 
			Long parentFolderId);

	/**
     * Create a group workspace project page file in the system with the specified ir file for the
     * given parent folder. This is created at the root level (added to the group workspace project page)
     * 
     * @param parentFolder - Folder to add the file to.
     * @param irFile - file to add
     * @param versionNumber - IrFile version number
     * 
     * @return the created researcher file
     * @throws DuplicateNameException 
     */
	public GroupWorkspaceProjectPageFolder addFile(
			GroupWorkspaceProjectPageFolder parentFolder,
			IrFile irFile, int versionNumber) ;

    /**
	 * Create the root publication.
	 * 
	 * @param groupWorkspaceProjectPage group workspace project page creating the publication
	 * @param item Item to add to the researcher
	 * @param the version of the publication 
	 */
	public GroupWorkspaceProjectPagePublication createRoot(GroupWorkspaceProjectPage groupWorkspaceProjectPage, GenericItem item, int versionNumber);

	/**
	 * Create the root institutional item
	 * 
	 * @param groupWorkspaceProjectPage groupWorkspaceProjectPage for creating the publication
	 * @param item Institutional Item to add to the researcher
	 */
	public GroupWorkspaceProjectPageInstitutionalItem createRoot(GroupWorkspaceProjectPage groupWorkspaceProjectPage, InstitutionalItem item);

	
	/**
	 * Delete a group workspace project page file.
	 * 
	 * @param groupWorkspaceProjectPageFile group workspace project page file to delete
	 */
	public void deleteFile(GroupWorkspaceProjectPageFile groupWorkspaceProjectPageFile);

	/**
	 * Delete a group workspace project page publication.
	 * 
	 * @param groupWorkspaceProjectPagePublication groupWorkspaceProjectPage to delete
	 */
	public void delete(GroupWorkspaceProjectPagePublication groupWorkspaceProjectPagePublication);
	
	/**
	 * Delete a group workspace project page publication.
	 * 
	 * @param groupWorkspaceProjectPageInstitutional Item to delete
	 */
	public void delete(GroupWorkspaceProjectPageInstitutionalItem groupWorkspaceProjectPageInstitutionalItem);
	
	/**
	 * Delete a group workspace project page link.
	 * 
	 * @param groupWorkspaceProjectPageLink
	 */
	public void delete(GroupWorkspaceProjectPageFileSystemLink gl);

	/** 
	 * Get the path to a specified folder with the specified folder id
	 * 
	 * @param groupWorkspaceProjectPageFolderId folder id to find the path for
	 * 
	 * @return Path of the folder
	 */
	public List<GroupWorkspaceProjectPageFolder> getFolderPath(Long groupWorkspaceFolderId);

	/**
	 * Get group workspace project page files within the specified parent folder
	 * 
	 * @param groupWorkspaceProjectPageId Id of groupWorkspaceProjectPageFolder holding the files
	 * @param parentFolderId Id of parent folder
	 * 
	 * @return List of files within the folder
	 */
	public List<GroupWorkspaceProjectPageFile> getFiles(Long groupWorkspaceProjectPageId, Long groupWorkspaceProjectPageFolderId);
	
	/**
	 * Get group workspace project page publications within the specified parent folder
	 * 
	 * @param groupWorkspaceProjectPageId Id of groupWorkspaceProjectPageFolder holding the files
	 * @param parentFolderId Id of parent folder
	 * 
	 * @return List of publications within the folder
	 */
	public List<GroupWorkspaceProjectPagePublication> getPublications(Long groupWorkspaceProjectPageId, Long parentFolderId);
	
	/**
	 * Get group workspace project page Institutional Item within the specified parent folder
	 * 
	 * @param groupWorkspaceProjectPageId Id of group workspace project page holding the Institutional Items
	 * @param parentFolderId Id of parent folder
	 * 
	 * @return List of publications within the folder
	 */
	public List<GroupWorkspaceProjectPageInstitutionalItem> getInstitutionalItems(Long groupWorkspaceProjectPageId, Long parentFolderId);


	/**
	 * Get the group workspace project page file with the specified id.
	 * 
	 * @param id - id of the group workspace project page file
     * @param lock - upgrade the lock
     * 
     * @return the group workspace project page file if found otherwise null.	 
     */
	public GroupWorkspaceProjectPageFile getFile(Long id, boolean lock);

	/**
	 * Allows a group workspace project page to create a new publication.
	 *
	 * @param parentFolder Folder to create the publication in
	 * @param item Item to create publication
	 * @param versionNumber - version of the personal publication 
	 *
	 * @return Newly created publication
	 */
	public GroupWorkspaceProjectPagePublication createPublication(GroupWorkspaceProjectPageFolder parentFolder, GenericItem item, int versionNumber);

	/**
	 * Allows a group workspace project page folder to create a new Institutional Item.
	 *
	 * @param parentFolder Folder to create the Institutional Item in
	 * @param item Item to create Institutional Item
	 *
	 * @return Newly created Institutional Item
	 */
	public GroupWorkspaceProjectPageInstitutionalItem createInstitutionalItem(GroupWorkspaceProjectPageFolder parentFolder, InstitutionalItem institutionalItem);

	
	/**
	 * Get the group workspace project page link with the specified id.
	 * 
	 * @param id - id of the group workspace link
     * @param lock - upgrade the lock
     * 
     * @return the researcher link if found otherwise null.	 
     */
	public GroupWorkspaceProjectPageFileSystemLink getLink(Long id, boolean lock);
	
	/**
	 * Get links for a group workspace project page in the specified folder
	 * 
	 * @param groupWorkspaceProjectPageId Id of group workspace project page id
	 * @param parentFolderId Id of parent folder
	 * 
	 *  @return List of group workspace project page file system links found
	 * 
	 */
	public List<GroupWorkspaceProjectPageFileSystemLink> getGroupWorkspacLinks(Long researcherId, Long parentFolderId);

	/**
	 * Get the group workspace project page publication with the specified id.
	 * 
	 * @param id - id of the group workspace project page publication
     * @param lock - upgrade the lock
     * 
     * @return the group workspace project page publication if found otherwise null.	 
     */
	public GroupWorkspaceProjectPagePublication getPublication(Long id, boolean lock);

	/**
	 * Get the group workspace project page Institutional Item with the specified id.
	 * 
	 * @param id - id of the group workspace project page Institutional Item
     * @param lock - upgrade the lock
     * 
     * @return the group workspace project page Institutional Item if found otherwise null.	 
     */
	public GroupWorkspaceProjectPageInstitutionalItem getInstitutionalItem(Long id, boolean lock);

	/**
	 * Save the group workspace project page link
	 * 
	 * @param link Link to be saved
	 */
	public void saveLink(GroupWorkspaceProjectPageFileSystemLink link);

	/**
	 * Get the count of group workspace project page files using this Irfile
	 * 
	 * @param irFile irFile used by researcher file
	 * 
	 * @return Number of group workspace project page files using this IrFile
	 */
	public Long getFileCount(IrFile irFile) ;
	
	/**
	 * Get the count of group workspace project page publications using this generic item
	 * 
	 * @param item Item to be searched for in the publication
	 * 
	 * @return Count of publications using this item in the group workspace project page
	 * 
	 */
	public Long getPublicationCount(GenericItem item) ;

	/**
	 * Get the count of group workspace project page Institutional Item using this generic item
	 * 
	 * @param item Item to be searched for in the Institutional Item
	 * 
	 * @return Count of Institutional Items using this item
	 * 
	 */
	public Long getGroupWorkspaceProjectPageInstitutionalItemCount(InstitutionalItem item) ;
	
	
	/**
	 * Save the group workspace project page file.
	 * 
	 * @param entity group workspace project page file to be saved
	 */
	public void save(GroupWorkspaceProjectPageFile entity);
	
	/**
	 * Save the group workspace project page institutional item
	 * 
	 * @param entity item to be saved
	 */
	public void save(GroupWorkspaceProjectPageInstitutionalItem entity);
	
	/**
	 * Save the group workspace project page publication
	 * 
	 * @param entity researcher file to be saved
	 */
	public void save(GroupWorkspaceProjectPagePublication entity);

	/**
	 * Delete the group workspace project page institutional item with the specified institutional item.
	 * 
	 * @param institutionalItem institutional item to be searched for
	 * 
	 */
	public void delete(InstitutionalItem institutionalItem);
	
	/**
	 * Get the files for the group workspace project page id and listed file id's.  If the list of fileIds 
	 * is null no files are returned.
	 * 
	 * @param groupWorkspaceProjectPageId
	 * @param fileIds
	 * 
	 * @return the found files
	 */
	public List<GroupWorkspaceProjectPageFile> getFiles(Long groupWorkspaceProjectPageId, List<Long> fileIds);
	
    /**
	 * Find the specified links for the given group workspace project page.
	 * 
	 * @param groupWorkspaceProjectPageId Researcher of the link
	 * @param linkIds Ids of  the link
	 * 
	 * @return List of links found
	 */
	public List<GroupWorkspaceProjectPageFileSystemLink> getLinks(final Long groupWorkspaceProjectPageId, final List<Long> linkIds);
	
	/**
	 * Get the specified institutional items for the group workspace project page.
	 * 
	 * @param groupWorkspaceProjectPageId - id of the group workspace project page ids
	 * @param institutionalItemIds - list of item ids to retrieve for the researcher
	 * 
	 * @return - List of institutional item found.
	 */
	public List<GroupWorkspaceProjectPageInstitutionalItem> getInstitutionalItems(final Long groupWorkspaceProjectPageId, final List<Long> institutionalItemIds);
	

    /**
	 * Find the specified publications for the given group workspace project page.
	 * 
	 * @param groupWorkspaceProjectPageId group workspace project page id of the publication
	 * @param publicationIds ids of  the publication
	 * 
	 * @return List of publications found
	 */
	public List<GroupWorkspaceProjectPagePublication> getPublications(final Long groupWorkspaceProjectPageId, final List<Long> groupWorkspaceProjectPageIds);

	/**
	 * Moves the specified folders, files, links, items and publications into the specified 
	 * group workspace project page folder destination.
	 * 
	 * @param destination - destination folder to move to
	 * @param foldersToMove - folders to move
	 * @param filesToMove - files to move
	 * @param linksToMove - links to move
	 * @param itemsToMove - items to move
	 * @param publicationsToMove publications to move
	 * 
	 * @return - the list of file system object that could not be moved.
	 */
	public List<FileSystem> moveFileSystemInformation(GroupWorkspaceProjectPageFolder destination,
			List<GroupWorkspaceProjectPageFolder> foldersToMove, 
			List<GroupWorkspaceProjectPageFile> filesToMove, 
			List<GroupWorkspaceProjectPageFileSystemLink> linksToMove,
			List<GroupWorkspaceProjectPageInstitutionalItem> itemsToMove,
			List<GroupWorkspaceProjectPagePublication> publicationsToMove);
	
	/**
	 * Moves the specified folders, files, links, items and publications into the specified 
	 * group workspace project page root location.
	 * 
	 * @param destination - destination folder to move to
	 * @param foldersToMove - folders to move
	 * @param filesToMove - files to move
	 * @param linksToMove - links to move
	 * @param itemsToMove - items to move
	 * @param publicationsToMove publications to move
	 * 
	 * @return - the list of file system object that could not be moved.
	 */
	public List<FileSystem> moveFileSystemInformation(GroupWorkspaceProjectPage groupWorkspaceProjectPage,
			List<GroupWorkspaceProjectPageFolder> foldersToMove, 
			List<GroupWorkspaceProjectPageFile> filesToMove, 
			List<GroupWorkspaceProjectPageLink> linksToMove,
			List<GroupWorkspaceProjectPageInstitutionalItem> itemsToMove,
			List<GroupWorkspaceProjectPagePublication> publicationsToMove);

}
