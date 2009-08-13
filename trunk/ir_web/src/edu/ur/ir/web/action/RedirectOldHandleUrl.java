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
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.institution.InstitutionalItemVersion;

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
	private HttpServletRequest request;
	
	/** Service for dealing with handles */
	private HandleService handleService;
	
	/** service for dealing with institutional item data */
	private InstitutionalItemService institutionalItemService;
	
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
		           InstitutionalItemVersion itemVersion = institutionalItemService.getInstitutionalItemByHandleId(handleInfo.getId());
		           
		           if(  itemVersion != null )
		           {
		        	   versionNumber = itemVersion.getVersionNumber();
		        	   InstitutionalItem item = institutionalItemService.getInstitutionalItemByVersionId(itemVersion.getId());
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

	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}


	public HandleService getHandleService() {
		return handleService;
	}


	public void setHandleService(HandleService handleService) {
		this.handleService = handleService;
	}


	public Long getInstitutionalItemId() {
		return institutionalItemId;
	}


	public int getVersionNumber() {
		return versionNumber;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

}
