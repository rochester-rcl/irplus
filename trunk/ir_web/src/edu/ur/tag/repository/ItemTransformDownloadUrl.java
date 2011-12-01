package edu.ur.tag.repository;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemFileSecurityService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.tag.TagUtil;

/**
 * Allows an item file url to be generated if the user has permissions to view the transformed file.
 * 
 * @author Nathan Sarr
 *
 */
public class ItemTransformDownloadUrl  extends SimpleTagSupport{
	
	/** The item file to get the transform from */
	private ItemFile itemFile;
	
	/** The thumbnail code to get the thumbnail */
	private String systemCode;
	
	/** Indicates that it should be a download url if possible*/
	private boolean download = false;
	
	/** var to set the path for*/
	private String var;
	
	/** Logger */
	private static final Logger log = Logger.getLogger(ItemTransformDownloadUrl.class);

	
	public void doTag() throws JspException {
		
		log.debug("Start item Transform download url item file = " + itemFile);
		boolean canView = false;
		
		PageContext pageContext = (PageContext) this.getJspContext();

        IrFile irFile = itemFile.getIrFile();
        
        // make sure the transformed file is available
		if( systemCode != null && irFile != null)
		{   
			log.debug("item file system code and ir file are NOT NULL");
			if(itemFile.getIrFile().getTransformedFileBySystemCode(systemCode) != null)
			{
				log.debug("found system code!");
				GenericItem genericItem = itemFile.getItem();
				log.debug(" generic item = " + genericItem.isPubliclyViewable() + " itemFile.isPublic() = " + itemFile.isPublic() +
						" irFile.isPublicViewable() = " + irFile.isPublicViewable() +
						" item is embargoed = " + genericItem.isEmbargoed());
				if (genericItem.isPubliclyViewable() && !genericItem.isEmbargoed() && itemFile.isPublic() ) 
		        {
					log.debug("CAN VIEW both are public");
					canView = true;
		        }
				else 
			    {
				   log.debug("Checking user permissions");
				   IrUser user = null;
				        
				   final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
						
				   if( auth != null) {
				       if(auth.getPrincipal() instanceof UserDetails) {
					       user = (IrUser)auth.getPrincipal();
					   }
				   }
				   
				   if( user != null)
				   {
				       ApplicationContext context = getContext(pageContext);
		               String[] beans = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context, 
		            		ItemFileSecurityService.class, false, false);

		                 if (beans.length == 0) {
		                     throw new JspException("No item file security was found the application context: " 
		                		 + context.toString());
		                 }

		                 ItemFileSecurityService itemFileSecurityService = (ItemFileSecurityService) context.getBean(beans[0]);
		             
		                 if(log.isDebugEnabled())
		                 {
		            	     log.debug("itemFileSecurityService manager found");
		                 }
			             if( genericItem.getOwner().equals(user) || 
			                 user.hasRole(IrRole.ADMIN_ROLE)|| 
			                 itemFileSecurityService.hasPermission(itemFile, user, ItemFileSecurityService.ITEM_FILE_READ_PERMISSION) )
			             {
			            	log.debug("User can view by permission access");
			                canView = true;	
			             }
			        }
			    }
			}
		}
		
		
		if( canView )
		{
			 try
			    {
		            String path = TagUtil.getPageContextPath((PageContext)getJspContext());
		            path += "/genericItemTransformedFileDownload.action?itemFileId=" + itemFile.getId();
		            path += "&systemCode=" + systemCode;
			
		            pageContext.setAttribute(var, path);
			    }
			    catch(Exception e)
			    {
				    throw new JspException("could not print path", e);
			    }
		}
		else
		{
			pageContext.setAttribute(var, null);
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
    
    
	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public ItemFile getItemFile() {
		return itemFile;
	}

	public void setItemFile(ItemFile itemFile) {
		this.itemFile = itemFile;
	}
}
