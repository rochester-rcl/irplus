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

package edu.ur.ir.researcher;

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;


/**
 * Interface for creating and saving researcher file information
 * to a relational database.
 * 
 * @author Sharmila Ranganathan
 *
 */
@SuppressWarnings("unchecked")
public interface ResearcherFileDAO extends CountableDAO, 
CrudDAO<ResearcherFile>
{
	/**
	 * Get the files for researcher id and listed file ids.  If the list of fileIds 
	 * is null no files are returned.
	 * 
	 * @param researcherId
	 * @param fileIds
	 * 
	 * @return the found files
	 */
	public List<ResearcherFile> getFiles(Long researcherId, List<Long> fileIds);

	/**
	 * Get the researcher file for given researcher id and ir file id .
	 * 
	 * @param researcherId
	 * @param irFileId
	 * 
	 * @return the found files
	 */
	public ResearcherFile getFileForResearcherWithSpecifiedIrFile(Long researcherId, Long irFileId);

	/**
	 * Get the files for researcher id and folder id .
	 * 
	 * @param researcherId
	 * @param folderId
	 * 
	 * @return the found files
	 */
	public List<ResearcherFile> getFilesInAFolderForResearcher(Long researcherId, Long folderId);
	
	
	/**
	 * Get the root files 
	 * 
	 * @param researcherId
	 * 
	 * @return the found files
	 */
	public List<ResearcherFile> getRootFiles(Long researcherId);
	
	/**
	 * Get the files with specified ir file id .
	 * 
	 * @param irFileId
	 * 
	 * @return the found files
	 */
	public Long getFileWithSpecifiedIrFile(Long irFileId);

	/**
	 * Get a count of the researcher files containing this irFile
	 * 
	 * @param irFileId IrFile id to find in researcher files
	 * 
	 *  @return Count of researcher files containing this ir file
	 */
	public Long getResearcherFileCount(Long irFileId) ;
}