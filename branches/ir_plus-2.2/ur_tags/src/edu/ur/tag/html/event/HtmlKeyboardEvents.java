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


package edu.ur.tag.html.event;

/**
 * Not valid in base, bdo, br, frame, frameset, head, html, iframe, 
 * meta, param, script, style, and title elements.
 * 
 * @author Nathan Sarr
 *
 */
public interface HtmlKeyboardEvents {
	
	/**
	 * What to do when key is pressed
	 * 
	 * @return
	 */
	public String getOnKeyDown();
	
	/**
	 * What to do when key is pressed
	 * 
	 * @param onKeyDown
	 */
	public void setOnKeyDown(String onKeyDown);
	
	/**
	 * What to do when key is pressed and released
	 * 
	 * @return
	 */
	public String getOnKeyPress();
	
	/**
	 * What to do when key is pressed and released
	 * 
	 * @param onKeyPress
	 */
	public void setOnKeyPress(String onKeyPress);
	
	/**
	 * What to do when key is released
	 * 
	 * @return
	 */
	public String getOnKeyUp();
	
	/**
	 * What to do when key is released
	 */
	public void setOnKeyUp(String onKeyUp);
	
	/**
	 * For all non empty attributes, returns
	 * the attributes in the form event="value"
	 * 
	 * @return
	 */
	public StringBuffer getAttributes();


}
