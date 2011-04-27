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
import edu.ur.ir.marc.MarcDataFieldMapper;
import edu.ur.ir.marc.MarcDataFieldMapperDAO;

/**
 * Default hibernate implementation of the marc data field mapper.
 * 
 * @author Nathan Sarr
 *
 */
public class HbMarcDataFieldMapperDAO implements MarcDataFieldMapperDAO{

	// eclispe generated id
	private static final long serialVersionUID = 5241221524538271973L;
	
	
	//  Helper for persisting information using hibernate.  	
	private final HbCrudDAO<MarcDataFieldMapper> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbMarcDataFieldMapperDAO() {
		hbCrudDAO = new HbCrudDAO<MarcDataFieldMapper>(MarcDataFieldMapper.class);
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
	 * Get all marc data field mappers.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	public List<MarcDataFieldMapper> getAll() {
		return  hbCrudDAO.getAll();
	}

	/**
	 * Get marc data field mapper by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public MarcDataFieldMapper getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Add the data field mapper to persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(MarcDataFieldMapper entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Remove the mapper from persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(MarcDataFieldMapper entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	public MarcDataFieldMapper getByMarcDataFieldId(Long marcDataFieldId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMarcDataFieldMapperByDataFieldId");
		q.setParameter("marcDataFieldId", marcDataFieldId);
		return (MarcDataFieldMapper)q.uniqueResult();
	}

}
