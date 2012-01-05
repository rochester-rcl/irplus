package edu.ur.ir.web.action.groupspace;

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
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Move group workspace file system objects 
 * @author Nathan Sarr
 *
 */
public class MoveGroupWorkspaceFileSystemObjects extends ActionSupport implements UserIdAware {
	
	// eclipse generated id
	private static final long serialVersionUID = 2109317538630353586L;

	// id of the user performing the action
	private Long userId;
	
	// set of folder ids to move 
	private Long[] groupFolderIds = {};
	
	// set of file ids to move 
	private Long[] groupFileIds = {};
	
	// folders to move 
	private List<GroupWorkspaceFolder> foldersToMove = new LinkedList<GroupWorkspaceFolder>();
	
	// files to move 
	private List<GroupWorkspaceFile> filesToMove = new LinkedList<GroupWorkspaceFile> ();
	
	// location to move the folders and files  
	private Long destinationId = GroupWorkspaceFileSystemService.ROOT_FOLDER_ID;
	
	// Files and folder service 
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;

	// User service 
	private UserService userService;
	
	//user performing the operation
	private IrUser user;
	
	// path to the destination 
	private List<GroupWorkspaceFolder> destinationPath;
	
	// current contents of the destination folder 
	private List<FileSystem> currentDestinationContents = new LinkedList<FileSystem>();
	

	
	// current destination */
	private GroupWorkspaceFolder destination;
	


	//  Logger 
	private static final Logger log = Logger.getLogger(MoveGroupWorkspaceFileSystemObjects.class);

    // current root location where all files are being moved from
    private Long parentFolderId;
    
    // id of the group workspace
    private Long groupWorkspaceId;
    
    // service to deal with group workspace inforamtion
    private GroupWorkspaceService groupWorkspaceService;
    
    // parent group workspace
    private GroupWorkspace groupWorkspace;
	



