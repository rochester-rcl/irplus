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

package edu.ur.dspace.export;

import edu.ur.dspace.model.DspaceUser;

/**
 * Determines if the user is an administrator.
 * 
 * @author Nathan Sarr
 *
 */
public interface DspaceAdminDeterminer {
	
	
	/**
	 * Returns true if the user is a dspace admin
	 * 
	 * @param dspaceUser
	 * @return
	 */
	public boolean isAdmin(DspaceUser dspaceUser);

}
