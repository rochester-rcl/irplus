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

import java.util.Date;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.hibernate.ir.test.helper.PropertiesLoader;
import edu.ur.ir.institution.DeletedInstitutionalItem;
import edu.ur.ir.institution.DeletedInstitutionalItemDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserDAO;
import edu.ur.ir.user.UserEmail;

/**
 * Test the persistence methods for an institutional item Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DeletedInstitutionalItemDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** Platform transaction manager  */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	/** Transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

	
	/** Properties file with testing specific information. */
	PropertiesLoader propertiesLoader = new PropertiesLoader();
	
	/** Get the properties file  */
	Properties properties = propertiesLoader.getProperties();
	
    /** user data access  */
    IrUserDAO userDAO= (IrUserDAO) ctx.getBean("irUserDAO");

    /** Deleted Institutional Item data access*/
    DeletedInstitutionalItemDAO deletedInstitutionalItemDAO = (DeletedInstitutionalItemDAO)ctx.getBean("deletedInstitutionalItemDAO");


	
	/**
	 * Test deleted Institutional item persistence
	 * 
	 */
	@Test
	public void baseDeletedInstitutionalItemDAOTest() {

	    // start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		
		UserEmail userEmail = new UserEmail("email");
				
		IrUser user = new IrUser("user", "password");
		user.setPasswordEncoding("encoding");
		user.addUserEmail(userEmail, true);

        userDAO.makePersistent(user);
        
        DeletedInstitutionalItem deletedInstitutionalItem = new DeletedInstitutionalItem(1l, "itemName", "collectionName");
        deletedInstitutionalItem.setDeletedBy(user);
        deletedInstitutionalItem.setDeletedDate(new Date());
		deletedInstitutionalItemDAO.makePersistent(deletedInstitutionalItem);

		tm.commit(ts);

		ts = tm.getTransaction(td);
		assert deletedInstitutionalItemDAO.getById(deletedInstitutionalItem.getId(), false).equals(deletedInstitutionalItem) :
			"Should be able to find deleted item " + deletedInstitutionalItem;
		
		tm.commit(ts);

		//create a new transaction
		ts = tm.getTransaction(td);
		deletedInstitutionalItemDAO.makeTransient(deletedInstitutionalItemDAO.getById(deletedInstitutionalItem.getId(), false));
		userDAO.makeTransient(userDAO.getById(user.getId(), false));
		
		tm.commit(ts);	
	}
	

}
