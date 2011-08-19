/**  
   Copyright 2008 - 2011 University of Rochester

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
import edu.ur.ir.item.PlaceOfPublication;
import edu.ur.ir.item.PlaceOfPublicationDAO;

/**
* Test the persistence methods for place of publication Information
* 
* @author Nathan Sarr
* 
*/
@Test(groups = { "baseTests" }, enabled = true)
public class PlaceOfPublicationDAOTest {

	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

    /** Language type data access */
    PlaceOfPublicationDAO placeOfPublicationDAO = (PlaceOfPublicationDAO) ctx
	.getBean("placeOfPublicationDAO");
    
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

     
	/**
	 * Test place of publication persistance
	 */
	@Test
	public void basePlaceOfPublicationDAOTest() throws Exception{
 		PlaceOfPublication placeOfPublication = new PlaceOfPublication();
		placeOfPublication.setName("placeOfPublication");
 		placeOfPublication.setDescription("placeDescription");
         
	    TransactionStatus ts = tm.getTransaction(td);
        placeOfPublicationDAO.makePersistent(placeOfPublication);
        tm.commit(ts);
        
        ts = tm.getTransaction(td);
         PlaceOfPublication other = placeOfPublicationDAO.getById(placeOfPublication.getId(), false);
         assert other.equals(placeOfPublication) : "Language types should be equal";
         
         PlaceOfPublication placeOfPublicationByName = placeOfPublicationDAO.findByUniqueName(placeOfPublication.getName());
         assert placeOfPublicationByName.equals(placeOfPublication) : "Language should be found";
                  
         placeOfPublicationDAO.makeTransient(other);
         assert placeOfPublicationDAO.getById(other.getId(), false) == null : "Should no longer be able to find place of publication";
         tm.commit(ts);
	}
}
