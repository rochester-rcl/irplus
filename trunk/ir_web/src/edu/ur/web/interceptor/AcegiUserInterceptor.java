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

package edu.ur.web.interceptor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.action.user.ChangePassword;
import edu.ur.ir.web.action.user.EmailVerification;
import edu.ur.ir.web.action.user.ViewWorkspace;

/**
 * Will populate the session with needed user information.  This
 * class also helps out with checking to see if the user is 
 * creating their account.
 * 
 * Places the user in the request with the value <code>user</code>.
 * 
 * @author Nathan Sarr
 */
public class AcegiUserInterceptor extends AbstractInterceptor implements StrutsStatics{

	/**  Eclipse generated serial id */
	private static final long serialVersionUID = 4890726152082586505L;

	/**  Logger for acegi user interceptor */
	private static final Logger log = Logger.getLogger(AcegiUserInterceptor.class);
	
	/** Service for dealing with users */
	private UserService userService;
	
	/**  Repository information data access  */
	private RepositoryService repositoryService;

	/**
	 * Gets the user and sets them in the session if they exist.  Also does some checking 
	 * and forces certain requirements for users who are logged in.
	 * 
	 * @see com.opensymphony.xwork2.interceptor.AbstractInterceptor#intercept(com.opensymphony.xwork2.ActionInvocation)
	 */
	public String intercept(ActionInvocation invocation) throws Exception {
		
		IrUser user = null;
		SecurityContext ctx = SecurityContextHolder.getContext();
		final Authentication auth = ctx.getAuthentication();
		final Object action = invocation.getAction();
       
		log.debug("auth = " + auth);
		if( auth != null)
		{
			 if(auth.getPrincipal() instanceof UserDetails)
			 {
				 // this is a new session use the service to retrieve the
				 // user otherwise, the session will be closed
		         Long userId = ((IrUser)auth.getPrincipal()).getId();
		         user = userService.getUser(userId, false);
			 }
		}

		// Check if the user default email is verified
		if ((user != null) && (!user.getDefaultEmail().isVerified()) && (!(action instanceof EmailVerification))) {
	   			invocation.getInvocationContext().getSession().remove("user");
	   			user = null;
	   			ctx.setAuthentication(null);
	   			SecurityContextHolder.setContext(ctx);
	   			return "registration-error";
		}

		
		//Any action that implements user id aware requires the user id
		//is set 
		if (action instanceof UserIdAware) {
			if( log.isDebugEnabled() )
			{
			    log.debug("In AcegiUserInterceptor User = " + user);
			}
			
			if( user != null )
			{
				log.debug("injecting user id");
	            ((UserIdAware) action).injectUserId(user.getId());
			}
			else
			{
				log.debug("setting user id to null");
				// make sure user id is cleared out
				((UserIdAware) action).injectUserId(null);
			}

	    }
		
		// put the user in the session
		invocation.getInvocationContext().getSession().put("user", user);

		
		// Force user to change password after login
		if (!(action instanceof ChangePassword) && (user != null) && (user.getPasswordChangeRequired())) {
			return "change-password";
		}
		
		//make the user accept the license if the user does not have
	    //the most current license.
		if(  (user != null) && (action instanceof ViewWorkspace) )
		{	   
	 	    Repository repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		    
	 	    // this can happen when setting up the repository
	 	    // all other times the repository should not be null
	 	    if( repository != null )
		    {
	 	        if( repository.getDefaultLicense() != null && user.getAcceptedLicense(repository.getDefaultLicense()) == null)
		        {
			        return "acceptLicense";
		        }
		    }
		}
		
		return invocation.invoke();
	}



	/**
	 * Set the user service to access information.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Set the repository service.
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}


}
