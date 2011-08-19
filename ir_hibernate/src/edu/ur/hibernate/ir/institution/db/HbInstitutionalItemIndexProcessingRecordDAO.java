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

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;


import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecord;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.PlaceOfPublication;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.Series;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.PersonName;

/**
 * Interface for dealing with institutional item index processing 
 * 
 * @author Nathan Sarr
 *
 */
public class HbInstitutionalItemIndexProcessingRecordDAO implements InstitutionalItemIndexProcessingRecordDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = -1245600447718553275L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<InstitutionalItemIndexProcessingRecord> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbInstitutionalItemIndexProcessingRecordDAO() {
		hbCrudDAO = new HbCrudDAO<InstitutionalItemIndexProcessingRecord>(InstitutionalItemIndexProcessingRecord.class);
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
	 * Get all items order by id then by updated date.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#getAllOrderByItemIdUpdatedDate()
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemIndexProcessingRecord> getAllOrderByItemIdUpdatedDate(int rowStart, int maxResults) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInstitutionalItemIndexProcessingRecordOrderByIdDate");
		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		return q.list();
	}

	/**
	 * Get the record by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public InstitutionalItemIndexProcessingRecord getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Add the record to persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(InstitutionalItemIndexProcessingRecord entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Remove the record from persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(InstitutionalItemIndexProcessingRecord entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get the count of institutional item index processing records.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		 Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("institutionalItemIndexProcessingRecordCount");
		 return (Long)q.uniqueResult();
	}

	/**
	 * Get the institutional item processing record by item id and processing type.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#get(java.lang.Long, edu.ur.ir.index.IndexProcessingType)
	 */
	public InstitutionalItemIndexProcessingRecord get(Long itemId,
			IndexProcessingType processingType) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("instItemProcessingRecByItemIdProcessingType");
		q.setParameter("itemId", itemId);
		q.setParameter("processingTypeId", processingType.getId());
		return (InstitutionalItemIndexProcessingRecord)q.uniqueResult();
	}

	
	/**
	 * Insert all items for collection.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#insertAllItemsForCollection(edu.ur.ir.institution.InstitutionalCollection, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForCollection(
			final InstitutionalCollection institutionalCollection,
			final IndexProcessingType processingType) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertAllItemsForCollection");
		q.setParameter("leftValue", institutionalCollection.getLeftValue());
		q.setParameter("rightValue", institutionalCollection.getRightValue());
		q.setParameter("treeRootId", institutionalCollection.getTreeRoot().getId());
		q.setParameter("processingTypeId", processingType.getId());
		return Long.valueOf(q.executeUpdate());
 	}
	
	/**
	 * Insert all items for a repository
	 * 
	 */
	public Long insertAllItemsForRepository(final IndexProcessingType processingType) {		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertAllItemsForRepository");
		q.setParameter("processingTypeId", processingType.getId());
		return Long.valueOf(q.executeUpdate());
 	}

	/**
	 * Set all items for re-indexing for a given content type
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#insertAllItemsForContentType(long)
	 */
	public Long insertAllItemsForContentType(ContentType contentType, IndexProcessingType processingType) {
		Long numCreated = 0l;
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertAllItemsForContentType");
		q.setParameter("contentTypeId", contentType.getId());
		q.setParameter("processingTypeId", processingType.getId());
		numCreated += q.executeUpdate();
		return numCreated;
	}

	/**
	 * Insert all institutional items with associated with a given contributor type.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#insertAllItemsForContributorType(java.lang.Long, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForContributorType(ContributorType contributorType,
			IndexProcessingType processingType) {
        Long numCreated = 0l;
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertAllItemsForContributorType");
		q.setParameter("contributorTypeId", contributorType.getId());
		q.setParameter("processingTypeId", processingType.getId());
		numCreated += q.executeUpdate();
		return numCreated;
	}

	/**
	 * Insert all items for the identifier type.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#insertAllItemsForIdentifierType(edu.ur.ir.item.IdentifierType, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForIdentifierType(IdentifierType identifierType,
			IndexProcessingType processingType) {
	    Long numCreated = 0l;
	    Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertAllItemsForIdentifierType");
		q.setParameter("identifierTypeId", identifierType.getId());
		q.setParameter("processingTypeId", processingType.getId());
		numCreated += q.executeUpdate();
		return numCreated;
	}

	/**
	 * insert all items for a language type.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#insertAllItemsForLanguageType(edu.ur.ir.item.LanguageType, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForLanguageType(LanguageType languageType,
			IndexProcessingType processingType) {
	    Long numCreated = 0l;
	    Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertAllItemsForLanguageType");
	    q.setParameter("languageTypeId", languageType.getId());
		q.setParameter("processingTypeId", processingType.getId());
		numCreated += q.executeUpdate();
		return numCreated;
	}
	
	/**
	 * insert all items for a place of publication.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#insertAllItemsForLanguageType(edu.ur.ir.item.LanguageType, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForPlaceOfPublication(PlaceOfPublication placeOfPublication,
			IndexProcessingType processingType) {
	    Long numCreated = 0l;
	    Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertAllItemsForPlaceOfPublication");
	    q.setParameter("placeOfPublicationId", placeOfPublication.getId());
		q.setParameter("processingTypeId", processingType.getId());
		numCreated += q.executeUpdate();
		return numCreated;
	}

	/**
	 *  insert all items for a person name
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#insertAllItemsForPersonName(edu.ur.ir.person.PersonName, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForPersonName(PersonName personName,
			IndexProcessingType processingType) {
	    Long numCreated = 0l;
	    Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertAllItemsForContributorPersonName");
		q.setParameter("personNameId", personName.getId());
		q.setParameter("processingTypeId", processingType.getId());
		numCreated += q.executeUpdate();
		return numCreated;
	}

	/**
	 * Insert all items for publisher change.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#insertAllItemsForPublisher(edu.ur.ir.item.Publisher, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForPublisher(Publisher publisher,
			IndexProcessingType processingType) {
	    Long numCreated = 0l;
	    Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertAllItemsForPublisher");
		q.setParameter("publisherId", publisher.getId());
		q.setParameter("processingTypeId", processingType.getId());
		numCreated += q.executeUpdate();
		return numCreated;
	}

	/**
	 * Insert all items for series change.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#insertAllItemsForSeries(edu.ur.ir.item.Series, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForSeries(Series series,
			IndexProcessingType processingType) {
		Long numCreated = 0l;
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertAllItemsForSeries");
		q.setParameter("seriesId", series.getId());
		q.setParameter("processingTypeId", processingType.getId());
		numCreated += q.executeUpdate();
		return numCreated;
	}

	/**
	 * Insert all items for a sponsor change.
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO#insertAllItemsForSponsor(edu.ur.ir.item.Sponsor, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForSponsor(Sponsor sponsor,
			IndexProcessingType processingType) {
		Long numCreated = 0l;
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("insertAllItemsForSponsor");
		q.setParameter("sponsorId", sponsor.getId());
		q.setParameter("processingTypeId", processingType.getId());
		numCreated += q.executeUpdate();
		return numCreated;
	}
	
	/**
	 * Get all institutional item index processing records for a given index processing type.
	 * 
	 * @return list of records found
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemIndexProcessingRecord> getAllByProcessingTypeUpdatedDate(Long processingTypeId)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("instItemProcessingRecByProcessingType");
		q.setParameter("processingTypeId", processingTypeId);
		return q.list();

	}



}
