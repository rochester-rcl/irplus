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

import java.util.List;

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceGroup;
import edu.ur.ir.groupspace.GroupWorkspaceGroupDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.order.OrderType;

/**
 * Hibernate implementation of group space group persistence
 * 
 * @author Nathan Sarr
 *
 */
public class HbGroupWorkspaceGroupDAO implements GroupWorkspaceGroupDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = 7129354264082297025L;
	
	/** hibernate helper */
	private final HbCrudDAO<GroupWorkspaceGroup> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceGroupDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceGroup>(GroupWorkspaceGroup.class);
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

	public List<GroupWorkspaceGroup> getGroups(Long groupSpaceId, int rowStart,
			int numberOfResultsToShow, OrderType orderType) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<GroupWorkspaceGroup> getGroups(Long groupSpaceId, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public IrUser getUserForGroup(Long groupId, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public GroupWorkspaceGroup getById(Long id, boolean lock) {
		// TODO Auto-generated method stub
		return null;
	}

	public void makePersistent(GroupWorkspaceGroup entity) {
		// TODO Auto-generated method stub
		
	}

	public void makeTransient(GroupWorkspaceGroup entity) {
		// TODO Auto-generated method stub
		
	}

}
