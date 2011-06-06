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
import edu.ur.metadata.marc.MarcSubField;
import edu.ur.metadata.marc.MarcSubFieldDAO;

/**
 * Data access for the marc sub field.
 * 
 * @author Nathan Sarr
 *
 */
public class HbMarcSubFieldDAO implements MarcSubFieldDAO{

    // eclipse generated id
	private static final long serialVersionUID = -8031550676841538178L;
	
	// crud helper
	private final HbCrudDAO<MarcSubField> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbMarcSubFieldDAO() {
		hbCrudDAO = new HbCrudDAO<MarcSubField>(MarcSubField.class);
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
		return (Long) hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("marcSubFieldCount").uniqueResult();
	}

	public List<MarcSubField> getAll() {
		return hbCrudDAO.getAll();
	}

	public MarcSubField getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(MarcSubField entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(MarcSubField entity) {
		hbCrudDAO.makeTransient(entity);
	}

	public MarcSubField findByUniqueName(String name) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMarcSubFieldByName");
		q.setParameter("name", name);
		return (MarcSubField)q.uniqueResult();
	}

}
