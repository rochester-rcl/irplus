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

import edu.ur.ir.person.FirstNamePersonNameFormatter;
import edu.ur.ir.person.LastNamePersonNameFormatter;
import edu.ur.ir.person.PersonName;

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
	
	/** formatter to show person first name */
	private FirstNamePersonNameFormatter firstNameFormatter = new FirstNamePersonNameFormatter();
	
	/** formatter to show a person name with last name first */
	private LastNamePersonNameFormatter lastNameFormatter = new LastNamePersonNameFormatter();
	
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
	        output = firstNameFormatter.getNameFormatted(personName, displayDates);
		}
		else if ( lastNameFirst)
		{
	        output = lastNameFormatter.getNameFormatted(personName, displayDates);	
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
	

	public boolean isLastNameFirst() {
		return lastNameFirst;
	}


	public void setLastNameFirst(boolean lastNameFirst) {
		this.lastNameFirst = lastNameFirst;
	}


}
