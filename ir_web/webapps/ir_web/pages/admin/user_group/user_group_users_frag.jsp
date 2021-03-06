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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="dataTable">
    <urstb:table width="95%">
        <urstb:caption>Current Group Members</urstb:caption>
            <urstb:thead>
                <urstb:tr>
                   <c:if test='${ir:userHasRole("ROLE_ADMIN", "OR") || ir:isAdminOfGroup(user,userGroup)}'>
                    	<urstb:td>Remove</urstb:td>
                    </c:if>
                    <urstb:td>User Name</urstb:td>
                    <urstb:td>First Name</urstb:td>
                    <urstb:td>Last Name</urstb:td>
                </urstb:tr>
            </urstb:thead>
            <urstb:tbody
                var="member" 
                oddRowClass="odd"
                evenRowClass="even"
                currentRowClassVar="rowClass"
                collection="${userGroup.users}">
                
                <urstb:tr 
                    cssClass="${rowClass}"
                    onMouseOver="this.className='highlight'"
                    onMouseOut="this.className='${rowClass}'">
                      	<c:if test='${ir:userHasRole("ROLE_ADMIN", "OR") || ir:isAdminOfGroup(user,userGroup)}'>
	                        <urstb:td><a href="javascript:YAHOO.ur.usergroup.edit.removeMember(${member.id}, ${userGroup.id}, 0, 1, 1);">Remove User</a></urstb:td>
	                    </c:if>
                        <urstb:td>${member.username}</urstb:td>
                        <urstb:td>${member.firstName}</urstb:td>
                        <urstb:td>${member.lastName}</urstb:td>
                </urstb:tr>
            </urstb:tbody>
        </urstb:table>
</div>