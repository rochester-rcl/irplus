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

import edu.ur.dspace.model.DspaceResearcher;
import edu.ur.dspace.model.DspaceResearcherFolder;
import edu.ur.dspace.model.ResearcherFolderLink;
import edu.ur.dspace.test.ContextHolder;
import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;

/**
 * Help testing with researcher import.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ResearcherImporterTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	ResearcherImporter researcherImporter = (ResearcherImporter)ctx.getBean("researcherImporter");
	
	UserImporter userImporter = (UserImporter)ctx.getBean("userImporter");
	
	/** transaction manager for dealing with transactions  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	/** the transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	public void testGetResearchers() throws IOException, DuplicateNameException, IllegalFileSystemNameException
	{
		
		File f = new File("C:\\dspace_export_lib\\researcher.xml");
	    List<DspaceResearcher> researchers = researcherImporter.getResearchers(f);
	    
	    for(DspaceResearcher r : researchers )
	    {
	    	System.out.println("Found researcher " + r.researcherId);
	    	
	    	
	    	if( r.folder != null )
	    	{
	    	    System.out.println("Root = " + r.folder);
	    	    for(ResearcherFolderLink link : r.folder.links)
	    	    {
	    	    	System.out.println("found link " + link);
	    	    }
	    	    printChildren(r.folder.children);
	    	}
	    	else
	    	{
	    		System.out.println("folder is NULL!");
	    	}
	    	
	    	
	    }
	    
		
	}
	
	public void testLoadResearchers() throws IOException, DuplicateNameException, IllegalFileSystemNameException, DOMException, ParseException
	{
		// Start the transaction 
		
		TransactionStatus ts = tm.getTransaction(td);
		userImporter.importUsers(new File("C:\\dspace_export_lib\\users.xml"));
		tm.commit(ts);
		
		ts = tm.getTransaction(td);
		RepositoryService repositoryService = (RepositoryService)ctx.getBean("repositoryService");
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		researcherImporter.importResearchers("C:\\dspace_export_lib\\researchers.zip", repository);
		tm.commit(ts);
		
	}
	
	private void printChildren(List<DspaceResearcherFolder> children)
	{

		    for(DspaceResearcherFolder child : children)
		    {
		    	System.out.print("child:" + child.title + " " );
		    	 for(ResearcherFolderLink link : child.links)
		    	 {
		    	    	System.out.println("found link " + link);
		    	 }
		    }
		    System.out.print("\n");
		    for(DspaceResearcherFolder child : children)
		    {
		    	printChildren(child.children);
		    }
		
	}

}
