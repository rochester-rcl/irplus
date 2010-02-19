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

package edu.ur.ir.user;

import edu.ur.persistent.BasePersistent;

/**
 * Represents an external user account that can be checked against.
 * 
 * @author Nathan Sarr
 *
 */
public class ExternalUserAccount extends BasePersistent{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 1233063477455271165L;

	/** Represents the user  */
	private IrUser user;
	
	/**  Represents the id of the external user */
	private String externalUserAccountName;
	
	/** The type of external account */
	private ExternalAccountType externalAccountType;
	
	/**
	 * Package protected constructor
	 */
	ExternalUserAccount(){};
	
	/**
	 * Constructor for an external user account.
	 * 
	 * @param user - user linked to the account
	 * @param externalUserAccountName - external user account name
	 * @param externalAuthorizationAccountType - authorization account type
	 */
	public ExternalUserAccount(IrUser user, 
			String externalUserAccountName, 
			ExternalAccountType externalAuthorizationAccountType)
	{
	    setUser(user);
	    setExternalUserAccountName(externalUserAccountName);
	    setExternalAccountType(externalAuthorizationAccountType);
	}

	/**
	 * Get the user who owns this external account.
	 * 
	 * @return user
	 */
	public IrUser getUser() {
		return user;
	}

	/**
	 * Set the user who owns the account.
	 * 
	 * @param user
	 */
	void setUser(IrUser user) {
		this.user = user;
	}

	/**
	 * The user name for the external account.
	 * 
	 * @return - the name of the external account.
	 */
	public String getExternalUserAccountName() {
		return externalUserAccountName;
	}

	/**
	 * Set the external user account name.
	 * 
	 * @param externalUserAccountName
	 */
	public void setExternalUserAccountName(String externalUserAccountName) {
		this.externalUserAccountName = externalUserAccountName;
	}

	/**
	 * External account type.
	 * 
	 * @return
	 */
	public ExternalAccountType getExternalAccountType() {
		return externalAccountType;
	}

	/**
	 * Set the external account type.
	 * 
	 * @param externalAccountType
	 */
	public void setExternalAccountType(
			ExternalAccountType externalAccountType) {
		this.externalAccountType = externalAccountType;
	}
	
	/**
	 * To string method
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" externalUserAccountName = ");
		sb.append(externalUserAccountName);
		sb.append(" externalAccountType = "); 
		sb.append(externalAccountType);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Hash code creation.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int hash = 0;
    	hash += user == null ? 0 : user.hashCode();
    	hash += externalUserAccountName == null ? 0 : externalUserAccountName.hashCode();
    	hash += externalAccountType == null ? 0 : externalAccountType.hashCode();
    	return hash;
	}
	
	/**
	 * Determines equality.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ExternalUserAccount)) return false;

		final ExternalUserAccount other = (ExternalUserAccount) o;

		if( ( user != null && !user.equals(other.getUser()) ) ||
			( user == null && other.getUser() != null ) ) return false;

		if( ( externalUserAccountName != null && !externalUserAccountName.equals(other.getExternalUserAccountName()) ) ||
			( externalUserAccountName == null && other.getExternalUserAccountName() != null ) ) return false;

		if( ( externalAccountType != null && !externalAccountType.equals(other.getExternalAccountType()) ) ||
			( externalAccountType == null && other.getExternalAccountType() != null ) ) return false;

		return true;
	}

}
