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

package edu.ur.dspace.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a researcher 
 * 
 * @author Nathan Sarr
 *
 */
public class DspaceResearcher {

	/** id for the dspace user */
	public int dspaceUserId;
	
	/** id for the researcher in dspace*/
	public int researcherId;
	
	/** interest data for the researcher */
	public String researchInterests;
	
	/** teaching interests for the researcher */
	public String teachingInterests;
	
	/** title for the researcher */
	public String title;
	
	/** department for the researcher */
	public String department;
	
	/** field for the researcher */
	public String field;
	
	/** fax for the researcher */
	public String fax;
	
	/** indicates if the researcher page is public */
	public boolean pagePublic;
	
	/** set of links for the researcher */
	public List<Link> links = new LinkedList<Link>();
	
	/** file name placed in the zip file */
	public String logoFileName;
	
	/** logo file information  */
	public BitstreamFileInfo logoFileInfo;
	
	/** logo id for the logo */
	public Long logoId;
	
	/** Researcher campus location */
	public String campusLocation;
	
	/** Folder for this researcher */
	public DspaceResearcherFolder folder;
	
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ eperson id = ");
		sb.append(dspaceUserId);
		sb.append(" researcherId = ");
		sb.append(researcherId);
	    sb.append(" researcher interests = ");
	    sb.append(researchInterests);
	    sb.append(" teaching interests = ");
	    sb.append(teachingInterests);
	    sb.append(" title = ");
	    sb.append(title);
	    sb.append(" campus location = ");
	    sb.append(campusLocation);
	    sb.append(" department = ");
	    sb.append(department);
	    sb.append(" field = ");
	    sb.append(field);
	    sb.append(" fax = ");
	    sb.append(fax);
	    sb.append(" page public = ");
	    sb.append(pagePublic);
	    sb.append(" log file name = ");
	    sb.append(logoFileName);
	    sb.append(" logo id = ");
	    sb.append(logoId);
	    
		sb.append("]");
	    return sb.toString();
	}
	
	
}
