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

package edu.ur.ir.groupspace.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.StringUtils;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceEmailInvite;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceInviteException;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.groupspace.GroupWorkspaceEmailInviteDAO;
import edu.ur.ir.groupspace.GroupWorkspaceInviteService;
import edu.ur.ir.groupspace.GroupWorkspaceUserDAO;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UnVerifiedEmailException;
import edu.ur.ir.user.UserService;
import edu.ur.util.TokenGenerator;

/**
 * Default implementation of the group workspace group invite service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultGroupWorkspaceInviteService implements GroupWorkspaceInviteService{
	
	/* Data access for the group workspace group invite  */
	private GroupWorkspaceEmailInviteDAO groupWorkspaceEmailInviteDAO;
	
	/* service to deal with user information */
    private UserService userService;

	/*  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultGroupWorkspaceInviteService.class);

	/* Mail message for inviting user who exist in the system*/
	private SimpleMailMessage userWorkspaceInviteUserExists;
	
	/* Mail message for inviting a user who is not in the system*/
	private SimpleMailMessage userWorkspaceInviteUserNotExistsMessage;
	
	/* Base path for the web app  */
	private String baseWebAppPath;
	
	/* Mail sender */
	private transient MailSender mailSender;
	
	/* send emails when an error occurs */
	private ErrorEmailService errorEmailService;
	
	/* deal with group workspace information */
	private GroupWorkspaceService groupWorkspaceService;
	
	/* group workspace file system service */
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;

	/* service to deal with group workspace users */
	private GroupWorkspaceUserDAO groupWorkspaceUserDAO;
	
	/* Data access for ACL */
	private SecurityService securityService;


	/**
	 * Find the Invite information for a specified token
	 * 
	 * @param token user token
	 * @return User token information
	 */
	public GroupWorkspaceEmailInvite getByToken(String token)
	{
		return  groupWorkspaceEmailInviteDAO.getInviteInfoForToken(token);
	}
	
	/**
	 * Find the Invite information for a specified email
	 * 
	 * @param email email address shared with
	 * @return List of invite information
	 */
	public List<GroupWorkspaceEmailInvite> getByEmail(String email)
	{
		return  groupWorkspaceEmailInviteDAO.getInviteInfoByEmail(email);
	}

	/**
	 * Get a count of the number of invite records.
	 * 
	 */
	public Long getCount()
	{
		return  groupWorkspaceEmailInviteDAO.getCount();
	}
	
	/**
	 * Set the group workspace group invite data access object.
	 * 
	 * @param groupWorkspaceEmailInviteDAO
	 */
	public void setGroupWorkspaceGroupInviteDAO(
			GroupWorkspaceEmailInviteDAO groupWorkspaceEmailInviteDAO) {
		this.groupWorkspaceEmailInviteDAO = groupWorkspaceEmailInviteDAO;
	}
	
	/**
	 * Set the mail message for invites for users who exist.
	 * 
	 * @param userWorkspaceGroupInviteUserExists
	 */
	public void setUserWorkspaceInviteUserExists(
			SimpleMailMessage userWorkspaceGroupInviteUserExists) {
		this.userWorkspaceInviteUserExists = userWorkspaceGroupInviteUserExists;
	}

	/**
	 * Set the workspace group invite for users who do NOT exist.
	 * 
	 * @param userWorkspaceGroupInviteUserNotExistsMessage
	 */
	public void setUserWorkspaceInviteUserNotExistsMessage(
			SimpleMailMessage userWorkspaceGroupInviteUserNotExistsMessage) {
		this.userWorkspaceInviteUserNotExistsMessage = userWorkspaceGroupInviteUserNotExistsMessage;
	}

	/**
	 * Set the base web app path
	 * 
	 * @param baseWebAppPath
	 */
	public void setBaseWebAppPath(String baseWebAppPath) {
		this.baseWebAppPath = baseWebAppPath;
	}
	
	/**
	 * Send the invite to a user who already exists in the system.
	 * 
	 * @param invite
	 */
	 public void sendEmailInvite(IrUser invitingUser, GroupWorkspace groupWorkspace, String email, String inviteMessage)	{
		SimpleMailMessage message = new SimpleMailMessage(userWorkspaceInviteUserExists);
		message.setTo(email);
		
		String subject = message.getSubject();
		subject = StringUtils.replace(subject, "%FIRST_NAME%", invitingUser.getFirstName());
		subject = StringUtils.replace(subject, "%LAST_NAME%",  invitingUser.getLastName());
		message.setSubject(subject);
		
		String text = message.getText();
		
		text = StringUtils.replace(text, "%NAME%", groupWorkspace.getName());
		text = StringUtils.replace(text, "%BASE_WEB_APP_PATH%", baseWebAppPath);
		if( inviteMessage != null && !inviteMessage.trim().equals(""))
		{
		    text = text.concat(inviteMessage);
		}
		message.setText(text);
		sendEmail(message);
	}
	
	/**
	 * Send an email to a user who has not yet registered in the system
	 * 
	 * @param invite
	 */
	private void sendEmailToNotExistingUser(GroupWorkspaceEmailInvite invite)
	{
		SimpleMailMessage message = new SimpleMailMessage(userWorkspaceInviteUserNotExistsMessage);
		message.setTo(invite.getInviteToken().getEmail());
	
		String subject = message.getSubject();
		subject = StringUtils.replace(subject, "%FIRST_NAME%", invite.getInviteToken().getInvitingUser().getFirstName());
		subject = StringUtils.replace(subject, "%LAST_NAME%", invite.getInviteToken().getInvitingUser().getLastName());
		message.setSubject(subject);

		String text = message.getText();
		
		text = StringUtils.replace(text, "%NAME%", invite.getGroupWorkspace().getName());
		text = StringUtils.replace(text, "%TOKEN%", invite.getInviteToken().getToken());
		text = StringUtils.replace(text, "%BASE_WEB_APP_PATH%", baseWebAppPath);
		if( invite.getInviteToken().getInviteMessage() != null )
		{
		    text = text.concat(invite.getInviteToken().getInviteMessage());
		}
		message.setText(text);
		sendEmail(message);
	}
	
	/**
	 * Invite the specified users with the given emails.  This will determine if the emails
	 * already exist for a user in the system.  If the user already exists in the system a
	 * group workspace invite will be added to the users group display for approval
	 * 
	 * @param invitingUser - user doing the inviting
	 * @param emails - list of emails of users to invite to join the group.
	 * @param setAsOwner - set the users as owners of the group
	 * @param permissions - permissions to give the group
	 * @param groupWorkspace - group workspaces to add the user to
	 * @param inviteMessage - message to send to users
	 * 
	 * @return list of emails that were found to be invalid or could not be sent a message
	 * @throws PermissionNotGrantedException - if the user does not have permissions to invite users into a group
	 * @throws GroupWorkspaceInviteException 
	 */
	public List<String> inviteUsers(IrUser invitingUser, 
			List<String> emails, 
			boolean setAsOwner,
			Set<IrClassTypePermission> permissions, 
			GroupWorkspace groupWorkspace, 
			String inviteMessage) throws PermissionNotGrantedException
	{
		log.debug("invite users called");
		List<String> emailsNotSent = new LinkedList<String>();
		GroupWorkspaceUser groupWorkspaceUser = groupWorkspace.getUser(invitingUser);
		if( !invitingUser.hasRole(IrRole.ADMIN_ROLE))
		{
		    if( groupWorkspaceUser == null || !groupWorkspaceUser.getOwner())
		    {
			    throw new PermissionNotGrantedException("Only an owner can add new members");
		    }
		}
		
		for(String email : emails)
		{
			email = email.trim();
			// Check if user exist in the system
			IrUser invitedUser = userService.getUserForVerifiedEmail(email);
			log.debug("invited user = " + invitedUser);
			if( invitedUser != null )
			{
			    //check if the user is sharing the file with themselves
			    if (invitingUser.equals(invitedUser)) {
				    //do nothing
			    }
			    else if( groupWorkspace.getUser(invitedUser) == null )
			    {
			    	    log.debug("group workspace user is null adding new");
						GroupWorkspaceUser user = null;
						try {
							user = groupWorkspaceService.addUserToGroup(invitedUser, groupWorkspace, permissions, setAsOwner);
							log.debug("created group workspace user " + user);
						    sendEmailInvite(invitingUser, groupWorkspace, email, inviteMessage);
						} catch (DuplicateNameException e) {
							//user already exists
							log.error("this should not happen - user already extists", e);
							errorEmailService.sendError(e);
						}
			    }
			}
			else
			{
				log.debug("user does not exist in system creating invite");
				GroupWorkspaceEmailInvite invite = groupWorkspace.getInvite(email);
				if( invite == null )
				{
					try {
						invite = groupWorkspace.addInviteUser(email, permissions, invitingUser, TokenGenerator.getToken() + email);
						 try
						 {
						     sendEmailInvite(invite);
						     groupWorkspaceEmailInviteDAO.makePersistent(invite);
						 }
						 catch(IllegalStateException e)
						 {
						     emailsNotSent.add(email);
						 }
					} catch (GroupWorkspaceInviteException e1) {
						//user already exists
						log.error("this should not happen - user already extists", e1);
						errorEmailService.sendError(e1);
					}
				}
			}
		}
			
		return emailsNotSent;
	}
	

	
	/**
	 * Add users to all invited groups for a given email.  This email must be verified as
	 * valid.
	 * 
	 * @param email - email for user to be verified
	 * @throws UnVerifiedEmailException - if the email has not yet been verified.
	 */
	public void addUserToGroupsForEmail(String email) throws UnVerifiedEmailException
	{
		IrUser user = userService.getUserForVerifiedEmail(email);
		if( user == null )
		{
			throw new UnVerifiedEmailException("email " + email + " has not been verified ");
		}
		
		List<GroupWorkspaceEmailInvite> invites = groupWorkspaceEmailInviteDAO.getInviteInfoByEmail(email);
        for(GroupWorkspaceEmailInvite invite : invites)
        {
        	GroupWorkspace groupWorkspace = invite.getGroupWorkspace();
        	try {
				groupWorkspace.add(user,invite.isSetAsOwner());
				groupWorkspace.deleteEmailInvite(email);
				groupWorkspaceService.save(groupWorkspace);
			} catch (DuplicateNameException e) {
				log.error("this should not happen - user already extists", e);
				errorEmailService.sendError(e);
			}
        }
	}
	
	/**
	 * Sends email
	 * 
	 * @param message Email message 
	 */
	private void sendEmail(SimpleMailMessage message) 
	{
		try 
		{
			log.debug("Before send email");
			mailSender.send(message);
			log.debug("after send email");
		} 
		catch (Exception e) 
		{
			log.error(e.getMessage());
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Delete the group workspace email invite.
	 * 
	 * @see edu.ur.ir.groupspace.GroupWorkspaceInviteService#delete(edu.ur.ir.groupspace.GroupWorkspaceEmailInvite)
	 */
	public void delete(GroupWorkspaceEmailInvite entity) {
		groupWorkspaceEmailInviteDAO.makeTransient(entity);
	}

	/**
	 * Get the workspace email invite.
	 * 
	 * @see edu.ur.ir.groupspace.GroupWorkspaceInviteService#getEmailInviteById(java.lang.Long, boolean)
	 */
	public GroupWorkspaceEmailInvite getEmailInviteById(Long id, boolean lock) {
		return groupWorkspaceEmailInviteDAO.getById(id, lock);
	}

	/**
	 * Get the email invite count.
	 * 
	 * @see edu.ur.ir.groupspace.GroupWorkspaceInviteService#getEmailInviteCount()
	 */
	public Long getEmailInviteCount() {
		return groupWorkspaceEmailInviteDAO.getCount();
	}


	/**
	 * Save the group workspace email invite.
	 * 
	 * @see edu.ur.ir.groupspace.GroupWorkspaceInviteService#save(edu.ur.ir.groupspace.GroupWorkspaceEmailInvite)
	 */
	public void save(GroupWorkspaceEmailInvite entity) {
		groupWorkspaceEmailInviteDAO.makePersistent(entity);
	}

	/**
	 * Send the email invite.
	 * 
	 * @see edu.ur.ir.groupspace.GroupWorkspaceInviteService#sendEmailInvite(edu.ur.ir.groupspace.GroupWorkspaceEmailInvite)
	 */
	public void sendEmailInvite(GroupWorkspaceEmailInvite invite) {
		sendEmailToNotExistingUser(invite);
	}


	
	/**
	 * Get the group workspace email invite data access object.
	 * 
	 * @return
	 */
	public GroupWorkspaceEmailInviteDAO getGroupWorkspaceEmailInviteDAO() {
		return groupWorkspaceEmailInviteDAO;
	}

	/**
	 * Set the group workspace email data access object.
	 * 
	 * @param groupWorkspaceEmailInviteDAO
	 */
	public void setGroupWorkspaceEmailInviteDAO(
			GroupWorkspaceEmailInviteDAO groupWorkspaceEmailInviteDAO) {
		this.groupWorkspaceEmailInviteDAO = groupWorkspaceEmailInviteDAO;
	}
	
	/**
	 * Get the mail sender.
	 * 
	 * @return
	 */
	public MailSender getMailSender() {
		return mailSender;
	}

	/**
	 * Set the mail sender.
	 * 
	 * @param mailSender
	 */
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
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
	 * Service to deal with errors.
	 * 
	 * @return
	 */
	public ErrorEmailService getErrorEmailService() {
		return errorEmailService;
	}

	/**
	 * Service to deal with errors.
	 * 
	 * @param errorEmailService
	 */
	public void setErrorEmailService(ErrorEmailService errorEmailService) {
		this.errorEmailService = errorEmailService;
	}
	
	/**
	 * Get the group workspace service.
	 * 
	 * @return
	 */
	public GroupWorkspaceService getGroupWorkspaceService() {
		return groupWorkspaceService;
	}

	/**
	 * Set the group workspace service.
	 * 
	 * @param groupWorkspaceService
	 */
	public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}
	
	/**
	 * Group workspace user data access.
	 * 
	 * @return
	 */
	public GroupWorkspaceUserDAO getGroupWorkspaceUserDAO() {
		return groupWorkspaceUserDAO;
	}

	/**
	 * Set the group workspace user data access.
	 * 
	 * @param groupWorkspaceUserDAO
	 */
	public void setGroupWorkspaceUserDAO(GroupWorkspaceUserDAO groupWorkspaceUserDAO) {
		this.groupWorkspaceUserDAO = groupWorkspaceUserDAO;
	}
	
	/**
	 * Get the security service.
	 * 
	 * @return
	 */
	public SecurityService getSecurityService() {
		return securityService;
	}

	/**
	 * Set the security service.
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
	
	/**
	 * Get the group workspace file system service.
	 * 
	 * @return
	 */
	public GroupWorkspaceFileSystemService getGroupWorkspaceFileSystemService() {
		return groupWorkspaceFileSystemService;
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
}
