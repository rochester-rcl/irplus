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

package edu.ur.ir.web.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.handle.HandleInfo;
import edu.ur.ir.handle.HandleService;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionService;

/**
 * Will handle the old dspace handle and redirect.
 * 
 * @author Nathan Sarr
 *
 */
public class RedirectOldHandleUrl extends ActionSupport implements ServletRequestAware{

	/**  Eclipse generated value */
	private static final long serialVersionUID = -3542335646160702315L;
	
	/** request made */
	private transient HttpServletRequest request;
	
	/** Service for dealing with handles */
	private HandleService handleService;
	
	/** Service for dealing with institutional item version information */
	private InstitutionalItemVersionService institutionalItemVersionService;
	



	/** institutional item id */
	private Long institutionalItemId;
	
	/** verison number of the item */
	private int versionNumber;
	
	/** logger */
	private static final Logger log = Logger.getLogger(RedirectOldHandleUrl.class);
	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		String url = request.getRequestURL().toString();
		int handleIndex = url.indexOf("handle");
		if( handleIndex != -1)
		{
		    String urlWithHandle = url.substring(handleIndex);
		    log.debug("Handle url = " + urlWithHandle);
		    String[] values = urlWithHandle.split("/");
		    
		    if( values.length == 3)
		    {
		       String authority = values[1].trim();
		       log.debug("authority = " + authority);
		       String localName = values[2].trim();
		       log.debug("name = " + localName);
		       
		       String fullHandle = authority + "/" + localName;
		       HandleInfo handleInfo = handleService.getHandleInfo(fullHandle);
		       
		       if( handleInfo != null )
		       {
		           InstitutionalItemVersion itemVersion = institutionalItemVersionService.getInstitutionalItemByHandleId(handleInfo.getId());
		           
		           if(  itemVersion != null )
		           {
		        	   versionNumber = itemVersion.getVersionNumber();
		        	   InstitutionalItem item = itemVersion.getVersionedInstitutionalItem().getInstitutionalItem();
		               if( item != null )
		               {
		            	   institutionalItemId = item.getId();
		            	   return SUCCESS;
		               }
		           }
		       }
		    }
		}
		return "notFound";
		
		
	}

	
	/**
	 * Set the servlet request
	 * 
	 * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}


	/**
	 * Get the handle service.
	 * 
	 * @return
	 */
	public HandleService getHandleService() {
		return handleService;
	}


	/**
	 * Set the handle service
	 * 
	 * @param handleService
	 */
	public void setHandleService(HandleService handleService) {
		this.handleService = handleService;
	}


	/**
	 * Get the institutional item id.
	 * 
	 * @return
	 */
	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}


	/**
	 * Get the version number.
	 * 
	 * @return
	 */
	public int getVersionNumber() {
		return versionNumber;
	}
	
	/**
	 * Set the institutional item version service.
	 * 
	 * @param institutionalItemVersionService
	 */
	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}

}
