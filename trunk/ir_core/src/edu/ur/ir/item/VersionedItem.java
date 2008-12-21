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

package edu.ur.ir.item;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.ur.ir.user.IrUser;
import edu.ur.persistent.CommonPersistent;

/**
 * Represents an item in the repository that can have 
 * multiple versions.  The current version is the version
 * with the largest version number.
 * 
 * This can be pointed to by mulitple people and is meant for
 * a single user and could be used for multiuser collaboration.
 * 
 * @author Nathan Sarr
 *
 */
public class VersionedItem extends CommonPersistent{

	/** Eclipse generated id  */
	private static final long serialVersionUID = -7504273735834708996L;

	/**  Starting number for all file versions */
	public static final int INITIAL_FILE_VERSION = 1;
	
	/** 
	 * Max number of versions allowed to be held - after this older versions 
	 * are discarded
	 */
	public static final int DEFAULT_MAX_ALLOWED_VERSIONS = 4;

	/**  highest number of versions allowed after which older versions are discarded*/
	private int maxAllowedVersions = DEFAULT_MAX_ALLOWED_VERSIONS;
	
	/**  The current higest file version number */
	private int maxVersion = INITIAL_FILE_VERSION;
	
	/**  The set of versions for this Versioned Ir File */
	private Set<ItemVersion> versions = new HashSet<ItemVersion>();
	
	/**
	 * Owner of the versioned file.  This person owns the file and 
	 * all related versions.
	 */
	private IrUser owner;
	
	/**  The user who has locked this versioned file */
	private IrUser lockedBy;
	
	/** Current version fo the set of versions */
	private ItemVersion currentVersion;
	
	/**  Package protected versioned item constructor. */
	VersionedItem(){}
	
	/**
	 * Create a new versioned item with the initial version  
	 * 
	 * @param irFile
	 */
	public VersionedItem(IrUser owner, GenericItem item, String name)
	{
		setMaxVersion(INITIAL_FILE_VERSION);
		setOwner(owner);
		item.setOwner(owner);
		ItemVersion version = new ItemVersion(item, this, maxVersion);
		setName(name);
		currentVersion = version;
		versions.add(version);
	}
	
	/**
	 * Add a new version of the item.  This version becomes
	 * the new current version.
	 * 
	 * @param fileInfo
	 */
	public ItemVersion addNewVersion(GenericItem item)
	{
		setMaxVersion(maxVersion + 1);
		item.setOwner(owner);
		ItemVersion version = new ItemVersion(item, this, maxVersion);
		versions.add(version);
		currentVersion = version;
		return version;
	}
	
	/**
	 * Returns the current item version.
	 * 
	 * @return the version with the higest version number.
	 */
	public ItemVersion getCurrentVersion()
	{
		return currentVersion;
	}

	/**
	 * Changes the current item version if the version exists otherwise,
	 * the current version is not changed.  This ALWAYS creates a new version in
	 * the series of versions.
	 * 
	 * @param irFile
	 *
	 * @return the new version or null if the new version is not found
	 */
	public ItemVersion changeCurrentIrVersion(int myVersion) {
		
		//the max version is always the current version
		if( myVersion == maxVersion ) return this.getCurrentVersion();
		
		for(ItemVersion version: versions)
		{
			if( version.getVersionNumber() == myVersion)
			{
				maxVersion = maxVersion + 1;
				ItemVersion newVersion = new ItemVersion(version.getItem(), this, maxVersion);
				this.versions.add(newVersion);
				currentVersion = newVersion;
				return newVersion;
			}
		}
		return null;
	}
	
	/**
	 * Get the item version with the given version number.  If it doesn't exist,
	 * an exception is thrown.
	 * 
	 * @param versionNumber
	 * @return the item version if found otherwise null.
	 */
	public ItemVersion getByVersionNumber(int versionNumber)
	{
		for( ItemVersion iv : versions)
		{
			if( iv.getVersionNumber() == versionNumber)
			{
				return iv;
			}
		}
		
		return null;
	}
	
