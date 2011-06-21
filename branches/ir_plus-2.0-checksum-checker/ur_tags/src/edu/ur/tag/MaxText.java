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
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag that shows only a certain amount of text.
 * 
 * @author Nathan Sarr
 *
 */
public class MaxText extends SimpleTagSupport{
	
	/** text to trim  */
	private String text;
	
	/** maximum number of characters  */
	private int numChars = 0;
	
	/** if true shows the ellipsis text  */
	private boolean showEllipsis = true;
	
	/** the ellipsis text to use */
	private String ellipsisText = "...";
	
	/**   Page context  */
	private PageContext context;
	
	public void doTag() throws JspException {
	    context = (PageContext) getJspContext();
	       
	    try {
	    	if( text != null )
	    	{
	    	    JspWriter out = context.getOut();
	    	    if( text.length() > numChars )
	    	    {
	      	        out.write(text.substring(0, numChars));
	      	        if( showEllipsis)
	      	        {
	      	        	out.write(ellipsisText);
	      	        }
	    	    }
	    	    else
	    	    {
	    	    	out.write(text);
	    	    }
	    	}
	    	
	   } catch (Exception e) {
	       throw new JspException(e);
	   }
	       
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getNumChars() {
		return numChars;
	}

	public void setNumChars(int numChars) {
		this.numChars = numChars;
	}

	public boolean isShowEllipsis() {
		return showEllipsis;
	}

	public void setShowEllipsis(boolean showEllipsis) {
		this.showEllipsis = showEllipsis;
	}

	public String getEllipsisText() {
		return ellipsisText;
	}

	public void setEllipsisText(String ellipsisText) {
		this.ellipsisText = ellipsisText;
	}


}
