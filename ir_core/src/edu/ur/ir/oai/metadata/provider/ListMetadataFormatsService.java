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
import edu.ur.ir.oai.exception.IdDoesNotExistException;
import edu.ur.ir.oai.exception.NoMetadataFormatsException;

/**
 * Service to send a response for the metadata formats supported.
 * 
 * @author Nathan Sarr
 *
 */
public interface ListMetadataFormatsService extends Serializable{
	
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
	public String listMetadataFormats(String identifier) throws BadArgumentException, IdDoesNotExistException, NoMetadataFormatsException;

}
