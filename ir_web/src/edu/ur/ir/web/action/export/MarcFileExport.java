/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.ir.web.action.export;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.marc4j.marc.Record;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.export.MarcExportService;
import edu.ur.ir.export.MrcMarcFileWriter;
import edu.ur.ir.export.XmlMarcFileWriter;
import edu.ur.ir.file.TemporaryFileCreator;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.util.InstitutionalCollectionPermissionHelper;
import edu.ur.ir.web.util.WebIoUtils;



/**
 * Allows for the export to a marc file.
 * 
 * @author Nathan Sarr
 *
 */
public class MarcFileExport extends ActionSupport implements ServletResponseAware, ServletRequestAware, UserIdAware {
	
	/** Eclipse generated Id */
	private static final long serialVersionUID = -3752593484718291238L;

	//  Logger for file upload */
	private static final Logger log = LogManager.getLogger(MarcFileExport.class);
	
	// Servlet response to write to */
	private HttpServletResponse response;
	
	// request made to the server */
	private HttpServletRequest request;
	
	// institutional item version id
	private Long institutionalItemVersionId;
	
	// Service for dealing with institutional item version inforamtion */
	private InstitutionalItemVersionService institutionalItemVersionService;
	
	// Temporary file creator to allow a temporary file to be created for processing */
	private TemporaryFileCreator temporaryFileCreator;
	
	// the marc export service
	private MarcExportService marcExportService;
	
	// id of the user.
	private Long userId;
	
	private String format = "normal";
	
	private InstitutionalCollectionPermissionHelper institutionalCollectionPermissionHelper;

	
	// user service 
	private UserService userService;

	// utilities for 
	WebIoUtils webIoUtils;

	/**
     * Gets the next ir file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String mrc() throws Exception {
    	
    	if( log.isDebugEnabled())
    	{
    		log.debug("get mrc institutional item version  " + institutionalItemVersionId);
    	}
    	
    	IrUser user = null;
    	log.debug("user id = " + userId);
    	if( userId != null )
    	{
    	    user = userService.getUser(userId, false);
    	    log.debug("user = " + user);
    	}
    	
    
    	
    	MrcMarcFileWriter writer = new MrcMarcFileWriter();
    	InstitutionalItemVersion version = institutionalItemVersionService.getInstitutionalItemVersion(institutionalItemVersionId, false);
    	
    	
	    boolean showAllFields = false;
    	
    	if( user != null && (user.hasRole(IrRole.ADMIN_ROLE) || 
    			institutionalCollectionPermissionHelper.isInstitutionalCollectionAdmin(user, version.getItem().getId())))
    	{
    		showAllFields = true;	
    	}
    	else if( version.getItem().isPubliclyViewable() && !version.getItem().isEmbargoed() && !version.isWithdrawn() )
        {
    		showAllFields = true;
        }
        	
    	log.debug("show all fields = " + showAllFields);
    	File f = temporaryFileCreator.createTemporaryFile("mrc");
    	String fileName = "institutional_item_version_" + version.getId(); 
    	
        boolean isRda = false;
    	if( format.equals("rda")){
    		isRda = true;
    	}
    	
    	Record marcRecord = marcExportService.export(version, showAllFields, isRda);
    	writer.writeFile(marcRecord, f);        	
    	webIoUtils.streamFile(fileName, f, "mrc", response, request, (1024*4), false, true);
    	f.delete();
        return SUCCESS;
    }
    
	/**
     * Gets the next ir file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String xml() throws Exception {
    	
    	if( log.isDebugEnabled())
    	{
    		log.debug("get xml institutional item version  " + institutionalItemVersionId);
    	}
    	
    	
    	XmlMarcFileWriter writer = new XmlMarcFileWriter();
    	InstitutionalItemVersion version = institutionalItemVersionService.getInstitutionalItemVersion(institutionalItemVersionId, false);
    	File f = temporaryFileCreator.createTemporaryFile("xml");
    	String fileName = "institutional_item_version_" + version.getId(); 
    	
    	IrUser user = null;
    	log.debug("user id = " + userId);
    	if( userId != null )
    	{
    	    user = userService.getUser(userId, false);
    	    log.debug("user = " + user);
    	}
    	
    	boolean showAllFields = false;
     	
     	if( user!= null && (user.hasRole(IrRole.ADMIN_ROLE) || 
     			institutionalCollectionPermissionHelper.isInstitutionalCollectionAdmin(user, version.getItem().getId())))
     	{
     		log.debug("user is admin");
     		showAllFields = true;	
     	}
     	else if( version.getItem().isPubliclyViewable() && !version.getItem().isEmbargoed() && !version.isWithdrawn() )
        {
     		showAllFields = true;
        }
    	
    	log.debug("show all fields = " + showAllFields);
    	
    	
    	
    	Record marcRecord = marcExportService.export(version, showAllFields, format.equals("rda"));
    	writer.writeFile(marcRecord, f);        	
    	webIoUtils.streamFile(fileName, f, "xml", response, request, (1024*4), false, true);
    	f.delete();
        return SUCCESS;
    }
    

	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.webwork.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}
	
	public void setWebIoUtils(WebIoUtils webIoUtils) {
		this.webIoUtils = webIoUtils;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}


	public void setTemporaryFileCreator(TemporaryFileCreator temporaryFileCreator) {
		this.temporaryFileCreator = temporaryFileCreator;
	}
	
	/**
	 * Set the institutional item version.
	 * 
	 * @param institutionalItemVersionId
	 */
	public void setInstitutionalItemVersionId(Long institutionalItemVersionId) {
		this.institutionalItemVersionId = institutionalItemVersionId;
	}

	/**
	 * Set the marc export service.
	 * 
	 * @param marcExportService
	 */
	public void setMarcExportService(MarcExportService marcExportService) {
		this.marcExportService = marcExportService;
	}

	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	public void setInstitutionalCollectionPermissionHelper(
			InstitutionalCollectionPermissionHelper institutionalCollectionPermissionHelper) {
		this.institutionalCollectionPermissionHelper = institutionalCollectionPermissionHelper;
	}



}
