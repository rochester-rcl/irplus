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

import edu.ur.ir.oai.exception.CannotDisseminateFormatException;
import edu.ur.ir.oai.exception.IdDoesNotExistException;

/**
 * @author NathanS
 *
 */
public interface OaiService {
	
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
	 * @return
	 */
	public String getNamespaceIdentifier();
	
	/**
	 * Get the oai identify response.
	 * 
	 * @return
	 */
	public String identify();
	
}
