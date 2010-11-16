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
import java.util.Set;

import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
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
public class GroupFolder extends PreOrderTreeSetNodeBase implements
Serializable, LongPersistentId, PersistentVersioned,
DescriptionAware, NameAware, Comparable<GroupFolder>, FileSystem{

	/** eclipse generated id */
	private static final long serialVersionUID = 5447153628861451307L;
	
	/** name of the folder */
	private String name;
	
	/** set of children folders */
	private Set<GroupFolder> children;
	
	/** description for the group folder */
	private String description;
	
	/**  Root of the entire tree. */
	private GroupFolder treeRoot;
	
	/** user who created the folder */
	private IrUser owner;
	
	/** group that owns the folder */
	private GroupSpace groupSpace;


	/**
	 * This is the conceptual path to the item.
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
	
	/** Unique id of the group folder. */
	private Long id;
	
	/**  Version of the database data read from the database. */
	private int version;
	
	/**
	 * Package protected constructor
	 */
	GroupFolder()
	{
		this.treeRoot = this;	
	}
	
	/**
	 * Create a group folder with the given name.
	 * 
	 * @param name
	 */
	public GroupFolder(GroupSpace groupSpace, String name, IrUser owner)
	{
		setName(name);
		setTreeRoot(this);
		setPath(PATH_SEPERATOR);
		setOwner(owner);
		setGroupSpace(groupSpace);
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
	void setName(String name) {
		this.name = name;
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
	public Set<GroupFolder> getChildren() {
		return Collections.unmodifiableSet(children);
	}
	
	/**
	 * List of children for the group folder.
	 * 
	 * @param children
	 */
	void setChildren(Set<GroupFolder> children)
	{
		this.children = children;
	}

	/**
	 * Return the parent folder.
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getParent()
	 */
	public GroupFolder getParent() {
		return (GroupFolder) parent;
	}

	/**
	 * Set the root folder.  This will cast the root to a GroupFolder
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#setRoot(edu.ur.tree.PreOrderTreeSetNodeBase)
	 */
	protected void setRoot(PreOrderTreeSetNodeBase root) {
		this.treeRoot = (GroupFolder)root;
	}

	/**
	 * Update the paths of all the children.
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#updatePaths(java.lang.String)
	 */
	protected void updatePaths(String path) {
		setPath(path);
		for(GroupFolder folder: children)
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
	public int compareTo(GroupFolder other) {
		GroupFolder c = (GroupFolder)other;
		return this.getName().compareTo(c.getName());
	}
	
	/**
	 * Returns the root folder
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getRoot()
	 */
	public GroupFolder getTreeRoot() {
		return treeRoot;
	}
	
	/**
	 * Set the tree root.
	 * 
	 * @param treeRoot
	 */
	public void setTreeRoot(GroupFolder treeRoot) {
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
	 * Equals is tested based on name and path of the
	 * object.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupFolder)) return false;

		final GroupFolder other = (GroupFolder) o;

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
	public GroupSpace getGroupSpace() {
		return groupSpace;
	}

	/**
	 * Set the group space that owns the folder.
	 * 
	 * @param groupSpace that contains the folder
	 */
	public void setGroupSpace(GroupSpace groupSpace) {
		this.groupSpace = groupSpace;
	}
}
