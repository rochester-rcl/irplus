/**  
   Copyright 2008 - 2011 University of Rochester

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

package edu.ur.tag.repository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.user.IrUser;

/**
 * 
 * Set of functions for Group workspaces.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceWebUtilFunctions {
	
	/**
	 * Returns true if the current user is the owner of the given group workspace
	 * 
	 * @param groupWorkspace - group workspace to check ownership
	 * 
	 * @return true if the user is one of the owners of the group
	 */
	public static boolean isCurrentUserGroupWorkspaceOwner(GroupWorkspace groupWorkspace)
	{
		boolean isOwner = false;
		IrUser user = null;
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 		
        if( auth != null) {
			 if(auth.getPrincipal() instanceof UserDetails) {
				 user = (IrUser)auth.getPrincipal();
			 }
        }
		
        if( user != null && groupWorkspace != null)
        {
        	GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
        	if( workspaceUser != null )
        	{
        		isOwner = workspaceUser.isOwner();
        	}
        }
        return isOwner;
	}
	
	/**
	 * Returns true if the current user is the owner of the given group workspace
	 * 
	 * @param groupWorkspace - group workspace to check ownership
	 * 
	 * @return true if the user is one of the owners of the group
	 */
	public static boolean isGroupWorkspaceMember(GroupWorkspace groupWorkspace)
	{
		boolean isMember = false;
		IrUser user = null;
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 		
        if( auth != null) {
			 if(auth.getPrincipal() instanceof UserDetails) {
				 user = (IrUser)auth.getPrincipal();
			 }
        }
		
        if( user != null && groupWorkspace != null)
        {
        	GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
        	if( workspaceUser != null )
        	{
        		isMember = true;
        	}
        }
        return isMember;
	}
	
	/**
	 * Returns true if the  user is the owner of the given group workspace
	 * 
	 * @param groupWorkspace - workspace to check
	 * @param user - user to check
	 * 
	 * @return true if the user is an owner of the group workspace
	 */
	public static boolean isUserGroupWorkspaceOwner(GroupWorkspace groupWorkspace, IrUser user)
	{
		boolean isOwner = false;
		
		
        if( user != null && groupWorkspace != null)
        {
        	GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
        	if( workspaceUser != null )
        	{
        		isOwner = workspaceUser.isOwner();
        	}
        }
        return isOwner;
	}


}
