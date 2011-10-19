/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.groupspace;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.user.IrUser;
import edu.ur.persistent.LongPersistentId;
import edu.ur.persistent.PersistentVersioned;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;
import edu.ur.tree.PreOrderTreeSetNodeBase;

/**
 * Represents a folder for a group space.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceFolder extends PreOrderTreeSetNodeBase implements
Serializable, LongPersistentId, PersistentVersioned,
DescriptionAware, NameAware, Comparable<GroupWorkspaceFolder>, FileSystem{

	/* eclipse generated id */
	private static final long serialVersionUID = 5447153628861451307L;
	
	/* Logger */
	private static final Logger log = Logger.getLogger(GroupWorkspaceFolder.class);
	
	/* name of the folder */
	private String name;
	
	/* set of children folders */
	private Set<GroupWorkspaceFolder> children = new HashSet<GroupWorkspaceFolder>();
	
	/* description for the group folder */
	private String description;
	
	/*  Root of the entire tree. */
	private GroupWorkspaceFolder treeRoot;
	
	/* user who created the folder */
	private IrUser owner;
	
	/* group that owns the folder */
	private GroupWorkspace groupWorkspace;
	
	/*  Files in this personal folder. This is a set of versioned files. */
	private Set<GroupWorkspaceFile> files = new HashSet<GroupWorkspaceFile>();

	/*
	 * This is the conceptual path to the group folder.
	 * The base path plus the root of the tree 
	 * down to itself.
	 * For example if you have a root folder named FOO and
	 * the following sub folders 
	 * A, B, C and D and A is a parent of B and 
	 * B is a parent of C and C is a parent of D Then the
	 * paths are as follows:
	 * 
	 *  /FOO/A/
	 *  /FOO/A/B
	 *  /FOO/A/B/C
	 *  /FOO/A/B/C/D
	 * 
	 */
	private String path;
	
	/* Unique id of the group folder. */
	private Long id;
	
	/*  Version of the database data read from the database. */
	private int version;
	
	/**  user will be given read permission on all files/folders within folder as they are added */
	public static String FOLDER_READ_PERMISSION = "GROUP_WORKSPACE_FOLDER_READ";
	
	/**  user can add/edit/delete files and folders within the folder and the specified folder */
	public static String FOLDER_EDIT_PERMISSION = "GROUP_WORKSPACE_FOLDER_EDIT";
	
	/** user can add/modifiy files they add but cannot modify files they did not add */
	public static String FOLDER_ADD_FILE_PERMISSION = "GROUP_WORKSPACE_FOLDER_ADD_FILE";
	
	/**
	 * Package protected constructor
	 */
	GroupWorkspaceFolder()
	{
		this.treeRoot = this;	
	}
	
	/**
	 * Create a group folder with the given name.
	 * 
	 * @param groupSpace - parent group space
	 * @param name - name of the folder
	 * 
	 * @throws IllegalFileSystemNameException - if name contains illegal file system characters
	 */
	GroupWorkspaceFolder(GroupWorkspace groupSpace, IrUser owner, String name) throws IllegalFileSystemNameException
	{
		setName(name);
		setTreeRoot(this);
		setPath(PATH_SEPERATOR);
		setGroupWorkspace(groupSpace);
		setOwner(owner);
	}
	
	/**
	 * Set the parent of this personal folder.  Results
	 * in setting path and all children paths.
	 * 
	 * @param parent
	 */
	void setParent(GroupWorkspaceFolder parent) {
		if(parent == null)
		{
			path = PATH_SEPERATOR ;
		}
		super.setParent(parent);
	}
	
	/**
	 * Adds an existing personal file to this folder.  This allows
	 * a personal file to be moved from one location to another.
	 * 
	 * @param pf - personal file to add
	 * @throws DuplicateNameException - if the file already exits
	 */
	public void addGroupFile(GroupWorkspaceFile gf) throws DuplicateNameException
	{
		if( !isVaildFileSystemName(gf.getVersionedFile().getNameWithExtension()))
		{
			throw new DuplicateNameException("Personal file with the name " + gf.getVersionedFile().getNameWithExtension() +
				" already exists", gf.getVersionedFile().getNameWithExtension());
		}
		if( gf.getGroupWorkspaceFolder() != null)
		{
		    gf.getGroupWorkspaceFolder().removeGroupFile(gf);
		}
		
		gf.setGroupWorkspaceFolder(this);
		files.add(gf);
	}
	
	/**
	 * Add a versioned file to this folder.  this creates a 
	 * new group file record.
	 * 
	 * @param vf
	 */
	public GroupWorkspaceFile addVersionedFile(VersionedFile vf) throws DuplicateNameException
	{
		 if( !isVaildFileSystemName(vf.getNameWithExtension()))
		 {
			 throw new DuplicateNameException("A file or folder with the name " + vf.getNameWithExtension() +
						" already exists in this folder", vf.getNameWithExtension());
		 }

         GroupWorkspaceFile pf = new GroupWorkspaceFile(vf, this);
		 files.add(pf);
		 return pf;
	}
	
	
	/**
	 * Returns the group folder type.
	 * 
	 * @see edu.ur.ir.FileSystem#getFileSystemType()
	 */
	public FileSystemType getFileSystemType() {
		return FileSystemType.GROUP_FOLDER;
	}

	/**
	 * Get the path for this folder.  Does not include the folder
	 * 
	 * @see edu.ur.ir.FileSystem#getPath()
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Description for the group folder.
	 * 
	 * @see edu.ur.simple.type.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set the description.
	 * 
	 * @param description - description for the group folder
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Get the name of the group folder.
	 * 
	 * @see edu.ur.simple.type.NameAware#getName()
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of the group folder.
	 * 
	 * @param name
	 */
	void setName(String name) throws IllegalFileSystemNameException {
		this.name = name;
	}
	
	/**
	 * Rename this group folder.
	 * 
	 * @param name - new name to give the folder
	 * @throws DuplicateNameException - if the name already exists in as a file or folder at the current
	 * level of the folder structure
	 * @throws IllegalFileSystemNameException - if the name contains illegal characters within the name
	 */
	public void reName(String name) throws DuplicateNameException, IllegalFileSystemNameException
	{
		GroupWorkspaceFolder parent = getParent();
		if( parent != null )
		{
			if( !parent.isVaildFileSystemName(name) )
			{
				throw new DuplicateNameException("A file or folder with the name " + name +
						" already exists in this folder", name );
			}
		}
		setName(name);
		this.updatePaths(getPath());
	}
	
	/**
	 * Returns true if the name is ok to add to a file or folder
	 * 
	 * @param name of the file or folder.  If it is a file, it should contain
	 * the extension.
	 * 
	 * @return true if the name does not exist.  This is case insensitive.
	 */
	public boolean isVaildFileSystemName(String name)
	{
		boolean ok = false;
		if( getChild(name) == null && getFile(name) == null)
		{
			ok = true;
		}
		return ok;
	}
	
	/**
	 * Find a file based on the name.  If
	 * no file is found a null object is returned.  This
	 * is case in-sensitive
	 * 
	 * @param name of the file including the extension
	 * @return the found file
	 */
	public GroupWorkspaceFile getFile(String nameWithExtension)
	{
		for(GroupWorkspaceFile gf : files)
		{
			if( gf.getVersionedFile().getNameWithExtension().equalsIgnoreCase(nameWithExtension))
			{
				return gf;
			}
		}
		
		return null;
	}
	
	/**
	 * Remove the child from the set of children. 
	 * Renumbers the tree to be correct.
	 * 
	 * @param child
	 * @return true if the child is removed.
	 */
	public boolean removeChild(GroupWorkspaceFolder child)
	{
		boolean removed = false;
		
		if( children.remove(child) )
		{
			log.debug("removing child " + child);
			removed = true;
			
			// re-number the tree
			cleanUpTree(child.getTreeSize(), child.getRightValue());
			log.debug( "child removed tree values = " + this);
			child.setParent(null);
	    }
		return removed;
	}
	
	/**
	 * Add a child folder to this personal folder's set of children.
	 * 
	 * @param child to add
	 */
	public void addChild(GroupWorkspaceFolder child) throws DuplicateNameException
	{
		if( child.equals(this))
		{
			throw new IllegalStateException("cannot add a personal folder to " +
					"itself as a child");
		}
		if(!isVaildFileSystemName(child.getName()))
		{
			throw new DuplicateNameException("Folder with the name " + 
					child.getName() + " already exists ", child.getName());
		}
		if (!child.isRoot()) {
			GroupWorkspaceFolder childParent = child.getParent();
			childParent.removeChild(child);
		}

		makeRoomInTree(child);
		child.setParent(this);
		child.setGroupWorkspace(this.groupWorkspace);
		child.setTreeRoot(getTreeRoot());
		children.add(child);
	}
	
	/**
	 * Create a new child folder for this 
	 * folder.
	 * 
	 * @param name of the child folder.
	 * @return the created folder
	 * 
	 * @throws IllegalArgumentException if a name of a folder that
	 * already exists is passed in.
	 */
	public GroupWorkspaceFolder createChild(String name, IrUser owner) throws DuplicateNameException, IllegalFileSystemNameException
	{
	    GroupWorkspaceFolder c = new GroupWorkspaceFolder();
	    c.setOwner(owner);
		c.setName(name);
		addChild(c);
		return c;
	}
	
	/**
	 * Find a Folder based on the name.  If
	 * no Folder is found a null object is returned.
	 * 
	 * This is <b>NOT</b>  a recursive operation and only searches
	 * the current list of children.
	 * 
	 * The search is case insensitive
	 * 
	 * @param name of the child personal folder.
	 * @return the found personal folder.
	 */
	public GroupWorkspaceFolder getChild(String name)
	{
		if( log.isDebugEnabled() )
		{
			log.debug("Searching for group folder " + name);
		}
		
		boolean found = false;
		Iterator<GroupWorkspaceFolder> iter = children.iterator();
		
		while( iter.hasNext() && !found)
		{
			GroupWorkspaceFolder c = iter.next();
			if( c.getName().equalsIgnoreCase(name))
			{
				return c;
		    }
		}
	    // not found
		return null;
	}	
	
	/**
	 * Get the path of the group folder plus the name and
	 * ending path separator.
	 * 
	 * @return full path
	 */
	public String getFullPath()
	{
		return getPath() + name + PATH_SEPERATOR;
	}
	
	/**
	 * Set the conceptual path for this collection.
	 * This DOES NOT update children paths.
	 * 
	 * @see updatePaths
	 * 
	 * @param path
	 */
	void setPath(String path) {
		
		path = path.trim();
		
        // verify the path is correct
		verifyPath(path);

		// add the end seperator
		if (!path.substring(path.length()-1, path.length()).equals(PATH_SEPERATOR)) {
			path = path + PATH_SEPERATOR;
		}
		
		this.path = path;
	}
	
	/**
	 * Make sure the path is correct. A root folder always returns true.
	 * 
	 * @param path -
	 *            the path of this object
	 * @return - true if the parent's full path matches this folders path or
	 *         this is the root folder.
	 * 
	 * 
	 */
	private boolean verifyPath(String path) {
		boolean valid = true;
		
        //make sure there is a beginning separator
		if(!path.substring(0,1).equals(PATH_SEPERATOR))
		{
		    throw new IllegalStateException("No beginning path Seperator path = " + path);
		}
		else if (!isRoot()) {
			if (!(path.equals(parent.getFullPath()))) {
				throw new IllegalArgumentException("Path is : " + path
						+ " but should equal parent path "
						+ " plus parents name which = " + parent.getFullPath());
			}
		}
		return valid;
	}

	/**
	 * Get the direct children of this folder
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getChildren()
	 */
	public Set<GroupWorkspaceFolder> getChildren() {
		return Collections.unmodifiableSet(children);
	}
	
	/**
	 * Get the set of group workspace files.
	 * 
	 * @return list of group workspace files
	 */
	public Set<GroupWorkspaceFile> getFiles()
	{
		return Collections.unmodifiableSet(files);
	}
	
	/**
	 * List of children for the group folder.
	 * 
	 * @param children
	 */
	void setChildren(Set<GroupWorkspaceFolder> children)
	{
		this.children = children;
	}
	
	/**
	 * Set the files for this group workspace folder
	 * 
	 * @param files - list of files for this group workspace folder
	 */
	void setFiles(Set<GroupWorkspaceFile> files)
	{
		this.files = files;
	}

	/**
	 * Return the parent folder.
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getParent()
	 */
	public GroupWorkspaceFolder getParent() {
		return (GroupWorkspaceFolder) parent;
	}

	/**
	 * Set the root folder.  This will cast the root to a GroupFolder
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#setRoot(edu.ur.tree.PreOrderTreeSetNodeBase)
	 */
	protected void setRoot(PreOrderTreeSetNodeBase root) {
		this.treeRoot = (GroupWorkspaceFolder)root;
	}

	/**
	 * Update the paths of all the children.
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#updatePaths(java.lang.String)
	 */
	protected void updatePaths(String path) {
		setPath(path);
		for(GroupWorkspaceFolder folder: children)
		{
			folder.updatePaths(getFullPath());
		}
	}

	/**
	 * Get the unique id of the group folder.
	 * 
	 * @see edu.ur.persistent.LongPersistentId#getId()
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Set the id of this group folder
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Version data for persistence.
	 * 
	 * @see edu.ur.persistent.PersistentVersioned#getVersion()
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Compares objects based on their names.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(GroupWorkspaceFolder other) {
		GroupWorkspaceFolder c = (GroupWorkspaceFolder)other;
		return this.getName().compareTo(c.getName());
	}
	
	/**
	 * Returns the root folder
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getRoot()
	 */
	public GroupWorkspaceFolder getTreeRoot() {
		return treeRoot;
	}
	
	/**
	 * Set the tree root.
	 * 
	 * @param treeRoot
	 */
	public void setTreeRoot(GroupWorkspaceFolder treeRoot) {
		this.treeRoot = treeRoot;
	}
	
	
	/**
	 * Hash code is based on the path and name of
	 * the collection.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		value += getPath() == null? 0 : getPath().hashCode();
		return value;
	}
	
	/**
	 * Remove a file from the personal folder
	 * 
	 * @param personalFile to remove from the folder
	 * @return true if the personal file is removed
	 */
	public boolean removeGroupFile(GroupWorkspaceFile gf)
	{
		return files.remove(gf);
	}
	
	/**
	 * Equals is tested based on name and path of the
	 * object.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupWorkspaceFolder)) return false;

		final GroupWorkspaceFolder other = (GroupWorkspaceFolder) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		if( ( getPath() != null && !getPath().equals(other.getPath()) ) ||
		    ( getPath() == null && other.getPath() != null ) ) return false;

		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ Group folder id = ");
		sb.append(id);
		sb.append(" left value = ");
		sb.append(leftValue);
		sb.append(" right value = ");
		sb.append(rightValue);
		sb.append( " name = ");
		sb.append(name);
		sb.append(" description = ");
		sb.append(description);
		sb.append( " path = ");
		sb.append(path);
		sb.append("]");
		return sb.toString();
	}

	
	/**
	 * User who owns/created the folder
	 * @return
	 */
	public IrUser getOwner() {
		return owner;
	}

	/**
	 * User who owns / created the folder.
	 * 
	 * @param owner
	 */
	public void setOwner(IrUser owner) {
		this.owner = owner;
	}
	
	/**
	 * Get the group space that owns the folder.
	 * 
	 * @return - group space that contains the folder
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}

	/**
	 * Set the group space that owns the folder.
	 * 
	 * @param groupSpace that contains the folder
	 */
	public void setGroupWorkspace(GroupWorkspace groupSpace) {
		this.groupWorkspace = groupSpace;
	}
	
}
