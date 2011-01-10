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

package edu.ur.ir.web.action.repository;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FileServerService;
import edu.ur.file.mime.MimeTypeService;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.web.util.WebIoUtils;

/**
 * Allows a transformed file to be downloaded
 * 
 * @author Nathan Sarr
 *
 */
public class TransformedFileDownloader extends ActionSupport 
implements ServletResponseAware, ServletRequestAware {
	
	/** Eclipse generated id */
	private static final long serialVersionUID = 1883888648967173415L;

	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(TransformedFileDownloader.class);

	/**  Service for accessing low level file information */
	private FileServerService fileServerService;
	
	/**  Service for dealing with mime information  */
	private MimeTypeService mimeTypeService;
		
	/**  Servlet response to write to */
	private transient HttpServletResponse response ;
	
	/**  Servlet response to write to */
	private transient HttpServletRequest request;
	
	/** Ir File to get the transformed file for */
	private Long irFileId;
	
	/** System code for the transformed file  */
	private String systemCode;
	
	/** repository information */
	private RepositoryService repositoryService;
	
	/** Utility to help stream files */
	private WebIoUtils webIoUtils;
	
	/**
     * Allows a file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	log.debug("Trying to download the file to user");
    	
    	IrFile irFile = repositoryService.getIrFile(irFileId, false);
    	
    	FileInfo info= repositoryService.getTransformByIrFileSystemCode(irFileId,systemCode);
    	if( info != null )
    	{
    		if( irFile.isPublicViewable() )
    		{
    		    webIoUtils.streamFileInfo(info.getName(), info, response, request, (1024*4), true, false);
    		}
    		else
    		{
    			webIoUtils.streamFileInfo(info.getName(), info, response, request, (1024*4), false, false);
    		}
    	}
        
        return SUCCESS;
    	
    }
    

    
    
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}


	public FileServerService getFileServerService() {
		return fileServerService;
	}


	public void setFileServerService(FileServerService fileServerService) {
		this.fileServerService = fileServerService;
	}


	public MimeTypeService getMimeTypeService() {
		return mimeTypeService;
	}


	public void setMimeTypeService(MimeTypeService mimeTypeService) {
		this.mimeTypeService = mimeTypeService;
	}


	public Long getIrFileId() {
		return irFileId;
	}


	public void setIrFileId(Long irFileId) {
		this.irFileId = irFileId;
	}


	public String getSystemCode() {
		return systemCode;
	}


	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
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


}
