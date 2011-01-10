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
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TransformedFile;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.action.repository.RepositoryThumbnailDownloader;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Get the thumbnail for a personal file.
 * 
 * @author Nathan Sarr
 *
 */
public class PersonalFileThumbnailDownloader extends ActionSupport 
implements ServletResponseAware, ServletRequestAware, UserIdAware{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = -663695133380631311L;

	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(RepositoryThumbnailDownloader.class);

	/**  Servlet response to write to */
	private transient HttpServletResponse response ;
	
	/**  Servlet response to write to */
	private transient HttpServletRequest request;
	
	/** id of the picture */
	private Long personalFileId;
	
	/** id of the user trying to access the information */
	private Long userId;
	
	/** user file service */
	private UserFileSystemService userFileSystemService;
	
	/** id for the researcher */
	private int versionNumber;

	/** Utility to help stream files */
	private WebIoUtils webIoUtils;
	
	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	log.debug("Trying to download the personal file thumbnail to user");
    	
        PersonalFile pf = userFileSystemService.getPersonalFile(personalFileId, false);
        if( !pf.getOwner().getId().equals(userId) )
        {
        	return "accessDenied";
        }
        
        FileVersion version = null;
        if( pf != null)
        {
    	    version = pf.getVersionedFile().getVersion(versionNumber);
        }
    	if( version != null )
    	{
    		IrFile irFile = version.getIrFile();
    		if( irFile != null )
    		{
    			TransformedFile transform = irFile.getTransformedFileBySystemCode(TransformedFileType.PRIMARY_THUMBNAIL);
    			if( transform != null )
    			{
    			    FileInfo info = transform.getTransformedFile();
    	    	    webIoUtils.streamFileInfo(info.getName(), info, response, request, (1024*4), true, false);
    		    }
    		}
    	}
        return SUCCESS;
    }
    
    
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
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


	public void setPersonalFileId(Long personalFileId) {
		this.personalFileId = personalFileId;
	}


	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}


	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}



}


