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


package edu.ur.tag.base;

import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.ur.tag.TagUtil;
import edu.ur.tag.html.attribute.HtmlCoreAttributes;

/**
 * Base HTML Class that almost all classes will extend.
 * 
 * These values cannot be used 
 * in base, head, html, meta, param, script, style, and title elements.
 * 
 * @author Nathan Sarr
 *
 */
public class BaseHtmlCoreAttributes extends SimpleTagSupport implements HtmlCoreAttributes {

	/** id of the element */
	protected String id;
	
	/** cascading style sheet class  */
	protected String cssClass; 

	/** title for the element*/
	protected String title;
	
	/** style of the element  */
	protected String style;
	
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#getCssClass()
	 */
	public String getCssClass() {
		return cssClass;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#setCssClass(java.lang.String)
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#getId()
	 */
	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#getStyle()
	 */
	public String getStyle() {
		return style;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#setStyle(java.lang.String)
	 */
	public void setStyle(String style) {
		this.style = style;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#getTitle()
	 */
	public String getTitle() {
		return title;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/* (non-Javadoc)
	 * @see edu.ur.tag.html.attribute.HtmlCoreAttributes#getCoreAttributes()
	 */
	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();
    	if(!TagUtil.isEmpty(id)){ sb.append("id=\"" + id + "\" ");}  
    	if(!TagUtil.isEmpty(cssClass)){sb.append("class=\"" + cssClass + "\" ");}  
    	if(!TagUtil.isEmpty(title)){sb.append("title=\"" + title + "\" "); }  
    	if(!TagUtil.isEmpty(style)){ sb.append("style=\"" + style + "\" "); }  
		return sb;
	}
}
