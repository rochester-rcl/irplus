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


package edu.ur.ir.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.db.FileInfo;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Write the repository picture file out to a stream
 * 
 * @author Nathan Sarr
 *
 */
public class RepositoryPictureFileDownload extends ActionSupport 
implements ServletResponseAware, ServletRequestAware
{
	
	/** Eclipse generated id*/
	private static final long serialVersionUID = 4286125332502792724L;
	

	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(RepositoryPictureFileDownload.class);
	
	/**  The new repository */
	private Repository repository;

	/** Service for dealing with repository */
	private RepositoryService repositoryService;
	
	/**  Servlet response to write to */
	private transient HttpServletResponse response;
	
	/** id of the ir file to download */
	private Long irFileId;
	
	/** request made to the server */
	private transient HttpServletRequest request;
	
	/** Utility for web utils */
	private WebIoUtils webIoUtils;
	
	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	
    	if( log.isDebugEnabled())
    	{
	        log.debug("Trying to download repository picture");
    	}
	    
	    // make sure this is a picture in the repository - otherwise anyone could get
	    // to the files.
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		if( repository != null )
		{
			// causes a load form the database;
		    IrFile irFile = repository.getPicture(irFileId);
            FileInfo fileInfo = irFile.getFileInfo();
        
            if( log.isDebugEnabled() )
            {
                log.debug("Found ir File " + irFile);
            }
        
           
             webIoUtils.streamFileInfo(fileInfo.getName(), fileInfo, response, request, (1024*4), true, false);
		}
        	
      
        
        return SUCCESS;
    }
	

	/* (non-Javadoc)
	 * @see com.opensymphony.webwork.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	public Repository getRepository() {
		return repository;
	}


	public void setRepository(Repository repository) {
		this.repository = repository;
	}


	public RepositoryService getRepositoryService() {
		return repositoryService;
	}


	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
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



}
