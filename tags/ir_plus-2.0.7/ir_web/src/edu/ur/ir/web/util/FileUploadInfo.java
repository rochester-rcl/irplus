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

/** 
 * Helper class for uploaded file information
 * 
 * @author Nathan Sarr
 *
 */
public class FileUploadInfo {
	
	/** name that was going to be given to file */
	private String fileName;
	
	/** description given by the user  */
	private String description;
	
	public FileUploadInfo(String fileName, String description)
	{
		this.fileName = fileName;
		this.description = description;
		
	}

	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[");
		sb.append("fileName = " + fileName);
		sb.append("description = " + description);
		sb.append("]");
		return sb.toString();
	}


	public String getFileName() {
		return fileName;
	}


	public String getDescription() {
		return description;
	}


}
