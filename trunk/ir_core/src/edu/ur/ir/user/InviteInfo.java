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

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.persistent.BasePersistent;

/**
 * Invite information
 * 
 * @author Sharmila Ranganathan
 *
 */
public class InviteInfo extends BasePersistent {

	/* Eclipse generated Id	 */
	private static final long serialVersionUID = 6007214729437637359L;

	/* Email id - to send the invitation to */
	private String email;
	
	/* Token sent to the user */
	private String token;

	/* Invite message */
	private String inviteMessage;

	/* User sending the invitation */
	private IrUser user;
	
	/* Versioned Files that has to be shared */
	private Set<VersionedFile> files = new HashSet<VersionedFile>();
	
	/* Permissions given to the user */
	private Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
	
	/* date the invite info was created */
	private Timestamp createdDate;



	/**
	 * Default Constructor
	 */
	InviteInfo() {}
	
	/**
	 *  Constructor 
	 */
	public InviteInfo(IrUser user, Set<VersionedFile> versionedFile) {
		setUser(user);
		setFiles(versionedFile);
		this.createdDate = new Timestamp(new Date().getTime());
	}

	/**
	 *  Constructor 
	 */
	public InviteInfo(IrUser user, VersionedFile versionedFile) {
		setUser(user);
		addFile(versionedFile);
		this.createdDate = new Timestamp(new Date().getTime());
	}
	/**
	 * Get the Email ID
	 * 
	 * @return Email Id 
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set the Email Id
	 * 
	 * @param email Email to which invitation was sent
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get the user information
	 * 
	 * @return User object
	 */	
	public IrUser getUser() {
		return user;
	}

	/**
	 * Set the user
	 * 
	 * @param user user sending the email
	 */
	public void setUser(IrUser user) {
		this.user = user;
	}

	/**
	 * Get files that has to shared
	 * 
	 * @return Set of VersionedFiles
	 */
	public Set<VersionedFile> getFiles() {
		return files;
	}
	
	/**
	 * Set file information
	 * 
	 * @param files versioned file information
	 */
	public void setFiles(Set<VersionedFile> files) {
		this.files = files;
	}
	
	/**
	 * Get the token
	 * 
	 * @return token 
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Set the token
	 * 
	 * @param token token for user
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * Date the record was created.
	 * 
	 * @return - date the record was created.
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	

	/**
	 * Set the date created.
	 * 
	 * @param dateCreated
	 */
	void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof InviteInfo)) return false;

		final InviteInfo other = (InviteInfo) o;

		if( ( token != null && !token.equals(other.getToken()) ) ||
			( token == null && other.getToken() != null ) ) return false;

		if( ( email != null && !email.equals(other.getEmail()) ) ||
			( email == null && other.getEmail() != null ) ) return false;

		if( ( permissions != null && !permissions.equals(other.getPermissions()) ) ||
			( permissions == null && other.getPermissions() != null ) ) return false;
		
		return true;
	}


	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += token == null ? 0 : token.hashCode();
		value += email == null ? 0 : email.hashCode();
		value += inviteMessage == null ? 0 : inviteMessage.hashCode();
		value += permissions  == null ? 0 : permissions.hashCode();
		return value;
	}

	/**
	 * Get permissions on the shared file for the user
	 * 
	 * @return
	 */
	public Set<IrClassTypePermission> getPermissions() {
		return permissions;
	}

	/**
	 * Set permissions on the shared file for the user
	 * 
	 * @param permissions permission code separated by commas
	 */
	public void setPermissions(Set<IrClassTypePermission> permissions) {
		this.permissions = permissions;
	}
	
	/**
	 * Get invitation message
	 *  
	 * @return message in the invitation
	 */
	public String getInviteMessage() {
		return inviteMessage;
	}

	/**
	 * Set message for the invitation
	 * 
	 * @param inviteMessage message for the invitation
	 */
	public void setInviteMessage(String inviteMessage) {
		this.inviteMessage = inviteMessage;
	}

	/**
	 * Add a permission to the invite information
	 * 
	 * @param permission
	 */
	public void addPermission(IrClassTypePermission permission)
	{
		permissions.add(permission);
	}

	/**
	 * Add a file to the invite information
	 * 
	 * @param file
	 */
	public void addFile(VersionedFile file)
	{
		files.add(file);
	}

	/**
	 * Remove a file from the invitation.
	 * 
	 * @param file
	 * @return true if the file is removed.
	 */
	public boolean removeFile(VersionedFile file)
	{
		return files.remove(file);
	}
	
	/**
	 * To string of the invite info.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ invite info id = ");
		sb.append(id);
		sb.append("email = ");
		sb.append(email);
		sb.append(" token = ");
		sb.append(token);
		sb.append(" invite message = ");
		sb.append(inviteMessage);
		sb.append("]");
		return sb.toString();
	}


	
}
