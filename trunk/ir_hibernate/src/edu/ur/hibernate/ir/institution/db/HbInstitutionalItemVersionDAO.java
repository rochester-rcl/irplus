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

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionDAO;
import edu.ur.ir.institution.InstitutionalItemVersionDownloadCount;
import edu.ur.order.OrderType;

/**
 * Persistence of Institutional item version data.
 * 
 * @author Nathan Sarr
 *
 */
public class HbInstitutionalItemVersionDAO implements InstitutionalItemVersionDAO{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 666924883180148194L;
	
	/** Helper for persisting information using hibernate.  */
	private final HbCrudDAO<InstitutionalItemVersion> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbInstitutionalItemVersionDAO() {
		hbCrudDAO = new HbCrudDAO<InstitutionalItemVersion>(InstitutionalItemVersion.class);
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
	 * Get all Institutional item version data.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get a Institutional item version by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public InstitutionalItemVersion getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the Institutional item version persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(InstitutionalItemVersion entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the Institutional item version transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(InstitutionalItemVersion entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get the  publications for a given set of names ordered by title.
	 * 
	 * @param rowStart - start position
	 * @param maxResults - maximum number of results to return
	 * @param personNameIds - set of name ids to get 
	 * @param orderType - way to order the set
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesByTitle(final int rowStart,
			final int maxResults, 
			final List<Long> personNameIds, 
			final OrderType orderType)
	{
		
		Query q = null;
		if( orderType.equals(OrderType.DESCENDING_ORDER))
		{
		    q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getPublicationVersionsByPersonNameIdTitleDesc");
		}
		else
		{
		    q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getPublicationVersionsByPersonNameIdTitleAsc");
		}
			    
		q.setParameterList("personNameIds", personNameIds);
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return (List<InstitutionalItemVersionDownloadCount>)q.list();
	}
	
	/**
	 * Get the  publications for a given set of names by title.
	 * 
	 * @param rowStart - start position
	 * @param maxResults - maximum number of results to return
	 * @param personNameIds - set of name ids to get 
	 * @param orderType - way to order the set
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesByDownload(
			final int rowStart, final int maxResults,
			final List<Long> personNameIds, final OrderType orderType) {
		Query q = null;
		if (orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = hbCrudDAO.getSessionFactory().getCurrentSession()
					.getNamedQuery(
							"getPublicationVersionsByPersonNameIdDownloadDesc");
		} else {
			q = hbCrudDAO.getSessionFactory().getCurrentSession()
					.getNamedQuery(
							"getPublicationVersionsByPersonNameIdDownloadAsc");
		}

		q.setParameterList("personNameIds", personNameIds);
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return (List<InstitutionalItemVersionDownloadCount>) q.list();
	}

	/**
	 * Get the institutional item version by handle id.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemVersionByHandleId(java.lang.Long)
	 */
	public InstitutionalItemVersion getItemVersionByHandleId(Long handleId) {
		
		Query query = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemByHandleId");
		query.setParameter(0, handleId);
		return (InstitutionalItemVersion)query.uniqueResult();
	}
    
	/**
	 * Get the download count for a sponsor
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getDownloadCountForSponsor(java.lang.Long)
	 */
	public Long getDownloadCountForSponsor(Long sponsorId) {
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDownloadCountBySponsor");
		q.setLong(0, sponsorId);
	    Long count = (Long)q.uniqueResult();
	    if( count == null )
	    {
	    	count = Long.valueOf(0l);
	    }
	    return count;
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getItemsBySponsorItemNameOrder(int,
	 *      int, long, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemNameOrder(
			final int rowStart, final int maxResults, final long sponsorId,
			final OrderType orderType) {
		Query q = null;
		if (orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = hbCrudDAO.getSessionFactory().getCurrentSession()
					.getNamedQuery("getPublicationsForSponsorDesc");
		} else {
			q = hbCrudDAO.getSessionFactory().getCurrentSession()
					.getNamedQuery("getPublicationsForSponsorAsc");
		}

		q.setLong("sponsorId", sponsorId);
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return (List<InstitutionalItemVersionDownloadCount>) q.list();
	}
	
	/**
	 * Get a count of the number of items sponsored by a particular person.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemDAO#getItemsBySponsorCount(long)
	 */
	public Long getItemsBySponsorCount(long sponsorId) {
        Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getPublicationsForSponsorCount");
	    q.setLong("sponsorId", sponsorId);
	    Long count = (Long)q.uniqueResult();
	    if( count == null )
	    {
	    	count = Long.valueOf(0l);
	    }
	    return count;

	}

	/**
	 * Get items by sponsor item deposit date order.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemsBySponsorItemDepositDateOrder(int,
	 *      int, long, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemDepositDateOrder(
			final int rowStart, final int maxResults, final long sponsorId,
			final OrderType orderType) {
		Query q = null;
		if (orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = hbCrudDAO.getSessionFactory().getCurrentSession()
					.getNamedQuery("getPublicationsForSponsorDepositDateDesc");
		} else {
			q = hbCrudDAO.getSessionFactory().getCurrentSession()
					.getNamedQuery("getPublicationsForSponsorDepositDateAsc");
		}

		q.setLong("sponsorId", sponsorId);
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return (List<InstitutionalItemVersionDownloadCount>) q.list();
	}

	/**
	 * Get items by sponsor item deposit download order.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemsBySponsorItemDepositDateOrder(int, int, long, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersionDownloadCount> getItemsBySponsorItemDownloadOrder(
			final int rowStart, final int maxResults, final long sponsorId,
			final OrderType orderType) {
		Query q = null;
		if (orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = hbCrudDAO
					.getSessionFactory()
					.getCurrentSession()
					.getNamedQuery(
							"getPublicationsSponsorDownloadCountOrderByCountDesc");
		} else {
			q = hbCrudDAO
					.getSessionFactory()
					.getCurrentSession()
					.getNamedQuery(
							"getPublicationsSponsorDownloadCountOrderByCountAsc");
		}

		q.setLong("sponsorId", sponsorId);
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return (List<InstitutionalItemVersionDownloadCount>) q.list();
	}
	
	/**
	 * Get the download counts for a set of person names.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getDownloadCountByPersonName(java.util.List)
	 */
	public Long getDownloadCountByPersonName(List<Long> personNameIds) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDownloadCountByPersonNames");
	    q.setParameterList("personNameIds", personNameIds);
	    Long count = (Long)q.uniqueResult();
	    if( count == null )
	    {
	    	count = Long.valueOf(0l);
	    }
	    return count;
	}

	/**
	 * Get list of downloads by submission date.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getPublicationVersionsForNamesBySubmissionDate(int,
	 *      int, java.util.List, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersionDownloadCount> getPublicationVersionsForNamesBySubmissionDate(
			final int rowStart, final int maxResults,
			final List<Long> personNameIds, final OrderType orderType) {
		Query q = null;
		if (orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = hbCrudDAO
					.getSessionFactory()
					.getCurrentSession()
					.getNamedQuery(
							"getPublicationVersionsByPersonNameIdSubmissionDateDesc");
		} else {
			q = hbCrudDAO
					.getSessionFactory()
					.getCurrentSession()
					.getNamedQuery(
							"getPublicationVersionsByPersonNameIdSubmissionDateAsc");
		}

		q.setParameterList("personNameIds", personNameIds);
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return (List<InstitutionalItemVersionDownloadCount>) q.list();
	}
	
	/**
	 * Get the earliest submission date found in the institutional item versions. 
	 * 
	 * @return the earliest date of deposit found in the repository.
	 */
	public Date getEarliestDateOfDeposit()
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getEarliestDateOfDeposit");
	    Date ealiestDateOfDepositoy = (Date)q.uniqueResult();
	    return ealiestDateOfDepositoy;
	}

	/**
	 * List of institutional item versions by generic item id.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getInstitutionalItemVersionsByGenericItemId(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersion> getInstitutionalItemVersionsByGenericItemId(
			Long genericItemId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionsForGenericItemId");
		q.setParameter(0, genericItemId);
		return (List<InstitutionalItemVersion>) q.list();
	}

	/**
	 * Get the items ordered by id that are grater than the last id
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemsIdOrder(long, int, edu.ur.order.OrderType)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersion> getItemsIdOrder(
			long lastInstitutionalItemVersionId, int maxResults) {
		                                                                           
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionByLastIdOrderedById");
		q.setParameter("lastId", lastInstitutionalItemVersionId);
		q.setMaxResults(maxResults);
		return (List<InstitutionalItemVersion>) q.list();
	}

	/**
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemsIdOrder(long, java.lang.Long, int)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersion> getItemsIdOrder(
			long lastInstitutionalItemVersionId, 
			InstitutionalCollection institutionalCollection, 
			int maxResults) {
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionByLastIdSetOrderedById");
		q.setParameter("lastId", lastInstitutionalItemVersionId);
		q.setParameter("leftValue", institutionalCollection.getLeftValue());
		q.setParameter("rightValue", institutionalCollection.getRightValue());
		q.setParameter("treeRootId", institutionalCollection.getTreeRoot().getId());
		q.setMaxResults(maxResults);
		return (List<InstitutionalItemVersion>) q.list();

	}

	/**
	 * Get items id order between deposit dates 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemsIdOrderBetweenDepositDates(long, java.util.Date, java.util.Date, java.lang.Long, int)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersion> getItemsIdOrderBetweenModifiedDates(
			long lastInstitutionalItemVersionId, Date fromModifiedDate,
			Date untilModifiedDate, InstitutionalCollection institutionalCollection, int maxResults)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionByLastIdSetBetweenDatesOrderedById");
		q.setParameter("lastId", lastInstitutionalItemVersionId);
		q.setParameter("fromDate", fromModifiedDate);
		q.setParameter("untilDate",  untilModifiedDate);
		q.setParameter("leftValue", institutionalCollection.getLeftValue());
		q.setParameter("rightValue", institutionalCollection.getRightValue());
		q.setParameter("treeRootId", institutionalCollection.getTreeRoot().getId());
		q.setMaxResults(maxResults);
		return (List<InstitutionalItemVersion>) q.list();
	}
	
	/**
	 * Get a count of the institutional item versions 
	 * 
	 * @param fromModifiedDate - from date modification
	 * @param untilModifiedDate - until date modification
	 * @param institutionalCollection - collections the items should be within
	 * 
	 * @return - count of items found
	 */
	public Long getItemsBetweenModifiedDatesCount( Date fromModifiedDate,
			Date untilModifiedDate, InstitutionalCollection institutionalCollection)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionSetBetweenDatesCount");
		q.setParameter("fromDate", fromModifiedDate);
		q.setParameter("untilDate",  untilModifiedDate);
		q.setParameter("leftValue", institutionalCollection.getLeftValue());
		q.setParameter("rightValue", institutionalCollection.getRightValue());
		q.setParameter("treeRootId", institutionalCollection.getTreeRoot().getId());
		return (Long) q.uniqueResult();
	}

	/**
	 * Get items ordered by id with a modification date between the specified dates.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemsIdOrderBetweenModifiedDates(long, java.util.Date, java.util.Date, int)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersion> getItemsIdOrderBetweenModifiedDates(
			long lastInstitutionalItemVersionId, Date fromModifiedDate,
			Date untilModifiedDate, int maxResults) {
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionByLastIdBetweenDatesOrderedById");
		q.setParameter("lastId", lastInstitutionalItemVersionId);
		q.setParameter("fromDate", fromModifiedDate);
		q.setParameter("untilDate",  untilModifiedDate);
		q.setMaxResults(maxResults);
		return (List<InstitutionalItemVersion>) q.list();
	}
	
	/**
	 * Get a count of institutional item versions between the modified dates.
	 * 
	 * @param fromModifiedDate - from date
	 * @param untilModifiedDate - until date
	 * 
	 * @return all items modified between the from date and until date.
	 */
	public Long getItemsBetweenModifiedDatesCount( Date fromModifiedDate,
			Date untilModifiedDate) 
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionBetweenDatesCount");
		q.setParameter("fromDate", fromModifiedDate);
		q.setParameter("untilDate",  untilModifiedDate);
		return (Long) q.uniqueResult();
	}

	/**
	 * Get the items id order greater than or equal to the date specified 
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemsIdOrderFromDepositDate(long, java.util.Date, int)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersion> getItemsIdOrderFromModifiedDate(
			long lastInstitutionalItemVersionId, Date fromModifiedDate,
			int maxResults) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionByLastIdFromDateOrderedById");
		q.setParameter("lastId", lastInstitutionalItemVersionId);
		q.setParameter("fromDate", fromModifiedDate);
		q.setMaxResults(maxResults);
		return (List<InstitutionalItemVersion>) q.list();
	}
	
	/**
	 * Get a count of all items added or modified from the specified date.
	 * 
	 * @param fromModifiedDate - date the modification or addition should be greather tahn or equal to
	 * @return
	 */
	public Long getItemsFromModifiedDateCount(Date fromModifiedDate) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionFromDateCount");
		q.setParameter("fromDate", fromModifiedDate);
		return (Long)q.uniqueResult();
	}
	

