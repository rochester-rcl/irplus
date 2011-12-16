package edu.ur.ir.web.action.groupspace;

import java.util.HashSet;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.groupspace.UserHasParentFolderPermissionsException;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserAccessControlEntry;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action to manage user permissions for a user group.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageGroupWorksapceUserPermissions  extends ActionSupport 
implements  UserIdAware{
	
	/*Eclipse generated id*/
	private static final long serialVersionUID = -1350616269344275109L;

	/* id of the user accessing the information */
	private Long userId;
	
	/* id of the user to edit permissions for */
	private Long editUserPermissionsId;

	/* user to edit permissions on */
	private IrUser editUser;	

	/* id of the group workspace folder */
	private Long groupWorkspaceId;

	/* the group workspace */
	private GroupWorkspace groupWorkspace;

	/* security information */
	private SecurityService securityService;

	/* service to deal with user information */
	private UserService userService;
	
	/* service to get group workspace information */
	private GroupWorkspaceService groupWorkspaceService;

	/* access control entry for the user */
    private IrUserAccessControlEntry editUserAcl;
    
    /* list of folder permissions */
    private String[] groupWorkspacePermissions;
    
    /* apply the permission to children */
    private boolean applyToChildren = false;

	/*  Logger for managing content types*/
	private static final Logger log = Logger.getLogger(ManageGroupWorkspaceFolderProperties.class);
	
	/**
	 * Allow a user to view and edit the permissions for a given user.
	 * 
	 * @return
	 */
	public String editUserPermissions()
	{
        IrUser user = userService.getUser(userId, false);
		
        if( user == null )
        {
        	return "accessDenied";
        }
		if( groupWorkspaceId != null )
		{
			groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		}
		if( groupWorkspace != null )
		{
			// only owners can make permission changes to folders
			GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
			if( !workspaceUser.isOwner() )
			{
				return "accessDenied";
			}
			editUser = userService.getUser(editUserPermissionsId, false);
			
			
			if( editUser == null )
			{
				return "userNotFound";
			}
			IrAcl userAcl = securityService.getAcl(groupWorkspace, editUser);
			editUserAcl = userAcl.getUserAccessControlEntryByUserId(editUser.getId());
			log.debug("editUserAcl = " + editUserAcl);
		}
		else
		{
			return "notFound";
		}
		return SUCCESS;
	}
	
	/**
	 * Save the selected permissions for the user.
	 * 
	 * @return
	 */
	public String saveUserPermissions()
	{
		boolean error = false;
        IrUser user = userService.getUser(userId, false);
		
        if( user == null )
        {
        	return "accessDenied";
        }
		if( groupWorkspaceId != null )
		{
			groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		}
		if( groupWorkspace != null )
		{
			// only owners can make permission changes to folders
			GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
			if( !workspaceUser.isOwner() )
			{
				return "accessDenied";
			}
			editUser = userService.getUser(editUserPermissionsId, false);
			
			
			if( editUser == null )
			{
				return "userNotFound";
			}
			
			log.debug("printing group workspace permissions ");
			HashSet<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
			
			if( groupWorkspacePermissions != null )
			{
			    for(String permission : groupWorkspacePermissions)
			    {
				    log.debug("permission = " + permission);
				    if(permission.equals(GroupWorkspace.GROUP_WORKSPACE_READ_PERMISSION) )
				    {
				    	IrClassTypePermission read = securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_READ_PERMISSION);
				    	if( read == null )
				    	{
				    		throw new IllegalStateException("read permission is null");
				    	}
					    permissions.add(read);
				    }
				
				    if( permission.equals(GroupWorkspace.GROUP_WORKSPACE_ADD_FILE_PERMISSION))
				    {
				    	IrClassTypePermission addFile = securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_ADD_FILE_PERMISSION);
				    	if( addFile == null )
				    	{
				    		throw new IllegalStateException("add file permission is null");
				    	}
				    	permissions.add(addFile);
				    }
				
				    if( permission.equals(GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION))
				    {
				    	IrClassTypePermission edit = securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
				    	if( edit == null )
				    	{
				    		throw new IllegalStateException("edit folder permission is null");
				    	}
				    	permissions.add(edit);
				    }
			    }
			}
			
			try {
				groupWorkspaceService.changeUserPermissions(editUser, groupWorkspace, permissions, applyToChildren);
			} catch (UserHasParentFolderPermissionsException e) {
				addFieldError("parentFolderPermissionsError", 
						"A parent folder has permissions which would override the permissions chosen please check parent folders for edit permissions");
				error = true;
			}
			IrAcl userAcl = securityService.getAcl(groupWorkspace, editUser);
			editUserAcl = userAcl.getUserAccessControlEntryByUserId(editUser.getId());
			
		}
		else
		{
			return "notFound";
		}
		
		
		if( !error )
		{
		    return SUCCESS;
		}
		else
		{
			return ERROR;
		}
	}
	
    /**
     * Get the group workspace service.
     * 
     * @param groupWorkspaceService
     */
    public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}
    
	/**
	 * Set the user id
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Get the group workspace 
	 * 
	 * @return
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}
	

	/**
	 * Set the user to edit.
	 * 
	 * @param editUserPermissionsId
	 */
	public void setEditUserPermissionsId(Long editUserPermissionsId) {
		this.editUserPermissionsId = editUserPermissionsId;
	}

	/**
	 * Set the group workspace to make permissions changes to.
	 * 
	 * @param groupWorkspaceId
	 */
	public void setGroupWorkspaceId(Long groupWorkspaceId) {
		this.groupWorkspaceId = groupWorkspaceId;
	}

	/**
	 * Set the folder permissions.
	 * 
	 * @param folderPermissions
	 */
	public void setGroupWorkspacePermissions(String[] groupWorkspacePermissions) {
		this.groupWorkspacePermissions = groupWorkspacePermissions;
	}

	/**
	 * Apply to children flag.
	 * 
	 * @param applyToChildren
	 */
	public void setApplyToChildren(boolean applyToChildren) {
		this.applyToChildren = applyToChildren;
	}

	/**
	 * Get the edit user.
	 * 
	 * @return
	 */
	public IrUser getEditUser() {
		return editUser;
	}

	/**
	 * Get the edit user access control list.
	 * 
	 * @return
	 */
	public IrUserAccessControlEntry getEditUserAcl() {
		return editUserAcl;
	}

	/**
	 * Set the security service.
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
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
	 * Get the group workspace id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceId() {
		return groupWorkspaceId;
	}
}
