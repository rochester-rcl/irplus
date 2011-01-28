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

package edu.ur.ir.file;

import edu.ur.dao.CrudDAO;
import edu.ur.file.db.FileInfo;

/**
 * Interface for transformed file data access.
 * 
 * @author Nathan Sarr
 *
 */
public interface TransformedFileDAO extends CrudDAO<TransformedFile>{
	
	/**
	 * Get a transformed file by it's ir file id and the type 
	 * 
	 * @param irFileId - id of the file
	 * @param transformedFileTypeName - type to download
	 * 
	 * @return - the transformed file type name
	 */
	public FileInfo getTransformedFile(Long irFileId, String transformedFileTypeName);
}
