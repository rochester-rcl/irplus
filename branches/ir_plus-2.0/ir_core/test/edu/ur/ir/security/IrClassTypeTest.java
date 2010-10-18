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

package edu.ur.ir.security;

import org.testng.annotations.Test;

import edu.ur.ir.user.IrRole;

/**
 * Test the permission classes.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrClassTypeTest {
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicClassTypeSets() 
	{
		IrClassType irClassType = new IrClassType();
		irClassType.setName("className");
		assert irClassType.getName().equals("className") : "ir class type should equal className"; 
	}
	
	public void testConstructor() throws ClassNotFoundException
	{
		IrClassType classType = new IrClassType(IrRole.class);
		
		assert classType.getName().equals("edu.ur.ir.user.IrRole") : 
			"Should equal edu.ur.ir.user.IrRole " + 
		    " but equals " + classType.getName();
	}
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
		IrClassType irClassType1 = new IrClassType();
		irClassType1.setName("irClassTypeName");
		irClassType1.setDescription("irClassTypeDescription");
		irClassType1.setId(55l);
		irClassType1.setVersion(33);
		
		IrClassType irClassType2 = new IrClassType();
		irClassType2.setName("irClassTypeName2");
		irClassType2.setDescription("irClassTypeDescription2");
		irClassType2.setId(55l);
		irClassType2.setVersion(33);

		
		IrClassType irClassType3 = new IrClassType();
		irClassType3.setName("irClassTypeName");
		irClassType3.setDescription("irClassTypeDescription");
		irClassType3.setId(55l);
		irClassType3.setVersion(33);
		
		assert irClassType1.equals(irClassType3) : "Classes should be equal";
		assert !irClassType1.equals(irClassType2) : "Classes should not be equal";
		
		assert irClassType1.hashCode() == irClassType3.hashCode() : "Hash codes should be the same";
		assert irClassType2.hashCode() != irClassType3.hashCode() : "Hash codes should not be the same";
	}
}
