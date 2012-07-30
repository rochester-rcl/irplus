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
import edu.ur.metadata.marc.MarcRelatorCode;
import edu.ur.metadata.marc.MarcRelatorCodeDAO;

/**
 * Data access for the marc relator code.
 * 
 * @author Nathan Sarr
 *
 */
public class HbMarcRelatorCodeDAO implements MarcRelatorCodeDAO{

	/** eclipse generated id */
	private static final long serialVersionUID = 2332424818056241859L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<MarcRelatorCode> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbMarcRelatorCodeDAO() {
		hbCrudDAO = new HbCrudDAO<MarcRelatorCode>(MarcRelatorCode.class);
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
		return (Long) hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("marcRelatorCodeCount").uniqueResult();

	}

	public List<MarcRelatorCode> getAll() {
		return hbCrudDAO.getAll();
	}

	public MarcRelatorCode getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(MarcRelatorCode entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(MarcRelatorCode entity) {
		hbCrudDAO.makeTransient(entity);
	}

	public MarcRelatorCode findByUniqueName(String name) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMarcRelatorCodeByName");
		q.setParameter("name", name);
		return (MarcRelatorCode)q.uniqueResult();
	
	}

	@Override
	public MarcRelatorCode getByRelatorCode(String relatorCode) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMarcRelatorCodeByRelatorCode");
		q.setParameter("relatorCode", relatorCode);
		return (MarcRelatorCode)q.uniqueResult();
	}

}
