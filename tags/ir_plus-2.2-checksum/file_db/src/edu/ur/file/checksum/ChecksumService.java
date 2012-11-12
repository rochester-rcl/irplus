/**
  Copyright 2008 University of Rochester

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
package edu.ur.file.checksum;

import java.io.Serializable;
import java.util.List;


/**
 * Service class for dealing with checksum calculators.
 * 
 * @author Nathan Sarr
 *
 */
public interface ChecksumService  extends Serializable{
	
	/**
	 * Get the checksum calculators managed by this service.
	 * 
	 * @return the list of file document creators.
	 */
	public List<ChecksumCalculator> getChecksumCalculators();
	
	
	/**
	 * Get a checksum calculator based on the type of checksum that 
	 * is to be created.
	 * 
	 * @param the type of checksum algorithm calculator you would like
	 * 
	 * @return the checksum calculator if found otherwise null.
	 */
	public ChecksumCalculator getChecksumCalculator(String checksumAlgorithmType);
	
	

}
