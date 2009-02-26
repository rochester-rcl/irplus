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
 * Represents a withdraw of an institutional item.
 * 
 * @author Nathan Sarr
 *
 */
public class WithdrawnToken extends BasePersistent{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -2832160995762046047L;

	/** Date the item was withdrawn. */
	private Date date;
	
	/** Reason this item was withdrawn */
	private String reason;
	
	/** User who withdrew the  version*/
	private IrUser user;
	
	/** Indicates whether metadata should be shown for withdrawn publications */
	private boolean showMetadata = false;
	
	/** Institutional item version this withdrawn token is for */
	private InstitutionalItemVersion institutionalItemVersion;
	
	/** Package protected constructor */
	WithdrawnToken(){}
	
	public WithdrawnToken(IrUser withdrawUser, 
			String withdrawnReason, 
			boolean showMetadata, 
			InstitutionalItemVersion institutionalItemVersion)
	{
		setUser(withdrawUser);
		setReason(withdrawnReason);
		setDate(new Date());
		setShowMetadata(showMetadata);
		setInstitutionalItemVersion(institutionalItemVersion);
	}
	
	public int hashCode()
	{
		int value = 0;
		value += getUser() == null? 0 : getUser().hashCode();
		value += getDate() == null ? 0 : getDate().hashCode();
		value += getInstitutionalItemVersion() == null ? 0 : getInstitutionalItemVersion().hashCode();
		return value;
	}
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof WithdrawnToken)) return false;

		final WithdrawnToken other = (WithdrawnToken) o;
		
		if( ( getUser() != null && !getUser().equals(other.getUser()) ) ||
		    ( getUser() == null && other.getUser() != null ) ) return false;

		if( ( getDate() != null && !getDate().equals(other.getDate()) ) ||
			( getDate() == null && other.getDate() != null ) ) return false;
		
		if( ( getInstitutionalItemVersion() != null && !getInstitutionalItemVersion().equals(other.getInstitutionalItemVersion()) ) ||
			( getInstitutionalItemVersion() == null && other.getInstitutionalItemVersion() != null ) ) return false;

		return true;
	}
	
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ withdraw user = ");
		sb.append(user);
		sb.append("withdrawn reason = " );
		sb.append(reason);
		sb.append(" show metadata = ");
		sb.append(showMetadata);
		sb.append(" institutional item version = ");
		sb.append("institutionalItemVersion");
		sb.append("]");
		return sb.toString();
	}

	public Date getDate() {
		return date;
	}

	void setDate(Date dateWithdrawn) {
		this.date = dateWithdrawn;
	}

	public String getReason() {
		return reason;
	}

	void setReason(String withdrawnReason) {
		this.reason = withdrawnReason;
	}

	public IrUser getUser() {
		return user;
	}

	void setUser(IrUser withdrawUser) {
		this.user = withdrawUser;
	}

	public boolean isShowMetadata() {
		return showMetadata;
	}

	void setShowMetadata(boolean showMetadata) {
		this.showMetadata = showMetadata;
	}

	public InstitutionalItemVersion getInstitutionalItemVersion() {
		return institutionalItemVersion;
	}

	void setInstitutionalItemVersion(
			InstitutionalItemVersion institutionalItemVersion) {
		this.institutionalItemVersion = institutionalItemVersion;
	}
}
