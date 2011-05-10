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
import edu.ur.ir.item.PlaceOfPublication;
import edu.ur.ir.item.PlaceOfPublicationDAO;
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
	
	   /** Language type data access */
    PlaceOfPublicationDAO placeOfPublicationDAO = (PlaceOfPublicationDAO) ctx
	.getBean("placeOfPublicationDAO");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test external published item data persistance
	 */
	@Test
	public void baseExternalPublishedItemDAOTest() throws Exception{

 		

        TransactionStatus ts = tm.getTransaction(td);
        Publisher p = new Publisher("publisher");
		publisherDAO.makePersistent(p);
		
		PlaceOfPublication placeOfPub = new PlaceOfPublication("Rochester");
		placeOfPublicationDAO.makePersistent(placeOfPub);
		
		ExternalPublishedItem e =  new ExternalPublishedItem();
 		e.setCitation("citation");
        e.setPublisher(p);
        e.updatePublishedDate(5,13, 2008);
        e.setPlaceOfPublication(placeOfPub);
        
 		externalPublishedItemDAO.makePersistent(e);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);

 		ExternalPublishedItem other = externalPublishedItemDAO.getById(e.getId(), false);
 		assert other.getPublishedDate().getMonth() == 5 : "Month equals " + other.getPublishedDate().getMonth();
 		assert other.getPublishedDate().getDay() == 13 : "Day equals " + other.getPublishedDate().getDay();
 		assert other.getPublishedDate().getYear() == 2008 : "Day equals " + other.getPublishedDate().getYear();
 		assert other.getPublisher().equals(p) : "Other publisher " + other.getPublishedDate() + " should equal " + p;
 		assert other.getPlaceOfPublication().equals(placeOfPub) : " Place of publication " + other.getPlaceOfPublication() +
 		" should equal " + placeOfPub;
 		
        assert other.equals(e) : "External published item data should be equal";
        
        externalPublishedItemDAO.makeTransient(other);
        assert  externalPublishedItemDAO.getById(other.getId(), false) == null : "Should no longer be able to find external published item data";
        tm.commit(ts);
        
        ts = tm.getTransaction(td);

        Publisher publisherOther = publisherDAO.getById(p.getId(), false);
        publisherDAO.makeTransient(publisherOther);
        placeOfPublicationDAO.makeTransient(placeOfPublicationDAO.getById(placeOfPub.getId(), false));
        assert  publisherDAO.getById(publisherOther.getId(), false) == null : "Should no longer be able to find publisher";
        tm.commit(ts);
	}
	
	/**
	 * Test external published item data persistance
	 */
	@Test
	public void externalPublishedItemUpdatePublishedDateDAOTest() throws Exception{

		TransactionStatus ts = tm.getTransaction(td);
 		
		Publisher p = new Publisher("publisher");
		publisherDAO.makePersistent(p);

		ExternalPublishedItem e = new ExternalPublishedItem();
 		e.setCitation("citation");
        e.setPublisher(p);
        e.updatePublishedDate(5,13, 2008);
        
 		externalPublishedItemDAO.makePersistent(e);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);

 		ExternalPublishedItem other = externalPublishedItemDAO.getById(e.getId(), false);
 		assert other.getPublishedDate().getMonth() == 5 : "Month equals " + other.getPublishedDate().getMonth();
 		assert other.getPublishedDate().getDay() == 13 : "Day equals " + other.getPublishedDate().getDay();
 		assert other.getPublishedDate().getYear() == 2008 : "Day equals " + other.getPublishedDate().getYear();
 		
        assert other.equals(e) : "External published item data should be equal";
        other.updatePublishedDate(0, 0, 0);
        assert other.getPublishedDate() == null : "Publshed date should be null";
        externalPublishedItemDAO.makePersistent(other);
        tm.commit(ts);
        
        
        ts = tm.getTransaction(td);
        other = externalPublishedItemDAO.getById(e.getId(), false);
        assert other.getPublishedDate() == null : "Publshed date should be null";
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
	