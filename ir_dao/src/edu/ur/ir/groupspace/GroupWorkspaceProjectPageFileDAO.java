/**  
   Copyright 2008-2012 University of Rochester

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
 * @author Nathan Sarr
 *
 */
public interface GroupWorkspaceProjectPageFileDAO extends CrudDAO<GroupWorkspaceProjectPageFile>
{
	/**
	 * Get the files for group workspace project page id and listed file ids.  If the list of fileIds 
	 * is null no files are returned.
	 * 
	 * @param groupWorkspaceProjectPageId
	 * @param fileIds
	 * 
	 * @return the found files
	 */
	public List<GroupWorkspaceProjectPageFile> getFiles(Long projectPageId, List<Long> fileIds);

	/**
	 * Get the group workspace project page file for given project id and ir file id .
	 * 
	 * @param researcherId
	 * @param irFileId
	 * 
	 * @return the found files
	 */
	public GroupWorkspaceProjectPageFile getWithSpecifiedIrFile(Long projectPageId, Long irFileId);

	/**
	 * Get the files for group workspace project page id and folder id .
	 * 
	 * @param projectPageId
	 * @param folderId
	 * 
	 * @return the found files
	 */
	public List<GroupWorkspaceProjectPageFile> getFiles(Long projectPageId, Long folderId);
	
	
	/**
	 * Get the root files 
	 * 
	 * @param projectPageId
	 * 
	 * @return the found files
	 */
	public List<GroupWorkspaceProjectPageFile> getRootFiles(Long projectPageId);
	

	/**
	 * Get a count of the project page files containing this irFile
	 * 
	 * @param irFileId IrFile id to find in project page files
	 * 
	 *  @return Count of project page files containing this ir file
	 */
	public Long getCountWithSpecifiedIrFile(Long irFileId) ;

}
