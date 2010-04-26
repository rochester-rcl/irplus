package edu.ur.ir.person;

import java.io.File;

/**
 * Re index the person name authorities.
 * 
 * @author Nathan Sarr
 *
 */
public interface ReIndexPersonNameAuthoritiesService {
	
	/**
	 * Re-Index the person name authorities in the institutional repository
	 * 
	 * @param batchSize - number of names to index at a time
	 * @param userIndexFolder - location of the person name authority index
	 * @return - number of person name authorities indexed.
	 */
	public int reIndexNameAuthorities(int batchSize, File personNameAuthorityIndexFolder);


}
