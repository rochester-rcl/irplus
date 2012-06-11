package edu.ur.ir.web.action.groupspace;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.FileSystem;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFile;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemLink;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolder;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageInstitutionalItem;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPagePublication;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * @author Nathan Sarr
 *
 * Move group workspace project page information.
 */
public class MoveGroupWorkspaceProjectPageInformation extends ActionSupport implements UserIdAware {
	
	// eclipse generated id */
	private static final long serialVersionUID = 4449780977528697976L;

	// id of the user  */
	private Long userId;

	// id of the project page */
	private Long groupWorkspaceProjectPageId;
	
	// set of folder ids to move */
	private Long[] folderIds = {};
	
	// set of file ids to move */
	private Long[] fileIds = {};
	
	// set of institutional item ids to move */
	private Long[] itemIds = {};
	
	// set of link ids to move */
	private Long[] linkIds = {};
	
	// folders to move */
	private List<GroupWorkspaceProjectPageFolder> foldersToMove = new LinkedList<GroupWorkspaceProjectPageFolder>();
	

	// files to move */
	private List<GroupWorkspaceProjectPageFile> filesToMove = new LinkedList<GroupWorkspaceProjectPageFile> ();
	
	// links to move */
	private List<GroupWorkspaceProjectPageFileSystemLink> linksToMove = new LinkedList<GroupWorkspaceProjectPageFileSystemLink> ();
	
	// publications to move */
	private List<GroupWorkspaceProjectPageInstitutionalItem> itemsToMove = new LinkedList<GroupWorkspaceProjectPageInstitutionalItem> ();
	
	// location to move the folders and files  */
	private Long destinationId = GroupWorkspaceProjectPageFileSystemService.USE_GROUP_WORKSPACE_PROJECT_PAGE_AS_ROOT;
	
	// Service for dealing with researcher information*/
    private GroupWorkspaceProjectPageFileSystemService groupWorkspaceProjectPageFileSystemService;
	
    // Service for getting user information */
	private UserService userService;
	
	// path to the destination */
	private List<GroupWorkspaceProjectPageFolder> destinationPath;
	
	// current contents of the destination folder */
	private List<FileSystem> currentDestinationContents = new LinkedList<FileSystem>();
	
	// current destination */
	private GroupWorkspaceProjectPageFolder destination;
		
	// Logger */
	private static final Logger log = Logger.getLogger(MoveGroupWorkspaceProjectPageInformation.class);

	// current root location where all files are being moved from*/
    private Long parentFolderId;
    
    // service to deal with group workspace projge page information
    private GroupWorkspaceProjectPageService groupWorkspaceProjectPageService;
    
    // publications to move */
	private List<GroupWorkspaceProjectPagePublication> publicationsToMove = new LinkedList<GroupWorkspaceProjectPagePublication> ();
	
	// project page
	private GroupWorkspaceProjectPage groupWorkspaceProjectPage;


