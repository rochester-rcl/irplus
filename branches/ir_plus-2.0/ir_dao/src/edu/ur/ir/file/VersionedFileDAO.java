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

package edu.ur.ir.file;

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.NameListDAO;
import edu.ur.dao.NonUniqueNameDAO;
import edu.ur.ir.file.VersionedFile;

/**
 * Data persistance for a versioned file.
 * 
 * @author Nathan Sarr
 *
 */
public interface VersionedFileDAO extends CountableDAO, 
CrudDAO<VersionedFile>, NameListDAO, NonUniqueNameDAO<VersionedFile>{
	
	/**
	 * Get the versioned file containing the given IrFile id as one of its version
	 * 
	 * @param irFileId file id to get the VersionedFile
	 * 
	 * @return VersionedFile containing the IrFile
	 */
	public VersionedFile getVersionedFileByIrFileId(Long irFileId);
	
	/**
	 * Get the versioned files containing the given IrFile ids
	 * 
	 * @param itemId item ids to get the VersionedFile
	 * 
	 * @return VersionedFiles containing the IrFile
	 */
	public List<VersionedFile> getVersionedFilesForItem(Long itemId);

	/**
	 * Get the sum of versioned file size for a user
	 * 
	 * @param userId user id the VersionedFile belongs to
	 * 
	 * @return sum of versioned files size
	 */
	public Long getSumOfVersionedFilesSizeForUser(Long userId) ;
}
