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

package edu.ur.ir.oai.metadata.provider.service;

import org.apache.log4j.Logger;

/**
 * Helps deal with OAI information
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultOaiIdentifierHelper {
	
	/**  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(DefaultOaiIdentifierHelper.class);
	
	/**
	 * Parse the oai id.
	 * 
	 * @param oaiId
	 * @return the unique identifier for the publication.  this returns null if the
	 * id could not be established.
	 */
	public static Long getInstitutionalItemVersionId(String oaiId)
	{
		String[] values = oaiId.split(":");
		Long value = null;
		if( values.length == 3)
		{
			try
			{
			    value = Long.valueOf(values[2]);
			}
			catch(Exception e)
			{
				log.error("the oai identifier " + oaiId + " could not be parsed");
			}
		}
		return value;
	}

}
