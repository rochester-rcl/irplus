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


package edu.ur.ir.web.action.user;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * This action is for locking a versioned file.
 * 
 * @author Nathan Sarr
 *
 */
public class LockVersionedFile extends ActionSupport implements UserIdAware {
	
	public static final String LOCK_OBTAINED = "LOCK_OBTAINED";
	public static final String LOCK_NOT_ALLOWED = "LOCK_NOT_ALLOWED";
	public static final String LOCK_BY_USER = "LOCKED_BY_USER";
	
	/** user id accessing the inforamtion */
	private Long userId;
	
	/** Eclipse generated id. */
	private static final long serialVersionUID = -4846716439383595220L;

	/**  Logger for add user action */
	private static final Logger log = Logger.getLogger(LockVersionedFile.class);
	
	/** Service for accessing or deleting files */
	private RepositoryService repositoryService;
		
	/** User file system service */
	private UserFileSystemService userFileSystemService;
	
	/** Id of the versioned file to be locked */
	private Long personalFileId;
	
	/** Indicates the lock has been obtained for the user */
	private boolean lockObtained;
	
	/** Status to help figure out what happened.  */
	private String returnStatus = LOCK_OBTAINED;
	
	/** the user who has the file locked if any  */
	private IrUser lockedBy;
	
	/**
	 * Lock the file 
	 * 
	 * @return
	 */
	public String execute()
	{
		log.debug("lock file called");
		
		PersonalFile personalFile = userFileSystemService.getPersonalFile(personalFileId, false);
		IrUser user = personalFile.getOwner();
		
		// verify access
		if( !user.getId().equals(userId))
		{
			return "accessDenied";
		}
		
		
		
		VersionedFile versionedFile =  personalFile.getVersionedFile();
		
		if( repositoryService.canLockVersionedFile(versionedFile, user))
		{
		    lockObtained = repositoryService.lockVersionedFile(versionedFile, user);
		    
		    if(!lockObtained)
		    {
		    	lockedBy = versionedFile.getLockedBy();
		    	
		    	if(lockedBy == null )
		    	{
		    		throw new IllegalStateException("User : " + user 
		    				+ " could not lock Versioned File :" + versionedFile 
		    				+ " but no user holds the lock ");
		    	}
		    	returnStatus = LOCK_BY_USER;
		    }
		}
		else
		{
			lockObtained = false;
			returnStatus = LOCK_NOT_ALLOWED;
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
	 * Get the personal file id to lock.
	 * 
	 * @return
	 */
	public Long getPersonalFileId() {
		return personalFileId;
	}

	/**
	 * Set the personal file id to lock.
	 * 
	 * @param versionedFileId
	 */
	public void setPersonalFileId(Long personalFileId) {
		this.personalFileId = personalFileId;
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

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

	
	public void injectUserId(Long userId) {
		this.userId = userId;
	}

}
