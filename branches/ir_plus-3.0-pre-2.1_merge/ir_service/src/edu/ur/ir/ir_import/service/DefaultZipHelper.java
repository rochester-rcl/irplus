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

package edu.ur.ir.ir_import.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.log4j.Logger;

import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.ir_import.ZipHelper;

/**
 * Helper class with zip files.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultZipHelper implements ZipHelper {
	
	/** eclipse generated id */
	private static final long serialVersionUID = -3185503653947432767L;

	/** Service for emailing errors */
	private ErrorEmailService errorEmailService;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultZipHelper.class);


	/**
	 * Extracts the entry from the specified zip file.
	 * 
	 * @param File f - file to write the data to
	 * @param entry - entry to extract from the zip file
	 * @param zip - zip file 
	 * 
	 */
	public void getZipEntry(File f, ZipArchiveEntry entry, ZipFile zip) {
		InputStream content = null;
		OutputStream outStream = null;
		OutputStream out = null;

		try {
			content = zip.getInputStream(entry);
			byte[] fileBytes = new byte[1024];
			
			outStream = new FileOutputStream(f);
			out = new BufferedOutputStream(outStream);
			int amountRead = 0;
		     while( (amountRead = content.read(fileBytes)) != -1)
		     {
			     out.write(fileBytes, 0, amountRead);
		     }
		     out.flush();
		}
		catch(Exception e)
		{
		    log.error(e);	
		    errorEmailService.sendError(e);
		}
		finally 
		{
			if( content != null)
			{
			    try {
					content.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
			
			if( out != null )
			{
				try {
					out.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
			
			if( outStream != null )
			{
				try {
					outStream.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
		
	}

}
