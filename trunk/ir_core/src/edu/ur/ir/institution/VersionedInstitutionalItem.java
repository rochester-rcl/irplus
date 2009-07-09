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

import java.util.HashSet;
import java.util.Set;

import edu.ur.ir.item.GenericItem;
import edu.ur.persistent.BasePersistent;

/**
 * Represents a publication that can have  
 * multiple versions.  The current version is the version
 * with the largest version number.  The name and name data
 * is DUPLICATED in this class for database performance and
 * to remove the need to traverse down to the current versioned item
 * for name information.
 * 
 * THIS MEANS editing the name data must be updated through this class
 * when editing the most recent version of the item.
 * 
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 */
public class VersionedInstitutionalItem extends BasePersistent {

	/**  Eclipse generated id. */
	private static final long serialVersionUID = -4235230376169888073L;

	/** Published items which hold publishing information for */
	private Set<InstitutionalItemVersion> institutionalItemVersions = new HashSet<InstitutionalItemVersion>();
	
	/**  Current version of the publication */
	private InstitutionalItemVersion currentVersion;

	/**  Starting number for all publication versions */
	public static final int INITIAL_ITEM_VERSION = 0;
	
	/**  The current highest publication version number */
	private int maxVersion = INITIAL_ITEM_VERSION;
	

	
	/**
	 * Package protected versioned publication constructor.
	 */
	VersionedInstitutionalItem(){}
	
	/**
	 * Create a new versioned publication with the initial version  
	 * 
	 * @param item Item to create versioned institutional Item
	 */
	public VersionedInstitutionalItem(GenericItem item)
	{
		addNewVersion(item);
	}
	
	/**
	 * Add a new version of the published item.  This version becomes
	 * the new current version.
	 * 
	 * @param item
	 */
	public InstitutionalItemVersion addNewVersion(GenericItem item)
	{
		// do not include the articles in the lower case name
		item.setPublishedToSystem(true);
		maxVersion = maxVersion + 1;
		InstitutionalItemVersion institutionalItemVersion = new InstitutionalItemVersion(item, this, maxVersion);
		institutionalItemVersions.add(institutionalItemVersion);
		currentVersion = institutionalItemVersion;
		return institutionalItemVersion;
	}

	/**
	 * Get the published.
	 * 
	 * @param versionNumber - version number to retrieve
	 * @return - version if it exists otherwise return null.
	 */
	public InstitutionalItemVersion getInstitutionalItemVersion(int versionNumber)
	{
		for(InstitutionalItemVersion institutionalItemVersion : institutionalItemVersions)
		{
			if( institutionalItemVersion.getVersionNumber() == versionNumber )
			{
				return institutionalItemVersion;
			}
		}
		
		return null;
	}
	
	public InstitutionalItemVersion getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(InstitutionalItemVersion currentVersion) {
		this.currentVersion = currentVersion;
	}

	public Set<InstitutionalItemVersion> getInstitutionalItemVersions() {
		return institutionalItemVersions;
	}

	public void setInstitutionalItemVersions(Set<InstitutionalItemVersion> institutionalItemVersions) {
		this.institutionalItemVersions = institutionalItemVersions;
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
		value += id == null? 0 : id.hashCode();
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
		if (!(o instanceof VersionedInstitutionalItem)) return false;

		final VersionedInstitutionalItem other = (VersionedInstitutionalItem) o;

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

}
