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

import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.FileSharingException;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action to verify email address
 * 
 * @author Sharmila Ranganathan
 *
 */
public class EmailVerification extends ActionSupport implements UserIdAware {

	/** Eclipse generated Id */
	private static final long serialVersionUID = -2859616602684397278L;

	/**  Logger for email verification action */
	private static final Logger log = Logger.getLogger(EmailVerification.class);

	/** Token to identify the email to be verified */
	private String token;
	
	/** Id of the user */
	private Long userId;

	/** User service class */
	private UserService userService;
	
	/** Invite user service class */
	private InviteUserService inviteUserService;
	
	/** Service for dealing with repositories */
	private RepositoryService repositoryService;
	
	/** repository object */
	private Repository repository;

	/**
	 * Execute method to verify email
	 */
	public String execute() {
		log.debug("execute email verification ");
		IrUser user = userService.getUser(userId, false);
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		if (token != null) {
			UserEmail email = userService.getUserEmailByToken(token);
			
			if ((email != null) && (email.getIrUser().equals(user))) {
				email.setVerified(true);
				email.setToken(null);
				userService.makeUserPersistent(user);
				
				try {
					// Share files -  If there are any invitations sent to this email address 
					inviteUserService.sharePendingFilesForEmail(userId, email.getEmail());
				} catch (FileSharingException e) {
					log.error("File cannot be shared with themselves" + e.getMessage());
				}
				
				addFieldError("emailVerified", 
						"The email address - " + email.getEmail() + " is verified.");
				
			} else {
				if (email == null) {
					addFieldError("tokenNotExist", 
						"The email address is not verified. The URL is incorrect. Please follow the URL sent to your email address for Email verification.");
				} else {
					addFieldError("tokenNotExist", 
					"The email address is not verified. Please follow the URL sent to your email address and login as user - "
							+ email.getIrUser().getFirstName() + " " + email.getIrUser().getLastName() + " to verify the Email.");
				}
			}
		} else {
			addFieldError("tokenNotExist", 
			"The email address is not verified. The URL is incorrect. Please follow the URL sent to your email address for Email verification.");
			
		}
		
		return SUCCESS;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

}
