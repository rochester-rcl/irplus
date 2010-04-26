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
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.w3c.dom.DOMException;

public class DspaceGroupLoad {
	
	/**
	 * Main method to execute for loading groups
	 * 
	 * @param args
	 * @throws ParseException 
	 * @throws DOMException 
	 * @throws IOException 

	 */
	public static void main(String[] args) throws DOMException, ParseException, IOException 
	{
		// xml file containing the users
		String xmlFileName = args[0];
		System.out.println("Starting group load xmlFile name = " + xmlFileName);
		
		ApplicationContext ctx  = new ClassPathXmlApplicationContext(
		"applicationContext.xml");

		GroupImporter groupImporter = (GroupImporter)ctx.getBean("groupImporter");
		
		/** transaction manager for dealing with transactions  */
		PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
		
		/** the transaction definition */
		TransactionDefinition td = new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRED);
	
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
		groupImporter.importGroups(new File(xmlFileName));
		tm.commit(ts);
		
		System.out.println("Group IMPORT has completed successfully");
		
	}

}
