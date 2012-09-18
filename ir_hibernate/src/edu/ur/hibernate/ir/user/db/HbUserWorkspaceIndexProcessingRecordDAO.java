package edu.ur.hibernate.ir.user.db;

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.FileSystem;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecord;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordDAO;

/**
 * Implementation of the User workspace index processing record DAO.
 * 
 * @author Nathan Sarr
 *
 */
public class HbUserWorkspaceIndexProcessingRecordDAO implements UserWorkspaceIndexProcessingRecordDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -6654151793460435743L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<UserWorkspaceIndexProcessingRecord> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public  HbUserWorkspaceIndexProcessingRecordDAO() {
		hbCrudDAO = new HbCrudDAO<UserWorkspaceIndexProcessingRecord>(UserWorkspaceIndexProcessingRecord.class);
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

	public UserWorkspaceIndexProcessingRecord get(FileSystem fileSystem,
			Long userId, IndexProcessingType processingType) {
		
		Object[] values = {fileSystem.getId(), fileSystem.getFileSystemType().getType(), userId, processingType.getId()};
		return (UserWorkspaceIndexProcessingRecord) 
		    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("userWorkspaceProcessingRecByItemIdTypeUserIdProcessingType", values));

	}

	@SuppressWarnings("unchecked")
	public List<UserWorkspaceIndexProcessingRecord> getAllOrderByUserIdDate() {
		return (List<UserWorkspaceIndexProcessingRecord>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getUserWorkspaceIndexProcessingRecordsByUserIdDateItemId");
	}

	public List<UserWorkspaceIndexProcessingRecord> getAll() {
		return hbCrudDAO.getAll();
	}

	public UserWorkspaceIndexProcessingRecord getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(UserWorkspaceIndexProcessingRecord entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(UserWorkspaceIndexProcessingRecord entity) {
		hbCrudDAO.makeTransient(entity);	
	}

	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("userWorkspaceIndexProcessingRecordCount"));
	}

}
