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
package edu.ur.hibernate.metadata;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.metadata.helper.test.ContextHolder;
import edu.ur.metadata.MetadataType;
import edu.ur.metadata.MetadataTypeDAO;

/**
 * Class to store the metadata types.
 * 
 * @author Nathan Sarr
 *
 */
public class MetadataTypeDAOTest 
{
    /** spring application context manager  */
    ApplicationContext ctx = ContextHolder.getApplicationContext();
    
    /** metadata type data access object */
    MetadataTypeDAO metadataTypeDAO = (MetadataTypeDAO)ctx.getBean("metadataTypeDAO");
    
    /** platform transaction manager  */
    PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");
    
    /** transaction definition */
    TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
    
	/**
	 * Test content type persistence
	 */
	@Test
	public void baseMetadataTypeDAOTest() throws Exception{

		MetadataType mt = new MetadataType("Dublin Core");
 		mt.setDescription("ctDescription");
         
        TransactionStatus ts = tm.getTransaction(td);
 		metadataTypeDAO.makePersistent(mt);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		MetadataType other = metadataTypeDAO.getById(mt.getId(), false);
        assert other.equals(mt) : "Metadata types should be equal mt = " + mt + " other = " + other;
         
        MetadataType metadataTypeByName =  metadataTypeDAO.findByUniqueName(mt.getName());
        assert metadataTypeByName.equals(mt) : "Metadata type should be found " + mt; 
         
        metadataTypeDAO.makeTransient(other);
        assert  metadataTypeDAO.getById(other.getId(), false) == null : "Should no longer be able to find content type";
	    tm.commit(ts);
	}
}
