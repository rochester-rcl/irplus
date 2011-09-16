package edu.ur.ir.groupspace.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.StringUtils;

import edu.ur.ir.groupspace.GroupWorkspaceInvite;
import edu.ur.ir.groupspace.GroupWorkspaceInviteDAO;
import edu.ur.ir.groupspace.GroupWorkspaceInviteService;

/**
 * Default implementation of the group workspace group invite service.
 * 
 * @author Nathan Sarr
 *
 */
public class DeafaultGroupWorkspaceInviteService implements GroupWorkspaceInviteService{
	
	/* Data access for the group workspace group invite  */
	private GroupWorkspaceInviteDAO groupWorkspaceGroupInviteDAO;
	
	/*  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DeafaultGroupWorkspaceInviteService.class);

	/* Mail message for inviting user who exist in the system*/
	private SimpleMailMessage userWorkspaceGroupInviteUserExists;
	
	/* Mail message for inviting a user who is not in the system*/
	private SimpleMailMessage userWorkspaceGroupInviteUserNotExistsMessage;
	
	/* Base path for the web app  */
	private String baseWebAppPath;
	
	/* Mail sender */
	private transient MailSender mailSender;


	/**
	 * Find the Invite information for a specified token
	 * 
	 * @param token user token
	 * @return User token information
	 */
	public GroupWorkspaceInvite findByToken(String token)
	{
		return  groupWorkspaceGroupInviteDAO.findInviteInfoForToken(token);
	}
	
	/**
	 * Find the Invite information for a specified email
	 * 
	 * @param email email address shared with
	 * @return List of invite information
	 */
	public List<GroupWorkspaceInvite> getByEmail(String email)
	{
		return  groupWorkspaceGroupInviteDAO.getInviteInfoByEmail(email);
	}
		

	
	/**
	 * Get the invited group workspace group user by id.
	 * 
	 * @param id - id of the invite id
	 * @param lock - upgrade the lock.
	 * 
	 * @return the invite if found.
	 */
	public GroupWorkspaceInvite getById(Long id, boolean lock)
	{
		return  groupWorkspaceGroupInviteDAO.getById(id, lock);
	}

	
	/**
	 * Make the invite record persistent.
	 * 
	 * @param entity
	 */
	public void save(GroupWorkspaceInvite entity)
	{
		 groupWorkspaceGroupInviteDAO.makePersistent(entity);
	}

	/**
	 * Delete the invite 
	 * 
	 * @param entity
	 */
	public void delete(GroupWorkspaceInvite entity)
	{
		 groupWorkspaceGroupInviteDAO.makeTransient(entity);
	}

	/**
	 * Get a count of the number of invite records.
	 * 
	 */
	public Long getCount()
	{
		return  groupWorkspaceGroupInviteDAO.getCount();
	}
	
	/**
	 * Sends an email notifying the specified user that  they have been invited
	 * to join a group workspace.
	 * 
	 * @param invite
	 */
	public void sendEmailInvite(GroupWorkspaceInvite invite)
	{
		if( invite.getInvitedUser() != null )
		{
			sendExistingUserEmail(invite);
		}
		else
		{
			sendEmailToNotExistingUser(invite);
		}
	}
	
	
	/**
	 * Set the group workspace group invite data access object.
	 * 
	 * @param groupWorkspaceGroupInviteDAO
	 */
	public void setGroupWorkspaceGroupInviteDAO(
			GroupWorkspaceInviteDAO groupWorkspaceGroupInviteDAO) {
		this.groupWorkspaceGroupInviteDAO = groupWorkspaceGroupInviteDAO;
	}
	
	/**
	 * Set the mail message for invites for users who exist.
	 * 
	 * @param userWorkspaceGroupInviteUserExists
	 */
	public void setUserWorkspaceGroupInviteUserExists(
			SimpleMailMessage userWorkspaceGroupInviteUserExists) {
		this.userWorkspaceGroupInviteUserExists = userWorkspaceGroupInviteUserExists;
	}

	/**
	 * Set the workspace group invite for users who do NOT exist.
	 * 
	 * @param userWorkspaceGroupInviteUserNotExistsMessage
	 */
	public void setUserWorkspaceGroupInviteUserNotExistsMessage(
			SimpleMailMessage userWorkspaceGroupInviteUserNotExistsMessage) {
		this.userWorkspaceGroupInviteUserNotExistsMessage = userWorkspaceGroupInviteUserNotExistsMessage;
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
	private void sendExistingUserEmail(GroupWorkspaceInvite invite)
	{
		SimpleMailMessage message = new SimpleMailMessage(userWorkspaceGroupInviteUserExists);
		message.setTo(invite.getInviteToken().getEmail());
		
		String subject = message.getSubject();
		subject = StringUtils.replace(subject, "%FIRST_NAME%", invite.getInviteToken().getInvitingUser().getFirstName());
		subject = StringUtils.replace(subject, "%LAST_NAME%",  invite.getInviteToken().getInvitingUser().getLastName());
		message.setSubject(subject);
		
		String text = message.getText();
		
		text = StringUtils.replace(text, "%NAME%", invite.getGroupWorkspace().getName());
		text = StringUtils.replace(text, "%BASE_WEB_APP_PATH%", baseWebAppPath);
		if( invite.getInviteToken().getInviteMessage() != null )
		{
		    text = text.concat(invite.getInviteToken().getInviteMessage());
		}
		message.setText(text);
		sendEmail(message);
	}
	
	/**
	 * Send an email to a user who has not yet registered in the system
	 * 
	 * @param invite
	 */
	private void sendEmailToNotExistingUser(GroupWorkspaceInvite invite)
	{
		SimpleMailMessage message = new SimpleMailMessage(userWorkspaceGroupInviteUserNotExistsMessage);
		message.setTo(invite.getInviteToken().getEmail());
	
		String subject = message.getSubject();
		subject = StringUtils.replace(subject, "%FIRST_NAME%", invite.getInvitedUser().getFirstName());
		subject = StringUtils.replace(subject, "%LAST_NAME%", invite.getInvitedUser().getLastName());
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
	

}
