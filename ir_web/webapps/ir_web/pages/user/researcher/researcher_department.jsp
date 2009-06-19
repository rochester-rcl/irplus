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

<!--This JSP file helps to add another row of department 
 -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<ur:styleSheet href="page-resources/css/global.css"/>

<table id="department_table_i">

	<tr>
      <td > 
      	   <select id="researcherForm_department" name="departmentIds" />
      	   <option value = "0"> Select </option>
      		<c:forEach items="${departments}" var="department">
      			<option value = "${department.id}"> <ur:maxText numChars="40" showEllipsis="true" text="${department.name}"/></option>
      		</c:forEach>
      	   </select>
      </td>
      <td>   
      	   <input type="button" class="ur_button" id="researcherForm_remove" value="Remove" onclick="javascript:YAHOO.ur.edit.researcher.removeDepartment('department_table_i');"/>
      </td>
      </tr>
</table>