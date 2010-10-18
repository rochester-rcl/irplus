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

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrRoleDAO;

/**
 * Test the persistence methods for IrRoles 
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrRoleDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	IrRoleDAO irRoleDAO = (IrRoleDAO) ctx.getBean("irRoleDAO");
	
	// Transaction manager
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
    		TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test role persistence
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseRoleDAOTest() throws Exception{

        TransactionStatus ts = tm.getTransaction(td);
		IrRole role = new IrRole();
		role.setName("aRoleName");
 		role.setDescription("roleDescription");
         
 		irRoleDAO.makePersistent(role);
 		tm.commit(ts);
 		IrRole other = irRoleDAO.getById(role.getId(), false);
        assert other.equals(role) : "Role types should be equal";
         
        List<IrRole> itemIrRoles =  irRoleDAO.getAllOrderByName(0, 1);
        assert itemIrRoles.size() == 1 : "One role should be found but found " + itemIrRoles.size();
         
        assert irRoleDAO.getAllNameOrder().size() == 1 : "One role should be found but found : " + irRoleDAO.getAllNameOrder().size();
         
        IrRole itemIrRoleByName =  irRoleDAO.findByUniqueName(role.getName());
        assert itemIrRoleByName.equals(role) : "Role should be found";
         
        irRoleDAO.makeTransient(other);
        assert  irRoleDAO.getById(other.getId(), false) == null : "Should no longer be able to find role";
	}

}
