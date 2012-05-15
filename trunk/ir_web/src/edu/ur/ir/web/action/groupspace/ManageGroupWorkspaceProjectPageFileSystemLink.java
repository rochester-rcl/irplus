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

package edu.ur.ir.web.action.groupspace;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemLink;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolder;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Deal with managing group workspace project page file system links.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageGroupWorkspaceProjectPageFileSystemLink extends ActionSupport implements UserIdAware{
	

	/* eclipse generated id */
	private static final long serialVersionUID = 7876218279386350288L;

	/* Group workspace project page service */
	private GroupWorkspaceProjectPageService groupWorkspaceProjectPageService;
	


	/* Service for dealing with group workspace project page file system information */
	private GroupWorkspaceProjectPageFileSystemService groupWorkspaceProjectPageFileSystemService;
	
	/* the name of the link to add */
	private String linkName;
	
	/* Description of the link */
	private String linkDescription;
	
	/* id of the link  */
	private Long linkId;
	
	/* URL */
	private String linkUrl;
	
	/* parent folder of the link  */
	private Long parentFolderId;
	
	/* loaded link */
	private GroupWorkspaceProjectPageFileSystemLink link;
	
	
	/*  Logger for add researcher folder action */
	private static final Logger log = Logger.getLogger(ManageGroupWorkspaceProjectPageFileSystemLink.class);
	
	/* Group workspace project page id */
	private Long groupWorkspaceProjectPageId;

	/*  Indicates the link has been added*/
	private boolean added = false;
	
	/* Message that can be displayed to the user. */
	private String message;
	
	/* id of the user making the changes */
	private Long userId;
	
	/* service to deal with user information*/
	private UserService userService;
	
	/**
	 * Get a link by it's id
	 * @return
	 */
	public String get()
	{
		GroupWorkspaceProjectPage groupWorkspaceProjectPage = null;
		link = groupWorkspaceProjectPageFileSystemService.getLink(linkId, false);
		if( link != null )
		{
		    groupWorkspaceProjectPage = link.getGroupWorkspaceProjectPage();
		    
		    if( groupWorkspaceProjectPage != null )
		    {
		        IrUser user = userService.getUser(userId, false);
			    GroupWorkspaceUser workspaceUser = groupWorkspaceProjectPage.getGroupWorkspace().getUser(user);
			    if( workspaceUser == null || !workspaceUser.isOwner())
			    {
				    return "accessDenied";
			    }
		    }
		}
		
		return SUCCESS;
	}
	
	/**
	 * Create the new link
	 */
	public String save()throws Exception
	{
		log.debug("creating a group workspace project page file system link in parent folderId = " + parentFolderId);
		GroupWorkspaceProjectPage projectPage = groupWorkspaceProjectPageService.getById(groupWorkspaceProjectPageId, false);
        IrUser user = userService.getUser(userId, false);
	    
	    
		if( projectPage != null )
	    {
			GroupWorkspaceUser workspaceUser = projectPage.getGroupWorkspace().getUser(user);
		    if( workspaceUser == null || !workspaceUser.isOwner())
		    {
			    return "accessDenied";
		    }
		    
		    added = false;
			// assume that if the current folder id is null or equal to 0
			// then we are adding a root folder to the user.
			if(parentFolderId == null || parentFolderId == 0)
			{
				 if( projectPage.getRootLink(linkName) == null )
			     {
					 GroupWorkspaceProjectPageFileSystemLink link = projectPage.createRootLink(linkUrl, linkName, linkDescription);
					 groupWorkspaceProjectPageFileSystemService.save(link);
					 added = true;
		         } 
			}
			else
			{
				GroupWorkspaceProjectPageFolder folder = groupWorkspaceProjectPageFileSystemService.getFolder(parentFolderId, true);
				
				if(!folder.getGroupWorkspaceProjectPage().getId().equals(groupWorkspaceProjectPageId))
				{
					return "accessDenied";
				}
				
				GroupWorkspaceProjectPageFileSystemLink link = folder.getLink(linkName);
				
				if( link == null )
			    {
				    folder.createLink(linkName, linkUrl, linkDescription);
				    groupWorkspaceProjectPageFileSystemService.save(folder);
				    added = true;

				}
			}
			
			if (!added) {
				message = getText("researcherLinkNameAlreadyExists", new String[]{linkName});
			}
	    }
        return SUCCESS;
	}
	
	/**
	 * Update a link with the given information.
	 * 
	 * @return success if the link is updated.
	 * 
	 */
	public String updateLink()
	{
		log.debug("updating a group workspace project page folder parent folderId = " + parentFolderId);
		added = false;

		GroupWorkspaceProjectPage projectPage = groupWorkspaceProjectPageService.getById(groupWorkspaceProjectPageId, false);
		
		IrUser user = userService.getUser(userId, false);
		GroupWorkspaceUser workspaceUser = projectPage.getGroupWorkspace().getUser(user);
		if( workspaceUser == null || !workspaceUser.isOwner())
		{
		    return "accessDenied";
		}
		 
		GroupWorkspaceProjectPageFileSystemLink other = null;
		
		// check the name.  This makes sure that 
		// if the name has been changed, it does not conflict
		// with a folder already in the folder system.
		if( parentFolderId == null || parentFolderId == 0)
		{
			other = projectPage.getRootLink(linkName);
		}
		else
		{
			GroupWorkspaceProjectPageFolder folder = groupWorkspaceProjectPageFileSystemService.getFolder(parentFolderId, true);
			
			if(!folder.getGroupWorkspaceProjectPage().getId().equals(groupWorkspaceProjectPageId))
			{
				return "accessDenied";
			}
			other = folder.getLink(linkName);

		}
		
		// name has been changed and does not conflict
		if( other == null)
		{
			GroupWorkspaceProjectPageFileSystemLink existingLink = groupWorkspaceProjectPageFileSystemService.getLink(linkId, true);
			if(!existingLink.getGroupWorkspaceProjectPage().getId().equals(groupWorkspaceProjectPageId))
			{
				return "accessDenied";
			}
			existingLink.setName(linkName);
			existingLink.setDescription(linkDescription);
			existingLink.setUrl(linkUrl);
			groupWorkspaceProjectPageFileSystemService.save(existingLink);
			added = true;
		}
		// name has not been changed
		else if(other.getId().equals(linkId))
		{
			other.setDescription(linkDescription);
			other.setUrl(linkUrl);
			groupWorkspaceProjectPageFileSystemService.save(other);
			added = true;
		}

		if( !added) 
		{
			message = getText("researcherLinkAlreadyExists", new String[]{linkName});
		}
		
			
	    return SUCCESS;
		
	}

	/**
	 * Current folder the user is looking at.
	 * 
	 * @return
	 */
	public Long getParentFolderId() {
		return parentFolderId;
	}

	/**
	 * The current folder the user is looking at.
	 * 
	 * @param currentFolderId
	 */
	public void setParentFolderId(Long currentFolderId) {
		this.parentFolderId = currentFolderId;
	}

	
	/**
	 * Indicates if the folder has been added 
	 * 
	 * @return
	 */
	public boolean isAdded() {
		return added;
	}

	/**
	 * Get the folder added message.
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Description of the folder.
	 * 
	 * @return
	 */
	public String getFolderDescription() {
		return linkDescription;
	}

	/**
	 * Description of the folder.
	 * 
	 * @param linkDescription
	 */
	public void setFolderDescription(String linkDescription) {
		this.linkDescription = linkDescription;
	}

	/**
	 * Get the link url set by the suer
	 * 
	 * @return
	 */
	public String getLinkUrl() {
		return linkUrl;
	}

	
	/**
	 * Sets the link url for the new link.
	 * 
	 * @param linkUrl
	 */
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	/**
	 * Name to use for the link.
	 * 
	 * @return
	 */
	public String getLinkName() {
		return linkName;
	}

	/**
	 * Set the name to use for the link.
	 * 
	 * @param linkName
	 */
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	/**
	 * Get the description for the link.
	 * 
	 * @return
	 */
	public String getLinkDescription() {
		return linkDescription;
	}

	/**
	 * Set the link description.
	 * 
	 * @param linkDescription
	 */
	public void setLinkDescription(String linkDescription) {
		this.linkDescription = linkDescription;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Get the link id.
	 * 
	 * @return
	 */
	public Long getLinkId() {
		return linkId;
	}

	/**
	 * Set the link id.
	 * 
	 * @param linkId
	 */
	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}

	/**
	 * Get the group workspace project id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceProjectPageId() {
		return groupWorkspaceProjectPageId;
	}

	/**
	 * Set the group workspace project id.
	 * 
	 * @param groupWorkspaceProjectPageId
	 */
	public void setGroupWorkspaceProjectPageId(Long groupWorkspaceProjectPageId) {
		this.groupWorkspaceProjectPageId = groupWorkspaceProjectPageId;
	}

	/**
	 * Get the created file system link.
	 * 
	 * @return
	 */
	public GroupWorkspaceProjectPageFileSystemLink getLink() {
		return link;
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
	 * Set the group workspace project page file system service.
	 * 
	 * @param groupWorkspaceProjectPageFileSystemService
	 */
	public void setGroupWorkspaceProjectPageFileSystemService(
			GroupWorkspaceProjectPageFileSystemService groupWorkspaceProjectPageFileSystemService) {
		this.groupWorkspaceProjectPageFileSystemService = groupWorkspaceProjectPageFileSystemService;
	}

	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
