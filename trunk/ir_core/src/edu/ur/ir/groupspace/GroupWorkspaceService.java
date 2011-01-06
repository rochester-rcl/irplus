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

package edu.ur.ir.groupspace;

import java.io.Serializable;
import java.util.List;

import edu.ur.exception.DuplicateNameException;
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
	public void save(GroupWorkspace groupWorkspace) throws DuplicateNameException;
	
	
    /**
     * Delete the group space from the system.
     * 
     * @param groupWorkspace
     */
    public void delete(GroupWorkspace groupWorkspace);
    
    /**
     * Get a count of the group spaces in the system.
     * 
     * @return - count of group spaces in the system
     */
    public Long getCount();
    
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

}
