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

import java.io.IOException;
import java.util.List;

import org.springframework.security.userdetails.UserDetailsService;

import edu.ur.order.OrderType;

/**
 * Service interface for accessing user based information.
 * 
 * @author Nathan Sarr
 *
 */
public interface UserService extends UserDetailsService {
	
	/**
	 * Get a count of the users in the system
	 */
	public Long getUserCount();
	
	/**
	 * Get all users in the system in user name order.
	 * 
	 * @return all users in name order
	 */
	public List<IrUser> getAllUsersNameOrder();

	/**
	 * Get all users ordered by name.
	 * 
	 * @param startRecord - start position
	 * @param numRecords - number of records 
	 * 
	 * @return list of users within the specified range.
	 */
	public List<IrUser> getAllUsersOrderByName(int startRecord, int numRecords);

	/**
	 * Load a user by user name.
	 * 
	 * @param username
	 * @return user with the specified user name.
	 */
	public IrUser loadUserByUsername(String username);

	/**
	 * Find all users.
	 * 
	 * @return all users in the system.
	 */
	public List<IrUser> getAllUsers();
	
	/**
	 * Get a user by id.
	 * 
	 * @param id - id of the user.
	 * @param lock - lock mode.
	 * 
	 * @return the user found with the id.
	 */
	public IrUser getUser(Long id, boolean lock);
	
	/**
	 * Get a user by user name.
	 * 
	 * @param name - name of the user.
	 * @return the user or null if no user is found.
	 */
	public IrUser getUser(String username);

	/**
	 * Make a user persistent.
	 * 
	 * @param user
	 */
	public void makeUserPersistent(IrUser entity);

	/**
	 * Delete the user from the system.
	 * 
	 * @param user - user to delete.
	 * @param deletingUser - user performing the delete
	 * 
	 * @throws UserHasPublishedDeleteException - if the user has published into the
	 * system.
	 * @throws UserDeletedPublicationException - if user has deleted publications
	 * @throws IOException 
	 */
	public boolean deleteUser(IrUser user, IrUser deletingUser) throws UserHasPublishedDeleteException, UserDeletedPublicationException;
	
	/**
	 * Get a set of users with the given ids.
	 * 
	 * @param userIds - set of user ids.
	 * 
	 * @return the list of users.
	 */
	public List<IrUser> getUsers(List<Long> userIds);
	
	/**
	 * Create a user in the system with the specified password and username.
	 * 
	 * @param password - password for the user
	 * @param username - username for the user
	 * @param email - default email for the user.
	 * 
	 * @return - Created user.
	 */
	public IrUser createUser(String password, String username, UserEmail email);
	
	/**
	 * Update the user with the specified password.
	 * 
	 * @param password
	 * @param user
	 */
	public void updatePassword(String password, IrUser user);
		
	/**
	 * Encode the password using the encoder.  This can 
	 * be used to compare passwords to see if
	 * when encoded they are the same.
	 * 
	 * @param password
	 * @return the encoded value.
	 */
	public String encodePassword(String password);
	
	
	/**
	 * Return how the password will be encoded.
	 * 
	 * @return encoding used.
	 */
	public String getPasswordEncoding();
	
	
	/**
	 * Get the User having this email
	 * 
	 * @param email Email to verify
	 * @return User if email exists in the system else return null
	 */
	public IrUser getUserByEmail(String email);	

	/**
	 * Get the User email if verified email id exists in the system.
	 * 
	 * @param email Email to verify
	 * @return User if email exists in the system else return null
	 */
	public IrUser getUserForVerifiedEmail(String email);
	
	/**
	 * Generates password token and saves it to the user
	 * 
	 * @param user user who forgot the password
	 * 
	 * @return password token
	 */
	public String savePasswordToken(IrUser user);
	
	/**
	 * Sends email with the token to change password
	 * 
	 * @param token password token
	 * @param email Email id to send email
	 */
	public void sendEmailForForgotPassword(String token, String email);

	/**
	 * Get user having the specified token
	 * 
	 * @param token Token given to user to change password
	 */
	public IrUser getUserByToken(String token);

	/**
	 * Sends email to user when an account is created by admin
	 * 
	 * @param user user for whom the account is created
	 * @param password password for the user
	 */
	public void sendAccountCreationEmailToUser(IrUser user, String password);

	/**
	 * Saves email
	 * 
	 * @param email email address
	 * 
	 */
	public void makeUserEmailPersistent(UserEmail email);
	

	/**
	 * Sends email to user when the admin resets the password
	 * 
	 * @param user User to send the message
	 * @param password new password for the user
	 * @param emailMessage Email message
	 */
	public void emailNewPassword(IrUser user, String password, String emailMessage);
	

