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


package edu.ur.tag.repository;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.ur.ir.person.BirthDate;
import edu.ur.ir.person.DeathDate;
import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameTitle;

/**
 * Tag to output an author name.
 * 
 * @author Nathan Sarr
 *
 */
public class AuthorNameTag extends SimpleTagSupport{
	
	/** variable to set with the collection of values  */
	private PersonName personName;
	
	/** boolean to indicate that birth and death dates should be shown */
	private boolean displayDates = false;
	
	/** show the name starting with last name */
	private boolean lastNameFirst = false;
	
	public void doTag() throws JspException
	{
		JspWriter out = this.getJspContext().getOut();
		String output = "";
		if( personName == null)	
	    {
			// do nothing
	    }
		else if( !lastNameFirst )
		{
	        output = this.showFirstNameFirst();
		}
		else if ( lastNameFirst)
		{
	        output = showLastNameFirst();	
	    }
	    
	    try {
			out.write(output);
		} catch (IOException e) {
			throw new JspException(e);
		}
	}


	public PersonName getPersonName() {
		return personName;
	}



	public void setPersonName(PersonName personName) {
		this.personName = personName;
	}


	public boolean isDisplayDates() {
		return displayDates;
	}


	public void setDisplayDates(boolean displayDates) {
		this.displayDates = displayDates;
	}
	
	private String showFirstNameFirst()
	{
		String output = "";
		if( personName.getPersonNameTitles() != null )
    	{
    	    for( PersonNameTitle title: personName.getPersonNameTitles())
    	    {
    		    output += title.getTitle() + " ";
    	    }
    	}
    	if( personName.getForename() != null)
    	{
    		output += personName.getForename() + " ";
    	}
    	
    	if( personName.getMiddleName() != null)
    	{
    		output += personName.getMiddleName() + " ";
    	}
    	if( personName.getSurname() != null)
    	{
    		output += personName.getSurname() + " ";
    	}
    	
    	if( personName.getNumeration() != null )
    	{
    		output += personName.getNumeration() + " ";
    	}
    	
    	if(displayDates)
    	{
    	    BirthDate birthDate = personName.getPersonNameAuthority().getBirthDate();
    	    DeathDate deathDate = personName.getPersonNameAuthority().getDeathDate();
    	    int birthYear = 0;
    	    int deathYear = 0;
    	    
    	    if(birthDate != null)
    	    {
    	    	birthYear = birthDate.getYear();
    	    }
    	    
    	    if(deathDate != null)
    	    {
    	    	deathYear = deathDate.getYear();
    	    }
    	    
    	    
    		if( birthYear > 0 || deathYear > 0 )
    		{
    		    output += "(";
    		    if( birthYear > 0)
    		    {
    			    output += birthYear;
    		    }
    		    output += " - ";
    		
    		    if(  deathYear > 0 )
    		    {
    			    output += deathYear;
    		    }
    		    output += ")";
    	    }
    	}
    	return output;
	}
	
	private String showLastNameFirst()
	{
		String output = "";
		if( personName.getSurname() != null &&  personName.getSurname().trim().length() > 0)
    	{
    		output += personName.getSurname();
    	}
		if( personName.getForename() != null && personName.getForename().trim().length() > 0)
    	{
			if(output.trim().length() > 0 )
			{
				output +=", ";
			}
    		output += personName.getForename();
    	}
		if( personName.getMiddleName() != null &&  personName.getMiddleName().trim().length() > 0)
    	{
			if(output.trim().length() > 0 )
			{
				output +=", ";
			}
    		output += personName.getMiddleName();
    	}
		if( personName.getNumeration() != null && personName.getNumeration().trim().length() > 0)
    	{
			if(output.trim().length() > 0 )
			{
				output +=", ";
			}
    		output += personName.getNumeration();
    	}
		if( personName.getPersonNameTitles() != null && personName.getPersonNameTitles().size() > 0)
    	{
			if(output.trim().length() > 0 )
			{
				output +=", ";
			}
    	    for( PersonNameTitle title: personName.getPersonNameTitles())
    	    {
    		    output += title.getTitle() + " ";
    	    }
    	}
    	
    	
    	if(displayDates)
    	{
    		output += " ";
    	    BirthDate birthDate = personName.getPersonNameAuthority().getBirthDate();
    	    DeathDate deathDate = personName.getPersonNameAuthority().getDeathDate();
    	    int birthYear = 0;
    	    int deathYear = 0;
    	    
    	    if(birthDate != null)
    	    {
    	    	birthYear = birthDate.getYear();
    	    }
    	    
    	    if(deathDate != null)
    	    {
    	    	deathYear = deathDate.getYear();
    	    }
    	    
    	    
    		if( birthYear > 0 || deathYear > 0 )
    		{
    		    output += "(";
    		    if( birthYear > 0)
    		    {
    			    output += birthYear;
    		    }
    		    output += " - ";
    		
    		    if(  deathYear > 0 )
    		    {
    			    output += deathYear;
    		    }
    		    output += ")";
    	    }
    	}
    	return output;
	}


	public boolean isLastNameFirst() {
		return lastNameFirst;
	}


	public void setLastNameFirst(boolean lastNameFirst) {
		this.lastNameFirst = lastNameFirst;
	}


}
