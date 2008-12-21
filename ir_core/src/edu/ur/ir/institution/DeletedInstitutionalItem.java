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

import java.util.Date;

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

	/** Date the item was withdrawn. */
	private Date deletedDate;
	
	/** Reason this item was withdran */
	private Long institutionalItemId;
	
	private String institutionalItemName;
	
	private String institutionalCollectionName;
	
	/** User who withdrew the  version*/
	private IrUser deletedBy;
	
	/** Package protected constructor */
	DeletedInstitutionalItem(){}
	
	/** Package protected constructor */
	public DeletedInstitutionalItem(Long institutionalItemId, String itemName, String collectionName) {
		
		this.institutionalItemId = institutionalItemId;
		this.institutionalItemName = itemName;
		this.institutionalCollectionName = collectionName;
	}
	
	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}

	public void setInstitutionalItemId(Long institutionalItemId) {
		this.institutionalItemId = institutionalItemId;
	}

	public String getInstitutionalItemName() {
		return institutionalItemName;
	}

	public void setInstitutionalItemName(String institutionalItemName) {
		this.institutionalItemName = institutionalItemName;
	}

	public String getInstitutionalCollectionName() {
		return institutionalCollectionName;
	}

	public void setInstitutionalCollectionName(String institutionalCollectionName) {
		this.institutionalCollectionName = institutionalCollectionName;
	}

	public IrUser getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(IrUser deletedBy) {
		this.deletedBy = deletedBy;
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
		StringBuffer sb = new StringBuffer("[ Deleted Institutional item Id= ");
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
