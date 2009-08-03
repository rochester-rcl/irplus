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
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.item.ItemFileSecurityService;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.statistics.DownloadStatisticsService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Action to download item files.
 * 
 * @author Nathan Sarr
 *
 */
public class GenericItemFileDownload extends ActionSupport implements ServletResponseAware, ServletRequestAware, UserIdAware
{

	/** Eclipse generated id. */
	private static final long serialVersionUID = 5430030320610916010L;

	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(GenericItemFileDownload.class);
	
	/**  File to download */
	private Long itemFileId;
	
	/** item that should contain the file */
	private Long itemId;

    /** Service for items */
    private ItemService itemService;
	
	/**  Servlet response to write to */
	private HttpServletResponse response;
	
	/**  Servlet request made */
	private HttpServletRequest request;
	
	/** Utility for streaming file */
	private WebIoUtils webIoUtils;

	/** Download statistics service */
	private DownloadStatisticsService downloadStatisticsService;
	
	/** Id of user logged in */
	private Long userId;
	
	/** User service */
	private UserService userService; 
	
	/** Item file security service */
	private ItemFileSecurityService itemFileSecurityService; 
	
    /**
     * Checks for user permission and then downloads the file 
     * 
     * @return
     * @throws Exception
     */
    public String fileDownloadWithPermissionCheck() throws Exception {

	    log.debug("Trying to download the file to user");
	    
        if (itemFileId == null) {
        	log.debug("file id is null");
            return INPUT;
        }
        
        if( itemId == null)
        {
        	log.debug("item id is null");
        	return INPUT;
        }
         
        GenericItem genericItem = itemService.getGenericItem(itemId, false);
 
        if( genericItem == null)
        {
        	log.debug("Item  is null");
        	return INPUT;
        }
        
        ItemFile itemFile = genericItem.getItemFile(itemFileId);
 
        if( itemFile == null)
        {
        	log.debug("Item file is null");
        	return INPUT;
        }
        
        IrUser user = null;
        
        if (userId != null) {
        	user = userService.getUser(userId, false);
        }
        
         // Check if file can be downloaded by user
        if (itemFile.isPublic() ) {
        	
        	if( user == null || !genericItem.getOwner().equals(user))
        	{
        	    downloadStatisticsService.addFileDownloadInfo(request.getRemoteAddr(),
            		itemFile.getIrFile());
        	}
        	downloadFile(itemFile);
        }
        else if ( user != null)
        {
        	if( genericItem.getOwner().equals(user) || 
            	(itemFileSecurityService.hasPermission(itemFile, user, ItemFileSecurityService.ITEM_FILE_READ_PERMISSION) > 0) )
        	{
        		if(genericItem.getOwner().equals(user))
        		{
        			downloadStatisticsService.addFileDownloadInfo(request.getRemoteAddr(),
                    		itemFile.getIrFile());
        		}
        		downloadFile(itemFile);
        	}
        }
        else
        {
        	log.debug("File is private.");
        	return INPUT;
        }
        
        return SUCCESS;

    }

    /*
     * Downloads the file
     */
    private void downloadFile(ItemFile itemFile) throws Exception {
        String fileName = itemFile.getIrFile().getName();
        FileInfo fileInfo =  itemFile.getIrFile().getFileInfo();
        webIoUtils.StreamFileInfo(fileName, fileInfo, response, request, (1024*4), false, true);
        
    }
    
	/* (non-Javadoc)
	 * @see com.opensymphony.webwork.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}


	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}



	public WebIoUtils getWebIoUtils() {
		return webIoUtils;
	}


	public void setWebIoUtils(WebIoUtils webIoUtils) {
		this.webIoUtils = webIoUtils;
	}


	public ItemService getItemService() {
		return itemService;
	}


	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}


	public Long getItemFileId() {
		return itemFileId;
	}


	public void setItemFileId(Long itemFileId) {
		this.itemFileId = itemFileId;
	}


	public Long getItemId() {
		return itemId;
	}


	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}


	public void setDownloadStatisticsService(
			DownloadStatisticsService downloadStatisticsService) {
		this.downloadStatisticsService = downloadStatisticsService;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setItemFileSecurityService(
			ItemFileSecurityService itemFileSecurityService) {
		this.itemFileSecurityService = itemFileSecurityService;
	}


}
