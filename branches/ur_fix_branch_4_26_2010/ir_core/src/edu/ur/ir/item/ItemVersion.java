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

import edu.ur.persistent.BasePersistent;

/**
 * This represents a single version of an item in a set of item
 * versions.  This also creates a link between the single version with
 * the container that holds the list of versions.
 * 
 * @author Nathan Sarr
 *
 */
public class ItemVersion extends BasePersistent{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 2711372177051503562L;

	/** Item this version points to. */
	private GenericItem item;
	
	/** Parent versioned item class */
	private VersionedItem versionedItem;
	
	/** Version number this item represents */
	private int versionNumber;

	/**
	 * Package protected constructor
	 */
	ItemVersion(){}
	
	/**
	 * Package protected constructor
	 * 
	 * @param item
	 * @param versionedItem
	 * @param versionNumber
	 */
	ItemVersion(GenericItem item, VersionedItem versionedItem, int versionNumber)
	{
		if(versionedItem == null )
		{
			throw new IllegalStateException("versionedItem cannot be null");
		}
		
		if( item == null )
		{
			throw new IllegalStateException("item cannot be null");
		}
		
		if( versionNumber <= 0 )
		{
			throw new IllegalStateException("Version number must be greater than 0 verion = " + versionNumber);
		}
		this.item = item;
		this.versionedItem = versionedItem;
		this.versionNumber = versionNumber;
	}

	/**
	 * Get the item
	 * 
	 * @return the item for this version
	 */
	public GenericItem getItem() {
		return item;
	}

	/**
	 * Set the item for this version.
	 * 
	 * @param item
	 */
	void setItem(GenericItem item) {
		this.item = item;
	}

	/**
	 * Get the versioned item for this version.
	 * 
	 * @return versioned item
	 */
	public VersionedItem getVersionedItem() {
		return versionedItem;
	}

	/**
	 * Set the versioned item 
	 * 
	 * @param versionedItem
	 */
	void setVersionedItem(VersionedItem versionedItem) {
		this.versionedItem = versionedItem;
	}

	/**
	 * Get the version number for this instance
	 * 
	 * @return version number
	 */
	public int getVersionNumber() {
		return versionNumber;
	}

	/**
	 * Set the version number
	 * 
	 * @param versionNumber
	 */
	void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append( " version number = ");
		sb.append(versionNumber);
		sb.append(" item = \n");
		sb.append(item);
		sb.append(" versionedItem = \n");
		sb.append(versionedItem);
		sb.append("]");
		
		return sb.toString();
	}
	
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ItemVersion)) return false;

		final ItemVersion other = (ItemVersion) o;

		if( getVersionedItem() != null && other.getVersionedItem() == null) return false;
		if(!getVersionedItem().equals(other.getVersionedItem())) return false;
		
		if(getVersionNumber() != other.getVersionNumber()) return false;
		
		return true;
	}
	
	public int hashCode()
	{
		int value = 0;
		value += getVersionNumber();
		value += getItem() == null ? 0 : getItem().hashCode();
		return value;
	}
}
