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
import edu.ur.ir.file.TransformedFile;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageImage;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Group workspace thumbnail downloader.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceThumbnailDownloader extends ActionSupport 
implements ServletResponseAware, ServletRequestAware, UserIdAware{
	
	// Eclipse generated id 
	private static final long serialVersionUID = -663695133380631311L;

	//  Logger for file upload 
	private static final Logger log = Logger.getLogger(GroupWorkspaceThumbnailDownloader.class);

	//  Servlet response to write to 
	private transient HttpServletResponse response ;
	
	//  Servlet response to write to 
	private transient HttpServletRequest request;
	
	// id of the picture 
	private Long irFileId;
	
	// id of the user trying to access the information 
	private Long userId;
	
	// id of the group workspace
	private Long groupWorkspaceId;


	// Utility to help stream files 
	private WebIoUtils webIoUtils;
	
	// service to deal with researcher information.
    private UserService userService;
    
    // service to deal with 
    private GroupWorkspaceService groupWorkspaceService;

	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	log.debug("Trying to download the instutional collection thumbnail to user");
    	
    	GroupWorkspace groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		IrUser user = userService.getUser(userId, false);
		GroupWorkspaceUser groupWorkspaceUser = groupWorkspace.getUser(user);
		
		// only a group workspace owner can edit the project page
		if( groupWorkspaceUser == null || !groupWorkspaceUser.isOwner())
		{
			return "accessDenied";
		}
		
		GroupWorkspaceProjectPage projectPage = groupWorkspace.getGroupWorkspaceProjectPage();
       	
		GroupWorkspaceProjectPageImage image = projectPage.getImageByFileId(irFileId);
    	IrFile irFile = null;
    	if( image != null )
    	{
    		irFile = image.getImageFile();
    	}
    	
    	if( irFile != null )
    	{
    	    TransformedFile transform = irFile.getTransformedFileBySystemCode(TransformedFileType.PRIMARY_THUMBNAIL);
    		if( transform != null )
    		{
    		    FileInfo info = transform.getTransformedFile();
    			if( irFile.isPublicViewable() )
    	    	{
    	    	    webIoUtils.streamFileInfo(info.getName(), info, response, request, (1024*4), true, false);
    	    	}
    	    	else
    	    	{
    	    	    webIoUtils.streamFileInfo(info.getName(), info, response, request, (1024*4), false, false);
    	    	}
    		}
    	}
    	
        return SUCCESS;
    }
    
    
	/**
	 * Sets the servlet response.
	 * 
	 * @see org.apache.struts2.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * Set the io web util helper.
	 * 
	 * @param webIoUtils
	 */
	public void setWebIoUtils(WebIoUtils webIoUtils) {
		this.webIoUtils = webIoUtils;
	}

	/**
	 * Sets the servlet request.
	 * 
	 * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Set the id of the file.
	 * 
	 * @param irFileId
	 */
	public void setIrFileId(Long irFileId) {
		this.irFileId = irFileId;
	}

	/**
	 * Id of the user trying to access the information.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Service to deal 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Service to deal with group workspace information.
	 * 
	 * @param groupWorkspaceService
	 */
	public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}
	
	/**
	 * Get the group workspace id.
	 * 
	 * @return group workspace id
	 */
	public Long getGroupWorkspaceId() {
		return groupWorkspaceId;
	}


	/**
	 * Set the group workspace id.
	 * 
	 * @param groupWorkspaceId - id of the group workspace
	 */
	public void setGroupWorkspaceId(Long groupWorkspaceId) {
		this.groupWorkspaceId = groupWorkspaceId;
	}



}
