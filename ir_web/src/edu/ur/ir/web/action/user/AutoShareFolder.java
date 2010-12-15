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

package edu.ur.ir.web.action.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.FileSharingException;
import edu.ur.ir.user.FolderAutoShareInfo;
import edu.ur.ir.user.FolderInviteInfo;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action to help managing sharing a folder.
 * 
 * @author Nathan Sarr
 *
 */
public class AutoShareFolder extends ActionSupport implements Preparable, UserIdAware{

	/** eclipse generated id. */
	private static final long serialVersionUID = -2498774770801545420L;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger( AutoShareFolder.class);

	
	/* id of the personal folder */
	private Long personalFolderId;
	
	/* personal folder */
	private PersonalFolder personalFolder;

	/* service for dealing with user file system information. */
	private UserFileSystemService userFileSystemService;
	
	/* id of the user making the request. */
	private Long userId;
	
	/* Permission types for the files within folders */
	private List<IrClassTypePermission> classTypePermissions;
	
	/* Service for dealing with security information */
	private SecurityService securityService;
	
	/*  Semicolon separated emails to invite users */
	private String emails;
	
    /* Service to deal with user information */
    private UserService userService;

	/* Message that can be displayed to the user. */
	private String inviteErrorMessage;

	/* invite user service. */
	private InviteUserService inviteUserService;
	
	/* determine if the permissions should cascade down to sub folders */
	private boolean includeSubFolders = false;

	/* Permissions to be assigned while sharing the file */
	private List<Long> selectedPermissions = new ArrayList<Long>();
	
	/* id of the auto share info to be removed - THIS doubles as the invite id*/
	private Long folderAutoShareInfoId;
	

	/* (non-Javadoc)
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;	
	}
	
	/**
	 * Load the personal folder information.
	 * 
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	public void prepare() {
		if( personalFolderId != null )
		{
			personalFolder = userFileSystemService.getPersonalFolder(personalFolderId, false);
		}
		classTypePermissions = this.orderPermissionsList(securityService.getClassTypePermissions(VersionedFile.class.getName()));

	}
	
	/**
	 * Initialize the invite user page with permissions
	 */
	public String execute() {
        if( !personalFolder.getOwner().getId().equals(userId))
        {
        	return "accessDenied";
        }
	    return SUCCESS;
	}
	
	/**
	 * Delete the auto share information.
	 * 
	 * @return
	 */
	public String deleteAutoShare()
	{
		FolderAutoShareInfo folderAutoShareInfo = inviteUserService.getFolderAutoShareInfoById(folderAutoShareInfoId, false);
		
		if( folderAutoShareInfo != null )
		{
			personalFolder = folderAutoShareInfo.getPersonalFolder();
			if(!personalFolder.getOwner().getId().equals(userId))
			{
				return "accessDenied";
			}
			else
			{
				
				if( includeSubFolders )
				{
					List<PersonalFolder> folders = userFileSystemService.getAllChildrenForFolder(personalFolder);
					for( PersonalFolder aFolder : folders)
					{
					    FolderAutoShareInfo info = aFolder.getAutoShareInfo(folderAutoShareInfo.getCollaborator());
					    if( info != null )
					    {
					    	aFolder.removeAutoShareInfo(info);
					    	inviteUserService.delete(info);
					    }
					}
				}
				personalFolder.removeAutoShareInfo(folderAutoShareInfo);
				inviteUserService.delete(folderAutoShareInfo);
			}
			
		}
		return SUCCESS;
	}
	
	/**
	 * Delete the auto share invite information. [For a user who
	 * does not yet exist in the system]
	 * 
	 * @return
	 */
	public String deleteFolderInvite()
	{
		FolderInviteInfo folderInviteInfo = inviteUserService.getFolderInviteInfoById(folderAutoShareInfoId, false);
		if( log.isDebugEnabled() )
		{
			log.debug(" invite info = " + folderInviteInfo);	
			log.debug(" include sub folders = " + includeSubFolders);
		}
		
		if( folderInviteInfo != null )
		{
			personalFolder = folderInviteInfo.getPersonalFolder();
			if(!folderInviteInfo.getPersonalFolder().getOwner().getId().equals(userId))
			{
				return "accessDenied";
			}
			else
			{
				
				if( includeSubFolders )
				{
					List<PersonalFolder> folders = userFileSystemService.getAllChildrenForFolder(personalFolder);
					for( PersonalFolder aFolder : folders)
					{
					    FolderInviteInfo info = aFolder.getFolderInviteInfo(folderInviteInfo.getEmail());
					    if( info != null )
					    {
					    	aFolder.removeFolderInviteInfo(info);
					    	inviteUserService.delete(info);
					    }
					}
				}
				personalFolder.removeFolderInviteInfo(folderInviteInfo);
				inviteUserService.delete(folderInviteInfo);
			}
			
		}
		return SUCCESS;
	}
	
