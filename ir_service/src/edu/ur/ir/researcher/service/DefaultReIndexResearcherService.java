package edu.ur.ir.researcher.service;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.researcher.ReIndexResearchersService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherIndexService;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.order.OrderType;

/**
 * Service to re-index researcher information.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultReIndexResearcherService implements ReIndexResearchersService{

	/** eclipse generated id */
	private static final long serialVersionUID = -375213147610576261L;

	/** Service for dealing with researchers. */
	private ResearcherService researcherService;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultReIndexResearcherService.class);
	
	/** Service for indexing user information */
	private ResearcherIndexService researcherIndexService;
	
	public int reIndexResearchers(int batchSize, File researcherIndexFolder) {
		log.debug("Re-Indexing researchers");
		
		if(batchSize <= 0 )
		{
			throw new IllegalStateException("Batch size cannot be less than or equal to 0 batch Size = " + batchSize);
		}
		
		int rowStart = 0;
		
		int numberOfResearchers = researcherService.getPublicResearcherCount().intValue();
		log.debug("processing a total of " + numberOfResearchers);
		
		boolean overwriteExistingIndex = true;
		
		int numProcessed = 0;
		
		while(rowStart <= (numberOfResearchers))
		{
			log.debug("row start = " + rowStart);
			log.debug("batch size = " +  batchSize);
			
			// notice the minus one because we are starting at 0
			log.debug("processing " + rowStart + " to " + (rowStart + batchSize - 1) );
			
			List<Researcher> researchers = researcherService.getPublicResearchersByLastFirstName(rowStart, batchSize, OrderType.ASCENDING_ORDER);
		    numProcessed = numProcessed + researchers.size();	
		    researcherIndexService.addResearchers(researchers, researcherIndexFolder, overwriteExistingIndex);
		    overwriteExistingIndex = false;
		    rowStart = rowStart + batchSize;
		}
		//optimize the index.
		researcherIndexService.optimize(researcherIndexFolder);
		
		return numProcessed;
		
	}

	public ResearcherService getResearcherService() {
		return researcherService;
	}

	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}

	public ResearcherIndexService getResearcherIndexService() {
		return researcherIndexService;
	}

	public void setResearcherIndexService(
			ResearcherIndexService researcherIndexService) {
		this.researcherIndexService = researcherIndexService;
	}

}
