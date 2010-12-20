/**  
   Copyright 2008-2010 University of Rochester

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
package edu.ur.ir.ir_export;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.repository.Repository;

/**
 * Class that allows a collection to be exported.
 * 
 * @author Nathan Sarr
 *
 */
public interface CollectionExportService extends Serializable {

	/**
	 * Export the specified institutional collection.
	 * 
	 * @param collection - collection to export
	 * @param includeChildren - if true children should be exported
	 * @param zipFileDestination - zip file destination to store the collection information
	 */
	public void export(InstitutionalCollection collection, boolean includeChildren, File zipFileDestination) throws IOException;
	
	
	/**
	 * Export all collections in the repository.
	 * 
	 * @param repository - repository to export
	 */
	public void export(Repository repository, File zipFileDestination)throws IOException;
	
	/** 
	 * Create the xml file for the set of collections.
	 * 
	 * @param xmlFile - file to write the xml to
	 * @param collections - set of parent collections to write
	 * @return - list of file information for the collection pictures to be added
	 * 
	 * @throws IOException - if writing to the file fails.
	 */
	public Set<FileInfo> createXmlFile(File xmlFile, Collection<InstitutionalCollection> collections, boolean includeChildren) throws IOException;
	
}
