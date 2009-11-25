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

package edu.ur.ir.web.action.researcher;

import java.io.File;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherIndexService;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;

/**
 * This action allows all of a researchers information to be deleted
 * and removes the researcher privilege from the user.
 * 
 * @author Nathan Sarr
 *
 */
public class DeleteResearcherPage extends ActionSupport {

	/** eclipse generated id. */
	private static final long serialVersionUID = -6873158421623113875L;
	
	/** Service for dealing with researcher information */
	private ResearcherService researcherService;
	
	/** Service to deal with researcher information */
	private UserService userService;
	
	/** id of the user to delete the researcher page from */
	private Long researcherUserId;
	
	/** Service for dealing with researcher information; */
	private ResearcherIndexService researcherIndexService;
	
	/** Service for dealing with repository information */
	private RepositoryService repositoryService;
	
	public String execute()
	{
		Repository repo = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		IrUser researcherUser = userService.getUser(researcherUserId, false);
		Researcher researcher = researcherUser.getResearcher();
		if( researcher != null )
		{
			Long researcherId = researcher.getId();
			researcherService.deleteResearcher(researcher);
			File researcherIndex = new File(repo.getResearcherIndexFolder());
			researcherIndexService.deleteFromIndex(researcherId, researcherIndex);
			
		}
		researcherUser.removeRole(IrRole.RESEARCHER_ROLE);	
		userService.makeUserPersistent(researcherUser);
		return SUCCESS;
	}


	public ResearcherService getResearcherService() {
		return researcherService;
	}

	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Long getResearcherUserId() {
		return researcherUserId;
	}

	public void setResearcherUserId(Long researcherUserId) {
		this.researcherUserId = researcherUserId;
	}


	public RepositoryService getRepositoryService() {
		return repositoryService;
	}


	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}


	public void setResearcherIndexService(
			ResearcherIndexService researcherIndexService) {
		this.researcherIndexService = researcherIndexService;
	}

}
