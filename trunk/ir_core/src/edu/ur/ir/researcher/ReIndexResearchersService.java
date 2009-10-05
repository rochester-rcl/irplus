package edu.ur.ir.researcher;

import java.io.File;

/**
 * Re index the researchers in the institutional repository.
 * 
 * @author Nathan Sarr
 *
 */
public interface ReIndexResearchersService {
	
	/**
	 * Re-Index the researchers in the institutional repository
	 * 
	 * @param batchSize - number of researchers to index at a time
	 * @param userIndexFolder - location of the researcher index
	 * @return - number of researchers indexed.
	 */
	public int reIndexResearchers(int batchSize, File researcherIndexFolder);


}
