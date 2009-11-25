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

package edu.ur.hibernate.ir.institution.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.institution.VersionedInstitutionalItem;
import edu.ur.ir.institution.VersionedInstitutionalItemDAO;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.item.VersionedItemDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;

/**
 * Test the persistence methods for a versioned Institutional Item
 * 
 * @author Sharmila Ranganathan
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class VersionedInstitutionalItemDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

    /** Versioned item data access*/
    VersionedItemDAO versionedItemDAO = (VersionedItemDAO)ctx.getBean("versionedItemDAO");
    
    VersionedInstitutionalItemDAO versionedInstitutionalItemDAO = (VersionedInstitutionalItemDAO)ctx.getBean("versionedInstitutionalItemDAO");

	/** Platform transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");

	/** Default transaction definition   */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

    /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");

	public void basicVersionedItemTest()
	{
	    GenericItem item = new GenericItem("myItem");
	    
		UserEmail userEmail = new UserEmail("email");
		
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);

	    VersionedInstitutionalItem vii = new VersionedInstitutionalItem(item);
	    
	    TransactionStatus ts = tm.getTransaction(td);
		userDAO.makePersistent(user);
	    versionedInstitutionalItemDAO.makePersistent(vii);

	    tm.commit(ts);
	    
	    ts = tm.getTransaction(td);
	    
	    VersionedInstitutionalItem other = versionedInstitutionalItemDAO.getById(vii.getId(), false);
	    assert other.equals(vii): "Other " + other + " should equal " + vii;
	    
	    int size = vii.getInstitutionalItemVersions().size();
	    assert size == 1 : "Should find one published version item but found " + size;
	    
	    assert vii.getCurrentVersion().getItem().equals(item) : "Item should be equal";
	    
	    versionedInstitutionalItemDAO.makeTransient(other);
	    tm.commit(ts);
	    
	    ts = tm.getTransaction(td);
	    assert versionedInstitutionalItemDAO.getById(vii.getId(), false) == null : 
	    	"Should not be able to find versioned institutional item " + vii;
	    
	    /** user data access  */
	    GenericItemDAO itemDAO= (GenericItemDAO) ctx.getBean("itemDAO");
	    itemDAO.makeTransient(itemDAO.getById(item.getId(), false));
	    
	    userDAO.makeTransient(userDAO.getById(user.getId(), true));
	    tm.commit(ts);
	}
	
	
}
