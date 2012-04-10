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

import java.util.List;

import edu.ur.dao.CrudDAO;

/**
 * Get the group workspace project page institutional items.
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupWorkspaceProjectPageInstitutionalItemDAO  extends CrudDAO<GroupWorkspaceProjectPageInstitutionalItem>
{
	/**
	 * Get the root institutional Items for given group workspace project page
	 * 
     * @param projectPageId - the id of the project page
	 * @return List of root institutional Items found .
	 */
	public List<GroupWorkspaceProjectPageInstitutionalItem> getRootItems(final Long projectPageId);
   
    
	/**
	 * Get institutional Items for specified project page and specified parent folder
	 * 
	 * @param projectPageId - the id of the project page
     * @param parentFolderId - the id of the parent folder 
     * 
	 * @return List of institutional Items found.
	 */
	public List<GroupWorkspaceProjectPageInstitutionalItem> getItems(final Long projectPageId, final Long parentFolderId);
	
	
    /**
	 * Find the specified institutional Items for the given project page.
	 * 
	 * @param projectPageId project page for the institutional Item
	 * @param institutional ItemIds Ids of  the institutional Item
	 * 
	 * @return List of institutional Items found
	 */
	public List<GroupWorkspaceProjectPageInstitutionalItem> getItems(final Long projectPageId, final List<Long> institutionalItemIds);

	/**
	 * Get a count of the project page institutional Items containing this item
	 * 
	 * @param itemId Item id to search for in the project page institutional Items
	 * 
	 * @return Count of generic item found in project page
	 */
	public Long getCount(Long itemId);

	/**
	 * Get project page institutional Items containing this item
	 * 
	 * @param itemId Item id to search for in the project page institutional Items
	 * 
	 * @return List of project page Institutional items
	 */
	public List<GroupWorkspaceProjectPageInstitutionalItem> getItems(Long itemId);

}
