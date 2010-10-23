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


package edu.ur.ir.user.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.StringUtils;

import edu.ur.cgLib.CgLibHelper;
import edu.ur.ir.file.FileCollaborator;
import edu.ur.ir.file.FileCollaboratorDAO;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.FileSharingException;
import edu.ur.ir.user.InviteInfo;
import edu.ur.ir.user.InviteInfoDAO;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFileDAO;
import edu.ur.ir.user.PersonalFileDeleteRecord;
import edu.ur.ir.user.PersonalFileDeleteRecordDAO;
import edu.ur.ir.user.RoleService;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.order.OrderType;


/**
 * Service for inviting user
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultInviteUserService implements InviteUserService {

	/** eclipse generated id */
	private static final long serialVersionUID = -6844595955214510755L;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultInviteUserService.class);
	
	/* Mail sender */
	private MailSender mailSender;

	/* Mail message for inviting user who exist in the system*/
	private SimpleMailMessage userExistMailMessage;
	
	/* Mail message for inviting a user who is not in the system*/
	private SimpleMailMessage userNotExistMailMessage;
	
	/* Mail message to unshare the document*/
	private SimpleMailMessage unShareMailMessage;

	/* User services */
	private UserService userService;
	
	/* Data access for invite information  */
	private InviteInfoDAO inviteInfoDAO;
	
	/* Data access for personal file */
	private PersonalFileDAO personalFileDAO;

	/* Data access for versioned file */
	private VersionedFileDAO versionedFileDAO;

	/* Data access for file collaborator */
	private FileCollaboratorDAO fileCollaboratorDAO;
	
	/* Data access for user */
	private IrUserDAO irUserDAO;

	/* Data access for ACL */
	private SecurityService securityService;
	
	/** Service for dealing with user file system */
	private UserFileSystemService userFileSystemService;
	
	/** Base path for the web app  */
	private String baseWebAppPath;
	
	/** Role service class */
	private RoleService roleService;
	
	/** captures information about deleting information */
	private PersonalFileDeleteRecordDAO personalFileDeleteRecordDAO;
	

	/**
	 * Persistent method for invite info
	 * 
	 * @see edu.ur.ir.user.InviteUserService#makeInviteInfoPersistent(InviteInfo entity)
	 */
	public void makeInviteInfoPersistent(InviteInfo entity) {
		inviteInfoDAO.makePersistent(entity);
	}

	/**
	 * Sends email invitation for collaborating on a file
	 * 
	 *  @see edu.ur.ir.user.InviteUserService#sendEmailToExistingUser(InviteInfo)
	 */
	public void sendEmailToExistingUser(InviteInfo inviteInfo) {
		
		SimpleMailMessage message = new SimpleMailMessage(userExistMailMessage);
		message.setTo(inviteInfo.getEmail());
		
		String subject = message.getSubject();
		subject = StringUtils.replace(subject, "%FIRST_NAME%", inviteInfo.getUser().getFirstName());
		subject = StringUtils.replace(subject, "%LAST_NAME%", inviteInfo.getUser().getLastName());
		message.setSubject(subject);
		
		String text = message.getText();
		
		// Get the name of files
		StringBuffer names = new StringBuffer();
		VersionedFile[] files = inviteInfo.getFiles().toArray(new VersionedFile[0]);
		for(int i = 0; i<files.length; i++) {
			names.append(files[i].getName());
			if ( i != (files.length-1) ) {
				names.append(", ");
			}
		}
		
		text = StringUtils.replace(text, "%NAME%", names.toString());
		text = StringUtils.replace(text, "%BASE_WEB_APP_PATH%", baseWebAppPath);
		text = text.concat(inviteInfo.getInviteMessage());
		message.setText(text);
		sendEmail(message);
	}
	
	/**
	 * Sends email invitation for collaborating on a file
	 * 
	 *  @see edu.ur.ir.user.InviteUserService#sendEmailToNotExistingUser(InviteInfo)
	 */
	public void sendEmailToNotExistingUser(InviteInfo inviteInfo) {
		SimpleMailMessage message = new SimpleMailMessage(userNotExistMailMessage);
		message.setTo(inviteInfo.getEmail());
	
		String subject = message.getSubject();
		subject = StringUtils.replace(subject, "%FIRST_NAME%", inviteInfo.getUser().getFirstName());
		subject = StringUtils.replace(subject, "%LAST_NAME%", inviteInfo.getUser().getLastName());
		message.setSubject(subject);

		String text = message.getText();

		// Get the name of files
		StringBuffer names = new StringBuffer();
		VersionedFile[] files = inviteInfo.getFiles().toArray(new VersionedFile[0]);
		for(int i = 0; i<files.length; i++) {
			names.append(files[i].getName());
			if ( i != (files.length-1) ) {
				names.append(", ");
			}
		}
		
		text = StringUtils.replace(text, "%NAME%", names.toString());
		text = StringUtils.replace(text, "%TOKEN%", inviteInfo.getToken());
		text = StringUtils.replace(text, "%BASE_WEB_APP_PATH%", baseWebAppPath);
		text = text.concat(inviteInfo.getInviteMessage());
		message.setText(text);
		sendEmail(message);
	}

	/**
	 * Sends email
	 * 
	 * @param message Email message 
	 */
	private void sendEmail(SimpleMailMessage message) {
		try {
			log.debug("Before send email");
			mailSender.send(message);
			log.debug("after send email");
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * Shares the file with the other user
	 * @throws FileSharingException 
	 * 
	 * @see edu.ur.ir.user.InviteUserService#shareFile(IrUser, VersionedFile)
	 */
	public SharedInboxFile shareFile(IrUser invitingUser, 
			IrUser invitedUser, VersionedFile versionedFile) throws FileSharingException {
		
		log.debug("Share file " + versionedFile.toString() + " with user " + invitedUser.toString());

		// Add collaborator to file
		versionedFile.addCollaborator(invitedUser);
		versionedFileDAO.makePersistent(versionedFile);

		SharedInboxFile inboxFile = invitedUser.addToSharedFileInbox(invitingUser, versionedFile);
		userFileSystemService.makeSharedInboxFilePersistent(inboxFile);
		return inboxFile;
	}

	/**
	 * Removes the specified shared file from the list of users
	 *  
	 * @param fileCollaborator List of users to remove the file from
	 */
	public void unshareFile(FileCollaborator fileCollaborator, IrUser unsharingUser){
		IrUser collaborator = fileCollaborator.getCollaborator();
		VersionedFile file = fileCollaborator.getVersionedFile();
		log.debug("UnShare file " + file.toString() + " with user " + collaborator);
		
		// Remove Personal file from user if it is in the users files
		PersonalFile personalFile 
		= personalFileDAO.getFileForUserWithSpecifiedVersionedFile(collaborator.getId(), 
					file.getId());
		
		if (personalFile != null) {
			// create a delete record 
			PersonalFileDeleteRecord personalFileDeleteRecord = new PersonalFileDeleteRecord(unsharingUser.getId(),
					personalFile.getId(),
					personalFile.getFullPath(), 
					personalFile.getDescription());
			personalFileDeleteRecord.setDeleteReason("UN-SHAREING FILE");
			personalFileDeleteRecordDAO.makePersistent(personalFileDeleteRecord);
			
			if(personalFile.getPersonalFolder() != null)
			{
				personalFile.getPersonalFolder().removePersonalFile(personalFile);
			}
			else
			{
			    personalFile.getOwner().removeRootFile(personalFile);
			}
			personalFileDAO.makeTransient(personalFile);
		}
		
		// Remove the personal file from user if the file is in the shared file inbox
		SharedInboxFile inboxFile = collaborator.getSharedInboxFile(fileCollaborator.getVersionedFile());
		if( inboxFile != null )
		{
			userFileSystemService.makeSharedInboFileTransient(inboxFile);
		}
		
		// Delete file collaborator
		file.removeCollaborator(fileCollaborator);
		fileCollaboratorDAO.makeTransient(fileCollaborator);
		
		securityService.deletePermissions(file.getId(), CgLibHelper.cleanClassName(file.getClass().getName()), fileCollaborator.getCollaborator());
	}
	
	
	/**
	 * Sends an un-share email.
	 * 
	 * @param fileOwnerEmail - owner of the file
	 * @param collaboratorEmail - collaborator who will be removed from the sharing
	 * @param fileName - file that is no longer shared.
	 */
	public void sendUnshareEmail(String fileOwnerEmail, String collaboratorEmail, String fileName)
	{
		// Send email to the user that the file is unshared
		sendEmailToUnShare(fileOwnerEmail, collaboratorEmail, fileName);
	}
	
	/**
	 * Sends email to inform the user that the document is removed from sharing
	 * 
	 * @param from Email from address
	 * @param to Email To address
	 * @param fileName Name of the unshared file 
	 */
	private void sendEmailToUnShare(String from, String to, String fileName) {
		log.debug("Sending email to unshare to " + to);
		SimpleMailMessage message = new SimpleMailMessage(unShareMailMessage);
		message.setTo(to);
		
		String subject = message.getSubject();
		message.setSubject(subject);

		String text = message.getText();
		text = StringUtils.replace(text, "%NAME%", fileName);
		message.setText(text);
		
		sendEmail(message);
	}
	
	/**
	 * Share file for user with the specified token 
	 * 
	 * @param UserId Id of the invited user
	 * @param token Token given to the invited user
	 * @throws FileSharingException - if the user tries to share a file with themselves
	 */
	public Set<SharedInboxFile> shareFileForUserWithToken(Long userId, String token) throws FileSharingException {
		
		IrUser invitedUser = userService.getUser(userId, true);
		
		InviteInfo inviteInfo = findInviteInfoByToken(token);
		Set<SharedInboxFile> inboxFiles = new HashSet<SharedInboxFile>();
		
		if (inviteInfo != null) {

			// If the shared user has no Author or collaborator or researcher or admin role, then assign collaborator role
			if (!invitedUser.hasRole(IrRole.AUTHOR_ROLE) && !invitedUser.hasRole(IrRole.COLLABORATOR_ROLE) 
					&& !invitedUser.hasRole(IrRole.RESEARCHER_ROLE) && !invitedUser.hasRole(IrRole.ADMIN_ROLE)) {
				
				invitedUser.addRole(roleService.getRole(IrRole.COLLABORATOR_ROLE));
				userService.makeUserPersistent(invitedUser);
			}
			
			// Add the email to the invited user
			UserEmail userEmail = new UserEmail(inviteInfo.getEmail());
			userEmail.setVerified(true);
			invitedUser.addUserEmail(userEmail, false);
			userService.makeUserPersistent(invitedUser);
			
			Set<VersionedFile> files = inviteInfo.getFiles();
			
			for(VersionedFile file:files) {
				if (file.getCollaborator(invitedUser) == null) {
	
					// Share the file with invited user
					SharedInboxFile inboxFile = shareFile(inviteInfo.getUser(), invitedUser, file);
					inboxFiles.add(inboxFile);
					// create permissions for the shared file
					securityService.createPermissions(file, invitedUser, inviteInfo.getPermissions());
		
				} else {
					log.debug("The file " + file.getName() 
							+ " is already shared with this user:" + invitedUser.getUsername());
				}
			}
			inviteInfoDAO.makeTransient(inviteInfo);
		}
		
		return inboxFiles;
	}
	
	

	/**
	 * Share file for user with the specified email. 
	 * When user adds new email, this method shares the files invited with newly added email.
	 * 
	 * @param UserId Id of the invited user
	 * @param email Email used to invite the user
	 * @throws FileSharingException - if the user tries to share a file with themselves
	 */
	public Set<SharedInboxFile> sharePendingFilesForEmail(Long userId, String email) throws FileSharingException {
		
		IrUser invitedUser = userService.getUser(userId, true);
		
		List<InviteInfo> invites = inviteInfoDAO.getInviteInfoByEmail(email);
		
		if ((invites != null) && (invites.size() > 0)) {
			// If the shared user has no Author or collaborator or researcher or admin role, then assign collaborator role
			if (!invitedUser.hasRole(IrRole.AUTHOR_ROLE) && !invitedUser.hasRole(IrRole.COLLABORATOR_ROLE) 
					&& !invitedUser.hasRole(IrRole.RESEARCHER_ROLE) && !invitedUser.hasRole(IrRole.ADMIN_ROLE)) {
				
				invitedUser.addRole(roleService.getRole(IrRole.COLLABORATOR_ROLE));
				userService.makeUserPersistent(invitedUser);
			}			
		}
		
		Set<SharedInboxFile> inboxFiles = new HashSet<SharedInboxFile>();
		
		for (InviteInfo inviteInfo: invites) {
			
			if (inviteInfo != null) {
				
				// Add the email to the invited user
				UserEmail userEmail = new UserEmail(inviteInfo.getEmail());
				invitedUser.addUserEmail(userEmail, false);
				
				Set<VersionedFile> files = inviteInfo.getFiles();
				
				
				for(VersionedFile file:files) {
					if (file.getCollaborator(invitedUser) == null) {
		
						// Share the file with invited user
						SharedInboxFile inboxFile = shareFile(inviteInfo.getUser(), invitedUser, file);
						inboxFiles.add(inboxFile);
						// create permissions for the shared file
						securityService.createPermissions(file, invitedUser, inviteInfo.getPermissions());
			
					} else {
						log.debug("The file " + file.getName() 
								+ " is already shared with this user:" + invitedUser.getUsername());
					}
				}
				inviteInfoDAO.makeTransient(inviteInfo);
			}
		}
		
		return inboxFiles;
	}
	
	/**
	 * Find invite information by token
	 * 
	 *  @see edu.ur.ir.user.InviteUserService#findInviteInfoByToken(String)
	 */
	public InviteInfo findInviteInfoByToken(String token){
		InviteInfo inviteUser = inviteInfoDAO.findInviteInfoForToken(token);
		return inviteUser;
	}
	
	/**
	 * Get the invite info information by email address.
	 * 
	 * @param email - email to find invite info information for.
	 * @return - all invite infos found
	 */
	public List<InviteInfo> findInviteInfoByEmail(String email)
	{
		return inviteInfoDAO.getInviteInfoByEmail(email);
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public InviteInfoDAO getInviteInfoDAO() {
		return inviteInfoDAO;
	}

	public void setInviteInfoDAO(InviteInfoDAO inviteInfoDAO) {
		this.inviteInfoDAO = inviteInfoDAO;
	}

	public IrUserDAO getIrUserDAO() {
		return irUserDAO;
	}

	public void setIrUserDAO(IrUserDAO irUserDAO) {
		this.irUserDAO = irUserDAO;
	}

	public PersonalFileDAO getPersonalFileDAO() {
		return personalFileDAO;
	}

	public void setPersonalFileDAO(PersonalFileDAO personalFileDAO) {
		this.personalFileDAO = personalFileDAO;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public VersionedFileDAO getVersionedFileDAO() {
		return versionedFileDAO;
	}

	public void setVersionedFileDAO(VersionedFileDAO versionedFileDAO) {
		this.versionedFileDAO = versionedFileDAO;
	}

	public FileCollaboratorDAO getFileCollaboratorDAO() {
		return fileCollaboratorDAO;
	}

	public void setFileCollaboratorDAO(FileCollaboratorDAO fileCollaboratorDAO) {
		this.fileCollaboratorDAO = fileCollaboratorDAO;
	}
	
	public FileCollaborator findFileCollaborator(Long fileCollaboratorId, boolean lock) {
		return fileCollaboratorDAO.getById(fileCollaboratorId, lock);
	}

	public void setUserExistMailMessage(SimpleMailMessage userExistMailMessage) {
		this.userExistMailMessage = userExistMailMessage;
	}

	public void setUserNotExistMailMessage(SimpleMailMessage userNotExistMailMessage) {
		this.userNotExistMailMessage = userNotExistMailMessage;
	}

	public void setUnShareMailMessage(SimpleMailMessage unShareMailMessage) {
		this.unShareMailMessage = unShareMailMessage;
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}
	
	/**
	 * Save versioned file 
	 * 
	 * @param versionedFile file to be saved
	 */
	public void saveVersionedFile(VersionedFile versionedFile) {
		versionedFileDAO.makePersistent(versionedFile);
	}
	
	/**
	 * Get invite info by id
	 * 
	 * @param id Id of the invite information
	 * @param lock
	 *  
	 * @return Invite information for the sepcified id
	 */
	public InviteInfo getInviteInfoById(Long id, boolean lock) {
		return inviteInfoDAO.getById(id, lock);
	}

	public String getBaseWebAppPath() {
		return baseWebAppPath;
	}

	public void setBaseWebAppPath(String baseWebAppPath) {
		this.baseWebAppPath = baseWebAppPath;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/** 
	 * Delete shared inbox file
	 * 
	 * @param inboxFile
	 */
	public void deleteSharedInboxFile(SharedInboxFile inboxFile) {
		VersionedFile vf = inboxFile.getVersionedFile();
		FileCollaborator fileCollaborator = vf.getCollaborator(inboxFile.getSharedWithUser());
		vf.removeCollaborator(fileCollaborator);

		versionedFileDAO.makePersistent(vf);
		
		userFileSystemService.makeSharedInboFileTransient(inboxFile);

		securityService.deletePermissions(vf.getId(), CgLibHelper.cleanClassName(vf.getClass().getName()), fileCollaborator.getCollaborator());
	}
	
	public PersonalFileDeleteRecordDAO getPersonalFileDeleteRecordDAO() {
		return personalFileDeleteRecordDAO;
	}

	public void setPersonalFileDeleteRecordDAO(
			PersonalFileDeleteRecordDAO personalFileDeleteRecordDAO) {
		this.personalFileDeleteRecordDAO = personalFileDeleteRecordDAO;
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
	public List<InviteInfo> getInviteInfosOrderByInviteor(int rowStart,
			int maxResults, OrderType orderType)
	{
		return inviteInfoDAO.getInviteInfosOrderByInviteor(rowStart, maxResults, orderType);
	}
	
	/**
	 * Get a count of invite info objects
	 * 
	 * @return count of invite info objects
	 */
	public Long getInviteInfoCount()
	{
		return inviteInfoDAO.getCount();
	}


}
