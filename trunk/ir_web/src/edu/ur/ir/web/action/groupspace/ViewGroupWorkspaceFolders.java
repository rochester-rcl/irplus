package edu.ur.ir.web.action.groupspace;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.FileSystem;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.user.IrUser;


import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.action.user.FileSystemSortHelper;

/**
 * Allow a user to view group workspace folders.
 * 
 * @author Nathan Sarr
 *
 */
public class ViewGroupWorkspaceFolders extends ActionSupport implements UserIdAware {
	

	/* eclipse generated id  */
	private static final long serialVersionUID = 4457825493582335593L;

	/*  Id of the suer who has folders  */
	private Long userId;
	
	/* The user who owns the folders  */
	private IrUser user;
	
	/*  User information data access  */
	private UserService userService;

	/* Group workspace user wants to access */
	private GroupWorkspace groupWorkspace;

	/* A collection of folders and files for a user in a given location of
        ther personal directory.*/
    private List<FileSystem> fileSystem;
    
    /* set of folders that are the path for the current folder */
    private Collection <GroupWorkspaceFolder> folderPath;
	
	/* The folder that owns the listed files and folders */
	private Long parentFolderId;	

	/* type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "desc";
	
	/* name of the element to sort on 
	 *   this is for incoming requests */
	private String sortElement = "type";
	
	/* use the type sort this is information for the page */
	private String folderTypeSort = "none";
	
	/* use the name sort this is information for the page */
	private String folderNameSort = "none";

	/*  Logger for vierw workspace action */
	private static final Logger log = Logger.getLogger(ViewGroupWorkspaceFolders.class);
	
	/* Parentg group folder */
	private GroupWorkspaceFolder parentFolder;

	/* Service to deal with group workspace information  */
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;
	
	/* id of the group workspace */
	private Long groupWorkspaceId;

	/* Service to deal with group workspace information */
	private GroupWorkspaceService groupWorkspaceService;
	
	/* list of folder ids to perform actions on*/
	private Long[] groupFolderIds;

	/* list of file ids to perform actions on*/
	private Long[] groupFileIds;

	/**
	 * Get folder table
	 * 
	 * @return
	 */
	public String getTable()
	{
		user = userService.getUser(userId, false);
		log.debug("parent folder id = " + parentFolderId);
		if(parentFolderId != null && parentFolderId > 0)
		{
			parentFolder = groupWorkspaceFileSystemService.getFolder(parentFolderId, false);
		    if( !groupWorkspaceService.userIsGroupWorkspaceMember(user.getId(), parentFolder.getGroupWorkspace().getId()) )
		    {
		    	return "accessDenied";
		    }
		}
		
		log.debug("getTableCalled");
		createFileSystem();

		return SUCCESS;
	}
	
	/**
	 * Removes the select files and folders.
	 * 
	 * @return
	 */
	public String deleteFileSystemObjects()
	{		
		log.debug("Delete folders called");
		user = userService.getUser(userId, false);
		
		if( groupFolderIds != null )
		{
		    for(int index = 0; index < groupFolderIds.length; index++)
		    {
			    log.debug("Deleting folder with id " + groupFolderIds[index]);
			    GroupWorkspaceFolder gf = groupWorkspaceFileSystemService.getFolder(groupFolderIds[index], false);
			    
			    
			    //un-index all the files
			    //List<PersonalFile> allFiles =  userFileSystemService.getAllFilesForFolder(pf);
			    
			    //for(PersonalFile aFile : allFiles)
			    //{
			    //	deleteFileFromIndex(aFile, user);
			    //}
			    
			    try {
					groupWorkspaceFileSystemService.delete(gf, user, "OWNER DELETING FOLDER - " + gf.getFullPath());
				} catch (PermissionNotGrantedException e) {
					return "accessDenied";
				}
		    }
		}
		
		if(groupFileIds != null)
		{
			for(int index = 0; index < groupFileIds.length; index++)
			{
				log.debug("Deleting file with id " + groupFileIds[index]);
				GroupWorkspaceFile gf = groupWorkspaceFileSystemService.getFile( groupFileIds[index], false);
				// if( !pf.getOwner().getId().equals(userId))
				// {
				//   	return "accessDenied";
				// }
				//deleteFileFromIndex(pf, user);
				groupWorkspaceFileSystemService.delete(gf, user, "OWNER DELETING FILE");
			}
		}
		createFileSystem();
		return SUCCESS;
	}
	
