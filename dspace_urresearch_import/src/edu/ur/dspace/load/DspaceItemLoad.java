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

import java.io.IOException;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.w3c.dom.DOMException;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;

/**
 * @author Nathan Sarr
 *
 */
public class DspaceItemLoad {
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(DefaultItemImporter.class);
	
	/**
	 * Main method to execute for loading researchers
	 * 
	 * @param args
	 * @throws ParseException 
	 * @throws DOMException 
	 * @throws NoIndexFoundException 
	 * @throws IOException 

	 */
	public static void main(String[] args) throws DOMException, ParseException, IOException, NoIndexFoundException 
	{
		// id of the repository
		String idStr = args[0];
		
		// path to the set of zip files
		String zipFilePath = args[1];
		
		// start file name
		String zipFileNameStart = args[2];
		
		// start index for the files
		int startIndex = new Integer(args[3]).intValue();
		
		// end index for the files
		int endIndex = new Integer(args[4]).intValue();
		
		// load only public files indicator
		boolean publicOnly = new Boolean(args[5]).booleanValue();
		
		
		System.out.println("Starting item load reposiotry id = " 
				+ idStr + " zipFile path = " + zipFilePath + " zipFileNameStart = " + zipFileNameStart
				+ " start index = " + startIndex + " endIndex = " + endIndex);
		
		
		/** get the application context */
		ApplicationContext ctx  = new ClassPathXmlApplicationContext(
		"applicationContext.xml");
		
		/** Importer for dealing with items */
		ItemImporter itemImporter = (ItemImporter)ctx.getBean("itemImporter");
		
		/** Service for dealing with repositories */
		RepositoryService repositoryService = (RepositoryService)ctx.getBean("repositoryService");
		
		/** transaction manager for dealing with transactions  */
		PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
		
		/** the transaction definition */
		TransactionDefinition td = new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRED);
		
		
		for(int index = startIndex; index <= endIndex; index ++)
		{
			// Start the transaction 
			TransactionStatus ts = tm.getTransaction(td);
		    Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
			String zipPath = zipFilePath + zipFileNameStart;
			zipPath = zipPath + index + ".zip";
			log.debug("loading " + zipPath);
			System.out.println("loading " + zipPath);
			itemImporter.importItems(zipPath, repository, publicOnly);
			tm.commit(ts);
		}
    	
		log.debug("Item IMPORT has completed successfully");
		System.out.println("Item IMPORT has completed successfully");
		
	}

}
