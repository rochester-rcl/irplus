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


package edu.ur.tag.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag to display the pagination
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class JavascriptPaginationTag extends SimpleTagSupport{
	
	/** total number of hits  */
	private int totalHits;
	
	/** Starting row number to get the result */
	private int rowStart;
	
	/** number of results to show per page */
	private int numberOfResultsToShow = 10;
	
	/** javascript action must have the format */
	private String javaScriptFunctionName;
	
	/**
	 * Tag to display the pages
	 */	
	public void doTag() throws JspException {
	    
		PageContext context = (PageContext) getJspContext();
		
		if( numberOfResultsToShow <= 0 )
		{
			numberOfResultsToShow = 1;
		}
		
		int rowEnd = rowStart + (numberOfResultsToShow - 1);
	    
	    try {
	    	JspWriter out = context.getOut();


	    	out.write("<table align=\"center\">");
	    	
	    	out.write("<tr>");

	    	if (totalHits > 0) {
	    		out.write("<td width=\"20\">");
	    		if ((totalHits <= numberOfResultsToShow) ||(rowStart == 0 )) {
	    			out.write("<<");
	    		} else {
	    			out.write("<a href=\"javascript:" + javaScriptFunctionName +"(" + (rowStart - numberOfResultsToShow) + ");\" style=\"text-decoration: none\" >" + "<<" + "</a>");	    		}
	    		
	    		out.write("</td>");
		    	
	    		for(int i = 0,page = 1 ; i < totalHits; i+=numberOfResultsToShow, page++) {
		    		out.write("<td width=\"20\">");
		    		if (rowStart == i) {
		    			out.write(Integer.toString(page));
		    		} else {
		    			out.write("<a href=\"javascript:" + javaScriptFunctionName +"(" + i + ");\" style=\"text-decoration: none\">" + page + "</a>");
		    		}
			    	out.write("</td>");
		    	}
		    	
		    	out.write("<td width=\"20\">");
		    	if ((totalHits <= numberOfResultsToShow) ||((rowEnd + 1) >= totalHits )) {
		    		out.write(">>");
	    		} else {
	    			out.write("<a href=\"javascript:" + javaScriptFunctionName +"(" + (rowStart + numberOfResultsToShow) + ");\" style=\"text-decoration: none\">" + ">>" + "</a>");
	    		}
	    		
	    		out.write("</td>");
	    	} else{
	    		out.write("<td> &nbsp; </td>");
	    	}

	    	out.write("</tr>");
	    	
	    	
	    	out.write("</table>");
		    	
		    	
	   } catch (Exception e) {
	       throw new JspException(e);
	   }
	       
	}

	
	/**
	 * Get the total number of results
	 * 
	 * @return
	 */
	public Integer getTotalHits() {
		return totalHits;
	}

	/**
	 * Set the total number of results
	 * 
	 * @param totalHits
	 */
	public void setTotalHits(Integer totalHits) {
		this.totalHits = totalHits;
	}
	/**
	 * Get start row to retrieve results
	 * 
	 * @return
	 */
	public int getRowStart() {
		return rowStart;
	}

	/**
	 * Set start row to retrieve results
	 * 
	 * @param rowStart
	 */
	public void setRowStart(int rowStart) {
		this.rowStart = rowStart;
	}

	public int getNumberOfResultsToShow() {
		return numberOfResultsToShow;
	}


	public void setNumberOfResultsToShow(int numberOfResultsToShow) {
		this.numberOfResultsToShow = numberOfResultsToShow;
	}


	public String getJavaScriptFunctionName() {
		return javaScriptFunctionName;
	}


	public void setJavaScriptFunctionName(String javaScriptAction) {
		this.javaScriptFunctionName = javaScriptAction;
	}
}