	/**

	
	/**
	 * Create the group workspace file system to view.
	 */
	private void createFileSystem()
	{
		groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		if(parentFolderId != null && parentFolderId > 0)
		{
			parentFolder = groupWorkspaceFileSystemService.getFolder(parentFolderId, false);
		    folderPath = groupWorkspaceFileSystemService.getFolderPath(parentFolder);
		}
		
		Collection<GroupWorkspaceFolder> folders = groupWorkspaceFileSystemService.getFolders(groupWorkspaceId, parentFolderId);
		Collection<GroupWorkspaceFile>files = groupWorkspaceFileSystemService.getFiles(groupWorkspaceId, parentFolderId);
		
	    fileSystem = new LinkedList<FileSystem>();
	    
	    fileSystem.addAll(folders);
	    fileSystem.addAll(files);
	    
	    FileSystemSortHelper sortHelper = new FileSystemSortHelper();
	    if( sortElement.equals("type"))
	    {
	    	if(sortType.equals("asc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.TYPE_ASC);
	    		folderTypeSort = "asc";
	    	}
	    	else if( sortType.equals("desc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.TYPE_DESC);
	    		folderTypeSort = "desc";
	    	}
	    	else
	    	{
	    		folderTypeSort = "none";
	    	}
	    }
	    
	    if( sortElement.equals("name"))
	    {
	    	if(sortType.equals("asc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.NAME_ASC);
	    		folderNameSort = "asc";
	    	}
	    	else if( sortType.equals("desc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.NAME_DESC);
	    		folderNameSort = "desc";
	    	}
	    	else
	    	{
	    		folderNameSort = "none";
	    	}
	    }
	    
	    if( log.isDebugEnabled())
	    {
	    	log.debug("done create file system");
	        log.debug("File system size = " + fileSystem.size());
	    }
	    
	}
	

	
	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	/**
	 * Set the parent id.
	 * 
	 * @param parentFolderId
	 */
	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}


	/**
	 * Get the list of file system objects found.
	 * 
	 * @return
	 */
	public Collection<FileSystem> getFileSystem() {
		return fileSystem;
	}
	

	/**
	 * Get the folder path for the given folder.
	 * 
	 * @return
	 */
	public Collection<GroupWorkspaceFolder> getFolderPath() {
		return folderPath;
	}

	/**
	 * Get the user accessing the inforamtion.
	 * 
	 * @return
	 */
	public IrUser getUser() {
		return user;
	}


	/**
	 * Get the sort type
	 * 
	 * @return
	 */
	public String getSortType() {
		return sortType;
	}

	/**
	 * Set the sort type.
	 * 
	 * @param sortType
	 */
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	/**
	 * Get the sort element.
	 * 
	 * @return
	 */
	public String getSortElement() {
		return sortElement;
	}

	/**
	 * Set the sort element.
	 * 
	 * @param sortElement
	 */
	public void setSortElement(String sortElement) {
		this.sortElement = sortElement;
	}

	/**
	 * Get the folder type sort.
	 * 
	 * @return
	 */
	public String getFolderTypeSort() {
		return folderTypeSort;
	}

	/**
	 * Set the folder type sort.
	 * 
	 * @param typeHeaderSort
	 */
	public void setFolderTypeSort(String typeHeaderSort) {
		this.folderTypeSort = typeHeaderSort;
	}

	/**
	 * Get the folder name sort.
	 * 
	 * @return
	 */
	public String getFolderNameSort() {
		return folderNameSort;
	}

	/**
	 * Set the folder name sort.
	 * 
	 * @param folderIdSort
	 */
	public void setFolderNameSort(String folderIdSort) {
		this.folderNameSort = folderIdSort;
	}


	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}

	
	/**
	 * Get the current parent folder.
	 * 
	 * @return
	 */
	public GroupWorkspaceFolder getParentFolder() {
		return parentFolder;
	}
	
	
	/**
	 * Get the group workspace.
	 * 
	 * @return
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}

	/**
	 * Set the group workspace id.
	 * 
	 * @param groupWorkspaceId
	 */
	public void setGroupWorkspaceId(Long groupWorkspaceId) {
		this.groupWorkspaceId = groupWorkspaceId;
	}

	/**
	 * Set the group workspace file system.
	 * 
	 * @param groupWorkspaceFileSystemService
	 */
	public void setGroupWorkspaceFileSystemService(
			GroupWorkspaceFileSystemService groupWorkspaceFileSystemService) {
		this.groupWorkspaceFileSystemService = groupWorkspaceFileSystemService;
	}

	/**
	 * Set the group workspace service.
	 * 
	 * @param groupWorkspaceService
	 */
	public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}
	
	/**
	 * Set the group folder ids.
	 * 
	 * @param groupFolderIds
	 */
	public void setGroupFolderIds(Long[] groupFolderIds) {
		this.groupFolderIds = groupFolderIds;
	}

	/**
	 * Set the group file ids.
	 * 
	 * @param groupFileIds
	 */
	public void setGroupFileIds(Long[] groupFileIds) {
		this.groupFileIds = groupFileIds;
	}
	
	/**
	 * Get the parent folder id.
	 * 
	 * @return
	 */
	public Long getParentFolderId() {
		return parentFolderId;
	}

}
