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


package edu.ur.ir.web.action.user;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.Affiliation;
import edu.ur.ir.user.AffiliationService;
import edu.ur.ir.user.Department;
import edu.ur.ir.user.DepartmentService;
import edu.ur.ir.user.InviteInfo;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.RoleService;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserIndexService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.util.TokenGenerator;

/**
 * Action to add user
 * 
 * @author Sharmila Ranganathan
 *
 */
public class AddUser extends ActionSupport implements UserIdAware, Preparable {

	/** Eclipse generated Id */
	private static final long serialVersionUID = -7094483818911517181L;

	/**  Logger for add user action */
	private static final Logger log = Logger.getLogger(AddUser.class);

	/** Token to identify the invited user */
	private String token;

	/** user  */
	private IrUser irUser;
	
	/** Id of the user */
	private Long userId;

	/** User service class */
	private UserService userService;
	
	/** Affiliation service class */
	private AffiliationService affiliationService;

	/** Service for accessing department information */
	private DepartmentService departmentService;
	
	/** Invite user service class */
	private InviteUserService inviteUserService;
	
	/** Service for indexing users */
	private UserIndexService userIndexService;
	
	/** Role service class */
	private RoleService roleService;

	/** Invite information */
	private InviteInfo inviteInfo;
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the user has been added*/
	private boolean added = false;
	
	/** Indicates the account has been locked */
	private boolean accountLocked = false;
	
	/** Default email */
	private UserEmail defaultEmail;
	
	/** List of all affiliations */
	private List<Affiliation> affiliations;

	/** Id of the affiliation selected */
	private Long affiliationId;

	/** List of all departments */
	private List<Department> departments;

	/** Id of the department selected */
	private Long departmentId;
	
	/** Repository service for placing information in the repository */
	private RepositoryService repositoryService;

	/**
	 * Execute method to initialize invite information
	 */
	public String execute() {

		if ((token != null) && (token.length() > 0)) {
			inviteInfo = inviteUserService.findInviteInfoByToken(token);
			defaultEmail = new UserEmail(inviteInfo.getEmail());
			affiliations = affiliationService.getAllAffiliations();
			departments = departmentService.getAllDepartments();
		}
		return SUCCESS;
	}

