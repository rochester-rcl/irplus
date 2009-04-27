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


package edu.ur.ir.web.action.user.admin;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherIndexService;
import edu.ur.ir.user.Affiliation;
import edu.ur.ir.user.AffiliationService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.RoleService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.table.Pager;

/**
 * Action to approve the user affiliations
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ApproveAffiliationAction extends Pager {

	/** eclipse generated id */
	private static final long serialVersionUID = 9223131298401384685L;

	/** user service */
	private UserService userService;

	/** Role service */
	private RoleService roleService;

	/** Affiliation service */
	private AffiliationService affiliationService;
	
	/** Set of users having pending approval */
	private Collection<IrUser> pendingUsers;
	
	/** Set of selected affiliation ids */
	private String affiliationIds;
	
	/** List of all affiliations */
	private List<Affiliation> affiliations;

	/** Set of user ids */
	private List<Long> userIds;
	
	/** Researcher Index Service */
	private ResearcherIndexService researcherIndexService;

	/** Researcher Service */
	private RepositoryService repositoryService;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of users to approve */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Default constructor */
	public  ApproveAffiliationAction() {
		
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}


	/**
	 * Save the approved affiliations
	 * 
	 * @return
	 */
	public String saveAffiliationApproval() throws NoIndexFoundException
	{
		
		StringTokenizer tokenizer = new StringTokenizer(affiliationIds, ",");
		
		IrUser user = null;
		Affiliation affiliation = null;
		Researcher researcher = null;
		
		for(int i=0;i<userIds.size();i++) {
			user = userService.getUser(userIds.get(i), false);
			affiliation = affiliationService.getAffiliation(new Long(tokenizer.nextToken()), false);
			user.setAffiliation(affiliation);
			
			/** Assign the role for the affiliation */
			if (affiliation.getAuthor()) {
				user.addRole(roleService.getRole(IrRole.AUTHOR_ROLE));
			}
				
			if (affiliation.getResearcher()) {
				user.addRole(roleService.getRole(IrRole.RESEARCHER_ROLE));
				// Create researcher object only if the user has no researcher object.
				// Sometimes user might have researcher object if the user is an admin.
				if (user.getResearcher() == null) {
					researcher = user.createResearcher();
				}
			}
			

			user.setAffiliationApproved(true);
			userService.makeUserPersistent(user);
			
			// Add researcher to index
			if (researcher != null) {
				Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
						false);
				
				researcherIndexService.addToIndex(researcher, 
						new File(repository.getResearcherIndexFolder()) );
			}


			userService.sendAffiliationConfirmationEmail(user, affiliation);
		}
		
		return SUCCESS;
	}
	
	
	/**
	 * Get the users having pending affiliation approval.
	 * 
	 * @return
	 */
	public String viewPendingApproval()
	{

		rowEnd = rowStart + numberOfResultsToShow;
	    
		pendingUsers = userService.getUsersPendingAffiliationApproval(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = userService.getUsersPendingAffiliationApprovalCount().intValue();
	    affiliations = affiliationService.getAllAffiliationsNameOrder();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;
	}
	
	/**
	 * Get the user service 
	 * 
	 * @return user service
	 * 
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Set the user service
	 * 
	 * @param userService user service
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Get affiliation service
	 * 
	 * @return
	 */
	public AffiliationService getAffiliationService() {
		return affiliationService;
	}

	/**
	 * Set affiliation service
	 * 
	 * @param affiliationService
	 */
	public void setAffiliationService(AffiliationService affiliationService) {
		this.affiliationService = affiliationService;
	}

	/**
	 * Get users whose affiliation approval is pending
	 * 
	 * @return
	 */
	public Collection<IrUser> getPendingUsers() {
		return pendingUsers;
	}

	/**
	 * Set users whose affiliation approval is pending
	 * 
	 * @param pendingUsers
	 */
	public void setPendingUsers(Collection<IrUser> pendingUsers) {
		this.pendingUsers = pendingUsers;
	}

	/**
	 * Get the selected affiliation ids
	 * 
	 * @return
	 */
	public String getAffiliationIds() {
		return affiliationIds;
	}

	/**
	 * Set the selected affiliation ids
	 * 
	 * @param affiliationIds
	 */
	public void setAffiliationIds(String affiliationIds) {
		this.affiliationIds = affiliationIds;
	}

	/**
	 * Get all affiliations
	 * 
	 * @return
	 */
	public List<Affiliation> getAffiliations() {
		return affiliations;
	}

	/**
	 * Set all affiliations
	 * 
	 * @param affiliations
	 */
	public void setAffiliations(List<Affiliation> affiliations) {
		this.affiliations = affiliations;
	}

	/**
	 * Get selected user ids
	 * 
	 * @return
	 */
	public List<Long> getUserIds() {
		return userIds;
	}

	/**
	 * Set selected user ids
	 * 
	 * @param userIds
	 */
	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}


	public RoleService getRoleService() {
		return roleService;
	}


	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}


	public void setResearcherIndexService(
			ResearcherIndexService researcherIndexService) {
		this.researcherIndexService = researcherIndexService;
	}


	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}


	public String getSortType() {
		return sortType;
	}


	public void setSortType(String sortType) {
		this.sortType = sortType;
	}


	public int getTotalHits() {
		return totalHits;
	}


	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}


	public int getRowEnd() {
		return rowEnd;
	}


	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}



}
