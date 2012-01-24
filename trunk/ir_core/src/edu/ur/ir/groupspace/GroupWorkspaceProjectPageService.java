/**  
   Copyright 2008-2012 University of Rochester

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

/**
 * Service layer for group project page information
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupWorkspaceProjectPageService extends Serializable{
	
	/**
	 * Save the group workspace project page.
	 * 
	 * @param projectPage
	 */
	public void save(GroupWorkspaceProjectPage projectPage);

	/**
	 * Get the group workspace project page by i.d
	 * 
	 * @param id - id of the group workspace
	 * @param lock - upgrade the lock mode for the object.
	 * 
	 * @return the group workspace if found otherwise null
	 */
	public GroupWorkspaceProjectPage getById(Long id,  boolean lock);

}