	/**
	 * Takes the user to view the locations that the folder can be moved to.
	 * 
	 * @return
	 */
	public String viewLocations()
	{
		log.debug("view move locations");
	
		IrUser user = userService.getUser(userId, false);
		groupWorkspaceProjectPage = groupWorkspaceProjectPageService.getById(groupWorkspaceProjectPageId, false);
		GroupWorkspaceUser workspaceUser = groupWorkspaceProjectPage.getGroupWorkspace().getUser(user);
		
		if( workspaceUser == null || !workspaceUser.isOwner() ) 
		{
			groupWorkspaceProjectPage = null;
			// user cannot move file if they are not an owner of the group workspace
	    	return "accessDenied";
		}

		// folders to move
		List<Long> listFolderIds = new LinkedList<Long>();
		for( Long id : folderIds)
		{
		    listFolderIds.add(id);
		}
		foldersToMove = groupWorkspaceProjectPageFileSystemService.getFolders(groupWorkspaceProjectPageId, listFolderIds);
		
		// files to move
		List<Long> listFileIds = new LinkedList<Long>();
		for( Long id : fileIds)
		{
			    listFileIds.add(id);
		}
		filesToMove = groupWorkspaceProjectPageFileSystemService.getFiles(groupWorkspaceProjectPageId, listFileIds);
		
		//links to move
		List<Long> listLinkIds = new LinkedList<Long>();
		for( Long id : linkIds)
		{
			listLinkIds.add(id);
		}
		linksToMove = groupWorkspaceProjectPageFileSystemService.getLinks(groupWorkspaceProjectPageId, listLinkIds);
		
		// items to move
		List<Long> listItemIds = new LinkedList<Long>();
		for( Long id : itemIds)
		{
			log.debug(" adding item id " + id);
			listItemIds.add(id);
		}
		
		itemsToMove = groupWorkspaceProjectPageFileSystemService.getInstitutionalItems(groupWorkspaceProjectPageId, listItemIds);
		log.debug("items size = " + itemsToMove.size() );
		for( GroupWorkspaceProjectPageInstitutionalItem i : itemsToMove )
		{
			log.debug("Found item " + i);
		}
		
		if( !destinationId.equals(ResearcherFileSystemService.USE_RESEARCHER_AS_ROOT))
		{
		    destination = 
		    	groupWorkspaceProjectPageFileSystemService.getFolder(destinationId, false);
		    
		    if( !destination.getGroupWorkspaceProjectPage().getId().equals(groupWorkspaceProjectPageId))
		    {
		    	// user cannot move file into a destination that they do not own
		    	return "accessDenied";
		    }
		    
		    // make sure the user has not navigated into a child or itself- this is illegal
		    for(GroupWorkspaceProjectPageFolder folder: foldersToMove)
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
		    
		    destinationPath = groupWorkspaceProjectPageFileSystemService.getFolderPath(destination.getId());
		    currentDestinationContents.addAll(destination.getChildren());
		    currentDestinationContents.addAll(destination.getFiles());
		    currentDestinationContents.addAll(destination.getLinks());
		    currentDestinationContents.addAll(destination.getPublications());
		    currentDestinationContents.addAll(destination.getInstitutionalItems());
		}
		else
		{
			currentDestinationContents.addAll(groupWorkspaceProjectPage.getRootFolders());
			currentDestinationContents.addAll(groupWorkspaceProjectPage.getRootFiles());
			currentDestinationContents.addAll(groupWorkspaceProjectPage.getRootLinks());
			currentDestinationContents.addAll(groupWorkspaceProjectPage.getRootPublications());
			currentDestinationContents.addAll(groupWorkspaceProjectPage.getRootInstitutionalItems());
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
		IrUser user = userService.getUser(userId, false);
		groupWorkspaceProjectPage = groupWorkspaceProjectPageService.getById(groupWorkspaceProjectPageId, false);
		GroupWorkspaceUser workspaceUser = groupWorkspaceProjectPage.getGroupWorkspace().getUser(user);
		
		if( workspaceUser == null || !workspaceUser.isOwner() ) 
		{
			groupWorkspaceProjectPage = null;
			// user cannot move file if they are not an owner of the group workspace
	    	return "accessDenied";
		}
		

		List<FileSystem> notMoved;

		List<Long> listFolderIds = new LinkedList<Long>();
		for( Long id : folderIds)
		{
			log.debug("adding folder id " + id);
		    listFolderIds.add(id);
		}
		
		// folders are accessed by user id so they cannot move folders that do
		// not belong to them.
		foldersToMove = groupWorkspaceProjectPageFileSystemService.getFolders(groupWorkspaceProjectPageId, listFolderIds);

		List<Long> listFileIds = new LinkedList<Long>();
		for( Long id : fileIds)
		{
			log.debug("adding file id " + id);
		    listFileIds.add(id);
		}
		
		// files are accessed by user id so this prevents users from accessing files
		// that do not belong to them
		filesToMove = groupWorkspaceProjectPageFileSystemService.getFiles(groupWorkspaceProjectPageId, listFileIds);
		
		//links to move
		List<Long> listLinkIds = new LinkedList<Long>();
		for( Long id : linkIds)
		{
			listLinkIds.add(id);
		}
		linksToMove = groupWorkspaceProjectPageFileSystemService.getLinks(groupWorkspaceProjectPageId, listLinkIds);
		
		// items to move
		List<Long> listItemIds = new LinkedList<Long>();
		for( Long id : itemIds)
		{
			listItemIds.add(id);
		}
		itemsToMove = groupWorkspaceProjectPageFileSystemService.getInstitutionalItems(groupWorkspaceProjectPageId, listItemIds);
		
		log.debug( "destination id = " + destinationId);
		if( !destinationId.equals(UserFileSystemService.ROOT_FOLDER_ID))
		{
		    destination = 
		    	groupWorkspaceProjectPageFileSystemService.getFolder(destinationId, false);
		    if( !destination.getGroupWorkspaceProjectPage().getId().equals(groupWorkspaceProjectPageId))
		    {
		    	// user cannot move file into a destination that they do not own
		    	return "accessDenied";
		    }
		    
		    notMoved = 
		    	groupWorkspaceProjectPageFileSystemService.moveFileSystemInformation(destination, 
		    			foldersToMove, 
		    			filesToMove, 
		    			linksToMove, 
		    			itemsToMove, 
		    			publicationsToMove);
		}
		else
		{
			notMoved = groupWorkspaceProjectPageFileSystemService.moveFileSystemInformation(groupWorkspaceProjectPage, 
					foldersToMove, 
	    			filesToMove, 
	    			linksToMove, 
	    			itemsToMove, 
	    			publicationsToMove);
		}
		
		if( notMoved.size() > 0 )
		{
			String message = getText("folderNamesAlreadyExist");
			StringBuffer sb = new StringBuffer(message);
			for(FileSystem fileSystem : notMoved)
			{
			   sb.append(message + " " + fileSystem.getName());
			}
			addFieldError("moveError", sb.toString());
			//load the data
	        viewLocations();
	        return ERROR;
		}
		
		return SUCCESS;
	}

	public void injectUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Set the user service for dealing with user information.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Get the user id
	 * 
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Get the project page id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceProjectPageId() {
		return groupWorkspaceProjectPageId;
	}

	/**
	 * Set the project page id.
	 * 
	 * @param projectPageId
	 */
	public void setGroupWorkspaceProjectPageId(Long projectPageId) {
		this.groupWorkspaceProjectPageId = projectPageId;
	}

	/**
	 * Get the folder ids.
	 * 
	 * @return
	 */
	public Long[] getFolderIds() {
		return folderIds;
	}

	/**
	 * Set the folder ids.
	 * 
	 * @param folderIds
	 */
	public void setFolderIds(Long[] folderIds) {
		this.folderIds = folderIds;
	}

	/**
	 * Get the file ids.
	 * 
	 * @return
	 */
	public Long[] getFileIds() {
		return fileIds;
	}

	/**
	 * Set the file ids.
	 * 
	 * @param fileIds
	 */
	public void setFileIds(Long[] fileIds) {
		this.fileIds = fileIds;
	}

	/**
	 * Get the item ids.
	 * 
	 * @return
	 */
	public Long[] getItemIds() {
		return itemIds;
	}

	/**
	 * Set the item ids.
	 * 
	 * @param itemIds
	 */
	public void setItemIds(Long[] itemIds) {
		this.itemIds = itemIds;
	}

	/**
	 * Get the link ids.
	 * 
	 * @return
	 */
	public Long[] getLinkIds() {
		return linkIds;
	}

	/**
	 * Set the link ids.
	 * 
	 * @param linkIds
	 */
	public void setLinkIds(Long[] linkIds) {
		this.linkIds = linkIds;
	}

	/**
	 * Get the destination id
	 * 
	 * @return
	 */
	public Long getDestinationId() {
		return destinationId;
	}

	/**
	 * Set the destination id.
	 * 
	 * @param destinationId
	 */
	public void setDestinationId(Long destinationId) {
		this.destinationId = destinationId;
	}

	
	/**
	 * Get the destination path.
	 * 
	 * @return
	 */
	public List<GroupWorkspaceProjectPageFolder> getDestinationPath() {
		return destinationPath;
	}

	/**
	 * Get th current destination contents.
	 * 
	 * @return
	 */
	public List<FileSystem> getCurrentDestinationContents() {
		return currentDestinationContents;
	}

	/**
	 * Get the destination folder.
	 * 
	 * @return
	 */
	public GroupWorkspaceProjectPageFolder getDestination() {
		return destination;
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
	 * Set the group workspace project page file system service.
	 * 
	 * @param groupWorkspaceProjectPageFileSystemService
	 */
	public void setGroupWorkspaceProjectPageFileSystemService(
			GroupWorkspaceProjectPageFileSystemService groupWorkspaceProjectPageFileSystemService) {
		this.groupWorkspaceProjectPageFileSystemService = groupWorkspaceProjectPageFileSystemService;
	}

	/**
	 * Set the group workspace project page service.
	 * 
	 * @param groupWorkspaceProjectPageService
	 */
	public void setGroupWorkspaceProjectPageService(
			GroupWorkspaceProjectPageService groupWorkspaceProjectPageService) {
		this.groupWorkspaceProjectPageService = groupWorkspaceProjectPageService;
	}

	
	/**
	 * Get the folders to move.
	 * 
	 * @return
	 */
	public List<GroupWorkspaceProjectPageFolder> getFoldersToMove() {
		return foldersToMove;
	}

	/**
	 * Get the files to move.
	 * 
	 * @return
	 */
	public List<GroupWorkspaceProjectPageFile> getFilesToMove() {
		return filesToMove;
	}

	/**
	 * Get the links to move.
	 * 
	 * @return
	 */
	public List<GroupWorkspaceProjectPageFileSystemLink> getLinksToMove() {
		return linksToMove;
	}

	/**
	 * Get the items to move.
	 * 
	 * @return
	 */
	public List<GroupWorkspaceProjectPageInstitutionalItem> getItemsToMove() {
		return itemsToMove;
	}
	
	/**
	 * Get the group workspace project page.
	 * @return
	 */
	public GroupWorkspaceProjectPage getGroupWorkspaceProjectPage() {
		return groupWorkspaceProjectPage;
	}


}
