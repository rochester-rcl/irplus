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
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.groupspace.GroupWorkspaceInviteService;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.ResearcherIndexService;
import edu.ur.ir.security.AuthenticateUserOverrideService;
import edu.ur.ir.user.Affiliation;
import edu.ur.ir.user.AffiliationService;
import edu.ur.ir.user.Department;
import edu.ur.ir.user.DepartmentService;
import edu.ur.ir.user.ExternalAccountType;
import edu.ur.ir.user.ExternalAccountTypeService;
import edu.ur.ir.user.ExternalUserAccount;
import edu.ur.ir.user.FileSharingException;
import edu.ur.ir.user.InviteUserService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.RoleService;
import edu.ur.ir.user.UnVerifiedEmailException;
import edu.ur.ir.user.UserDeletedPublicationException;
import edu.ur.ir.user.UserEmail;
import edu.ur.ir.user.UserGroupService;
import edu.ur.ir.user.UserHasPublishedDeleteException;
import edu.ur.ir.user.UserIndexService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;
import edu.ur.simple.type.AscendingNameComparator;

/**
 * Class for managing user information.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageUsers extends Pager implements Preparable, UserIdAware {

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
	
	/** id of the admin managing the users */
	private Long adminUserId;
	
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
	
	/** Indicates if the user is an author */
	private boolean collaboratorRole = false;
	
	/** Indicates if the user can import data */
	private boolean importerRole = false;

    /** Phone number of the user */
	private String phoneNumber;
    
    /** Password of the user */
	private String password;
	
	/** Indicated whether to email new password details to user */
	private boolean emailPassword;

	/** Message from the admin to the user for changing password */
	private String emailMessage;

	/** Id of the affiliation selected */
	private Long affiliationId = -1l;
	
	/** Affiliation service class */
	private AffiliationService affiliationService;

	/** Id of the department selected */
	private Long departmentIds[];

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
	
	/** Service to allow an administrator to login as a different user */
	private AuthenticateUserOverrideService authenticateUserOverrideService;
	
	/** process for setting up personal workspace information to be indexed */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;
	
	/** service for accessing index processing types */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/** Comparator for name based classes */
	private AscendingNameComparator nameComparator = new AscendingNameComparator();
	
	/** service for dealing with external account type information */
	private ExternalAccountTypeService externalAccountTypeService;
	
	/** Type of external account for the user */
	private long externalAccountTypeId = 0;
	
	/** the external account user name */
	private String externalAccountUserName;
	
	private Long roleId = -1l;
	
	/** service to view user group information */
	private UserGroupService userGroupService;
	
	/** list of groups the user belongs to */
	private List<IrUserGroup> userGroups;
	
	/* service to deal with inviting users to group workspaces */
	private GroupWorkspaceInviteService groupWorkspaceInviteService;
	



	/**
	 * Set the user group service.
	 * 
	 * @param userGroupService
	 */
	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

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
    	
    	return SUCCESS;
    }
    
	/**
	 * Method to create a new user.
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
			
	        return "added";
		}
		
		IrUser emailExist = userService.getUserByEmail(defaultEmail.getEmail());
		if(emailExist != null)
		{
			addFieldError("emailExistError", 
					"This Email already exists in the system. Email: " + defaultEmail.getEmail());
			
	        return "added";
		}
		

		if( !externalAccountIsOk() )
		{
			return "added";
		}
		if ((!isAdminRole()) && (!isUserRole()) )
		{
			addFieldError("rolesNotSelectedError", 
			"Please select a role for this user." );
             return "added";
		}
		
		String password = irUser.getPassword();
		String firstName = irUser.getFirstName();
		String lastName = irUser.getLastName();
					
		defaultEmail.setVerifiedTrue();
		irUser = userService.createUser(irUser.getPassword(), irUser.getUsername(), defaultEmail);

		irUser.setAccountExpired(accountExpired);
		irUser.setCredentialsExpired(credentialsExpired);
		irUser.setAccountLocked(accountLocked);
		irUser.setPhoneNumber(phoneNumber);
		irUser.setFirstName(firstName);
		irUser.setLastName(lastName);
		
		if( externalAccountTypeId > 0 )
		{
			ExternalAccountType externalAccountType = externalAccountTypeService.get(externalAccountTypeId, false);
			irUser.createExternalUserAccount(this.externalAccountUserName, externalAccountType);
		}

		setDepartments();
		updateAffiliation();
		updateRoles();
				    
		// Force the user to change password after login
		irUser.setPasswordChangeRequired(true);
				    
		userService.makeUserPersistent(irUser); 
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
							false);
		userIndexService.addToIndex(irUser, 
							new File( repository.getUserIndexFolder()) );
		
		
		if (emailPassword) {
		    userService.sendAccountCreationEmailToUser(irUser, password);
		}

		try {
		    // Share files -  If there are any invitations sent to this email address 
			groupWorkspaceInviteService.addUserToGroupsForEmail(defaultEmail.getEmail());
			inviteUserService.sharePendingFilesForEmail(irUser.getId(), defaultEmail.getEmail());
		
		} 
		catch (FileSharingException e) 
		{
		    log.error("File cannot be shared with themselves" + e.getMessage());
		}
		catch(UnVerifiedEmailException uvef)
		{
			log.error("email not verified " + defaultEmail.getEmail() + " " + uvef.getMessage());
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
		

		if( !externalAccountIsOk() )
		{
			return "updated";
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
		
		ExternalUserAccount externalUserAccount = irUser.getExternalAccount();
		
		// if the id passed in is greater than zero this means
		// the xternal account type was updated or current - if
		// the id is less then zero it either does not exist or was
		// deleted
		if( externalAccountTypeId > 0 )
		{
			ExternalAccountType externalAccountType = externalAccountTypeService.get(externalAccountTypeId, false);
			
			if( externalUserAccount == null )
			{
			    
			    irUser.createExternalUserAccount(externalAccountUserName, externalAccountType);
			}
			else
			{
				externalUserAccount.setExternalAccountType(externalAccountType);
				externalUserAccount.setExternalUserAccountName(externalAccountUserName);
			}
		}
		else if(externalUserAccount != null)
		{
			ExternalUserAccount externalAccount = irUser.getExternalAccount();
			irUser.deleteExternalUserAccount();
			userService.delete(externalAccount);
		}
				
		setDepartments();
			    	
		//update the affiliation for the user
		updateAffiliation();
		updateRoles();
				
		userService.makeUserPersistent(irUser);
		Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID,
						false);
		userIndexService.updateIndex(irUser, 
						new File( repository.getUserIndexFolder()) );
		
		if (irUser.getResearcher() != null) {
			
			if( irUser.getResearcher().isPublic() )
			{
			    researcherIndexService.updateIndex(irUser.getResearcher(), 
					    new File(repository.getResearcherIndexFolder()) );
			}
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
			fileSystemSize = repositoryService.getFileSystemSizeForUser(irUser);
			userGroups = userGroupService.getUserGroupsForUser(id);
		}
		
		return SUCCESS;
	}

	/**
	 * Removes the selected users.
	 * 
	 * @return
	 * @throws IOException 
	 * @throws NoIndexFoundException 
	 * @throws UserHasPublishedDeleteException
	 * @throws UserDeletedPublicationException 
	 */
	public String delete() throws IOException 
	{
		log.debug("Delete users called");
		
		message = "The following user could not be deleted because they have published into the system :";
		
		deleted = true;
		if( irUser != null ) 
		{

            IrUser user = userService.getUser(id, false);
            IrUser admin = userService.getUser(adminUserId, false);
            
            if( !admin.hasRole(IrRole.ADMIN_ROLE))
            {
            	return "accessDenied";
            }
            
 			try 
 			{
			    userService.deleteUser(user, admin);
			    Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
			    userIndexService.deleteFromIndex(user.getId(), 
						new File( repository.getUserIndexFolder()) );
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
 		}
		return "deleted";
	}
 
	/**
	 * Reset password and send email to the user
	 * 
	 */
	public String changePassword() {
		
		IrUser admin = userService.getUser(adminUserId, false);
        
        if( !admin.hasRole(IrRole.ADMIN_ROLE))
        {
        	return "accessDenied";
        }
		
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
	public String loginAsUser() 
	{
		IrUser admin = userService.getUser(adminUserId, false);
        
        if( !admin.hasRole(IrRole.ADMIN_ROLE))
        {
        	return "accessDenied";
        }
		log.debug("user id = " + id);
		irUser = userService.getUser(id, false);
		log.debug("User = " + irUser);
		if( irUser != null )
		{
		    authenticateUserOverrideService.authenticateUser(irUser);
		}
		return SUCCESS;
	}
	
	/**
	 * Get the users table data.
	 * 
	 * @return
	 */
	public String viewUsers()
	{
		if(log.isDebugEnabled())
		{
		    log.debug("RowStart = " + rowStart
	    		+ " numberOfResultsToShow=" + numberOfResultsToShow
	    		+ " sortElement="  + sortElement + "   orderType =" + sortType
	    		+ " affilationId = " + affiliationId
	    		+ " roleId = " + roleId);
		   
		    log.debug("admin user id = " + adminUserId);
		}
		
		IrUser admin = userService.getUser(adminUserId, false);
        
        if( !admin.hasRole(IrRole.ADMIN_ROLE))
        {
        	return "accessDenied";
        }
		rowEnd = rowStart + numberOfResultsToShow;
	    
		OrderType orderType = OrderType.getOrderType(sortType);
		
		
	    if( sortElement.equalsIgnoreCase("lastName"))
	    {
	    	if(affiliationId == -1l && roleId == -1l  )
	    	{
	            users = userService.getUsersByLastNameOrder(rowStart, numberOfResultsToShow, orderType);
	            totalHits = userService.getUserCount().intValue();
	    	}
	    	else if(affiliationId > 0 && roleId == -1)
	    	{
	    		users = userService.getUsersByAffiliationFullNameOrder(affiliationId, rowStart, numberOfResultsToShow, orderType);
	    		totalHits = userService.getUserByAffiliationCount(affiliationId).intValue();
	    	}
	    	else if(affiliationId == -1 && roleId > 0 )
	    	{
	    		users = userService.getUsersByRoleFullNameOrder(roleId, rowStart, numberOfResultsToShow, orderType);
	    		totalHits = userService.getUserByRoleCount(roleId).intValue();
	    	}
	    	else if(affiliationId > 0 && roleId > 0 )
	    	{
	    		users = userService.getUsersByRoleAffiliationFullNameOrder(roleId, affiliationId, rowStart, numberOfResultsToShow, orderType);
	    	    totalHits = userService.getUserByRoleAffiliationCount(roleId, affiliationId).intValue();
	    	}
	    
	    } 
	    else if ( sortElement.equalsIgnoreCase("username") )
	    {
	    	if(affiliationId == -1l && roleId == -1l  )
	    	{
	    		users = userService.getUsersByUsernameOrder(rowStart, numberOfResultsToShow, orderType);
		        totalHits = userService.getUserCount().intValue();
	    	}
	    	else if(affiliationId > 0 && roleId == -1)
	    	{
	    		users = userService.getUsersByAffiliationUsernameOrder(affiliationId, rowStart, numberOfResultsToShow, orderType);
	    	    totalHits = userService.getUserByAffiliationCount(affiliationId).intValue();
	    	}
	    	else if(affiliationId == -1 && roleId > 0 )
	    	{
	    		users = userService.getUsersByRoleUsernameOrder(roleId, rowStart, numberOfResultsToShow, orderType);
	    		totalHits = userService.getUserByRoleCount(roleId).intValue();
	    	}
	    	else if(affiliationId > 0 && roleId > 0 )
	    	{
	    		users = userService.getUsersByRoleAffiliationUsernameOrder(roleId, affiliationId, totalHits, numberOfResultsToShow, orderType);
	    	    totalHits = userService.getUserByRoleAffiliationCount(roleId, affiliationId).intValue();
	    	}
	    } 
	    else if ( sortElement.equalsIgnoreCase("email") )
	    {
	    	if(affiliationId == -1l && roleId == -1l  )
	    	{
	    		users = userService.getUsersByEmailOrder(rowStart, numberOfResultsToShow, orderType);
	            totalHits = userService.getUserCount().intValue();
	    	}
	    	else if(affiliationId > 0 && roleId == -1)
	    	{
	    		users = userService.getUsersByAffiliationEmailOrder(affiliationId, rowStart, numberOfResultsToShow, orderType);
	    		totalHits = userService.getUserByAffiliationCount(affiliationId).intValue();
	    	}
	    	else if(affiliationId == -1 && roleId > 0 )
	    	{
	    		users = userService.getUsersByRoleEmailOrder(roleId, rowStart, numberOfResultsToShow, orderType);
	    		totalHits = userService.getUserByRoleCount(roleId).intValue();
	    	}
	    	else if(affiliationId > 0 && roleId > 0)
	    	{
	    		users = userService.getUsersByRoleAffiliationEmailOrder(roleId, affiliationId, rowStart, numberOfResultsToShow, orderType);	    		
	    		totalHits = userService.getUserByRoleAffiliationCount(roleId, affiliationId).intValue();
	    	}
	    }		
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;

	}
	
	/**
	 * Re Index a users workspace.
	 * 
	 * @return
	 * @throws IOException 
	 */
	public String reIndexUserWorkspace() throws IOException
	{
		IrUser admin = userService.getUser(adminUserId, false);
        
        if( !admin.hasRole(IrRole.ADMIN_ROLE))
        {
        	return "accessDenied";
        }
		log.debug("user id = " + id);
		viewEditUser();
		irUser.setReBuildUserWorkspaceIndex(true);
		userService.makeUserPersistent(irUser);
		userWorkspaceIndexProcessingRecordService.reIndexAllUserItems(irUser, 
    			    indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		return SUCCESS;
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
		}
		
	}

	public IrUser getIrUser() {
		return irUser;
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
	 * Get all roles
	 * 
	 * @return
	 */
	public List<IrRole> getRoles() {
		List<IrRole> roles = roleService.getAllRoles();
		log.debug(" rolses size = " + roles.size());
		Collections.sort(roles, nameComparator);
		return roles ;
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

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public List<Department> getDepartments() {
		List<Department> departments;
		departments = departmentService.getAllDepartments();
		Collections.sort(departments, nameComparator);
		return departments ;
	}
	
	public List<ExternalAccountType> getExternalAccountTypes() {
		List<ExternalAccountType> externalAccountTypes;
		externalAccountTypes = externalAccountTypeService.getAll();
		Collections.sort(externalAccountTypes, nameComparator);
		return  externalAccountTypes;
	}

	public Long[] getDepartmentId() {
		return departmentIds;
	}

	public void setDepartmentIds(Long[] departmentIds) {
		this.departmentIds = departmentIds;
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

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
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
	
	public void setCollaboratorRole(boolean collaboratorRole) {
		this.collaboratorRole = collaboratorRole;
	}
	
	public void setImporterRole(boolean importerRole) {
		this.importerRole = importerRole;
	}

	public boolean isAuthorRole() {
		return authorRole;
	}

	public void setAuthorRole(boolean authorRole) {
		this.authorRole = authorRole;
	}
	
	/**
	 * Make sure the external account is set up correctly
	 * @return
	 */
	private boolean externalAccountIsOk()
	{
		boolean ok = true;
		
		// same as null
		if(externalAccountUserName != null && externalAccountUserName.trim().equals(""))
		{
			externalAccountUserName = null;
		}
		
		if( externalAccountTypeId > 0 && externalAccountUserName == null )
		{
			ok = false;
			addFieldError("externalAccountError", 
					"The user must have both an external account type and user name - clear out both fields if it should not exist1");
			
	        
		}
		
		if( externalAccountTypeId <= 0 && externalAccountUserName != null)
		{
			addFieldError("externalAccountError", 
					"The user must have both an external account type and user name - clear out both fields if it should not exist2");
			
	        ok = false;
		}
		return ok;
		
	}
	
	/**
	 * Updates the affiliation for the user
	 */
	private void updateAffiliation() throws NoIndexFoundException
	{
		Affiliation affiliation = affiliationService.getAffiliation(affiliationId, false); 

		// affilation changed
		if (irUser.getAffiliation() == null ||  !irUser.getAffiliation().equals(affiliation)) 
		{		
			// send notification to user about affiliation change
			userService.sendAffiliationConfirmationEmail(irUser, affiliation);
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
		
		if( collaboratorRole )
		{
			irUser.addRole(roleService.getRole(IrRole.COLLABORATOR_ROLE));
		}
		else
		{
			irUser.removeRole(roleService.getRole(IrRole.COLLABORATOR_ROLE));
		}
		
		if( importerRole )
		{
			irUser.addRole(roleService.getRole(IrRole.IMPORTER_ROLE));
		}
		else
		{
			irUser.removeRole(roleService.getRole(IrRole.IMPORTER_ROLE));
		}

	}
	
	private void setDepartments()
	{
		irUser.removeAllDepartments();
		
		if( departmentIds != null && departmentIds.length > 0)
		{
			for( int index = 0; index < departmentIds.length; index++)
			{
			    Department department = departmentService.getDepartment(departmentIds[index], false); 
			    if( department != null )
			    {
			        irUser.addDepartment(department);
			    }
			}
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

	public void setAuthenticateUserOverrideService(
			AuthenticateUserOverrideService authenticateUserOverrideService) {
		this.authenticateUserOverrideService = authenticateUserOverrideService;
	}

	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}

	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

	public void injectUserId(Long userId) {
		adminUserId = userId;
	}

	public void setExternalAccountTypeService(
			ExternalAccountTypeService externalAccountTypeService) {
		this.externalAccountTypeService = externalAccountTypeService;
	}

	public long getExternalAccountTypeId() {
		return externalAccountTypeId;
	}

	public void setExternalAccountTypeId(long externalAccountTypeId) {
		this.externalAccountTypeId = externalAccountTypeId;
	}

	public String getExternalAccountUserName() {
		return externalAccountUserName;
	}

	public void setExternalAccountUserName(String externalAccountUserName) {
		this.externalAccountUserName = externalAccountUserName;
	}
	
	/**
	 * Get the role id.
	 * 
	 * @return
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * Set the role id
	 * 
	 * @param roleId
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	/**
	 * Get the list of user groups the user belongs to.
	 * 
	 * @return
	 */
	public List<IrUserGroup> getUserGroups() {
		return userGroups;
	}

	/**
	 * Set the role service.
	 * 
	 * @param roleService
	 */
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	
	/**
	 * Set the group workspace invite service.
	 * 
	 * @param groupWorkspaceInviteService
	 */
	public void setGroupWorkspaceInviteService(
			GroupWorkspaceInviteService groupWorkspaceInviteService) {
		this.groupWorkspaceInviteService = groupWorkspaceInviteService;
	}

}
