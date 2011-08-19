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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.invite.InviteToken;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.persistent.BasePersistent;

/**
 * Invite information
 * 
 * @author Sharmila Ranganathan
 *
 */
public class FileInviteInfo extends BasePersistent {

	/* Eclipse generated Id	 */
	private static final long serialVersionUID = 6007214729437637359L;

	/* Versioned Files that has to be shared */
	private Set<VersionedFile> files = new HashSet<VersionedFile>();
	
	/* Permissions given to the user */
	private Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
	
    /* token for the invite  */
    private InviteToken inviteToken;
    
 	/**
	 * Default Constructor
	 */
	FileInviteInfo() {}
	
	/**
	 *  Constructor 
	 */
	public FileInviteInfo(Set<VersionedFile> versionedFile, Set<IrClassTypePermission> permissions, InviteToken inviteToken) {
		setFiles(versionedFile);
		setPermissions(permissions);
		this.setInviteToken(inviteToken);
	}


	/**
	 * Get files that has to shared
	 * 
	 * @return Set of VersionedFiles
	 */
	public Set<VersionedFile> getFiles() {
		return Collections.unmodifiableSet(files);
	}
	
	/**
	 * Set file information
	 * 
	 * @param files versioned file information
	 */
	void setFiles(Set<VersionedFile> files) {
		this.files = files;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof FileInviteInfo)) return false;

		final FileInviteInfo other = (FileInviteInfo) o;

		if( ( inviteToken != null && !inviteToken.equals(other.getInviteToken()) ) ||
			( inviteToken == null && other.getInviteToken() != null ) ) return false;
		return true;
	}

	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += inviteToken == null ? 0 : inviteToken.hashCode();
		return value;
	}

	/**
	 * Get permissions on the shared file for the user
	 * 
	 * @return an unmodifiable set
	 */
	public Set<IrClassTypePermission> getPermissions() {
		return Collections.unmodifiableSet(permissions);
	}

	/**
	 * Set permissions on the shared file(s) for the user
	 * 
	 * @param permissions permission code separated by commas
	 */
	void setPermissions(Set<IrClassTypePermission> permissions) {
		this.permissions = permissions;
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
	 * Remove the permission from the set of permissions on the file(s).
	 * 
	 * @param permission - permission to be removed.
	 * @return true if the permission is removed otherwise false.
	 */
	public boolean removePermission(IrClassTypePermission permission)
	{
		return permissions.remove(permission);
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
	 * Get the invite token.
	 * 
	 * @return the invite token
	 */ 
	public InviteToken getInviteToken() {
		return inviteToken;
	}
	
	/**
	 * Set the invite token.
	 * 
	 * @param inviteToken
	 */
	void setInviteToken(InviteToken inviteToken)
	{
		this.inviteToken = inviteToken;
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
		sb.append("token = ");
		sb.append(inviteToken);
		sb.append("]");
		return sb.toString();
	}


	
}
