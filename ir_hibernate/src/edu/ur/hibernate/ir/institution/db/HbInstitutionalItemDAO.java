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

package edu.ur.hibernate.ir.institution.db;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemDAO;
import edu.ur.ir.item.ContentTypeCount;
import edu.ur.order.OrderType;

/**
 * Implementation of relational storage for an institutional item.
 * 
 * @author Nathan Sarr
 *
 */
public class HbInstitutionalItemDAO implements InstitutionalItemDAO {
	
	/** eclipse generated id */
	private static final long serialVersionUID = -828719967486672639L;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(HbInstitutionalItemDAO.class);
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<InstitutionalItem> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbInstitutionalItemDAO() {
		hbCrudDAO = new HbCrudDAO<InstitutionalItem>(InstitutionalItem.class);
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
	 * Return all institutional items.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	
	/**
	 * Get an institutional item by it's id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public InstitutionalItem getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Add an institutional item to relational storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(InstitutionalItem entity) {
		hbCrudDAO.makePersistent(entity);
		
	}

	/**
	 * Remove an institutional item from relational storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(InstitutionalItem entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Find if the item version is already published to this collection.
	 * 
	 * @param institutional item id
	 * @param institutionalCollectionId Id of the institutional collection
	 * @param itemVersionId Id of the item version
	 * 
	 * @return True if item is published to the collection else false
	 */
	public boolean isItemPublishedToCollection(Long institutionalCollectionId, Long genericItemId)
	{
		if (getInstitutionalItem(institutionalCollectionId, genericItemId) != null) 
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Get an institutional item by collection id and generic item id.
	 * 
	 * @param collectionId - id of the collection 
	 * @param genericItemId - the generic item id.
	 * 
	 * @return the institutional item
	 */
	public InstitutionalItem getInstitutionalItem(Long collectionId, Long genericItemId)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("isItemPublishedToThisCollection");
		q.setParameter("collectionId",  collectionId);
		q.setParameter("itemId", genericItemId);
		return (InstitutionalItem)q.uniqueResult();
	}
	
	/**
	 * Get the institutional items for given collection Ids
	 * 
	 * @param itemIds - List of item ids
	 * 
	 * @return List of items ids
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getInstitutionalItems(final List<Long> itemIds) {
	
		List<InstitutionalItem> foundItems = new LinkedList<InstitutionalItem>();
			if( itemIds.size() > 0 )
	        {
				foundItems = (List<InstitutionalItem>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() {
			    public Object doInHibernate(Session session)
	                throws HibernateException, SQLException {
	                    Criteria criteria = session.createCriteria(hbCrudDAO.getClazz());
	                    criteria.add(Restrictions.in("id",itemIds));
	                return criteria.list();
	                }
	             });
	        }
			return foundItems;
	}
	


	
	
	/**
	 * Get a list of items for a specified repository by name.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the collection to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsByName(final int rowStart,
			final int maxResults, 
			final Long repositoryId, 
			final OrderType orderType) 
 {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();

		Query q = null;
		if (orderType != null && orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = session.getNamedQuery("getRepositoryItemsByNameOrderDesc");
		} else {
			q = session.getNamedQuery("getRepositoryItemsByNameOrderAsc");
		}
		q.setLong("repositoryId", repositoryId);
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setCacheable(false);
		q.setReadOnly(true);
		q.setFetchSize(maxResults);
		return q.list();

	}
	

	
	/**
	 * Get a list of items for a specified repository by first character of the name
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsByChar(final int rowStart,
			final int maxResults, 
			final Long repositoryId,
			final char firstChar,
			final OrderType orderType) 
 {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();

		Query q = null;
		if (orderType != null && orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = session.getNamedQuery("getRepositoryItemsByCharOrderDesc");
		} else {
			q = session.getNamedQuery("getRepositoryItemsByCharOrderAsc");
		}
		q.setLong("repositoryId", repositoryId);
		q.setCharacter("firstChar", Character.toLowerCase(firstChar));
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return q.list();
	}
	
	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsBetweenChar(final int rowStart,
			final int maxResults, 
			final Long repositoryId,
			final char firstChar,
			final char lastChar,
			final OrderType orderType)
 {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();

		Query q = null;
		if (orderType != null && orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = session.getNamedQuery("getRepositoryItemsByCharRangeOrderDesc");
		} else {
			q = session.getNamedQuery("getRepositoryItemsByCharRangeOrderAsc");
		}
		q.setLong("repositoryId", repositoryId);
		q.setCharacter("firstChar", Character.toLowerCase(firstChar));
		q.setCharacter("lastChar", Character.toLowerCase(lastChar));
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return q.list();

	}
	

	/** 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCollectionItemsByName(int, int, edu.ur.ir.institution.InstitutionalCollection, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsByName(final int rowStart,
			final int maxResults, final InstitutionalCollection collection,
			final OrderType orderType) {
		
		if(collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = null;
			if (orderType != null && orderType.equals(OrderType.DESCENDING_ORDER)) {
				q = session
						.getNamedQuery("getInstitutionalCollectionItemsByNameOrderDesc");
			} else {
				q = session
						.getNamedQuery("getInstitutionalCollectionItemsByNameOrderAsc");
			}

			q.setLong("leftVal", collection.getLeftValue());
			q.setLong("rightVal", collection.getRightValue());
			q.setLong("rootId", collection.getTreeRoot().getId());
			q.setFirstResult(rowStart);
			q.setMaxResults(maxResults);
			q.setFetchSize(maxResults);
			return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		

	}
	
	/**
	 * Return a count  of all items in the system.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("institutionalItemCount"));
	}
	
	/**
	 * Get a count of institutional items in the repository.
	 * 
	 * @param repositoryId - id of the repository
	 * @return
	 */
	public Long getCount(Long repositoryId)
	{
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("institutionalItemCountForRepository", repositoryId));
	}
	
