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

<!-- This JSP file displays the series for the item
-->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>


<c:if test="${reportsCount == 0}">
	<%@ include file="/pages/user/workspace/item/item_series.jsp" %>
</c:if>
 
<c:forEach items="${item.itemReports}" var="report" varStatus="rowCounter">
       	<table id="series_table_${rowCounter.count}">

			<tr>
		      <td>
		          Series Name    
		      </td>
		      <td colspan="2">
		         Report or Paper number
		      </td>
		            
		      
		     </tr> 
			<tr>
		      <td > 
		      	   <select id="itemForm_series" name="seriesIds" />
		      		<c:forEach items="${seriesList}" var="series">
		      			<option value = "${series.id}" 
			      			<c:if test="${report.series.name == series.name}">
			      				selected
			      			</c:if>
			      		><ur:maxText numChars="65" text="${series.name}" /> </option>
		      		</c:forEach>
		      	   </select>
		      </td>
		      <td>
		      	   <input type="text" id="itemForm_report_number" name="reportNumbers" value="${report.reportNumber}" size="40"/>
		      </td>
		      <td>   
		      	   
		      	   
		      	   <input type="button" class="ur_button" id="itemForm_remove" value="Remove Series" onclick="javascript:YAHOO.ur.item.metadata.removeSeries('series_table_${rowCounter.count}');"/>
		      </td>
		    </tr>
		</table>
</c:forEach>  
