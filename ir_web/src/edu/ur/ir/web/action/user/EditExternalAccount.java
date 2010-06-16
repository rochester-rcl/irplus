package edu.ur.ir.web.action.user;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.BadCredentialsException;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.security.ExternalAuthenticationDetails;
import edu.ur.ir.security.ExternalAuthenticationProvider;
import edu.ur.ir.security.service.LdapAuthenticationToken;
import edu.ur.ir.user.ExternalUserAccount;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * This action allows a irUser to change their own net id.
 * 
 * @author Nathan Sarr
 *
 */
public class EditExternalAccount extends ActionSupport implements UserIdAware {
	
	/** eclipse generated id */
	private static final long serialVersionUID = -3627366596099060879L;

	/**  Logger for add irUser action */
	private static final Logger log = Logger.getLogger(EditExternalAccount.class);
	
	/**  User object */
	private Long userId;
	
	/** User loaded for changing ldap information */
	private IrUser irUser;
	
	/** external account user name */
	private String userName;
	
	/** password for external account */
	private String password;

	/** User service */
	private UserService userService;
	
	/** true if the net id was added/updated */
	private boolean added = true;
	
	/** External authentication provider */
	private ExternalAuthenticationProvider externalAuthenticationProvider;
	
	/** Repository service for placing information in the repository */
	private RepositoryService repositoryService;
	
	/**
	 * Execute method
	 */
	public String execute(){
		
		irUser = userService.getUser(userId, false);
		
		if( irUser == null )
		{
			addFieldError("userNull", "The user is null");
			return SUCCESS;
     	}
		
		if( !repositoryService.isExternalAuthenticationEnabled())
		{
			addFieldError("externalAutthenticationDisabled", "External account authentication is disabled");
			return SUCCESS;
     	}
		
		if( userName != null  && !userName.trim().equals(""))
		{
			log.debug("creating LDAP authentication token");
	
			// don't hit external accounts unless a irUser has entered a username and password
			if( password != null && !password.trim().equals("")  )
			{
			    try
			    {
			    	Authentication auth = externalAuthenticationProvider.authenticate(new LdapAuthenticationToken(userName, this.password));
			        ExternalAuthenticationDetails externalDetails = null;
			    	
			    	if( auth.getDetails() instanceof ExternalAuthenticationDetails)
			        {
			    		externalDetails  = (ExternalAuthenticationDetails)auth.getDetails();
			        }
			    	
			        ExternalUserAccount externalUserAccount = userService.getByExternalUserNameAccountType( userName, externalDetails.getType());
			        
					
			        if( externalUserAccount != null )
			        {
			        	IrUser externalAccountUser = externalUserAccount.getUser();
			        	
			        	// we have an interesting problem
						// irUser has authenticated correctly - but the irUser name already exists in the 
						// system for some other user
			        	if(!externalAccountUser.equals(irUser) )
			        	{
			        		added = false;
					    	addFieldError("userNameAlreadyExists", "The user name already exists - you may have more than one account please contact the admistrator");
			        	}
			        	else
			        	{
			        		// user already added with account information
			        	}
			        }
			        else if(externalDetails != null)
			        {
			        	externalUserAccount =  irUser.getExternalAccount();
			        	if( externalUserAccount != null )
			        	{
			        		externalUserAccount.setExternalUserAccountName(userName);
			        		externalUserAccount.setExternalAccountType(externalDetails.getType());
			        	}
			        	else
			        	{
			        	    irUser.createExternalUserAccount(userName, externalDetails.getType());
			        	}
			        }
			        else
			        {
			        	added = false;
			        	addFieldError("passwordFail", "The external account credentials could not be verified");
			        }
			    }
			    catch(BadCredentialsException bce)
			    {
			    	 log.error(bce);
			    	 added = false;
			    	 addFieldError("passwordFail", "The external account credentials could not be verified");
			    }
		     }
			 else
			 {
			      added = false;
				  addFieldError("passwordEmpty", "The external account password must be entered for verification");
			 }
			
		}
		else
		{
			added = true;
			ExternalUserAccount externalAccount = irUser.getExternalAccount();
			irUser.deleteExternalUserAccount();
			userService.delete(externalAccount);
		}
		

		if( added )
		{
		    userService.makeUserPersistent(irUser);
		}
		return SUCCESS;
	}


	/**
	 * Get User Service
     *
	 * @return User Service
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Set User service
     *
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public IrUser getIrUser() {
		return irUser;
	}


	public void setIrUser(IrUser user) {
		this.irUser = user;
	}


	public String getpassword() {
		return password;
	}


	public void setpassword(String password) {
		this.password = password;
	}


	public Long getUserId() {
		return userId;
	}


	public boolean isAdded() {
		return added;
	}


	public void setAdded(boolean added) {
		this.added = added;
	}


	public ExternalAuthenticationProvider getExternalAuthenticationProvider() {
		return externalAuthenticationProvider;
	}


	public void setExternalAuthenticationProvider(
			ExternalAuthenticationProvider externalAuthenticationProvider) {
		this.externalAuthenticationProvider = externalAuthenticationProvider;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public RepositoryService getRepositoryService() {
		return repositoryService;
	}


	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
}
