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
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.item.VersionedItem;
import edu.ur.persistent.LongPersistentId;
import edu.ur.persistent.PersistentVersioned;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;
import edu.ur.tree.PreOrderTreeSetNodeBase;

/**
 * Represents a collection that is owned  by a user.  This allows a user to create
 * their own collections to manage their personalItems.
 * 
 * @author Nathan Sarr
 *
 */
@SuppressWarnings("unchecked")
public class PersonalCollection extends PreOrderTreeSetNodeBase implements
Serializable,  LongPersistentId, PersistentVersioned,
DescriptionAware, NameAware, Comparable, FileSystem
{
	/** Logger */
	private static final Logger log = LogManager.getLogger(PersonalCollection.class);
	
	/**  Children of this IrCollection */
	private Set<PersonalCollection> children = new HashSet<PersonalCollection>();
	
	/**  The id of the collection  */
	private Long id;
	
	/**  Name of the collection */
	private String name;
	
	/**  Description of the collection */
	private String description;
	
	/**  Version of the database data read from the database. */
	private int version;
	
	/**  personal Items in this collection. */
	private Set<PersonalItem> personalItems = new HashSet<PersonalItem>();
	
	/**  Root of the entire tree. */
	private PersonalCollection treeRoot;
	
	/** Owner of the personal collection  */
	private IrUser owner;
		
	/**
	 * This is the conceptual path to the personal collection.
	 * The base path plus the root of the tree 
	 * down to itself.
	 * For example if you have a user aUser
	 * with a personal collection named myCollection and
	 * the following collections 
	 * A, B, C and D and A is a parent of B and 
	 * B is a parent of C and C is a parent of D Then the
	 * paths are as follows:
	 * 
	 *  /myCollection/A/
	 *  /myCollection/A/B/
	 *  /myCollection/A/B/C/
	 *  /myCollection/A/B/C/D/
	 * 
	 */
	private String path;
	
	/**  Logo used for the collection */
	private FileInfo logo;
	
	/**  Generated version id. */
	private static final long serialVersionUID = -6434282254966436616L;
	
	/** represents the file system type for this personal item */
	private FileSystemType fileSystemType = FileSystemType.PERSONAL_COLLECTION;
	
	/**
	 * Default Constructor 
	 */
	PersonalCollection()
	{
		this.treeRoot = this;
	}
	

	
	/**
	 * Public constructor to create a root personal collection with the 
	 * given collection owner and the name of the personal collection.
	 * 
	 * @param owner
	 * @param name
	 */
	public PersonalCollection(IrUser owner, String name)
	{
		if(name == null)
		{
			throw new IllegalStateException("Collection name cannot be null");
		}
		
		if( owner == null )
		{
			throw new IllegalStateException("owner cannot be null");
		}
		
		setTreeRoot(this);
		setOwner(owner);
		setName(name);
		setPath(PATH_SEPERATOR);
	
	}
	
	/**
	 * Find a collection based on the name.  If
	 * no collection is found a null object is returned.
	 * 
	 * @param name of the collection.
	 * @return the found collection.
	 */
	public PersonalCollection getChild(String name)
	{
		if( log.isDebugEnabled() )
		{
			log.debug("Searching for collection " + name);
		}
		PersonalCollection irCollection = null;
		boolean found = false;
		Iterator<PersonalCollection> iter = children.iterator();
		
		while( iter.hasNext() && !found)
		{
			PersonalCollection c = iter.next();
			if( c.getName().equals(name))
			{
				irCollection = c;
				found = true;
		    }
		}
	
		return irCollection;
	}

	/**
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getChildren()
	 */
	@Override
	public Set<PersonalCollection> getChildren() {
		return Collections.unmodifiableSet(new TreeSet<PersonalCollection>(children));
	}

	/**
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getParent()
	 */
	@Override
	public PersonalCollection getParent() {
		return (PersonalCollection)parent;
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
	void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Rename this personal collection
	 * 
	 * @param name
	 * @throws DuplicateNameException
	 */
	public void reName(String name) throws DuplicateNameException
	{
		PersonalCollection parent = getParent();
		if( parent != null )
		{
			if( parent.getChild(name) != null )
			{
				throw new DuplicateNameException("A collection with the name " + name +
						" already exists in this folder", name );
			}
		}
		this.name = name;
		updatePaths(this.getPath());
		
	}
	
	/**
	 * Add an item to this collection.  If the item exists
	 * in a different collection, it is removed from that 
	 * collection.
	 * 
	 * 
	 * @param item - item to add
	 */
	public void addItem(PersonalItem item)
	{

		if( item.getPersonalCollection() != null && !item.getPersonalCollection().equals(this) )
		{
			PersonalCollection oldParent = item.getPersonalCollection();
			oldParent.removePersonalItem(item);
		}
		item.setPersonalCollection(this);
		personalItems.add(item);
	}
	
	/**
	 * Creates a personal item with the specified versioned item.
	 * 
	 * @param versionedItem
	 */
	public PersonalItem addVersionedItem(VersionedItem versionedItem)
	{
		PersonalItem personalItem = new PersonalItem(this, versionedItem);
		addItem(personalItem);
		return personalItem;
	}
	
	/**
	 * Remove and item from the collection
	 * and sets it's collection to null.
	 * 
	 * @param personal item
	 * @return true if the item is added.
	 */
	public boolean removePersonalItem(PersonalItem item)
	{
		boolean removed = false;
		if( personalItems.contains(item))
		{
			removed = personalItems.remove(item);
			item.setPersonalCollection(null);
		}
		return removed;
	}
	
	/**
	 * Find a  personal item based on the name.  If
	 * no item is found a null object is returned.
	 * 
	 * @param name of the personal item. This is the full name including the articles
	 * 
	 * @return the found personal item.
	 */
	public PersonalItem getPersonalItem(String name)
	{
		if( log.isDebugEnabled() )
		{
			log.debug("Searching for item " + name);
		}
		PersonalItem item = null;
		boolean found = false;
		Iterator<PersonalItem> iter = personalItems.iterator();
		
		while( iter.hasNext() && !found)
		{
			PersonalItem personalItem = iter.next();
			if( personalItem.getVersionedItem().getCurrentVersion().getItem().getFullName().equals(name))
			{
				item = personalItem;
				found = true;
		    }
		}
	
		return item;
	}
	
	/**
	 * Find a  personal item based on the name.  If
	 * no item is found a null object is returned.
	 * 
	 * @param id of the personal item.
	 * @return the found personal item.
	 */
	public PersonalItem getPersonalItem(Long id)
	{
		if( log.isDebugEnabled() )
		{
			log.debug("Searching for item with id " + id);
		}
		PersonalItem item = null;
		boolean found = false;
		Iterator<PersonalItem> iter = personalItems.iterator();
		
		while( iter.hasNext() && !found)
		{
			PersonalItem personalItem = iter.next();
			if( personalItem.getId().equals(id))
			{
				item = personalItem;
				found = true;
		    }
		}
	
		return item;
	}

	/**
	 * An unmodifiable set of personalItems in this collection.
	 * 
	 * @return the set of personal personalItems.
	 */
	public Set<PersonalItem> getPersonalItems() {
		return Collections.unmodifiableSet(personalItems);
	}

	/**
	 * Items in this collection.
	 * 
	 * @param personalItems
	 */
	void setPersonalItems(Set<PersonalItem> personalItems) {
		this.personalItems = personalItems;
	}
	
	/**
	 * Returns a collection root.
	 * 
	 * @see edu.ur.tree.PreOrderTreeSetNodeBase#getRoot()
	 */
	@Override
	public PersonalCollection getTreeRoot() {
		return treeRoot;
	}
	
	/**
	 * Remove the child from the set of children. 
	 * Renumbers the tree to be correct.
	 * 
	 * @param child
	 * @return true if the child is removed.
	 */
	public boolean removeChild(PersonalCollection child)
	{
		boolean removed = false;
		
		if( children.remove(child) )
		{
			removed = true;
			
			// re-number the tree
			cleanUpTree(child.getTreeSize(), child.getRightValue());
			child.setParent(null);
	    }
		return removed;
	}
	
	/**
	 * Add a child to this collections set of children.
	 * 
	 * @param child to add
	 */
	public void addChild(PersonalCollection child) throws DuplicateNameException
	{
		if( log.isDebugEnabled() )
		{
		    log.debug( "\n\n------------------------------------\n\n");
		    log.debug( "Adding child " + child.toString());
		}
		if(this.equals(child))
		{
			throw new IllegalStateException("Cannot add a personal collection to itself");
		}
		if(! isVaildPersonalCollectionSystemName(child.getName()))
		{
			throw new DuplicateNameException("Collection with the name " + 
					child.getName() + " already exists ", child.getName());
		}
		if (!child.isRoot()) {
			PersonalCollection childParent = child.getParent();
			childParent.removeChild(child);
		}

		child.setTreeRoot(null);
		makeRoomInTree(child);
		child.setParent(this);
		child.setTreeRoot(getTreeRoot());
		child.setOwner(owner);
		children.add(child);
		
		if ( log.isDebugEnabled() )
		{
		    log.debug( "Done Adding child " + child.toString());
		    log.debug( "\n\n------------------------------------\n\n");
		}
	}
	
	/**
	 * Create a new child collection for this 
	 * collection.
	 * 
	 * @param name of the child collection.
	 * @return the created collection
	 * 
	 * @throws DuplicateNameException if a name of a collection that
	 * already exists is passed in.
	 */
	public PersonalCollection createChild(String name) throws DuplicateNameException
	{
	    PersonalCollection c = new PersonalCollection();
		c.setName(name);
		c.setOwner(owner);
		c.setPath(getFullPath());
		addChild(c);
		return c;
	}

	/**
	 * Set the children in this collection.
	 * 
	 * @param children
	 */
	void setChildren(Set<PersonalCollection> children) {
		this.children = children;
	}

	/**
	 * Set the id of this collection.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Set the parent of this collection.  Results
	 * in setting path and all children paths.
	 * 
	 * @param parent
	 */
	void setParent(PersonalCollection parent) {
		// make sure the path is set correctly 
		// on call to parent
		if(parent == null)
		{
			path = PersonalFolder.PATH_SEPERATOR ;
		}
		super.setParent(parent);
	}

	/**
	 * Set the root folder for the entire tree.
	 * 
	 * @param root
	 */
	void setTreeRoot(PersonalCollection root) {
		this.treeRoot = root;
		
		for( PersonalCollection p : children)
		{
			p.setTreeRoot(root);
		}
	}

	/**
	 * Set the version of data for the database data
	 * of this collection.
	 * 
	 * @param version
	 */
	public void setVersion(int version) {
		this.version = version;
	}
		
	/**
	 * Get the path.  This should be
	 * the repository name plus the path of all 
	 * parents.
	 * 
	 * @throws IllegalStateException if the collection is a root 
	 * collection and the repository does not exist.
	 *  
	 * @return the path
	 */
	public String getPath() 
	{
		return path;
	}
	
	/**
	 * Get the full path of this collection.
	 * 
	 * @return the full path
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
		this.path = path;
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
		if (!(o instanceof PersonalCollection)) return false;

		final PersonalCollection other = (PersonalCollection) o;

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
		StringBuffer sb = new StringBuffer("[PersonalCollection id = ");
		sb.append(id);
		sb.append( " name = ");
		sb.append(name);
		sb.append(" path = ");
		sb.append(path);
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
		for(PersonalCollection c: children)
		{
			c.updatePaths(getFullPath());
		}
	}
	
	/**
	 * Logo for the collection.
	 * 
	 * @return
	 */
	public FileInfo getLogo() {
		return logo;
	}

	/**
	 * Logo for the collection.
	 * 
	 * @param logo
	 */
	public void setLogo(FileInfo logo) {
		this.logo = logo;
	}
	
	/**
	 * Compars two collections by name.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object other) {
		PersonalCollection c = (PersonalCollection)other;
		return this.getName().compareTo(c.getName());
	}

	/**
	 * Owner of the personal collection.
	 * 
	 * @return
	 */
	public IrUser getOwner() {
		return owner;
	}

	/**
	 * Owner of the personal collection.
	 * 
	 * @param owner
	 */
	void setOwner(IrUser owner) {
		this.owner = owner;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.FileSystem#getFileSystemType()
	 */
	public FileSystemType getFileSystemType() {
		return fileSystemType;
	}

	@Override
	protected void setRoot(PreOrderTreeSetNodeBase root) {
		this.treeRoot = (PersonalCollection) root;
	}
	
	/**
	 * Returns true if the name is ok to add to a file or folder
	 * 
	 * @param name
	 * @return
	 */
	private boolean isVaildPersonalCollectionSystemName(String name)
	{
		boolean ok = false;
		if( getChild(name) == null)
		{
			ok = true;
		}
		return ok;
	}

}
