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
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.ur.file.IllegalFileSystemNameException;

/**
 * This is an in memeory file server service.  Information is not saved to a database. Files
 * are persisted to the file system and must be deleted if they are not supposed to remain on
 * disk.  This class is for testing purposes ONLY.
 * 
 * @author Nathan Sarr
 *
 */
public class InMemoryDefaultFileServerService implements FileServerService{

	/**In memory set of file servers. */
	private Set<DefaultFileServer> fileServers = new HashSet<DefaultFileServer>();
	
	/**
	 * Create a file server with the specified name.
	 * 
	 * @see edu.ur.file.db.FileServerService#createFileServer(java.lang.String)
	 */
	public DefaultFileServer createFileServer(String name)
	{
		//try to find the file server first
		DefaultFileServer f = getFileServer(name);
		if( f == null)
		{
			f = new DefaultFileServer(name);
			
			try
			{
			    saveFileServer(f);
			}
			catch(Exception e)
			{
				throw new RuntimeException("Could not save the file server " 
						+ name + " to the database ", e);
			}
		}
		else
		{
			throw new RuntimeException("A file server with the name " + name + " already exists");
		}
		return f;
	}
	/**
	 * Add a file to the file system 
	 * @throws IllegalFileSystemNameException 
	 * 
	 * @see edu.ur.file.db.FileServerService#addFile(java.lang.Long, java.io.File, java.lang.String, java.lang.String)
	 */
	public FileInfo addFile(FileDatabase fileDatabase, File f, String uniqueName, String extension) throws IllegalFileSystemNameException {
		return addFile(fileDatabase, f, uniqueName, extension, null);
	}

	/**
	 * Add a file to the file system
	 * @throws IllegalFileSystemNameException 
	 * 
	 * @see edu.ur.file.db.FileServerService#addFile(java.lang.Long, java.io.File, java.lang.String, java.lang.String, java.lang.String)
	 */
	public FileInfo addFile(FileDatabase fileDatabase, File f, String uniqueName, String extension, String displayName) throws IllegalFileSystemNameException {
		FileInfo info = null;
		
		if(fileDatabase == null)
		{
			throw new IllegalStateException("File database with id = " + fileDatabase + " does not exist");
		}
		
		info = fileDatabase.addFile(f, uniqueName);
		info.setDisplayName(displayName);
		info.setExtension(extension);
		return info;
	}

	/**
	 * Delete the file from the file system.
	 * 
	 * @see edu.ur.file.db.FileServerService#deleteFile(java.lang.String)
	 */
	public boolean deleteFile(String uniqueFileName) {

		DefaultFileInfo fileInfo = findFile(uniqueFileName, false);
		TreeFolderInfo folder = fileInfo.getFolderInfo();
		boolean removed = false;
		
		if( fileInfo != null )
		{
		    folder.removeFileInfo(fileInfo);
			removed = true;
		}
		
		return removed;
	}

	/**
	 * Delete the specified file.
	 * 
	 * @see edu.ur.file.db.FileServerService#deleteFile(java.lang.Long)
	 */
	public boolean deleteFile(FileInfo fileInfo) {
		DefaultFileInfo info = (DefaultFileInfo)fileInfo;
		TreeFolderInfo folder = info.getFolderInfo();
		boolean removed = false;
		
		if( fileInfo != null )
		{
		    folder.removeFileInfo(info);
			removed = true;
		}
		
		return removed;
	}

	/**
	 * Delete the specified file server.
	 * 
	 * @see edu.ur.file.db.FileServerService#deleteFileServer(java.lang.Long)
	 */
	public boolean deleteFileServer(FileServer fileServer) {
		try
		{
		    ((DefaultFileServer)fileServer).deleteFileServer();
		}
		catch (Exception e)
		{
			throw new RuntimeException("Could not remove file server " 
					+ fileServer + " from database", e);
		   
		}
		
		return true;
	}