	/**
	 * Get the institutional items order id that have a modification date greater than or
	 * equal to the specified from date within the specified collection
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemsIdOrderFromModifiedDate(long, java.util.Date, edu.ur.ir.institution.InstitutionalCollection, int)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersion> getItemsIdOrderFromModifiedDate(
			long lastInstitutionalItemVersionId, 
			Date fromModifiedDate,
			InstitutionalCollection institutionalCollection, 
			int maxResults) {
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionByLastIdSetFromDateOrderedById");
		q.setParameter("lastId", lastInstitutionalItemVersionId);
		q.setParameter("fromDate", fromModifiedDate);
		q.setParameter("leftValue", institutionalCollection.getLeftValue());
		q.setParameter("rightValue", institutionalCollection.getRightValue());
		q.setParameter("treeRootId", institutionalCollection.getTreeRoot().getId());
		q.setMaxResults(maxResults);
		return (List<InstitutionalItemVersion>) q.list();
	}
	
	/**
	 * Get a count of items within a given collection that have a modification date
	 * greater than or equal to the specified date.  This includes sub collections.
	 * 
	 * @param fromModifiedDate - date the modification or creation must be greater than or equal to
	 * @param institutionalCollection - the institutional collection they must reside in.  
	 * 
	 * @return the count of the number of items found greater than or equal to the specified date and within the specified collection
	 * or sub collections
	 */
	public Long getItemsFromModifiedDateCount(
			Date fromModifiedDate,
			InstitutionalCollection institutionalCollection) {
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionSetFromDateCount");
		q.setParameter("fromDate", fromModifiedDate);
		q.setParameter("leftValue", institutionalCollection.getLeftValue());
		q.setParameter("rightValue", institutionalCollection.getRightValue());
		q.setParameter("treeRootId", institutionalCollection.getTreeRoot().getId());
		return (Long) q.uniqueResult();
	}


