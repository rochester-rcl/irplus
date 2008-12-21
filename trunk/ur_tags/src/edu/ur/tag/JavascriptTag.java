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
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class JavascriptTag extends SimpleTagSupport{
	
	private String type = "text/javascript";
	
	private String charset;
	
	private String defer;
	
	private String src;
	
	private String xmlSpace;
	
	private PageContext context;
	
	/**
	 * Create the form tag.
	 * 
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	public void doTag() throws JspException {
	      
		
        context = (PageContext) getJspContext();
	    JspWriter out = context.getOut();
	    
	       
	    try {
	        out.print("<script ");
	    	out.print(getJavascriptAttributes());
	    	out.print(">");
	    	
	    	JspFragment body = getJspBody();
	    	if( body != null)
	    	{
	    		body.invoke(null);
	    	}
            out.print("</script>");
	    	
	    	
	       } catch (Exception e) {
	           throw new JspException(e);
	       }
	}

	
	

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getDefer() {
		return defer;
	}

	public void setDefer(String defer) {
		this.defer = defer;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getXmlSpace() {
		return xmlSpace;
	}

	public void setXmlSpace(String xmlSpace) {
		this.xmlSpace = xmlSpace;
	}
	
	public StringBuffer getJavascriptAttributes() {

		StringBuffer sb = new StringBuffer();
    	if(!TagUtil.isEmpty(type)) { sb.append("type=\"" + type + "\" "); }
    	if(!TagUtil.isEmpty(charset)) { sb.append("charset=\"" + charset + "\" "); }
    	if(!TagUtil.isEmpty(defer)) { sb.append("defer=\"" + defer + "\" "); }
    	
    	if(!TagUtil.isEmpty(src)) 
    	{ 
    		try {
			    sb.append("src=\"" + TagUtil.fixRelativePath(src,context) + "\" ");
		    } catch (ELException e) {
		    	throw new RuntimeException(e);
		    } 
		}

    	
    	if(!TagUtil.isEmpty(xmlSpace)) { sb.append("xml:space=\"" + xmlSpace + "\" "); }  
		return sb;
	} 

}
