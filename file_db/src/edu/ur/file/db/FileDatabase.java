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

import edu.ur.persistent.LongPersistentId;
import edu.ur.persistent.PersistentVersioned;

import java.io.InputStream;
import java.io.Serializable;
import java.io.File;
import java.util.Set;


/**
 * An interface for a  file database which holds files.  This interface
 * has no knowledge of how the file database stores files.  This interface
 * is only for basic file storage. 
 * 
 * It is expected that if a file database has versioning capabilities, this
 * can still be handled with this interface using the uniqueFileName and FileInfo
 * object.
 * 
 * It is expected any implementation of this interface will produce unique ids for
 * all files added to the system and this id will be unique across all added files.
 * 
 * @author Nathan Sarr
 *
 */
public interface FileDatabase extends LongPersistentId, PersistentVersioned, 
Serializable {

	/**
	 * Add a file to this file database.  The file should have a size
	 * greater than zero.  If not see <code>addFile(String uniqueName)</code>
	 * 
	 * @param f file to add to this file database
	 * @param uniqueFileName - unique file name identifier.  This name
	 * should be unique across all files and folders in the database.
	 * 
	 * @return the file information
	 */
	public FileInfo addFile(File f, String uniqueFileName);
	
	/**
	 * Create a new empty file with the specified name.  
	 * 
	 * @param uniqueName - name of the file which is unique.
	 * @return the file information.
	 */
	public FileInfo addFile(String uniqueName);
	
	/**
	 * Remove the file from the file database.
	 * 
	 * @param unique file name to remove.
	 * @return true if the file is removed from the system.
	 */
	public boolean removeFile(String uniqueFileName);
		
	/**
	 * Remove a file using it's unique id.
	 * 
	 * @param id - id of the file
	 * @return true if the fileinfo has been removed from the system.
	 */
	public boolean removeFile(Long id);
	
	/**
	 * Get the file information for the specified file.
	 * 
	 * @param id - Unique id of the file.
	 * @return The file information or null if the file is not found.
	 */
	public FileInfo getFile(Long id);
	
	/**
	 * Get the file information for the specified file.
	 * 
	 * @param uniqueFileName - Unique file name
	 * @return The file information or null if the file is not found.
	 */
	public FileInfo getFile(String uniqueFileName);
	
	/**
	 * Get the file input stream.
	 * 
	 * @param id - unique id for the file
	 * @return an input stream for the file or null if the file does not exist.
	 */
	public InputStream getInputStream(Long id);
	
	/**
	 * Get the input stream for the file.
	 * 
	 * @param uniqueFileName - name of the file
	 * @return an input stream for the file or null if the file does not exist.
	 */
	public InputStream getInputStream(String uniqueFileName);

	/**
	 * Name displayed to the user for this file database.
	 * 
	 * @return display name
	 */
	public String getDisplayName();

	/**
	 * Get the description for this file database.
	 * 
	 * @return the description.
	 */
	public String getDescription();
	
	/**
	 * Get the name of this file database
	 * 
	 * @return name of the file database
	 */
	public String getName();
	
	/**
	 * Create a folder in the file database.
	 * 
	 * @param uniqueName
	 * @return - the created folder.
	 */
	public FolderInfo createFolder(String uniqueName);
	
	/**
	 * Get the folder with the specified name
	 * 
	 * @param uniqueName - unique name of the folder
	 * 
	 * @return - the found folder information or null if not found.
	 */
	public FolderInfo getFolder(String uniqueName);
	
	/**
	 * Set the default folder to store all the files.
	 * 
	 * @param folderName
	 * @return true if the file store is changed
	 */
	public boolean setCurrentFileStore(String folderName);
	
	/**
	 * Get the set of root folders for the file database FolderInfo
	 * objects.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set getRootFolders();
	
	
	
}
