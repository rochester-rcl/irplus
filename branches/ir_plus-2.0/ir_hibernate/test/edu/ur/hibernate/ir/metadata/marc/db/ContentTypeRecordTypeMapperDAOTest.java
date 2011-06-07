/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.hibernate.ir.metadata.marc.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeDAO;
import edu.ur.ir.item.metadata.marc.ContentTypeRecordTypeMapper;
import edu.ur.ir.item.metadata.marc.ContentTypeRecordTypeMapperDAO;
import edu.ur.metadata.marc.MarcTypeOfRecordService;
import edu.ur.metadata.marc.MarcTypeOfRecord;

/**
 * Test the persistence of the content type record type mapper.
 * 
 * @author Nathan Sarr
 *
 */
public class ContentTypeRecordTypeMapperDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** dublin core mapping data access object */
	ContentTypeRecordTypeMapperDAO contentTypeRecordTypeMapperDAO = (ContentTypeRecordTypeMapperDAO) ctx
	.getBean("contentTypeRecordTypeMapperDAO");
  
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
    
    /** contributor type data access object */
    ContentTypeDAO contentTypeDAO = (ContentTypeDAO) ctx
	.getBean("contentTypeDAO");
    
    MarcTypeOfRecordService marcTypeOfRecordService  = (MarcTypeOfRecordService) ctx.getBean("marcTypeOfRecordService");
    
	/**
	 * Test mapping persistence
	 */
	@Test
	public void baseContentTypeRelatorCodeDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);
 		
        // create a identifier type
 		ContentType ct = new ContentType("ctName", "description");
 		contentTypeDAO.makePersistent(ct);	
 	   
 	    MarcTypeOfRecord mtr = new MarcTypeOfRecord("a", 'a');
 	    marcTypeOfRecordService.save(mtr);
 	    
 	    // create the mapping
 		ContentTypeRecordTypeMapper mapper = new ContentTypeRecordTypeMapper(ct, mtr);
 		contentTypeRecordTypeMapperDAO.makePersistent(mapper);
 	    
 	    tm.commit(ts);
 	   
 	    ts = tm.getTransaction(td);
 	    
 	    ContentTypeRecordTypeMapper other = contentTypeRecordTypeMapperDAO.getById(mapper.getId(), false);
 	    assert other.equals(mapper) : " Other " + other + "\n should be equal to " + mapper;
 	    
 	    other =  contentTypeRecordTypeMapperDAO.getByContentType(ct.getId());
 	    assert other.equals(mapper) : " Other " + other + "\n should be equal to " + mapper;
 	    tm.commit(ts);
 	    
 	    
	    ts = tm.getTransaction(td);
        // delete data
	    contentTypeRecordTypeMapperDAO.makeTransient(contentTypeRecordTypeMapperDAO.getById(mapper.getId(), false));
	    contentTypeDAO.makeTransient(contentTypeDAO.getById(ct.getId(), false));
	    marcTypeOfRecordService.delete(marcTypeOfRecordService.getById(mtr.getId(), false));
	    tm.commit(ts);
	}

}
