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
	 * Add the personal file to the index.
	 * 
	 * @param personalFile
	 */
	public void addToIndex(Repository repository, PersonalFile personalFile);
	
	/**
	 * Update the personal file in the index.
	 * 
	 * @param personalFile
	 */
	public void updateIndex(Repository repository, PersonalFile personalFile);
	
	/**
	 * Delete the personal file in the index.
	 * 
	 * @param personalFile
	 */
	public void deleteFromIndex(PersonalFile personalFile);
	

	/**
	 * Add the personal folder to the index.
	 * 
	 * @param personalFile
	 */
	public void addToIndex(Repository repository, PersonalFolder personalFolder);
	
	/**
	 * Update the personal folder in the index.
	 * 
	 * @param personalFile
	 */
	public void updateIndex(Repository repository, PersonalFolder personalFolder);
	
	/**
	 * Delete the personal folder from the index.
	 * 
	 * @param personalFile
	 */
	public void deleteFromIndex(PersonalFolder personalFolder);
	
	/**
	 * Add the shared inbox file  to the index.
	 * 
	 * @param personalFile
	 */
	public void addToIndex(Repository repository, SharedInboxFile inboxFile);
	
	/**
	 * Update the shared inbox file  in the index.
	 * 
	 * @param personalFile
	 */
	public void updateIndex(Repository repository, SharedInboxFile inboxFile);
	
	/**
	 * Delete the shared inbox file from the index.
	 * 
	 * @param personalFile
	 */
	public void deleteFromIndex(SharedInboxFile inboxFile);
	
	/**
	 * Updates all indexes for all collaborators and the owner who share the specified file.
	 *  
	 * @param personalFile
	 */
	public void updateAllIndexes(Repository repository, PersonalFile personalFile);
	
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
	public void deleteFromIndex(PersonalItem personalItem);
	
	/**
	 * Add a personal item to the index.
	 * 
	 * @param repository
	 * @param personalItem
	 */
	public void addToIndex(Repository repository, PersonalItem personalItem);
	
	/**
	 * Update the index.
	 * 
	 * @param repository
	 * @param personalItem
	 */
	public void updateIndex(Repository repository, PersonalItem personalItem);

}