	/**
	 * Return the default file database if found.  Otherwise return null.
	 * 
	 * @see edu.ur.file.db.FileServerService#getDatabaseById(java.lang.Long, boolean)
	 */
	public DefaultFileDatabase getDatabaseById(Long id, boolean lock) {
		for(DefaultFileServer dfs : fileServers)
		{
			for(DefaultFileDatabase fd : dfs.getFileDatabases())
			{
				if( fd.getId().equals(id))
				{
					return fd;
				}
			}
		}
		return null;
	}

	/**
	 * Find the file database by name.
	 * 
	 * @see edu.ur.file.db.FileServerService#getDatabaseByName(java.lang.Long, java.lang.String)
	 */
	public FileDatabase getDatabaseByName(Long fileServerId, String name) {
		
		DefaultFileServer fileServer = (DefaultFileServer)getFileServer(fileServerId, false);
		
		Set<DefaultFileDatabase> fileDatabases = fileServer.getFileDatabases();
		for( FileDatabase fd : fileDatabases)
		{
			if( fd.getName().equals(name))
			{
				return fd;
			}
		}
		return null;
	}

	/**
	 * Find a file with the specified file id.  If it does not exist null
	 * is returned.
	 * 
	 * @see edu.ur.file.db.FileServerService#getFileById(java.lang.Long, boolean)
	 */
	public DefaultFileInfo getFileById(Long fileId, boolean lock) {
		
		for( DefaultFileServer fileServer : fileServers)
		{
			for(DefaultFileDatabase fd : fileServer.getFileDatabases())
			{
				return fd.getFile(fileId);
			}
		}
		return null;
	}

	/**
	 * Finds a file server by name.
	 * 
	 * @see edu.ur.file.db.FileServerService#getFileServer(java.lang.String)
	 */
	public DefaultFileServer getFileServer(String name) {
		for( DefaultFileServer fileServer: fileServers)
		{
			if( fileServer.getName().equals(name))
			{
				return fileServer;
			}
		}
		return null;
	}

	/**
	 * Finds a specified file server service in the system.
	 * 
	 * @see edu.ur.file.db.FileServerService#getFileServer(java.lang.Long, boolean)
	 */
	public DefaultFileServer getFileServer(Long id, boolean lock) {
		for( DefaultFileServer fileServer: fileServers)
		{
			if( fileServer.getId().equals(id))
			{
				return fileServer;
			}
		}
		return null;
	}


	/**
	 * Returns all the file servers in the system.
	 * 
	 * @see edu.ur.file.db.FileServerService#getAllFileServers()
	 */
	@SuppressWarnings("unchecked")
	public List getAllFileServers() {
		List myServers = new LinkedList<FileServer>(fileServers);
		return Collections.unmodifiableList(myServers);
	}

	/**
	 * Returns a count of all databases in the system.
	 * 
	 * @see edu.ur.file.db.FileServerService#getFileDatabaseCount(java.lang.Long)
	 */
	public Long getFileDatabaseCount() {
		Long count = 0l;
		for( FileServer fs : fileServers)
		{
			count += fs.getFileDatabases().size();
		}
		
		return count;
	}

	/**
	 * This is a persistence method and does nothing 
	 * 
	 * @see edu.ur.file.db.FileServerService#saveDatabase(edu.ur.file.db.FileDatabase)
	 */
	public void saveDatabase(FileDatabase fileDatabase) {
		//does nothing
	}

	/**
	 * This is a persistence method and does nothing in this implementation.
	 * 
	 * @see edu.ur.file.db.FileServerService#saveFileInfo(edu.ur.file.db.FileInfo)
	 */
	public void saveFileInfo(FileInfo fileInfo) {
		//does nothing
	}

	/**
	 * This method adds the file server to the set of file servers
	 * 
	 * @see edu.ur.file.db.FileServerService#saveFileServer(edu.ur.file.db.FileServer)
	 */
	public void saveFileServer(FileServer fileServer) {
		fileServers.add((DefaultFileServer)fileServer);
	}

