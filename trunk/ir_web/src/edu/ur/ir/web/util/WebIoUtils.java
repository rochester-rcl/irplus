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


package edu.ur.ir.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.ur.file.db.FileInfo;
import edu.ur.file.mime.MimeTypeService;
import edu.ur.ir.web.action.ResponseBufferedOutputWriter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Utility for dealing with web file io.
 * 
 * @author Nathan Sarr
 *
 */
public class WebIoUtils {
	
	/**  Service for dealing with mime information  */
	private MimeTypeService mimeTypeService;
	
	/** response writer. */
	private ResponseBufferedOutputWriter responseOutputWriter;
	
	/**  Logger for class */
	private static final Logger log = Logger.getLogger(WebIoUtils.class);
	
	
	/**
	 * @param fileName - name to give the file when down loading - should not
	 * include the extension.
	 * 
	 * @param fileInfo - file information
	 * @param response - response to write to
	 * @param request - request
	 * @param bufferSize - size to make the buffer
	 * 
	 * @throws Exception
	 */
	public void StreamFileInfo(String fileName, FileInfo fileInfo, 
			HttpServletResponse response, HttpServletRequest request, 
			int bufferSize, boolean isPublic, boolean forceDownload) throws Exception
	{
        if (request.getScheme().equals("https"))
        {
        	response.setHeader("Pragma", "public");
        	response.setHeader("Cache-Control", "public");
        	
        	if( isPublic == false)
        	{
        	    response.setHeader("Cache-Control", "must-revalidate");
        	}
        	
        	if( isPublic )
        	{
        		// have the system cache the file if it is public
        		GregorianCalendar calendar = new GregorianCalendar();
        		
        		calendar.add(Calendar.HOUR, 72);
        	    SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        	    
        	    String date = sdf.format(calendar.getTime());
        	    date = date + " GMT";
        		response.setHeader("Expires", date);
        	}
        }
		String fileExtension = "";
        if( fileInfo.getExtension() != null)
        {
            fileExtension = "." + fileInfo.getExtension();	
        }
        
        //default top media type and subtype
        String topMediaName = "application";
        String subTypeName = "octet-stream";
        
        String fullFileName = fileName + fileExtension;
        
        //determine browser type
        
        String browserType = request.getHeader("user-agent");
        
        log.debug("user-agent = " + browserType);
        
        if(browserType.indexOf("MSIE") > -1)
        {
        	//use uri to encode the file name to prevent IE from
        	//putting underscores in file name
            URI uri = new URI(null, fileName + fileExtension, null);
            fullFileName = uri.toASCIIString();
        }
        
        if( forceDownload )
        {
            //tell it to download only
            response.addHeader("Content-Disposition", "attachment; filename=\"" + 
        		 fullFileName + "\"");
        }
        
        String contentType = null;
        if( fileInfo.getExtension() != null)
        {
            contentType = mimeTypeService.findMimeType(fileInfo.getExtension());
        }
        
        if(contentType == null)
        {
        	contentType = topMediaName + "/" + subTypeName;
        }
        
        
        log.debug("Setting the content type to " + contentType);
        
        
        // Set the response MIME type
        response.setContentType(contentType);
        
        // we must read from the file system because certain files are compressed.
        // docx being the offender
        File f = new File(fileInfo.getFullPath());

        // Response length
        response.setHeader("Content-Length", String.valueOf(f.length()));
        

        writeStream(f, response, bufferSize);
	}
	
	/**
	 * Write the stream to the response.
	 * 
	 * @param path location of file
	 * @throws Exception 
	 * @throws Exception
	 */
	private void writeStream(File f, HttpServletResponse response, int bufferSize) throws Exception 
	{
		InputStream is = null;
		OutputStream os = null;
		Exception originalException = null;
		
		try
		{
	        //Write the file to the response output stream.
            is = new FileInputStream(f);
            os = response.getOutputStream();
            responseOutputWriter.writeStream(is, os, bufferSize);
           
		}
        catch(Exception e)
        {
        	log.error(e);
        	originalException = e;
        }
        finally
        {
            if( is != null)
            {
        	    try {
					is.close();
					is = null;
				} catch (IOException e) {
					log.error(e);
				}
            }
        
            if( os != null)
            {
        	    try {
					os.flush();
				} catch (Exception e) {
					log.error(e);
				}
        	    try {
					os.close();
					os = null;
				} catch (Exception e) {
					log.error(e);
				}
            }
            
            if( originalException != null)
            {
            	throw(originalException);
            }
        }
       
	}

	public MimeTypeService getMimeTypeService() {
		return mimeTypeService;
	}

	public void setMimeTypeService(MimeTypeService mimeTypeService) {
		this.mimeTypeService = mimeTypeService;
	}

	public ResponseBufferedOutputWriter getResponseOutputWriter() {
		return responseOutputWriter;
	}

	public void setResponseOutputWriter(
			ResponseBufferedOutputWriter responseOutputWriter) {
		this.responseOutputWriter = responseOutputWriter;
	}

}
