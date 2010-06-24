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

package edu.ur.ir.web.action.person;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorService;
import edu.ur.ir.person.NameAuthorityIndexService;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;
import edu.ur.ir.person.PersonService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserIndexService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;


/**
 * This allows for the management of person names.
 * 
 * @author Nathan Sarr
 *
 */
public class ManagePersonNames extends ActionSupport implements   Preparable, UserIdAware{
	
	/** eclipse generated id. */
	private static final long serialVersionUID = 9067167667985194047L;

	/** person service */
	private PersonService personService;
	
	/** item service */
	private ItemService itemService;

	/** Name index service */
	private NameAuthorityIndexService nameAuthorityIndexService;
	
	/** Repository service */
	private RepositoryService repositoryService;

	/** total number of persons */
	private Integer totalNumberOfPersonNames;
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(ManagePersonNames.class);
	
	/** Set of persons for viewing  */
	private Collection<PersonName> personNames;
	
	/** person loaded  for editing*/
	private PersonNameAuthority personNameAuthority;

	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the person name has been added*/
	private boolean added = false;
	
	/** Indicates the person names have been deleted */
	private boolean deleted = false;
	
	/** id of the person */
	private Long personId;
	
	/** Set of person name ids */
	private long[] personNameIds;
	
	/** Name of person being edited. */
	private PersonName personName = new PersonName();
	
	/**  indicates if the person name is authoritative */
	private boolean authoritative = false;
	
	/** Date person was born  */
	private String birthDate;
	
	/** Date person died */
    private String deathDate;
    
    /** id of the person name  */
    private Long id;
    
    /** User index service */
    private UserIndexService userIndexService;
    
    /** User service */
    private UserService userService;
    
    /** id of the user */
    private Long userId;
    
    private int start = 0;
    
    private int maxResults = 25;
    
    /** service for dealing with contributors*/
    private ContributorService contributorService;
    
	/** Service for dealing with institutional item version services */
	private InstitutionalItemVersionService institutionalItemVersionService;


	/**
	 * Loads a person for viewing and editing.
	 * 
	 * @return view for edit screen 
	 */
	public String editPerson()
	{
		personNameAuthority = personService.getAuthority(personId, false);
		return "veiwForEdit";
	}
	
	/**
	 * Create a new person name.
	 * 
	 * @return
	 */
	public String create() throws NoIndexFoundException
	{
		personNameAuthority = personService.getAuthority(personId, false);
		
		if (personNameAuthority.addName(personName, authoritative))
		{
			personService.save(personNameAuthority);
			
			Repository repo = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
			File nameAuthorityFolder = new File(repo.getNameIndexFolder());
			nameAuthorityIndexService.updateIndex(personNameAuthority, nameAuthorityFolder);
	
			IrUser user = userService.getUserByPersonNameAuthority(personId);
	
			if (user != null) {
			    userIndexService.updateIndex(user, 
						new File( repo.getUserIndexFolder()) );
			}
	
			added = true;
		} else {
			message =  getText("personNameExist");
			added = false;
		}
			return "added";
	}
	
	/**
	 * Method to update an existing person type.
	 * 
	 * @return
	 */
	public String update() throws NoIndexFoundException
	{
		log.debug("updating person name = " + personNameAuthority);
		log.debug("authoritative = " + authoritative);
		
		// user who has the 
		IrUser user = userService.getUserByPersonNameAuthority(personId);
		// get the user making the change
		IrUser userMakingChange = userService.getUser(userId, false);
		// user making change to a name that does not belong to them.
    	if(userMakingChange == null || !(userMakingChange.equals(user) || userMakingChange.hasRole(IrRole.ADMIN_ROLE)))
    	{
    		return "accessDenied";
    	}
		
		
	    if( authoritative )
	    {
	    	if( personNameAuthority.changeAuthoritativeName(personName.getId()))
	    	{
	    		log.debug("person name chaged!");
	    	}
	    	else
	    	{
	    		log.debug("DIDN't change!");
	    	}
	    }
		personService.save(personNameAuthority);
		
		if( personName != null )
		{
			institutionalItemVersionService.setAllVersionsAsUpdatedForPersonName(personName, userMakingChange, "person name - " + personName + " updated ");
		}
		
		Repository repo = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		File nameAuthorityFolder = new File(repo.getNameIndexFolder());
		nameAuthorityIndexService.updateIndex(personNameAuthority, nameAuthorityFolder);
		
		

		if (user != null) {
		    userIndexService.updateIndex(user, 
					new File( repo.getUserIndexFolder()) );
		}			


		added = true;
	    return "added";
	}
	
