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

import java.io.File;
import java.util.List;

import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FileServer;

/**
 * Service layer over the file server - allows the system to interact with
 * the file server. 
 * 
 * @author Nathan Sarr
 *
 */
public interface FileServerService {
	
	/**
	 * Create a file server with the specified name.
	 * 
	 * @param name - name to give the file server.
	 * @return - the created file server.
	 */
	public FileServer createFileServer(String name);
	
	/**
	 * Get a file server by name.  It is assumed all file servers
	 * have a unique name.
	 * 
	 * @param name of the file server
	 * @return the file server or null if no file server found.
	 */
	public FileServer getFileServer(String name);
	
	/**
	 * Creates a file database with the specified file database info.  It is
	 * expected this class will be casted to the proper type for the given implementation.
	 * 
	 * @param fileDatabaseInfo
	 * @return - the file database created
	 * @throws LocationAlreadyExistsException - if the file database location already exists
	 */
	public FileDatabase createFileDatabase(FileDatabaseInfo fileDatabaseInfo) throws LocationAlreadyExistsException;
	
	/**
	 * Get the file server by id.
	 * 
	 * @param id of the file server
	 * @param lock
	 * @return the file server or null if the file server is not found.
	 */
	public FileServer getFileServer(Long id, boolean lock);
	
    /**
     * Get the number of file databases in the system
     * 
     * @param Get the database count for the specified file server.
     * @return the number of file databases 
     */
	public Long getFileDatabaseCount();
	
	/**
	 * Get all the file servers in the system.
	 * 
	 * @return the file servers.
	 */
	@SuppressWarnings("unchecked")
	public List getAllFileServers();
 
	/** 
     * Find a file database by name.  This assumes all file databases have unique names.
     * 
     * @param fileServerId - id of the file server.
     * @param name - name of the database.
     * 
     * @return the found database otherwise null.
	 */
	public FileDatabase getDatabaseByName(Long fileServerId, String name );
	
	/**
	 * Find a file database by it's id.  This assumes all databases in 
	 * the system have a unique id even accross different file servers.
	 * 
	 * @param id - id of the database
	 * @param lock - upgrade the lock on the database information 
	 * 
	 * @return the found database otherwise null.
	 */
	public FileDatabase getDatabaseById( Long id, boolean lock);

	/**
	 * Save the specified database to persistent storage.  This only
	 * saves pre-existing file databases.  This does not create a new 
	 * file database.
	 * 
	 * @param name
	 */
	public void saveDatabase(FileDatabase fileDatabase);
	
	/**
	 * Save the specified file server to persistent storage.  This
	 * only saves pre-existing file servers.
	 * 
	 * @param fileServer
	 */
	public void saveFileServer(FileServer fileServer);
		
	/**
	 * Add a file to the specified file database.  This places
	 * a file info wrapper around the specified file.  This 
	 * adds the file to physical storage 
	 * 
	 * @param fileDabaseId - database to add the file to
	 * @param f - file to be added
	 * @param uniqueName - unique name to be given to the file.
	 * @param extension - file extension to be used for this file. The extension should not be prefixed
	 * by a period. For example The extension should be "txt" and NOT ".txt"
	 * 
	 * @return File Information wrapper of file stored to persistent storage
	 */
	public FileInfo addFile(FileDatabase fileDatabase, File f, 
			String uniqueName, String extension);
	
	/**
	 * Add a file to the specified file database.  This places
	 * a file info wrapper around the specified file.  This 
	 * adds the file to physical storage
	 * 
	 * @param fileDabase - database to add the file to
	 * @param f - file to be added
	 * @param uniqueName - unique name to be given to the file.
	 * @param extension - file extension to be used for this file. The extension should not be prefixed
	 * by a period. For example The extension should be "txt" and NOT ".txt"
	 *  
	 * @param displayName - name to help identify the file when unique name is generated automatically
	 * 
	 * @return File Information wrapper stored to disk
	 */
	public FileInfo addFile(FileDatabase fileDatabase, File f, String uniqueName, 
			String extension, String displayName);
	
	/**
	 * Create an empty a file in the specified file database.  This places
	 * a file info wrapper around the specified file.  This 
	 * adds the file to physical storage 
	 * 
	 * @param fileDabaseId - database to add the file to
	 * @param uniqueName - unique name to be given to the file.
	 * @param extension - file extension to be used for this file. The extension should not be prefixed
	 * by a period. For example The extension should be "txt" and NOT ".txt"
	 */
	public FileInfo createEmptyFile(FileDatabase fileDatabase, String uniqueName, 
			String extension);
	
