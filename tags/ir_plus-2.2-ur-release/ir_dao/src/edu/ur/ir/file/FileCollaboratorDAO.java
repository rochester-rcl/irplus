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

/**
 * Persistence for File collaborator
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface FileCollaboratorDAO extends CountableDAO, 
CrudDAO<FileCollaborator>{
	
	/**
	 * Find the collaborators for Versioned file
	 * 
	 * @param versionedFileId versioned file id
	 * @return List of collaborators
	 */
	public List<FileCollaborator> findCollaboratorsForVerionedFileId(Long versionedFileId);
	
	/**
	 * Get the file collaborator for the user id and versioned file id.
	 * 
	 * @param userId - id of the user who is a collaborator
	 * @param versionedFileId - id of the versioned file.
	 * 
	 * @return the file collaborator.
	 */
	public FileCollaborator findByUserIdVersionedFileId(Long userId, Long versionedFileId);
	
	/**
	 * Get the list for file collaborator objects for the user id and versioned file ids.
	 * 
	 * @param userId - id of the user who is a collaborator
	 * @param versionedFileIds - List of versioned files to check for
	 * 
	 * @return the file collaborator infos found for the given versioned file ids.
	 */
	public List<FileCollaborator> findByUserIdVersionedFileId(Long userId, List<Long> versionedFileIds);

}



