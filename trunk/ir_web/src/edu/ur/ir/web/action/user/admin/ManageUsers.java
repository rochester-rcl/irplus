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

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.ResearcherIndexService;
import edu.ur.ir.user.Affiliation;
import edu.ur.ir.user.AffiliationService;
import edu.ur.ir.user.Department;
import edu.ur.ir.user.DepartmentService;
import edu.ur.ir.user.FileSharingException;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.RoleService;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserIndexService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * Class for managing user information.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageUsers extends Pager implements Preparable{

	/**  Generated version id */
	private static final long serialVersionUID = -2827667086799910951L;
	
	/** user service */
	private UserService userService;
	
	/** Service for accessing role information */
	private RoleService roleService;

	/** Service for accessing department information */
	private DepartmentService departmentService;
	
	/**  Logger  */
	private static final Logger log = Logger.getLogger(ManageUsers.class);
	
	/** Set of language types for viewing the users */
	private Collection<IrUser> users;
	
	/** user to edit */
	private IrUser irUser;
	
	/** Default email */
	private UserEmail defaultEmail;
	
	/** Person service */
	private PersonService personService;
	
	/**  Indicates the user has been added*/
	private boolean added = false;
	
	/** Indicates the users have been deleted */
	private boolean deleted = false;
	
	/** Indicates the account has been locked */
	private boolean accountLocked = false;
	
	/** Indicates the account has been locked */
	private boolean accountExpired = false;
	
	/** Indicates the account has been locked */
	private boolean credentialsExpired = false;
	
	/** id of the user */
	private Long id;
	
	/** indicates if the user is an admin */
	private boolean adminRole = false;
	
	/** Indicates if the user is a general user */
	private boolean userRole = false;
	
	/** Indicates if the user is a researcher */
	private boolean researcherRole = false;
	
	/** Indicates if the user is an author */
	private boolean authorRole = false;

	/** Indicates if the user is an author */
	private boolean collectionAdminRole = false;

    /** Phone number of the user */
	private String phoneNumber;
    
    /** Password of the user */
	private String password;
	
	/** Indicated whether to email new password details to user */
	private boolean emailPassword;

	/** Message from the admin to the user for changing password */
	private String emailMessage;

	/** List of all affiliations */
	private List<Affiliation> affiliations;

	/** Id of the affiliation selected */
	private Long affiliationId;
	
	/** Affiliation service class */
	private AffiliationService affiliationService;

	/** List of all departments */
	private List<Department> departments;

	/** Id of the department selected */
	private Long departmentId;

	/** Repository service for placing information in the repository */
	private RepositoryService repositoryService;
	
	/** Service for indexing users */
	private UserIndexService userIndexService;
	
	/** Service for inviting users */
	private InviteUserService inviteUserService;
	
	/** Researcher Index Service */
	private ResearcherIndexService researcherIndexService;
	
	/** Size of file system */
	private Long fileSystemSize;
	
	/** Id of authority name */
	private Long authorityId;
	
	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** name of the element to sort on 
	 *   this is for incoming requests */
	private String sortElement = "lastName";

	/** Total number of users */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** message sent to the user */
	private String message;
	


	/** Default constructor */
	public  ManageUsers() 
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
     * Execute method
     */
    public String execute(){
    	
		affiliations = affiliationService.getAllAffiliations();
		departments = departmentService.getAllDepartments();

    	return SUCCESS;
    }
    
	/**
	 * Method to create a new user type.
	 * 
	 * Create a new user type
	 * @throws NoIndexFoundException 
	 */
	public String create() throws NoIndexFoundException
	{
		log.debug("creating a user username = " + irUser.getUsername());
		added = false;
		IrUser myIrUser = 
			userService.getUser(irUser.getUsername());
		
		if( myIrUser != null )
		{
			addFieldError("userAlreadyExists", 
					"This User name already exist : " + irUser.getUsername());
			// Reloads the affiliation & department drop downs
			execute();
			
	        return "added";
		}
		
		IrUser emailExist = userService.getUserByEmail(defaultEmail.getEmail());
		if(emailExist != null)
		{
			addFieldError("emailExistError", 
					"This Email already exists in the system. Email: " + defaultEmail.getEmail());
			
            execute();
			
	        return "added";
		}
		
		if ((!isAdminRole()) && (!isUserRole()) )
		{
			addFieldError("rolesNotSelectedError", 
			"Please select a role for this user." );
             execute();
			
	        return "added";
		}
		
		if( irUser.getLdapUserName() != null && !irUser.getLdapUserName().trim().equals(""))
		{
		
		    myIrUser = userService.getUserByLdapUserName(irUser.getLdapUserName());
		
		    if( myIrUser != null )
		    {
			    addFieldError("ldapNameExists", 
					    "This Ldap User name already exist : " + irUser.getLdapUserName());
			    // Reloads the affiliation & department drop downs
			    execute();
			
	            return "added";
		    }
		}
		
		
		
		String password = irUser.getPassword();
		String firstName = irUser.getFirstName();
		String lastName = irUser.getLastName();
		String ldapUserName = irUser.getLdapUserName();
					
		defaultEmail.setVerified(true);
		irUser = userService.createUser(irUser.getPassword(), irUser.getUsername(), defaultEmail);

		irUser.setAccountExpired(accountExpired);
		irUser.setCredentialsExpired(credentialsExpired);
		irUser.setAccountLocked(accountLocked);
		irUser.setPhoneNumber(phoneNumber);
		irUser.setFirstName(firstName);
		irUser.setLastName(lastName);
		irUser.setLdapUserName(ldapUserName);

		setDepartment();
		updateAffilation();
		updateRoles();
				    
		// Force the user to change password after login
		irUser.setPasswordChangeRequired(true);
				    
		userService.makeUserPersistent(irUser); 
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
							false);
		userIndexService.addToIndex(irUser, 
							new File( repository.getUserIndexFolder()) );
		
		if (irUser.getResearcher() != null) {
			
			researcherIndexService.addToIndex(irUser.getResearcher(), 
					new File(repository.getResearcherIndexFolder()) );
		}
				    
		if (emailPassword) {
		    userService.sendAccountCreationEmailToUser(irUser, password);
		}

		try {
		    // Share files -  If there are any invitations sent to this email address 
			inviteUserService.sharePendingFilesForEmail(irUser.getId(), defaultEmail.getEmail());
		} catch (FileSharingException e) {
		    log.error("File cannot be shared with themselves" + e.getMessage());
		}
					
		added = true;
				    
		
		// Reloads the affiliation & department drop downs
		execute();
		
        return "added";
	}
	
	/**
	 * Method to update an existing user 
	 * 
	 * @return
	 * @throws NoIndexFoundException 
	 */
	public String update() throws NoIndexFoundException
	{
		log.debug("updating user id = " + id);
		
		added = false;
		
		IrUser other = 
			userService.getUser(irUser.getUsername());
		
		// if the user is found and the id's are not the same
		// then they are trying to rename it to the same name.
		if(other != null && !other.getId().equals(irUser.getId()))
		{
			addFieldError("userAlreadyExists", 
					"This User name already exist. " + irUser.getUsername());
			return "updated";
		}
		
		if( irUser.getLdapUserName() != null && !irUser.getLdapUserName().trim().equals(""))
		{
		    other = 
			    userService.getUserByLdapUserName(irUser.getLdapUserName());
		    if(other != null && !other.getId().equals(irUser.getId()))
		    {
			    addFieldError("ldapNameExists", 
					    "The net-id User name already exist. " + irUser.getLdapUserName());
			    return "updated";
		    }
		}
		
		if ((!isAdminRole()) && (!isUserRole()) ) 
		{
			addFieldError("rolesNotSelectedError", 
			"Please select a role for this user." );
			 return "updated";
		}
		
		irUser.setPhoneNumber(phoneNumber);
		irUser.setAccountExpired(accountExpired);
		irUser.setCredentialsExpired(credentialsExpired);
		irUser.setAccountLocked(accountLocked);
				
		setDepartment();
			    	
		//update the affilation for the user
		updateAffilation();
			    
		// roles can override affilation settings
		updateRoles();
				
		userService.makeUserPersistent(irUser);
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
						false);
		userIndexService.updateIndex(irUser, 
						new File( repository.getUserIndexFolder()) );
		
		if (irUser.getResearcher() != null) {
			
			researcherIndexService.updateIndex(irUser.getResearcher(), 
					new File(repository.getResearcherIndexFolder()) );
		}

		added = true;
        return "updated";
	}
	
	
	/**
	 * Get all roles.
	 * 
	 * @return
	 */
	public String viewEditUser()
	{
			
		if( id != null)
		{
			irUser = userService.getUser(id, false);
			defaultEmail = irUser.getDefaultEmail();
			affiliations = affiliationService.getAllAffiliations();
			departments = departmentService.getAllDepartments();
			
			fileSystemSize = repositoryService.getFileSystemSizeForUser(irUser);

		}
		
		return SUCCESS;
	}

	/**
	 * Removes the selected users.
	 * 
	 * @return
	 * @throws NoIndexFoundException 
	 * @throws UserHasPublishedDeleteException
	 * @throws UserDeletedPublicationException 
	 */
	public String delete() 
	{
		log.debug("Delete users called");
		
		message = "The following user could not be deleted because they have published into the system :";
		
		deleted = true;
		if( id != null ) 
		{

            IrUser user = userService.getUser(id, false);
 			try 
 			{
			    userService.deleteUser(user);
			} 
 			catch (UserHasPublishedDeleteException e) 
 			{
 			    message = message + " " + user.getUsername() + ", ";
 			    deleted = false;
				log.error("user has published", e);
					
			} 
 			catch (UserDeletedPublicationException e) 
 			{
 			    message = message + " " + user.getUsername() + ", ";
 			    deleted = false;
				log.error("user has published", e);
			}
 			
 			Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
 		
			userIndexService.deleteFromIndex(user, 
							new File( repository.getUserIndexFolder()) );
		}

		
		return "deleted";
	}
 
	/**
	 * Reset password and send email to the user
	 * 
	 */
	public String changePassword() {
		
		irUser = userService.getUser(id, false);
		
		userService.updatePassword(password, irUser);
		
		irUser.setPasswordChangeRequired(true);
		userService.makeUserPersistent(irUser);
		
		if (emailPassword) {
			userService.emailNewPassword(irUser, password, emailMessage);
		}

		return SUCCESS;
	}

	/**
	 * Logs in the user as the selected user
	 * 
	 * @return Success 
	 */
	public String loginAsUser() {
		
		irUser = userService.getUser(id, false);
		
		userService.authenticateUser(irUser, irUser.getPassword(), irUser.getRoles());
				
		return SUCCESS;
	}
	
	/**
	 * Get the users table data.
	 * 
	 * @return
	 */
	public String viewUsers()
	{
		log.debug("RowStart = " + rowStart
	    		+ "   numberOfResultsToShow=" + numberOfResultsToShow + "   sortElement="  + sortElement + "   orderType =" + sortType);
		rowEnd = rowStart + numberOfResultsToShow;
	    
		OrderType orderType = OrderType.getOrderType(sortType);
	    users = userService.getUsers(rowStart, 
	    		numberOfResultsToShow, sortElement, orderType);
	    totalHits = userService.getUserCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;

	}

	/**
	 * Get the user type service.
	 * 
	 * @return
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Set the user type service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * List of users for display.
	 * 
	 * @return
	 */
	public Collection<IrUser> getUsers() {
		return users;
	}

	/**
	 * Set the list of users.
	 * 
	 * @param users
	 */
	public void setUsers(Collection<IrUser> users) {
		this.users = users;
	}

	public boolean isAdded() {
		return added;
	}

	public void setAdded(boolean added) {
		this.added = added;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void prepare() throws Exception {
				
		if( id != null)
		{
			irUser = userService.getUser(id, false);
			defaultEmail = irUser.getDefaultEmail();
			affiliations = affiliationService.getAllAffiliations();
			departments = departmentService.getAllDepartments();
		}
		
	}

	public IrUser getIrUser() {
		return irUser;
	}

	public void setIrUser(IrUser irUser) {
		this.irUser = irUser;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public UserEmail getDefaultEmail() {
		return defaultEmail;
	}

	public void setDefaultEmail(UserEmail defaultEmail) {
		this.defaultEmail = defaultEmail;
	}

	public boolean isAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public boolean isCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(boolean emailPassword) {
		this.emailPassword = emailPassword;
	}

	public RoleService getRoleService() {
		return roleService;
	}

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

	public String getEmailMessage() {
		return emailMessage;
	}

	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
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

	public boolean isAdminRole() {
		return adminRole;
	}

	public void setAdminRole(boolean adminRole) {
		this.adminRole = adminRole;
	}

	public boolean isUserRole() {
		return userRole;
	}

	public void setUserRole(boolean userRole) {
		this.userRole = userRole;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public UserIndexService getUserIndexService() {
		return userIndexService;
	}

	public void setUserIndexService(UserIndexService userIndexService) {
		this.userIndexService = userIndexService;
	}

	public boolean isResearcherRole() {
		return researcherRole;
	}

	public void setResearcherRole(boolean researcherRole) {
		this.researcherRole = researcherRole;
	}

	public boolean isAuthorRole() {
		return authorRole;
	}

	public void setAuthorRole(boolean authorRole) {
		this.authorRole = authorRole;
	}
	
	/**
	 * Updates the affilation for the user
	 */
	private void updateAffilation() throws NoIndexFoundException
	{
		Affiliation affiliation = affiliationService.getAffiliation(affiliationId, false); 

		
		if (irUser.getAffiliation() == null || 
				!irUser.getAffiliation().equals(affiliation)) {
			
	    	/** Assign the role for the affiliation */
			if (affiliation.getAuthor()) {
				irUser.addRole(roleService.getRole(IrRole.AUTHOR_ROLE));
				authorRole = true;
			} else {
				irUser.removeRole(IrRole.AUTHOR_ROLE);
			}
				
			if (affiliation.getResearcher()) {
				irUser.addRole(roleService.getRole(IrRole.RESEARCHER_ROLE));
				researcherRole = true;
				// Create researcher object only if the user has no researcher object.
				// Sometimes user might have researcher object if the user is a admin 
				if (irUser.getResearcher() == null) {
					irUser.createResearcher();
				}
			} else {
				irUser.removeRole(IrRole.RESEARCHER_ROLE);
			}
    	}
    	
    	irUser.setAffiliation(affiliation);
    	irUser.setAffiliationApproved(true);

	}
	
	/**
	 * Updates a user roles
	 */
	private void updateRoles() throws NoIndexFoundException
	{
		if (isAdminRole()) {
			irUser.addRole(roleService.getRole(IrRole.ADMIN_ROLE));

			// Create researcher object only if the user has no researcher object.
			// Sometimes user might have researcher object if the user is a researcher
			if (irUser.getResearcher() == null) {
				irUser.createResearcher();
			}
		} else {
			irUser.removeRole(IrRole.ADMIN_ROLE);
		}
		
		if (isUserRole()) {
			irUser.addRole(roleService.getRole(IrRole.USER_ROLE));
		} else {
			irUser.removeRole(IrRole.USER_ROLE);
		}
		
		if( isResearcherRole())
		{
			irUser.addRole(roleService.getRole(IrRole.RESEARCHER_ROLE));
			if (irUser.getResearcher() == null) {
				irUser.createResearcher();
			}
		}
		else
		{
			irUser.removeRole(IrRole.RESEARCHER_ROLE);
		}
			
		
		if( isAuthorRole())
		{
			irUser.addRole(roleService.getRole(IrRole.AUTHOR_ROLE));
		}
		else
		{
			irUser.removeRole(IrRole.AUTHOR_ROLE);
		}
		
		if( isCollectionAdminRole())
		{
			irUser.addRole(roleService.getRole(IrRole.COLLECTION_ADMIN_ROLE));
		}
		else
		{
			irUser.removeRole(IrRole.COLLECTION_ADMIN_ROLE);
		}

	}
	
	private void setDepartment()
	{
		if (departmentId != 0) {
	    	Department department = departmentService.getDepartment(departmentId, false); 
	    	irUser.setDepartment(department);
	    }	
	}

	public boolean isCollectionAdminRole() {
		return collectionAdminRole;
	}

	public void setCollectionAdminRole(boolean collectionAdminRole) {
		this.collectionAdminRole = collectionAdminRole;
	}

	public void setInviteUserService(InviteUserService inviteUserService) {
		this.inviteUserService = inviteUserService;
	}

	public Long getFileSystemSize() {
		return fileSystemSize;
	}
	
	/**
	 * Remove user's authoritative name
	 * 
	 * @return
	 */
	public String removeAuthoritativeName() {
		
		irUser = userService.getUser(id, false);
		
		irUser.setPersonNameAuthority(null);
		
		userService.makeUserPersistent(irUser);
		
		return SUCCESS;
	}

	/**
	 * Add user's authoritative name
	 * 
	 * @return
	 */
	public String addAuthoritativeName() {
		
		irUser = userService.getUser(id, false);
		
		PersonNameAuthority p = personService.getAuthority(authorityId, false);
		irUser.setPersonNameAuthority(p);
		
		userService.makeUserPersistent(irUser);
		
		return SUCCESS;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public void setAuthorityId(Long authorityId) {
		this.authorityId = authorityId;
	}

	/**
	 * Get total number of researchers
	 * 
	 * @see edu.ur.ir.web.table.Pager#getTotalHits()
	 */
	public int getTotalHits() {		
		return totalHits;
	}


	public int getRowEnd() {
		return rowEnd;
	}


	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}


	public String getSortType() {
		return sortType;
	}


	public void setSortType(String sortType) {
		this.sortType = sortType;
	}


	public String getSortElement() {
		return sortElement;
	}


	public void setSortElement(String sortElement) {
		this.sortElement = sortElement;
	}

	public void setResearcherIndexService(
			ResearcherIndexService researcherIndexService) {
		this.researcherIndexService = researcherIndexService;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



}
