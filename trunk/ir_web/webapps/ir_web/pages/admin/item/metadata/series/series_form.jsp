
<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

<!--  
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
-->

<!--  form fragment for dealing with editing series
      this form will return with error messages in it if there
      is an issue.
 -->
<%@ taglib prefix="ir" uri="ir-tags"%>

		<!--  represents a successful submission -->
		<input type="hidden" id="newSeriesForm_success" 
		       value="${added}"/>
		       
		<!--  if editing an id must be passed -->     
	    <input type="hidden" id="newSeriesForm_id"
		        name="id" value="${series.id}"/>
		               
	    <input type="hidden" id="newSeriesForm_new"
		        name="newSeries" value="true"/>
		           
		<div id="seriesError">		              
		    <!--  get the error messages from fieldErrors -->
			<p class="errorMessage"><ir:printError errors="${fieldErrors}" 
			key="series.name"/></p>
			<p class="errorMessage"><ir:printError errors="${fieldErrors}" 
			key="seriesAlreadyExists"/></p>  
		</div>
		
		
		<table class="formTable">    
		    <tr>       
	            <td align="left" class="label">Name:*</td>
	            <td align="left" class="input"><input type="text" 
			    id="newSeriesForm_name" 
			    name="series.name" 
			    value="${series.name}" size="45"/> </td>
			</tr>
			<tr>
			    <td align="left" class="label">Number:*</td>
			    <td align="left" colspan="2" class="input"> <input type="text" 
                id="newSeriesForm_number" 
			    name="series.number" 
			    value="${series.number}" size="45"/>
	            </td>
			</tr>
			<tr>
			    <td align="left" class="label">Description:</td>
			    <td align="left" colspan="2" class="input"> <textarea name="series.description" 
	    		id="newSeriesForm_description" cols="42" rows="4">${series.description}</textarea>
	            </td>
			</tr>
			
	    </table>
