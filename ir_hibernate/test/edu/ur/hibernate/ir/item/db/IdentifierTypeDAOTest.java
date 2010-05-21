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

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeDAO;


/**
 * Test the persistance methods for identifier type Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IdentifierTypeDAOTest {

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	IdentifierTypeDAO identifierType = (IdentifierTypeDAO) ctx
	.getBean("identifierTypeDAO");

	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

	
	/**
	 * Test identifier type persistance
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseIdentifierTypeDAOTest() throws Exception{
		
		IdentifierType identType = new IdentifierType("identTypeName","identTypeDescription" );

        // Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
 		 identifierType.makePersistent(identType);
 		tm.commit(ts);
 		
 		ts = tm.getTransaction(td);
 		 IdentifierType other = identifierType.getById(identType.getId(), false);
         assert other.equals(identType) : "Idententifier types should be equal";
         
         List<IdentifierType> itemIdentifierTypes =  identifierType.getAllOrderByName(0, 1);
         assert itemIdentifierTypes.size() == 1 : "One identifier type should be found";
         
         assert identifierType.getAllNameOrder().size() == 1 : "One identifier type should be found";
         
         IdentifierType itemIdentifierTypeByName =  identifierType.findByUniqueName(identType.getName());
         assert itemIdentifierTypeByName.equals(identType) : "Identifier types should be found";
         
         identifierType.makeTransient(other);
         assert  identifierType.getById(other.getId(), false) == null : 
        	 "Should no longer be able to find identifier type";
         tm.commit(ts);
	}
}
