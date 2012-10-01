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


package edu.ur.hibernate.metadata.marc;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.metadata.marc.MarcDataField;
import edu.ur.metadata.marc.MarcDataFieldDAO;

/**
 * Data access for the marc data field.
 * 
 * @author Nathan Sarr
 *
 */
public class HbMarcDataFieldDAO implements MarcDataFieldDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = 4069588211804487008L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<MarcDataField> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbMarcDataFieldDAO() {
		hbCrudDAO = new HbCrudDAO<MarcDataField>(MarcDataField.class);
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

	public Long getCount() {
		return (Long) hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("marcDataFieldCount").uniqueResult();

	}

	public List<MarcDataField> getAll() {
		return hbCrudDAO.getAll();
	}

	public MarcDataField getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(MarcDataField entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(MarcDataField entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * This will look at the code field and NOT the name field.
	 * 
	 * @see edu.ur.dao.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public MarcDataField findByUniqueName(String name) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMarcDataFieldByCode");
		q.setParameter("code", name);
		return (MarcDataField)q.uniqueResult();
	
	}

}
