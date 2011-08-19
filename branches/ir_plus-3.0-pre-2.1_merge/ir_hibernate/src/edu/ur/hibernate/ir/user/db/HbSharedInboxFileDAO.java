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

package edu.ur.hibernate.ir.user.db;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.SharedInboxFileDAO;

/**
 * Hibernate implemenation of saving a personal inbox file.
 * 
 * @author Nathan Sarr
 *
 */
public class HbSharedInboxFileDAO implements SharedInboxFileDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -1868166571342032403L;
	
	/** hibernate helper   */
	private final HbCrudDAO<SharedInboxFile> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbSharedInboxFileDAO() {
		hbCrudDAO = new HbCrudDAO<SharedInboxFile>(SharedInboxFile.class);
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
	
	/**
	 * Get a count of personal inbox files.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("sharedInboxFileCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get the personal file inbox by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public SharedInboxFile getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	/**
	 * Save the personal inbox file to the system.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(SharedInboxFile entity) {
		hbCrudDAO.makePersistent(entity);
		
	}

	
	/**
	 * Remove the personal inbox file from the system.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(SharedInboxFile entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	/**
	 * Determine the number of in-box files the user has.
	 * 
	 * @see edu.ur.ir.user.SharedInboxFileDAO#getSharedInboxFileCount(edu.ur.ir.user.IrUser)
	 */
	public Long getSharedInboxFileCount(IrUser user) {
		Object[] values = new Object[] {user.getId()};
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().
				findByNamedQuery("sharedInboxFileCountForUser", values));
	}
	
	
	/**
	 * Get the shared inbox files for the user.
	 * 
	 * @see edu.ur.ir.user.SharedInboxFileDAO#getSharedInboxFiles(edu.ur.ir.user.IrUser)
	 */
	@SuppressWarnings("unchecked")
	public List<SharedInboxFile> getSharedInboxFiles(IrUser user) {
		Object[] values = new Object[] {user.getId()};
		return (List<SharedInboxFile>)(hbCrudDAO.getHibernateTemplate().
				findByNamedQuery("sharedInboxFilesUser", values));
	}
	
	/**
	 * Find the specified files.
	 * 
	 * @see edu.ur.ir.user.PersonalFolderDAO#getSharedInboxFiles(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<SharedInboxFile> getSharedInboxFiles(final Long userId, final List<Long> fileIds) {
		List<SharedInboxFile> foundFiles = new LinkedList<SharedInboxFile>();
		if( fileIds.size() > 0 )
        {
			foundFiles = (List<SharedInboxFile>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
		    public Object doInHibernate(Session session)
                throws HibernateException, SQLException {
                    Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
                    criteria.createCriteria("sharedWithUser").add(Restrictions.idEq(userId));
                    criteria.add(Restrictions.in("id",fileIds));
                return criteria.list();
                }
             });
        }
		return foundFiles;
	}


}
