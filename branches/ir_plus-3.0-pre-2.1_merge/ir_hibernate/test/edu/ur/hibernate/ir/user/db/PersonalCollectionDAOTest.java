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


import java.util.List;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.item.VersionedItemDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.PersonalCollection;
import edu.ur.ir.user.PersonalCollectionDAO;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.PersonalItemDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserManager;

/**
 * Test the persistence methods for personal collections
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class PersonalCollectionDAOTest {
	
	
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
	
	/** Personal Collection data access */
	PersonalCollectionDAO personalCollectionDAO = (PersonalCollectionDAO) ctx
	.getBean("personalCollectionDAO");

    /** User data access */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");
    
    /** personal item relational data access */
    PersonalItemDAO personalItemDAO = (PersonalItemDAO) 
    ctx.getBean("personalItemDAO");
    
    /** versioned item data access */
    VersionedItemDAO versionedItemDAO = 
    	(VersionedItemDAO) ctx.getBean("versionedItemDAO");
    
    /** Generic item data access*/
    GenericItemDAO itemDAO = (GenericItemDAO)ctx.getBean("itemDAO");
    
	/**
	 * Test personal collection persistence
	 * 
	 */
	@Test
	public void basePersonalFolderDAOTest() throws DuplicateNameException{
  		
		UserEmail userEmail = new UserEmail("user@email");

        // create a user who has their own collection
		IrUser user = new IrUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setPasswordEncoding("none");
		
		// create the user and their collection.
		userDAO.makePersistent(user);
		PersonalCollection personalCollection = user.createRootPersonalCollection("topCollection");
		personalCollectionDAO.makePersistent(personalCollection);
		
		assert personalCollectionDAO.getById(personalCollection.getId(), false) != null : "Should be able to find "
			+ " personal collection " + personalCollection;
		
        // Start the transaction 
        TransactionStatus ts = tm.getTransaction(td);
		assert personalCollectionDAO.getById(personalCollection.getId(), false).equals(personalCollection) : 
			"collections should be equal";
		
		tm.commit(ts);
		
		personalCollectionDAO.makeTransient(personalCollectionDAO.getById(personalCollection.getId(), false));
		
		assert personalCollectionDAO.getById(personalCollection.getId(), false) == null: 
			"Should not be able to find personal collection";
		
		ts = tm.getTransaction(td);
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		
		
	}
	
	/**
	 * Test get all personal collection for user
	 * 
	 */
	@Test
	public void getAllPersonalFolderDAOTest() throws DuplicateNameException{
  		
		UserEmail userEmail = new UserEmail("user@email");

        // create a user who has their own collection
		IrUser user = new IrUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setPasswordEncoding("none");
		
		// create the user and their collection.
		userDAO.makePersistent(user);
		PersonalCollection personalCollection = user.createRootPersonalCollection("topCollection");
		personalCollectionDAO.makePersistent(personalCollection);
		
		PersonalCollection personalCollection1 = user.createRootPersonalCollection("topCollection1");
		personalCollectionDAO.makePersistent(personalCollection1);

		assert personalCollectionDAO.getAllPersonalCollectionsForUser(user.getId()).size() == 2 : "Should be equal to 2";
		
		personalCollectionDAO.makeTransient(personalCollectionDAO.getById(personalCollection.getId(), false));
		
		personalCollectionDAO.makeTransient(personalCollectionDAO.getById(personalCollection1.getId(), false));
		
		assert personalCollectionDAO.getById(personalCollection.getId(), false) == null: 
			"Should not be able to find personal collection";
		assert personalCollectionDAO.getById(personalCollection1.getId(), false) == null: 
			"Should not be able to find personal collection1";

		
        // Start the transaction 
        TransactionStatus ts = tm.getTransaction(td);
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
		
		
	}
	
	/**
	 * Test PersonalCollection persistence with children
	 * 
	 */
	@Test
	public void personalCollectionWithChildrenDAOTest() throws DuplicateNameException{
		UserEmail userEmail = new UserEmail("user@email");

         // create a user who has their own collection
 		IrUser user = new IrUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setPasswordEncoding("none");
		
		// create the user and their collection.
		userDAO.makePersistent(user);
		PersonalCollection personalCollection1 = user.createRootPersonalCollection("topCollection");
		personalCollectionDAO.makePersistent(personalCollection1);
		
		// make sure parent starts out with 1 and 2 left/right values.
		assert personalCollection1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalCollection1.getLeftValue();

		assert personalCollection1.getRightValue() == 2L : "Right value should be 2 but is " + 
		personalCollection1.getRightValue();

		PersonalCollection personalCollection2 = user.createRootPersonalCollection("topCollection2");
		
		assert personalCollection2.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalCollection2.getLeftValue();

		assert personalCollection2.getRightValue() == 2L : "Right value should be 2 but is " + 
		personalCollection2.getRightValue();
		
		// add a child
		PersonalCollection childFolder1 = personalCollection1.createChild("childFolder1");
		
		assert personalCollection1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalCollection1.getLeftValue();

		assert personalCollection1.getRightValue() == 4L : "Right value should be 2 but is " + 
		personalCollection1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 1 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 3L : "Right value should be 2 but is " + 
		childFolder1.getRightValue();
		
		
		// add another child
		PersonalCollection childFolder2 = personalCollection1.createChild("child2");
		
		assert personalCollection1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalCollection1.getLeftValue();

		assert personalCollection1.getRightValue() == 6L : "Right value should be 2 but is " + 
		personalCollection1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 1 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 3L : "Right value should be 2 but is " + 
		childFolder1.getRightValue();
		
		assert childFolder2.getLeftValue() == 4L : "Left value should be 1 but is " + 
		childFolder2.getLeftValue();

		assert childFolder2.getRightValue() == 5L : "Right value should be 2 but is " + 
		childFolder2.getRightValue();
		
		
		// add another child
		PersonalCollection subFolder1 = childFolder1.createChild("subFolder1");
		
		
		assert personalCollection1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalCollection1.getLeftValue();

		assert personalCollection1.getRightValue() == 8L : "Right value should be 8 but is " + 
		personalCollection1.getRightValue();
		
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
		PersonalCollection subCollection2 = childFolder1.createChild("subCollection2");
		
		
		assert personalCollection1.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalCollection1.getLeftValue();

		assert personalCollection1.getRightValue() == 10L : "Right value should be 10 but is " + 
		personalCollection1.getRightValue();
		
		assert childFolder1.getLeftValue() == 2L : "Left value should be 2 but is " + 
		childFolder1.getLeftValue();

		assert childFolder1.getRightValue() == 7L : "Right value should be 7 but is " + 
		childFolder1.getRightValue();
		
		assert subFolder1.getLeftValue() == 3L : "Left value should be 3 but is " + 
		subFolder1.getLeftValue();

		assert subFolder1.getRightValue() == 4L : "Right value should be 4 but is " + 
		subFolder1.getRightValue();
		
		assert subCollection2.getLeftValue() == 5L : "Left value should be 5 but is " + 
		subFolder1.getLeftValue();

		assert subCollection2.getRightValue() == 6L : "Right value should be 6 but is " + 
		subFolder1.getRightValue();
		
		assert childFolder2.getLeftValue() == 8L : "Left value should be 8 but is " + 
		childFolder2.getLeftValue();

		assert childFolder2.getRightValue() == 9L : "Right value should be 9 but is " + 
		childFolder2.getRightValue();
		


		// persist the tree
		personalCollectionDAO.makePersistent(personalCollection1);
		personalCollectionDAO.makePersistent(personalCollection2);
		
		
		/**********************************
		 * TEST TREE SELECTS
		 **********************************/
        TransactionStatus ts = tm.getTransaction(td);

		
		// make sure we can grab nodes that have a left and right value greater than
		// a specified value.
		List<PersonalCollection> nodes = personalCollectionDAO.getNodesLeftRightGreaterEqual(personalCollection1.getRightValue(),
				childFolder1.getTreeRoot().getId());
		
		assert nodes.size() == 1 : "Expected root node but got "  + nodes.size();
		
		nodes = personalCollectionDAO.getNodesLeftRightGreaterEqual(personalCollection1.getLeftValue(),
				childFolder1.getTreeRoot().getId());
		
		assert nodes.size() == 5 : "Expected all 5 nodes but got " + nodes.size();

		
		// make sure tree without specified nodes can be retrieved
		nodes = personalCollectionDAO.getAllNodesNotInChildTree(personalCollection1);
        assert nodes.size() == 0 : "Nodes size should be zero but is " + nodes.size() + "using data "
            + " id = " + personalCollection1.getId() + " and root id = " + personalCollection1.getTreeRoot().getId();
		
        nodes = personalCollectionDAO.getAllNodesNotInChildTree(childFolder1);
        assert nodes.size() == 2 : "Nodes size should be 2 but is " + nodes.size() + " using data "
         + " id = " + childFolder1.getId() + " and root id = " + personalCollection1.getTreeRoot().getId();
        tm.commit(ts);
        

        /*****************************************
         * END TEST TREE SELECTS
         *****************************************/
        // Start the transaction this is for lazy loading
        ts = tm.getTransaction(td);
        
		// make sure object has been persisted
		PersonalCollection other = personalCollectionDAO.getById(personalCollection1.getId(), false);
		assert other != null : "PersonalCollection should be found";
		assert other.equals(personalCollection1) : "PersonalCollection should be the same as is1 ";

		// make sure types are correct
		PersonalCollection other2 = personalCollectionDAO.getById(personalCollection2.getId(), false);
		assert !other.equals(other2) : "PersonalCollection should be different";

		assert other.getChild(childFolder1.getName()).equals(
				childFolder1) : "PersonalCollection should have child";
		assert other.getChild(childFolder2.getName()).equals(
				childFolder2) : "PersonalCollection should have child";

		String name = childFolder1.getName();
		assert other.getChild(name).getChild(subFolder1.getName())
				.equals(subFolder1) : "PersonalCollection should have child";
		assert other.getChild(name).getChild(subCollection2.getName())
				.equals(subCollection2) : "PersonalCollection should have child";

		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		//*************************************
		// Make sure we can add new nodes after
		// data has been committed.
        //*************************************
	
		// Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);

		//PersonalCollection myRoot = personalCollectionDAO.getById(personalCollection1.getId(), false);
		// make sure object has been persisted
		PersonalCollection akaSubTreeFolderInfo1 = personalCollectionDAO.getById(subFolder1.getId(), false);
		
		assert akaSubTreeFolderInfo1.getLeftValue() == 3L : "Left value should be 5 but is " + 
		akaSubTreeFolderInfo1.getLeftValue();

		assert akaSubTreeFolderInfo1.getRightValue() == 4L : "Right value should be 6 but is " + 
		akaSubTreeFolderInfo1.getRightValue();
		
		assert !akaSubTreeFolderInfo1.isRoot(): "Should not be root collection";
		
		
		PersonalCollection akaSubTreeParent = akaSubTreeFolderInfo1.getParent();
		
		assert akaSubTreeParent.getLeftValue() == 2L : "Left value should be 2 but is " + 
		childFolder1.getLeftValue();

		assert akaSubTreeParent.getRightValue() == 7L : "Right value should be 7 but is " + 
		childFolder1.getRightValue();
		
		PersonalCollection akaRoot =  akaSubTreeParent.getParent();
		
		assert akaRoot.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalCollection1.getLeftValue();

		assert akaRoot.getRightValue() == 10L : "Right value should be 10 but is " + 
		personalCollection1.getRightValue();
		
		PersonalCollection subSubTreeFolderInfo1 = 
			akaSubTreeFolderInfo1.createChild("subSubFolder1");
		
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
		
		personalCollectionDAO.makePersistent(akaSubTreeFolderInfo1.getTreeRoot());
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		
        //*************************************
		// Start the transaction this is for lazy loading
		ts = tm.getTransaction(td);
		
		PersonalCollection topCollection = personalCollectionDAO.getById(personalCollection1.getId(), true);
		
		assert topCollection.getLeftValue() == 1L : "Left value should be 1 but is " + 
		personalCollection1.getLeftValue();

		assert topCollection.getRightValue() == 12L : "Right value should be 12 but is " + 
		personalCollection1.getRightValue();
		

		// test deleting an object
		personalCollectionDAO.makeTransient(topCollection);
		
		tm.commit(ts);
		

		// make sure it is gone

        // Start the transaction this is for lazy loading
        ts = tm.getTransaction(td);
        
		assert personalCollectionDAO.getById(personalCollection1.getId(), true) == null : "Should not find  TreeFolderInfo1";
		assert personalCollectionDAO.getById(personalCollection2.getId(), true).equals(
				personalCollection2) : "should find TreeFolderInfo2";
				
		tm.commit(ts);

		// clean up the rest
		personalCollectionDAO.makeTransient(personalCollectionDAO.getById(personalCollection2.getId(), true));
		
		assert personalCollectionDAO.getById(personalCollection2.getId(), false) == null : "should not find TreeFolderInfo2";
	    
		ts = tm.getTransaction(td);
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
	}

	/**
	 * Test adding files to a folder
	 * 
     */
	@Test
	public void personalCollectionItemDAOTest() throws DuplicateNameException{
		UserEmail userEmail = new UserEmail("user@email");

		TransactionStatus ts = tm.getTransaction(td);
		
		
	    // create a user who has their own collection
		IrUser user = new IrUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setPasswordEncoding("none");
		
		// create the user and their collection.
		userDAO.makePersistent(user);
		PersonalCollection personalCollection = user.createRootPersonalCollection("topCollection");
		personalCollectionDAO.makePersistent(personalCollection);
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		GenericItem genericItem = new GenericItem("aItem");
		VersionedItem versionedItem = new VersionedItem(user, genericItem);
		PersonalItem personalItem = personalCollection.addVersionedItem(versionedItem);
		personalCollectionDAO.makePersistent(personalCollection);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		
		PersonalCollection other = personalCollectionDAO.getById(personalCollection.getId(), false);
		assert other.getPersonalItem(personalItem.getId()) != null : "Personal item should not be null";
		PersonalItem compare2 = other.getPersonalItem(personalItem.getId());
		
		
		assert compare2.getId().equals(personalItem.getId()) : " compare id " + compare2.getId() +
		" should equal " + personalItem.getId();
		
		assert compare2.getId().equals(personalItem.getId()) : " compare id " + compare2.getId() +
		" should equal " + personalItem.getId();
		
		
		assert compare2.equals(personalItem) : "Personal item \n" +
		personalItem + " should be found and equal to \n" + compare2;
		
		// delete the collection
		user = other.getOwner();
		assert user.removeRootPersonalCollection(personalCollection) : "Collection should be removed " 
			+ personalCollection;
		personalCollectionDAO.makeTransient(other);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);

		assert personalCollectionDAO.getById(personalCollection.getId(), false) == null: 
			"Should not be able to find personal collection";
		
		versionedItemDAO.makeTransient(versionedItemDAO.getById(versionedItem.getId(), false));
	    itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
	}

	
	/**
	 * Test moving personal collections accross two different root collections.
	 * @throws DuplicateNameException 
	 */
	@Test
	public void moveCollectionsTest() throws DuplicateNameException
	{
		TransactionStatus ts = tm.getTransaction(td);
		UserEmail userEmail = new UserEmail("user@email");

        // create a user who has their own collection
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);
		
		// create the user and their collection.
		userDAO.makePersistent(user);
		PersonalCollection collectionA = user.createRootPersonalCollection("Collection A");
		PersonalCollection collectionC = user.createRootPersonalCollection("Collection C");
		
		PersonalCollection collectionA1 = collectionA.createChild("Collection A1");
		
		
		PersonalCollection collectionAA1 = collectionA1.createChild("Collection AA1");
		PersonalCollection collectionAA2 = collectionA1.createChild("Collection AA2");
		
		collectionC.createChild("Collection C1");
		
		personalCollectionDAO.makePersistent(collectionA);
		personalCollectionDAO.makePersistent(collectionC);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		// move the child
		collectionC.addChild(collectionA1);
		tm.commit(ts);

		ts = tm.getTransaction(td);
		assert collectionAA1.getTreeRoot().equals(collectionC) : "Root collection should be " + 
		collectionC + " but is " + collectionAA1.getTreeRoot();
		assert collectionAA2.getTreeRoot().equals(collectionC) : "Root collection should be " + 
		collectionC + " but is " + collectionAA2.getTreeRoot();
		
		personalCollectionDAO.makeTransient(personalCollectionDAO.getById(collectionA.getId(), true));
		personalCollectionDAO.makeTransient(personalCollectionDAO.getById(collectionC.getId(), true));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		tm.commit(ts);
	}


}
