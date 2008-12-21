
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


package edu.ur.tag.repository;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;

import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;

/**
 * This tag sets the variable passed in to true or false 
 * based on if the user has the specified permission.
 * 
 * @author Nathan Sarr
 *
 */
public class IrAclSimpleTag extends SimpleTagSupport{
	
	/** variable to set with the true value*/
	private String var;
	
	/** domain object */
    private Object domainObject;
    
    /** Permissions that need to be checked */
    private String hasPermission;
    
	/** Log for IrAclTag */
	private static final Logger log = Logger.getLogger(IrAclSimpleTag.class);


	
	public void doTag() throws JspException {
    	log.debug("do start tag for ACL");
        boolean granted = false;
        
        PageContext pageContext = (PageContext)this.getJspContext();
        
    	if ((null == hasPermission) || hasPermission.length() == 0) {
            granted = false;
        }
        
        Object resolvedDomainObject = null;

        if (domainObject instanceof String) {
            resolvedDomainObject = ExpressionEvaluationUtils.evaluate("domainObject", 
            		(String) domainObject,
                    Object.class, pageContext);
        } else {
            resolvedDomainObject = domainObject;
        }

        if (resolvedDomainObject == null) {
            if (log.isDebugEnabled()) {
                log.debug("domainObject resolved to null, so including tag body");
            }

            // Of course they have access to a null object!
           granted = true;
        }
        else
        {
        	 ApplicationContext context = getContext(pageContext);
             String[] beans = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context, 
            		 SecurityService.class, false, false);

             if (beans.length == 0) {
                 throw new JspException("No IrAclManager was found the application context: " 
                		 + context.toString());
             }

             SecurityService aclManager = (SecurityService) context.getBean(beans[0]);
             
             if(log.isDebugEnabled())
             {
            	 log.debug("acl manager found");
             }
             
             context = getContext(pageContext);
             
             IrUser user = null;
             
             final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
     		
             if( auth != null) {
     			 if(auth.getPrincipal() instanceof UserDetails) {
     				 user = (IrUser)auth.getPrincipal();
     			 }
             }
     		
             if( user != null )
             {
            	 log.debug("User " + user + " found");
            	 if( user.hasRole(IrRole.ADMIN_ROLE))
            	 {
            		 granted = true;
            	 }
             }
             
             if( resolvedDomainObject != null )
             {
            	 log.debug("Resoved object = " + resolvedDomainObject);
             }
             
             
             Long count = aclManager.hasPermission(resolvedDomainObject, user, hasPermission);
             
             if( count > 0)
             {
            	 granted = true;
             }
             else {
				log.debug("No ACLS found");
			 }

		}
        
        log.debug("Setting var " + var + " to " + granted);
        pageContext.setAttribute(var, granted);
       
	    try
		{
	    	if( getJspBody() != null )
	    	{
		        getJspBody().invoke(null);
	    	}
		}
		catch(Exception e)
		{
			throw new JspException("could not invoke body",e);
		}
	}
	
    /**
     * Allows test cases to override where application context obtained from.
     *
     * @param pageContext so the <code>ServletContext</code> can be accessed as required by Spring's
     *        <code>WebApplicationContextUtils</code>
     *
     * @return the Spring application context (never <code>null</code>)
     */
    protected ApplicationContext getContext(PageContext pageContext) {
        ServletContext servletContext = pageContext.getServletContext();

        return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    }

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public Object getDomainObject() {
		return domainObject;
	}

	public void setDomainObject(Object domainObject) {
		this.domainObject = domainObject;
	}

	public String getHasPermission() {
		return hasPermission;
	}

	public void setHasPermission(String hasPermission) {
		this.hasPermission = hasPermission;
	}

}
