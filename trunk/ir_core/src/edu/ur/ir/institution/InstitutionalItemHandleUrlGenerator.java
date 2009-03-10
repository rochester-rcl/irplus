package edu.ur.ir.institution;

/**
 * Generates a URL for an institutional item version.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalItemHandleUrlGenerator 
{
	
	/**
	 * This creates a new url which can be placed as the url for the handle.
	 * 
	 * @param item - to create the url for.
	 * @return the url
	 */
	public String getUrl(InstitutionalItemVersion item);

}
