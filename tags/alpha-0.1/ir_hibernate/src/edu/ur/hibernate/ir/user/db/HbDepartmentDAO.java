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
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.user.Department;
import edu.ur.ir.user.DepartmentDAO;

/**
 * Data access for an department.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbDepartmentDAO implements DepartmentDAO {

	private final HbCrudDAO<Department> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbDepartmentDAO() {
		hbCrudDAO = new HbCrudDAO<Department>(Department.class);
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
	 * Get a count of the departments in the system
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("departmentCount"));
	}

	/**
	 * Get all departments in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<Department> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(Department.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<Department>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all departments in name order.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<Department> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllDepartmentNameAsc", startRecord, numRecords);
	}

	/**
	 * Find the role by unique name.
	 * 
	 * @see edu.ur.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public Department findByUniqueName(String name) {
		return (Department) 
	    HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getDepartmentByName", name));
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	public Department getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(Department entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(Department entity) {
		hbCrudDAO.makeTransient(entity);
	}


	@SuppressWarnings("unchecked")
	public List<Department> getDepartments(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		List<Department> departments = 
			(List<Department>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
		        Query q = null;
		        if (sortType.equalsIgnoreCase("asc")) {
		        	q = session.getNamedQuery("getDepartmentsOrderByNameAsc");
		        } else {
		        	q = session.getNamedQuery("getDepartmentsOrderByNameDesc");
		        }
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
			    q.setReadOnly(true);
			    q.setFetchSize(numberOfResultsToShow);
	            return q.list();

            }
        });

        return departments;
	}
}
