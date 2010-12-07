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

package edu.ur.hibernate.ir.person.db;

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;

import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorDAO;


/**
 * Database access for a contributor.
 * 
 * @author Nathan Sarr
 *
 */
public class HbContributorDAO implements ContributorDAO{
	
	/** eclipse generated id. */
	private static final long serialVersionUID = 1475362787137379446L;
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<Contributor> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbContributorDAO() {
		hbCrudDAO = new HbCrudDAO<Contributor>(Contributor.class);
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
	 * Get a count of the contributors.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("contributorCount"));
	}
	
	/**
	 * Find the contibutor by person name id, contributor type id.
	 * 
	 * @see edu.ur.ir.person.ContributorDAO#findByNameType(java.lang.Long, java.lang.Long)
	 */
	public Contributor findByNameType(Long personNameId, Long contributorTypeId)
	{
		Object[] values = new Object[] {personNameId, contributorTypeId};
		return (Contributor)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("findContributorByNameType", values));
	}
	
	/**
	 * Find the contibutor by person name id, contributor type id.
	 * 
	 * @see edu.ur.ir.person.ContributorDAO#findByNameType(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Contributor> getAllForName(Long personNameId)
	{
		return (List<Contributor>)hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllContributorForName", personNameId);
	}

	public Contributor getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(Contributor entity) {
		hbCrudDAO.makePersistent(entity);
		
	}

	public void makeTransient(Contributor entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