	/**
	 * Execute method to initialize invite information
	 */
	public String viewUserRegistration() {
		log.debug("viewUserRegistration called");

		affiliations = affiliationService.getAllAffiliations();
		departments = departmentService.getAllDepartments();
		return SUCCESS;
	}

	
	/**
	 * Create user.
	 * Creating user with the invitation sent to collaborate on a document 
	 *  
	 * @return
	 * @throws NoIndexFoundException 
	 */
	public String createUser() throws NoIndexFoundException
	{
		log.debug("creating a user = " + irUser.getUsername());
		String returnStatus = SUCCESS;

		IrUser myIrUser = 
			userService.getUser(irUser.getUsername());
		
		
		if( myIrUser == null)
		{
			IrUser emailExist = userService.getUserByEmail(defaultEmail.getEmail());
			
			
			if (emailExist == null) {
				String firstName = irUser.getFirstName();
				String lastName = irUser.getLastName();
				
				irUser = userService.createUser(irUser.getPassword(), irUser.getUsername(), 
			    		defaultEmail);

				/* Setup Email verification if the user chooses to add a different Email then
				 * the one to which Share invitation was sent.
				 */
				if ((token != null) && (token.length() > 0)) {
					inviteInfo = inviteUserService.findInviteInfoByToken(token);

					if (!inviteInfo.getEmail().equals(defaultEmail.getEmail())) {
						String emailToken = TokenGenerator.getToken();

						UserEmail anotherEmail = new UserEmail(inviteInfo.getEmail());
						anotherEmail.setVerified(true);
						irUser.addUserEmail(anotherEmail, true);

						irUser.getUserEmail(defaultEmail.getEmail()).setToken(emailToken);
						
						// send email With URL to verify email
						userService.sendEmailForEmailVerification(emailToken, defaultEmail.getEmail(), irUser.getUsername());
					} else {
						irUser.getDefaultEmail().setVerified(true);
					}
				} 
				
				irUser.setAccountLocked(accountLocked);
			    irUser.setFirstName(firstName);
			    irUser.setLastName(lastName);
			    
			    if (departmentId != 0) {
			    	Department department = departmentService.getDepartment(departmentId, false); 
			    	irUser.setDepartment(department);
			    }

		    	IrRole role = roleService.getRole("ROLE_USER");
		    	irUser.addRole(role);
		    	
		    	Affiliation affiliation = affiliationService.getAffiliation(affiliationId, false); 
		    	irUser.setAffiliation(affiliation);
		    	
				irUser.setAffiliationApproved(!affiliation.isNeedsApproval());
		    	
		    	// Add collaborator role since the user was invited to collaborate on a document
		    	irUser.addRole(roleService.getRole(IrRole.COLLABORATOR_ROLE));
		    	
		    	
		    	Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
						false);
		    	userService.makeUserPersistent(irUser);
		    	
				userIndexService.addToIndex(irUser, 
							new File( repository.getUserIndexFolder()) );
				
		    	
		    	// Automatically logins the newUser after registering with the system
		    	userService.authenticateUser(irUser, irUser.getPassword(), irUser.getRoles());
		    	
		    	if (affiliation.isNeedsApproval()) {
		    		userService.sendAffiliationVerificationEmailForUser(irUser);
		    		userService.sendPendingAffiliationEmailForUser(irUser);
		    	}
		    	

			} else {
				addFieldError("emailExistError", 
						"This Email already exists in the system. Email: " + defaultEmail.getEmail());
				returnStatus = INPUT;	
				affiliations = affiliationService.getAllAffiliations();
				departments = departmentService.getAllDepartments();
			}
		}
		else
		{
			addFieldError("userNameError", 
					"The user name already exists. User name : " + irUser.getUsername());
			returnStatus = INPUT;
			affiliations = affiliationService.getAllAffiliations();
			departments = departmentService.getAllDepartments();
		}
		
		
        //return "added";
		return returnStatus;
	}

	
	/**
	 * Register with the system
	 *  
	 * @return
	 */
	public String registerUser() throws NoIndexFoundException
	{
		log.debug("creating a user = " + irUser.getUsername());
		String returnStatus = SUCCESS;

		IrUser myIrUser = 
			userService.getUser(irUser.getUsername());
		if( myIrUser == null)
		{
			IrUser emailExist = userService.getUserByEmail(defaultEmail.getEmail());
			
			if (emailExist == null) {
				String firstName = irUser.getFirstName();
				String lastName = irUser.getLastName();
				
				defaultEmail.setVerified(false);
				defaultEmail.setToken(TokenGenerator.getToken());
				
				irUser = userService.createUser(irUser.getPassword(), irUser.getUsername(), 
			    		defaultEmail);
			    irUser.setAccountLocked(accountLocked);
			    irUser.setFirstName(firstName);
			    irUser.setLastName(lastName);
			    
			    if (departmentId != 0) {
			    	Department department = departmentService.getDepartment(departmentId, false); 
			    	irUser.setDepartment(department);
			    }

		    	IrRole role = roleService.getRole("ROLE_USER");
		    	irUser.addRole(role);
		    	
		    	Affiliation affiliation = affiliationService.getAffiliation(affiliationId, false); 
		    	irUser.setAffiliation(affiliation);

		    	irUser.setAffiliationApproved(!affiliation.isNeedsApproval());
		    	
		    	userService.makeUserPersistent(irUser);
		    	
		    	if (affiliation.isNeedsApproval()) {
		    		userService.sendAffiliationVerificationEmailForUser(irUser);
		    		userService.sendPendingAffiliationEmailForUser(irUser);
		    	}

		    	userService.sendAccountVerificationEmailForUser(defaultEmail.getToken(), defaultEmail.getEmail(), irUser.getUsername());
		    	
			    Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
						false);
			    userIndexService.addToIndex(irUser, 
						new File( repository.getUserIndexFolder()) );
		    	
		    	
			} else {
				addFieldError("emailExistError", 
						"This Email already exists in the system. Email: " + defaultEmail.getEmail());
				returnStatus = INPUT;	
				affiliations = affiliationService.getAllAffiliations();
				departments = departmentService.getAllDepartments();
			}
		}
		else
		{
			addFieldError("userNameError", 
					"The user name already exists. User name : " + irUser.getUsername());
			returnStatus = INPUT;
			affiliations = affiliationService.getAllAffiliations();
			departments = departmentService.getAllDepartments();
		}
		
		
		return returnStatus;
	}

	/** 
	 * Prepare the action - get user
	 */
	public void prepare() {
		log.debug("Prepare user Id =" + userId);

		if (userId != null) {
			irUser = userService.getUser(userId, false);
		}
	}

	/**
	 * View for editing my account
	 * 
	 * @return
	 */
	public String viewEditAccount() {
		
		irUser = userService.getUser(userId, false);
		
		return SUCCESS;
	}
	
	/**
	 * Saves the account
	 * 
	 * @return
	 */
	public String saveMyAccount() throws NoIndexFoundException
	{
		
		userService.makeUserPersistent(irUser);
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
				false);
	    userIndexService.updateIndex(irUser, 
				new File( repository.getUserIndexFolder()) );		
		
		return SUCCESS;
	}
	
	/**
	 * Get the invite token
	 *  
	 * @return
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Set the invite token
	 * 
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Get the message
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Set the message
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Returns true if user is added
	 * @return
	 */
	public boolean isAdded() {
		return added;
	}
	
	/**
	 * Set true if user added else set false
	 *  
	 * @param added
	 */
	public void setAdded(boolean added) {
		this.added = added;
	}

	/**
	 * Get the user details
	 * 
	 * @return
	 */
	public IrUser getIrUser() {
		return irUser;
	}

	/**
	 * Set the user
	 * 
	 * @param user
	 */
	public void setIrUser(IrUser irUser) {
		this.irUser = irUser;
	}

	/**
	 * Get the service for user access
	 * 
	 * @return
	 */
	public UserService getUserService() {
		return userService;
	}

	/** 
	 * Set the service for user access
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns true if account locked else false
	 * 
	 * @return
	 */
	public boolean isAccountLocked() {
		return accountLocked;
	}

	/**
	 * Set the account locked value
	 * 
	 * @param accountLocked
	 */
	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	/**
	 * Get the default email for user
	 * 
	 * @return
	 */
	public UserEmail getDefaultEmail() {
		return defaultEmail;
	}
	
	/**
	 * Set the default user email
	 * 
	 * @param defaultEmail
	 */
	public void setDefaultEmail(UserEmail defaultEmail) {
		this.defaultEmail = defaultEmail;
	}

	/**
	 * Service for inviting users
	 * 
	 * @param inviteUserService
	 */
	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}

	/**
	 * Invitation information.
	 * 
	 * @return
	 */
	public InviteInfo getInviteInfo() {
		return inviteInfo;
	}

	/**
	 * Get the role service.
	 * 
	 * @return
	 */
	public RoleService getRoleService() {
		return roleService;
	}

	/**
	 * Set the role service information.
	 * 
	 * @param roleService
	 */
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
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
	 * Set affiliations
	 * 
	 * @param affiliations
	 */
	public void setAffiliations(List<Affiliation> affiliations) {
		this.affiliations = affiliations;
	}

	/**
	 * Get service class for affiliation
	 * 
	 * @return
	 */
	public AffiliationService getAffiliationService() {
		return affiliationService;
	}

	/**
	 * Set service class for affiliation
	 * 
	 * @param affiliationService
	 */
	public void setAffiliationService(AffiliationService affiliationService) {
		this.affiliationService = affiliationService;
	}

	/**
	 * Get affiliation id
	 * 
	 * @return
	 */
	public Long getAffiliationId() {
		return affiliationId;
	}

	/**
	 * Set affiliation id
	 * 
	 * @param affiliationId
	 */
	public void setAffiliationId(Long affiliationId) {
		this.affiliationId = affiliationId;
	}

	public DepartmentService getDepartmentService() {
		return departmentService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public UserIndexService getUserIndexService() {
		return userIndexService;
	}

	public void setUserIndexService(UserIndexService userIndexService) {
		this.userIndexService = userIndexService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

}
