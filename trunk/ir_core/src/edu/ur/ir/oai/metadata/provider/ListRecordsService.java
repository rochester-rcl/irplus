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

import edu.ur.ir.oai.exception.BadArgumentException;
import edu.ur.ir.oai.exception.BadResumptionTokenException;
import edu.ur.ir.oai.exception.CannotDisseminateFormatException;
import edu.ur.ir.oai.exception.NoRecordsMatchException;
import edu.ur.ir.oai.exception.NoSetHierarchyException;

/**
 * @author NathanS
 *
 */
public interface ListRecordsService {

	/**
	 * Get the list identifers as an xml document.
	 * 
	 * @param metadataPrefix - metadata prefix
	 * @param set - set to get
	 * @param from - from start date
	 * @param until - until date
	 * @param resumptionToken - resumption token
	 * 
	 * @return - list of identifiers
	 * 
	 * @throws BadResumptionTokenException
	 * @throws CannotDisseminateFormatException
	 * @throws NoRecordsMatchException
	 * @throws NoSetHierarchyException
	 */
	public String listRecords(String metadataPrefix, 
			String set, 
			String from, 
			String until, 
			String resumptionToken)throws BadArgumentException,
			BadResumptionTokenException, 
			CannotDisseminateFormatException, 
			NoRecordsMatchException, 
			NoSetHierarchyException;
	
}
