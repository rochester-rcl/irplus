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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.persistent.LongPersistentId;
import edu.ur.persistent.PersistentVersioned;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;
import edu.ur.tree.PreOrderTreeSetNodeBase;

/**
 * Represents a personal folder which users can store 
 * their files in.  A Personal Folder can have sub 
 * folders.
 * 
 * Files and folders cannot have the same name within a given
 * folder.  This means that two files cannot have the same name
 * nor can a file and folder have the same name within a folder.
 * 
 * Having the same name will also include case insensitivity.  This
 * means that a file named "HeLlo.txT" will be considered to have the
 * same name as "hello.txt"
 * 
 * @author Nathan Sarr
 *
 */

public class PersonalFolder extends PreOrderTreeSetNodeBase implements
Serializable,  LongPersistentId, PersistentVersioned,
DescriptionAware, NameAware, Comparable<PersonalFolder>, FileSystem{
	
	/* Logger */
	private static final Logger log = Logger.getLogger(PersonalFolder.class);
	
	/*  Children of this PersonalFolder */
	private Set<PersonalFolder> children = new HashSet<PersonalFolder>();
	
	/*  The owner this folder belongs to */
	private IrUser owner;
	
	/*  The id of the folder  */
	private Long id;
	
	/*  Name of the personal folder */
	private String name;
	
	/* Description of the folder */
	private String description;
	
	/*  Version of the data read from the database.  */
	private int version;
	
	/*  Files in this personal folder. This is a set of versioned files. */
	private Set<PersonalFile> files = new HashSet<PersonalFile>();
	
	/* Root of the entire folder tree. */
	private PersonalFolder treeRoot;
	
	/* Auto share information for a folder  */
	private Set<FolderAutoShareInfo> autoShareInfos = new HashSet<FolderAutoShareInfo>();

	/* set of invite infos for auto sharing folder information  */
	private Set<FolderInviteInfo> folderInviteInfos = new HashSet<FolderInviteInfo>();

	/*
	 * This is the conceptual path to the folder.
	 * The base path plus the root of the tree 
	 * down to itself.
	 * For example if you have a owner named owner and
	 * the following sub folders  
	 * A, B, C and D and A is a parent of B and 
	 * B is a parent of C and C is a parent of D Then the
	 * paths are as follows:
	 * 
	 *  /A/
	 *  /A/B/
	 *  /A/B/C/
	 *  /A/B/C/D/
	 * 
	 */
	private String path;
	
	/**  Eclipse generated id. */
	private static final long serialVersionUID = -6434282254966436616L;
	
	/**
	 * Default Constructor 
	 */
	PersonalFolder()
	{
		this.treeRoot = this;
	}
	
	/**
	 * Default public constructor.  A valid owner name
	 * and folder name must be passed.
	 * 
	 * @param owner - owner who will own the folder
	 * @param folderName - name of the folder
	 * 
	 * @throws IllegalStateException if the folder name or
	 * owner are null.
	 */
	PersonalFolder(IrUser user, String folderName) throws IllegalFileSystemNameException
	{
		if(folderName == null)
		{
			throw new IllegalStateException("folder name cannot be null");
		}
		
		if( user == null )
		{
			throw new IllegalStateException("owner cannot be null");
		}
		
		setTreeRoot(this);
		setOwner(user);
		setName(folderName);
		setPath( PATH_SEPERATOR );
	}
	
	/**
	 * Find a PersonalFolder based on the name.  If
	 * no PersonalFolder is found a null object is returned.
	 * 
	 * This is <b>NOT</b>  a recursive operation and only searches
	 * the current list of children.
	 * 
	 * The search is case insensitive
	 * 
	 * @param name of the child personal folder.
	 * @return the found personal folder.
	 */
	public PersonalFolder getChild(String name)
	{
		if( log.isDebugEnabled() )
		{
			log.debug("Searching for personal folder " + name);
		}
		PersonalFolder PersonalFolder = null;
		boolean found = false;
		Iterator<PersonalFolder> iter = children.iterator();
		
		while( iter.hasNext() && !found)
		{
			PersonalFolder c = iter.next();
			if( c.getName().equalsIgnoreCase(name))
			{
				PersonalFolder = c;
				found = true;
		    }
		}
	
		return PersonalFolder;
	}	

	/**
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getChildren()
	 */
	@Override
	public Set<PersonalFolder> getChildren() {
		return Collections.unmodifiableSet(new TreeSet<PersonalFolder>(children));
	}

	/**
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getParent()
	 */
	@Override
	public PersonalFolder getParent() {
		return (PersonalFolder)parent;
	}
	
	/**
	 * @see edu.ur.persistent.LongPersistentId#getId()
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @see edu.ur.persistent.PersistentVersioned#getVersion()
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Get the description of this collection
	 * 
	 * @see edu.ur.common.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Description of the collection.
	 * 
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Get the name of this collection
	 * 
	 * @see edu.ur.common.NameAware#getName()
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Name of the collection.
	 * 
	 * @param name
	 */
	void setName(String name) throws IllegalFileSystemNameException
	{
		List<Character> illegalCharacters = IllegalFileSystemNameException.nameHasIllegalCharacerter(name);
		if( illegalCharacters.size() > 0 )
		{
			throw new IllegalFileSystemNameException(illegalCharacters, name);
		}
		
		this.name = name;
	}
	
	/**
	 * Rename this personal folder.
	 * 
	 * @param name - new name to give the personal folder
	 * @throws DuplicateNameException - if the name already exists in as a file or folder at the current
	 * level of the folder structure
	 * @throws IllegalFileSystemNameException - if the name contains illegal characters within the name
	 */
	public void reName(String name) throws DuplicateNameException, IllegalFileSystemNameException
	{
		PersonalFolder parent = getParent();
		if( parent != null )
		{
			if( !parent.isVaildPersonalFileSystemName(name) )
			{
				throw new DuplicateNameException("A file or folder with the name " + name +
						" already exists in this folder", name );
			}
		}
		setName(name);
		this.updatePaths(getPath());
	}
	
	/**
	 * Add a versioned file to this folder.  this creates a 
	 * new personal file record.
	 * 
	 * @param vf
	 */
	public PersonalFile addVersionedFile(VersionedFile vf) throws DuplicateNameException
	{
		 if( !isVaildPersonalFileSystemName(vf.getNameWithExtension()))
		 {
			 throw new DuplicateNameException("A file or folder with the name " + vf.getNameWithExtension() +
						" already exists in this folder", vf.getNameWithExtension());
		 }
		 SharedInboxFile inboxFile = owner.getSharedInboxFile(vf);
         if ( inboxFile != null )
         {
        	owner.removeFromSharedFileInbox(inboxFile);
         }

         PersonalFile pf = new PersonalFile(owner, vf, this);
		 files.add(pf);
		 return pf;
	}
	
	/**
	 * Adds an existing personal file to this folder.  This allows
	 * a personal file to be moved from one location to another.
	 * 
	 * @param pf - personal file to add
	 * @throws DuplicateNameException - if the file already exits
	 */
	public void addPersonalFile(PersonalFile pf) throws DuplicateNameException
	{
		if( !isVaildPersonalFileSystemName(pf.getVersionedFile().getNameWithExtension()))
		{
			throw new DuplicateNameException("Personal file with the name " + pf.getVersionedFile().getNameWithExtension() +
				" already exists", pf.getVersionedFile().getNameWithExtension());
		}
		if( pf.getPersonalFolder() != null)
		{
		    pf.getPersonalFolder().removePersonalFile(pf);
		}
		
		pf.setOwner(this.getOwner());
		pf.setPersonalFolder(this);
		files.add(pf);
        
		SharedInboxFile inboxFile = owner.getSharedInboxFile(pf.getVersionedFile());
        if ( inboxFile != null )
        {
        	owner.removeFromSharedFileInbox(inboxFile);
        }
	}
	
	/**
	 * Remove a file from the personal folder
	 * 
	 * @param personalFile to remove from the folder
	 * @return true if the personal file is removed
	 */
	public boolean removePersonalFile(PersonalFile pf)
	{
		boolean removed = false;
		if( files.contains(pf))
		{
			removed = files.remove(pf);
		    pf.setPersonalFolder(null);
		}
		return removed;
	}
	
	/**
	 * Find a file based on the name.  If
	 * no file is found a null object is returned.  This
	 * is case in-sensitive
	 * 
	 * @param name of the file including the extension
	 * @return the found file
	 */
	public PersonalFile getFile(String nameWithExtension)
	{
		for(PersonalFile pf : files)
		{
			if( pf.getVersionedFile().getNameWithExtension().equalsIgnoreCase(nameWithExtension))
			{
				return pf;
			}
		}
		
		return null;
	}

	/**
	 * An unmodifiable set of files in this personal folder
	 * 
	 * 
	 * @return the set of items.
	 */
	public Set<PersonalFile> getFiles() {
		return Collections.unmodifiableSet(files);
	}

	/**
	 * Files in this folder
	 * 
	 * @param items
	 */
	void setFiles(Set<PersonalFile> files) {
		this.files = files;
	}
	
	/**
	 * Returns a folder root.
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getRoot()
	 */
	@Override
	public PersonalFolder getTreeRoot() {
		return treeRoot;
	}

	/**
	 * Remove the child from the set of children. 
	 * Renumbers the tree to be correct.
	 * 
	 * @param child
	 * @return true if the child is removed.
	 */
	public boolean removeChild(PersonalFolder child)
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
	public void addChild(PersonalFolder child) throws DuplicateNameException
	{
		if( child.equals(this))
		{
			throw new IllegalStateException("cannot add a personal folder to " +
					"itself as a child");
		}
		if(!isVaildPersonalFileSystemName(child.getName()))
		{
			throw new DuplicateNameException("Folder with the name " + 
					child.getName() + " already exists ", child.getName());
		}
		if (!child.isRoot()) {
			PersonalFolder childParent = child.getParent();
			childParent.removeChild(child);
		}

		child.setTreeRoot(null);
		makeRoomInTree(child);
		child.setParent(this);
		child.setOwner(owner);
		child.setTreeRoot(getTreeRoot());
		children.add(child);
	}
	
	/**
	 * Create a new child folder for this 
	 * personal folder.
	 * 
	 * @param name of the child folder.
	 * @return the created folder
	 * 
	 * @throws IllegalArgumentException if a name of a folder that
	 * already exists is passed in.
	 */
	public PersonalFolder createChild(String name) throws DuplicateNameException, IllegalFileSystemNameException
	{
	    PersonalFolder c = new PersonalFolder();
		c.setName(name);
		addChild(c);
		return c;
	}
	
	/**
	 * Returns true if the name is ok to add to a file or folder
	 * 
	 * @param name of the file or folder.  If it is a file, it should contain
	 * the extension.
	 * 
	 * @return true if the name does not exist.  This is case insensitive.
	 */
	public boolean isVaildPersonalFileSystemName(String name)
	{
		boolean ok = false;
		if( getChild(name) == null && getFile(name) == null)
		{
			ok = true;
		}
		return ok;
	}

	/**
	 * Set the children in this folder.
	 * 
	 * @param children
	 */
	void setChildren(Set<PersonalFolder> children) {
		this.children = children;
	}

	/**
	 * Set the id of this folder.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Set the parent of this personal folder.  Results
	 * in setting path and all children paths.
	 * 
	 * @param parent
	 */
	void setParent(PersonalFolder parent) {
		if(parent == null)
		{
			path = PATH_SEPERATOR ;
		}
		super.setParent(parent);
	}

	/**
	 * Set the root folder for the entire tree.  
	 * This recursively updates the children.
	 * 
	 * @param root
	 */
	void setTreeRoot(PersonalFolder root) {
		this.treeRoot = root;
		for(PersonalFolder c: children)
		{
			c.setTreeRoot(root);
		}
	}

	/**
	 * Set the version of data for the database data
	 * of this folder.
	 * 
	 * @param version
	 */
	public void setVersion(int version) {
		this.version = version;
	}
		
	/**
	 * Get the path.  This should be
	 * path including all 
	 * parents.
	 * 
	 * @throws IllegalStateException if the folder is the root 
	 * folder and the owner does not exist.
	 *  
	 * @return the path
	 */
	public String getPath() 
	{
		return path;
	}
	
	/**
	 * Get the full path of this folder.
	 * 
	 * @return the full path
	 */
	public String getFullPath()
	{
		return getPath() + name + PATH_SEPERATOR;
	}

	/**
	 * Set the conceptual path for this folder.
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

		// add the end seperator if it does not exist
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
	 * Hash code is based on the path and name of
	 * the folder.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += owner == null ? 0 : owner.hashCode();
		value += getFullPath() == null? 0 : getFullPath().hashCode();
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
		if (!(o instanceof PersonalFolder)) return false;

		final PersonalFolder other = (PersonalFolder) o;

		if( ( owner != null && !owner.equals(other.getOwner()) ) ||
			( owner == null && other.getOwner() != null ) ) return false;
		
		if( ( getFullPath() != null && !getFullPath().equals(other.getFullPath()) ) ||
		    ( getFullPath() == null && other.getFullPath() != null ) ) return false;

		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[PersonalFolder id = ");
		sb.append(id);
		sb.append(" name = ");
		sb.append(name);
		sb.append(" path = ");
		sb.append(path);
		sb.append( " left value = ");
		sb.append(leftValue);
		sb.append(" right value = ");
		sb.append(rightValue);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Sets the path and the path of all
	 * children. This should be used when a
	 * path should be passed down to all children.
	 * 
	 * @param path
	 */
	protected void updatePaths(String path)
	{
		setPath(path);
		for(PersonalFolder c: children)
		{
			c.updatePaths(getFullPath());
		}
	}
	
	/**
	 * Compares two personal folders by name.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(PersonalFolder other) {
		return this.getName().compareTo(other.getName());
	}

	/**
	 * Get the owner who owns the folder.
	 * 
	 * @return
	 */
	public IrUser getOwner() {
		return owner;
	}

	/**
	 * Set the owner who owns the folder.
	 * 
	 * @param owner
	 */
	void setOwner(IrUser user) {
		this.owner = user;
	}

	/**
	 * Get the file system type.
	 * 
	 * @see edu.ur.ir.FileSystem#getType()
	 */
	public FileSystemType getFileSystemType() {
		return FileSystemType.PERSONAL_FOLDER;
	}

	/**
	 * Set the root personal folder.
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#setRoot(edu.ur.tree.PreOrderTreeSetNodeBase)
	 */
	protected void setRoot(PreOrderTreeSetNodeBase root) {
		this.treeRoot = (PersonalFolder)root;
	}
	
	/**
	 * Get the auto share information for this folder.  This is an
	 * unmodifiable set.
	 * 
	 * @return - auto share information for this folder.
	 */
	public Set<FolderAutoShareInfo> getAutoShareInfos() {
		return Collections.unmodifiableSet(autoShareInfos);
	}
	
	/**
	 * Creates an auto share info object with the specified permissions and collaborator.
	 * 
	 * @param permissions - set of permissions for auto sharing
	 * @param collaborator - collaborator to give auto sharing to
	 * 
	 * @return - created folder autoshare information.

	 * @throws FileSharingException - if auto shared to owner of folder (sharing with yourself)
	 */
	public FolderAutoShareInfo createAutoShareInfo(Set<IrClassTypePermission> permissions, 
			IrUser collaborator) throws FileSharingException
	{
		if( collaborator.equals(owner))
		{
			throw new FileSharingException("Cann't set auto share with yourself");
		}
		FolderAutoShareInfo autoShareInfo = getAutoShareInfo(collaborator);
		if( autoShareInfo != null )
		{
			autoShareInfo.setPermissions(permissions);  
		}
		else
		{
			autoShareInfo = new FolderAutoShareInfo(this, permissions, collaborator);
		}
	    autoShareInfos.add(autoShareInfo);
		return autoShareInfo;
	}
	
	/**
	 * Create an invite info object for this folder.
	 * 
	 * @param permissions - permissions for the folder
	 * @param email - email to add
	 * @return - the created invite info or the updated existing one if it exists.
	 * @throws FileSharingException
	 */
	public FolderInviteInfo createInviteInfo(Set<IrClassTypePermission> permissions, String email)
	    throws FileSharingException
	{
		
		FolderInviteInfo inviteInfo = getFolderInviteInfo(email);
		if( inviteInfo != null )
		{
			inviteInfo.setPermissions(permissions);
		}
		else
		{
		    inviteInfo = new FolderInviteInfo(this, email, permissions);
		}
		folderInviteInfos.add(inviteInfo);
		return inviteInfo;
	}
	
	/**
	 * Determine if this folder object has auto sharing set up.   
	 * 
	 * @return true if there is one or more auto shares or invites to auto share on the folder
	 */
	public boolean getHasAutoSharing()
	{
		return (autoShareInfos.size() > 0 || folderInviteInfos.size() > 0); 
	}
	
	/**
	 * Remove the auto share information.
	 * 
	 * @param info - auto share information to remove
	 * @return true if the auto share information is removed.
	 */
	public boolean removeAutoShareInfo(FolderAutoShareInfo info)
	{
		return autoShareInfos.remove(info);
	}
	
	/**
	 * Get the folder auto sharing information for a given collaborator.
	 * 
	 * @param collaborator - collaborator to get the autosharing for
	 * @return - the auto share information or null if the information is not found.
	 */
	public FolderAutoShareInfo getAutoShareInfo(IrUser collaborator)
	{
		for(FolderAutoShareInfo autoShareInfo : autoShareInfos)
		{
			if( autoShareInfo.getCollaborator().equals(collaborator))
			{
				return autoShareInfo;
			}
		}
		return null;
	}
	
	/**
	 * Get the folder auto sharing invite information for a given email
	 * 
	 * @param email - email to search for
	 * @return - the invite information or null if the information is not found.
	 */
	public FolderInviteInfo getFolderInviteInfo(String email)
	{
		for(FolderInviteInfo inviteInfo : folderInviteInfos)
		{
			if( inviteInfo.getEmail().equalsIgnoreCase(email.trim()))
			{
				return inviteInfo;
			}
		}
		return null;
	}
	
	/**
	 * Get the folder invite info information.  This returns an
	 * unmodifiable set.
	 * 
	 * @return - folder invite information
	 */
	public Set<FolderInviteInfo> getFolderInviteInfos() {
		return Collections.unmodifiableSet(folderInviteInfos);
	}

	/**
	 * Set the folder invite information.
	 * 
	 * @param folderInviteInfo
	 */
	void setFolderInviteInfo(Set<FolderInviteInfo> folderInviteInfo) {
		this.folderInviteInfos = folderInviteInfo;
	}
	
	/**
	 * Remove the invite info.
	 * 
	 * @param inviteInfo - invite info to remove
	 * @return - true if the invite info was removed.
	 */
	public boolean removeFolderInviteInfo(FolderInviteInfo inviteInfo)
	{
		return folderInviteInfos.remove(inviteInfo);
	}

}
