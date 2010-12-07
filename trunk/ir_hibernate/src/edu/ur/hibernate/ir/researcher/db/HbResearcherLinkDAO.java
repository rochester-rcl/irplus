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

package edu.ur.hibernate.ir.researcher.db;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherLinkDAO;

/**
 * Hibernate implementation of researcher link data access and storage.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbResearcherLinkDAO implements ResearcherLinkDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = 8043336684186921114L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<ResearcherLink> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbResearcherLinkDAO() {
		hbCrudDAO = new HbCrudDAO<ResearcherLink>(ResearcherLink.class);
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
	 * Return ResearcherLink by id
	 */
	public ResearcherLink getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ResearcherLink entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ResearcherLink entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Get the root researcher links for given researcher
	 * 
	 * @see edu.ur.ir.researcher.ResearcherLinkDAO#getRootResearcherLinks(Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherLink> getRootResearcherLinks( final Long researcherId)
	{
		List<ResearcherLink> items = (List<ResearcherLink>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
                criteria.createCriteria("researcher").add(Restrictions.idEq(researcherId));
                criteria.add(Restrictions.isNull("parentFolder"));
                return criteria.list();
            }
        });

        return items;
	}
    
	/**
	 * Get researcher links for specified researcher and specified parent folder
	 * 
	 * @see edu.ur.ir.researcher.ResearcherLinkDAO#getSubResearcherLinks(Long, Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherLink> getSubResearcherLinks( final Long researcherId, final Long parentCollectionId)
	{
		List<ResearcherLink> items = (List<ResearcherLink>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
                
                criteria.createCriteria("researcher").add(Restrictions.idEq(researcherId));
                criteria.createCriteria("parentFolder").add(Restrictions.idEq(parentCollectionId));
                return criteria.list();
            }
        });

        return items;
	}

	/**
	 * Find the specified items for the given researcher.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherLinkDAO#getResearcherLinks(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherLink> getResearcherLinks(final Long researcherId, final List<Long> itemIds) {
		List<ResearcherLink> foundItems = new LinkedList<ResearcherLink>();
		if( itemIds.size() > 0 )
		{
		   foundItems = 
			    (List<ResearcherLink>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
                public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                    Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
                    criteria.createCriteria("researcher").add(Restrictions.idEq(researcherId));
                    criteria.add(Restrictions.in("id", itemIds));
                    return criteria.list();
                }
            });
		}
		return foundItems;
	}

}