	/**
	 * Removes the selected person names.
	 * 
	 * @return
	 */
	public String delete() throws NoIndexFoundException
	{
		log.debug("Delete person names called");
		deleted = true;
		
		// user who has the name
		IrUser user = userService.getUserByPersonNameAuthority(personId);
		
		// get the user making the change
		IrUser userMakingChange = userService.getUser(userId, false);
		
		// user making change to a name that does not belong to them.
    	if(!userMakingChange.hasRole(IrRole.ADMIN_ROLE))
    	{
    		if(user == null || !user.equals(userMakingChange))
    		{
    			return "accessDenied";
    		}
    	}
		
		if( personNameIds != null )
		{
			StringBuffer personNamesNotDeleted = new StringBuffer();
			
			Repository repo = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		    
			for(int index = 0; index < personNameIds.length; index++)
		    {
			    log.debug("Deleting person name with id " + personNameIds[index]);
			    PersonName pn = personNameAuthority.getName(personNameIds[index]);
			    
			    if (itemService.getItemCountByPersonName(pn) > 0) {
			    	personNamesNotDeleted.append(pn.getForename());
			    	personNamesNotDeleted.append(" ");
			    	personNamesNotDeleted.append(pn.getSurname());
			    	personNamesNotDeleted.append(",");
		    	} else {
		    		// delete any old contributors
				    List<Contributor> contributors = contributorService.get(pn);
				    for( Contributor c : contributors)
				    {
				    	contributorService.delete(c);
				    }
				    
				    // remove the person name
			        personNameAuthority.removeName(pn);
		    	}
		    }
		    personService.save(personNameAuthority);

		    if (personNamesNotDeleted.length() > 0) {
		    	deleted = false;
		    	personNamesNotDeleted.deleteCharAt(personNamesNotDeleted.length() - 1);
		    	message = getText("personNamesNotDeleted", 
						new String[]{personNamesNotDeleted.toString()});
		    }
	    	
			File nameAuthorityFolder = new File(repo.getNameIndexFolder());
			nameAuthorityIndexService.updateIndex(personNameAuthority, nameAuthorityFolder);

			if (user != null) {
			    userIndexService.updateIndex(user, 
						new File( repo.getUserIndexFolder()) );
			}	

		}
		
		return "deleted";
	}
	
	/**
	 * Get the persons name table data.
	 * 
	 * @return
	 */
	public String viewPersonNames()
	{
		personNames =  personService.get(start, maxResults, personId);
		return SUCCESS;

	}

	/**
	 * Convert the string types to the nesessary types for searching.
	 * 
	 * @see edu.ur.ir.web.table.PropertyConverter#convertValue(java.lang.String, java.lang.String)
	 */
	public Object convertValue(String property, String value) {
		Object returnValue = value;
        if( property.equalsIgnoreCase("id") )
		{
			String tempValue = value.toString();
			returnValue = new Long(tempValue);
		}
        
        return returnValue;
	}

	/**
	 * Get the total number of results.
	 * 
	 * @see edu.ur.ir.web.table.TableCollectionInfo#getTotalNumberOfResults(java.util.List)
	 */
	public int getTotalNumberOfResults() {
		totalNumberOfPersonNames = personService.getCount(personId);
		log.debug("Total number of results = " + totalNumberOfPersonNames);
		return totalNumberOfPersonNames;
	}

	/**
	 * Get the person type service.
	 * 
	 * @return
	 */
	public PersonService getPersonService() {
		return personService;
	}

