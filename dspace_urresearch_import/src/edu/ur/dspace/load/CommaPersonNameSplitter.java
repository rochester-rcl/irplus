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

package edu.ur.dspace.load;

import org.apache.log4j.Logger;

import edu.ur.ir.person.PersonName;
import edu.ur.ir.person.PersonNameAuthority;

/**
 * This splitter will take an author name that is split based on commas
 * It assumes the format for the name is 
 * 
 * First Name[,] Last Name Middle Initial[.][,] Birth Year[-]Death Year
 * @author Nathan Sarr
 *
 */
public class CommaPersonNameSplitter implements AuthorNameSplitter {

	/**  Logger */
	private static final Logger log = Logger
			.getLogger(CommaPersonNameSplitter.class);

	/* (non-Javadoc)
	 * @see edu.ur.dspace.load.AuthorNameSplitter#splitName(java.lang.String)
	 */
	public PersonNameAuthority splitName(String authorName) {
		PersonName personName = new PersonName();
		PersonNameAuthority nameAuthority = new PersonNameAuthority(personName);

		// split on the name
		String[] fullNameParts = authorName.split(",");

		int size = fullNameParts.length;

		if( size < 4)
		{
			nameAuthority = maxThreePartNameSplit(fullNameParts);
		}
		if (size ==  4) {
			nameAuthority = fourPartNameSplit(fullNameParts);
		} else {
			personName.setSurname(authorName);
		}

		return nameAuthority;
	}
	
	/**
	 * This handles a 3 part name
	 * @param nameParts
	 * @return
	 */
	private PersonNameAuthority maxThreePartNameSplit(String[] fullNameParts)
	{
		PersonName personName = new PersonName();
		PersonNameAuthority nameAuthority = new PersonNameAuthority(personName);
		int size = fullNameParts.length;

		if (size >= 1) {
			personName.setSurname(fullNameParts[0].trim());
		}
		if (size >= 2) {
			String firstNameParts = fullNameParts[1].trim();
			if (firstNameParts.indexOf("(") == -1) {
				secondPartWithoutParens(personName, fullNameParts[1].trim());
			} else {
				secondPartWithParens(personName, fullNameParts[1].trim());
			}
		}
		if (size >= 3) {
			int birthYear = getBirthYear(fullNameParts[2]);
			if (birthYear > 0) {
				nameAuthority.addBirthDate(birthYear);
			}

			int deathYear = getDeathYear(fullNameParts[2]);
			if (deathYear > 0) {
				nameAuthority.addDeathDate(deathYear);
			}
		}

		return nameAuthority;
	}
	
	/**
	 * Handles a name that has four parts.
	 * 
	 * @param fullNameParts
	 * @return
	 */
	private PersonNameAuthority fourPartNameSplit(String[] fullNameParts)
	{
		PersonName personName = new PersonName();
		PersonNameAuthority nameAuthority = new PersonNameAuthority(personName);
		
		// set the last name
		personName.setSurname(fullNameParts[0].trim());
		
		// take care of last/middle name
		String firstNameParts = fullNameParts[1].trim();
		if (firstNameParts.indexOf("(") == -1) {
			secondPartWithoutParens(personName, fullNameParts[1].trim());
		} else {
			secondPartWithParens(personName, fullNameParts[1].trim());
		}
		
		String title = fullNameParts[2];
		personName.addPersonNameTitle(title);
		
		int birthYear = getBirthYear(fullNameParts[3]);
		if (birthYear > 0) {
			nameAuthority.addBirthDate(birthYear);
		}

		int deathYear = getDeathYear(fullNameParts[3]);
		if (deathYear > 0) {
			nameAuthority.addDeathDate(deathYear);
		}

		return nameAuthority;
	}
	
	
	/**
	 * Attempts to convert the birth year - returns a zero if it could
	 * not convert 
	 * 
	 * @param yearsPart
	 * @return
	 */
	private int getBirthYear(String yearsPart)
	{
		int birthYearInt = 0;
		int dashIndex = yearsPart.indexOf("-");
		if (dashIndex > -1) {
			String birthYear = yearsPart.substring(0, dashIndex)
					.trim();
			try {
				birthYearInt = new Integer(birthYear).intValue();
			} catch (NumberFormatException nfe) {
				log.debug("could not convert birth year" + birthYear);
				birthYearInt = 0;

			}
		} else {
			log.debug("could not convert birth/death years"
					+ yearsPart);
		}
		
		return birthYearInt;

	}
	
	/**
	 * Attempts to convert the death year - returns a zero if it could
	 * not convert 
	 * 
	 * @param yearsPart
	 * @return
	 */
	private int getDeathYear(String yearsPart)
	{
		int deathYearInt = 0;
		int dashIndex = yearsPart.indexOf("-");
		if (dashIndex > -1) {
			
			String deathYear = yearsPart.substring(
					dashIndex + 1, yearsPart.length())
					.trim();
			int periodIndex = deathYear.indexOf(".");
			if (periodIndex > -1) {
				deathYear = deathYear.substring(0, periodIndex);
			}
			try {
				deathYearInt = new Integer(deathYear).intValue();
				
			} catch (NumberFormatException nfe) {
				log.debug("could not convert death year" + deathYear);
				deathYearInt = 0;
			}
		} else {
			log.debug("could not convert birth/death years"
					+ yearsPart);
		}
		
		return deathYearInt;
	}
	
	
	/**
	 * Handles a name part that looks like the following: H. C. (Hans Christian)
	 * @param personName
	 * @param firstAndMiddleNameParts
	 */
	private void secondPartWithParens(PersonName personName, String firstAndMiddleNameParts)
	{
		if( (firstAndMiddleNameParts.indexOf("(") > -1) && 
			(firstAndMiddleNameParts.indexOf(")") > -1) &&
			(firstAndMiddleNameParts.indexOf("(") < firstAndMiddleNameParts.indexOf(")") ) )
			{
			    try
			    {
			        String initials = firstAndMiddleNameParts.substring(0, firstAndMiddleNameParts.indexOf("(") - 1 );
			        String firstAndMiddle = firstAndMiddleNameParts.substring(firstAndMiddleNameParts.indexOf("(") + 1, firstAndMiddleNameParts.indexOf(")"));
			        secondPartWithoutParens(personName, firstAndMiddle);
			        personName.setInitials(initials);
			    }
			    catch(StringIndexOutOfBoundsException sioobe)
			    {
			    	log.debug("Error with name " + firstAndMiddleNameParts, sioobe);
			    	secondPartWithoutParens(personName, firstAndMiddleNameParts);
			    }
			}
		else
		{
			//unbalanced take best guess
			secondPartWithoutParens(personName, firstAndMiddleNameParts);
		}
	}
	
	private void secondPartWithoutParens(PersonName personName, String firstAndMiddleNameParts)
	{
		int firstSpaceIndex = firstAndMiddleNameParts.indexOf(" ");
		if (firstSpaceIndex > -1) {
			String firstName = firstAndMiddleNameParts.substring(0,
					firstSpaceIndex).trim();
			String middleName = firstAndMiddleNameParts.substring(
					firstSpaceIndex, firstAndMiddleNameParts.length()).trim();
			personName.setForename(firstName);
			personName.setMiddleName(middleName);
		} else {
			personName.setForename(firstAndMiddleNameParts);
		}
	}

}
