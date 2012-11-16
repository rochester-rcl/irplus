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

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.StringUtils;

import edu.ur.cgLib.CgLibHelper;
import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.file.FileCollaborator;
import edu.ur.ir.file.FileCollaboratorDAO;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.invite.InviteToken;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.FileSharingException;
import edu.ur.ir.user.FolderAutoShareInfo;
import edu.ur.ir.user.FolderAutoShareInfoDAO;
import edu.ur.ir.user.FolderInviteInfo;
import edu.ur.ir.user.FolderInviteInfoDAO;
import edu.ur.ir.user.FileInviteInfo;
import edu.ur.ir.user.FileInviteInfoDAO;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFileDAO;
import edu.ur.ir.user.PersonalFileDeleteRecord;
import edu.ur.ir.user.PersonalFileDeleteRecordDAO;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.RoleService;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.UnVerifiedEmailException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.order.OrderType;
import edu.ur.util.TokenGenerator;

/**
 * Service for inviting user to share files or notifying workspace users
 * of changes within the workspace
 * 
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultInviteUserService implements InviteUserService {

	/* eclipse generated id */
	private static final long serialVersionUID = -6844595955214510755L;

	/*  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultInviteUserService.class);
	
	/* Mail sender */
	private transient MailSender mailSender;

	/* Mail message for inviting user who exist in the system*/
	private SimpleMailMessage userExistMailMessage;
	
	/* Mail message for inviting a user who is not in the system*/
	private SimpleMailMessage userNotExistMailMessage;
	
	/* Message to notify users of a new file version */
	private SimpleMailMessage newFileVersionMailMessage;
	

	/* Mail message to unshare the document*/
	private SimpleMailMessage unShareMailMessage;

	/* User services */
	private UserService userService;
	
	/* Data access for invite information  */
	private FileInviteInfoDAO fileInviteInfoDAO;
	
	/* Data access for personal file */
	private PersonalFileDAO personalFileDAO;

	/* Data access for versioned file */
	private VersionedFileDAO versionedFileDAO;

	/* Data access for file collaborator */
	private FileCollaboratorDAO fileCollaboratorDAO;
	
	/* Data access for ACL */
	private SecurityService securityService;
	
	/* Service for dealing with user file system */
	private UserFileSystemService userFileSystemService;
	
	/* Base path for the web app  */
	private String baseWebAppPath;
	
	/* Role service class */
	private RoleService roleService;
	
	/* captures information about deleting information */
	private PersonalFileDeleteRecordDAO personalFileDeleteRecordDAO;

	/* invite information for folders  */
	private FolderInviteInfoDAO folderInviteInfoDAO;
	
	/* auto share information for folders  */
	private FolderAutoShareInfoDAO folderAutoShareInfoDAO;
	
	/* service for creating workspace index processing records */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;
	
	/* service for accessing index processing types */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/*  Service for sending an email when an error occurs */
	private ErrorEmailService errorEmailService;


	/**
	 * Persistent method for invite info
	 * 
	 * @see edu.ur.ir.user.InviteUserService#makeInviteInfoPersistent(FileInviteInfo entity)
	 */
	public void makeInviteInfoPersistent(FileInviteInfo entity) {
		fileInviteInfoDAO.makePersistent(entity);
	}

	/**
	 * Sends email invitation for collaborating on a file
	 * 
	 *  @see edu.ur.ir.user.InviteUserService#sendEmailToExistingUser(FileInviteInfo)
	 */
	public void sendEmailToExistingUser(FileInviteInfo inviteInfo) {
		
		SimpleMailMessage message = new SimpleMailMessage(userExistMailMessage);
		message.setTo(inviteInfo.getInviteToken().getEmail());
		
		String subject = message.getSubject();
		subject = StringUtils.replace(subject, "%FIRST_NAME%", inviteInfo.getInviteToken().getInvitingUser().getFirstName());
		subject = StringUtils.replace(subject, "%LAST_NAME%", inviteInfo.getInviteToken().getInvitingUser().getLastName());
		message.setSubject(subject);
		
		String text = message.getText();
		
		// Get the name of files
		StringBuffer names = new StringBuffer();
		VersionedFile[] files = inviteInfo.getFiles().toArray(new VersionedFile[0]);
		for(int i = 0; i<files.length; i++) {
			names.append(files[i].getNameWithExtension());
			if ( i != (files.length-1) ) {
				names.append(", ");
			}
		}
		
		text = StringUtils.replace(text, "%NAME%", names.toString());
		text = StringUtils.replace(text, "%BASE_WEB_APP_PATH%", baseWebAppPath);
		if( inviteInfo.getInviteToken().getInviteMessage() != null )
		{
		    text = text.concat(inviteInfo.getInviteToken().getInviteMessage());
		}
		message.setText(text);
		sendEmail(message);
	}
	
	/**
	 * Sends email invitation for collaborating on a file
	 * 
	 *  @see edu.ur.ir.user.InviteUserService#sendEmailToNotExistingUser(FileInviteInfo)
	 */
	public void sendEmailToNotExistingUser(FileInviteInfo inviteInfo) {
		SimpleMailMessage message = new SimpleMailMessage(userNotExistMailMessage);
		message.setTo(inviteInfo.getInviteToken().getEmail());
	
		String subject = message.getSubject();
		subject = StringUtils.replace(subject, "%FIRST_NAME%", inviteInfo.getInviteToken().getInvitingUser().getFirstName());
		subject = StringUtils.replace(subject, "%LAST_NAME%", inviteInfo.getInviteToken().getInvitingUser().getLastName());
		message.setSubject(subject);

		String text = message.getText();

		// Get the name of files
		StringBuffer names = new StringBuffer();
		VersionedFile[] files = inviteInfo.getFiles().toArray(new VersionedFile[0]);
		for(int i = 0; i<files.length; i++) {
			names.append(files[i].getNameWithExtension());
			if ( i != (files.length-1) ) {
				names.append(", ");
			}
		}
		
		text = StringUtils.replace(text, "%NAME%", names.toString());
		text = StringUtils.replace(text, "%TOKEN%", inviteInfo.getInviteToken().getToken());
		text = StringUtils.replace(text, "%BASE_WEB_APP_PATH%", baseWebAppPath);
		if( inviteInfo.getInviteToken().getInviteMessage() != null )
		{
		    text = text.concat(inviteInfo.getInviteToken().getInviteMessage());
		}
		message.setText(text);
		sendEmail(message);
	}
	
	
	/**
	 * Based on the inviting user returns only the files that can be shared.
	 * 
	 * @param invitingUser
	 * @param personalFilesToShare
	 * 
	 * @return - list of files that can be shared for the given user.
	 */
	public List<PersonalFile> getOnlyShareableFiles(IrUser invitingUser, Collection<PersonalFile> personalFilesToShare)
	{
		// only add files that the user can share
		List<PersonalFile> actualFilesToShare = new LinkedList<PersonalFile>();
		for( PersonalFile pf : personalFilesToShare)
		{
			IrAcl acl = securityService.getAcl(pf.getVersionedFile(), invitingUser);
			if( acl != null && acl.isGranted(VersionedFile.SHARE_PERMISSION, invitingUser, false))
			{
				actualFilesToShare.add(pf);
			}
		}
		return actualFilesToShare;
	}

	/**
	 * Invite the specified users with the given emails.  This will determine if the emails
	 * already exist for a user in the system.  If the user already exists in the system, the file is
	 * automatically added to the users workspace.  
	 * 
	 * @param emails - list of emails to share with  
	 * @param permissions - permissions to give the files.
	 * @param personalFilesToShare - files to share
	 * @param inviteMessage - message to send to users
	 * 
	 * @return list of emails that were found to be invalid or could not be sent a message
	 * @throws FileSharingException - if the user tries to share with themselves 
	 * @throws PermissionNotGrantedException - if the user does not have permissions to share one or more of the files passed in
	 */
	public List<String> inviteUsers(IrUser invitingUser, 
			List<String> emails, 
			Set<IrClassTypePermission> permissions, 
			List<PersonalFile> personalFilesToShare, 
			String inviteMessage) throws FileSharingException, PermissionNotGrantedException
	{
		List<String> emailsNotSent = new LinkedList<String>();
		// do not let user share with themselves
		for(String email : emails)
		{
			email = email.trim();
			// Check if user exist in the system
			IrUser invitedUser = userService.getUserForVerifiedEmail(email);
			//check if the user is sharing the file with themselves
			if (invitingUser.equals(invitedUser)) {
				throw new FileSharingException("Cannot share with yourself");
			}	
		}
		
		// only add files that the user can share
		for( PersonalFile pf : personalFilesToShare)
		{
			IrAcl acl = securityService.getAcl(pf.getVersionedFile(), invitingUser);
			if( acl == null || !acl.isGranted(VersionedFile.SHARE_PERMISSION, invitingUser, false))
			{
				throw new PermissionNotGrantedException("User " + invitingUser + "does not have permissions to share file " + pf.getVersionedFile());
			}
		}
		
		// only invite users who are not the current user
		for(String email : emails)
		{
			log.debug("checking email " + email);
			IrUser invitedUser = userService.getUserForVerifiedEmail(email);
			Set<VersionedFile>  versionedFiles = new HashSet<VersionedFile>();
			for(PersonalFile pf : personalFilesToShare)
			{
				VersionedFile versionedFile = pf.getVersionedFile();
				
				//check if the file is already shared with this user 
				//AND 
				//check if email is already sent to this Id for sharing and the user has not created the account yet
			    FileCollaborator collaborator = versionedFile.getCollaborator(invitedUser);
			    FileInviteInfo inviteInfo = versionedFile.getInvitee(email);
				if ((collaborator == null) && (inviteInfo == null)) 
				{
					versionedFiles.add(versionedFile);
				}
				// collaborator exists or invite exists - update permissions
				else if( collaborator != null )
				{
			         securityService.updatePermissions(versionedFile, 
	    	 				collaborator.getCollaborator(), permissions);
				}
			}
			
			log.debug("invitied user = " + invitedUser);
			/* If user exist in the system then share the file and send email*/
			if (invitedUser != null && versionedFiles.size() > 0) 
			{
				InviteToken inviteToken = new InviteToken(email, null, invitingUser);
				inviteToken.setInviteMessage(inviteMessage);
				FileInviteInfo inviteInfo = new FileInviteInfo(versionedFiles, null, inviteToken );
				
				
				// If the shared user has no Author or collaborator or researcher or admin role, then assign collaborator role
				if (!invitedUser.hasRole(IrRole.AUTHOR_ROLE) && !invitedUser.hasRole(IrRole.COLLABORATOR_ROLE) 
						&& !invitedUser.hasRole(IrRole.RESEARCHER_ROLE) && !invitedUser.hasRole(IrRole.ADMIN_ROLE)) 
				{
					invitedUser.addRole(roleService.getRole(IrRole.COLLABORATOR_ROLE));
					userService.makeUserPersistent(invitedUser);
				}

				for (VersionedFile file : versionedFiles) {
					try 
					{
						SharedInboxFile sif = shareFile(invitingUser, invitedUser, file);
						userFileSystemService.makeSharedInboxFilePersistent(sif);
						userWorkspaceIndexProcessingRecordService.save(sif.getSharedWithUser().getId(), sif, 
				    	indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
					} 
					catch (FileSharingException e1)
					{
						throw new IllegalStateException("This should never happen", e1);
					}
					
					// Create permissions for the file that is being shared
					securityService.createPermissions(file, invitedUser, permissions);
				}

				try 
				{
					sendEmailToExistingUser(inviteInfo);
				} 
				catch(IllegalStateException e) 
				{
					emailsNotSent.add(email);
				}
			} 
			else if( versionedFiles.size() > 0 )
			{
				/* If user does not exist in the system then get a token and send email with the token */
				InviteToken inviteToken = new InviteToken(email, TokenGenerator.getToken(), invitingUser);
				inviteToken.setInviteMessage(inviteMessage);
				FileInviteInfo inviteInfo = new FileInviteInfo(versionedFiles, permissions, inviteToken );
	
				try 
				{
					sendEmailToNotExistingUser(inviteInfo);
					makeInviteInfoPersistent(inviteInfo);
				} 
				catch(IllegalStateException e) 
				{
					emailsNotSent.add(email);
				}
			}
		}
		return emailsNotSent;
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
	 * Shares the file with the other user
	 * @throws FileSharingException 
	 * 
	 * @see edu.ur.ir.user.InviteUserService#shareFile(IrUser, VersionedFile)
	 */
	public SharedInboxFile shareFile(IrUser invitingUser, 
			IrUser invitedUser, VersionedFile versionedFile) throws FileSharingException 
	{
		
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
	public void unshareFile(FileCollaborator fileCollaborator, IrUser unsharingUser)
	{
		
		IrUser collaborator = fileCollaborator.getCollaborator();
		VersionedFile file = fileCollaborator.getVersionedFile();
		log.debug("UnShare file " + file.toString() + " with user " + collaborator);
		
		// un-index the file
		PersonalFile pf = userFileSystemService.getPersonalFile(collaborator, fileCollaborator.getVersionedFile());

		// Check if personal file exist. Sometimes the file may be still in Shared file inbox.
		// In that case, there is no need to delete from index
		if (pf != null) {
			
			userWorkspaceIndexProcessingRecordService.save(pf.getOwner().getId(), pf, 
	    			indexProcessingTypeService.get(IndexProcessingTypeService.DELETE));
		}
		else
		{
			SharedInboxFile sif = collaborator.getSharedInboxFile(fileCollaborator.getVersionedFile());
			if( sif != null )
			{
				userWorkspaceIndexProcessingRecordService.save(sif.getSharedWithUser().getId(), sif, 
		    			indexProcessingTypeService.get(IndexProcessingTypeService.DELETE));
			}
		}
		
		// Remove Personal file from user if it is in the users files
		PersonalFile personalFile 
		= personalFileDAO.getFileForUserWithSpecifiedVersionedFile(collaborator.getId(), 
					file.getId());
		
		if (personalFile != null) 
		{
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
		
		FileInviteInfo inviteInfo = findInviteInfoByToken(token);
		Set<SharedInboxFile> inboxFiles = new HashSet<SharedInboxFile>();
		
		if (inviteInfo != null) {

			// If the shared user has no Author or collaborator or researcher or admin role, then assign collaborator role
			if (!invitedUser.hasRole(IrRole.AUTHOR_ROLE) && !invitedUser.hasRole(IrRole.COLLABORATOR_ROLE) 
					&& !invitedUser.hasRole(IrRole.RESEARCHER_ROLE) && !invitedUser.hasRole(IrRole.ADMIN_ROLE)) {
				
				invitedUser.addRole(roleService.getRole(IrRole.COLLABORATOR_ROLE));
				userService.makeUserPersistent(invitedUser);
			}
			
			// Add the email to the invited user
			UserEmail userEmail = new UserEmail(inviteInfo.getInviteToken().getEmail());
			userEmail.setVerifiedTrue();
			invitedUser.addUserEmail(userEmail, false);
			userService.makeUserPersistent(invitedUser);
			
			Set<VersionedFile> files = inviteInfo.getFiles();
			
			for(VersionedFile file:files) {
				if (file.getCollaborator(invitedUser) == null) {
	
					// Share the file with invited user
					SharedInboxFile inboxFile = shareFile(inviteInfo.getInviteToken().getInvitingUser(), invitedUser, file);
					inboxFiles.add(inboxFile);
					// create permissions for the shared file
					securityService.createPermissions(file, invitedUser, inviteInfo.getPermissions());
		
				} else {
					log.debug("The file " + file.getName() 
							+ " is already shared with this user:" + invitedUser.getUsername());
				}
			}
			fileInviteInfoDAO.makeTransient(inviteInfo);
		}
		
		return inboxFiles;
	}
	
	

	/**
	 * Share file for user with the specified email. 
	 * When user adds new email, this method shares the files invited with newly added email.
	 * 
	 * @param UserId Id of the invited user
	 * @param email Email used to invite the user
	 * @throws FileSharingException 
	 * 
	 * @throws FileSharingException - if the user tries to share a file with themselves or the email has 
	 * not yet been verified.
	 */
	public Set<SharedInboxFile> sharePendingFilesForEmail(Long userId, String email) throws UnVerifiedEmailException, FileSharingException {
		
		IrUser invitedUser = userService.getUser(userId, true);
		
		UserEmail userEmail = invitedUser.getUserEmail(email);
		if( userEmail == null || !userEmail.isVerified())
		{
			UnVerifiedEmailException e = new UnVerifiedEmailException("user email has not yet been verified");
			errorEmailService.sendError(e);
			throw e;
		}
		
		List<FileInviteInfo> invites = fileInviteInfoDAO.getInviteInfoByEmail(email);
		
		if ( invites.size() > 0) {
			// If the shared user has no Author or collaborator or researcher or admin role, then assign collaborator role
			if (!invitedUser.hasRole(IrRole.AUTHOR_ROLE) && !invitedUser.hasRole(IrRole.COLLABORATOR_ROLE) 
					&& !invitedUser.hasRole(IrRole.RESEARCHER_ROLE) && !invitedUser.hasRole(IrRole.ADMIN_ROLE)) {
				
				invitedUser.addRole(roleService.getRole(IrRole.COLLABORATOR_ROLE));
				userService.makeUserPersistent(invitedUser);
			}			
		}
		
		Set<SharedInboxFile> inboxFiles = new HashSet<SharedInboxFile>();
		
		for (FileInviteInfo inviteInfo: invites) {
			
			if (inviteInfo != null) {
				Set<VersionedFile> files = inviteInfo.getFiles();
				
				
				for(VersionedFile file:files) {
					if (file.getCollaborator(invitedUser) == null) {
		
						// Share the file with invited user
						SharedInboxFile inboxFile = shareFile(inviteInfo.getInviteToken().getInvitingUser(), invitedUser, file);
						inboxFiles.add(inboxFile);
						// create permissions for the shared file
						securityService.createPermissions(file, invitedUser, inviteInfo.getPermissions());
			
					} else {
						log.debug("The file " + file.getName() 
								+ " is already shared with this user:" + invitedUser.getUsername());
					}
				}
				fileInviteInfoDAO.makeTransient(inviteInfo);
			}
		}
		
		List<FolderInviteInfo> folderInvites = folderInviteInfoDAO.getInviteInfoByEmail(email.trim().toLowerCase());
		for( FolderInviteInfo folderInvite : folderInvites)
		{
			if( folderInvite != null )
			{
				// create permissions for the shared file
				Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
				permissions.addAll(folderInvite.getPermissions());
				FolderAutoShareInfo autoShare = new FolderAutoShareInfo(folderInvite.getPersonalFolder(), 
						permissions, invitedUser);
				folderAutoShareInfoDAO.makePersistent(autoShare);
				folderInviteInfoDAO.makeTransient(folderInvite);
			}
		}
		
		return inboxFiles;
	}
	
	/**
	 * Notify users that a new version of a file has been added.
	 * 
	 * @param personalFile - personal file that has been updated.
	 * @param collaboratorIds - list of collaborators to notify
	 * @return - list of collaborators where the email could not be sent
	 */
	public List<IrUser> notifyCollaboratorsOfNewVersion(PersonalFile personalFile, List<Long> collaboratorIds, boolean notifyOwner)
	{
		List<IrUser> emailsNotSent = new LinkedList<IrUser>();
		VersionedFile versionedFile = personalFile.getVersionedFile();
		List<IrUser> users = new LinkedList<IrUser>();
		for(long id : collaboratorIds)
		{	
			FileCollaborator collaborator = versionedFile.getCollaboratorById(id);
	        if( collaborator != null )
	        {
	        	users.add(collaborator.getCollaborator());
	        }
		}
		if( notifyOwner )
		{
			users.add(personalFile.getVersionedFile().getOwner());
		}
		
		for(IrUser user: users)
		{	
		    try 
			{
			    SimpleMailMessage message = new SimpleMailMessage(newFileVersionMailMessage);
				message.setTo(user.getDefaultEmail().getEmail());
				
				String subject = message.getSubject();
				subject = StringUtils.replace(subject, "%FIRST_NAME%", personalFile.getOwner().getFirstName());
				subject = StringUtils.replace(subject, "%LAST_NAME%", personalFile.getOwner().getLastName());
				subject = StringUtils.replace(subject, "%NAME%", personalFile.getName());
				message.setSubject(subject);
				
				String text = message.getText();
				String path = personalFile.getName();
				    
				PersonalFile collabPersonalFile = personalFileDAO.getFileForUserWithSpecifiedVersionedFile(user.getId(), 
								versionedFile.getId());
				    
				// may be null if user has not yet moved the file into their worksapce
				if( collabPersonalFile != null )
				{
				    path = collabPersonalFile.getFullPath();
				}
				// Get the name of files
				text = StringUtils.replace(text, "%NAME%", path);
				text = StringUtils.replace(text, "%BASE_WEB_APP_PATH%", baseWebAppPath);
				
				if( personalFile.getDescription() != null && !personalFile.getDescription().trim().equals(""))
				{
				    text = text + "\n Notes: \n\n" + personalFile.getDescription();
				}
				
				message.setText(text);
				sendEmail(message);
			} 
			catch(IllegalStateException e) 
			{
			    emailsNotSent.add(user);
			}
		}
		return emailsNotSent;
	}
	
	/**
	 * Find invite information by token
	 * 
	 *  @see edu.ur.ir.user.InviteUserService#findInviteInfoByToken(String)
	 */
	public FileInviteInfo findInviteInfoByToken(String token){
		FileInviteInfo inviteUser = fileInviteInfoDAO.findInviteInfoForToken(token);
		return inviteUser;
	}
	
	/**
	 * Get the invite info information by email address.
	 * 
	 * @param email - email to find invite info information for.
	 * @return - all invite infos found
	 */
	public List<FileInviteInfo> findInviteInfoByEmail(String email)
	{
		return fileInviteInfoDAO.getInviteInfoByEmail(email);
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setFileInviteInfoDAO(FileInviteInfoDAO inviteInfoDAO) {
		this.fileInviteInfoDAO = inviteInfoDAO;
	}

	public void setPersonalFileDAO(PersonalFileDAO personalFileDAO) {
		this.personalFileDAO = personalFileDAO;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setVersionedFileDAO(VersionedFileDAO versionedFileDAO) {
		this.versionedFileDAO = versionedFileDAO;
	}

	public void setFileCollaboratorDAO(FileCollaboratorDAO fileCollaboratorDAO) {
		this.fileCollaboratorDAO = fileCollaboratorDAO;
	}
	
	public FileCollaborator findFileCollaborator(Long fileCollaboratorId, boolean lock) {
		return fileCollaboratorDAO.getById(fileCollaboratorId, lock);
	}
	
	/**
	 * Get the list for file collaborator objects for the user id and versioned file ids.
	 * 
	 * @param userId - id of the user who is a collaborator
	 * @param versionedFileIds - List of versioned files to check for
	 * 
	 * @return the file collaborator.
	 */
	public List<FileCollaborator> getCollaborations(Long userId, List<Long> versionedFileIds)
	{
		return fileCollaboratorDAO.findByUserIdVersionedFileId(userId, versionedFileIds);
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

	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
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
	public FileInviteInfo getInviteInfoById(Long id, boolean lock) {
		return fileInviteInfoDAO.getById(id, lock);
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
	public List<FileInviteInfo> getInviteInfosOrderByInviteor(int rowStart,
			int maxResults, OrderType orderType)
	{
		return fileInviteInfoDAO.getInviteInfosOrderByInviteor(rowStart, maxResults, orderType);
	}
	
	/**
	 * Get a count of invite info objects
	 * 
	 * @return count of invite info objects
	 */
	public Long getInviteInfoCount()
	{
		return fileInviteInfoDAO.getCount();
	}

	/**
	 * Delete the invite info object.
	 * 
	 * @see edu.ur.ir.user.InviteUserService#delete(edu.ur.ir.user.FileInviteInfo)
	 */
	public void delete(FileInviteInfo inviteInfo) {
		fileInviteInfoDAO.makeTransient(inviteInfo);
	}
	
	/**
	 * Will set the personal folder to auto share with the given email.  This
	 * will first check to see if the user already exists in the system.
	 * 
	 * @param email - to share with.
	 * @param personalFolder - personal folder to auto share files when added to.
	 * @param cascade - cascade down to sub folders
	 * @throws FileSharingException - if the user tries sharing with themselves
	 * 
	 * @throws PermissionNotGrantedException 
	 */
	public void autoShareFolder(List<String> emails, PersonalFolder personalFolder, Set<IrClassTypePermission> permissions, boolean cascade) throws FileSharingException
	{
		for(String email : emails)
		{
			IrUser user = userService.getUserByEmail(email);
			if( user != null )
			{
				if( user.equals(personalFolder.getOwner()))
				{
					throw new FileSharingException("Cannot share folder with yourself");
				}
			}
		}
		
		// add the share information to folders
		for( String email : emails)
		{
		    IrUser user = userService.getUserByEmail(email);
		    
		    if( user == null )
		    {
		    	FolderInviteInfo inviteInfo = personalFolder.createInviteInfo(permissions, email);
		        save(inviteInfo);
		    }
		    else
		    {
				 FolderAutoShareInfo shareInfo  = personalFolder.createAutoShareInfo(permissions, user);
				 save(shareInfo);
		    }
		    if( log.isDebugEnabled() )
		    {
		        log.debug("Cascade = " + cascade);
		    }
		    
		    // cascade share permissions to all sub folders 
		    if( cascade )
		    {
			     // take care of folders
		         List<PersonalFolder> folders = userFileSystemService.getAllChildrenForFolder(personalFolder);
		         if( log.isDebugEnabled())
		         {
		        	 log.debug("Number of folders to be processed = " + folders.size());
		        	 log.debug(" user = " + user);
		        	 log.debug(" email = " + email );
		         }
		         for(PersonalFolder f : folders)
		         {
		        	 if( log.isDebugEnabled())
		        	 {
		        	     log.debug("Sharing folder " + f);
		        	 }
		    	     if( user == null )
		    	     {
		    	    	 log.debug("Creating invite for email " + email);
		    	    	 FolderInviteInfo inviteInfo = f.createInviteInfo(permissions, email);
		 		         save(inviteInfo);
		    	     }
		    	     else
		    	     {
		    	    	 log.debug("Creating share for user " + user);
		    		     FolderAutoShareInfo shareInfo  = f.createAutoShareInfo(permissions, user);
					     save(shareInfo);
		    	     }
		         }
		     }
		}
		if( cascade )
		{
			 // take care of files in all folders and sub folders
	         List<PersonalFile> files = getOnlyShareableFiles(personalFolder.getOwner(), userFileSystemService.getAllFilesForFolder(personalFolder));
	         try 
	    	 {
			     inviteUsers(personalFolder.getOwner(), emails, permissions, files, null);
			 } 
	    	 catch (PermissionNotGrantedException e)
	    	 {
				log.error(e);
				errorEmailService.sendError(e);
			 }
	        
		}
		else
		{
			 // only share files in the given folder
			 List<PersonalFile> files = getOnlyShareableFiles(personalFolder.getOwner(), personalFolder.getFiles());
	         try 
	         {
			     inviteUsers(personalFolder.getOwner(), emails, permissions, files, null);
		     } 
	         catch (PermissionNotGrantedException e) 
	         {
			    log.error(e);
			    errorEmailService.sendError(e);
		     }
		}
	}
	
	/**
	 * Will set the personal folder to auto share with the given email.  This
	 * will first check to see if the user already exists in the system.
	 * 
	 * @param email - to share with.
	 * @param personalFolder - personal folder to auto share files when added to.
	 * @param cascade - cascade down to sub folders
	 * @throws FileSharingException - if the user tries sharing with themselves
	 * 
	 * @throws PermissionNotGrantedException 
	 */
	public void updateAutoSharePermissions(FolderAutoShareInfo folderAutoShareInfo, Set<IrClassTypePermission> permissions, 
			boolean cascadeFolders, boolean cascadeFiles)
	{
	    folderAutoShareInfo.changePermissions(permissions);
	    save(folderAutoShareInfo);
		
	    // cascade share permissions to all sub folders 
		if( cascadeFolders )
		{
		    // take care of folders
		    List<PersonalFolder> folders = userFileSystemService.getAllChildrenForFolder(folderAutoShareInfo.getPersonalFolder());
		    if( log.isDebugEnabled())
		    {
		        log.debug("Number of folders to be processed = " + folders.size());
		        log.debug(" user = " + folderAutoShareInfo.getCollaborator());
		    }
		    for(PersonalFolder f : folders)
		    {
		        FolderAutoShareInfo subFolderShare = f.getAutoShareInfo(folderAutoShareInfo.getCollaborator());
		        if(subFolderShare != null )
		        {
		        	subFolderShare.changePermissions(permissions);
		        	save(folderAutoShareInfo);
		        }
		    }
		}
		if( cascadeFiles )
		{
			 List<PersonalFile> files = new LinkedList<PersonalFile>();
			 
			 if( cascadeFolders )
			 {
			     // take care of files in all folders and sub folders
	             files = getOnlyShareableFiles(folderAutoShareInfo.getPersonalFolder().getOwner(), 
	        		 userFileSystemService.getAllFilesForFolder(folderAutoShareInfo.getPersonalFolder()));
			 }
			 else
			 {
				 files = getOnlyShareableFiles(folderAutoShareInfo.getPersonalFolder().getOwner(),
						 folderAutoShareInfo.getPersonalFolder().getFiles());
			 }
	         if( files.size() > 0 )
	         {
	        	 LinkedList<Long> versionedFileIds = new LinkedList<Long>();
	    	     for(PersonalFile f : files )
	    	     {
	    	    	 versionedFileIds.add(f.getVersionedFile().getId());
	    	     }
	    	     List<FileCollaborator> collaboratorRecords = getCollaborations(folderAutoShareInfo.getCollaborator().getId(), versionedFileIds);
	    	     for(FileCollaborator collab : collaboratorRecords)
	    	     {
	    	    	 securityService.updatePermissions(collab.getVersionedFile(), 
	    	 				collab.getCollaborator(), permissions);
	    	     }
	    	     
	         }
		}
	}
	
	/**
	 * Adds permissions to files and folders added to an existing folder.  This uses the parent 
	 * folders invite info and folder information to determine the permissions for the newly added
	 * file and folders
	 * 
	 * @param parentFolder - parent folder the files and folders were added to
	 * @param folders - list of top level folders being added - no children of these folders should be in the list
	 * @param files - list of top level file being added.
	 * 
	 * @throws FileSharingException
	 * @throws PermissionNotGrantedException
	 */
	public void addNewFilesFoldersToFolderWithAutoShare(PersonalFolder parentFolder, 
			List<PersonalFolder> folders, List<PersonalFile> files) throws FileSharingException, 
			PermissionNotGrantedException
	{
		// permissions to share to the files and folders
		Set<FolderAutoShareInfo> shareInfos = parentFolder.getAutoShareInfos();
    	Set<FolderInviteInfo> inviteInfos = parentFolder.getFolderInviteInfos();
    	
    	Set<PersonalFolder> allFoldersToGivePermissions = new HashSet<PersonalFolder>();
    	List<PersonalFile> allFilesToGivePermissions = new LinkedList<PersonalFile>();
    	
    	for(PersonalFile pf : files )
    	{
    		IrAcl acl = securityService.getAcl(pf.getVersionedFile(), parentFolder.getOwner());
			
			// Check if the user has SHARE permission for the file
			if (acl.isGranted(VersionedFile.SHARE_PERMISSION, parentFolder.getOwner(), false)) {
				allFilesToGivePermissions.add(pf);
			}
    	}
    	
    	// flatten out the list for folders, sub-folders and files
    	for( PersonalFolder pf : folders )
    	{
    		allFoldersToGivePermissions.add(pf);
    		allFoldersToGivePermissions.addAll(userFileSystemService.getAllChildrenForFolder(pf));
    		allFilesToGivePermissions.addAll(getOnlyShareableFiles(parentFolder.getOwner(), 
	        		 userFileSystemService.getAllFilesForFolder(pf)));
    	}
    	
    	// assign auto share information to folders
    	for(PersonalFolder f : allFoldersToGivePermissions)
        {
    		for(FolderAutoShareInfo shareInfo : shareInfos)
    		{
    			Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
        		permissions.addAll(shareInfo.getPermissions());
    			FolderAutoShareInfo newShareInfo  = f.createAutoShareInfo(permissions, shareInfo.getCollaborator());
			    save(newShareInfo);
    		}
    		
    		for(FolderInviteInfo inviteInfo : inviteInfos)
    		{
    			Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
        		permissions.addAll(inviteInfo.getPermissions());
    			FolderInviteInfo newInviteInfo  = f.createInviteInfo(permissions, inviteInfo.getEmail());
			    save(newInviteInfo);
    		}
        }
    	
    	// send out emails for new files and invites for newly added files
    	for(FolderAutoShareInfo shareInfo : shareInfos)
		{
    		 LinkedList<String> emails = new LinkedList<String>();
    		 emails.add(shareInfo.getCollaborator().getDefaultEmail().getEmail());
    		 Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
    		 permissions.addAll(shareInfo.getPermissions());
    		 inviteUsers(parentFolder.getOwner(), emails, permissions, allFilesToGivePermissions , null);
		}
    	
    	for(FolderInviteInfo inviteInfo : inviteInfos)
		{
    		LinkedList<String> emails = new LinkedList<String>();
   		    emails.add(inviteInfo.getEmail());
   		    Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
   		    permissions.addAll(inviteInfo.getPermissions());
   		    inviteUsers(parentFolder.getOwner(), emails, permissions,allFilesToGivePermissions , null);
		}
    	
	}
	

	
	/**
	 * Get the invites made by a particular user.
	 * 
	 * @param user - invites made by a given user
	 * @return - all invites made by the user or an empty list if no invites found
	 */
	public List<FileInviteInfo> getInvitesMadeByUser(IrUser user)
	{
		return fileInviteInfoDAO.getInvitesMadeByUser(user);
	}

	/**
	 * Delete the specified folder invite information.
	 * 
	 * @see edu.ur.ir.user.InviteUserService#delete(edu.ur.ir.user.FolderInviteInfo)
	 */
	public void delete(FolderInviteInfo inviteInfo, boolean cascadeToSubFolders, boolean cascadeToFiles) {
		
		PersonalFolder personalFolder = inviteInfo.getPersonalFolder();
		personalFolder.removeFolderInviteInfo(inviteInfo);
		folderInviteInfoDAO.makeTransient(inviteInfo);
		if( cascadeToSubFolders )
		{
		    List<PersonalFolder> folders = userFileSystemService.getAllChildrenForFolder(personalFolder);
			for( PersonalFolder aFolder : folders)
			{
			    FolderInviteInfo info = aFolder.getFolderInviteInfo(inviteInfo.getEmail());
				if( info != null )
				{
					aFolder.removeFolderInviteInfo(info);
				    folderInviteInfoDAO.makeTransient(info);
				}
			}
		}
		
		if( cascadeToFiles )
		{
			List<PersonalFile> files = new LinkedList<PersonalFile>();
			
			if( cascadeToSubFolders )
			{
			    // take care of files in all folders and sub folders
	            files = getOnlyShareableFiles(personalFolder.getOwner(), 
	        		 userFileSystemService.getAllFilesForFolder(personalFolder));
			}
			else
			{
				files = getOnlyShareableFiles(personalFolder.getOwner(), 
		        		 personalFolder.getFiles());
			}
	        log.debug("cascade to file, files size = " + files.size());
	        if( files.size() > 0 )
	        {
	        	 LinkedList<Long> versionedFileIds = new LinkedList<Long>();
	    	     for(PersonalFile f : files )
	    	     {
	    	    	 log.debug("adding versioned file id " + f.getVersionedFile().getId());
	    	    	 versionedFileIds.add(f.getVersionedFile().getId());
	    	     }
	    	     List<FileInviteInfo> infos = fileInviteInfoDAO.getInviteInfosWithVersionedFilesAndEmail(versionedFileIds, inviteInfo.getEmail());
	    	     
	    	     log.debug( " found invite infos with size " + infos.size());
	    	     for(FileInviteInfo fileInvite : infos)
	    	     {
	    	    	 List<VersionedFile> inviteFiles = new LinkedList<VersionedFile>();
	    	    	 inviteFiles.addAll(fileInvite.getFiles());
	    	    	 
	    	    	 log.debug(" added invite files " + inviteFiles.size());
	    	    	 // check each file if it is in the list of files
	    	    	 // to be unshared remove it
	    	    	 for(VersionedFile f : inviteFiles)
	    	    	 {
	    	    		 if( versionedFileIds.contains(f.getId()))
	    	    		 {
	    	    			 log.debug(" removing file  " + f);
	    	    			 fileInvite.removeFile(f);
	    	    		 }
	    	    	 }
	    	    	 
	    	    	 // delete the invite if it doesn't have any files
	    	    	 if(fileInvite.getFiles().size() <= 0 )
	    	    	 {
	    	    		 fileInviteInfoDAO.makeTransient(fileInvite);
	    	    	 }
	    	    	 else
	    	    	 {
	    	    		 fileInviteInfoDAO.makePersistent(fileInvite);
	    	    	 }
	    	    	 
	    	     }
	    	     
	         }
		}
		
	}

	/**
	 * Save the specified folder information.
	 * 
	 * @see edu.ur.ir.user.InviteUserService#save(edu.ur.ir.user.FolderInviteInfo)
	 */
	public void save(FolderInviteInfo inviteInfo) {
		folderInviteInfoDAO.makePersistent(inviteInfo);
	}
	
	/**
	 * Set the folder invite information.
	 * 
	 * @param folderInviteInfoDAO
	 */
	public void setFolderInviteInfoDAO(FolderInviteInfoDAO folderInviteInfoDAO) {
		this.folderInviteInfoDAO = folderInviteInfoDAO;
	}

	/**
	 * Set the folder auto share information.
	 * 
	 * @param folderAutoShareInfoDAO
	 */
	public void setFolderAutoShareInfoDAO(
			FolderAutoShareInfoDAO folderAutoShareInfoDAO) {
		this.folderAutoShareInfoDAO = folderAutoShareInfoDAO;
	}

	/**
	 * Save the folder auto share info.
	 * 
	 * @param autoShareInfo - folder auto share information
	 */
	public void save(FolderAutoShareInfo autoShareInfo)
	{
		folderAutoShareInfoDAO.makePersistent(autoShareInfo);
	}
	
	/**
	 * Delete the folder auto share information.
	 * 
	 * @param autoShareInfo - folder auto share information
	 */
	public void delete(IrUser unAutoShareUser, FolderAutoShareInfo folderAutoShareInfo, boolean cascadeToSubFolders, boolean cascadeToFiles)
	{
		
		PersonalFolder personalFolder = folderAutoShareInfo.getPersonalFolder();
		personalFolder.removeAutoShareInfo(folderAutoShareInfo);
		folderAutoShareInfoDAO.makeTransient(folderAutoShareInfo);
		if( cascadeToSubFolders )
		{
		    List<PersonalFolder> folders = userFileSystemService.getAllChildrenForFolder(personalFolder);
			for( PersonalFolder aFolder : folders)
			{
			    FolderAutoShareInfo info = aFolder.getAutoShareInfo(folderAutoShareInfo.getCollaborator());
				if( info != null )
				{
					aFolder.removeAutoShareInfo(info);
				    folderAutoShareInfoDAO.makeTransient(info);
				}
			}
		}
		
		if( cascadeToFiles )
		{
			List<PersonalFile> files = new LinkedList<PersonalFile>();
			if( cascadeToSubFolders ){
			    // take care of files in all folders and sub folders
	            files = getOnlyShareableFiles(folderAutoShareInfo.getPersonalFolder().getOwner(), 
	        		 userFileSystemService.getAllFilesForFolder(folderAutoShareInfo.getPersonalFolder()));
			}
			else
			{
			    files = getOnlyShareableFiles(folderAutoShareInfo.getPersonalFolder().getOwner(),
						folderAutoShareInfo.getPersonalFolder().getFiles());
			}
	        if( files.size() > 0 )
	        {
	        	 LinkedList<Long> versionedFileIds = new LinkedList<Long>();
	    	     for(PersonalFile f : files )
	    	     {
	    	    	 versionedFileIds.add(f.getVersionedFile().getId());
	    	     }
	    	     List<FileCollaborator> collaboratorRecords = getCollaborations(folderAutoShareInfo.getCollaborator().getId(), versionedFileIds);
	    	     for(FileCollaborator collab : collaboratorRecords)
	    	     {
	    	    	 unshareFile(collab, unAutoShareUser);
	    	     }
	         }
		}
		
	}
	
	/**
	 * Set the email error service.
	 * 
	 * @param errorEmailService
	 */
	public void setErrorEmailService(ErrorEmailService errorEmailService) {
		this.errorEmailService = errorEmailService;
	}
	
	/**
	 * Get invite information by email.
	 * 
	 * @param email - email to get the invite information for
	 * @return the invite information
	 */
	public List<FileInviteInfo> getInviteInfo(String email)
	{
		return fileInviteInfoDAO.getInviteInfoByEmail(email);
	}
	
	/**
	 * Set the user workspace index processing record service.
	 * 
	 * @param userWorkspaceIndexProcessingRecordService
	 */
	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}

	/**
	 * Set the index processing type service.
	 * 
	 * @param indexProcessingTypeService
	 */
	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

	/**
	 * Get the folder invite info by id.
	 * 
	 * @param id - id of the folder invite info
	 * @param lock - upgrade the lock mode.
	 * 
	 * @return - the folder invite info if found.
	 */
	public FolderInviteInfo getFolderInviteInfoById(Long id, boolean lock) {
		return this.folderInviteInfoDAO.getById(id, lock);
	}
	
	/**
	 * Get the folder auto share info by id.
	 * 
	 * @param id - id of the folder auto share info
	 * @param lock - upgrade the lock mode.
	 * 
	 * @return - the folder auto share info if found.
	 */
	public FolderAutoShareInfo getFolderAutoShareInfoById(Long id, boolean lock)
	{
		return this.folderAutoShareInfoDAO.getById(id, lock);
	}
	
	
	/**
	 * Set the new file version mail message.
	 * 
	 * @param newFileVersionMailMessage
	 */
	public void setNewFileVersionMailMessage(
			SimpleMailMessage newFileVersionMailMessage) {
		this.newFileVersionMailMessage = newFileVersionMailMessage;
	}
	
    /**
     * Get all folder auto shares made to a given user.
     * 
     * @param user - user who was auto shared with
     * @return - list of all folder auto share infos.
     */
    public List<FolderAutoShareInfo> getAllAutoSharesForUser(IrUser user)
    {
    	return folderAutoShareInfoDAO.getAllAutoSharesForUser(user);
    }
}
