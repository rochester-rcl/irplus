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


package edu.ur.ir.person.service;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeService;
import edu.ur.ir.repository.service.test.helper.ContextHolder;

/**
 * Test the service methods for contributor service
 *   
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultContributorTypeServiceTest {
	
	ApplicationContext ctx =  ContextHolder.getApplicationContext();
 	
	// Check the repositories
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	
	/** contributor type data access */
	ContributorTypeService contributorTypeService = (ContributorTypeService) ctx
	.getBean("contributorTypeService");
	
	/**
	 * Test getting contributor information
	 */
	public void testGetContributorType()
	{
		TransactionStatus ts = tm.getTransaction(td);
	    // create a contributor type
		ContributorType contributorType1 = new ContributorType("contributorType1");
		contributorType1.setUniqueSystemCode("AUTHOR");
		contributorTypeService.save(contributorType1);
		tm.commit(ts);
		
		
		ts = tm.getTransaction(td);
		
		ContributorType other = contributorTypeService.getByUniqueSystemCode(contributorType1.getUniqueSystemCode());
		assert other.equals(contributorType1) : " other = " + other + 
		" should equalcontributorType 1 = " + contributorType1;
		
		contributorTypeService.delete(other);
 	
		tm.commit(ts);
	}

}
