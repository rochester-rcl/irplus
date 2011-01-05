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


package edu.ur.hibernate.ir.groupspace.db;

import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.UniqueNameGenerator;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.ir.file.IrFileDAO;
import edu.ur.ir.file.VersionedFileDAO;
import edu.ur.ir.groupspace.GroupFolder;
import edu.ur.ir.groupspace.GroupFolderDAO;
import edu.ur.ir.groupspace.GroupSpace;
import edu.ur.ir.groupspace.GroupSpaceDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserManager;

/**
 * Group folder data access object test.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupFolderDAOTest {
	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/**  Transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
			.getBean("transactionManager");

	/** transaction definition  */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	/** Group folder data access */
	GroupFolderDAO groupFolderDAO = (GroupFolderDAO) ctx
	.getBean("groupFolderDAO");

    /** User data access */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");
    
	/** versioned file data access object  */
	VersionedFileDAO versionedFileDAO = (VersionedFileDAO) ctx
			.getBean("versionedFileDAO");
	
	/** IrFile relational data access   */
    IrFileDAO fileDAO= (IrFileDAO) ctx
	.getBean("irFileDAO");
    
    /** Unique name generator  */
	UniqueNameGenerator uniqueNameGenerator = (UniqueNameGenerator) 
	ctx.getBean("uniqueNameGenerator");
	
	/** Group space data access */
	GroupSpaceDAO groupSpaceDAO = (GroupSpaceDAO) ctx
	.getBean("groupSpaceDAO");
	
	
	/**
	 * Test personal folder persistence
	 * 
	 * @throws DuplicateNameException 
	 * 
	*/
	@Test
	public void baseGroupFolderDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException {
  		
		TransactionStatus ts = tm.getTransaction(td);
		UserEmail userEmail = new UserEmail("user@email");

        // create a user who has their own folder
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		GroupSpace groupSpace = new GroupSpace("grouName", "groupDescription");
		groupSpaceDAO.makePersistent(groupSpace);
        
		// create the user and their folder.
		userDAO.makePersistent(user);
		GroupFolder groupFolder = groupSpace.createRootFolder(user, "topFolder");
		groupFolderDAO.makePersistent(groupFolder);
		tm.commit(ts);
		
        // Start the transaction 
        ts = tm.getTransaction(td);

        assert groupFolderDAO.getById(groupFolder.getId(), false) != null : "Should be able to find "
			+ " group Folder " + groupFolder;

		assert groupFolderDAO.getById(groupFolder.getId(), false).equals(groupFolder) : 
			"Folders should be equal";
		
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		groupFolderDAO.makeTransient(groupFolderDAO.getById(groupFolder.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert groupFolderDAO.getById(groupFolder.getId(), false) == null: 
			"Should not be able to find group folder";
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
        groupSpaceDAO.makeTransient(groupSpaceDAO.getById(groupSpace.getId(), false));

		tm.commit(ts);
	}
	
	/**
	 * Test GroupFolder persistence with children
	 * 
	 * @throws DuplicateNameException 
	 */
	@Test
	public void groupFolderWithChildrenDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException {
        TransactionStatus ts = tm.getTransaction(td);


		UserEmail userEmail = new UserEmail("user@email");

         // create a user who has their own folder
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);

		GroupSpace groupSpace = new GroupSpace("grouName", "groupDescription");
		groupSpaceDAO.makePersistent(groupSpace);

		
		// create the user and their folder.
		userDAO.makePersistent(user);
		GroupFolder groupFolder1 = groupSpace.createRootFolder(user, "topFolder");
		groupFolderDAO.makePersistent(groupFolder1);
		
		// make sure parent starts out with 1 and 2 left/right values.
		assert groupFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		groupFolder1.getLeftValue();

		assert groupFolder1.getRightValue() == 2L : "Right value should be 2 but is " + 
		groupFolder1.getRightValue();

		GroupFolder groupFolder2 = groupSpace.createRootFolder(user, "topFolder2");
		
		assert groupFolder2.getLeftValue() == 1L : "Left value should be 1 but is " + 
		groupFolder2.getLeftValue();

		assert groupFolder2.getRightValue() == 2L : "Right value should be 2 but is " + 
		groupFolder2.getRightValue();
		
		// add a child
		GroupFolder childFolder1 = null;
		try
		{
		    childFolder1 = groupFolder1.createChild("childFolder1", user);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert groupFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		groupFolder1.getLeftValue();

		assert groupFolder1.getRightValue() == 4L : "Right value should be 2 but is " + 
		groupFolder1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 1 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 3L : "Right value should be 2 but is " + 
		childFolder1.getRightValue();
		
		
		// add another child
		 GroupFolder childFolder2 = null;
		try
		{
		   childFolder2 = groupFolder1.createChild("child2",user);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert groupFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		groupFolder1.getLeftValue();

		assert groupFolder1.getRightValue() == 6L : "Right value should be 2 but is " + 
		groupFolder1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 1 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 3L : "Right value should be 2 but is " + 
		childFolder1.getRightValue();
		
		assert childFolder2.getLeftValue() == 4L : "Left value should be 1 but is " + 
		childFolder2.getLeftValue();

		assert childFolder2.getRightValue() == 5L : "Right value should be 2 but is " + 
		childFolder2.getRightValue();
		
		
		// add another child
		GroupFolder subFolder1 = null;
		try
		{
		    subFolder1 = childFolder1.createChild("subFolder1",user);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		
		assert groupFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		groupFolder1.getLeftValue();

		assert groupFolder1.getRightValue() == 8L : "Right value should be 8 but is " + 
		groupFolder1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 2 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 5L : "Right value should be 5 but is " + 
		childFolder1.getRightValue();
		
		assert subFolder1.getLeftValue() == 3L : "Left value should be 3 but is " + 
		subFolder1.getLeftValue();

		assert subFolder1.getRightValue() == 4L : "Right value should be 4 but is " + 
		subFolder1.getRightValue();
		
		assert childFolder2.getLeftValue() == 6L : "Left value should be 6 but is " + 
		childFolder2.getLeftValue();

		assert childFolder2.getRightValue() == 7L : "Right value should be 7 but is " + 
		childFolder2.getRightValue();
		
		// add another child
		 GroupFolder subFolder2 = null;
		try
		{
		    subFolder2 = childFolder1.createChild("subFolder2",user);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		
		
		assert groupFolder1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		groupFolder1.getLeftValue();

		assert groupFolder1.getRightValue() == 10L : "Right value should be 10 but is " + 
		groupFolder1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 2 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 7L : "Right value should be 7 but is " + 
		childFolder1.getRightValue();
		
		assert subFolder1.getLeftValue() == 3L : "Left value should be 3 but is " + 
		subFolder1.getLeftValue();

		assert subFolder1.getRightValue() == 4L : "Right value should be 4 but is " + 
		subFolder1.getRightValue();
		
		assert subFolder2.getLeftValue() == 5L : "Left value should be 5 but is " + 
		subFolder1.getLeftValue();

		assert subFolder2.getRightValue() == 6L : "Right value should be 6 but is " + 
		subFolder1.getRightValue();
		
		assert childFolder2.getLeftValue() == 8L : "Left value should be 8 but is " + 
		childFolder2.getLeftValue();

		assert childFolder2.getRightValue() == 9L : "Right value should be 9 but is " + 
		childFolder2.getRightValue();
		


		// persist the tree
		groupFolderDAO.makePersistent(groupFolder1);
		groupFolderDAO.makePersistent(groupFolder2);

	    tm.commit(ts);
        
   
	    
        // Start the transaction this is for lazy loading
        ts = tm.getTransaction(td);
        
		// make sure object has been persisted
		GroupFolder other = groupFolderDAO.getById(groupFolder1.getId(), false);
		assert other != null : "GroupFolder should be found";
		assert other.equals(groupFolder1) : "GroupFolder should be the same as is1 ";

		// make sure types are correct
		GroupFolder other2 = groupFolderDAO.getById(groupFolder2.getId(), false);
		assert !other.equals(other2) : "GroupFolder should be different";

		assert other.getChild(childFolder1.getName()).equals(
				childFolder1) : "GroupFolder should have child";
		assert other.getChild(childFolder2.getName()).equals(
				childFolder2) : "GroupFolder should have child";

		String name = childFolder1.getName();
		assert other.getChild(name).getChild(subFolder1.getName())
				.equals(subFolder1) : "GroupFolder should have child";
		assert other.getChild(name).getChild(subFolder2.getName())
				.equals(subFolder2) : "GroupFolder should have child";

		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		//*************************************
		// Make sure we can add new nodes after
		// data has been committed.
        //*************************************
	
		// Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);

		//GroupFolder myRoot = groupFolderDAO.getById(groupFolder1.getId(), false);
		// make sure object has been persisted
		GroupFolder akaSubTreeFolderInfo1 = groupFolderDAO.getById(subFolder1.getId(), false);
		
		assert akaSubTreeFolderInfo1.getLeftValue() == 3L : "Left value should be 5 but is " + 
		akaSubTreeFolderInfo1.getLeftValue();

		assert akaSubTreeFolderInfo1.getRightValue() == 4L : "Right value should be 6 but is " + 
		akaSubTreeFolderInfo1.getRightValue();
		
		assert !akaSubTreeFolderInfo1.isRoot(): "Should not be root folder";
		
		
		GroupFolder akaSubTreeParent = akaSubTreeFolderInfo1.getParent();
		
		assert akaSubTreeParent.getLeftValue() == 2L : "Left value should be 2 but is " + 
		childFolder1.getLeftValue();

		assert akaSubTreeParent.getRightValue() == 7L : "Right value should be 7 but is " + 
		childFolder1.getRightValue();
		
		GroupFolder akaRoot =  akaSubTreeParent.getParent();
		
		assert akaRoot.getLeftValue() == 1L : "Left value should be 1 but is " + 
		groupFolder1.getLeftValue();

		assert akaRoot.getRightValue() == 10L : "Right value should be 10 but is " + 
		groupFolder1.getRightValue();
		
		GroupFolder subSubTreeFolderInfo1 = null;
		try
		{
		    subSubTreeFolderInfo1 = 
			    akaSubTreeFolderInfo1.createChild("subSubFolder1",user);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		
		assert akaSubTreeFolderInfo1.getLeftValue() == 3L : "Left value should be 3 but is " + 
		akaSubTreeFolderInfo1.getLeftValue();

		assert akaSubTreeFolderInfo1.getRightValue() == 6L : "Right value should be 6 but is " + 
		akaSubTreeFolderInfo1.getRightValue();
		
		assert subSubTreeFolderInfo1.getLeftValue() == 4L : "Left value should be 4 but is " + 
		subSubTreeFolderInfo1.getLeftValue();

		assert subSubTreeFolderInfo1.getRightValue() == 5L : "Right value should be 5 but is " + 
		subSubTreeFolderInfo1.getRightValue();
		
		assert akaSubTreeFolderInfo1.getTreeRoot().getLeftValue() == 1L : "Left value should be 1 but is " + 
		akaSubTreeFolderInfo1.getTreeRoot().getLeftValue();

		assert akaSubTreeFolderInfo1.getTreeRoot().getRightValue() == 12L : "Right value should be 12 but is " + 
		akaSubTreeFolderInfo1.getTreeRoot().getRightValue();
		
		groupFolderDAO.makePersistent(akaSubTreeFolderInfo1.getTreeRoot());
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		
        //*************************************
		// Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		
		GroupFolder topFolder = groupFolderDAO.getById(groupFolder1.getId(), true);
		
		assert topFolder.getLeftValue() == 1L : "Left value should be 1 but is " + 
		groupFolder1.getLeftValue();

		assert topFolder.getRightValue() == 12L : "Right value should be 12 but is " + 
		groupFolder1.getRightValue();
		
		// test deleting an object
		groupFolderDAO.makeTransient(topFolder);
		tm.commit(ts);
		

		// make sure it is gone
        // Start the transaction this is for lazy loading
        ts = tm.getTransaction(td);
        
		assert groupFolderDAO.getById(groupFolder1.getId(), true) == null : "Should not find  TreeFolderInfo1";
		assert groupFolderDAO.getById(groupFolder2.getId(), true).equals(
				groupFolder2) : "should find TreeFolderInfo2";
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		// clean up the rest
		groupFolderDAO.makeTransient(groupFolderDAO.getById(groupFolder2.getId(), true));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
        groupSpaceDAO.makeTransient(groupSpaceDAO.getById(groupSpace.getId(), false));
		tm.commit(ts);

		ts = tm.getTransaction(td);
		assert groupFolderDAO.getById(groupFolder2.getId(), false) == null : "should not find TreeFolderInfo2";
		tm.commit(ts);

	}
	
	
	/**
	 * Test moving group folders across two different root folders.
	 * 
	 * @throws DuplicateNameException 
	 * @throws IllegalFileSystemNameException 
	 */
	@Test
	public void moveGroupFoldersTest() throws DuplicateNameException, IllegalFileSystemNameException
	{
		TransactionStatus ts = tm.getTransaction(td);
		UserEmail userEmail = new UserEmail("user@email");

        // create a user who has their own folder
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		GroupSpace groupSpace = new GroupSpace("grouName", "groupDescription");
		groupSpaceDAO.makePersistent(groupSpace);
		
		// persist the user.
		userDAO.makePersistent(user);
		
		GroupFolder folderA = groupSpace.createRootFolder(user, "Folder A");
		GroupFolder folderC = groupSpace.createRootFolder(user, "Folder C");
		
		GroupFolder folderA1 = folderA.createChild("Folder A1", user);
		
		
		GroupFolder folderAA1 = folderA1.createChild("Folder AA1", user);
		GroupFolder folderAA2 = folderA1.createChild("Folder AA2", user);
		
		folderC.createChild("Folder C1", user);

		// save folder c first because it is the new parent
		groupFolderDAO.makePersistent(folderC);
		groupFolderDAO.makePersistent(folderA);
		
		folderC.addChild(folderA1);
		tm.commit(ts);
		
		assert folderAA1.getTreeRoot().equals(folderC) : "Root folder should be " + 
		folderC + " but is " + folderAA1.getTreeRoot();
		assert folderAA2.getTreeRoot().equals(folderC) : "Root folder should be " + 
		folderC + " but is " + folderAA2.getTreeRoot();
		
		ts = tm.getTransaction(td);
		groupFolderDAO.makeTransient(groupFolderDAO.getById(folderA.getId(), true));
		groupFolderDAO.makeTransient(groupFolderDAO.getById(folderC.getId(), true));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		groupSpaceDAO.makeTransient(groupSpaceDAO.getById(groupSpace.getId(), false));
		tm.commit(ts);
	}
	
	
	/**
	 * Test - get root personal folder 
	 * @throws DuplicateNameException 
	 * 
	*/
	@Test
	public void baseGetRootPersonalFolderDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException {

        TransactionStatus ts = tm.getTransaction(td);

		UserEmail userEmail = new UserEmail("user@email");

        // create a user who has their own folder
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		
		GroupSpace groupSpace = new GroupSpace("grouName", "groupDescription");
		groupSpaceDAO.makePersistent(groupSpace);

		// persist user.
		userDAO.makePersistent(user);
		GroupFolder groupFolder = groupSpace.createRootFolder(user, "topFolder");
		groupFolderDAO.makePersistent(groupFolder);
		
		GroupFolder groupFolder1 = groupSpace.createRootFolder(user, "topFolder1");
		groupFolderDAO.makePersistent(groupFolder1);

		tm.commit(ts);
		
		
		// Start the transaction 
        ts = tm.getTransaction(td);
        groupSpace = groupSpaceDAO.getById(groupSpace.getId(), false);
        assert groupSpace.getRootFolders().size() == 2 : "Should be equal to 2 but equals " + groupSpace.getRootFolders().size();
		tm.commit(ts);

        ts = tm.getTransaction(td);
		groupFolderDAO.makeTransient(groupFolderDAO.getById(groupFolder.getId(), false));
		groupFolderDAO.makeTransient(groupFolderDAO.getById(groupFolder1.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert groupFolderDAO.getById(groupFolder.getId(), false) == null: 
			"Should not be able to find personal folder";
		assert groupFolderDAO.getById(groupFolder1.getId(), false) == null: 
			"Should not be able to find personal folder";

		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		groupSpaceDAO.makeTransient(groupSpaceDAO.getById(groupSpace.getId(), false));

		tm.commit(ts);
		
	}

	/**
	 * Test - get folders within a folder
	 * @throws DuplicateNameException 
	 * 
	*/
	@Test
	public void baseGetFolderInsideFolderDAOTest() throws DuplicateNameException,  IllegalFileSystemNameException {
  		
		TransactionStatus ts = tm.getTransaction(td);
		UserEmail userEmail = new UserEmail("user@email");

        // create a user who has their own folder
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		GroupSpace groupSpace = new GroupSpace("grouName", "groupDescription");
		groupSpaceDAO.makePersistent(groupSpace);

		// create the user and their folder.
		userDAO.makePersistent(user);
		GroupFolder groupFolder = groupSpace.createRootFolder(user, "topFolder");
		groupFolderDAO.makePersistent(groupFolder);
		
		GroupFolder groupFolder1 = null;
		GroupFolder groupFolder2 = null;
		try
		{
		    groupFolder1 = groupFolder.createChild("subFolder1",user);
		    groupFolderDAO.makePersistent(groupFolder1);
		
		    groupFolder2 = groupFolder.createChild("subFolder2",user);
		    groupFolderDAO.makePersistent(groupFolder2);
		}
		catch(Exception e)
		{
			throw new IllegalStateException(e);
		}
		tm.commit(ts);
		
		// Start the transaction 
        ts = tm.getTransaction(td);
        groupSpace = groupSpaceDAO.getById(groupSpace.getId(), false);
        assert groupSpace.getRootFolders().size() == 1 : "Should be equal to 1 but equals " + groupSpace.getRootFolders().size();
		tm.commit(ts);

        ts = tm.getTransaction(td);
		groupFolderDAO.makeTransient(groupFolderDAO.getById(groupFolder1.getId(), false));
		groupFolderDAO.makeTransient(groupFolderDAO.getById(groupFolder2.getId(), false));
		groupFolderDAO.makeTransient(groupFolderDAO.getById(groupFolder.getId(), false));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		assert groupFolderDAO.getById(groupFolder.getId(), false) == null: 
			"Should not be able to find personal folder";
		assert groupFolderDAO.getById(groupFolder1.getId(), false) == null: 
			"Should not be able to find personal folder";

		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		groupSpaceDAO.makeTransient(groupSpaceDAO.getById(groupSpace.getId(), false));
		
		tm.commit(ts);
		
	}




}
