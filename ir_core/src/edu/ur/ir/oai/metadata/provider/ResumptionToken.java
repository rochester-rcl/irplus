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


/**
 * Interface for the resumption token.
 * 
 * @author Nathan Sarr
 *
 */
public interface ResumptionToken extends Serializable{
	
	/**
	 * Get the resumption token as a string.
	 * 
	 * @return the resumption token as a valid string that can be parsed by the parse
	 * resumption token method.
	 */
	public String getAsTokenString();
	
	/**
	 * Helper method to parse the resumption token.
	 * 
	 * @param resumptionToken
	 * @throws BadResumptionTokenException 
	 * @throws BadArgumentException 
	 */
	public void parseResumptionToken(String resumptionToken) throws BadResumptionTokenException, BadArgumentException;
	

}
