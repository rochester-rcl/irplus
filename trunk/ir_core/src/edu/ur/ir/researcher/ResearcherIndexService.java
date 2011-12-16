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

import java.io.File;
import java.io.Serializable;
import java.util.List;

import edu.ur.ir.NoIndexFoundException;

/**
 * Index for researcher
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface ResearcherIndexService extends Serializable{
	
	/**
	 * Add the researcher to the index.
	 * 
	 * @param researcher
	 * @param researcherIndexFolder
	 */
	public void addToIndex(Researcher researcher, File researcherIndexFolder) throws NoIndexFoundException;
	
	/**
	 * Update researcher in the index.
	 * 
	 * @param researcher
	 * @param researcherIndexFolder
	 */
	public void updateIndex(Researcher researcher, File researcherIndexFolder) throws NoIndexFoundException;
	
	/**
	 * Delete the researcher in the index.
	 * 
	 * @param researcherId - id of the researcher
	 * @param researcherIndexFolder  - folder location of the researcher index
	 */
	public void deleteFromIndex(Long researcherId, File researcherIndexFolder);
	
	/**
	 * Re-index the specified researchers.  This can be used to re-index 
	 * all researchers
	 * 
	 * @param researchers - researchers to re index
	 * @param researcherIndexFolder - folder location of the index
	 * @param overwriteExistingIndex - if set to true, will overwrite the exiting researcher.
	 */
	public void addResearchers(List<Researcher> researchers, File researcherIndexFolder,
			boolean overwriteExistingIndex);

}
