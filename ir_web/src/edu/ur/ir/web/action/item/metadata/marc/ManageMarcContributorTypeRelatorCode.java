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

package edu.ur.ir.web.action.item.metadata.marc;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.marc.MarcContributorTypeRelatorCode;
import edu.ur.ir.marc.MarcContributorTypeRelatorCodeService;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeService;
import edu.ur.metadata.marc.MarcRelatorCode;
import edu.ur.metadata.marc.MarcRelatorCodeService;
import edu.ur.simple.type.AscendingNameComparator;

/**
 * Manage the relator codes 
 * 
 * @author Nathan Sarr
 *
 */
public class ManageMarcContributorTypeRelatorCode extends ActionSupport 
    implements Comparator<MarcContributorTypeRelatorCode>{
	
	//eclipse generated id
	private static final long serialVersionUID = -8258929517165194606L;
	
	//  Logger for managing copyright statements*/
	private static final Logger log = Logger.getLogger(ManageMarcContributorTypeRelatorCode.class);
	
	// id of the contributor type relator code
	private Long id;
	
	// id of the relator code
	private Long relatorCodeId;

	// current marc contributor type relator code
    private MarcContributorTypeRelatorCode marcContributorTypeRelatorCode;

	// id of the contributor type id
	private Long contributorTypeId;
	
	// contributor type service
	private ContributorTypeService contributorTypeService;
	
	// service to deal with marc relator code information
	private MarcRelatorCodeService marcRelatorCodeService;

	// marc contributor type relator code service
	private MarcContributorTypeRelatorCodeService marcContributorTypeRelatorCodeService;

	// Used for sorting name based entities 
	private AscendingNameComparator nameComparator = new AscendingNameComparator();
	
    // Message that can be displayed to the user. */
	private String message;
	


	/**
	 * Method to create a new copyright statement.
	 * 
	 * Create a new copyright statement
	 */
	public String save()
	{
		log.debug("Save called");
		ContributorType contributorType = null;
		MarcRelatorCode marcRelatorCode = null;
		
		if( contributorTypeId != null )
		{
		    contributorType = contributorTypeService.get(contributorTypeId, false);
		}
		
		if( relatorCodeId != null )
		{
		    marcRelatorCode = marcRelatorCodeService.getById(relatorCodeId, false);
		}
		
		if( contributorType == null || marcRelatorCode == null )
		{
		    message = getText("contentTypeMarcMappingMissingDataError");
		    addFieldError("contentTypeMarcMappingMissingDataError", message);
		    return "addError";
		}
		
		
		MarcContributorTypeRelatorCode other = marcContributorTypeRelatorCodeService.getByContributorTypeId(contributorTypeId);
		
		if(id != null)
		{
			marcContributorTypeRelatorCode = marcContributorTypeRelatorCodeService.getById(id,false);
		}
		
		if( marcContributorTypeRelatorCode != null )
		{
			if(other == null ||  marcContributorTypeRelatorCode.getId().equals(other.getId()))
			{
				marcContributorTypeRelatorCode.setContributorType(contributorType);
				marcContributorTypeRelatorCode.setMarcRelatorCode(marcRelatorCode);
				marcContributorTypeRelatorCodeService.save(marcContributorTypeRelatorCode);
			}
			else
			{
				message = getText("marcContributorTypeRelatorCodeExistsError");
				addFieldError("marcContributorTypeRelatorCodeAlreadyExists", message);
				return "addError";
			}
		}
		else
		{
			// new contributor type relator code
			if( other != null )
			{
				message = getText("marcContributorTypeRelatorCodeExistsError");
				addFieldError("marcContributorTypeRelatorCodeAlreadyExists", message);
				return "addError";
			}
			else
			{
				marcContributorTypeRelatorCode = new MarcContributorTypeRelatorCode(marcRelatorCode, contributorType);
			    marcContributorTypeRelatorCodeService.save(marcContributorTypeRelatorCode);
			}
		}
		
		return "added";
	}
	
	/**
	 * Delete the contributor type code.
	 * 
	 * @return
	 */
	public String delete()
	{
		marcContributorTypeRelatorCode = marcContributorTypeRelatorCodeService.getById(id,false);
		if(marcContributorTypeRelatorCode != null)
		{
			 marcContributorTypeRelatorCodeService.delete(marcContributorTypeRelatorCode);
		}
		return "deleted";
	}
	
	/**
	 * Edit the selected element.
	 * 
	 * @return
	 */
	public String edit()
	{
		if( id != null )
		{
			marcContributorTypeRelatorCode = marcContributorTypeRelatorCodeService.getById(id,false);
		}
		return "edit";
	}
	
	public List<MarcContributorTypeRelatorCode> getMarcContributorTypeRelatorCodes()
	{
		List<MarcContributorTypeRelatorCode> contributorTypeRelatorCodes = marcContributorTypeRelatorCodeService.getAll();
	    Collections.sort(contributorTypeRelatorCodes, this);
	    return contributorTypeRelatorCodes;
	}
	
	/**
	 * Get the marc relator codes
	 * 
	 * @return
	 */
	public  List<MarcRelatorCode> getMarcRelatorCodes()
	{
		List<MarcRelatorCode> relatorCodes = marcRelatorCodeService.getAll();
		Collections.sort(relatorCodes, nameComparator);
		return relatorCodes;
	}
	
	/**
	 * Get the contributor types in IR+
	 * 
	 * @return
	 */
	public  List<ContributorType> getContributorTypes()
	{
		List<ContributorType> contributorTypes = contributorTypeService.getAll();
		Collections.sort(contributorTypes, nameComparator);
		return contributorTypes;
	}
	
	public String execute()
	{
	    return SUCCESS;
	}
	
	/**
	 * Get the relator code id.
	 * 
	 * @return
	 */
	public Long getRelatorCodeId() {
		return relatorCodeId;
	}

	/**
	 * Set the relator code id.
	 * 
	 * @param relatorCodeId
	 */
	public void setRelatorCodeId(Long relatorCodeId) {
		this.relatorCodeId = relatorCodeId;
	}

	/**
	 * Get the contributor type id.
	 * 
	 * @return
	 */
	public Long getContributorTypeId() {
		return contributorTypeId;
	}

	/**
	 * Set the id.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Get the current marc contributor type relator code.
	 * 
	 * @return
	 */
	public MarcContributorTypeRelatorCode getMarcContributorTypeRelatorCode() {
		return marcContributorTypeRelatorCode;
	}
	
	/**
	 * Set the contributor type service.
	 * 
	 * @param contributorTypeService
	 */
	public void setContributorTypeService(
			ContributorTypeService contributorTypeService) {
		this.contributorTypeService = contributorTypeService;
	}

	/**
	 * Set the marc contributor type relator code service.
	 * 
	 * @param marcContributorTypeRelatorCodeService
	 */
	public void setMarcContributorTypeRelatorCodeService(
			MarcContributorTypeRelatorCodeService marcContributorTypeRelatorCodeService) {
		this.marcContributorTypeRelatorCodeService = marcContributorTypeRelatorCodeService;
	}
	
	/**
	 * Set the marc relator code service.
	 * 
	 * @param marcRelatorCodeService
	 */
	public void setMarcRelatorCodeService(
			MarcRelatorCodeService marcRelatorCodeService) {
		this.marcRelatorCodeService = marcRelatorCodeService;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(MarcContributorTypeRelatorCode o1,
			MarcContributorTypeRelatorCode o2) {
		return o1.getContributorType().getName().compareToIgnoreCase(o2.getContributorType().getName());
	}


	/**
	 * Set the contributor id.
	 * 
	 * @param contributorTypeId
	 */
	public void setContributorTypeId(Long contributorTypeId) {
		this.contributorTypeId = contributorTypeId;
	}
	
	/**
	 * Get the message.
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}
}
