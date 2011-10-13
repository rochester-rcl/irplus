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


package edu.ur.ir.web.action.user;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action for going to the users workspace.
 * 
 * @author Nathan Sarr
 *
 */
public class ViewWorkspace extends ActionSupport implements Preparable, 
UserIdAware {

	public static final String FOLDER_TAB = "FOLDER";
	public static final String SEARCH_TAB = "SEARCH";
	public static final String COLLECTION_TAB = "COLLECTION";
	public static final String GROUP_WORKSPACE_TAB = "GROUP_WORKSPACE";
	public static final String INBOX_TAB = "FILE_INBOX";
	
	/*  Collection data access  */
	private UserService userService;
	
	/*  Collection invite user data access  */
	private InviteUserService inviteUserService;	
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = 2267179706676467266L;
	
	/*  Logger for vierw workspace action */
	private static final Logger log = Logger.getLogger(ViewWorkspace.class);
	
	/*  User object */
	private Long userId;

	/*  Token given to an invited user */
	private String token;
	
	/* the id of the parent folder  */
	private Long parentFolderId = 0L;
	
	/* tab name to be shown */
	private String tabName = FOLDER_TAB;
	
	/* The collection that owns the item*/
	private Long parentCollectionId;
	
	/* process for setting up personal files to be indexed */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;
	
	/* service for accessing index processing types */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/* list of group workspaces */
	private List<GroupWorkspace> groupWorkspaces = new LinkedList<GroupWorkspace>();
	
	
	/* Service to help deal with group workspace information */
	private GroupWorkspaceService groupWorkspaceService;

	/* group workspace id */
	private Long groupWorkspaceId;




	/**
	 * Prepare the repository.
	 * 
	 * 
	 * @see com.opensymphony.xwork.Preparable#prepare()
	 */
	public void prepare() throws Exception{
		log.debug("prepare called");
	}
	
	/**
	 * Prepare the repository.
	 * 
	 * 
	 * @see com.opensymphony.xwork.Preparable#prepare()
	 */
	public String execute() throws Exception{
		log.debug("execute called");
		log.debug("Token = " + token);
		
		IrUser user = userService.getUser(userId, false);
		
		if( user.hasRole(IrRole.ADMIN_ROLE) ||
			user.hasRole(IrRole.COLLABORATOR_ROLE) ||
			user.hasRole(IrRole.AUTHOR_ROLE) ||
			user.hasRole(IrRole.RESEARCHER_ROLE) ||
			user.hasRole(IrRole.COLLECTION_ADMIN_ROLE))
		{
			// if the user has shared files - add them for the first time to their index
		    if (token != null) {
			    Set<SharedInboxFile> inboxFiles = inviteUserService.shareFileForUserWithToken(userId, token);
				
				for(SharedInboxFile sif : inboxFiles)
				{
					userWorkspaceIndexProcessingRecordService.save(sif.getSharedWithUser().getId(), sif, 
			    			indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
				}
		    }
		    groupWorkspaces = groupWorkspaceService.getGroupWorkspacesForUser(userId);
		    return SUCCESS;
		}
		else
		{
			// this is a basic user who does not have a workspace account
			return "basic_view";
		}
	}
	
	/**
	 * The user service to deal with user related information.
	 * 
	 * @return the user service
	 */
	public UserService getUserService() {
		return userService;
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
	 * Get the user.
	 * 
	 * @return
	 */
	public IrUser getUser() {
		return userService.getUser(userId, false);
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.web.action.UserAware#setUser(edu.ur.ir.user.IrUser)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Get Parent folder id
	 * 
	 * @return
	 */
	public Long getParentFolderId() {
		return parentFolderId;
	}

	/**
	 * Set parent folder id
	 * 
	 * @param parentFolderId
	 */
	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	/**
	 * Get the invite token 
	 * @return
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Set the invite token 
	 * 
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Get the invite service
	 * 
	 * @return
	 */
	public InviteUserService getInviteUserService() {
		return inviteUserService;
	}

	/**
	 * Set the invite service
	 * 
	 * @param inviteUserService
	 */
	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}


	/**
	 * Get the collection id of the item
	 * 
	 * @return
	 */
	public Long getParentCollectionId() {
		return parentCollectionId;
	}

	/**
	 * Set the collection id of the item
	 * 
	 * @param parentCollectionId
	 */
	public void setParentCollectionId(Long parentCollectionId) {
		this.parentCollectionId = parentCollectionId;
	}


	/**
	 * Set the user workspace index processing record service.
	 * 
	 * @param userWorkspaceIndexProcessingRecordService
	 */
	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}


	/**
	 * Set the index processing type service.
	 * 
	 * @param indexProcessingTypeService
	 */
	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}
	
	
	/**
	 * Get the tab name that should be shown in the interface.
	 * 
	 * @return tab name that should be shown
	 */
	public String getTabName() {
		return tabName;
	}

	/**
	 * Set the tab name to be shown in the interface.
	 * 
	 * @param tabName
	 */
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	
	/**
	 * Get the group workspaces to be shown in the interface.
	 * 
	 * @return the group workspaces the user owns.
	 */
	public List<GroupWorkspace> getGroupWorkspaces() {
		return groupWorkspaces;
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
}
