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

package edu.ur.hibernate.ir.handle.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.handle.UniqueHandleNameGenerator;

/**
 * Class for testing handle name authority persistence.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class UniqueHandleNameDAOTest 
{
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	/** used to store handle name authority data */
	UniqueHandleNameGenerator uniqueHandleNameGenerator = (UniqueHandleNameGenerator) ctx
	.getBean("uniqueHandleNameGenerator");
	
	/** transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test basic persistance.
	 * 
	 * @throws Exception
	 */
	public void baseHandleNameAuthorityDAOTest() throws Exception
	{
		TransactionStatus ts = tm.getTransaction(td);
		String handleName1 = uniqueHandleNameGenerator.nextName();
		String handleName2 = uniqueHandleNameGenerator.nextName();
		assert !handleName1.equals(handleName2) : "Handle names should not be the same handleName 1 = " 
			+ handleName1 + " handleName2= " + handleName2;
		tm.commit(ts);
	}

}
