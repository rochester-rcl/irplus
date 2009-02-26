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

package edu.ur.hibernate.ir.item.db;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.item.License;
import edu.ur.ir.item.LicenseDAO;

/**
 * Persist License information.
 * 
 * @author Nate Sarr
 *
 */
public class HbLicenseDAO implements LicenseDAO{
	
	private final HbCrudDAO<License> hbCrudDAO;


	/**
	 * Default Constructor
	 */
	public HbLicenseDAO() {
		hbCrudDAO = new HbCrudDAO<License>(License.class);
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
	 * Get a count of the licenses.
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("licenseCount"));
	}

	/**
	 * Get all the licenses with the specified name.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<License> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(License.class);
    	dc.addOrder(Order.asc("name"));
    	dc.addOrder(Order.asc("version"));
    	return (List<License>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all licenses ordered by name.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<License> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllLicenseNameAsc", startRecord, numRecords);
	}

	/**
	 * Find licenses listed by name.
	 * 
	 * @see edu.ur.NonUniqueNameDAO#findByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<License> findByName(String name) {
	  	return (List<License>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getLicenseByName", name);
	}
	
	/**
	 * Get a license by name and version.
	 * 
	 * @see edu.ur.ir.repository.LicenseDAO#getLicense(java.lang.String, java.lang.String)
	 */
	public License getLicense(String name, String version)
	{
		Object[] values = new Object[] {name, version};
		return (License) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getLicenseByNameVersion", 
				values));
	}

	public License getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(License entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(License entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}
	
}
