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

package edu.ur.ir.institution;

import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;

/**
 * Represents a relationship between
 * a collection and an item
 * 
 * This is a wrapper around a versioned institutional item.
 * 
 * @author Nathan Sarr
 *
 */
public class InstitutionalItem extends BasePersistent implements FileSystem{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = 3451184761322961505L;

	/**  collection the item belongs to. */
	private InstitutionalCollection institutionalCollection;
	
	/** represents the file system type for this personal item */
	private FileSystemType fileSystemType = FileSystemType.INSTITUTIONAL_ITEM;
	
	/**  Versioned institutional item */
	private VersionedInstitutionalItem versionedInstitutionalItem;
	
	/** Owner of the institutional item */
	private IrUser owner;
	
	/**
	 * Package protected constructor.
	 */
	InstitutionalItem(){}; 
	
	/**
	 * Create a link between a collection and item.  This creates a published version
	 * with the current version being the published version.
	 * 
	 * @param item - item to create a link with
	 * @param institutionalCollection - collection to match with the item
	 */
	InstitutionalItem(InstitutionalCollection institutionalCollection, 
			GenericItem item)
	{
		setInstitutionalCollection(institutionalCollection);
		setOwner(item.getOwner());
		versionedInstitutionalItem = new VersionedInstitutionalItem(this, item);
	}
	
	/**
	 * Add a new version of the publication.  This version becomes
	 * the new current version.
	 * 
	 * @param item item to be set as current published item
	 * @return Published version
	 */
	public InstitutionalItemVersion addNewVersion(GenericItem item)
	{
		return versionedInstitutionalItem.addNewVersion(item);
	}
	
	/**
	 * Get the institutional collection.
	 * 
	 * @return the institutional collection.
	 */
	public InstitutionalCollection getInstitutionalCollection() {
		return institutionalCollection;
	}

	/**
	 * Set the ir collection.
	 * 
	 * @param institutionalCollection
	 */
	void setInstitutionalCollection(InstitutionalCollection institutionalCollection) {
		this.institutionalCollection = institutionalCollection;
	}

	/**
	 * Returns the path for this item.
	 * 
	 * The path is the path of the parent collection 
	 * 
	 * @return
	 */
	public String getPath()
	{
		String path = null;
		if (institutionalCollection!= null )
		{
			path =  institutionalCollection.getFullPath();
		}
		else
		{
			path = PATH_SEPERATOR;
		}
		return path;
	}
	
	/**
	 * Get the full path of this item.  If there is 
	 * no parent collection the path is just the name of
	 * the item.
	 * 
	 * @return the full path.
	 */
	public String getFullPath()
	{
		String path = getPath();
		String name = versionedInstitutionalItem.getCurrentVersion().getItem().getFullName();
		
		path = path + name;

		return path;
	}
	
	/**
	 * Hash code for the institutional item.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += id == null ? 0 : id.hashCode();
		value += getFullPath() == null ? 0 : getFullPath().hashCode();
		return value;
	}
	
	/**
	 * Equals 
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof InstitutionalItem)) return false;

		final InstitutionalItem other = (InstitutionalItem) o;
		
		if( ( id != null && !id.equals(other.getId()) ) ||
			( id == null && other.getId() != null ) ) return false;

		if( ( getFullPath() != null && !getFullPath().equals(other.getFullPath()) ) ||
			( getFullPath() == null && other.getFullPath() != null ) ) return false;
		
		return true;
	}
	

	/**
	 * Get the file system type the institutional item represents.
	 * 
	 * @see edu.ur.ir.FileSystem#getFileSystemType()
	 */
	public FileSystemType getFileSystemType() {
		return fileSystemType;
	}

	/**
	 * Set the file system type of this institutional item.
	 * 
	 * @param fileSystemType
	 */
	public void setFileSystemType(FileSystemType fileSystemType) {
		this.fileSystemType = fileSystemType;
	}

	public String getDescription() {
		return versionedInstitutionalItem.getCurrentVersion().getItem().getDescription();
	}

	public String getName() {
		return versionedInstitutionalItem.getCurrentVersion().getItem().getFullName();
	}

	public VersionedInstitutionalItem getVersionedInstitutionalItem() {
		return versionedInstitutionalItem;
	}

	public void setVersionedInstitutionalItem(
			VersionedInstitutionalItem versionedInstitutionalItem) {
		this.versionedInstitutionalItem = versionedInstitutionalItem;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" full path = ");
		sb.append(getFullPath());
		sb.append(" institutional collection = ");
		sb.append(institutionalCollection);
		sb.append("]");
		
		return sb.toString();
	}

	public IrUser getOwner() {
		return owner;
	}

	public void setOwner(IrUser owner) {
		this.owner = owner;
	}

}
