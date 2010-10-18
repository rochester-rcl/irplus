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
 * Implements the htmlCore attributes.
 * 
 * @author Nathan Sarr
 *
 */
public class HtmlCoreAttributesImpl implements HtmlCoreAttributes{
	
	private String id;
	private String cssClass; 
	private String title;
	private String style;
	
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();
    	if(!TagUtil.isEmpty(id)){ sb.append("id=\"" + id + "\" ");}  
    	if(!TagUtil.isEmpty(cssClass)){sb.append("class=\"" + cssClass + "\" ");}  
    	if(!TagUtil.isEmpty(title)){sb.append("title=\"" + title + "\" "); }  
    	if(!TagUtil.isEmpty(style)){ sb.append("style=\"" + style + "\" "); }  
		return sb;
	}
}
