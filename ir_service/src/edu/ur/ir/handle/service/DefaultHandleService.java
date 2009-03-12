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

	public HandleInfo getHandleInfo(Long id, boolean lock) {
		return handleInfoDAO.getById(id, lock);
	}

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

	public HandleNameAuthority getNameAuthority(String nameAuthority) {
		return handleNameAuthorityDAO.findByUniqueName(nameAuthority);
	}

	public HandleNameAuthority getNameAuthority(Long id, boolean lock) {
		return handleNameAuthorityDAO.getById(id, lock);
	}

	public void save(HandleNameAuthority handleNameAuthority) {
		handleNameAuthorityDAO.makePersistent(handleNameAuthority);
	}

	public void save(HandleInfo handleInfo) {
		handleInfoDAO.makePersistent(handleInfo);
	}
	
	public HandleInfoDAO getHandleInfoDAO() {
		return handleInfoDAO;
	}

	public void setHandleInfoDAO(HandleInfoDAO handleInfoDAO) {
		this.handleInfoDAO = handleInfoDAO;
	}

	public HandleNameAuthorityDAO getHandleNameAuthorityDAO() {
		return handleNameAuthorityDAO;
	}

	public void setHandleNameAuthorityDAO(
			HandleNameAuthorityDAO handleNameAuthorityDAO) {
		this.handleNameAuthorityDAO = handleNameAuthorityDAO;
	}

	public Long getHandleCount() {
		return handleInfoDAO.getCount();
	}

	public Long getNameAuthorityCount() {
		return handleNameAuthorityDAO.getCount();
	}

	public Long getHandleCountForNameAuthority(Long nameAuthorityId) {
	    return handleInfoDAO.getHandleCountForNameAuthority(nameAuthorityId);
	}

	@SuppressWarnings("unchecked")
	public List<HandleNameAuthority> getAllNameAuthorities() {
		return handleNameAuthorityDAO.getAll();
	}


}
