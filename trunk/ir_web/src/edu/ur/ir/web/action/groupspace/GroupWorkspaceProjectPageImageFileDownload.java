/**  
   Copyright 2008-2012 University of Rochester

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

package edu.ur.ir.web.action.groupspace;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.action.researcher.ResearcherPictureFileDownload;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Group workspace image file download
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceProjectPageImageFileDownload  extends ActionSupport 
implements ServletResponseAware, ServletRequestAware, UserIdAware
{
	// Eclipse generated id
	private static final long serialVersionUID = -8296841373758041431L;
	
	//  Logger for file upload */
	private static final Logger log = Logger.getLogger(ResearcherPictureFileDownload.class);

	// Id of group workspace */
	private Long groupWorkspaceId;

	// Service for dealing with group workspaces */
	private GroupWorkspaceService groupWorkspaceService;
	
	// Service to deal with user information
	private UserService userService;

	//  Servlet response to write to */
	private transient HttpServletResponse response;
	
	// id of the ir file to download */
	private Long irFileId;
	
	// request made to the server */
	private transient HttpServletRequest request;
	
	// Utility for web utils */
	private WebIoUtils webIoUtils;
	
	// id of the user accessing the file */
	private Long userId;

	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	
    	if( log.isDebugEnabled())
    	{
	        log.debug("Trying to download group workspace project page picture");
    	}
	    
	    // make sure this is a picture in the researcher - otherwise anyone could get
	    // to the files.
		GroupWorkspace groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		GroupWorkspaceProjectPage groupWorkspaceProjectPage = null;
		IrUser user = userService.getUser(userId, false);
		
		if(groupWorkspace != null)
		{
			groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
		}
		
		if( groupWorkspaceProjectPage != null && 
				(groupWorkspaceProjectPage.getPagePublic() || groupWorkspace.getUser(user) != null ))
		{
			// causes a load form the database;
		    IrFile irFile = null;
		    if( groupWorkspaceProjectPage.getImageByFileId(irFileId) != null )
		    {
		    	irFile = groupWorkspaceProjectPage.getImageByFileId(irFileId).getImageFile();
		    }
		    
		    if( irFile != null )
		    {
                FileInfo fileInfo = irFile.getFileInfo();

                if( log.isDebugEnabled() )
                {
                    log.debug("Found ir File " + irFile);
                }
                webIoUtils.streamFileInfo(fileInfo.getName(), fileInfo, response, request, (1024*4), true, false);
		    }
		}
        return SUCCESS;
    }
	

	/* (non-Javadoc)
	 * @see com.opensymphony.webwork.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	/**
	 * Set the file 
	 * 
	 * @param irFileId
	 */
	public void setIrFileId(Long irFileId) {
		this.irFileId = irFileId;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Set the web io utils.
	 * 
	 * @param webIoUtils
	 */
	public void setWebIoUtils(WebIoUtils webIoUtils) {
		this.webIoUtils = webIoUtils;
	}

	/**
	 * Get the group workspace id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceId() {
		return groupWorkspaceId;
	}

	/**
	 * Set the group workspace id.
	 * 
	 * @param groupWorkspaceId
	 */
	public void setGroupWorkspaceId(Long groupWorkspaceId) {
		this.groupWorkspaceId = groupWorkspaceId;
	}

	/**
	 * Set the group workspace service.
	 * 
	 * @param groupWorkspaceService
	 */
	public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}

	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
}
