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
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.BadCredentialsException;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.repository.LicenseVersion;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.security.ExternalAuthenticationDetails;
import edu.ur.ir.security.ExternalAuthenticationProvider;
import edu.ur.ir.security.service.LdapAuthenticationToken;
import edu.ur.ir.user.Affiliation;
import edu.ur.ir.user.AffiliationService;
import edu.ur.ir.user.Department;
import edu.ur.ir.user.DepartmentService;
import edu.ur.ir.user.ExternalUserAccount;
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
import edu.ur.simple.type.AscendingNameComparator;
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
	
	/** Comparator for name based classes */
	private AscendingNameComparator nameComparator = new AscendingNameComparator();

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
	
	/** indicates the email already exists*/
	private boolean emailAlreadyExists = false;
	
	/** Default email */
	private UserEmail defaultEmail;
	
	/** Id of the affiliation selected */
	private Long affiliationId;

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
	
	/** Set the external account password */
	private String externalAccountPassword;
	
	/** indicates net id already exists and the net id validated against the password*/
	private boolean externalAccountAlreadyExists = false;
	



	/** External authentication provider */
	private ExternalAuthenticationProvider externalAuthenticationProvider;
	
	/** external user account name */
	private String externalUserAccountName;
	
	/**
	 * Execute method to initialize invite information
	 */
	public String execute() {

		if ((token != null) && (token.length() > 0)) {
			inviteInfo = inviteUserService.findInviteInfoByToken(token);
			defaultEmail = new UserEmail(inviteInfo.getEmail());
		}
		return SUCCESS;
	}

	/**
	 * Execute method to initialize invite information
	 */
	public String viewUserRegistration() {
		log.debug("viewUserRegistration called");
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

		String returnVal = SUCCESS;
	
		boolean failure = false;
		IrUser myIrUser = null;
		LicenseVersion license = repository.getDefaultLicense();
		
		if( irUser.getUsername() == null || irUser.getUsername().trim().equals(""))
		{
			failure = true;
			addFieldError("enterUserName", "You must enter a user name");
		}
		else
		{
		     myIrUser = 
			    userService.getUser(irUser.getUsername().trim());
		}
		
		
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
		
		failure = createExternalAccount();
		
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
			emailAlreadyExists = true;
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
		List<Affiliation> affiliations = affiliationService.getAllAffiliations();
		Collections.sort(affiliations, nameComparator);
		return affiliations ;
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
		List<Department> departments;
		departments = departmentService.getAllDepartments();
		Collections.sort(departments, nameComparator);
		return departments ;
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


	
	private String createAccount(LicenseVersion license) throws NoIndexFoundException, FileSharingException
	{
		String returnVal = SUCCESS;
		// we do not use the created ir user but instead
		// will be creating a new user.  So the information is copied over
		// to save it.
		String firstName = irUser.getFirstName().trim();
		String lastName = irUser.getLastName().trim();
		String phoneNumber = irUser.getPhoneNumber().trim();
		ExternalUserAccount externalAccount = irUser.getExternalAccount();
	
		defaultEmail.setVerifiedFalse(TokenGenerator.getToken() + defaultEmail.getEmail());
				
		irUser = userService.createUser(irUser.getPassword().trim(), irUser.getUsername().trim(), 
			    		defaultEmail);
		irUser.setAccountLocked(accountLocked);
		irUser.setFirstName(firstName);
		irUser.setLastName(lastName);
		if( externalAccount != null   && repositoryService.isExternalAuthenticationEnabled())
		{
		    irUser.createExternalUserAccount(externalAccount.getExternalUserAccountName(), externalAccount.getExternalAccountType());
		}
		irUser.setSelfRegistered(true);
		irUser.setPhoneNumber(phoneNumber);
		
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
		    	
		if (affiliation.isNeedsApproval()) 
		{
		    userService.sendAffiliationVerificationEmailForUser(irUser);
		    userService.sendPendingAffiliationEmailForUser(irUser);
		}
		else
		{
			/** Assign the role for the affiliation */
			if (affiliation.getAuthor()) 
			{
				irUser.addRole(roleService.getRole(IrRole.AUTHOR_ROLE));
			}
				
			if (affiliation.getResearcher()) 
			{
				irUser.addRole(roleService.getRole(IrRole.RESEARCHER_ROLE));
				// Create researcher object only if the user has no researcher object.
				// Sometimes user might have researcher object if the user is an admin.
				if (irUser.getResearcher() == null) 
				{
					irUser.createResearcher();
				}
			}
			userService.makeUserPersistent(irUser);
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
				anotherEmail.setVerifiedTrue();
				irUser.addUserEmail(anotherEmail, true);

				irUser.getUserEmail(defaultEmail.getEmail()).setVerifiedFalse(TokenGenerator.getToken() + defaultEmail.getEmail());
				
				// send email With URL to verify email
				userService.sendEmailForEmailVerification(emailToken, defaultEmail.getEmail(), irUser.getUsername());
			} else {
				log.debug("setting default email verified ");
				irUser.getDefaultEmail().setVerifiedTrue();
				userService.makeUserPersistent(irUser);
			}
			
			inviteUserService.shareFileForUserWithToken(irUser.getId(), token);
			inviteUserService.sharePendingFilesForEmail(irUser.getId(), inviteInfo.getEmail());
			
			returnVal = "successInvite";
		} 
		else
		{
			userService.sendAccountVerificationEmailForUser(defaultEmail.getToken(), defaultEmail.getEmail(), irUser.getUsername());
		}
		
		// add the user to the user index
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
						false);
		
		userIndexService.addToIndex(irUser, 
						new File( repository.getUserIndexFolder()) );
		
		return returnVal;
	}

	public boolean getEmailAlreadyExists() {
		return emailAlreadyExists;
	}

	public void setEmailAlreadyExists(boolean emailAlreadyExists) {
		this.emailAlreadyExists = emailAlreadyExists;
	}



	public ExternalAuthenticationProvider getExternalAuthenticationProvider() {
		return externalAuthenticationProvider;
	}

	public void setExternalAuthenticationProvider(
			ExternalAuthenticationProvider externalAuthenticationProvider) {
		this.externalAuthenticationProvider = externalAuthenticationProvider;
	}

	public String getExternalUserAccountName() {
		return externalUserAccountName;
	}

	public void setExternalUserAccountName(String externalUserAccountName) {
		this.externalUserAccountName = externalUserAccountName;
	}
	
	/**
	 * Try to create an external account.  
	 * @return - false if there is a failure when creating the external account.
	 */
	private boolean createExternalAccount()
	{
		boolean failure = false;
		if( repositoryService.isExternalAuthenticationEnabled())
		{
			if( externalUserAccountName != null  && !externalUserAccountName.trim().equals(""))
			{
				log.debug("creating LDAP authentication token");
		
				// don't hit ldap unless a user has entered a username and password
				if( externalAccountPassword != null && !externalAccountPassword.trim().equals("")  )
				{
				    try
				    {
				    	Authentication auth = externalAuthenticationProvider.authenticate(new LdapAuthenticationToken(externalUserAccountName, externalAccountPassword));
				        
				    	log.debug("Auth = " + auth);
				    	ExternalAuthenticationDetails externalDetails = null;
				    	
				        log.debug( " auth details = " + auth.getDetails());
				    	if( auth.getDetails() instanceof ExternalAuthenticationDetails)
				        {
				    		externalDetails  = (ExternalAuthenticationDetails)auth.getDetails();
				        }
				        
				    	if( externalDetails != null  && externalDetails.getType() != null)
				    	{
				    		externalDetails.getType();
				    		ExternalUserAccount externalAccount = userService.getByExternalUserNameAccountType(externalUserAccountName, externalDetails.getType());
				    		
						    // we have an interesting problem
						    // user has authenticated correctly - but the user name already exists in the 
						    // system  
						    if( externalAccount != null )
						    {
							    failure = true;
							    externalAccountAlreadyExists = true;
					    	    addFieldError("externalAccountExists", "The specified user name already has an account for system: " + externalAccount.getExternalAccountType().getName());
						    }
						    else
						    {
						    	irUser.createExternalUserAccount(externalUserAccountName, externalDetails.getType());
						    }
				    	}
				    	else
				    	{
				    		failure = true;
					    	addFieldError("externalAccountPasswordFail", "The external account credentials could not be verified");
				    	}

				    }
				    catch(BadCredentialsException bce)
				    {
				    	 failure = true;
				    	 addFieldError("externalAccountPasswordFail", "The external account credentials could not be verified");
				    }
			     }
				 else
				 {
				      failure = true;
					  addFieldError("externalAccountPasswordEmpty", "The external account password must be entered for external account verification");
				 }
			}
		}
		return failure;
	}

	public void setExternalAccountPassword(String externalAccountPassword) {
		this.externalAccountPassword = externalAccountPassword;
	}

	public boolean getExternalAccountAlreadyExists() {
		return externalAccountAlreadyExists;
	}

}
