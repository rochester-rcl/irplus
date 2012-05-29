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

package edu.ur.ir.web.action.groupspace;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.FileSystem;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFile;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemLink;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolder;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageInstitutionalItem;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPagePublication;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageService;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.action.user.FileSystemSortHelper;

/**
 * Manage the group workspace project page file system.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageGroupWorkspaceProjectPageFileSystem extends ActionSupport implements  UserIdAware, Preparable{

	// eclipse generated id
	private static final long serialVersionUID = -3552738672697925437L;

	/* id of the user accessing the information */
	private Long userId;
	
	/* id of the group workspace project page to access */
	private Long groupWorkspaceProjectPageId;

	/* service to deal with group workspace project page information */
	private GroupWorkspaceProjectPageService groupWorkspaceProjectPageService;
	
	/* group workspace project page file system service information */
	private GroupWorkspaceProjectPageFileSystemService groupWorkspaceProjectPageFileSystemService;
	
	/* group workspace service information */
	private GroupWorkspaceService groupWorkspaceService;
	


	/* Id of the group workspace */
	private Long groupWorkspaceId;

	/* A collection of folders and files for a user in a given location of */
    private List<FileSystem> fileSystem;

	/* user service for dealing with user information */
	private UserService userService;

	/* group workspace project page */
	private GroupWorkspaceProjectPage groupWorkspaceProjectPage;

	/*  Logger. */
	private static final Logger log = Logger.getLogger(ManageGroupWorkspaceProjectPage.class);
	
	/* The folder that owns the listed files and folders */
	private Long parentFolderId = 0l;

	/* set of folders that are the path for the current folder */
    private Collection <GroupWorkspaceProjectPageFolder> folderPath;
    
	/*  type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/*  name of the element to sort on 
	 *   this is for incoming requests */
	private String sortElement = "name";
	
	/* use the type sort this is information for the page */
	private String folderTypeSort = "none";
	
	/* use the name sort this is information for the page */
	private String folderNameSort = "none";

	/* list of folder ids to perform actions on*/
	private Long[] folderIds;

	/* list of file ids to perform actions on*/
	private Long[] fileIds;
	
	/* list of publication ids to perform actions on*/
	private Long[] publicationIds;
	
	/* list of link ids to perform actions on*/
	private Long[] linkIds;
	
	/* List of institutional item ids to perform action on */
	private Long[] itemIds;
	


	public String execute()
	{
		
		if( groupWorkspaceProjectPage != null )
		{
			IrUser user = userService.getUser(userId, false);
			GroupWorkspaceUser workspaceUser = groupWorkspaceProjectPage.getGroupWorkspace().getUser(user);
			if( workspaceUser != null && workspaceUser.isOwner())
			{
				createFileSystem();
		        return SUCCESS;
			}
			else
			{
				return "accessDenied";
			}
		}
		
		else
		{
			return "notFound";
		}
	}
	
	/**
	 * Get the group workspace table
	 * 
	 * @return
	 */
	public String getTable()
	{
		log.debug("getTableCalled");
		
		
		if( groupWorkspaceProjectPage != null )
		{
			IrUser user = userService.getUser(userId, false);
			GroupWorkspaceUser workspaceUser = groupWorkspaceProjectPage.getGroupWorkspace().getUser(user);
			if( workspaceUser != null && workspaceUser.isOwner())
			{
		       createFileSystem();
		       return SUCCESS;
		    }
		    else
		    {
			    return "accessDenied";
		    }
		}
		
		return "notFound";
		
	}
	
	/**
	 * Removes the select files, publications, links and folders.
	 * 
	 * @return
	 */
	public String deleteFileSystemObjects()
	{
		log.debug("Delete folders called");
		if( groupWorkspaceProjectPage == null )
		{
			return "notFound";
		}
		else if( groupWorkspaceProjectPage != null )
		{
			IrUser user = userService.getUser(userId, false);
			GroupWorkspaceUser workspaceUser = groupWorkspaceProjectPage.getGroupWorkspace().getUser(user);
			if( workspaceUser == null || !workspaceUser.isOwner())
			{
				return "accessDenied";
			}
		}
		
		
		
		if( folderIds != null )
		{
		    for(int index = 0; index < folderIds.length; index++)
		    {
			    log.debug("Deleting folder with id " + folderIds[index]);
			    GroupWorkspaceProjectPageFolder pf = groupWorkspaceProjectPageFileSystemService.getFolder(folderIds[index], false);
			    
			    if( !pf.getGroupWorkspaceProjectPage().equals(groupWorkspaceProjectPage))
			    {
			    	return "accessDenied";
			    }

			    groupWorkspaceProjectPageFileSystemService.delete(pf);
		    }
		}
		
		if(fileIds != null)
		{
			for(int index = 0; index < fileIds.length; index++)
			{
				log.debug("Deleting file with id " + fileIds[index]);
				GroupWorkspaceProjectPageFile rf = groupWorkspaceProjectPageFileSystemService.getFile( fileIds[index], false);
				
				if( !rf.getGroupWorkspaceProjectPage().equals(groupWorkspaceProjectPage))
				{
					return "accessDenied";
				}
				
				groupWorkspaceProjectPageFileSystemService.delete(rf);
			}
		}
		
		if(publicationIds != null)
		{
			for(int index = 0; index < publicationIds.length; index++)
			{
				log.debug("Deleting publication with id " + publicationIds[index]);
				GroupWorkspaceProjectPagePublication rp = groupWorkspaceProjectPageFileSystemService.getPublication( publicationIds[index], false);
				if( !rp.getGroupWorkspaceProjectPage().equals(groupWorkspaceProjectPage))
				{
					return "accessDenied";
				}
				groupWorkspaceProjectPageFileSystemService.delete(rp);
			}
		}
		
		if(itemIds != null)
		{
			for(int index = 0; index < itemIds.length; index++)
			{
				log.debug("Deleting Institutional Item with id " + itemIds[index]);
				GroupWorkspaceProjectPageInstitutionalItem ri = groupWorkspaceProjectPageFileSystemService.getInstitutionalItem(itemIds[index], false);
				if( !ri.getGroupWorkspaceProjectPage().equals(groupWorkspaceProjectPage))
				{
					return "accessDenied";
				}
				groupWorkspaceProjectPageFileSystemService.delete(ri);
			}
		}		
		
		if(linkIds != null)
		{
			for(int index = 0; index < linkIds.length; index++)
			{
				log.debug("Deleting link with id " + linkIds[index]);
				GroupWorkspaceProjectPageFileSystemLink rl = groupWorkspaceProjectPageFileSystemService.getLink( linkIds[index], false);
				if( !rl.getGroupWorkspaceProjectPage().equals(groupWorkspaceProjectPage))
				{
					return "accessDenied";
				}
				groupWorkspaceProjectPageFileSystemService.delete(rl);
			}
		}
		
		createFileSystem();
		return SUCCESS;
	}
	
	/**
	 * Create the researcher file system to view.
	 */
	private void createFileSystem()
	{
		
		
		if(parentFolderId != null && parentFolderId > 0)
		{
		    folderPath = groupWorkspaceProjectPageFileSystemService.getFolderPath(parentFolderId);
		}
		
		log.debug("Folder Path ::" + folderPath);
		log.debug("Parent Folder Id ::" + parentFolderId);
		
		Collection<GroupWorkspaceProjectPageFolder> folders = groupWorkspaceProjectPageFileSystemService.getFolders(groupWorkspaceProjectPageId, parentFolderId);
		Collection<GroupWorkspaceProjectPageFile> files = groupWorkspaceProjectPageFileSystemService.getFiles(groupWorkspaceProjectPageId, parentFolderId);
		Collection<GroupWorkspaceProjectPagePublication> publications = groupWorkspaceProjectPageFileSystemService.getPublications(groupWorkspaceProjectPageId, parentFolderId);
		Collection<GroupWorkspaceProjectPageFileSystemLink> links = groupWorkspaceProjectPageFileSystemService.getLinks(groupWorkspaceProjectPageId, parentFolderId);
		Collection<GroupWorkspaceProjectPageInstitutionalItem> institutionalItems = groupWorkspaceProjectPageFileSystemService.getInstitutionalItems(groupWorkspaceProjectPageId, parentFolderId);
		
	    fileSystem = new LinkedList<FileSystem>();

	    fileSystem.addAll(files);
	    fileSystem.addAll(folders);
	    fileSystem.addAll(publications);
	    fileSystem.addAll(links);
	    fileSystem.addAll(institutionalItems);

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
	    

	}
	
	
	
	public void prepare() throws Exception {
		log.debug("Prepare called id = " + groupWorkspaceProjectPageId);
		if( groupWorkspaceProjectPageId != null )
		{
		    groupWorkspaceProjectPage = groupWorkspaceProjectPageService.getById(groupWorkspaceProjectPageId, false);
		    log.debug("Group workspace project page found value  = " + groupWorkspaceProjectPage);
		}
		else if( groupWorkspaceId != null )
		{
			GroupWorkspace groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
			if( groupWorkspace != null )
			{
			  groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
			  groupWorkspaceProjectPageId = groupWorkspaceProjectPage.getId();
			}
		}
	}

	
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Get the group workspace project page id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceProjectPageId() {
		return groupWorkspaceProjectPageId;
	}

	/**
	 * Set the group workspace project page id.
	 * 
	 * @param groupWorkspaceProjectPageId
	 */
	public void setGroupWorkspaceProjectPageId(Long groupWorkspaceProjectPageId) {
		this.groupWorkspaceProjectPageId = groupWorkspaceProjectPageId;
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
	 * Set the user service
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Set the group workspace project page
	 * 
	 * @param groupWorkspaceProjectPage
	 */
	public void setGroupWorkspaceProjectPage(
			GroupWorkspaceProjectPage groupWorkspaceProjectPage) {
		this.groupWorkspaceProjectPage = groupWorkspaceProjectPage;
	}
	
	/**
	 * Get the group workspace project page.
	 * 
	 * @return
	 */
	public GroupWorkspaceProjectPage getGroupWorkspaceProjectPage() {
		return groupWorkspaceProjectPage;
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
	 * Get the folder path.
	 * 
	 * @return
	 */
	public Collection<GroupWorkspaceProjectPageFolder> getFolderPath() {
		return folderPath;
	}

	/**
	 * Set the group workspace project page file system.
	 * 
	 * @param groupWorkspaceProjectPageFileSystemService
	 */
	public void setGroupWorkspaceProjectPageFileSystemService(
			GroupWorkspaceProjectPageFileSystemService groupWorkspaceProjectPageFileSystemService) {
		this.groupWorkspaceProjectPageFileSystemService = groupWorkspaceProjectPageFileSystemService;
	}
	
	/**
	 * Get the sort type.
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
	 * Get the file system.
	 * 
	 * @return
	 */
	public List<FileSystem> getFileSystem() {
		return fileSystem;
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
	 * @param folderTypeSort
	 */
	public void setFolderTypeSort(String folderTypeSort) {
		this.folderTypeSort = folderTypeSort;
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
	 * @param folderNameSort
	 */
	public void setFolderNameSort(String folderNameSort) {
		this.folderNameSort = folderNameSort;
	}
	
	
	public Long[] getFolderIds() {
		return folderIds;
	}

	public void setFolderIds(Long[] folderIds) {
		this.folderIds = folderIds;
	}

	public Long[] getFileIds() {
		return fileIds;
	}

	public void setFileIds(Long[] fileIds) {
		this.fileIds = fileIds;
	}

	public Long[] getPublicationIds() {
		return publicationIds;
	}

	public void setPublicationIds(Long[] publicationIds) {
		this.publicationIds = publicationIds;
	}

	public Long[] getLinkIds() {
		return linkIds;
	}

	public void setLinkIds(Long[] linkIds) {
		this.linkIds = linkIds;
	}
	
	public Long[] getItemIds() {
		return itemIds;
	}

	public void setItemIds(Long[] itemIds) {
		this.itemIds = itemIds;
	}
	
    public Long getGroupWorkspaceId() {
		return groupWorkspaceId;
	}

	public void setGroupWorkspaceId(Long groupWorkspaceId) {
		this.groupWorkspaceId = groupWorkspaceId;
	}
	
	public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}
}
