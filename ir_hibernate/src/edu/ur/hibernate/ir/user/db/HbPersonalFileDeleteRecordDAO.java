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

package edu.ur.hibernate.ir.user.db;

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.user.PersonalFileDeleteRecord;
import edu.ur.ir.user.PersonalFileDeleteRecordDAO;

/**
 * Implementation of the personal file delete record data access object.
 * 
 * @author Nathan Sarr
 *
 */
public class HbPersonalFileDeleteRecordDAO implements PersonalFileDeleteRecordDAO{
	
	/** eclipse generated id*/
	private static final long serialVersionUID = 3499126646933499847L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<PersonalFileDeleteRecord> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbPersonalFileDeleteRecordDAO() {
		hbCrudDAO = new HbCrudDAO<PersonalFileDeleteRecord>(PersonalFileDeleteRecord.class);
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
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("personalFileDeleteRecordCount"));
	}

	public List<PersonalFileDeleteRecord> getAll() {
		return hbCrudDAO.getAll();
	}

	public PersonalFileDeleteRecord getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(PersonalFileDeleteRecord entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(PersonalFileDeleteRecord entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	public int deleteAll() {
		int count = hbCrudDAO.getAll().size();
		hbCrudDAO.getHibernateTemplate().deleteAll(hbCrudDAO.getAll());
		return count;
	}

}
