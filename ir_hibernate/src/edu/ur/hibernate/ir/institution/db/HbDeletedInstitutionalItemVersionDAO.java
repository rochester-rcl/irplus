/**  
   Copyright 2008-2010 University of Rochester

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
import edu.ur.ir.institution.DeletedInstitutionalItemVersion;
import edu.ur.ir.institution.DeletedInstitutionalItemVersionDAO;

/**
 * Hibernate implementation of the deleted institutional item version.
 * 
 * @author Nathan Sarr
 *
 */
public class HbDeletedInstitutionalItemVersionDAO implements DeletedInstitutionalItemVersionDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = -4778762252679281734L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<DeletedInstitutionalItemVersion> hbCrudDAO;
	
	
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
	 * Default Constructor
	 */
	public HbDeletedInstitutionalItemVersionDAO() {
		hbCrudDAO = new HbCrudDAO<DeletedInstitutionalItemVersion>(DeletedInstitutionalItemVersion.class);
	}
	
	/**
	 * 
	 * @see edu.ur.ir.institution.DeletedInstitutionalItemVersionDAO#get(java.lang.Long)
	 */
	public DeletedInstitutionalItemVersion get(final Long institutionalItemVersionId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeleteInstitutionalItemVersionById");
		q.setParameter("instititutionalItemVersionId", institutionalItemVersionId);
        return (DeletedInstitutionalItemVersion)q.uniqueResult();
	}

	/**
	 * 
	 * @see edu.ur.ir.institution.DeletedInstitutionalItemVersionDAO#get(java.lang.Long, java.lang.Long)
	 */
	public DeletedInstitutionalItemVersion get(final Long institutionalItemId, final int versionNumber) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeleteInstitutionalItemVersionByInstVersion");
		q.setParameter("versionNumber", versionNumber);
		q.setParameter("institutionalItemId", institutionalItemId);
	    return (DeletedInstitutionalItemVersion)q.uniqueResult();
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<DeletedInstitutionalItemVersion> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public DeletedInstitutionalItemVersion getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(DeletedInstitutionalItemVersion entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(DeletedInstitutionalItemVersion entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get a list of deleted institutional items ordered by institutional item version id ascending.
	 * 
	 * 
	 * @param lastInstitutionalItemVersionId - the last institutional item version id
	 * to be processed.  Use 0 if no items have yet been processed.  Will grab max results
	 * of where ids are greater than the given id.
	 * 
	 * @param maxResulsts - maximum number of results to fetch
	 * 
	 * @return List of institutional item version
	 */
	@SuppressWarnings("unchecked")
	public List<DeletedInstitutionalItemVersion> getItemsIdOrder( long lastDeletedInstitutionalItemVersionId,
			int maxResults)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemVersionByLastIdOrderedById");
		q.setParameter("lastId", lastDeletedInstitutionalItemVersionId);
		q.setMaxResults(maxResults);
		return (List<DeletedInstitutionalItemVersion>) q.list();
	}
	
	/**
	 * Get a list of deleted institutional items ordered by institutional item version id order ascending.
	 * 
	 * @param lastDeletedInstitutionalItemVersionId - the last deleted institutional item version id
	 * to be processed.  Use 0 if no items have yet been processed.  Will grab max results
	 * of where ids are greater than the given id.
	 * 
	 * @param institutional collection ids - the collections to look within
	 * @param maxResults - maximum number of results
	 * 
	 * @return - deleted items greater than the given id and belong to the specified set
	 */
	@SuppressWarnings("unchecked")
	public List<DeletedInstitutionalItemVersion> getItemsIdOrder( long lastDeletedItemVersionId,
			List<Long> institutionalCollectionIds, int maxResults)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeletedInstitutionalItemVersionByLastIdSetOrderedById");
        q.setParameter("lastId", lastDeletedItemVersionId);
        q.setParameterList("collectionIds", institutionalCollectionIds);
        q.setMaxResults(maxResults);
		return (List<DeletedInstitutionalItemVersion>)q.list();
	}
	
	/**
	 * Get a count of the total number of deleted institutional item versions.
	 * 
	 * @return
	 */
	public Long getCount()
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("deletedInstitutionalItemVersionCount");
		return (Long)q.uniqueResult();
	}
	
	/**
	 * Get a count of deleted institutional item versions within a collection
	 * 
	 * @param institutional collection ids - the collections to look within
	 * 
	 * @return the count
	 */
	public Long getCount(List<Long> institutionalCollectionIds)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeletedInstitutionalItemVersionBySetCount");
        q.setParameterList("collectionIds", institutionalCollectionIds);
		return (Long)q.uniqueResult();

	}	
	/**
	 * Get a count of the deleted institutional item versions that have been deleted
	 * between the specified dates - this includes new additions to the repository
	 * 
	 * @param fromDeletedDate - from date deleted
	 * @param untilDeletedDate - until date deleted
	 * @param institutional collection ids - the collections to look within
	 * 
	 * @return - count of items found
	 */
	public Long getItemsBetweenDeletedDatesCount( Date fromDeletedDate,
			Date untilDeletedDate, List<Long> institutionalCollectionIds)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeletedInstitutionalItemVersionSetBetweenDatesCount");
        q.setParameterList("collectionIds", institutionalCollectionIds);
        q.setParameter("fromDate", fromDeletedDate);
        q.setParameter("untilDate", untilDeletedDate);
		return (Long)q.uniqueResult();
	}	
	
	/**
	 * Get a count of deleted institutional item versions between the deleted dates.
	 * 
	 * @param fromDeletedDate - from date
	 * @param untilDeletedDate - until date
	 * 
	 * @return all items deleted between the from date and until date.
	 */
	public Long getItemsBetweenDeletedDatesCount( Date fromDeletedDate,
			Date untilDeletedDate)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeletedInstitutionalItemVersionBetweenDatesCount");
		q.setParameter("fromDate", fromDeletedDate);
		q.setParameter("untilDate",  untilDeletedDate);
		return (Long) q.uniqueResult();
	}	
	/**
	 * Get a count of deleted items within a given collection that have a deleted date
	 * greater than or equal to the specified date.  This includes sub collections.
	 * 
	 * @param fromDeletedDate - date the deletion must be greater than or equal to
	 * @param institutional collection ids - the collections to look within
	 * 
	 * @return the count of the number of items found greater than the specified date and within the specified collection
	 * or sub collections
	 */
	public Long getItemsFromDeletedDateCount( Date fromDeletedDate,
			List<Long> institutionalCollectionIds)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeletedInstitutionalItemVersionSetFromDateCount");
	    q.setParameterList("collectionIds", institutionalCollectionIds);
	    q.setParameter("fromDate", fromDeletedDate);
		return (Long)q.uniqueResult();
	}	
	/**
	 * Get a count of all deleted items  equal to or before the specified date.
	 * 
	 * @param until Deleted Date - date the deletion should be less than or equal to
	 * @return
	 */
	public Long getItemsUntilDeletedDateCount(Date untilDeletedDate)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeletedInstitutionalItemVersionUntilDateCount");
		q.setParameter("untilDate", untilDeletedDate);
		return (Long)q.uniqueResult();
	}
	
	/**
	 * Get a count of deleted items within a given collection that have a deletion date
	 * less than or equal to the specified date.  This includes sub collections.
	 * 
	 * @param fromDeletedDate - date the deletion must be greater than or equal to
	 * @param institutional collection ids - the collections to look within
	 * 
	 * @return the count of the number of items found less than or equal to the specified date and within the specified collection
	 * or sub collections
	 */
	public Long getItemsUntilDeletedDateCount(
			Date untilDeletedDate,
			List<Long> institutionalCollectionIds)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeletedInstitutionalItemVersionSetUntilDateCount");
	    q.setParameterList("collectionIds", institutionalCollectionIds);
	    q.setParameter("untilDate", untilDeletedDate);
		return (Long)q.uniqueResult();
	}
	
	/**
	 * Get the deleted items ordered by id  that are greater than or equal to the given deletion date.  This will include 
	 * those items that were deleted on the date.Will grab max results
	 * of where ids are greater than the given id.
	 * 
	 * @param lastDeletedInstitutionalItemVersionId - last deleted institutional item id.  Use 0 if no items have yet been processed.  Will grab max results
	 * of where ids are greater than the given id.
	 * @param fromDeletedDate - starting from deletion date
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return list of institutional item versions greater than the given id and greater than or equal to
	 * the given from delete date.
	 */
	@SuppressWarnings("unchecked")
	public List<DeletedInstitutionalItemVersion> getItemsIdOrderFromDeletedDate(
			long lastDeletedInstitutionalItemVersionId, Date fromDeletedDate, int maxResults)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeletedInstitutionalItemVersionByLastIdFromDateOrderedById");
		q.setParameter("lastId", lastDeletedInstitutionalItemVersionId);
		q.setParameter("fromDate", fromDeletedDate);
		q.setMaxResults(maxResults);
		return (List<DeletedInstitutionalItemVersion>) q.list();
	}
	
	/**
	 * Get a count of deleted items that have a deletion date
	 * greater than or equal to the specified date. 
	 * 
	 * @param fromDeletedDate - date the deletion must be greater than or equal to
	 * 
	 * @return the count of the number of items found greater than the specified date 
	 */
	public Long getItemsFromDeletedDateCount(Date fromDeletedDate)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeletedInstitutionalItemVersionFromDateCount");
		q.setParameter("fromDate", fromDeletedDate);
		return (Long)q.uniqueResult();
	}
	
	/**
	 * Get the items ordered by id  that are less than or equal to the given deletion date.  
	 * This will include those items that were deleted on the date.  Will grab max results
	 * of where ids are greater than the given id.

	 * @param lastDeletedInstitutionalItemVersionId - last institutional item id that items must be greater than
	 * @param untilDeletedDate - deleted date the items must be less than or equal to
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return - list of items id ordered that are less than or equal to the given date.
	 */
	@SuppressWarnings("unchecked")
	public List<DeletedInstitutionalItemVersion> getItemsIdOrderUntilDeletedDate(
			long lastDeletedInstitutionalItemVersionId, Date untilDeletedDate, int maxResults)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeletedInstitutionalItemVersionByLastIdUntilDateOrderedById");
		q.setParameter("lastId", lastDeletedInstitutionalItemVersionId);
		q.setParameter("untilDate", untilDeletedDate);
		q.setMaxResults(maxResults);
		return (List<DeletedInstitutionalItemVersion>) q.list();
	}
	
	/**
	 * Get the items ordered by id  that are between the given from and until deposit dates.  Will return 
	 * only items greater than the given last institutional item ids.
	 *   
	 * This will include those items that were deleted on the given dates.  Will grab max results
	 * where ids are greater than the given id.

	 * @param lastDeletedInstitutionalItemVersionId - id that institutional item versions must be greater than
     * @param fromDeletedDate - items deleted date must be greater than or equal to
	 * @param untilDeletedDate - items deleted date must be less than or equal to
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return - list of items that meet the specified criteria.
	 */
	@SuppressWarnings("unchecked")
	public List<DeletedInstitutionalItemVersion> getItemsIdOrderBetweenDeletedDates(
			long lastDeletedInstitutionalItemVersionId, Date fromDeletedDate, Date untilDeletedDate, int maxResults) 
	{		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeletedInstitutionalItemVersionByLastIdBetweenDatesOrderedById");
		q.setParameter("lastId", lastDeletedInstitutionalItemVersionId);
		q.setParameter("fromDate", fromDeletedDate);
		q.setParameter("untilDate",  untilDeletedDate);
		q.setMaxResults(maxResults);
		return (List<DeletedInstitutionalItemVersion>) q.list();
	}	
	
	/**
	 * Get the items ordered by id  that are greater than or equal to the given deleted date.  This will include 
	 * those items that were deleted on the date.  Will grab max results
	 * of where ids are greater than the given id.
	 * 
	 * @param lastDeletedInstitutionalItemVersionId - last institutional item id.  Use 0 if no items have yet been processed.  Will grab max results
	 * of where ids are greater than the given id.
	 * @param fromDeletedDate - starting from deleted date
	 * @param institutional collection ids - the collections to look within
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return list of institutional item versions greater than the given id and greater than or equal to
	 * the given from deleted date.
	 */
	@SuppressWarnings("unchecked")
	public List<DeletedInstitutionalItemVersion> getItemsIdOrderFromDeletedDate(
			long lastDeletedInstitutionalItemVersionId, Date fromDeletedDate, 
			List<Long> institutionalCollectionIds, int maxResults)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeletedInstitutionalItemVersionByLastIdSetFromDateOrderedById");
	    q.setParameterList("collectionIds", institutionalCollectionIds);
		q.setParameter("lastId", lastDeletedInstitutionalItemVersionId);
		q.setParameter("fromDate", fromDeletedDate);
	    q.setMaxResults(maxResults);
		return (List<DeletedInstitutionalItemVersion>)q.list();
	}
	
	/**
	 * Get the items ordered by id  that are less than or equal to the given deleted date.  
	 * This will include those items that were deleted on the date.  Will grab max results
	 * of where ids are greater than the given id.

	 * @param lastDeletedInstitutionalItemVersionId
	 * @param untilDeletedDate - deleted date the items must be less than or equal to
	 * @param institutional collection ids - the collections to look within
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return list of items id ordered that are less than or equal to the given date and belong to 
	 * the specified set id
	 */
	@SuppressWarnings("unchecked")
	public List<DeletedInstitutionalItemVersion> getItemsIdOrderUntilDeletedDate(
			long lastDeletedInstitutionalItemVersionId, Date untilDeletedDate, 
			List<Long> institutionalCollectionIds, int maxResults)
    {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeletedInstitutionalItemVersionByLastIdSetUntilDateOrderedById");
	    q.setParameterList("collectionIds", institutionalCollectionIds);
		q.setParameter("lastId", lastDeletedInstitutionalItemVersionId);
		q.setParameter("untilDate", untilDeletedDate);
	    q.setMaxResults(maxResults);
		return (List<DeletedInstitutionalItemVersion>)q.list();
	}
	
	/**
	 * Get the items ordered by id  that are between the given from and until deleted dates.  Will return 
	 * only items greater than the given last institutional item ids.
	 *   
	 * This will include those items that were deleted on the given dates.  Will grab max results
	 * where ids are greater than the given id.

	 * @param lastDeletedInstitutionalItemVersionId - id that institutional item versions must be greater than
     * @param fromDeletedDate - items deleted date must be greater than or equal to
	 * @param untilDeletedDate - items deleted date must be less than or equal to
	 * @param institutional collection ids - the collections to look within
	 * @param maxResults - maximum number of results to return
	 * 
	 * @return - list of items that meet the specified criteria.
	 */

	@SuppressWarnings("unchecked")
	public List<DeletedInstitutionalItemVersion> getItemsIdOrderBetweenDeletedDates(
			long lastDeletedInstitutionalItemVersionId, Date fromDeletedDate, Date untilDeletedDate, 
			List<Long> institutionalCollectionIds, int maxResults)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getDeletedInstitutionalItemVersionByLastIdSetBetweenDatesOrderedById");
	    q.setParameterList("collectionIds", institutionalCollectionIds);
		q.setParameter("lastId", lastDeletedInstitutionalItemVersionId);
		q.setParameter("fromDate", fromDeletedDate);
		q.setParameter("untilDate", untilDeletedDate);
	    q.setMaxResults(maxResults);
		return (List<DeletedInstitutionalItemVersion>)q.list();
	}


}
