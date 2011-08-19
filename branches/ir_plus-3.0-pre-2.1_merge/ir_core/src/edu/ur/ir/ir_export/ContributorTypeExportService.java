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
import edu.ur.ir.person.ContributorType;

/**
 * Service to export contributor type information.
 * 
 * @author Nathan Sarr
 *
 */
public interface ContributorTypeExportService extends Serializable{
	
	/** 
	 * Create the xml file for the set of collections.
	 * 
	 * @param xmlFile - file to write the xml to
	 * @param contributor types - set of contributor types to export
	 * 
	 * @throws IOException - if writing to the file fails.
	 */
	public void createXmlFile(File xmlFile, Collection<ContributorType> contributorTypes) throws IOException;


}
