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
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Write the researcher picture file out to a stream
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ResearcherPictureFileDownload extends ActionSupport 
implements ServletResponseAware, ServletRequestAware, UserIdAware
{
	
	/** Eclipse generated id*/
	private static final long serialVersionUID = -6039053858718096614L;	

	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(ResearcherPictureFileDownload.class);
	
	/**  The new researcher */
	private Researcher researcher;
	
	/** Id of researcher */
	private Long researcherId;

	/** Service for dealing with researcher */
	private ResearcherService researcherService;
	
	/**  Servlet response to write to */
	private transient HttpServletResponse response;
	
	/** id of the ir file to download */
	private Long irFileId;
	
	/** request made to the server */
	private transient HttpServletRequest request;
	
	/** Utility for web utils */
	private WebIoUtils webIoUtils;
	
	/** id of the user accessing the file */
	private Long userId;

	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	
    	if( log.isDebugEnabled())
    	{
	        log.debug("Trying to download researcher picture");
    	}
	    
	    // make sure this is a picture in the researcher - otherwise anyone could get
	    // to the files.
		researcher = researcherService.getResearcher(researcherId, false);
		if( researcher != null && 
				(researcher.isPublic() || researcher.getUser().getId().equals(userId)))
		{
			// causes a load form the database;
		    IrFile irFile = researcher.getPicture(irFileId);
		    
		    if (irFile == null) {
		        if (researcher.getPrimaryPicture().getId().equals(irFileId)) {
		    	    irFile = researcher.getPrimaryPicture();
		        }
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

	public Researcher getResearcher() {
		return researcher;
	}


	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}


	public ResearcherService getResearcherService() {
		return researcherService;
	}


	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}

	public Long getIrFileId() {
		return irFileId;
	}


	public void setIrFileId(Long irFileId) {
		this.irFileId = irFileId;
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


	public Long getResearcherId() {
		return researcherId;
	}

	public void setResearcherId(Long researcherId) {
		this.researcherId = researcherId;
	}

	public void injectUserId(Long userId) {
		this.userId = userId;
	}


}
