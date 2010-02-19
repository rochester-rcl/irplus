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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.providers.encoding.MessageDigestPasswordEncoder;
import org.springframework.util.StringUtils;

import edu.ur.ir.file.FileCollaborator;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.institution.InstitutionalCollectionSubscription;
import edu.ur.ir.institution.InstitutionalCollectionSubscriptionService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.Affiliation;
import edu.ur.ir.user.ExternalAccountType;
import edu.ur.ir.user.ExternalUserAccount;
import edu.ur.ir.user.ExternalUserAccountDAO;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFileDeleteRecord;
import edu.ur.ir.user.PersonalFileDeleteRecordDAO;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.PersonalItemDeleteRecord;
import edu.ur.ir.user.PersonalItemDeleteRecordDAO;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserEmailDAO;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserGroupService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.order.OrderType;
import edu.ur.util.TokenGenerator;

/**
 * Services for dealing with user related information.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultUserService implements UserService {
	
	/**  Encoder for passwords.  */
	private MessageDigestPasswordEncoder passwordEncoder;
	
	/**  User data access  */
	private IrUserDAO irUserDAO;
	
	/** Data access for user emails */
	private UserEmailDAO userEmailDAO;

	/** Service class for dealing with the repository  */
	private RepositoryService repositoryService;

	/** Service class for dealing with the (A)cess (C)ontrol (L)ists */
	private SecurityService securityService;

	/** Service class for dealing with file sharing*/
	private InviteUserService inviteUserService;
	
	/** Service for dealing with user file system. */
	private UserFileSystemService userFileSystemService;
	
	/** Service for dealing with user file system. */
	private UserPublishingFileSystemService userPublishingFileSystemService;

	/** Researcher Service */
	private ResearcherService researcherService;
		
	/* Mail sender */
	private MailSender mailSender;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultUserService.class);
	
	/** Mail message to change password */
	private SimpleMailMessage passwordTokenMessage;	
	
	/** Mail message for account creation */
	private SimpleMailMessage accountCreationMessage;
	
	/** Mail message for reset password */
	private SimpleMailMessage resetPasswordMessage;
	
	/** Mail message for user affiliation confirmation */
	private SimpleMailMessage userAffiliationMessage;
	
	/** Mail message for admin to verify the user affiliation */
	private SimpleMailMessage affiliationVerificationMessage;
	
	/** Mail message for user with pending affiliation approval*/
	private SimpleMailMessage userPendingAffiliationMessage;
	
	/** Mail message for user registration */
	private SimpleMailMessage accountVerificationMessage;
	
	/** Mail message for email verification */
	private SimpleMailMessage emailVerificationMessage;

	/** Base path for the web app  */
	private String baseWebAppPath;
	
	/** Service for dealing with items. */
	private ItemService itemService;
	
	/** Service for dealing with institutional items. */
	private InstitutionalItemService institutionalItemService;
	
	/** service for dealing with subscriptions */
	private InstitutionalCollectionSubscriptionService institutionalCollectionSubscriptionService;
	
	/** Service for dealing with user group information */
	private UserGroupService userGroupService;
	
	/** captures information about deleting information */
	private PersonalItemDeleteRecordDAO personalItemDeleteRecordDAO;
	
	/** captures information about deleting information */
	private PersonalFileDeleteRecordDAO personalFileDeleteRecordDAO;
	
	/** data access for external user accounts */
	private ExternalUserAccountDAO externalUserAccountDAO;

	/**
	 * Get the User email if email id exists in the system.
	 * 
	 * @see edu.ur.ir.user.InviteUserService#getUserByEmail(String email)
	 */
	public IrUser getUserByEmail(String email) {
		IrUser user = null;
		
		UserEmail userEmail = userEmailDAO.getUserByEmail(email);
		
		if (userEmail != null) {
			user = userEmail.getIrUser();
		}
		return user; 
	}
	
	/**
	 * Get the User email if verified email id exists in the system.
	 * 
	 * @see edu.ur.ir.user.InviteUserService#getUserForVerifiedEmail(String email)
	 */
	public IrUser getUserForVerifiedEmail(String email) {
		IrUser user = null;
		
		UserEmail userEmail = userEmailDAO.getUserByEmail(email);
		
		if ((userEmail != null) &&(userEmail.isVerified())) {
			user = userEmail.getIrUser();
		}
		return user; 
	}
	
	/**
	 * Get the User email if email  exists in the system.
	 * 
	 * @see edu.ur.ir.user.InviteUserService#getUserByEmail(String email)
	 */
	public UserEmail getUserEmailByEmail(String email) {
		
		UserEmail userEmail = userEmailDAO.getUserByEmail(email);
		
		return userEmail; 
	}

	/**
	 * Encode the specified password.
	 * 
	 * @see edu.ur.ir.user.UserService#encodePassword(java.lang.String)
	 */
	public String encodePassword(String password) {
		return passwordEncoder.encodePassword(password, null);
	}

	/**
	 * Get all users 
	 * 
	 * @see edu.ur.ir.user.UserService#getAllUsers()
	 */
	@SuppressWarnings("unchecked")
	public List<IrUser> getAllUsers() {
		return irUserDAO.getAll();
	}

	/**
	 * Get all user in name order.
	 * 
	 * @see edu.ur.ir.user.UserService#getAllUsersNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<IrUser> getAllUsersNameOrder() {
		return irUserDAO.getAllNameOrder();
	}

	/**
	 * Get all users ordered by name.
	 * 
	 * @see edu.ur.ir.user.UserService#getAllUsersOrderByName(int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<IrUser> getAllUsersOrderByName(int startRecord, int numRecords) {
		return irUserDAO.getAllOrderByName(startRecord, numRecords);
	}

	/**
	 * Get the password encoding used by this service.
	 * 
	 * @see edu.ur.ir.user.UserService#getPasswordEncoding()
	 */
	public String getPasswordEncoding() {
		return passwordEncoder.getAlgorithm();
	}


	/**
	 * Get the user with the specified id.
	 * 
	 * @see edu.ur.ir.user.UserService#getUser(java.lang.Long, boolean)
	 */
	public IrUser getUser(Long id, boolean lock) {
		return irUserDAO.getById(id, lock);
	}

	/**
	 * Get a count of the of the users in the system.
	 * 
	 * @see edu.ur.ir.user.UserService#getUserCount()
	 */
	public Long getUserCount() {
		return irUserDAO.getCount();
	}


	/**
	 * Load a user by user name.
	 * 
	 * @see edu.ur.ir.user.UserService#loadUserByUsername(java.lang.String)
	 */
	public IrUser loadUserByUsername(String username) {
		return (IrUser) irUserDAO.loadUserByUsername(username);
	}

	/**
	 * Save the user 
	 * 
	 * @see edu.ur.ir.user.UserService#makeUserPersistent(edu.ur.ir.user.IrUser)
	 */
	public void makeUserPersistent(IrUser entity) {
		irUserDAO.makePersistent(entity);
	}

	/**
	 * Delete a user and all related information.  This will not
	 * delete the user if they have published into the system.
	 * 
	 * @throws UserHasPublishedDeleteException 
	 * @throws UserDeletedPublicationException
	 * @throws IOException 
	 * 
	 * @see edu.ur.ir.user.service.UserService#deleteUser(java.lang.Long)
	 */
	public boolean deleteUser(IrUser user, IrUser deletingUser) throws UserHasPublishedDeleteException, UserDeletedPublicationException
	{
		
		//Get all the generic items created by this user so they
		// can be deleted
		// Get all items owned by this user
		List<GenericItem> items = 
			itemService.getAllItemsForUser(irUserDAO.getById(user.getId(), false));
		
		
		//now delete all the generic items for this user that have not been published
		// Delete  items
		for( GenericItem item: items)
		{
			// If item is not published, then throw an exception - this user cannot be deleted
			if (item.isPublishedToSystem()) {
				throw new UserHasPublishedDeleteException(user);
			}
		}
		
		if( user.getResearcher() != null)
		{
			Researcher r = user.getResearcher();
			researcherService.deleteResearcher(r);
		}
		
		List<InstitutionalCollectionSubscription> subscriptions = institutionalCollectionSubscriptionService.getAllSubscriptionsForUser(user);
		
		for(InstitutionalCollectionSubscription subscription : subscriptions)
		{
			institutionalCollectionSubscriptionService.delete(subscription);
		}
		

		//remove the user from all groups
	    List<IrUserGroup> groups = userGroupService.getUserGroupsForUser(user.getId());
	    for(IrUserGroup group : groups)
	    {
	    	group.removeUser(user);
	    	userGroupService.save(group);
	    }
	    
 	
		Long countOfDeletedItems = institutionalItemService.getDeletedInstitutionalItemCountForUser(user.getId());
		
		if (countOfDeletedItems != 0 ) {
			throw new UserDeletedPublicationException(user);
		}
		
		//remove all the root personal items
		Set<PersonalItem> personalItems = new HashSet<PersonalItem>();
		personalItems.addAll(user.getRootPersonalItems());
		
		for( PersonalItem pi : personalItems )
		{
			user.removeRootPersonalItem(pi);
			userPublishingFileSystemService.deletePersonalItem(pi, deletingUser, "USER BEING DELETED");
		}
		

		
		
		//delete all the personal collections for the user
		//this should cascade down to item version
		Set<PersonalCollection> personalCollections = new HashSet<PersonalCollection>();
		personalCollections.addAll(user.getRootPersonalCollections());
		for( PersonalCollection pc : personalCollections )
		{
			//set delete records for all items
			List<PersonalItem> collectionItems = userPublishingFileSystemService.getAllItemsForCollection(pc);
			
			for(PersonalItem personalItem : collectionItems)
			{
				PersonalItemDeleteRecord personalItemDeleteRecord = new PersonalItemDeleteRecord(deletingUser.getId(),
						personalItem.getId(),
						personalItem.getFullPath(), 
						personalItem.getDescription());
				personalItemDeleteRecord.setDeleteReason("DELETING USER");
				personalItemDeleteRecordDAO.makePersistent(personalItemDeleteRecord);
			}
			
			user.removeRootPersonalCollection(pc);
		}
		
		// delete all versioned items
		List<VersionedItem> versionedItems = itemService.getAllVersionedItemsForUser(user); 
		for(VersionedItem item : versionedItems)
		{
			itemService.deleteVersionedItem(item);
		}

		List<VersionedFile> versionedFiles = new LinkedList<VersionedFile>();

		// delete the users root files
		Set<PersonalFile> personalFiles = new HashSet<PersonalFile>();
		personalFiles.addAll(user.getRootFiles());
		for(PersonalFile pf : personalFiles)
		{
		    versionedFiles.add(pf.getVersionedFile());
	        user.removeRootFile(pf);
		}
		
		// delete the users shared inbox files
		Set<SharedInboxFile> sharedInboxFiles = new HashSet<SharedInboxFile>();
		sharedInboxFiles.addAll(user.getSharedInboxFiles());
		for(SharedInboxFile  sif : sharedInboxFiles)
		{
			versionedFiles.add(sif.getVersionedFile());
			user.removeFromSharedFileInbox(sif);
		}
		
		Set<PersonalFolder> rootFolders = new HashSet<PersonalFolder>();
		rootFolders.addAll(user.getRootFolders());
		// get all of the users root folders
		for(PersonalFolder rootFolder : rootFolders)
		{
			//set delete records for all items
			List<PersonalFile> folderFiles = userFileSystemService.getAllFilesForFolder(rootFolder);
			
			for(PersonalFile folderFile : folderFiles)
			{
				PersonalFileDeleteRecord personalFileDeleteRecord = new PersonalFileDeleteRecord(deletingUser.getId(),
						folderFile.getId(),
						folderFile.getFullPath(), 
						folderFile.getDescription());
				personalFileDeleteRecord.setDeleteReason("DELETING USER");
				personalFileDeleteRecordDAO.makePersistent(personalFileDeleteRecord);
			}
			
			versionedFiles.addAll(userFileSystemService.getAllVersionedFilesForFolder(rootFolder));
		    user.removeRootFolder(rootFolder);
		}
		
		// Delete acl and collaborators for versioned files 
		for( VersionedFile aFile : versionedFiles)
		{	
			 //Delete versioned file only if the file owner is the user that is being deleted.
			 //Otherwise just unshare the file for this user
			 
			if (aFile.getOwner().equals(user)) {
				for(FileCollaborator collaborator : aFile.getCollaborators())
				{
				    inviteUserService.unshareFile(collaborator, deletingUser);
				}
			    userFileSystemService.deleteAclForVersionedFile(aFile, user);
			    repositoryService.deleteVersionedFile(aFile);
			} else {
				// un-share the file that is shared with this user
				FileCollaborator collaborator = aFile.getCollaborator(user);
				if( collaborator != null)
				{
				    inviteUserService.unshareFile(collaborator, deletingUser);
				}
			}
		}
		
		try {
			userFileSystemService.deleteIndexFolder(user);
		} catch (IOException e) {
		    log.error(e);
		}
		irUserDAO.makeTransient(user);
		return true;
	}

	/**
	 * Get the password encoder.
	 * 
	 * @return
	 */
	public MessageDigestPasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	/**
	 * Set the password encoder.
	 * 
	 * @param encoder
	 */
	public void setPasswordEncoder(MessageDigestPasswordEncoder encoder) {
		this.passwordEncoder = encoder;
	}


	/**
	 * Get the user data access object.
	 * 
	 * @return
	 */
	public IrUserDAO getIrUserDAO() {
		return irUserDAO;
	}

	/**
	 * Set the user data access object.
	 * 
	 * @param irUserDAO
	 */
	public void setIrUserDAO(IrUserDAO irUserDAO) {
		this.irUserDAO = irUserDAO;
	}



	/**
	 * Get the user by username.
	 * 
	 * @see edu.ur.ir.user.UserService#getUser(java.lang.String)
	 */
	public IrUser getUser(String username) {
		return irUserDAO.findByUniqueName(username);
	}


	/**
	 * Creat a user.
	 * 
	 * @see edu.ur.ir.user.service.UserService#createUser(java.lang.String, java.lang.String)
	 */
	public IrUser createUser(String password, String username, UserEmail email) 
	{
		IrUser user = new IrUser();
		user.setPassword(passwordEncoder.encodePassword(password, null));
		user.setUsername(username);
		
		user.addUserEmail(email, true);
		
		user.setPasswordEncoding(passwordEncoder.getAlgorithm());
		user.setAccountExpired(false);
		user.setAccountLocked(false);
		user.setCredentialsExpired(false);
		makeUserPersistent(user);
		return user;
	}

	/**
	 * Update a users password.
	 * 
	 * @see edu.ur.ir.user.service.UserService#updatePassword(java.lang.String, edu.ur.ir.user.IrUser)
	 */
	public void updatePassword(String password, IrUser user) {
		user.setPassword(passwordEncoder.encodePassword(password, null));
		user.setPasswordEncoding(passwordEncoder.getAlgorithm());
		makeUserPersistent(user);
	}

	/**
	 * Get repository service used for file management.
	 * 
	 * @return
	 */
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	/**
	 * Set the repository service.
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	

	/**
	 * Get a list of users with the given set of ids.
	 * 
	 * @see edu.ur.ir.user.UserService#getUsers(java.util.List)
	 */
	public List<IrUser> getUsers(List<Long> userIds) {
		return irUserDAO.getUsers(userIds);
	}

	/**
	 * Get a list of emails with the given set of ids.
	 * 
	 * @see edu.ur.ir.user.UserService#getEmails(java.util.List)
	 */
	public List<UserEmail> getEmails(List<Long> emailIds) {
		return userEmailDAO.getEmails(emailIds);
	}
	
	/**
	 * Get a email with the given id.
	 * 
	 * @see edu.ur.ir.user.UserService#getEmail(Long, boolean)
	 */
	public UserEmail getEmail(Long emailId, boolean lock) {
		return userEmailDAO.getById(emailId, lock);
	}
	
	/**
	 * Generates password token and send email to the user
	 * 
	 * @see edu.ur.ir.user.UserService#savePasswordToken(IrUser)
	 */
	public String savePasswordToken(IrUser user) {
		String token = TokenGenerator.getToken();
		user.setPasswordToken(token);
		irUserDAO.makePersistent(user);
		
		return token;
		
	}
	
	/**
	 * Sends email with the token to change password
	 * 
	 * @see edu.ur.ir.user.UserService#sendEmailForForgotPassword(String, String)
	 */
	public void sendEmailForForgotPassword(String token, String email) {

		SimpleMailMessage message = new SimpleMailMessage(passwordTokenMessage);
		message.setTo(email);
		String text = message.getText();
		text = StringUtils.replace(text, "%TOKEN%", token);
		text = StringUtils.replace(text, "%BASE_WEB_APP_PATH%", baseWebAppPath);
		message.setText(text);

		try {
			mailSender.send(message);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * Sends email with the token to verify email
	 * 
	 * @see edu.ur.ir.user.UserService#sendEmailForEmailVerification(String, String, String)
	 */
	public void sendEmailForEmailVerification(String token, String email, String username) {

		SimpleMailMessage message = new SimpleMailMessage(emailVerificationMessage);
		message.setTo(email);
		String text = message.getText();
		text = StringUtils.replace(text, "%TOKEN%", token);
		text = StringUtils.replace(text, "%BASE_WEB_APP_PATH%", baseWebAppPath);
		text = StringUtils.replace(text, "%USERNAME%", username);
		message.setText(text);

		try {
			mailSender.send(message);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new IllegalStateException(e);
		}
	}	
	/**
	 * Get user having the specified token
	 * 
	 * @see edu.ur.ir.user.UserService#getUserByToken(String)
	 */
	public IrUser getUserByToken(String token) {

		return irUserDAO.getUserByToken(token);
	}
	
	/**
	 * Get acl service
	 * 
	 * @return
	 */
	public SecurityService getSecurityService() {
		return securityService;
	}

	/**
	 * Set ACL service
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	/**
	 * Get service to handle inviting a user
	 * 
	 * @return
	 */
	public InviteUserService getInviteUserService() {
		return inviteUserService;
	}

	/**
	 * Set service to handle inviting a user
	 * 
	 * @param inviteUserService
	 */
	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}


	/**
	 * Get the email user data access object.
	 * 
	 * @return
	 */
	public UserEmailDAO getUserEmailDAO() {
		return userEmailDAO;
	}

	/**
	 * Set the user email data access object.
	 * 
	 * @param userEmailDAO
	 */
	public void setUserEmailDAO(UserEmailDAO userEmailDAO) {
		this.userEmailDAO = userEmailDAO;
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
	 * Set the mail sender object.
	 * 
	 * @param mailSender
	 */
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * get the password token message.
	 * 
	 * @return
	 */
	public SimpleMailMessage getPasswordTokenMessage() {
		return passwordTokenMessage;
	}

	/**
	 * Set the password token message.
	 * 
	 * @param passwordTokenMessage
	 */
	public void setPasswordTokenMessage(SimpleMailMessage passwordTokenMessage) {
		this.passwordTokenMessage = passwordTokenMessage;
	}

	/**
	 * Sends email to user when an account is created by admin
	 * 
	 * @see edu.ur.ir.user.service.UserService#sendAccountCreationEmailToUser(IrUser, String)
	 */
	public void sendAccountCreationEmailToUser(IrUser user, String password) {

		SimpleMailMessage message = new SimpleMailMessage(accountCreationMessage);
		message.setTo(user.getDefaultEmail().getEmail());
		String text = message.getText();
		text = StringUtils.replace(text, "%USERNAME%", user.getUsername());
		text = StringUtils.replace(text, "%PASSWORD%", password);
		text = StringUtils.replace(text, "%BASE_WEB_APP_PATH%", baseWebAppPath);
		message.setText(text);

		try {
			mailSender.send(message);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new IllegalStateException(e);
		}

	}

	/**
	 * Sends email to user when the admin resets the password
	 * 
	 * @see edu.ur.ir.user.service.UserService#emailNewPassword(IrUser, String, String)
	 */
	public void emailNewPassword(IrUser user, String password, String emailMessage) {

		SimpleMailMessage message = new SimpleMailMessage(resetPasswordMessage);
		message.setTo(user.getDefaultEmail().getEmail());
		String text = message.getText();
		text = StringUtils.replace(text, "%PASSWORD%", password);
		text = StringUtils.replace(text, "%MESSAGE%", emailMessage);
		message.setText(text);

		try {
			mailSender.send(message);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new IllegalStateException(e);
		}

	}

	/**
	 * Get the account creation message.
	 * 
	 * @return
	 */
	public SimpleMailMessage getAccountCreationMessage() {
		return accountCreationMessage;
	}

	/**
	 * Set the account creation message.
	 * 
	 * @param accountCreationMessage
	 */
	public void setAccountCreationMessage(SimpleMailMessage accountCreationMessage) {
		this.accountCreationMessage = accountCreationMessage;
	}

	/**
	 * Saves email
	 * 
	 * @see edu.ur.ir.user.service.UserService#makeUserEmailPersistent(UserEmail)
	 * 
	 */
	public void makeUserEmailPersistent(UserEmail email) {
		userEmailDAO.makePersistent(email);
	}
	
	
	/**
	 * Get message for emailing user about reseted password
	 *  
	 * @return email message
	 */
	public SimpleMailMessage getResetPasswordMessage() {
		return resetPasswordMessage;
	}

	/**
	 * Set message for emailing user about reseted password
	 * 
	 * @param resetPasswordMessage email message
	 */
	public void setResetPasswordMessage(SimpleMailMessage resetPasswordMessage) 
	{
		this.resetPasswordMessage = resetPasswordMessage;
	}

	/**
	 * Delete email .
	 * 
	 * @see edu.ur.ir.user.UserService#makeEmailTransient(UserEmail)
	 */
	public void makeEmailTransient(UserEmail email) {
		userEmailDAO.makeTransient(email);
	}

	/**
	 * Delete the given list of emails
	 * 
	 * @see edu.ur.ir.user.UserService#deleteEmails(List)
	 */
	public void deleteEmails(List<UserEmail> emails) {
		
		for(UserEmail email:emails) {
			userEmailDAO.makeTransient(email);
		}
	}


	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}
	
	/**
	 * Gets the count of users for whom the affiliation approval is pending
	 * 
 	 * @see edu.ur.ir.user.UserService#getUsersPendingAffiliationApprovalCount()
	 */
	public Long getUsersPendingAffiliationApprovalCount() {
		return irUserDAO.getUsersPendingAffiliationApprovalCount();
	}

	/**
	 * Get the list of users for whom the affiliation approval is pending
	 * 
	 * @see edu.ur.ir.user.UserService#getUsersPendingAffiliationApproval(int, int, String)
	 */
	public List<IrUser> getUsersPendingAffiliationApproval(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return irUserDAO.getUsersPendingAffiliationApprovals(rowStart, numberOfResultsToShow, sortType);
	}

	/**
	 * Sends affiliation confirmation email to user
	 * 
	 * @see edu.ur.ir.user.UserService#sendAffiliationConfirmationEmail(IrUser, Affiliation)
	 */
	public void sendAffiliationConfirmationEmail(IrUser user, Affiliation affiliation) {

		SimpleMailMessage message = new SimpleMailMessage(userAffiliationMessage);
		message.setTo(user.getDefaultEmail().getEmail());
		String text = message.getText();
		text = StringUtils.replace(text, "%USERNAME%", user.getUsername());
		text = StringUtils.replace(text, "%AFFILIATION%", affiliation.getName());
		text = StringUtils.replace(text, "%BASE_WEB_APP_PATH%", baseWebAppPath);
		message.setText(text);

		try {
			mailSender.send(message);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new IllegalStateException(e);
		}
	}

	public SimpleMailMessage getUserAffiliationMessage() {
		return userAffiliationMessage;
	}

	public void setUserAffiliationMessage(SimpleMailMessage userAffiliationMessage) {
		this.userAffiliationMessage = userAffiliationMessage;
	}
	
	/**
	 * Get the user having the specified role
	 * 
	 * @param role role of the user
	 * 
	 * @return List of users with the specified role
	 */
	public List<IrUser> getUserByRole(String roleName) {
		return irUserDAO.getUserByRole(roleName);
	}

	/**
	 * Sends email to admin to verify the affiliation chosen by the user
	 * 
	 * @param user User whose affiliation needs to be verified
	 */
	public void sendAffiliationVerificationEmailForUser(IrUser user) {
		List<IrUser> admins = getUserByRole(IrRole.ADMIN_ROLE);
		
		List<String> toAddresses = new ArrayList<String>();
		
		for (IrUser admin: admins) {
			toAddresses.add((String)(admin.getDefaultEmail().getEmail()));
		}
		
		SimpleMailMessage message = new SimpleMailMessage(affiliationVerificationMessage);
		message.setTo(toAddresses.toArray(new String[0]));
		
		String text = message.getText();
		text = StringUtils.replace(text, "%USERNAME%", user.getUsername());
		text = StringUtils.replace(text, "%AFFILIATION%", user.getAffiliation().getName());
		text = StringUtils.replace(text, "%BASE_WEB_APP_PATH%", baseWebAppPath);
		message.setText(text);

		try {
			mailSender.send(message);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new IllegalStateException(e);
		}
	}

	public SimpleMailMessage getAffiliationVerificationMessage() {
		return affiliationVerificationMessage;
	}

	public void setAffiliationVerificationMessage(
			SimpleMailMessage affiliationVerificationMessage) {
		this.affiliationVerificationMessage = affiliationVerificationMessage;
	}

	/**
	 * Sends email to user with link to login
	 * 
	 * @param user registered user 
	 */
	public void sendAccountVerificationEmailForUser(String token, String email, String username) {
	
		SimpleMailMessage message  = new SimpleMailMessage(accountVerificationMessage);
		message.setTo(email);
		String text = message.getText();
		text = StringUtils.replace(text, "%USERNAME%", username);
		text = StringUtils.replace(text, "%TOKEN%", token);
		text = StringUtils.replace(text, "%BASE_WEB_APP_PATH%", baseWebAppPath);
		message.setText(text);

		try {
			mailSender.send(message);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Sends email to user that the affiliation is currently being verified
	 * 
	 * @param user user 
	 */
	public void sendPendingAffiliationEmailForUser(IrUser user) {

		SimpleMailMessage message = new SimpleMailMessage(userPendingAffiliationMessage);
		message.setTo(user.getDefaultEmail().getEmail());
		String text = message.getText();
		text = StringUtils.replace(text, "%USERNAME%", user.getUsername());
		text = StringUtils.replace(text, "%AFFILIATION%", user.getAffiliation().getName());
		message.setText(text);

		try {
			mailSender.send(message);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new IllegalStateException(e);
		}
	}

	public SimpleMailMessage getUserPendingAffiliationMessage() {
		return userPendingAffiliationMessage;
	}

	public void setUserPendingAffiliationMessage(
			SimpleMailMessage userPendingAffiliationMessage) {
		this.userPendingAffiliationMessage = userPendingAffiliationMessage;
	}

	public SimpleMailMessage getAccountVerificationMessage() {
		return accountVerificationMessage;
	}

	public void setAccountVerificationMessage(
			SimpleMailMessage accountVerificationMessage) {
		this.accountVerificationMessage = accountVerificationMessage;
	}

	public String getBaseWebAppPath() {
		return baseWebAppPath;
	}


	public void setBaseWebAppPath(String baseWebAppPath) {
		this.baseWebAppPath = baseWebAppPath;
	}


	public ResearcherService getResearcherService() {
		return researcherService;
	}


	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}


	public UserPublishingFileSystemService getUserPublishingFileSystemService() {
		return userPublishingFileSystemService;
	}


	public void setUserPublishingFileSystemService(
			UserPublishingFileSystemService userPublishingFileSystemService) {
		this.userPublishingFileSystemService = userPublishingFileSystemService;
	}


	public ItemService getItemService() {
		return itemService;
	}


	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	/**
	 * Get user having the specified person name authority
	 * 
	 * @param personNameAuthorityId Id of person name authority
	 * @return User
	 */
	public IrUser getUserByPersonNameAuthority(Long personNameAuthorityId) {
		return irUserDAO.getUserByPersonNameAuthority(personNameAuthorityId);
	}
	
	
	public SimpleMailMessage getEmailVerificationMessage() {
		return emailVerificationMessage;
	}

	public void setEmailVerificationMessage(
			SimpleMailMessage emailVerificationMessage) {
		this.emailVerificationMessage = emailVerificationMessage;
	}

	public UserEmail getUserEmailByToken(String token) {
		return userEmailDAO.getUserEmailByToken(token);
	}

	
	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	/**
	 * Sort users
	 * 
	 * @see edu.ur.ir.user.UserService#getUsers(int, int, String, String)
	 */
	public List<IrUser> getUsers(int rowStart, 
    		int numberOfResultsToShow, String sortElement, OrderType orderType) {
		return irUserDAO.getUsers(rowStart, 
	    		numberOfResultsToShow, sortElement, orderType);
	}

	public InstitutionalCollectionSubscriptionService getInstitutionalCollectionSubscriptionService() {
		return institutionalCollectionSubscriptionService;
	}

	public void setInstitutionalCollectionSubscriptionService(
			InstitutionalCollectionSubscriptionService institutionalCollectionSubscriptionService) {
		this.institutionalCollectionSubscriptionService = institutionalCollectionSubscriptionService;
	}

	public UserGroupService getUserGroupService() {
		return userGroupService;
	}

	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

	public PersonalItemDeleteRecordDAO getPersonalItemDeleteRecordDAO() {
		return personalItemDeleteRecordDAO;
	}

	public void setPersonalItemDeleteRecordDAO(
			PersonalItemDeleteRecordDAO personalItemDeleteRecordDAO) {
		this.personalItemDeleteRecordDAO = personalItemDeleteRecordDAO;
	}

	public PersonalFileDeleteRecordDAO getPersonalFileDeleteRecordDAO() {
		return personalFileDeleteRecordDAO;
	}

	public void setPersonalFileDeleteRecordDAO(
			PersonalFileDeleteRecordDAO personalFileDeleteRecordDAO) {
		this.personalFileDeleteRecordDAO = personalFileDeleteRecordDAO;
	}

	
	public void delete(ExternalUserAccount externalUserAccount) {
		externalUserAccountDAO.makeTransient(externalUserAccount);
	}

	public ExternalUserAccountDAO getExternalUserAccountDAO() {
		return externalUserAccountDAO;
	}

	public void setExternalUserAccountDAO(
			ExternalUserAccountDAO externalUserAccountDAO) {
		this.externalUserAccountDAO = externalUserAccountDAO;
	}

	
	/**
	 * Get the list of user(s) with the given external user name.
	 * 
	 * @see edu.ur.ir.user.UserService#getByExternalUserName(java.lang.String)
	 */
	public List<ExternalUserAccount> getByExternalUserName(
			String externalUserName) {
		return externalUserAccountDAO.getByExternalUserName(externalUserName);
	}

	
	/**
	 * Save the external user account.
	 * 
	 * @see edu.ur.ir.user.UserService#save(edu.ur.ir.user.ExternalUserAccount)
	 */
	public void save(ExternalUserAccount externalUserAccount) {
		externalUserAccountDAO.makePersistent(externalUserAccount);
	}

	
	/**
	 * Get the external user account by using the external user name and account type.
	 * 
	 * @see edu.ur.ir.user.UserService#getByExternalUserNameAccountType(java.lang.String, edu.ur.ir.user.ExternalAccountType)
	 */
	public ExternalUserAccount getByExternalUserNameAccountType(
			String externalUserName, ExternalAccountType externalAccountType) {
		return externalUserAccountDAO.getByExternalUserNameAccountType(externalUserName, externalAccountType);
	}

}

