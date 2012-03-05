/**  
   Copyright 2008-2011 University of Rochester

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

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.invite.InviteToken;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;


/**
 * Represents a space for a group of people to collaborate on a shared folder structure
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspace extends BasePersistent implements NameAware, DescriptionAware {
	
	/* eclipse generated id */
	private static final long serialVersionUID = -6440204761384913224L;

	/*  Name of the group space */
	private String name;
	
	/* lower case value of the name */
	private String lowerCaseName;

	/* Description of the group space */
	private String description;
	
	/*  Root Folder for this person. */
	private Set<GroupWorkspaceFolder> rootFolders = new HashSet<GroupWorkspaceFolder>();
	
	/*  Root files for this person.  */
	private Set<GroupWorkspaceFile> rootFiles = new HashSet<GroupWorkspaceFile>();
	
	/* list of users this group space */
	private Set<GroupWorkspaceUser> users = new HashSet<GroupWorkspaceUser>();
	
	/* users who are not yet registered or have an email that is not yet registered */
	private Set<GroupWorkspaceEmailInvite> emailInvites = new HashSet<GroupWorkspaceEmailInvite>();
	
	/* project page for the group workspace */
	private GroupWorkspaceProjectPage groupWorkspaceProjectPage;

	/* date this record was created */
	private Timestamp createdDate;

	/**  User will be assigned read all files and folders within the workspace  */
	public static String GROUP_WORKSPACE_READ_PERMISSION = "GROUP_WORKSPACE_READ";
	
	/**  Users will be assigned file add to all folders in the workspace */
	public static String GROUP_WORKSPACE_ADD_FILE_PERMISSION = "GROUP_WORKSPACE_ADD_FILE";
	
	/**  Users will be assigned add/edit/delete to all files and folders within the workspace  */
	public static String GROUP_WORKSPACE_EDIT_PERMISSION = "GROUP_WORKSPACE_EDIT";

	/**  Package protected workspace  */
	GroupWorkspace(){}
	
    /**
     * Create a group space with the given name.
     * 
     * @param name - name of the group space
     * @param owner - owner of the group space
     * @throws IllegalFileSystemNameException 
     */
    public GroupWorkspace(String name) 
    {
    	
    	setName(name);
    	groupWorkspaceProjectPage = new GroupWorkspaceProjectPage(this);
    	createdDate = new Timestamp(new Date().getTime());
    }
    
    /**
     * Create a group space with the given name.
     * 
     * @param name - name of the group space
     * @param owner - owner of the group space
     * @param description - description of the group space
     * 
     * @throws IllegalFileSystemNameException 
     */
    public GroupWorkspace(String name, String description) 
    {
    	this(name);
    	setDescription(description);
    }
    
	/**
	 * Get the name of the workspace
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the group.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		if( name == null || name.trim().equals(""))
    	{
    		throw new IllegalStateException("Group workspace name cannot be null");
    	}
		this.name = name.trim();
		lowerCaseName = this.name.toLowerCase();
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
		value += lowerCaseName == null ? 0 : lowerCaseName.hashCode();
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
		if (!(o instanceof GroupWorkspace)) return false;

		final GroupWorkspace other = (GroupWorkspace) o;

		if( ( lowerCaseName != null && !lowerCaseName.equalsIgnoreCase(other.getLowerCaseName()) ) ||
			( lowerCaseName == null && other.getLowerCaseName() != null ) ) return false;

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

	/**
	 * Set the description of the group space.
	 * 
	 * @see edu.ur.simple.type.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set the description of the group space.
	 * 
	 * @param description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * Get the date the group was created.
	 * 
	 * @return - date the record was created.
	 */ 
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	
	/**
	 * Lower case name value
	 * 
	 * @return lower case value of the name
	 */
	public String getLowerCaseName() {
		return lowerCaseName;
	}
	
	/**
	 * Remove a folder from this set of root folders.
	 * 
	 * @param folder
	 * @return true if the folder is removed.
	 * 
	 */
	public boolean removeRootFolder(GroupWorkspaceFolder rootFolder)
	{
		return rootFolders.remove(rootFolder);
	}
	
	/**
	 * Remove the group file from this group.
	 * 
	 * @param groupFile
	 * @return true if the file is removed.
	 */
	public boolean removeRootFile(GroupWorkspaceFile groupFile)
	{
		return rootFiles.remove(groupFile);
	}
	
	/**
	 * Creates the root folder by name if it does not exist.  The 
	 * name should not be null or the name of an existing root
	 * folder for this group.
	 * 
	 * @param name of root folder to create.
	 * @return Created Folder if it does not already exist
	 * @throws DuplicateNameException 
	 * 
	 * @throws IllegalArgumentException if the name of the folder 
	 * already exists or the name is null.
	 */
	public GroupWorkspaceFolder createRootFolder(IrUser owner, String name) throws DuplicateNameException, IllegalFileSystemNameException
	{
		if( name == null)
		{
			throw new IllegalArgumentException("Name cannot be null");
		}
		
		if( !isVaildPersonalFileSystemName(name))
		{
			throw new DuplicateNameException("A file or folder with name " + name +
			" already exists ", name );
        }
		
		GroupWorkspaceFolder f = new GroupWorkspaceFolder(this, owner, name);
		rootFolders.add(f);
		return f;
	}
	
	/**
	 * Adds an existing folder to the root of this group.
	 * If the folder was a child of an existing folder.  It
	 * is removed from its parents list.
	 * 
	 * @param folder - to add as a root
	 * @throws DuplicateNameException 
	 */
	public void addRootFolder(GroupWorkspaceFolder folder) throws DuplicateNameException
	{
		if( !isVaildPersonalFileSystemName(folder.getName()))
        {
        	throw new DuplicateNameException("A file or folder with name " + folder.getName() +
        			" already exists in this folder", folder.getName());
        }
		
		GroupWorkspaceFolder parent = folder.getParent();
		if(parent != null)
		{
		    parent.removeChild(folder);
		}
		rootFolders.add(folder);
	}
	
	/**
	 * Set of root folders for this group.
	 * 
	 * @return Unmodifiable set of root folders
	 */
	public Set<GroupWorkspaceFolder> getRootFolders() {
		return Collections.unmodifiableSet(rootFolders);
	}

	/**
	 * The set of root folders owned by this group.
	 * 
	 * @param rootFolders
	 */
	void setFolders(Set<GroupWorkspaceFolder> rootFolders) {
		this.rootFolders = rootFolders;
	}
	
	/**
	 * Get a root folder by name.  The comparison
	 * is case insensitive.
	 * 
	 * @param name - name of the folder to return
	 * @return The folder if found otherwise null.
	 */
	public GroupWorkspaceFolder getRootFolder(String name)
	{
		for(GroupWorkspaceFolder f: rootFolders )
		{
			if( f.getName().equalsIgnoreCase(name))
			{
				return f;
			}
		}
		return null;
	}
	
		
	/**
	 * Get a group file by name.
	 * 
	 * @param name
	 * @return the found file
	 */
	public GroupWorkspaceFile getRootFile(String nameWithExtension)
	{
		for(GroupWorkspaceFile pvf: rootFiles )
		{
			if( pvf.getVersionedFile().getNameWithExtension().equalsIgnoreCase(nameWithExtension))
			{
				return pvf;
			}
		}
		return null;
	}
	
	/**
	 * Adds an existing file to the root of this group.
	 * If the file was a child of an existing folder.  It
	 * is removed from the old folders file list.
	 * 
	 * @param file - to add as a root file
	 */
	public void addRootFile(GroupWorkspaceFile file) throws DuplicateNameException
	{
		if( !isVaildPersonalFileSystemName(file.getVersionedFile().getNameWithExtension()))
        {
        	throw new DuplicateNameException("A file or folder with name " + file.getName() +
        			" already exists in this folder", file.getName());
        }
		
		GroupWorkspaceFolder folder = file.getGroupWorkspaceFolder();
		if( folder != null )
		{
			folder.removeGroupFile(file);
			file.setGroupWorkspaceFolder(null);
			
			GroupWorkspace current = folder.getGroupWorkspace();
			if(current != null && !current.equals(this))
			{
				current.removeRootFile(file);
			}
		}
		
		rootFiles.add(file);
		
	}
	
	/**
	 * Create a root group file.  
	 * 
	 * @param group file to add to the root.	
	 * @return created file
	 * 
	 * @throws DuplicateNameException - if the file name already exists as a root file
	 */
	public GroupWorkspaceFile createRootFile(VersionedFile vf)throws DuplicateNameException
	{
        if( !isVaildPersonalFileSystemName(vf.getNameWithExtension()) )
        {
        	throw new DuplicateNameException("A file or folder with name " + vf.getName() +
        			" already exists in this folder", vf.getName());
        }
        
        
		GroupWorkspaceFile pvf = new GroupWorkspaceFile(vf, this);
		rootFiles.add(pvf);
		return pvf;
		
	}

	/**
	 * Returns an unmodifiable set of root folders.
	 * 
	 * @return
	 */
	public Set<GroupWorkspaceFile> getRootFiles() {
		return Collections.unmodifiableSet(rootFiles);
	}

	/**
	 * Set the root files for this group.
	 * 
	 * @param rootFiles
	 */
	void setRootFiles(Set<GroupWorkspaceFile> rootFiles) {
		this.rootFiles = rootFiles;
	}
	
	/**
	 * Get root file by name.
	 * 
	 * @param name - name of the file to return
	 * @return The file if found otherwise null.
	 */
	public GroupWorkspaceFile getRootGroupFile(String name)
	{
		for(GroupWorkspaceFile gf: rootFiles )
		{
			if( gf.getName().equals(name))
			{
				return gf;
			}
		}
		return null;
	}
	
	/**
	 * Get root file by id.
	 * 
	 * @param id - id of the file to return
	 * @return The file if found otherwise null.
	 */
	public GroupWorkspaceFile getRootGroupFile(Long id)
	{
		for(GroupWorkspaceFile gf: rootFiles )
		{
			if( gf.getId().equals(id))
			{
				return gf;
			}
		}
		return null;
	}

	
	/**
	 * Returns true if the name is ok to add to a file or folder
	 * 
	 * @param name of the file or folder.  If it is a file, it should contain
	 * the extension.
	 * 
	 * @return true if the name does not exist.  This is case insensitive.
	 */
	private boolean isVaildPersonalFileSystemName(String name)
	{
		boolean ok = false;
		if( getRootFolder(name) == null && getRootFile(name) == null)
		{
			ok = true;
		}
		return ok;
	}
	
	/**
	 * Get a workspace user by user.
	 * 
	 * @param name - name of the group
	 * @return group if found otherwise null
	 */
	public GroupWorkspaceUser getUser(IrUser user)
	{
		if(user != null )
		{	
	        for(GroupWorkspaceUser workspaceUser : users)
	        {
	    	    if( workspaceUser.getUser().equals(user))
	    	    {
	    		    return workspaceUser;
	    	    }
	        }
		}
	    return null;
	}
	
	/**
	 * Get a workspace user by id.
	 * 
	 * @param name - name of the group
	 * @return group if found otherwise null
	 */
	public GroupWorkspaceUser getUser(Long id)
	{
		if(id != null )
		{	
	        for(GroupWorkspaceUser workspaceUser : users)
	        {
	    	    if( workspaceUser.getId().equals(id))
	    	    {
	    		    return workspaceUser;
	    	    }
	        }
		}
	    return null;
	}
	
	/**
	 * Remove the group from the set of groups.
	 * 
	 * @param group - group to remove
	 * @return - true if the group is removed
	 */
	public boolean remove(GroupWorkspaceUser groupWorkspaceUser)
	{
		GroupWorkspaceProjectPageMember member = groupWorkspaceProjectPage.getMember(groupWorkspaceUser);
		if( member != null )
		{
		    groupWorkspaceProjectPage.removeMember(member);
		}
		return users.remove(groupWorkspaceUser);
	}
	
	/**
	 * Add a user to the workspace.
	 * 
	 * @param name - name to create the group with
	 * @param description - description of the group
	 * @return - created group
	 * @throws DuplicateNameException - if a group with the given name exists regardless of case 
	 */
	public GroupWorkspaceUser add(IrUser user, boolean setAsOwner) throws DuplicateNameException
	{
		GroupWorkspaceUser workspaceUser = getUser(user);
		if( workspaceUser == null )
		{
			workspaceUser = new GroupWorkspaceUser(this, user, setAsOwner);
			users.add(workspaceUser);
		}
		return workspaceUser;

	}
	
	/**
	 * Get the users for this group space.  This returns an
	 * unmodifiable set.
	 * 
	 * @return list of users
	 */
	public Set<GroupWorkspaceUser> getUsers() {
		return Collections.unmodifiableSet(users);
	}

	/**
	 * Set the groups for this space.
	 * 
	 * @param groups
	 */
	void setUsers(Set<GroupWorkspaceUser> users) {
		this.users = users;
	}
	
	/**
	 * Get the invited user by email - ignores case.  Retun the invite if found
	 * otherwise return null.
	 * 
	 * @param email
	 * @return invite if found otherwise null.
	 */
	public GroupWorkspaceEmailInvite getInvite(String email)
	{
		for(GroupWorkspaceEmailInvite invite : emailInvites)
		{
			if( invite.getInviteToken().getEmail().equalsIgnoreCase(email))
			{
				return invite;
			}
		}
		return null;
	}
	

	
	/**
	 * Returns the user if they are a member of the users othwerwise returns null.
	 * 
	 * @param email - email of the user to look for
	 * @return user if found otherwise null
	 */
	public GroupWorkspaceUser getUser(String email)
	{
		for(GroupWorkspaceUser workspaceUser : users)
		{
			if( workspaceUser.getUser().getUserEmail(email) != null)
			{
				return workspaceUser;
			}
		}
		return null;
	}
	
	/**
	 * Create an invite for a user who does not yet exist in the system
	 * 
	 * @param email - email 
	 * @param permissions - permissions to be given
	 * @param inviteingUser - user doing the invite
	 * @param token - token for the invite
	 * 
	 * @return - the created invite
	 * @throws GroupWorkspaceInviteException 
	 */
	public GroupWorkspaceEmailInvite addInviteUser(String email, 
			Set<IrClassTypePermission> permissions,
			IrUser invitingUser, 
			String token ) throws GroupWorkspaceInviteException
	{
		
		if( getUser(email) != null )
		{
			throw new GroupWorkspaceInviteException("user already exists");
		}
		GroupWorkspaceEmailInvite invite = getInvite(email);
		
		if(  invite == null )
		{
			InviteToken inviteToken = new InviteToken(email, token, invitingUser);
			invite = new GroupWorkspaceEmailInvite(this, permissions, inviteToken); 
			emailInvites.add(invite);
		}
		return invite;
	}
	
	/**
	 * Delete the invite if found.  If the invite is not found
	 * this returns true as it is not part of the set.
	 * 
	 * @param email - remove the invite based on email - ignores case
	 * @return true if the invite is deleted.
	 */
	public boolean deleteEmailInvite(String email)
	{
		GroupWorkspaceEmailInvite invite = getInvite(email);
		if( invite != null )
		{
			return emailInvites.remove(invite);
		}
		return true;
	}

	
	/**
	 * Get email invited users - users who have not yet created an account or
	 * do not have an email registered.
	 * 
	 * @return
	 */
	public Set<GroupWorkspaceEmailInvite> getEmailInvites() {
		return Collections.unmodifiableSet(emailInvites);
	}

	/**
	 * Set the email invited users
	 * @param emailInvitedUsers
	 */
	void setEmailInvites(
			Set<GroupWorkspaceEmailInvite> emailInvitedUsers) {
		this.emailInvites = emailInvitedUsers;
	}
	
	/**
	 * Get the group workspace project page.
	 * 
	 * @return
	 */
	public GroupWorkspaceProjectPage getGroupWorkspaceProjectPage() {
		return groupWorkspaceProjectPage;
	}

	/**
	 * Set the group workspace project page.
	 * 
	 * @param groupWorkspaceProjectPage
	 */
	void setGroupWorkspaceProjectPage(
			GroupWorkspaceProjectPage groupWorkspaceProjectPage) {
		this.groupWorkspaceProjectPage = groupWorkspaceProjectPage;
	}


}
