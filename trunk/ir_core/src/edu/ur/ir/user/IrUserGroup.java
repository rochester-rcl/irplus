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

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

import edu.ur.ir.security.Sid;
import edu.ur.persistent.CommonPersistent;

/**
 * Create a group of users for permissions.
 * 
 * @author Nathan Sarr
 *
 */
public class IrUserGroup extends CommonPersistent implements Sid{
	
	public static final String GROUP_SID_TYPE = "GROUP_SID_TYPE";
	
	/**  Eclipse generated id. */
	private static final long serialVersionUID = -4489281606591244383L;
	
	/**  Set of users who belong to this group. */
	private Set<IrUser> users = new HashSet<IrUser>();
	
	/** Users who can administer this user group */
	private Set<IrUser> administrators = new HashSet<IrUser>();
	
	/**  Package protected constructor. */
	IrUserGroup(){};
	
	/**
	 * Default Public constructor.
	 * 
	 * @param name of the group
	 */
	public IrUserGroup(String name)
	{
		this(name, null);
	}
	
	/**
	 * Default Public constructor.
	 * 
	 * @param name of the group
	 */
	public IrUserGroup(String name, String description)
	{
		setName(name);
		setDescription(description);
	}

	

	/**
	 * Get the users for this group.
	 * 
	 * @return
	 */
	public Set<IrUser> getUsers() {
		return Collections.unmodifiableSet(users);
	}
	
	/**
	 * Add the user to this group.
	 * 
	 * @param user
	 */
	public void addUser(IrUser user)
	{
		users.add(user);
	}
	
	/**
	 * Remove user from this group 
	 * @param user
	 */
	public boolean removeUser(IrUser user)
	{
		boolean removed = users.remove(user);
		return removed;
	}
	
	/**
	 * Add the user to this group.
	 * 
	 * @param user
	 */
	public void addAdministrator(IrUser user)
	{
		administrators.add(user);
	}
	
	/**
	 * Remove user from this group 
	 * @param user
	 */
	public boolean removeAdministrator(IrUser user)
	{
		boolean removed = administrators.remove(user);
		return removed;
	}
	
	/**
	 * Set the users for this group.
	 * 
	 * @param users
	 */
	void setUsers(Set<IrUser> users) {
		this.users = users;
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
		if (!(o instanceof IrUserGroup)) return false;

		final IrUserGroup other = (IrUserGroup) o;

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
		return GROUP_SID_TYPE;
	}

	public Set<IrUser> getAdministrators() {
		return Collections.unmodifiableSet(administrators);
	}

	void setAdministrators(Set<IrUser> administrators) {
		this.administrators = administrators;
	}

}
