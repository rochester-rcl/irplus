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

package edu.ur.ir.user;

import org.springframework.security.core.GrantedAuthority;



import edu.ur.security.PersistentRole;
import edu.ur.ir.security.Sid;
import edu.ur.persistent.CommonPersistent;

/**
 * The Roles that can be assigned to users.
 * 
 * @author Nathan Sarr
 *
 */
public class IrRole extends CommonPersistent implements GrantedAuthority, PersistentRole, Sid{
	
	/** The administrator role */
	public static final String ADMIN_ROLE ="ROLE_ADMIN";
	
	/** indicates a user is a collaborator  */
	public static final String COLLABORATOR_ROLE ="ROLE_COLLABORATOR";
	
	/** user can author documents in the system */
	public static final String AUTHOR_ROLE ="ROLE_AUTHOR";
	
	/** user can be a researcher  */
	public static final String RESEARCHER_ROLE ="ROLE_RESEARCHER";
	
	/** basic user role to create group spaces  */
	public static final String GROUP_SPACE_ROLE ="ROLE_GROUP_SPACE";

	/** basic user role to log into the system  */
	public static final String USER_ROLE ="ROLE_USER";
	
	/** user can administer collections in the system  */
	public static final String COLLECTION_ADMIN_ROLE = "ROLE_COLLECTION_ADMIN";
	
	/** this class is a type of security id */
	public static final String ROLE_SID_TYPE = "ROLE_SID_TYPE";

	/**
	 * Eclipse generated id.
	 */
	private static final long serialVersionUID = 1136721460566237677L;

	/**
	 * The name of the role.
	 * 
	 * @see org.acegisecurity.GrantedAuthority#getAuthority()
	 */
	public String getAuthority() {
		return name;
	}

	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof IrRole)) return false;

		final IrRole other = (IrRole) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;

		return true;
	}
	
	/**
	 * Return the string representation.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Name = ");
		sb.append(name);
		sb.append(" description ");
		sb.append(description);
		sb.append(" id = ");
		sb.append(id);
		sb.append(" version = ");
		sb.append(version);
		sb.append("]");
		return sb.toString();
	}

	
	/**
	 * The sid type for this class.
	 * 
	 * @see edu.ur.ir.security.Sid#getSidType()
	 */
	public String getSidType() {
		return ROLE_SID_TYPE;
	}

	
	public int compareTo(Object o) {
		if(this.equals(o))
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}
}
