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
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TransformedFile;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.action.repository.RepositoryThumbnailDownloader;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Allows researcher thumbnails to be downloaded.
 * 
 * @author Nathan Sarr
 *
 */
public class ResearcherThumbnailDownloader extends ActionSupport 
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
	private Long irFileId;
	
	/** id of the user trying to access the information */
	private Long userId;
	
	/** service for dealing with researchers */
	private ResearcherService researcherService;
	
	/** id for the researcher */
	private Long researcherId;

	/** Utility to help stream files */
	private WebIoUtils webIoUtils;
	
	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	log.debug("Trying to download the instutional collection thumbnail to user");
    	
    	Researcher researcher = researcherService.getResearcher(researcherId, false);
    	
    	if( researcher != null )
    	{
    		if( researcher.isPublic()  || researcher.getUser().getId().equals(userId))
            {	
       	
    			IrFile irFile = researcher.getPicture(irFileId);
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


	public void setIrFileId(Long irFileId) {
		this.irFileId = irFileId;
	}


	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}


	public void setResearcherId(Long researcherId) {
		this.researcherId = researcherId;
	}


	
	public void injectUserId(Long userId) {
		this.userId = userId;
	}

}

