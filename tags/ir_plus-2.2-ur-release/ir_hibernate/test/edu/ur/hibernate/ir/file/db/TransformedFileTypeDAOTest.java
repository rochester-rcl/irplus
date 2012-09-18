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

package edu.ur.hibernate.ir.file.db;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.testng.annotations.Test;

import edu.ur.hibernate.ir.test.helper.ContextHolder;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.TransformedFileTypeDAO;

/**
 * Test the persistence methods for transformed file type Information
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class TransformedFileTypeDAOTest {
	

	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();

     TransformedFileTypeDAO transformedFileTypeDAO = (TransformedFileTypeDAO) ctx
	.getBean("transformedFileTypeDAO");

	/**
	 * Test transformed file type persistence
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void baseTransformedFileTypeDAOTest() throws Exception{
		

 		TransformedFileType transformedFileType = new TransformedFileType("myFileType");
 		transformedFileType.setDescription("transformedFileTypeDescription");
 		transformedFileType.setSystemCode("systemCode");
         
        transformedFileTypeDAO.makePersistent(transformedFileType);
        TransformedFileType other = transformedFileTypeDAO.getById(transformedFileType.getId(), false);
        assert other.equals(transformedFileType) : "transformed file types should be equal";
         
        List<TransformedFileType> transformedFileTypes = transformedFileTypeDAO.getAllOrderByName(0, 1);
        assert transformedFileTypes.size() == 1 : "One language type should be found";
         
        TransformedFileType transformedFileTypeByName = transformedFileTypeDAO.findByUniqueName(transformedFileType.getName());
        assert transformedFileTypeByName.equals(transformedFileType) : "transformed file should be found";
         
        TransformedFileType transformedFileTypeBySystemCode = transformedFileTypeDAO.findBySystemCode("systemCode");
        assert transformedFileTypeBySystemCode.equals(transformedFileType);
        transformedFileTypeDAO.makeTransient(other);
        assert transformedFileTypeDAO.getById(other.getId(), false) == null : "Should no longer be able to find transformedFile type";
	}

}
