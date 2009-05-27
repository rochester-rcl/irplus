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

package edu.ur.dspace.load;

import edu.ur.dspace.model.DspaceItem;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.repository.Repository;

/**
 * Handles populating a generic item with data.
 * 
 * @author Nathan Sarr
 *
 */
public interface GenericItemPopulator {
	
	/**
	 * Creates a generic item in the specified repository.
	 * 
	 * @param repository - repository to place the generic item in
	 * @param dspaceItem - dspace item to ingest.
	 * @return created generic item.
	 * 
	 * @throws NoIndexFoundException
	 */
	public GenericItem createGenericItem(Repository repository, DspaceItem dspaceItem) throws NoIndexFoundException;

}
