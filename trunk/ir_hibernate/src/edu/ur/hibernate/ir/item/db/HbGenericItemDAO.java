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

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.GenericItemDAO;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.PersonName;

/**
 * Persist Item information.
 * 
 * @author Nathan Sarr
 *
 */
public class HbGenericItemDAO implements GenericItemDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -1889366463922904654L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<GenericItem> hbCrudDAO;


	/**
	 * Default Constructor
	 */
	public HbGenericItemDAO() {
		hbCrudDAO = new HbCrudDAO<GenericItem>(GenericItem.class);;
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
	 * Get a count of the items.
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("itemCount"));
	}

	/**
	 * Get all the items with the specified name.
	 * 
	 * @see edu.ur.NameListDAO#getAllNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<GenericItem> getAllNameOrder() {
		DetachedCriteria dc = DetachedCriteria.forClass(GenericItem.class);
    	dc.addOrder(Order.asc("name"));
    	return (List<GenericItem>) hbCrudDAO.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * Get all item ordered by name.
	 * 
	 * @see edu.ur.NameListDAO#getAllOrderByName(int, int)
	 */
	public List<GenericItem> getAllOrderByName(int startRecord, int numRecords) {
		return hbCrudDAO.getByQuery("getAllItemNameAsc", startRecord, numRecords);
	}

	/**
	 * Find items listed by name.
	 * 
	 * @see edu.ur.NonUniqueNameDAO#findByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<GenericItem> findByName(String name) {
	  	return (List<GenericItem>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getItemByName", name);
	}
	
	/**
	 * Find the item for the specified collection.
	 * 
	 * @see edu.ur.ir.item.GenericItemDAO#getItemForCollection(java.lang.String, java.lang.Long)
	 */
	public GenericItem getItemForCollection(String name, Long irCollectionId)
	{
		Object[] values = new Object[] {name, irCollectionId};
		return (GenericItem) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getItemByNameCollection", 
				values));
	}
	
	/**
	 * Returns a list of contribution types.
	 * 
	 * @see edu.ur.ir.item.GenericItemDAO#getPossibleContributions(edu.ur.ir.person.Person)
	 */
	@SuppressWarnings("unchecked")
	public List<ContributorType> getPossibleContributions(Long personNameId, Long itemId)
	{
		Object[] values = new Object[] {personNameId, itemId};
		return (List<ContributorType>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPossibleContributions", values);
	}
	
	/**
	 * Get a count for the number of contributions made by a given name/contribution type
	 * 
	 * @param The name/contribution type we are looking for
	 * @return count of item contributions.
	 */
	public Long getItemContributionCount(Contributor contributor)
	{
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("itemContributionCount", contributor.getId()) );
	}
	
	/**
	 * Get the list of items owned by an user
	 * 	
	 * @param userId - User Id owning the items 
	 * @return list of Items
	 */
	@SuppressWarnings("unchecked")
	public List<GenericItem> getAllItemsForUser(Long userId)
	{
		return (List<GenericItem>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getAllItemsForUser", userId);
	}
		
	/**
	 * Get a list of identifier types that can be applied to this
	 * item.  Identifier types that have already been used will not
	 * be returned.
	 * 
	 * @param itemId the item to get possible identifier types for.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<IdentifierType> getPossibleIdentifierTypes(Long itemId)
	{
		Object[] values = new Object[] {itemId};
		return (List<IdentifierType>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getPossibleIdentifierTypes", values);
	}

	public GenericItem getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(GenericItem entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(GenericItem entity) {
		hbCrudDAO.makeTransient(entity);
	}

	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	public Long getContributionCountByPersonName(PersonName personName) {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("itemContributionByPersonNameCount", personName.getId()) );
	}

	
	/**
	 * Get the download count for the item.
	 * 
	 * @see edu.ur.ir.item.GenericItemDAO#getDownloadCount(java.lang.Long)
	 */
	public Long getDownloadCount(Long itemId) {
		Long value =    (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getItemDownloadCount", itemId));
		if( value != null )
		{
		    return value;
		}
		else
		{
		    return 0l;
		}
	}

	/**
	 * Get a count of the number of items containing the content types.
	 * 
	 * @see edu.ur.ir.item.GenericItemDAO#getContentTypeCount(edu.ur.ir.item.ContentType)
	 */
	public Long getContentTypeCount(ContentType contentType) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("itemContentTypeCount");
        q.setParameter("contentTypeId", contentType.getId());
        return((Integer)q.uniqueResult()).longValue();
  }

	/**
	 * Get a count of the number of items containg the contirbutor type
	 * @see edu.ur.ir.item.GenericItemDAO#getContributorTypeCount(edu.ur.ir.person.ContributorType)
	 */
	public Long getContributorTypeCount(ContributorType contributorType) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("itemContributorTypeCount");
        q.setParameter("contributorTypeId", contributorType.getId());
        return((Integer)q.uniqueResult()).longValue();
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.item.GenericItemDAO#getSecondaryContentTypeCount(edu.ur.ir.item.ContentType)
	 */
	public Long getSecondaryContentTypeCount(ContentType contentType) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("itemSecondaryContentTypeCount");
        q.setParameter("contentTypeId", contentType.getId());
        return((Integer)q.uniqueResult()).longValue();
	}


}
