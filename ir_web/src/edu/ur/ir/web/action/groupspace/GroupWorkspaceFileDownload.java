/**  
   Copyright 2008 - 2011 University of Rochester

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
import edu.ur.ir.file.FileVersion;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Allows users to download group workspace files.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWorkspaceFileDownload  extends ActionSupport 
implements ServletResponseAware, ServletRequestAware, UserIdAware
{
	/* eclipse generated id*/
	private static final long serialVersionUID = -4196678277252147739L;

	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(GroupWorkspaceFileDownload.class);
	
	/**  File to download */
	private Long groupWorkspaceFileId;

	/** id of the user **/
	private Long userId;
	
	/*  Version of the file to download */
	private int versionNumber;
	
	/* Service for accessing user information */
	private UserService userService;
	
	/* group workpsace file services */
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;
	
	/*  Servlet response to write to */
	private transient HttpServletResponse response;
	
	/*  Servlet request made */
	private transient HttpServletRequest request;
	
	/* Utility for streaming files */
	private WebIoUtils webIoUtils;
	
	/* Service to check and see if user can access the file */
	private SecurityService securityService;


	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
	    log.debug("Trying to download the file to user");
	    
        if (groupWorkspaceFileId == null) {
        	log.debug("File  id is null");
            return "notFound";
        }
        
        GroupWorkspaceFile workspaceFile = groupWorkspaceFileSystemService.getFile(groupWorkspaceFileId, false);
        IrUser user = userService.getUser(userId, false);
        
        //FIX this deal with permissions
        
        if( securityService.hasPermission(workspaceFile.getVersionedFile(), user, VersionedFile.VIEW_PERMISSION) <= 0 )
        {
        	return "accessDenied";
        }
        
        FileVersion fileVersion = null;
        if( versionNumber != 0)
        {
            fileVersion = workspaceFile.getVersionedFile().getVersion(versionNumber);
        }
        else
        {
        	fileVersion = workspaceFile.getVersionedFile().getCurrentVersion();
        }

        FileInfo fileInfo = null;
        if( fileVersion != null)
        {
        	fileInfo = fileVersion.getIrFile().getFileInfo();
        }
        else
        {
        	throw new IllegalStateException( "File version for personal file id = " + groupWorkspaceFileId +
        			" and file version number " + versionNumber + " could not be found");
        }
      
        webIoUtils.streamFileInfo(fileVersion.getVersionedFile().getName(), fileInfo, response, request, (1024*4), false, true);
        return SUCCESS;
    }
	

	/* (non-Javadoc)
	 * @see com.opensymphony.webwork.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	/**
	 * Set the version number for the file
	 * 
	 * @param versionNumber
	 */
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * Set the user service
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	/** id of the user */
	public void injectUserId(Long userId)
	{
		this.userId = userId;
	}

	/**
	 * Set the servlet request.
	 * 
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
	 * Set the group workspace file id.
	 * 
	 * @param groupWorkspaceFileId
	 */
	public void setGroupWorkspaceFileId(Long groupWorkspaceFileId) {
		this.groupWorkspaceFileId = groupWorkspaceFileId;
	}

	/**
	 * Set the group workspace file system service.
	 * 
	 * @param groupWorkspaceFileSystemService
	 */
	public void setGroupWorkspaceFileSystemService(
			GroupWorkspaceFileSystemService groupWorkspaceFileSystemService) {
		this.groupWorkspaceFileSystemService = groupWorkspaceFileSystemService;
	}
	
	/**
	 * Set the security service.
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
}
