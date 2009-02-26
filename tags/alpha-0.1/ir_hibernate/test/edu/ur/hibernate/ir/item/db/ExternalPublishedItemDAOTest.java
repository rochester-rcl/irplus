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
import edu.ur.ir.item.ExternalPublishedItem;
import edu.ur.ir.item.ExternalPublishedItemDAO;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.PublisherDAO;

/**
 * Test the persistance methods for external published item Information
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ExternalPublishedItemDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	ExternalPublishedItemDAO externalPublishedItemDAO = (ExternalPublishedItemDAO) ctx
	.getBean("externalPublishedItemDAO");
	
	PublisherDAO publisherDAO = (PublisherDAO) ctx
	.getBean("publisherDAO");

	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test external published item data persistance
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseExternalPublishedItemDAOTest() throws Exception{

 		Publisher p = new Publisher("publisher");
		publisherDAO.makePersistent(p);

        TransactionStatus ts = tm.getTransaction(td);
 
		ExternalPublishedItem e = new ExternalPublishedItem();
 		e.setCitation("citation");
        e.setPublisher(p);
        e.addPublishedDate(5,13, 2008);
        
 		externalPublishedItemDAO.makePersistent(e);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);

 		ExternalPublishedItem other = externalPublishedItemDAO.getById(e.getId(), false);
        assert other.equals(e) : "External published item data should be equal";
        
        externalPublishedItemDAO.makeTransient(other);
        assert  externalPublishedItemDAO.getById(other.getId(), false) == null : "Should no longer be able to find external published item data";
        tm.commit(ts);
        
        ts = tm.getTransaction(td);

        Publisher publisherOther = publisherDAO.getById(p.getId(), false);
        publisherDAO.makeTransient(publisherOther);
        assert  publisherDAO.getById(publisherOther.getId(), false) == null : "Should no longer be able to find publisher";
        
        tm.commit(ts);
	}
}
	