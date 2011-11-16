/**  
   Copyright 2008 - 2011 University of Rochester

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


package edu.ur.ir.web.action.groupspace;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Unlock the versioned file.
 * 
 * @author Nathan Sarr
 *
 */
public class UnLockVersionedFile extends ActionSupport implements UserIdAware {
	
	/* eclipse generated id */
	private static final long serialVersionUID = 3706024762678185133L;
	
	public static final String UN_LOCK_NOT_ALLOWED = "UN_LOCK_NOT_ALLOWED";
	public static final String UN_LOCKED_BY_USER = "UN_LOCKED_BY_USER";
	
	/* id of the user attempting access */
	private Long userId;

	/*  Logger for add user action */
	private static final Logger log = Logger.getLogger(UnLockVersionedFile.class);
	
	/* Service for accessing or deleting files */
	private RepositoryService repositoryService;
	
	/* group workspace file system service */
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;

	/* Id of the versioned file to be locked */
	private Long groupWorkspaceFileId;

	/* Indicates the lock has been obtained for the user */
	private boolean lockObtained;
	
	/* Status to help figure out what happened.  */
	private String returnStatus = UN_LOCKED_BY_USER;
	
	/* the user who has the file locked if any  */
	private IrUser lockedBy;
	
	/* service to deal with user information */
    private UserService userService;

	

	/**
	 * Lock the file 
	 * 
	 * @return
	 */
	public String execute()
	{
		log.debug("unlock file called");
		
        GroupWorkspaceFile groupWorkspaceFile = groupWorkspaceFileSystemService.getFile(groupWorkspaceFileId, false);
		
		IrUser user = userService.getUser(userId, false);
		VersionedFile versionedFile =  groupWorkspaceFile.getVersionedFile();
		
		// only attempt to unlock the file if it is locked
		if( versionedFile.isLocked())
		{
		    if( repositoryService.canUnlockFile(versionedFile, user) )
		    {
		        boolean unlocked = repositoryService.unlockVersionedFile(versionedFile, user);
		        if(!unlocked)
		        {
		    	    throw new IllegalStateException("User : " + user 
		    				+ " could not un lock Versioned File :" + versionedFile );
		        }
		    }
		    else
			{
				lockObtained = false;
				returnStatus = UN_LOCK_NOT_ALLOWED;
			}
		}
		
		return SUCCESS;
	}

	/**
	 * Repository service to lock and retrieve files
	 * 
	 * @return
	 */
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	/**
	 * Repository service to lock and retrieve files.
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**
	 * True if the lock is obtained.
	 * 
	 * @return
	 */
	public boolean isLockObtained() {
		return lockObtained;
	}

	/**
	 * Set the lock as obtained.
	 * 
	 * @param lockObtained
	 */
	public void setLockObtained(boolean lockObtained) {
		this.lockObtained = lockObtained;
	}

	/**
	 * Get the user who has locked the file.
	 * 
	 * @return
	 */
	public IrUser getLockedBy() {
		return lockedBy;
	}

	/**
	 * Set the user who locked the file.
	 * 
	 * @param lockedBy
	 */
	public void setLockedBy(IrUser lockedBy) {
		this.lockedBy = lockedBy;
	}

	/**
	 * Status of the file - locked / unlocked.
	 * 
	 * @return
	 */
	public String getReturnStatus() {
		return returnStatus;
	}

	/**
	 * Set the return status of the file.
	 * 
	 * @param returnStatus
	 */
	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	/**
	 * User tying to access the data
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Id of the group workspace file id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceFileId() {
		return groupWorkspaceFileId;
	}

	/**
	 * Id of the group workspace file id.
	 * 
	 * @param groupWorkspaceFileId
	 */
	public void setGroupWorkspaceFileId(Long groupWorkspaceFileId) {
		this.groupWorkspaceFileId = groupWorkspaceFileId;
	}
	
	/**
	 * Set the group workspace file system service.
	 * 
	 * @param groupWorkspaceFileSystemService
	 */
	public void setGroupWorkspaceFileSystemService(
			GroupWorkspaceFileSystemService groupWorkspaceFileSystemService) {
		this.groupWorkspaceFileSystemService = groupWorkspaceFileSystemService;
	}
	
	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


}
