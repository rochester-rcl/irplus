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

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.item.ItemService;
import edu.ur.ir.person.BirthDate;
import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorService;
import edu.ur.ir.person.DeathDate;
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
import edu.ur.ir.web.table.Pager;

/**
 * Class for dealing with people.
 * 
 * @author Nathan Sarr
 *
 */
public class ManagePersons extends Pager implements  Preparable, UserIdAware {
	
	/**eclipse generated id */
	private static final long serialVersionUID = -3575881684154936394L;

	/** person service */
	private PersonService personService;
	
	/** item service */
	private ItemService itemService;
	
	/** Name index service */
	private NameAuthorityIndexService nameAuthorityIndexService;
	
	/** Repository service */
	private RepositoryService repositoryService;
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(ManagePersons.class);
	
	/** Set of persons for viewing  */
	private Collection<PersonNameAuthority> personNameAuthorities;
	
	/** person loaded  for editing*/
	private PersonNameAuthority personNameAuthority;
	
	/**  Name used by person */
	private PersonName personName = new PersonName();
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the person has been added*/
	private boolean added = false;
	
	/** Indicates the persons have been deleted */
	private boolean deleted = false;
	
	/** id of the person */
	private Long id;
	
	/** id of the person name */
	private Long personNameId;
	
	/** Set of person type ids */
	private long[] personIds;
	
	/** Year person was born  */
	private int birthYear;

	/** Year person died  */
	private int deathYear;
	
	/** id of the user to add the person to */
	private Long addToUserId;

    /** Id of the User making the changes */ 
    private Long userId;
    
    /** User service */
    private UserService userService;
    
    /** service for dealing with contributors*/
    private ContributorService contributorService;
    
 
	/** User's name */
    private boolean myName;
    
    /** User index service */
    private UserIndexService userIndexService;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** name of the element to sort on 
	 *   this is for incoming requests */
	private String sortElement = "surname";

