package edu.ur.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;

/**
 * This is a class that will email an error to the administrator
 * 
 * @author Nathan Sarr
 *
 */
public class EmailErrorInterceptor extends AbstractInterceptor implements StrutsStatics{

	/** eclipse generated id */
	private static final long serialVersionUID = -5332022001841367970L;

	/**  Logger for acegi user interceptor */
	private static final Logger log = Logger.getLogger(EmailErrorInterceptor.class);
	
	/** Service for dealing with users */
	private UserService userService;
	
	/** Service for dealing with emailing errors */
	private ErrorEmailService errorEmailService;
	
	/**
	 * Send an error email if an error occurs.
	 * 
	 * @see com.opensymphony.xwork2.interceptor.AbstractInterceptor#intercept(com.opensymphony.xwork2.ActionInvocation)
	 */
	public String intercept(ActionInvocation invocation){
		
		log.debug("error handling interceptor");
		
		try
		{
			log.debug("checking value");
			String value = invocation.invoke();
			log.debug("OK return");
			return value;
		}
		catch(Exception e)
		{
			log.error("emailing error ", e);
			SecurityContext ctx = SecurityContextHolder.getContext();
			final Authentication auth = ctx.getAuthentication();
	        IrUser user = null;
	        
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
			
			String userInfo = "";
			if( user != null)
			{
			    userInfo = "User Info = " + user.toString() + "\n\n";
			}
			
			StringWriter sw = null;
			PrintWriter pw = null;
			
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
			pw.close();
			
			errorEmailService.sendError(userInfo + sw.toString());
			
			try 
			{
				sw.close();
			} 
			catch (IOException e1) 
			{
				log.error("Swallowing error ", e1);
			}
			
			
			
		}
		
		return "handleError";
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ErrorEmailService getErrorEmailService() {
		return errorEmailService;
	}

	public void setErrorEmailService(ErrorEmailService errorEmailService) {
		this.errorEmailService = errorEmailService;
	}


}
