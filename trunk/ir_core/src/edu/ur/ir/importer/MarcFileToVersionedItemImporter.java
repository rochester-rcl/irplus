/**  
   Copyright 2008 - 2011 University of Rochester

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

package edu.ur.ir.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;

import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.item.VersionedItem;
import edu.ur.ir.user.IrUser;

/**
 * This is the interface for importing marc file information.
 * 
 * @author Nathan Sarr
 *
 */
public interface MarcFileToVersionedItemImporter extends Serializable{
	
	
	/**
	 * Import the marc file into a list of versioned items
	 * 
	 * @param f - marc file to import
	 * @param owner - user who will own the versioned items
	 * @return list of versioned items
	 * @throws FileNotFoundException
	 * @throws NoIndexFoundException 
	 * @throws BadMarcFileException if the marc file could not be parsed
	 */
	public List<VersionedItem> importMarc(File f, IrUser owner) throws FileNotFoundException, 
	NoIndexFoundException,BadMarcFileException;

}
