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


package edu.ur.ir.web.table;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import edu.ur.dao.CriteriaHelper;



/**
 * Helper class for dealing with table information.
 * 
 * @author Nathan Sarr
 *
 */
public class TableUtil {
	
	/**  Logger for table util */
	private static final Logger log = Logger.getLogger(TableUtil.class);
	
	/**
	 * Create a list of the sort values.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static void getSortValues(HttpServletRequest request, Hashtable<String,CriteriaHelper> criterias)
	{
		Pattern sortPattern = Pattern.compile("_sort_");
		Matcher matcher; 
		
        Enumeration parameterNames = request.getParameterNames();
		
		while(parameterNames.hasMoreElements())
		{
			String name = parameterNames.nextElement().toString();
			matcher = sortPattern.matcher(name);
			
			//first find the sort value
			if( matcher.find() )
			{
				int index = matcher.end();
				
				//get the property value
				String property = name.substring(index);
				
				String paramValue = request.getParameter(name);
				if(!isEmpty(paramValue))
				{
					//get the sort type
					int charIndex = paramValue.indexOf('_');
					String sortType = paramValue.substring(0, charIndex);
					
					// get the remainder of the string
					String remainder = paramValue.substring(charIndex + 1);
					charIndex = remainder.indexOf('_');
					
					// get the order
					int order = new Integer(remainder.substring(0,charIndex));
					
					// get the ignore case value
					String boolValue = remainder.substring(charIndex + 1);
					boolean ignoreCase = new Boolean(boolValue).booleanValue();
					
					CriteriaHelper helper = criterias.get(property);
					if(helper == null)
					{
						helper = new CriteriaHelper(property);
						criterias.put(property, helper);
					}
					helper.setSort(true);
					helper.setSortType(sortType);
					helper.setOrder(order);
					helper.setIgnoreCaseOnSort(ignoreCase);
					
					
					log.debug("Updateing criteira " + helper);
				}
				
			}
			
		}
	}
	
	/**
	 * Create a list of the filter values.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static void getFilterValues(HttpServletRequest request, 
			PropertyConverter converter, Hashtable<String,CriteriaHelper> criterias)
	{
		Pattern filterPattern = Pattern.compile("_filter_");
		Matcher matcher; 
		
        Enumeration parameterNames = request.getParameterNames();
		
		while(parameterNames.hasMoreElements())
		{
			String name = parameterNames.nextElement().toString();
			matcher = filterPattern.matcher(name);
			
			// the first find will be the _filter_ value
			if( matcher.find() )
			{
				int index = matcher.end();
				
				//get the property value
				String property = name.substring(index);
				
				if(!isEmpty(request.getParameter(name)))
				{
					try
					{
						Object newValue = converter.convertValue(property, request.getParameter(name));
						CriteriaHelper helper = criterias.get(property);
						
						if(helper == null)
						{
							helper = new CriteriaHelper(property);
							criterias.put(property, helper);
						}
						helper.setFilter(true);
						helper.setValue(newValue);
				       
				        log.debug("Adding criteria " + helper);
					}
					catch(Exception e)
					{
						log.debug("error convertering value", e);
					}
				}
				
			}
			
		}
		
	}
	
	/**
	 * Places the filter request values back on the request as attributes.
	 * s
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public static void putFilterValuesInRequest(HttpServletRequest request)
	{
		Pattern filterPattern = Pattern.compile("_filter_");
		Matcher matcher; 
        Enumeration parameterNames = request.getParameterNames();
		
		while(parameterNames.hasMoreElements())
		{
			String name = parameterNames.nextElement().toString();
			matcher = filterPattern.matcher(name);
			
			// the first find will be _filter_
			if( matcher.find() )
			{
				if(!isEmpty(request.getParameter(name)))
				{
					log.debug("Adding filter name: " + name + " value: " + 
							request.getParameter(name) + " to request");
					request.setAttribute(name, request.getParameter(name));
				}
			}
		}
	}
	
	/**
	 * Put the list of values in the request
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static void putSortValuesInRequest(HttpServletRequest request)
	{
		Pattern sortPattern = Pattern.compile("_sort_");
		Matcher matcher; 
		
        Enumeration parameterNames = request.getParameterNames();
		
		while(parameterNames.hasMoreElements())
		{
			String name = parameterNames.nextElement().toString();
			matcher = sortPattern.matcher(name);
			
			// the first find will be the sort value
			if( matcher.find() )
			{
				if(!isEmpty(request.getParameter(name)))
				{
					log.debug("Adding sort name: " + name + " value: " + 
							request.getParameter(name) + " to request");
					request.setAttribute(name, request.getParameter(name));
				}
			}
			
		}
	}

	
	/**
	 * Determine if the parameter value is empty or a null string.
	 * 
	 * @param value to check
	 * @return true if the value is empty or a null string
	 */
	public static boolean isEmpty(String value)
	{
		if( value == null || value.trim().equals(""))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Calculate the row start 
	 * 
	 * @param currentPage - current page th user is on
	 * @param totalNumberOfResults - total number of results
	 * @param maxRows - max rows allowed per page.
	 * @return
	 */
	public static int getRowStart(int currentPage, int totalNumberOfResults, int maxResultsPerPage)
	{
		if( currentPage > 1)
	    {
	    	return ((currentPage - 1) * maxResultsPerPage);
	    }
	    else
	    {
	    	return 0;
	    }
	}
	
	/**
	 * Determine the row end of the current page.
	 * 
	 * @param currentPage - page the user is on
	 * @param resultsPerPage - 
	 * @param totalNumberOfResults
	 * @return
	 */
	public static int getRowEnd(int currentPage, int maxResultsPerPage, int totalNumberOfResults)
	{
		if( currentPage * maxResultsPerPage > totalNumberOfResults)
		{
			return totalNumberOfResults;
		}
		else
		{
		    return currentPage * maxResultsPerPage;
		}
		
	}
	
	/**
	 * Determine the total number of pages.
	 * 
	 * @param totalNumberOfResults
	 * @param maxResultsPerPage
	 * @return
	 */
	public static int getTotalNumberOfPages(int totalNumberOfResults, int maxResultsPerPage)
	{
		double value = (double)totalNumberOfResults / (double)maxResultsPerPage;
		value = Math.ceil(value);
		return new Double(value).intValue();
	}

}
