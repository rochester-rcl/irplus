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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

public class BasicButtonInputTag extends BaseInputTag{
	
	private String disabled;
	
	private String type = "button";
	
	private String value;
	
	/**
	 * Create the checkbox tag.
	 * 
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	public void doTag() throws JspException {
	     
	    BasicFormTag formTag = (BasicFormTag) findAncestorWithClass(this,
				BasicFormTag.class);
	    
	    if (formTag == null) {
			throw new JspTagException("the <ur:button> tag must"
					+ " be nested within a <ur:form> tag");
		}
	    
        PageContext pageContext = (PageContext) getJspContext();
	    JspWriter out = pageContext.getOut();
	    
	    try {
	        out.print("\n<input ");
	    	out.print(getAttributes());
	    	out.print("/>");
	  } catch (Exception e) {
	           throw new JspException(e);
	  }
	}


	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String vlaue) {
		this.value = vlaue;
	}

	public String getType() {
		return type;
	}
	
	public StringBuffer getAttributes()
	{
		StringBuffer sb = new StringBuffer();
	
    	if(!TagUtil.isEmpty(disabled)) { sb.append("disabled=\"" + disabled + "\" "); }
    	if(!TagUtil.isEmpty(value)) { sb.append("value=\"" + value + "\" "); }  
    	sb.append(getInputAttributes());
		return sb;
	}

	public void setType(String type) {
		this.type = type;
	}


}
