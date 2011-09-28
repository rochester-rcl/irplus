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
import edu.ur.ir.file.TransformedFile;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.web.action.repository.RepositoryThumbnailDownloader;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Class to allow an institutional collection thumbnails to be downloaded
 * 
 * @author Nathan Sarr
 *
 */
public class InstitutionalCollectionThumbnailDownloader extends ActionSupport 
implements ServletResponseAware, ServletRequestAware {
	
	/** Eclipse generated id */
	private static final long serialVersionUID = 8343254141851201752L;
	
	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(RepositoryThumbnailDownloader.class);

	/**  Servlet response to write to */
	private HttpServletResponse response ;
	
	/**  Servlet response to write to */
	private HttpServletRequest request;
	
	/** id of the collection */
	private Long collectionId;
	
	/** id of the picture for the collection */
	private Long irFileId;
	
	/** service for dealing with institutional collections */
	private InstitutionalCollectionService institutionalCollectionService;

	/** Utility to help stream files */
	private WebIoUtils webIoUtils;
	
	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	log.debug("Trying to download the instutional collection thumbnail to user");
    	
    	
    	if(collectionId != null )
    	{
    		InstitutionalCollection collection = institutionalCollectionService.getCollection(collectionId, false);
    		if( collection != null )
    		{
    			IrFile irFile = collection.getPicture(irFileId);
    			
    			// do not send anything if the fils is not part of the collection's pictures
    			if( irFile != null )
    			{
    			    TransformedFile transform = irFile.getTransformedFileBySystemCode(TransformedFileType.PRIMARY_THUMBNAIL);
    			    if( transform != null )
    			    {
    				    FileInfo info = transform.getTransformedFile();
    				    if( irFile.isPublicViewable() )
    	    		    {
    	    		        webIoUtils.StreamFileInfo(info.getName(), info, response, request, (1024*4), true, false);
    	    		    }
    	    		    else
    	    		    {
    	    			    webIoUtils.StreamFileInfo(info.getName(), info, response, request, (1024*4), false, false);
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


	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}


	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}


	public void setIrFileId(Long irFileId) {
		this.irFileId = irFileId;
	}

}
