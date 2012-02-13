/**  
   Copyright 2008-2012 University of Rochester

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

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageMember;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageMemberDAO;

/**
 * Hibernate implementation of the group workspace project member
 * 
 * @author Nathan Sarr
 *
 */
public class HbGroupWorkspaceProjectPageMemberDAO implements GroupWorkspaceProjectPageMemberDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = -7759949046111878191L;

	
	/** hibernate helper  */
	private final HbCrudDAO<GroupWorkspaceProjectPageMember> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceProjectPageMemberDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceProjectPageMember>(GroupWorkspaceProjectPageMember.class);
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
	
	public GroupWorkspaceProjectPageMember getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	public void makePersistent(GroupWorkspaceProjectPageMember entity) {
		hbCrudDAO.makePersistent(entity);	
	}

	public void makeTransient(GroupWorkspaceProjectPageMember entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
