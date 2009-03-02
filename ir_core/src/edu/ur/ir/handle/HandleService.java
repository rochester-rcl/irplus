package edu.ur.ir.handle;

/**
 * Interface for dealing with handle information.
 * 
 * @author Nathan Sarr
 *
 */
public interface HandleService {
	
	/**
	 * Add the naming authority to the system.
	 * 
	 * @param namingAuthority - naming authority value
	 * @param localName - local name value
	 * 
	 * @return - the created naming authority
	 */
	public NameAuthority addNamingAuthority(String namingAuthority, String localName);

}