	/**
	 * Create an empty file in the specified file database.  This places
	 * a file info wrapper around the specified file.  This 
	 * adds the file to physical storage
	 * 
	 * @param fileDabaseId - database to add the file to
	 * @param uniqueName - unique name to be given to the file.
	 * @param extension - file extension to be used for this file. The extension should not be prefixed
	 * by a period. For example The extension should be "txt" and NOT ".txt"
	 *  
	 * @param displayName - name to help identify the file when unique name is generated automatically
	 * @return - 
	 */
	public FileInfo createEmptyFile(FileDatabase fileDatabase, String uniqueName, 
			String extension, String displayName);
	
	/**
	 * Remove the file from the file database.  This permanently 
	 * removes the file from the system.
	 * 
	 * @param the file database to remove the file from
	 * @param unique file name to remove.
	 * @return true if the file is removed from the system.
	 */
	public boolean deleteFile(String uniqueFileName);
		
	/**
	 * Remove a file using it's unique id.  This assumes all
	 * files in the system have a unique id.This permenatly 
	 * removes the file from the system.
	 * 
	 * @param id - the file info to delete
	 * @return true if the fileinfo has been removed from the system.
	 */
	public boolean deleteFile(FileInfo fileInfo);
	
	/**
	 * Get the file information for the specified file.  This assumes all
	 * files in the system have a unique id.
	 * 
	 * @param id - Unique id of the file.
	 * @param lock
	 * @return The file information or null if the file is not found.
	 */
	public FileInfo getFileById(Long fileId, boolean lock);
	
	/**
	 * Get the file information for the specified file.  This assumes all
	 * files in the system have a unique id.
	 * 
	 * @param id - Unique name of the file.
	 * @param lock
	 * @return The file information or null if the file is not found.
	 */
	public FileInfo findFile(String uniqueFileName , boolean lock);
	
	/**
	 * Save the specified file information to persistent storage.  This
	 * only saves pre-existing file info objects.
	 * 
	 * @param FileInfo
	 */
	public void saveFileInfo(FileInfo fileInfo);
	
	/**
	 * Deletes the file server from the system.
	 * 
	 * @param id
	 * @return true if the file server is deleted
	 */
	public boolean deleteFileServer(FileServer fileServer);
	
	/**
	 * Create a root level folder in the file database.
	 * 
	 * @param fileDatabase - database to create the folder in.
	 * @param uniqueName - unique name for the folder.
	 * 
	 * @return - the folder information.
	 * @throws LocationAlreadyExistsException - if the folder location already exists
	 */
	public FolderInfo createFolder(FileDatabase fileDatabase, String uniqueName) throws LocationAlreadyExistsException;

	/**
	 * Create a root level folder in the file database.
	 * 
	 * @param fileDatabase - database to create the folder in.
	 * @param uniqueName - unique name for the folder.
	 * @param displayName - display name to give the folder.
	 * 
	 * @return - the folder information.
	 * @throws LocationAlreadyExistsException - if the folder location already exists
	 */
	public FolderInfo createFolder(FileDatabase fileDatabase, String uniqueName, 
			String displayName) throws LocationAlreadyExistsException;

	
	/**
	 * Create a folder underneath the parent folder.
	 * 
	 * @param parent - parent folder to create the folder in
	 * @param uniqueName - unique name to use for the folder name
	 * 
	 * @return - the created child folder information.
	 * @throws LocationAlreadyExistsException - if the folder location already exists
	 */
	public FolderInfo createFolder(FolderInfo parent, String uniqueName) throws LocationAlreadyExistsException;
	
	
	/**
	 * Get the folder with the specified id.
	 * 
	 * @param folderId - unique id of the folder
	 * @param lock - upgrade the lock on the database information
	 * 
	 * @return - the found folder information or null if not found.
	 */
	public FolderInfo getFolder(Long folderId, boolean lock);
	
	/**
	 * Save the folder information to the database.
	 * 
	 * @param folderInfo
	 */
	public void saveFolder(FolderInfo folderInfo);
	
	/**
	 * Delete the folder.
	 * 
	 * @param folder
	 */
	public void deleteFolder(FolderInfo folder);
	
	
	/**
	 * Get a count of all files in the folder.
	 * 
	 * @param folderId - id of the folder
	 * @return a count of files in the folder
	 */
	public Long getFileCount(Long folderId);

	/**
	 * Find a folder by name.
	 * 
	 * @param name - name of the folder
	 * @param fileDatabaseId Id of  the file database
	 * 
	 * @return - the folder information or null if not found.
	 */
	public TreeFolderInfo getTreeFolderInfoByName(String name, Long fileDatabaseId);
}

