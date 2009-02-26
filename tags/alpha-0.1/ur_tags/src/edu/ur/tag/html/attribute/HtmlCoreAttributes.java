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

/**
 *Not valid in base, head, html, meta, param, script, style, and title elements.
 */
public interface HtmlCoreAttributes {
	
	/**
	 * 	The class of the element
	 * 
	 * @return
	 */
	public String getCssClass();
	
	/**
	 * The class of the element
	 * 
	 * @param cssClass
	 */
	public void setCssClass(String cssClass);
	
	/**
	 * A unique id for the element
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * A unique id for the element
	 * 
	 * @param id
	 */
	public void setId(String id);

	/**
	 * 	An inline style definition
	 * 
	 * @return
	 */
	public String getStyle();

	/**
	 * 	An inline style definition
	 * 
	 * @param style
	 */
	public void setStyle(String style);
	
	/**
	 * A text to display in a tool tip
	 * 
	 * @return
	 */
	public String getTitle();
	
	/**
	 * A text to display in a tool tip
	 * 
	 * @param title
	 */
	public void setTitle(String title);
	
	/**
	 * For all non empty attributes, returns
	 * the attributes in the form attribute="value"
	 * 
	 * @return
	 */
	public StringBuffer getAttributes();
	

}
