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

package edu.ur.ir.groupspace;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.ur.ir.user.IrUser;
import edu.ur.persistent.CommonPersistent;

/**
 * Represents a group of users within a group space.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceGroup extends CommonPersistent{
	
	/* eclipse generated id */
	private static final long serialVersionUID = 186970723642439395L;

	/* list of users in the group */
	private Set<IrUser> users = new HashSet<IrUser>();
	
	/* Owning group Space  */
	private GroupWorkspace groupWorkspace;
	
	/* lower case name value */
	private String lowerCaseName;

	/**
	 * Package protected constructor
	 */
	GroupWorkspaceGroup(){}
	
	/**
	 * Create a group space with the given name.
	 * 
	 * @param name
	 */
	public GroupWorkspaceGroup(GroupWorkspace groupSpace, String name)
	{
		this.groupWorkspace = groupSpace;
		setName(name);
	}
	
	/**
	 * Create a group space with the given name and description.
	 * 
	 * @param groupSpace
	 * @param name
	 * @param description
	 */
	public GroupWorkspaceGroup(GroupWorkspace groupSpace, String name, String description)
	{
		this(groupSpace, name);
		setDescription(description);
	}

	/**
	 * Get the list of users in this group space.
	 * 
	 * @return
	 */
	public Set<IrUser> getUsers() {
		return Collections.unmodifiableSet(users);
	}

	/**
	 * Set the users in this group space group.
	 * 
	 * @param users
	 */
	void setUsers(Set<IrUser> users) {
		this.users = users;
	}
	
	/**
	 * Get the group space.
	 * 
	 * @return
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}

	/**
	 * Set the group space.
	 * 
	 * @param groupSpace
	 */
	void setGroupWorkspace(GroupWorkspace groupSpace) {
		this.groupWorkspace = groupSpace;
	}

	/**
	 * Get the lower case name value.
	 * 
	 * @return
	 */
	public String getLowerCaseName() {
		return lowerCaseName;
	}
	
	/**
	 * Set the name - also sets the lower case name value.
	 * 
	 * @see edu.ur.persistent.CommonPersistent#setName(java.lang.String)
	 */
	public void setName(String name)
	{
		super.setName(name);
		this.lowerCaseName = name.toLowerCase();
	}
	
	
	/**
	 * Hash code is based on the name of
	 * the group space
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		value += groupWorkspace == null ? 0 : groupWorkspace.hashCode();
		return value;
	}
	
	/**
	 * Equals is tested based on name ignoring case 
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof GroupWorkspaceGroup)) return false;

		final GroupWorkspaceGroup other = (GroupWorkspaceGroup) o;

		if( ( lowerCaseName != null && !lowerCaseName.equalsIgnoreCase(other.getLowerCaseName()) ) ||
			( lowerCaseName == null && other.getLowerCaseName() != null ) ) return false;

		if( ( groupWorkspace != null && !groupWorkspace.equals(other.getGroupWorkspace()) ) ||
			( groupWorkspace == null && other.getGroupWorkspace() != null ) ) return false;

		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ Group space id = ");
		sb.append(id);
		sb.append( " name = ");
		sb.append(name);
		sb.append(" description = ");
		sb.append(description);
		sb.append("]");
		return sb.toString();
	}


}
