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

import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.Validateable;

import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameDAO;
import edu.ur.ir.person.PersonNameTitle;
import edu.ur.ir.person.PersonNameTitleDAO;

public class EditPersonNameTitle extends ActionSupport implements Preparable, Validateable{
	
	/**
	 * Eclipse generated id.
	 */
	private static final long serialVersionUID = -7050953690475661645L;

	/**
	 * Logger for editing a file database.
	 */
	private static final Logger log = Logger.getLogger(EditPersonNameTitle.class);
	
	/**
	 * Id of the name of the person to edit.
	 */
	private Long personNameId;
	
	/**
	 * Id of the person name title.
	 */
	private Long personNameTitleId;
	
	/**
	 * Data access for the person name title 
	 */
	PersonNameTitleDAO personNameTitleDAO;
	
	/**
	 * Person name title
	 */
	private PersonNameTitle personNameTitle;

	/**
	 * Person name data access.
	 */
	private PersonNameDAO personNameDAO;
	
	/**
	 * Prepare the person name
	 * 
	 * @see com.opensymphony.xwork.Preparable#prepare()
	 */
	public void prepare() throws Exception{
		log.debug("execute called");
		
		if(personNameTitleId != null)
		{
			personNameTitle = personNameTitleDAO.getById(personNameTitleId, false);
			personNameId = personNameTitle.getPersonName().getId();
		}
		
	}
	
	/**
	 * Save the person name
	 * 
	 * @return
	 */
	public String save() {
		
		if( personNameTitle == null )
		{
			throw new IllegalStateException("Person name cannot be null");
		}
		
		if( personNameDAO == null )
		{
			throw new IllegalStateException ("Person Name DAO is null");
		}
		
		if( personNameTitleDAO == null )
		{
			throw new IllegalStateException ("Person Name Title DAO is null");
		}
				
		PersonName personName = personNameDAO.getById(personNameId, false);
		
		if( personName == null )
		{
			throw new IllegalStateException("Person name cannot be null");
		}
		
		if( personNameTitle.getId() != null )
		{
			Set<PersonNameTitle> titles = personName.getPersonNameTitles();
			
			for( PersonNameTitle t : titles)
			{
				if( t.getId().equals(personNameTitle.getId()))
				{
					t.setTitle(personNameTitle.getTitle());
				}
			}
		}
		else
		{
			personName.addPersonNameTitle(personNameTitle.getTitle());
		}
		
		personNameDAO.makePersistent(personName);
		
		return SUCCESS;
	}
	
	/**
	 * Delete the institution 
	 * 
	 * @return
	 */
	public String delete()
	{
		if (personNameTitle == null) {
			return INPUT;
		}
		personNameTitleDAO.makeTransient(personNameTitle);
		return SUCCESS;
	}

	/**
	 * Cancel called
	 * 
	 * @return cancel
	 */
	public String cancel() {
		return "cancel";
	}

	/**
	 * Validate the person information.
	 * 
	 * @see com.opensymphony.xwork.ActionSupport#validate()
	 */
	public void validate()
	{
		// make sure that person does not have a title more than once.
	}

	/**
	 * Person name data access.
	 * 
	 * @return
	 */
	public PersonNameDAO getPersonNameDAO() {
		return personNameDAO;
	}

	/**
	 * Person name data access.
	 * 
	 * @param personNameDAO
	 */
	public void setPersonNameDAO(PersonNameDAO personNameDAO) {
		this.personNameDAO = personNameDAO;
	}

	/**
	 * Get the id of the person name.
	 * 
	 * @return
	 */
	public Long getPersonNameId() {
		return personNameId;
	}

	/**
	 * Set the id of the person name.
	 * 
	 * @param personNameId
	 */
	public void setPersonNameId(Long personNameId) {
		this.personNameId = personNameId;
	}

	public PersonNameTitle getPersonNameTitle() {
		return personNameTitle;
	}

	public void setPersonNameTitle(PersonNameTitle personNameTitle) {
		this.personNameTitle = personNameTitle;
	}

	public Long getPersonNameTitleId() {
		return personNameTitleId;
	}

	public void setPersonNameTitleId(Long personNameTitleId) {
		this.personNameTitleId = personNameTitleId;
	}

	public PersonNameTitleDAO getPersonNameTitleDAO() {
		return personNameTitleDAO;
	}

	public void setPersonNameTitleDAO(PersonNameTitleDAO personNameTitleDAO) {
		this.personNameTitleDAO = personNameTitleDAO;
	}
}
