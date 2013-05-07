package edu.ur.hibernate.file.db;

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.file.db.FileInfoChecksumResetHistory;
import edu.ur.file.db.FileInfoChecksumResetHistoryDAO;
import edu.ur.hibernate.HbCrudDAO;

public class HbDefaultFileInfoChecksumResetHistoryDAO implements FileInfoChecksumResetHistoryDAO{
	/* eclipse generated id */
	private static final long serialVersionUID = 1L;
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<FileInfoChecksumResetHistory> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbDefaultFileInfoChecksumResetHistoryDAO() {
		hbCrudDAO = new HbCrudDAO<FileInfoChecksumResetHistory>(FileInfoChecksumResetHistory.class);
	}
	
	/**
	 * Set the session factory.  
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory)
    {
        hbCrudDAO.setSessionFactory(sessionFactory);
    }
	
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	public FileInfoChecksumResetHistory getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(FileInfoChecksumResetHistory entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(FileInfoChecksumResetHistory entity) {
		hbCrudDAO.makePersistent(entity);
	}

}
