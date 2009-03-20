package edu.ur.ir.handle;

import java.util.List;

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
	 * Get a count of name authorities.
	 * 
	 * @return the number of name authorities
	 */
	public Long getNameAuthorityCount();
	
	/**
	 * Get a count of the number of handles.
	 * 
	 * @return count in number of handles
	 */
	public Long getHandleCount();
	
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
	 * Get a count of the handles with the specified name authority.
	 *  
	 * @param nameAuthorityId - id for the name authority
	 * @return - count of handles found for the name authority
	 */
	public Long getHandleCountForNameAuthority(Long nameAuthorityId);
	
	/**
	 * Get all name authorities in the system.
	 * 
	 * @return all name authorities in the system.
	 */
	public List<HandleNameAuthority> getAllNameAuthorities();
	
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
	 * Get all handles for a given name authority.
	 * 
	 * @param nameAuthority - name authority
	 * @return list of handles found for the name authority
	 */
	public List<HandleInfo> getAllHandlesForAuthority(String nameAuthority);
	
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
