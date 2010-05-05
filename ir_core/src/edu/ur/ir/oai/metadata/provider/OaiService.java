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

package edu.ur.ir.oai.metadata.provider;

import java.io.Serializable;

import edu.ur.ir.oai.exception.BadResumptionTokenException;
import edu.ur.ir.oai.exception.CannotDisseminateFormatException;
import edu.ur.ir.oai.exception.IdDoesNotExistException;
import edu.ur.ir.oai.exception.NoRecordsMatchException;
import edu.ur.ir.oai.exception.NoSetHierarchyException;

/**
 * @author NathanS
 *
 */
public interface OaiService extends Serializable{
	
	/**
	 * Returns the body of the record in the specified format.
	 * 
	 * @param identifier - identifier 
	 * @param metadataPrefix
	 * 
	 * @return the body of the record in the specified format
	 */
	public String getRecord(String identifier, String metadataPrefix) throws CannotDisseminateFormatException, IdDoesNotExistException;
	
	/**
	 * Get the oai namespace identifier.
	 * 
	 * @return the namsespace identifier
	 */
	public String getNamespaceIdentifier();
	
	/**
	 * Get the oai identify response.
	 * 
	 * @return an xml document for the identify request
	 */
	public String identify();
	
	/**
	 * List the identifiers.
	 * 
	 * @return a list of identifiers 
	 */
	public String listIdentifiers(String metadataPrefix, String set, String from, String until, String resumptionToken) throws BadResumptionTokenException, CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException ;
	
}
