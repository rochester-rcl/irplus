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

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;

/**
 * This represents a particular item version that has been published.
 * 
 * @author Nathan Sarr
 *
 */
public class InstitutionalItemVersion extends BasePersistent{
	
	/**eclipse generated id */
	private static final long serialVersionUID = -4522488346384119099L;
	
	/**  Particular Item that is in the institutional item */
	private GenericItem item;
	
	/** information about the most recent withdraw if not withdrawn, this object is null*/
	private WithdrawnToken withdrawnToken;
	
	/**  Date this item was submitted to this collection.*/
	private Timestamp dateOfDeposit;
	
	/** Parent versioned publication  */
	private VersionedInstitutionalItem versionedInstitutionalItem;
	
	/** Particular version number in the versioned publication */
	private int versionNumber;	

	/** history of withdraws*/
	private Set<WithdrawnToken> withdrawHistory = new HashSet<WithdrawnToken>();
	
	/** history of reinstate actions*/
	private Set<ReinstateToken> reinstateHistory = new HashSet<ReinstateToken>();
	
	/** handle information for the item  */
	private HandleInfo handleInfo;
	
	InstitutionalItemVersion(){
		dateOfDeposit = new java.sql.Timestamp(new Date().getTime());
	}
	
	/**
	 * Represents a institutional item version.
	 * 
	 * @param item
	 * @param versionedInstitutionalItem
	 * @param versionNumber
	 */
	InstitutionalItemVersion(GenericItem item,
			VersionedInstitutionalItem versionedInstitutionalItem, int versionNumber)
	{
		setItem(item);
		setVersionedInstitutionalItem(versionedInstitutionalItem);
		setVersionNumber(versionNumber);
		dateOfDeposit = new java.sql.Timestamp(new Date().getTime());
		
	}
	
	/**
	 * Get the date this item was submitted to this collection
	 * 
	 * @return
	 */
	public Timestamp getDateOfDeposit() {
		return dateOfDeposit;
	}

	/**
	 * Set the date this item was submitted to this collection.
	 * 
	 * @param dateOfDeposit
	 */
	public void setDateOfDeposit(Timestamp dateOfDeposit) {
		this.dateOfDeposit = dateOfDeposit;
	}
	
	/**
	 * Determine if this institutional item is withdrawn.
	 * 
	 * @return true if the item is withdrawn
	 */
	public boolean getWithdrawn() {
		return withdrawnToken != null;
	}
	
	/**
	 * Determine if this version has been withdrawn.
	 * 
	 * @return true if the item is withdrawn
	 */
	public boolean isWithdrawn()
	{
		return getWithdrawn();
	}

	/**
	 * Withdraw the item from the institution.  Returns the created token
	 * or null if a token is not created.  A token will not be created if the 
	 * item is already withdrawn.
	 * 
	 * @param reason
	 */
	public WithdrawnToken withdraw(IrUser user, String reason, boolean showMetadata)
	{
	    if( !getWithdrawn() )
	    {
	    	withdrawnToken = new WithdrawnToken(user, reason, showMetadata, this);
	    	withdrawHistory.add(withdrawnToken);
	    	return withdrawnToken;
	    }
	    return null;
	}
	
	/**
	 * Allows a withdrawn item to be re-instated
	 * 
	 * @param user - user who re-instated the object
	 * @param reason - reason for the re-instatement
	 */
	public ReinstateToken reInstate(IrUser user, String reason)
	{
	    if(getWithdrawn())
	    {
	    	ReinstateToken token = new ReinstateToken(user, reason, this);
	    	reinstateHistory.add(token);
	    	withdrawnToken = null;
	    	return token;
	    }
	    return null;
	}
	

	/**
	 * To String for outputting a institutional item version
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" withdrawn = ");
		sb.append(getWithdrawn());
		sb.append(" item = " );
		sb.append(item);
		sb.append("]");
		
		
		return sb.toString();
		
	}
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof InstitutionalItemVersion)) return false;

		final InstitutionalItemVersion other = (InstitutionalItemVersion) o;
		
		if( ( getItem() != null && !getItem().equals(other.getItem()) ) ||
		    ( getItem() == null && other.getItem() != null ) ) return false;

		if( ( versionedInstitutionalItem != null && !versionedInstitutionalItem.equals(other.getVersionedInstitutionalItem()) ) ||
			    ( versionedInstitutionalItem == null && other.getVersionedInstitutionalItem() != null ) ) return false;

		return true;
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
		value += getItem() == null? 0 : getItem().hashCode();
		return value;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	public VersionedInstitutionalItem getVersionedInstitutionalItem() {
		return versionedInstitutionalItem;
	}

	public void setVersionedInstitutionalItem(
			VersionedInstitutionalItem versionedInstitutionalItem) {
		this.versionedInstitutionalItem = versionedInstitutionalItem;
	}

	public GenericItem getItem() {
		return item;
	}

	public void setItem(GenericItem item) {
		this.item = item;
	}

	public WithdrawnToken getWithdrawnToken() {
		return withdrawnToken;
	}

	public void setWithdrawnToken(WithdrawnToken withdrawnToken) {
		this.withdrawnToken = withdrawnToken;
	}

	public Set<WithdrawnToken> getWithdrawHistory() {
		return Collections.unmodifiableSet(withdrawHistory);
	}

	void setWithdrawHistory(Set<WithdrawnToken> withdrawHistory) {
		this.withdrawHistory = withdrawHistory;
	}

	public Set<ReinstateToken> getReinstateHistory() {
		return Collections.unmodifiableSet(reinstateHistory);
	}

	void setReinstateHistory(Set<ReinstateToken> reInstateHistory) {
		this.reinstateHistory = reInstateHistory;
	}
	
	public HandleInfo getHandleInfo() {
		return handleInfo;
	}

	public void setHandleInfo(HandleInfo handleInfo) {
		this.handleInfo = handleInfo;
	}


}