	/**
	 * Auto share the folder with the selected users.
	 * 
	 * @return
	 */
	public String autoShareFolder()
	{
		log.debug("auto share folder");
		
		if (selectedPermissions.size() == 0) {
			inviteErrorMessage = getText("emptyPermissions");
			return "added";
		}
		
		// Create the list of permissions
		Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		for(Long id : selectedPermissions)
		{
			permissions.add(securityService.getIrClassTypePermissionById(id, false));
		}			
		
		boolean errorSet = false;
		
		emails = emails.trim();
		String[] splitEmails = emails.split(";");
		
		LinkedList<String> emailsToInvite = new LinkedList<String>();
		for(String email : splitEmails)
		{
			email = email.trim();
			// Check if user exist in the system
			IrUser invitedUser = userService.getUserForVerifiedEmail(email);
			//check if the user is sharing the file with themselves - remove their email if they are
			if (personalFolder.getOwner().equals(invitedUser)) 
			{
		        if( !errorSet )
			    {
				    inviteErrorMessage = getText("sharingWithYourself") + " ";
					errorSet = true;
				}
			}	
			else 
			{
				emailsToInvite.add(email);
			}
		}
		
		try 
		{
			inviteUserService.autoShareFolder(emailsToInvite, personalFolder, permissions,  includeSubFolders);
		} 
		catch (FileSharingException e) {
			// this should not happen
			if( !errorSet )
		    {
			    inviteErrorMessage = getText("sharingWithYourself") + " ";
				errorSet = true;
			}
		}
		
		// reload the personal folder
		personalFolder = userFileSystemService.getPersonalFolder(personalFolderId, false);
		
		log.debug("Personal Folder  auto share info = " + personalFolder.getAutoShareInfos().size() + " invite infos =  "
				+ personalFolder.getFolderInviteInfos().size());
		return SUCCESS;
	}

	/**
	 * Get the personal folder id
	 * 
	 * @return
	 */
	public Long getPersonalFolderId() {
		return personalFolderId;
	}

	/**
	 * Set the personal folder id.
	 * 
	 * @param personalFolderId
	 */
	public void setPersonalFolderId(Long personalFolderId) {
		this.personalFolderId = personalFolderId;
	}

	/**
	 * Set the user file system service.
	 * 
	 * @param userFileSystemService
	 */
	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}
	
	/**
	 * Get the personal folder.
	 * 
	 * @return - personal folder
	 */
	public PersonalFolder getPersonalFolder() {
		return personalFolder;
	}
	
	/**
	 * Get the list of class type permissions.
	 * 
	 * @return - list of class type permissions
	 */
	public List<IrClassTypePermission> getClassTypePermissions() {
		return classTypePermissions;
	}

	/**
	 * Set the security service
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	/**
	 * Order the permissions in a perdictable manner.
	 * 
	 * @param permissionsToOrder
	 * @return
	 */
	private List<IrClassTypePermission> orderPermissionsList(List<IrClassTypePermission> permissionsToOrder)
	{
		List<IrClassTypePermission> orderedPermissions = new LinkedList<IrClassTypePermission>();
		IrClassTypePermission view = null;
		IrClassTypePermission edit = null;
		IrClassTypePermission share = null;
		
		for(IrClassTypePermission permission : permissionsToOrder)
		{
			if( permission.getName().equals("VIEW"))
			{
				view = permission;
			}
			else if( permission.getName().equals("EDIT"))
			{
				edit = permission;
			}
			else if( permission.getName().equals("SHARE"))
			{
				 share = permission;
			}
		}
		
		if( view != null )
		{
			orderedPermissions.add(view);
		}
		if( edit != null )
		{
			orderedPermissions.add(edit);
		}
		if( share != null )
		{
			orderedPermissions.add(share);
		}
		
		return orderedPermissions;
	}
	
	/**
	 * Get the emails 
	 * 
	 * @return
	 */
	public String getEmails() {
		return emails;
	}

	/**
	 * Set the emails.
	 * 
	 * @param emails
	 */
	public void setEmails(String emails) {
		this.emails = emails;
	}
	
	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Set the invite user service.
	 * 
	 * @param inviteUserService
	 */
	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}
	
	/**
	 * Set the include sub folders 
	 * 
	 * @param includeSubFolders
	 */
	public void setIncludeSubFolders(boolean includeSubFolders) {
		this.includeSubFolders = includeSubFolders;
	}
	
	/**
	 * Set the selected permissions for the user.
	 * 
	 * @param selectedPermissions
	 */
	public void setSelectedPermissions(List<Long> selectedPermissions) {
		this.selectedPermissions = selectedPermissions;
	}
	
	/**
	 * Get the invite error message
	 * 
	 * @return - invite error message
	 */
	public String getInviteErrorMessage() {
		return inviteErrorMessage;
	}
	

	/**
	 * Set the auto share info id.
	 * 
	 * @param folderAutoShareInfoId
	 */
	public void setFolderAutoShareInfoId(Long folderAutoShareInfoId) {
		this.folderAutoShareInfoId = folderAutoShareInfoId;
	}

}
