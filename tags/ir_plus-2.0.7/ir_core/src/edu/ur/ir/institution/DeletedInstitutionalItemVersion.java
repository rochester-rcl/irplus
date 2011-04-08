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

package edu.ur.ir.institution;

import edu.ur.ir.handle.HandleInfo;
import edu.ur.persistent.BasePersistent;

/**
 * Represents a deleted version of an institutional item.
 * 
 * @author Nathan Sarr
 *
 */
public class DeletedInstitutionalItemVersion extends BasePersistent{
	
	/** generated serial version id */
	private static final long serialVersionUID = -508053926140625884L;

	/** represents the parent deleted institutional item */
	private DeletedInstitutionalItem deletedInstitutionalItem;
	
	/** id of the institutional item version */
	private Long institutionalItemVersionId;	

	/** version number of the item version */
	private int versionNumber;
	
	/** handle information for the deleted version  */
	private HandleInfo handleInfo;
	
	/**  Package protected constructor */
	DeletedInstitutionalItemVersion(){}
	
	/**
	 * Construct a record for the deleted institutional item version.
	 * 
	 * @param institutionalItemVersion
	 */
	public DeletedInstitutionalItemVersion(DeletedInstitutionalItem deletedInstitutionalItem, InstitutionalItemVersion institutionalItemVersion)
	{
		setHandleInfo(institutionalItemVersion.getHandleInfo());
		setInstitutionalItemVersionId(institutionalItemVersion.getId());
		setDeletedInstitutionalItem(deletedInstitutionalItem);
		setVersionNumber(institutionalItemVersion.getVersionNumber());
		
	}

	/**
	 * Parent deleted institutional item record.
	 * 
	 * @return - the deleted institutional item
	 */
	public DeletedInstitutionalItem getDeletedInstitutionalItem() {
		return deletedInstitutionalItem;
	}

	/**
	 * Set the deleted institutional item.
	 * 
	 * @param deletedInstitutionalItem - the deleted institutional item
	 */
	public void setDeletedInstitutionalItem(
			DeletedInstitutionalItem deletedInstitutionalItem) {
		this.deletedInstitutionalItem = deletedInstitutionalItem;
	}
	
	/**
	 * Get the version number.
	 * 
	 * @return get the version number
	 */
	public int getVersionNumber() {
		return versionNumber;
	}

	/**
	 * Set the version number.
	 * 
	 * @param versionNumber
	 */
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * Get the handle info.
	 * 
	 * @return the handle info
	 */
	public HandleInfo getHandleInfo() {
		return handleInfo;
	}

	/**
	 * Set the handle info.
	 * 
	 * 
	 * @param handleInfo
	 */
	public void setHandleInfo(HandleInfo handleInfo) {
		this.handleInfo = handleInfo;
	}
	
	/**
	 * Get the institutional item version id.
	 * 
	 * @return
	 */
	public Long getInstitutionalItemVersionId() {
		return institutionalItemVersionId;
	}

	/**
	 * Set the institutional item version id.
	 * 
	 * @param institutionalItemVersionId
	 */
	public void setInstitutionalItemVersionId(Long institutionalItemVersionId) {
		this.institutionalItemVersionId = institutionalItemVersionId;
	}
	
	public int hashCode()
	{
		int value = 0;
		value += getInstitutionalItemVersionId() == null ? 0 : getInstitutionalItemVersionId().hashCode();
		return value;
	}
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof DeletedInstitutionalItemVersion)) return false;

		final DeletedInstitutionalItemVersion other = (DeletedInstitutionalItemVersion) o;
		
		if( (  getInstitutionalItemVersionId() != null && ! getInstitutionalItemVersionId().equals(other. getInstitutionalItemVersionId()) ) ||
		    (  getInstitutionalItemVersionId() == null && other. getInstitutionalItemVersionId() != null ) ) return false;

		return true;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer( "[ id = " );
		sb.append(id);
		sb.append(" Deleted Institutional item version Id= ");
		sb.append(institutionalItemVersionId);
		sb.append(" handle info = " );
		sb.append(handleInfo);
		sb.append(" version number  = ");
		sb.append(versionNumber );
		sb.append("]");
		return sb.toString();
	}


}
