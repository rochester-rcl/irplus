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

package edu.ur.dspace.export;

import java.io.IOException;

import edu.ur.dspace.model.BitstreamFileInfo;

/**
 * Creates a file handle for the specified bitstream.
 * 
 * @author Nathan Sarr
 *
 */
public interface BitstreamFileLoader {
	
	/**
	 * Retrieves bitstream file information out of a dspace database.
	 * 
	 * @param bitstreamId
	 * @return the bitstream info found  or null if no bitstream info was found.
	 * @throws IOException
	 */
	public BitstreamFileInfo getBitstreamFileInfo(long bitstreamId) throws IOException; 

}