	/**
	 * Set the group workspace.
	 * 
	 * @param groupWorkspaceService
	 */
	public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}

	/**
	 * Takes the user to view the locations that the folder can be moved to.
	 * 
	 * @return
	 */
	public String viewLocations()
	{
		log.debug("view move locations");
		user = userService.getUser(userId, false);

		groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		List<Long> listFolderIds = new LinkedList<Long>();
		for( Long id : groupFolderIds)
		{
		    listFolderIds.add(id);
		}
		
		foldersToMove = groupWorkspaceFileSystemService.getFoldersByIds(groupWorkspaceId, listFolderIds);
		List<Long> listFileIds = new LinkedList<Long>();
		for( Long id : groupFileIds)
		{
			    listFileIds.add(id);
		}
		filesToMove = groupWorkspaceFileSystemService.getFilesByIds(groupWorkspaceId, listFileIds);
		
		if( !destinationId.equals(UserFileSystemService.ROOT_FOLDER_ID))
		{
		    destination = groupWorkspaceFileSystemService.getFolder(destinationId, false);
		    
		   
		    IrUser user = userService.getUser(userId, false);
		    
		    // user cannot access a folder in a workspace they do not have permissions for
		    if( !user.hasRole(IrRole.ADMIN_ROLE) || groupWorkspace.getUser(user) == null )
		    {
		    	return "accessDenied";
		    }
		    
		    // make sure the user has not navigated into a child or itself- this is illegal
		    for(GroupWorkspaceFolder folder: foldersToMove)
		    {
		    	if(destination.equals(folder))
		    	{
		    		throw new IllegalStateException("cannot move a folder into itself destination = " + destination
		    				+ " folder = " + folder);
		    	}
		    	else if( folder.getTreeRoot().equals(destination.getTreeRoot()) &&
		    			 destination.getLeftValue() > folder.getLeftValue() &&
		    			 destination.getRightValue() < folder.getRightValue() )
		    	{
		    		throw new IllegalStateException("cannot move a folder into a child destination = " + destination
		    				+ " folder = " + folder);
		    	}
		    }
		    
		    destinationPath = groupWorkspaceFileSystemService.getFolderPath(destination);
		    currentDestinationContents.addAll(destination.getChildren());
		    currentDestinationContents.addAll(destination.getFiles());
		}
		else
		{
			
			// user cannot access a folder in a workspace they do not have permissions for
		    if( !user.hasRole(IrRole.ADMIN_ROLE) || groupWorkspace.getUser(user) == null )
		    {
		    	return "accessDenied";
		    }
			currentDestinationContents.addAll(groupWorkspace.getRootFolders());
			currentDestinationContents.addAll(groupWorkspace.getRootFiles());
		}
		
		return SUCCESS;
	}

	/**
	 * Takes the user to view the locations that the folder can be moved to.
	 * 
	 * @return
	 */
	public String move()
	{
		log.debug("move files and folders called");
		user = userService.getUser(userId, false);
		groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		
		List<FileSystem> notMoved;
		

		List<Long> listFolderIds = new LinkedList<Long>();
		for( Long id : groupFolderIds)
		{
			log.debug("adding folder id " + id);
		    listFolderIds.add(id);
		}
		
		// folders are accessed by user id so they cannot move folders that do
		// not belong to them.
		foldersToMove = groupWorkspaceFileSystemService.getFoldersByIds(groupWorkspaceId, listFolderIds);

		List<Long> listFileIds = new LinkedList<Long>();
		for( Long id : groupFileIds)
		{
			log.debug("adding file id " + id);
		    listFileIds.add(id);
		}
		
		// files are accessed by user id so this prevents users from accessing files
		// that do not belong to them
		filesToMove = groupWorkspaceFileSystemService.getFilesByIds(groupWorkspaceId, listFileIds);
		

		log.debug( "destination id = " + destinationId);
		if( !destinationId.equals(UserFileSystemService.ROOT_FOLDER_ID))
		{
		    destination = 
		    	 groupWorkspaceFileSystemService.getFolder(destinationId, false);
		    
		    destinationPath = groupWorkspaceFileSystemService.getFolderPath(destination);
		    currentDestinationContents.addAll(destination.getChildren());
		    currentDestinationContents.addAll(destination.getFiles());
		    try {
				notMoved = groupWorkspaceFileSystemService.moveFolderSystemInformation(user, destination, foldersToMove, filesToMove);
			} catch (PermissionNotGrantedException e) {
				return "accessDenied";
			}
					    
		}
		else
		{
			currentDestinationContents.addAll(groupWorkspace.getRootFolders());
			currentDestinationContents.addAll(groupWorkspace.getRootFiles());
			try {
				notMoved = groupWorkspaceFileSystemService.moveFolderSystemInformation(user, groupWorkspace, foldersToMove, filesToMove);
			} catch (PermissionNotGrantedException e) {
				return "accessDenied";
			}
			
		}
		
		if( notMoved.size() > 0 )
		{
			String message = getText("folderNamesAlreadyExist");
			StringBuffer sb = new StringBuffer(message);
			for(FileSystem fileSystem : notMoved)
			{
			    sb.append( " " + fileSystem.getName());
			}
			addFieldError("moveError", sb.toString());
			return ERROR;
		}
		
		
		//load the data
        viewLocations();		
		
		return SUCCESS;
	}

	/**
	 * Set the folder ids.
	 * 
	 * @param folderIds
	 */
	public void setGroupFolderIds(Long[] folderIds) {
		this.groupFolderIds = folderIds;
	}
	

	/**
	 * Get the destination.
	 * 
	 * @return
	 */
	public GroupWorkspaceFolder getDestination() {
		return destination;
	}



	/**
	 * Set the file ids.
	 * 
	 * @param fileIds
	 */
	public void setGroupFileIds(Long[] fileIds) {
		this.groupFileIds = fileIds;
	}


	/**
	 * Set the folder destination id.
	 * 
	 * @param destinationId
	 */
	public void setDestinationId(Long destinationId) {
		this.destinationId = destinationId;
	}

	/**
	 * Get teh destination path.
	 * 
	 * @return
	 */
	public List<GroupWorkspaceFolder> getDestinationPath() {
		return destinationPath;
	}


	/**
	 * Get the current destination contents.
	 * 
	 * @return
	 */
	public List<FileSystem> getCurrentDestinationContents() {
		return currentDestinationContents;
	}


	/**
	 * Get the destination id.
	 * 
	 * @return
	 */
	public Long getDestinationId() {
		return destinationId;
	}

	/**
	 * Get the parent folder id.
	 * 
	 * @return
	 */
	public Long getParentFolderId() {
		return parentFolderId;
	}

	/**
	 * Set the parent folder id.
	 * 
	 * @param parentFolderId
	 */
	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	/**
	 * Allow a user id to be passed in.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
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
	 * Get the files to move.
	 * 
	 * @return
	 */
	public List<GroupWorkspaceFile> getFilesToMove() {
		return filesToMove;
	}


	/**
	 * Get the folders to move.
	 * 
	 * @return
	 */
	public List<GroupWorkspaceFolder> getFoldersToMove() {
		return foldersToMove;
	}

	/**
	 * Get the user.
	 * 
	 * @return
	 */
	public IrUser getUser() {
		return user;
	}
	
	/**
	 * Get the group workspace id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceId() {
		return groupWorkspaceId;
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
	 * Set the group workspace file system service.
	 * @param groupWorkspaceFileSystemService
	 */
	public void setGroupWorkspaceFileSystemService(
			GroupWorkspaceFileSystemService groupWorkspaceFileSystemService) {
		this.groupWorkspaceFileSystemService = groupWorkspaceFileSystemService;
	}
	
	
	/**
	 * Get the group workspace.
	 * 
	 * @return
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}
	

}
