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
import javax.servlet.jsp.tagext.JspFragment;


import edu.ur.tag.html.HtmlForm;


/**
 * Creates a new form and places the form name as a hidden input value called
 * form name.
 * 
 * @author Nathan Sarr
 *
 */
public class BasicFormTag extends CommonSimpleTag implements HtmlForm {
	
	private String method;
	private String action;
	private String accept;
	private String acceptCharset;
	private String enctype;
	private String name;
	private String target;
	private String onReset;
	private String onSubmit;

	
	/**
	 * Create the form tag.
	 * 
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	public void doTag() throws JspException {
	      
		JspFragment body = getJspBody();
        PageContext pageContext = (PageContext) getJspContext();
	    JspWriter out = pageContext.getOut();
	    
	       
	    try {
	        out.print("\n<form ");
	    	out.print(getAttributes());
	    	out.print(">");
	    	if( body!= null )
	    	{
	    		out.print("<input type=\"hidden\" name=\"formName\" value=\"" + getName() + "\"/>");
	            body.invoke(null);
	    	}
	    	out.print("</form>");
	    	
	       } catch (Exception e) {
	           throw new JspException(e);
	       }
	}
	

	/**
	 * Set the action.  If the action is relative, it creates the full
	 * path.
	 * 
	 * @see edu.ur.tag.html.HtmlForm#setAction(java.lang.String)
	 */
	public void setAction(String action) {
       this.action = action;
	}
	
	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getAcceptCharset() {
		return acceptCharset;
	}

	public void setAcceptCharset(String acceptCharset) {
		this.acceptCharset = acceptCharset;
	}

	public String getAction() {
		return action;
	}

	public String getEnctype() {
		return enctype;
	}

	public void setEnctype(String enctype) {
		this.enctype = enctype;
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	

	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();
		if(TagUtil.isEmpty(method))
    	{
    	    sb.append("method=\"post\" ");
    	}
    	else
    	{
    		sb.append("method=\"" + method + "\" ");
    	}
		
		if( !TagUtil.isEmpty(action) )
		{
			PageContext pc = (PageContext)getJspContext();
			
			try {
				action = TagUtil.fixRelativePath(action, pc);
				sb.append("action=\"" + action + "\" ");
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Exception thrown", e);
			}
		}
    	
    	if(!TagUtil.isEmpty(accept)) { sb.append("accept=\"" + accept + "\" "); }
    	if(!TagUtil.isEmpty(acceptCharset)) { sb.append("accept-charset=\"" + acceptCharset + "\" "); }  
    	if(!TagUtil.isEmpty(enctype)) { sb.append("enctype=\"" + enctype + "\" "); }  
    	if(!TagUtil.isEmpty(name)) { sb.append("name=\"" + name + "\" "); }  
    	if(!TagUtil.isEmpty(target)) { sb.append("target=\"" + target + "\" "); }  
    	if(!TagUtil.isEmpty(onReset)) { sb.append("onreset=\"" + onReset + "\" "); }  
    	if(!TagUtil.isEmpty(onSubmit)) { sb.append("onsubmit=\"" + onSubmit + "\" "); }  
    	
    	sb.append(getAllSimpleTagAttributes());
		return sb;
	}


	public String getOnReset() {
		return onReset;
	}


	public void setOnReset(String onReset) {
		this.onReset = onReset;
	}


	public String getOnSubmit() {
		return onSubmit;
	}


	public void setOnSubmit(String onSubmit) {
		this.onSubmit = onSubmit;
	} 


}
