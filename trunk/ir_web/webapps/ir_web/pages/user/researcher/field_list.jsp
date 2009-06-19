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

<!-- This JSP file helps to display the list of fields
-->

 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<c:if test="${researcherFieldsCount == 0}">
    <c:import url="researcher_field.jsp"/>
</c:if>

	<c:forEach items="${researcher.fields}" var="researcherField" varStatus="rowCounter">
    
   	<table id="field_table_${rowCounter.count}">

		<tr>
	      <td > 
	      	   <select id="researcherForm_field" name="fieldIds" />
	      		<c:forEach items="${fields}" var="field">
	      			<option value = "${field.id}" 
		      			<c:if test="${field.name == researcherField.name}">
		      				selected
		      			</c:if>
		      		> <ur:maxText numChars="40" showEllipsis="true" text="${field.name}"/></option>
	      		</c:forEach>
	      	   </select>
	      </td>
	      <td>   
	      	   <input type="button" class="ur_button" id="researcherForm_remove" value="Remove Field" onclick="javascript:YAHOO.ur.edit.researcher.removeField('field_table_${rowCounter.count}');"/>
	      </td>
	    </tr>
	</table>
	</c:forEach> 

