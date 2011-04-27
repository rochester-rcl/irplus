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
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeDAO;
import edu.ur.ir.marc.IdentifierTypeSubFieldMapper;
import edu.ur.ir.marc.IdentifierTypeSubFieldMapperDAO;
import edu.ur.ir.marc.MarcDataFieldMapper;
import edu.ur.ir.marc.MarcDataFieldMapperDAO;
import edu.ur.metadata.marc.MarcDataField;
import edu.ur.metadata.marc.MarcDataFieldService;
import edu.ur.metadata.marc.MarcSubField;
import edu.ur.metadata.marc.MarcSubFieldService;

/**
 * Test identifier type sub field mapping.
 * 
 * @author Nathan Sarr
 *
 */
public class IdentifierTypeSubFieldMapperDAOTest {

	// get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	// marc data field data mapper data access object */
	MarcDataFieldMapperDAO marcDataFieldMapperDAO = (MarcDataFieldMapperDAO) ctx
	.getBean("marcDataFieldMapperDAO");
	
	IdentifierTypeSubFieldMapperDAO identifierTypeSubFieldMapperDAO = (IdentifierTypeSubFieldMapperDAO) ctx
	.getBean("identifierTypeSubFieldMapperDAO");

	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
    
    // marc data field service
    MarcDataFieldService marcDataFieldService = (MarcDataFieldService) ctx
	.getBean("marcDataFieldService");
    
    // identifier type data access
    IdentifierTypeDAO identifierTypeDAO = (IdentifierTypeDAO) ctx
	.getBean("identifierTypeDAO");
    
    // marc sub field service
    MarcSubFieldService marcSubFieldService = (MarcSubFieldService) ctx.getBean("marcSubFieldService");
    
	/**
	 * Test mapping persistence
	 */
	@Test
	public void baseIdentifierTypeSubfieldMapperDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);
 		
	    // create new data field
	    MarcDataField element = new MarcDataField("field", true, "100");
	    marcDataFieldService.save(element);
	    
	    // create the sub field
	    MarcSubField marcSubField = new MarcSubField("j");
	    marcSubFieldService.save(marcSubField);
	    
	    // create the identifier type
	    IdentifierType identType = new IdentifierType("identTypeName","identTypeDescription" );
	    identifierTypeDAO.makePersistent(identType);
	    
	    // create the parent mapper
 		MarcDataFieldMapper mapper = new MarcDataFieldMapper(element);
 		marcDataFieldMapperDAO.makePersistent(mapper);	  
 		
 		IdentifierTypeSubFieldMapper subFieldMapper = mapper.add(identType, marcSubField);
 		
 		identifierTypeSubFieldMapperDAO.makePersistent(subFieldMapper);
 	    tm.commit(ts);
 	   
 	    ts = tm.getTransaction(td);
 	    
 	    IdentifierTypeSubFieldMapper other = identifierTypeSubFieldMapperDAO.getById(subFieldMapper.getId(), false);
 	    assert other.equals(subFieldMapper) : " Other " + other + "\n should be equal to " + mapper;

 	    tm.commit(ts);
 	    
 	    
	    ts = tm.getTransaction(td);
        // delete data
	    marcDataFieldMapperDAO.makeTransient(marcDataFieldMapperDAO.getById(mapper.getId(), false));
	    marcSubFieldService.delete(marcSubFieldService.getById(marcSubField.getId(), false));
	    marcDataFieldService.delete(marcDataFieldService.getById(element.getId(), false));
	    identifierTypeDAO.makeTransient(identifierTypeDAO.getById(identType.getId(), false));
	    tm.commit(ts);
	}
	
}
