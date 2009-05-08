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

import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.item.VersionedItemDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.PersonalItem;
import edu.ur.ir.user.PersonalItemDAO;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserManager;

/**
 * Class for testing personal item persistence.
 * 
 * @author Nathan Sarr  
 *
 */
public class PersonalItemDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/**  Transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
			.getBean("transactionManager");

	/** transaction definition  */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);

	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();

    /** User relational data access   */
    IrUserDAO userDAO= (IrUserDAO) ctx
	.getBean("irUserDAO");
    
    /** personal item relational data access */
    PersonalItemDAO personalItemDAO = (PersonalItemDAO) 
    ctx.getBean("personalItemDAO");
    
    VersionedItemDAO versionedItemDAO = 
    	(VersionedItemDAO) ctx.getBean("versionedItemDAO");
    
    /** Generic item data access*/
    GenericItemDAO itemDAO = (GenericItemDAO)ctx.getBean("itemDAO");
    
	/**
	 * Test personal item persistence
	 * 
	 */
	@Test
	public void rootPersonalItemDAOTest() {

		TransactionStatus ts = tm.getTransaction(td);
		UserEmail userEmail = new UserEmail("user@email");

		// create a user who has their own folder
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("none");
		user.addUserEmail(userEmail, true);
		
		// create the user 
		userDAO.makePersistent(user);
		tm.commit(ts);
		

		ts = tm.getTransaction(td);
		GenericItem genericItem = new GenericItem("aItem");
		VersionedItem versionedItem = new VersionedItem(user, genericItem, "myItem");
		PersonalItem personalItem = new PersonalItem(user, versionedItem);
		personalItemDAO.makePersistent(personalItem);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		PersonalItem other = personalItemDAO.getById(personalItem.getId(), false);
		VersionedItem otherVersionedItem = other.getVersionedItem();
		assert otherVersionedItem != null : "Versioned Item should be found";
		assert otherVersionedItem.getOwner() != null : "Owner should not be null";
		assert other.equals(personalItem) : "The personal item " + personalItem + " should be found";
		
		personalItemDAO.makeTransient(other);
		versionedItemDAO.makeTransient(otherVersionedItem);
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
	    userDAO.makeTransient(userDAO.getById(user.getId(), false));
	    tm.commit(ts);
	
	}
	
	/**
	 * Test retrieveing a personal item by a generic item id
	 * 
	*/
	@Test
	public void getPersonalItemByGenericItemIdDAOTest() {

		TransactionStatus ts = tm.getTransaction(td);
		UserEmail userEmail = new UserEmail("user@email");

		// create a user who has their own folder
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("none");
		user.addUserEmail(userEmail, true);
		
		// create the user 
		userDAO.makePersistent(user);
		tm.commit(ts);
		

		ts = tm.getTransaction(td);
		GenericItem genericItem = new GenericItem("aItem");
		GenericItem genericItem2 = new GenericItem("aSecondItem");
		VersionedItem versionedItem = new VersionedItem(user, genericItem, "myItem");
		versionedItem.addNewVersion(genericItem2);
		PersonalItem personalItem = new PersonalItem(user, versionedItem);
		personalItemDAO.makePersistent(personalItem);
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		PersonalItem other = personalItemDAO.getById(personalItem.getId(), false);
		VersionedItem otherVersionedItem = other.getVersionedItem();
		assert otherVersionedItem != null : "Versioned Item should be found";
		assert otherVersionedItem.getOwner() != null : "Owner should not be null";
		assert other.equals(personalItem) : "The personal item " + personalItem + " should be found";
		
		// make sure select works 
		PersonalItem other2 = personalItemDAO.getPersonalItem(genericItem.getId());
		assert other2.equals(other) : " other 2 = " + other2 + " other = " + other;
		
		PersonalItem other3 = personalItemDAO.getPersonalItem(genericItem2.getId());
		assert other3.equals(other) : " other 3 = " + other3 + " other = " + other;
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		personalItemDAO.makeTransient(personalItemDAO.getById(personalItem.getId(), false));
		versionedItemDAO.makeTransient(versionedItemDAO.getById(versionedItem.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItem2.getId(), false));
	    userDAO.makeTransient(userDAO.getById(user.getId(), false));
	    tm.commit(ts);
	}
	 
	/**
	 * Test getting the set of root personal items
	 */
	@Test
	public void getRootPersonalItemsDAOTest()throws Exception{
		
		UserEmail userEmail = new UserEmail("user@email");
  		
  		UserManager userManager = new UserManager();
		IrUser user = userManager.createUser("passowrd", "userName");
		user.addUserEmail(userEmail, true);
		user.setAccountExpired(true);
		user.setAccountLocked(true);
		user.setCredentialsExpired(true);

		userDAO.makePersistent(user);
		
        TransactionStatus ts = tm.getTransaction(td);
        
		IrUser other = userDAO.getById(user.getId(), false);
		GenericItem genericItem = new GenericItem("aItem");
		VersionedItem versionedItem = new VersionedItem(user, genericItem, "myItem");
		PersonalItem personalItem = other.createRootPersonalItem(versionedItem);

		//complete the transaction
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		other = userDAO.getById(user.getId(), false);
		assert other != null : "Other should not be null";
		
		Set<PersonalItem> personalItems = other.getRootPersonalItems();
		
		assert personalItems != null : "Should be able to find the personal items";
		assert personalItems.size() == 1 : "Root personal items should be 1 but is " +
		    personalItems.size();
		
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		personalItemDAO.makeTransient(personalItemDAO.getById(personalItem.getId(), false));
		versionedItemDAO.makeTransient(versionedItemDAO.getById(versionedItem.getId(), false));
		itemDAO.makeTransient(itemDAO.getById(genericItem.getId(), false));
		userDAO.makeTransient(userDAO.getById(other.getId(), false));
		tm.commit(ts);
		
		assert userDAO.getById(other.getId(), false) == null : "Should not be able to find other";
	}

}
