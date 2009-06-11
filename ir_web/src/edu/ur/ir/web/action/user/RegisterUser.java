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
import org.springframework.security.BadCredentialsException;
import org.springframework.security.providers.AbstractAuthenticationToken;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.providers.ldap.LdapAuthenticator;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.repository.LicenseVersion;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.security.service.LdapAuthenticationToken;
import edu.ur.ir.security.service.UrLdapAuthenticationProvider;
import edu.ur.ir.user.Affiliation;
import edu.ur.ir.user.AffiliationService;
import edu.ur.ir.user.Department;
import edu.ur.ir.user.DepartmentService;
import edu.ur.ir.user.FileSharingException;
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
 * Action to add/register user
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class RegisterUser extends ActionSupport implements UserIdAware, Preparable {

	/** Eclipse generated Id */
	private static final long serialVersionUID = -7094483818911517181L;

	/**  Logger for add user action */
	private static final Logger log = Logger.getLogger(RegisterUser.class);

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
	
	/** Password check value  */
	private String passwordCheck;
	
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
	private Long[] departmentIds;
	
	/** Repository service for placing information in the repository */
	private RepositoryService repositoryService;
	
	/** Repository information  */
	private Repository repository;
	
	/** indicates the user has accepted the license */
	private boolean acceptLicense = false;
	
	/** id of the license the user has agreed to */
	private Long licenseId;
	
	/** Set the net id password */
	private String netIdPassword;
	
	/** Authenticator for ldap username/password */
	private LdapAuthenticator authenticator;
	
	/** Authentication provider for ldap */
	private UrLdapAuthenticationProvider ldapAuthProvider;
	

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
	 * Register with the system
	 *  
	 * @return
	 * @throws FileSharingException 
	 */
	public String registerUser() throws NoIndexFoundException, FileSharingException
	{
		log.debug("creating a user = " + irUser.getUsername());
		affiliations = affiliationService.getAllAffiliations();
		departments = departmentService.getAllDepartments();
		
		String returnVal = SUCCESS;
		
		IrUser myIrUser = 
			userService.getUser(irUser.getUsername());
		
		LicenseVersion license = repository.getDefaultLicense();
		
		boolean failure = false;
		
		
		if(irUser.getPassword() == null || irUser.getPassword().length() < 8)
		{
			failure = true;
			addFieldError("enterPassword", "The password must be at least 8 characters long");
		}
		
		/* very unlikely but if the license changes while the user was accepting then
		 * make them re-accept
		 */
		if(license  != null && !license.getId().equals(licenseId))
		{
			addFieldError("licenseChangeError", 
					"This license has changed please re-accept the new license");
			failure = true;
		}
		
		// make sure the user has accepted the license
		if( !acceptLicense )
		{
			addFieldError("licenseError", "You must accept the license to create an account");
			failure = true;
		}
		
		// check the passwords
		if( !irUser.getPassword().equals(this.passwordCheck))
		{
			addFieldError("passwordCheck", "The passwords do not match");
			failure = true;
		}
		
		if( irUser.getLdapUserName() != null  && !irUser.getLdapUserName().trim().equals(""))
		{
			log.debug("creating LDAP authentication token");
	
			// don't hit ldap unless a user has entered a username and password
			if( netIdPassword != null && !netIdPassword.trim().equals("")  )
			{
			    try
			    {
			        authenticator.authenticate(new UsernamePasswordAuthenticationToken(irUser.getLdapUserName(), this.netIdPassword));
			        IrUser ldapUser = userService.getUserByLdapUserName(irUser.getLdapUserName());
					// we have an interesting problem
					// user has authenticated correctly - but the user name already exists in the 
					// system - 
					if( ldapUser != null )
					{
						AbstractAuthenticationToken authRequest = new LdapAuthenticationToken(irUser.getLdapUserName(), this.netIdPassword);
						ldapAuthProvider.authenticate(authRequest);
						failure = true;
				    	addFieldError("netIdAlreadyExists", "The net id user name already exists - you may already have an account please contact the admistrator");
					}

			    }
			    catch(BadCredentialsException bce)
			    {
			    	 failure = true;
			    	 addFieldError("netIdPasswordFail", "The net id credentials could not be verified");
			    }
		     }
			 else
			 {
			      failure = true;
				  addFieldError("netIdPasswordEmpty", "The net id password must be entered for net id user name verification");
			 }
		}
		if( myIrUser != null )
		{
			addFieldError("userNameError", 
					"The user name already exists. User name : " + irUser.getUsername());
			failure = true;
		}
		
		IrUser emailExist = userService.getUserByEmail(defaultEmail.getEmail());
		if (emailExist != null) 
		{
			addFieldError("emailExistError", 
					"This Email already exists in the system. Email: " + defaultEmail.getEmail());
			failure = true;
		}
		
		if( failure )
		{
			returnVal = INPUT;
		}
		else
		{
			returnVal = createAccount(license);
		}

			    
		this.updateUserDepartments();

		
		return returnVal;
	}

	/** 
	 * Prepare the action - get user
	 */
	public void prepare() {
		log.debug("Prepare user Id =" + userId);
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		affiliations = affiliationService.getAllAffiliations();
		departments = departmentService.getAllDepartments();
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

	public Long[] getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(Long[] departmentIds) {
		this.departmentIds = departmentIds;
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

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public boolean getAcceptLicense() {
		return acceptLicense;
	}

	public void setAcceptLicense(boolean acceptedLicense) {
		this.acceptLicense = acceptedLicense;
	}

	public Long getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(Long licenseId) {
		this.licenseId = licenseId;
	}

	public String getPasswordCheck() {
		return passwordCheck;
	}

	public void setPasswordCheck(String passwordCheck) {
		this.passwordCheck = passwordCheck;
	}

	public UrLdapAuthenticationProvider getLdapAuthProvider() {
		return ldapAuthProvider;
	}

	public void setLdapAuthProvider(
			UrLdapAuthenticationProvider urLdapAuthenticationProvider) {
		this.ldapAuthProvider = urLdapAuthenticationProvider;
	}

	public void setNetIdPassword(String netIdPassword) {
		this.netIdPassword = netIdPassword;
	}
	
	private void updateUserDepartments()
	{
		if (departmentIds != null && departmentIds.length > 0) {
	    	for( int index = 0; index < departmentIds.length; index++)
	    	{
	    	    Department department = departmentService.getDepartment(departmentIds[index], false); 
	    	    irUser.addDepartment(department);
	    	}
	    }
	}

	public LdapAuthenticator getAuthenticator() {
		return authenticator;
	}

	public void setAuthenticator(LdapAuthenticator authenticator) {
		this.authenticator = authenticator;
	}
	
	private String createAccount(LicenseVersion license) throws NoIndexFoundException, FileSharingException
	{
		String returnVal = SUCCESS;
		// we do not use the created ir user but instead
		// will be creating a new user.  So the information is copied over
		// to save it.
		String firstName = irUser.getFirstName();
		String lastName = irUser.getLastName();
		String ldapUserName = irUser.getLastName();
		
				
		defaultEmail.setVerified(false);
		defaultEmail.setToken(TokenGenerator.getToken());
				
		irUser = userService.createUser(irUser.getPassword(), irUser.getUsername(), 
			    		defaultEmail);
		irUser.setAccountLocked(accountLocked);
		irUser.setFirstName(firstName);
		irUser.setLastName(lastName);
		irUser.setLdapUserName(ldapUserName);
		
		if( license != null )
		{
		    irUser.addAcceptedLicense(license);
		}
			    
		updateUserDepartments();

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
		
		/* Setup Email verification if the user chooses to add a different Email then
		 * the one to which Share invitation was sent.
		 */
		if ((token != null) && (token.length() > 0)) {
			log.debug("found token ");
			inviteInfo = inviteUserService.findInviteInfoByToken(token);
            log.debug(" checking emails inviteInfo email = " + inviteInfo.getEmail() + " default email = " + defaultEmail.getEmail());
			if (!inviteInfo.getEmail().equals(defaultEmail.getEmail())) {
				log.debug("NOT EQUAL adding default email " + defaultEmail );
				String emailToken = TokenGenerator.getToken();

				UserEmail anotherEmail = new UserEmail(inviteInfo.getEmail());
				anotherEmail.setVerified(true);
				irUser.addUserEmail(anotherEmail, true);

				irUser.getUserEmail(defaultEmail.getEmail()).setToken(emailToken);
				
				// send email With URL to verify email
				userService.sendEmailForEmailVerification(emailToken, defaultEmail.getEmail(), irUser.getUsername());
			} else {
				log.debug("setting default email verified ");
				irUser.getDefaultEmail().setVerified(true);
				userService.makeUserPersistent(irUser);
			}
			
			inviteUserService.shareFileForUserWithToken(irUser.getId(), token);
			
			returnVal = "successInvite";
		} 
		
		// add the user to the user index
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
						false);
		
		userIndexService.addToIndex(irUser, 
						new File( repository.getUserIndexFolder()) );
		
		return returnVal;
	}



}
