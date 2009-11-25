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

package edu.ur.dspace.load;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;
import org.w3c.dom.DOMException;

import edu.ur.dspace.test.ContextHolder;
import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.IllegalFileSystemNameException;


/**
 * Help with testing user imports
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class UserImporterTest {
	
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	UserImporter userImporter = (UserImporter)ctx.getBean("userImporter");
	
	/** transaction manager for dealing with transactions  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	/** the transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	public void testGetUsers() throws IOException, DuplicateNameException, IllegalFileSystemNameException, DOMException, ParseException
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		userImporter.importUsers(new File("C:\\users.xml"));
		tm.commit(ts);
	}
}
