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

package edu.ur.ir.groupspace;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.user.IrUser;
import edu.ur.order.OrderType;

/**
 * Service to help manage group spaces.
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupWorkspaceService extends Serializable{
	
	/**
	 * Save the group space to the system.
	 * 
	 * @param groupWorkspace - group space to add to the system.
	 * @throws DuplicateNameException - if the group space already exists 
	 */
	public void save(GroupWorkspace groupWorkspace);
	
	/**
	 * Get a group workspace by it's name.
	 * 
	 * @param name - name of the group workspace
	 * @return - the group workspace if found otherwise null
	 */
	public GroupWorkspace get(String name);
	
	
    /**
     * Delete the group space from the system.
     * 
     * @param groupWorkspace  - workspace to delete
     * @param user - user performing the delete
     * 
     * @throws PermissionNotGrantedException - if the user does not have permission to delete the 
     * workpsace.
     */
    public void delete(GroupWorkspace groupWorkspace, IrUser user) throws PermissionNotGrantedException;
    
    /**
     * Get a count of the group spaces in the system.
     * 
     * @return - count of group spaces in the system
     */
    public Long getCount();
    
    /**
     * Get a count of the group spaces in the system that a particular user belongs to.
     * 
     * @return - count of group spaces in the system the user belongs to
     */
    public Long getCount(Long userId);
    
    /**
     * Get the group space based on id.
     * 
     * @param id - id of the group space
     * @param lock - lock the data
     * 
     * @return - upgrade the lock
     */
    public GroupWorkspace get(Long id, boolean lock);
    
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
	public List<GroupWorkspace> getGroupWorkspacesNameOrder(int rowStart, int numberOfResultsToShow, OrderType orderType);
	
	
	/**
	 * Get all group workspaces for a given user - this includes groups they own or belong to a group within the workspace.
	 * 
	 * @param userId - id of the user to get the group workspaces for
	 * 
	 * @return - list of all groups the user belongs to.
	 */
	public List<GroupWorkspace> getGroupWorkspacesForUser(Long userId, int rowStart, int numberOfResultsToShow, OrderType orderType);
	
    /**
     * Get the group workspace user for the given user id and group workspace id.
     * 
     * @param userId - user id
     * @param groupWorkspaceId - group workspace id
     * 
     * @return the group workspace user or null if the group workspace user is not found.
     */
    public GroupWorkspaceUser getGroupWorkspaceUser(Long userId, Long groupWorkspaceId);
	
	
	/**
	 * Determine if a user is a group workspace member.
	 * 
	 * @param userId - id of the user
	 * @param groupWorkspaceId - group workspace id
	 * 
	 * @return true if the user is a group workspace member.
	 */
	public boolean userIsGroupWorkspaceMember(Long userId, Long groupWorkspaceId);
	
	/**
	 * Remove the user from the group.  This will also remove all permissions for the user
	 * to the group.
	 * 
	 * @param user - user to remove the group from
	 * @param groupWorkspace - group workspace to remove the user from
	 */
	public void removeUserFromGroup(IrUser user, GroupWorkspace groupWorkspace);
	
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
	    throws DuplicateNameException;
	
	
	/**
	 * Change the user permissions for the group workspace.
	 * 
	 * @param user - user to change the permissions for
	 * @param groupWorkspace - the group workspace to change the permissions on
	 * @param permissions - set of permissions to give - all other permissions will be removed if the
	 * user has other permissions - empty set will remove all permissions for the user
	 * @param applyToChildren  - apply the permission changes to all child files and folders for the user.
	 * 
	 * @throws UserHasParentFolderPermissionsException - if the user has owner or edit privileges on the group workspace
	 */
	public void changeUserPermissions(IrUser user, GroupWorkspace groupWorkspace,
			Set<IrClassTypePermission> permissions, boolean applyToChildren) throws UserHasParentFolderPermissionsException;

}
