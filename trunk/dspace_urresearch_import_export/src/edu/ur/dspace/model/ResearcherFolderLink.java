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

/**
 * Represents a link to a 
 * @author nathans
 *
 */
public class ResearcherFolderLink {

	public String title;
	
	public String url;
	
	public String description;
	
	public Long linkId;
	
	public Long folderId;
	
	public String linkType;
	
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ link id = ");
		sb.append(linkId);
		sb.append(" title = ");
		sb.append(title);
		sb.append(" url = ");
		sb.append(url);
		sb.append(" description = ");
		sb.append(description);
		sb.append(" folder id = ");
		sb.append(folderId);
		sb.append(" link type = ");
		sb.append(linkType);
		sb.append("]");
		
		return sb.toString();
	}
	
}
