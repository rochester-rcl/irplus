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
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.mime.SubTypeDAO;
import edu.ur.file.mime.SubTypeExtensionDAO;
import edu.ur.file.mime.TopMediaTypeDAO;
import edu.ur.file.mime.SubType;
import edu.ur.file.mime.SubTypeExtension;
import edu.ur.file.mime.TopMediaType;
import edu.ur.hibernate.file.test.helper.ContextHolder;

/**
 * Test data access for the Mime Sub type DAO 
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class HbSubTypeExtensionDAOTest {

	/**  Use Spring to get the correct objects */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** Sub type data access  */
	SubTypeDAO subTypeDAO = (SubTypeDAO)ctx.getBean("subTypeDAO");
	
	/** Top media type data access  */
	TopMediaTypeDAO topMediaTypeDAO = (TopMediaTypeDAO)ctx.getBean("topMediaTypeDAO");
	
	/** SubType extension data access */
	SubTypeExtensionDAO subTypeExtensionDAO = (SubTypeExtensionDAO)ctx.getBean("subTypeExtensionDAO");
	
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

    // Start the transaction this is for lazy loading
	TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);


	
	/**
	 * General DB Integration Tests
	 * @throws DuplicateNameException 
	 *
	 */
	@Test
	public void mimeSubTypeDAOTest() throws DuplicateNameException
	{
		TransactionStatus ts = tm.getTransaction(td);
		TopMediaType top1 = new TopMediaType("mimeTopType1", "topDescription1" );
		TopMediaType top2 = new TopMediaType("mimeTopType2", "topDescription2");
		
		SubType t1 = top1.createSubType("type1");
		t1.setDescription("description1");
		
		SubType t2 = top2.createSubType("type2");
		t2.setDescription("description2");
		
		SubTypeExtension ext1 = t1.createExtension("doc");
		SubTypeExtension ext2 = t2.createExtension("pdf");
		
		// make the top media types persistent
		topMediaTypeDAO.makePersistent(top1);
		topMediaTypeDAO.makePersistent(top2);
		
		// make the data persistent
		subTypeDAO.makePersistent(t1);
		subTypeDAO.makePersistent(t2);
		
		
		subTypeExtensionDAO.makePersistent(ext1);
		subTypeExtensionDAO.makePersistent(ext2);
		

		

		// make sure you can retrieve it from the database
		SubTypeExtension other = subTypeExtensionDAO.getById(ext1.getId(), false);
		assert other.equals(ext1) : "other should be equal to current object";
		assert other.getSubType() != null : "Sub type should not be null";
		assert other.getSubType().equals(ext1.getSubType()) : "Sub types should " +
				"be the same";
		
		// commit the transaction - this block is only needed when lazy loading
		// must occur in testing
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		// make sure you can retireve by name from the database
		SubTypeExtension otherByName = subTypeExtensionDAO.findByUniqueName(ext1.getName());
		assert otherByName.equals(ext1) : "Should be able to find by extensions name";
		
		
		// make sure you get them all back
		assert  subTypeExtensionDAO.getCount() == 2l : "size should be 2 but was " + subTypeExtensionDAO.getCount();
		
		// test deleting data
		subTypeExtensionDAO.makeTransient(subTypeExtensionDAO.getById(ext1.getId(), false));
		assert subTypeExtensionDAO.getById(ext1.getId(), false) == null : "Should not find ext1";
		assert subTypeExtensionDAO.getById(ext2.getId(), false) != null : "should find ext2";
		subTypeExtensionDAO.makeTransient(subTypeExtensionDAO.getById(ext2.getId(), false));
		tm.commit(ts);

		ts = tm.getTransaction(td);

		subTypeDAO.makeTransient(subTypeDAO.getById(t1.getId(), false));
		subTypeDAO.makeTransient(subTypeDAO.getById(t2.getId(), false));
		
		assert subTypeDAO.getById(t1.getId(), false) == null : "Should not find t1";
		assert subTypeDAO.getById(t2.getId(), false) == null : "Should not find t2";

		topMediaTypeDAO.makeTransient(topMediaTypeDAO.getById(top1.getId(), false));
		topMediaTypeDAO.makeTransient(topMediaTypeDAO.getById(top2.getId(), false));
		
		tm.commit(ts);

		ts = tm.getTransaction(td);

		assert topMediaTypeDAO.getById(top1.getId(), false) == null : "Should not find top 1";
		assert topMediaTypeDAO.getById(top2.getId(), false) == null : "Should not find top 2";
		tm.commit(ts);
	}

}
