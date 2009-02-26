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


package edu.ur.ir.security.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrUserAccessControlEntry;
import edu.ur.ir.security.IrUserGroupAccessControlEntry;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserGroupService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;
import edu.ur.util.FileUtil;

/**
 * Test class for Security service
 *  
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */ 
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultSecurityServiceTest {

	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User data access */
	UserService userService = (UserService) ctx
	.getBean("userService");
	
	/** User data access */
	UserGroupService userGroupService = (UserGroupService) ctx
	.getBean("userGroupService");
	
	UserFileSystemService userFileSystemService = (UserFileSystemService)
	ctx.getBean("userFileSystemService");

	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

	/** Base Security data access */
	SecurityService securityService = (SecurityService) ctx
	.getBean("securityService");
	
	/** service class for dealing with repository information  */
	RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");


 	/**
	 * Test assigning permissions to a user 
	 * @throws ClassNotFoundException 
	 * @throws DuplicateNameException 
 	 * @throws UserHasPublishedDeleteException 
 	 * @throws DuplicateAccessControlEntryException 
	 */
	public void aclUserAcessControlEntryTest() throws ClassNotFoundException, DuplicateNameException,
			IllegalFileSystemNameException, UserHasPublishedDeleteException, UserDeletedPublicationException{

		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);
			
        // Start the transaction 
		ts = tm.getTransaction(td);
		
		
		UserEmail email = new UserEmail("admin@library.rochester.edu");
		IrUser user = userService.createUser("password", "username", email);
		
		UserEmail email1 = new UserEmail("sranganathan@library.rochester.edu");
		IrUser user1 = userService.createUser("password1", "username1", email1);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		PersonalFile pf = null;
		
		pf = userFileSystemService.addFileToUser(repo, f, user, 
				"test file", "description");
	

        tm.commit(ts);

		//Start a transaction 
		ts = tm.getTransaction(td);
		
		IrClassTypePermission viewPermission = 
			securityService.getPermissionForClass(pf.getVersionedFile(), "VIEW");
		
		assert viewPermission != null : "Veiew permission is null";
		
		IrClassTypePermission editPermission = 
			securityService.getPermissionForClass(pf.getVersionedFile(), "EDIT");
		
		assert editPermission != null : "Edit permission is null";
		
		List<IrClassTypePermission> permissions = new ArrayList<IrClassTypePermission>();
		permissions.add(viewPermission);
		permissions.add(editPermission);
		
		securityService.createPermissions(pf.getVersionedFile(), user1, permissions);
		
		tm.commit(ts);

		//Start a transaction 
		ts = tm.getTransaction(td);

		IrAcl acl = securityService.getAcl(pf.getVersionedFile(), user1);
		IrUserAccessControlEntry aEntry = acl.getUserAccessControlEntry(user1.getUsername());

		assert aEntry.getPermissions().size() == 2 : "Should be equal to 2";
		assert aEntry.getPermissions().contains("VIEW") : "Should be VIEW";
		assert aEntry.getPermissions().contains("EDIT") : "Should be EDIT";
				
		tm.commit(ts);

		// Start a transaction 
		ts = tm.getTransaction(td);
		securityService.deletePermissions(pf.getVersionedFile().getId(), pf.getVersionedFile().getClass().getName(), user1);
		
		tm.commit(ts);

		
		// Start a transaction 
		ts = tm.getTransaction(td);
		userService.deleteUser(userService.getUser(user.getId(), false));
		userService.deleteUser(userService.getUser(user1.getId(), false));
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		assert userService.getUser(user1.getId(), false) == null : "User should be null"; 
		assert userService.getUser(user.getId(), false) == null : "User should be null";
		helper.cleanUpRepository();
		tm.commit(ts);	 
	}
	
    /**
     * Test assigning permissions to a user group through the service 
     * class
     * @throws DuplicateNameException 
     * @throws UserHasPublishedDeleteException 
     * @throws DuplicateAccessControlEntryException 
     */
    public void aclUserGroupAcessControlEntryTest() throws DuplicateNameException, UserHasPublishedDeleteException, UserDeletedPublicationException{

		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper helper = new RepositoryBasedTestHelper(ctx);
		Repository repo = helper.createTestRepositoryDefaultFileServer(properties);
		// save the repository
		tm.commit(ts);
			
        // Start the transaction 
		ts = tm.getTransaction(td);
		
		
		UserEmail email = new UserEmail("admin@library.rochester.edu");
		IrUser user = userService.createUser("password", "username", email);
		
		UserEmail email1 = new UserEmail("sranganathan@library.rochester.edu");
		IrUser user1 = userService.createUser("password1", "username1", email1);
		
        IrUserGroup userGroup = new IrUserGroup("myGroup");
        userGroupService.save(userGroup);
        
        assert userGroup.getId() != null : "User group should have id " + userGroup;
        
        InstitutionalCollection myCollection = repo.createInstitutionalCollection("myCollection");
	
        repositoryService.saveRepository(repo);
        tm.commit(ts);

		//Start a transaction 
		ts = tm.getTransaction(td);
        
		
		
		IrClassTypePermission viewPermission = 
			securityService.getPermissionForClass(myCollection, "VIEW");
		assert viewPermission != null : "View permission is null for collection " + myCollection;

		IrClassTypePermission adminPermission = 
			securityService.getPermissionForClass(myCollection, "ADMINISTRATION");
		
		assert adminPermission != null : "Administration permission is null";
		
		List<IrClassTypePermission> permissions = new ArrayList<IrClassTypePermission>();
		permissions.add(viewPermission);
		permissions.add(adminPermission);
		
		securityService.createPermissions(myCollection, userGroup, permissions);
		
		tm.commit(ts);

		//Start a transaction 
		ts = tm.getTransaction(td);

		IrAcl acl = securityService.getAcl(myCollection, userGroup);
		
		Set<IrUserGroupAccessControlEntry> entries = acl.getGroupEntries();
		assert entries.size() == 1 : "There should be 1 entry but there are " + entries.size();
		
		for( IrUserGroupAccessControlEntry entry : entries)
		{
			System.out.println("entry = " + entry);
		}
		
		
		IrUserGroupAccessControlEntry aEntry = acl.getGroupAccessControlEntryByGroupId(userGroup.getId());
		
		
		assert aEntry.getPermissions().size() == 2 : "Should be equal to 2";
		assert aEntry.getPermissions().contains("VIEW") : "Should be view";
		assert aEntry.getPermissions().contains("ADMINISTRATION") : "Should be admin";
	
		tm.commit(ts);

		// Start a transaction 
		ts = tm.getTransaction(td);
		securityService.deletePermissions(myCollection.getId(), myCollection.getClass().getName(), userGroup);
		tm.commit(ts);

		
		// Start a transaction 
		ts = tm.getTransaction(td);
		userService.deleteUser(userService.getUser(user.getId(), false));
		userService.deleteUser(userService.getUser(user1.getId(), false));
		userGroupService.delete(userGroupService.get(userGroup.getId(), false));
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		helper.cleanUpRepository();
		tm.commit(ts);	 
	
	}

    
}
