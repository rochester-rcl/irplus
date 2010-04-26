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
import edu.ur.ir.user.PersonalItemDeleteRecord;
import edu.ur.ir.user.PersonalItemDeleteRecordDAO;

/**
 * Implementation of the personal file delete record data access object.
 * 
 * @author Nathan Sarr
 *
 */
public class HbPersonalItemDeleteRecordDAO implements PersonalItemDeleteRecordDAO{
	
	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<PersonalItemDeleteRecord> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbPersonalItemDeleteRecordDAO() {
		hbCrudDAO = new HbCrudDAO<PersonalItemDeleteRecord>(PersonalItemDeleteRecord.class);
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
		return (Long)HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("personalItemDeleteRecordCount"));
	}

	public List<PersonalItemDeleteRecord> getAll() {
		return hbCrudDAO.getAll();
	}

	public PersonalItemDeleteRecord getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(PersonalItemDeleteRecord entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(PersonalItemDeleteRecord entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	public int deleteAll() {
		int count = hbCrudDAO.getAll().size();
		hbCrudDAO.getHibernateTemplate().deleteAll(hbCrudDAO.getAll());
		return count;
	}

}
