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
 * This indicates if a versioned file can be locked
 * by a particular user.
 * 
 * @author Nathan Sarr
 *
 */
public interface VersionedFileLockStrategy {
	
	/**
	 * Returns true if the user can lock the file.
	 * 
	 * @return true if the user can lock the specified version file
	 */
	public boolean canLockFile(VersionedFile file, IrUser user);

}
