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

public interface HtmlTable extends HtmlCoreAttributes, HtmlKeyboardEvents, HtmlMouseEvents, 
   HtmlLanguageAttributes {
	
	
	public String getAlign();
	
	public void setAlign(String align);
	
	public String getBgColor();
	
	public void setBgColor(String bgColor);
	
	public String getBorder();
	
	public void setBorder(String border);
	
	public String getCellPadding();
	
	public void setCellPadding(String cellPadding);
	
	public String getCellSpacing();
	
	public void setCellSpacing(String cellSpacing);
	
	public String getFrame();
	
	public void setFrame(String frame);
	
	public String getRules();
	
	public void setRules(String rules);
	
	public String getSummary();
	
	public void setSummary(String summary);
	
	public String getWidth();
	
	public void setWidth(String width);
		
	public StringBuffer getAttributes();
	
	

}
