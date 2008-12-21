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

<c:if test="${researcherDepartmentsCount == 0}">
    <c:import url="researcher_department.jsp"/>
</c:if>

	<c:forEach items="${researcher.departments}" var="researcherDepartment" varStatus="rowCounter">
    
   	<table id="department_table_${rowCounter.count}">

		<tr>
	      <td > 
	      	   <select id="researcherForm_department" name="departmentIds" />
	      		<c:forEach items="${departments}" var="department">
	      			<option value = "${department.id}" 
		      			<c:if test="${department.name == researcherDepartment.name}">
		      				selected
		      			</c:if>
		      		><ur:maxText numChars="40" showEllipsis="true" text="${department.name}"/></option>
	      		</c:forEach>
	      	   </select>
	      </td>
	      <td>   
	      	   <input type="button" class="ur_button" id="researcherForm_remove" value="Remove Department" onclick="javascript:YAHOO.ur.edit.researcher.removeDepartment('department_table_${rowCounter.count}');"/>
	      </td>
	    </tr>
	</table>
	</c:forEach> 

