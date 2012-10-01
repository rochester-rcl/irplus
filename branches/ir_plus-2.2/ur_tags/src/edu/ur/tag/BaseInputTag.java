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


package edu.ur.tag;

import edu.ur.tag.html.HtmlBaseInput;
import edu.ur.tag.html.attribute.HtmlCoreAttributesImpl;

public abstract class BaseInputTag extends CommonSimpleTag implements HtmlBaseInput
{
	protected String name;
	protected String tabIndex;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract String getType();

	public void setCoreAttributes(HtmlCoreAttributesImpl coreAttributes) {
		this.coreAttributes = coreAttributes;
	}

	public StringBuffer getInputAttributes() {
		StringBuffer sb = new StringBuffer();
		
    	if(!TagUtil.isEmpty(name)) { sb.append("name=\"" + name + "\" "); }  
    	if(!TagUtil.isEmpty(getType())) { sb.append("type=\"" + getType() + "\" "); }  
    	if(!TagUtil.isEmpty(tabIndex)) { sb.append("tabIndex=\"" + tabIndex + "\" "); }  
    	
    	sb.append(getKeyboardEvents());
    	sb.append(getCoreAttributes());
    	sb.append(getMouseEvents());
    	sb.append(getLanguageAttributes());
		return sb;
	}

}
