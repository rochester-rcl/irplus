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
package edu.ur.ir.export;

import java.io.Serializable;

import org.marc4j.marc.Record;

import edu.ur.ir.institution.InstitutionalItemVersion;

/**
 * Service to export an institutional item version into a MARC21 record.
 * 
 * @author Nathan Sarr
 *
 */
public interface MarcExportService extends Serializable{
	
	/**
	 * Export the record and return a marc4j Record.
	 * 
	 * @param version - institutional item version to convert.
	 * @param showAllFields - will show all fields.  If set to false only header
	 * title and main author will be shown.
	 * @param isRda - determine if this should be an RDA record
	 * 
	 * @return marc4j record
	 */
	public Record export(InstitutionalItemVersion version, boolean showAllFields, boolean isRda);

}
