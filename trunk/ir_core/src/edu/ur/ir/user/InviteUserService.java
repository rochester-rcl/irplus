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

package edu.ur.ir.user;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import edu.ur.ir.file.FileCollaborator;
import edu.ur.ir.file.VersionedFile;
import edu.ur.order.OrderType;

/**
 * Service interface for inviting a user
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface InviteUserService extends Serializable{

	/** permissions that can be granted on versioned files */
	public static final String VIEW_PERMISSION = "VIEW";
	public static final String EDIT_PERMISSION = "EDIT";
	public static final String SHARE_PERMISSION = "SHARE";

	/**
	 * Persistent method for invite info
	 * 
	 * @param entity invite information
	 */
	public void makeInviteInfoPersistent(InviteInfo entity) ;

	/**
	 * Sends email to user existing in the system for collaborating on a document/file
	 * 
	 * @param InviteInfo invite information containing information to send email
	 */
	public void sendEmailToExistingUser(InviteInfo inviteInfo);
	
	/**
	 * Sends email to user not in the system for collaborating on a document/file
	 * 
	 * @param inviteUser Invite information containing information to send email
	 */
	public void sendEmailToNotExistingUser(InviteInfo inviteInfo);

	/**
	 * Shares the file with the other user
	 * 
	 * @param versionedFile File that has to be shared
	 * @param invitingUser - user who invited the other user to share the file with
	 * @param invitiedUser The file has to be shared with this user
	 * 
	 * @throws FileSharingException if the user tries to share a file with themselves.
	 */
	public SharedInboxFile shareFile(IrUser invitingUser, IrUser invitiedUser, 
			VersionedFile versionedFile) throws FileSharingException;
	
	/**
	 * Removes the specified shared file from the list of users
	 *  
	 * @param fileCollaborator - user to unshare with
	 * @param unshareUser - user who is unsharing the file
	 */
	public void unshareFile(FileCollaborator fileCollaborator, IrUser unshareUser);	
	
	/**
	 * Sends an un-share email.
	 * 
	 * @param fileOwnerEmail - owner of the file
	 * @param collaboratorEmail - collaborator who will be removed from the sharing
	 * @param fileName - file that is no longer shared.
	 */
	public void sendUnshareEmail(String fileOwnerEmail, String collaboratorEmail, String fileName);

	/**
	 * Find the collaborator by unique Id
	 * 
	 * @param fileCollaboratorId Id of file collaborator
	 * @param lock 
	 * @return File collaborator 
	 */
	public FileCollaborator findFileCollaborator(Long fileCollaboratorId, boolean lock);
	
	/**
	 * Find inviting User information by token
	 * 
	 *  @param token Token given for invited user
	 *  @return Inviting user and file details
	 */
	public InviteInfo findInviteInfoByToken(String token);
	
	/**
	 * Share files for user with the specified token 
	 * 
	 * @param UserId Id of the invited user
	 * @param token Token given to the invited user
	 * @return set of files shared with the user
	 * 
	 * @throws FileSharingException if the user tries to share a file with themselves
	 */
	public Set<SharedInboxFile> shareFileForUserWithToken(Long userId, String token) 
	    throws FileSharingException;
	
	/**
	 * Get all invite info information for a sepcified email.
	 * 
	 * @param email - email to get
	 * @return the list of invite info objects
	 * 
	 * @throws FileSharingException if the user tries to share a file with themselves
	 */
	public List<InviteInfo> findInviteInfoByEmail(String email);
	
	/**
	 * Delete the specified invite information.
	 * 
	 * @param inviteInfo - invite info to delete
	 */
	public void delete(InviteInfo inviteInfo);
	
	/**
	 * Save versioned file 
	 * 
	 * @param versionedFile file to be saved
	 */
	public void saveVersionedFile(VersionedFile versionedFile);
	
	/**
	 * Get invite info by id
	 * 
	 * @param id Id of the invite information
	 * @param lock
	 *  
	 * @return Invite information for the sepcified id
	 */
	public InviteInfo getInviteInfoById(Long id, boolean lock);
	

	/**
	 * Share file for user with the specified email. 
	 * When user adds new email, this method shares the files invited with newly added email.
	 * 
	 * @param UserId Id of the invited user
	 * @param email Email used to invite the user
	 * @throws FileSharingException - if the user tries to share a file with themselves
	 */
	public Set<SharedInboxFile> sharePendingFilesForEmail(Long userId, String email) throws FileSharingException ;

	/**
	 * Delete shared inbox file
	 * 
	 * @param inboxFile
	 */
	public void deleteSharedInboxFile(SharedInboxFile inboxFile) ;
	
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
			int maxResults, OrderType orderType);
	
	/**
	 * Get a count of invite info objects
	 * 
	 * @return count of invite info objects
	 */
	public Long getInviteInfoCount();
	
}
