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

import edu.ur.tag.html.HtmlStyleSheet;

public class StyleSheetTag extends KeyMouseCommonTag implements HtmlStyleSheet 
{
	
	private String charset;
	private String href;
	private String media = "all";
	private String target;
	private String type = "text/css";
	
	private String rel = "stylesheet";
	private String rev = "stylesheet";
	
	private PageContext context;

	
	public void doTag() throws JspException {
	    context = (PageContext) getJspContext();
	       
	    try {
	    	JspWriter out = context.getOut();
	    	out.print("<link " + getAttributes() + "/>");
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
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();

    	if(!TagUtil.isEmpty(href)) 
    	{ 
    		try {
			    sb.append("href=\"" + TagUtil.fixRelativePath(href,context) + "\" ");
		    } catch (Exception e) {
		    	throw new RuntimeException(e);
		    } 
		}
    	
		if(!TagUtil.isEmpty(charset)) { sb.append("charset=\"" + charset + "\" "); }  
    	if(!TagUtil.isEmpty(media)) { sb.append("media=\"" + media + "\" "); }  
    	if(!TagUtil.isEmpty(rev)) { sb.append("rev=\"" + rev + "\" "); } 
    	if(!TagUtil.isEmpty(rel)) { sb.append("rel=\"" + rel + "\" "); } 
    	if(!TagUtil.isEmpty(target)) { sb.append("target=\"" + target + "\" "); } 
    	if(!TagUtil.isEmpty(type)) { sb.append("type=\"" + type + "\" "); }
    	
    	sb.append(super.getAttriubes());
		return sb;

	}



}
