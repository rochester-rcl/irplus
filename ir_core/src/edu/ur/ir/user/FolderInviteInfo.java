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

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.persistent.BasePersistent;

/**
 * Represents a user who has set up auto sharing for a given folder.
 * 
 * @author Nathan Sarr
 *
 */
public class FolderInviteInfo extends BasePersistent
{
	/* eclipse generated id */
	private static final long serialVersionUID = 6886925184868598247L;
	
	/* personal folder to be shared */
	private PersonalFolder personalFolder;
	
	/* email to share the personal folder with */
	private String email;
	
	/* date the invite info was created */
	private Timestamp dateCreated;
	
	/* Permissions given to the collaborator */
	private Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();


	/** Default constructor for the folder invite info */
    public FolderInviteInfo(){}

	/**
	 * Constructor for the folder invite info.
	 * 
	 * @param personalFolder - personal folder 
	 * @param email - email for the folder invite info
	 */
	public FolderInviteInfo(PersonalFolder personalFolder, String email, Set<IrClassTypePermission> permissions)
	{
		this.personalFolder = personalFolder;
		this.email = email;
		this.dateCreated = new Timestamp(new Date().getTime());
		this.permissions = permissions;
	}
	
	/**
	 * Get the personal folder 
	 * 
	 * @return the personal folder shared with the email.
	 */
	public PersonalFolder getPersonalFolder() {
		return personalFolder;
	}

	/**
	 * Get the email address shared with this user.
	 * 
	 * @return - the email address shared with the user.
	 */
	public String getEmail() {
		return email;
	}
	
    /**
     * Set the permissions for the folder info.
     * 
     * @param permissions
     */
    public void setPermissions(Set<IrClassTypePermission> permissions) {
		this.permissions = permissions;
	}


	/**
	 * Get the permissions assigned to this folder.
	 * 
	 * @return
	 */
	public Set<IrClassTypePermission> getPermissions() {
		return permissions;
	}
	
	/**
	 * Get the date the invite was created.
	 * 
	 * @return - date the invite info was created
	 */
	public Timestamp getDateCreated() {
		return dateCreated;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof FolderInviteInfo)) return false;

		final FolderInviteInfo other = (FolderInviteInfo) o;

		if( ( personalFolder != null && !personalFolder.equals(other.getPersonalFolder()) ) ||
			( personalFolder == null && other.getPersonalFolder() != null ) ) return false;

		if( ( email != null && !email.equals(other.getEmail()) ) ||
			( email == null && other.getEmail() != null ) ) return false;
		
		return true;
	}


	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += email == null ? 0 : email.hashCode();
		value += personalFolder == null ? 0 : personalFolder.hashCode();
		return value;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ folder invite info id = ");
		sb.append(id);
		sb.append("email = ");
		sb.append(email);
		sb.append("personalFolder =  ");
		sb.append(personalFolder);
		sb.append("]");
		return sb.toString();
	}

}
