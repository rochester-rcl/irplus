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

import org.apache.log4j.Logger;

/**
 * Displays the last page 
 * 
 * @author Sharmila Ranganathan
 *
 */
public class LastPageTag extends SimpleTagSupport {
	
	/** Logger */
	private static final Logger log = Logger.getLogger(LastPageTag.class);
	
	public void doTag() throws JspException {
		log.debug("do tag called");
		PagerTag pagerTag =
			 (PagerTag)findAncestorWithClass(this,
					 PagerTag.class);

	    if(pagerTag == null)
	    {
	    	throw new JspTagException("the <ur:lastPage> tag must"
	    			+ " be nested within a <ur:pager> tag");
	    }
	    
	    
		JspFragment body = getJspBody();
		PageContext pageContext = (PageContext) getJspContext();

		try {
			int currentPageNumber = 1;
		
			if (pagerTag.getTotalHits() != 0) {
				if (pagerTag.getTotalHits() % pagerTag.getNumberOfResultsToShow() == 0) {
					currentPageNumber = pagerTag.getTotalHits() / pagerTag.getNumberOfResultsToShow();
				} else {
					currentPageNumber = (pagerTag.getTotalHits() / pagerTag.getNumberOfResultsToShow() )+ 1;
				}
	
				if (pagerTag.getCurrentPageNumber() != currentPageNumber) {
					
					if( body != null )
					{
							pageContext.setAttribute("currentPageNumber", currentPageNumber);
							
							int startPageNumber = 1;
							if( currentPageNumber % pagerTag.getNumberOfPagesToShow() == 0 )
							{
								startPageNumber = currentPageNumber - pagerTag.getNumberOfPagesToShow() + 1;
							}
							else
							{
								startPageNumber = (currentPageNumber / pagerTag.getNumberOfPagesToShow()) * pagerTag.getNumberOfPagesToShow() + 1;
							}
						    
							pageContext.setAttribute("startPageNumber", startPageNumber);
					    
							int rowstart = (currentPageNumber * pagerTag.getNumberOfResultsToShow()) - pagerTag.getNumberOfResultsToShow();
							pageContext.setAttribute("rowstartForLastPage", rowstart);
							body.invoke(null);
	
					}
					
				}
			}
		} catch (Exception e) {
			throw new JspException(e);
		}

	}
	
}
