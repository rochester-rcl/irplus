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

package edu.ur.hibernate.ir.user.db;

import java.io.File;
import java.util.List;
import java.util.Properties;

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
import edu.ur.ir.repository.Repository;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.SharedInboxFileDAO;
import edu.ur.ir.user.UserManager;
import edu.ur.util.FileUtil;

/**
 * Test sharing file with a user
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class SharedInboxFileDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	// Transaction manager
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
    		TransactionDefinition.PROPAGATION_REQUIRED);
	
     IrUserDAO userDAO= (IrUserDAO) ctx
     	.getBean("irUserDAO");

     SharedInboxFileDAO sharedInboxFileDAO = (SharedInboxFileDAO) ctx.getBean("sharedInboxFileDAO");
     
     VersionedFileDAO versionedFileDAO= (VersionedFileDAO) ctx
		.getBean("versionedFileDAO");

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
	 * Test PersonName persistance
	 */
	@Test
	public void baseInviteUserTokenDAOTest()throws Exception{

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
		IrUser sharingUser = userManager.createUser("passowrd", "userName1");
		sharingUser .setAccountExpired(true);
		sharingUser .setAccountLocked(true);
		sharingUser .setCredentialsExpired(true);
		userDAO.makePersistent(sharingUser);
		
		IrUser sharingWithUser = userManager.createUser("passowrd", "userName2");
		sharingWithUser .setAccountExpired(true);
		sharingWithUser .setAccountLocked(true);
		sharingWithUser .setCredentialsExpired(true);
		userDAO.makePersistent(sharingWithUser);
		
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
		VersionedFile vf = new VersionedFile(sharingUser, fileInfo, "name");
		versionedFileDAO.makePersistent(vf);
		Long irFileId = vf.getCurrentVersion().getIrFile().getId(); 
		SharedInboxFile inboxFile = sharingWithUser.addToSharedFileInbox(sharingUser, vf);
		sharedInboxFileDAO.makePersistent(inboxFile);
		
		//complete the transaction
		tm.commit(ts);
        
		
		ts = tm.getTransaction(td);
		SharedInboxFile other = sharedInboxFileDAO.getById(inboxFile.getId(),  false);
		assert  other != null : 
			"Should be able to find inbox file " + inboxFile;
		
		assert userDAO.getById(sharingWithUser.getId(), false).getSharedInboxFiles().contains(other):
			"Sharing with user should see have the file in inbox but doesn't";
		
		Long sharingWithUserCount =  sharedInboxFileDAO.getSharedInboxFileCount(sharingWithUser);
		assert sharingWithUserCount.equals(1l) : " There should be 1 shared file but there are " + 
		sharingWithUserCount;
		
		List<SharedInboxFile> userInboxFiles = sharedInboxFileDAO.getSharedInboxFiles(sharingWithUser);
		
		assert userInboxFiles.contains(other) : "User inbox files should contain " + other;
		tm.commit(ts);
		
		
		// delete the shared inbox file
		ts = tm.getTransaction(td);
		sharedInboxFileDAO.makeTransient(sharedInboxFileDAO.getById(inboxFile.getId(), false));
		tm.commit(ts);
		
		
		// start a new transaction
		ts = tm.getTransaction(td);
		assert sharedInboxFileDAO.getById(inboxFile.getId(), false) == null : "Should not be able to find other";
		

			
		
		versionedFileDAO.makeTransient(versionedFileDAO.getById(vf.getId(), false));
		fileDAO.makeTransient(fileDAO.getById(irFileId, false));
		
		// delete the users
		userDAO.makeTransient(userDAO.getById(sharingUser.getId(), false));
		userDAO.makeTransient(userDAO.getById(sharingWithUser.getId(), false));
		tm.commit(ts);
		
		// start new transaction
		ts = tm.getTransaction(td);
		// clean up the repostory
		repoHelper.cleanUpRepository();
		tm.commit(ts);
		
	}

}
