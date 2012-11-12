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


package edu.ur.file.db.service;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.checksum.ChecksumCalculator;
import edu.ur.file.checksum.ChecksumService;
import edu.ur.file.db.DefaultDatabaseFileStoreStrategy;
import edu.ur.file.db.DefaultFileDatabase;
import edu.ur.file.db.DefaultFileDatabaseInfo;
import edu.ur.file.db.DefaultFileInfo;
import edu.ur.file.db.DefaultFileServer;
import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileDatabaseDAO;
import edu.ur.file.db.FileDatabaseInfo;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FileInfoDAO;
import edu.ur.file.db.FileServer;
import edu.ur.file.db.FileServerDAO;
import edu.ur.file.db.FileServerService;
import edu.ur.file.db.FolderInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.TreeFolderInfo;
import edu.ur.file.db.TreeFolderInfoDAO;




/**
 * Service class for interacting with the file system.  This takes
 * care of both database access and dealing with the file system.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultFileServerService implements FileServerService{
	
	/* eclipse generated id */
	private static final long serialVersionUID = 7485322616249733422L;

	/** Data access for file servers in the database  */
	private FileServerDAO fileServerDAO;

	/** File database data access  */
	private FileDatabaseDAO fileDatabaseDAO;

	/** Default file info database access.  */
	private FileInfoDAO fileInfoDAO;
	
	/** Tree folder info database access  */
	private TreeFolderInfoDAO treeFolderInfoDAO;
	
	/** Service for checksuming files */
	private ChecksumService checksumService;
	
	/** Strategy for storing files */
	private DefaultDatabaseFileStoreStrategy defaultDatabaseFileStoreStrategy;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultFileServerService.class);

	/**
	 * Default Constructor
	 * 
	 * @param fileServerDAO - database persistent storage for file server
	 * @param fileDatabaseDAO - database persistent store for file databases
	 * @param fileInfoDAO - persistent storage for files
	 */
	public DefaultFileServerService(FileServerDAO fileServerDAO, 
			FileDatabaseDAO fileDatabaseDAO, 
			TreeFolderInfoDAO treeFolderInfoDAO,
			FileInfoDAO fileInfoDAO)
	{
		this.fileServerDAO = fileServerDAO;
		this.fileDatabaseDAO = fileDatabaseDAO;
		this.fileInfoDAO = fileInfoDAO;
		this.treeFolderInfoDAO = treeFolderInfoDAO;
	}
	
	/**
	 * Package protected constructor
	 */
	DefaultFileServerService(){}
	
	/**
	 * Create a file server in the system.  If the name of the file server already
	 * exists, a runtime exception is thrown.
	 * 
	 * @param name
	 * @return
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
	 * Get the default file database DAO
	 * 
	 * @return
	 */
	public FileDatabaseDAO getFileDatabaseDAO() {
		return fileDatabaseDAO;
	}

	/**
	 * Get teh default file database dao.
	 * 
	 * @param hbDefaultFileDatabaseDAO
	 */
	public void setFileDatabaseDAO(FileDatabaseDAO fileDatabaseDAO) {
		this.fileDatabaseDAO = fileDatabaseDAO;
	}

	/**
	 * Get the default file server DAO.
	 * 
	 * @return
	 */
	public FileServerDAO getFileServerDAO() {
		return fileServerDAO;
	}

	/**
	 * Set the default file server DAO.
	 * 
	 * @param hbDefaultFileServerDAO
	 */
	public void setFileServerDAO( FileServerDAO fileServerDAO) {
		this.fileServerDAO = fileServerDAO;
	}

	/**
	 * Adds a file to the specified database.
	 * @throws IllegalFileSystemNameException 
	 * 
	 * @see edu.ur.file.db.service.FileServerService#addFile(java.lang.Long, java.io.File, java.lang.String)
	 */
	public DefaultFileInfo addFile(FileDatabase fileDatabase, File f, String uniqueName, String extension) throws IllegalFileSystemNameException {
		return addFile(fileDatabase, f, uniqueName, extension, null);
	}

	/**
	 * Create a new file database in the system.
	 * 
	 * @param fileServerId - file server to create the file database in
	 * @param displayName - user friendly name displayed to the user.
	 * @param fileSystemFolderName - folder to create on the file system.  This name should
	 * not have illegal file name characters or spaces.
	 * 
	 * @param path - full path to create the folder at
	 * @param description - description of the file database.
	 * 
	 * @return create file database.
	 * @throws LocationAlreadyExistsException - if the file database location already exists
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
		
		DefaultFileServer defaultFileServer = (DefaultFileServer)fileServerDAO.getById(fileServerId, false);
		DefaultFileDatabase fileDatabase = null;
		
		if(defaultFileServer == null)
		{
			throw new IllegalStateException("Could not find file server with id = " + fileServerId );
		}
		
		fileDatabase = defaultFileServer.createFileDatabase(displayName, fileSystemFolderName,
				path, description);
		TreeFolderInfo folder = fileDatabase.createRootFolder(defaultFolderDisplayName, defaultFolderUniqueName);
		fileDatabase.setCurrentFileStore(folder.getName());
		try
		{
			this.fileDatabaseDAO.makePersistent(fileDatabase);
		}
		catch(Exception e)
		{
			//try and clean up the file system.
			defaultFileServer.deleteDatabase(fileSystemFolderName);
			throw new RuntimeException("Could not create file database in file server " +
					defaultFileServer, e);
		}
		return fileDatabase;
	}

	/**
	 * Delete the file server from the system.
	 * 
	 * @param fileServerId
	 */
	public boolean deleteFileServer(FileServer fileServer) {
		DefaultFileServer defaultFileServer = (DefaultFileServer)fileServer;
		boolean deleted = false;
		if(defaultFileServer != null)
		{
		    fileServerDAO.makeTransient(defaultFileServer);
		
		    try
		    {
			    defaultFileServer.deleteFileServer();
			    deleted = true;
		    }
		    catch(Exception e)
		    {
			    throw new RuntimeException("Could not delete file server " + 
			    		defaultFileServer + " from file system ",e);
		    }
		}
		return deleted;
	}

	/**
	 * Delete fhe file database from the specified file server.
	 * 
	 * @param fileServerId - id of the file server
	 * @param name - name of the file database.
	 * 
	 * @return true if the database is deleted.
	 */
	public boolean deleteDatabase(Long fileServerId, String name) {
		DefaultFileServer defaultFileServer = (DefaultFileServer)this.fileServerDAO.getById(fileServerId, false);
		boolean deleted = false;
		
		if(defaultFileServer != null)
		{
			defaultFileServer.deleteDatabase(name);
		}
		
		try
		{
			fileServerDAO.makePersistent(defaultFileServer);
		}
		catch(Exception e)
		{
			throw new RuntimeException("Could not delete the file database " + name 
					+ " in file server " + defaultFileServer );
		}
		
		return deleted;
	}

	/**
	 * Delete the file server with the specified name.
	 * 
	 * @param name - name of the file server.
	 */
	public void deleteFileServer(String name) {
		DefaultFileServer defaultFileServer = (DefaultFileServer)fileServerDAO.findByUniqueName(name);
		fileServerDAO.makeTransient(defaultFileServer);
		
		try
		{
		    defaultFileServer.deleteFileServer();
		}
		catch (Exception e)
		{
			throw new RuntimeException("Could not remove file server " 
					+ defaultFileServer + " from database");
		}
	}

	/**
	 * Find a file database by id.
	 * 
	 * @see edu.ur.file.db.service.FileServerService#getDatabaseById(java.lang.Long, boolean)
	 */
	public DefaultFileDatabase getDatabaseById(Long id, boolean lock) {
		return (DefaultFileDatabase)fileDatabaseDAO.getById(id, lock);
	}

	/**
	 * Find a database by name.
	 * 
	 * @see edu.ur.file.db.service.FileServerService#getDatabaseByName(java.lang.Long, java.lang.String)
	 */
	public DefaultFileDatabase getDatabaseByName(Long fileServerId, String name) {
		return (DefaultFileDatabase)fileDatabaseDAO.findByName(name, fileServerId);
	}
	
	/**
	 * Find a folder by id.
	 * 
	 * @param id - Unique id of the folder
	 * @param lock - lock the data
	 * @return - the folder information or null if not found.
	 */
	public TreeFolderInfo getTreeFolderInfoById(Long id, boolean lock)
	{
		return (TreeFolderInfo) treeFolderInfoDAO.getById(id, lock);
	}

	/**
	 * Get all file servers in the system.  This can be an expensive operation
	 * if there is a large list of file servers.
	 * 
	 * @see edu.ur.file.db.service.FileServerService#getAllFileServers()
	 */
	@SuppressWarnings("unchecked")
	public List<DefaultFileServer> getAllFileServers() {
		return fileServerDAO.getAll();
	}

	/**
	 * Get a file with the specified unique id.
	 * 
	 * @see edu.ur.file.db.service.FileServerService#getFile(java.lang.Long)
	 */
	public DefaultFileInfo getFileById(Long id, boolean lock) {
		return (DefaultFileInfo)fileInfoDAO.getById(id, lock);
	}
	
	/**
	 * Get the file info.
	 * 
	 * @param fileDatabaseId
	 * @param uniqueFileName
	 * @return
	 */
	public DefaultFileInfo findFile(String uniqueFileName)
	{
		return (DefaultFileInfo)fileInfoDAO.findByUniqueName(uniqueFileName);
	}

	/**
	 * Get the file database count
	 * 
	 * @see edu.ur.file.db.service.FileServerService#getFileDatabaseCount(java.lang.Long)
	 */
	public Long getFileDatabaseCount() {
		return fileDatabaseDAO.getCount();
	}

	/**
	 * Get the file server by name.
	 * 
	 * @see edu.ur.file.db.service.FileServerService#getFileServer(java.lang.String)
	 */
	public DefaultFileServer getFileServer(String name) {
		return (DefaultFileServer)fileServerDAO.findByUniqueName(name);
	}

	/**
	 * Get the file server 
	 * 
	 * @see edu.ur.file.db.service.FileServerService#getFileServer(java.lang.Long)
	 */
	public DefaultFileServer getFileServer(Long id, boolean lock) {
		return (DefaultFileServer)fileServerDAO.getById(id, lock);
	}

	/**
	 * Remove the file from the file database.This implemenation does not need
	 * a file database id as all files within the file server have a unique 
	 * file id.
	 * 
	 * @see edu.ur.file.db.service.FileServerService#removeFile(java.lang.Long, java.lang.String)
	 */
	public boolean deleteFile(String uniqueFileName) {
		
		DefaultFileInfo fileInfo = (DefaultFileInfo)fileInfoDAO.findByUniqueName(uniqueFileName);
		TreeFolderInfo folder = fileInfo.getFolderInfo();
		boolean removed = false;
		
		if( fileInfo != null )
		{
		    try
		    {
		    	folder.removeFileInfo(fileInfo);
			    treeFolderInfoDAO.makePersistent(folder);
			    removed = true;
		    }
		    catch(Exception e)
		    {
			    throw new RuntimeException("Could not delete file info " + fileInfo +
					" from the database", e);
		    }
		}
		
		return removed;
	}

	/**
	 * Remove the file from the file system.  This implemenation does not need
	 * a file database id as all files within the file server have a unique 
	 * file id.
	 * 
	 * @see edu.ur.file.db.service.FileServerService#deleteFile(java.lang.Long)
	 */
	public boolean deleteFile(FileInfo fileInfo) {
		
		//this implementation does need a file database id.
		DefaultFileInfo info = (DefaultFileInfo)fileInfo;
		boolean removed = false;
		
		if( info != null )
		{
			TreeFolderInfo folder = info.getFolderInfo();
		    try
		    {
		    	log.debug("Making file info " + info + " transient " );
		    	removed = folder.removeFileInfo(info);
		    	if(removed)
		    	{	
			        fileInfoDAO.makeTransient(info);
		    	}
		    }
		    catch(Exception e)
		    {
			    throw new RuntimeException(e);
		    }
		}
		
		return removed;
	}
	
	/**
	 * Save the file information.
	 * 
	 * @see edu.ur.file.db.service.FileServerService#saveFileInfo(edu.ur.file.db.FileInfo)
	 */
	public void saveFileInfo(FileInfo fileInfo)
	{
		fileInfoDAO.makePersistent(fileInfo);
	}

	/**
	 * Save the file database.
	 * 
	 * @see edu.ur.file.db.service.FileServerService#save(edu.ur.file.db.FileServer)
	 */
	public void saveFileServer(FileServer fileServer) {
		fileServerDAO.makePersistent(fileServer);
	}

	/**
	 * Save the file database.
	 * 
	 * @see edu.ur.file.db.service.FileServerService#saveDatabase(edu.ur.file.db.FileDatabase)
	 */
	public void saveDatabase(FileDatabase fileDatabase) {
		fileDatabaseDAO.makePersistent(fileDatabase);
	}

	/**
	 * Get the file database persistent storage.
	 * 
	 * @return
	 */
	public FileInfoDAO getFileInfoDAO() {
		return fileInfoDAO;
	}

	/**
	 * Set the file database persistent storage.
	 * 
	 * @param hbDefaultFileInfoDAO
	 */
	public void setFileInfoDAO(FileInfoDAO hbDefaultFileInfoDAO) {
		this.fileInfoDAO = hbDefaultFileInfoDAO;
	}
	
	/**
	 * Get tree folder info data access.
	 * 
	 * @return
	 */
	public TreeFolderInfoDAO getTreeFolderInfoDAO() {
		return treeFolderInfoDAO;
	}

	/**
	 * Tree folder info data access.
	 * 
	 * @param treeFolderInfoDAO
	 */
	public void setTreeFolderInfoDAO(TreeFolderInfoDAO treeFolderInfoDAO) {
		this.treeFolderInfoDAO = treeFolderInfoDAO;
	}

	/**
	 * Add a file to the specified file database.
	 * @throws IllegalFileSystemNameException 
	 * 
	 * @see edu.ur.file.db.FileServerService#addFile(java.lang.Long, java.io.File, java.lang.String, java.lang.String, java.lang.String)
	 */
	public DefaultFileInfo addFile(FileDatabase fileDatabase, File f, String uniqueName,
			String extension, String displayName) throws IllegalFileSystemNameException {
		DefaultFileDatabase defaultFileDatabase = (DefaultFileDatabase)fileDatabase;
		
		
		
		DefaultFileInfo info = null;
		
		if(defaultFileDatabase == null)
		{
			throw new IllegalStateException("File database with id = " + 
					fileDatabase + " does not exist");
		}
		
		info = defaultDatabaseFileStoreStrategy.addFile(defaultFileDatabase, 
				f, uniqueName);

		ChecksumCalculator checksumCalculator = checksumService.getChecksumCalculator("md5");
		
		if( checksumCalculator != null )
		{
		
			info.addFileInfoChecksum(checksumCalculator);
		}
		
		info.setDisplayName(displayName);
		info.setExtension(extension);
		try
		{
	        fileInfoDAO.makePersistent(info);
		}
		catch(Exception e)
		{
			//try and remove the file from the file system if we could not save it to the system
			defaultFileDatabase.removeFile(uniqueName);
			throw new RuntimeException("Cound not add file to database " + defaultFileDatabase, e);
		}
		
		return info;
	}

	/**
	 * Find a file with the specified name.
	 * 
	 * @see edu.ur.file.db.FileServerService#findFile(java.lang.String, boolean)
	 */
	public DefaultFileInfo findFile(String uniqueFileName, boolean lock) {
		return (DefaultFileInfo) fileInfoDAO.findByUniqueName(uniqueFileName);
	}

	
	/**
	 * Create an empty file in the specified file database.
	 * @throws IllegalFileSystemNameException 
	 * 
	 * @see edu.ur.file.db.FileServerService#createEmptyFile(java.lang.Long, java.lang.String, java.lang.String)
	 */
	public DefaultFileInfo createEmptyFile(FileDatabase fileDatabase, String uniqueName,
			String extension) throws IllegalFileSystemNameException {
		return createEmptyFile(fileDatabase, uniqueName, extension, null);
	}

	
	/**
	 * Create an empty file in the specified file database.
	 * @throws IllegalFileSystemNameException 
	 * 
	 * @see edu.ur.file.db.FileServerService#createEmptyFile(java.lang.Long, java.lang.String, java.lang.String, java.lang.String)
	 */
	public DefaultFileInfo createEmptyFile(FileDatabase fileDatabase, String uniqueName,
			String extension, String displayName) throws IllegalFileSystemNameException {
		DefaultFileDatabase defaultFileDatabase = (DefaultFileDatabase)fileDatabase;
		DefaultFileInfo info = null;
		
		info = defaultDatabaseFileStoreStrategy.addFile(defaultFileDatabase, uniqueName);
		info.setDisplayName(displayName);
		info.setExtension(extension);
		try
		{
	        this.fileInfoDAO.makePersistent(info);
		}
		catch(Exception e)
		{
			//try and remove the file from the file system if we could not save it to the system
			defaultFileDatabase.removeFile(uniqueName);
			throw new RuntimeException("Cound not add file to database " + defaultFileDatabase, e);
		}
		
		return info;
	}

	
	/**
	 * Create a folder with the specified unique name. The display name is set to the same
	 * as the unique name.
	 * @throws LocationAlreadyExistsException - if the folder location already exists
	 * 
	 * @see edu.ur.file.db.FileServerService#createFolder(edu.ur.file.db.FileDatabase, java.lang.String)
	 */
	public FolderInfo createFolder(FileDatabase fileDatabase, String uniqueName) throws LocationAlreadyExistsException {
		DefaultFileDatabase defaultFileDatabase = (DefaultFileDatabase)fileDatabase;
		TreeFolderInfo folderInfo = defaultFileDatabase.createRootFolder(uniqueName, uniqueName);
	    folderInfo.setDisplayName(uniqueName);
		treeFolderInfoDAO.makePersistent(folderInfo);
		return folderInfo;
	}
	
	/**
	 * Create a folder with the specified unique name. The display name is set to the same
	 * as the unique name.
	 * @throws LocationAlreadyExistsException - if the folder location already exists
	 * 
	 * @see edu.ur.file.db.FileServerService#createFolder(edu.ur.file.db.FileDatabase, java.lang.String, java.lang.String)
	 */
	public FolderInfo createFolder(FileDatabase fileDatabase, String uniqueName, String displayName) throws LocationAlreadyExistsException {
		DefaultFileDatabase defaultFileDatabase = (DefaultFileDatabase)fileDatabase;
		TreeFolderInfo folderInfo = defaultFileDatabase.createRootFolder(uniqueName, uniqueName);
	    folderInfo.setDisplayName(displayName);
		treeFolderInfoDAO.makePersistent(folderInfo);
		return folderInfo;
	}
	
	/**
	 * Create a child folder underneath the specified parent folder
	 * @throws LocationAlreadyExistsException - if the folder location already exists
	 * @see edu.ur.file.db.FileServerService#createFolder(edu.ur.file.db.FolderInfo, 
	 *     java.lang.String)
	 */
	public FolderInfo createFolder(FolderInfo parent, String uniqueName) throws LocationAlreadyExistsException {
		TreeFolderInfo parentFolderInfo = (TreeFolderInfo)parent;
		TreeFolderInfo child = parentFolderInfo.createChild(uniqueName, uniqueName);
		treeFolderInfoDAO.makePersistent(child);
		return null;
	}

	
	/**
	 * Delete the folder from the file system 
	 * 
	 * @see edu.ur.file.db.FileServerService#deleteFolder(edu.ur.file.db.FolderInfo)
	 */
	public void deleteFolder(FolderInfo folder) {
		TreeFolderInfo folderInfo = (TreeFolderInfo)folder;
		
		if( folderInfo.isRoot() )
		{
			DefaultFileDatabase fileDatabase = folderInfo.getFileDatabase();
			fileDatabase.removeRootFolder(folderInfo);
			fileDatabaseDAO.makePersistent(fileDatabase);
		}
		else
		{
			TreeFolderInfo parent = folderInfo.getParent();
			parent.removeChild(folderInfo);
			treeFolderInfoDAO.makePersistent(parent);
		}
	}

	
	/**
	 * Get the folder by id.
	 * 
	 * @see edu.ur.file.db.FileServerService#getFolder(java.lang.Long, boolean)
	 */
	public FolderInfo getFolder(Long folderId, boolean lock) {
		return treeFolderInfoDAO.getById(folderId, lock);
	}

	/**
	 * Save the folder information.
	 * 
	 * @see edu.ur.file.db.FileServerService#saveFolder(edu.ur.file.db.FolderInfo)
	 */
	public void saveFolder(FolderInfo folderInfo) {
		treeFolderInfoDAO.makePersistent((TreeFolderInfo)folderInfo);
	}

	/**
	 * Service for checksuming files.
	 * 
	 * @return
	 */
	public ChecksumService getChecksumService() {
		return checksumService;
	}

	/**
	 * Service for checksuming files.
	 * 
	 * @param checksumService
	 */
	public void setChecksumService(ChecksumService checksumService) {
		this.checksumService = checksumService;
	}

	
	/**
	 * Return the count of the files in the folder.
	 * 
	 * @see edu.ur.file.db.FileServerService#getFileCount(java.lang.Long)
	 */
	public Long getFileCount(Long folderId) {
	    return treeFolderInfoDAO.getFileCount(folderId);
	}

	/**
	 * Get the file store strategy.
	 * 
	 * @return
	 */
	public DefaultDatabaseFileStoreStrategy getDefaultDatabaseFileStoreStrategy() {
		return defaultDatabaseFileStoreStrategy;
	}

	/**
	 * Set the file store strategy.
	 * 
	 * @param defaultDatabaseFileStoreStrategy
	 */
	public void setDefaultDatabaseFileStoreStrategy(
			DefaultDatabaseFileStoreStrategy defaultDatabaseFileStoreStrategy) {
		this.defaultDatabaseFileStoreStrategy = defaultDatabaseFileStoreStrategy;
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
		return (TreeFolderInfo) treeFolderInfoDAO.findRootByDisplayName(name, fileDatabaseId);
	}

	
	/**
	 * Return a list of all file databases.
	 * 
	 * @see edu.ur.file.db.FileServerService#getFileDatabases()
	 */
	@SuppressWarnings("unchecked")
	public List<FileDatabase> getFileDatabases() {
		
		return fileDatabaseDAO.getAll();
	}

}
