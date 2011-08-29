/**  
   Copyright 2008 - 2011 University of Rochester

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

package edu.ur.hibernate.ir.groupspace.db;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceFileDeleteRecordDAO;
import edu.ur.ir.groupspace.GroupWorkspaceFileDeleteRecord;

/**
 * Implementation of the group workspace file delete record - for
 * tracking the deletion of group workspace files.
 * 
 * @author Nathan Sarr
 *
 */
public class HbGroupWorkspaceFileDeleteRecordDAO implements GroupWorkspaceFileDeleteRecordDAO {
	
	/* Eclipse generated id */
	private static final long serialVersionUID = 3300445865233862130L;
	
	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<GroupWorkspaceFileDeleteRecord> hbCrudDAO;

	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceFileDeleteRecordDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceFileDeleteRecord>(GroupWorkspaceFileDeleteRecord.class);
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
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("groupWorkspaceFileDeleteRecordCount");
		return (Long)q.uniqueResult();
	}

	public GroupWorkspaceFileDeleteRecord getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(GroupWorkspaceFileDeleteRecord entity) {
		hbCrudDAO.makePersistent(entity);
	}

	public void makeTransient(GroupWorkspaceFileDeleteRecord entity) {
		hbCrudDAO.makeTransient(entity);
	}

	
	public int deleteAll() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("groupWorkspaceFileDeleteRecordDeleteAll");
		return q.executeUpdate();
	}


}
