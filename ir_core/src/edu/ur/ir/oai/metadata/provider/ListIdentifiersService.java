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

import edu.ur.ir.oai.exception.BadResumptionTokenException;
import edu.ur.ir.oai.exception.CannotDisseminateFormatException;
import edu.ur.ir.oai.exception.NoRecordsMatchException;
import edu.ur.ir.oai.exception.NoSetHierarchyException;

/**
 * @author Nathan Sarr
 *
 */
public interface ListIdentifiersService {
	
	/**
	 * Get the list identifers as an xml document.
	 * 
	 * @return - the list 
	 */
	public String listIdentifiers(String metadataPrefix, String set, String from, String until, String resumptionToken)throws BadResumptionTokenException, CannotDisseminateFormatException, NoRecordsMatchException, NoSetHierarchyException ;



}
