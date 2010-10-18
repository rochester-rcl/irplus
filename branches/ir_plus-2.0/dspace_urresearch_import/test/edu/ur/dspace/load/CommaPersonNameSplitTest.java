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

import org.testng.annotations.Test;

import edu.ur.ir.person.PersonNameAuthority;

/**
 * Test name splitting
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class CommaPersonNameSplitTest {
	
	/**
	 * Test the extraction of names
	 */
	public void splitNameTest()
	{
		CommaPersonNameSplitter nameSplitter = new CommaPersonNameSplitter();
		PersonNameAuthority authorityName;
		
		String name1 = "(aka Ogihara)";
		authorityName = nameSplitter.splitName(name1);
		assert authorityName.getAuthoritativeName().getSurname().equals("(aka Ogihara)") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();

		String name2 = "A. L. (Amelia Lehmann)";
		authorityName = nameSplitter.splitName(name2);
		assert authorityName.getAuthoritativeName().getSurname().equals("A. L. (Amelia Lehmann)") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();

		String name3 = "Aarestrup, Emil, 1800-1856.";
		authorityName = nameSplitter.splitName(name3);
		assert authorityName.getAuthoritativeName().getSurname().equals("Aarestrup") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getBirthDate().getYear() == 1800 : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getDeathDate().getYear() == 1856 : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getAuthoritativeName().getForename().equals("Emil") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		
		String name4 = "Aasen, Ivar Andreas, 1813-1896.";
		authorityName = nameSplitter.splitName(name4);
		assert authorityName.getAuthoritativeName().getSurname().equals("Aasen") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getBirthDate().getYear() == 1813 : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getDeathDate().getYear() == 1896 : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getAuthoritativeName().getForename().equals("Ivar") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getAuthoritativeName().getMiddleName().equals("Andreas") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		
		String name5 = "Abbamonte, Sarah";
		authorityName = nameSplitter.splitName(name5);
		assert authorityName.getAuthoritativeName().getSurname().equals("Abbamonte") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getAuthoritativeName().getForename().equals("Sarah") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		
		
		String name6 = "Abrams, Drew R.";
		authorityName = nameSplitter.splitName(name6);
		assert authorityName.getAuthoritativeName().getSurname().equals("Abrams") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getAuthoritativeName().getForename().equals("Drew") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getAuthoritativeName().getMiddleName().equals("R.") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		
		String name7 = "Albert, Prince Consort of Victoria, Queen of Great Britain, 1819-1861";
		authorityName = nameSplitter.splitName(name7);
		assert authorityName.getAuthoritativeName().getSurname().equals("Albert") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getBirthDate().getYear() == 1819 : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getDeathDate().getYear() == 1861 : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getAuthoritativeName().getForename().equals("Prince") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getAuthoritativeName().getMiddleName().equals("Consort of Victoria") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();

		
		String name8 = "Andersen, H. C. (Hans Christian), 1805-1875.";
		authorityName = nameSplitter.splitName(name8);
		assert authorityName.getAuthoritativeName().getSurname().equals("Andersen") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getBirthDate().getYear() == 1805 : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getDeathDate().getYear() == 1875 : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getAuthoritativeName().getMiddleName().equals("Christian") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getAuthoritativeName().getForename().equals("Hans") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getAuthoritativeName().getInitials().equals("H. C.") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		
		
		String name9 = "Baker, H. W. (Henry Williams), Sir, 1821-1877";
		authorityName = nameSplitter.splitName(name9);
		assert authorityName.getAuthoritativeName().getSurname().equals("Baker") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getBirthDate().getYear() == 1821 : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getDeathDate().getYear() == 1877 : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getAuthoritativeName().getMiddleName().equals("Williams") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getAuthoritativeName().getForename().equals("Henry") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();
		assert authorityName.getAuthoritativeName().getInitials().equals("H. W.") : "Authority name = " + authorityName + " person name = " + authorityName.getAuthoritativeName();

	
	}

}
