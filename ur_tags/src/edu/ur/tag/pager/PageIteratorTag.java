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


package edu.ur.tag.pager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This tag displays the page iterator
 * 
 * @author Sharmila Ranganathan
 *
 */
public class PageIteratorTag extends SimpleTagSupport {
	
	/** Iterator variable name */
	private String var; 
	
	/** Logger */
	private static final Logger log = LogManager.getLogger(PageIteratorTag.class);

	public void doTag() throws JspException {
		log.debug("do tag called");

		PagerTag pagerTag =
			 (PagerTag)findAncestorWithClass(this,
					 PagerTag.class);

	    if(pagerTag == null)
	    {
	    	throw new JspTagException("the <ur:forEachPage> tag must"
	    			+ " be nested within a <ur:pager> tag");
	    }

		JspFragment body = getJspBody();

		try {
			for(int i = pagerTag.getStartPageNumber(); i <= pagerTag.getEndPageNumber(); i++) {
				getJspContext().setAttribute(var, i);
				if( body != null )
				{
					int rowStart = (i * pagerTag.getNumberOfResultsToShow())  - pagerTag.getNumberOfResultsToShow();
				    getJspContext().setAttribute("rowStart", rowStart);
					body.invoke(null);
				}
			}
			
		} catch (Exception e) {
			throw new JspException(e);
		}

	}


	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
	
	
}
