/**  
   Copyright 2008-2011 University of Rochester

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

import java.io.File;
import java.io.Serializable;
import java.util.List;

import edu.ur.ir.NoIndexFoundException;

/**
 * Service to allow institutional collections to be indexed.
 * 
 * @author Nathan Sarr
 *
 */
public interface InstitutionalCollectionIndexService extends Serializable{

	/**
	 * Add the institutional collection to the index.
	 * 
	 * @param collection - institutional collection to add.
	 * @param collectionIndexFolder - folder which holds the institutional collection index.
	 */
	public void add(InstitutionalCollection collection, File collectionIndexFolder) throws NoIndexFoundException;
	
	/**
	 * Update the institutional collection to the index.
	 * 
	 * @param collection - institutional collection to add.
	 * @param collectionIndexFolder - folder which holds the institutional collection index.
	 */
	public void update(InstitutionalCollection collection, File collectionIndexFolder) throws NoIndexFoundException;
	
	/**
	 * Delete the collection in the index.
	 * 
	 * @param collectionId - id of the collection
	 * @param collectionIndexFolder  - folder location of the collection index
	 */
	public void delete(Long collectionId, File collectionIndexFolder);
	
	/**
	 * Re-index the specified collections.  This can be used to re-index 
	 * all collections
	 * 
	 * @param collections - collections to re index
	 * @param collectionIndexFolder - folder location of the index
	 * @param overwriteExistingIndex - if set to true, will overwrite the existing index.
	 */
	public void add(List<InstitutionalCollection> collections, File collectionIndexFolder,
			boolean overwriteExistingIndex);
	
}
