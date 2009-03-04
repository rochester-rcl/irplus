package edu.ur.ir.handle;

/**
 * Interface for dealing with handle information.
 * 
 * @author Nathan Sarr
 *
 */
public interface HandleService {
	
	/**
	 * Get a name authority 
	 * 
	 * @param nameAuthority - name authority
	 * 
	 * @return the name authority with the name authority
	 */
	public HandleNameAuthority getNameAuthority(String nameAuthority);
	
	/**
	 * Get a name authority by it's unique id.
	 * 
	 * @param id - id of the name authority.
	 * 
	 * @return HandleNameAuthority or null if none found
	 */
	public HandleNameAuthority getNameAuthority(Long id, boolean lock);
	
	/**
	 * Save the handle name authority.
	 * 
	 * @param handleNameAuthority
	 */
	public void save(HandleNameAuthority handleNameAuthority);
	
	/**
	 * Delete the handle name authority.
	 * 
	 * @param handleNameAuthority
	 */
	public void delete(HandleNameAuthority handleNameAuthority);
	
	/**
	 * Get the handle information.
	 * 
	 * @param id - id of the handle info
	 * @param lock - lock the handle inforamtion
	 * 
	 * @return Handle info if found otherwise null
	 */
	public HandleInfo getHandleInfo(Long id, boolean lock);
	
	/**
	 * @param fullHandle in the form &lt;NamingAuthority&gt;/&lt;LocalName&gt;
	 * 
	 * @return the handle info if found otherwise null.
	 */
	public HandleInfo getHandleInfo(String fullHandle);
	
	/**
	 * Save the handle information
	 * 
	 * @param handleNameAuthority
	 */
	public void save(HandleInfo handleInfo);
	
	/**
	 * Delete the handle name authority.
	 * 
	 * @param handleNameAuthority
	 */
	public void delete(HandleInfo handleInfo);
}
