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

package edu.ur.ir.repository;

import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.user.IrUser;

/**
 * This allows a client to determine if the client
 * would be able to lock the software.  
 * 
 * @author Nathan Sarr
 *
 */
public interface VersionedFileUnLockStrategy {
	
	/**
	 * Returns true if the user can unlock the file.
	 * 
	 * @param file - versioned file the user would like to unlock
	 * @param user - the user who wishes to unlock the file
	 * 
	 * @return true if the user can unlock the file. 
	 */
	public boolean canUnLockFile(VersionedFile file, IrUser user);

}
