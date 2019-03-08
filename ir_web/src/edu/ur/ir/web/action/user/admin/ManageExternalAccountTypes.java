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

package edu.ur.ir.web.action.user.admin;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.user.ExternalAccountType;
import edu.ur.ir.user.ExternalAccountTypeService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.simple.type.AscendingNameComparator;

/**
 * Way to manage external account types.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageExternalAccountTypes extends ActionSupport implements  Preparable, UserIdAware
{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = 6486286857365164721L;

	/** external account type service */
	private ExternalAccountTypeService externalAccountTypeService;

	/**  Logger for managing external account types*/
	private static final Logger log = LogManager.getLogger(ManageExternalAccountTypes.class);
	
	/** Set of external account types for viewing the external account types */
	private List<ExternalAccountType> externalAccountTypes;
	
	/** External account type */
	private ExternalAccountType externalAccountType;
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the external account has been added*/
	private boolean added = false;
	
	/** Indicates the external account types have been deleted */
	private boolean deleted = false;
	
	/** id of the external account type  */
	private Long id;
	
	/** User id who is making the changes */
	private Long userId;
	
	/** Service for checking user information */
	private UserService userService;
	
	/** Used for sorting name based entities */
	private AscendingNameComparator nameComparator = new AscendingNameComparator();
	
	/** Define case sensitive ase false */
	boolean caseSensitive = false;
	

	/**
	 * Method to create a new external account type.
	 * 
	 * Create a new external account type
	 */
	public String create()
	{
		log.debug("creating an external account type = " + externalAccountType.getName());
		IrUser user = userService.getUser(userId, false);
		if( user == null || (!user.hasRole(IrRole.ADMIN_ROLE)) )
		{
		    return "accessDenied";	
		}
		added = false;
		
		ExternalAccountType myExternalAccountType = 
			externalAccountTypeService.get(externalAccountType.getName());
		if( myExternalAccountType == null)
		{
			externalAccountType.setUserNameCaseSensitive(caseSensitive);
		    externalAccountTypeService.save(externalAccountType);
		    added = true;
		}
		else
		{
			message = getText("externalAccountTypeAlreadyExists", 
					new String[]{externalAccountType.getName()});
		}
        return "added";
	}
	
	/**
	 * Get an external account type
	 * 
	 * @return the external account type
	 */
	public String get()
	{
		IrUser user = userService.getUser(userId, false);
		if( user == null || (!user.hasRole(IrRole.ADMIN_ROLE)) )
		{
		    return "accessDenied";	
		}
		externalAccountType = externalAccountTypeService.get(id, false);
		return "get";
	}
	
	/**
	 * Method to update an existing external account type.
	 * 
	 * @return
	 */
	public String update()
	{
		IrUser user = userService.getUser(userId, false);
		if( user == null || (!user.hasRole(IrRole.ADMIN_ROLE)) )
		{
		    return "accessDenied";	
		}
		log.debug("updateing external account type id = " +  externalAccountType.getId());
		added = false;

		ExternalAccountType other = 
			externalAccountTypeService.get(externalAccountType.getName());
		
		// if the item is found and the id's are not the same
		// then they are trying to rename it to the same name.
		if(other == null || other.getId().equals(externalAccountType.getId()))
		{
			externalAccountType.setUserNameCaseSensitive(caseSensitive);
			externalAccountTypeService.save(externalAccountType);
			added = true;
		}
		else
		{
			message = getText("externalAccountTypeAlreadyExists",
					new String[]{externalAccountType.getName()});
		}
        return "added";
	}
	
	/**
	 * Removes the selected items and collections.
	 * 
	 * @return
	 */
	public String delete()
	{
		IrUser user = userService.getUser(userId, false);
		if( user == null || (!user.hasRole(IrRole.ADMIN_ROLE)) )
		{
		    return "accessDenied";	
		}
		log.debug("Delete external account type called");
		externalAccountType = externalAccountTypeService.get(id, false);
		if( externalAccountType != null )
		{
		    externalAccountTypeService.delete(externalAccountType);
		}
		deleted = true;
		return "deleted";
	}
	
	/**
	 * Set the case sensitive value.
	 * 
	 * @param caseSenstive
	 */
	public void setCaseSensitive(boolean caseSensitive)
	{
	    this.caseSensitive = caseSensitive;	
	}
	
	/**
	 * Get the external account types table.
	 * 
	 * @return
	 */
	public String getExternalAccountTypesTable()
	{
		
		externalAccountTypes = externalAccountTypeService.getAll();
		Collections.sort(externalAccountTypes, nameComparator);
		return SUCCESS;
	}
	
	/**
	 * Get the external account types table data.
	 * 
	 * @return
	 */
	public String viewExternalAccountTypes()
	{
		
		externalAccountTypes = externalAccountTypeService.getAll();
		Collections.sort(externalAccountTypes, nameComparator);
		return SUCCESS;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isAdded() {
		return added;
	}
	public void setAdded(boolean added) {
		this.added = added;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	
	public void prepare() throws Exception {
		if( id != null)
		{
			externalAccountType = externalAccountTypeService.get(id, false);
		}
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Collection<ExternalAccountType> getExternalAccountTypes() {
		return externalAccountTypes;
	}

	
	public ExternalAccountType getExternalAccountType() {
		return externalAccountType;
	}

	public void setExternalAccountTypeService(
			ExternalAccountTypeService externalAccountTypeService) {
		this.externalAccountTypeService = externalAccountTypeService;
	}

	public void setExternalAccountType(ExternalAccountType externalAccountType) {
		this.externalAccountType = externalAccountType;
	}

}
