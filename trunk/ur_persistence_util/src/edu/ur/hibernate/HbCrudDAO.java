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


package edu.ur.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import edu.ur.dao.CrudDAO;
import edu.ur.dao.ListAllDAO;


/**
 * Base class for objects that can persist 
 * data.
 * 
 * @author Nathan Sarr
 *
 * @param <T>
 */
public class HbCrudDAO<T> implements CrudDAO<T>, ListAllDAO, Serializable{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -4547406037263219068L;

	/** hibernate template */
	protected HibernateTemplate hibernateTemplate;
	
	/** reference to the session factory */
	protected SessionFactory sessionFactory;
	
	/**
	 * Class Which this object can persist
	 */
	@SuppressWarnings("unchecked")
	protected Class clazz;
	
	/**
	 * Default Constructor that requires it
	 * takes a class
	 * 
	 * @param clazz
	 */
	@SuppressWarnings("unchecked")
	public HbCrudDAO(Class clazz)
	{
		this.clazz = clazz;
	}
	
	/**
	 * Get the class this class is holding.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class getClazz()
	{
	    return clazz;	
	}
	
	/**
	 * @deprecated - this should no longer be used instead use getSessionFactory.getSession().
	 * @see http://blog.springsource.com/2007/06/26/so-should-you-still-use-springs-hibernatetemplate-andor-jpatemplate/
	 * Get the hibernate template
	 * 
	 * @return hibernat template
	 */
	public HibernateTemplate getHibernateTemplate()
	{
		return hibernateTemplate;
	}
	    
    /**
     * Set the session factory this data access object will use
     * see spring documentation for more information about
     * hibernate integration
     * 
     * @param sessionFactory
     */
    public void setSessionFactory(SessionFactory sessionFactory)
    {
    	this.sessionFactory = sessionFactory;
    	this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }
    
    
    @SuppressWarnings("unchecked")
	public T getById(Long id, boolean lock) {
        if (lock)
        	return (T)sessionFactory.getCurrentSession().get(clazz, id, LockOptions.UPGRADE);
        else
        	return (T)sessionFactory.getCurrentSession().get(clazz, id);
	}

	
	public void makePersistent(T entity) {
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
		
	}

	public void makeTransient(T entity) {
		sessionFactory.getCurrentSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
	    return (List<T>)sessionFactory.getCurrentSession().createCriteria(clazz).list();
	}
	
    /**
     * Helper query for find information found 
     * 
     * @param queryName - the query to execute
     * @param startRecord - the index to start at 
     * @param numRecords - the number of records to get
     * @return the data found found
     */
    @SuppressWarnings("unchecked")
   public List<T> getByQuery(final String queryName, 
		   final int startRecord, 
		   final int numRecords) {
    	Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
    	query.setFirstResult(startRecord);
    	query.setMaxResults(numRecords);
    	query.setFetchSize(numRecords);
        return query.list();
    	
	}
    
	/**
	 * Allows sub classes to get the session factory
	 * @return
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
