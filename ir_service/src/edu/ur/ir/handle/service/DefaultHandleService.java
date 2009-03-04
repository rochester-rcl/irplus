package edu.ur.ir.handle.service;

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
		// TODO Auto-generated method stub
		return null;
	}

	public HandleNameAuthority getNameAuthority(String nameAuthority) {
		// TODO Auto-generated method stub
		return null;
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


}
