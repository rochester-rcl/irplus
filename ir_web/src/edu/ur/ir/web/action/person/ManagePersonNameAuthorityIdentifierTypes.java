package edu.ur.ir.web.action.person;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.person.PersonNameAuthorityIdentifierType;
import edu.ur.ir.person.service.DefaultPersonNameAuthorityIdentifierTypeService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;

public class ManagePersonNameAuthorityIdentifierTypes  extends Pager implements  Preparable, UserIdAware {
	

	/** Eclipse generated id */
	private static final long serialVersionUID = 8520113497191797635L;

	/** identifier type service */
	private DefaultPersonNameAuthorityIdentifierTypeService defaultPersonNameAuthorityIdentifierTypeService;

	/**  Logger for managing identifier types*/
	private static final Logger log = LogManager.getLogger(ManagePersonNameAuthorityIdentifierTypes.class);
	
	/** Set of identifier types for viewing the identifier types */
	private Collection<PersonNameAuthorityIdentifierType> personNameAuthorityIdentifierTypes;
	
	private PersonNameAuthorityIdentifierType personNameAuthorityIdentifierType = new PersonNameAuthorityIdentifierType();
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the identifier has been added*/
	private boolean added = false;
	
	/** Indicates the identifier types have been deleted */
	private boolean deleted = false;
	
	/** id of the identifier type  */
	private Long id;
	
	/** Set of identifier type ids */
	private long[] personNameAuthorityIdentifierTypeIds;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** Total number of identifier types */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** User id who is making the changes */
	private Long userId;
	
	/** Service for checking user information */
	private UserService userService;
	

	/** Default constructor */
	public ManagePersonNameAuthorityIdentifierTypes() {
	
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Method to create a new person name authority identifier type.
	 * 
	 * Create a new person name identifier type
	 */
	public String create()
	{
		log.debug("creating a person name identifier type = " + personNameAuthorityIdentifierType.getName());
		IrUser user = userService.getUser(userId, false);
		if( user == null || !(user.hasRole(IrRole.AUTHOR_ROLE) || user.hasRole(IrRole.ADMIN_ROLE)) )
		{
		    return "accessDenied";	
		}
		added = false;
		PersonNameAuthorityIdentifierType myIdentifierType = 
			defaultPersonNameAuthorityIdentifierTypeService.get(personNameAuthorityIdentifierType.getName());
		if( myIdentifierType == null)
		{
		    defaultPersonNameAuthorityIdentifierTypeService.save(personNameAuthorityIdentifierType);
		    added = true;
		}
		else
		{
			message = getText("personNameAuthorityIdentifierTypeAlreadyExists", 
					new String[]{personNameAuthorityIdentifierType.getName()});
		}
        return "added"; 
	}
	
	/**
	 * Get a person name identifier type
	 * 
	 * @return the person name identifier type if found
	 */
	public String get()
	{
		personNameAuthorityIdentifierType = defaultPersonNameAuthorityIdentifierTypeService.get(id, false);
		return "get";
	}
	
	/**
	 * Method to update an existing identifier type.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing identifier type id = " + personNameAuthorityIdentifierType.getId());
		added = false;

		PersonNameAuthorityIdentifierType other = 
			defaultPersonNameAuthorityIdentifierTypeService.get(personNameAuthorityIdentifierType.getName());
		

		
		// if the item is found and the id's are not the same
		// then they are trying to rename it to the same name.
		if(other == null || other.getId().equals(personNameAuthorityIdentifierType.getId()))
		{
			defaultPersonNameAuthorityIdentifierTypeService.save(personNameAuthorityIdentifierType);
			added = true;
		}
		else
		{
			message = getText("personNameAuthorityIdentifierTypeAlreadyExists",
					new String[]{personNameAuthorityIdentifierType.getName()});
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
		log.debug("Delete identifier types called");
		if( personNameAuthorityIdentifierTypeIds != null )
		{
		    for(int index = 0; index < personNameAuthorityIdentifierTypeIds.length; index++)
		    {
			    log.debug("Deleting identifier type with id " + personNameAuthorityIdentifierTypeIds[index]);
			    defaultPersonNameAuthorityIdentifierTypeService.delete(defaultPersonNameAuthorityIdentifierTypeService.get(personNameAuthorityIdentifierTypeIds[index], false));
		    }
		}
		deleted = true;
		return "deleted";
	}
 

	
	/**
	 * Get the identifier types table data.
	 * 
	 * @return
	 */
	public String viewPersonNameAuthorityIdentifierTypes()
	{
		rowEnd = rowStart + numberOfResultsToShow;
	    
		personNameAuthorityIdentifierTypes = defaultPersonNameAuthorityIdentifierTypeService.getPersonNameAuthorityIdentifierTypesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = defaultPersonNameAuthorityIdentifierTypeService.getPersonNameAuthorityIdentifierTypesCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;
	}
		
	/**
	 * Get the identifier type service.
	 * 
	 * @return
	 */
	public DefaultPersonNameAuthorityIdentifierTypeService getDefaultPersonNameAuthorityIdentifierTypeService() {
		return defaultPersonNameAuthorityIdentifierTypeService;
	}

	/**
	 * Set the identifier type service.
	 * 
	 * @param personNameAuthorityIdentifierTypeService
	 */
	public void setDefaultPersonNameAuthorityIdentifierTypeService(DefaultPersonNameAuthorityIdentifierTypeService personNameAuthorityIdentifierTypeService) {
		this.defaultPersonNameAuthorityIdentifierTypeService = personNameAuthorityIdentifierTypeService;
	}
	
	/**
	 * List of identifier types for display.
	 * 
	 * @return
	 */
	public Collection<PersonNameAuthorityIdentifierType> getPersonNameAuthorityIdentifierTypes() {
		return personNameAuthorityIdentifierTypes;
	}
	/**
	 * Set the list of identifier types.
	 * 
	 * @param personNameAuthorityIdentifierTypes
	 */
	public void setPersonNameAuthorityIdentifierTypes(Collection<PersonNameAuthorityIdentifierType> personNameAuthorityIdentifierTypes) {
		this.personNameAuthorityIdentifierTypes = personNameAuthorityIdentifierTypes;
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

	public long[] getPersonNameAuthorityIdentifierTypeIds() {
		return personNameAuthorityIdentifierTypeIds;
	}

	public void setPersonNameAuthorityIdentifierTypeIds(long[] personNameAuthorityIdentifierTypeIds) {
		this.personNameAuthorityIdentifierTypeIds = personNameAuthorityIdentifierTypeIds;
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
			personNameAuthorityIdentifierType = defaultPersonNameAuthorityIdentifierTypeService.get(id, false);
		}		
	}

	public PersonNameAuthorityIdentifierType getPersonNameAuthorityIdentifierType() {
		return personNameAuthorityIdentifierType;
	}

	public void setPersonNameAuthorityIdentifierType(PersonNameAuthorityIdentifierType personNameAuthorityIdentifierType) {
		this.personNameAuthorityIdentifierType = personNameAuthorityIdentifierType;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
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

}
