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

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;

/**
 * Represents deleted institutional item.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DeletedInstitutionalItem extends BasePersistent{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 2632928311286244395L;
	
	/** List of deleted institutional item versions */
	private Set<DeletedInstitutionalItemVersion> deletedInstitutionalItemVersions = new HashSet<DeletedInstitutionalItemVersion>(); 

	/** Date the item was withdrawn. */
	private Date deletedDate;
	
	/** Reason this item was withdran */
	private Long institutionalItemId;
	
	/** name of the institutional item */
	private String institutionalItemName;
	
	/** Name of the collection the item was deleted from */
	private String institutionalCollectionName;
	
	/** User who withdrew the  version*/
	private IrUser deletedBy;
	
	/** Package protected constructor */
	DeletedInstitutionalItem(){}
	
	/** Constructor */
	public DeletedInstitutionalItem(InstitutionalItem institutionalItem) {
		
		institutionalItemId = institutionalItem.getId();
		institutionalItemName = institutionalItem.getName();
		institutionalCollectionName = institutionalItem.getInstitutionalCollection().getName();
		
		Set<InstitutionalItemVersion> versions = institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersions();
		for(InstitutionalItemVersion version : versions)
		{
			deletedInstitutionalItemVersions.add(new DeletedInstitutionalItemVersion(this, version));
		}
		
	}
	
	/**
	 * Date this item was deleted.
	 * 
	 * @return
	 */
	public Date getDeletedDate() {
		return deletedDate;
	}

	/**
	 * Set the deleted date.
	 * 
	 * @param deletedDate
	 */
	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	/**
	 * Get the institutional item id.
	 * 
	 * @return the institutional item id.
	 */
	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	/**
	 * Set the institutional item id.
	 * 
	 * @param institutionalItemId
	 */
	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}

	/**
	 * Get the institutional item name.
	 * 
	 * @return get the institutional item name.
	 */
	public String getInstitutionalItemName() {
		return institutionalItemName;
	}

	/**
	 * Set the institutional item name.
	 * 
	 * @param institutionalItemName
	 */
	public void setInstitutionalItemName(String institutionalItemName) {
		this.institutionalItemName = institutionalItemName;
	}

	/**
	 * Get the parent collection this deleted item was in.
	 * 
	 * @return the institutional collection name
	 */
	public String getInstitutionalCollectionName() {
		return institutionalCollectionName;
	}

	/**
	 * Set the institutional collection name.
	 * 
	 * @param institutionalCollectionName
	 */
	public void setInstitutionalCollectionName(String institutionalCollectionName) {
		this.institutionalCollectionName = institutionalCollectionName;
	}

	/**
	 * Get the user who deleted the item.
	 * 
	 * @return
	 */
	public IrUser getDeletedBy() {
		return deletedBy;
	}

	/**
	 * Set the user who deleted the item.
	 * 
	 * @param deletedBy
	 */
	public void setDeletedBy(IrUser deletedBy) {
		this.deletedBy = deletedBy;
	}
	
	/**
	 * Get the list of version information for this deleted item.
	 * 
	 * @return list of versions for the deleted information
	 */
	public Set<DeletedInstitutionalItemVersion> getDeletedInstitutionalItemVersions() {
		return Collections.unmodifiableSet(deletedInstitutionalItemVersions);
	}

	/**
	 * Set the deleted institutional item versions.
	 * 
	 * @param deletedInstitutionalItemVersions
	 */
	public void setDeletedInstitutionalItemVersions(
			Set<DeletedInstitutionalItemVersion> deletedInstitutionalItemVersions) {
		this.deletedInstitutionalItemVersions = deletedInstitutionalItemVersions;
	}

	public int hashCode()
	{
		int value = 0;
		value += getInstitutionalItemId() == null ? 0 : getInstitutionalItemId().hashCode();
		return value;
	}
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof DeletedInstitutionalItem)) return false;

		final DeletedInstitutionalItem other = (DeletedInstitutionalItem) o;
		
		if( ( getInstitutionalItemId() != null && !getInstitutionalItemId().equals(other.getInstitutionalItemId()) ) ||
		    ( getInstitutionalItemId() == null && other.getInstitutionalItemId() != null ) ) return false;

		return true;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append( " Deleted Institutional item Id= ");
		sb.append(institutionalItemId);
		sb.append(" item name = " );
		sb.append(institutionalItemName);
		sb.append(" collection name = ");
		sb.append(institutionalCollectionName);
		sb.append(" deleted by  = ");
		sb.append(deletedBy);
		sb.append(" deleted date = ");
		sb.append(deletedDate);
		sb.append("]");
		return sb.toString();
	}
}
