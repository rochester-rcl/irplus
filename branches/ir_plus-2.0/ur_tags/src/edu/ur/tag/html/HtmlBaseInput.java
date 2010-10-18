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


package edu.ur.tag.html;

import edu.ur.tag.html.attribute.HtmlCoreAttributes;
import edu.ur.tag.html.attribute.HtmlLanguageAttributes;
import edu.ur.tag.html.event.HtmlKeyboardEvents;
import edu.ur.tag.html.event.HtmlMouseEvents;

/**
 * Input fields valid on all input tags.
 * 
 * @author Nathan Sarr
 *
 */
public interface HtmlBaseInput extends HtmlCoreAttributes, HtmlKeyboardEvents, 
HtmlMouseEvents, HtmlLanguageAttributes {
	
	public String getName();

	public void setName(String name);

	public String getType();

	public void setType(String type);
	
	public StringBuffer getInputAttributes();
	
	public String getTabIndex();
	
	public void setTabIndex(String index);

}