	/**
	 * Set the person type service.
	 * 
	 * @param personService
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	/**
	 * List of persons for display.
	 * 
	 * @return
	 */
	public Collection<PersonName> getPersonNames() {
		return personNames;
	}
	/**
	 * Set the list of persons.
	 * 
	 * @param persons
	 */
	public void setPersonNames(Collection<PersonName> personNames) {
		this.personNames = personNames;
	}

	/**
	 * Message to be returned to UI.
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Set the message to be returned to the UI.
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Boolean to indicate if the person has been added.
	 * 
	 * @return
	 */
	public boolean isAdded() {
		return added;
	}
	
	/**
	 * Set to true if the person is added.
	 * 
	 * @param added
	 */
	public void setAdded(boolean added) {
		this.added = added;
	}

	/**
	 * Get the id for the person
	 * 
	 * @return
	 */
	public Long getPersonId() {
		return personId;
	}

	/**
	 * Set the id for the person.
	 * 
	 * @param id
	 */
	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	/**
	 * Get the person ids.
	 * 
	 * @return
	 */
	public long[] getPersonNameIds() {
		return personNameIds;
	}

	/**
	 * Set the person ids.
	 * 
	 * @param personIds
	 */
	public void setPersonNameIds(long[] personNameIds) {
		this.personNameIds = personNameIds;
	}

	/**
	 * Tells if the person has been deleted.
	 * 
	 * @return
	 */
	public boolean getDeleted() {
		return deleted;
	}

	/**
	 * Determines if a person or persons has been deleted
	 * @param deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	public void prepare() throws Exception {
		if( personId != null)
		{
			log.debug("Getting person with id " + personId);
			personNameAuthority = personService.getAuthority(personId, false);
			personName = personNameAuthority.getName(id);
			
		}
		
	}

	/**
	 * Get the person information.
	 * 
	 * @return
	 */
	public PersonNameAuthority getPersonNameAuthority() {
		return personNameAuthority;
	}

	/**
	 * Set the person.
	 * 
	 * @param personNameAuthority
	 */
	public void setPersonNameAuthority(PersonNameAuthority personNameAuthority) {
		this.personNameAuthority = personNameAuthority;
	}

	/**
	 * Date person was born
	 * 
	 * @return
	 */
	public String getBirthDate() {
		return birthDate;
	}

	/**
	 * Date person was born.
	 * 
	 * @param birthDate
	 */
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * Date person died.
	 * 
	 * @return
	 */
	public String getDeathDate() {
		return deathDate;
	}

	/**
	 * Date person died.
	 * 
	 * @param deathDate
	 */
	public void setDeathDate(String deathDate) {
		this.deathDate = deathDate;
	}

	/**
	 * Person name being edited
	 * 
	 * @return
	 */
	public PersonName getPersonName() {
		return personName;
	}

	/**
	 * Set the person name.
	 * 
	 * @param personName
	 */
	public void setPersonName(PersonName personName) {
		this.personName = personName;
	}

	/**
	 * Determine if the person name is authoritative.
	 * 
	 * @return
	 */
	public boolean isAuthoritative() {
		return authoritative;
	}

	/**
	 * Set the current person name as authoritative.
	 * 
	 * @param authoritative
	 */
	public void setAuthoritative(boolean authoritative) {
		this.authoritative = authoritative;
	}

	/**
	 * Id for the person name.
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the id for the person name.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get name index service
	 * 
	 * @return
	 */
	public NameAuthorityIndexService getNameAuthorityIndexService() {
		return nameAuthorityIndexService;
	}

	/**
	 * Set name index service
	 * 
	 * @param nameAuthorityIndexService
	 */
	public void setNameAuthorityIndexService(
			NameAuthorityIndexService nameAuthorityIndexService) {
		this.nameAuthorityIndexService = nameAuthorityIndexService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public UserIndexService getUserIndexService() {
		return userIndexService;
	}

	public void setUserIndexService(UserIndexService userIndexService) {
		this.userIndexService = userIndexService;
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

	public ContributorService getContributorService() {
		return contributorService;
	}

	public void setContributorService(ContributorService contributorService) {
		this.contributorService = contributorService;
	}
	
	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}

}
