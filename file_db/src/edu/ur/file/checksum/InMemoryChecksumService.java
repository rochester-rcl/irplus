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

import java.util.LinkedList;
import java.util.List;

/**
 * In-memory implementation of the checksum service.  This
 * is for testing purposes only.
 * 
 * @author Nathan Sarr
 *
 */
public class InMemoryChecksumService implements ChecksumService{
	
	private LinkedList<ChecksumCalculator> checksumCalculators = 
		    new LinkedList<ChecksumCalculator>();
	
	public InMemoryChecksumService()
	{
		checksumCalculators.add(new Md5ChecksumCalculator());
	}

	
	public ChecksumCalculator getChecksumCalculator(String checksumAlgorithmType) {
		for(ChecksumCalculator cc : checksumCalculators)
		{
			if( cc.getAlgorithmType().equals(checksumAlgorithmType))
			{
				return cc;
			}
		}
		return null;
	}

	public List<ChecksumCalculator> getChecksumCalculators() {
		return checksumCalculators;
	}

}
