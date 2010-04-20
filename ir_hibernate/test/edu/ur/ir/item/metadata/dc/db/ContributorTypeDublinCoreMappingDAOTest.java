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
package edu.ur.ir.item.metadata.dc.db;


import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMapping;
import edu.ur.ir.item.metadata.dc.ContributorTypeDublinCoreMappingDAO;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeDAO;
import edu.ur.metadata.dc.DublinCoreElement;
import edu.ur.metadata.dc.DublinCoreElementService;

/**
 * @author Nathan Sarr
 *
 */
public class ContributorTypeDublinCoreMappingDAOTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** dublin core mapping data access object */
	ContributorTypeDublinCoreMappingDAO contributorTypeDublinCoreMappingDAO = (ContributorTypeDublinCoreMappingDAO) ctx
	.getBean("contributorTypeDublinCoreMappingDAO");

    /** dublin core element data access object */
	DublinCoreElementService dublinCoreElementService = (DublinCoreElementService)ctx.getBean("dublinCoreElementService");
	
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx
	.getBean("transactionManager");
	
    TransactionDefinition td = new DefaultTransactionDefinition(
	TransactionDefinition.PROPAGATION_REQUIRED);
    
    /** contributor type data access object */
    ContributorTypeDAO contributorTypeDAO = (ContributorTypeDAO) ctx
	.getBean("contributorTypeDAO");
    
	/**
	 * Test mapping persistence
	 */
	@Test
	public void baseContributorTypeDublinCoreMappingDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);

	    // create a dublin core element
		DublinCoreElement element = new DublinCoreElement("Dublin Core Element");
 		element.setDescription("ctDescription");
 		dublinCoreElementService.save(element);
 		
        // create a contributor type
 		ContributorType ct = new ContributorType("ctName");
 		ct.setDescription("ctDescription");
 		contributorTypeDAO.makePersistent(ct);	
 	    tm.commit(ts);
 	    
 	    // create the mapping
 	    ContributorTypeDublinCoreMapping ctMapping = new ContributorTypeDublinCoreMapping(ct, element);
 	    contributorTypeDublinCoreMappingDAO.makePersistent(ctMapping);
 	    
 	    ts = tm.getTransaction(td);
 	    
 	    ContributorTypeDublinCoreMapping other = contributorTypeDublinCoreMappingDAO.getById(ctMapping.getId(), false);
 	    assert other.equals(ctMapping) : " Other " + other + "\n should be equal to " + ctMapping;
 	    tm.commit(ts);
 	    
 	    
	    ts = tm.getTransaction(td);
        // delete data
	    contributorTypeDublinCoreMappingDAO.makeTransient(contributorTypeDublinCoreMappingDAO.getById(ctMapping.getId(), false));
	    dublinCoreElementService.delete(dublinCoreElementService.getById(element.getId(), false));
	    contributorTypeDAO.makeTransient(contributorTypeDAO.getById(ct.getId(), false));
	    tm.commit(ts);
	}
	
	
	/**
	 * Test mapping persistence
	 */
	@Test
	public void updateContributorTypeDublinCoreMappingDAOTest() throws Exception{

	    TransactionStatus ts = tm.getTransaction(td);

	    // create a dublin core element
		DublinCoreElement element = new DublinCoreElement("Dublin Core Element");
 		element.setDescription("ctDescription");
 		dublinCoreElementService.save(element);
 		
 	    // create a 2nd dublin core element
		DublinCoreElement element2 = new DublinCoreElement("Dublin Core Element2");
 		dublinCoreElementService.save(element2);
 		
        // create a contributor type
 		ContributorType ct = new ContributorType("ctName");
 		ct.setDescription("ctDescription");
 		contributorTypeDAO.makePersistent(ct);	
 	    tm.commit(ts);
 	    
 	    // create the mapping
 	    ContributorTypeDublinCoreMapping ctMapping = new ContributorTypeDublinCoreMapping(ct, element);
 	    contributorTypeDublinCoreMappingDAO.makePersistent(ctMapping);
 	    
 	    ts = tm.getTransaction(td);
 	    
 	    ContributorTypeDublinCoreMapping other = contributorTypeDublinCoreMappingDAO.getById(ctMapping.getId(), false);
 	    assert other.equals(ctMapping) : " Other " + other + "\n should be equal to " + ctMapping;
	    other.setDublinCoreElement(element2);
	    contributorTypeDublinCoreMappingDAO.makePersistent(other);
 	    tm.commit(ts);
 	    
 	    ts = tm.getTransaction(td);
 	    other = contributorTypeDublinCoreMappingDAO.getById(ctMapping.getId(), false);
 	    assert other.getDublinCoreElement().equals(element2) : "other element2 should = " + element2 + " other dc = " + other.getDublinCoreElement();
 	    tm.commit(ts);
 	    
	    ts = tm.getTransaction(td);
        // delete data
	    contributorTypeDublinCoreMappingDAO.makeTransient(contributorTypeDublinCoreMappingDAO.getById(ctMapping.getId(), false));
	    dublinCoreElementService.delete(dublinCoreElementService.getById(element.getId(), false));
	    dublinCoreElementService.delete(dublinCoreElementService.getById(element2.getId(), false));
	    contributorTypeDAO.makeTransient(contributorTypeDAO.getById(ct.getId(), false));
	    tm.commit(ts);
	}
	
}