	/** Total number of persons */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Default constructor */
	public  ManagePersons() 
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}
	
	/**
	 * Method to create a new person type.
	 * 
	 * Create a new person type
	 */
	public String create() throws NoIndexFoundException
	{
		
		log.debug("Creating a person with person name " + personName);
		
		// In user account, to add the names to a user or to assign in admin section
		if (addToUserId != null) 
		{
			IrUser user = userService.getUser(userId, false);
			
			// users who are not administrators can only add person to an account
			// account
			if( !addToUserId.equals(userId) && !user.hasRole(IrRole.ADMIN_ROLE))
			{
			    return "accessDenied";	
			}
		}

		personNameAuthority = new PersonNameAuthority(personName);
		personNameAuthority.addBirthDate(birthYear);
		personNameAuthority.addDeathDate(deathYear);
		personService.save(personNameAuthority);
		
		Repository repo = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		File nameAuthorityFolder = new File(repo.getNameIndexFolder());
		nameAuthorityIndexService.addToIndex(personNameAuthority, nameAuthorityFolder);
		
		log.debug( " my Name = " + myName + " add to user id = " + addToUserId);
		// In user account, to add the names to a user
		if (myName && (addToUserId != null)) {
			IrUser user = userService.getUser(addToUserId, false);
			user.setPersonNameAuthority(personNameAuthority);
			userService.makeUserPersistent(user);
		    userIndexService.updateIndex(user, 
					new File( repo.getUserIndexFolder()) );			
		}
		
		added = true;
	    return "added";
	}
	
	/**
	 * Loads a person for viewing and editing.
	 * 
	 * @return view for edit screen 
	 */
	public String viewSingle()
	{
		personNameAuthority = personService.getAuthority(id, false);
		return "veiwForEdit";
	}
	
	/**
	 * Method to update an existing person type.
	 * 
	 * @return
	 */
	public String update() throws NoIndexFoundException
	{
		log.debug("updateing person = " + personNameAuthority);
		
		BirthDate bd = personNameAuthority.getBirthDate();
		bd.setYear(birthYear);
		
		DeathDate dd = personNameAuthority.getDeathDate();
		dd.setYear(deathYear);

		IrUser user = userService.getUserByPersonNameAuthority(id);
		
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
		
		personService.save(personNameAuthority);

		Repository repo = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		File nameAuthorityFolder = new File(repo.getNameIndexFolder());
		nameAuthorityIndexService.updateIndex(personNameAuthority, nameAuthorityFolder);

		if (user != null) 
		{
			userIndexService.updateIndex(user, 
				new File( repo.getUserIndexFolder()) );	
		}


		added = true;
	    return "added";
	}
	
	/**
	 * Removes the selected persons
	 * 
	 * @return
	 */
	public String delete() throws NoIndexFoundException
	{
		log.debug("Delete persons called");
		deleted = true;
		
		StringBuffer personsNotDeleted = new StringBuffer();
		
		if( personIds != null )
		{
			Repository repo = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
			File nameAuthorityFolder = new File(repo.getNameIndexFolder());
			
			// get the user making the change
			IrUser userMakingChange = userService.getUser(userId, false);
			
		    for(int index = 0; index < personIds.length; index++)
		    {
		    	boolean isContributor = false;
		    	log.debug("Deleting person with id " + personIds[index]);
			    PersonNameAuthority p = personService.getAuthority(personIds[index], false);
			    
			    for (PersonName personName: p.getNames()) {
			    	if (itemService.getItemCountByPersonName(personName) > 0) {
			    		isContributor = true;
			    		break;
			    	}
			    }
			    
			    // Delete Person only if the person is not a contributor
			    if (!isContributor) {
			    	IrUser user = userService.getUserByPersonNameAuthority(personIds[index]);
				    
			    	// user making change to a name that does not belong to them.
			    	if(!userMakingChange.hasRole(IrRole.ADMIN_ROLE) && !userMakingChange.hasRole(IrRole.COLLECTION_ADMIN_ROLE))
			    	{
			    		if(user == null || !user.equals(userMakingChange))
			    		{
			    			return "accessDenied";
			    		}
			    	}
			    	
			    	for (PersonName personName: p.getNames()) 
			    	{
			    		// delete any old contributors
					    List<Contributor> contributors = contributorService.get(personName);
					    for( Contributor c : contributors)
					    {
					    	contributorService.delete(c);
					    }
					    
					}
					
				    // Update the user indices with user's person names
					if (user != null) {
						user.setPersonNameAuthority(null);
						userService.makeUserPersistent(user);
						userIndexService.updateIndex(user, 
							new File( repo.getUserIndexFolder()) );	
					}
					personService.delete(p);
				    nameAuthorityIndexService.deleteFromIndex(p, nameAuthorityFolder);
			    } else {
			    	deleted = false;
			    	personsNotDeleted.append(p.getAuthoritativeName().getForename());
			    	personsNotDeleted.append(" ");
			    	personsNotDeleted.append(p.getAuthoritativeName().getSurname());
			    	personsNotDeleted.append(",");
			    	
			    }

		    }
		    
		    if (personsNotDeleted.length() > 0) {
		    	personsNotDeleted.deleteCharAt(personsNotDeleted.length() - 1);
		    	message = getText("personNotDeleted", 
						new String[]{personsNotDeleted.toString()});
		    }

		}
		
		return "deleted";
	}
 

	
	/**
	 * Get the persons table data.
	 * 
	 * @return
	 */
	public String viewPersons()
	{
	
		log.debug("RowStart = " + rowStart
	    		+ "   numberOfResultsToShow=" + numberOfResultsToShow + "   sortElement="  + sortElement + "   sortType =" + sortType);
		rowEnd = rowStart + numberOfResultsToShow;
	    
		personNameAuthorities = personService.get(rowStart, 
	    		numberOfResultsToShow, sortElement, sortType);
	    totalHits = personService.getCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;

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
	public Collection<PersonNameAuthority> getPersonNameAuthorities() {
		return personNameAuthorities;
	}
	/**
	 * Set the list of persons.
	 * 
	 * @param personNameAuthorities
	 */
	public void setPersonNameAuthorities(Collection<PersonNameAuthority> personNameAuthorities) {
		this.personNameAuthorities = personNameAuthorities;
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
	public Long getId() {
		return id;
	}

	/**
	 * Set the id for the person.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the person ids.
	 * 
	 * @return
	 */
	public long[] getPersonIds() {
		return personIds;
	}

	/**
	 * Set the person ids.
	 * 
	 * @param personIds
	 */
	public void setPersonIds(long[] personIds) {
		this.personIds = personIds;
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
		if( id != null)
		{
			personNameAuthority = personService.getAuthority(id, false);

			if (personNameId != null) {
				personName = personService.getName(personNameId, false);
			} else {
				personName = personNameAuthority.getAuthoritativeName();
			}
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
	 * Get the name of the person.
	 * 
	 * @return
	 */
	public PersonName getPersonName() {
		return personName;
	}

	/**
	 * Set the name of the person.
	 * 
	 * @param personName
	 */
	public void setPersonName(PersonName personName) {
		this.personName = personName;
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

	public Long getAddToUserId() {
		return addToUserId;
	}

	public void setAddToUserId(Long addToUserId) {
		this.addToUserId = addToUserId;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public boolean isMyName() {
		return myName;
	}

	public void setMyName(boolean myName) {
		this.myName = myName;
	}

	public Long getPersonNameId() {
		return personNameId;
	}

	public void setPersonNameId(Long personNameId) {
		this.personNameId = personNameId;
	}

	public int getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	public int getDeathYear() {
		return deathYear;
	}

	public void setDeathYear(int deathYear) {
		this.deathYear = deathYear;
	}

	public UserIndexService getUserIndexService() {
		return userIndexService;
	}

	public void setUserIndexService(UserIndexService userIndexService) {
		this.userIndexService = userIndexService;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getSortElement() {
		return sortElement;
	}

	public void setSortElement(String sortElement) {
		this.sortElement = sortElement;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

	public int getTotalHits() {
		return totalHits;
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


}
