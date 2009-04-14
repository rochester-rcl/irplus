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
import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import edu.ur.tree.PreOrderTreeSetNodeBase;

/**
 * Tree of folders that represent folders in a file system. It is assumed a
 * folder path always ends with a /.
 * 
 * @author Nathan Sarr
 * 
 */
public class TreeFolderInfo extends PreOrderTreeSetNodeBase implements
		Serializable, FolderInfo {
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(TreeFolderInfo.class);

	/**  Eclipse Generated id. */
	private static final long serialVersionUID = -714164342008847164L;

	/**  The root of the entire tree. */
	private TreeFolderInfo treeRoot;

	/**  Children of this node. */
	private Set<TreeFolderInfo> children = new LinkedHashSet<TreeFolderInfo>();

	/**  The unique id of the folder type */
	private Long id;

	/**  Version of the database the value has been read from. */
	private int version;

	/**  The name of the folder displayed to the user */
	private String displayName;

	/**  The name of the folder in the file system */
	private String name;

	/**  The description of the folder */
	private String description;

	/**  Indicates if the folder is in the file system */
	private boolean exists = false;

	/**  The path of the folder not including the base path */
	private String path;
	
	/**  Date the folder was created */
	private Date createdDate;
	
	/** Date the folder was modified */
	private Date modifiedDate;

	/**  FileInfos in this folder */
	private Set<DefaultFileInfo> files = new LinkedHashSet<DefaultFileInfo>();

	/**  The file database this file is in */
	private DefaultFileDatabase fileDatabase;

	/**
	 * Default constructor
	 */
	TreeFolderInfo() {
		treeRoot = this;
	}

	/**
	 * Add an existing folder to this tree. Removes the child from any existing parent.
	 * This is compareble to a move.
	 * 
	 * @param child
	 *            to add
	 * @return true if the child is added
	 */
	public boolean addChild(TreeFolderInfo child) {
		boolean added = false;
		if (log.isDebugEnabled()) {
			log.debug("Adding child " + child.toString());
			if( (child.getParent() != null) && child.getParent().equals(this))
			{
				log.debug("child already child of this folder" );
			}
		}
		
		if( (child.getParent() != null) && child.getParent().equals(this) )
		{
			added = true;
		}
		else
		{
		    // copy the directory to the new location.
		    String beforeMovePath = child.getFullPath();
		    String newChildPath = this.getFullPath() + 
		             child.getName() + IOUtils.DIR_SEPARATOR;

		    FileSystemManager.renameFile(new File(beforeMovePath), new File(newChildPath));
		
		    // remove the child from the old parent node.
		    if (!child.isRoot()) {
			    TreeFolderInfo childParent = child.getParent();
			    childParent.removeChild(child);
			    childParent.cleanUpTree(child.getTreeSize(), child.getRightValue());
		    }
		    else if( (child.getName() != null) && (child.getFullPath() != null) )
		    {
	            FileSystemManager.deleteDirectory(child.getFullPath());
		    }

		    makeRoomInTree(child);
		
		    if( !fileDatabase.equals(child.getFileDatabase()))
		    {
		        child.updateFileDatabase(fileDatabase);
		    }
		
		    child.setExists(true);
		    child.setParent(this);
		
		    children.add(child);
		    added = true;
		}
		return added;
	}

	/**
	 * Add file information for a file in this folder. Removes the child from
	 * any existing parent.
	 * 
	 * @param file to move
	 */
	void addFileInfo(DefaultFileInfo file) {
		if (file.getFolderInfo() != null) {
			file.getFolderInfo().removeFileInfo(file);
		}

		file.setFolderInfo(this);
		files.add(file);
	}

	/**
	 * Add a new child folder to this folder with the specified display name and
	 * unique file name.  This creates a folder in the file system.
	 * 
	 * @param displayName - name that can be displayed to the user.
	 *   
	 * @param uniqueName - unique name to be given - this name must be
	 * unique against all files in the file database management system.
	 * 
	 * @return the created TreeFolderInfo
	 * @throws LocationAlreadyExistsException - if the location already exists 
	 */
	public TreeFolderInfo createChild(String displayName, String uniqueName) throws LocationAlreadyExistsException  {
		TreeFolderInfo child = FileSystemManager.createChildFolder(this,
				uniqueName);
		child.setName(uniqueName);
		child.setExists(true);
		makeRoomInTree(child);
		child.setDisplayName(displayName);
		child.updateFileDatabase(fileDatabase);
	    child.setParent(this);
	    children.add(child);
		return child;
	}

	/**
	 * Add a file to the file system for the specified folder. 
	 * If the unique file name has
	 * an extension, the extension will be removed.  The extension
	 * must be added to the returned file info object.
	 * 
	 * @param f
	 *            the file to add
	 * @param uniqueName
	 *            name to use in the file system
	 * 
	 * @return the information about the added file.
	 */
	public DefaultFileInfo createFileInfo(File f, String uniqueName) {
		DefaultFileInfo info = FileSystemManager.addFile(this, f, uniqueName);
		addFileInfo(info);
		return info;
	}
	
	/**
	 * Add an empty file with the specified name to the file system. 
	 * If the unique file name has
	 * an extension, the extension will be removed.  The extension
	 * must be added to the returned file info object.
	 * 
	 * @param uniqueName
	 *            name to use in the file system
	 * 
	 * @return the information about the added file.
	 */
	public DefaultFileInfo createFileInfo(String uniqueName) {
		DefaultFileInfo info = FileSystemManager.addFile(this, uniqueName);
		addFileInfo(info);
		return info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof TreeFolderInfo))
			return false;

		final TreeFolderInfo other = (TreeFolderInfo) o;

		if ((name != null && !name.equals(other.getName()))
				|| (name == null && other.getName() != null))
			return false;

		if ((getPath() != null && !getPath().equals(other.getPath()))
				|| (getPath() == null && other.getPath() != null))
			return false;

		return true;
	}

	/**
	 * Get the base path.
	 * 
	 * @return the base path
	 */
	public String getBasePath() {
		if( fileDatabase == null )
		{
			return null;
		}
		else
		{
		    return fileDatabase.getFullPath();
		}
	}

	/**
	 * Return the folder with the specified id in the folder. This is NOT
	 * recursive.
	 * 
	 * @param id
	 *            the id of the child
	 * 
	 * @return the folderInfo or null if it is not found.
	 */
	public TreeFolderInfo getChild(Long id) {
		TreeFolderInfo child = null;
		if (id == 0) {
			return child;
		}

		for (TreeFolderInfo f : children) {
			if (f.getId().equals(id)) {
				child = f;
			}
		}
		return child;
	}

	/**
	 * Get the child folder with the specified name.
	 * 
	 * @param name
	 * @return the folder information or null if not found.
	 * 
	 */
	public TreeFolderInfo getChild(String name) {
		TreeFolderInfo child = null;
		for (TreeFolderInfo f : children) {
			if (f.getName().equals(name)) {
				child = f;
			}
		}
		return child;
	}

	/**
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getChildren()
	 * 
	 * Return the set of children. This is an un-modifiable set.
	 * 
	 * @return the children folders.
	 */
	@Override
	public Set<TreeFolderInfo> getChildren() {
		return Collections.unmodifiableSet(children);
	}

	/**
	 * Get the description of this folder
	 * 
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * The name to be displayed to the user.
	 * 
	 * @return Returns the displayName.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return Returns the fileDatabase.
	 */
	public DefaultFileDatabase getFileDatabase() {
		return fileDatabase;
	}

	/**
	 * Get the information for a file with the specified id.  This
	 * only retires the file from this folder, it is not recursive.
	 * 
	 * @param id
	 *            unique id for the file
	 * @return
	 */
	public DefaultFileInfo getFileInfo(Long id) {
		DefaultFileInfo file = null;
		if (id == 0) {
			return file;
		}

		for (DefaultFileInfo f : files) {
			if (f.getId() != null && f.getId().equals(id)) {
				file = f;
			}
		}
		return file;
	}
	
	/**
	 * Calls into the tree (recursive) to find the file info.  This
	 * can be a very expensive call depending on the size
	 * of the tree.
	 * 
	 * @param id - id of the file to get.
	 * @return the file info when found otherwise null
	 */
	public DefaultFileInfo getFileInfoInTree(Long id)
	{
		boolean found = false;
		DefaultFileInfo fileInfo = getFileInfo(id);
		
		if( fileInfo == null )
		{
		    Iterator<TreeFolderInfo> iter = children.iterator();
		    while( !found && iter.hasNext() )
		    {
			    TreeFolderInfo tfi = iter.next();
			    fileInfo = tfi.getFileInfoInTree(id);
			    if(fileInfo != null)
			    {
			    	found = true;
			    }
		    }
		}
		return fileInfo;
	}
	
	/**
	 * Calls into the tree (recursive) to find the file info.  This
	 * can be a very expensive call depending on the size
	 * of the tree.
	 * 
	 * @param uniqueFileName - name of the file info object.
	 * @return the file info when found otherwise null
	 */
	public DefaultFileInfo getFileInfoInTree(String uniqueFileName)
	{
		boolean found = false;
		DefaultFileInfo fileInfo = getFileInfo(uniqueFileName);
		
		if( fileInfo == null )
		{
		    Iterator<TreeFolderInfo> iter = children.iterator();
		    while( !found && iter.hasNext() )
		    {
			    TreeFolderInfo tfi = iter.next();
			    fileInfo = tfi.getFileInfoInTree(uniqueFileName);
			    if(fileInfo != null)
			    {
			    	found = true;
			    }
		    }
		}
		return fileInfo;
	}
	
	/**
	 * Get the file information with the specified name.  This is
	 * a non re-cursive call and only looks in the immediate 
	 * set of child files.
	 * 
	 * @param name
	 *            of the fileName
	 * @return 
	 */
	public DefaultFileInfo getFileInfo(String fileName) {
		DefaultFileInfo file = null;
		for (DefaultFileInfo f : files) {
			if (f.getName().equals(fileName)) {
				file = f;
			}
		}

		return file;
	}

	/**
	 * The set of files in the folder returned as an unmodifiable set.
	 * 
	 * @return Returns the files.
	 */
	public Set<DefaultFileInfo> getFiles() {
		return Collections.unmodifiableSet(files);
	}

	/**
	 * Gets the full path including the folder name this includes the end
	 * separator.
	 * 
	 * @return full path including the folder name.
	 */
	public String getFullPath() {
		String fp = null;

		if (getPath() != null) {
			fp = getPath();
		}
		
		if (fp != null) {
			fp += name + IOUtils.DIR_SEPARATOR;
		}
		
		return fp;
	}
	

	/**
	 * The unique id
	 * 
	 * @return the unique id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Return true if the folder is in the file system.
	 * 
	 * @return
	 */
	public boolean getExists() {
		return exists;
	}

	/**
	 * Get the file systems folders name
	 * 
	 * @return the name of the folder
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getParent()
	 */
	@Override
	public TreeFolderInfo getParent() {
		return (TreeFolderInfo)parent;
	}

	/**
	 * Get the path of the folder.  This will return null if the
	 * file database has not yet been set.
	 * 
	 * @return the path
	 */
	public String getPath() {
		if (isRoot()) {
			return getBasePath();
		} else {
			//make sure the base path is set
			if( getBasePath() != null )
			{
			    return getBasePath() + path;
			}
			else
			{
				return null;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.ur.file.db.PathAware#getPrefix()
	 */
	public String getPrefix() {
		return fileDatabase.getPrefix();
	}

	/**
	 * Returns a TreeFolderInfo instead of a PreOderTreeSetNodeBase.
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getTreeRoot()
	 */
	public TreeFolderInfo getTreeRoot()
	{
		return treeRoot;
	}

	/**
	 * Get the URI of the folder.
	 * 
	 * @return the uri
	 */
	public URI getUri() {
		return new File(getFullPath()).toURI();
	}

	/**
	 * Get the database version of this data
	 * 
	 * @return the database verison
	 */
	public int getVersion() {
		return version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		value += getPath() == null ? 0 : getPath().hashCode();

		return value;
	}
	
	/**
	 * Find the folder in the tree.  This is a recursive 
	 * 
	 * @param uniqueName
	 * 
	 * @return the found folder in the tree or null if it 
	 * is not found
	 */
	TreeFolderInfo findFolderInTree(String uniqueName)
	{
		TreeFolderInfo folder = getChild(uniqueName);
		if( folder == null)
		{
			Iterator<TreeFolderInfo> iter = children.iterator();
			
			while(iter.hasNext() && folder == null)
			{
				TreeFolderInfo f = iter.next();
				folder = f.findFolderInTree(uniqueName);
			}
		}
		
		return folder;
	}

	/**
	 * Remove the child from this tree. The child must be in the direct
	 * decendents
	 * 
	 * @param child -
	 *            child to remove
	 * @return true if the child is removed
	 */
	public boolean removeChild(TreeFolderInfo child) {
		boolean deleted = true;

		if (!children.contains(child)) {
			if( log.isDebugEnabled())
			{
				log.debug("child " + child + " not removed ");
			}
			return false;
		}
		children.remove(child);
		
		// re-number the tree
		cleanUpTree(child.getTreeSize(), child.getRightValue());
		
		if( !FileSystemManager.deleteFolder(child) )
		{
			log.error("Folder " + child.getFullPath() + " Could not be deleted");
			throw new IllegalStateException("File " + child.getFullPath() 
					+ " deleted from parent folder "+ this.getFullPath()  
					+ "but not removed from file system ");	
		}
		return deleted;
	}

	/**
	 * Remove the file information from the folder. Deletes the file from the
	 * file system. Sets the parent of the file info to null.  This is not
	 * a recursive operation.
	 * 
	 * @param f
	 * @return true if the file info is removed
	 */
	public boolean removeFileInfo(DefaultFileInfo f) {
		boolean removed = true;
		
		if (!files.contains(f)) {
			return false;
		}
		
		files.remove(f);
		
		if(!FileSystemManager.deleteFile(f))
		{
			throw new IllegalStateException("Deleted file "
					+ f.getFullPath() + " from "
					+ "the folder information but could not remove from file system ");
		}
		
		f.setFolderInfo(null);
	
		return removed;
	}
	
	/**
	 * Remove a file info from the tree this is a recursive action.
	 * 
	 * @param id - unique id of the tree to remove
	 * 
	 * @return the removed default file info or null if the file info is
	 * not found.
	 */
	public boolean removeFileInfoFromTree(Long id)
	{
		DefaultFileInfo fileInfo = getFileInfo(id);
		boolean removed = false;
		
		if( fileInfo == null )
		{
		    Iterator<TreeFolderInfo> iter = children.iterator();
		    while( iter.hasNext() && fileInfo == null)
		    {
			    TreeFolderInfo tfi = iter.next();
			    fileInfo = tfi.getFileInfo(id);
			    if(fileInfo != null)
			    {
			    	removed = tfi.removeFileInfo(fileInfo);
			    }
			    else
			    {
			    	removed = tfi.removeFileInfoFromTree(id);
			    }
		    }
		}
		else
		{
			removed = removeFileInfo(fileInfo);
		}
		return removed;
	}
	
	/**
	 * Remove the file from the tree using the unique file name - this
	 * is a recursive operation.
	 * 
	 * @param uniqueFileName
	 * @return
	 */
	public boolean removeFileInfoFromTree(String uniqueFileName)
	{
		DefaultFileInfo fileInfo = getFileInfo(uniqueFileName);
		boolean removed = false;
		
		if( fileInfo == null )
		{
		    Iterator<TreeFolderInfo> iter = children.iterator();
		    while( iter.hasNext() && fileInfo == null)
		    {
			    TreeFolderInfo tfi = iter.next();
			    fileInfo = tfi.getFileInfo(uniqueFileName);
			    if(fileInfo != null)
			    {
			    	removed = tfi.removeFileInfo(fileInfo);
			    }
			    else
			    {
			    	removed = tfi.removeFileInfoFromTree(uniqueFileName);
			    }
		    }
		}
		else
		{
			removed = removeFileInfo(fileInfo);
		}
		return removed;
	}

	/**
	 * Set the children folders.
	 * 
	 * @param folders
	 */
	void setChildren(Set<TreeFolderInfo> folders) {
		children = folders;
	}

	/**
	 * Set the description of this folder
	 * 
	 * @param description
	 *            of the folder.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * The name to be displayed to the user.
	 * 
	 * @param displayName
	 *            The displayName to set.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @param fileDatabase
	 *            The fileDatabase to set.
	 */
	void setFileDatabase(DefaultFileDatabase fileDatabase) {
		this.fileDatabase = fileDatabase;
	}

	/**
	 * The set of files in this folder
	 * 
	 * @param files
	 *            The files to set.
	 */
	public void setFiles(Set<DefaultFileInfo> files) {
		this.files = files;
	}

	/**
	 * Set the id
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Set if ithis file is in the file system.
	 * 
	 * @param inFileSystem
	 */
	public void setExists(boolean exists) {
		this.exists = exists;
	}

	/**
	 * Set this folders file system name.
	 * 
	 * @param name
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the parent of this tree
	 * 
	 * @throws IllegalArgument exception if the
	 * path does not equal the parents path.
	 * 
	 * @param parent
	 */
	void setParent(TreeFolderInfo parent) {
		super.setParent(parent);

		
		if( !verifyPath(getPath()))
		{
			throw new IllegalStateException("Path " + getPath() 
					+ " is not legal after changing parent to " + parent.getFullPath());
		}
	}

	/**
	 * Set the path of the folder. This makes no changes to the children
	 * folders. 
	 * 
	 * 
	 * This converts the paths to the correct path immediately / for unix and \
	 * for windows.
	 * 
	 * @param path
	 * @throws IllegalState Exception if the file database is equal to null.
	 */
	void setPath(String inPath) {

		if( fileDatabase == null )
		{
			throw new IllegalStateException("File Database cannot be null");
		}
		if( isRoot() )
		{
			path = null;
		}
		else
		{
		    inPath = FilenameUtils.separatorsToSystem(inPath.trim());

		    // add the end seperator
		    if (inPath.charAt(inPath.length() - 1) != IOUtils.DIR_SEPARATOR) 
		    {
			    inPath = inPath + IOUtils.DIR_SEPARATOR;
		    }

		    // verify the path is valid if this has any parents.
		    if (!verifyPath(inPath)) 
		    {
			    throw new IllegalArgumentException("inPath is : " + inPath
					    + " but should equal parent path "
					    + " plus parents name which = " + parent.getPath());
		    }
		
			String basePath = fileDatabase.getFullPath();
			path = inPath.substring(basePath.length());
		}
	}

	/**
	 * Set the persistent version of the data.
	 * 
	 * @param version
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[FolderInfo id = ");
		sb.append(id);
		sb.append(" display name = ");
		sb.append(displayName);
		sb.append(" folder name = ");
		sb.append(name);
		sb.append(" full path = ");
		sb.append(getFullPath());
		sb.append(" left value = ");
		sb.append(leftValue);
		sb.append(" right value = ");
		sb.append(rightValue);
		sb.append("]");
		
		return  sb.toString();
	}

	/**
	 * Update the tree with the correct file database.
	 * 
	 * @param fileDatabase
	 */
	void updateFileDatabase(DefaultFileDatabase fileDatabase) {
		setFileDatabase(fileDatabase);

		// update the children with the correct
		// database
		for (TreeFolderInfo f : children) {
		    f.updateFileDatabase(fileDatabase);
		}
	}
	
	/**
	 * should be called any time the parent path changes.
	 * 
	 * @param path
	 */
	protected void updatePaths(String path) {
		setPath(path);
		
		// update the children with the correct
		// path		
		for (TreeFolderInfo f : children) {
		    f.updatePaths(getFullPath());
		}
	}

	/**
	 * Make sure the path is correct. A root folder always returns true.
	 * 
	 * @param path -
	 *            the path of this object
	 * @return - true if the parent's full path matches this folders path or
	 *           if this is the root folder then the path must match the 
	 */
	private boolean verifyPath(String path) {
		boolean valid = true;
		if (!isRoot()) {
			log.debug("Path = " + path + " parent path = " + parent.getFullPath() + " equals ? = " + 
					path.equals(parent.getFullPath()) );
			if (!(path.equals(parent.getFullPath()))) {
				
				valid = false;
			}
		}
		return valid;
	}
	
	protected void updateLeftRightValues(Long change, Long minValue)
	{
		super.updateLeftRightValues(change, minValue);
	}

	
	/* (non-Javadoc)
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#setRoot(edu.ur.tree.PreOrderTreeSetNodeBase)
	 */
	protected void setRoot(PreOrderTreeSetNodeBase root) {
		this.treeRoot = (TreeFolderInfo)root;
	}

	/**
	 * Get the data this folder was created.
	 * 
	 * @see edu.ur.file.db.FolderInfo#getCreatedDate()
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Set the date this folder was created.
	 * 
	 * @param createdDate
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Get the modified date.
	 * 
	 * @see edu.ur.file.db.FolderInfo#getModifiedDate()
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * Set the date this folder was created
	 * 
	 * @param modifiedDate
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	
	/**
	 * Get the size in bytes of this folder.
	 * 
	 * @see edu.ur.file.db.FolderInfo#getSize()
	 */
	public Long getSize() {
		return FileUtils.sizeOfDirectory(new File(this.getFullPath()));
	}
}
