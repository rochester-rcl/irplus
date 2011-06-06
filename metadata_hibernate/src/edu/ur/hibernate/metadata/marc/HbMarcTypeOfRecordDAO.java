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
import edu.ur.metadata.marc.MarcTypeOfRecord;
import edu.ur.metadata.marc.MarcTypeOfRecordDAO;

/**
 * Default implementation of marc type of record.
 * 
 * @author Nathan Sarr
 *
 */
public class HbMarcTypeOfRecordDAO implements MarcTypeOfRecordDAO {
	
	//eclipse generated id.
	private static final long serialVersionUID = 2941313248903731861L;
	
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<MarcTypeOfRecord> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbMarcTypeOfRecordDAO() {
		hbCrudDAO = new HbCrudDAO<MarcTypeOfRecord>(MarcTypeOfRecord.class);
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
		return (Long) hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("marcTypeOfRecordCount").uniqueResult();

	}

	public List<MarcTypeOfRecord> getAll() {
		return hbCrudDAO.getAll();
	}

	public MarcTypeOfRecord getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(MarcTypeOfRecord entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(MarcTypeOfRecord entity) {
		hbCrudDAO.makeTransient(entity);
	}

	public MarcTypeOfRecord findByUniqueName(String name) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMarcTypeOfRecordByName");
		q.setParameter("name", name);
		return (MarcTypeOfRecord)q.uniqueResult();
	
	}

	@Override
	public MarcTypeOfRecord getByRecordType(String recordType) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getMarcTypeOfRecordByRecordType");
		q.setParameter("recordType", recordType);
		return (MarcTypeOfRecord)q.uniqueResult();
	}


}
