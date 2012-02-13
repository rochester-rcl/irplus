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

import edu.ur.ir.groupspace.GroupWorkspaceProjectPageMember;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageMemberDAO;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageMemberService;

/**
 * Service to deal with group workspace project page member information.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultGroupWorkspaceProjectPageMemberService implements GroupWorkspaceProjectPageMemberService {

	// eclipse generated id
	private static final long serialVersionUID = 6557749405320278420L;
	
	// data access for group workspace project page info
	private GroupWorkspaceProjectPageMemberDAO groupWorkspaceProjectPageMemberDAO;


	
	/**
	 * Get the group workspace project member by unique id.
	 * 
	 * @see edu.ur.ir.groupspace.GroupWorkspaceProjectPageMemberService#getById(java.lang.Long, boolean)
	 */
	public GroupWorkspaceProjectPageMember getById(Long id, boolean lock) {
		return groupWorkspaceProjectPageMemberDAO.getById(id, lock);
	}

	
	/**
	 * Save the group worskpace project member.
	 * 
	 * @see edu.ur.ir.groupspace.GroupWorkspaceProjectPageMemberService#save(edu.ur.ir.groupspace.GroupWorkspaceProjectPageMember)
	 */
	public void save(GroupWorkspaceProjectPageMember projectPageMember) {
		groupWorkspaceProjectPageMemberDAO.makePersistent(projectPageMember);
	}
	
	/**
	 * Get the data access for the project page member.
	 * 
	 * @return
	 */
	public GroupWorkspaceProjectPageMemberDAO getGroupWorkspaceProjectPageMemberDAO() {
		return groupWorkspaceProjectPageMemberDAO;
	}

	/**
	 * Set the project page member data access.
	 * 
	 * @param groupWorkspaceProjectPageMemberDAO
	 */
	public void setGroupWorkspaceProjectPageMemberDAO(
			GroupWorkspaceProjectPageMemberDAO groupWorkspaceProjectPageMemberDAO) {
		this.groupWorkspaceProjectPageMemberDAO = groupWorkspaceProjectPageMemberDAO;
	}


}
