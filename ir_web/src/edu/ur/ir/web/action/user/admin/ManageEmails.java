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


package edu.ur.ir.web.action.user.admin;

import java.io.File;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.groupspace.GroupWorkspaceInviteService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.FileSharingException;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UnVerifiedEmailException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserIndexService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.util.TokenGenerator;

/**
 * Action for managing the emails of the user
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ManageEmails extends ActionSupport implements  Preparable, UserIdAware{

	/** eclipse generated id */
	private static final long serialVersionUID = 4890200754192783056L;

	/**  Logger  */
	private static final Logger log = Logger.getLogger(ManageEmails.class);


	/** user service */
	private UserService userService;
	
	/** Set of emails  */
	private Collection<UserEmail> emails;
	
	/**  Indicates the email has been added*/
	private boolean added = false;
	
	/** Indicates the emails have been deleted */
	private boolean deleted = false;
	
	/** id of the user  */
	private Long id;
	
	/** id of the user making the changes - could be admin - could be user */
	private Long userId;
	
	/** id of the email  */
	private Long emailId;
	
	/** Email of the user being created */
	private UserEmail email;
	
	/** User being edited */
	private IrUser irUser;
	
	/** Indicates whether the email is the default email or not  */
	private boolean defaultEmail;
	
	/** Service for indexing users */
	private UserIndexService userIndexService;
	
	/** Repository service for placing information in the repository */
	private RepositoryService repositoryService;
	
	/** Holds the old email before editing it */
	private String oldEmail;
	
	/** Message about email verification */
	private String emailVerificationMessage;
	
	/** Service for inviting users */
	private InviteUserService inviteUserService;
	
	
	/* service to deal with invitations to a group workspace */
	private GroupWorkspaceInviteService groupWorkspaceInviteService;




	/**
	 * Prepares for the action
	 * 
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	public void prepare() throws Exception {
		log.debug( "Preparing id " + id);

		if( id != null)
		{
			irUser = userService.getUser(id, false);
		}
		
		if( emailId != null)
		{
			email = userService.getEmail(emailId, false);
		}
	}
	
	
	/**
	 * Create a new email.
	 * 
	 * @return
	 * @throws NoIndexFoundException 
	 */
	public String create() throws NoIndexFoundException
	{
		email.setEmail(email.getEmail().trim());
		IrUser otherUser = userService.getUserByEmail(email.getEmail());
		StringBuffer buffer = new StringBuffer();
		
		if (otherUser == null) {
			//user making the change
			IrUser changeMakingUser = userService.getUser(userId, false);
			
			// if they are not an administrator and trying to change
			// an account that does not belong to them then deny
			// access 
			if( !changeMakingUser.hasRole(IrRole.ADMIN_ROLE) && !changeMakingUser.equals(irUser))
			{
				return "accessDenied";
			}
			
			String emailToken = TokenGenerator.getToken();
			email.setVerifiedFalse(emailToken);
			irUser.addUserEmail(email, false);
			userService.makeUserPersistent(irUser);

			// send email With URL to verify email
			userService.sendEmailForEmailVerification(emailToken, email.getEmail(), irUser.getUsername());

			buffer.append("An email is sent to the address - " + email.getEmail() 
					+ ". Please follow the URL in the email to verify this email address.");
			
			if (defaultEmail) {
				buffer.append(
						"The email Id - " + email.getEmail() + " cannot be set as Default email until its verified.");				
			}
			
			Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
						false);
			userIndexService.updateIndex(irUser, 
					new File( repository.getUserIndexFolder()) );
			emailVerificationMessage = buffer.toString();
			added = true;
		} else {
			addFieldError("emailExistError", 
					"This Email already exists in the system. Email: " + email.getEmail());
		}
		
		return "added";
	}
	

	
	/**
	 * Method to change the verification of an email - setting it to unverified will
	 * cause the user to get a new email to verify.
	 * 
	 * @return
	 * @throws FileSharingException 
	 * @throws UnVerifiedEmailException 
	 */
	public String setVerified() throws FileSharingException, UnVerifiedEmailException
	{
		log.debug("verifing email with id = " + emailId);

		//user making the change
		IrUser changeMakingUser = userService.getUser(userId, false);
		
		// if they are not an administrator then can only change verification
		// through following email
		if( !changeMakingUser.hasRole(IrRole.ADMIN_ROLE))
		{
			 return "accessDenied";
		}

		UserEmail userEmail = userService.getEmail(emailId, false);
		userEmail.setVerifiedTrue();
		userService.makeUserEmailPersistent(userEmail);
		emails = userEmail.getIrUser().getUserEmails();
		
		log.debug("invite user service = " + inviteUserService);
		log.debug("user id = " + userEmail.getIrUser().getId());
		log.debug(" user email = " + userEmail.getEmail() );
		groupWorkspaceInviteService.addUserToGroupsForEmail(userEmail.getEmail());
		inviteUserService.sharePendingFilesForEmail(userEmail.getIrUser().getId(), userEmail.getEmail());

		return "viewEmails";
	}
	
	/**
	 * Method to change the verification of an email - setting it to unverified will
	 * cause the user to get a new email to verify.
	 * 
	 * @return
	 */
	public String requestNewVerification()
	{
		log.debug("verifing email with id = " + emailId);

		//user making the change
		IrUser changeMakingUser = userService.getUser(userId, false);
		
		// if they are not an administrator or owner trying to change
		// an account that does not belong to them then deny
		// access 
		if( !changeMakingUser.hasRole(IrRole.ADMIN_ROLE) && !changeMakingUser.equals(irUser))
		{
			return "accessDenied";
		}

		UserEmail userEmail = userService.getEmail(emailId, false);
		userEmail.setVerifiedFalse(TokenGenerator.getToken());
	    userService.sendAccountVerificationEmailForUser(TokenGenerator.getToken(), userEmail.getEmail(), irUser.getUsername());

		return "added";
	}
	
	/**
	 * Removes the selected emails.
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("Delete email called" + emailId);
		
		//user making the change
		IrUser changeMakingUser = userService.getUser(userId, false);
		
		if( emailId != null )
		{
			UserEmail removeEmail = userService.getEmail(emailId, false);
			if( !removeEmail.getIrUser().equals(changeMakingUser) && !changeMakingUser.hasRole(IrRole.ADMIN_ROLE))
			{
			    return "accessDenied";
			}
			userService.makeEmailTransient(removeEmail);
		    
		}
		deleted = true;
		return "deleted";
	}
	
	/**
	 * Set the email as default.
	 * 
	 * @return
	 */
	public String setDefault()
	{
		log.debug("set default email called" + emailId);
		
		//user making the change
		IrUser changeMakingUser = userService.getUser(userId, false);
		
		if( emailId != null )
		{
			UserEmail defaultEmail = userService.getEmail(emailId, false);
			log.debug("change making user = " + changeMakingUser + " default email user = " + defaultEmail.getIrUser());
			if( !defaultEmail.getIrUser().equals(changeMakingUser) && !changeMakingUser.hasRole(IrRole.ADMIN_ROLE))
			{
			    return "accessDenied";
			}

			if( defaultEmail.isVerified())
			{
				IrUser user = defaultEmail.getIrUser();
				user.setDefaultEmail(defaultEmail);
				userService.makeUserPersistent(user);
				emails = user.getUserEmails();
			}
		    
		}
		return "viewEmails";
	}
	
	/**
	 * Get the emails table data.
	 * 
	 * @return
	 */
	public String viewEmails()
	{
		//user making the change
		IrUser changeMakingUser = userService.getUser(userId, false);
		
		// if they are not an administrator and trying to view
		// an account that does not belong to them then deny
		// access 
		if( !changeMakingUser.hasRole(IrRole.ADMIN_ROLE) && !changeMakingUser.getId().equals(id))
		{
			return "accessDenied";
		}
		
		IrUser user = userService.getUser(id, false);
		emails = user.getUserEmails();
		
		return SUCCESS;

	}
	


	/**
	 * Get the user service 
	 * 
	 * @return user service
	 * 
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Set the user service
	 * 
	 * @param userService user service
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Get emails for this user
	 * 
	 * @return Collection of emails for the user
	 */
	public Collection<UserEmail> getEmails() {
		return emails;
	}

	/**
	 * Set emails for this user
	 * 
	 * @param emails Collection of user emails
	 */
	public void setEmails(Collection<UserEmail> emails) {
		this.emails = emails;
	}


	/**
	 * Indicates whether the email is added
	 * 
	 * @return Returns true if email added else returns false
	 */
	public boolean isAdded() {
		return added;
	}

	/**
	 * Set true if email added else set false
	 * 
	 * @param added True if email added else false
	 */
	public void setAdded(boolean added) {
		this.added = added;
	}

	/**
	 * Indicates whether the email is deleted
	 * 
	 * @return Returns true if email deleted else returns false
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Set true if email deleted else set false
	 * 
	 * @param deleted True if email deleted else false
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Get the user id
	 * 
	 * @return id of the user
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the id of the user 
	 * 
	 * @param id user id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get email that is created or edited
	 * 
	 * @return email
	 */
	public UserEmail getEmail() {
		return email;
	}

	/**
	 * Set email that is created or edited
	 * 
	 * @param email email information
	 */
	public void setEmail(UserEmail email) {
		this.email = email;
	}

	/**
	 * Get user that is being edited
	 * 
	 * @return user information
	 */
	public IrUser getIrUser() {
		return irUser;
	}

	/**
	 * Set user that is being edited
	 * 
	 * @param irUser user information
	 */
	public void setIrUser(IrUser irUser) {
		this.irUser = irUser;
	}
	
	/**
	 * Get email Id that is being edited
	 * 
	 * @return id of the email
	 */
	public Long getEmaild() {
		return emailId;
	}
	
	/**
	 * Set email id 
	 * 
	 * @param emailId id of the email
	 */
	public void setEmailId(Long emailId) {
		this.emailId = emailId;
	}
	
	/**
	 * Returns true if the email is default email else returns false
	 * 
	 * @return True if the email is default email else returns false
	 */
	public boolean isDefaultEmail() {
		return defaultEmail;
	}
	
	/**
	 * Set true if the email is default email else set false
	 * 
	 * @param defaultEmail True if the email is default email else false
	 */
	public void setDefaultEmail(boolean defaultEmail) {
		this.defaultEmail = defaultEmail;
	}
	public UserIndexService getUserIndexService() {
		return userIndexService;
	}
	public void setUserIndexService(UserIndexService userIndexService) {
		this.userIndexService = userIndexService;
	}
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	public String getOldEmail() {
		return oldEmail;
	}
	
	public void setOldEmail(String oldEmail) {
		this.oldEmail = oldEmail;
	}

	public String getEmailVerificationMessage() {
		return emailVerificationMessage;
	}
	
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}

	
	/**
	 * Set the group workspace invite service.
	 * 
	 * @param groupWorkspaceInviteService
	 */
	public void setGroupWorkspaceInviteService(
			GroupWorkspaceInviteService groupWorkspaceInviteService) {
		this.groupWorkspaceInviteService = groupWorkspaceInviteService;
	}


}


