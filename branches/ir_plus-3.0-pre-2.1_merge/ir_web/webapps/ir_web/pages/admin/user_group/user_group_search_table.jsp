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
  
<c:import url="search_all_user_groups_pager.jsp"/>

<div class="dataTable">
    <urstb:table width="100%">
        <urstb:caption>Found User Groups</urstb:caption>
            <urstb:thead>
                <urstb:tr>
                    <urstb:td>Name</urstb:td>
                    <urstb:td>Description</urstb:td>
                </urstb:tr>
            </urstb:thead>
            <urstb:tbody
                var="userGroup" 
                oddRowClass="odd"
                evenRowClass="even"
                currentRowClassVar="rowClass"
                collection="${userGroups}">
                <urstb:tr 
                    cssClass="${rowClass}"
                    onMouseOver="this.className='highlight'"
                    onMouseOut="this.className='${rowClass}'">
                       
                        <urstb:td>
                             <c:url var="editUserGroupUrl" value="/admin/viewUserGroup.action">
			                        <c:param name="id" value="${userGroup.id}"/>
			                 </c:url>
                            <a href="${editUserGroupUrl}">${userGroup.name}</a>
                        </urstb:td>
                        <urstb:td>${userGroup.description}</urstb:td>
                </urstb:tr>
            </urstb:tbody>
        </urstb:table>
</div>

<c:import url="search_all_user_groups_pager.jsp"/>