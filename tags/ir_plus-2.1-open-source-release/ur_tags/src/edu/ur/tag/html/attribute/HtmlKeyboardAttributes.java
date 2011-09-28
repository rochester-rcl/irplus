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
 * Represents html keyboard attributes.
 * 
 * access key - Sets a keyboard shortcut to access an element
 * 
 * tab index - Sets the tab order of an element
 * 
 * @author Nathan Sarr
 */
public interface HtmlKeyboardAttributes {
	
	/**
	 * The tab order of an element
	 * 
	 * @param tabIndex
	 */
	public void setTabIndex(String tabIndex);
	
	/**
	 * The tab order of an element
	 * 
	 * @return
	 */
	public String getTabIndex();
	
	/**
	 * Sets a keyboard shortcut to access an element
	 * 
	 * @param accessKey
	 */
	public void setAccessKey(String accessKey);
	
	/**
	 * Sets a keyboard shortcut to access an element
	 * 
	 * @return
	 */
	public String getAccessKey();
	
	/**
	 * For all non empty attributes, returns
	 * the attributes in the form attribute="value"
	 * 
	 * @return
	 */
	public StringBuffer getAttributes();
	

}
