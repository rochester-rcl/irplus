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
import edu.ur.metadata.dc.DublinCoreEncodingSchemeDAO;
import edu.ur.metadata.dc.DublinCoreEncodingScheme;

/**
 * Simple test for dublin core encoding scheme.
 * 
 * @author Nathan Sarr
 *
 */
public class DublinCoreEncodingSchemeDAOTest {
	
    /** spring application context manager  */
    ApplicationContext ctx = ContextHolder.getApplicationContext();
    
    /** dublin core element data access object */
    DublinCoreEncodingSchemeDAO dublinCoreEncodingSchemeDAO = (DublinCoreEncodingSchemeDAO)ctx.getBean("dublinCoreEncodingSchemeDAO");
    
    /** platform transaction manager  */
    PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
    
    /** transaction definition */
    TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
    
	/**
	 * Test dublin core EncodingScheme persistence
	 */
	@Test
	public void baseDublinCoreEncodingSchemeDAOTest() throws Exception{

		TransactionStatus ts = tm.getTransaction(td);
		DublinCoreEncodingScheme element = new DublinCoreEncodingScheme("Dublin Core Scheme");
 		element.setDescription("schemeDescription");
 		dublinCoreEncodingSchemeDAO.makePersistent(element);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		DublinCoreEncodingScheme other = dublinCoreEncodingSchemeDAO.getById(element.getId(), false);
        assert other.equals(element) : "Dublin Core EncodingSchemes should be equal mt = " + element + " other = " + other;
         
        DublinCoreEncodingScheme dublinCoreElementByName =  dublinCoreEncodingSchemeDAO.findByUniqueName(element.getName());
        assert dublinCoreElementByName.equals(element) : "Dublin core scheme should be found " + element; 
         
        dublinCoreEncodingSchemeDAO.makeTransient(other);
        assert  dublinCoreEncodingSchemeDAO.getById(other.getId(), false) == null : "Should no longer be able to find dublin core EncodingScheme";
	    tm.commit(ts);
	}


}
