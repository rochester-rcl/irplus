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
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.PublisherDAO;

/**
 * Test the persistence methods for a publisher
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class PublisherDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	PublisherDAO publisherDAO = (PublisherDAO) ctx
	.getBean("publisherDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test Publisher persistence
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void basePublisherTypeDAOTest() throws Exception{

		Publisher publisher = new Publisher("publisherName");
 		publisher.setDescription("publisherDescription");
         
        TransactionStatus ts = tm.getTransaction(td);

 		 publisherDAO.makePersistent(publisher);
 		 Publisher other = publisherDAO.getById(publisher.getId(), false);
         assert other.equals(publisher) : "Publishers should be equal";
         
         List<Publisher> publishers =  publisherDAO.getAllOrderByName(0, 1);
         assert publishers.size() == 1 : "One publisher should be found";
         
         assert publisherDAO.getAllNameOrder().size() == 1 : "One publisher should be found";
         
         Publisher publisherByName =  publisherDAO.findByUniqueName(publisher.getName());
        
         assert publisherByName.equals(publisher) : "Publisher should be found";
         
         publisherDAO.makeTransient(other);
         assert  publisherDAO.getById(other.getId(), false) == null : 
        	 "Should no longer be able to find publisher";
        
         tm.commit(ts);
	}
}
