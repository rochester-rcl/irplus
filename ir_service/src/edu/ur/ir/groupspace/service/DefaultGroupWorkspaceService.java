/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.ir.groupspace.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ur.cgLib.CgLibHelper;
import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceDAO;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.groupspace.GroupWorkspaceUserDAO;
import edu.ur.ir.groupspace.UserHasParentFolderPermissionsException;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserAccessControlEntry;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.order.OrderType;

/**
 * Default implementation of the group space service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultGroupWorkspaceService implements GroupWorkspaceService {
	
	/* eclipse generated id */
	private static final long serialVersionUID = 1L;
	
	/* group space data access object  */
	private GroupWorkspaceDAO groupWorkspaceDAO;
	
	/* group workspace user data access */
	private GroupWorkspaceUserDAO groupWorkspaceUserDAO;

	/* service to deal with group workspace file system information */
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;
	
	/* Data access for ACL */
	private SecurityService securityService;

	/*  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultGroupWorkspaceService.class);
	

	/**
	 * Save the group space to the system.
	 * 
	 * @param groupSpace - group space to add to the system.
	 * @throws DuplicateNameException - if the group space already exists 
	 */
	public void save(GroupWorkspace groupWorkspace) 
	{
		groupWorkspaceDAO.makePersistent(groupWorkspace);
	}
	
	/**
	 * Get a group workspace by it's name.
	 * 
	 * @param name - name of the group workspace
	 * @return - the group workspace if found otherwise null
	 */
	public GroupWorkspace get(String name)
	{
		return groupWorkspaceDAO.findByUniqueName(name);
	}
	
	
    /**
     * Delete the group space from the system.
     * 
     * @param groupSpace
     * @throws PermissionNotGrantedException 
     */
    public void delete(GroupWorkspace groupWorkspace, IrUser user) throws PermissionNotGrantedException
    {
    	GroupWorkspaceUser groupWorkspaceUser = groupWorkspace.getUser(user);
    	if( !user.hasRole(IrRole.ADMIN_ROLE) )
    	{
    		if( groupWorkspaceUser == null || !groupWorkspaceUser.isOwner())
    		{
    			throw new PermissionNotGrantedException("User does not have permission to delete workspace");
    		}
    	}
    	
    	// delete all files within the group workspace
        deleteRootFiles(groupWorkspace, user);
        
        List<GroupWorkspaceFolder> rootFolders = groupWorkspaceFileSystemService.getFolders(groupWorkspace.getId(), null);
       
        // delete all of the access controls for the folders.
        for(GroupWorkspaceFolder folder : rootFolders)
        {
        	groupWorkspaceFileSystemService.delete(folder, user, "delete group workspace");
        }
        
        IrAcl groupWorkspaceAcl = securityService.getAcl(groupWorkspace);
        if( groupWorkspaceAcl != null )
        {
        	securityService.deleteAcl(groupWorkspaceAcl);
        }
       
    	groupWorkspaceDAO.makeTransient(groupWorkspace);
    }
    
    /**
     * Get a count of the group spaces in the system.
     * 
     * @return - count of group spaces in the system
     */
    public Long getCount()
    {
    	return groupWorkspaceDAO.getCount();
    }
    
    /**
     * Get the group space based on id.
     * 
     * @param id - id of the group space
     * @param lock - lock the data
     * 
     * @return - upgrade the lock
     */
    public GroupWorkspace get(Long id, boolean lock)
    {
    	return groupWorkspaceDAO.getById(id, lock);
    }
    
	/**
	 * Set the group space data access object.
	 * 
	 * @param groupWorkspaceDAO
	 */
	public void setGroupWorkspaceDAO(GroupWorkspaceDAO groupWorkspaceDAO) {
		this.groupWorkspaceDAO = groupWorkspaceDAO;
	}
	
	/**
	 * Get the list of group spaces ordered by name.
	 * 
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param orderType - Order (Desc/Asc) 
	 * 
	 * @return list of group spaces found.
	 */
	public List<GroupWorkspace> getGroupWorkspacesNameOrder(int rowStart, int numberOfResultsToShow, OrderType orderType)
	{
		return groupWorkspaceDAO.getGroupWorkspacesNameOrder(rowStart, numberOfResultsToShow, orderType);
	}
	
	/**
	 * Get all group workspaces for a given user - this includes groups they own or belong to a group within the workspace.
	 * 
	 * @param userId - id of the user to get the group workspaces for
	 * 
	 * @return - list of all groups the user belongs to.
	 */
	public List<GroupWorkspace> getGroupWorkspacesForUser(Long userId)
	{
		return groupWorkspaceDAO.getGroupWorkspacesForUser(userId);
	}
	
	/**
	 * Get the group workpsace file system service.
	 * 
	 * @return the group workspace file system service.
	 */
	public GroupWorkspaceFileSystemService getGroupWorkspaceFileSystemService() {
		return groupWorkspaceFileSystemService;
	}

	/**
	 * Set the group workspace file system service.
	 * 
	 * @param groupWorkspaceFileSystemService - set the group workspace file system service.
	 */
	public void setGroupWorkspaceFileSystemService(
			GroupWorkspaceFileSystemService groupWorkspaceFileSystemService) {
		this.groupWorkspaceFileSystemService = groupWorkspaceFileSystemService;
	}
	
	/**
	 * Delete the root files for the user.
	 * 
	 * @param user
	 * @param deletingUser
	 */
	private void deleteRootFiles(GroupWorkspace workspace, IrUser deletingUser)
	{
		log.debug("Delete root files");
		// delete the users root files
		Set<GroupWorkspaceFile> files = new HashSet<GroupWorkspaceFile>();
		files.addAll(workspace.getRootFiles());
		for(GroupWorkspaceFile wf : files)
		{
			workspace.removeRootFile(wf);
			groupWorkspaceFileSystemService.delete(wf, deletingUser, "DELETING USER");
		}
		log.debug("DONE deleting root files");
	}
	
	   /**
     * Get the group workspace user for the given user id and group workspace id.
     * 
     * @param userId - user id
     * @param groupWorkspaceId - group workspace id
     * 
     * @return the group workspace user or null if the group workspace user is not found.
     */
    public GroupWorkspaceUser getGroupWorkspaceUser(Long userId, Long groupWorkspaceId)
    {
    	return groupWorkspaceUserDAO.getGroupWorkspaceUser(userId, groupWorkspaceId);
    }
	
	/**
	 * Determine if a user is a group workspace member.
	 * 
	 * @param userId - id of the user
	 * @param groupWorkspaceId - group workspace id
	 * 
	 * @return true if the user is a group workspace member.
	 */
	public boolean userIsGroupWorkspaceMember(Long userId, Long groupWorkspaceId)
	{
		boolean isMember = false;
		GroupWorkspaceUser user = this.getGroupWorkspaceUser(userId, groupWorkspaceId);
		if( user != null )
		{
			isMember = true;
		}
		return isMember;
	}
	
	/**
	 * Remove the user from the group.  This will also remove all permissions for the user
	 * to the group.
	 * 
	 * @param user - user to remove the group from
	 * @param groupWorkspace - group workspace to remove the user from
	 */
	public void removeUserFromGroup(IrUser user, GroupWorkspace groupWorkspace)
	{
		GroupWorkspaceUser removeGroupWorkspaceUser = groupWorkspace.getUser(user);
        if(removeGroupWorkspaceUser != null )
        {
        	securityService.deletePermissions(groupWorkspace.getId(), CgLibHelper.cleanClassName(groupWorkspace.getClass().getName()), user);
        	groupWorkspaceFileSystemService.removeUserPermissionsFromGroupFileSystem(user, groupWorkspace);
        	groupWorkspace.remove(removeGroupWorkspaceUser);
        	save(groupWorkspace);
        }
		
	}
	
	/**
	 * Add a user to the group workspace with the given permissions.
	 * 
	 * @param user - user to give the permissions to 
	 * @param groupWorkspace - workspace to give the permissions to
	 * @param permissions - list of permissions
	 * @param setAsOwner - set the user as an owner of the group workspace
	 * @throws DuplicateNameException - if the user already exists in the group.
	 */
	public GroupWorkspaceUser addUserToGroup(IrUser user, 
			GroupWorkspace groupWorkspace, Set<IrClassTypePermission> permissions, boolean setAsOwner)
	    throws DuplicateNameException
	{
		GroupWorkspaceUser workspaceUser = groupWorkspace.add(user, setAsOwner);
		groupWorkspaceUserDAO.makePersistent(workspaceUser);
	    // Create permissions for the group that is being shared
		securityService.createPermissions(groupWorkspace, user, permissions);
		List<IrClassTypePermission> fileSystemPermissions = new LinkedList<IrClassTypePermission>();
		List<IrUser> users = new LinkedList<IrUser>();
		users.add(user);
		
		if( setAsOwner)
		{
			fileSystemPermissions.addAll(securityService.getClassTypePermissions(VersionedFile.class.getName()));
			fileSystemPermissions.addAll(securityService.getClassTypePermissions(GroupWorkspaceFolder.class.getName()));
		}
		else
		{
			boolean workspaceEdit = false;
			boolean workspaceRead = false;
			boolean workspaceAddFile = false;
			
		    for(IrClassTypePermission permission : permissions)
		    {
			    if( permission.getName().equals(GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION))
			    {
				    workspaceEdit = true;
			    }			   
			    else if( permission.getName().equals(GroupWorkspace.GROUP_WORKSPACE_READ_PERMISSION) )
			    {
				    workspaceRead = true;
			    }
			    else if( permission.getName().equals(GroupWorkspace.GROUP_WORKSPACE_ADD_FILE_PERMISSION) )
			    {
			        workspaceAddFile = true;	
			    }
		    }
		    
		    if( workspaceEdit )
		    {
		    	fileSystemPermissions.addAll(securityService.getClassTypePermissions(VersionedFile.class.getName()));
		    	fileSystemPermissions.addAll(securityService.getClassTypePermissions(GroupWorkspaceFolder.class.getName()));
		    }
		    else if ( workspaceAddFile )
		    {
		    	fileSystemPermissions.add(securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_READ_PERMISSION));
		    	fileSystemPermissions.add(securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION));
		    	fileSystemPermissions.add(securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.VIEW_PERMISSION));	
		    }
		    else if( workspaceRead )
		    {
		    	fileSystemPermissions.add(securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_READ_PERMISSION));
		    	fileSystemPermissions.add(securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.VIEW_PERMISSION));
		    }
		}
		
		
		groupWorkspaceFileSystemService.giveUsersPermissionsToGroupFileSystem(users, groupWorkspace, fileSystemPermissions);
		return workspaceUser;
	}
	
	/**
	 * Set the group workspace user data access object.
	 * 
	 * @param groupWorkspaceUserDAO
	 */
	public void setGroupWorkspaceUserDAO(GroupWorkspaceUserDAO groupWorkspaceUserDAO) {
		this.groupWorkspaceUserDAO = groupWorkspaceUserDAO;
	}
	
	/**
	 * Get the security service.
	 * 
	 * @return
	 */
	public SecurityService getSecurityService() {
		return securityService;
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
	 * Change the user permissions for the group workspace.
	 * 
	 * @param user - user to change the permissions for
	 * @param groupWorkspace - the group workspace to change the permissions on
	 * @param permissions - set of permissions to give - all other permissions will be removed if the
	 * user has other permissions - empty set will remove all permissions for the user
	 * 
	 * @param applyToChildren  - apply the permission changes to all child files and folders for the user.
	 * 
	 * @throws UserHasParentFolderPermissionsException - if user is owner or has edit privileges on the group workspace
	 */
	public void changeUserPermissions(IrUser user,
			GroupWorkspace groupWorkspace,
			Set<IrClassTypePermission> permissions, boolean applyToChildren) throws UserHasParentFolderPermissionsException {
		log.debug("changing permissions for user " + user);
		
		// figure out which permissions are set
		boolean hasRead = false;
		boolean hasEdit = false;
		boolean hasAddFile = false;
		
		// get group Workspace permissions
		IrClassTypePermission groupWorkspaceReadPermission = securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_READ_PERMISSION);
		IrClassTypePermission groupWorkspaceAddFilePermission = securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_ADD_FILE_PERMISSION);
		IrClassTypePermission groupWorkspaceEditPermission = securityService.getClassTypePermission(GroupWorkspace.class.getName(), GroupWorkspace.GROUP_WORKSPACE_EDIT_PERMISSION);
		
		// get folder permissions
		IrClassTypePermission folderReadPermission = securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_READ_PERMISSION);
		IrClassTypePermission folderAddFilePermission = securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_ADD_FILE_PERMISSION);
		IrClassTypePermission folderEditPermission = securityService.getClassTypePermission(GroupWorkspaceFolder.class.getName(), GroupWorkspaceFolder.FOLDER_EDIT_PERMISSION);
		
		// get file permissions
		IrClassTypePermission fileViewPermission = securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.VIEW_PERMISSION);
		IrClassTypePermission fileEditPermission = securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.EDIT_PERMISSION);
		IrClassTypePermission fileSharePermission = securityService.getClassTypePermission(VersionedFile.class.getName(), VersionedFile.SHARE_PERMISSION);
		
		
		if( permissions != null )
		{
		    for(IrClassTypePermission permission : permissions)
		    {
			    log.debug("permission = " + permission);
			    log.debug(" compare to " + groupWorkspaceReadPermission);
			    if(permission.equals(groupWorkspaceReadPermission) )
			    {
				    hasRead = true;
			    }
			    log.debug(" compare to " + groupWorkspaceAddFilePermission);
			    if( permission.equals(groupWorkspaceAddFilePermission))
			    {
				    hasAddFile = true;
			    }
			    log.debug(" compare to " + groupWorkspaceEditPermission);
			    if( permission.equals(groupWorkspaceEditPermission))
			    {
				    hasEdit = true;
			    }
		    }
		}
		
		
		GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
		
		// this means a user is trying to remove edit permissions from a 
		// group workspace where the user is the owner
		if( !hasEdit && workspaceUser.isOwner()) 
		{
			throw new UserHasParentFolderPermissionsException("user = " + user + "child folder = " + groupWorkspace);
		}
		
		
		log.debug("hasRead " + hasRead + " has AddFile = " + hasAddFile + " hasEdit = " + hasEdit + " applytoChildren = " + applyToChildren);
		if( workspaceUser.isOwner() || hasEdit || applyToChildren)
		{
			log.debug("hasEdit = " + hasEdit + " applytoChildren = " + applyToChildren);
			List<GroupWorkspaceFile> files = groupWorkspaceFileSystemService.getAllFiles(groupWorkspace.getId());
			List<GroupWorkspaceFolder> folders = groupWorkspaceFileSystemService.getAllFolders(groupWorkspace.getId());
			
			// deal with files
			for(GroupWorkspaceFile f : files )
			{
				IrAcl acl = securityService.getAcl(f.getVersionedFile());
				if( acl == null )
				{
					acl = securityService.createAclForObject(f.getVersionedFile());
				}
				
				IrUserAccessControlEntry uace = acl.getUserAccessControlEntry(user.getId());
				if( uace == null )
				{
					uace = acl.createUserAccessControlEntry(user);
				}
				
				if( hasEdit )
				{
					log.debug("adding edit and share permission to file " + f);
				    uace.addPermission(fileEditPermission); 
				    uace.addPermission(fileSharePermission);
				    hasRead = true;
				}
				else
				{
					uace.removePermission(fileEditPermission);
					uace.removePermission(fileSharePermission);
				}
				
				if( hasAddFile )
				{
					hasRead = true;
				}
				
				
				if(hasRead)
				{
					log.debug("adding read permission to file " + f);
					uace.addPermission(fileViewPermission);
				}
				else
				{
					uace.removePermission(fileViewPermission);
				}
				securityService.save(acl);
			}
			
			// deal with folders
			for( GroupWorkspaceFolder f : folders)
			{
				IrAcl acl = securityService.getAcl(f);
				if( acl == null )
				{
					acl = securityService.createAclForObject(f);
				}
				IrUserAccessControlEntry uace = acl.getUserAccessControlEntry(user.getId());
				if( uace == null )
				{
					uace = acl.createUserAccessControlEntry(user);
				}
				
				if( hasEdit )
		        {
					log.debug("adding edit permission to folder " + f);
		        	uace.addPermission(folderEditPermission);
		        	hasRead = true;
		        	hasAddFile = true;
		        }
		        else
		        {
		        	uace.removePermission(folderEditPermission);
		        }
		        
		        if( hasAddFile )
		        {
		        	log.debug("adding add file permission to folder " + f);
		        	uace.addPermission(folderAddFilePermission);
		        	hasRead = true;
		        }
		        else
		        {
		        	uace.removePermission(folderAddFilePermission);
		        }
		        
		        if( hasRead )
		        {
		        	log.debug("adding has read permission to folder " + f);
		        	uace.addPermission(folderReadPermission);
		        }
		        else
		        {
		        	uace.removePermission(folderReadPermission);
		        }
		        securityService.save(acl);
			}
		}
	    
		IrAcl userAcl = securityService.getAcl(groupWorkspace, user);
		if( userAcl == null )
		{
			userAcl = securityService.createAclForObject(groupWorkspace);
		}
		
		
		IrUserAccessControlEntry uace = userAcl.getUserAccessControlEntryByUserId(user.getId());
		if( uace == null )
		{
			uace = userAcl.createUserAccessControlEntry(user);
		}
		
		if( hasEdit )
		{
		    uace.addPermission(groupWorkspaceEditPermission);
		    hasRead = true;
		    hasAddFile = true;
		}
		else
		{
		    uace.removePermission(groupWorkspaceEditPermission);
		}
		        
		if( hasAddFile )
		{
		    uace.addPermission(groupWorkspaceAddFilePermission);
		    hasRead = true;
		}
		else
		{
		    uace.removePermission(groupWorkspaceAddFilePermission);
		}
			        
		if( hasRead )
		{
		    uace.addPermission(groupWorkspaceReadPermission);
		}
		else
		{
		    uace.removePermission(groupWorkspaceReadPermission);
		}
		securityService.save(userAcl);
	}

}
