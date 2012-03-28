/**  
   Copyright 2008 - 2012 University of Rochester

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


package edu.ur.hibernate.ir.groupspace.db;

import java.io.File;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.LocationAlreadyExistsException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.hibernate.ir.test.helper.RepositoryBasedTestHelper;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageImage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageImageDAO;
import edu.ur.ir.repository.Repository;

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.util.FileUtil;

/**
 * Group workspace project page image data access test.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class GroupWorkspaceProjectPageImageDAOTest 
{
	
	// get the application context 
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	// Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	// Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	// Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	// Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
	
	
	// Researcher data access.  */
	IrUserDAO userDAO = (IrUserDAO) ctx
	.getBean("irUserDAO");	
	
	// Ir File relational data access.  */
	IrFileDAO irFileDAO = (IrFileDAO) ctx.getBean("irFileDAO");
	
	// group workspace data access
	GroupWorkspaceDAO groupWorkspaceDAO = (GroupWorkspaceDAO) ctx
	.getBean("groupWorkspaceDAO");
	
	// group workspace project page data access
	GroupWorkspaceProjectPageDAO groupWorkspaceProjectPageDAO = (GroupWorkspaceProjectPageDAO) ctx
	.getBean("groupWorkspaceProjectPageDAO");
	
	// group workspace project page data access
	GroupWorkspaceProjectPageImageDAO groupWorkspaceProjectPageImageDAO = (GroupWorkspaceProjectPageImageDAO) ctx
	.getBean("groupWorkspaceProjectPageImageDAO");
	
	/**
	 * test adding pictures to the group workspace project page
	 * @throws LocationAlreadyExistsException 
	 */
	public void addGroupWorkspaceProjectPagePictures()  throws IllegalFileSystemNameException, LocationAlreadyExistsException
	{
		TransactionStatus ts = tm.getTransaction(td);
		
		// create the first file to store in the temporary folder
		String tempDirectory = properties.getProperty("ir_hibernate_temp_directory");
		File directory = new File(tempDirectory);
		
		RepositoryBasedTestHelper repoHelper = new RepositoryBasedTestHelper(ctx);
		Repository repo = repoHelper.createRepository("localFileServer",
				"displayName",
				"file_database", 
				"my_repository", 
				properties.getProperty("a_repo_path"),
				"default_folder");
        
        UserEmail userEmail = new UserEmail("email");
		
		// create a user and add the versioned file to the item
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		userDAO.makePersistent(user);
		
		GroupWorkspace groupWorkspace = new GroupWorkspace("grouName", "groupDescription");
        groupWorkspaceDAO.makePersistent(groupWorkspace);
        GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        groupWorkspaceProjectPageDAO.makePersistent(groupWorkspaceProjectPage);
 		
        // helper to create the file
		// we are only testing the ability to add a file to
		// the researcher rather than an actual picture.
		FileUtil testUtil = new FileUtil();
		testUtil.createDirectory(directory);
		
		File f = testUtil.creatFile(directory, "testFile", 
				"Hello  - irFile This is text in a file");

		FileInfo fileInfo1 = repo.getFileDatabase().addFile(f, "newFile1");
		fileInfo1.setDescription("testThis");
        IrFile primaryIrFile = new IrFile(fileInfo1, "newName2");
        
        irFileDAO.makePersistent(primaryIrFile);
        
        tm.commit(ts);
		
		// add the file to the group workspace project page
        ts = tm.getTransaction(td);
        groupWorkspace = groupWorkspaceDAO.getById(groupWorkspace.getId(), false);
        groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
        primaryIrFile = irFileDAO.getById(primaryIrFile.getId(), false);  
        GroupWorkspaceProjectPageImage gwImage = groupWorkspaceProjectPage.addImage(primaryIrFile);
        
        groupWorkspaceProjectPageImageDAO.makePersistent(gwImage);
        
		tm.commit(ts);
		
		//create a new transaction
		ts = tm.getTransaction(td);
		//reload the group workspace
		groupWorkspaceProjectPage = groupWorkspaceProjectPageDAO.getById(groupWorkspaceProjectPage.getId(), false);
		assert groupWorkspaceProjectPage.getImageByIrFileId(primaryIrFile.getId()) != null : "The picture should be found";
		tm.commit(ts);	
		
		//create a new transaction
		ts = tm.getTransaction(td);
		groupWorkspaceProjectPage = groupWorkspaceProjectPageDAO.getById(groupWorkspaceProjectPage.getId(), false);
		Set<GroupWorkspaceProjectPageImage> images = groupWorkspaceProjectPage.getImages();
		
		LinkedList<Long> fileIds = new LinkedList<Long>();
		
		for(GroupWorkspaceProjectPageImage image: images)
		{
			groupWorkspaceProjectPage.remove(image);
			IrFile file = image.getImageFile();
			groupWorkspaceProjectPageImageDAO.makeTransient(image);
			fileIds.add(file.getId());
			irFileDAO.makeTransient(file);
		}
		
		GroupWorkspace other = groupWorkspaceDAO.getById(groupWorkspace.getId(), false);
        groupWorkspaceDAO.makeTransient(other);
		
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		
		//start a new transaction
		ts = tm.getTransaction(td);
		assert fileIds.size() > 0 : "File ids should be greater than zero but is not";
		for(Long id : fileIds)
		{
			assert irFileDAO.getById(id, false) == null : "File id should be null but is not " + id;
		}
		repoHelper.cleanUpRepository();
		tm.commit(ts);
	}

}
