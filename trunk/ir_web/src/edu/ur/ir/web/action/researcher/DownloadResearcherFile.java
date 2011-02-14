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


package edu.ur.ir.web.action.researcher;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.web.util.WebIoUtils;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Allows a researcher file to be downloaded.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DownloadResearcherFile extends ActionSupport 
implements ServletResponseAware, ServletRequestAware, UserIdAware
{
	/** Service for dealing with researcher file system information */
	private ResearcherFileSystemService researcherFileSystemService;

	/** Eclipse generated id. */
	private static final long serialVersionUID = 5430030320610916010L;

	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(DownloadResearcherFile.class);
	
	/**  File to download */
	private Long researcherFileId;


	/**  Servlet response to write to */
	private transient HttpServletResponse response;
	
	/**  Servlet request made */
	private transient HttpServletRequest request;
	
	/** id of the user downloading the file**/
	private Long userId;

	private WebIoUtils webIoUtils;

	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
	    log.debug("Trying to download the file to user");
	    
        if (researcherFileId == null) {
        	log.debug("File  id is null");
        	return "notFound";
        }
        
        ResearcherFile researcherFile = researcherFileSystemService.getResearcherFile(researcherFileId, false);
        
        if( researcherFile == null )
        {
        	return "notFound";
        }
        
        Researcher researcher = researcherFile.getResearcher();
        
        if( researcher.isPublic()  || researcher.getUser().getId().equals(userId))
        {	
            FileInfo fileInfo =  researcherFile.getIrFile().getFileInfo();
            webIoUtils.streamFileInfo(researcherFile.getName(), fileInfo, response, request, (1024*4), false, true);
            return SUCCESS;
        }
        else
        {
        	return "accessDenied";
        }
    }
	

	/* (non-Javadoc)
	 * @see com.opensymphony.webwork.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	/**
	 * Get the researcher file id
	 * 
	 * @return
	 */
	public Long getResearcherFileId() {
		return researcherFileId;
	}

	/**
	 * Set the researcher file id.
	 * 
	 * @param researcherFileId
	 */
	public void setResearcherFileId(Long researcherFileId) {
		this.researcherFileId = researcherFileId;
	}

	/**
	 * Get the user id.
	 * 
	 * @param userId
	 */
	public void injectUserId(Long userId)
	{
		this.userId = userId;
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


	public ResearcherFileSystemService getResearcherFileSystemService() {
		return researcherFileSystemService;
	}


	public void setResearcherFileSystemService(
			ResearcherFileSystemService researcherFileSystemService) {
		this.researcherFileSystemService = researcherFileSystemService;
	}

}
