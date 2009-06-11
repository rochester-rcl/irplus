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

import java.io.IOException;

import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.repository.Repository;


/**
 * Interface for indexing user information.
 * 
 * @author Nathan Sarr
 *
 */
public interface UserWorkspaceIndexService {
	

	
	/**
	 * Index the user and their data.  This could take
	 * a fair amount of time it would be best to update 
	 * an index over time.  This completely re-indexes the
	 * user information.
	 * 
	 * @param user - user to re-index.
	 */
	public void updateUserIndex(Repository repository, IrUser user);
	
			
	/**
	 * Add the personal file to the index.  This will create an index folder if one does not already exist.
	 * 
	 * @param personalFile
	 * @throws LocationAlreadyExistsException - if the folder location does not already exist  
	 * @throws IOException - if location cannot be created and it needs to be.
	 */
	public void addToIndex(Repository repository, PersonalFile personalFile) throws LocationAlreadyExistsException, IOException;
	
	/**
	 * Update the personal file in the index. This will create an index folder if one does not already exist.
	 * 
	 * @param personalFile
	 * @throws LocationAlreadyExistsException - if the folder location does not already exist
	 * @throws IOException - if location cannot be created and it needs to be.
	 */
	public void updateIndex(Repository repository, PersonalFile personalFile) throws LocationAlreadyExistsException, IOException;
	
	/**
	 * Delete the personal file in the index.
	 * 
	 * @param personalFile
	 */
	public void deleteFileFromIndex(IrUser user, Long personalFileId );
	

	/**
	 * Add the personal folder to the index.  Will create a folder if one does not already exist
	 * 
	 * @param personalFile
	 * @throws LocationAlreadyExistsException - if the folder already exists
	 * @throws IOException - if folder location needs to be created an cannot be created
	 */
	public void addToIndex(Repository repository, PersonalFolder personalFolder) throws LocationAlreadyExistsException, IOException;
	
	/**
	 * Update the personal folder in the index.  Will create the folder location for the user if one does not already
	 * exist
	 * 
	 * @param personalFile
	 * @throws LocationAlreadyExistsException - if the folder location already exists when trying to create a new folder.
	 * @throws IOException - if location cannot be created and it needs to be
	 */
	public void updateIndex(Repository repository, PersonalFolder personalFolder) throws LocationAlreadyExistsException, IOException;
	
	/**
	 * Delete the personal folder from the index.
	 * 
	 * @param personalFile
	 */
	public void deleteFolderFromIndex(IrUser user, Long personalFolderId);
	
	/**
	 * Add the shared inbox file  to the index. Will create the location if one already exists
	 * 
	 * @param personalFile
	 * @throws LocationAlreadyExistsException 
	 * @throws IOException 
	 */
	public void addToIndex(Repository repository, SharedInboxFile inboxFile) throws LocationAlreadyExistsException, IOException;
	
	/**
	 * Update the shared inbox file  in the index.Will create the location if one does not already exist
	 * 
	 * @param personalFile
	 * @throws LocationAlreadyExistsException - if the new location already doesn't exist
	 * @throws IOException - if location cannot be created
	 */
	public void updateIndex(Repository repository, SharedInboxFile inboxFile) throws LocationAlreadyExistsException, IOException;
	
	/**
	 * Delete the shared inbox file from the index.
	 * 
	 * @param personalFile
	 */
	public void deleteInboxFileFromIndex(IrUser user, Long sharedInboxFileId);
	
	/**
	 * Updates all indexes for all collaborators and the owner who share the specified file.  Creates an index folder if 
	 * one doesn't already exist
	 *  
	 * @param personalFile
	 * @throws LocationAlreadyExistsException - if trying to create a location that already exists
	 * @throws IOException - if location cannot be created when needed
	 */
	public void updateAllIndexes(Repository repository, PersonalFile personalFile) throws LocationAlreadyExistsException, IOException;
	
	/**
	 * Delete the file from all indexes including collaborators.
	 * 
	 * @param personalFile
	 */
	public void deleteFromAllIndexes(PersonalFile personalFile);
	
	/**
	 * Delete a personal item from the index.
	 * 
	 * @param personalItem
	 */
	public void deleteItemFromIndex(IrUser user, Long personalItemId);
	
	/**
	 * Add a personal item to the index.  Creates an index floder if one does not already exist
	 * 
	 * @param repository
	 * @param personalItem
	 * @throws LocationAlreadyExistsException - if the new location already exists 
	 * @throws IOException - if location cannot be created
	 */
	public void addToIndex(Repository repository, PersonalItem personalItem) throws LocationAlreadyExistsException, IOException;
	
	/**
	 * Update the index.  Creates an index folder if one does not already exist
	 * 
	 * @param repository
	 * @param personalItem
	 * @throws LocationAlreadyExistsException - if trying to create a location that already exists
	 * @throws IOException - if folder locaton cannot be created
	 */
	public void updateIndex(Repository repository, PersonalItem personalItem) throws LocationAlreadyExistsException, IOException;

}
