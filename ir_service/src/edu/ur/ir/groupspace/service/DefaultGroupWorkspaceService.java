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
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceDAO;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.groupspace.GroupWorkspaceUserDAO;
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
     */
    public void delete(GroupWorkspace groupSpace, IrUser user)
    {
    	// delete all files within the group workspace
        deleteRootFiles(groupSpace, user);
    	groupWorkspaceDAO.makeTransient(groupSpace);
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
	 * Set the group workspace user data access object.
	 * 
	 * @param groupWorkspaceUserDAO
	 */
	public void setGroupWorkspaceUserDAO(GroupWorkspaceUserDAO groupWorkspaceUserDAO) {
		this.groupWorkspaceUserDAO = groupWorkspaceUserDAO;
	}
}
