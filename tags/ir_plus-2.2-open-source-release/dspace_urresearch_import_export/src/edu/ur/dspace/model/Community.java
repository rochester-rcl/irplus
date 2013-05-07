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
 * Simple community record.
 * 
 * @author Nathan Sarr
 *
 */
public class Community {
	
	/** dspace community id 	 */
	public Long id;
	
	/** name of the community */
	public String name;
	
	/** introductory text */
	public String introductoryText;
	
	/** side bar text */
	public String sideBarText;
	
	/** copyright */
	public String copyright;
	
	/** short description */
	public String shortDescription;
	
	/** logo id  */
	public Long logoId;
	
	/** logo file name */
	public String logoFileName;
	
	/** log file information   */
	public BitstreamFileInfo logoFileInfo;
	
	/** links for the community */
	public List<Link> links = new LinkedList<Link>();
	
	/** Group permissions for this community */
	public List<GroupPermission> groupPermissions = new LinkedList<GroupPermission>();
	
	/** Eperson permissions for this community */
	public List<EpersonPermission> epersonPermissions = new LinkedList<EpersonPermission>();
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" name = ");
		sb.append(name);
		sb.append(" introductoryText = ");
		sb.append(introductoryText);
		sb.append(" short description ");
		sb.append(shortDescription);
		sb.append(" sideBarText = ");
		sb.append(sideBarText);
		sb.append(" copyright = ");
		sb.append(copyright);
		sb.append(" logo id = ");
		sb.append(logoId);
		sb.append(" logo file name = ");
		sb.append(logoFileName);
		sb.append("]");
		return sb.toString();
	}

}
