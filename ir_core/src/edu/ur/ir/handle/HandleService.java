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
	public NameAuthority createNamingAuthority(String namingAuthority, String localName);
	
	/**
	 * Get a name authority 
	 * 
	 * @param nameAuthority - name authority
	 * @param localName - local name
	 * 
	 * @return the name authority with the name authority and local name
	 */
	public NameAuthority getNameAuthority(String nameAuthority, String localName);
	
	

}
