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

import edu.ur.exception.DuplicateNameException;

/**
 * Service to help manage group spaces.
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupSpaceService {
	
	/**
	 * Save the group space to the system.
	 * 
	 * @param groupSpace - group space to add to the system.
	 * @throws DuplicateNameException - if the group space already exists 
	 */
	public void save(GroupSpace groupSpace) throws DuplicateNameException;
	
	
    /**
     * Delete the group space from the system.
     * 
     * @param groupSpace
     */
    public void delete(GroupSpace groupSpace);
    
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
    public GroupSpace get(Long id, boolean lock);
}
