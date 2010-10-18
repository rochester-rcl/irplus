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

package edu.ur.ir.item;

import org.testng.annotations.Test;

import edu.ur.ir.item.LanguageType;


/**
 * Test the Language type Class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class LanguageTypeTest {
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		LanguageType lt = new LanguageType();
		lt.setName("ltName");
		lt.setDescription("ltDescription");
		lt.setId(55l);
		lt.setVersion(33);
		lt.setIso639_1("en");
		lt.setIso639_2("eng");
		
		assert lt.getName().equals("ltName") : "Should equal ltName";
		assert lt.getIso639_1().equals("en") : "Should equal en but equals " + lt.getIso639_1();
		assert lt.getIso639_2().equals("eng");
		assert lt.getDescription().equals("ltDescription") : "Shoud equal ltDescription";
		assert lt.getId().equals(55l) : "Should equal 55l";
		assert lt.getVersion() == 33 : "Should equal 33";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		LanguageType lt1 = new LanguageType();
		lt1.setName("ltName");
		lt1.setDescription("ltDescription");
		lt1.setId(55l);
		lt1.setVersion(33);
		
		LanguageType lt2 = new LanguageType();
		lt2.setName("ltName2");
		lt2.setDescription("ltDescription2");
		lt2.setId(55l);
		lt2.setVersion(33);

		
		LanguageType lt3 = new LanguageType();
		lt3.setName("ltName");
		lt3.setDescription("ltDescription");
		lt3.setId(55l);
		lt3.setVersion(33);
		
		assert lt1.equals(lt3) : "Language types should be equal";
		assert !lt1.equals(lt2) : "Language types should not be equal";
		
		assert lt1.hashCode() == lt3.hashCode() : "Hash codes should be the same";
		assert lt2.hashCode() != lt3.hashCode() : "Hash codes should not be the same";
	}
}
