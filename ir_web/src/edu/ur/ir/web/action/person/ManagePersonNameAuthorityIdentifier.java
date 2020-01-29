package edu.ur.ir.web.action.person;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonNameAuthorityIdentifier;
import edu.ur.ir.person.PersonNameAuthorityIdentifierType;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.person.service.DefaultPersonNameAuthorityIdentifierTypeService;
import edu.ur.simple.type.AscendingNameComparator;

public class ManagePersonNameAuthorityIdentifier extends ActionSupport  implements Preparable{
	
	private static final long serialVersionUID = -5304256516050272949L;

	//  Logger for managing copyright statements*/
	private static final Logger log = LogManager.getLogger(ManagePersonNameAuthorityIdentifier.class);
	
	// id of the person name authority identifier
	private Long id;
	
	private String value = null;
	
	// id of the data field
	private Long personNameAuthorityId;
	
	// id of the person name authority identifier type
	private Long personNameAuthorityIdentifierTypeId;

	// service for the person name
	private PersonService personService; 
	
	//person name authority
	private PersonNameAuthority personNameAuthority;
	
	// person name authority identifier
	private PersonNameAuthorityIdentifier personNameAuthorityIdentifier = null; 

	// identifier type service.
	private DefaultPersonNameAuthorityIdentifierTypeService defaultPersonNameAuthorityIdentifierTypeService;
	
    // Message that can be displayed to the user. */
	private String message;
	
	// Used for sorting name based entities 
	private AscendingNameComparator nameComparator = new AscendingNameComparator();
	
	// identifier type for person name
	private PersonNameAuthorityIdentifierType identifierType;
	
	/**
	 * Method to create a new person name Authority identifier
	 * 
	 * Create a new copyright statement
	 */
	public String save() {
		log.debug("Save person name authority Identifier  called");

		if (value == null || identifierType == null || personNameAuthority == null) {
			message = "Invalid data identifier type = " + identifierType + " value = " + value
					+ " person name authority = " + personNameAuthority;
			addFieldError("personAuthorityidentifierTypeMissingDataError", message);
			return "addError";
		}

		// update
		if (personNameAuthorityIdentifier != null) {
			// see if the identifier already exists for the person name
			for (PersonNameAuthorityIdentifier pai : personNameAuthority.getIdentifiers()) {
				if(pai.getId() == personNameAuthorityIdentifier.getId()) {
					pai.setValue(value);
				}
			}
			personService.save(personNameAuthority);
			
		} else { // add
			
			if (personService.getByTypeAuthority(identifierType.getId(), personNameAuthority.getId()) != null) {
				throw new IllegalStateException("trying to add an existing peronal authority identifier "
						+ identifierType.getId() + " name id = " + personNameAuthority.getId());
			}
			personNameAuthority.addIdentifier(value, identifierType);
			personService.save(personNameAuthority);
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
		personNameAuthority.removeIdentifier(personNameAuthorityIdentifier);
		personService.save(personNameAuthority);
		return "deleted";
	}
	
	/**
	 * Edit the selected element.
	 * 
	 * @return
	 */
	public String edit()
	{
		
		return "edit";
	}
	

	public String execute()
	{
	    return SUCCESS;
	}
	
	public void prepare() throws Exception {
		if( id != null)
		{
			personNameAuthorityIdentifier = personService.getPersonNameAuthorityIdentifier(id, false);
			personNameAuthority = personNameAuthorityIdentifier.getPersonNameAuthority();
			identifierType = personNameAuthorityIdentifier.getPersonNameAuthorityIdentifierType();
		}
		
		if (personNameAuthority == null && personNameAuthorityId != null)
		{
			personNameAuthority = personService.getAuthority(personNameAuthorityId, false);
		}
		
		if(personNameAuthorityIdentifierTypeId != null) {
			identifierType = defaultPersonNameAuthorityIdentifierTypeService.get(personNameAuthorityIdentifierTypeId, false);
		}
	}

	
	/**
	 * Get the id for the identifier type sub field.
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the id for the identifier type sub field.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the message.
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}
	 
	/**
	 * Ascending sort of identifier types by name.
	 * 
	 * @return - list of sorted name identifiers
	 */
	public List<PersonNameAuthorityIdentifierType> getIdentifierTypes()
	{
		List<PersonNameAuthorityIdentifierType> identifierTypes = personService.getPossibleIdentifierTypes(personNameAuthorityId);
		Collections.sort(identifierTypes, nameComparator);
		return identifierTypes;
	}

	public PersonNameAuthority getPersonNameAuthority() {
		return personNameAuthority;
	}

	public void setPersonNameAuthority(PersonNameAuthority personNameAuthority) {
		this.personNameAuthority = personNameAuthority;
	}

	public PersonService getPersonService() {
		return personService;
	}

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public DefaultPersonNameAuthorityIdentifierTypeService getDefaultPersonNameAuthorityIdentifierTypeService() {
		return defaultPersonNameAuthorityIdentifierTypeService;
	}

	public void setDefaultPersonNameAuthorityIdentifierTypeService(
			DefaultPersonNameAuthorityIdentifierTypeService defaultPersonNameAuthorityIdentifierTypeService) {
		this.defaultPersonNameAuthorityIdentifierTypeService = defaultPersonNameAuthorityIdentifierTypeService;
	}
	
	public Long getPersonNameAuthorityId() {
		return personNameAuthorityId;
	}

	public void setPersonNameAuthorityId(Long personNameAuthorityId) {
		this.personNameAuthorityId = personNameAuthorityId;
	}

	public PersonNameAuthorityIdentifier getPersonNameAuthorityIdentifier() {
		return personNameAuthorityIdentifier;
	}

	public void setPersonNameAuthorityIdentifier(PersonNameAuthorityIdentifier personNameAuthorityIdentifier) {
		this.personNameAuthorityIdentifier = personNameAuthorityIdentifier;
	}
	
	public Long getPersonNameAuthorityIdentifierTypeId() {
		return personNameAuthorityIdentifierTypeId;
	}

	public void setPersonNameAuthorityIdentifierTypeId(Long personNameAuthorityIdentifierTypeId) {
		this.personNameAuthorityIdentifierTypeId = personNameAuthorityIdentifierTypeId;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	 
	
	 
}