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
import javax.servlet.jsp.tagext.JspFragment;

import edu.ur.tag.html.HtmlTextArea;

/**
 * Text area tag.
 * 
 * @author Nathan Sarr
 *
 */
public class BasicTextAreaTag extends CommonSimpleTag implements HtmlTextArea {
	
	private String disabled;
	
	private String maxLength;
	
    private String readOnly;
    
    private String name;
    
    private String cols;
    
    private String rows;
    
	/**
	 * Create the text input tag.
	 * 
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	public void doTag() throws JspException {
	     
	    BasicFormTag formTag = (BasicFormTag) findAncestorWithClass(this,
				BasicFormTag.class);
	    
	    if (formTag == null) {
			throw new JspTagException("the <ur:textArea> tag must"
					+ " be nested within a <ur:form> tag");
		}
	    
	    JspFragment body = getJspBody();
        PageContext pageContext = (PageContext) getJspContext();
	    JspWriter out = pageContext.getOut();
	    
	    try {
	        out.print("<textarea ");
	    	out.print(getAttributes());
	    	out.print(">");
	    	if( body!= null )
	    	{
	            body.invoke(null);
	    	}
	    	out.print("</textarea>");
	    	
	  } catch (Exception e) {
	           throw new JspException(e);
	  }
	}


	
	public StringBuffer getAttributes()
	{
		StringBuffer sb = new StringBuffer();
    	if(!TagUtil.isEmpty(disabled)) { sb.append("disabled=\"" + disabled + "\" "); }  
    	if(!TagUtil.isEmpty(maxLength)) { sb.append("maxlength=\"" + maxLength + "\" "); }  
    	if(!TagUtil.isEmpty(readOnly)) { sb.append("readonly=\"" + readOnly + "\" "); }  
    	if(!TagUtil.isEmpty(name)) { sb.append("name=\"" + name + "\" "); }
    	if(!TagUtil.isEmpty(cols)) { sb.append("cols=\"" + cols + "\" "); }
    	if(!TagUtil.isEmpty(rows)) { sb.append("rows=\"" + rows + "\" "); }
    	sb.append(this.getAllSimpleTagAttributes());
		return sb;
	}



	public String getDisabled() {
		return disabled;
	}



	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}



	public String getMaxLength() {
		return maxLength;
	}



	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}



	public String getReadOnly() {
		return readOnly;
	}



	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getCols() {
		return cols;
	}



	public void setCols(String cols) {
		this.cols = cols;
	}



	public String getRows() {
		return rows;
	}



	public void setRows(String rows) {
		this.rows = rows;
	}

}