	/**
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemsIdOrderUntilModifiedDate(long, java.util.Date, int)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersion> getItemsIdOrderUntilModifiedDate(
			long lastInstitutionalItemVersionId, Date untilModifiedDate,
			int maxResults) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionByLastIdUntilDateOrderedById");
		q.setParameter("lastId", lastInstitutionalItemVersionId);
		q.setParameter("untilDate", untilModifiedDate);
		q.setMaxResults(maxResults);
		return (List<InstitutionalItemVersion>) q.list();

	}
	
	/**
	 * Get a count of all items added or modified equal to or before the specified date.
	 * 
	 * @param until ModifiedDate - date the modification or addition should be less than or equal to
	 * @return
	 */
	public Long getItemsUntilModifiedDateCount(Date untilModifiedDate) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionUntilDateCount");
		q.setParameter("untilDate", untilModifiedDate);
		return (Long)q.uniqueResult();
	}
	

	/**
	 * Get all items that were modified on or before the specified until date.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getItemsIdOrderUntilModifiedDate(long, java.util.Date, java.lang.Long, int)
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemVersion> getItemsIdOrderUntilModifiedDate(
			long lastInstitutionalItemVersionId, Date untilModifiedDate,
			InstitutionalCollection institutionalCollection, int maxResults) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionByLastIdSetUntilDateOrderedById");
		q.setParameter("lastId", lastInstitutionalItemVersionId);
		q.setParameter("untilDate", untilModifiedDate);
		q.setParameter("leftValue", institutionalCollection.getLeftValue());
		q.setParameter("rightValue", institutionalCollection.getRightValue());
		q.setParameter("treeRootId", institutionalCollection.getTreeRoot().getId());
		q.setMaxResults(maxResults);
		return (List<InstitutionalItemVersion>) q.list();
	}
	
	/**
	 * Get a count of items within a given collection that have a modification date
	 * less than or equal to the specified date.  This includes sub collections.
	 * 
	 * @param fromModifiedDate - date the modification or creation must be greater than or equal to
	 * @param institutionalCollection - the institutional collection they must reside in.  
	 * 
	 * @return the count of the number of items found less than or equal to the specified date and within the specified collection
	 * or sub collections
	 */
	public Long getItemsUntilModifiedDateCount(
			Date untilModifiedDate,
			InstitutionalCollection institutionalCollection) {
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionSetUntilDateCount");
		q.setParameter("untilDate", untilModifiedDate);
		q.setParameter("leftValue", institutionalCollection.getLeftValue());
		q.setParameter("rightValue", institutionalCollection.getRightValue());
		q.setParameter("treeRootId", institutionalCollection.getTreeRoot().getId());
		return (Long) q.uniqueResult();
	}

	/**
	 * Get a count of the total number of institutional item versions.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get all institutional item versions within the collection.  This includes sub collections
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemVersionDAO#getCount(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public Long getCount(InstitutionalCollection collection) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionBySetCount");
		q.setParameter("leftValue", collection.getLeftValue());
		q.setParameter("rightValue", collection.getRightValue());
		q.setParameter("treeRootId", collection.getTreeRoot().getId());
		return (Long) q.uniqueResult();
	}
	

}
