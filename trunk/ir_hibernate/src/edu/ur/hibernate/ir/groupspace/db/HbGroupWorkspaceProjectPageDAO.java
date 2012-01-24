package edu.ur.hibernate.ir.groupspace.db;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageDAO;

/**
 * @author Nathan Sarr
 *
 */
public class HbGroupWorkspaceProjectPageDAO implements GroupWorkspaceProjectPageDAO {

	// eclipse generated id.
	private static final long serialVersionUID = 4701854040973241277L;
	
	/** hibernate helper  */
	private final HbCrudDAO<GroupWorkspaceProjectPage> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceProjectPageDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceProjectPage>(GroupWorkspaceProjectPage.class);
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


	public GroupWorkspaceProjectPage getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(GroupWorkspaceProjectPage entity) {
		hbCrudDAO.makePersistent(entity);
		
	}

	public void makeTransient(GroupWorkspaceProjectPage entity) {
		hbCrudDAO.makeTransient(entity);
	}

	public GroupWorkspaceProjectPage findByUniqueName(String name) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("");
		q.setParameter("lowerCaseName", name.toLowerCase());
		return (GroupWorkspaceProjectPage)q.uniqueResult();
	}

}
