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

package edu.ur.ir.user;

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.ir.file.IrFile;


/**
 * Interface for creating and saving personal file information
 * to a relational database.
 * 
 * @author Nathan Sarr
 *
 */
public interface PersonalFileDAO extends CountableDAO, 
CrudDAO<PersonalFile>
{

	/**
	 * Get the files for user id and listed file ids.  If the list of fileIds 
	 * is null no files are returned.
	 * 
	 * @param userId
	 * @param fileIds
	 * 
	 * @return the found files
	 */
	public List<PersonalFile> getFiles(Long userId, List<Long> fileIds);

	/**
	 * Get the personal file for given user id and ir file id .
	 * 
	 * @param userId
	 * @param irFileId
	 * 
	 * @return the found files
	 */
	public PersonalFile getFileForUserWithSpecifiedIrFile(Long userId, Long irFileId);

	/**
	 * Get the personal file for given user id and versioned file id .
	 * 
	 * @param userId
	 * @param versionedFileId
	 * 
	 * @return the found files
	 */
	public PersonalFile getFileForUserWithSpecifiedVersionedFile(Long userId, Long versionedFileId);

	/**
	 * Get the files for user id and folder id .
	 * 
	 * @param userId
	 * @param folderId
	 * 
	 * @return the found files or empty list if no files are found
	 */
	public List<PersonalFile> getFilesInFolderForUser(Long userId, Long folderId);
	
	
	/**
	 * Get the root files 
	 * 
	 * @param userId
	 * 
	 * @return the found files
	 */
	public List<PersonalFile> getRootFiles(Long userId);
	
	/**
	 * Get the files with specified ir file id .
	 * 
	 * @param irFileId
	 * 
	 * @return the found files
	 */
	public Long getPersonalFileCount(Long irFileId);
	
	/**
	 * Get all item files uses the specified ir file.
	 * 
	 * @param irFile - ir file being used
	 * @return the list of item files being used.
	 */
	public List<PersonalFile> getPersonalFilesWithIrFile(IrFile irFile);
	
	/**
	 * Delete the personal file record from persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(PersonalFile pf);
	
	
	/**
	 * Get a list of personal files shared witht he given user.
	 * 
	 * @param rowStart - start position in the list
	 * @param maxResults - maximum number of results
	 * @param ownerId - owner of the personal files.
	 * @param sharedWithUserId - id of the user who files are shared with
	 * 
	 * @return list of files shared with the user.
	 */
	public List<PersonalFile> getFilesSharedWithUser(int rowStart,
			int maxResults, Long ownerId, Long sharedWithUserId);
	
	/**
	 * Get the count of files shared with a given user.
	 * 
	 * @param ownerId - owner of the personal file sto check
	 * @param sharedWithUserId - id of the shared with user id.
	 * 
	 * @return count of files shared with the given shared with user id
	 */
	public Long getFilesSharedWithUserCount(Long ownerId, Long sharedWithUserId);
}
