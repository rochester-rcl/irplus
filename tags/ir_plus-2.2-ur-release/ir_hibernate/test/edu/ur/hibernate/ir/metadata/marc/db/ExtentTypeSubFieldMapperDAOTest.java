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
import edu.ur.ir.item.ExtentType;
import edu.ur.ir.item.ExtentTypeDAO;
import edu.ur.ir.item.metadata.marc.ExtentTypeSubFieldMapper;
import edu.ur.ir.item.metadata.marc.ExtentTypeSubFieldMapperDAO;
import edu.ur.ir.item.metadata.marc.MarcDataFieldMapper;
import edu.ur.ir.item.metadata.marc.MarcDataFieldMapperDAO;
import edu.ur.metadata.marc.MarcDataField;
import edu.ur.metadata.marc.MarcDataFieldService;
import edu.ur.metadata.marc.MarcSubField;
import edu.ur.metadata.marc.MarcSubFieldService;

/**
 * Test Extent type mapping.
 * 
 * @author Nathan Sarr
 *
 */
public class ExtentTypeSubFieldMapperDAOTest {

	// get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	// marc data field data mapper data access object */
	MarcDataFieldMapperDAO marcDataFieldMapperDAO = (MarcDataFieldMapperDAO) ctx
	.getBean("marcDataFieldMapperDAO");
	
	ExtentTypeSubFieldMapperDAO extentTypeSubFieldMapperDAO = (ExtentTypeSubFieldMapperDAO) ctx
	.getBean("extentTypeSubFieldMapperDAO");

	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
    
    // marc data field service
    MarcDataFieldService marcDataFieldService = (MarcDataFieldService) ctx
	.getBean("marcDataFieldService");
    
    // identifier type data access
    ExtentTypeDAO extentTypeDAO = (ExtentTypeDAO) ctx
	.getBean("extentTypeDAO");
    
    // marc sub field service
    MarcSubFieldService marcSubFieldService = (MarcSubFieldService) ctx.getBean("marcSubFieldService");
    
	/**
	 * Test mapping persistence
	 */
	@Test
	public void baseExtentTypeSubfieldMapperDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);
 		
	    // create new data field
	    MarcDataField element = new MarcDataField("field", true, "100");
	    marcDataFieldService.save(element);
	    
	    // create the sub field
	    MarcSubField marcSubField = new MarcSubField("j");
	    marcSubFieldService.save(marcSubField);
	    
	    // create the extent type
	    ExtentType extentType = new ExtentType("extentTypeName");
	    extentTypeDAO.makePersistent(extentType);
	    
	    // create the parent mapper
 		MarcDataFieldMapper mapper = new MarcDataFieldMapper(element);
 		marcDataFieldMapperDAO.makePersistent(mapper);	  
 		
 		ExtentTypeSubFieldMapper subFieldMapper = mapper.add(extentType, marcSubField);
 		
 		extentTypeSubFieldMapperDAO.makePersistent(subFieldMapper);
 	    tm.commit(ts);
 	   
 	    ts = tm.getTransaction(td);
 	    
 	    ExtentTypeSubFieldMapper other = extentTypeSubFieldMapperDAO.getById(subFieldMapper.getId(), false);
 	    assert other.equals(subFieldMapper) : " Other " + other + "\n should be equal to " + mapper;

 	    tm.commit(ts);
 	    
 	    
	    ts = tm.getTransaction(td);
        // delete data
	    marcDataFieldMapperDAO.makeTransient(marcDataFieldMapperDAO.getById(mapper.getId(), false));
	    marcSubFieldService.delete(marcSubFieldService.getById(marcSubField.getId(), false));
	    marcDataFieldService.delete(marcDataFieldService.getById(element.getId(), false));
	    extentTypeDAO.makeTransient(extentTypeDAO.getById(extentType.getId(), false));
	    tm.commit(ts);
	}
}
