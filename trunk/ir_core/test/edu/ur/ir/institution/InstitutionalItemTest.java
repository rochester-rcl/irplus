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

import java.util.Set;

import org.testng.annotations.Test;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.repository.Repository;

/**
 * Test the Institutional Item class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class InstitutionalItemTest {
	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testBasicSets() throws CollectionDoesNotAcceptItemsException 
	{
		Repository repository  = new Repository();
		repository.setDescription("myDescription");
		repository.setName("myName");
		
		GenericItem genericItem = new GenericItem("genericItem");
		GenericItem genericItem2 = new GenericItem("genericItem2");
		
		// create a new institution
		InstitutionalCollection institutionalCollection = 
			new InstitutionalCollection(repository, "myInstitution");
		
		// create the owner of the personal item
		InstitutionalItem institutionalItem1 =  new InstitutionalItem(institutionalCollection, genericItem);
		
		InstitutionalItem institutionalItem2 =  new InstitutionalItem(institutionalCollection, genericItem);
		
		InstitutionalItem institutionalItem3 =  new InstitutionalItem(institutionalCollection, genericItem2);
	
	    assert institutionalItem2.equals(institutionalItem1) : "Items should be equal";
	    assert !institutionalItem1.equals(institutionalItem3) : "Institutional Items should not be equal";
	}
	
	public void testInstitutionalItemPublishing()throws CollectionDoesNotAcceptItemsException 
	{
		
		Repository repository  = new Repository();
		repository.setDescription("myDescription");
		repository.setName("myName");
		
		GenericItem genericItem = new GenericItem("genericItem");
		GenericItem genericItem2 = new GenericItem("genericItem2");
		
		// create a new institution
		InstitutionalCollection institutionalCollection = 
			new InstitutionalCollection(repository, "myInstitution");
		
		// create the owner of the personal item
		IrUser user = new IrUser("nate", "password");
		VersionedItem versionedItem = new VersionedItem(user, genericItem);
		
		InstitutionalItem institutionalItem = institutionalCollection.createInstitutionalItem(genericItem);
		InstitutionalItemVersion institutionalItemVersion = 
			institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersion(institutionalItem.getVersionedInstitutionalItem().getLargestVersion());
		
		assert institutionalItemVersion != null : "Published version should not be null";
		
		assert institutionalItemVersion.getItem().equals(genericItem) : 
			"Item should equal ";
		
		
		// add another version to the versioned item.
		versionedItem.addNewVersion(genericItem2);
		
		// add a new version to the institutional item
		institutionalItem.addNewVersion(versionedItem.getCurrentVersion().getItem());

		Set<InstitutionalItemVersion> institutionalItemVersions = institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersions();
		assert institutionalItemVersions.size() == 2 : "There should be two versions "
			+ " but there are " + institutionalItemVersions.size();
	
		InstitutionalItemVersion publishedVersion2 = 
			institutionalItem.getVersionedInstitutionalItem().getInstitutionalItemVersion(versionedItem.getCurrentVersion().getVersionNumber());
		assert publishedVersion2.getItem().equals(genericItem2) : "Generic item " +
		genericItem2 + " should equal " + publishedVersion2.getItem();
		
	}
	

}
