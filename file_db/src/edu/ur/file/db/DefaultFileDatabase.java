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
import java.io.FileInputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.HashSet;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.persistent.BasePersistent;

/**
 * Implementation of the file database interface.  This is a default file database
 * that will store files on the local file system.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultFileDatabase extends BasePersistent implements FileDatabase 
{
	/** Logger */
	private static final Logger log = LogManager.getLogger(DefaultFileDatabase.class);
	
	/**  Name for the file server */
	private String name;
	
	/**  Generic description  */
	private String description;
	
	/**  Name displayed to the user of this file database. */
	private String displayName;
	
	/**  The path for this file database. */
	private String path;
	
	/**
	 * The root of the folder this is either / or C:\ or the specified file
	 * system root.
	 */
	private String prefix;
	
	/**  The set of root folders for this file database. */
	private Set<TreeFolderInfo> rootFolders = new LinkedHashSet<TreeFolderInfo>();
	
	/**  Eclipse Generated id. */
	private static final long serialVersionUID = -4840134852825044703L;
	
	/**  The file server that owners this file database */
	private DefaultFileServer fileServer;
	
	/**
	 * Location where all files are stored when a file
	 * is added to the file database.
	 */
	private TreeFolderInfo currentFileFolder;

	/**   Default Constructor  */
	DefaultFileDatabase(){}

	/**
	 * Get the parent file server
	 * 
	 * @return the parent file server
	 */
	public DefaultFileServer getFileServer() {
		return fileServer;
	}

	/**
	 * Set the file server of this file database
	 * 
	 * @param fileServer
	 */
	public void setFileServer(DefaultFileServer fileServer) {
		this.fileServer = fileServer;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		
		value += name == null ? 0 : name.hashCode();
		return value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof DefaultFileDatabase)) return false;

		final DefaultFileDatabase other = (DefaultFileDatabase) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		if( ( fileServer != null && !fileServer.equals(other.getFileServer()) ) ||
			( fileServer == null && other.getFileServer() != null ) ) return false;

		return true;
	}
	
	/**
	 * Get the root folder with the specified name.
	 * 
	 * @param name
	 * @return the folder information or null if not found.
	 * 
	 */
	public TreeFolderInfo getRootFolder(String name) {
		TreeFolderInfo folder = null;
		for (TreeFolderInfo f : rootFolders) {
			if (f.getName().equals(name)) {
				folder = f;
			}
		}
		return folder;
	}
	
	/**
	 * Add a new root folder to this file database with the specified display name and
	 * unique file name.  This creates a folder in the file system and adds the
	 * folder information to the list of root folders.
	 * 
	 * @param displayName - name to display to the user
	 * @param uniqueName - unique file name to be used to save to the file system
	 * 
	 * @return the created TreeFolderInfo
	 * @throws LocationAlreadyExistsException 
	 */
	public TreeFolderInfo createRootFolder(String displayName, String uniqueName) throws LocationAlreadyExistsException {
		TreeFolderInfo folder = FileSystemManager.createFolder(uniqueName, this);
		folder.setName(uniqueName);
		folder.setExists(true);
		folder.setDisplayName(displayName);
		folder.setFileDatabase(this);
		rootFolders.add(folder);
		return folder;
	}
	
	/**
	 * Add the existing folder as a root folder in the file 
	 * database.
	 * 
	 * @param folder - folder which exists at some lower level in the 
	 * file system
	 */
	void addExistingFolder(TreeFolderInfo folder)
	{
		// remove the child from the parent
		if(folder.getParent() != null )
		{
			folder.getParent().removeChild(folder);
		}

		// remove the folder from the file database
		if( folder.getFileDatabase() != null )
		{
			if( folder.getFileDatabase().equals(this))
			{
				return;
			}
			else
			{
		        folder.getFileDatabase().removeRootFolder(folder);
			}
		}

		// add the folder to this file database
		folder.setFileDatabase(this);
		
		// set the folder as it's own root.
		folder.setParent(null);
		
		folder.updatePaths(null);
		
		// we must renumber all the nodes ( or shift them
	    // back where the root stats at 1 ).
		if( folder.getLeftValue() != 1)
		{
		    Long shiftValue = 1l - folder.getLeftValue(); 
		    folder.updateLeftRightValues(shiftValue,folder.getLeftValue());
		}
		
		// add the filder to the list
		rootFolders.add(folder);
	}
	
	/**
	 * Remove the root folder from this database. The child must be in the direct
	 * decendents
	 * 
	 * @param child -
	 *            child to remove
	 * @return true if the child is removed
	 */
	public boolean removeRootFolder(TreeFolderInfo folder) {
		boolean deleted = true;

		if (!rootFolders.contains(folder)) {
			return false;
		}
		rootFolders.remove(folder);
		
		if( !FileSystemManager.deleteFolder(folder) )
		{
			log.error("Folder " + folder.getFullPath() + " Could not be deleted");
			throw new IllegalStateException("File " + folder.getFullPath() 
					+ " deleted from parent folder "+ this.getFullPath()  
					+ "but not removed from file system ");	
		}
		return deleted;
	}
	
	/**
	 * Write the string representation of the 
	 * file database.
	 *  
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Default File Database id = ");
		sb.append(id);
		sb.append(" name = ");
		sb.append(name);
		sb.append(" path = ");
		sb.append(getPath());
		sb.append("]");
	
		return sb.toString();
	}

	/**
	 * Get the path for this file database.
	 * 
	 * @return
	 */
	public String getPath() {
		return prefix + path ;
	}
	
	/**
	 * Get the full path for this file database
	 * This includes the file database name.
	 * 
	 * @return
	 */
	public String getFullPath()
	{
		return getPath()  + name + IOUtils.DIR_SEPARATOR;
	}
	
	/**
	 * Set the path of the folder. This makes no changes to the children
	 * folders. The path must be a full path and cannot be relative. The path
	 * must also include the prefix i.e. C:/ (for windows) or / (for unix).
	 * 
	 * 
	 * This converts the paths to the correct path immediately / for *NIX and \
	 * for windows.
	 * 
	 * @param path
	 */
	 void setPath(String path) {

		path = FilenameUtils.separatorsToSystem(path.trim());

		// add the end separator
		if (path.charAt(path.length() - 1) != IOUtils.DIR_SEPARATOR) {
			path = path + IOUtils.DIR_SEPARATOR;
		}

		// get the prefix
		prefix = FilenameUtils.getPrefix(path).trim();

		// the prefix cannot be null.
		if (prefix == null || prefix.equals("")) {
			throw new IllegalArgumentException("Path must have a prefix");
		}
		
		this.path = FilenameUtils.getPath(path);
		

	}

	/**
	 * This will be the prefix for the file 
	 * system. For unix it will be '/' and
	 * for windows systems it will be 'C:\' or
	 * similar 
	 * 
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * This will be the prefix for the file 
	 * system. For unix it will be '/' and
	 * for windows systems it will be 'C:\' or
	 * similar 
	 * 
	 * @param prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Unmodifiable Set of root folders for this file database.
	 * 
	 * @return the set of root folders
	 */
	public Set<TreeFolderInfo> getRootFolders() {
		return Collections.unmodifiableSet(rootFolders);
	}

	/**
	 * Set of root folders for this file database.
	 * 
	 * @param rootFolders
	 */
	void setRootFolders(Set<TreeFolderInfo> rootFolders) {
		this.rootFolders = rootFolders;
	}
	
	/**
	 * Delete all root folders from this file Database.
	 * This also deletes the files and folders in this 
	 * database from the file system.
	 * 
	 */
	void deleteRootFolders()
	{
		//set the path for all root folders
		for( TreeFolderInfo info : rootFolders )
		{
			if( FileSystemManager.deleteFolder(info) )
			{
				throw new IllegalStateException("Could not delete folder " + info.toString());
			}
		}
	}

	/**
	 * Name displayed to the user.
	 * 
	 * @return
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Set the name displayed to the users.
	 * 
	 * @param displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Get the description.
	 * 
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set the description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the name
	 * 
	 * @return name of the file database
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name.  This does not 
	 * change the name in the file system.
	 * 
	 * @param name
	 * @see rename
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Save the file to the file system.  If the unique file name has
	 * an extension, the extension will be removed.  The extension
	 * must be added to the file info object.
	 * @throws IllegalFileSystemNameException 
	 * 
	 * @see edu.ur.file.db.FileDatabase#addFile(java.io.File, java.lang.String)
	 */
	public DefaultFileInfo addFile(File f, String uniqueFileName) throws IllegalFileSystemNameException {
		return currentFileFolder.createFileInfo(f, uniqueFileName);
	}
	
	/**
	 * Create a new empty file with the specified file in the 
	 * file system. If the unique file name has
	 * an extension, the extension will be removed.  The extension
	 * must be added to the file info object.
	 * @throws IllegalFileSystemNameException 
	 * 
	 * @see edu.ur.file.db.FileDatabase#addFile(java.lang.String)
	 */
	public DefaultFileInfo addFile(String uniqueFileName) throws IllegalFileSystemNameException
	{
		return currentFileFolder.createFileInfo(uniqueFileName);
	}

	/**
	 * Location where all files are stored.
	 * 
	 * @return the folder information where files are stored
	 */
	public TreeFolderInfo getCurrentFileStore() {
		return currentFileFolder;
	}

	/**
	 * Set the default folder to store all the files.
	 * 
	 * @param currentFileFolder
	 * @return true if the folder is changed.
	 */
	public boolean setCurrentFileStore(String folderName) {
		
		TreeFolderInfo folder = findFolder(folderName);
		boolean changed = false;
		
		if( folder != null)
		{
		    this.currentFileFolder = folder;
		    changed = true;
		}
		
		return changed;
	}
	
	
	/**
	 * Recursively look through the tree to find the specified folder.
	 * 
	 * @param uniqueFolderName
	 * @return
	 */
	TreeFolderInfo findFolder(String uniqueFolderName)
	{
		TreeFolderInfo folder = null;
		
		folder = getRootFolder(uniqueFolderName);
		if( folder == null )
		{
			Iterator<TreeFolderInfo> iter = rootFolders.iterator();
			while( iter.hasNext() && folder == null)
			{
				TreeFolderInfo f = iter.next();
				folder = f.findFolderInTree(uniqueFolderName);
			}
		}
		
		return folder;
	}
	

	/**
	 * Returns all top level file paths managed by this file system
	 * 
	 * @see edu.ur.file.db.FileDatabase#getAllFilePaths()
	 */
	public Set<String> getAllFilePaths() {
		HashSet<String> filePaths = new HashSet<String>();
		
		for(TreeFolderInfo rootFolder : rootFolders)
		{
			filePaths.add(rootFolder.getFullPath());
		}
		return filePaths;
	}

	/**
	 * Find the file using the specified file id.
	 * 
	 * @see edu.ur.file.db.FileDatabase#getFile(java.lang.Long)
	 */
	public DefaultFileInfo getFile(Long id) {
		boolean found = false;
		
		DefaultFileInfo fileInfo = null;
		
		Iterator<TreeFolderInfo> iter = rootFolders.iterator();
		while( !found && iter.hasNext() )
		{
		    TreeFolderInfo tfi = iter.next();
		    fileInfo = tfi.getFileInfoInTree(id);
		    if(fileInfo != null)
		    {
		    	found = true;
		    }
	    }
		return fileInfo;
	}

	/**
	 * Remove the file from the database using the unique file name.
	 * 
	 * @see edu.ur.file.db.FileDatabase#removeFile(java.lang.String)
	 */
	public boolean removeFile(String uniqueFileName) {
		boolean removed = false;
		
		Iterator<TreeFolderInfo> iter = rootFolders.iterator();
		while( !removed && iter.hasNext() )
		{
		    TreeFolderInfo tfi = iter.next();
		    removed = tfi.removeFileInfoFromTree(uniqueFileName);
		}
		return removed;
	}
	
	
	/**
	 * Get an input stream to the file.
	 * 
	 * @see edu.ur.file.db.FileDatabase#getFileInputStream(java.lang.Long)
	 */
	public FileInputStream getInputStream(Long id)
	{
		FileInputStream is = null;
		FileInfo fileInfo = this.getFile(id);
		
		if( fileInfo != null)
		{
			File f = new File(fileInfo.getFullPath());
			if(f.exists())
			{
				try{
					is = new FileInputStream(f);
				}
			    catch(Exception e)
			    {
			    	throw new RuntimeException(e);
			    }
			}
		}
		return is;
	}
	
	/**
	 * Get an input stream to the file.
	 * 
	 * @see edu.ur.file.db.FileDatabase#getFileInputStream(java.lang.Long)
	 */
	public FileInputStream getInputStream(String uniqueFileName)
	{
		FileInputStream is = null;
		FileInfo fileInfo = this.getFile(uniqueFileName);
		
		if( fileInfo != null)
		{
			File f = new File(fileInfo.getFullPath());
			if(f.exists())
			{
				try{
					is = new FileInputStream(f);
				}
			    catch(Exception e)
			    {
			    	throw new RuntimeException(e);
			    }
			}
		}
		return is;
	}

	/**
	 * Remove the file from the database using the unique id.
	 * 
	 * @see edu.ur.file.db.FileDatabase#removeFile(java.lang.Long)
	 */
	public boolean removeFile(Long id) {
		boolean removed = false;
		
		Iterator<TreeFolderInfo> iter = rootFolders.iterator();
		while( !removed && iter.hasNext() )
		{
		    TreeFolderInfo tfi = iter.next();
		    removed = tfi.removeFileInfoFromTree(id);
		}
		return removed;
	}

	/**
	 * Get a file info using the unique file name.  This is a recursive 
	 * lookup and can be very expensive depending on the size of the tree.
	 * 
	 * @see edu.ur.file.db.FileDatabase#getFile(java.lang.String)
	 */
	public DefaultFileInfo getFile(String uniqueFileName) {
		boolean found = false;
		
		DefaultFileInfo fileInfo = null;
		
		Iterator<TreeFolderInfo> iter = rootFolders.iterator();
		while( !found && iter.hasNext() )
		{
		    TreeFolderInfo tfi = iter.next();
		    fileInfo = tfi.getFileInfoInTree(uniqueFileName);
		    if(fileInfo != null)
		    {
		    	found = true;
		    }
	    }
		return fileInfo;
	}
	
	/**
	 * Find the root folder with the specified unique name.
	 * 
	 * @param uniqueFolderName
	 * @return the found folder or null if the folder is not found
	 */
	public TreeFolderInfo findRootFolder(String uniqueFolderName)
	{
		Iterator<TreeFolderInfo> iter = rootFolders.iterator();
		while( iter.hasNext() )
		{
			TreeFolderInfo rootFolder = iter.next();
			if(rootFolder.getName().endsWith(uniqueFolderName))
			{
				return rootFolder;
			}
		}
		return null;
	}

	
	/**
	 * This creates a root folder in the database.
	 * @throws LocationAlreadyExistsException 
	 * 
	 * @see edu.ur.file.db.FileDatabase#createFolder(java.lang.String)
	 */
	public FolderInfo createFolder(String uniqueName) throws LocationAlreadyExistsException {
		return createRootFolder(null, uniqueName);
	}

	/**
	 * Get the root folder by unique name
	 * 
	 * @see edu.ur.file.db.FileDatabase#getFolder(java.lang.String)
	 */
	public FolderInfo getFolder(String uniqueName) {
		return this.getRootFolder(name);
	}
}