	/**
	 * Get a set of emails with the given ids.
	 * 
	 * @param emailIds - set of email ids.
	 * 
	 * @return the list of emails.
	 */

	public List<UserEmail> getEmails(List<Long> emailIds) ;
	
	/**
	 * Get a email with the given id.
	 * 
	 * @param id - id of the email.
	 * @param lock - lock mode.
	 * 
	 * @return the email found with the id.
	 */
	public UserEmail getEmail(Long id, boolean lock);

	/**
	 * Delete email .
	 * 
	 * @param email - email to be deleted
	 */
	public void makeEmailTransient(UserEmail email);

	/**
	 * Delete the given list of emails
	 * 
	 * @param emails list of emails to be deleted
	 */
	public void deleteEmails(List<UserEmail> emails);

	/**
     * Get a count of users having pending approval based on the given criteria.
     * 
     * @return - the number of users found
     */ 
	public Long getUsersPendingAffiliationApprovalCount() ;

	/**
	 * Gets the users for whom the affiliation approval is pending
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param sortType - sort order(Asc/desc)
	 * 
	 * @return List of users for the specified information.
	 */
	public List<IrUser> getUsersPendingAffiliationApproval(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);
	
	/**
	 * Sends affiliation confirmation email to user
	 * 
	 * @param user User form whom the affiliation confirmation email has to be sent
	 * @param affiliation Affiliation given to the user
	 * 
	 */
	public void sendAffiliationConfirmationEmail(IrUser user, Affiliation affiliation) ;

	/**
	 * Get the user having the specified role
	 * 
	 * @param roleName role name of the user
	 * 
	 * @return List of users with the specified role
	 * 
	 */
	public List<IrUser> getUserByRole(String roleName) ;

	/**
	 * Sends email to admin to verify the affiliation chosen by the user
	 * 
	 * @param user User whose affiliation needs to be verified
	 */
	public void sendAffiliationVerificationEmailForUser(IrUser user);

	/**
	 * Sends email to user with link to login
	 * 
	 * @param user registered user 
	 */
	public void sendAccountVerificationEmailForUser(String token, String email, String username);

	/**
	 * Sends email to user that the affiliation is currently being verified
	 * 
	 * @param user user 
	 */
	public void sendPendingAffiliationEmailForUser(IrUser user);

	/**
	 * Get the User email if email  exists in the system.
	 * 
	 * @param email Email to verify
	 * @return Email if email exists in the system else return null
	 */
	public UserEmail getUserEmailByEmail(String email) ;

	/**
	 * Get user having the specified person name authority
	 * 
	 * @param personNameAuthorityId Id of person name authority
	 * @return User
	 */
	public IrUser getUserByPersonNameAuthority(Long personNameAuthorityId);

	/**
	 * Sends email with the token to verify email
	 * 
	 * @param token Token for email verification
	 * @param email Email address to send the email
	 * @param username User name of the user receiving the email
	 */
	public void sendEmailForEmailVerification(String token, String email, String username);

	/**
	 * Get user email for the specified verification token
	 * 
	 * @param token Token to verify the email
	 * 
	 * @return Email containing the token
	 */
	public UserEmail getUserEmailByToken(String token);

	/**
	 * Get a list of users for a specified sort criteria.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param numberOfResultsToShow - maximum number of results to fetch
	 * @param sortElement - column to sort on 
	 * @param sortType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<IrUser> getUsers(int rowStart, 
    		int numberOfResultsToShow, String sortElement, OrderType orderType);
	
	
	/**
	 * Delete the specified user account.
	 * 
	 * @param externalUserAccount
	 */
	public void delete(ExternalUserAccount externalUserAccount);
	
	/**
	 * Save the external user account.
	 * 
	 * @param externalUserAccount
	 */
	public void save(ExternalUserAccount externalUserAccount);
	
	/**
	 * Get the list of external user accounts for a given user name.
	 * 
	 * @see edu.ur.ir.user.ExternalUserAccountDAO#getByExternalUserName(java.lang.String)
	 */
	public List<ExternalUserAccount> getByExternalUserName(String externalUserName);
	
	/**
	 * Get the external user account by the given external user name and external account type.
	 * 
	 * @param externalUserName - the external user name
	 * @param externalAccountType - the external account type used
	 *  
	 * @return - the found external user account or null if the external user account is not found.
	 */
	public ExternalUserAccount getByExternalUserNameAccountType(String externalUserName, ExternalAccountType externalAccountType);
}



