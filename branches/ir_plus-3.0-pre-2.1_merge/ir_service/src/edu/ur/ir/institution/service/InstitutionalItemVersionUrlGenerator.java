/**  
   Copyright 2008 - 2010 University of Rochester

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


package edu.ur.ir.institution.service;


import java.io.Serializable;

import edu.ur.ir.institution.InstitutionalItem;

/**
 * This class will generate a web url for a versioned institutional item
 * that looks something like the following:
 * 
 * https://urresearch3.lib.rochester.edu/institutionalPublicationPublicView.action?institutionalItemId=5002&versionNumber=1
 * @author Nathan Sarr
 *
 */
public class InstitutionalItemVersionUrlGenerator implements Serializable
{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 6042212230149968886L;

	/** should already contain the trailing slash(/) */
	private String baseWebPath;

	/** action to view the institutional publication */
	private String action = "institutionalPublicationPublicView.action?";
	
	/** item id identifier  */
	private String itemIdIdentifier = "institutionalItemId=";
	
	/** version number identifier  */
	private String versionNumberIdentifier = "versionNumber=";
	
	/**
	 * Create the url for the institutional item version.
	 * 
	 * @param version
	 * @return
	 */
	public String createUrl(InstitutionalItem institutionalItem,  int version)
	{
		String url = baseWebPath;
		url = baseWebPath + action + "?" + itemIdIdentifier + "=" + institutionalItem.getId() +"&" + versionNumberIdentifier + "=" + version;
		
		return url;
	}
	
	public String getBaseWebPath() {
		return baseWebPath;
	}


	public void setBaseWebPath(String baseWebPath) {
		this.baseWebPath = baseWebPath;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getItemIdIdentifier() {
		return itemIdIdentifier;
	}

	public void setItemIdIdentifier(String itemIdIdentifier) {
		this.itemIdIdentifier = itemIdIdentifier;
	}

	public String getVersionNumberIdentifier() {
		return versionNumberIdentifier;
	}

	public void setVersionNumberIdentifier(String versionNumberIdentifier) {
		this.versionNumberIdentifier = versionNumberIdentifier;
	}


}
