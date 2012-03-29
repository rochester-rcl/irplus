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

package edu.ur.ir.groupspace.service;

import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageImage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageService;
import edu.ur.ir.repository.RepositoryService;

/**
 * Default implementation of the group workspace group project page service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultGroupWorkspaceProjectPageService implements GroupWorkspaceProjectPageService{

	// eclipse generated id
	private static final long serialVersionUID = 3827791719303070221L;
	
	// data access for group workspace project page info
	private GroupWorkspaceProjectPageDAO groupWorkspaceProjectPageDAO;
	
	// service to deal with repository information.
	private RepositoryService repositoryService;


	/**
	 * Get the group workspace project page by i.d
	 * 
	 * @param id - id of the group workspace
	 * @param lock - upgrade the lock mode for the object.
	 * 
	 * @return the group workspace if found otherwise null
	 */
	public GroupWorkspaceProjectPage getById(Long id,  boolean lock) {
		return groupWorkspaceProjectPageDAO.getById(id, lock);
	}

	/**
	 * Save the group workspace project page.
	 * 
	 * @param projectPage
	 */
	public void save(GroupWorkspaceProjectPage projectPage) {
		groupWorkspaceProjectPageDAO.makePersistent(projectPage);
		
	}
	
	/**
	 * Get the group workspace project data access object.
	 * 
	 * @return
	 */
	public GroupWorkspaceProjectPageDAO getGroupWorkspaceProjectPageDAO() {
		return groupWorkspaceProjectPageDAO;
	}

	/**
	 * Set the group workspace project data access object.
	 * 
	 * @param groupWorkspaceProjectPageDAO
	 */
	public void setGroupWorkspaceProjectPageDAO(
			GroupWorkspaceProjectPageDAO groupWorkspaceProjectPageDAO) {
		this.groupWorkspaceProjectPageDAO = groupWorkspaceProjectPageDAO;
	}

	/**
	 * Delete the group workspace project page image.  This also removes the file from the 
	 * repository.
	 * 
	 * @param groupWorkspaceProjectPageImage
	 */
	public void delete(
			GroupWorkspaceProjectPageImage groupWorkspaceProjectPageImage) {
		if( groupWorkspaceProjectPageImage != null )
		{
		    GroupWorkspaceProjectPage projectPage = groupWorkspaceProjectPageImage.getGroupWorkspaceProjectPage();
		    if( projectPage.remove(groupWorkspaceProjectPageImage) )
		    {
		    	groupWorkspaceProjectPageDAO.makePersistent(projectPage);
			    repositoryService.deleteIrFile(groupWorkspaceProjectPageImage.getImageFile());
		    }
		}
		
	}
	
	/**
	 * Set the repository service.
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	
}
