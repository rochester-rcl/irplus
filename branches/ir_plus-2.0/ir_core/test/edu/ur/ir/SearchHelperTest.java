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

package edu.ur.ir;

import org.testng.annotations.Test;

/**
 * Test the search helper
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class SearchHelperTest {
	
	/**
	 * Make sure strings are processed correctly
	 */
	public void testPrepareFacetSearchString()
	{
		assert SearchHelper.prepareFacetSearchString("test*", false).equals("test*") : "equals " + SearchHelper.prepareFacetSearchString("test*", false);
		assert SearchHelper.prepareFacetSearchString("the* *cool test *", false).equals("the* cool test") : "equals " + SearchHelper.prepareFacetSearchString("the* *cool test *", false);
		assert SearchHelper.prepareFacetSearchString("the:", false).equals("the\\:") : "equals " + SearchHelper.prepareFacetSearchString("the: ", false);
		assert SearchHelper.prepareFacetSearchString("the: ", false).equals("the\\:") : "equals " + SearchHelper.prepareFacetSearchString("the: ", false);
		assert SearchHelper.prepareFacetSearchString("the: :", false).equals("the\\: \\:") : "equals " + SearchHelper.prepareFacetSearchString("the: ", false);
		assert SearchHelper.prepareFacetSearchString("the()", false).equals("the\\(\\)") : "equals " + SearchHelper.prepareFacetSearchString("the()", false);
		assert SearchHelper.prepareFacetSearchString("*the", false).equals("the") : "equals " + SearchHelper.prepareFacetSearchString("*the", false);
		assert SearchHelper.prepareFacetSearchString("?the", false).equals("the") : "equals " + SearchHelper.prepareFacetSearchString("?the", false);
		assert SearchHelper.prepareFacetSearchString("th?", false).equals("th?") : "equals " + SearchHelper.prepareFacetSearchString("th?", false);
		assert SearchHelper.prepareFacetSearchString("test *", false).equals("test") : "equals " + SearchHelper.prepareFacetSearchString("test *", false);
		assert SearchHelper.prepareFacetSearchString("test ?", false).equals("test") : "equals " + SearchHelper.prepareFacetSearchString("test *", false);
		assert SearchHelper.prepareFacetSearchString("test \\", false).equals("test \\\\") : "equals " + SearchHelper.prepareFacetSearchString("test \\", false);

		assert SearchHelper.prepareFacetSearchString("test*", true).equals("test*") : "equals " + SearchHelper.prepareFacetSearchString("test*", true);
		assert SearchHelper.prepareFacetSearchString("the* *cool test *", true).equals("the* cool* test*") : "equals " + SearchHelper.prepareFacetSearchString("the* *cool test *", true);
		assert SearchHelper.prepareFacetSearchString("the: ", true).equals("the\\:") : "equals " + SearchHelper.prepareFacetSearchString("the: ", true);
		assert SearchHelper.prepareFacetSearchString("the()", true).equals("the\\(\\)") : "equals " + SearchHelper.prepareFacetSearchString("the()", true);
		assert SearchHelper.prepareFacetSearchString("*the", true).equals("the*") : "equals " + SearchHelper.prepareFacetSearchString("*the", true);
		assert SearchHelper.prepareFacetSearchString("?the", true).equals("the*") : "equals " + SearchHelper.prepareFacetSearchString("?the", true);
		assert SearchHelper.prepareFacetSearchString("test *", true).equals("test*") : "equals " + SearchHelper.prepareFacetSearchString("test *", true);
		assert SearchHelper.prepareFacetSearchString("test mult terms *", true).equals("test* mult* terms*") : "equals " + SearchHelper.prepareFacetSearchString("test mult terms *", true);
		//assert SearchHelper.prepareFacetSearchString("dash-test", true).equals("dash\\-test*") : "equals " + SearchHelper.prepareFacetSearchString("dash-test", true);
		//assert SearchHelper.prepareFacetSearchString("dash -test", true).equals("dash* \\-test*") : "equals " + SearchHelper.prepareFacetSearchString("dash -test", true);

		
	}
	
	/**
	 * Make sure strings are processed correctly
	 */
	public void testPrepareMainSearchString()
	{
		

		assert SearchHelper.prepareMainSearchString("test*", false).equals("test*") : "equals " + SearchHelper.prepareMainSearchString("test*", false);
		assert SearchHelper.prepareMainSearchString("the* *cool test *", false).equals("the* cool test") : "equals " + SearchHelper.prepareMainSearchString("the* *cool test *", false);
		assert SearchHelper.prepareMainSearchString("the:", false).equals("the\\:") : "equals " + SearchHelper.prepareMainSearchString("the: ", false);
		assert SearchHelper.prepareMainSearchString("the: ", false).equals("the\\:") : "equals " + SearchHelper.prepareMainSearchString("the: ", false);
		assert SearchHelper.prepareMainSearchString("the: :", false).equals("the\\: \\:") : "equals " + SearchHelper.prepareMainSearchString("the: ", false);
		assert SearchHelper.prepareMainSearchString("the()", false).equals("the\\(\\)") : "equals " + SearchHelper.prepareMainSearchString("the()", false);
		assert SearchHelper.prepareMainSearchString("*the", false).equals("the") : "equals " + SearchHelper.prepareMainSearchString("*the", false);
		assert SearchHelper.prepareMainSearchString("?the", false).equals("the") : "equals " + SearchHelper.prepareMainSearchString("?the", false);
		assert SearchHelper.prepareMainSearchString("th?", false).equals("th?") : "equals " + SearchHelper.prepareMainSearchString("th?", false);
		assert SearchHelper.prepareMainSearchString("test *", false).equals("test") : "equals " + SearchHelper.prepareMainSearchString("test *", false);
		assert SearchHelper.prepareMainSearchString("test ?", false).equals("test") : "equals " + SearchHelper.prepareMainSearchString("test *", false);
		assert SearchHelper.prepareMainSearchString("test \\", false).equals("test \\\\") : "equals " + SearchHelper.prepareMainSearchString("test \\", false);

		assert SearchHelper.prepareMainSearchString("test*", true).equals("test*") : "equals " + SearchHelper.prepareMainSearchString("test*", true);
		assert SearchHelper.prepareMainSearchString("the* *cool test *", true).equals("the* cool* test*") : "equals " + SearchHelper.prepareMainSearchString("the* *cool test *", true);
		assert SearchHelper.prepareMainSearchString("the: ", true).equals("the\\:") : "equals " + SearchHelper.prepareMainSearchString("the: ", true);
		assert SearchHelper.prepareMainSearchString("the()", true).equals("the\\(\\)") : "equals " + SearchHelper.prepareMainSearchString("the()", true);
		assert SearchHelper.prepareMainSearchString("*the", true).equals("the*") : "equals " + SearchHelper.prepareMainSearchString("*the", true);
		assert SearchHelper.prepareMainSearchString("?the", true).equals("the*") : "equals " + SearchHelper.prepareMainSearchString("?the", true);
		assert SearchHelper.prepareMainSearchString("test *", true).equals("test*") : "equals " + SearchHelper.prepareMainSearchString("test *", true);
		assert SearchHelper.prepareMainSearchString("test mult terms *", true).equals("test* mult* terms*") : "equals " + SearchHelper.prepareMainSearchString("test mult terms *", true);
		//assert SearchHelper.prepareFacetSearchString("dash-test", true).equals("dash\\-test*") : "equals " + SearchHelper.prepareFacetSearchString("dash-test", true);
		//assert SearchHelper.prepareFacetSearchString("dash -test", true).equals("dash* \\-test*") : "equals " + SearchHelper.prepareFacetSearchString("dash -test", true);
		
		assert SearchHelper.prepareMainSearchString("\"test\"", false).equals("\"test\"") : "equals " + SearchHelper.prepareMainSearchString("\"test\"", false);		
		assert SearchHelper.prepareMainSearchString("\"test\" this + ", false).equals("\"test\" this \\+ ") : "equals " + SearchHelper.prepareMainSearchString("\"test\" this + ", false);		
		assert SearchHelper.prepareMainSearchString("\"test\" this + \"more quotes\" ", false).equals("\"test\" this \\+ \"more quotes\"") : "equals " + SearchHelper.prepareMainSearchString("\"test\" this + \"more quotes\"", false);		
		
		assert SearchHelper.prepareMainSearchString("\"P-Selectivity\" Immunity, and", true).equals("\"P-Selectivity\" Immunity,* and*") : "equals " + SearchHelper.prepareMainSearchString("\"P-Selectivity\" Immunity, and", true);

	}
	
	

}
