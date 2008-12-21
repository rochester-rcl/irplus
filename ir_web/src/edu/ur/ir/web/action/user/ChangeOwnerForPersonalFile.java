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

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;

/**
 * Action to change personal file owner
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ChangeOwnerForPersonalFile extends ActionSupport{

	/** Eclipse generated Id	 */
	private static final long serialVersionUID = -7262077243101493697L;
	
	/** User file system service. */
	private UserFileSystemService userFileSystemService;
	
	/** User service. */
	private UserService userService;
	
	/**  Logger for action */
	private static final Logger log = Logger.getLogger(ChangeOwnerForPersonalFile.class);
	
	/** Id of personal file */
	private Long personalFileId;
	
	/** Collaborator to be made as new owner */
	private Long newOwnerId;

	/**
	 * changes the file owner 
	 * 
	 * @return
	 */
	public String execute() {
		
		log.debug("Change owner Personal file::" + personalFileId);
		
		PersonalFile personalFile = userFileSystemService.getPersonalFile(personalFileId, false);
		
		IrUser newOwner = userService.getUser(newOwnerId, false);
		
		personalFile.getVersionedFile().changeOwner(newOwner);

		userFileSystemService.makePersonalFilePersistent(personalFile);
		
		return SUCCESS;
	}


	/**
	 * Get User file system service
	 *  
	 * @return
	 */
	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	/**
	 * Set User file system service
	 * 
	 * @param userFileSystemService
	 */
	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

	/**
	 * Get personal file id
	 * 
	 * @return
	 */
	public Long getPersonalFileId() {
		return personalFileId;
	}

	/**
	 * Set personal file id
	 * 
	 * @param personalFileId
	 */
	public void setPersonalFileId(Long personalFileId) {
		this.personalFileId = personalFileId;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public Long getNewOwnerId() {
		return newOwnerId;
	}


	public void setNewOwnerId(Long newOwnerId) {
		this.newOwnerId = newOwnerId;
	}

}
