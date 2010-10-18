/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.hibernate.metadata.dc;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.metadata.helper.test.ContextHolder;
import edu.ur.metadata.dc.DublinCoreTerm;
import edu.ur.metadata.dc.DublinCoreTermDAO;

/**
 * @author Nathan Sarr
 *
 */
public class DublinCoreTermDAOTest {
	
    /** spring application context manager  */
    ApplicationContext ctx = ContextHolder.getApplicationContext();
    
    /** dublin core element data access object */
    DublinCoreTermDAO dublinCoreTermDAO = (DublinCoreTermDAO)ctx.getBean("dublinCoreTermDAO");
    
    /** platform transaction manager  */
    PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
    
    /** transaction definition */
    TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
    
	/**
	 * Test dublin core term persistence
	 */
	@Test
	public void baseDublinCoreTermDAOTest() throws Exception{

		DublinCoreTerm element = new DublinCoreTerm("Dublin Core Element");
 		element.setDescription("ctDescription");
 		element.setIsSimpleDublinCoreElement(true);
         
        TransactionStatus ts = tm.getTransaction(td);
 		dublinCoreTermDAO.makePersistent(element);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		DublinCoreTerm other = dublinCoreTermDAO.getById(element.getId(), false);
        assert other.equals(element) : "Dublin Core Terms should be equal mt = " + element + " other = " + other;
         
        DublinCoreTerm dublinCoreElementByName =  dublinCoreTermDAO.findByUniqueName(element.getName());
        assert dublinCoreElementByName.equals(element) : "Dublin core element should be found " + element; 
         
        dublinCoreTermDAO.makeTransient(other);
        assert  dublinCoreTermDAO.getById(other.getId(), false) == null : "Should no longer be able to find dublin core term";
	    tm.commit(ts);
	}

}
