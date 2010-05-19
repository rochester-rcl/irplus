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

import edu.ur.ir.oai.exception.BadArgumentException;
import edu.ur.ir.oai.exception.BadResumptionTokenException;
import edu.ur.ir.oai.exception.CannotDisseminateFormatException;
import edu.ur.ir.oai.exception.IdDoesNotExistException;
import edu.ur.ir.oai.exception.NoMetadataFormatsException;
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
	public String getRecord(String identifier, String metadataPrefix) throws CannotDisseminateFormatException, 
	IdDoesNotExistException, BadArgumentException;
	
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
	public String identify() throws BadArgumentException;
	
	/**
	 * List the identifiers.
	 * 
	 * @return a list of identifiers 
	 */
	public String listIdentifiers(String metadataPrefix, 
			String set, 
			String from, 
			String until, 
			String resumptionToken) throws BadArgumentException,
			                               BadResumptionTokenException,
			                               CannotDisseminateFormatException,
			                               NoRecordsMatchException, 
			                               NoSetHierarchyException,
			                               BadArgumentException;
	
	/**
	 * List the records
	 * 
	 * @return a list of records
	 */
	public String listRecords(String metadataPrefix, 
			String set, 
			String from, 
			String until, 
			String resumptionToken) throws BadArgumentException,
			                               BadResumptionTokenException,
			                               CannotDisseminateFormatException,
			                               NoRecordsMatchException, 
			                               NoSetHierarchyException,
			                               BadArgumentException;
    /**
     * Get the list of sets in the repository
     * 
     * @param resumptionToken - resumption token
     * 
     * @return list of sets in the repository.
     * 
     * @throws BadResumptionTokenException - if the resumption token is incorrect
     * @throws NoSetHierarchyException - if this does not support sets
     */
    public String listSets(String resumptionToken) throws BadResumptionTokenException, 
        NoSetHierarchyException,
        BadArgumentException;
    
	/**
	 * Interface for listing the metadata formats for a specific identifier
	 * 
	 * @param identifier - optional oai identifier for a specific item.
	 * 
	 * @return an xml string representing the formats made available by the system
	 *  
	 * @throws BadArgumentException - if a bad argument is passed in
	 * @throws IdDoesNotExistException - if the id does not exist
	 * @throws NoMetadataFormatsException - no metadata formats exist for the specified id
	 */
	public String listMetadataFormats(String identifier) throws BadArgumentException, IdDoesNotExistException, 
	NoMetadataFormatsException;


}
