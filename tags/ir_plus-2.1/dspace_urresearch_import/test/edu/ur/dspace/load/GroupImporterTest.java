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
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;
import org.w3c.dom.DOMException;

import edu.ur.dspace.model.DspaceGroup;
import edu.ur.dspace.test.ContextHolder;
import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;

/**
 * Help with testing user imports
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class GroupImporterTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	GroupImporter groupImporter = (GroupImporter)ctx.getBean("groupImporter");
	
	/** transaction manager for dealing with transactions  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	/** the transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**
	 * Test getting the groups out of the XML file.
	 * 
	 * @throws IOException
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 * @throws DOMException
	 * @throws ParseException
	 */
	public void testGetGroups() throws IOException, DuplicateNameException, IllegalFileSystemNameException, DOMException, ParseException
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		List<DspaceGroup> groups = groupImporter.getGroups(new File("C:\\groups.xml"));
		
		for(DspaceGroup g : groups)
		{
			System.out.println("Found group " + g);
			for( Long userId :  g.dspaceEpersonIds )
			{
				System.out.println("\t Found user id " + userId);
			}
		}
		
		tm.commit(ts);
	}
	
	/**
	 * Test importing groups into the database.
	 * @throws ParseException 
	 * @throws DOMException 
	 * @throws IOException 
	 */
	public void testImportGroups() throws DOMException, ParseException, IOException
	{		
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		groupImporter.importGroups(new File("C:\\groups.xml"));
		tm.commit(ts);
		
	}

}