	/**
	 * Creates a default file database in memory.
	 * @throws LocationAlreadyExistsException 
	 * 
	 * @see edu.ur.file.db.FileServerService#createFileDatabase(edu.ur.file.db.FileDatabaseInfo)
	 */
	public DefaultFileDatabase createFileDatabase(FileDatabaseInfo fileDatabaseInfo) throws LocationAlreadyExistsException {
		DefaultFileDatabaseInfo defaultFileDatabaseInfo = (DefaultFileDatabaseInfo)fileDatabaseInfo;
		
		Long fileServerId = defaultFileDatabaseInfo.getFileServerId();
		String displayName = defaultFileDatabaseInfo.getFileDatabaseDisplayName();
		String path = defaultFileDatabaseInfo.getPath();
		String description = defaultFileDatabaseInfo.getDescription();
		String fileSystemFolderName = defaultFileDatabaseInfo.getFileSystemFolderName();
		String defaultFolderDisplayName = defaultFileDatabaseInfo.getDefaultFolderDisplayName();
		String defaultFolderUniqueName = defaultFileDatabaseInfo.getDefaultFolderUniqueName();
		
		DefaultFileServer defaultFileServer = null;
		for( DefaultFileServer dfs : fileServers)
		{
			if( dfs.getId().equals(fileServerId))
			{
				defaultFileServer = dfs;
			}
		}
		
		if(defaultFileServer == null)
		{
			throw new IllegalStateException("Could not find file server with id = " + fileServerId );
		}
		
		DefaultFileDatabase fileDatabase = null;
		fileDatabase = defaultFileServer.createFileDatabase(displayName, fileSystemFolderName,
				path, description);
		fileDatabase.createRootFolder(defaultFolderDisplayName, defaultFolderUniqueName);
		fileDatabase.setCurrentFileStore(defaultFolderUniqueName);
		
		
		return fileDatabase;
	}

	/**
	 * Find the file by the unique name - otherwise return null.
	 * 
	 * @see edu.ur.file.db.FileServerService#findFile(java.lang.String, boolean)
	 */
	public DefaultFileInfo findFile(String uniqueFileName, boolean lock) {
		for( DefaultFileServer fileServer : fileServers)
		{
			for(DefaultFileDatabase fd : fileServer.getFileDatabases())
			{
				return fd.getFile(uniqueFileName);
			}
		}
		
		return null;
	}
	
	
	/**
	 * Create an empty file in the file system.
	 * 
	 * @see edu.ur.file.db.FileServerService#createEmptyFile(java.lang.Long, java.lang.String, java.lang.String)
	 */
	public FileInfo createEmptyFile(FileDatabase fileDatabase, String uniqueName,
			String extension) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public FileInfo createEmptyFile(FileDatabase fileDatabase, String uniqueName,
			String extension, String displayName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public FolderInfo createFolder(FileDatabase fileDatabase, String uniqueName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public FolderInfo createFolder(FolderInfo parent, String uniqueName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void deleteFolder(FolderInfo folder) {
		// TODO Auto-generated method stub
		
	}
	public FolderInfo getFolder(Long folderId, boolean lock) {
		// TODO Auto-generated method stub
		return null;
	}
	public void saveFolder(FolderInfo folderInfo) {
		// TODO Auto-generated method stub
		
	}
	public FolderInfo createFolder(FileDatabase fileDatabase,
			String uniqueName, String displayName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Long getFileCount(Long folderId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Find a folder by name.
	 * 
	 * @param name - name of the folder
     * @param fileDatabaseId Id of  the file database
	 * 
	 * @return - the folder information or null if not found.
	 */
	public TreeFolderInfo getTreeFolderInfoByName(String name, Long fileDatabaseId)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<FileDatabase> getFileDatabases() {
		// TODO Auto-generated method stub
		return null;
	}

}
