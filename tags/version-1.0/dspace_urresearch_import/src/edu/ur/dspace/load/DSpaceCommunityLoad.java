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

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;

/**
 * Load community data into the UR Research system
 * 
 * @author Nathan Sarr
 *
 */
public class DSpaceCommunityLoad {
	
	/**
	 * Main method to execute
	 * 
	 * @param args
	 * @throws IOException
	 * @throws DuplicateNameException
	 * @throws IllegalFileSystemNameException
	 */
	public static void main(String[] args) throws IOException, DuplicateNameException, IllegalFileSystemNameException
	{
		
		String idStr = args[0];
		String zipFileName = args[1];
		System.out.println("Starting community load idStr = " + idStr + " zipFile name = " + zipFileName);
		/** get the application context */
		ApplicationContext ctx  = new ClassPathXmlApplicationContext(
		"applicationContext.xml");
		
		CommunityImporter communityImporter = (CommunityImporter)ctx.getBean("communityImporter");
		
		/** transaction manager for dealing with transactions  */
		PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
		
		/** the transaction definition */
		TransactionDefinition td = new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRED);
		
		
	    RepositoryService repositoryService = (RepositoryService)ctx.getBean("repositoryService");	
		// start a new transaction
		TransactionStatus ts = tm.getTransaction(td);
	
		Long id = new Long(idStr);
		
		Repository repository = repositoryService.getRepository(id, false);
		
		if( repository == null )
		{
			throw new IllegalStateException("Could not find repository with id " + idStr);
		}
			
		communityImporter.ImportCommunities(zipFileName, repository);
		tm.commit(ts);
		
		System.out.println("IMPORT has completed successfully");
		
	}

}
