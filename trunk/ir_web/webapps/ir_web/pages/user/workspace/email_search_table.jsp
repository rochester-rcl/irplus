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

<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:if test="${totalHits > 0}">
	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
	<c:import url="user_email_search_pager.jsp"/>
</c:if>
  


<table class="simpleTable">
    <tr>
        <th>Last Name</th>
        <th>First Name</th>
        <th>Email</th>
    </tr>
    <c:forEach items="${users}" var="userObject">
        <tr>
             <td>${userObject.lastName}</td>
             <td> ${userObject.firstName}</td>
             <td>
	             <c:forEach items="${userObject.userEmails}" var="emailObject">
	             	 <a href="javascript:YAHOO.ur.invite.search.showInvite('${emailObject.email}')"> ${emailObject.email} </a> &nbsp;&nbsp;&nbsp;
				 </c:forEach>  
			 </td>
        </tr>
    </c:forEach>

</table>


<c:if test="${totalHits > 0}">
	<c:import url="user_email_search_pager.jsp"/>
</c:if>

