package edu.ur.ir.groupspace.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.StringUtils;

import edu.ur.ir.groupspace.GroupWorkspaceGroupInvite;
import edu.ur.ir.groupspace.GroupWorkspaceGroupInviteDAO;
import edu.ur.ir.groupspace.GroupWorkspaceGroupInviteService;
import edu.ur.order.OrderType;

/**
 * Default implementation of the group workspace group invite service.
 * 
 * @author Nathan Sarr
 *
 */
public class DeafaultGroupWorkspaceGroupInviteService implements GroupWorkspaceGroupInviteService{
	
	/* Data access for the group workspace group invite  */
	private GroupWorkspaceGroupInviteDAO groupWorkspaceGroupInviteDAO;
	
	/*  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DeafaultGroupWorkspaceGroupInviteService.class);

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
	public GroupWorkspaceGroupInvite findByToken(String token)
	{
		return  groupWorkspaceGroupInviteDAO.findInviteInfoForToken(token);
	}
	
	/**
	 * Find the Invite information for a specified email
	 * 
	 * @param email email address shared with
	 * @return List of invite information
	 */
	public List<GroupWorkspaceGroupInvite> getByEmail(String email)
	{
		return  groupWorkspaceGroupInviteDAO.getInviteInfoByEmail(email);
	}
		
	/**
	 * Get the list of invite infos ordered by inviteor
	 * 
	 * @param rowStart - start position in the list
	 * @param maxResults - maximum number of results to retrieve
	 * @param orderType - ascending/descending order
	 * 
	 * @return list of invite infos found
	 */
	public List<GroupWorkspaceGroupInvite> getAllOrderByGroup(int rowStart,
			int maxResults, OrderType orderType)
	{
	    return  groupWorkspaceGroupInviteDAO.getInviteInfosOrderByGroup(rowStart, maxResults, orderType);	
	}
	
	
	/**
	 * Get the invited group workspace group user by id.
	 * 
	 * @param id - id of the invite id
	 * @param lock - upgrade the lock.
	 * 
	 * @return the invite if found.
	 */
	public GroupWorkspaceGroupInvite getById(Long id, boolean lock)
	{
		return  groupWorkspaceGroupInviteDAO.getById(id, lock);
	}

	
	/**
	 * Make the invite record persistent.
	 * 
	 * @param entity
	 */
	public void save(GroupWorkspaceGroupInvite entity)
	{
		 groupWorkspaceGroupInviteDAO.makePersistent(entity);
	}

	/**
	 * Delete the invite 
	 * 
	 * @param entity
	 */
	public void delete(GroupWorkspaceGroupInvite entity)
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
	public void sendEmailInvite(GroupWorkspaceGroupInvite invite)
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
			GroupWorkspaceGroupInviteDAO groupWorkspaceGroupInviteDAO) {
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
	private void sendExistingUserEmail(GroupWorkspaceGroupInvite invite)
	{
		SimpleMailMessage message = new SimpleMailMessage(userWorkspaceGroupInviteUserExists);
		message.setTo(invite.getInviteToken().getEmail());
		
		String subject = message.getSubject();
		subject = StringUtils.replace(subject, "%FIRST_NAME%", invite.getInviteToken().getInvitingUser().getFirstName());
		subject = StringUtils.replace(subject, "%LAST_NAME%",  invite.getInviteToken().getInvitingUser().getLastName());
		message.setSubject(subject);
		
		String text = message.getText();
		
		text = StringUtils.replace(text, "%NAME%", invite.getGroup().getName());
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
	private void sendEmailToNotExistingUser(GroupWorkspaceGroupInvite invite)
	{
		SimpleMailMessage message = new SimpleMailMessage(userWorkspaceGroupInviteUserNotExistsMessage);
		message.setTo(invite.getInviteToken().getEmail());
	
		String subject = message.getSubject();
		subject = StringUtils.replace(subject, "%FIRST_NAME%", invite.getInvitedUser().getFirstName());
		subject = StringUtils.replace(subject, "%LAST_NAME%", invite.getInvitedUser().getLastName());
		message.setSubject(subject);

		String text = message.getText();

		
		
		text = StringUtils.replace(text, "%NAME%", invite.getGroup().getName());
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
