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

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.item.License;
import edu.ur.ir.item.LicenseDAO;

/**
 * Action to add/edit a license
 * 
 * @author Nathan Sarr
 *
 */
public class EditLicense extends ActionSupport implements Preparable {
	
	/**  Generated id. */
	private static final long serialVersionUID = 6482978317871202032L;

	/**  Logger. */
	private static final Logger log = Logger.getLogger(EditLicense.class);
	
	/**  License id for preparable. */
	private Long licenseId;

	/**  License information */
	private License license;

	/**  Item database data access */
	private LicenseDAO licenseDAO;
	

	/**
	 * Load the license information.
	 * 
	 * 
	 * @see com.opensymphony.xwork.Preparable#prepare()
	 */
	public void prepare() throws Exception {
		log.debug("Prepare called");
		if (licenseId != null) {
			license = licenseDAO.getById(licenseId, false);
		}
	}

	/**
	 * Create a new license Info
	 * 
	 * @return {@link #SUCCESS}
	 */
	public String save() {
		log.debug("save called");
		if (license == null) {
			throw new IllegalStateException("License cannot be null");
		}
		licenseDAO.makePersistent(license);
		
		return SUCCESS;
	}
	
	/**
	 * Delete the license type
	 * 
	 * @return
	 */
	public String delete()
	{
		if (license == null) {
			return INPUT;
		}
		licenseDAO.makeTransient(license);
		return SUCCESS;
	}

	/**
	 * Cancel called
	 * 
	 * @return cancel
	 */
	public String cancel() {
		return SUCCESS;
	}
	
	/**
	 * Validate the license information.
	 * 
	 * @see com.opensymphony.xwork.ActionSupport#validate()
	 */
	public void validate()
	{
		// make sure that the license 
		// does not exist in the database
		License other =licenseDAO.getLicense(license.getName(), license.getLicenseVersion());
		
		if( other != null && other.getId() != license.getId() )
		{
			addFieldError("license.name", getText("licenseNameError") + 
					other.getId());
		}
	}

	/**
	 * License object.
	 * 
	 * @return
	 */
	public License getLicense() {
		return license;
	}

	/**
	 * License object.
	 * 
	 * @param license
	 */
	public void setLicense(License license) {
		this.license = license;
	}

	/**
	 * Get license data access.
	 * 
	 * @return
	 */
	public LicenseDAO getLicenseDAO() {
		return licenseDAO;
	}

	/**
	 * Set license data access.
	 * 
	 * @param licenseDAO
	 */
	public void setLicenseDAO(LicenseDAO licenseDAO) {
		this.licenseDAO = licenseDAO;
	}

	/**
	 * Get the license id.
	 * 
	 * @return
	 */
	public Long getLicenseId() {
		return licenseId;
	}

	/**
	 * Set the license id.
	 * 
	 * @param licenseId
	 */
	public void setLicenseId(Long licenseId) {
		this.licenseId = licenseId;
	}


}
