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
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.simple.type.AscendingNameComparator;

public class ManagePersonNameAuthorityIdentifier extends ActionSupport  implements Preparable, UserIdAware{
	
	private static final long serialVersionUID = -5304256516050272949L;

	//  Logger for managing copyright statements*/
	private static final Logger log = LogManager.getLogger(ManagePersonNameAuthorityIdentifier.class);
	
	// id of the person name authority identifier
	private Long id;
	
	private String value = null;
	
	// id of the data field
	private Long personNameAuthorityId;
	
	// get the list of identifier types
	private List<PersonNameAuthorityIdentifierType> identifierTypes;
	
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
	
	/** User service */
    private UserService userService;

	// identifier type for person name
	private PersonNameAuthorityIdentifierType identifierType;
	
	/** id of the user */
    private Long userId;
	
	/**
	 * Method to create a new person name Authority identifier
	 * 
	 * Create a new copyright statement
	 */
	public String save() {
		log.debug("Save person name authority Identifier  called");
		IrUser user = userService.getUser(userId, false);
		if( user == null || !(user.hasRole(IrRole.AUTHOR_ROLE) || user.hasRole(IrRole.ADMIN_ROLE)) )
		{
		    return "accessDenied";	
		}

		if (value == null || identifierType == null || personNameAuthority == null) {
			message = "Invalid data identifier type = " + identifierType + " value = " + value
					+ " person name authority = " + personNameAuthority;
			addFieldError("personAuthorityidentifierTypeMissingDataError", message);
			return "addError";
		}
		
		PersonNameAuthorityIdentifier existingIdentifier = personService.getByTypeValue(identifierType.getId(),value);
		if(existingIdentifier != null && existingIdentifier.getPersonNameAuthority().getId() != personNameAuthority.getId()) {
			message = "identifier type = " + identifierType + " value = " + value
					+ " already exists for person name authority = " + existingIdentifier.getPersonNameAuthority();
			addFieldError("personAuthorityidentifierTypeMissingDataError", message);
			return "addError";
		}
		// update
		if (personNameAuthorityIdentifier != null) {
			// see if the identifier already exists for the person name
			for (PersonNameAuthorityIdentifier pai : personNameAuthority.getIdentifiers()) {
				if(pai.getId() == personNameAuthorityIdentifier.getId()) {
					pai.setValue(value);
					pai.setPersonNameAuthorityIdentifierType(identifierType);
				}
			}
			personService.save(personNameAuthority);
			
		} else { // add
			
			 
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
		IrUser user = userService.getUser(userId, false);
		if( user == null || !(user.hasRole(IrRole.AUTHOR_ROLE) || user.hasRole(IrRole.ADMIN_ROLE)) )
		{
		    return "accessDenied";	
		}
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
		 
		identifierTypes = defaultPersonNameAuthorityIdentifierTypeService.getAll();
		Collections.sort(identifierTypes, nameComparator);
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
	 
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	 
}