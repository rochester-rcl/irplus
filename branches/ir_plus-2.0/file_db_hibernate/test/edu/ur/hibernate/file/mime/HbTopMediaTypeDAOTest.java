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

package edu.ur.hibernate.file.mime;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.mime.TopMediaType;
import edu.ur.file.mime.SubType;
import edu.ur.file.mime.TopMediaTypeDAO;
import edu.ur.hibernate.file.test.helper.ContextHolder;


/**
 * Test data access for the Mime top
 * Media Type DAO 
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class HbTopMediaTypeDAOTest {
	
	private TopMediaType t1;
	private TopMediaType t2;

	/**
	 * Use Spring to get the correct objects
	 */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	TopMediaTypeDAO topMediaTypeDAO = (TopMediaTypeDAO)ctx.getBean("topMediaTypeDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

    // Start the transaction this is for lazy loading
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

	
	@BeforeTest(alwaysRun= true)
	public void setUp() {
		t1 = new TopMediaType("type1", "description1" );
		t2 = new TopMediaType("type2","description2" );
	}
	
	/**
	 * General DB Tests
	 * @throws DuplicateNameException 
	 *
	 */
	@Test
	public void topMediaTypeDAOTest() throws DuplicateNameException
	{
		TransactionStatus ts = tm.getTransaction(td);
		// make the data persistent
		topMediaTypeDAO.makePersistent(t1);
		topMediaTypeDAO.makePersistent(t2);
		
		// make sure you can retireve it from the database
		TopMediaType other = topMediaTypeDAO.getById(t1.getId(), false);
		assert other.equals(t1) : "other should be equal to current object";
		
		// make sure you can retireve by name from the database
		TopMediaType otherByName = topMediaTypeDAO.findByUniqueName(t2.getName());
		assert otherByName.equals(t2) : "Should be able to find by name";
		
		// make sure you get them all back
		assert  topMediaTypeDAO.getAll().size() == 2 : "size should be 2 but is " + topMediaTypeDAO.getAll().size();
		
		// test deleting data
		topMediaTypeDAO.makeTransient(t1);
		assert topMediaTypeDAO.getById(t1.getId(), false) == null : "Should not find  t1";
		assert topMediaTypeDAO.getById(t2.getId(), false) != null : "should find t2";
		
		// add sub types to the top media types
		SubType subType1 = t2.createSubType("name1");
		subType1.createExtension("desc1");
		
		SubType subType2 = t2.createSubType("name2");
		subType1.setDescription("desc2");
		SubType subType3 = t2.createSubType("name3");
		subType3.setDescription("desc3" );

		topMediaTypeDAO.makePersistent(t2);
		tm.commit(ts);
	
		ts = tm.getTransaction(td);

		TopMediaType other2 = topMediaTypeDAO.getById(t2.getId(), false);
		assert other2.getSubTypes().size() == 3 : "Should have 3 sub types";
		
		assert other2.getSubType("name1").equals(subType1) : "Sub type 1 should be found";
		assert other2.getSubType("name2").equals(subType2) : "Sub type 2 should be found";
		assert other2.getSubType("name3").equals(subType3) : "Sub type 3 should be found";
		
		// end the lazy transaction
		topMediaTypeDAO.makeTransient(topMediaTypeDAO.getById(t2.getId(), false));	

		tm.commit(ts);
		
		ts = tm.getTransaction(td);

		assert topMediaTypeDAO.getById(t2.getId(), false) == null : "should not find t2";
		tm.commit(ts);
	}
}