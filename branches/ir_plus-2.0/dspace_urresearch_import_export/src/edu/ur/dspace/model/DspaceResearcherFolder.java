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
 * Represents the folders for a researcher
 * 
 * @author Nathan Sarr
 *
 */
public class DspaceResearcherFolder {
	
	/** folder id in the dspace system */
	public Long folderId;
	
	/** id of the researcher */
	public Long researcherId;
	
	/** parent folder id */
	public Long parentId;
	
	/** left value of the folder */
	public Long leftValue;
	
	/** right value of the folder */
	public Long rightValue;
	
	/** Description of the researcher */
	public String description;
	
	/** title of the folder */
	public String title;
	
	/** parent researcher folder */
	public DspaceResearcherFolder parent;
	
	/** children folders for this folder */
	public LinkedList<DspaceResearcherFolder> children = new LinkedList<DspaceResearcherFolder>();
	
	/** Set of links in the folder */
	public List<ResearcherFolderLink> links = new LinkedList<ResearcherFolderLink>();
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer ("[ folder id = ");
		sb.append(folderId);
		sb.append(" title = ");
		sb.append(title);
		sb.append(" description = ");
		sb.append(description);
		sb.append(" leftValue = ");
		sb.append(leftValue);
		sb.append(" rightValue = ");
		sb.append(rightValue);
		sb.append(" parent id = ");
		sb.append(parentId);
		sb.append("]");
		return sb.toString();
	}

}
