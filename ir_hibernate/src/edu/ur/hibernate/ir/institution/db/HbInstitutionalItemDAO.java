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
import java.text.SimpleDateFormat;
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
import edu.ur.order.OrderType;

/**
 * Implementation of relational storage for an institutional item.
 * 
 * @author Nathan Sarr
 *
 */
public class HbInstitutionalItemDAO implements InstitutionalItemDAO {
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(HbInstitutionalItemDAO.class);
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<InstitutionalItem> hbCrudDAO;
	
	/** Format for date comparison - this can be needed by certain databases - for
	 * example postgres - the default set here - however - defferent strings can be
	 * set using injection
	 */
	private String dateFormat = "yyyy-MM-dd HH:mm:ss";

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
		boolean isItemPublishedToCollection = false;
		Object[] values = new Object[] {institutionalCollectionId, genericItemId};
		InstitutionalItem item = (InstitutionalItem) HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("isItemPublishedToThisCollection", 
				values));
		
		if (item != null) {
			isItemPublishedToCollection = true;
		}
		
		return isItemPublishedToCollection;
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
		List<InstitutionalItem> foundItems = new LinkedList<InstitutionalItem>();
			
			foundItems = (List<InstitutionalItem>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
			{
			    public Object doInHibernate(Session session) throws HibernateException, SQLException 
			    {
			        Query q = null;
				    if( orderType.equals(OrderType.DESCENDING_ORDER))
				    {
				        q = session.getNamedQuery("getRepositoryItemsByNameOrderDesc");
				    }
			 	    else
				    {
				        q = session.getNamedQuery("getRepositoryItemsByNameOrderAsc");
				    }
				    q.setLong(0, repositoryId);
				    q.setFirstResult(rowStart);
				    q.setMaxResults(maxResults);
				    q.setCacheable(false);
				    q.setReadOnly(true);
				    q.setFetchSize(maxResults);
		            return q.list();
			    }
		    });
	    return foundItems;	
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
		List<InstitutionalItem> foundItems = new LinkedList<InstitutionalItem>();
			
			foundItems = (List<InstitutionalItem>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
			{
			    public Object doInHibernate(Session session) throws HibernateException, SQLException 
			    {
			        Query q = null;
				    if( orderType.equals(OrderType.DESCENDING_ORDER))
				    {
				        q = session.getNamedQuery("getRepositoryItemsByCharOrderDesc");
				    }
			 	    else
				    {
				        q = session.getNamedQuery("getRepositoryItemsByCharOrderAsc");
				    }
				    q.setLong(0, repositoryId);
				    q.setCharacter( 1, Character.toLowerCase(firstChar) );
				    q.setFirstResult(rowStart);
				    q.setMaxResults(maxResults);
				    q.setFetchSize(maxResults);
		            return q.list();
			    }
		    });
	    return foundItems;	
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
		List<InstitutionalItem> foundItems = new LinkedList<InstitutionalItem>();
		
		foundItems = (List<InstitutionalItem>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getRepositoryItemsByCharRangeOrderDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getRepositoryItemsByCharRangeOrderAsc");
			    }
			    q.setLong(0, repositoryId);
			    q.setCharacter( 1, Character.toLowerCase(firstChar) );
			    q.setCharacter( 2, Character.toLowerCase(lastChar) );
			    q.setFirstResult(rowStart);
			    q.setMaxResults(maxResults);
			    q.setFetchSize(maxResults);
	            return q.list();
		    }
	    });
        return foundItems;	
	}
	

	/** 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCollectionItemsByName(int, int, edu.ur.ir.institution.InstitutionalCollection, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsByName(final int rowStart, final int maxResults, 
			final InstitutionalCollection collection, final OrderType orderType)
	{
	
		List<InstitutionalItem> foundItems = new LinkedList<InstitutionalItem>();
		
			foundItems = (List<InstitutionalItem>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
			{
			    public Object doInHibernate(Session session) throws HibernateException, SQLException 
			    {
			        Query q = null;
				    if( orderType.equals(OrderType.DESCENDING_ORDER))
				    {
				        q = session.getNamedQuery("getInstitutionalCollectionItemsByNameOrderDesc");
				    }
			 	    else
				    {
				        q = session.getNamedQuery("getInstitutionalCollectionItemsByNameOrderAsc");
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
		
	    return foundItems;	
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
		Object[] values = new Object[]{repositoryId, new Character(Character.toLowerCase(nameFirstChar))};
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
				new Character(Character.toLowerCase(nameFirstCharRange)),
				new Character(Character.toLowerCase(namelastCharRange))};
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
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCollectionItemsBetweenChar(int, int, edu.ur.ir.institution.InstitutionalCollection, char, char, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsBetweenChar(final int rowStart,
			final int maxResults, 
			final InstitutionalCollection collection, 
			final char firstChar,
			final char lastChar, 
			final OrderType orderType) {
		List<InstitutionalItem> foundItems = new LinkedList<InstitutionalItem>();
		
		foundItems = (List<InstitutionalItem>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getCollectionItemsByCharRangeOrderDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getCollectionItemsByCharRangeOrderAsc");
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
        return foundItems;	
	}

	/**
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCollectionItemsByChar(int, int, edu.ur.ir.institution.InstitutionalCollection, char, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getCollectionItemsByChar(final int rowStart,
			final int maxResults, 
			final InstitutionalCollection collection,
			final char firstChar, 
			final OrderType orderType) {
		List<InstitutionalItem> foundItems = new LinkedList<InstitutionalItem>();
		
		foundItems = (List<InstitutionalItem>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
			    {
			        q = session.getNamedQuery("getInstitutionalCollectionItemsByCharOrderDesc");
			    }
		 	    else
			    {
			        q = session.getNamedQuery("getInstitutionalCollectionItemsByCharOrderAsc");
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
        return foundItems;	
	}

	
	/**
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCount(edu.ur.ir.institution.InstitutionalCollection, char)
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstChar) {
		Object[] values = new Object[]{
				collection.getLeftValue(),
				collection.getRightValue(),
				collection.getTreeRoot().getId(),
				new Character(Character.toLowerCase(nameFirstChar))};
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("institutionalItemCountForCollectionByChar", values));

	}

	
	/**
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getCount(edu.ur.ir.institution.InstitutionalCollection, char, char)
	 */
	public Long getCount(InstitutionalCollection collection,
			char nameFirstCharRange, char namelastCharRange) {
		Object[] values = new Object[]{
				collection.getLeftValue(),
				collection.getRightValue(),
				collection.getTreeRoot().getId(),
				new Character(Character.toLowerCase(nameFirstCharRange)),
				new Character(Character.toLowerCase(namelastCharRange))};
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("institutionalItemCountForCollectionByCharRange", values));
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
		List<InstitutionalItem> foundItems = new LinkedList<InstitutionalItem>();
		
		foundItems = (List<InstitutionalItem>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
		        
			    if( orderType.equals(OrderType.DESCENDING_ORDER))
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
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getItems(edu.ur.ir.institution.InstitutionalCollection, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItem> getItems(final InstitutionalCollection collection,
			final Date startDate, final Date endDate) {
        List<InstitutionalItem> foundItems = new LinkedList<InstitutionalItem>();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		log.debug("Trying dates " + simpleDateFormat.format(startDate) + " and " + simpleDateFormat.format(endDate));

        foundItems = (List<InstitutionalItem>) hbCrudDAO.getHibernateTemplate().execute(new HibernateCallback() 
		{
		    public Object doInHibernate(Session session) throws HibernateException, SQLException 
		    {
		        Query q = null;
			  
			    q = session.getNamedQuery("getInstitutionalCollectionItemsByAcceptedDateRange");
			   
			    q.setLong(0, collection.getId());
			    q.setString(1, simpleDateFormat.format(startDate));
			    q.setString(2, simpleDateFormat.format(endDate));
	            return q.list();
		    }
	    });
        return foundItems;	
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}


}
