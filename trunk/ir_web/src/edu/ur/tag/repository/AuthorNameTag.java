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
	
	public void doTag() throws JspException
	{
		JspWriter out = this.getJspContext().getOut();
		String output = "";
	    if( personName != null)	
	    {
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


}
