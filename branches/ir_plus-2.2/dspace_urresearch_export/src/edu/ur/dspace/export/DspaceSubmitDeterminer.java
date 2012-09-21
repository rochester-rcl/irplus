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
 * Determines if a user can submit - this allows us to give them
 * authoring privileges.
 * 
 * @author Nathan Sarr
 *
 */
public interface DspaceSubmitDeterminer {
	
	/**
	 * Returns true if the user can submit to one or more 
	 * dspace collections.
	 * 
	 * @return true if the user can sbumit
	 */
	public boolean canSubmit(DspaceUser user);

}
