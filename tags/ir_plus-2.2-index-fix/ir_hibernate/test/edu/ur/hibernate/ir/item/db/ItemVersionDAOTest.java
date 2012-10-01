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

package edu.ur.hibernate.ir.item.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.item.ItemVersion;
import edu.ur.ir.item.ItemVersionDAO;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.item.VersionedItemDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;


/**
 * Test the persistence methods for Item Version Information
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ItemVersionDAOTest {

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

	/** versioned item data access  */
	ItemVersionDAO itemVersionDAO = (ItemVersionDAO)ctx.getBean("itemVersionDAO");
	
    /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");
    
    /** Versioned item data access */
    VersionedItemDAO versionedItemDAO = (VersionedItemDAO) ctx.getBean("versionedItemDAO");

    GenericItemDAO itemDAO= (GenericItemDAO) ctx.getBean("itemDAO");
    


	/**
	 * Makes sure an item version can be saved and retrieved.
	 */
	public void basicItemVersionDAOTest()
	{
	    TransactionStatus ts = tm.getTransaction(td);
		UserEmail userEmail = new UserEmail("email");
		GenericItem item = new GenericItem("myItem");
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);
		VersionedItem versionedItem = new VersionedItem(user, item);
		ItemVersion itemVersion = versionedItem.getCurrentVersion();
	    userDAO.makePersistent(user);
	    itemVersionDAO.makePersistent(itemVersion);
	    ItemVersion otherItemVersion = itemVersionDAO.getById(itemVersion.getId(), false);
	    versionedItem = otherItemVersion.getVersionedItem();
	    assert versionedItem != null : "otherItemVersion version is null " + otherItemVersion;
	    assert versionedItem.getId() != null : "versioned item id is null " + versionedItem.getId();
	    
	    assert otherItemVersion != null : "should be able to find item version with id " + itemVersion.getId();
	    assert otherItemVersion.equals(itemVersion) : "GenericItem version " + itemVersion + " should equal other " 
	    + otherItemVersion;
	    tm.commit(ts);
	    
	    ts = tm.getTransaction(td);
	    assert itemVersionDAO.getById(itemVersion.getId(), false) != null : "should be able to find item " + itemVersion;
	    
	    assert versionedItemDAO.getById(versionedItem.getId(), false) != null : "Should be able to get versionedItem " + versionedItem;
	    versionedItemDAO.makeTransient(versionedItemDAO.getById(versionedItem.getId(), false));
		   
	    assert itemVersionDAO.getById(itemVersion.getId(), false) == null : "Should not be able to find " 
	    	+ itemVersion.getId();

	    itemDAO.makeTransient(itemDAO.getById(item.getId(), false));
	    userDAO.makeTransient(userDAO.getById(user.getId(), true));
	    
	    tm.commit(ts);
	
	}

}
