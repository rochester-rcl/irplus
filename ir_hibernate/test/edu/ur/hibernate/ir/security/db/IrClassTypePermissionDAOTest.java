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

package edu.ur.hibernate.ir.security.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.repository.InMemoryRepositoryService;
import edu.ur.ir.security.IrClassType;
import edu.ur.ir.security.IrClassTypeDAO;
import edu.ur.ir.security.IrClassTypePermission;
import edu.ur.ir.security.IrClassTypePermissionDAO;


/**
 * Test the persistence methods for Class Type Permissions
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrClassTypePermissionDAOTest {
	
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

    // create the class type
    IrClassTypeDAO irClassTypeDAO = (IrClassTypeDAO) ctx
    .getBean("irClassTypeDAO");

    IrClassTypePermissionDAO classTypePermissionDAO = 
    	(IrClassTypePermissionDAO) ctx.getBean("irClassTypePermissionDAO");
    
	/**
	 * Test irClassTypePermission persistance
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseClassTypePermissionDAOTest() throws Exception{


	    IrClassType irClassType = new IrClassType(InMemoryRepositoryService.class);
        irClassTypeDAO.makePersistent(irClassType);
 		
	
 		// create a permission for the class type
	    IrClassTypePermission classTypePermission = new IrClassTypePermission(irClassType);
		classTypePermission.setName("permissionName");
		classTypePermission.setDescription("permissionDescription");
 	    classTypePermissionDAO.makePersistent(classTypePermission);
	    
		PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

        // Start the transaction this is for lazy loading
		TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus ts = tm.getTransaction(td);
		
	    IrClassTypePermission other = classTypePermissionDAO.getById(classTypePermission.getId(), false);
	    assert other.equals(classTypePermission) : "Class type Permissions should be equal";
	    
	    assert other.getIrClassType().equals(irClassType) : "Should be equal";
	    assert other.getName().equals("permissionName") : "Should be equal";
	         
	    IrClassTypePermission classTypePermissionByNameClass =  
	    	classTypePermissionDAO.getClassTypePermissionByNameAndClassType(irClassType.getName(), 
	    			classTypePermission.getName());
	    assert classTypePermissionByNameClass.equals(classTypePermission) : "Class Type Permission should be found";
	    
        // commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
	    classTypePermissionDAO.makeTransient(other);
	    irClassTypeDAO.makeTransient(irClassType);
	    
	    assert  classTypePermissionDAO.getById(other.getId(), false) == null : 
	    	"Should no longer be able to find classType Permission";

	}
}
