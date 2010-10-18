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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;


/**
 * Calculates the Md5Checksum of the specified file.
 * 
 * @author Nathan Sarr
 *
 */
public class Md5ChecksumCalculator implements ChecksumCalculator{
	
	public static final String algorithmType = "MD5";

	/** 
	 * Calculate the md5 checksum of the specified file.
	 * 
	 * @see edu.ur.file.db.ChecksumCalculator#calculate(java.io.File)
	 */
	public String calculate(File f) {
		 
		String checksum = null;
		try {
			InputStream fis = new FileInputStream(f);

			byte[] buffer = new byte[1024];
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			int numRead;
			do {
				numRead = fis.read(buffer);
				if (numRead > 0) {
					messageDigest.update(buffer, 0, numRead);
				}
			} while (numRead != -1);
			fis.close();
			byte[] value = messageDigest.digest();

			Hex hex = new Hex();
			checksum = new String(hex.encode(value));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	     
	     return checksum;

	}

	/**
	 * Returns the algorithm type
	 * 
	 * @see edu.ur.file.checksum.ChecksumCalculator#getAlgorithm()
	 */
	public String getAlgorithmType() {
		return algorithmType;
	}
}
