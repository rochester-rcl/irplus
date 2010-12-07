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

package edu.ur.hibernate.ir.file.db;

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.hibernate.HbHelper;
import edu.ur.ir.file.FileVersion;
import edu.ur.ir.file.FileVersionDAO;

/**
 * This class holds the versions of IrFile
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbFileVersionDAO implements FileVersionDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = 4095692888365210258L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<FileVersion> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbFileVersionDAO() {
		hbCrudDAO = new HbCrudDAO<FileVersion>(FileVersion.class);
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
	 * Get a count of the Versions
	 * 
	 * @see edu.ur.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)
		HbHelper.getUnique(hbCrudDAO.getHibernateTemplate().findByNamedQuery("versionCount"));
	}

	/**
	 * Return Version by id
	 */
	public FileVersion getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(FileVersion entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(FileVersion entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
