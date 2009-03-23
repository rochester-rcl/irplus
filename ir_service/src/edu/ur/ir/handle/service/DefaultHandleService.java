/**  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  


package edu.ur.ir.handle.service;

import java.util.List;

import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.handle.HandleInfoDAO;
import edu.ur.ir.handle.HandleNameAuthority;
import edu.ur.ir.handle.HandleNameAuthorityDAO;
import edu.ur.ir.handle.HandleService;

/**
 * Service for dealing with handle information.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultHandleService implements HandleService {
	
	/** Handle info data access */
	private HandleInfoDAO handleInfoDAO;
	

	/** Data access for handle name authority information */
	private HandleNameAuthorityDAO handleNameAuthorityDAO;

	public void delete(HandleNameAuthority handleNameAuthority) {
		handleNameAuthorityDAO.makeTransient(handleNameAuthority);
	}

	public void delete(HandleInfo handleInfo) {
		handleInfoDAO.makeTransient(handleInfo);
	}

	/**
	 * @see edu.ur.ir.handle.HandleService#getHandleInfo(java.lang.Long, boolean)
	 */
	public HandleInfo getHandleInfo(Long id, boolean lock) {
		return handleInfoDAO.getById(id, lock);
	}

	/**
	 * @see edu.ur.ir.handle.HandleService#getHandleInfo(java.lang.String)
	 */
	public HandleInfo getHandleInfo(String fullHandle) {
		HandleInfo info = null;
		if( fullHandle != null )
		{
		    String[] parts = fullHandle.split("/");
		    if( parts.length == 2)
		    {
		    	String prefix = parts[0];
		    	String localName = parts[1];
		    	System.out.println("prefix = " + prefix + " localName = " + localName);
		    	info =handleInfoDAO.get(prefix, localName);
		    }
		}
		return info;
	}

	/**
	 * @see edu.ur.ir.handle.HandleService#getNameAuthority(java.lang.String)
	 */
	public HandleNameAuthority getNameAuthority(String nameAuthority) {
		return handleNameAuthorityDAO.findByUniqueName(nameAuthority);
	}

	/**
	 * @see edu.ur.ir.handle.HandleService#getNameAuthority(java.lang.Long, boolean)
	 */
	public HandleNameAuthority getNameAuthority(Long id, boolean lock) {
		return handleNameAuthorityDAO.getById(id, lock);
	}

	/**
	 * @see edu.ur.ir.handle.HandleService#save(edu.ur.ir.handle.HandleNameAuthority)
	 */
	public void save(HandleNameAuthority handleNameAuthority) {
		handleNameAuthorityDAO.makePersistent(handleNameAuthority);
	}

	/**
	 * @see edu.ur.ir.handle.HandleService#save(edu.ur.ir.handle.HandleInfo)
	 */
	public void save(HandleInfo handleInfo) {
		handleInfoDAO.makePersistent(handleInfo);
	}
	
	/**
	 * Get the handle info DAO object
	 * @return
	 */
	public HandleInfoDAO getHandleInfoDAO() {
		return handleInfoDAO;
	}

	/**
	 * Set the handle info DAO object.
	 * @param handleInfoDAO
	 */
	public void setHandleInfoDAO(HandleInfoDAO handleInfoDAO) {
		this.handleInfoDAO = handleInfoDAO;
	}

	/**
	 * Get the handle name authority DAO object
	 * @return
	 */
	public HandleNameAuthorityDAO getHandleNameAuthorityDAO() {
		return handleNameAuthorityDAO;
	}

	/**
	 * Set the handle name authority dao object.
	 * 
	 * @param handleNameAuthorityDAO
	 */
	public void setHandleNameAuthorityDAO(
			HandleNameAuthorityDAO handleNameAuthorityDAO) {
		this.handleNameAuthorityDAO = handleNameAuthorityDAO;
	}

	/**
	 * @see edu.ur.ir.handle.HandleService#getHandleCount()
	 */
	public Long getHandleCount() {
		return handleInfoDAO.getCount();
	}

	/**
	 * @see edu.ur.ir.handle.HandleService#getNameAuthorityCount()
	 */
	public Long getNameAuthorityCount() {
		return handleNameAuthorityDAO.getCount();
	}

	/**
	 * @see edu.ur.ir.handle.HandleService#getHandleCountForNameAuthority(java.lang.Long)
	 */
	public Long getHandleCountForNameAuthority(Long nameAuthorityId) {
	    return handleInfoDAO.getHandleCountForNameAuthority(nameAuthorityId);
	}

	/**
	 * @see edu.ur.ir.handle.HandleService#getAllNameAuthorities()
	 */
	@SuppressWarnings("unchecked")
	public List<HandleNameAuthority> getAllNameAuthorities() {
		return handleNameAuthorityDAO.getAll();
	}

	
	/**
	 * @see edu.ur.ir.handle.HandleService#getAllHandlesForAuthority(java.lang.String)
	 */
	public List<HandleInfo> getAllHandlesForAuthority(String nameAuthority) {
        return handleInfoDAO.getAllHandlesForAuthority(nameAuthority);		
	}


}
