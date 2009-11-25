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

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import edu.ur.dao.CrudDAO;


/**
 * Base class for objects that can persist 
 * data.
 * 
 * @author Nathan Sarr
 *
 * @param <T>
 */
public class HbCrudDAO<T> implements CrudDAO<T>{
	
	protected HibernateTemplate hibernateTemplate;
	
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
    	this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }
    
    
    @SuppressWarnings("unchecked")
	public T getById(Long id, boolean lock) {
        if (lock)
        	return (T)hibernateTemplate.get(clazz, id, LockMode.UPGRADE);
        else
        	return (T)hibernateTemplate.get(clazz, id);
	}

	
	public void makePersistent(T entity) {
		hibernateTemplate.saveOrUpdate(entity);
		
	}

	public void makeTransient(T entity) {
		hibernateTemplate.delete(entity);
	}

	public List<T> getAll() {
	    return findByCriteria();
	}
	
    /**
     * Use this inside subclasses as a convenience method.
     * 
     * @param criterion
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> findByCriteria(Criterion... criterion) {
       	DetachedCriteria dc = DetachedCriteria.forClass(clazz);
    	for( Criterion c: criterion)
    	{
    		dc.add(c);
    	}
    	return (List<T>)hibernateTemplate.findByCriteria(dc);
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
    	return (List<T>) hibernateTemplate.executeFind( new HibernateCallback() 
    			{
    		         public Object doInHibernate(Session session)
    		         {
    		     	    Query query = session.getNamedQuery(queryName);
    		     		query.setFirstResult(startRecord);
    		     		query.setMaxResults(numRecords);
    		     		query.setFetchSize(numRecords);
                        return query.list();
    		         }
    			});
	}
}
