/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.hibernate.ir.item.metadata.marc.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.item.metadata.marc.ContentTypeRecordTypeMapper;
import edu.ur.ir.item.metadata.marc.ContentTypeRecordTypeMapperDAO;

/**
 * Hibernate content type record type data access layer implementation.
 * 
 * @author Nathan Sarr
 *
 */
public class HbContentTypeRecordTypeMapperDAO implements ContentTypeRecordTypeMapperDAO{

	
	// Eclipse generated id
	private static final long serialVersionUID = 1176196729061317271L;

	//  Helper for persisting information using hibernate.  	
	private final HbCrudDAO<ContentTypeRecordTypeMapper> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbContentTypeRecordTypeMapperDAO() {
		hbCrudDAO = new HbCrudDAO<ContentTypeRecordTypeMapper>(ContentTypeRecordTypeMapper.class);
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
	 * Get the mapping by content type id.
	 * 
	 * @param contentTypeId - id of the content type
	 * @return - the mapping otherwise null
	 */
	public ContentTypeRecordTypeMapper getByContentType(
			Long contentTypeId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getContentTypeRecordTypeByContentTypeId");
		q.setParameter("contentTypeId", contentTypeId);
		return (ContentTypeRecordTypeMapper)q.uniqueResult();
	}

	/**
	 * Get the mapping by record type.
	 * 
	 * @param recordTypeId - id of the record type
	 * @return the mapping otherwise null.
	 */
	public ContentTypeRecordTypeMapper getByRecordType(Long recordTypeId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getContentTypeRecordTypeByRecordTypeId");
		q.setParameter("recordTypeId", recordTypeId);
		return (ContentTypeRecordTypeMapper)q.uniqueResult();
	}
	
	/**
	 * Returns the list of records that have the specified record type code.
	 * 
	 * @param recordTypeCode - record type code 
	 * @return the list of records found with the record type string value
	 */
	@SuppressWarnings("unchecked")
	public List<ContentTypeRecordTypeMapper> getByRecordType(String recordType)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getContentTypeRecordTypeByRecordType");
		q.setParameter("recordType",  recordType);
		return q.list();
	}

	/**
	 * Get all records
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<ContentTypeRecordTypeMapper> getAll() {
		return hbCrudDAO.getAll();
	}

	/**
	 * Get by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public ContentTypeRecordTypeMapper getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Save the content type record type mapper to persistant storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ContentTypeRecordTypeMapper entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Delete the content type record type mapper from persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ContentTypeRecordTypeMapper entity) {
		hbCrudDAO.makeTransient(entity);
	}
}
