package edu.ur.ir.web.action.user;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.ldap.authentication.LdapAuthenticator;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.institution.InstitutionalCollectionSubscription;
import edu.ur.ir.institution.InstitutionalCollectionSubscriptionService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.security.service.UrLdapAuthenticationProvider;
import edu.ur.ir.user.Affiliation;
import edu.ur.ir.user.AffiliationService;
import edu.ur.ir.user.Department;
import edu.ur.ir.user.DepartmentService;
import edu.ur.ir.user.InviteInfo;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.RoleService;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserIndexService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.simple.type.AscendingNameComparator;

/**
 * Class for updating a users account.
 * 
 * @author Nathan Sarr
 *
 */
public class UpdateUserAccount extends ActionSupport implements UserIdAware, Preparable {

	/** Eclipse generated Id */
	private static final long serialVersionUID = -7094483818911517181L;

	/**  Logger for add user action */
	private static final Logger log = Logger.getLogger(UpdateUserAccount.class);
	
	/** Comparator for name based classes */
	private AscendingNameComparator nameComparator = new AscendingNameComparator();

	/** user  */
	private IrUser irUser;
	
	/** Id of the user */
	private Long userId;
	
	/** Users phone number */
	private String phoneNumber;
	
	/** first name of the user */
	private String firstName;
	
	/** last name of the user */
	private String lastName;
	
	/** User service class */
	private UserService userService;
	
	/** Affiliation service class */
	private AffiliationService affiliationService;

	/** Service for accessing department information */
	private DepartmentService departmentService;
	
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
	
	/** Id of the affiliation selected */
	private Long affiliationId;

	/** Id of the department selected */
	private Long[] departmentIds;
	
	/** Repository service for placing information in the repository */
	private RepositoryService repositoryService;
	
	/** Service for dealing with institutional collection subscriptions */
	private InstitutionalCollectionSubscriptionService institutionalCollectionSubscriptionService;
	
	/** list of subscriptions for the user */
	private List<InstitutionalCollectionSubscription> subscriptions = new LinkedList<InstitutionalCollectionSubscription>();

	/** Repository information  */
	private Repository repository;
	
	/** indicates the user has accepted the license */
	private boolean acceptLicense = false;
	
	/** id of the license the user has agreed to */
	private Long licenseId;
	
	/** Authenticator for ldap username/password */
	private LdapAuthenticator authenticator;
	
	/** Authentication provider for ldap */
	private UrLdapAuthenticationProvider ldapAuthProvider;
	
	
	/** 
	 * Prepare the action - get user
	 */
	public void prepare() {
		log.debug("Prepare user Id =" + userId);
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
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
	 * View for editing my account
	 * 
	 * @return
	 */
	public String viewEditAccount() {
		irUser = userService.getUser(userId, false);
		subscriptions = institutionalCollectionSubscriptionService.getAllSubscriptionsForUser(irUser);
		return SUCCESS;
	}
	
	/**
	 * Saves the account
	 * 
	 * @return
	 */
	public String saveMyAccount() throws NoIndexFoundException
	{
		irUser = userService.getUser(userId, false);
		updateUserDepartments();
		irUser.setPhoneNumber(phoneNumber);
		irUser.setFirstName(firstName);
		irUser.setLastName(lastName);
		userService.makeUserPersistent(irUser);
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
				false);
	    userIndexService.updateIndex(irUser, 
				new File( repository.getUserIndexFolder()) );		
		
		return SUCCESS;
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

	public InstitutionalCollectionSubscriptionService getInstitutionalCollectionSubscriptionService() {
		return institutionalCollectionSubscriptionService;
	}

	public void setInstitutionalCollectionSubscriptionService(
			InstitutionalCollectionSubscriptionService institutionalCollectionSubscriptionService) {
		this.institutionalCollectionSubscriptionService = institutionalCollectionSubscriptionService;
	}

	public List<InstitutionalCollectionSubscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(
			List<InstitutionalCollectionSubscription> subscriptions) {
		this.subscriptions = subscriptions;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}