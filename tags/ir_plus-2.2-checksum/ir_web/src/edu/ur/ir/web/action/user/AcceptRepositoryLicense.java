package edu.ur.ir.web.action.user;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.repository.LicenseVersion;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Allows  a user to accept the license.
 * 
 * @author Nathan Sarr
 *
 */
public class AcceptRepositoryLicense extends ActionSupport implements  
UserIdAware{

	/** eclipse generated id */
	private static final long serialVersionUID = -5738030237716649442L;

	/** Id of the user accepting the license*/
	private Long userId;
	
	/** repository service for accessing repository information  */
	private RepositoryService repositoryService;
	
	/** allows user to continue submission  */
	private Long genericItemId;
	
	/** id of the license the user has accepted */
	private Long licenseId;
	
	/** indicates the user has accepted the license */
	private boolean acceptLicense = false;
	
	/** Service for dealing with users */
	private UserService userService;
	
	/** repository for the system */
	private Repository repository;
	
	public String execute()
	{
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);

		// make sure the user has accepted the license
		if( !acceptLicense )
		{
			addFieldError("licenseError", "You must accept the license to use to the workspace");
			return INPUT;
		}
		
		/* very unlikely but if the license changes while the user was accepting then
		 * make them re-accept */
		LicenseVersion license = repository.getDefaultLicense();
		if(license != null && !license.getId().equals(licenseId))
		{
			addFieldError("licenseChangeError", 
					"This license has changed please re-accept the new license");
			return INPUT;
		}
		
		IrUser user = userService.getUser(userId, false);
		
		user.addAcceptedLicense(license);
		userService.makeUserPersistent(user);
		
		// if user is working on submitting an item send them to the
		// submission page otherwise go to workspace
		if( genericItemId != null )
		{
		    return SUCCESS;
		}
		else
		{
			return "workspace";
		}
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;	
	}

	public void setRepositoryService(RepositoryService repostioryService) {
		this.repositoryService = repostioryService;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getGenericItemId() {
		return genericItemId;
	}

	public void setGenericItemId(Long genericItemId) {
		this.genericItemId = genericItemId;
	}

	public Long getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(Long licenseId) {
		this.licenseId = licenseId;
	}

	public boolean getAcceptLicense() {
		return acceptLicense;
	}

	public void setAcceptLicense(boolean acceptLicense) {
		this.acceptLicense = acceptLicense;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

}
