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

import java.util.HashSet;
import java.util.Set;

import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.persistent.BasePersistent;

/**
 * Allows any add/remove of a file to be automatically shared with a given
 * user
 * 
 * @author Nathan Sarr
 *
 */
public class PersonalFolderCollaborator extends BasePersistent{
	
	/* eclipse generated id */
	private static final long serialVersionUID = 1794578932083744349L;

	/*  Collaborator for the folder */
	private IrUser collaborator;
	
	/* personal folder to attach to the user */
	private PersonalFolder personalFolder;
	
	/* Permissions given to the collaborator */
	private Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();

	/**  Default personal folder collaborator. */
	PersonalFolderCollaborator(){}
	
	/**
	 * Create a personal folder 
	 * 
	 * @param personalFolder
	 * @param collaborator
	 * @param permissions
	 */
	public PersonalFolderCollaborator(PersonalFolder personalFolder, IrUser collaborator, Set<IrClassTypePermission> permissions )
	{
		this.personalFolder = personalFolder;
		this.collaborator = collaborator;
		this.permissions = permissions;
	}
	
	/**
	 * Get collaborator that files should be shared with.
	 * 
	 * @return
	 */
	public IrUser getCollaborator() {
		return collaborator;
	}

	/**
	 * Personal file that collaborators can be shared with.
	 * 
	 * @return
	 */
	public PersonalFolder getPersonalFolder() {
		return personalFolder;
	}
	
	/**
	 * Set the permissions that should be assigned to the user.
	 * 
	 * @param permissions
	 */
	public void setPermissions(Set<IrClassTypePermission> permissions) {
		this.permissions = permissions;
	}
	
	/**
	 * Permissions to give the user.
	 * 
	 * @return - permissions to give the user when a file is added to the folder.
	 */
	public Set<IrClassTypePermission> getPermissions() {
		return permissions;
	}
	
	/**
     * Get the hash code.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
    	int hash = 0;
    	hash += personalFolder == null ? 0 : personalFolder.hashCode();
    	hash += collaborator == null ? 0 : collaborator.hashCode();
    	return hash;
    }
    
    /**
     * File Collaborator Equals.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
    	if (this == o) return true;
		if (!(o instanceof PersonalFolderCollaborator)) return false;

		final PersonalFolderCollaborator other = (PersonalFolderCollaborator) o;

		if( ( collaborator != null && !collaborator.equals(other.getCollaborator()) ) ||
			( collaborator == null && other.getCollaborator() != null ) ) return false;
		
		if( ( personalFolder != null && !personalFolder.equals(other.getPersonalFolder())) ||
			( personalFolder == null && other.getPersonalFolder() != null ) ) return false;
		
		return true;
    }
	
	/**
	 * To string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("[ Id = " );
		sb.append(id);
		sb.append(" IrUser = ");
		sb.append(collaborator);
		sb.append(" PersonalFolder = ");
		sb.append(personalFolder);
		sb.append("]");
		return sb.toString();
	}


}
