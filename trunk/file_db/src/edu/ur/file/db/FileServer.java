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
package edu.ur.file.db;

import java.io.Serializable;
import java.util.Set;

import edu.ur.persistent.LongPersistentId;
import edu.ur.persistent.PersistentVersioned;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;

/**
 * This is the interface for a file database server it
 * will manage storing file databases
 *   
 * 
 * @author Nathan Sarr
 *
 */
public interface FileServer extends LongPersistentId, 
PersistentVersioned, Serializable, DescriptionAware, NameAware{

	/**
	 * Get the set of the set of file databases managed by this server.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set getFileDatabases();
	
	/**
	 * Find a file database by its name
	 * 
	 * @param name of the file database.
	 * @return the found file database or null if none exists by
	 * the given name
	 */
	public FileDatabase getFileDatabase(String name);

	/**
	 * Get the file database by the specified id.
	 * 
	 * @param id of the file database
	 * 
	 * @return the file database or null if no file database is found.
	 */
	public FileDatabase getFileDatabase(Long id);

	/**
	 * Delete a file Database.
	 * 
	 * @param name the name of the file database to delete
	 * @return true if the file database is deleted.
	 */
	public boolean deleteDatabase(String name);
		
	/**
	 * Set the description of the file database
	 * 
	 * @param description
	 */
	public void setDescription(String description);
	
	/**
	 * Allow the id to be set.
	 * 
	 * @param id
	 */
	public void setId(Long id);
	
	/**
	 * Create a new file database  with the specified
	 * name and unique folder name.  
	 * 
	 * @param displayName name displayed to the user
	 * 
	 * @param name of the file database used when creating the 
	 *        dabase in the file system.
	 *        
	 * @param path of the file database
	 * 
	 * @return the created file database
	 */	
	public DefaultFileDatabase createFileDatabase(String displayName, String name, String path, String description);
}
