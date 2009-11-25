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
 * Not valid in base, bdo, br, frame, frameset, head, html, iframe, meta, 
 * param, script, style, and title elements.
 * 
 * @author Nathan Sarr
 *
 */
public interface HtmlMouseEvents {
	
	/**
	 * What to do on a mouse click
	 * 
	 * @return
	 */
	public String getOnClick();
	
	/**
	 * What to do on a mouse click
	 * 
	 * @param onClick
	 */
	public void setOnClick(String onClick);
	
	/**
	 * What to do on a mouse doubleclick
	 * 
	 * @return
	 */
	public String getOnDblClick();
	
	/**
	 * What to do on a mouse doubleclick
	 * 
	 * @param onDblClick
	 */
	public void setOnDblClick(String onDblClick);
	
	/**
	 * What to do when mouse button is pressed
	 * 
	 * @return
	 */
	public String getOnMouseDown();
	
	/**
	 * What to do when mouse button is pressed
	 * 
	 * @param onMouseDown
	 */
	public void setOnMouseDown(String onMouseDown);
	
	/**
	 * 	What to do when mouse pointer moves
	 * 
	 * @return
	 */
	public String getOnMouseMove();
	
	/**
	 * What to do when mouse pointer moves
	 * 
	 * @param onMouseMove
	 */
	public void setOnMouseMove(String onMouseMove);
	
	/**
	 * 	What to do when mouse pointer moves over an element
	 * 
	 * @return
	 */
	public String getOnMouseOver();
	
	/**
	 * 	What to do when mouse pointer moves over an element
	 * 
	 * @param onMouseOver
	 */
	public void setOnMouseOver(String onMouseOver);
	
	/**
	 * What to do when mouse pointer moves out of an element
	 * 
	 * @return
	 */
	public String getOnMouseOut();
	
	/**
	 * What to do when mouse pointer moves out of an element
	 * 
	 * @param onMouseOut
	 */
	public void setOnMouseOut(String onMouseOut);
	
	/**
	 * What to do when mouse button is released
	 * 
	 * @return
	 */
	public String getOnMouseUp();
	
	/**
	 * What to do when mouse button is released
	 * 
	 * @param onMouseUp
	 */
	public void setOnMouseUp(String onMouseUp);
	
	/**
	 * For all non empty attributes, returns
	 * the attributes in the form event="value"
	 * 
	 * @return
	 */
	public StringBuffer getAttributes();

}
