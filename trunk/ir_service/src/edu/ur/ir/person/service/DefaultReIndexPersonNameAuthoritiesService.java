package edu.ur.ir.person.service;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.person.NameAuthorityIndexService;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.person.ReIndexPersonNameAuthoritiesService;
import edu.ur.order.OrderType;


/**
 * Service for re-indexing person name authorities.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultReIndexPersonNameAuthoritiesService implements ReIndexPersonNameAuthoritiesService {

	/** Service for dealing with researchers. */
	private PersonService personService;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultReIndexPersonNameAuthoritiesService.class);
	
	/** Service for indexing user information */
	private NameAuthorityIndexService nameAuthorityIndexService;
	
	
	
	public int reIndexNameAuthorities(int batchSize,
			File personNameAuthorityIndexFolder) {
		log.debug("Re-Indexing name authorities");
		
		if(batchSize <= 0 )
		{
			throw new IllegalStateException("Batch size cannot be less than or equal to 0 batch Size = " + batchSize);
		}
		
		int rowStart = 0;
		
		int numberOfPersonNameAuthorities = personService.getCount().intValue();
		log.debug("processing a total of " + numberOfPersonNameAuthorities);
		
		boolean overwriteExistingIndex = true;
		
		int numProcessed = 0;
		
		while(rowStart <= (numberOfPersonNameAuthorities))
		{
			log.debug("row start = " + rowStart);
			log.debug("batch size = " +  batchSize);
			
			// notice the minus one because we are starting at 0
			log.debug("processing " + rowStart + " to " + (rowStart + batchSize - 1) );
			
			List<PersonNameAuthority> names = personService.getPersonNameAuthorityByLastName(rowStart, batchSize, OrderType.ASCENDING_ORDER);
		    numProcessed = numProcessed + names.size();	
		    nameAuthorityIndexService.addNames(names, personNameAuthorityIndexFolder, overwriteExistingIndex);
		    overwriteExistingIndex = false;
		    rowStart = rowStart + batchSize;
		}
		//optimize the index.
		nameAuthorityIndexService.optimize(personNameAuthorityIndexFolder);
		return numProcessed;
	}


	public NameAuthorityIndexService getNameAuthorityIndexService() {
		return nameAuthorityIndexService;
	}


	public void setNameAuthorityIndexService(
			NameAuthorityIndexService nameAuthorityIndexService) {
		this.nameAuthorityIndexService = nameAuthorityIndexService;
	}


	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
