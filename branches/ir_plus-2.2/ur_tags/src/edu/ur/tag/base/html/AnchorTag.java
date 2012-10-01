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


package edu.ur.tag.base.html;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

import edu.ur.tag.TagUtil;
import edu.ur.tag.base.CommonBaseHtmlTag;

/**
 * Anchor Tag
 * 
 * @author Nathan Sarr
 *
 */
public class AnchorTag extends CommonBaseHtmlTag{
	
	/** character encoding */
	private String charset;
	
	/**  region of an image - for image maps */
	private String coords;
	
	/** target of the link  */
	private String href;
	
	/** base language  */
	private String hreflang;
	
	/** name of the anchor  */
	private String name;
	
	/** relationship between document and target  */
	private String rel;
	
	/** relationship between target and current doc   */
	private String rev;
	
	/**  mapping in current area  */
	private String shape;
	
	/** where to open the target URL  */
	private String target;
	
	/** mime type  */
	private String type;
	
	public void doTag() throws JspException {
		JspFragment body = getJspBody();
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter o = pageContext.getOut();

		try {
			
			o.print("<a ");
			o.print(getAttributes());
			o.print(">");
			if( body != null )
			{
			    body.invoke(null);
			}
			o.print("</a>");

		} catch (Exception e) {
			throw new JspException(e);
		}	       
	}
	
	
	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();
		PageContext pageContext = (PageContext) getJspContext();
    	if(!TagUtil.isEmpty(href)) 
    	{ 
    		try {
			    sb.append("href=\"" + TagUtil.fixRelativePath(href,pageContext) + "\" ");
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
    	
    	sb.append(super.getAttributes());
		return sb;

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

}
