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


package edu.ur.ir.web.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class to write data out to the stream
 * 
 * @author Nathan Sarr
 *
 */
public class ResponseBufferedOutputWriter {
	
	/**
	 * Writes the input stream to the given output stream.  Both streams are wrpaped
	 * in buffered streams.
	 * 
	 * @param is - input stream to read from.
	 * @param os - output stream to write to.
	 * @param bufferSize - size of buffer to create.
	 * @throws IOException 
	 * 
	 * @throws Exception
	 */
	public static void writeStream(InputStream is, OutputStream os, int bufferSize) throws IOException 
	{
        final BufferedInputStream input = new BufferedInputStream(is);
        final BufferedOutputStream output = new BufferedOutputStream(os);
        
        final byte[] buffer = new byte[bufferSize];

        try
        {
            int count = 0;
            while (count != -1)
            {
                count = input.read(buffer, 0, bufferSize);
                // write out those same bytes
                if( count > 0 )
                {
                    output.write(buffer, 0, count);
                }
            }
        }
        finally
        {
            output.flush();
            input.close();
            output.close();
        }
	}

}
