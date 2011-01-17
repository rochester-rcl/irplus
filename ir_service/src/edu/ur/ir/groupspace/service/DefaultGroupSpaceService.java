/**  
   Copyright 2008-2010 University of Rochester

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

import java.util.List;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceDAO;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.order.OrderType;

/**
 * Default implementation of the group space service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultGroupSpaceService implements GroupWorkspaceService {
	
	/* eclipse generated id */
	private static final long serialVersionUID = 1L;
	
	/* group space data access object  */
	private GroupWorkspaceDAO groupWorkspaceDAO;

	/**
	 * Save the group space to the system.
	 * 
	 * @param groupSpace - group space to add to the system.
	 * @throws DuplicateNameException - if the group space already exists 
	 */
	public void save(GroupWorkspace groupWorkspace) throws DuplicateNameException
	{
		GroupWorkspace other = groupWorkspaceDAO.findByUniqueName(groupWorkspace.getName());
		if( other != null && !other.getId().equals(groupWorkspace.getId()))
		{
			throw new DuplicateNameException("Duplicate name error " + groupWorkspace.getName());
		}
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
    public void delete(GroupWorkspace groupSpace)
    {
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
}
