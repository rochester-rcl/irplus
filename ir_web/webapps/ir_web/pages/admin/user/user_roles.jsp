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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ir" uri="ir-tags"%>

<table>
    <tr>
        <td>
        
				Select Roles
		</td>
	</tr>
<c:forEach items="${roles}" var="role">
    <tr>
        <td>
            <c:if test='${ir:userHasRole(role.name, "OR")}'>
             <input checked="true" type="checkbox" name="selectedRoles" value="${role.id}"/> ${role.description}
            </c:if>
            <c:if test='${!ir:userNoRole(role, "")}'>
            <input type="checkbox" name="selectedRoles" value="${role.id}"/> ${role.description}
            </c:if>
        </td>
    </tr>
</c:forEach>
</table>