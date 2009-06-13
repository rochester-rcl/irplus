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

import org.apache.log4j.Logger;

/**
 * Class to write data out to the stream
 * 
 * @author Nathan Sarr
 *
 */
public class ResponseBufferedOutputWriter {
	
	/**  Logger for class */
	private static final Logger log = Logger.getLogger(ResponseBufferedOutputWriter.class);
	
	/**
	 * Writes the input stream to the given output stream.  Both streams are wrpaped
	 * in buffered streams.
	 * 
	 * @param is - input stream to read from.
	 * @param os - output stream to write to.
	 * @param bufferSize - size of buffer to create.
	 * @throws Exception 
	 * @throws IOException 
	 * 
	 * @throws Exception
	 */
	public void writeStream(InputStream is, OutputStream os, int bufferSize) throws Exception
	{

        BufferedInputStream input = new BufferedInputStream(is);
        BufferedOutputStream output = new BufferedOutputStream(os);
        Exception originalException = null;
        byte[] buffer = new byte[bufferSize];

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
      
        catch(Exception e)
        {
        	// this will only happen in a tomcat contanier - it is when
        	// the user selects cancel on the download window.
        	if( e.getClass().getName().equals("ClientAbortException"))
        	{
        		log.error("client abort exception - this should be ok ",e);
        	}
        	else
        	{
        		originalException = e;
        	}
        }
        finally
        {
			try {
				if( output != null)
				{
				    output.flush();
				}
			} catch (Exception e) {
				log.error(e);
			}
			try {
				if( input != null)
				{
				    input.close();
				}
			} catch (Exception e) {
				log.error(e);
			}
			try {
				if( output != null)
				{
				    output.close();
				}
			} catch (Exception e) {
				log.error(e);
			}
			buffer = null;
			output = null;
			input = null;
        }
        
        if( originalException != null)
        {
        	throw(originalException);
        }
	}

}
