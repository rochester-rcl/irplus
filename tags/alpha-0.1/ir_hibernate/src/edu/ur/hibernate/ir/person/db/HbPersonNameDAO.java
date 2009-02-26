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

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.dao.CriteriaHelper;
import edu.ur.hibernate.CriteriaBuilder;
import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameDAO;


/**
 * Hibernate persistence layer for Person names.
 * 
 * @author Nathan Sarr
 *
 */
public class HbPersonNameDAO  implements PersonNameDAO {

	/** Helper for persisting information using hibernate.*/
	private final HbCrudDAO<PersonName> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbPersonNameDAO() {
		hbCrudDAO = new HbCrudDAO<PersonName>(PersonName.class);
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
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<PersonName> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(PersonName.class);
    	dc.addOrder(Order.asc("surname"));
    	dc.addOrder(Order.asc("forename"));
    	dc.addOrder(Order.asc("familyName"));
    	return (List<PersonName>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Bet all people ordered by last name, first name
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<PersonName> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllPersonNameAsc", startRecord, numRecords);
	}
	
	/**
	 * Returns all people with a first name like the specified value.
	 * 
	 * @param firstName
	 * @param startRecord
	 * @param numRecords
	 */
	@SuppressWarnings("unchecked")
	public List<PersonName> findPersonLikeFirstName(String firstName, final int startRecord, final int numRecords)
	{
		final String qFirstName = firstName + '%';
		//return getByQuery("findPersonLikeFirstName", startRecord, numRecords);
	  	return (List<PersonName>) hbCrudDAO.getHibernateTemplate().executeFind( new HibernateCallback() 
		{
	         public Object doInHibernate(Session session)
	         {
	     	    Query query = session.getNamedQuery("findPersonLikeFirstName");
	     		query.setFirstResult(startRecord);
	     		query.setMaxResults(numRecords);
	     		query.setString(0, qFirstName);
	     		query.setFetchSize(numRecords);
                return query.list();
	         }
		});
	}
	
	/**
	 * Finds all people with a last name like the specified last name.
	 * 
	 * @param lastName
	 * @param startRecord
	 * @param numRecords
	 */
	@SuppressWarnings("unchecked")
	public List<PersonName> findPersonLikeLastName(String lastName, final int startRecord, final int numRecords)
	{
		final String qLastName = lastName + '%';
		//return getByQuery("findPersonLikeFirstName", startRecord, numRecords);
	  	return (List<PersonName>) hbCrudDAO.getHibernateTemplate().executeFind( new HibernateCallback() 
		{
	         public Object doInHibernate(Session session)
	         {
	     	    Query query = session.getNamedQuery("findPersonLikeLastName");
	     		query.setFirstResult(startRecord);
	     		query.setMaxResults(numRecords);
	     		query.setString(0, qLastName);
	     		query.setFetchSize(numRecords);
                return query.list();
	         }
		});
	}
	
	
	/**
	 * Finds all people with the specified first and last name.
	 * 
	 * @param firstName
	 * @param lastName
	 * @param startRecord
	 * @param numRecords
	 */
	@SuppressWarnings("unchecked")
	public List<PersonName> findPersonLikeFirstLastName(String firstName, String lastName, 
			final int startRecord, final int numRecords)
	{
		final String qLastName = lastName + '%';
		final String qFirstName = firstName + '%';
		//return getByQuery("findPersonLikeFirstName", startRecord, numRecords);
	  	return (List<PersonName>) hbCrudDAO.getHibernateTemplate().executeFind( new HibernateCallback() 
		{
	         public Object doInHibernate(Session session)
	         {
	     	    Query query = session.getNamedQuery("findPersonLikeFirstLastName");
	     		query.setFirstResult(startRecord);
	     		query.setMaxResults(numRecords);
	     		query.setString(0, qLastName);
	     		query.setString(1, qFirstName);
	     		query.setFetchSize(numRecords);
                return query.list();
	         }
		});
	}

	/**
	 * @see edu.ur.ir.person.PersonNameDAO#getPersonNamesCount(java.util.List, java.lang.Long)
	 */
	public Integer getPersonNamesCount(final List<CriteriaHelper> criteriaHelpers,
			final Long personId) {
		Integer count = (Integer) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
                CriteriaBuilder criteriaBuilder = new CriteriaBuilder();
                criteriaBuilder.executeWithFiltersOnly(criteria, criteriaHelpers);
                criteria.createCriteria("personNameAuthority").add(Restrictions.idEq(personId));
                return criteria.setProjection(Projections.rowCount()).uniqueResult();
            }
        });
    	
    	return count;
	}

	/**
	 * @see edu.ur.ir.person.PersonNameDAO#getPersonNames(java.util.List, int, int, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonName> getPersonNames(final List<CriteriaHelper> criteriaHelpers,
			final int rowStart, final int rowEnd, final Long personId) {
		List<PersonName> names = 
			(List<PersonName>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
                
                CriteriaBuilder criteriaBuilder = new CriteriaBuilder();
                criteria.createCriteria("personNameAuthority").add(Restrictions.idEq(personId));
                criteriaBuilder.execute(criteria, criteriaHelpers);
                criteria.setFirstResult(rowStart);
                criteria.setMaxResults(rowEnd - rowStart);
                return criteria.list();
            }
        });

        return names;
	}

	
	/**
	 * @see edu.ur.dao.CountableDAO#getCount()
	 * 
	 */
	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("personNameCount"));
	}

	
	/**
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<PersonName> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public PersonName getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(PersonName entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(PersonName entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Searches for person name containing the given string
	 * 
	 * @see edu.ur.ir.person.PersonNameDAO#searchPersonName(String)
	 */
	public List<PersonName> searchPersonName(String name) {
		return null;
	}
}
