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

package edu.ur.ir.researcher.service;

import java.io.File;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FileServerService;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.service.test.helper.ContextHolder;
import edu.ur.ir.repository.service.test.helper.PropertiesLoader;
import edu.ur.ir.repository.service.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserService;
import edu.ur.util.FileUtil;

/**
 * Testing for the researcher file system service.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ResearcherFileSystemServiceTest 
{
	
	/** Application context  for loading information*/
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** User data access */
	UserService userService = (UserService) ctx.getBean("userService");
	
	/** User file systemdata access */
	ResearcherService researcherService = 
		(ResearcherService) ctx.getBean("researcherService");
	
	/** User file systemdata access */
	ResearcherFileSystemService researcherFileSystemService = 
		(ResearcherFileSystemService) ctx.getBean("researcherFileSystemService");
	
	/** Item services */
	ItemService itemService = (ItemService) ctx.getBean("itemService");
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	

	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** Unique name generator  */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) 
	ctx.getBean("uniqueNameGenerator");

	
	
	/**
	 * Test creating a file
	 * @throws UserHasPublishedDeleteException 
	 * @throws LocationAlreadyExistsException 
	 * @throws DuplicateNameException 
	 */
	public void createFileFolderTest() throws IllegalFileSystemNameException, UserHasPublishedDeleteException, 
	UserDeletedPublicationException, LocationAlreadyExistsException, DuplicateNameException 
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
		Researcher researcher = user.createResearcher();
		userService.makeUserPersistent(user);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_service_temp_directory");
		File directory = new File(tempDirectory);
		
        // helper to create the file
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);

		File f = testUtil.creatFile(directory, "testFile", 
		"Hello  - irFile This is text in a file - VersionedFileDAO test");
		
		FileServerService fileServerService = helper.getFileServerService();
		FileInfo info = fileServerService.addFile(repo.getFileDatabase(), f,
				uniqueNameGenerator.getNextName(), "txt");

		IrFile irFile = new IrFile(info, "testFile"); 
		tm.commit(ts);
		
		assert f != null : "File should not be null";
		assert user.getResearcher().getId() != null : "Researcher id should not be null";
		assert repo.getFileDatabase().getId() != null : "File database id should not be null";
		
		// new transaction
		ts = tm.getTransaction(td);
		ResearcherFolder rootFolder = researcher.createRootFolder("root");
		ResearcherFolder subFolder = rootFolder.createChild("subFolder");
		ResearcherFile rootFolderFile = rootFolder.addFile(irFile);
		ResearcherFile subFolderFile = subFolder.addFile(irFile);
		researcherService.saveResearcher(researcher);
     	tm.commit(ts);
		
        //new transaction
		ts = tm.getTransaction(td);

		Researcher otherResearcher2 = researcherService.getResearcher(researcher.getId(), false);
		rootFolder = otherResearcher2.getRootFolder("root");
		assert rootFolder != null : "root folder should not be null";
		subFolder = rootFolder.getChild("subFolder");
		assert subFolder != null : "sub folder should not be null";
		
		rootFolderFile = researcherFileSystemService.getResearcherFile(rootFolderFile.getId(), false);
		
		assert rootFolder.getFiles().contains(rootFolderFile) : "Should contain root folder file";
		assert subFolder.getFiles().contains(subFolderFile) : "Should contain sub folder file";
		
		researcherFileSystemService.deleteFolder(subFolder);
		researcherFileSystemService.deleteFile(rootFolderFile);
		
		tm.commit(ts);
	     
		
        //new transaction
		ts = tm.getTransaction(td);
		
		IrUser otherUser2 = userService.getUser(user.getUsername());
		userService.deleteUser(otherUser2, otherUser2);
		tm.commit(ts);
		
	    // Start new transaction
		ts = tm.getTransaction(td);
		helper.cleanUpRepository();
		tm.commit(ts);	
	}
	

	


}
