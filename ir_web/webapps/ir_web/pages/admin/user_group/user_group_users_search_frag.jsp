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

<c:if test="${totalHits > 0}">
	<h3>Viewing: ${rowStart + 1} - ${rowEnd} of ${totalHits}</h3>
</c:if>
  
<c:import url="user_group_user_search_pager.jsp"/>

<div class="dataTable">
    <urstb:table width="95%">
        <urstb:caption>Found Users</urstb:caption>
            <urstb:thead>
                <urstb:tr>
                    <urstb:td>Action</urstb:td>
                    <urstb:td>User Name</urstb:td>
                    <urstb:td>First Name</urstb:td>
                    <urstb:td>Last Name</urstb:td>
                </urstb:tr>
            </urstb:thead>
            <urstb:tbody
                var="user" 
                oddRowClass="odd"
                evenRowClass="even"
                currentRowClassVar="rowClass"
                collection="${users}">
                
                <urstb:tr 
                    cssClass="${rowClass}"
                    onMouseOver="this.className='highlight'"
                    onMouseOut="this.className='${rowClass}'">
                        <c:if test="${!ir:isInGroup(user,userGroup)}">
                        <urstb:td><a href="javascript:YAHOO.ur.usergroup.edit.addMember(${user.id}, ${userGroup.id}, ${rowStart}, ${startPageNumber}, ${currentPageNumber});">Add User</a></urstb:td>
                        </c:if>
                        <c:if test="${ir:isInGroup(user,userGroup)}">
                        <urstb:td><a href="javascript:YAHOO.ur.usergroup.edit.removeMember(${user.id}, ${userGroup.id}, ${rowStart}, ${startPageNumber}, ${currentPageNumber});">Remove User</a></urstb:td>
                        </c:if>
                        <urstb:td>${user.username}</urstb:td>
                        <urstb:td>${user.firstName}</urstb:td>
                        <urstb:td>${user.lastName}</urstb:td>
                
                </urstb:tr>
            </urstb:tbody>
        </urstb:table>
</div>

<c:import url="user_group_user_search_pager.jsp"/>