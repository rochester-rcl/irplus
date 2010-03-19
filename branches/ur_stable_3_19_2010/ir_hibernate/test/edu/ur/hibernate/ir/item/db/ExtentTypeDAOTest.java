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
import edu.ur.ir.item.ExtentType;
import edu.ur.ir.item.ExtentTypeDAO;


/**
 * Test the persistance methods for extent type Information
 * 
 * @author Sharmila Rangnathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ExtentTypeDAOTest {

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	ExtentTypeDAO extentTypeDAO = (ExtentTypeDAO) ctx
	.getBean("extentTypeDAO");

	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

	
	/**
	 * Test extent type persistence
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseExtentTypeDAOTest() throws Exception{
		
		ExtentType extentType = new ExtentType();
		extentType.setName("extentTypeName");
 		extentType.setDescription("extentTypeDescription");

        // Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
 		extentTypeDAO.makePersistent(extentType);
 		tm.commit(ts);
 		
 		ts = tm.getTransaction(td);
 		ExtentType other = extentTypeDAO.getById(extentType.getId(), false);
        assert other.equals(extentType) : "Idententifier types should be equal";
         
        List<ExtentType> itemExtentTypes =  extentTypeDAO.getAllOrderByName(0, 1);
        assert itemExtentTypes.size() == 1 : "One extent type should be found";
         
        assert extentTypeDAO.getAllNameOrder().size() == 1 : "One extent type should be found";
         
        ExtentType itemExtentTypeByName =  extentTypeDAO.findByUniqueName(extentType.getName());
        assert itemExtentTypeByName.equals(extentType) : "Extent types should be found";
         
        extentTypeDAO.makeTransient(other);
        assert  extentTypeDAO.getById(other.getId(), false) == null : 
        	 "Should no longer be able to find extent type";
        tm.commit(ts);
	}

}
