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
import javax.servlet.jsp.el.ELException;

import edu.ur.tag.TagUtil;
import edu.ur.tag.base.CommonBaseHtmlTag;

public class ImageTag extends CommonBaseHtmlTag{
	
	/** url of the image to display */
	protected String src;
	
	/** description of the image  */
	protected String alt;
	
	/**  align the image according to surrounding text */
	protected String align;
	
	/** border around an image  */
	protected String border;
	
	/** defines the height of an image  */
	protected String height;
	
	/** white space on left and right side of the image  */
	protected String hSpace;
	
	/** server-side image map */
	protected String ismap;

	/** a URL to a document that contains a long description  */
	protected String longDesc;
	
	/** image as a client side image map  */
	protected String useMap;
	
	/** white space on the top and bottom of the image  */
	protected String vSpace;
	
	/**   sets the width of an image*/
	protected String width;
	
	@SuppressWarnings("unchecked")
	public void doTag() throws JspException {
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter o = pageContext.getOut();

		try {
			
			o.print("<img ");
			o.print(getAttributes());
			o.print("/>");

		} catch (Exception e) {
			throw new JspException(e);
		}
	}

	
	
	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();
		PageContext pageContext = (PageContext) getJspContext();
		if(!TagUtil.isEmpty(src)) 
    	{ 
    		try {
			    sb.append("src=\"" + TagUtil.fixRelativePath(src,pageContext) + "\" ");
		    } catch (ELException e) {
		    	throw new RuntimeException(e);
		    } 
		}
		if (!TagUtil.isEmpty(alt)) { sb.append("alt=\"" + alt + "\" "); }
		if (!TagUtil.isEmpty(align)) { sb.append("align=\"" + align + "\" "); }
		if (!TagUtil.isEmpty(border)) { sb.append("border=\"" + border + "\" "); }
		if (!TagUtil.isEmpty(height)) { sb.append("height=\"" + height + "\" "); }
		if (!TagUtil.isEmpty(hSpace)) { sb.append("hspace=\"" + hSpace + "\" "); }
		if (!TagUtil.isEmpty(ismap)) { sb.append("ismap=\"" + ismap + "\" "); }
		if (!TagUtil.isEmpty(longDesc)) { sb.append("longdesc=\"" + longDesc + "\" "); }
		if (!TagUtil.isEmpty(useMap)) { sb.append("usemap=\"" + useMap + "\" "); }
		if (!TagUtil.isEmpty(vSpace)) { sb.append("vspace=\"" + vSpace + "\" "); }
		if (!TagUtil.isEmpty(width)) { sb.append("width=\"" + width + "\" "); }
		

		sb.append(super.getAttributes());
		return sb;
	}
	

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getBorder() {
		return border;
	}

	public void setBorder(String border) {
		this.border = border;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getHSpace() {
		return hSpace;
	}

	public void setHSpace(String space) {
		hSpace = space;
	}

	public String getIsmap() {
		return ismap;
	}

	public void setIsmap(String ismap) {
		this.ismap = ismap;
	}

	public String getLongDesc() {
		return longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public String getUseMap() {
		return useMap;
	}

	public void setUseMap(String useMap) {
		this.useMap = useMap;
	}

	public String getVSpace() {
		return vSpace;
	}

	public void setVSpace(String space) {
		vSpace = space;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

}
