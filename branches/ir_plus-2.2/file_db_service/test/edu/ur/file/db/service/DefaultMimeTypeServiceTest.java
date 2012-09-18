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


package edu.ur.file.db.service;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.db.service.test.helper.ContextHolder;
import edu.ur.file.mime.MimeTypeService;
import edu.ur.file.mime.SubType;
import edu.ur.file.mime.SubTypeExtension;
import edu.ur.file.mime.TopMediaType;



/**
 * Test the service methods for the default file server service.
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultMimeTypeServiceTest {

	/**  Use Spring to get the correct objects */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/**  Mime service */
	MimeTypeService mimeService = (MimeTypeService)ctx.getBean("mimeTypeService");
	
	/** transaction manager */
	PlatformTransactionManager tm = (PlatformTransactionManager)ctx.getBean("transactionManager");

	
	/**
	 * General top media type functions.
	 *
	 */
	@Test
	public void testTopMediaType()
	{
		TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus ts = tm.getTransaction(td);
		
		TopMediaType topMediaType = mimeService.createTopMediaType("testTopType");
		
		tm.commit(ts);
		
		
		td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = tm.getTransaction(td);

		assert topMediaType.getId() != null : "Should have an assigned id";
		
		assert mimeService.getTopMediaType("testTopType").equals(topMediaType):
			"Should be able to find the top media type by name";
		
		assert mimeService.getTopMediaType(topMediaType.getId(), false).equals(topMediaType) :
			"Should be able to find the top media type by id";

		mimeService.deleteTopMediaType(mimeService.getTopMediaType(topMediaType.getId(), false));
		tm.commit(ts);
		
		
		
		assert mimeService.getTopMediaType(topMediaType.getId(), false) == null : 
			"Should not be able to find top media type";
	}
	
	/**
	 * General sub type functions.
	 * @throws DuplicateNameException 
	 *
	 */
	@Test
	public void testSubType() throws DuplicateNameException
	{
		TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus ts = tm.getTransaction(td);
		
		TopMediaType topMediaType = mimeService.createTopMediaType("testTopType");
		SubType subType = topMediaType.createSubType("subType1");
		mimeService.saveTopMediaType(topMediaType);
		
		tm.commit(ts);
		
		
		td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = tm.getTransaction(td);

		assert subType.getId() != null : "Should have an assigned id";
		
		assert mimeService.getSubType(subType.getId(), false).equals(subType):
			"Should be able to find the sub media type by id";
		
		assert mimeService.getSubType(subType.getName(), topMediaType.getId()).equals(subType) :
			"Should be able to find the sub type type by name";

		mimeService.deleteSubType(mimeService.getSubType(subType.getId(), false));
		tm.commit(ts);
		
		assert mimeService.getSubType(subType.getId(), false) == null : 
			"Should not be able to find sub type";
		mimeService.deleteTopMediaType(mimeService.getTopMediaType(topMediaType.getId(), false));
	}
	
	/**
	 * General sub type extension functions.
	 * @throws DuplicateNameException 
	 *
	 */
	@Test
	public void testSubTypeExtension() throws DuplicateNameException
	{
		
		TransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus ts = tm.getTransaction(td);
		
		TopMediaType topMediaType = mimeService.createTopMediaType("testTopType");
		SubType subType = topMediaType.createSubType("subType1");
		SubTypeExtension subTypeExtension = subType.createExtension("pdf");
		mimeService.saveTopMediaType(topMediaType);
		
		tm.commit(ts);
		
		
		td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = tm.getTransaction(td);

		assert subTypeExtension.getId() != null : "Should have an assigned id";
		
		assert mimeService.getSubTypeExtension(subTypeExtension.getId(), false).equals(subTypeExtension):
			"Should be able to find the sub type extension by id";
		
		assert mimeService.getSubTypeExtension(subTypeExtension.getName()).equals(subTypeExtension) :
			"Should be able to find the sub type type by name";

		mimeService.deleteSubTypeExtension(mimeService.getSubTypeExtension(subTypeExtension.getId(), false));
		tm.commit(ts);
		
		assert mimeService.getSubTypeExtension(subTypeExtension.getId(), false) == null : 
			"Should not be able to find sub type extension";
		mimeService.deleteTopMediaType(mimeService.getTopMediaType(topMediaType.getId(), false));
	}

}
