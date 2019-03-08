package edu.ur.dspace.load;

import java.io.IOException;
import java.text.ParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.w3c.dom.DOMException;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.institution.CollectionDoesNotAcceptItemsException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;

/**
 * Loads a single zip file containing one or more items
 * 
 * @author Nathan Sarr
 *
 */
public class SingleItemZipLoad {
	
	/**  Logger */
	private static final Logger log = LogManager.getLogger(SingleItemZipLoad.class);
	
	/**
	 * Main method to execute for loading researchers
	 * 
	 * @param args
	 * @throws ParseException 
	 * @throws DOMException 
	 * @throws NoIndexFoundException 
	 * @throws IOException 
	 * @throws CollectionDoesNotAcceptItemsException 

	 */
	public static void main(String[] args) throws DOMException, ParseException, IOException, NoIndexFoundException, CollectionDoesNotAcceptItemsException 
	{
		// id of the repository
		String idStr = args[0];
		
		// zip file to load
		String zipFile = args[1];
		
		// load only public files indicator
		boolean publicOnly = new Boolean(args[2]).booleanValue();
		
		
		System.out.println("Starting item load reposiotry id = " 
				+ idStr + " zipFile = " + zipFile + " public only = " + publicOnly);
		
		
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
		
		// Start the transaction 
		TransactionStatus ts = tm.getTransaction(td);
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		System.out.println("loading zip file" + zipFile);
		itemImporter.importItems(zipFile, repository, publicOnly);
		tm.commit(ts);
	
		log.debug("Item IMPORT has completed successfully");
		System.out.println("Item IMPORT has completed successfully");
	}

}
