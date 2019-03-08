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
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrRole;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;


/**
 * Tag to display acl
 * 
 * @author Sharmila Ranganathan
 */
public class IrAclTag extends TagSupport {
	
	/**  Eclipse generated serial id  */
	private static final long serialVersionUID = 6325082719380355635L;

	/** Log for IrAclTag */
	private static final Logger log = LogManager.getLogger(IrAclTag.class);

	/** domain object */
    private Object domainObject;
    
    /** Permissions that need to be checked */
    private String hasPermission;

    /**
     * Create the acl tag
     */
    public int doStartTag() throws JspException {
    	log.debug("do start tag for ACL");
        
    	if ((null == hasPermission) || hasPermission.length() == 0) {
            return Tag.SKIP_BODY;
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
            return Tag.EVAL_BODY_INCLUDE;
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
            		 return Tag.EVAL_BODY_INCLUDE;
            	 }
             }
             
             if( resolvedDomainObject != null )
             {
            	 log.debug("Resoved object = " + resolvedDomainObject);
             }
             
             
             Long count = aclManager.hasPermission(resolvedDomainObject, user, hasPermission);
             
             if( count > 0)
             {
            	 return Tag.EVAL_BODY_INCLUDE;
             }
             else {
				log.debug("No ACLS found");
			 }

		}
        log.debug("returning body");
        return Tag.SKIP_BODY;
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

    /**
     * Domain object to check to see if permissions exist.
     * 
     * @return
     */
    public Object getDomainObject() {
        return domainObject;
    }

    /**
     * Permission to check
     * 
     * @return
     */
    public String getHasPermission() {
        return hasPermission;
    }

    /**
     * Domain object to check to see if permissions exist.
     * 
     * @param domainObject
     */
    public void setDomainObject(Object domainObject) {
        this.domainObject = domainObject;
    }

    /**
     * Permission to check
     * 
     * @param hasPermission
     */
    public void setHasPermission(String hasPermission) {
        this.hasPermission = hasPermission;
    }

}
