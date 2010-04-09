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


import edu.ur.tag.html.HtmlAnchor;

public class AnchorTag extends CommonSimpleTag implements HtmlAnchor{
	
	private String charset;
	private String coords;
	private String href;
	private String hreflang;
	private String name;
	private String rel;
	private String rev;
	private String shape;
	private String target;
	private String type;
	
	private PageContext context;
	
	public void doTag() throws JspException {
	    context = (PageContext) getJspContext();
	       
	    try {
	    	JspWriter out = context.getOut();
	    	out.print("<a " + getAttributes() + ">");
	    	
	    	if(getJspBody() != null)
	    	{
			    getJspBody().invoke(null);
	    	}
	    	
	    	out.print("</a>");
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
	public String getCoords() {
		return coords;
	}
	public void setCoords(String coords) {
		this.coords = coords;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getHreflang() {
		return hreflang;
	}
	public void setHreflang(String hreflang) {
		this.hreflang = hreflang;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public String getRev() {
		return rev;
	}
	public void setRev(String rev) {
		this.rev = rev;
	}
	public String getShape() {
		return shape;
	}
	public void setShape(String shape) {
		this.shape = shape;
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
    	if(!TagUtil.isEmpty(coords)) { sb.append("coords=\"" + coords + "\" "); }  
    	if(!TagUtil.isEmpty(hreflang)) { sb.append("hreflang=\"" + hreflang + "\" "); }  
    	if(!TagUtil.isEmpty(name)) { sb.append("name=\"" + name + "\" "); }  
    	if(!TagUtil.isEmpty(rel)) { sb.append("rel=\"" + rel + "\" "); }  
    	if(!TagUtil.isEmpty(shape)) { sb.append("shape=\"" + shape + "\" "); }  

    	if(!TagUtil.isEmpty(target)) { sb.append("target=\"" + target + "\" "); } 
    	if(!TagUtil.isEmpty(type)) { sb.append("type=\"" + type + "\" "); }
    	
    	sb.append(getAllSimpleTagAttributes());
		return sb;

	}

}
