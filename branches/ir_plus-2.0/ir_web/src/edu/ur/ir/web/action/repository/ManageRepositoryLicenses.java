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


import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.repository.License;
import edu.ur.ir.repository.LicenseService;
import edu.ur.ir.repository.LicenseVersion;
import edu.ur.ir.repository.VersionedLicense;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;


/**
 * Allows a user to manage repository licenses.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageRepositoryLicenses extends ActionSupport implements UserIdAware{

	/** eclipse generated id */
	private static final long serialVersionUID = -2115322382820478264L;
	
	/**  Logger. */
	private static final Logger log = Logger.getLogger(ManageRepositoryLicenses.class);
	
	/** Service for dealing with licenses */
	private LicenseService licenseService;
	
	/** Service for dealing with users */
	private UserService userService;
	
	/**  Name of the license */
	private String name;
	
	/** Text for the license */
	private String text;
	
	/** description of the changes made */
	private String description;
	
	/** id of the user */
	private Long userId;
	
	/** Set of versioned licenses for the repository */
	private List<VersionedLicense> licenses;
	
	/** id for a particular versioned license. */
	private Long versionedLicenseId;
	
	/** a particular version of a versioned license*/
	private int version;
	
	/** License version to view */
	private LicenseVersion licenseVersion;
	
	/** Particular versioned license  */
	private VersionedLicense versionedLicense;
	
    /**
     * Save the specified license.
     * 
     * @return
     */
    public String saveNew()
    {
    	IrUser user = userService.getUser(userId, false);
    	
    	if( name == null || name.trim().equals(""))
    	{
    		String message = getText("licenseNameRequiredError");
    		this.addFieldError("name", message);
    		return "saveNewInput";
    	}
    	

    	
    	licenseService.createLicense(user, text, name, description);
    	return SUCCESS;
    }
    
    /**
     * Add a new license version.
     * 
     * @return
     */
    public String addNewLicenseVersion()
    {
 
    	versionedLicense = licenseService.get(versionedLicenseId, false);
    	return SUCCESS;
    }
    
    /**
     * Save a new version of a license
     * @return
     */
    public String saveNewVersion()
    {
       	versionedLicense = licenseService.get(versionedLicenseId, false);
    	IrUser user = userService.getUser(userId, false);
 
       	if( name == null || name.trim().equals(""))
    	{
    		String message = getText("licenseNameRequiredError");
    		this.addFieldError("name", message);
    		return "newVersionInput";
    	}
    	
    	licenseVersion = versionedLicense.addNewVersion(text, user);
    	versionedLicense.setName(name);
    	License license = licenseVersion.getLicense();
    	license.setName(name);
    	license.setDescription(description);
    	licenseService.save(versionedLicense);
    	return SUCCESS;
    }
    
    /**
     * View a specific versioned license
     * @return
     */
    public String viewVersionedLicense()
    {
    	if( versionedLicenseId != null)
    	{
    	    versionedLicense = licenseService.get(versionedLicenseId, false);
    	}
    	return SUCCESS;
    }
    
    /**
     * Get a particular version of a versioned license
     * @return
     */
    public String viewLicenseVersion()
    {
    	log.debug("view license version");
    	if( versionedLicenseId != null)
    	{
    	    versionedLicense = licenseService.get(versionedLicenseId, false);
    	    log.debug("versioned license = " + versionedLicense );
    	    
    	    log.debug("version = " + version);
    	    if( versionedLicense != null )
    	    {
    	        licenseVersion = versionedLicense.getByVersionNumber(version);
    	        log.debug("license version = " + licenseVersion );
    	    }
    	}
    	
    	return SUCCESS;
    }
    
    /**
     * Get all available licenses.
     * @return
     */
    public String viewLicenses()
    {
    	licenses = licenseService.getAll();
    	return SUCCESS;
    }
    
    /**
     * Add a new repository license.
     * 
     * @return - add new
     */
    public String add()
    {
    	return "addNew";
    }
    
    public String delete()
    {
    	return "";
    }

	public LicenseService getLicenseService() {
		return licenseService;
	}

	public void setLicenseService(LicenseService licenseService) {
		this.licenseService = licenseService;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<VersionedLicense> getLicenses() {
		return licenses;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getVersionedLicenseId() {
		return versionedLicenseId;
	}

	public void setVersionedLicenseId(Long versionedLicenseId) {
		this.versionedLicenseId = versionedLicenseId;
	}

	public VersionedLicense getVersionedLicense() {
		return versionedLicense;
	}

	public void setVersionedLicense(VersionedLicense versionedLicense) {
		this.versionedLicense = versionedLicense;
	}

	public LicenseVersion getLicenseVersion() {
		return licenseVersion;
	}

	public void setLicenseVersion(LicenseVersion licenseVersion) {
		this.licenseVersion = licenseVersion;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
