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


package edu.ur.file.db.service;

import java.util.LinkedList;
import java.util.List;

import edu.ur.file.checksum.ChecksumCalculator;
import edu.ur.file.checksum.ChecksumService;

/**
 * This is a default checksum service for getting calculators to 
 * calculate a checksum of a particular type.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultChecksumService implements ChecksumService{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 1287611944328999221L;
	
	/** List of checksum calculators */
	private List<ChecksumCalculator> checksumCalculators = 
		new LinkedList<ChecksumCalculator>();

	/**
	 * Get the checksum calculators
	 * @see edu.ur.file.checksum.ChecksumService#getChecksumCalculator(java.lang.String)
	 */
	public ChecksumCalculator getChecksumCalculator(String checksumAlgorithmType) {
		for(ChecksumCalculator cc: checksumCalculators)
		{
			if( cc.getAlgorithmType().equalsIgnoreCase(checksumAlgorithmType))
			{
				return cc;
			}
		}
		return null;
	}

	/**
	 * Get the checksum calculators.
	 * 
	 * @see edu.ur.file.checksum.ChecksumService#getChecksumCalculators()
	 */
	public List<ChecksumCalculator> getChecksumCalculators() {
		return checksumCalculators;
	}
	
	/**
	 * Set the checksum calculators.
	 * 
	 * @param checksumCalculators
	 */
	public void setChecksumCalculators(List<ChecksumCalculator> checksumCalculators)
	{
		this.checksumCalculators = checksumCalculators;
	}

}
