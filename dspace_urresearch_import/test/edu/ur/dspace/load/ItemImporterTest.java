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
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.testng.annotations.Test;

import edu.ur.dspace.model.BitstreamFileInfo;
import edu.ur.dspace.model.DspaceItem;
import edu.ur.dspace.model.DspaceItemMetadata;
import edu.ur.dspace.model.EpersonPermission;
import edu.ur.dspace.model.GroupPermission;
import edu.ur.dspace.test.ContextHolder;
import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.repository.RepositoryService;


/**
 * Help testing with item import.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ItemImporterTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** Importer for dealing with items */
	ItemImporter itemImporter = (ItemImporter)ctx.getBean("itemImporter");
	
	/** Helps populate items */
	GenericItemPopulator genericItemPopulator = (GenericItemPopulator)ctx.getBean("genericItemPopulator");
	
	/** Service for dealing with repositories */
	RepositoryService repositoryService = (RepositoryService)ctx.getBean("repositoryService");
	
	/** transaction manager for dealing with transactions  */
	PlatformTransactionManager tm = (PlatformTransactionManager) ctx.getBean("transactionManager");
	
	/** the transaction definition */
	TransactionDefinition td = new DefaultTransactionDefinition(
			TransactionDefinition.PROPAGATION_REQUIRED);
	
	/**  Logger */
	private static final Logger log = LogManager.getLogger(ItemImporterTest.class);
	
	public void testItemImport() throws IOException, DuplicateNameException, IllegalFileSystemNameException, NoIndexFoundException
	{
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		
		File f = new File("C:\\items.xml");
	    List<DspaceItem> items = itemImporter.getItems(f);
	    
	    for(DspaceItem i :  items)
	    {
	    	log.debug("Found item " + i);
	    	
	    	for(DspaceItemMetadata meta : i.metadata)
	    	{
	    		log.debug("meta = " + meta);
	    	}
	    	
	    	for(BitstreamFileInfo fileInfo : i.files)
	    	{
	    		log.debug("file info = " + fileInfo);
	    		for(GroupPermission groupPermission : fileInfo.groupPermissions)
		    	{
		    		System.out.println("File Info group permission = " + groupPermission);
		    	}
		    	
		    	for(EpersonPermission epersonPermission : fileInfo.epersonPermissions)
		    	{
		    		System.out.println("File info eperson permission = " + epersonPermission);
		    	}
	    	}
	    	
	    	for(Long collectionId : i.collectionIds)
	    	{
	    		log.debug("Collecition id = " + collectionId);
	    	}
	    	
	    	for(GroupPermission groupPermission : i.groupPermissions)
	    	{
	    		System.out.println("group permission = " + groupPermission);
	    	}
	    	
	    	for(EpersonPermission epersonPermission : i.epersonPermissions)
	    	{
	    		System.out.println("eperson permission = " + epersonPermission);
	    	}
	    }
	    /*
	    Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
		for(int index = 1; index < 15; index ++)
		{
			String zipPath = "C:\\dspace_export_lib\\items_";
			zipPath = zipPath + index + ".zip";
			itemImporter.importItems(zipPath, repository);
		}
    	*/
	    tm.commit(ts);
	}

}
