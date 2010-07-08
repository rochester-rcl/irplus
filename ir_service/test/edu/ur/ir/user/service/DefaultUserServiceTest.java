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

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.user.ExternalAccountType;
import edu.ur.ir.user.ExternalAccountTypeService;
import edu.ur.ir.user.ExternalUserAccount;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.PersonalItemDeleteRecordDAO;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserPublishingFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.util.FileUtil;

/**
 * Test the service methods for the default user services.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultUserServiceTest {

	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");
	
	
	/** Repository data access */
	RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
	
	/** External account type service */
	ExternalAccountTypeService externalAccountTypeService = (ExternalAccountTypeService) ctx.getBean("externalAccountTypeService");


	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultUserServiceTest.class);
	
	/** User data access */
	UserPublishingFileSystemService userPublishingFileSystemService = 
		(UserPublishingFileSystemService) ctx.getBean("userPublishingFileSystemService");

	/** delete item data access object  */
	PersonalItemDeleteRecordDAO personalItemDeleteRecordDAO = (PersonalItemDeleteRecordDAO)ctx.getBean("personalItemDeleteRecordDAO");

	UserFileSystemService userFileSystemService = (UserFileSystemService) ctx.getBean("userFileSystemService");
	
	
	/**
	 * Test creating a user
	 * @throws UserHasPublishedDeleteException 
	 */
	public void createUserTest() throws UserHasPublishedDeleteException, UserDeletedPublicationException
	{
 	    // Start the transaction this is for lazy loading
		TransactionStatus ts = tm.getTransaction(td);
		UserEmail email = new UserEmail("email");

		UserEmail email2 = new UserEmail("email2");

		IrUser user = userService.createUser("password","username",email);
		IrUser user2 = userService.createUser("password2", "username2", email2);
		tm.commit(ts);
		
		// make sure the users exist
		ts = tm.getTransaction(td);
		IrUser otherUser = userService.getUser(user.getId(), false);
		assert otherUser.equals(user) : "Users " + otherUser + 
		" should be the same as " + user;
       
		IrUser otherUser2 = userService.getUser(user.getUsername());
		
		assert otherUser.equals(otherUser2):
			"other user " + otherUser + " should equal " + otherUser2;
		tm.commit(ts);

		
        // start a new transaction
		ts = tm.getTransaction(td);
		
		List<Long> userIds = new LinkedList<Long>();
		userIds.add(user.getId());
		userIds.add(user2.getId());
		
		List<IrUser> users = userService.getUsers(userIds);
		assert users.size() == 2 : "should find 2 users but found " + users.size();
		
		IrUser myUser1 = userService.getUser(user.getId(), false);
		IrUser myUser2 = userService.getUser(user2.getId(), false);
		
		userService.deleteUser(myUser1, myUser1);
		userService.deleteUser(myUser2, myUser2);
		
		tm.commit(ts);
		
		assert userService.getUser(myUser1.getId(), false) == null :
			"should not be able to find other user " + myUser1;
		
		assert userService.getUser(myUser2.getId(), false) == null :
			"should not be able to find other user " + myUser2;
		
	}
	
	public void testHandleForgotPassword() throws UserHasPublishedDeleteException, UserDeletedPublicationException { 
		// determine if we should be sending emails 
		boolean sendEmail = new Boolean(properties.getProperty("send_emails")).booleanValue();

		log.debug("Send email for testHandleForgotPassword test = " + sendEmail);
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		UserEmail email = new UserEmail("email");
		IrUser user = userService.createUser("password", "username", email);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		String token = userService.savePasswordToken(user);
		if (sendEmail) {
			String user_2_email = properties.getProperty("user_2_email").trim();
			log.debug("Sending email to " + user_2_email);
			userService.sendEmailForForgotPassword(token, user_2_email);
		}
		
		IrUser otherUser = userService.getUser("username");
		
		assert otherUser.getPasswordToken() != null : "Password token should exist";
		tm.commit(ts);
		
		// Start new transaction
		ts = tm.getTransaction(td);
		IrUser deleteUser = userService.getUser(user.getId(), false);
		userService.deleteUser(deleteUser,deleteUser);
		tm.commit(ts);
		
		assert userService.getUser(user.getId(), false) == null : "User should be null";
	}
	
	/**
	 * Test getting users by an external user name - this includes case sensitive and case insensitive 
	 * accounts.
	 */
	public void testGetByExternalUserName()throws UserHasPublishedDeleteException, UserDeletedPublicationException
	{
		
		
	    // Start the transaction this is for lazy loading
		TransactionStatus ts = tm.getTransaction(td);
		
		ExternalAccountType caseInsensitiveExternalAccountType = new ExternalAccountType("caseInsenstiveType");
		ExternalAccountType caseSensitiveExternalAccountType = new ExternalAccountType("caseSensitiveType");
		caseSensitiveExternalAccountType.setUserNameCaseSensitive(true);
		
		externalAccountTypeService.save(caseInsensitiveExternalAccountType);
		externalAccountTypeService.save(caseSensitiveExternalAccountType);
		
		UserEmail email = new UserEmail("email");
		UserEmail email2 = new UserEmail("email2");

		IrUser user = userService.createUser("password","username",email);
		IrUser user2 = userService.createUser("password2", "username2", email2);
		
		user.createExternalUserAccount("userAccount",caseInsensitiveExternalAccountType);
		ExternalUserAccount externalAccount1 = user.getExternalAccount();
        user2.createExternalUserAccount("userAccount", caseSensitiveExternalAccountType);
        ExternalUserAccount externalAccount2 = user2.getExternalAccount();
		
        userService.makeUserPersistent(user);
        userService.makeUserPersistent(user2);
        
		tm.commit(ts);
		
		// make sure the users exist
		ts = tm.getTransaction(td);
		IrUser otherUser = userService.getUser(user.getId(), false);
		assert otherUser.equals(user) : "Users " + otherUser + 
		" should be the same as " + user;
       
		IrUser otherUser2 = userService.getUser(user.getUsername());
		
		assert otherUser.equals(otherUser2):
			"other user " + otherUser + " should equal " + otherUser2;
		tm.commit(ts);

		
        // start a new transaction
		ts = tm.getTransaction(td);
		
		
		Set<ExternalUserAccount> userAccounts = userService.getByExternalUserName("userAccount");
		assert userAccounts.size() == 2 : "Should only have two accounts but has " + userAccounts.size();
		assert userAccounts.contains(externalAccount1) : "user accounts should contain " + externalAccount1 + "but does not";
		assert userAccounts.contains(externalAccount2) : "user accounts should contain " + externalAccount2 + "but does not";
		
		userAccounts = userService.getByExternalUserName("useraccount");
		assert userAccounts.size() == 1 : "Should only have one account but has " + userAccounts.size();
		assert userAccounts.contains(externalAccount1) : "Should contain " + externalAccount1 + "but doesn't";
		
		
		ExternalUserAccount account = userService.getByExternalUserNameAccountType("userAccount", caseInsensitiveExternalAccountType);
		assert account.equals(externalAccount1) : " account " + account + " should equal " + externalAccount1;
		account = userService.getByExternalUserNameAccountType("useraccount", caseInsensitiveExternalAccountType);
		assert account.equals(externalAccount1) : " account " + account + " should equal " + externalAccount1;

		account = userService.getByExternalUserNameAccountType("userAccount", caseSensitiveExternalAccountType);
		assert account.equals(externalAccount2) : " account " + account + " should equal " + externalAccount2;

		account = userService.getByExternalUserNameAccountType("useraccount", caseSensitiveExternalAccountType);
		assert account == null : "Account should not be found but was found = " + account;

		
		
 		
		IrUser myUser1 = userService.getUser(user.getId(), false);
		IrUser myUser2 = userService.getUser(user2.getId(), false);
		
		userService.deleteUser(myUser1, myUser1);
		userService.deleteUser(myUser2, myUser2);
		
		externalAccountTypeService.delete(externalAccountTypeService.get(caseInsensitiveExternalAccountType.getId(),false));
		externalAccountTypeService.delete(externalAccountTypeService.get(caseSensitiveExternalAccountType.getId(),false));
		
		tm.commit(ts);
		
	}
	
	/**
	 * Delete a user with a file that is in a publication from a workspace
	 * @throws UserDeletedPublicationException 
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws IllegalFileSystemNameException 
	 * @throws DuplicateNameException 
	 */
	public void testDeleteUserWithFilePublicationWorkspace() throws UserHasPublishedDeleteException, UserDeletedPublicationException, LocationAlreadyExistsException, IllegalFileSystemNameException, DuplicateNameException
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);
		
		UserEmail email = new UserEmail("email");

        // Start the transaction 
		ts = tm.getTransaction(td);
		IrUser user = userService.createUser("password", "username", email);
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		assert f != null : "File should not be null";
		assert user.getId() != null : "User id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		PersonalFile pf = userFileSystemService.addFileToUser(repo, f, user, "a file", "a file");

		tm.commit(ts);
		
		// new transaction
		ts = tm.getTransaction(td);
		PersonalItem personalItem = userPublishingFileSystemService.createRootPersonalItem(user, "articles", "rootCollection");
		GenericItem item1 = personalItem.getVersionedItem().getCurrentVersion().getItem();
		ItemFile itemFile = item1.addFile(pf.getVersionedFile().getCurrentVersion().getIrFile());
		userPublishingFileSystemService.makePersonalItemPersistent(personalItem);
		tm.commit(ts);
		
        //new transaction
		ts = tm.getTransaction(td);
		PersonalItem otherPersonalItem = userPublishingFileSystemService.getPersonalItem(personalItem.getId(), false);
		assert otherPersonalItem.getVersionedItem().getCurrentVersion().getItem().equals(item1) : "Should be equal to item1";
		assert otherPersonalItem.getVersionedItem().getCurrentVersion().getItem().getItemFiles().size() == 1 : "Should have one file but has " +  otherPersonalItem.getVersionedItem().getCurrentVersion().getItem().getItemFiles().size();
		assert otherPersonalItem.getVersionedItem().getCurrentVersion().getItem().getItemFile(itemFile.getId()) != null : "Should be able to find file " + itemFile;
		IrUser deleteUser = userService.getUser(user.getId(), false);
		userService.deleteUser(deleteUser, deleteUser);
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	



}
