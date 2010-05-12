/**  
   Copyright 2008-2010 University of Rochester

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
package edu.ur.ir.person;

import java.io.Serializable;

/**
 * Format the person name, last name first.
 * 
 * @author Nathan Sarr
 *
 */
public class LastNamePersonNameFormatter implements BasicPersonNameFormatter, Serializable{

	/** eclipse generated id */
	private static final long serialVersionUID = 7720831991095283802L;

	/**
	 * Format the person name with the first name showing.
	 * 
	 * @see edu.ur.ir.person.BasicPersonNameFormatter#getNameFormatted(edu.ur.ir.person.PersonName, boolean)
	 */
	public String getNameFormatted(PersonName personName, boolean includeDates) {
		StringBuffer output = new StringBuffer("");
		if( personName.getSurname() != null &&  personName.getSurname().trim().length() > 0)
    	{
    		output.append(personName.getSurname());
    	}
		if( personName.getForename() != null && personName.getForename().trim().length() > 0)
    	{
			if(output.length() > 0 )
			{
				output.append(", ");
			}
			output.append(personName.getForename());
    	}
		if( personName.getMiddleName() != null &&  personName.getMiddleName().trim().length() > 0)
    	{
			if(output.length() > 0 )
			{
				output.append(", ");
			}
			output.append(personName.getMiddleName());
    	}
		if( personName.getNumeration() != null && personName.getNumeration().trim().length() > 0)
    	{
			if(output.length() > 0 )
			{
				output.append(", ");
			}
			output.append(personName.getNumeration());
    	}
		if( personName.getPersonNameTitles() != null && personName.getPersonNameTitles().size() > 0)
    	{
			if(output.length() > 0 )
			{
				output.append(", ");
			}
    	    for( PersonNameTitle title: personName.getPersonNameTitles())
    	    {
    	    	output.append(title.getTitle() + " ");
    	    }
    	}
    	
    	
    	if(includeDates)
    	{
    		output.append(" ");
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
    			output.append("(");
    		    if( birthYear > 0)
    		    {
    		    	output.append(birthYear);
    		    }
    		    output.append(" - ");
    		
    		    if(  deathYear > 0 )
    		    {
    		    	output.append(deathYear);
    		    }
    		    output.append(")");
    	    }
    	}
    	return output.toString();
	}

}
