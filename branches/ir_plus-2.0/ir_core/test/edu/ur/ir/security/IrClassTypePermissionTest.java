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
import edu.ur.ir.user.IrUser;

/**
 * Test the permission classes.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrClassTypePermissionTest 
{
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 * @throws ClassNotFoundException 
	 */
	public void baseIrClassTypePermissionTest() throws ClassNotFoundException
	{
		IrClassType classType = new IrClassType(IrRole.class);

		IrClassTypePermission classTypePermission = new IrClassTypePermission(classType);
		classTypePermission.setName("read");
		classTypePermission.setDescription("Gives users read permission");
		classTypePermission.setId(55l);
		classTypePermission.setVersion(33);
	     
		assert classTypePermission.getIrClassType().equals(classType) : "Class types should be equal";
		assert classTypePermission.getName().equals("read") : "Name should be equal";
		assert classTypePermission.getDescription().equals("Gives users read permission") : "Description should be equal";
		     
	}
	
	/**
	 * Test equals and hash code methods.
	 * @throws ClassNotFoundException 
	 */
	public void testEquals() throws ClassNotFoundException
	{
		IrClassType irClassType1 = new IrClassType(IrRole.class);
		irClassType1.setDescription("irClassTypeDescription");
		irClassType1.setId(55l);
		irClassType1.setVersion(33);


        IrClassTypePermission classTypePermission1 = new IrClassTypePermission(irClassType1);		
        classTypePermission1.setName("permissionName");
        classTypePermission1.setDescription("permissionDescription");
        classTypePermission1.setId(55l);
        classTypePermission1.setVersion(33);
		
		
		IrClassType irClassType2 = new IrClassType(IrUser.class);
		irClassType2.setDescription("irClassTypeDescription2");
		irClassType2.setId(55l);
		irClassType2.setVersion(33);

        IrClassTypePermission classTypePermission2 = new IrClassTypePermission(irClassType2);		
        classTypePermission2.setName("permissionName2");
        classTypePermission2.setDescription("permissionDescription2");
        classTypePermission2.setId(55l);
        classTypePermission2.setVersion(33);
		
		IrClassType irClassType3 = new IrClassType(IrRole.class);
		irClassType3.setDescription("irClassTypeDescription");
		irClassType3.setId(55l);
		irClassType3.setVersion(33);
		
		
        IrClassTypePermission classTypePermission3 = new IrClassTypePermission(irClassType3);		
        classTypePermission3.setName("permissionName");
        classTypePermission3.setDescription("permissionDescription");
        classTypePermission3.setId(55l);
        classTypePermission3.setVersion(33);
		
		assert classTypePermission1.equals(classTypePermission3) : "Class type permissions should be equal";
		assert !classTypePermission1.equals(classTypePermission2) : "Classes should not be equal";
		
		assert classTypePermission1.hashCode() == classTypePermission3.hashCode() : "Hash codes should be the same";
		assert classTypePermission2.hashCode() != classTypePermission3.hashCode() : "Hash codes should not be the same";
	}
}
