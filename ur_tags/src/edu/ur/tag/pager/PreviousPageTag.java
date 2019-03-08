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
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Displays the previous page 
 * 
 * @author Sharmila Ranganathan
 *
 */
public class PreviousPageTag extends SimpleTagSupport {
	
	/** Logger */
	private static final Logger log = LogManager.getLogger(PreviousPageTag.class);
	
	public void doTag() throws JspException {
		log.debug("do tag called");
		PagerTag pagerTag =
			 (PagerTag)findAncestorWithClass(this,
					 PagerTag.class);

	    if(pagerTag == null)
	    {
	    	throw new JspTagException("the <ur:previousPage> tag must"
	    			+ " be nested within a <ur:pager> tag");
	    }
	    
	    
		JspFragment body = getJspBody();
		PageContext pageContext = (PageContext) getJspContext();

		try {
			if (pagerTag.getCurrentPageNumber() != 1 && pagerTag.getTotalPageNumber() > 1) {
				
				if( body != null )
				{
					int rowStart = ((pagerTag.getCurrentPageNumber() - 1) * pagerTag.getNumberOfResultsToShow())  - pagerTag.getNumberOfResultsToShow();
					pageContext.setAttribute("rowStart", rowStart);
					
					int startPageNumber = 1;
					if( pagerTag.getCurrentPageNumber()  == pagerTag.getStartPageNumber() )
					{
						startPageNumber = pagerTag.getStartPageNumber() - pagerTag.getNumberOfPagesToShow();
					}
					else
					{
						startPageNumber = pagerTag.getStartPageNumber();
					}
				    
					pageContext.setAttribute("prevousPageStartPageNumber", startPageNumber);
				    body.invoke(null);
				}
				
			}
		} catch (Exception e) {
			throw new JspException(e);
		}

	}
	
}
