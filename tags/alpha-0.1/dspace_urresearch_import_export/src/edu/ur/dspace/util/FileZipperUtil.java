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

package edu.ur.dspace.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 * Utitlity for zipping up a set of files
 * 
 * @author Nathan Sarr
 *
 */
public class FileZipperUtil {
	
	/**
	 * Creates a zip file with the specified files.
	 * 
	 * @param destination - zip file to create
	 * @param fileNames - list of files to add tot he zip file
	 * @throws IOException
	 */
	public static void createZipFile(String destination, List<ZipFileNameHelper> fileNames) throws IOException
	{
	    byte[] byteArray = new byte[1024];
	    ZipOutputStream out = null;
	    FileInputStream in = null;
	    try
	    {
	       // Create the ZIP file
	       out = new ZipOutputStream(new FileOutputStream(destination));
	       
           // add the log files
	       for( ZipFileNameHelper f : fileNames)
	       {
	        	in = new FileInputStream(new File(f.absoluteFilePath));
	        	 // Add ZIP entry to output stream.
	            out.putNextEntry(new ZipEntry(f.zipFileName));
	            
	            // Transfer bytes from the file to the ZIP file
	            int len = 0;
	            while ((len = in.read(byteArray)) > 0) {
	                out.write(byteArray, 0, len);
	            }
	    
	            // Complete the entry
	            out.closeEntry();
	            in.close();
	        }
	       
	    }
	    finally
	    {
	    	if( in != null)
	    	{
	    		in.close();
	    	}
	    	if( out != null)
	    	{
	    	    // Complete the ZIP file
	            out.close();
	    	}
	    }

	}
	
	
	/**
	 * Get an entry from the specified zip file
	 * 
	 * @param f  - zip file to read from.
	 * @param nameToRead - name of the file to read in  from the zip file
	 * @param fileName - name to give the file to create
	 * 
	 * @return- an in memory file.
	 * @throws IOException
	 */
	public static File getZipEntry(File f, String nameToRead, String fileName) throws IOException
	{
		 byte[] fileBytes = new byte[1024];
		 File fileOut = new File(fileName);
		 FileOutputStream outStream = null;
	     FileInputStream fileInputStream = null;
	     ZipInputStream zipInputStream = null;
	     ZipEntry temp;
	     
		 try
		 {
		     outStream = new FileOutputStream(fileOut);
		     fileInputStream = new FileInputStream(f);
		     zipInputStream = new ZipInputStream(fileInputStream);
		     
		     boolean found = false;
		     while( !found && (temp=zipInputStream.getNextEntry()) != null )
		     {
			     System.out.println("entry = " +  temp.getName());
			     System.out.println("name to Read = " + nameToRead);
			     System.out.println("equals = " + temp.getName().equals(nameToRead));
			     if( temp.getName().equals(nameToRead))
			     {
				     found = true;
			     }
		     }
		 
		     // write the data to the file
		     if( found)
		     {
			     int amountRead = 0;
			     while( (amountRead = zipInputStream.read(fileBytes)) != -1)
			     {
				     outStream.write(fileBytes, 0, amountRead);
			     }
			     outStream.flush();
		     }
		 }
		 finally
		 {
			 if( outStream != null)
			 {
		         outStream.close();
			 }
			 
			 if( zipInputStream != null)
			 {
		         zipInputStream.close();
			 }
			 
			 if(fileInputStream != null)
			 {
		         fileInputStream.close();
			 }
		 }
		 
		return fileOut;
		 
	}

}
