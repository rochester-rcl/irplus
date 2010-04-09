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
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeDAO;

/**
 * Test the persistance methods for Item content type Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ContentTypeDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	ContentTypeDAO contentTypeDAO = (ContentTypeDAO) ctx
	.getBean("contentTypeDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test Contributor type persistance
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseContentTypeDAOTest() throws Exception{

		ContentType ct = new ContentType("ctName");
 		ct.setDescription("ctDescription");
         
        TransactionStatus ts = tm.getTransaction(td);
 		contentTypeDAO.makePersistent(ct);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 		ContentType other = contentTypeDAO.getById(ct.getId(), false);
        assert other.equals(ct) : "Content types should be equal";
         
        List<ContentType> itemContentTypes =  contentTypeDAO.getAllOrderByName(0, 1);
        assert itemContentTypes.size() == 1 : "One content should be found";
        assert contentTypeDAO.getAllNameOrder().size() == 1 : "One content type should be found";
         
        ContentType itemContentTypeByName =  contentTypeDAO.findByUniqueName(ct.getName());
        assert itemContentTypeByName.equals(ct) : "Content type should be found";
         
        contentTypeDAO.makeTransient(other);
        assert  contentTypeDAO.getById(other.getId(), false) == null : "Should no longer be able to find content type";
	    tm.commit(ts);
	}
}
