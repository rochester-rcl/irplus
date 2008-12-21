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

package edu.ur.hibernate.ir.security.db;

import org.springframework.context.ApplicationContext;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.security.IrClassType;
import edu.ur.ir.security.IrClassTypeDAO;

import edu.ur.ir.repository.InMemoryRepositoryService;

/**
 * Test the persistence methods for Class Types
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IrClassTypeDAOTest {

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

	IrClassTypeDAO irClassTypeDAO = (IrClassTypeDAO) ctx
	.getBean("irClassTypeDAO");
	
	/**
	 * Test irClassType persistance
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseClassTypeDAOTest() throws Exception{
		IrClassType irClassType = new IrClassType(InMemoryRepositoryService.class);
         
 		irClassTypeDAO.makePersistent(irClassType);
 		IrClassType other = irClassTypeDAO.getById(irClassType.getId(), false);
        assert other.equals(irClassType) : "Class types should be equal";
         
        IrClassType itemIrClassTypeByName =  irClassTypeDAO.findByUniqueName(irClassType.getName());
        assert itemIrClassTypeByName.equals(irClassType) : "Class Type should be found";
         
        irClassTypeDAO.makeTransient(other);
        assert  irClassTypeDAO.getById(other.getId(), false) == null : "Should no longer be able to find irClassType";
	}
}
