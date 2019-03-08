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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.item.VersionedItem;
import edu.ur.persistent.BasePersistent;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;

/**
 * This is an itemVersion that belongs to a personal collection.  This 
 * creates a link between a version of a given itemVersion and a personal 
 * collection
 * 
 * @author Nathan Sarr
 *
 */
public class PersonalItem extends BasePersistent implements NameAware, 
DescriptionAware, FileSystem{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = 4509167297589083886L;

	/**  Logger */
	private static final Logger log = LogManager.getLogger(PersonalItem.class);
	
	/**  Item that is in the collection */
	private VersionedItem versionedItem;
	
	/**  personal collection the itemVersion belongs to. */
	private PersonalCollection personalCollection;
	
	/** owner of the personal item */
	private IrUser owner;
	
	/** represents the file system type for this personal item */
	private FileSystemType fileSystemType = FileSystemType.PERSONAL_ITEM;
	
	/**
	 * Package protected constructor.
	 */
	PersonalItem(){};
	
	/**
	 * Create a personal item with a null personal collection.  This means this
	 * is a root personal item.
	 * 
	 * @param itemVersion
	 */
	public PersonalItem(IrUser owner, VersionedItem versionedItem)
	{
		setOwner(owner);
		versionedItem.changeOwner(owner);
		setVersionedItem(versionedItem);
	}
	
	/**
	 * Create a link between a collection and itemVersion.
	 * 
	 * @param itemVersion - itemVersion to create a link with
	 * @param personalCollection - collection to match with the itemVersion.  
	 */
	PersonalItem(PersonalCollection personalCollection, VersionedItem versionedItem)
	{
		if(versionedItem == null)
		{
			throw new IllegalStateException("itemVersion cannot be null");
		}
		
		setOwner(personalCollection.getOwner());
		setVersionedItem(versionedItem);
		versionedItem.changeOwner(owner);
		personalCollection.addItem(this);
	}

	/**
	 * Get the ir collection.
	 * 
	 * @return the ir collection.
	 */
	public PersonalCollection getPersonalCollection() {
		return personalCollection;
	}

	/**
	 * Set the ir collection.
	 * 
	 * @param personalCollection
	 */
	void setPersonalCollection(PersonalCollection personalCollection) {
		this.personalCollection = personalCollection;
	}

	/**
	 * Get the itemVersion for this collection itemVersion..
	 * 
	 * @return the particular item version this personal item is associated with.
	 */
	public VersionedItem getVersionedItem() {
		return versionedItem;
	}

	/**
	 * Set the itemVersion for this collection itemVersion.
	 * 
	 * @param itemVersion
	 */
	void setVersionedItem(VersionedItem versionedItem) {
		this.versionedItem = versionedItem;
	}
	
	/**
	 * Returns the path for this itemVersion.
	 * 
	 * The path is the path of the parent collection 
	 * 
	 * @return
	 */
	public String getPath()
	{
		String path = null;
		if (personalCollection!= null )
		{
			path = personalCollection.getFullPath();
		}
		else
		{
			path = PATH_SEPERATOR;
		}
		
		if(log.isDebugEnabled())
		{
			log.debug("path is " + path);
		}
		return path;
	}
	
	
	/**
	 * Overridden to string method.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append( " full path = ");
		sb.append(getFullPath());
		sb.append( " personal collection = ");
		sb.append(personalCollection);
		sb.append(" versionedItem = ");
		sb.append(versionedItem);
		sb.append("]");
		return sb.toString();
	}
	

	
	/**
	 * Get the full path of this itemVersion.  If there is 
	 * no parent collection the path is just the name of
	 * the itemVersion.
	 * 
	 * @return the full path.
	 */
	public String getFullPath()
	{
		return getPath() + versionedItem.getCurrentVersion().getItem().getName();
	}
	
	/**
	 * Hash code for a personal item.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += getId() == null ? 0 : getId().hashCode();
		return value;
	}
	
	/**
	 * Equals method for a personal item.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof PersonalItem)) return false;

		final PersonalItem other = (PersonalItem) o;
        
		if( ( id != null && !id.equals(other.getId()) ) ||
			( id == null && other.getId() != null ) ) return false;
		
		return true;
	}

	/**
	 * Returns the name of the versioned item.
	 * 
	 * @see edu.ur.simple.type.NameAware#getName()
	 */
	public String getName() {
		return versionedItem.getCurrentVersion().getItem().getFullName();
	}

	/**
	 * Returns the description of the versioned item.
	 * 
	 * @see edu.ur.simple.type.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return versionedItem.getCurrentVersion().getItem().getDescription();
	}

	/**
	 * Owner of this personal item.
	 * 
	 * @return the owner of the personal item
	 */
	public IrUser getOwner() {
		return owner;
	}

	/**
	 * Set the owner of the personal item.
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
	
	
	
}
