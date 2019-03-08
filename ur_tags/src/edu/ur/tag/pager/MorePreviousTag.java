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
 * This tag displays the previous set of pages
 * 
 * @author Sharmila Ranganathan
 *
 */
public class MorePreviousTag extends SimpleTagSupport {
	
	/** Logger */
	private static final Logger log = LogManager.getLogger(MorePreviousTag.class);

	public void doTag() throws JspException {
		log.debug("do tag called");

		PagerTag pagerTag =
			 (PagerTag)findAncestorWithClass(this,
					 PagerTag.class);

	    if(pagerTag == null)
	    {
	    	throw new JspTagException("the <ur:morePrevious> tag must"
	    			+ " be nested within a <ur:PreviousPage> tag");
	    }

		JspFragment body = getJspBody();
		PageContext pageContext = (PageContext) getJspContext();

		try {
			int startPageNumberForPreviousSet = 1;
			
			startPageNumberForPreviousSet = getStartPageNumber(pagerTag);
			
			pageContext.setAttribute("startPageNumberForPreviousSet", startPageNumberForPreviousSet);

			int rowStart = (startPageNumberForPreviousSet * pagerTag.getNumberOfResultsToShow())  - pagerTag.getNumberOfResultsToShow();
			pageContext.setAttribute("rowStart", rowStart);

			if (pagerTag.getStartPageNumber() > 1) {
				if( body != null )
				{
				    body.invoke(null);
				}
			}
		} catch (Exception e) {
			throw new JspException(e);
		}

	}
	
	/*
	 * Determines the starting page number for the display
	 * when user clicks on NEXT
	 * 
	 */
	private int getStartPageNumber(PagerTag pagerTag) {
		
		int startPageNumberForPreviousSet = 1;
		
		// Ex: ... 4 5 6 
		// Determines the page number to start with when user clicks on "...". In this case the page
		// number should start from 1.
		// So that "1 2 3 .. Next" will be displayed
		startPageNumberForPreviousSet = pagerTag.getStartPageNumber() - pagerTag.getNumberOfPagesToShow();
		
		return startPageNumberForPreviousSet;
	}

}
