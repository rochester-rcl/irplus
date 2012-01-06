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

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.FileSystem;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherInstitutionalItem;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherPublication;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * @author Nathan Sarr
 * 
 * Action to move researcher information
 *
 */
public class MoveResearcherInformation extends ActionSupport implements UserIdAware {
	
	/** eclipse generated id */
	private static final long serialVersionUID = -7358586770881717803L;
	
	/** id of the user  */
	private Long userId;
	
	/** id of the researcher */
	private Long researcherId;
	
	/** set of folder ids to move */
	private Long[] folderIds = {};
	
	/** set of file ids to move */
	private Long[] fileIds = {};
	
	/** set of institutional item ids to move */
	private Long[] itemIds = {};
	
	/** set of link ids to move */
	private Long[] linkIds = {};
	
	/** set of publication ids to move */
	private Long[] publicationIds = {};
	
	/** folders to move */
	private List<ResearcherFolder> foldersToMove = new LinkedList<ResearcherFolder>();
	
	/** files to move */
	private List<ResearcherFile> filesToMove = new LinkedList<ResearcherFile> ();
	
	/** links to move */
	private List<ResearcherLink> linksToMove = new LinkedList<ResearcherLink> ();
	
	/** publications to move */
	private List<ResearcherPublication> publicationsToMove = new LinkedList<ResearcherPublication> ();
	
	/** publications to move */
	private List<ResearcherInstitutionalItem> itemsToMove = new LinkedList<ResearcherInstitutionalItem> ();
	
	/** location to move the folders and files  */
	private Long destinationId = ResearcherFileSystemService.USE_RESEARCHER_AS_ROOT;
	
    /** Service for dealing with researcher information*/
    private ResearcherFileSystemService researcherFileSystemService;
	
	/** Service for getting user information */
	private UserService userService;
	
	/** path to the destination */
	private List<ResearcherFolder> destinationPath;
	
	/** current contents of the destination folder */
	private List<FileSystem> currentDestinationContents = new LinkedList<FileSystem>();
	
	/** boolean to indicate that the action was successful */
	private boolean actionSuccess;
	
	/** current destination */
	private ResearcherFolder destination;
	
	/** researcher information  */
	private Researcher researcher;
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(MoveResearcherInformation.class);

    /** current root location where all files are being moved from*/
    private Long parentFolderId;
	