	/**
	 * Get a count of institutional items in the repository with a name
	 * that starts with the specified first character.
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstChar - first character of the name
	 * 
	 * @return the count found
	 */
	public Long getCount(Long repositoryId, char nameFirstChar)
	{
		Object[] values = new Object[]{repositoryId, Character.valueOf(Character.toLowerCase(nameFirstChar))};
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("institutionalItemCountForRepositoryByChar", values));
	}
	
	/**
	 * Get a count of institutional items in the repository with a name
	 * that starts with the specified first character in the given range.
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstCharRange - first character of the name start of range
	 * @param nameLastCharRange- first character of the name end of range
	 * 
	 * @return the count found
	 */
	public Long getCount(Long repositoryId, char nameFirstCharRange, char namelastCharRange)
	{
		Object[] values = new Object[]{repositoryId, 
				Character.valueOf(Character.toLowerCase(nameFirstCharRange)),
				Character.valueOf(Character.toLowerCase(namelastCharRange))};
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("institutionalItemCountForRepositoryByCharRange", values));
	}
	
	/**
	 * Get a count of institutional items in a collection and its children.
	 * 
	 * @param collectionId - id of the collection
	 * @return
	 */
	public Long getCountForCollectionAndChildren(InstitutionalCollection collection)
	{
		Long[] ids = new Long[] {collection.getLeftValue(), collection.getRightValue(), 
				collection.getTreeRoot().getId()};

		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("institutionalItemCountForCollectionAndchildren", ids));
	}

	/**
	 * Get a count of institutional items in a collection.
	 * 
	 * @param collectionId - id of the collection
	 * @return
	 */
	public Long getCount(InstitutionalCollection collection)
	{
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("institutionalItemCountForCollection", collection.getId()));
	}

	
	public Long getCountByGenericItem(Long genericItemId) {
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("institutionalItemCountForGenericItem", genericItemId));
	}

	/**
	 * Get all institutional items that contain any of the generic item ids in the given list.
	 * 
	 * @param genericItemIds - list of generic item ids
	 * @return list of institutional items found.
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getInstitutionalItemsByGenericItemIds(List<Long> genericItemIds) {
	  //getInstitutionalItemsForGenericItemIds
	  Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
	  Query q = session.getNamedQuery("getInstitutionalItemsForGenericItemIds");
	  q.setParameterList("itemIds", genericItemIds);
	  return q.list();
	}
	/**
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCollectionItemsBetweenChar(int, int, edu.ur.ir.institution.InstitutionalCollection, char, char, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsBetweenChar(
			final int rowStart, final int maxResults,
			final InstitutionalCollection collection, final char firstChar,
			final char lastChar, final OrderType orderType) {

		Query q = null;
		if(collection != null ) {
			if (orderType != null && orderType.equals(OrderType.DESCENDING_ORDER)) {
				q = hbCrudDAO.getSessionFactory().getCurrentSession()
						.getNamedQuery("getCollectionItemsByCharRangeOrderDesc");
			} else {
				q = hbCrudDAO.getSessionFactory().getCurrentSession()
						.getNamedQuery("getCollectionItemsByCharRangeOrderAsc");
			}

			q.setLong("leftVal", collection.getLeftValue());
			q.setLong("rightVal", collection.getRightValue());
			q.setLong("rootId", collection.getTreeRoot().getId());
			q.setCharacter("firstChar", Character.toLowerCase(firstChar));
			q.setCharacter("lastChar", Character.toLowerCase(lastChar));
			q.setFirstResult(rowStart);
			q.setMaxResults(maxResults);
			q.setFetchSize(maxResults);
			return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		

	}

	/**
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCollectionItemsByChar(int, int, edu.ur.ir.institution.InstitutionalCollection, char, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsByChar(final int rowStart,
			final int maxResults, final InstitutionalCollection collection,
			final char firstChar, final OrderType orderType) {

		if( collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = null;
			if (orderType != null && orderType.equals(OrderType.DESCENDING_ORDER)) {
				q = session
						.getNamedQuery("getInstitutionalCollectionItemsByCharOrderDesc");
			} else {
				q = session
						.getNamedQuery("getInstitutionalCollectionItemsByCharOrderAsc");
			}

			q.setLong("leftVal", collection.getLeftValue());
			q.setLong("rightVal", collection.getRightValue());
			q.setLong("rootId", collection.getTreeRoot().getId());
			q.setCharacter("firstChar", Character.toLowerCase(firstChar));
			q.setFirstResult(rowStart);
			q.setMaxResults(maxResults);
			q.setFetchSize(maxResults);
			return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCount(edu.ur.ir.institution.InstitutionalCollection, char)
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstChar) {
		if( collection != null ) {
			Object[] values = new Object[]{
					collection.getLeftValue(),
					collection.getRightValue(),
					collection.getTreeRoot().getId(),
					Character.valueOf(Character.toLowerCase(nameFirstChar))};
			return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("institutionalItemCountForCollectionByChar", values));
		} else {
			return 0l;
		}
		

	}

	
	/**
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCount(edu.ur.ir.institution.InstitutionalCollection, char, char)
	 */
	public Long getCount(InstitutionalCollection collection,
			char nameFirstCharRange, char namelastCharRange) {
		if( collection != null ) {
			Object[] values = new Object[]{
					collection.getLeftValue(),
					collection.getRightValue(),
					collection.getTreeRoot().getId(),
					Character.valueOf(Character.toLowerCase(nameFirstCharRange)),
					Character.valueOf(Character.toLowerCase(namelastCharRange))};
			return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("institutionalItemCountForCollectionByCharRange", values));
		} else {
			return 0l;
		}
		
	}

	/**
	 * Get a count of distinct institutional items in the repository.
	 * 
	 * @return
	 */
	public Long getDistinctInstitutionalItemCount()
	{
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("distinctInstitutionalItemCount"));
	}

	/**
	 * Get a institutional collections  the generic item exists in
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalCollection> getInstitutionalCollectionsForGenericItem(Long itemId)
	{
		return (List<InstitutionalCollection>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getCollectionsForGenericItem", itemId);
	}


	/**
	 * get the publication count  for given name id.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getPublicationCountByPersonName(List)
	 */
	public Long getPublicationCountByPersonName(List<Long> personNameIds) {
		
	    Query q = hbCrudDAO.getHibernateTemplate().getSessionFactory().getCurrentSession().getNamedQuery("getPublicationCountByPersonNameId");
		
	    q.setParameterList("personNameIds", personNameIds);
	
	    Long count = (Long)q.uniqueResult();
	    return count;

	}

	/**
	 * Get Institutional item by given version id
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getInstitutionalItemByVersionId(Long)
	 */
	public InstitutionalItem getInstitutionalItemByVersionId(Long institutionalVerisonId) {
		return (InstitutionalItem) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("getInstitutionalItemByVersionId", institutionalVerisonId));
	}

	/**
	 * Get a institutional items where the generic item is the latest version
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getInstitutionalItemsForGenericItemId(Long itemId)
	{
		return (List<InstitutionalItem>) hbCrudDAO.getHibernateTemplate().findByNamedQuery("getInstitutionalItemsForGenericItemId", itemId);
	}

	
	/**
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getItems(int, int, edu.ur.ir.institution.InstitutionalCollection, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getItemsOrderByDate(final int rowStart, final int maxResults,
			final InstitutionalCollection collection, final OrderType orderType) {
		
		if( collection != null ) {
			 List<InstitutionalItem> foundItems = (List<InstitutionalItem>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
				{
				    public Object doInHibernate(Session session) throws HibernateException, SQLException 
				    {
				        Query q = null;
				        
					    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
					    {
					        q = session.getNamedQuery("getInstitutionalCollectionItemsByAcceptedDateDesc");
					    }
				 	    else
					    {
					        q = session.getNamedQuery("getInstitutionalCollectionItemsByAcceptedDateAsc");
					    }
					   
					    q.setLong(0, collection.getId());
					    
					    q.setFirstResult(rowStart);
					    q.setMaxResults(maxResults);
					    q.setFetchSize(maxResults);
			            return q.list();
				    }
			    });
		        return foundItems;	
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getItems(edu.ur.ir.institution.InstitutionalCollection, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getItems(final InstitutionalCollection collection,
			final Date startDate, final Date endDate) {
		
		if(collection != null && startDate != null && endDate != null ) {
			log.debug("Trying dates " + startDate + " and " + endDate);

			 List<InstitutionalItem> foundItems = (List<InstitutionalItem>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
			{
			    public Object doInHibernate(Session session) throws HibernateException, SQLException 
			    {
			        Query q = null;
				  
				    q = session.getNamedQuery("getInstitutionalCollectionItemsByAcceptedDateRange");
				   
				    q.setLong(0, collection.getId());
				    q.setTimestamp(1, startDate);
				    q.setTimestamp(2, endDate);
		            return q.list();
			    }
		    });
	        return foundItems;	
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
	}

	
	/**
	 * Get the list of collection items by id.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCollectionItemsById(int, int, edu.ur.ir.institution.InstitutionalCollection, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getCollectionItemsIds(final int rowStart, final int maxResults,
			final InstitutionalCollection collection, final OrderType orderType) {
		
		if( collection != null ) {
			 List<Long> foundIds = (List<Long>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
				{
				    public Object doInHibernate(Session session) throws HibernateException, SQLException 
				    {
				        Query q = null;
					    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
					    {
					        q = session.getNamedQuery("getInstitutionalCollectionItemIdsOrderDesc");
					    }
				 	    else
					    {
					        q = session.getNamedQuery("getInstitutionalCollectionItemIdsOrderAsc");
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
			
		    return foundIds;	
		} else {
			return new LinkedList<Long>();
		}
		
	}

	
	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - the institutional collection 
	 * @param contentTypeId - content type id the items must have
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return list of items matching the specified criteria
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCollectionItemsBetweenChar(int, int, edu.ur.ir.institution.InstitutionalCollection, java.lang.Long, char, char, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsBetweenChar(int rowStart,
			int maxResults, InstitutionalCollection collection,
			Long contentTypeId, char firstChar, char lastChar,
			OrderType orderType) {
		
		if( collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = null;
		    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
		    {
		        q = session.getNamedQuery("getCollectionItemsContentTypeByCharRangeOrderDesc");
		    }
	 	    else
		    {
		        q = session.getNamedQuery("getCollectionItemsContentTypeByCharRangeOrderAsc");
		    }
		    
			q.setParameter("leftVal", collection.getLeftValue());
			q.setParameter("rightVal", collection.getRightValue());
			q.setParameter("rootId", collection.getTreeRoot().getId());
			q.setParameter("firstChar", Character.valueOf(Character.toLowerCase(firstChar)));
			q.setParameter("lastChar", Character.valueOf(Character.toLowerCase(lastChar)));
			q.setParameter("contentTypeId", contentTypeId);
			q.setFirstResult(rowStart);
		    q.setMaxResults(maxResults);
		    q.setFetchSize(maxResults);
	        return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
		
	}

	
	/**
	 * Get a list of items for a specified collection by first character of the name
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param institutional collection - the institutional collection 
	 * @param contentTypeId - id of the content type 
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCollectionItemsByChar(int, int, edu.ur.ir.institution.InstitutionalCollection, java.lang.Long, char, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsByChar(int rowStart,
			int maxResults, InstitutionalCollection collection,
			Long contentTypeId, char firstChar, OrderType orderType) {
		
		if(collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = null;
		    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
		    {
		        q = session.getNamedQuery("getInstitutionalCollectionItemsContentTypeByCharOrderDesc");
		    }
	 	    else
		    {
		        q = session.getNamedQuery("getInstitutionalCollectionItemsContentTypeByCharOrderAsc");
		    }
		    
			q.setParameter("leftVal", collection.getLeftValue());
			q.setParameter("rightVal", collection.getRightValue());
			q.setParameter("rootId", collection.getTreeRoot().getId());
			q.setParameter("firstChar", Character.valueOf(Character.toLowerCase(firstChar)));
			q.setParameter("contentTypeId", contentTypeId);
			q.setFirstResult(rowStart);
		    q.setMaxResults(maxResults);
		    q.setFetchSize(maxResults);
	        return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
	}

	/**
	 * Get the list of items for the specified collection with the given 
	 * content type id.  This includes items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults -  maximum number of results to return
	 * @param collection - the collection to get items 
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCollectionItemsByName(int, int, edu.ur.ir.institution.InstitutionalCollection, java.lang.Long, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsByName(int rowStart,
			int maxResults, InstitutionalCollection collection,
			Long contentTypeId, OrderType orderType) {
		if( collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = null;
		    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
		    {
		        q = session.getNamedQuery("getInstitutionalCollectionItemsContentTypeByNameOrderDesc");
		    }
	 	    else
		    {
		        q = session.getNamedQuery("getInstitutionalCollectionItemsContentTypeByNameOrderAsc");
		    }
		    
			q.setParameter("leftVal", collection.getLeftValue());
			q.setParameter("rightVal", collection.getRightValue());
			q.setParameter("rootId", collection.getTreeRoot().getId());
			q.setParameter("contentTypeId", contentTypeId);
			q.setFirstResult(rowStart);
		    q.setMaxResults(maxResults);
		    q.setFetchSize(maxResults);
	        return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
	}

	/**
	 * Get a  count of institutional items with the given content type id.
	 * 
	 * @param repositoryId - id of the repository
	 * @param contentTypeId - content type
	 * 
	 * @return the count of institutional items with the content type in the specified
	 * repository.
	 */
	public Long getCount(Long repositoryId, Long contentTypeId) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = session.getNamedQuery("getRepositoryItemsContentTypeCount");
		q.setParameter("repositoryId", repositoryId);
		q.setParameter("contentTypeId", contentTypeId);
		return (Long)q.uniqueResult();
	}

	/**
	 * Get a count of institutional items with the specified repository id
	 * has a name starting with the first character and the specified content type id.
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstChar - name of the first character
	 * @param contentTypeId - specified content type id
	 * 
	 * @return the count of items with the specified criteria
	 */
	public Long getCount(Long repositoryId, char firstChar,
			Long contentTypeId) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = session.getNamedQuery("institutionalItemCountContentTypeForRepositoryByChar");
		q.setParameter("repositoryId", repositoryId);
		q.setParameter("contentTypeId", contentTypeId);
		q.setParameter("char", Character.valueOf(Character.toLowerCase(firstChar)));
		return (Long)q.uniqueResult();
	}

	
	/**
	 * Get a count of all items within the specified repository with a name
	 * first character starting between the given character range and the content type id.
	 * 
	 * @param repositoryId - id of the repository
	 * @param nameFirstCharRange - starting character range
	 * @param namelastCharRange - ending character range
	 * @param contentTypeId - id of the content type
	 * 
	 * @return the count of items 
	 */
	public Long getCount(Long repositoryId, char nameFirstCharRange,
			char namelastCharRange, Long contentTypeId) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = session.getNamedQuery("institutionalItemCountForRepositoryContentTypeByCharRange");
		q.setParameter("repositoryId", repositoryId);
		q.setParameter("contentTypeId", contentTypeId);
		q.setParameter("firstChar", Character.valueOf(Character.toLowerCase(nameFirstCharRange)));
		q.setParameter("lastChar", Character.valueOf(Character.toLowerCase(namelastCharRange)));
		return (Long)q.uniqueResult();
	}

	/**
	 * Get a count of all items within the specified collection, has the specified first character 
	 * and a given content type id.  This includes a count of items within sub collections.
	 * 
	 * @param collection - collection items must be within sub collections
	 * @param nameFirstChar - name starts with the specified first character
	 * @param contentTypeId - id of the content type
	 * 
	 * @return count of items 
	 */
	public Long getCount(InstitutionalCollection collection,
			char nameFirstChar, Long contentTypeId) {
		if( collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = session.getNamedQuery("institutionalItemCountForCollectionContentTypeByChar");
			q.setParameter("leftVal", collection.getLeftValue());
			q.setParameter("rightVal", collection.getRightValue());
			q.setParameter("contentTypeId", contentTypeId);
			q.setParameter("rootId", collection.getTreeRoot().getId());
			q.setParameter("firstChar", Character.valueOf(Character.toLowerCase(nameFirstChar)));
			return (Long)q.uniqueResult();
		} else {
			return 0l;
		}
		
	}
	
	/**
	 Get a count of all institutional items in the specified collection with
	 * specified first character in the given character range with the given content type id-  THIS INCLUDES items in child collections 
	 * 
	 * @param institutional collection - parent collection
	 * @param nameFirstCharRange - first character in range
	 * @param nameLastCharRange - last character in the range
	 * 
	 * @return count of titles found that have a first character in the specified range
	 */
	public Long getCount(InstitutionalCollection collection,
			char nameFirstCharRange, char nameLastCharRange, Long contentTypeId)
	{
		if(collection != null) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = session.getNamedQuery("institutionalItemCountForCollectionContentTypeByCharRange");
			q.setParameter("leftVal", collection.getLeftValue());
			q.setParameter("rightVal", collection.getRightValue());
			q.setParameter("contentTypeId", contentTypeId);
			q.setParameter("rootId", collection.getTreeRoot().getId());
			q.setParameter("firstChar", Character.valueOf(Character.toLowerCase(nameFirstCharRange)));
			q.setParameter("lastChar", Character.valueOf(Character.toLowerCase(nameLastCharRange)));
			return (Long)q.uniqueResult();
		} else {
			return 0l;
		}
		
	}

	/**
	 * Get a count of institutional items in a collection and its children with
	 * the specified content type.
	 * 
	 * @param collection - collection to start counting from
	 * @param contentTypeId - id of the content type 
	 * 
	 * @return Items within the specified collection and its sub collection
	 */
	public Long getCount(InstitutionalCollection collection, Long contentTypeId) {
		if( collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = session.getNamedQuery("getInstitutionalCollectionItemsContentTypeCount");
			q.setParameter("leftVal", collection.getLeftValue());
			q.setParameter("rightVal", collection.getRightValue());
			q.setParameter("contentTypeId", contentTypeId);
			q.setParameter("rootId", collection.getTreeRoot().getId());
			return (Long)q.uniqueResult();
		} else {
			return 0l;
		}
		
	}

	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters with the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getRepositoryItemsBetweenChar(int, int, java.lang.Long, char, char, java.lang.Long, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsBetweenChar(int rowStart,
			int maxResults, Long repositoryId, char firstChar, char lastChar,
			Long contentTypeId, OrderType orderType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = null;
	    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByCharRangeOrderDesc");
	    }
 	    else
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByCharRangeOrderAsc");
	    }
	    
	    q.setParameter("repositoryId", repositoryId);
		q.setParameter("firstChar",Character.valueOf(Character.toLowerCase(firstChar)));
		q.setParameter("lastChar", Character.valueOf(Character.toLowerCase(lastChar)));
		q.setParameter("contentTypeId", contentTypeId);
		q.setFirstResult(rowStart);
	    q.setMaxResults(maxResults);
	    q.setFetchSize(maxResults);
        return q.list();
	}

	/**
	 * Get a list of items for a specified repository by first character of the name and
	 * the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsByChar(int rowStart,
			int maxResults, Long repositoryId, Long contentTypeId,
			char firstChar, OrderType orderType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = null;
	    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByCharOrderDesc");
	    }
 	    else
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByCharOrderAsc");
	    }
	    
	    q.setParameter("repositoryId", repositoryId);
		q.setParameter("char", Character.valueOf(Character.toLowerCase(firstChar)));
		q.setParameter("contentTypeId", contentTypeId);
		q.setFirstResult(rowStart);
	    q.setMaxResults(maxResults);
	    q.setFetchSize(maxResults);
        return q.list();
	}

	/**
	 * Get a list of items for a specified repository with the given content type id.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param rowEnd -  End row to get data
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param propertyName - The property to sort on
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getRepositoryItemsOrderByName(int, int, java.lang.Long, java.lang.Long, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsOrderByName(int rowStart,
			int maxResults, Long repositoryId, Long contentTypeId,
			OrderType orderType) {
		
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = null;
	    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByNameOrderDesc");
	    }
 	    else
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByNameOrderAsc");
	    }
	    
	    q.setParameter("repositoryId", repositoryId);
		q.setParameter("contentTypeId", contentTypeId);
		q.setFirstResult(rowStart);
	    q.setMaxResults(maxResults);
	    q.setFetchSize(maxResults);
        return q.list();
		
	}

	/**
	 * Get a list of of repository content types with counts for the number
	 * of items within the repository.  This will return only those
	 * items that have a count greater than 0.
	 * 
	 * @return - list of content type counts
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getRepositoryContentTypeCount()
	 */
	@SuppressWarnings("unchecked")
	public List<ContentTypeCount> getRepositoryContentTypeCount(Long repositoryId) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = session.getNamedQuery("getRepositoryItemsSumByContentType");
		q.setParameter("repositoryId", repositoryId);
        return q.list();
	}
	
	/**
	 * Get a list of of repository content types with counts for the number
	 * of items within the repository. 
	 * 
	 * @param collection - institutional collection
	 * @return - list of content type counts
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemService#getRepositoryContentTypeCount()
	 */
	@SuppressWarnings("unchecked")
	public List<ContentTypeCount> getCollectionContentTypeCount(InstitutionalCollection collection)
	{
		if( collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = session.getNamedQuery("getCollectionItemsSumByContentType");
			q.setParameter("leftVal", collection.getLeftValue());
			q.setParameter("rightVal", collection.getRightValue());
			q.setParameter("rootId", collection.getTreeRoot().getId());
			return q.list();
		} else {
			return new LinkedList<ContentTypeCount>();
		}
		
	}

	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - the institutional collection 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return list of items matching the specified criteria
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCollectionItemsBetweenChar(int, int, edu.ur.ir.institution.InstitutionalCollection, java.lang.Long, char, char, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsBetweenCharPublicationDateOrder(
			int rowStart, int maxResults, InstitutionalCollection collection,
			char firstChar, char lastChar, OrderType orderType) {
		
		if( collection != null) {
			Query q = null;
			if (orderType != null && orderType.equals(OrderType.DESCENDING_ORDER)) {
				q = hbCrudDAO.getSessionFactory().getCurrentSession()
						.getNamedQuery("getInstitutionalCollectionItemsByCharRangePublicationDateOrderDesc");
			} else {
				q = hbCrudDAO.getSessionFactory().getCurrentSession()
						.getNamedQuery("getInstitutionalCollectionItemsByCharRangePublicationDateOrderAsc");
			}

			q.setLong("leftVal", collection.getLeftValue());
			q.setLong("rightVal", collection.getRightValue());
			q.setLong("rootId", collection.getTreeRoot().getId());
			q.setCharacter("firstChar", Character.toLowerCase(firstChar));
			q.setCharacter("lastChar", Character.toLowerCase(lastChar));
			q.setFirstResult(rowStart);
			q.setMaxResults(maxResults);
			q.setFetchSize(maxResults);
			return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
	}

	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - the institutional collection 
	 * @param contentTypeId - content type id the items must have
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return list of items matching the specified criteria
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCollectionItemsBetweenChar(int, int, edu.ur.ir.institution.InstitutionalCollection, java.lang.Long, char, char, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsBetweenCharPublicationDateOrder(
			int rowStart, int maxResults, InstitutionalCollection collection,
			Long contentTypeId, char firstChar, char lastChar,
			OrderType orderType) {
		
		if( collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = null;
		    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
		    {
		        q = session.getNamedQuery("getCollectionItemsContentTypeByCharRangePublicationDateOrderDesc");
		    }
	 	    else
		    {
		        q = session.getNamedQuery("getCollectionItemsContentTypeByCharRangePublicationDateOrderAsc");
		    }
		    
			q.setParameter("leftVal", collection.getLeftValue());
			q.setParameter("rightVal", collection.getRightValue());
			q.setParameter("rootId", collection.getTreeRoot().getId());
			q.setParameter("firstChar", Character.valueOf(Character.toLowerCase(firstChar)));
			q.setParameter("lastChar", Character.valueOf(Character.toLowerCase(lastChar)));
			q.setParameter("contentTypeId", contentTypeId);
			q.setFirstResult(rowStart);
		    q.setMaxResults(maxResults);
		    q.setFetchSize(maxResults);
	        return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
	}

	/**
	 * Get a list of items for a specified collection by first character of the name
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param institutional collection - the institutional collection 
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsByCharPublicationDateOrder(
			int rowStart, int maxResults,
			InstitutionalCollection collection, char firstChar,
			OrderType orderType) {
		
		if( collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();

			Query q = null;
			if (orderType != null && orderType.equals(OrderType.DESCENDING_ORDER)) {
				q = session
						.getNamedQuery("getInstitutionalCollectionItemsByCharPublicationDateOrderDesc");
			} else {
				q = session
						.getNamedQuery("getInstitutionalCollectionItemsByCharPublicationDateOrderAsc");
			}

			q.setLong("leftVal", collection.getLeftValue());
			q.setLong("rightVal", collection.getRightValue());
			q.setLong("rootId", collection.getTreeRoot().getId());
			q.setCharacter("firstChar", Character.toLowerCase(firstChar));
			q.setFirstResult(rowStart);
			q.setMaxResults(maxResults);
			q.setFetchSize(maxResults);
			return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
	}

	/**
	 * Get a list of items for a specified collection by first character of the name
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param institutional collection - the institutional collection 
	 * @param contentTypeId - id of the content type 
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsByCharPublicationDateOrder(
			int rowStart, int maxResults, InstitutionalCollection collection,
			Long contentTypeId, char firstChar, OrderType orderType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = null;
		
		if( collection != null ) {
			if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
		    {
		        q = session.getNamedQuery("getInstitutionalCollectionItemsContentTypeByCharPublicationDateOrderDesc");
		    }
	 	    else
		    {
		        q = session.getNamedQuery("getInstitutionalCollectionItemsContentTypeByCharPublicationDateOrderAsc");
		    }
		    
			q.setParameter("leftVal", collection.getLeftValue());
			q.setParameter("rightVal", collection.getRightValue());
			q.setParameter("rootId", collection.getTreeRoot().getId());
			q.setParameter("firstChar", Character.valueOf(Character.toLowerCase(firstChar)));
			q.setParameter("contentTypeId", contentTypeId);
			q.setFirstResult(rowStart);
		    q.setMaxResults(maxResults);
		    q.setFetchSize(maxResults);
	        return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
	    
	}

	/**
	 * Get the list of items for the specified collection.  
	 * This includes items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults -  maximum number of results to return
	 * @param collection - the collection to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsPublicationDateOrder(
			int rowStart, int maxResults, InstitutionalCollection collection,
			OrderType orderType) {
		
		if(collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = null;
			if (orderType != null && orderType.equals(OrderType.DESCENDING_ORDER)) {
				q = session
						.getNamedQuery("getInstitutionalCollectionItemsPublicationDateOrderDesc");
			} else {
				q = session
						.getNamedQuery("getInstitutionalCollectionItemsPublicationDateOrderAsc");
			}

			q.setLong("leftVal", collection.getLeftValue());
			q.setLong("rightVal", collection.getRightValue());
			q.setLong("rootId", collection.getTreeRoot().getId());
			q.setFirstResult(rowStart);
			q.setMaxResults(maxResults);
			q.setFetchSize(maxResults);
			return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
	}

	/**
	 * Get the list of items for the specified collection with the given 
	 * content type id.  This includes items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults -  maximum number of results to return
	 * @param collection - the collection to get items 
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsPublicationDateOrder(
			int rowStart, int maxResults, InstitutionalCollection collection,
			Long contentTypeId, OrderType orderType) {
		if( collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = null;
			if (orderType != null && orderType.equals(OrderType.DESCENDING_ORDER)) {
				q = session
						.getNamedQuery("getInstitutionalCollectionItemsContentTypeByPublicationDateOrderDesc");
			} else {
				q = session
						.getNamedQuery("getInstitutionalCollectionItemsContentTypeByPublicationDateOrderAsc");
			}

			q.setLong("leftVal", collection.getLeftValue());
			q.setLong("rightVal", collection.getRightValue());
			q.setLong("rootId", collection.getTreeRoot().getId());
			q.setLong("contentTypeId", contentTypeId);
			q.setFirstResult(rowStart);
			q.setMaxResults(maxResults);
			q.setFetchSize(maxResults);
			return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
	}

	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsBetweenCharPublicationDateOrder(
			int rowStart, int maxResults, Long repositoryId, char firstChar,
			char lastChar, OrderType orderType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();

		Query q = null;
		if (orderType != null && orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = session.getNamedQuery("getRepositoryItemsByCharRangePublicationDateOrderDesc");
		} else {
			q = session.getNamedQuery("getRepositoryItemsByCharRangePublicationDateOrderAsc");
		}
		q.setLong("repositoryId", repositoryId);
		q.setCharacter("firstChar", Character.toLowerCase(firstChar));
		q.setCharacter("lastChar", Character.toLowerCase(lastChar));
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return q.list();

	}

	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters with the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getRepositoryItemsBetweenChar(int, int, java.lang.Long, char, char, java.lang.Long, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsBetweenCharPublicationDateOrder(
			int rowStart, int maxResults, Long repositoryId, char firstChar,
			char lastChar, Long contentTypeId, OrderType orderType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = null;
	    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByCharRangeOrderDesc");
	    }
 	    else
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByCharRangeOrderAsc");
	    }
	    
	    q.setParameter("repositoryId", repositoryId);
		q.setParameter("firstChar",Character.valueOf(Character.toLowerCase(firstChar)));
		q.setParameter("lastChar", Character.valueOf(Character.toLowerCase(lastChar)));
		q.setParameter("contentTypeId", contentTypeId);
		q.setFirstResult(rowStart);
	    q.setMaxResults(maxResults);
	    q.setFetchSize(maxResults);
        return q.list();
	}

	/**
	 * Get a list of items for a specified repository by first character of the name and
	 * the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsByCharPublicationDateOrder(
			int rowStart, int maxResults, Long repositoryId,
			Long contentTypeId, char firstChar, OrderType orderType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = null;
	    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByCharPublicationDateOrderDesc");
	    }
 	    else
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByCharPublicationDateOrderAsc");
	    }
	    
	    q.setParameter("repositoryId", repositoryId);
		q.setParameter("char", Character.valueOf(Character.toLowerCase(firstChar)));
		q.setParameter("contentTypeId", contentTypeId);
		q.setFirstResult(rowStart);
	    q.setMaxResults(maxResults);
	    q.setFetchSize(maxResults);
        return q.list();
	}

	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters 
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsByCharPublicationDateOrder(
			int rowStart, int maxResults, Long repositoryId, char firstChar,
			OrderType orderType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();

		Query q = null;
		if (orderType != null && orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = session.getNamedQuery("getRepositoryItemsByCharPublicationDateOrderDesc");
		} else {
			q = session.getNamedQuery("getRepositoryItemsByCharPublicationDateOrderAsc");
		}
		q.setLong("repositoryId", repositoryId);
		q.setCharacter("firstChar", Character.toLowerCase(firstChar));
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return q.list();
	}

	/**
	 * Get a list of items for a specified repository by publication date.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the collection to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsPublicationDateOrder(
			int rowStart, int maxResults, Long repositoryId,
			 OrderType orderType) {
		Query q = null;
		if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
	    {
	        q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getRepositoryItemsByPublicationDateOrderDesc");
	    }
 	    else
	    {
	        q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getRepositoryItemsByPublicationDateOrderAsc");
	    }
	    q.setLong("repositoryId", repositoryId);
	    q.setFirstResult(rowStart);
	    q.setMaxResults(maxResults);
	    q.setCacheable(false);
	    q.setReadOnly(true);
	    q.setFetchSize(maxResults);
        return (List<InstitutionalItem>)q.list();
	}
	
	/**
	 * Get a list of items for a specified repository by publication date.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the collection to get items 
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsPublicationDateOrder(
			int rowStart, int maxResults, Long repositoryId,
			Long contentTypeId, OrderType orderType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = null;
	    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByPublicationDateOrderDesc");
	    }
 	    else
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByPublicationDateOrderAsc");
	    }
	    
	    q.setParameter("repositoryId", repositoryId);
		q.setParameter("contentTypeId", contentTypeId);
		q.setFirstResult(rowStart);
	    q.setMaxResults(maxResults);
	    q.setFetchSize(maxResults);
        return q.list();
	}

	/**
	 * Get the list of items for the specified collection with the given 
	 * content type id.  This includes items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults -  maximum number of results to return
	 * @param collection - the collection to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsFirstAvailableOrder(
			int rowStart, int maxResults, InstitutionalCollection collection,
			OrderType orderType) {
		
		if(collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = null;
			if (orderType != null && orderType.equals(OrderType.DESCENDING_ORDER)) {
				q = session
						.getNamedQuery("getInstitutionalCollectionItemsFirstAvailableOrderDesc");
			} else {
				q = session
						.getNamedQuery("getInstitutionalCollectionItemsFirstAvailableOrderAsc");
			}

			q.setLong("leftVal", collection.getLeftValue());
			q.setLong("rightVal", collection.getRightValue());
			q.setLong("rootId", collection.getTreeRoot().getId());
			q.setFirstResult(rowStart);
			q.setMaxResults(maxResults);
			q.setFetchSize(maxResults);
			return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
		
	}

	
	/**
	 * Get the list of items for the specified collection with the given 
	 * content type id.  This includes items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults -  maximum number of results to return
	 * @param collection - the collection to get items 
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsFirstAvailableOrder(
			int rowStart, int maxResults, InstitutionalCollection collection,
			Long contentTypeId, OrderType orderType) {
		
		if(collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = null;
			if (orderType != null && orderType.equals(OrderType.DESCENDING_ORDER)) {
				q = session
						.getNamedQuery("getInstitutionalCollectionItemsContentTypeByFirstAvailableOrderDesc");
			} else {
				q = session
						.getNamedQuery("getInstitutionalCollectionItemsContentTypeByFirstAvailableOrderAsc");
			}

			q.setLong("leftVal", collection.getLeftValue());
			q.setLong("rightVal", collection.getRightValue());
			q.setLong("rootId", collection.getTreeRoot().getId());
			q.setLong("contentTypeId", contentTypeId);
			q.setFirstResult(rowStart);
			q.setMaxResults(maxResults);
			q.setFetchSize(maxResults);
			return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
		
	}

	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - the institutional collection 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return list of items matching the specified criteria
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCollectionItemsBetweenChar(int, int, edu.ur.ir.institution.InstitutionalCollection, java.lang.Long, char, char, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsBetweenCharFirstAvailableOrder(
			int rowStart, int maxResults, InstitutionalCollection collection,
			char firstChar, char lastChar, OrderType orderType) {
		
		if( collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = null;
		    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
		    {
		        q = session.getNamedQuery("getInstitutionalCollectionItemsByCharRangeFirstAvailableOrderDesc");
		    }
	 	    else
		    {
		        q = session.getNamedQuery("getInstitutionalCollectionItemsByCharRangeFirstAvailableOrderAsc");
		    }
		    
			q.setParameter("leftVal", collection.getLeftValue());
			q.setParameter("rightVal", collection.getRightValue());
			q.setParameter("rootId", collection.getTreeRoot().getId());
			q.setParameter("firstChar", Character.valueOf(Character.toLowerCase(firstChar)));
			q.setParameter("lastChar", Character.valueOf(Character.toLowerCase(lastChar)));
			q.setFirstResult(rowStart);
		    q.setMaxResults(maxResults);
		    q.setFetchSize(maxResults);
	        return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
	}

	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param collection - the institutional collection 
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return list of items matching the specified criteria
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCollectionItemsBetweenChar(int, int, edu.ur.ir.institution.InstitutionalCollection, java.lang.Long, char, char, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsBetweenCharFirstAvailableOrder(
			int rowStart, int maxResults, InstitutionalCollection collection,
			Long contentTypeId, char firstChar, char lastChar,
			OrderType orderType) {
		
		if( collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = null;
		    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
		    {
		        q = session.getNamedQuery("getInstitutionalCollectionItemsContentTypeByCharRangeFirstAvailableOrderDesc");
		    }
	 	    else
		    {
		        q = session.getNamedQuery("getInstitutionalCollectionItemsContentTypeByCharRangeFirstAvailableOrderAsc");
		    }
		    
			q.setParameter("leftVal", collection.getLeftValue());
			q.setParameter("rightVal", collection.getRightValue());
			q.setParameter("rootId", collection.getTreeRoot().getId());
			q.setParameter("firstChar", Character.valueOf(Character.toLowerCase(firstChar)));
			q.setParameter("lastChar", Character.valueOf(Character.toLowerCase(lastChar)));
			q.setParameter("contentTypeId", contentTypeId);
			q.setFirstResult(rowStart);
		    q.setMaxResults(maxResults);
		    q.setFetchSize(maxResults);
	        return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
		
	}

	/**
	 * Get a list of items for a specified collection by first character of the name
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param institutional collection - the institutional collection 
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsByCharFirstAvailableOrder(
			int rowStart, int maxResults,
			InstitutionalCollection collection, char firstChar,
			OrderType orderType) {
		
		if(collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = null;
		    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
		    {
		        q = session.getNamedQuery("getInstitutionalCollectionItemsByCharFirstAvailableOrderDesc");
		    }
	 	    else
		    {
		        q = session.getNamedQuery("getInstitutionalCollectionItemsByCharFirstAvailableOrderAsc");
		    }
		    
			q.setParameter("leftVal", collection.getLeftValue());
			q.setParameter("rightVal", collection.getRightValue());
			q.setParameter("rootId", collection.getTreeRoot().getId());
			q.setParameter("firstChar", Character.valueOf(Character.toLowerCase(firstChar)));
			q.setFirstResult(rowStart);
		    q.setMaxResults(maxResults);
		    q.setFetchSize(maxResults);
	        return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
	}

	/**
	 * Get a list of items for a specified collection by first character of the name
	 * 
	 * NOTE: This search includes all items in child collections
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param institutional collection - the institutional collection 
	 * @param contentTypeId - id of the content type 
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsByCharFirstAvailableOrder(
			int rowStart, int maxResults, InstitutionalCollection collection,
			Long contentTypeId, char firstChar, OrderType orderType) {
		
		if( collection != null ) {
			Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
			Query q = null;
		    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
		    {
		        q = session.getNamedQuery("getInstitutionalCollectionItemsContentTypeByCharFirstAvailableOrderDesc");
		    }
	 	    else
		    {
		        q = session.getNamedQuery("getInstitutionalCollectionItemsContentTypeByCharFirstAvailableOrderAsc");
		    }
		    
			q.setParameter("leftVal", collection.getLeftValue());
			q.setParameter("rightVal", collection.getRightValue());
			q.setParameter("rootId", collection.getTreeRoot().getId());
			q.setParameter("firstChar", Character.valueOf(Character.toLowerCase(firstChar)));
			q.setParameter("contentTypeId", contentTypeId);
			q.setFirstResult(rowStart);
		    q.setMaxResults(maxResults);
		    q.setFetchSize(maxResults);
	        return q.list();
		} else {
			return new LinkedList<InstitutionalItem>();
		}
		
	}

	/**
	 * Get a list of items for a specified repository by publication date.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the collection to get items 
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsFirstAvailableOrder(
			int rowStart, int maxResults, Long repositoryId,
			Long contentTypeId, OrderType orderType) {
		
		
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = null;
	    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByFirstAvailableOrderDesc");
	    }
 	    else
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByFirstAvailableOrderAsc");
	    }
	    
	    q.setParameter("repositoryId", repositoryId);
		q.setParameter("contentTypeId", contentTypeId);
		q.setFirstResult(rowStart);
	    q.setMaxResults(maxResults);
	    q.setFetchSize(maxResults);
        return q.list();
	}

	/**
	 * Get a list of items for a specified repository by publication date.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the collection to get items 
	 * @param orderType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsFirstAvailableOrder(
			int rowStart, int maxResults, Long repositoryId, OrderType orderType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = null;
	    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
	    {
	        q = session.getNamedQuery("getRepositoryItemsByFirstAvailableOrderDesc");
	    }
 	    else
	    {
	        q = session.getNamedQuery("getRepositoryItemsByFirstAvailableOrderAsc");
	    }
	    
	    q.setParameter("repositoryId", repositoryId);
		q.setFirstResult(rowStart);
	    q.setMaxResults(maxResults);
	    q.setFetchSize(maxResults);
        return q.list();
	}

	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters with the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getRepositoryItemsBetweenChar(int, int, java.lang.Long, char, char, java.lang.Long, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsBetweenCharFirstAvailableOrder(
			int rowStart, int maxResults, Long repositoryId, char firstChar,
			char lastChar, OrderType orderType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = null;
	    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
	    {
	        q = session.getNamedQuery("getRepositoryItemsByCharRangeFirstAvailableOrderDesc");
	    }
 	    else
	    {
	        q = session.getNamedQuery("getRepositoryItemsByCharRangeFirstAvailableOrderAsc");
	    }
	    
	    q.setParameter("repositoryId", repositoryId);
		q.setParameter("firstChar",Character.valueOf(Character.toLowerCase(firstChar)));
		q.setParameter("lastChar", Character.valueOf(Character.toLowerCase(lastChar)));
		q.setFirstResult(rowStart);
	    q.setMaxResults(maxResults);
	    q.setFetchSize(maxResults);
        return q.list();
	}

	/**
	 * Get a list of items for a specified repository by between the that have titles
	 * that start between the specified characters with the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResulsts - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character in range that the first letter of the name can have
	 * @param lastChar - last character in range that the first letter of the name can have
	 * @param contentTypeId - id of the content type
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getRepositoryItemsBetweenChar(int, int, java.lang.Long, char, char, java.lang.Long, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsBetweenCharFirstAvailableOrder(
			int rowStart, int maxResults, Long repositoryId, char firstChar,
			char lastChar, Long contentTypeId, OrderType orderType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = null;
	    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByCharRangeFirstAvailableOrderDesc");
	    }
 	    else
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByCharRangeFirstAvailableOrderAsc");
	    }
	    
	    q.setParameter("repositoryId", repositoryId);
		q.setParameter("firstChar",Character.valueOf(Character.toLowerCase(firstChar)));
		q.setParameter("lastChar", Character.valueOf(Character.toLowerCase(lastChar)));
		q.setParameter("contentTypeId", contentTypeId);
		q.setFirstResult(rowStart);
	    q.setMaxResults(maxResults);
	    q.setFetchSize(maxResults);
        return q.list();
	}

	/**
	 * Get a list of items for a specified repository by first character of the name and
	 * the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsByCharFirstAvailableOrder(
			int rowStart, int maxResults, Long repositoryId, char firstChar,
			OrderType orderType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = null;
	    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
	    {
	        q = session.getNamedQuery("getRepositoryItemsByCharFirstAvailableOrderDesc");
	    }
 	    else
	    {
	        q = session.getNamedQuery("getRepositoryItemsByCharFirstAvailableOrderAsc");
	    }
	    
	    q.setParameter("repositoryId", repositoryId);
		q.setParameter("firstChar", Character.valueOf(Character.toLowerCase(firstChar)));
		q.setFirstResult(rowStart);
	    q.setMaxResults(maxResults);
	    q.setFetchSize(maxResults);
        return q.list();
	}

	/**
	 * Get a list of items for a specified repository by first character of the name and
	 * the given content type id
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param repositoryId - id of the repository to get items 
	 * @param contentTypeId - id of the content type
	 * @param firstChar - first character that the name should have
	 * @param orderType - The order to sort by (asc/desc)
	 * 
	 * @return List of institutional items
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getRepositoryItemsByCharFirstAvailableOrder(
			int rowStart, int maxResults, Long repositoryId,
			Long contentTypeId, char firstChar, OrderType orderType) {
		Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
		Query q = null;
	    if( orderType != null && orderType.equals(OrderType.DESCENDING_ORDER))
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByCharFirstAvailableOrderDesc");
	    }
 	    else
	    {
	        q = session.getNamedQuery("getRepositoryItemsContentTypeByCharFirstAvailableOrderAsc");
	    }
	    
	    q.setParameter("repositoryId", repositoryId);
		q.setParameter("char", Character.valueOf(Character.toLowerCase(firstChar)));
		q.setParameter("contentTypeId", contentTypeId);
		q.setFirstResult(rowStart);
	    q.setMaxResults(maxResults);
	    q.setFetchSize(maxResults);
        return q.list();
	}

	
}
