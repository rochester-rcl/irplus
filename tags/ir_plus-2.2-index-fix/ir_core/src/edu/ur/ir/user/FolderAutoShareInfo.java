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
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.persistent.BasePersistent;

/**
 * Holds the permissions to auto share any file added to the folder
 * with a given user with the specified permissions.
 * 
 * @author Nathan Sarr
 *
 */
public class FolderAutoShareInfo extends BasePersistent{

	/** eclipse generated id */
	private static final long serialVersionUID = 6029900446681789453L;
	
	/* personal folder this is attached to */
	private PersonalFolder personalFolder;
	
	/* Permissions given to the collaborator */
	private Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
	
	/* user that is to collaborate on the files added to the folder */
	private IrUser collaborator;
	
	/* date the record was created */
	private Timestamp createdDate; 

	/**  Package protected auto share */
	FolderAutoShareInfo(){}
	
	/**
	 * Default constructor for auto share of folder information.
	 * 
	 * @param personalFolder - personal folder that has auto share permissions
	 * @param permissions - set of permissions to give the collaborator
	 * @param collaborator - 
	 */
	public FolderAutoShareInfo(PersonalFolder personalFolder, 
			Set<IrClassTypePermission> permissions, 
			IrUser collaborator)
	{
		setPersonalFolder(personalFolder);
		if( permissions != null )
		{
		  this.permissions.addAll(permissions);
		}
		setCollaborator(collaborator);
		createdDate = new Timestamp(new Date().getTime());
	}
	
	/**
	 * Personal folder that is to have auto share on.
	 * 
	 * @return personal folder to be auto shared
	 */
	public PersonalFolder getPersonalFolder() {
		return personalFolder;
	}

	/**
	 * Set the personal folder to be auto shared.
	 * 
	 * @param personalFolder
	 */
	void setPersonalFolder(PersonalFolder personalFolder) {
		this.personalFolder = personalFolder;
	}

	/**
	 * Permissions that should be assigned when a folder is shared.  This
	 * returns an unmodifiable set.
	 * 
	 * @return - permissions to be shared
	 */
	public Set<IrClassTypePermission> getPermissions() {
		return Collections.unmodifiableSet(permissions);
	}
	
	/**
	 * Remove the permission from the set of permissions.
	 * 
	 * @param permission - permission to be removed
	 * @return true if the permission is removed.
	 */
	public boolean removePermission(IrClassTypePermission permission)
	{
		return permissions.remove(permission);
	}
	
	/**
	 * Add permission to the set of permissions.
	 * 
	 * @param permission - permission to add.
	 */
	public void addPermission(IrClassTypePermission permission)
	{
		permissions.add(permission);
	}

	/**
	 * Set of permissions to be shared.
	 * 
	 * @param permissions
	 */
	void setPermissions(Set<IrClassTypePermission> permissions) 
	{
		this.permissions = permissions;
	}
	
	/**
	 * Change the permissions for the folder auto share.  This will remove
	 * all of the old permissions and set the new list of permissions to the set
	 * given.
	 * 
	 * @param newPermissions - new permissions to give the user
	 */
	public void changePermissions(Set<IrClassTypePermission> newPermissions)
	{
		Set<IrClassTypePermission> oldPermissions = new HashSet<IrClassTypePermission>();
		oldPermissions.addAll(getPermissions());
		for(IrClassTypePermission permission : oldPermissions)
		{
			removePermission(permission);
		}
		
		for(IrClassTypePermission p : newPermissions)
		{
			addPermission(p);
		}
	}

	/**
	 * Get the collaborator set to have the given permissions.
	 * 
	 * @return
	 */
	public IrUser getCollaborator() {
		return collaborator;
	}

	/**
	 * Set the collaborator to have the permissions.
	 * 
	 * @param collaborator
	 */
	void setCollaborator(IrUser collaborator) {
		this.collaborator = collaborator;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof  FolderAutoShareInfo)) return false;

		final  FolderAutoShareInfo other = (FolderAutoShareInfo) o;

		if( ( personalFolder != null && !personalFolder.equals(other.getPersonalFolder()) ) ||
			( personalFolder == null && other.getPersonalFolder() != null ) ) return false;

		if( ( collaborator != null && !collaborator.equals(other.getCollaborator()) ) ||
			( collaborator == null && other.getCollaborator() != null ) ) return false;
		
		return true;
	}
	
	/**
	 * Date the record was created.
	 * 
	 * @return - date record was created
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}


	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += collaborator == null ? 0 : collaborator.hashCode();
		value += personalFolder == null ? 0 : personalFolder.hashCode();
		return value;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ folder invite info id = ");
		sb.append(id);
		sb.append("collaborator = ");
		sb.append(collaborator);
		sb.append("personalFolder =  ");
		sb.append(personalFolder);
		sb.append("]");
		return sb.toString();
	}



}
