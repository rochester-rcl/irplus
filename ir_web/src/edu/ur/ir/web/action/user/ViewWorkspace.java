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

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.RoleService;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.ir.user.UserWorkspaceIndexService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action for going to the users workspace.
 * 
 * @author Nathan Sarr
 *
 */
public class ViewWorkspace extends ActionSupport implements Preparable, 
UserIdAware {

	/**  Collection data access  */
	private UserService userService;
	
	/**  Collection invite user data access  */
	private InviteUserService inviteUserService;	
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = 2267179706676467266L;
	
	/**  Logger for vierw workspace action */
	private static final Logger log = LogManager.getLogger(ViewWorkspace.class);
	
	/**  User object */
	private Long userId;

	/**  Token given to an invited user */
	private String token;
	
	/** the id of the parent folder  */
	private Long parentFolderId = 0L;
	
	/**  Folders for the person. */
	Set<PersonalFolder> personalFolders;
	
	/**  Indicates whether to show the collection tab active. */
	private boolean showCollectionTab;
	
	/** The collection that owns the item*/
	private Long parentCollectionId;
	
	/** User index service for indexing files */
	private UserWorkspaceIndexService userWorkspaceIndexService;
	
	/** Service for dealing with user file systems */
	private UserFileSystemService userFileSystemService;

	/** Service for dealing with roles */
	private RoleService roleService;
	
	/** process for setting up personal files to be indexed */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;
	
	/** service for accessing index processing types */
	private IndexProcessingTypeService indexProcessingTypeService;

	
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
	public void setUserId(Long userId) {
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
	 * Returns true to show the collection tab as active else returns false
	 *  
	 * @return
	 */
	public boolean isShowCollectionTab() {
		return showCollectionTab;
	}

	/**
	 * Indicates whether to show the collection tab as active
	 * 
	 * @param showCollectionTab
	 */
	public void setShowCollectionTab(boolean showCollectionTab) {
		this.showCollectionTab = showCollectionTab;
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

	public UserWorkspaceIndexService getUserWorkspaceIndexService() {
		return userWorkspaceIndexService;
	}

	public void setUserWorkspaceIndexService(
			UserWorkspaceIndexService userWorkspaceIndexService) {
		this.userWorkspaceIndexService = userWorkspaceIndexService;
	}

	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public UserWorkspaceIndexProcessingRecordService getUserWorkspaceIndexProcessingRecordService() {
		return userWorkspaceIndexProcessingRecordService;
	}

	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}

	public IndexProcessingTypeService getIndexProcessingTypeService() {
		return indexProcessingTypeService;
	}

	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

}
