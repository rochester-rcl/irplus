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


package edu.ur.tag.html.attribute;

import edu.ur.tag.TagUtil;

/**
 * Implemntation of the keyboard attributes.
 * 
 * @author Nathan Sarr
 *
 */
public class HtmlKeyboardAttributesImpl implements HtmlKeyboardAttributes {
	
	private String tabIndex;
	
	private String accessKey;

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();
	    if(!TagUtil.isEmpty(tabIndex)){ sb.append("tabindex=\"" + tabIndex + "\" ");}  
	    if(!TagUtil.isEmpty(accessKey)){sb.append("accesskey=\"" + accessKey + "\" ");}  
		return sb;

	}
	

}
