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
 * Represents information for a re-instatement of a withrdawn
 * InstitutionalItemVersion.
 * 
 * @author Nathan Sarr
 *
 */
public class ReinstateToken extends BasePersistent{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 2324220244089802342L;

	/** Date the item was withdrawn. */
	private Date date;
	
	/** Reason this item was withdrawn */
	private String reason;
	
	/** User who re-instated the  version*/
	private IrUser user;
	
	/** Institutional item version this re-instate token is for */
	private InstitutionalItemVersion institutionalItemVersion;
	
	/** Package Protected reinstate token*/
	ReinstateToken(){};
	
	public ReinstateToken(IrUser reInstateUser, 
			String reInstateReason, 
			InstitutionalItemVersion institutionalItemVersion)
	{
		setUser(reInstateUser);
		setReason(reInstateReason);
		setDate(new Date());
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

		final ReinstateToken other = (ReinstateToken) o;
		
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
		StringBuffer sb = new StringBuffer("[ re instate  user = ");
		sb.append(user);
		sb.append("re instate reason = " );
		sb.append(reason);
		sb.append(" institutional item version = ");
		sb.append(institutionalItemVersion);
		sb.append("]");
		return sb.toString();
	}

	public Date getDate() {
		return date;
	}

	void setDate(Date dateReInstated) {
		this.date = dateReInstated;
	}

	public String getReason() {
		return reason;
	}

	void setReason(String reInstateReason) {
		this.reason = reInstateReason;
	}

	public IrUser getUser() {
		return user;
	}

	void setUser(IrUser reInstateUser) {
		this.user = reInstateUser;
	}

	public InstitutionalItemVersion getInstitutionalItemVersion() {
		return institutionalItemVersion;
	}

	void setInstitutionalItemVersion(
			InstitutionalItemVersion institutionalItemVersion) {
		this.institutionalItemVersion = institutionalItemVersion;
	}

}
