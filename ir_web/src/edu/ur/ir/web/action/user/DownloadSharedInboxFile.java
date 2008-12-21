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


package edu.ur.ir.web.action.user;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.file.FileVersion;
import edu.ur.ir.user.SharedInboxFile;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Allows a shared in box file to be downloaded.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DownloadSharedInboxFile extends ActionSupport 
implements ServletResponseAware, ServletRequestAware
{

	/** Eclipse generated id. */
	private static final long serialVersionUID = -4567084083497516308L;
	
	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(DownloadSharedInboxFile.class);
	
	/**  File to download */
	private Long inboxFileId;
	
	/**  Version of the file to download */
	private int versionNumber;
	
	/** Service for accessing user information */
	private UserService userService;
	
	/** file system managagment services */
	private UserFileSystemService userFileSystemService;
	
	/**  Servlet response to write to */
	private HttpServletResponse response;
	
	/**  Servlet request made */
	private HttpServletRequest request;
	
	/** Utility for streaming files */
	private WebIoUtils webIoUtils;

	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
	    log.debug("Trying to download the file to user");
	    
        if (inboxFileId == null) {
        	log.debug("File  id is null");
            return INPUT;
        }
        
        SharedInboxFile sharedInboxFile = userFileSystemService.getSharedInboxFile(inboxFileId, false);
        FileVersion fileVersion = null;
        if( versionNumber != 0)
        {
            fileVersion = sharedInboxFile.getVersionedFile().getVersion(versionNumber);
        }
        else
        {
        	fileVersion = sharedInboxFile.getVersionedFile().getCurrentVersion();
        }

        FileInfo fileInfo = null;
        if( fileVersion != null)
        {
        	fileInfo = fileVersion.getIrFile().getFileInfo();
        }
        else
        {
        	throw new IllegalStateException( "File version for personal file id = " + inboxFileId +
        			" and file version number " + versionNumber + " could not be found");
        }
      
        webIoUtils.StreamFileInfo(fileVersion.getVersionedFile().getName(), fileInfo, response, request, (1024*4), false, true);
        return SUCCESS;
    }
	

	/* (non-Javadoc)
	 * @see com.opensymphony.webwork.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	/**
	 * Get the personal file id
	 * 
	 * @return
	 */
	public Long getInboxFileId() {
		return inboxFileId;
	}


	/**
	 * Set the personal file id.
	 * 
	 * @param inboxFileId
	 */
	public void setInboxFileId(Long inboxFileId) {
		this.inboxFileId = inboxFileId;
	}


	/**
	 * Get the version number for the file 
	 * 
	 * @return
	 */
	public int getVersionNumber() {
		return versionNumber;
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
	 * User service
	 * 
	 * @return
	 */
	public UserService getUserService() {
		return userService;
	}


	/**
	 * Set the user service
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	/**
	 * Get the user file system service.
	 * 
	 * @return
	 */
	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}


	/**
	 * Set the user file system service.
	 * 
	 * @param userFileSystemService
	 */
	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
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

}
