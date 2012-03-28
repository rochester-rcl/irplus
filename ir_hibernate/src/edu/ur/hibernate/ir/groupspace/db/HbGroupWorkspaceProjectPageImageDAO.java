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

import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageImage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageImageDAO;

/**
 * Hibernate Group workspace project page image data access
 * 
 * @author Nathan Sarr
 *
 */
public class HbGroupWorkspaceProjectPageImageDAO implements GroupWorkspaceProjectPageImageDAO {

	// eclipse generated id
	private static final long serialVersionUID = 2247622296378207707L;
	
	/** hibernate helper  */
	private final HbCrudDAO<GroupWorkspaceProjectPageImage> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceProjectPageImageDAO() {
		hbCrudDAO = new HbCrudDAO< GroupWorkspaceProjectPageImage>( GroupWorkspaceProjectPageImage.class);
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

	
	public GroupWorkspaceProjectPageImage getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	
	public void makePersistent(GroupWorkspaceProjectPageImage entity) {
		hbCrudDAO.makePersistent(entity);
		
	}

	
	public void makeTransient(GroupWorkspaceProjectPageImage entity) {
		hbCrudDAO.makeTransient(entity);
	}

}