	/**
	 * Takes the user to view the locations that the folder can be moved to.
	 * 
	 * @return
	 */
	public String viewLocations()
	{
		log.debug("view move locations");
	
		IrUser user = userService.getUser(userId, false);
		researcher = user.getResearcher();
		
		if( researcher == null || !researcher.getUser().getId().equals(userId) || !researcher.getUser().hasRole(IrRole.RESEARCHER_ROLE) )
		{
			// user cannot move file if they are not a researcher or messing with someone else's researcher page
	    	return "accessDenied";
		}
	    researcherId = researcher.getId();

		// folders to move
		List<Long> listFolderIds = new LinkedList<Long>();
		for( Long id : folderIds)
		{
		    listFolderIds.add(id);
		}
		foldersToMove = researcherFileSystemService.getFolders(researcherId, listFolderIds);
		
		
		// files to move
		List<Long> listFileIds = new LinkedList<Long>();
		for( Long id : fileIds)
		{
			    listFileIds.add(id);
		}
		filesToMove = researcherFileSystemService.getFiles(researcherId, listFileIds);
		
		//links to move
		List<Long> listLinkIds = new LinkedList<Long>();
		for( Long id : linkIds)
		{
			listLinkIds.add(id);
		}
		linksToMove = researcherFileSystemService.getResearcherLinks(researcherId, listLinkIds);
		
		
		// publications to move
		List<Long> listPublicationIds = new LinkedList<Long>();
		for( Long id : publicationIds)
		{
		    listPublicationIds.add(id);
		}
		publicationsToMove = researcherFileSystemService.getResearcherPublications(researcherId, listPublicationIds);
		
		
		// items to move
		List<Long> listItemIds = new LinkedList<Long>();
		for( Long id : itemIds)
		{
			log.debug(" adding item id " + id);
			listItemIds.add(id);
		}
		
		itemsToMove = researcherFileSystemService.getResearcherInstitutionalItems(researcherId, listItemIds);
		log.debug("items size = " + itemsToMove.size() );
		for( ResearcherInstitutionalItem i : itemsToMove )
		{
			log.debug("Found item " + i);
		}
		
		if( !destinationId.equals(ResearcherFileSystemService.USE_RESEARCHER_AS_ROOT))
		{
		    destination = 
		    	researcherFileSystemService.getResearcherFolder(destinationId, false);
		    
		    if( !destination.getResearcher().getUser().getId().equals(userId))
		    {
		    	// user cannot move file into a destination that they do not own
		    	return "accessDenied";
		    }
		    
		    // make sure the user has not navigated into a child or itself- this is illegal
		    for(ResearcherFolder folder: foldersToMove)
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
		    
		    destinationPath = researcherFileSystemService.getResearcherFolderPath(destination.getId());
		    currentDestinationContents.addAll(destination.getChildren());
		    currentDestinationContents.addAll(destination.getFiles());
		    currentDestinationContents.addAll(destination.getLinks());
		    currentDestinationContents.addAll(destination.getPublications());
		    currentDestinationContents.addAll(destination.getInstitutionalItems());
		}
		else
		{
			currentDestinationContents.addAll(researcher.getRootFolders());
			currentDestinationContents.addAll(researcher.getRootFiles());
			currentDestinationContents.addAll(researcher.getRootLinks());
			currentDestinationContents.addAll(researcher.getRootPublications());
			currentDestinationContents.addAll(researcher.getRootInstitutionalItems());
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
		researcher = user.getResearcher();
		
		
		if( researcher == null || !researcher.getUser().getId().equals(userId) || !researcher.getUser().hasRole(IrRole.RESEARCHER_ROLE) )
		{
			// user cannot move file if they are not a researcher or messing with someone elses researcher page
	    	return "accessDenied";
		}
		researcherId = researcher.getId();


		List<FileSystem> notMoved;
		actionSuccess = true;

		List<Long> listFolderIds = new LinkedList<Long>();
		for( Long id : folderIds)
		{
			log.debug("adding folder id " + id);
		    listFolderIds.add(id);
		}
		
		// folders are accessed by user id so they cannot move folders that do
		// not belong to them.
		foldersToMove = researcherFileSystemService.getFolders(researcher.getId(), listFolderIds);

		List<Long> listFileIds = new LinkedList<Long>();
		for( Long id : fileIds)
		{
			log.debug("adding file id " + id);
		    listFileIds.add(id);
		}
		
		// files are accessed by user id so this prevents users from accessing files
		// that do not belong to them
		filesToMove = researcherFileSystemService.getFiles(researcherId, listFileIds);
		
		//links to move
		List<Long> listLinkIds = new LinkedList<Long>();
		for( Long id : linkIds)
		{
			listLinkIds.add(id);
		}
		linksToMove = researcherFileSystemService.getResearcherLinks(researcherId, listLinkIds);
		
		
		// publications to move
		List<Long> listPublicationIds = new LinkedList<Long>();
		for( Long id : publicationIds)
		{
		    listPublicationIds.add(id);
		}
		publicationsToMove = researcherFileSystemService.getResearcherPublications(researcherId, listPublicationIds);
		
		
		// items to move
		List<Long> listItemIds = new LinkedList<Long>();
		for( Long id : itemIds)
		{
			listItemIds.add(id);
		}
		itemsToMove = researcherFileSystemService.getResearcherInstitutionalItems(researcherId, listItemIds);
		
		log.debug( "destination id = " + destinationId);
		if( !destinationId.equals(UserFileSystemService.ROOT_FOLDER_ID))
		{
		    destination = 
		    	researcherFileSystemService.getResearcherFolder(destinationId, false);
		    if( !destination.getResearcher().getUser().getId().equals(userId))
		    {
		    	// user cannot move file into a destination that they do not own
		    	return "accessDenied";
		    }
		    
		    notMoved = 
		    	researcherFileSystemService.moveResearcherFileSystemInformation(destination, 
		    			foldersToMove, 
		    			filesToMove, 
		    			linksToMove, 
		    			itemsToMove, 
		    			publicationsToMove);
		    	
					    
		}
		else
		{
			notMoved = researcherFileSystemService.moveResearcherFileSystemInformation(researcher, 
					foldersToMove, 
	    			filesToMove, 
	    			linksToMove, 
	    			itemsToMove, 
	    			publicationsToMove);
		}
		
		if( notMoved.size() > 0 )
		{
			String message = getText("folderNamesAlreadyExist");
			actionSuccess = false;
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

	public void setFolderIds(Long[] folderIds) {
		this.folderIds = folderIds;
	}


	public void setFileIds(Long[] fileIds) {
		this.fileIds = fileIds;
	}


	public void setFoldersToMove(List<ResearcherFolder> foldersToMove) {
		this.foldersToMove = foldersToMove;
	}


	public void setDestinationId(Long destinationId) {
		this.destinationId = destinationId;
	}

	public List<ResearcherFolder> getDestinationPath() {
		return destinationPath;
	}


	public List<FileSystem> getCurrentDestinationContents() {
		return currentDestinationContents;
	}

	public boolean getActionSuccess() {
		return actionSuccess;
	}

	public Long getDestinationId() {
		return destinationId;
	}

	public ResearcherFolder getDestination() {
		return destination;
	}

	public Long getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	public List<ResearcherFile> getFilesToMove() {
		return filesToMove;
	}

	public List<ResearcherFolder> getFoldersToMove() {
		return foldersToMove;
	}


	public void setItemIds(Long[] institutionalItemIds) {
		this.itemIds = institutionalItemIds;
	}

	public void setLinkIds(Long[] linkIds) {
		this.linkIds = linkIds;
	}

	public void setPublicationIds(Long[] publicationIds) {
		this.publicationIds = publicationIds;
	}

	public void setResearcherFileSystemService(
			ResearcherFileSystemService researcherFileSystemService) {
		this.researcherFileSystemService = researcherFileSystemService;
	}

	public Long[] getItemIds() {
		return itemIds;
	}

	public List<ResearcherPublication> getPublicationsToMove() {
		return publicationsToMove;
	}

	public List<ResearcherInstitutionalItem> getItemsToMove() {
		return itemsToMove;
	}

	public void setLinksToMove(List<ResearcherLink> linksToMove) {
		this.linksToMove = linksToMove;
	}

	public void injectUserId(Long userId) {
		this.userId = userId;
	}

	public List<ResearcherLink> getLinksToMove() {
		return linksToMove;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Long getResearcherId() {
		return researcherId;
	}

	public void setResearcherId(Long researcherId) {
		this.researcherId = researcherId;
	}
	
}