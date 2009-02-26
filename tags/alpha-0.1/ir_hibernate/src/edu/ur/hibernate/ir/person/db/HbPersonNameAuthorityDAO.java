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
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.dao.CriteriaHelper;
import edu.ur.hibernate.CriteriaBuilder;
import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonNameAuthorityDAO;

/**
 * Persist person information into the database.
 * 
 * @author Nathan Sarr
 *
 */
public class HbPersonNameAuthorityDAO implements PersonNameAuthorityDAO{
	
	/** Helper for persisting information using hibernate.*/
	private final HbCrudDAO<PersonNameAuthority> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbPersonNameAuthorityDAO() {
		hbCrudDAO = new HbCrudDAO<PersonNameAuthority>(PersonNameAuthority.class);
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
	 * Get the number of people in the system.
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("personCount"));
	}
	
	/**
	 * Get all people ordered by authoritative name.
	 * 
	 * @param startRecord
	 * @param numRecords
	 * @return all people starting from start record up to number of records.
	 */
	public List<PersonNameAuthority> getAllAuthoritativeNameAsc(int startRecord, int numRecords)
	{
		return hbCrudDAO.getByQuery("getAllAuthoritativeNameAsc", startRecord, numRecords);
	}

	
	@SuppressWarnings("unchecked")
	public List<PersonNameAuthority> getPersons(final List<CriteriaHelper> criteriaHelpers,
			final int rowStart, final int rowEnd) {
		List<PersonNameAuthority> people = (List<PersonNameAuthority>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
                CriteriaBuilder criteriaBuilder = new CriteriaBuilder();
                criteriaBuilder.execute(criteria, criteriaHelpers);
                criteria.setFirstResult(rowStart);
                criteria.setMaxResults(rowEnd - rowStart);
                return criteria.list();
            }
        });
		
		 return people;
	}

	
	public Integer getPersonsCount(final List<CriteriaHelper> criteriaHelpers) {
		Integer count = (Integer) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
                CriteriaBuilder criteriaBuilder = new CriteriaBuilder();
                criteriaBuilder.executeWithFiltersOnly(criteria, criteriaHelpers);
                return criteria.setProjection(Projections.rowCount()).uniqueResult();
            }
        });
    	
    	return count;
	}

	
	public List<PersonNameAuthority> getAll() {
		return hbCrudDAO.getAll();
	}

	public PersonNameAuthority getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(PersonNameAuthority entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(PersonNameAuthority entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Get a list of person authorities for a specified sort criteria.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param numberOfResultsToShow - maximum number of results to fetch
	 * @param sortElement - column to sort on 
	 * @param sortType - The order to sort by (ascending/descending)
	 * 
	 * @return List of person authorities
	 */
	@SuppressWarnings("unchecked")
	public List<PersonNameAuthority> getPersons(final int rowStart, 
    		final int numberOfResultsToShow, final String sortElement, final String sortType) {
		
		List<PersonNameAuthority> persons = new LinkedList<PersonNameAuthority>();
		
		persons = (List<PersonNameAuthority>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( sortElement.equalsIgnoreCase("surname") && sortType.equals("asc")) {
			        q = session.getNamedQuery("getPersonsBySurnameOrderAsc");
			    } else if ( sortElement.equalsIgnoreCase("surname") && sortType.equals("desc")){
			        q = session.getNamedQuery("getPersonsBySurnameOrderDesc");
			    } else if ( sortElement.equalsIgnoreCase("forename") && sortType.equals("asc")){
			        q = session.getNamedQuery("getPersonsByForenameOrderAsc");
			    } else if ( sortElement.equalsIgnoreCase("forename") && sortType.equals("desc")){
			        q = session.getNamedQuery("getPersonsByForenameOrderDesc");
			    } 
			    
			    q.setFirstResult(rowStart);
			    q.setMaxResults(numberOfResultsToShow);
	            return q.list();
		    }
	    });
		return persons;	
		
	}
}
