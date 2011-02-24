/**  
   Copyright 2008 - 2011 University of Rochester

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

package edu.ur.hibernate.ir.user.db;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FileServerService;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.invite.InviteToken;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.security.IrClassTypeDAO;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrClassTypePermissionDAO;
import edu.ur.ir.user.InviteInfo;
import edu.ur.ir.user.InviteInfoDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserManager;
import edu.ur.util.FileUtil;

/**
 * Test class for persistent methods of InviteInfo
 * 
 * @author Sharmila Ranganathan
 *
 */

@Test(groups = { "baseTests" }, enabled = true)
public class InviteInfoDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	// Transaction manager
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
    		TransactionDefinition.PROPAGATION_REQUIRED);
	
     IrUserDAO userDAO= (IrUserDAO) ctx
     	.getBean("irUserDAO");

     InviteInfoDAO inviteInfoDAO= (InviteInfoDAO) ctx
 		.getBean("inviteInfoDAO");
     
     VersionedFileDAO versionedFileDAO= (VersionedFileDAO) ctx
		.getBean("versionedFileDAO");
     
     IrClassTypeDAO irClassTypeDAO= (IrClassTypeDAO) ctx
		.getBean("irClassTypeDAO");
     
     IrClassTypePermissionDAO irClassTypePermissionDAO= (IrClassTypePermissionDAO) ctx
		.getBean("irClassTypePermissionDAO");

 	/** Properties file with testing specific information. */
 	PropertiesLoader propertiesLoader = new PropertiesLoader();
 	
 	/** Get the properties file  */
 	Properties properties = propertiesLoader.getProperties();


	/** Unique name generator  */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) 
	ctx.getBean("uniqueNameGenerator");
	
    /** IrFile relational data access   */
    IrFileDAO fileDAO= (IrFileDAO) ctx
	.getBean("irFileDAO");
     
	/**
	 * Test Invite user persistence
	 */
	@Test
	public void baseInviteInfoDAOTest()throws Exception{

		TransactionStatus ts = tm.getTransaction(td);

		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer",
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");
		tm.commit(ts);

        // Start a transaction 
		ts = tm.getTransaction(td);
		
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		userDAO.makePersistent(user);
		
		tm.commit(ts);
		
        // Start a transaction 
		ts = tm.getTransaction(td);

		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		FileServerService fileServerService = repoHelper.getFileServerService();
		FileInfo fileInfo = fileServerService.addFile(repo.getFileDatabase(), f,
				uniqueNameGenerator.getNextName(), "txt");
		fileInfo.setDisplayName("name");
		VersionedFile vf = new VersionedFile(user, fileInfo, "name");
		versionedFileDAO.makePersistent(vf);
		Long irFileId = vf.getCurrentVersion().getIrFile().getId(); 

		IrClassTypePermission permission 
			= irClassTypePermissionDAO.getClassTypePermissionByNameAndClassType("edu.ur.ir.file.VersionedFile", "VIEW");

		InviteToken token = new InviteToken("test@mail.com", "123", user);
		token.setInviteMessage("invite message");
		
		Set<VersionedFile> files = new HashSet<VersionedFile>();
		files.add(vf);
		
		Set<IrClassTypePermission> permissions = new HashSet<IrClassTypePermission>();
		permissions.add(permission);
		
		InviteInfo inviteInfo = new InviteInfo(files, permissions, token );
		
		

		inviteInfoDAO.makePersistent(inviteInfo);
		
		//complete the transaction
		tm.commit(ts);
        
		ts = tm.getTransaction(td);
		InviteInfo other = inviteInfoDAO.getById(inviteInfo.getId(), false);
		assert other.equals(inviteInfo) : "The user inviteInfo information should be equal";
		assert inviteInfo.getInviteToken().getEmail() == "test@mail.com" : "Email should be equal";
		assert inviteInfo.getInviteToken().getInviteMessage() == "invite message" : "Message should be equal";
		assert inviteInfo.getInviteToken().getToken() == "123" : "inviteInfo should be equal";
		assert inviteInfo.getPermissions().contains(permission) : "Permissions should exit";
		assert inviteInfo.getInviteToken().getInvitingUser().equals(user) : "User should be equal";
		assert inviteInfo.getFiles().contains(vf) : "VersionedFile should be equal";
		assert (inviteInfoDAO.findInviteInfoForToken("123")).equals(inviteInfo) : "The user inviteInfo should be equal";
		List<InviteInfo> invites = inviteInfoDAO.getInviteInfoByEmail("TeSt@Mail.com");
		assert invites.size() == 1 : "Should find one invite but found " + invites.size();
		assert invites.get(0).equals(inviteInfo) : "invite " + invites.get(0) + " should equal " + inviteInfo;
		
		
		invites = inviteInfoDAO.getInviteInfoByEmail("test@mail.com");
		assert invites.size() == 1 : "Should find one invite but found " + invites.size();
		assert invites.get(0).equals(inviteInfo) : "invite " + invites.get(0) + " should equal " + inviteInfo;
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		inviteInfoDAO.makeTransient(inviteInfoDAO.getById(other.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert inviteInfoDAO.getById(other.getId(), false) == null : "Should not be able to find other";
		versionedFileDAO.makeTransient(versionedFileDAO.getById(vf.getId(), false));
		fileDAO.makeTransient(fileDAO.getById(irFileId, false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		repoHelper.cleanUpRepository();
		tm.commit(ts);
		
	}
	
	
}
