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

package edu.ur.ir.web.action.item;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TransformedFile;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemFileSecurityService;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Allows a user to download a transformed file for a generic item 
 * 
 * @author Nathan Sarr
 *
 */
public class GenericItemTransformedFileDownloader extends ActionSupport 
implements ServletResponseAware, ServletRequestAware, UserIdAware {
	
	/** eclipse generated id */
	private static final long serialVersionUID = 646071772507853416L;

	/** Id of user logged in */
	private Long userId;

	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(GenericItemTransformedFileDownloader.class);

	/**  Servlet response to write to */
	private transient HttpServletResponse response ;
	
	/**  Servlet response to write to */
	private transient HttpServletRequest request;
	
	/** System code for the transformed file  */
	private String systemCode;
	
	/** Utility to help stream files */
	private WebIoUtils webIoUtils;
	
	/**  File to download */
	private Long itemFileId;
	
	/** User service */
	private UserService userService; 
	
	   /** Service for items */
    private ItemService itemService;
    
	/** Item file security service */
	private ItemFileSecurityService itemFileSecurityService; 

	
	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception 
    {
    	log.debug("Trying to transformed item download the file to user itemFileId = " + itemFileId );
    	
	    log.debug("Trying to download the file to user");
	    
        if (itemFileId == null) {
        	log.debug("file id is null");
            return SUCCESS;
        }
        
        ItemFile itemFile = itemService.getItemFile(itemFileId, false);
       
        if( itemFile == null)
        {
       	    log.debug("Item file is null");
       	    return SUCCESS;
        }
        
        IrUser user = null;
        
        if (userId != null) {
        	user = userService.getUser(userId, false);
        }
        
        IrFile irFile = itemFile.getIrFile();
        GenericItem genericItem = itemFile.getItem();

        
        // Check if file can be downloaded by user
        if (genericItem.isPubliclyViewable() && !genericItem.isEmbargoed() && itemFile.isPublic()) 
        {
        	TransformedFile tf = irFile.getTransformedFileBySystemCode(systemCode);
        	if( tf != null )
        	{
        		FileInfo info = tf.getTransformedFile();
        	    webIoUtils.streamFileInfo(info.getName(), info, response, request, (1024*4), true, false);
        	}
        }
        else if ( user != null)
        {
        	if( genericItem.getOwner().equals(user) || user.hasRole(IrRole.ADMIN_ROLE) ||
            	(itemFileSecurityService.hasPermission(itemFile, user, ItemFileSecurityService.ITEM_FILE_READ_PERMISSION) > 0) )
        	{
        		TransformedFile tf = irFile.getTransformedFileBySystemCode(systemCode);
            	if( tf != null )
            	{
            		FileInfo info = tf.getTransformedFile();
            	    webIoUtils.streamFileInfo(info.getName(), info, response, request, (1024*4), true, false);
            	}
        	}
        }
        else
        {
        	log.debug("File is private.");
        	return SUCCESS;
        }
        
        return SUCCESS;
    	
    }
    
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}


	public String getSystemCode() {
		return systemCode;
	}


	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public WebIoUtils getWebIoUtils() {
		return webIoUtils;
	}

	public void setWebIoUtils(WebIoUtils webIoUtils) {
		this.webIoUtils = webIoUtils;
	}


	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}




	public void setItemFileId(Long itemFileId) {
		this.itemFileId = itemFileId;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}




	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}




	public void setItemFileSecurityService(
			ItemFileSecurityService itemFileSecurityService) {
		this.itemFileSecurityService = itemFileSecurityService;
	}


}
