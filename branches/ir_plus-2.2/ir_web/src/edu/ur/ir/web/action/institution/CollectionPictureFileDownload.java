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

package edu.ur.ir.web.action.institution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.web.util.WebIoUtils;

public class CollectionPictureFileDownload extends ActionSupport 
implements ServletResponseAware, ServletRequestAware
{
	
	/** Eclipse generated id*/
	private static final long serialVersionUID = 4286125332502792724L;
	
	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(CollectionPictureFileDownload.class);

	/** Service for dealing with repository */
	private RepositoryService repositoryService;

	/** Service for dealing with insitutional collections */
	private InstitutionalCollectionService institutionalCollectionService;

	/**  Servlet response to write to */
	private HttpServletResponse response;
	
	/** id of the ir file to download */
	private Long irFileId;
	
	/** Id of the institutional collection */
	private Long collectionId;
	
	/** request sent by the user */
	private HttpServletRequest request;
	
	/** Utility to help stream files */
	private WebIoUtils webIoUtils;
	

	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	
    	if( log.isDebugEnabled())
    	{
	        log.debug("Trying to download collection picture with id " + collectionId);
    	}
    	InstitutionalCollection institutionalCollection = null;
    	
	    // make sure this is a picture in the repository - otherwise anyone could get
	    // to the files.
    	if( collectionId != null )
    	{
		  institutionalCollection = 
			institutionalCollectionService.getCollection(collectionId, false);
    	}
		
		if( institutionalCollection != null )
		{
			// causes a load form the database;
			IrFile	irFile = institutionalCollection.getPicture(irFileId);
		   
			log.debug("Found ir file " + irFile);
			if( irFile != null )
			{
                FileInfo fileInfo = irFile.getFileInfo();
        
                if( log.isDebugEnabled() )
                {
                    log.debug("sending file for download " + irFile);
                }
                webIoUtils.StreamFileInfo(fileInfo.getName(), fileInfo, response, request, (1024*4), true, false);
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


	public Long getCollectionId() {
		return collectionId;
	}


	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}


	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}


	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}


	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}


	public WebIoUtils getWebIoUtils() {
		return webIoUtils;
	}


	public void setWebIoUtils(WebIoUtils webIoUtils) {
		this.webIoUtils = webIoUtils;
	}
}
