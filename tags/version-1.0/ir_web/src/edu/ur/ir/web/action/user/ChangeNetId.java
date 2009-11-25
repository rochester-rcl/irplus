package edu.ur.ir.web.action.user;

import org.apache.log4j.Logger;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.providers.ldap.LdapAuthenticator;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.security.service.UrLdapAuthenticationProvider;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * This action allows a irUser to change their own net id.
 * 
 * @author Nathan Sarr
 *
 */
public class ChangeNetId extends ActionSupport implements UserIdAware {
	
	/** eclipse generated id */
	private static final long serialVersionUID = -3627366596099060879L;

	/**  Logger for add irUser action */
	private static final Logger log = Logger.getLogger(ChangeNetId.class);
	
	/**  User object */
	private Long userId;
	
	/** User loaded for changing ldap information */
	private IrUser irUser;
	
	/** ldap irUser name */
	private String netId;
	
	/** password for net id */
	private String netIdPassword;

	/** Authenticator for ldap username/password */
	private LdapAuthenticator ldapAuthenticator;
	
	/** Authentication provider for ldap */
	private UrLdapAuthenticationProvider ldapAuthProvider;
	
	/** User service */
	private UserService userService;
	
	/** true if the net id was added/updated */
	private boolean added = true;
	
	
	/**
	 * Execute method
	 */
	public String execute(){
		
		irUser = userService.getUser(userId, false);
		
		// user did not change net id
		if( (irUser.getLdapUserName() != null && irUser.getLdapUserName().equals(netId)) ||
		    (irUser.getLdapUserName() == null && netId == null) )
		{
			return SUCCESS;
		}
		
		
		
		if( netId != null  && !netId.trim().equals(""))
		{
			log.debug("creating LDAP authentication token");
	
			// don't hit ldap unless a irUser has entered a username and password
			if( netIdPassword != null && !netIdPassword.trim().equals("")  )
			{
			    try
			    {
			        ldapAuthenticator.authenticate(new UsernamePasswordAuthenticationToken(netId, netIdPassword));
			        IrUser ldapUser = userService.getUserByLdapUserName(netId);
					// we have an interesting problem
					// irUser has authenticated correctly - but the irUser name already exists in the 
					// system - 
					if( ldapUser != null )
					{
						added = false;
				    	addFieldError("netIdAlreadyExists", "The net id irUser name already exists - you may already have an account please contact the admistrator");
					}
					else
					{
						irUser.setLdapUserName(netId);
					}

			    }
			    catch(BadCredentialsException bce)
			    {
			    	 log.error(bce);
			    	 added = false;
			    	 addFieldError("netIdPasswordFail", "The net id credentials could not be verified");
			    }
		     }
			 else
			 {
			      added = false;
				  addFieldError("netIdPasswordEmpty", "The net id password must be entered for net id user name verification");
			 }
			
		}
		else
		{
			irUser.setLdapUserName(null);
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


	public String getNetId() {
		return netId;
	}


	public void setNetId(String ldapUserName) {
		this.netId = ldapUserName;
	}


	public String getNetIdPassword() {
		return netIdPassword;
	}


	public void setNetIdPassword(String netIdPassword) {
		this.netIdPassword = netIdPassword;
	}


	public LdapAuthenticator getLdapAuthenticator() {
		return ldapAuthenticator;
	}


	public void setLdapAuthenticator(LdapAuthenticator ldapAuthenticator) {
		this.ldapAuthenticator = ldapAuthenticator;
	}


	public UrLdapAuthenticationProvider getLdapAuthProvider() {
		return ldapAuthProvider;
	}


	public void setLdapAuthProvider(UrLdapAuthenticationProvider ldapAuthProvider) {
		this.ldapAuthProvider = ldapAuthProvider;
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
}
