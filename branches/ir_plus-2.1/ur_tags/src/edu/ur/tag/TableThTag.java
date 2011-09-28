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


import edu.ur.tag.html.HtmlTh;



public class TableThTag extends CommonSimpleTag implements HtmlTh{
	
	private String align;
	private String myChar;
	private String charOff;
	private String vAlign;
	
	public void doTag() throws JspException {
		   JspFragment body = getJspBody();
	       PageContext pageContext = (PageContext) getJspContext();
	       JspWriter out = pageContext.getOut();
	       
	       try {
	    	   out.print("<th ");
	    	   out.print(getAttributes());
	    	   out.print(">\n");
	    	   
	    	   if( body!= null )
	    	   {
	    	       body.invoke(null);
	    	   }
	    	   
	    	   out.print("</th>");
	    	   
	       } catch (Exception e) {
	          throw new JspException(e);
	       }
	       
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getCharOff() {
		return charOff;
	}

	public void setCharOff(String charOff) {
		this.charOff = charOff;
	}

	public String getMyChar() {
		return myChar;
	}

	public void setMyChar(String myChar) {
		this.myChar = myChar;
	}

	public String getVAlign() {
		return vAlign;
	}

	public void setVAlign(String align) {
		vAlign = align;
	}
	
	public StringBuffer getAttributes() {
		
		StringBuffer sb = new StringBuffer();
    	if(!TagUtil.isEmpty(align)) { sb.append("align=\"" + align + "\" "); }
    	if(!TagUtil.isEmpty(myChar)) { sb.append("char=\"" + myChar + "\" "); }
    	if(!TagUtil.isEmpty(charOff)) { sb.append("charoff=\"" + charOff + "\" "); }  
    	if(!TagUtil.isEmpty(vAlign)) { sb.append("valign=\"" + vAlign + "\" "); }  
    	
    	sb.append(getKeyboardEvents());
    	sb.append(getKeyboardAttributes());
    	sb.append(getCoreAttributes());
    	sb.append(getLanguageAttributes());
    	sb.append(getMouseEvents());
		return sb;
	}

}
