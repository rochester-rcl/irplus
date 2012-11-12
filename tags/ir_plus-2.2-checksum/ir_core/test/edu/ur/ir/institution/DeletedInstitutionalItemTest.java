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

package edu.ur.ir.institution;

import java.util.Date;

import org.testng.annotations.Test;

import edu.ur.ir.user.IrUser;

/**
 * Test the deleted Institutional Item class
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DeletedInstitutionalItemTest {
	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		DeletedInstitutionalItem deletedInstitutionalItem  = new DeletedInstitutionalItem();
		deletedInstitutionalItem.setInstitutionalCollectionName("institutionalCollectionName");
		deletedInstitutionalItem.setInstitutionalItemName("institutionalItemName");
		deletedInstitutionalItem.setInstitutionalItemId(1001l);
		
		Date date = new Date();
		deletedInstitutionalItem.setDeletedDate(date);
		
		IrUser deletedBy = new IrUser("name", "pass");
		deletedInstitutionalItem.setDeletedBy(deletedBy);
		
		assert deletedInstitutionalItem.getDeletedBy().equals(deletedBy) : "Deleted by user should be equal";
		assert deletedInstitutionalItem.getDeletedDate().equals(date) : "Deleted date should be equal";
		assert deletedInstitutionalItem.getInstitutionalCollectionName().equals("institutionalCollectionName") : "should be equal to institutionalCollectionName";
		assert deletedInstitutionalItem.getInstitutionalItemName().equals("institutionalItemName") : "should be equal to institutionalItemName";
		assert deletedInstitutionalItem.getInstitutionalItemId().equals(1001l) : "Should be equal to 1001";
	}
	
	public void testEqual()
	{
		
		DeletedInstitutionalItem deletedInstitutionalItem  = new DeletedInstitutionalItem();
		deletedInstitutionalItem.setInstitutionalCollectionName("institutionalCollectionName");
		deletedInstitutionalItem.setInstitutionalItemName("institutionalItemName");
		deletedInstitutionalItem.setInstitutionalItemId(1001l);
		deletedInstitutionalItem.setDeletedDate(new Date());
		
		IrUser deletedBy = new IrUser("name", "pass");
		deletedInstitutionalItem.setDeletedBy(deletedBy);

		DeletedInstitutionalItem deletedInstitutionalItem1  = new DeletedInstitutionalItem();
		deletedInstitutionalItem1.setInstitutionalCollectionName("institutionalCollectionName1");
		deletedInstitutionalItem1.setInstitutionalItemName("institutionalItemName1");
		deletedInstitutionalItem1.setInstitutionalItemId(1002l);
		deletedInstitutionalItem1.setDeletedDate(new Date());
		
		IrUser deletedBy1 = new IrUser("name1", "pass1");
		deletedInstitutionalItem1.setDeletedBy(deletedBy1);
		
		DeletedInstitutionalItem deletedInstitutionalItem2  = new DeletedInstitutionalItem();
		deletedInstitutionalItem2.setInstitutionalCollectionName("institutionalCollectionName");
		deletedInstitutionalItem2.setInstitutionalItemName("institutionalItemName");
		deletedInstitutionalItem2.setInstitutionalItemId(1001l);
		deletedInstitutionalItem2.setDeletedDate(new Date());
		
		deletedInstitutionalItem2.setDeletedBy(deletedBy);		

		assert deletedInstitutionalItem.equals(deletedInstitutionalItem2) : "Deleted institutional should be equal";
		assert !deletedInstitutionalItem1.equals(deletedInstitutionalItem2) : "Deleted institutional  should not be equal";
		
		assert deletedInstitutionalItem.hashCode() == deletedInstitutionalItem2.hashCode() : "Hash codes should be the same";
		assert deletedInstitutionalItem1.hashCode() != deletedInstitutionalItem2.hashCode() : "Hash codes should not be the same";
	}
	

}
