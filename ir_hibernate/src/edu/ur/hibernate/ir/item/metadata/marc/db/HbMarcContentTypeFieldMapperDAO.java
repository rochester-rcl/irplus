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
import edu.ur.ir.item.metadata.marc.MarcContentTypeFieldMapper;
import edu.ur.ir.item.metadata.marc.MarcContentTypeFieldMapperDAO;

/**
 * Implementation of the Marc Content type field mapper data access object.
 * 
 * @author Nathan Sarr
 *
 */
public class HbMarcContentTypeFieldMapperDAO implements MarcContentTypeFieldMapperDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = -5853383312640313089L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<MarcContentTypeFieldMapper> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbMarcContentTypeFieldMapperDAO() {
		hbCrudDAO = new HbCrudDAO<MarcContentTypeFieldMapper>(MarcContentTypeFieldMapper.class);
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


	
	public MarcContentTypeFieldMapper getByContentTypeId(Long contentTypeId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMarcContentTypeFieldMapperByContentTypeId");
		q.setParameter("contentTypeId", contentTypeId);
        return (MarcContentTypeFieldMapper)q.uniqueResult();
	}

	
	public List<MarcContentTypeFieldMapper> getAll() {
		return hbCrudDAO.getAll();
	}

	
	public MarcContentTypeFieldMapper getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	public void makePersistent(MarcContentTypeFieldMapper entity) {
		hbCrudDAO.makePersistent(entity);
	}

	
	public void makeTransient(MarcContentTypeFieldMapper entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Get the list of all content types with the specified data field name and indicator settings.
	 * 
	 * @param code - code of the data field (100, 200, etc)
	 * @param indicator1 - first indicator value
	 * @param indicator2 - second indicator value
	 * @param subField - subfield value.
	 * 
	 * @return list of content type sub filed mappings.
	 */
	@SuppressWarnings("unchecked")
	public List<MarcContentTypeFieldMapper> getByDataField(String code, 
			String indicator1, 
			String indicator2,
			String subField)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getContentTypeMapperByDataField");
		q.setParameter("code", code);
		q.setParameter("indicator1", indicator1);
		q.setParameter("indicator2", indicator2);
		q.setParameter("subField", subField);
		return q.list();
	}

}
