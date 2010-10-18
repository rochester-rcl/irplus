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

package edu.ur.hibernate.ir.item.metadata.dc.db;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.item.metadata.dc.IdentifierTypeDublinCoreMapping;
import edu.ur.ir.item.metadata.dc.IdentifierTypeDublinCoreMappingDAO;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeDAO;
import edu.ur.metadata.dc.DublinCoreEncodingScheme;
import edu.ur.metadata.dc.DublinCoreEncodingSchemeService;
import edu.ur.metadata.dc.DublinCoreTerm;
import edu.ur.metadata.dc.DublinCoreTermService;

/**
 * @author NathanS
 *
 */
public class IdentifierTypeDublinCoreMappingDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** dublin core mapping data access object */
	IdentifierTypeDublinCoreMappingDAO identifierTypeDublinCoreMappingDAO = (IdentifierTypeDublinCoreMappingDAO) ctx
	.getBean("identifierTypeDublinCoreMappingDAO");

    /** dublin core element data access object */
	DublinCoreTermService dublinCoreTermService = (DublinCoreTermService)ctx.getBean("dublinCoreTermService");
	
    /** dublin core encoding scheme service data access object */
	DublinCoreEncodingSchemeService dublinCoreEncodingSchemeService = (DublinCoreEncodingSchemeService)ctx.getBean("dublinCoreEncodingSchemeService");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
    
    /** identifier type data access object */
    IdentifierTypeDAO identifierTypeDAO = (IdentifierTypeDAO) ctx
	.getBean("identifierTypeDAO");
    
	/**
	 * Test mapping persistence
	 */
	@Test
	public void baseIdentifierTypeDublinCoreMappingDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);

	    // create a dublin core element
		DublinCoreTerm element = new DublinCoreTerm("Dublin Core Term");
 		element.setDescription("termDescription");
 		dublinCoreTermService.save(element);
 		
 		DublinCoreEncodingScheme scheme = new DublinCoreEncodingScheme("Dublin Core Scheme");
 		scheme.setDescription("schemeDescription");
 		dublinCoreEncodingSchemeService.save(scheme);
 		
        // create a identifier type
 		IdentifierType it = new IdentifierType("identName", "identDescription");
 		identifierTypeDAO.makePersistent(it);	
 	   
 	    
 	    // create the mapping
 	    IdentifierTypeDublinCoreMapping itMapping = new IdentifierTypeDublinCoreMapping(it, element);
 	    itMapping.setDublinCoreEncodingScheme(scheme);
 	    identifierTypeDublinCoreMappingDAO.makePersistent(itMapping);
 	    tm.commit(ts);
 	   
 	    ts = tm.getTransaction(td);
 	    
 	    IdentifierTypeDublinCoreMapping other = identifierTypeDublinCoreMappingDAO.getById(itMapping.getId(), false);
 	    assert other.equals(itMapping) : " Other " + other + "\n should be equal to " + itMapping;
 	    
 	    other = identifierTypeDublinCoreMappingDAO.get(it.getId(), element.getId());
 	    assert other.equals(itMapping) : " Other " + other + "\n should be equal to " + itMapping;
 	    
 	    other = identifierTypeDublinCoreMappingDAO.get(it.getId());
 	    assert other.equals(itMapping) : " Other " + other + "\n should be equal to " + itMapping;
 	    
 	    Long count = identifierTypeDublinCoreMappingDAO.getCount();
 	    assert count == 1 : "Should find one but found " + count;
 	    
 	    tm.commit(ts);
 	    
 	    
	    ts = tm.getTransaction(td);
        // delete data
	    identifierTypeDublinCoreMappingDAO.makeTransient(identifierTypeDublinCoreMappingDAO.getById(itMapping.getId(), false));
	    dublinCoreTermService.delete(dublinCoreTermService.getById(element.getId(), false));
	    identifierTypeDAO.makeTransient(identifierTypeDAO.getById(it.getId(), false));
	    dublinCoreEncodingSchemeService.delete(scheme);
	    tm.commit(ts);
	}
	
	
	/**
	 * Test mapping persistence
	 */
	@Test
	public void updateIdentifierTypeDublinCoreMappingDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);

	    // create a dublin core element
		DublinCoreTerm element = new DublinCoreTerm("Dublin term Element");
 		element.setDescription("ctDescription");
 		dublinCoreTermService.save(element);
 		
 	    // create a 2nd dublin core element
		DublinCoreTerm element2 = new DublinCoreTerm("Dublin Core Term2");
 		dublinCoreTermService.save(element2);
 		
        // create a identifier type
 		IdentifierType it = new IdentifierType("identtName", "identDescription");
 		identifierTypeDAO.makePersistent(it);	
 	    tm.commit(ts);
 	    
 	    // create the mapping
 	    IdentifierTypeDublinCoreMapping itMapping = new IdentifierTypeDublinCoreMapping(it, element);
 	    identifierTypeDublinCoreMappingDAO.makePersistent(itMapping);
 	    
 	    ts = tm.getTransaction(td);
 	    
 	    IdentifierTypeDublinCoreMapping other = identifierTypeDublinCoreMappingDAO.getById(itMapping.getId(), false);
 	    assert other.equals(itMapping) : " Other " + other + "\n should be equal to " + itMapping;
	   
 	    // update the identifier type
 	    other.setDublinCoreTerm(element2);
	    identifierTypeDublinCoreMappingDAO.makePersistent(other);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 	    other = identifierTypeDublinCoreMappingDAO.getById(itMapping.getId(), false);
 	    assert other.getDublinCoreTerm().equals(element2) : "other element2 should = " + element2 + " other dc = " + other.getDublinCoreTerm();
 	    tm.commit(ts);
 	    
	    ts = tm.getTransaction(td);
        // delete data
	    identifierTypeDublinCoreMappingDAO.makeTransient(identifierTypeDublinCoreMappingDAO.getById(itMapping.getId(), false));
	    dublinCoreTermService.delete(dublinCoreTermService.getById(element.getId(), false));
	    dublinCoreTermService.delete(dublinCoreTermService.getById(element2.getId(), false));
	    identifierTypeDAO.makeTransient(identifierTypeDAO.getById(it.getId(), false));
	    tm.commit(ts);
	}

}
