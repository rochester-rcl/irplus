/**  
   Copyright 2008 - 2012 University of Rochester

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

package edu.ur.ir.groupspace;

import org.testng.annotations.Test;

import edu.ur.ir.item.GenericItem;

@Test(groups = { "baseTests" }, enabled = true)
public class GroupWorkspaceProjectPagePublicationTest {

	
	/**
	 * Test the basic get and set  methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		GenericItem genericItem = new GenericItem("genericItem");
		
		// create the owner of the personal item
		GroupWorkspace groupWorkspace = new GroupWorkspace("groupWorkspace");
		GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();


		GroupWorkspaceProjectPagePublication publication = new GroupWorkspaceProjectPagePublication(groupWorkspaceProjectPage, genericItem, 1);
		assert publication.getPublication() != null : "Should be able to find publication";
		
		assert publication.getPublication().equals(genericItem) : "Publications should be equal";
	    assert publication.getGroupWorkspaceProjectPage().equals(groupWorkspaceProjectPage): "Researchers should be equal";
	}
	
}