	/**
	 * The versions of this item.
	 * 
	 * @return the versions
	 */
	public Set<ItemVersion> getItemVersions() {
		return Collections.unmodifiableSet(versions);
	}

	/**
	 * Set the versions for this ir file.
	 * 
	 * @param versions
	 */
	void setItemVersions(Set<ItemVersion> versions) {
		this.versions = versions;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		value += owner == null ? 0 : owner.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[VersionedItem id = ");
		sb.append(getId());
		sb.append( " largestVersion = ");
		sb.append(getLargestVersion());
		sb.append( " name = ");
		sb.append(name);
		sb.append("]");
		
		return sb.toString();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof VersionedItem)) return false;

		final VersionedItem other = (VersionedItem) o;

		if( (owner == null && other.getOwner() != null ) ||
		    (owner != null && !owner.equals(other.getOwner())) ) return false;

		if( (name == null && other.getName() != null ) || 
			(name != null && !name.equals(other.getName()))) return false;
		
		// normally we do not want to use an id - however, it would have to be this or
		// some other globally unique id.
		if( ( id != null && !id.equals(other.getId()) ) ||
			( id == null && other.getId() != null ) ) return false;
		
		return true;
	}

	/**
	 * Get the current largest ir version number.  
	 * 
	 * @return
	 */
	public int getLargestVersion() {
		return maxVersion;
	}

	/**
	 * Set the largest ir version number.
	 * 
	 * @param largestIrVersion
	 */
	 void setLargestVersion(int largestVersion) {
		this.maxVersion = largestVersion;
	}

	/**
	 * Owner of the versioned file.
	 * 
	 * @return the user who owns the file
	 */
	 public IrUser getOwner() {
		return owner;
	}

	/**
	 * Owner of the versioned file.
	 * 
	 * @param owner
	 */
	 void setOwner(IrUser owner) {
		this.owner = owner;
	}
	
	/**
	 * Change the owner for this versioned item and all
	 * items which belong to it.
	 * 
	 * @param owner
	 */
	public  void changeOwner(IrUser owner)
	{
		this.owner = owner;
		for(ItemVersion itemVersion: versions)
		{
			itemVersion.getItem().setOwner(owner);
		}
	}
	
	/**
	 * Lock this versioned file.
	 * 
	 * @param user
	 * @return
	 */
	public  boolean lock(IrUser user)
	{
		if(lockedBy == null)
		{
			lockedBy = user;
			return true;
		}
		else if( lockedBy.equals(user))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Unlock the versioned file
	 */
	public  void unLock()
	{
		lockedBy = null;
	}

	/**
	 * Determine if this versioned file is locked.
	 * 
	 * @return true if the versioned file is locked
	 */
	public  boolean isLocked() {
		return lockedBy == null;
	}
	
	/**
	 * Return true if the versioned file is locked.
	 * 
	 * @return
	 */
	public  boolean getLocked()
	{
		return isLocked();
	}

	/**
	 * Return the user who has locked the versioned file
	 * or null if no one has locked the file.
	 * 
	 * @return user who has locked the versioned file
	 */
	public  IrUser getLockedBy() {
		return lockedBy;
	}
	
	/**
	 * Set the max version.
	 * 
	 * @param maxVersion
	 */
	private void setMaxVersion(int maxVersion)
	{
		this.maxVersion = maxVersion;
	}

	/**
	 * Get the max allowed versions.
	 * 
	 * @return
	 */
	public int getMaxAllowedVersions() {
		return maxAllowedVersions;
	}

	/**
	 * Set the max allowed versions.
	 * 
	 * @param maxAllowedVersions
	 */
	public void setMaxAllowedVersions(int maxAllowedVersions) {
		this.maxAllowedVersions = maxAllowedVersions;
	}
}
