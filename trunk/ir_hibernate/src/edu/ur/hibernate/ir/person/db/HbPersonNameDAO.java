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

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameDAO;
import edu.ur.order.OrderType;


/**
 * Hibernate persistence layer for Person names.
 * 
 * @author Nathan Sarr
 *
 */
public class HbPersonNameDAO  implements PersonNameDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = -4929168805622244755L;
	
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
	public Integer getPersonNamesCount(final Long personId) {
		Integer count = (Integer) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
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
	public List<PersonName> getPersonNames(final int rowStart, final int rowEnd, final Long personId) {
		List<PersonName> names = 
			(List<PersonName>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
                
                criteria.createCriteria("personNameAuthority").add(Restrictions.idEq(personId));
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
	 * @see edu.ur.ir.person.PersonNameDAO#getCollectionPersonNamesBetweenChar(int, int, edu.ur.ir.institution.InstitutionalCollection, char, char, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonName> getCollectionPersonNamesBetweenChar(final int rowStart,
			final int maxResults, final InstitutionalCollection collection, final char firstChar,
			final char lastChar, final OrderType orderType) {
		
		 List<PersonName> personNames = (List<PersonName>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getCollectionPersonNameByCharRangeOrderDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getCollectionPersonNameByCharRangeOrderAsc");
			    }
			    
			    q.setLong(0, collection.getLeftValue());
			    q.setLong(1, collection.getRightValue());
			    q.setLong(2, collection.getTreeRoot().getId());
			    q.setCharacter( 3, Character.toLowerCase(firstChar) );
			    q.setCharacter( 4, Character.toLowerCase(lastChar) );
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setFetchSize(maxResults);
	            return q.list();
		    }
	    });
        return personNames;	
	}

	/**
	 * @see edu.ur.ir.person.PersonNameDAO#getCollectionPersonNamesByChar(int, int, edu.ur.ir.institution.InstitutionalCollection, char, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonName> getCollectionPersonNamesByChar(final int rowStart,
			final int maxResults, final InstitutionalCollection collection,
			final char firstChar, final OrderType orderType) {
		
         List<PersonName> personNames = (List<PersonName>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getCollectionPersonNameByCharOrderDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getCollectionPersonNameByCharOrderAsc");
			    }
			    
			    q.setLong(0, collection.getLeftValue());
			    q.setLong(1, collection.getRightValue());
			    q.setLong(2, collection.getTreeRoot().getId());
			    q.setCharacter( 3, Character.toLowerCase(firstChar) );
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setFetchSize(maxResults);
	            return q.list();
		    }
	    });
        return personNames;	
	}

	/**
	 * @see edu.ur.ir.person.PersonNameDAO#getCollectionPersonNamesByLastName(int, int, edu.ur.ir.institution.InstitutionalCollection, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonName> getCollectionPersonNamesByLastName(final int rowStart,
			final int maxResults, final InstitutionalCollection collection,
			final OrderType orderType) {
		
		 List<PersonName> personNames = (List<PersonName>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getCollectionPersonNameByNameOrderDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getCollectionPersonNameByNameOrderAsc");
			    }
			    
			    q.setLong(0, collection.getLeftValue());
			    q.setLong(1, collection.getRightValue());
			    q.setLong(2, collection.getTreeRoot().getId());
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setFetchSize(maxResults);
	            return q.list();
		    }
	    });
	
        return personNames;	
	}

	/**
	 * @see edu.ur.ir.person.PersonNameDAO#getCount(char)
	 */
	public Long getCount(char nameFirstChar) {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("allPersonNameByChar", Character.valueOf(Character.toLowerCase(nameFirstChar))));
	}

	/**
	 * @see edu.ur.ir.person.PersonNameDAO#getCount(char, char)
	 */
	public Long getCount(char lastNameFirstCharRange, char lastNamelastCharRange) {
		Object[] values = new Object[]{Character.valueOf(Character.toLowerCase(lastNameFirstCharRange)), Character.valueOf(Character.toLowerCase(lastNamelastCharRange))};
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("personNameCountByCharRange", values));
	}

	/**
	 * @see edu.ur.ir.person.PersonNameDAO#getCount(edu.ur.ir.institution.InstitutionalCollection, char)
	 */
	public Long getCount(InstitutionalCollection collection,
			char lastNameFirstChar) {
		Object[] values = new Object[]{collection.getLeftValue(), collection.getRightValue(), 
				collection.getTreeRoot().getId(), Character.valueOf(Character.toLowerCase(lastNameFirstChar))};

		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("personCollectionNameCountByChar", values));

	}

	/**
	 * @see edu.ur.ir.person.PersonNameDAO#getCount(edu.ur.ir.institution.InstitutionalCollection, char, char)
	 */
	public Long getCount(InstitutionalCollection collection,
			char lastNameFirstCharRange, char lastNamelastCharRange) {
		Object[] values = new Object[]{collection.getLeftValue(), collection.getRightValue(), 
				collection.getTreeRoot().getId(), Character.valueOf(Character.toLowerCase(lastNameFirstCharRange)), Character.valueOf(Character.toLowerCase(lastNamelastCharRange))};

		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("personCollectionNameCountByCharRange", values));
	}

	/**
	 * @see edu.ur.ir.person.PersonNameDAO#getPersonNamesBetweenChar(int, int, char, char, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonName> getPersonNamesBetweenChar(final int rowStart,
			final int maxResults, final char firstChar, final char lastChar, final OrderType orderType) {
		
		 List<PersonName> personNames = (List<PersonName>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if(orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getPersonNameByCharRangeOrderDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getPersonNameByCharRangeOrderAsc");
			    }
			    q.setCharacter( 0, Character.toLowerCase(firstChar) );
			    q.setCharacter( 1, Character.toLowerCase(lastChar) );
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setFetchSize(maxResults);
	            return q.list();
		    }
	    });
        return personNames;	
	}

	/**
	 * @see edu.ur.ir.person.PersonNameDAO#getPersonNamesByChar(int, int, char, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<PersonName> getPersonNamesByChar(final int rowStart, final int maxResults,
			final char firstChar, final OrderType orderType) {
		
		 List<PersonName> personNames = (List<PersonName>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getPersonNameByCharOrderDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getPersonNameByCharOrderAsc");
			    }
			    q.setCharacter( 0, Character.toLowerCase(firstChar) );
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setFetchSize(maxResults);
	            return q.list();
		    }
	    });
        return personNames;	
	}

	/**
	 * @see edu.ur.ir.person.PersonNameDAO#getPersonNamesByLastName(int, int, edu.ur.order.OrderType)
	 */
	public List<PersonName> getPersonNamesByLastName(int rowStart,
			int maxResults, OrderType orderType) {
		
		if( orderType.equals(OrderType.DESCENDING_ORDER))
		{
			return hbCrudDAO.getByQuery("getAllPersonNameDesc", rowStart, maxResults);
		}
		else
		{
			return hbCrudDAO.getByQuery("getAllPersonNameAsc", rowStart, maxResults);
		}
		
	}

	
	/**
	 * @see edu.ur.ir.person.PersonNameDAO#getCount(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public Long getCount(InstitutionalCollection collection) {
		Object[] values = new Object[]{collection.getLeftValue(), collection.getRightValue(), 
				collection.getTreeRoot().getId()};

		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("personCollectionNameCount", values));
	}


}
